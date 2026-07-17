package com.edatasite.workforce.gwt.invoice.client.ui.view.expense;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.importfile.client.rpc.CustomExpenseImportItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by Khasan on 14.08.14.
 */
public class CustomExpenseImportView extends View {

    private DataListBox firstName;
    private DataListBox lastName;
    private DataListBox expenseDate;
    private DataListBox reportTitle;
    private DataListBox description;
    private DataListBox supplier;
    private DataListBox relatedProject;
    private DataListBox approver;
    private DataListBox categoryItem;
    private DataListBox descriptionItem;
    private DataListBox unitsItem;
    private DataListBox costUnitsItem;
    private DataListBox taxItem;
    private DataListBox purchaseOrder;
    private DataListBox currency;
    private DataListBox exchangeRate;
    private KpiCheckBox hasHeader;
    private WfmButton2 saveButton;

    private WfmForm.Field firstNameField;
    private WfmForm.Field lastNameField;
    private WfmForm.Field expenseNumberField;
    private WfmForm.Field expenseDateField;
    private WfmForm.Field reportTitleField;
    private WfmForm.Field descriptionField;
    private WfmForm.Field supplierField;
    private WfmForm.Field unitsField;
    private WfmForm.Field relatedProjectField;
    private WfmForm.Field approverField;
    private WfmForm.Field categoryItemField;
    private WfmForm.Field descriptionItemField;
    private WfmForm.Field unitsItemField;
    private WfmForm.Field costUnitsItemField;
    private WfmForm.Field taxItemField;
    private WfmForm.Field purchaseOrderField;
    private WfmForm.Field currencyField;
    private WfmForm.Field exchangeRateField;

    private final Integer objectID;
    private final String importExpenseView = "import_expense_view_";
    private char defaultSeparator = ',';
    private boolean isCompanyExpense;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public CustomExpenseImportView(Integer objectID) {
        super("add", wfmStrings.importExpense());
        this.objectID = objectID;
    }

    public CustomExpenseImportView(Integer objectID, boolean isCompanyExpense) {
        super("add", wfmStrings.importExpense());
        this.objectID = objectID;
        this.isCompanyExpense = isCompanyExpense;
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

        WfmForm table = new WfmForm(new String[]{"7%", "100%", "25%"});
        table.setLabelSize("150px");

        firstNameField = table.addField(wfmStrings.firstName(), firstName, !isCompanyExpense);
        lastNameField = table.addField(wfmStrings.lastName(), lastName, !isCompanyExpense);
        expenseDateField = table.addField(wfmStrings.date(), expenseDate, true);
        reportTitleField = table.addField(wfmStrings.report() + " " + wfmStrings.title(), reportTitle, true);
        descriptionField = table.addField(wfmStrings.report() + " " + wfmStrings.description(), description);
//        descriptionField.setVisible(false);
        supplierField = table.addField(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), supplier);
        relatedProjectField = table.addField(Property.get(Constants.PROJECT, wfmStrings.relatedSupplier(), wfmStrings.project()), relatedProject);
        approverField = table.addField(wfmStrings.approver(), approver, true);
        categoryItemField = table.addField(wfmStrings.category(), categoryItem, isCompanyExpense);
        descriptionItemField = table.addField(wfmStrings.descriptionItem(), descriptionItem);
        unitsItemField = table.addField(wfmStrings.units(), unitsItem);
//        unitsItemField.setVisible(false);
        costUnitsItemField = table.addField(wfmStrings.costPerUnit(), costUnitsItem, true);
        taxItemField = table.addField(wfmStrings.tax(), taxItem);
        taxItemField.setVisible(false);
        purchaseOrderField = table.addField(wfmStrings.purchaseorder(), purchaseOrder);
        purchaseOrderField.setVisible(Utils.hasGenericAccess(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE));
        currencyField = table.addField(wfmStrings.currency(), currency);
        exchangeRateField = table.addField(wfmStrings.exchangeRate(), exchangeRate);

        table.addField(wfmStrings.myCSVFileHasHeaders(), hasHeader, true);
        table.addButton(saveButton);

        AccountingService.App.get().getCSVColumns(objectID, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
            }

            public void success(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    SelectItem[] listItems = null;
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        listItems = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(listItems);
                    LoadingPanel.loading(false);
                });
            }
        });

        add(table);

        return null;
    }

    private void setItems(SelectItem[] items) {
        firstName.setItems(items, firstNameField);
        lastName.setItems(items, lastNameField);
        expenseDate.setItems(items, expenseDateField);
        reportTitle.setItems(items, reportTitleField);
        description.setItems(items, descriptionField);
        supplier.setItems(items, supplierField);
        relatedProject.setItems(items, relatedProjectField);
        approver.setItems(items, approverField);
        categoryItem.setItems(items, categoryItemField);
        descriptionItem.setItems(items, descriptionItemField);
        unitsItem.setItems(items, unitsItemField);
        costUnitsItem.setItems(items, costUnitsItemField);
        taxItem.setItems(items, taxItemField);
        purchaseOrder.setItems(items, purchaseOrderField);
        currency.setItems(items, currencyField);
        exchangeRate.setItems(items, exchangeRateField);
    }

    private void initializeFields() {
        firstName = new DataListBox();
        firstName.ensureDebugId(importExpenseView + "firstName");

        lastName = new DataListBox();
        lastName.ensureDebugId(importExpenseView + "lastName");

        expenseDate = new DataListBox();
        expenseDate.ensureDebugId(importExpenseView + "date");

        reportTitle = new DataListBox();
        reportTitle.ensureDebugId(importExpenseView + "title");

        description = new DataListBox();
        description.ensureDebugId(importExpenseView + "description");

        supplier = new DataListBox();
        supplier.ensureDebugId(importExpenseView + "supplier");

        relatedProject = new DataListBox();
        relatedProject.ensureDebugId(importExpenseView + "relatedProject");

        approver = new DataListBox();
        approver.ensureDebugId(importExpenseView + "approver");

        categoryItem = new DataListBox();
        categoryItem.ensureDebugId(importExpenseView + "categoryItem");

        descriptionItem = new DataListBox();
        descriptionItem.ensureDebugId(importExpenseView + "descriptionItem");

        unitsItem = new DataListBox();
        unitsItem.ensureDebugId(importExpenseView + "unitsItem");

        costUnitsItem = new DataListBox();
        costUnitsItem.ensureDebugId(importExpenseView + "costUnitsItem");

        taxItem = new DataListBox();
        taxItem.ensureDebugId(importExpenseView + "taxItem");

        purchaseOrder = new DataListBox();
        purchaseOrder.ensureDebugId(importExpenseView + "purchaseOrder");

        currency = new DataListBox();
        currency.ensureDebugId(importExpenseView + "currency");

        exchangeRate = new DataListBox();
        exchangeRate.ensureDebugId(importExpenseView + "exchangeRate");

        taxItem = new DataListBox();
        taxItem.ensureDebugId(importExpenseView + "taxItem");

        hasHeader = new KpiCheckBox("");
        hasHeader.setValue(true);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(event -> save());

        firstName.addStyleName(DEFAULT_WIDTH);
        lastName.addStyleName(DEFAULT_WIDTH);
        expenseDate.addStyleName(DEFAULT_WIDTH);
        reportTitle.addStyleName(DEFAULT_WIDTH);
        description.addStyleName(DEFAULT_WIDTH);
        supplier.addStyleName(DEFAULT_WIDTH);
        relatedProject.addStyleName(DEFAULT_WIDTH);
        approver.addStyleName(DEFAULT_WIDTH);
        categoryItem.addStyleName(DEFAULT_WIDTH);
        descriptionItem.addStyleName(DEFAULT_WIDTH);
        unitsItem.addStyleName(DEFAULT_WIDTH);
        costUnitsItem.addStyleName(DEFAULT_WIDTH);
        taxItem.addStyleName(DEFAULT_WIDTH);
        purchaseOrder.addStyleName(DEFAULT_WIDTH);
        currency.addStyleName(DEFAULT_WIDTH);
        exchangeRate.addStyleName(DEFAULT_WIDTH);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        CustomExpenseImportItem importItem = new CustomExpenseImportItem();
        importItem.setObjectID(objectID);
        importItem.setFirstName(firstName.getSelectedId());
        importItem.setLastName(lastName.getSelectedId());
        importItem.setExpenseDate(expenseDate.getSelectedId());
        importItem.setReportTitle(reportTitle.getSelectedId());
        importItem.setDescription(description.getSelectedId());
        importItem.setSupplier(supplier.getSelectedId());
        importItem.setRelatedProject(relatedProject.getSelectedId());
        importItem.setApprover(approver.getSelectedId());
        importItem.setCategoryItem(categoryItem.getSelectedId());
        importItem.setDescriptionItem(descriptionItem.getSelectedId());
        importItem.setUnitsItem(unitsItem.getSelectedId());
        importItem.setCostUnitsItem(costUnitsItem.getSelectedId());
        importItem.setTaxItem(taxItem.getSelectedId());
        importItem.setPurchaseOrder(purchaseOrder.getSelectedId());
        importItem.setCurrency(currency.getSelectedId());
        importItem.setExchangeRate(exchangeRate.getSelectedId());
        importItem.setCompanyExpense(isCompanyExpense);

        ImportFile importFile = importItem.getImportFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader.getValue());
        importFile.setType(isCompanyExpense ? ImportTypeEnum.COMPANY_EXPENSE : ImportTypeEnum.EXPENSE);

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
            }
        });
    }

    private void showSuccessMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(WfmMessages.App.get().itemsSuccessfullyImported(wfmStrings.expenseClaims()));
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
        if (!isCompanyExpense) {
            if (!Validation.validateListBoxRequired(firstName, firstNameField, wfmStrings.pleaseSelect())) {
                errors++;
            }
            if (!Validation.validateListBoxRequired(lastName, lastNameField, wfmStrings.pleaseSelect())) {
                errors++;
            }
        }
        if (!Validation.validateListBoxRequired(expenseDate, expenseDateField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(reportTitle, reportTitleField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!isCompanyExpense) {
            if (!Validation.validateListBoxRequired(categoryItem, categoryItemField, wfmStrings.pleaseSelect())) {
                errors++;
            }
        }
        if (!Validation.validateListBoxRequired(costUnitsItem, costUnitsItemField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(approver, approverField, wfmStrings.pleaseSelect())) {
            errors++;
        }

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
