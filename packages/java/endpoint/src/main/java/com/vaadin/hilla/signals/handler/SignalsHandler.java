package com.vaadin.hilla.signals.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.EndpointInvocationException;
import com.vaadin.hilla.signals.core.event.ListStateEvent;
import com.vaadin.hilla.signals.core.registry.SecureSignalsRegistry;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Flux;

/**
 * Handler Endpoint for Fullstack Signals' subscription and update events.
 */
@AnonymousAllowed
@BrowserCallable
public class SignalsHandler {

    private final SecureSignalsRegistry registry;

    public SignalsHandler(SecureSignalsRegistry registry) {
        this.registry = registry;
    }

    /**
     * Subscribes to a signal.
     *
     * @param providerEndpoint
     *            the endpoint that provides the signal
     * @param providerMethod
     *            the endpoint method that provides the signal
     * @param clientSignalId
     *            the client signal id
     *
     * @return a Flux of JSON events
     */
    public Flux<ObjectNode> subscribe(String providerEndpoint,
            String providerMethod, String clientSignalId, ObjectNode body,
            @Nullable String parentClientSignalId) {
        try {
            if (parentClientSignalId != null) {
                return subscribe(parentClientSignalId, clientSignalId);
            }
            var signal = registry.get(clientSignalId);
            if (signal != null) {
                return signal.subscribe().doFinally(
                        (event) -> registry.unsubscribe(clientSignalId));
            }
            registry.register(clientSignalId, providerEndpoint, providerMethod,
                    body);
            return registry.get(clientSignalId).subscribe()
                    .doFinally((event) -> registry.unsubscribe(clientSignalId));
        } catch (Exception e) {
            return Flux.error(e);
        }
    }

    private Flux<ObjectNode> subscribe(String parentClientSignalId,
            String clientSignalId)
            throws EndpointInvocationException.EndpointAccessDeniedException,
            EndpointInvocationException.EndpointNotFoundException {
        var parentSignal = registry.get(parentClientSignalId);
        if (parentSignal == null) {
            throw new IllegalStateException(String.format(
                    "Parent Signal not found for parent client signal id: %s",
                    parentClientSignalId));
        }
        return parentSignal.subscribe(clientSignalId)
                .doFinally((event) -> registry.unsubscribe(clientSignalId));
    }

    /**
     * Updates a signal with an event.
     *
     * @param clientSignalId
     *            the clientSignalId associated with the signal to update
     * @param event
     *            the event to update with
     */
    public void update(String clientSignalId, ObjectNode event)
            throws EndpointInvocationException.EndpointAccessDeniedException,
            EndpointInvocationException.EndpointNotFoundException {
        var parentSignalId = ListStateEvent.extractParentSignalId(event);
        if (parentSignalId != null) {
            if (registry.get(parentSignalId) == null) {
                throw new IllegalStateException(String.format(
                        "Parent Signal not found for signal id: %s",
                        parentSignalId));
            }
            registry.get(parentSignalId).submit(event);
        } else {
            if (registry.get(clientSignalId) == null) {
                throw new IllegalStateException(
                        String.format("Signal not found for client signal: %s",
                                clientSignalId));
            }
            registry.get(clientSignalId).submit(event);
        }
    }
}
