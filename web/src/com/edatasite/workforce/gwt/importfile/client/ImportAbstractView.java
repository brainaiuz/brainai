package com.edatasite.workforce.gwt.importfile.client;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 20-Jul-2010
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImportAbstractView extends CustomForm implements Constants {
    
    protected static final CommonServiceAsync commonService = CommonService.App.get();

    protected Integer objectId;
    protected SelectItem[] items;

    protected KpiSwitcher hasHeader;
    protected KpiRadioButton skipDuplicates;
    protected KpiRadioButton mergeDuplicates;
    protected KpiRadioButton cloneDuplicates;
    protected int errors = 0;
    public List<CompanyCustomFieldItem> companyCustomFieldItems;
    public DataListBox[] tbValues;

    protected char defaultSeparator = ',';


    public ImportAbstractView(String s, String s1) {
        super(s, s1);
    }

    public void drawForm() {
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            getCustomFieldUtil().drawCustomFields(this, objectId);
        }
        show();
    }

    public void loadPage() {
        if (getViewName() != null) {
            CommonService.App.get().getCompanyCustomFields(getViewName(), new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {

                }

                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        companyCustomFieldItems = result;
                        initialize();
                    }
                }
            });
        } else {
            initialize();
        }
    }

    protected abstract ViewName getViewName();

    protected Widget onInitialize() {
        super.onInitialize();
        loadPage();
        return null;
    }

    public void initialize() {
        drawForm();
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
            tbValues = new DataListBox[companyCustomFieldItems.size()];
            for (int i = 0; i < companyCustomFieldItems.size(); i++) {
                tbValues[i] = new DataListBox();
                tbValues[i].addStyleName(DEFAULT_WIDTH);
                addField(companyCustomFieldItems.get(i).getColumnCode(), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
            }
        }

        hasHeader = new KpiSwitcher();
        hasHeader.getElement().setId("has_header");
        hasHeader.setValue(true);
        //hasHeaderField = table.addField(wfmStrings.myCSVFileHasHeaders(), hasHeader);
        skipDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.skip());
        skipDuplicates.getElement().setId("skip_duplicates");
        skipDuplicates.setFormValue(ImportFile.SKIP);
        skipDuplicates.setValue(Boolean.TRUE);
        mergeDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.overwrite());
        mergeDuplicates.getElement().setId("merge_duplicates_overwrite");
        mergeDuplicates.setFormValue(ImportFile.MERGE);
        cloneDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.clonE());
        cloneDuplicates.getElement().setId("clone_duplicates");
        cloneDuplicates.setFormValue(ImportFile.CLONE);
        initDefaultImportPreference();
        FlexTable duplicateActionTable = new FlexTable();
        duplicateActionTable.addStyleName(Constants.DEFAULT_WIDTH);
        duplicateActionTable.setCellPadding(3);
        duplicateActionTable.setCellSpacing(3);
        duplicateActionTable.setWidget(0, 0, skipDuplicates);
        duplicateActionTable.setWidget(0, 1, mergeDuplicates);
        duplicateActionTable.setWidget(0, 2, cloneDuplicates);
        addField(MY_CSV_FILE_HAS_HEADERS, hasHeader, wfmStrings.myCSVFileHasHeaders());
        addField(DUPLICATE, duplicateActionTable, wfmStrings.duplicateAction());
        LoadingPanel.loading(true);
    }

    public abstract void setItems(SelectItem[] items);

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
                showFailureMessage();
            }

            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if (result != null && !"".equals(result)) {
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    showFailureMessage(errorMessage);
                } else {
                    showSuccessMessage();
                }
                if (ImportTypeEnum.BUDGET_MANAGER.equals(getType())) {
                    Timer timer = new Timer() {

                        @Override
                        public void run() {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BUDGET_SHEET_UPDATE, null, ImportAbstractView.this);
                        }
                    };
                    timer.schedule(2000);
                }
            }
        });
    }

    public String getSelectedItemAsString(DataListBox box) {
        return box.getSelectedItem().getId().toString();
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

    public void save() {
        LoadingPanel.loading(true);
        if (!validate()) {
            LoadingPanel.loading(false);
            return;
        }

        boolean hasHeader_ = hasHeader.getValue();
        ImportFile importFile = getImportFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader_);
        importFile.setType(getType());
        save(importFile);
    }

    protected abstract ImportFile getImportFile();

    public boolean validate() {
        errors = 0;
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void showSuccessMessage() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(wfmMessages.itemsSuccessfullyImported(getType().getCode()));
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, clickEvent -> save());
    }

    @Override
    protected void getDataToFillFields() {
        CommonService.App.get().getCSVColumns(objectId, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
                closeTab();
            }

            public void success(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        items = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(items);
                    setCustomFildsItems(items);
                    LoadingPanel.loading(false);
                });
            }
        });
    }

    private void setCustomFildsItems(SelectItem[] items) {
        if (items != null) {
            if (tbValues != null && tbValues.length > 0) {
                int i = 0;
                for (DataListBox box : tbValues) {
                    String title = companyCustomFieldItems.size() > i && companyCustomFieldItems.get(i) != null ? companyCustomFieldItems.get(i).getFieldName() : null;
                    box.setItems(items, title);
                    i++;
                }
            }
        }
    }

    protected abstract ImportTypeEnum getType();

    public void showFailureMessage(final String... message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message != null && message.length > 0 ? message[0] : wfmMessages.messImportItemError(getType().getCode()));
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            if (message == null || message.length == 0) {
                closeTab();
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

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

    private FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void initDefaultImportPreference() {
        ImportFileService.App.get().getImportPreference(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    if (s.equals("CLONE")) {
                        cloneDuplicates.setValue(true);
                    } else if (s.equals("OVERWRITE")) {
                        mergeDuplicates.setValue(true);
                    }
                }
            }
        });
    }
}