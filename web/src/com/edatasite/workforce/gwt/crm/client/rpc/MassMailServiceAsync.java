package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 13, 2010
 * Time: 4:00:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MassMailServiceAsync {
    Request getMailLists(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<MailListItem>> async);

    void getMailList(Integer object_id, AsyncCallback<MailListItem> async);

    void saveMailList(MailListItem item, ListingFilterParameter fp, boolean isLead, AsyncCallback<Integer> async);

    void deleteMailList(Integer mailListID, AsyncCallback<Void> async);

    void getMailListByCrmEntityID(Integer contactID, AsyncCallback<SelectItem[]> async);

    void getMailListsByMessage(Integer leadID, AsyncCallback<SelectItem[]> async);

    void updateCrmEntityMailLists(ListingFilterParameter fp, ArrayList<Integer> checkedMailLists, AsyncCallback<Void> async);

    Request getMailListMembers(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SelectItem>> async);

    void unsubscribeFromMailList(Integer mailListID, boolean unsbuscribe, ArrayList<Integer> subscriberIDs, AsyncCallback<Void> async);

    Request getMessageViewTrackList(ListingFilterParameter fp, AsyncCallback<ListResult<MessageTrackListItem>> async);

    Request getMessageClickTrackList(ListingFilterParameter fp, AsyncCallback<ListResult<MessageTrackListItem>> async);

    void saveMailMessage(MailMessageItem item, ArrayList<Integer> subscribedMailLists, AsyncCallback<Integer> async);

    void getMailMessage(Integer objectId, boolean isSMS, boolean isViewMode, AsyncCallback<MailMessageItem> async);

    void deleteMailMessage(Integer objectID, AsyncCallback<Void> async);

    void cancelSchedule(Integer objectID, AsyncCallback<Void> async);

    Request getMailMessageList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<MailMessageItem>> async);

    void createBatchSelectMailingList(ArrayList<Integer> checkedMailingLists, ArrayList<Integer> selectedIds, AsyncCallback<Void> callback);

    Request getMessageBouncedList(ListingFilterParameter fp, AsyncCallback<ListResult<MessageTrackListItem>> async);

    Request getUnsubscribedList(ListingFilterParameter fp, AsyncCallback<ListResult<MessageTrackListItem>> async);

    void checkMassMailLimit(ArrayList<Integer> mailListIDs, Date scheduled, AsyncCallback<Long> async);

    void getFromEmailsAsSelectItem(String searchKey, AsyncCallback<SelectItem[]> async);

    void sendTestEmail(MailMessageItem item, String toEmail, AsyncCallback<Void> async);

    void deleteMails(ArrayList<Integer> mailIDs, AsyncCallback<Void> async);

}
