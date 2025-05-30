package com.vaadin.hilla.test.reactgrid;

import org.junit.Test;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class ReadOnlyGridSinglePropertyFilterIT extends AbstractGridTest {

    @Test
    public void findsMatches() {
        setFilter("Ali");
        assertRowCount(1);
        assertName(0, "Alice", "Johnson");

        setFilter("");
        assertRowCount(50);
        assertName(0, "Abigail", "Carter");

        setFilter("xan");
        assertRowCount(2);
        assertName(0, "Xander", "Hill");
        assertName(1, "Xander", "Zane");

    }

    private void setFilter(String filter) {
        $(TextFieldElement.class).id("filter").setValue(filter);
    }

}
