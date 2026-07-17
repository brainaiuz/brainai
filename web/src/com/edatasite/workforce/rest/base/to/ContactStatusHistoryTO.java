package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: abror
 * Date: 7/10/15 3:31 PM
 */
public class ContactStatusHistoryTO implements IsSerializable {
    Integer id;
    SelectItemTO status;
    Long createdDate;
    Long modifiedDate;
    SelectItemTO modifiedBy;

    public ContactStatusHistoryTO() {
    }

    public ContactStatusHistoryTO(ContactListItem item) {
        this.status = new SelectItemTO(item.getLeadStatus(true).getId(), item.getLeadStatus(true).getName());
        this.createdDate = WrapUtils.dateToLong(item.getCreatedDate());
        this.modifiedDate = WrapUtils.dateToLong(item.getUpdatedDate());
        this.modifiedBy = new SelectItemTO(item.getOwnerId(), item.getOwner());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SelectItemTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(SelectItemTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
