package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 23.07.12
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImportCrmAccountAbstractView extends CustomForm implements Constants, FormHasCustomFieldInterface {
    protected static final CommonServiceAsync commonService = CommonService.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();

    protected char defaultSeparator = ',';
    protected Integer objectId;
    protected SelectItem[] items;

    protected KpiSwitcher hasHeader;
    protected KpiRadioButton skipDuplicates;
    protected KpiRadioButton mergeDuplicates;
    protected KpiRadioButton cloneDuplicates;
    protected String successMessage;
    protected String errorMessage;
    protected int errors = 0;
    protected FlexTable duplicateActionTable;

    public ImportCrmAccountAbstractView(String s, String s1) {
        super(s, s1);
    }

    public abstract void loadPage();

    protected Widget onInitialize() {
        super.onInitialize();
        loadPage();
        return null;
    }

    public void initialize() {
        hasHeader = new KpiSwitcher();
        hasHeader.setValue(Boolean.TRUE);
        skipDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.skip());
        skipDuplicates.setFormValue(ImportFile.SKIP);
        skipDuplicates.setValue(Boolean.TRUE);
        mergeDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.overwrite());
        mergeDuplicates.setFormValue(ImportFile.MERGE);
        cloneDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.clonE());
        cloneDuplicates.setFormValue(ImportFile.CLONE);
        duplicateActionTable = new FlexTable();
        duplicateActionTable.addStyleName(DEFAULT_WIDTH);
        duplicateActionTable.setCellPadding(3);
        duplicateActionTable.setCellSpacing(3);
        duplicateActionTable.setWidget(0, 0, skipDuplicates);
        duplicateActionTable.setWidget(0, 1, mergeDuplicates);
        duplicateActionTable.setWidget(0, 2, cloneDuplicates);

        drawForm();
    }

    protected abstract void drawForm();

    public abstract boolean validate();

    public abstract void setItems(SelectItem[] items);

    protected abstract ImportTypeEnum getImportType();

    public abstract void save();

    protected void save(ImportFile importFile) {
        if (skipDuplicates.getValue()) {
            importFile.setDuplicateAction(skipDuplicates.getFormValue());
        }
        if (mergeDuplicates.getValue()) {
            importFile.setDuplicateAction(mergeDuplicates.getFormValue());
        }
        if (cloneDuplicates.getValue()) {
            importFile.setDuplicateAction(cloneDuplicates.getFormValue());
        }
        ImportFileService.App.get().addImportToQueue(importFile, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                showFailureMessage(null);
            }

            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if (!Utils.isNullOrEmpty(result)) {
                    showFailureMessage(result + " " + wfmStrings.importIsAlreadyInProgress());
                } else {
                    showSuccessMessage();
                }
            }
        });
    }

    public Integer getSelectedItem(DataListBox dataListBox, boolean... isNullLabled) {
        if (dataListBox != null) {
            if (dataListBox.getSelectedItem() != null) {
                return dataListBox.getSelectedItem().getId();
            } else if (isNullLabled != null && isNullLabled.length > 0 && isNullLabled[0]) {
                return dataListBox.getItems() != null && dataListBox.getItems().length > 0 && dataListBox.getItems()[0] != null ? dataListBox.getItems()[0].getId() : null;
            }
        }
        return null;
    }

    public void showSuccessMessage() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmMessages.itemsSuccessfullyImported(getImportType().getCode()));
        messageBox.addCloseHandler(click -> {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_IMPORT_RELOAD_PAGE, null, null);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOMER_IMPORT_RELOAD_PAGE, null, null);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONVERSION_BALANCE_RELOAD_PAGE, null, null);
            closeTab();
        });
        messageBox.open();
    }

    public void showFailureMessage(final String message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(!Utils.isNullOrEmpty(message) ? message : errorMessage);
        messageBox.open();
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (message == null) {
                    closeTab();
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }
}
