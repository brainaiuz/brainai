package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class EventDetailsInfoTO extends ResponseData {
    private EventBaseInfoTO base_info;
    private EventAdditionalInfoTO additional_info;
    private String share_link;
    private ArrayList<CustomFieldsTO> custom_fields;
    private boolean can_edit;

    public EventBaseInfoTO getBase_info() {
        return base_info;
    }

    public void setBase_info(EventBaseInfoTO base_info) {
        this.base_info = base_info;
    }

    public EventAdditionalInfoTO getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(EventAdditionalInfoTO additional_info) {
        this.additional_info = additional_info;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public boolean getCan_edit() {
        return can_edit;
    }

    public void setCan_edit(boolean can_edit) {
        this.can_edit = can_edit;
    }
}
