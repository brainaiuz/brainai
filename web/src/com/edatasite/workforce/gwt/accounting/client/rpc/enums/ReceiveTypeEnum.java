package com.edatasite.workforce.gwt.accounting.client.rpc.enums;

/**
 * Created by Normurod on 2/10/16.
 */
public enum ReceiveTypeEnum {
    RECEIVE_BY_QTY(1, "By quantity"),
    RECEIVE_BY_VALUE(2, "By amount");

    private int id;
    private String title;

    ReceiveTypeEnum(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static ReceiveTypeEnum buildWithId(int id) {
        switch (id) {
            case 1:
                return RECEIVE_BY_QTY;
            case 2:
                return RECEIVE_BY_VALUE;
            default:
                return RECEIVE_BY_QTY;
        }
    }
}
