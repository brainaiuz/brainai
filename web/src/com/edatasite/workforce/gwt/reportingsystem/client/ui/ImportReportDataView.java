package com.edatasite.workforce.gwt.reportingsystem.client.ui;

import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.edatasite.workforce.gwt.importfile.client.rpc.ReportDataImportItem;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportReportDataView extends View {

    private WfmButton2 saveButton;

    private Integer objectID;
    private Integer categoryId;
    private char defaultSeparator = ',';
    private DynamicTable dynamicTable;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public ImportReportDataView(Integer objectID, Integer categoryId) {
        super("add", "Improt ReportData");
        this.objectID = objectID;
        this.categoryId = categoryId;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {

        addStyleName("box-bg--1 box-radius section-box");
        LoadingPanel.loading(true);

        initializeFields();

        DynamicTableColumn columns[] = new DynamicTableColumn[3];
        columns[0] = new DynamicTableColumn(wfmStrings.columnName(), "name", 300);
        columns[1] = new DynamicTableColumn(wfmStrings.type(), "type", 300);
        columns[2] = new DynamicTableColumn("Format", "format", 300);

        dynamicTable = new DynamicTable(columns, false);

        CommonService.App.get().getCSVColumns(objectID, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            public void onFailure(Throwable d) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    SelectItem[] listItems = null;
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        listItems = entry.getValue();
                        if (!key.equals(new String(new char[]{defaultSeparator}))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(listItems);
                    LoadingPanel.loading(false);
                });
            }
        });

        add(dynamicTable);

        add(createFooter());

        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ImportReportDataView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> items = new ArrayList<>();
        items.add(wrapToDiv(saveButton));
        return items;
    }

    private SelectItem[] formetTypes() {
        SelectItem[] items = new SelectItem[5];
        items[0] = new SelectItem(1, "String", "string", true);
        items[1] = new SelectItem(2, "ShortDate", "date");
        items[2] = new SelectItem(3, "LongDate", "date");
        items[3] = new SelectItem(4, "Number", "number");
        items[4] = new SelectItem(5, "Money", "money");
        return items;
    }

    private void setItems(SelectItem[] items) {
        for (int i = 0; i < items.length; i++) {
            Widget widgets[] = new Widget[3];
            Label columnName = new Label(items[i].getName());

            DataListBox typeBox = new DataListBox();
            typeBox.setItems(formetTypes());
            typeBox.setSelected(1);
            int finalI = i;
            typeBox.addValueChangeHandler(valueChangeEvent -> showHideDateformat(finalI, typeBox.getSelectedId()));

            DataListBox dateFormat = new DataListBox();
            dateFormat.setEnabled(false);

            widgets[0] = columnName;
            widgets[1] = typeBox;
            widgets[2] = dateFormat;
            dynamicTable.addRow(widgets);

            showHideDateformat(i, typeBox.getSelectedId());
        }
    }


    public SelectItem[] getShortDateFormats() {
        SelectItem[] items = new SelectItem[18];
        items[0] = new SelectItem(1, EdsCompanySettings.SHORT_DATE_FORMAT_1, EdsCompanySettings.SHORT_DATE_FORMAT_1);
        items[1] = new SelectItem(2, EdsCompanySettings.SHORT_DATE_FORMAT_2, EdsCompanySettings.SHORT_DATE_FORMAT_2);
        items[2] = new SelectItem(3, EdsCompanySettings.SHORT_DATE_FORMAT_3, EdsCompanySettings.SHORT_DATE_FORMAT_3);
        items[3] = new SelectItem(4, EdsCompanySettings.SHORT_DATE_FORMAT_4, EdsCompanySettings.SHORT_DATE_FORMAT_4);
        items[4] = new SelectItem(5, EdsCompanySettings.SHORT_DATE_FORMAT_5, EdsCompanySettings.SHORT_DATE_FORMAT_5);
        items[5] = new SelectItem(6, EdsCompanySettings.SHORT_DATE_FORMAT_6, EdsCompanySettings.SHORT_DATE_FORMAT_6);
        items[6] = new SelectItem(7, EdsCompanySettings.SHORT_DATE_FORMAT_7, EdsCompanySettings.SHORT_DATE_FORMAT_7);
        items[7] = new SelectItem(8, EdsCompanySettings.SHORT_DATE_FORMAT_8, EdsCompanySettings.SHORT_DATE_FORMAT_8);
        items[8] = new SelectItem(9, EdsCompanySettings.SHORT_DATE_FORMAT_9, EdsCompanySettings.SHORT_DATE_FORMAT_9);
        items[9] = new SelectItem(10, EdsCompanySettings.SHORT_DATE_FORMAT_10, EdsCompanySettings.SHORT_DATE_FORMAT_10);
        items[10] = new SelectItem(11, EdsCompanySettings.SHORT_DATE_FORMAT_11, EdsCompanySettings.SHORT_DATE_FORMAT_11);
        items[11] = new SelectItem(12, EdsCompanySettings.SHORT_DATE_FORMAT_12, EdsCompanySettings.SHORT_DATE_FORMAT_12);
        items[12] = new SelectItem(13, EdsCompanySettings.SHORT_DATE_FORMAT_13, EdsCompanySettings.SHORT_DATE_FORMAT_13);
        items[13] = new SelectItem(14, EdsCompanySettings.SHORT_DATE_FORMAT_14, EdsCompanySettings.SHORT_DATE_FORMAT_14);
        items[14] = new SelectItem(15, EdsCompanySettings.SHORT_DATE_FORMAT_15, EdsCompanySettings.SHORT_DATE_FORMAT_15);
        items[15] = new SelectItem(16, EdsCompanySettings.SHORT_DATE_FORMAT_16, EdsCompanySettings.SHORT_DATE_FORMAT_16);
        items[16] = new SelectItem(17, EdsCompanySettings.SHORT_DATE_FORMAT_17, EdsCompanySettings.SHORT_DATE_FORMAT_17);
        items[17] = new SelectItem(18, EdsCompanySettings.SHORT_DATE_FORMAT_18, EdsCompanySettings.SHORT_DATE_FORMAT_18);
        return items;
    }

    public SelectItem[] getLongDateFormats() {
        SelectItem[] items = new SelectItem[30];
        items[0] = new SelectItem(1, EdsCompanySettings.LONG_DATE_FORMAT_1, EdsCompanySettings.LONG_DATE_FORMAT_1);
        items[1] = new SelectItem(2, EdsCompanySettings.LONG_DATE_FORMAT_2, EdsCompanySettings.LONG_DATE_FORMAT_2);
        items[2] = new SelectItem(3, EdsCompanySettings.LONG_DATE_FORMAT_3, EdsCompanySettings.LONG_DATE_FORMAT_3);
        items[3] = new SelectItem(4, EdsCompanySettings.LONG_DATE_FORMAT_4, EdsCompanySettings.LONG_DATE_FORMAT_4);
        items[4] = new SelectItem(5, EdsCompanySettings.LONG_DATE_FORMAT_5, EdsCompanySettings.LONG_DATE_FORMAT_5);
        items[5] = new SelectItem(6, EdsCompanySettings.LONG_DATE_FORMAT_6, EdsCompanySettings.LONG_DATE_FORMAT_6);
        items[6] = new SelectItem(7, EdsCompanySettings.LONG_DATE_FORMAT_7, EdsCompanySettings.LONG_DATE_FORMAT_7);
        items[7] = new SelectItem(8, EdsCompanySettings.LONG_DATE_FORMAT_8, EdsCompanySettings.LONG_DATE_FORMAT_8);
        items[8] = new SelectItem(9, EdsCompanySettings.LONG_DATE_FORMAT_9, EdsCompanySettings.LONG_DATE_FORMAT_9);
        items[9] = new SelectItem(10, EdsCompanySettings.LONG_DATE_FORMAT_10, EdsCompanySettings.LONG_DATE_FORMAT_10);
        items[10] = new SelectItem(11, EdsCompanySettings.LONG_DATE_FORMAT_11, EdsCompanySettings.LONG_DATE_FORMAT_11);
        items[11] = new SelectItem(12, EdsCompanySettings.LONG_DATE_FORMAT_12, EdsCompanySettings.LONG_DATE_FORMAT_12);
        items[12] = new SelectItem(13, EdsCompanySettings.LONG_DATE_FORMAT_13, EdsCompanySettings.LONG_DATE_FORMAT_13);
        items[13] = new SelectItem(14, EdsCompanySettings.LONG_DATE_FORMAT_14, EdsCompanySettings.LONG_DATE_FORMAT_14);
        items[14] = new SelectItem(15, EdsCompanySettings.LONG_DATE_FORMAT_15, EdsCompanySettings.LONG_DATE_FORMAT_15);
        items[15] = new SelectItem(16, EdsCompanySettings.LONG_DATE_FORMAT_16, EdsCompanySettings.LONG_DATE_FORMAT_16);
        items[16] = new SelectItem(17, EdsCompanySettings.LONG_DATE_FORMAT_17, EdsCompanySettings.LONG_DATE_FORMAT_17);
        items[17] = new SelectItem(18, EdsCompanySettings.LONG_DATE_FORMAT_18, EdsCompanySettings.LONG_DATE_FORMAT_18);
        items[18] = new SelectItem(19, EdsCompanySettings.LONG_DATE_FORMAT_19, EdsCompanySettings.LONG_DATE_FORMAT_19);
        items[19] = new SelectItem(20, EdsCompanySettings.LONG_DATE_FORMAT_20, EdsCompanySettings.LONG_DATE_FORMAT_20);
        items[20] = new SelectItem(21, EdsCompanySettings.LONG_DATE_FORMAT_21, EdsCompanySettings.LONG_DATE_FORMAT_21);
        items[21] = new SelectItem(22, EdsCompanySettings.LONG_DATE_FORMAT_22, EdsCompanySettings.LONG_DATE_FORMAT_22);
        items[22] = new SelectItem(23, EdsCompanySettings.LONG_DATE_FORMAT_23, EdsCompanySettings.LONG_DATE_FORMAT_23);
        items[23] = new SelectItem(24, EdsCompanySettings.LONG_DATE_FORMAT_24, EdsCompanySettings.LONG_DATE_FORMAT_24);
        items[24] = new SelectItem(25, EdsCompanySettings.LONG_DATE_FORMAT_25, EdsCompanySettings.LONG_DATE_FORMAT_25);
        items[25] = new SelectItem(26, EdsCompanySettings.LONG_DATE_FORMAT_26, EdsCompanySettings.LONG_DATE_FORMAT_26);
        items[26] = new SelectItem(27, EdsCompanySettings.LONG_DATE_FORMAT_27, EdsCompanySettings.LONG_DATE_FORMAT_27);
        items[27] = new SelectItem(28, EdsCompanySettings.LONG_DATE_FORMAT_28, EdsCompanySettings.LONG_DATE_FORMAT_28);
        items[28] = new SelectItem(29, EdsCompanySettings.LONG_DATE_FORMAT_29, EdsCompanySettings.LONG_DATE_FORMAT_29);
        items[29] = new SelectItem(30, EdsCompanySettings.LONG_DATE_FORMAT_30, EdsCompanySettings.LONG_DATE_FORMAT_30);
        return items;
    }

    private void showHideDateformat(int rowIndex, Integer typeBoxSelectedId) {
        DynamicTableItem item = dynamicTable.getItem(rowIndex);
        DataListBox type = (DataListBox) item.getColumnById("format");
        type.clear();
        if (typeBoxSelectedId != null && (typeBoxSelectedId.equals(2) || typeBoxSelectedId.equals(3))) {
            type.setItems(typeBoxSelectedId.equals(2) ? getShortDateFormats() : getLongDateFormats());
            type.setSelected(1);
            type.setEnabled(true);
        } else {
            type.setEnabled(false);
        }
    }

    private void initializeFields() {

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(event -> save());

    }

    private void save() {
        if (!validate()) {
            return;
        }
        ReportDataImportItem importItem = new ReportDataImportItem();
        importItem.setObjectID(objectID);
        importItem.setCategoryID(categoryId);

        SelectItem[] dynamicItems = new SelectItem[dynamicTable.getRowNumber()];
        int stringValue = 1, numberValue = 1, dateValue = 1;
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            Label columnName = (Label) tableItem.getColumnById("name");
            DataListBox type = (DataListBox) tableItem.getColumnById("type");
            DataListBox format = (DataListBox) tableItem.getColumnById("format");
            dynamicItems[i] = new SelectItem();
            dynamicItems[i].setId(i);
            dynamicItems[i].setName(columnName.getText());
            if (type.getSelectedId() != null) {
                dynamicItems[i].setSelectedId(type.getSelectedId());
                String referencecode;
                if (type.getSelectedId().equals(2) || type.getSelectedId().equals(3)) {
                    referencecode = "date_value" + dateValue;
                    dateValue++;
                } else if (type.getSelectedId().equals(4) || type.getSelectedId().equals(5)) {
                    referencecode = "double_value" + numberValue;
                    numberValue++;
                } else {
                    referencecode = "string_value" + stringValue;
                    stringValue++;
                }
                dynamicItems[i].setReferenceCode(referencecode);
            }
            if (type.getSelectedItem() != null) {
                dynamicItems[i].setCode(type.getSelectedItem().getDescription());
            }
            if (format.getSelectedItem() != null) {
                dynamicItems[i].setParam(format.getSelectedItem().getName());
            }

        }
        importItem.setItems(dynamicItems);

        ImportFile importFile = importItem.getImportFile();

        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(true);
        importFile.setType(ImportTypeEnum.REPORT_DATA);

        ImportFileService.App.get().addImportToQueue(importFile, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                showFailureMessage();
            }

            @Override
            public void onSuccess(String result) {
                if (result != null && !"".equals(result)) {
                    LoadingPanel.loading(false);
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    showFailureMessage(errorMessage);
                } else {
                    ReportingService.App.get().createReportXmlTemplateFromFile(importFile, new AbstractAsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            showFailureMessage();
                        }

                        @Override
                        public void onSuccess(Integer templateId) {
                            LoadingPanel.loading(false);
                            showSuccessMessage();
                            closeTab();
                            SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + templateId + "/template");
                        }
                    });
                }
            }
        });
    }

    private void showSuccessMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(WfmMessages.App.get().itemsSuccessfullyImported("Report Data"));
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
        messageBox.open();
    }

    private void showFailureMessage(final String... message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message != null && message.length > 0 ? message[0] : wfmStrings.error());
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            if (message == null || message.length == 0) {
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;

        if (errors > 0) {
            WfmWindow.alert(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
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
}
