package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Atabaev on 10/10/2018.
 */
public class ExtendedItemUploadForm extends HorizontalPanel implements CustomCellInterface {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private int folderType;
    private Integer itemID;
    private boolean hideAttachLink = false;
    private HashMap<Integer, FileResource> savedFiles;
    private ImageUploadDialog uploadDialog;
    private SimpleLink viewLink, attachLink;

    public ExtendedItemUploadForm(int folderType, Integer itemID) {
        super();
        this.folderType = folderType;
        this.itemID = itemID;
        draw();
    }

    public ExtendedItemUploadForm(int folderType, boolean hideAttachLink) {
        super();
        this.folderType = folderType;
        this.hideAttachLink = hideAttachLink;
        draw();
    }

    public FileResource[] getAttachedFiles() {
        return savedFiles.values().toArray(new FileResource[]{});
    }

    public void setFile(FileResource file) {
        if (file != null) {
            savedFiles.put(file.getObjectId(), file);
            viewLink.setVisible(true);
        }
    }

    public void setFiles(ArrayList<FileResource> files) {
        if (files != null && files.size() > 0) {
            for (FileResource file : files) {
                savedFiles.put(file.getObjectId(), file);
            }
            viewLink.setVisible(true);
        }
    }

    private void draw() {
        savedFiles = new HashMap<>();
        viewLink = new SimpleLink(wfmStrings.summaryView());
        viewLink.setVisible(false);
        viewLink.getElement().getStyle().setPaddingLeft(10d, Style.Unit.PX);
        uploadDialog = new ImageUploadDialog(itemID, this);
        viewLink.addClickHandler(clickEvent -> showAttachmentsPanel());

        if (!hideAttachLink) {
            attachLink = new SimpleLink(wfmStrings.attach());
            attachLink.addClickHandler(clickEvent -> {
                uploadDialog = new ImageUploadDialog(itemID, this);
                uploadDialog.center();
                //uploadDialog.open();
            });
            add(attachLink);
        }
        add(viewLink);
    }

    private void showAttachmentsPanel() {
        if (savedFiles.size() > 0) {
            FileResource[] files = savedFiles.values().toArray(new FileResource[]{});
            Arrays.sort(files, (o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));
            final KpiModal dialogBox = new KpiModal();
            final GeneralAttachmentLinksComponent attachmentsPanel = new GeneralAttachmentLinksComponent(files,
                    false,
                    true);
            attachmentsPanel.onRemoveAttachment(id -> {
                if (attachmentsPanel.getDataGrid().getRowCount() == 0) {
                    dialogBox.close();
                    viewLink.setVisible(false);
                }
                DocumentsService.App.get().deleteFile(id, new AbstractAsyncCallback() {
                    public void success(Object result) {
                        if (savedFiles.containsKey(id)) {
                            savedFiles.remove(id);
                        }
                    }
                });
            });
            dialogBox.add(attachmentsPanel);

            WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
            cancel.addClickHandler(clickEvent -> dialogBox.close());
            dialogBox.addButton(cancel);
            dialogBox.open();
        }
    }

    @Override
    public String getDisplayValue() {
        return "<u>" + wfmStrings.attachment() + "</u>";
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {

    }
}
