package com.edatasite.workforce.gwt.core.client.reference;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 05-Jul-2010
 * Time: 22:25:04
 */
public enum CRMContactRelatedReference {
    PHONES(1),
    EMAILS(2),
    ADDRESSES(3),
    WEBSITES(4),
    IMADDRESSES(5),
    DATES(6),
    CONECTIONS(7);


    CRMContactRelatedReference(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }
}
