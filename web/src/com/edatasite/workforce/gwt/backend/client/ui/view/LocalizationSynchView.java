package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 2016/11/29.
 */
public class LocalizationSynchView extends BaseListView {
    private static LocalizationSynchViewUiBinder ourUiBinder = GWT.create(LocalizationSynchViewUiBinder.class);
    @UiField
    Button importUpload;
    @UiField
    Button updateDatabase;
    @UiField
    Button updateResource;

    private DataListBox propertyType;
    private DataListBox locale;

    public LocalizationSynchView() {
        super("synchronizationView", "Synchronization");
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        add(ourUiBinder.createAndBindUi(this));
        initPropertyItems();
        initLocaleItems();
        importUpload.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                showImportPopup();
            }
        });

        updateDatabase.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                BackendService.App.get().localizationUpdateDataBase(new AbstractAsyncCallback<Void>() {
                    @Override
                    public void success(Void result) {
                        super.success(result);
                    }
                });
            }
        });
        updateResource.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                BackendService.App.get().localizationUpdateResource(new AbstractAsyncCallback<Void>() {
                    @Override
                    public void success(Void result) {
                        super.success(result);
                    }
                });
            }
        });
        return null;
    }

    private void initPropertyItems() {
        propertyType = new DataListBox();
        propertyType.setWithoutNullLabel(true);
        BackendService.App.get().getPropertyItems(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                propertyType.setItems(selectItems);
                propertyType.setSelected(0);
            }
        });
    }

    private void initLocaleItems() {
        locale = new DataListBox();
        locale.setWithoutNullLabel(true);
        SelectItem[] item = new SelectItem[10];
        item[0] = new SelectItem(0, "English", "en");
        item[1] = new SelectItem(1, "Russian", "ru");
        item[2] = new SelectItem(2, "French", "fr");
        item[3] = new SelectItem(3, "Nederland", "nl");
        item[4] = new SelectItem(4, "Portugal", "pt");
        item[5] = new SelectItem(5, "Spain", "es");
        item[6] = new SelectItem(6, "Thai", "th");
        item[7] = new SelectItem(7, "Turkish", "tr");
        item[8] = new SelectItem(8, "Italian", "it");
        item[9] = new SelectItem(9, "Arabic", "ar");
        locale.setItems(item);
        locale.setSelected(0);
    }

    private void showImportPopup() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);
        panel.add(propertyType);
        panel.add(locale);
        KpiModal popup = new KpiModal();
        popup.setSize(250, 100);

        FlexTable cont = new FlexTable();

        final FileUpload upload = new FileUpload();
        upload.setName(CommandConstants.ATTACHMENT_PARAM_BASE + 0);
        TextArea description = new TextArea();
        description.setName(CommandConstants.DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(CommandConstants.UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final WfmFormPanel uploadLabel = new WfmFormPanel("/CreateAttachment");
        uploadLabel.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                if (uploadLabel.getErrorString() == null) {
                    ImportFile importFile = new ImportFile();
                    importFile.setDefaultSeparator('|');
                    importFile.setFileID(uploadLabel.getObjectID());
                    importFile.setViewType(propertyType.getSelectedItem().getName());//Property name
                    importFile.setCategoryColumns(locale.getSelectedItem().getDescription());//Language
                    BackendService.App.get().importLocalizationPropertyToDB(importFile, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            super.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            super.onSuccess(result);
                        }
                    });
                    popup.close();
                }
                Info.show(uploadLabel.getErrorString() != null ? wfmStrings.messParseErrorCompareFile() : Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
            }
        });
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(upload);
        hp.add(description);
        hp.add(uploadType);

        Button imp = new Button(wfmStrings.uploadFile(), (ClickHandler) event -> {
            if (upload.getFilename() != null && !"".equals(upload.getFilename())) {
                if (".csv".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf(".")))) {
                    uploadLabel.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(wfmStrings.messSelectCSVFile(), Info.Type.WARNING);
                }
            }
        });
        imp.setWidth("80px");

        Button cancel = new Button(wfmStrings.cancel(), (ClickHandler) event -> popup.close());

        uploadLabel.setWidget(hp);
        cont.setWidget(0, 0, uploadLabel);

        panel.add(imp);
        panel.add(cancel);
        cont.setWidget(1, 0, panel);
        cont.setCellPadding(5);
        cont.setCellSpacing(5);

        popup.add(cont);
        popup.open();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    interface LocalizationSynchViewUiBinder extends UiBinder<HTMLPanel, LocalizationSynchView> {
    }
}