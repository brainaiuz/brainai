package com.edatasite.workforce.rest.v2.release10.core.to.base;

import com.edatasite.workforce.rest.v2.release10.core.to.hrms.NotificationsListTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 01/22/2018.
 */
public class NotificationResultTO extends ResponseData {
    private Integer total_count;
    private Integer offset;
    private Integer count;
    private Integer left;
    private ArrayList<NotificationsListTO> notifications;

    public NotificationResultTO() {
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public ArrayList<NotificationsListTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<NotificationsListTO> notifications) {
        this.notifications = notifications;
    }
}
