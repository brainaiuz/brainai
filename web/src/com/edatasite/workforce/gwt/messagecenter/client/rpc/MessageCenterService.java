package com.edatasite.workforce.gwt.messagecenter.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 18, 2010
 * Time: 6:24:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageCenterService extends RemoteService {

    CaseItem getCase(Integer id);

    ArrayList<SelectItem> getContactAccounts(Integer ContactId);

    ArrayList<FileResource> getAttachedFilesByAttachmentId(Integer attachmentId);

    ListResult<Email> getEmails(ListingFilterParameter params);

    ListResult<Email> getEmailsToTop(ListingFilterParameter params);

    Integer getEmailsCountToTop(ListingFilterParameter params);

    void manuallyFetchEmails(Integer folderID);

    void sendReferMessage(Email message);

    void sendUserPostedToFacebookEmail();

    Integer sendMessage(Email messageMessage);

    Email getEmailWithContent(String emailID);

    ArrayList<EmailAccountItem> getSharedEmailAccounts(boolean withInboxCount);

    SelectItem[] getUserEmailAccounts(boolean showAll);

    Integer testConnection(EmailAccountItem item);

    Integer saveEmailAccount(EmailAccountItem item);

    EmailAccountItem getEmailAccount(Integer objectID);

    boolean deleteEmailAccount(Integer objectID);

    void addEmailAccountToHazelcast(Integer accountID);

    void setEmailFlags(ArrayList<String> emailIDs, Integer emailFolderID, String flag);

    ArrayList<EmailFolder> getEmailAccountFolders(Integer emailSettingID, boolean refresh);

    HashMap<EmailAccountItem, HashSet<EmailFolder>> getUserFetchableEmailFolders();

    void saveFetchableFolders(ArrayList<Integer> fetchableFolderIDs, Integer trashFolderID, Integer sentFolderID, Integer emailSettingID);

    boolean isEmailAccountSetup();

    String getRelatedEmail(String relationType, Integer relationID);

    void saveAsDraft(Email messageMessage);

    Email getEmail(String objectID);

    SelectItem[] getImapHostAndSmptHost(ListingFilterParameter parameter);


    class App {
        public static MessageCenterServiceAsync get() {
            ServiceDefTarget target = GWT.create(MessageCenterService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/messageCenter");
            return (MessageCenterServiceAsync) target;
        }
    }
}
