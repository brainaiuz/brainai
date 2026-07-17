package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 10/01/2018.
 */
public class NotificationsListResultTO extends ResponseData {
    private ArrayList<NotificationsListTO> notifications;

    public NotificationsListResultTO() {
    }

    public NotificationsListResultTO(ArrayList<NotificationsListTO> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<NotificationsListTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<NotificationsListTO> notifications) {
        this.notifications = notifications;
    }
}
