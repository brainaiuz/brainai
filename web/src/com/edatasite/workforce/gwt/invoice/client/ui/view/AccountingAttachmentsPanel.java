package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/27/11
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingAttachmentsPanel extends VerticalPanel {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private FileItem[] attachments;
    private boolean isClient;
    private String width;

    public AccountingAttachmentsPanel(FileItem[] attachments) {
        this.attachments = attachments;
        init();
    }
    public AccountingAttachmentsPanel(FileItem[] attachments, String width) {
        this.attachments = attachments;
        this.width = width;
        init();
    }
    public AccountingAttachmentsPanel(FileItem[] attachments, boolean isClient) {
        this.isClient = isClient;
        this.attachments = attachments;
        init();
    }

    private void init() {
        if (attachments != null && attachments.length > 0) {
            FlowPanel attContainer = new FlowPanel();
            FileResource[] fileResources = new FileResource[attachments.length];
            for (int i = 0; i < attachments.length; i++) {
                FileResource fileResource = new FileResource();
                fileResource.setObjectId(attachments[i].getAttachmentId());
                fileResource.setBodyId(attachments[i].getAttachmentId());
                fileResource.setContentLength(attachments[i].getSize());
                fileResource.setCreationDate(attachments[i].getDate());
                fileResource.setDescription(attachments[i].getDescription());
                fileResource.setUploadType(attachments[i].getUploadType());
                switch (attachments[i].getUploadType()) {
                    case Constants.GOOGLE:
                        fileResource.setGoogleDownloadLink(attachments[i].getGoogleDocumentLink());
                        break;
                    case Constants.OFFICE_365:
                    case Constants.OFFICE_365_SHARE_POINT:
                        fileResource.setDocumentID(attachments[i].getDocumentID());
                        fileResource.setDocumentOpenID(attachments[i].getDocumentOpenID());
                        fileResource.setOfficeDownloadLink(attachments[i].getOfficeDocumentLink());
                        break;
                    default:
                        fileResource.setAmazonLink(attachments[i].getAmazonLink());
                        break;
                }
                fileResource.setName(attachments[i].getFileName());
                fileResources[i] = fileResource;
            }
            GeneralAttachmentLinksComponent attachments = new GeneralAttachmentLinksComponent(fileResources, !isClient);
            attachments.onRemoveAttachment(id -> InvoiceService.App.get().deleteAttachment(id, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void success(Void aVoid) {

                }
            }));
            attContainer.add(attachments);

            setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
            setWidth(this.width == null || "".equals(this.width) ? "95%" : width);
            add(new HTML("<b class=customTitle>" + wfmStrings.attachments() + ":</b>"));
            add(attContainer);
        }
    }

}
