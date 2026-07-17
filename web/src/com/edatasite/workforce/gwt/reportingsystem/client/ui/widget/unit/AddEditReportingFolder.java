package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Virus on 9/11/14.
 */
public class AddEditReportingFolder extends KpiModal {
    interface AddEditReportingFolderUiBinder extends UiBinder<HTMLPanel, AddEditReportingFolder> {
    }

    private static final AddEditReportingFolderUiBinder ourUiBinder = GWT.create(AddEditReportingFolderUiBinder.class);
    @UiField
    DataListBox folderType;
    @UiField
    DataListBox moduleType;
    @UiField
    TextArea2 description;
    @UiField
    InputElement name;
    @UiField
    HTML errorName;
    @UiField
    HTML errorModuleType;
    @UiField
    LabelElement folderNameLabel;
    @UiField
    LabelElement descriptionLabel;
    @UiField
    LabelElement folderTypeLabel;
    @UiField
    LabelElement moduleTypeLabel;

    private final ReportingStrings reportingStrings = ReportingStrings.App.get();

    WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
    WfmButton2 closeButton = new WfmButton2(wfmStrings.cancel());

    private Integer id;
    public static Integer categoryId;
    private Integer folderId;

    public AddEditReportingFolder() {
        super();
        setWidth("400px");
        addStyleName("bt_area");
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        add(rootElement);
        folderType.setItems(FolderType.asSelectItem(false));
        loading();

        addButton(closeButton);
        addButton(saveButton);
        initHandler(null);

        DOM.sinkEvents(name, Event.ONKEYDOWN);
        DOM.setEventListener(name, event -> errorName.setHTML(""));
    }

    public AddEditReportingFolder(FolderRpc folderRpc) {
        super();
        this.folderId = folderRpc.getId();
        setWidth("400px");
        addStyleName("bt_area");
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        add(rootElement);
        name.setValue(folderRpc.getName());
        description.setText(folderRpc.getDescription());
        String type = "Public".equalsIgnoreCase(folderRpc.getType()) ? "Private" : "Public";
        folderType.setItems(new SelectItem[]{
                new SelectItem(0, folderRpc.getType()),
                new SelectItem(1, type)
        });
        folderType.setReadOnly(true);

        Set<SelectItem> categories = new TreeSet<>(Comparator.comparing(SelectItem::getId));
        if (folderRpc.getCategoryId() != null && folderRpc.getCategoryName() != null) {
            categories.add(new SelectItem(folderRpc.getCategoryId(), folderRpc.getCategoryName()));
        }
        moduleType.setItems(categories.toArray(new SelectItem[0]));
        moduleType.setSelected(folderRpc.getCategoryId());
        moduleType.setReadOnly(true);

        addButton(closeButton);
        addButton(saveButton);
        initHandler(folderId);

        DOM.sinkEvents(name, Event.ONKEYDOWN);
        DOM.setEventListener(name, event -> errorName.setHTML(""));
    }

    private void loading() {
        ReportingService.App.get().getFolders(null, new AbstractAsyncCallback<FolderRpc[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(FolderRpc[] result) {
                if (result != null && result.length > 0) {
                    Set<SelectItem> folders = new TreeSet<>(Comparator.comparing(SelectItem::getId));
                    for (FolderRpc folder : result) {
                        if (folder.getCategoryId() != null && folder.getCategoryName() != null) {
                            folders.add(new SelectItem(folder.getCategoryId(), folder.getCategoryName()));
                        }
                    }
                    moduleType.setItems(folders.toArray(new SelectItem[0]));
                    moduleType.setSelected(categoryId);
                }
            }
        });
    }

    private void initHandler(final Integer folderId) {
        saveButton.addClickHandler(event -> {
            if (!isValidate()) {
                return;
            }
            FolderRpc rpc = new FolderRpc();

            if (folderId != null && folderId != 0) {
                rpc.setId(folderId);
            } else {
                rpc.setId(id);
            }
            rpc.setName(name.getValue());
            rpc.setDescription(description.getText());
            rpc.setType(folderType.getSelectedItem(true).getName());
            rpc.setCategoryName(moduleType.getSelectedItem(true).getName());
            rpc.setCategoryId(moduleType.getSelectedId(true));
            LoadingPanel.loading(true);
            ReportingService.App.get().saveFolder(rpc, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    new WfmMessageBox(IconEnum.ERROR, Action.OK, reportingStrings.folderAlreadyExists()).open();
                }

                @Override
                public void onSuccess(Boolean result) {
                    LoadingPanel.loading(false);
                    if (Boolean.TRUE.equals(result)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORTING_FOLDER_SAVED, result, AddEditReportingFolder.this);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.folders()));
                        close();
                    } else {
                        new WfmMessageBox(IconEnum.ERROR, Action.OK, reportingStrings.folderAlreadyExists()).open();
                    }
                }
            });
        });
        LoadingPanel.loading(false);
        closeButton.addClickHandler(event -> close());


        folderNameLabel.setInnerText(wfmStrings.folderName());
        descriptionLabel.setInnerText(wfmStrings.description());
        folderTypeLabel.setInnerText(wfmStrings.folderType());
        moduleTypeLabel.setInnerText(reportingStrings.moduleType());
    }


    private boolean isValidate() {
        int error = 0;
        errorName.setHTML("");
        errorModuleType.setHTML("");

        if ("".equals(name.getValue()) || name.getValue() == null) {
            errorName.setHTML("<b style='color:red;'>" + wfmStrings.fillRequiredField() + "</b>");
            error++;
        }
        if (moduleType.getItems().length == 0) {
            errorModuleType.setHTML("<b style='color:red;'>Please select required fields</b>");
            error++;
        }
        if (error > 0) {
            new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.fillRequiredField()).open();
            return false;
        }
        return true;
    }
}
