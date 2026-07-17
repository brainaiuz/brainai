package com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;


public class ItemTableSystemFieldQuickAdd extends KpiSideNavBox {

    private static NumberFormat qtyNumberFormat = NumberFormat.getFormat(",##0.00");
    private WfmButton2 save;
    private String type;
    private CustomFieldSection section;
    private ItemTableEnum itemTableEnum;
    private ColumnConfigs columnConfigs;
    private SelectItem accountValue;
    private SelectItem discountValue;
    private TextBox name;
    private TextBox minValue;
    private TextBox aliasName;
    private KpiSwitcher required;
    private KpiSwitcher disabled;
    private AccountsLookUp accountsLookUp;
    private DataListBox dcBox;
    private MultiTable showTo;
    private MultiTable showToView;
    private MultiTable allowEdit;
    private String oldTitleName;
    private ArrayList<SelectItem> rolesList = new ArrayList<>();
    private ArrayList<SelectItem> rolesListView = new ArrayList<>();


    public ItemTableSystemFieldQuickAdd(CustomFieldSection section, ColumnConfigs columnConfigs, ItemTableEnum itemTableEnum) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.section = section;
        this.columnConfigs = columnConfigs;
        this.itemTableEnum = itemTableEnum;
        this.oldTitleName = columnConfigs.getTitle();

        if (CustomFieldSection.SaleInvoiceItem.equals(section)
                || CustomFieldSection.SaleQuoteItem.equals(section)
                || CustomFieldSection.SaleOrderItem.equals(section)) {
            type = RECEIVABLE;
        } else {
            type = PAYABLE;
        }
        InvoiceService.App.get().getItemTableDefaultAccount(section, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(SelectItem result) {
                super.success(result);
                accountValue = result;
                InvoiceService.App.get().getItemTableDefaultDiscount(section, new AbstractAsyncCallback<SelectItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void success(SelectItem result) {
                        discountValue = result;
                        initialize();
                    }
                });


            }
        });
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.properties());
        addHeader(header);

        FlowPanel panel = new FlowPanel();
        name = new TextBox();
        name.setText(columnConfigs.getTitle());
        FormGroup nameFormGroup = new FormGroup(wfmStrings.name(), name);
        panel.add(nameFormGroup);

        aliasName = new TextBox();
        aliasName.setEnabled(false);
        aliasName.setText(columnConfigs.getCode());
        FormGroup aliasNameFormGroup = new FormGroup(wfmStrings.aliasName(), aliasName);
        panel.add(aliasNameFormGroup);


        MultiTableWidgets multiTableWidgetsDisabled = new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.addStyleName(Constants.DEFAULT_WIDTH);
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
        allowEdit = new MultiTable(multiTableWidgetsDisabled);
        Command allowEditCommand = () -> setRoleItemsToListBox(allowEdit, false);
        allowEdit.setOnLinesAdded(allowEditCommand);

        FormGroup allowEditFormGroup = new FormGroup(wfmStrings.allowEdit(), allowEdit);
        allowEditFormGroup.setVisible(false);

        required = new KpiSwitcher();
        required.setValue(columnConfigs.isRequired());
        FormGroup requiredFormGroup = new FormGroup(wfmStrings.mandatory(), required);

        disabled = new KpiSwitcher();
        disabled.setValue(columnConfigs.isDisabled());
        FormGroup disabledFormGroup = new FormGroup(wfmStrings.disabled(), disabled);

        switch (itemTableEnum) {
            case SALE_INVOICE_ITEM:
            case SALE_QUOTE_ITEM:
            case SALE_ORDER_ITEM:
            case PURCHASE_ORDER_ITEM:
            case PURCHASE_INVOICE_ITEM:
                allowEditFormGroup.setVisible(disabled.getValue());
                disabled.addValueChangeHandler((valueChangeEvent) -> {
                    if (disabled.getValue()) {
                        allowEditFormGroup.setVisible(true);
                    } else {
                        allowEditFormGroup.setVisible(false);
                    }
                });
        }
        panel.add(new GRow(new GColumn(GColumnEnum.COL_6, requiredFormGroup), new GColumn(GColumnEnum.COL_6, disabledFormGroup)));
        panel.add(allowEditFormGroup);

        minValue = new TextBox();
        minValue.setText(formatQty(columnConfigs.getMinValue() != null ? columnConfigs.getMinValue() : BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(minValue, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, true);
        FormGroup minValueFormGroup = new FormGroup(wfmStrings.minValue(), minValue);
        minValueFormGroup.setVisible(false);
        if ((CustomFieldSection.OpportunitySubItem.equals(section) || CustomFieldSection.SaleOrderItem.equals(section)) && (ItemTableConstants.QTY.equals(columnConfigs.getCode()) || ItemTableConstants.UNITPRICE.equals(columnConfigs.getCode()))) {
            panel.add(minValueFormGroup);
            if (required.getValue()) {
                minValueFormGroup.setVisible(true);
            }

            required.addValueChangeHandler(handlar -> {
                if (required.getValue()) {
                    minValueFormGroup.setVisible(true);
                } else {
                    minValueFormGroup.setVisible(false);
                }
            });
        }
        if ("ACCOUNT".equals(columnConfigs.getCode())) {
            accountsLookUp = new AccountsLookUp(type);
            if (accountValue != null) {
                accountsLookUp.setSelected(accountValue);
            }

            FormGroup formGroup = new FormGroup(wfmStrings.defaultAccount(), accountsLookUp);
            panel.add(formGroup);
        }
        if ("DISCOUNT_LIST".equals(columnConfigs.getCode())){
            dcBox =  new DataListBox();
            final ArrayList<DiscountItem> dlist = new ArrayList<>();
            dlist.add(new DiscountItem(Constants.ONE_OFF_DISCOUNT, wfmStrings.percentage()));
            dlist.add(new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, wfmStrings.fixedAmount()));
            dcBox.setItems(dlist.toArray(new SelectItem[]{}));
            if (discountValue != null) {
                dcBox.setSelected(discountValue);
            }

            FormGroup formGroup = new FormGroup(wfmStrings.defaultValue(), dcBox);
            panel.add(formGroup);
        }

        MultiTableWidgets multiTableWidgets = new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.addStyleName(Constants.DEFAULT_WIDTH);
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
        showTo = new MultiTable(multiTableWidgets);
        Command command = () -> setRoleItemsToListBox(showTo, false);
        showTo.setOnLinesAdded(command);

        MultiTableWidgets multiTableWidgetsView = new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.addStyleName(Constants.DEFAULT_WIDTH);
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };

        showToView = new MultiTable(multiTableWidgetsView);
        Command commandView = () -> setRoleItemsToListBox(showToView, true);
        showToView.setOnLinesAdded(commandView);

        ProfileService.App.get().getRoles(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                rolesList = result;
                rolesListView = result;
                setRoleItemsToListBox(allowEdit, false);
                setRoleItemsToListBox(showTo, false);
                setRoleItemsToListBox(showToView, true);
                if (columnConfigs.getAllowedRolesDisabled() != null && !columnConfigs.getAllowedRolesDisabled().isEmpty()) {
                    allowEdit.removeAllRows();
                    allowEdit.getWidgetsMaps().clear();
                    for (Integer roleID : columnConfigs.getAllowedRolesDisabled()) {
                        allowEdit.onAddLinkClicked();
                    }
                    for (int i = 0; i < columnConfigs.getAllowedRolesDisabled().size(); i++) {
                        ((DataListBox) allowEdit.getWidgetsMaps().get(i).getWidgets()[0]).setSelected(columnConfigs.getAllowedRolesDisabled().get(i));
                    }
                }
                if (columnConfigs.getAllowedRoles() != null && !columnConfigs.getAllowedRoles().isEmpty()) {
                    showTo.removeAllRows();
                    showTo.getWidgetsMaps().clear();
                    for (Integer roleID : columnConfigs.getAllowedRoles()) {
                        showTo.onAddLinkClicked();
                    }
                    for (int i = 0; i < columnConfigs.getAllowedRoles().size(); i++) {
                        ((DataListBox) showTo.getWidgetsMaps().get(i).getWidgets()[0]).setSelected(columnConfigs.getAllowedRoles().get(i));
                    }
                }
                if (columnConfigs.getAllowedRolesView() != null && !columnConfigs.getAllowedRolesView().isEmpty()) {
                    showToView.removeAllRows();
                    showToView.getWidgetsMaps().clear();
                    for (Integer roleID : columnConfigs.getAllowedRolesView()) {
                        showToView.onAddLinkClicked();
                    }
                    for (int i = 0; i < columnConfigs.getAllowedRolesView().size(); i++) {
                        ((DataListBox) showToView.getWidgetsMaps().get(i).getWidgets()[0]).setSelected(columnConfigs.getAllowedRolesView().get(i));
                    }
                }
            }
        });

        boolean invoiceTableMandatory = ("ACCOUNT".equals(columnConfigs.getCode()) || "PRODUCT".equals(columnConfigs.getCode()) ||
                "QTY".equals(columnConfigs.getCode()) || "UNITPRICE".equals(columnConfigs.getCode()) ||
                "WAREHOUSE".equals(columnConfigs.getCode()));

        switch (itemTableEnum) {
            case SALE_INVOICE_ITEM:
            case SALE_QUOTE_ITEM:
            case SALE_ORDER_ITEM:
            case PURCHASE_ORDER_ITEM:
            case PURCHASE_INVOICE_ITEM:
                if (invoiceTableMandatory) {
                    required.setValue(true);
                    required.setEnabled(false);
                    break;
                } else {
                    FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                    panel.add(formGroup);
                    break;
                }
            case EXPENSE_CLAIM_ITEM:
                if ("ACCOUNT_LIST".equals(columnConfigs.getCode()) || "UNITS".equals(columnConfigs.getCode()) || "COST".equals(columnConfigs.getCode())) {
                    required.setValue(true);
                    required.setEnabled(false);
                    break;
                } else {
                    FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                    panel.add(formGroup);
                    break;
                }
            case RFQ_ITEM:
            case CLIENT_ITEM:
            case SUPPLIER_ITEM:
                if ("PRODUCT".equals(columnConfigs.getCode())) {
                    required.setValue(true);
                    required.setEnabled(false);
                    break;
                } else {
                    FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                    panel.add(formGroup);
                    break;
                }
            case RFP_ITEM:
                if ("PRODUCT".equals(columnConfigs.getCode())) {
                    required.setValue(true);
                    required.setEnabled(false);
                    break;
                } else if ("QTY_ON_HAND".equals(columnConfigs.getCode())) {
                    required.setValue(false);
                    required.setEnabled(false);

                    FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                    panel.add(formGroup);
                    break;
                } else {
                    FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                    panel.add(formGroup);
                    break;
                }
            case CLIENT_FORM_ATTACHMENTS:
            case PRODUCT_ATTACHMENTS:
                disabledFormGroup.setVisible(false);
                break;
            default:
                FormGroup formGroup = new FormGroup(wfmStrings.visibleTo(), showTo);
                panel.add(formGroup);
                break;
        }

        FormGroup formGroup = new FormGroup(wfmStrings.visibleTo() + " (" + wfmStrings.summaryView() + ")", showToView);
        panel.add(formGroup);

        addBody(panel);


        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(event -> {
            if (!validate()) {
                return;
            }
            columnConfigs.setTitle(name.getText());
            columnConfigs.setCode(columnConfigs.getCode());
            columnConfigs.setSelected(required != null && required.getValue());
            columnConfigs.setDisabled(disabled != null && disabled.getValue());
            columnConfigs.setMinValue(required.getValue() && minValue.getText() != null ? parseToBigDecimal(minValue.getText()) : null);

            ArrayList<Integer> roles = null;
            if (!showTo.getWidgetsMaps().isEmpty()) {
                roles = new ArrayList<>();
                for (WidgetsMap widgetsMap : showTo.getWidgetsMaps()) {
                    if (widgetsMap.getWidgets() != null && widgetsMap.getWidgets().length > 0) {
                        for (Widget widget : widgetsMap.getWidgets()) {
                            if (((DataListBox) widget).isSomethingSelected()) {
                                roles.add(((DataListBox) widget).getSelectedId());
                            }
                        }
                    }
                }
            }
            roles = roles.isEmpty() ? null : roles;
            columnConfigs.setAllowedRoles(roles);

            ArrayList<Integer> rolesView = null;
            if (!showToView.getWidgetsMaps().isEmpty()) {
                rolesView = new ArrayList<>();
                for (WidgetsMap widgetsMap : showToView.getWidgetsMaps()) {
                    if (widgetsMap.getWidgets() != null && widgetsMap.getWidgets().length > 0) {
                        for (Widget widget : widgetsMap.getWidgets()) {
                            if (((DataListBox) widget).isSomethingSelected()) {
                                rolesView.add(((DataListBox) widget).getSelectedId());
                            }
                        }
                    }
                }
            }
            rolesView = rolesView.isEmpty() ? null : rolesView;
            columnConfigs.setAllowedRolesView(rolesView);

            if (disabled.getValue()) {
                ArrayList<Integer> rolesDisabled = null;
                if (!allowEdit.getWidgetsMaps().isEmpty()) {
                    rolesDisabled = new ArrayList<>();
                    for (WidgetsMap widgetsMap : allowEdit.getWidgetsMaps()) {
                        if (widgetsMap.getWidgets() != null && widgetsMap.getWidgets().length > 0) {
                            for (Widget widget : widgetsMap.getWidgets()) {
                                if (((DataListBox) widget).isSomethingSelected()) {
                                    rolesDisabled.add(((DataListBox) widget).getSelectedId());
                                }
                            }
                        }
                    }
                }
                rolesDisabled = rolesDisabled.isEmpty() ? null : rolesDisabled;
                columnConfigs.setAllowedRolesDisabled(rolesDisabled);
            }

            InvoiceService.App.get().saveItemTableItems(columnConfigs, accountsLookUp != null && accountsLookUp.getSelectedItemID() != null ? accountsLookUp.getSelectedItemID() : null,dcBox != null  && dcBox.getSelectedItem() != null ? dcBox.getSelectedId() : null, oldTitleName, itemTableEnum, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Void result) {
                    super.success(result);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.itemTable()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, null, null);
                    remove();
                }
            });
        });
        addFooter(save);

        show();
    }

    public String formatQty(BigDecimal bigDecimal) {
        return qtyNumberFormat.format(bigDecimal.setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            return new BigDecimal(Utils.universalParse(qtyNumberFormat, text)).setScale(5, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public boolean validate() {
        if (!Validation.validateTextBoxRequired(name)) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setRoleItemsToListBox(MultiTable showTo, boolean fromView) {
        for (int i = 0; i < showTo.getWidgetsMaps().size(); i++) {
            for (Widget widget : showTo.getWidgetsMaps().get(i).getWidgets()) {
                DataListBox listBox = (DataListBox) widget;
                listBox.clear();
                if (fromView) {
                    listBox.setItems(rolesListView.toArray(new SelectItem[]{}));
                } else {
                    listBox.setItems(rolesList.toArray(new SelectItem[]{}));
                }
            }
        }
    }
}
