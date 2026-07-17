package com.edatasite.workforce.gwt.messagecenter.server;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import jakarta.mail.Store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayot on 6/28/2014.
 */
public interface MessageCenterServiceLocal {

    SelectItem[] getUserEmailAccounts(boolean showAll);

    void saveAsDraft(Email messageMessage);

    Integer testConnection(EmailAccountItem item);

    Integer saveEmailAccount(EmailAccountItem item);

    Integer sendMessage(Email messageMessage);

    ListResult<Email> getEmails(ListingFilterParameter params);

    Email getEmail(String objectID);

    void setEmailFlags(ArrayList<String> emails, Integer emailSettingID, String flag);

    Email getEmailWithContent(String emailID);

    ArrayList<FileResource> getAttachedFilesByAttachmentId(Integer attachmentId);

    List<EdsEmail> getEmailsByIDs(Iterable<String> ids);

    List<EdsEmail> getLastFiveUnreadEmails(Integer emailSettingID);

    Integer fetchEmail(Integer folderId, Integer companyID, Integer emailSettingId, Store store) throws Exception;

    Object getContentOnly(String emailId);

    InputStream getInputStream(EdsEmailAttachment attachment);

    void updateEmailSettingsAfterException(Integer emailSettingsId, String exceptionMessage);
}
