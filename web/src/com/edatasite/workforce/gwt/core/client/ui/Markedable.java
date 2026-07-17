package com.edatasite.workforce.gwt.core.client.ui;

public interface Markedable {
    /**
     * Marker interface indicating that transfer objects can be
     * marked - as new, for example
     */
    Boolean isMarked();

    void setMarked(Boolean marked);


}
