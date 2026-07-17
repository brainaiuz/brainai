package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;


import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;


/**
 * Created by Anvar Akramov on 26/3/2018.
 */
public class WarehouseListItemTO extends IdNameTO {

    private String contactname;
    private String phone;
    private String email;
    private String address;
    private String notes;


    public WarehouseListItemTO() {
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
