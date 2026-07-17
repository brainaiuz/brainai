package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov on 31.11.2016.
 */
public class NotificationTO implements IsSerializable {

    private Integer id;
    private String name;
    private Long date;
    private boolean read;
    private UserTO user;

    public NotificationTO() {
    }

    public NotificationTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public NotificationTO(NotificationItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.date = WrapUtils.dateToLong(item.getDate());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
