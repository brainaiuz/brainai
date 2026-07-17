package com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldQuickAdd;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.CI_DEPARTMENT;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.CI_EMPLOYEE;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.CI_POSITION;

/**
 * Created by Dilshod Madrahimov on 3/17/2017.
 */
public class ItemTableSettingsDraggableView extends View implements Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Span spanTotal;
    private int totalWidth;

    private HashMap<String, ColumnConfigs> columnsMap;

    private VerticalPanel columnsVerticalPanel;
    private PickupDragController draggableController;

    private String uuid;

    private ColumnConfigs[] allColumns;
    private MaterialPanel innerContainer;
    private DataListBox sectionListBox;
    private DataListBox entityListBox;
    private DataListBox relationFieldListBox;

    private ItemTableEnum itemTableEnum;
    private CustomFieldSection section;

    private String formID;
    private String itemTableName;
    private String fieldSection;
    private boolean customizableTable = false;
    private boolean isPickList = false;

    private Div formColumn;
    private Div itemTableColumn;
    private Div entity;
    private Div relationField;
    private Heading itemTableColumnHeader;
    private Heading formColumnHeader;
    private WfmButton2 applyChangeButton;
    protected LocalizationCFModal localizationCFModal;
    private HashMap<Integer, SelectItem> cfEntityMap = new HashMap<>();

    public ItemTableSettingsDraggableView() {
        super("itemtablesettingsdraggable", accountingStrings.productTableSettings());
    }

    public ItemTableSettingsDraggableView(String[] params) {
        super("itemtablesettingsdraggable", accountingStrings.productTableSettings());
        this.formID = params[0];
        this.fieldSection = params[1];
        this.itemTableName = params[2];
        if (params.length > 2 && !params[2].isEmpty() && Boolean.parseBoolean(params[2])) {
            this.customizableTable = Boolean.parseBoolean(params[2]);
        }
    }

    @Override
    protected Widget onInitialize() {

        MaterialPanel container = new MaterialPanel();
        container.getElement().setAttribute("style", "padding:20px;border-radius:10px;background-color:white;");

        Div headerPanel = new Div("form-row");

        Div sectionColumn = new Div("col-2");
        Heading sectionHeader = new Heading(HeadingSize.H6);
        sectionHeader.setText(wfmStrings.section());

        sectionColumn.add(sectionHeader);

        formColumn = new Div("col-3");
        formColumnHeader = new Heading(HeadingSize.H6);
        formColumnHeader.setText(wfmStrings.form());
        formColumn.add(formColumnHeader);


        itemTableColumn = new Div("col-3");
        itemTableColumnHeader = new Heading(HeadingSize.H6);
        itemTableColumnHeader.setText(wfmStrings.itemTable());
        itemTableColumn.add(itemTableColumnHeader);


        entity = new Div("col-2");
        Heading entityHeader = new Heading(HeadingSize.H6);
        entityHeader.setText(wfmStrings.customField());
        entity.add(entityHeader);


        relationField = new Div("col-2");
        Heading relationFieldHeader = new Heading(HeadingSize.H6);
        relationFieldHeader.setText(wfmStrings.relatedTo());
        relationField.add(relationFieldHeader);


        headerPanel.add(sectionColumn);
        headerPanel.add(formColumn);
        headerPanel.add(itemTableColumn);
        headerPanel.add(entity);
        headerPanel.add(relationField);
        container.add(headerPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, ItemTableSettingsDraggableView.this, (sender, args) -> getData());

        this.uuid = null;
        sectionListBox = new DataListBox();
        sectionListBox.setItems(getSections());
        sectionColumn.add(sectionListBox);

        innerContainer = new MaterialPanel();
        container.add(innerContainer);

        entityListBox = new DataListBox();
        setEntityItems(entityListBox);
        entity.add(entityListBox);

        relationFieldListBox = new DataListBox();
        addSelectionHandler(entityListBox, relationFieldListBox);
        relationField.add(relationFieldListBox);

        columnsMap = new HashMap<>();
        if (!Utils.isNullOrEmpty(fieldSection) && customizableTable) {
            if (CustomFormConstants.CRM_OPPORTUNITY_STAGE_HISTORY.equals(fieldSection)) {
                sectionListBox.setSelected(getSectionByCode(ItemTableEnum.OPPORTUNITY_STAGE_HISTORY));
            } else {
                sectionListBox.setSelected(getSectionByCode(ItemTableEnum.valueOf(formID + "_" + fieldSection)));
            }
            sectionListBox.setEnabled(false);
            drawMainTable();
        } else if (!Utils.isNullOrEmpty(formID) && "OPPORTUNITY_FORM".equals(formID) && !Utils.isNullOrEmpty(fieldSection)) {
            sectionListBox.setSelected(getSectionByCode(ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM));
            drawOpportunitySection();

        } else if (!Utils.isNullOrEmpty(formID) && "HRMS_EMPLOYEE_FORM".equals(formID) && !Utils.isNullOrEmpty(fieldSection)) {
            sectionListBox.setSelected(getSectionByCode(ItemTableEnum.EMPLOYEE_CUSTOM_ITEM));
            drawOpportunitySection();

        }else if (!Utils.isNullOrEmpty(formID) && "PROJECT_FORM".equals(formID) && !Utils.isNullOrEmpty(fieldSection)) {
            sectionListBox.setSelected(getSectionByCode(ItemTableEnum.PROJECT_CUSTOM_ITEM));
            drawOpportunitySection();

        } else if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
            sectionListBox.setSelected(getSectionByCode(ItemTableEnum.CUSTOM_FORM));
            drawOpportunitySection();
        } else {
            sectionListBox.setSelected(sectionListBox.getItems()[0]);
            drawMainTable();
        }

        sectionListBox.addValueChangeHandler(clickEvent -> drawOpportunitySection());

        add(container);
        return null;
    }

    private SelectItem[] getSections() {
        LinkedList<SelectItem> sections = new LinkedList<>();
        int i = 0;
        sections.add(new SelectItem(i++, Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), ItemTableEnum.SALE_INVOICE_ITEM.getTitle(), null, CustomFieldSection.SaleInvoiceItem.name()));
        sections.add(new SelectItem(i++, accountingStrings.creditNote(), ItemTableEnum.CREDIT_NOTE_ITEM.getTitle(), null, CustomFieldSection.SaleInvoiceItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.SALE_ORDER_CODE, accountingStrings.salesOrder()), ItemTableEnum.SALE_ORDER_ITEM.getTitle(), null, CustomFieldSection.SaleOrderItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), ItemTableEnum.SALE_QUOTE_ITEM.getTitle(), null, CustomFieldSection.SaleQuoteItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), ItemTableEnum.PURCHASE_INVOICE_ITEM.getTitle(), null, CustomFieldSection.PurchaseInvoiceItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseInvoices()), ItemTableEnum.PURCHASE_INVOICE_ITEM.getTitle(), null, CustomFieldSection.PurchaseInvoiceItem.name()));
        sections.add(new SelectItem(i++, accountingStrings.debitNote(), ItemTableEnum.DEBIT_NOTE_ITEM.getTitle(), null, CustomFieldSection.PurchaseInvoiceItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), ItemTableEnum.PURCHASE_ORDER_ITEM.getTitle(), null, CustomFieldSection.PurchaseOrderItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expense()), ItemTableEnum.EXPENSE_CLAIM_ITEM.getTitle(), null, CustomFieldSection.ExpenseReportItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.Opportunities, wfmStrings.opportunity()), ItemTableEnum.OPPORTUNITY_SUB_ITEM.getTitle(), null, CustomFieldSection.OpportunitySubItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.REQUEST_FOR_PURCHASE, wfmStrings.requestForPurchase()), ItemTableEnum.RFP_ITEM.getTitle(), null, CustomFieldSection.RFPItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), ItemTableEnum.RFQ_ITEM.getTitle(), null, CustomFieldSection.RFQItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), ItemTableEnum.CLIENT_ITEM.getTitle(), null, CustomFieldSection.ClientItem.name()));
        sections.add(new SelectItem(i++, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), ItemTableEnum.SUPPLIER_ITEM.getTitle(), null, CustomFieldSection.SupplierItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.customForms(), ItemTableEnum.CUSTOM_FORM.getTitle(), null, CustomFieldSection.CustomFormItemTable.name()));
        sections.add(new SelectItem(i++, accountingStrings.opportunityCustomItemTable(), ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.OpportunityItemTable.name()));
        sections.add(new SelectItem(i++, accountingStrings.employeeCustomItemTable(), ItemTableEnum.EMPLOYEE_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.EmployeeItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.manualEntry(), ItemTableEnum.MANUAL_JOURNAL_ITEM.getTitle(), null, CustomFieldSection.ManualJournalItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.bankPayment(), ItemTableEnum.BANK_PAYMENT_ITEM.getTitle(), null, CustomFieldSection.BankPaymentItem.name()));
        sections.add(new SelectItem(i++, accountingStrings.receiveMoney(), ItemTableEnum.BANK_RECEIPT_ITEM.getTitle(), null, CustomFieldSection.BankReceiptItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.cashPayment(), ItemTableEnum.CASH_PAYMENT_ITEM.getTitle(), null, CustomFieldSection.CashPaymentItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.cashReceipt(), ItemTableEnum.CASH_RECEIPT_ITEM.getTitle(), null, CustomFieldSection.CashReceiptItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.lead(), ItemTableEnum.LEAD_ITEM.getTitle(), null, CustomFieldSection.LeadItem.name()));
        sections.add(new SelectItem(i++, accountingStrings.rentalOrder(), ItemTableEnum.RENTAL_ORDER_ITEM.getTitle(), null, CustomFieldSection.RentalOrderItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.additionalPayment(), ItemTableEnum.ADDITIONAL_PAYMENT_ITEM.getTitle(), null, CustomFieldSection.AdditionalPaymentItem.name()));
        sections.add(new SelectItem(i++, wfmStrings.placement(), ItemTableEnum.PLACEMENT_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.PlacementItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.project(), ItemTableEnum.PROJECT_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.ProjectItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.vacancy(), ItemTableEnum.VACANCY_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.VacancyItemTable.name()));
        sections.add(new SelectItem(i++, "Rotation", ItemTableEnum.ROTATION_ITEM_TABLE.getTitle(), null, CustomFieldSection.RotationItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.candidate(), ItemTableEnum.CANDIDATE_CUSTOM_ITEM.getTitle(), null, CustomFieldSection.CandidateCustomItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.workExperience(), ItemTableEnum.EXPERIENCE_ITEM_TABLE.getTitle(), null, CustomFieldSection.ExperienceItemTable.name()));
        sections.add(new SelectItem(i++, wfmStrings.picklist(), ItemTableEnum.PICKLIST.getTitle(), null, null));

        if (!Utils.isNullOrEmpty(fieldSection) && customizableTable) {
            if (CustomFormConstants.CRM_OPPORTUNITY_STAGE_HISTORY.equals(fieldSection)) {
                sections.add(new SelectItem(i, accountingStrings.opportunityStageHistory(), ItemTableEnum.OPPORTUNITY_STAGE_HISTORY.getTitle(), null, null));
            } else {
                sections.add(new SelectItem(i, fieldSection, formID + "_" + fieldSection, null, null));
            }
        }

        return sections.toArray(new SelectItem[]{});
    }

    private void setEntityItems(DataListBox entityListBox) {
        ItemTableSettingService.App.get().getItemTableEntities(formID, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                entityListBox.setItems(result);
            }
        });
    }

    private SelectItem[] getRelationItems() {
        LinkedList<SelectItem> relations = new LinkedList<>();
        relations.add(new SelectItem(CI_POSITION, EdsCFItemTableSetting.POSITION));
        relations.add(new SelectItem(CI_EMPLOYEE, EdsCFItemTableSetting.EMPLOYEE));
        relations.add(new SelectItem(CI_DEPARTMENT, EdsCFItemTableSetting.DEPARTMENT));
        return relations.toArray(new SelectItem[]{});
    }

    private void addSelectionHandler(DataListBox entityListBox, DataListBox listBox) {
        entityListBox.addValueChangeHandler(e -> {
            setRelationItems(entityListBox, listBox, null);
        });
    }

    private void setRelationItems(DataListBox entityListBox, DataListBox listBox, SelectItem item) {
        LinkedList<SelectItem> relations = new LinkedList<>();
        switch (entityListBox.getSelectedItem().getName()) {
            case EdsCFItemTableSetting.DEPARTMENT:
                listBox.clear();
                relations.clear();
                relations.add(new SelectItem(CI_EMPLOYEE, EdsCFItemTableSetting.EMPLOYEE));
                relations.add(new SelectItem(CI_POSITION, EdsCFItemTableSetting.POSITION));
                listBox.setItems(relations.toArray(new SelectItem[]{}));
                break;
            case EdsCFItemTableSetting.PROJECT:
                listBox.clear();
                relations.clear();
                relations.add(new SelectItem(CI_EMPLOYEE, EdsCFItemTableSetting.EMPLOYEE));
                listBox.setItems(relations.toArray(new SelectItem[]{}));
                break;

            case EdsCFItemTableSetting.LOCATION:
                listBox.clear();
                relations.clear();
                relations.add(new SelectItem(CI_POSITION, EdsCFItemTableSetting.POSITION));
                relations.add(new SelectItem(CI_EMPLOYEE, EdsCFItemTableSetting.EMPLOYEE));
                relations.add(new SelectItem(CI_DEPARTMENT, EdsCFItemTableSetting.DEPARTMENT));
                listBox.setItems(relations.toArray(new SelectItem[]{}));
                break;
        }
        listBox.setSelected(item);
    }

    private void drawOpportunitySection() {
        if (sectionListBox.isSomethingSelected()) {
            ItemTableEnum selectedItem = ItemTableEnum.valueOf(sectionListBox.getSelectedItem().getCode());
            formColumn.clear();
            itemTableColumn.clear();
            formColumn.add(formColumnHeader);
            itemTableColumn.add(itemTableColumnHeader);
            innerContainer.clear();
            columnsMap.clear();
            isPickList = selectedItem.equals(ItemTableEnum.PICKLIST);
            LoadingPanel.loading(true);
            if (ItemTableEnum.CUSTOM_FORM.equals(selectedItem)) {

                Map<Integer, SelectItem[]> formListMap = new HashMap<>();
                DataListBox formList = new DataListBox();
                DataListBox tableList = new DataListBox();

                formColumn.add(formList);
                itemTableColumn.add(tableList);

                ItemTableSettingService.App.get().getCustomFormItems(new AsyncCallback<ArrayList<CustomFormItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<CustomFormItem> customFormItem) {
                        LoadingPanel.loading(false);
                        if (customFormItem != null) {

                            SelectItem[] formArray = customFormItem.stream()
                                    .map(x -> new SelectItem(x.getObjectId(), x.getName()))
                                    .toArray(SelectItem[]::new);

                            if (formArray.length > 0) {
                                formList.setItems(formArray);
                            }

                            customFormItem.forEach(item -> {
                                formListMap.put(item.getObjectId(), item.getTableArray());
                                if (!Utils.isNullOrEmpty(item.getFormId()) && item.getFormId().equals(formID)) {
                                    cfEntityMap = item.getCfItemTableEntityMap().get(formID);
                                }
                            });
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                ItemTableSettingService.App.get().getCustomFormItemByFormID(formID, itemTableName, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        super.failure(throwable);
                                    }

                                    @Override
                                    public void success(ArrayList<SelectItem> result) {
                                        LoadingPanel.loading(false);
                                        if (result != null && result.size() > 0) {
                                            formList.setSelected(result.get(0).getId());
                                            relationFieldListBox.setItems(getRelationItems());
                                            entityListBox.setSelected(new SelectItem(result.get(1).getItemTableEntityId(), result.get(1).getItemTableEntity()));
                                            setRelationItems(entityListBox, relationFieldListBox, result.get(1));
                                            relationFieldListBox.setSelected(new SelectItem(result.get(1).getItemTableRelationId(), result.get(1).getItemTableRelation()));
                                            SelectItem[] tableAarray = formListMap.get(result.get(0).getId());
                                            if (tableAarray != null && tableAarray.length > 0) {
                                                tableList.setItems(tableAarray);
                                                Arrays.asList(tableAarray).forEach(item -> {
                                                    if (itemTableName.equals(item.getName())) {
                                                        tableList.setSelected(item.getId());
                                                    }
                                                });
                                                innerContainer.clear();
                                                uuid = tableList.getSelectedItem().getDescription();
                                                drawMainTable();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                formList.addValueChangeHandler(v -> {
                    tableList.clear();
                    if (formList.isSomethingSelected()) {
                        SelectItem[] tableAarray = formListMap.get(formList.getSelectedId());
                        if (tableAarray.length > 0) {
                            tableList.setItems(tableAarray);
                            tableList.clearSelected();
                        }
                    }
                });


                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        SelectItem selectItem = cfEntityMap.get(tableList.getSelectedId());
                        if (selectItem != null) {
                            if (selectItem.getItemTableEntity() == null) {
                                entityListBox.clearSelected();
                            } else {
                                entityListBox.setSelected(new SelectItem(selectItem.getItemTableEntityId(), selectItem.getItemTableEntity()));
                            }
                            if (selectItem.getItemTableRelation() == null) {
                                relationFieldListBox.clearSelected();
                            } else {
                                relationFieldListBox.setSelected(new SelectItem(selectItem.getItemTableRelationId(), selectItem.getItemTableRelation()));
                            }

                        }
                        drawMainTable();
                    }
                });

            } else if (ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM.equals(selectedItem)) {
                DataListBox tableList = new DataListBox();

                formColumn.add(tableList);

                ItemTableSettingService.App.get().getOpportunityItemTables("OPPORTUNITY_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });

            } else if (ItemTableEnum.EMPLOYEE_CUSTOM_ITEM.equals(selectedItem)) {
                DataListBox tableList = new DataListBox();
                formColumn.add(tableList);

                ItemTableSettingService.App.get().getEmployeeItemTables("HRMS_EMPLOYEE_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });

            } else if (ItemTableEnum.PLACEMENT_CUSTOM_ITEM.equals(selectedItem)) {
                DataListBox tableList = new DataListBox();

                formColumn.add(tableList);

                ItemTableSettingService.App.get().getItemTablesByFormID("PLACEMENT_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });

            } else if (ItemTableEnum.VACANCY_CUSTOM_ITEM.equals(selectedItem)) {
                DataListBox tableList = new DataListBox();

                formColumn.add(tableList);

                ItemTableSettingService.App.get().getItemTablesByFormID("VACANCY_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }
                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });

            } else if (ItemTableEnum.PROJECT_CUSTOM_ITEM.equals(ItemTableEnum.valueOf(sectionListBox.getSelectedItem().getCode()))) {
                DataListBox tableList = new DataListBox();

                formColumn.add(tableList);

                ItemTableSettingService.App.get().getProjectItemTables("PROJECT_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });
            } else if (ItemTableEnum.CANDIDATE_CUSTOM_ITEM.equals(selectedItem)) {
                DataListBox tableList = new DataListBox();

                formColumn.add(tableList);

                ItemTableSettingService.App.get().getItemTablesByFormID("CANDIDATE_FORM", new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        super.success(result);
                        LoadingPanel.loading(false);
                        if (result.length > 0) {
                            tableList.setItems(result);
                            tableList.clearSelected();
                            if (!Utils.isNullOrEmpty(formID) && !Utils.isNullOrEmpty(fieldSection)) {
                                for (SelectItem table : result) {
                                    if (fieldSection.equals(table.getName())) {
                                        tableList.setSelected(table);
                                        uuid = tableList.getSelectedItem().getDescription();
                                        drawMainTable();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                tableList.addValueChangeHandler(v -> {
                    if (tableList.isSomethingSelected()) {
                        innerContainer.clear();
                        this.uuid = tableList.getSelectedItem().getDescription();
                        drawMainTable();
                    }
                });

            } else {
                this.uuid = null;
                drawMainTable();
            }
        }
    }

    private void saveSettings(boolean validateWidth) {
        if (!validateWidth || validate()) {
            LoadingPanel.loading(true);
            applyChangeButton.setEnabled(false);
            ItemTableSettingService.App.get().saveColumnConfigsNew(itemTableEnum, getConfiguredColumns(), uuid, entityListBox.getSelectedItem(), relationFieldListBox.getSelectedItem(), new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    applyChangeButton.setEnabled(true);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    applyChangeButton.setEnabled(true);
                    LoadingPanel.loading(false);
                    if (validateWidth) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.settings()) + "!", Info.Type.INFO);
                    }
                    getData();
                }
            });
        } else {
            Info.warn("Column total width can not be more than 100%");
            applyChangeButton.setEnabled(true);
        }
    }

    private boolean validate() {
        return totalWidth == 100;
    }

    private void getData() {
        LoadingPanel.loading(true);
        ItemTableSettingService.App.get().getTableSettingsColumnConfigsNew(itemTableEnum, uuid, new AsyncCallback<ItemTableSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ItemTableSettingsItem itemTableSettingsItem) {
                LoadingPanel.loading(false);
                allColumns = itemTableSettingsItem.getAllColumns();
                drawItemTable();
            }
        });
    }

    private void drawItemTable() {
        columnsVerticalPanel.clear();
        draggableController.clearSelection();
        for (ColumnConfigs column : allColumns) {
            addNewColumn(column);
            columnsMap.put(column.getCode(), column);
        }
        calculateTotalWidth();
    }

    private void drawMainTable() {
        itemTableEnum = ItemTableEnum.valueOf(sectionListBox.getSelectedItem().getCode());
        if (!customizableTable && !isPickList) {
            section = CustomFieldSection.valueOf(sectionListBox.getSelectedItem().getCategory());
        }

        MaterialPanel pnlContainer = new MaterialPanel();

        columnsVerticalPanel = new VerticalPanel();
        AbsolutePanel draggableAbsolutePanel = new AbsolutePanel();
        draggableAbsolutePanel.addStyleName("drag-tiles--bordered");
        draggableAbsolutePanel.add(columnsVerticalPanel);

        draggableController = new PickupDragController(draggableAbsolutePanel, false);
        draggableController.setBehaviorMultipleSelection(false);
        draggableController.registerDropController(new VerticalPanelDropController(columnsVerticalPanel));

        pnlContainer.add(drawHeaderPanel());
        pnlContainer.add(draggableAbsolutePanel);
        pnlContainer.add(drawTotalWidthPanel());

        Div webhookDiv = new Div("panel-box__item");
        webhookDiv.add(new WfmButton2(wfmStrings.webHook(), WfmButton2.BTN_PRIMARY, click -> {
            if (sectionListBox.getSelectedItem() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("webhooklist|workflowWebHooks/" + sectionListBox.getSelectedItem().getCode() + "/" + uuid, sectionListBox.getSelectedItem().getName());
            }
        }));

        WfmButton2 addCustomFieldButton = new WfmButton2(wfmStrings.addCustomField(), WfmButton2.BTN_PRIMARY);
        addCustomFieldButton.getElement().setId("add_custom_field_button");
        addCustomFieldButton.addClickHandler(clickEvent -> new CustomFieldQuickAdd(formID, fieldSection, itemTableName, section, itemTableEnum, uuid, null, () -> saveSettings(false)));

        Div pnlBox = new Div("panel-box panel-box--right");
        Div pnlBoxItem = new Div("panel-box__item");
        pnlBoxItem.add(addCustomFieldButton);


        applyChangeButton = new WfmButton2(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY);
        applyChangeButton.getElement().setId("apply_change_button");
        applyChangeButton.addClickHandler(clickEvent -> saveSettings(true));
        Div pnlBoxItem2 = new Div("panel-box__item");
        pnlBoxItem2.add(applyChangeButton);

        pnlBox.add(webhookDiv);
        if (!customizableTable && !isPickList) {
            pnlBox.add(pnlBoxItem);
        }
        pnlBox.add(pnlBoxItem2);
        pnlContainer.add(pnlBox);

        getData();

        innerContainer.clear();
        innerContainer.add(pnlContainer);
    }

    private Div drawHeaderPanel() {
        Div formRowPanel = new Div("form-row");
        formRowPanel.getElement().setAttribute("style", "border-bottom:1px solid #ced5db;padding-top:20px;");
        Div fieldNameColumn = new Div("col-3");
        fieldNameColumn.add(getSpan(wfmStrings.fieldName()));

        Div widthColumn = new Div("col-1");
        Span widthSpan = getSpan(wfmStrings.width());
        widthSpan.setDisplay(Display.INLINE);
        widthColumn.add(getSpan(wfmStrings.width()));
        spanTotal = new Span();
        spanTotal.addStyleName("total_column_width");
        spanTotal.setDisplay(Display.INLINE);
        widthColumn.add(spanTotal);

        Div aliasNameColumn = new Div("col-2");
        aliasNameColumn.add(getSpan(wfmStrings.aliasName()));

        Div uiTypeColumn = new Div("col-2");
        uiTypeColumn.add(getSpan(settingsStrings.uiType()));

        Div dataTypeColumn = new Div("col-2");
        dataTypeColumn.add(getSpan(wfmStrings.dataType()));

        Div requiredColumn = new Div("col-1");
        requiredColumn.add(getSpan(wfmStrings.required()));

        Div actionColumn = new Div("col-1");
        actionColumn.add(getSpan(wfmStrings.action()));

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(widthColumn);
        formRowPanel.add(aliasNameColumn);
        formRowPanel.add(uiTypeColumn);
        formRowPanel.add(dataTypeColumn);
        formRowPanel.add(requiredColumn);
        formRowPanel.add(actionColumn);

        return formRowPanel;
    }

    private Div drawTotalWidthPanel() {
        Div formRowPanel = new Div("form-row");

        Div fieldNameColumn = new Div("col-3");
        Div widthColumn = new Div("col-1");
        Div aliasNameColumn = new Div("col-2");
        Div uiTypeColumn = new Div("col-2");
        Div dataTypeColumn = new Div("col-2");
        Div requiredColumn = new Div("col-1");
        Div actionColumn = new Div("col-1");

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(widthColumn);
        formRowPanel.add(aliasNameColumn);
        formRowPanel.add(uiTypeColumn);
        formRowPanel.add(dataTypeColumn);
        formRowPanel.add(requiredColumn);
        formRowPanel.add(actionColumn);

        return formRowPanel;
    }

    private Span getSpan(String fielName) {
        Span span = new Span(fielName);
        span.addStyleName("form-group__label");
        return span;
    }

    private ColumnConfigs[] getConfiguredColumns() {
        LinkedList<ColumnConfigs> list = new LinkedList<>();
        for (int order = 0; order < columnsVerticalPanel.getWidgetCount(); order++) {
            Div formRowPanel = (Div) columnsVerticalPanel.getWidget(order);
            Div nameColumn = (Div) formRowPanel.getWidget(0);
            Div columnContainer = (Div) nameColumn.getWidget(0);
            Div widthColumn = (Div) formRowPanel.getWidget(1);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (switcher.getValue()) {
                ColumnConfigs columnConfig = columnsMap.get(switcher.getLayoutData());
                columnConfig.setSelected(true);
                columnConfig.setOrder(order);
                try {
                    columnConfig.setWidth(Integer.valueOf(((KpiTextBox) widthColumn.getWidget(0)).getText()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    GWT.log("Error occurred while parsing column width: " + columnConfig.getTitle());
                }
                list.add(columnConfig);
            }
        }

        return list.toArray(new ColumnConfigs[0]);
    }

    private void calculateTotalWidth() {
        totalWidth = 0;
        for (int i = 0; i < columnsVerticalPanel.getWidgetCount(); i++) {
            Div formRowPanel = (Div) columnsVerticalPanel.getWidget(i);
            Div nameColumn = (Div) formRowPanel.getWidget(0);
            Div columnContainer = (Div) nameColumn.getWidget(0);
            Div widthColumn = (Div) formRowPanel.getWidget(1);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (switcher.getValue()) {
                String widthStr = ((KpiTextBox) widthColumn.getWidget(0)).getText();
                if (!Utils.isNullOrEmpty(widthStr)) {
                    totalWidth += Integer.valueOf(widthStr);
                }
            }
        }

        spanTotal.setText("(" + totalWidth + "%" + ")");
        if (totalWidth == 0) {
            spanTotal.setText("");
        } else if (totalWidth == 100) {
            spanTotal.getElement().setAttribute("style", "color:green;padding-left:15px;");
        } else if (totalWidth > 100) {
            spanTotal.getElement().setAttribute("style", "color:red;padding-left:15px;");
        } else {
            spanTotal.getElement().setAttribute("style", "color:red;padding-left:15px;");
        }
    }

    private void addNewColumn(ColumnConfigs item) {

        Div fieldNameColumn = new Div("col-3");
        Div widthColumn = new Div("col-1");
        Div aliasNameColumn = new Div("col-2");
        Div uiTypeColumn = new Div("col-2");
        Div dataTypeColumn = new Div("col-2");
        Div requiredColumn = new Div("col-1");
        Div actionColumn = new Div("col-1");

        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(item.getCode());
        switcher.setValue(item.isSelected());
        switcher.setEnabled(true);

        Div formRowPanel = new Div("form-row" + (switcher.getValue() ? " state-on" : " state-off"));


        KpiTextBox widthTextBox = new KpiTextBox(InputType.NUMBER);
        if (item.getWidth() != null) {
            widthTextBox.setText(String.valueOf(item.getWidth()));
        }
        widthTextBox.addValueChangeHandler(event -> calculateTotalWidth());
        widthColumn.add(widthTextBox);

        HTML columnTitle = new HTML(item.getTitle());
        columnTitle.setStyleName("drag-tile__text");

        //Custom Field Data
        if (item.getCompanyCustomFieldID() != null) {
            MaterialLink aliasNameLink = new MaterialLink(item.getAliasName());
            aliasNameLink.addClickHandler(clickEvent -> new CustomFieldQuickAdd(section, itemTableEnum, uuid, item.getCompanyCustomFieldID(), () -> saveSettings(false)));
            aliasNameColumn.add(aliasNameLink);

            HTML uiType = new HTML(item.getUiType());
            uiType.setStyleName("drag-tile__text");
            uiTypeColumn.add(uiType);

            HTML dateType = new HTML(item.getDataType());
            dateType.setStyleName("drag-tile__text");
            dataTypeColumn.add(dateType);

        } else {
            HTML aliasNameLink = new HTML(item.getAliasName() != null ? item.getAliasName() : item.getCode());

            if (!customizableTable || item.isClickable()) {
                aliasNameLink.addStyleName("uploadLinkStyle2");
                aliasNameLink.addClickHandler(clickEvent -> {
                    saveSettings(false);
                    new ItemTableSystemFieldQuickAdd(section, item, itemTableEnum);
                });
            } else {
                aliasNameLink.removeStyleName("uploadLinkStyle2");
            }

            aliasNameColumn.add(aliasNameLink);

            HTML uiType = new HTML("System");
            uiType.setStyleName("drag-tile__text");
            uiTypeColumn.add(uiType);

            HTML dateType = new HTML("System");
            dateType.setStyleName("drag-tile__text");
            dataTypeColumn.add(dateType);
        }

        HTML required = new HTML(item.isRequired() ? wfmStrings.yes() : wfmStrings.no());
        required.setStyleName("drag-tile__text");
        requiredColumn.add(required);

        if (item.getCompanyCustomFieldID() != null) {
            MenuBar menuBar = new MenuBar(true);
            menuBar.setAutoOpen(true);

            MenuPopItem localization = new MenuPopItem(wfmStrings.localization());
            localization.ensureDebugId("localization-button");
            localization.setCommand(() -> {
                localizationCFModal = new LocalizationCFModal(item.getCompanyCustomFieldID(), LocalizationTypeEnum.FIELD);
                localizationCFModal.center();
            });
            menuBar.addItem(localization);

            MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
            delete.setCommand(() -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(settingsStrings.areYouSureWantRemoveCustomField());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                ProfileService.App.get().deleteCustomField(item.getCompanyCustomFieldID(), null, new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        throwable.printStackTrace();
                                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void success(Void aVoid) {
                                        getData();
                                        Scheduler.get().scheduleFixedDelay(() -> {
                                            LoadingPanel.loading(false);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, null, null);
                                            return false;
                                        }, 100);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    }
                });
                messageBox.open();
            });
            menuBar.addItem(delete);

            final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(2);
            toolItem.setWidget(menuBar);
            actionColumn.add(toolItem.getAction());
        }

        Div pnlColumn = new Div("drag-tile drag-tile--sm");
        Div pnlGrip = new Div("drag-tile__grip");

        Div pnlAction = new Div("drag-tile__actions");
        pnlAction.add(switcher);
        switcher.addValueChangeHandler(vh -> {
            if (switcher.getValue()) {
                formRowPanel.removeStyleName("state-off");
                formRowPanel.addStyleName("state-on");
            } else if (item.isRequired()) {
                if (ItemTableConstants.ACCOUNT.equals(item.getCode()) && !item.hasDefault()) {
                    switcher.setValue(true);
                    Info.warn(accountingStrings.youCannotDisableRequiredAccountWithoutDefaultValue());
                } else if (!ItemTableConstants.ACCOUNT.equals(item.getCode())) {
                    switcher.setValue(true);
                    Info.warn(wfmStrings.youCannotDisableRequiredField());
                }
            } else {
                formRowPanel.removeStyleName("state-on");
                formRowPanel.addStyleName("state-off");
            }
            calculateTotalWidth();
        });

        pnlColumn.add(pnlGrip);
        pnlColumn.add(columnTitle);
        pnlColumn.add(pnlAction);
        pnlColumn.setLayoutData(switcher);
        fieldNameColumn.add(pnlColumn);

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(widthColumn);
        formRowPanel.add(aliasNameColumn);
        formRowPanel.add(uiTypeColumn);
        formRowPanel.add(dataTypeColumn);
        formRowPanel.add(requiredColumn);
        formRowPanel.add(actionColumn);

        columnsVerticalPanel.add(formRowPanel);
        draggableController.makeDraggable(formRowPanel, pnlGrip);
    }

    private SelectItem getSectionByCode(ItemTableEnum itemTableEnum) {
        for (SelectItem item : getSections()) {
            if (itemTableEnum.getTitle().equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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
