package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

public class ItemDetailTO extends ResponseData {
    private ItemBaseInfoTO base_info;
    private FlowSettingsTO status;
    private ItemContactTO contacts;

    public ItemBaseInfoTO getBase_info() {
        return base_info;
    }

    public void setBase_info(ItemBaseInfoTO base_info) {
        this.base_info = base_info;
    }

    public FlowSettingsTO getStatus() {
        return status;
    }

    public void setStatus(FlowSettingsTO status) {
        this.status = status;
    }

    public ItemContactTO getContacts() {
        return contacts;
    }

    public void setContacts(ItemContactTO contacts) {
        this.contacts = contacts;
    }
}
