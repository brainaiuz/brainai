package com.edatasite.workforce.gwt.hrms.client.ui.cell;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Sher(sherali.pirnafaosov@gmail.com)
 * Date: 9/24/12
 * Time: 4:59 PM
 * Finnet Technologies
 */
public class EditDataItem implements IsSerializable {

    private Integer objectId;
    private Double value;
    private String description;
    private boolean editable = true;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
