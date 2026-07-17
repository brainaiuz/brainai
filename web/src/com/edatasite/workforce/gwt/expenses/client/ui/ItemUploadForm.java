package com.edatasite.workforce.gwt.expenses.client.ui;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 8/16/11
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemUploadForm extends Composite implements CustomCellInterface {

    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private int folderType;
    private boolean isPaymentWidget;
    private HashMap<Integer, FileResource> savedFiles = new HashMap<>();
    private GWTFileUploadDialog uploadDialog;
    private MaterialLink viewLink;

    public ItemUploadForm(int folderType) {
        this.folderType = folderType;
        draw();
    }

    public ItemUploadForm(int folderType, boolean isPaymentWidget) {
        this.folderType = folderType;
        this.isPaymentWidget = isPaymentWidget;
        draw();
    }

    public FileResource[] getAttachedFiles() {
        return savedFiles.values().toArray(new FileResource[]{});
    }

    public void setFiles(FileResource[] files) {
        if (files != null && files.length > 0) {
            for (FileResource file : files) {
                this.savedFiles.put(file.getObjectId(), file);
            }
            viewLink.setVisible(true);
        }
    }

    private void draw() {
//        HorizontalPanel attachViewPanel = new HorizontalPanel();
//        attachViewPanel.setSpacing(5);

        if (isPaymentWidget) {
            MaterialLink attachLink = new MaterialLink();
            attachLink.add(new SvgIcon(SvgEnum.uploadCloud));

            viewLink = new MaterialLink(wfmStrings.summaryView());
            viewLink.setVisible(false);

            uploadDialog = new GWTFileUploadDialog(folderType, null, null);
            uploadDialog.onLoadCommand(() -> {
                HashMap<Integer, FileResource> files = uploadDialog.getUploadedFiles();
                if (files != null && files.size() > 0) {
                    for (FileResource fr : files.values()) {
                        savedFiles.put(fr.getObjectId(), fr);
                    }
                }
            });
            uploadDialog.setActivator(attachLink);
            initWidget(attachLink);
        } else {
            MaterialLink attachLink = new MaterialLink(wfmStrings.attach());
            viewLink = new MaterialLink(wfmStrings.summaryView());
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


            GColumn attachColumn = new GColumn();
            attachColumn.add(attachLink);
            GColumn viewColumn = new GColumn();
            viewColumn.add(viewLink);
            GRow row = new GRow(attachColumn, viewColumn);
//        attachViewPanel.add(attachLink);
//        attachViewPanel.add(viewLink);

            initWidget(row);
        }

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
                        savedFiles.remove(id);
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
        return getParent() != null ? getParent().getElement().getInnerHTML() : wfmStrings.attach();
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {

    }
}
