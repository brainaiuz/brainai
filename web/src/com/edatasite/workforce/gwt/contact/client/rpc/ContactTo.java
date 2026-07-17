package com.edatasite.workforce.gwt.contact.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContactTo implements IsSerializable {
    private String name;
    private String first_name;//added for shopifier
    private String last_name;//added for shopifier
    private Integer item_id;
    private String phone;
    private String avatar_image;
    private String contact_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public String getContactType() {
        return contact_type;
    }

    public void setContactType(String contactType) {
        this.contact_type = contactType;
    }
}
