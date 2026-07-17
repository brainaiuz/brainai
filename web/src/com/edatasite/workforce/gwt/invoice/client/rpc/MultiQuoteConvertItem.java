package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 8/13/13
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiQuoteConvertItem implements Serializable {

    private ArrayList<Integer> quoteIds;
    private Integer selectedProjectQuoteId;
    private String objectType;
    private boolean groupByItem;
    private ArrayList<QIGroupingField> groupingFields;


    public ArrayList<Integer> getQuoteIds() {
        if (quoteIds == null) {
            quoteIds = new ArrayList<>();
        }
        return quoteIds;
    }

    public void setQuoteIds(ArrayList<Integer> quoteIds) {
        this.quoteIds = quoteIds;
    }

    public Integer getSelectedProjectQuoteId() {
        return selectedProjectQuoteId;
    }

    public void setSelectedProjectQuoteId(Integer selectedProjectQuoteId) {
        this.selectedProjectQuoteId = selectedProjectQuoteId;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public boolean isGroupByItem() {
        return groupByItem;
    }

    public void setGroupByItem(boolean groupByItem) {
        this.groupByItem = groupByItem;
    }

    public ArrayList<QIGroupingField> getGroupingFields() {
        return groupingFields;
    }

    public void setGroupingFields(ArrayList<QIGroupingField> groupingFields) {
        this.groupingFields = groupingFields;
    }
}
