package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 13, 2010
 * Time: 3:56:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MassMailService extends RemoteService {

    ListResult<MailListItem> getMailLists(ListingFilterParameter filterParametrs);

    MailListItem getMailList(Integer object_id);

    Integer saveMailList(MailListItem item, ListingFilterParameter filterParametrs, boolean isLead);

    void deleteMailList(Integer mailListID);

    ListResult<SelectItem> getMailListMembers(ListingFilterParameter filterParametrs);

    ListResult<MailMessageItem> getMailMessageList(ListingFilterParameter filterParametrs);

    void createBatchSelectMailingList(ArrayList<Integer> checkedMailingLists, ArrayList<Integer> selectedIds);

    Integer saveMailMessage(MailMessageItem item, ArrayList<Integer> subscribedMailLists);

    MailMessageItem getMailMessage(Integer objectId, boolean isSMS, boolean isViewMode);

    void deleteMailMessage(Integer objectID);

    void cancelSchedule(Integer objectID);

    void unsubscribeFromMailList(Integer mailListID, boolean unsbuscribe, ArrayList<Integer> subscriberID);

    ListResult<MessageTrackListItem> getMessageViewTrackList(ListingFilterParameter fp);

    ListResult<MessageTrackListItem> getMessageBouncedList(ListingFilterParameter fp);

    ListResult<MessageTrackListItem> getUnsubscribedList(ListingFilterParameter fp);

    Long checkMassMailLimit(ArrayList<Integer> mailListIDs, Date scheduled);

    SelectItem[] getFromEmailsAsSelectItem(String searchKey);

    void sendTestEmail(MailMessageItem item, String toEmail);

    void updateCrmEntityMailLists(ListingFilterParameter fp, ArrayList<Integer> checkedMailLists);

    SelectItem[] getMailListByCrmEntityID(Integer contactID);

    ListResult<MessageTrackListItem> getMessageClickTrackList(ListingFilterParameter filterParameter);

    SelectItem[] getMailListsByMessage(Integer messageID);

    void deleteMails(ArrayList<Integer> mailIDs);



    class App {
        public static MassMailServiceAsync get() {
            ServiceDefTarget target = GWT.create(MassMailService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/massmail");
            return (MassMailServiceAsync) target;
        }
    }
}
