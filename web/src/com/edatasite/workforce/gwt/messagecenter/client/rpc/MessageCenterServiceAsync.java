package com.edatasite.workforce.gwt.messagecenter.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 18, 2010
 * Time: 6:24:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageCenterServiceAsync {

    void getCase(Integer id, AsyncCallback<CaseItem> callback);

    void getAttachedFilesByAttachmentId(Integer attachmentId, AsyncCallback<ArrayList<FileResource>> callback);

    void getEmails(ListingFilterParameter params, AsyncCallback<ListResult<Email>> callback);

    void getEmailsToTop(ListingFilterParameter params, AsyncCallback<ListResult<Email>> callback);

    void getEmailsCountToTop(ListingFilterParameter params, AsyncCallback<Integer> callback);

    void getEmail(String objectID, AsyncCallback<Email> async);

    void getEmailWithContent(String emailID, AsyncCallback<Email> async);

    void manuallyFetchEmails(Integer folderID, AsyncCallback<Void> callback);

    void getSharedEmailAccounts(boolean withInboxCount, AsyncCallback<ArrayList<EmailAccountItem>> asyncCallback);

    void getUserEmailAccounts(boolean showAll, AsyncCallback<SelectItem[]> callback);

    void addEmailAccountToHazelcast(Integer accountID, AsyncCallback<Void> asyncCallback);

    void getEmailAccount(Integer objectID, AsyncCallback<EmailAccountItem> async);

    void testConnection(EmailAccountItem item, AsyncCallback<Integer> async);

    void saveEmailAccount(EmailAccountItem item, AsyncCallback<Integer> async);

    void deleteEmailAccount(Integer objectID, AsyncCallback<Boolean> async);

    void setEmailFlags(ArrayList<String> emailIDs, Integer emailFolderID, String flag, AsyncCallback<Void> callback);

    void getEmailAccountFolders(Integer emailSettingID, boolean refresh, AsyncCallback<ArrayList<EmailFolder>> callback);

    void getUserFetchableEmailFolders(AsyncCallback<HashMap<EmailAccountItem, HashSet<EmailFolder>>> callback);

    void saveFetchableFolders(ArrayList<Integer> fetchableFolderIDs, Integer trashFolderID, Integer sentFolderID, Integer emailSettingID, AsyncCallback<Void> callback);

    void isEmailAccountSetup(AsyncCallback<Boolean> callback);

    void getRelatedEmail(String relationType, Integer relationID, AsyncCallback<String> callback);

    void saveAsDraft(Email email, AsyncCallback<Void> callback);

    void sendReferMessage(Email message, AsyncCallback<Void> callback);

    void sendUserPostedToFacebookEmail(AsyncCallback<Void> callback);

    void sendMessage(Email messageMessage, AsyncCallback<Integer> callback);

    void getContactAccounts(Integer ContactId, AsyncCallback<ArrayList<SelectItem>> async);

    void getImapHostAndSmptHost(ListingFilterParameter parameter, AsyncCallback<SelectItem[]> async);
}
