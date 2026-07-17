package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 01.06.2018 15:09
 */
public class DashboardComboItem implements IsSerializable {

    private DashboardWeatherItem weatherItem;
    private ArrayList<DashboardNewsItem> newsItems;
    private ArrayList<DashboardBirthdayItem> birthdayItems;
    private EmailAccountItem messageItem;

    public DashboardWeatherItem getWeatherItem() {
        return weatherItem;
    }

    public void setWeatherItem(DashboardWeatherItem weatherItem) {
        this.weatherItem = weatherItem;
    }

    public ArrayList<DashboardNewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(ArrayList<DashboardNewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    public ArrayList<DashboardBirthdayItem> getBirthdayItems() {
        return birthdayItems;
    }

    public void setBirthdayItems(ArrayList<DashboardBirthdayItem> birthdayItems) {
        this.birthdayItems = birthdayItems;
    }

    public EmailAccountItem getMessageItem() {
        return messageItem;
    }

    public void setMessageItem(EmailAccountItem messageItem) {
        this.messageItem = messageItem;
    }
}
