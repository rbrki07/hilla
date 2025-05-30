/*
 * Copyright 2000-2022 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.hilla.parser.plugins.nonnull.kotlin.superclasses.javaendpointexposed;

import com.vaadin.hilla.parser.plugins.nonnull.kotlin.annotation.EndpointExposed;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@EndpointExposed
public interface PagedData<T> {
    default List<@Nonnull T> getNonNullablePage(int pageSize, int pageNumber,
            Map<String, @Nonnull T> parameters) {
        return Collections.emptyList();
    }

    default List<T> getPage(int pageSize, int pageNumber) {
        return Collections.emptyList();
    }

    default int size() {
        return 0;
    }
}
