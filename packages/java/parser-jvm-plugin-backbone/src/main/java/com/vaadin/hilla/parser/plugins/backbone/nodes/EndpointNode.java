package com.vaadin.hilla.parser.plugins.backbone.nodes;

import org.jspecify.annotations.NonNull;

import com.vaadin.hilla.parser.core.AbstractNode;
import com.vaadin.hilla.parser.models.ClassInfoModel;
import io.swagger.v3.oas.models.tags.Tag;

public class EndpointNode extends AbstractNode<ClassInfoModel, Tag> {
    protected EndpointNode(@NonNull ClassInfoModel value, @NonNull Tag schema) {
        super(value, schema);
    }

    @NonNull
    public static EndpointNode of(@NonNull ClassInfoModel cls) {
        return new EndpointNode(cls, new Tag());
    }
}
