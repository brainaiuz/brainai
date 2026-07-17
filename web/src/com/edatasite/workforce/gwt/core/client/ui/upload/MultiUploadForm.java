package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.WfmTableItem;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmTable;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MultiUploadForm extends Composite {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private final GWTFileUpload uploadForm;
    private WfmTable table;
    private HTML attachedDocs;
    private final HorizontalPanel receiptsPanel;
    private SimpleLink attach;
    private KpiModal messageModal;
    private SimpleLink view;
    private String uploadedText;
    private boolean viewAndEdit = false;
    private Integer objectId;

    public MultiUploadForm(boolean viewOnly) {
        viewAndEdit = viewOnly;

        uploadForm = new GWTFileUpload();

        receiptsPanel = new HorizontalPanel();
        receiptsPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        receiptsPanel.setWidth("100%");
        receiptsPanel.setSpacing(5);

        initWidget(receiptsPanel);
        initComplit();
    }

    public void clear() {
        uploadForm.clearAndAdd();
        attach.removeFromParent();
        initComplit();
    }

    public Integer getObjectId() {
        return objectId;
    }

    public GWTFileUpload getUploadForm() {
        return uploadForm;
    }

    public boolean isUploaded() {
        return uploadForm.isFinished();
    }

    public void refreshTable(Integer expenseId) {
        ReportService.App.get().getExpenseAttachments(expenseId, new AbstractAsyncCallback<FileItem[]>() {
            public void success(FileItem[] fileItems) {
                loadTable(fileItems);
            }
        });
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setUploadedText(String uploadedText) {
        this.uploadedText = uploadedText;
    }

    public void viewForm() {
        KpiModal messageModal = new KpiModal();

        attachedDocs = new HTML();
        final HorizontalPanel uploadPanel = new HorizontalPanel();
        uploadPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        uploadPanel.setSpacing(10);
        uploadPanel.setWidth("100%");
        uploadPanel.setHeight("100%");

        if (table.getItemsCount() == 0) {
            table.setVisible(false);
            attachedDocs.setHTML("<span style='font-size:18px;color:#13649B'><b>" + wfmStrings.noDocumentsAttached() + "</b></span>");
        } else {
            table.setVisible(true);
        }
        messageModal.setWidth(420);
        messageModal.setScrollable(true);
        uploadPanel.add(attachedDocs);
        uploadPanel.setCellVerticalAlignment(attachedDocs, HorizontalPanel.ALIGN_MIDDLE);
        uploadPanel.add(table);
        uploadPanel.setCellVerticalAlignment(table, HorizontalPanel.ALIGN_TOP);
        messageModal.add(uploadPanel);
        messageModal.setTitle(wfmStrings.summaryView());
        messageModal.open();
        messageModal.addCloseHandler(popupPanelCloseEvent -> uploadPanel.remove(table));
    }

    public void uploadForm() {
        WfmButton2 okButton = new WfmButton2(wfmStrings.ok().toUpperCase(), WfmButton2.BTN_PRIMARY);
        okButton.addClickHandler(event -> {
            if (!uploadForm.isEmpty()) {
                setSuccessText();
            }
            messageModal.close();
        });

        final FlexPanel panel = new FlexPanel();
        panel.add(uploadForm);
        panel.setPadding(7);

        messageModal = new KpiModal();
        messageModal.addButton(okButton);
        messageModal.add(panel);
        messageModal.setWidth(420);
        messageModal.setScrollable(true);
        messageModal.getScrollPanel().setWidth("100%");
        messageModal.setTitle(wfmStrings.upload());
        messageModal.open();
        messageModal.addCloseHandler(popupPanelCloseEvent -> {
            if (uploadForm.isEmpty()) {
                setClearText();
            }
        });
        uploadForm.onStartUpload(() -> {
            LoadingPanel.loading(true, messageModal);
            okButton.setEnabled(false);
        });
        uploadForm.onFinishUpload(() -> {
            LoadingPanel.loading(false);
            okButton.setEnabled(true);
            setSuccessText();
        });
        uploadForm.onCancelUpload(id -> {
            LoadingPanel.loading(false);
            okButton.setEnabled(true);
            setSuccessText();
        });
    }

    private void setClearText() {
        attach.setStyleName("uploadLinkStyle2");
        attach.setText(wfmStrings.attach());
    }

    private void setSuccessText() {
        if (uploadedText != null) {
            attach.setText(uploadedText);
        } else {
            attach.setText(wfmStrings.uploaded());
        }
        attach.setStyleName("uploadLinkStyle");
    }

    private void loadTable(final FileItem[] fileItems) {
        table.removeAll();
        for (int i = 0; i < fileItems.length; i++) {
            SimpleLink downloadLink = new SimpleLink(wfmStrings.download());
            SimpleLink removeLink = new SimpleLink(wfmStrings.delete());
            final WfmTableItem item = new WfmTableItem(fileItems[i], downloadLink, removeLink, false);
            if (!viewAndEdit) {
                item.deleteRemoveLink();
            }
            table.add(item);

            final int id = i;
            downloadLink.addClickHandler(sender -> {
                String action;
                if (Constants.AMAZON.equals(fileItems[id].getUploadType())) {
//                        action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadFile?id=" + fileItems[id].getAttachmentId().toString();
                    action = fileItems[id].getAmazonLink();
                } else if (Constants.GOOGLE.equals(fileItems[id].getUploadType())) {
                    action = fileItems[id].getGoogleDocumentLink();
                } else if (Constants.OFFICE_365.equals(fileItems[id].getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(fileItems[id].getUploadType())) {
                    action = fileItems[id].getOfficeDocumentLink();
                } else {
                    action = fileItems[id].getAmazonLink();
                    if (action == null || "".equals(action)) {
                        action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadFile?id=" + fileItems[id].getAttachmentId().toString();
                    }
                }
                /*String action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadFile?id=" + fileItems[id].getAttachmentId().toString();*/
                Window.open(action, "_blank", "");
            });

            removeLink.addClickHandler(sender -> DocumentsService.App.get().deleteFile(fileItems[id].getId(), new AbstractAsyncCallback() {
                public void success(Object object) {
                    table.remove(item);
                    if (table.getItemsCount() == 0) {
                        table.setVisible(false);
                        attachedDocs.setHTML("<span style='font-size:18px;color:#13649B'><b>" + wfmStrings.noDocumentsAttached() + "</b></span>");
                    }
                }
            }));
        }
    }

    private void initComplit() {
        attach = new SimpleLink(wfmStrings.attach());
        attach.addClickHandler(sender -> {
            if (messageModal == null) {
                uploadForm();
            } else {
                messageModal.setWidth(420);
                messageModal.open();
            }
        });
        if (viewAndEdit) {
            initView();
            receiptsPanel.add(view);
        }
        receiptsPanel.add(attach);
    }

    private void initView() {
        table = new WfmTable(new FlowPanel(/*Style.HEADER)*/));
        table.setWidth("395px");

        view = new SimpleLink(wfmStrings.summaryView());
        view.addClickHandler(sender -> viewForm());
    }
}