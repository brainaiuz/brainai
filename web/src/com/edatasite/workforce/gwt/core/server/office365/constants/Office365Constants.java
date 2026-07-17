package com.edatasite.workforce.gwt.core.server.office365.constants;

/**
 * Created by umakarimov on 9/30/15.
 */
public interface Office365Constants {
    String OFFICE365_ROOT = "/office365";
    String AUTH_PAGE = "/auth";
    String AUTH_LINK_PAGE = "/link";
    String AUTH_VERIFY_PAGE = "/verify";
    String AUTH_EMAIL_VERIFY_PAGE = "/email/verify";

    String AUTH_PAGE_URL = OFFICE365_ROOT + AUTH_PAGE;
    String AUTH_VERIFY_PAGE_URL = AUTH_PAGE_URL + AUTH_VERIFY_PAGE;
    String AUTH_EMAIL_VERIFY_PAGE_URL = AUTH_PAGE_URL + AUTH_VERIFY_PAGE;

    String STATE_COOKIE = "office-365-state";
    String WEBSITE_URL_COOKIE = "website_url";

    // 98aa6926-b915-48a4-a190-e258ff48ff75 - kpi-dev
    String CLIENT_ID = "37dc7665-0982-4387-9790-eda3f638992c"; //Created by Anvar Akramov ("KPI Office 365 Integrator" - https://apps.dev.microsoft.com/#/appList)
    //    String CLIENT_SECRET = "iSG6j7NyuOsfOsVK1rAbU6koR614/mz/7yKA7faj8Ck=";
//    String CLIENT_SECRET = "Ue/QoTMvi6K/YDx+rXtBsNMxRbwdxfXCd00Yiv9SB5I=";
    String CLIENT_SECRET = "qzJBDIL611*!mkygfRK12[$";// KPI key

    //SharePoint App IDS for localhost
    String SHARE_POINT_LOCAL_CLIENT_ID = "4b3b0a33-c009-4619-990d-b847d7cebdd7";
    String SHARE_POINT_LOCAL_CLIENT_SECRET = "UhUBgKbnpU6SoVobxdSGRTMQmyEQRQuAtGQDI/t98wY=";

    //SharePoint App IDS for aws.goodsystems.com.au
    String SHARE_POINT_AWS_CLIENT_ID = "9542721c-335d-452c-b4ca-4b4e5637acb9";
    String SHARE_POINT_AWS_CLIENT_SECRET = "xZpyS/op42miDoUaYwOSHZ9iBvouF4x5iGLkmfMOUgs=";

    //SharePoint App IDS for app.goodsystems.com.au
    String SHARE_POINT_APP_CLIENT_ID = "7e0c55dc-6902-451e-8911-8ab7dc61b63b";
    String SHARE_POINT_APP_CLIENT_SECRET = "ly7CxcDLQqcBTXqNf/j4wao+pYV83TOSzugBmpcD7vs=";

    String HASH_SECRET = "^0ffice#^%$5(r3t*";

    String OAUTH_TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";

    String GRAPH_API = "https://graph.microsoft.com";
    String GRAPH_URL = GRAPH_API + "/v1.0";

    String GRAPH_ME_URL = GRAPH_URL + "/me";

    String OFFICE_ONE_DRIVE = "OFFICE_365";

    String DRIVE = GRAPH_ME_URL + "/drive";
    String DRIVES = GRAPH_ME_URL + "/drives";

    String DRIVE_ROOT = DRIVE + "/root";
    String DRIVE_ROOT_CHILDREN = DRIVE_ROOT + "/children";
    String DRIVE_ROOT_SEARCH = DRIVE_ROOT + "/microsoft.graph.search?q={search=%s}";

    String DRIVE_ITEM = DRIVE + "/items/%s";
    String DRIVE_ITEM_CONTENT = DRIVE_ITEM + "/content";
    String DRIVE_ITEM_CHILDREN = DRIVE_ITEM + "/children";
    String DRIVE_ITEM_CHILDREN_CONTENT = DRIVE_ITEM_CHILDREN + "/%s/content";

    String OFFICE_SHARE_POINT = "OFFICE_365_SHARE_POINT";

    String SHAREPOINT = GRAPH_URL + "/drive";

    String SHAREPOINT_ROOT = SHAREPOINT + "/root";
    String SHAREPOINT_ROOT_CHILDREN = SHAREPOINT_ROOT + "/children";
    String SHAREPOINT_ROOT_SEARCH = SHAREPOINT_ROOT + "/microsoft.graph.search?q={search=%s}";

    String SHAREPOINT_ITEM = SHAREPOINT + "/items/%s";
    String SHAREPOINT_ITEM_CONTENT = SHAREPOINT_ITEM + "/content";
    String SHAREPOINT_ITEM_CHILDREN = SHAREPOINT_ITEM + "/children";
    String SHAREPOINT_ITEM_CHILDREN_CONTENT = SHAREPOINT_ITEM_CHILDREN + "/%s/content";

    String CALENDAR_LIST = GRAPH_ME_URL + "/calendars";
    String CALENDAR_ITEM = CALENDAR_LIST + "/%s";
    String CALENDAR_ITEM_EVENTS = CALENDAR_ITEM + "/events";

    String CALENDAR_DEFAULT_ITEM = GRAPH_ME_URL + "/calendar";
    String CALENDAR_DEFAULT_ITEM_EVENTS = CALENDAR_DEFAULT_ITEM + "/events";

    String EVENT_LIST = GRAPH_ME_URL + "/events";
    String EVENT_ITEM = EVENT_LIST + "/%s";
    String EVENT_ITEM_ACCEPT = EVENT_ITEM + "/accept";
    String EVENT_ITEM_TENTATIVE_ACCEPT = EVENT_ITEM + "/microsoft.graph.tentativelyAccept";
    String EVENT_ITEM_DECLINE = EVENT_ITEM + "/microsoft.graph.decline";
    String EVENT_ITEM_DISMISS_REMINDER = EVENT_ITEM + "/microsoft.graph.dismissReminder";
    String EVENT_ITEM_SNOOZE_REMINDER = EVENT_ITEM + "/microsoft.graph.snoozeReminder";

    //    String OUTLOOK_URL = "https://outlook.office.com/api/v1.0";
    String OUTLOOK_URL = "https://graph.microsoft.com/v1.0";

    String OUTLOOK_MESSAGE_LIST_URL = OUTLOOK_URL + "/me/messages";
    String OUTLOOK_MESSAGE_ITEM_URL = OUTLOOK_MESSAGE_LIST_URL + "/%s";

    String OUTLOOK_MESSAGE_SEND_ON_THE_FLY_URL = OUTLOOK_URL + "/me/sendmail";

    String OUTLOOK_MESSAGE_ITEM_SEND_URL = OUTLOOK_MESSAGE_ITEM_URL + "/send";
    String OUTLOOK_MESSAGE_ITEM_MOVE_URL = OUTLOOK_MESSAGE_ITEM_URL + "/move";
    String OUTLOOK_MESSAGE_ITEM_COPY_URL = OUTLOOK_MESSAGE_ITEM_URL + "/copy";
    String OUTLOOK_MESSAGE_ITEM_REPLY_URL = OUTLOOK_MESSAGE_ITEM_URL + "/reply";
    String OUTLOOK_MESSAGE_ITEM_REPLY_ALL_URL = OUTLOOK_MESSAGE_ITEM_URL + "/replyall";
    String OUTLOOK_MESSAGE_ITEM_CREATE_REPLY_ALL_URL = OUTLOOK_MESSAGE_ITEM_URL + "/createreplyall";

    String OUTLOOK_MESSAGE_ITEM_FORWARD_URL = OUTLOOK_MESSAGE_ITEM_URL + "/forward";
    String OUTLOOK_MESSAGE_ITEM_CREATE_FORWARD_URL = OUTLOOK_MESSAGE_ITEM_URL + "/createforward";

    String OUTLOOK_MESSAGE_ITEM_ATTACHMENT_LIST_URL = OUTLOOK_MESSAGE_ITEM_URL + "/attachments";
    String OUTLOOK_MESSAGE_ITEM_ATTACHMENT_ITEM_URL = OUTLOOK_MESSAGE_ITEM_URL + "/attachments/%s";

    String OUTLOOK_MESSAGE_FOLDER_LIST_URL = OUTLOOK_URL + "/me/folders";
    String OUTLOOK_MESSAGE_FOLDER_ITEM_URL = OUTLOOK_MESSAGE_FOLDER_LIST_URL + "/%s";
    String OUTLOOK_MESSAGE_FOLDER_ITEM_MOVE_URL = OUTLOOK_MESSAGE_FOLDER_ITEM_URL + "/move";
    String OUTLOOK_MESSAGE_FOLDER_ITEM_COPY_URL = OUTLOOK_MESSAGE_FOLDER_ITEM_URL + "/copy";
    String OUTLOOK_MESSAGE_FOLDER_ITEM_MESSAGE_LIST_URL = OUTLOOK_MESSAGE_FOLDER_ITEM_URL + "/messages";

    String OUTLOOK_CONTACT_LIST_URL = OUTLOOK_URL + "/me/contacts";
    String OUTLOOK_CONTACT_ITEM_URL = OUTLOOK_CONTACT_LIST_URL + "/%s";

    String OUTLOOK_CONTACT_FOLDER_LIST_URL = OUTLOOK_URL + "/me/contactfolders";
    String OUTLOOK_CONTACT_FOLDER_ITEM_URL = OUTLOOK_CONTACT_FOLDER_LIST_URL + "/%s";
    String OUTLOOK_CONTACT_FOLDER_ITEM_CONTACT_LIST_URL = OUTLOOK_CONTACT_FOLDER_ITEM_URL + "/contacts";
    String OUTLOOK_CONTACT_FOLDER_ITEM_CHILD_FOLDER_LIST_URL = OUTLOOK_CONTACT_FOLDER_ITEM_URL + "/childfolders";

    String OUTLOOK_CONTACT_GROUP_LIST_URL = OUTLOOK_URL + "/groups";
    String OUTLOOK_CONTACT_GROUP_MEMBERS = OUTLOOK_CONTACT_GROUP_LIST_URL + "/%s/members";

    String CALENDAR_VIEW = "/CalendarView";
    String START_DATE_TIME = "/?startDateTime=";
    String END_DATE_TIME = "&endDateTime=";
}
