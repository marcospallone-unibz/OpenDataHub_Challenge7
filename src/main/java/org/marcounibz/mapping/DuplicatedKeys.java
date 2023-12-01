package org.marcounibz.mapping;

public class DuplicatedKeys {
    String pathFromItem;

    public String[] getKeyPath() {
        return pathFromItem.split(">");
    }
}
