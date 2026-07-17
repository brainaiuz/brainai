package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlexTable;
import gwt.material.design.client.base.MaterialWidget;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Shohruh on 12-Feb-16.
 */
public class ItemUploadTable extends FlexTable implements CustomCellInterface {

    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private int folderType;
    private HashMap<Integer, FileResource> savedFiles = new HashMap<>();
    private GWTFileUploadDialog uploadDialog;
    private SimpleLink viewLink;
    private MaterialWidget attachLink;

    public ItemUploadTable(int folderType) {
        super();
        this.folderType = folderType;

        attachLink = new MaterialWidget(Document.get().createAnchorElement());
        attachLink.getElement().setInnerHTML(wfmStrings.attach());
        viewLink = new SimpleLink(wfmStrings.summaryView());
        viewLink.setVisible(false);
        uploadDialog = new GWTFileUploadDialog(folderType, null, null);
        uploadDialog.onLoadCommand(() -> {
            HashMap<Integer, FileResource> files = uploadDialog.getUploadedFiles();
            if (files != null && files.size() > 0) {
                for (FileResource fr : files.values()) {
                    savedFiles.put(fr.getObjectId(), fr);
                }
            }
            viewLink.setVisible(true);
        });
        uploadDialog.setActivator(attachLink);
        viewLink.addClickHandler(clickEvent -> showAttachmentsPanel());

        setWidget(0, 0, attachLink);
        setWidget(0, 1, viewLink);
    }

    public FileResource[] getAttachedFiles() {
        return savedFiles.values().toArray(new FileResource[]{});
    }

    public FileItem[] getAttachedFiles1() {
        FileItem[] attachments = new FileItem[savedFiles.size()];

        int i = 0;
        for (FileResource file : savedFiles.values()) {
            attachments[i] = new FileItem();
            attachments[i].setId(file.getObjectId());
            attachments[i].setFileName(file.getName());
            i++;
        }
        return attachments;
    }

    public void setFiles(FileResource[] files) {
        if (files != null && files.length > 0) {
            for (FileResource file : files) {
                this.savedFiles.put(file.getObjectId(), file);
            }
            viewLink.setVisible(true);
        }
    }

    private void showAttachmentsPanel() {
        if (savedFiles.size() > 0) {
            FileResource[] files = savedFiles.values().toArray(new FileResource[]{});
            Arrays.sort(files, (o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));
            final KpiModal dialogBox = new KpiModal();
            dialogBox.setWidth("740px");
            final GeneralAttachmentLinksComponent attachmentsPanel = new GeneralAttachmentLinksComponent(files, false, true);
            attachmentsPanel.getDataGrid().addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
            attachmentsPanel.onRemoveAttachment(id -> {
                if (attachmentsPanel.getDataGrid().getRowCount() == 0) {
                    dialogBox.close();
                    viewLink.setVisible(false);
                }
                DocumentsService.App.get().deleteFile(id, new AbstractAsyncCallback() {
                    public void success(Object result) {
                        savedFiles.remove(id);
                    }
                });
            });
            dialogBox.add(attachmentsPanel);
            dialogBox.open();
        }
    }

    @Override
    public String getDisplayValue() {
        return wfmStrings.attach() + (viewLink.isVisible() ? " | " + wfmStrings.summaryView() : "");
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
    }
}
