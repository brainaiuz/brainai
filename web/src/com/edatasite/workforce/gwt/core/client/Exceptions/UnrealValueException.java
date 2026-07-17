package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;


public class UnrealValueException extends WfmBaseException {

    private String msg = "Currently you do not have enough number of this product in stock.";

    private String linkToEdit;

    private static final long serialVersionUID = 1L;

    public UnrealValueException() {
    }

    public UnrealValueException(String msg) {
        this(msg, null);
    }

    public UnrealValueException(String msg, String linkToEdit) {
        this.msg = msg;
        this.linkToEdit = linkToEdit;
    }

    public String getMessage() {
        return msg;
    }

    public void setLinkToEdit(String linkToEdit) {
        this.linkToEdit = linkToEdit;
    }

    public String getLinkToEdit() {
        return linkToEdit;
    }

}
