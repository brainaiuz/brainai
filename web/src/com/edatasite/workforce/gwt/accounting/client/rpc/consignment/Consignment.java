package com.edatasite.workforce.gwt.accounting.client.rpc.consignment;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by Normurod on 6/15/15.
 */
public class Consignment implements IsSerializable, Serializable {

    private Integer objectID;
    private String name;
    private DateNonConvertable date;
    private NumberData numberData;
    private String number;
    private String subsidiaryUniqNum;
    private String reference;
    private ConsignmentItem[] items;
    private SelectItem[] subsidiaries;

    private String layoutHtml;

    private boolean deleted;
    private boolean subsidiaryConsignment;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ConsignmentItem[] getItems() {
        return items;
    }

    public void setItems(ConsignmentItem[] items) {
        this.items = items;
    }

    public String getSubsidiaryUniqNum() {
        return subsidiaryUniqNum;
    }

    public void setSubsidiaryUniqNum(String subsidiaryUniqNum) {
        this.subsidiaryUniqNum = subsidiaryUniqNum;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public SelectItem[] getSubsidiaries() {
        return subsidiaries;
    }

    public void setSubsidiaries(SelectItem[] subsidiaries) {
        this.subsidiaries = subsidiaries;
    }

    public boolean isSubsidiaryConsignment() {
        return subsidiaryConsignment;
    }

    public void setSubsidiaryConsignment(boolean subsidiaryConsignment) {
        this.subsidiaryConsignment = subsidiaryConsignment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
