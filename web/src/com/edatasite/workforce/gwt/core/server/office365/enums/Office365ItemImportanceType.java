package com.edatasite.workforce.gwt.core.server.office365.enums;

/**
 * Created by umakarimov on 9/30/15.
 */
public enum Office365ItemImportanceType {
    Low(0),
    Normal(1),
    High(2);

    private int id;

    /**
     * @param id
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesEvent
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesMessage
     */
    Office365ItemImportanceType(int id) {
        this.id = id;
    }
}
