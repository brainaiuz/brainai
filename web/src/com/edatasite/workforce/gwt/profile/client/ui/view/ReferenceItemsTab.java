package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddNewListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 25.05.12
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceItemsTab extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    public static final AllInOneServiceAsync service = AllInOneService.App.get();
    private DynamicTable referenceItemsTable;
    private ArrayList<ReferenceItem> items;
    private final HashMap<Integer, String> oldNameMap = new HashMap<>();
    //    private boolean showSystemColumn = true;
    private String itemNameString = wfmStrings.name();
    private final String parentCode;

    public ReferenceItemsTab(ArrayList<ReferenceItem> children) {
        this.items = children;
        this.parentCode = children != null && !children.isEmpty() ? children.get(0).getParentCode() : null;
        initialize();
    }

    public ReferenceItemsTab(ArrayList<ReferenceItem> children, boolean showSystemColumn, String... itemNameString) {
        this.items = children;
        this.parentCode = children != null && !children.isEmpty() ? children.get(0).getParentCode() : null;
//        this.showSystemColumn = showSystemColumn;
        if (itemNameString != null && itemNameString.length > 0)
            this.itemNameString = itemNameString[0];
        initialize();
    }

    private void initialize() {
        referenceItemsTable = new DynamicTable(getColumn(), true, true, Utils.hasEitherRole(Constants.ADMIN_CODE));
        drawDefaultRows(referenceItemsTable, items);
        referenceItemsTable.addStyleName("valign-top");
        referenceItemsTable.addNewListener(new AddNewListener() {
            @Override
            public void plusClicked(int rowId) {
                addNewRow(items.size());
            }

            @Override
            public void minusClicked(final int rowId, Integer objectId) {
                if (items.get(rowId) != null) {
                    WfmMessageBox message = null;
                    if (items.get(rowId).isSystemReference()) {
                        message = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.youCannotDeleteSystemReferences());
                    } else {
                        message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.messAreDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                if (parentCode.equals(ReferenceParentEnum.POSITION_TITLES.name()) || parentCode.equals(ReferenceParentEnum.DEPARTMENT_TITLES.name())) {
                                    service.getReferenceRelatedItems(objectId,parentCode, new AsyncCallback<ArrayList<SelectItem>>() {
                                        @Override
                                        public void onFailure(Throwable caught) {}

                                        @Override
                                        public void onSuccess(ArrayList<SelectItem> result) {
                                            if (result.size() > 0){
                                                StringBuilder text = new StringBuilder();
                                                String message = wfmStrings.reference() + " " + wfmStrings.isAlreadySelected() + " " + wfmStrings.to();
                                                for (int i = 0; i < result.size(); i++) {
                                                    text.append(result.get(i));
                                                    if (result.size() > 1 && i < result.size() - 1) {
                                                        text.append(", ");
                                                    }
                                                }
                                                Info.warn(message + " -> " + text.toString());
                                            } else {
                                                deleteRow(rowId);
                                            }
                                        }
                                    });
                                } else {
                                    deleteRow(rowId);
                                }
                            }
                        });
                    }
                    message.open();
                } else {
                    Info.show(wfmStrings.systemReferenceNotDeleted(), Info.Type.WARNING);
                }
            }
        });
        initWidget(referenceItemsTable);
    }


    private void drawDefaultRows(DynamicTable referenceItemsTable, ArrayList<ReferenceItem> itemsList) {
        if (itemsList != null && !itemsList.isEmpty()) {
            for (ReferenceItem item : itemsList) {
                referenceItemsTable.addRow(item.getObjectID(), getWidget(item));
            }
        } else {
            items = new ArrayList<>();
            addNewRow(items.size());
        }
    }

    private Widget[] getWidget(ReferenceItem item) {
        if (item == null) {
            item = new ReferenceItem();
        }
        int widgetCount = 4;
        if (ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode)) {
            widgetCount = 6;
        } else if (ReferenceParentEnum._CASE_STATUS.name().equals(parentCode)
                || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode)
                || "VACANCY_STATUSES".equals(parentCode)
                || ContactListItem._CANDIDATE_STATUS.equals(parentCode)
                || "_OPPORTUNITY_SUB_STAGE".equals(parentCode)
                || "SALES_ORDER_REJECTION_REASON".equals(parentCode)
                || "SALES_QUOTE_REJECTION_REASON".equals(parentCode)
                || "_CANDIDATE_SUB_STAGE".equals(parentCode)
                || "_CANDIDATE_STATUS".equals(parentCode)) {
            widgetCount = 5;
        }

        Widget[] widgets = new Widget[widgetCount];
        TextBox name = new TextBox();
        name.ensureDebugId("onboarding_step_statuses-itemName");
        name.setText(item.getOriginalName());
        if (item.getObjectID() != null) {
            oldNameMap.put(item.getObjectID(), item.getOriginalName());
        }

        WfmButton2 locale = new WfmButton2(wfmStrings.vacancyLocale() + " ->", WfmButton2.BTN_WHITE_OUTLINE);
        locale.setEnabled(item.getObjectID() != null);
        Integer id = item.getObjectID();
        locale.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("referencelocale|localeview/" + id));
        if (items != null && items.size() > 0 && "_GOAL_SCORE1".equals(items.get(0).getCode())) {
            Validation.addNumericKeyboardListener(name);
        }
        //Opportunity stage
        TextBox stage = new TextBox();
        stage.setText(item.getTextDescription());
        Validation.addNumericKeyboardListener(stage);

        TextArea2 description = new TextArea2(400);
        description.ensureDebugId("onboarding_step_statuses-description");
        if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || "VACANCY_STATUSES".equals(parentCode) || ContactListItem._CANDIDATE_STATUS.equals(parentCode)) {
            description.setText(item.getShortName() != null ? item.getShortName() : "");
        } else if (!Utils.isNullOrEmpty(item.getTextDescription())) {
            description.setText(item.getTextDescription());
        }

        KpiCheckBox requiredComment = new KpiCheckBox();
        requiredComment.setValue(item.isRequiredComment());

        WfmButton2 roles = new WfmButton2(wfmStrings.roles() + " ->", WfmButton2.BTN_WHITE_OUTLINE);
        roles.addClickHandler(clickEvent -> new ReferencePermissionSideNavBox(id, parentCode, name.getText()));
        roles.setEnabled(item.getObjectID() != null);


        TextBox order = new TextBox();
        order.ensureDebugId("onboarding_step_statuses-order");
        Validation.addNumericKeyboardListener(order);
        order.setText(item.getOrder() != null ? item.getOrder().toString() : "");

        ColorWidget color = new ColorWidget();
        color.ensureDebugId("onboarding_step_statuses-color");
        color.setColor(item.getColorHex());

        TextBox isSystem = new TextBox();
        isSystem.setText(item.isRemovable() ? wfmStrings.no() : wfmStrings.yes());
        isSystem.setEnabled(false);

        Integer index = 0;
        widgets[index++] = name;
        widgets[index++] = locale;
        if ( ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || "VACANCY_STATUSES".equals(parentCode) || ContactListItem._CANDIDATE_STATUS.equals(parentCode)) {
            widgets[index++] = stage;
        }
        widgets[index++] = description;
        if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._CASE_STATUS.name().equals(parentCode) || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode) || "_OPPORTUNITY_SUB_STAGE".equals(parentCode) || "SALES_ORDER_REJECTION_REASON".equals(parentCode) || "SALES_QUOTE_REJECTION_REASON".equals(parentCode) || "_CANDIDATE_SUB_STAGE".equals(parentCode) || "_CANDIDATE_STATUS".equals(parentCode)) {
            widgets[index++] = requiredComment;
        }
        widgets[index++] = roles;
        widgets[index++] = order;
        widgets[index++] = color;
//        if (showSystemColumn) {
//            widgets[index++] = isSystem;
//        }\
        GWT.log("ReferenceItemsTab.getWidget() - widgets.length: " + widgets.length);
            GWT.log("Columns:" +getColumn().length);

        return widgets;
    }

    private void addNewRow(int rowId) {
        Widget[] widgets = getWidget(null);
        items.add(new ReferenceItem());
        referenceItemsTable.insertRow(rowId, widgets);
    }

    private DynamicTableColumn[] getColumn() {
        Integer index = 0;
        int widgetCount = 5;
        if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode)) {
            widgetCount = 7;
        } else if (ReferenceParentEnum._CASE_STATUS.name().equals(parentCode)
                || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode)
                || "VACANCY_STATUSES".equals(parentCode)
                || "_OPPORTUNITY_SUB_STAGE".equals(parentCode)
                || "SALES_ORDER_REJECTION_REASON".equals(parentCode)
                || "SALES_QUOTE_REJECTION_REASON".equals(parentCode)
                || "_CANDIDATE_STATUS".equals(parentCode)
                || "_CANDIDATE_SUB_STAGE".equals(parentCode)) {
            widgetCount = 6;
        }
        DynamicTableColumn[] columns = new DynamicTableColumn[widgetCount];

        columns[index] = new DynamicTableColumn(itemNameString, itemNameString, new ColumnStatements(".", "Please enter Item Name"), 300);
        columns[index++].setColumnName(itemNameString);

        columns[index++] = new DynamicTableColumn(wfmStrings.vacancyLocale(), wfmStrings.vacancyLocale(), new ColumnStatements(".", ""), 300);
        if (ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || "VACANCY_STATUSES".equals(parentCode) || ContactListItem._CANDIDATE_STATUS.equals(parentCode) || ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) ) {
            columns[index] = new DynamicTableColumn(wfmStrings.percentage(), wfmStrings.percentage(), new ColumnStatements(".", ""), 350);
            columns[index++].setColumnName(wfmStrings.percentage());
        }

        columns[index] = new DynamicTableColumn(wfmStrings.description(), wfmStrings.description(), new ColumnStatements(".", ""), 400);
        columns[index++].setColumnName(wfmStrings.description());

        if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._CASE_STATUS.name().equals(parentCode) || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode) || "_OPPORTUNITY_SUB_STAGE".equals(parentCode) || "SALES_ORDER_REJECTION_REASON".equals(parentCode) || "SALES_QUOTE_REJECTION_REASON".equals(parentCode) || "_CANDIDATE_SUB_STAGE".equals(parentCode) || "_CANDIDATE_STATUS".equals(parentCode)) {
            columns[index] = new DynamicTableColumn(wfmStrings.comment(), wfmStrings.comment(), new ColumnStatements(".", ""), 350);
            columns[index++].setColumnName(wfmStrings.comment());
        }

        columns[index] = new DynamicTableColumn(wfmStrings.roles(), "ROLES", new ColumnStatements(".", ""), 250);
        columns[index++].setColumnName(wfmStrings.roles());

        columns[index] = new DynamicTableColumn(wfmStrings.order(), wfmStrings.order(), new ColumnStatements(".", ""), 50);
        columns[index++].setColumnName(wfmStrings.order());

        columns[index] = new DynamicTableColumn(wfmStrings.color(), wfmStrings.color(), new ColumnStatements(".", ""), 200);
        columns[index++].setColumnName(wfmStrings.color());

//        if (showSystemColumn) {
//            columns[index] = new DynamicTableColumn(wfmStrings.isSystem(), wfmStrings.isSystem(), new ColumnStatements(".", ""), 50);
//            columns[index].setColumnName(wfmStrings.isSystem());
//        }
        return columns;
    }

    public int resetValidation() {
        int errors = 0;
        ArrayList<String> percentageList = new ArrayList<>();
        for (int rowId = 0; rowId < referenceItemsTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = referenceItemsTable.getItem(rowId);
            TextArea2 description = (TextArea2) tableItem.getColumnById(wfmStrings.description());
            TextBox itemName = (TextBox) tableItem.getColumnById(itemNameString);
            if (Utils.isNullOrEmpty(itemName.getText()) && !Utils.isNullOrEmpty(description.getText())) {
                referenceItemsTable.notValid(rowId, itemNameString);
                errors++;
            }
            if (ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || "VACANCY_STATUSES".equals(parentCode)) {
                TextBox stage = (TextBox) tableItem.getColumnById(wfmStrings.percentage());
                if (stage != null && !Utils.isNullOrEmpty(stage.getText())) {
                    if (percentageList != null && percentageList.contains(stage.getText())) {
                        referenceItemsTable.notValid(rowId, wfmStrings.percentage());
                        errors++;
                    } else {
                        percentageList.add(stage.getText());
                    }
                }
            }
        }
        return errors;
    }

    public ArrayList<ReferenceItem> save(ReferenceItem parent) {
        ArrayList<ReferenceItem> items = new ArrayList<>();
        for (int i = 0; i < referenceItemsTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = referenceItemsTable.getItem(i);
            TextBox itemName = (TextBox) tableItem.getColumnById(itemNameString);
            TextBox stage = null;
            if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || "VACANCY_STATUSES".equals(parentCode) || ContactListItem._CANDIDATE_STATUS.equals(parentCode)) {
                stage = (TextBox) tableItem.getColumnById(wfmStrings.percentage());
            }
            KpiCheckBox requiredComment = null;
            if (ReferenceParentEnum.RENT_ITEM_STATUS.name().equals(parentCode) || ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._CASE_STATUS.name().equals(parentCode) || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode) || "_OPPORTUNITY_SUB_STAGE".equals(parentCode) || "_CANDIDATE_SUB_STAGE".equals(parentCode) || "SALES_ORDER_REJECTION_REASON".equals(parentCode) || "SALES_QUOTE_REJECTION_REASON".equals(parentCode) || "_CANDIDATE_STATUS".equals(parentCode)) {
                requiredComment = (KpiCheckBox) tableItem.getColumnById(wfmStrings.comment());
            }
            TextArea2 description = (TextArea2) tableItem.getColumnById(wfmStrings.description());
            TextBox order = (TextBox) tableItem.getColumnById(wfmStrings.order());
            ColorWidget color = (ColorWidget) tableItem.getColumnById(wfmStrings.color());
            if (!Utils.isNullOrEmpty(itemName.getText())) {
                ReferenceItem item = new ReferenceItem();
                if (parent != null) {
                    item.setParentID(parent.getObjectID());
                    item.setParent(parent.getName());
                    item.setParentCode(parent.getCode());
                }
                item.setObjectID(tableItem.getObjectId());
                item.setName(itemName.getText());
                if (tableItem.getObjectId() != null && oldNameMap.get(tableItem.getObjectId()) != null && !oldNameMap.get(tableItem.getObjectId()).equals(itemName.getText())) {
                    item.setSelected(true);
                }
                if (stage != null) {
                    item.setDescription(stage.getText());
                    item.setShortName(description.getText());
                } else {
                    item.setDescription(description.getText());
                }
                if (requiredComment != null) {
                    item.setRequiredComment(requiredComment.getValue());
                }
                item.setColorHex(color.getColor());
                if (!Utils.isNullOrEmpty(order.getText())) {
                    try {
                        item.setOrder(Integer.valueOf(order.getText()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                items.add(item);
            }
        }
        return items;
    }

    private void deleteRow(Integer rowId){
        referenceItemsTable.deleteRow(rowId);
        items.remove(rowId);
    }
}
