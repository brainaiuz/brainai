package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.edatasite.workforce.gwt.accounting.client.DateFormatParseException;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountImportStatementData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountStatementTO;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountTransactionMapper;
import com.edatasite.workforce.gwt.accounting.client.rpc.MappingDto;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnsTabelWidget extends Composite {
    interface ColumnsTabelWidgetUiBinder extends UiBinder<Widget, ColumnsTabelWidget> {
    }

    private static ColumnsTabelWidget.ColumnsTabelWidgetUiBinder uiBinder = GWT.create(ColumnsTabelWidget.ColumnsTabelWidgetUiBinder.class);

    protected AccountingServiceAsync accountingServiceAsync = AccountingService.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer bankAccountAttachmentId;
    private ImportStatementWidget importStatementWidget;

    //    @UiField
//    Label transactionDate;
//    @UiField
//    Label amount;
//    @UiField
//    Label description;
//    @UiField
//    Label reference;
//    @UiField
//    Label checkNumber;
//    @UiField
//    WfmDropdown transactionDateDropdown;
//    @UiField
//    WfmDropdown transactionAmountDropdown;
//    @UiField
//    WfmDropdown transactionDescriptionDropdown;
//    @UiField
//    WfmDropdown transactionReferenceDropdown;
//    @UiField
//    WfmDropdown transactionCheckNoDropdown;
    @UiField
    FlowPanel panelBodyContainer;


    public ColumnsTabelWidget(ImportStatementWidget importStatementWidget) {
        initWidget(uiBinder.createAndBindUi(this));
        this.bankAccountAttachmentId = importStatementWidget.bankAccountAttachmentID;
        this.importStatementWidget = importStatementWidget;
        initTableData();
    }

    protected void initTableData() {
        Map<String,SelectItem> selectItemMap = new HashMap<>();
        SelectItem[] items = new SelectItem[8];
        items[0] = new SelectItem(Constants.TRANSACTION_DATE, Constants.TRANSACTION_DATE_STR);
        selectItemMap.put(Constants.TRANSACTION_DATE_STR, items[0]);
        items[1] = new SelectItem(Constants.TRANSACTION_DESCRIPTION, Constants.TRANSACTION_DESCRIPTION_STR);
        selectItemMap.put(Constants.TRANSACTION_DESCRIPTION_STR, items[1]);
        items[2] = new SelectItem(Constants.TRANSACTION_DEBIT, Constants.TRANSACTION_DEBIT_STR);
        selectItemMap.put(Constants.TRANSACTION_DEBIT_STR, items[2]);
        items[3] = new SelectItem(Constants.TRANSACTION_CREDIT, Constants.TRANSACTION_CREDIT_STR);
        selectItemMap.put(Constants.TRANSACTION_CREDIT_STR, items[3]);
        items[4] = new SelectItem(Constants.TRANSACTION_BALANCE, Constants.TRANSACTION_BALANCE_STR);
        selectItemMap.put(Constants.TRANSACTION_BALANCE_STR, items[4]);
        items[5] = new SelectItem(Constants.TRANSACTION_ACCOUNT_CODE, Constants.TRANSACTION_ACCOUNT_CODE_STR);
        selectItemMap.put(Constants.TRANSACTION_ACCOUNT_CODE_STR, items[5]);
        items[6] = new SelectItem(Constants.TRANSACTION_NAME, Constants.TRANSACTION_NAME_STR);
        selectItemMap.put(Constants.TRANSACTION_NAME_STR, items[6]);
        items[7] = new SelectItem(Constants.TRANSACTION_EXCHANGE_RATE, Constants.TRANSACTION_EXCHANGE_RATE_STR);
        selectItemMap.put(Constants.TRANSACTION_EXCHANGE_RATE_STR, items[7]);
        items[8] = new SelectItem(Constants.TRANSACTION_AMOUNT, Constants.TRANSACTION_AMOUNT_STR);
        selectItemMap.put(Constants.TRANSACTION_AMOUNT_STR, items[8]);
        items[9] = new SelectItem(Constants.TRANSACTION_REFERENCE, Constants.TRANSACTION_REFERENCE_STR);
        selectItemMap.put(Constants.TRANSACTION_REFERENCE_STR, items[9]);

        LoadingPanel.loading(true);
        accountingServiceAsync.getTransactionMapping(this.bankAccountAttachmentId, new AsyncCallback<BankAccountImportStatementData>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(BankAccountImportStatementData bankAccountImportStatementData) {
                LoadingPanel.loading(false);
                BankAccountTransactionMapper[] formMappedData = bankAccountImportStatementData.getFormMappedData();
                List<MappingDto> mappings = bankAccountImportStatementData.getMappings();
                Map<String,SelectItem> mappedItems = new HashMap<>();
                if (formMappedData != null) {
                    int rowNum = 1;
                    for (BankAccountTransactionMapper formMappedDatum : formMappedData) {
                        HTMLPanel panelBody = new HTMLPanel("div", "");
                        panelBody.setStyleName("panel__body");


                        HTMLPanel rowTag = new HTMLPanel("i", String.valueOf(rowNum));
                        rowTag.setStyleName("row-tag");

                        HTMLPanel gridRow = new HTMLPanel("div", "");
                        gridRow.setStyleName("grid-row");


                        HTMLPanel colA = createColumn(formMappedDatum.getFileColumnName(), "icon--list");


                        HTMLPanel colB = createColumn(formMappedDatum.getFileColumnValue(), "icon--view");


                        HTMLPanel colC = new HTMLPanel("div", "");
                        colC.setStyleName("col");
                        WfmDropdown dropdown = new WfmDropdown();
                        dropdown.addItems(items);
                        dropdown.addValueChangeHandler(event -> {
                            formMappedDatum.setTransactionField(dropdown.getSelectedItem().getId());
                        });
                        SelectItem selectItem = selectItemMap.get(formMappedDatum.getFileColumnName());
                        if (selectItem != null) {
                            dropdown.setSelected(selectItem.getId());
                            formMappedDatum.setTransactionField(selectItem.getId());
                        }
                        colC.add(dropdown);



                        gridRow.add(colA);
                        gridRow.add(colB);
                        gridRow.add(colC);

                        panelBody.add(gridRow);
                        panelBodyContainer.add(panelBody);

                        rowNum++;
                    }

                }

                importStatementWidget.nextButton.addClickHandler(event -> {
                    BankAccountImportStatementData importStatementData = new BankAccountImportStatementData();
                    importStatementData.setFormMappedData(formMappedData);
                    importStatementData.setMappings(mappings);
                    LoadingPanel.loading(true);

                    AccountingService.App.get().getStatementItems(bankAccountAttachmentId, new AsyncCallback<BankAccountStatementTO[]>() {
                        public void onFailure(Throwable caught) {
                            Window.alert("Error: " + caught.getMessage());
                        }

                        public void onSuccess(BankAccountStatementTO[] result) {
                            if (result == null || result.length == 0) {
                                AccountingService.App.get().saveStatements(importStatementData, new AsyncCallback<Boolean>() {
                                    public void onFailure(Throwable ex) {
                                        if (ex instanceof DateFormatParseException) {
                                            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                                            messageBox.setTitle(wfmStrings.error());
                                            messageBox.setMessage(ex.getMessage());
                                            messageBox.addCloseHandler(new CloseHandler() {
                                                @Override
                                                public void onSubmit() {
                                            /*mappingShell.close();
                                            loadFiles();*/
                                                }
                                            });
                                            messageBox.open();
                                        } else {
                                            Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                        }
                                    }

                                    public void onSuccess(Boolean result) {
                                        importStatementWidget.initSteps();
                                        importStatementWidget.loadStep(importStatementWidget.currentStep);
                                        LoadingPanel.loading(false);
//                                        LoadingPanel.loading(false);
                                    }
                                });
                            } else {
                                importStatementWidget.initSteps();
                                importStatementWidget.loadStep(importStatementWidget.currentStep);
                                LoadingPanel.loading(false);
//                                LoadingPanel.loading(false);
                            }
                        }
                    });
                });

            }
        });
    }

    private HTMLPanel createColumn(String text, String iconClass) {
        HTMLPanel col = new HTMLPanel("div", "");
        col.setStyleName("col");

        HTMLPanel uploadedColsOpt = new HTMLPanel("div", "");
        uploadedColsOpt.setStyleName("uploadedCols__opt");

        HTMLPanel iconWrapper = new HTMLPanel("div", "");
        iconWrapper.setStyleName("col-auto");

        HTMLPanel icon = new HTMLPanel("svg", "");
        icon.setStyleName(iconClass);
        iconWrapper.add(icon);

        HTMLPanel textWrapper = new HTMLPanel("div", text);
        textWrapper.setStyleName("col");

        uploadedColsOpt.add(iconWrapper);
        uploadedColsOpt.add(textWrapper);
        col.add(uploadedColsOpt);

        return col;
    }
}
