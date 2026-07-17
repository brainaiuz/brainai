package com.edatasite.workforce.gwt.core.server.office365.enums;

/**
 * Created by umakarimov on 9/30/15.
 */
public enum Office365ItemBodyType {
    Text(0),
    HTML(1);

    int id;

    /**
     * @param id
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#ItemBody
     */
    Office365ItemBodyType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
