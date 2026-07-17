package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountAppliesItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountMultiRangeItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroupAppend;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 6:45:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditDiscountView extends CustomForm implements Constants, Colapse {

    private static final AccountingUtils utils = AccountingUtils.get();
    private static final DiscountServiceAsync discountService = DiscountService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final SelectItem TYPE_PERCENTAGE = new SelectItem(ONE_OFF_DISCOUNT, wfmStrings.percentage());
    private static final SelectItem TYPE_FIXED_AMOUNT = new SelectItem(Constants.ONE_OFF_FIXED_AMOUNT, wfmStrings.fixedAmount());

    private TextBox txtCode;
    private TextBox txtName;
    private TextArea2 txtDescription;
    private KpiSwitcher isActiveCheckBox;
    private DataListBox discountType;
    private DataListBox multiRangeType;
    private FormGroup multiRangeTypeField;
    private DataListBox simpleDiscountType;
    private TextBox simpleDiscountTxt;
    private Div simpleDiscountUnit;
    private FormGroup simpleDiscountField;
    private FormGroup multiDiscountField;
    private MultiTableNewUI multiRangeDiscountTable;

    private KpiRadioButton applyToProduct;
    private KpiRadioButton applyToClient;
    private VerticalPanel productsPanel;
    private VerticalPanel clientsPanel;
    private Tree productTree;
    private CustomerLookUpItem clientsTree;

    private WfmButton2 saveButton;

    private DiscountItem discountItem;
    private ExtendedCommand provider;
    private Command closeCommand;
    private Integer objectID;
    private Integer productID;
    private KpiModal box;
    private final String editDiscountView = "edit_discount_view_";

    public EditDiscountView() {
        super("adddiscount", accountingStrings.addDiscount());
    }

    public EditDiscountView(ExtendedCommand provider, Integer productID, Command closeCommand, KpiModal box) {
        super("adddiscount", accountingStrings.addDiscount());
        this.provider = provider;
        this.productID = productID;
        this.closeCommand = closeCommand;
        this.box = box;
    }

    public EditDiscountView(Integer objectID) {
        super("edit", accountingStrings.editDiscount());
        this.objectID = objectID;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        discountService.getDiscountData(objectID, new AsyncCallback<DiscountItem>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(DiscountItem item) {
                LoadingPanel.loading(false);
                discountItem = item;
                setValues();
            }
        });
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.DISCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, (e) -> {
            setEnabledButtons(false);
            save();
        });
        saveButton.ensureDebugId("Discount_savebutton_id");


        if (box != null) {
            footer.removeFromParent();
            box.addButton(saveButton);

        } else {
            addButton(saveButton);
        }
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        if (closeCommand != null) {
        }
        txtCode = new TextBox();
        txtCode.ensureDebugId(editDiscountView + "txtCode");

        txtName = new TextBox();
        txtName.ensureDebugId(editDiscountView + "txtName");

        txtDescription = new TextArea2();
        txtDescription.ensureDebugId(editDiscountView + "txtDescription");

        isActiveCheckBox = new KpiSwitcher();
        isActiveCheckBox.setOffLabel(wfmStrings.isActive());
        isActiveCheckBox.ensureDebugId(editDiscountView + "isActiveCheckBox");
        isActiveCheckBox.setValue(Boolean.TRUE);

        discountType = new DataListBox();
        discountType.ensureDebugId(editDiscountView + "discountType");
        discountType.setWithoutNullLabel(true);
        discountType.setItems(new SelectItem[]{new SelectItem(SIMPLE_DISCOUNT, accountingStrings.simpleDiscount()), new SelectItem(MULTI_RANGE_DISCOUNT, accountingStrings.multiRangeDiscount())});
        discountType.addValueChangeHandler(changeEvent -> {
            if (discountType.getSelectedId().equals(MULTI_RANGE_DISCOUNT)) {
                multiDiscountField.setVisible(true);
                simpleDiscountField.setVisible(false);
                multiRangeTypeField.setVisible(true);
            } else {
                simpleDiscountField.setVisible(true);
                multiRangeTypeField.setVisible(false);
                multiDiscountField.setVisible(false);
            }
        });

        simpleDiscountType = new DataListBox();
        simpleDiscountType.setWithoutNullLabel(true);
        simpleDiscountType.addListItem(TYPE_PERCENTAGE);
        simpleDiscountType.addListItem(TYPE_FIXED_AMOUNT);
        simpleDiscountType.addValueChangeHandler(event -> {
            simpleDiscountTxt.setText(AccountingUtils.discountNumberFormat.format(0));
            simpleDiscountUnit.setVisible(TYPE_PERCENTAGE.equals(simpleDiscountType.getSelectedItem()));
        });
        simpleDiscountType.setSelected(TYPE_PERCENTAGE);

        simpleDiscountUnit = new InputGroupAppend(new Span("%"), true);

        simpleDiscountTxt = new TextBox();
        simpleDiscountTxt.setText(AccountingUtils.discountNumberFormat.format(0));
        simpleDiscountTxt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(simpleDiscountTxt, Utils.getAccountingDiscountScale());
        Validation.checkToFocusTextBox(simpleDiscountTxt, AccountingUtils.discountNumberFormat.format(0));
        simpleDiscountTxt.addKeyUpHandler(event -> {
            if (TYPE_PERCENTAGE.equals(simpleDiscountType.getSelectedItem())) {
                BigDecimal discountAmount = BigDecimal.ZERO;
                try {
                    discountAmount = utils.parseToBigDecimal(simpleDiscountTxt.getText(), AccountingUtils.discountNumberFormat);
                } catch (NumberFormatException ex) {
                    simpleDiscountTxt.setText(AccountingUtils.getDiscountZero());
                } finally {
                    if (discountAmount.compareTo(utils.HUNDRED) > 0) {
                        simpleDiscountTxt.setText(simpleDiscountTxt.getText().substring(0, AccountingUtils.getDiscountScale()));
                    }
                }
            }
        });


        InputGroup simpleDiscount = new InputGroup(simpleDiscountType, simpleDiscountTxt, simpleDiscountUnit);
        simpleDiscountField = new FormGroup(accountingStrings.discountType(), simpleDiscount);

        multiRangeType = new DataListBox();
        multiRangeType.ensureDebugId(editDiscountView + "multiRangeType");
        multiRangeType.setWithoutNullLabel(true);
        multiRangeType.setItems(new SelectItem[]{new SelectItem(Constants.QUANTITY, wfmStrings.qty()), new SelectItem(TOTAL_PURCHASE_AMOUNT, accountingStrings.totalPurchaseAmount())});
        multiRangeType.setSelected(Constants.QUANTITY);

        multiRangeTypeField = new FormGroup(accountingStrings.multiRangeDiscountType(), multiRangeType);
        multiRangeTypeField.setVisible(false);

        multiRangeDiscountTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiRangeWidgets(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        multiDiscountField = new FormGroup("&nbsp;", multiRangeDiscountTable);
        multiDiscountField.setVisible(false);

        applyToProduct = new KpiRadioButton("group");
        applyToProduct.addStyleName("mb-3");
        applyToProduct.ensureDebugId("producty-radioButton");
        applyToProduct.setText(wfmStrings.products());
        applyToProduct.setValue(true);

        applyToClient = new KpiRadioButton("group");
        applyToClient.addStyleName("mb-3");
        applyToClient.setText(wfmStrings.customers());
        applyToClient.ensureDebugId("clients-radioButton");
        applyToClient.setValue(false);

        productTree = new Tree(new Tree.Resources() {
            public ImageResource treeClosed() {
                return DocumentImages.get().getTreeClosed();
            }

            public ImageResource treeLeaf() {
                return DocumentImages.get().groups();
            }

            public ImageResource treeOpen() {
                return DocumentImages.get().getTreeOpen();
            }
        });
        productTree.ensureDebugId(editDiscountView + "productTree");
        productTree.setVisible(provider == null);

        clientsTree = new CustomerLookUpItem();
        clientsTree.ensureDebugId(editDiscountView + "clientTree");

        clientsTree.setHeight(200);
        clientsTree.setVisible(provider == null);

        productsPanel = new VerticalPanel();
        productsPanel.setSpacing(5);
        productsPanel.addStyleName("wg_tree-table-2");
        productsPanel.add(applyToProduct);
        productsPanel.add(productTree);

        clientsPanel = new VerticalPanel();
        clientsPanel.add(applyToClient);
        clientsPanel.add(clientsTree);

        addTitleField(TITLE, objectID != null ? accountingStrings.editDiscount() : accountingStrings.addDiscount());
        addField(CustomFormConstants.CODE, txtCode, wfmStrings.code());
        addField(NAME, txtName, wfmStrings.name());
        addField(DESCRIPTION, txtDescription, wfmStrings.description());
        addField(CustomFormConstants.ACTIVE, isActiveCheckBox);
        addField(TYPE, discountType, wfmStrings.type());
        addField(SIMPLE_DISCOUNT_TYPE, simpleDiscountField);
        addField(MULTI_RANGE_DISCOUNT_TYPE, multiRangeTypeField);
        addField(DISCOUNT_PANEL, multiDiscountField);

        addTitleField(APPLY_TO, accountingStrings.appliesTo());
        addField("CLIENTS", clientsPanel);
        addField("PRODUCTS", productsPanel);

        show();
        return null;
    }

    private void setValues() {
        txtCode.setValue(discountItem.getCode());
        txtName.setValue(discountItem.getName());
        txtDescription.setText(discountItem.getDescription());

        if (discountItem.getPercentage() != null) {
            simpleDiscountType.setSelected(TYPE_PERCENTAGE);
            simpleDiscountTxt.setText(AccountingUtils.discountNumberFormat.format(discountItem.getPercentage()));
            simpleDiscountUnit.setVisible(true);
        } else if (discountItem.getFixedAmount() != null) {
            simpleDiscountType.setSelected(TYPE_FIXED_AMOUNT);
            simpleDiscountTxt.setText(AccountingUtils.discountNumberFormat.format(discountItem.getFixedAmount()));
            simpleDiscountUnit.setVisible(false);
        }
        if (discountItem.getType() != null) {
            discountType.setSelected(discountItem.getType());
            if (discountItem.getType().equals(MULTI_RANGE_DISCOUNT)) {
                simpleDiscountField.setVisible(false);

                multiRangeTypeField.setVisible(true);
                multiRangeType.setSelected(discountItem.getMultiRangeDiscountType());
                multiDiscountField.setVisible(true);
                if (discountItem.getMultiRangeItems() != null && discountItem.getMultiRangeItems().length > 0) {
                    multiRangeDiscountTable.removeAllRows();
                    for (DiscountMultiRangeItem item : discountItem.getMultiRangeItems()) {
                        multiRangeDiscountTable.addWidgets(getMultiRangeWidgets(item));
                    }
                }
            } else {
                simpleDiscountField.setVisible(true);
                multiRangeTypeField.setVisible(false);
                multiDiscountField.setVisible(false);
            }
        } else {
            discountType.setSelected(SIMPLE_DISCOUNT);
        }

        initProductList();
        initClientList();
    }

    private void initProductList() {
        for (DiscountAppliesItem appliesItem : discountItem.getProductList()) {
            final KpiCheckBox categorBox = new KpiCheckBox(appliesItem.getName());
            final TreeItem category = productTree.addItem(categorBox);
            categorBox.addClickHandler(clickEvent -> {
                Boolean itemValue = categorBox.getValue();
                for (int i = 0; i < category.getChildCount(); i++) {
                    TreeItem childItem = category.getChild(i);
                    ((KpiCheckBox) childItem.getWidget()).setValue(itemValue);
                }
            });

            if (appliesItem.getItems() != null) {
                appliesItem.getItems();
                for (SelectItem product : appliesItem.getItems()) {
                    final KpiCheckBox checkBox = new KpiCheckBox(product.getName());
                    checkBox.setName(product.getId().toString());
                    category.addItem(checkBox);
                    if (discountItem.getAppliedProductIDs() != null && discountItem.getAppliedProductIDs().length > 0) {
                        boolean isSelectParent = false;
                        for (Integer productID : discountItem.getAppliedProductIDs()) {
                            if (productID.equals(product.getId())) {
                                checkBox.setValue(true);
                                if (!isSelectParent) {
                                    ((KpiCheckBox) category.getWidget()).setValue(true);
                                    isSelectParent = true;
                                }
                            }
                        }
                    }
                    checkBox.addClickHandler(clickEvent -> {
                        Boolean isChecked = checkBox.getValue();
                        if (isChecked) {
                            ((KpiCheckBox) category.getWidget()).setValue(true);
                        } else {
                            isChecked = false;
                            for (int i = 0; i < category.getChildCount(); i++) {
                                TreeItem childItem = category.getChild(i);
                                if (((KpiCheckBox) childItem.getWidget()).getValue()) {
                                    isChecked = true;
                                }
                            }
                            if (!isChecked) {
                                ((KpiCheckBox) category.getWidget()).setValue(false);
                            }
                        }
                    });
                }
            }
        }
    }

    private void initClientList() {
        if (discountItem.getAppliedClients() != null && discountItem.getAppliedClients().length > 0) {
            clientsTree = new CustomerLookUpItem(Design.CHECK, true, discountItem.getAppliedClients());

            clientsTree.setHeight(200);
            clientsTree.initialize();
            applyToClient.setValue(true);
        } else {
            applyToProduct.setValue(true);
            clientsTree.removeItems();
            clientsTree.initialize();
        }

        clientsPanel.clear();
        clientsPanel.add(applyToClient);
        clientsPanel.add(clientsTree);

    }

    private void setEnabledButtons(boolean b) {
        if (saveButton != null) {
            saveButton.setEnabled(b);
        }
    }

    private WidgetsMap getMultiRangeWidgets(DiscountMultiRangeItem multiRangeItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        if (multiRangeItem == null) {
            multiRangeItem = new DiscountMultiRangeItem();
        }

        TextBox txtFrom = new TextBox();
        txtFrom.setPlaceHolder(wfmStrings.from());
        txtFrom.setWidth(MIN_DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(txtFrom, AccountingUtils.calculationScale);

        if (multiRangeItem.getFromAmount() != null) {
            txtFrom.setValue(utils.formatPrice(multiRangeItem.getFromAmount()));
        } else if (multiRangeItem.getFromQty() != null) {
            txtFrom.setValue(multiRangeItem.getFromQty().toString());
        }

        TextBox txtTo = new TextBox();
        txtTo.setWidth(MIN_DEFAULT_WIDTH);
        txtTo.setPlaceHolder(wfmStrings.to());
        Validation.addNumericKeyboardListener(txtTo, AccountingUtils.calculationScale);

        if (multiRangeItem.getToAmount() != null) {
            txtTo.setValue(utils.formatPrice(multiRangeItem.getToAmount()));
        } else if (multiRangeItem.getToQty() != null) {
            txtTo.setValue(multiRangeItem.getToQty().toString());
        }
        TextBox txtAmount = new TextBox();
        Div unit = InputGroup.wrapIntoGroupText(new Span("%"));

        DataListBox type = new DataListBox();
        type.setWidth(MIN_DEFAULT_WIDTH);
        type.setWithoutNullLabel(true);
        type.addListItem(TYPE_PERCENTAGE);
        type.addListItem(TYPE_FIXED_AMOUNT);
        type.addValueChangeHandler(event -> {
            txtAmount.setText(AccountingUtils.discountNumberFormat.format(0));
            unit.setVisible(TYPE_PERCENTAGE.equals(type.getSelectedItem()));
        });
        type.setSelected(TYPE_PERCENTAGE);

        txtAmount.setText(AccountingUtils.discountNumberFormat.format(0));
        txtAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtAmount, Utils.getAccountingDiscountScale());
        Validation.checkToFocusTextBox(txtAmount, AccountingUtils.discountNumberFormat.format(0));
        txtAmount.addKeyUpHandler(event -> {
            if (TYPE_PERCENTAGE.equals(type.getSelectedItem())) {
                BigDecimal discountAmount = BigDecimal.ZERO;
                try {
                    discountAmount = utils.parseToBigDecimal(txtAmount.getText(), AccountingUtils.discountNumberFormat);
                } catch (NumberFormatException ex) {
                    txtAmount.setText(AccountingUtils.getDiscountZero());
                } finally {
                    if (discountAmount.compareTo(utils.HUNDRED) > 0) {
                        txtAmount.setText(txtAmount.getText().substring(0, AccountingUtils.getDiscountScale()));
                    }
                }
            }
        });

        if (multiRangeItem.getPercentage() != null) {
            type.setSelected(TYPE_PERCENTAGE);
            txtAmount.setText(AccountingUtils.discountNumberFormat.format(multiRangeItem.getPercentage()));
            unit.setVisible(true);
        } else if (multiRangeItem.getFixedAmount() != null) {
            type.setSelected(TYPE_FIXED_AMOUNT);
            txtAmount.setText(AccountingUtils.discountNumberFormat.format(multiRangeItem.getFixedAmount()));
            unit.setVisible(false);
        }

        widgetsMap.addToCenter("from", txtFrom);
        widgetsMap.addToCenter("to", txtTo);
        widgetsMap.addToCenter("type", type);
        widgetsMap.addToCenter("amount", txtAmount);
        widgetsMap.addToRight("", unit);

        return widgetsMap;
    }

    private void save() {
        if (validate()) {
            discountItem = getDiscountData();
            LoadingPanel.loading(true);
            discountService.save(discountItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    setEnabledButtons(true);
                    LoadingPanel.loading(false);
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(Integer result) {
                    setEnabledButtons(true);
                    LoadingPanel.loading(false);
                    if (result != null && result > 0) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.discount()), Info.Type.INFO);
                        if (provider != null) {
                            provider.execute(result);
                        }
                        if (closeCommand != null) {
                            closeCommand.execute();
                        } else {
                            closeTab();
                        }
                        discountItem.setId(result);
                        if (closeCommand == null) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DISCOUNT_SAVED, discountItem, null);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DOUBLE_DISCOUNT_SAVED, discountItem, null);
                        }
                    } else {
                        Info.show(accountingStrings.discountCouldNotSaved(), Info.Type.INFO);
                    }
                }
            });
        } else {
            setEnabledButtons(true);
        }
    }

    private DiscountItem getDiscountData() {
        discountItem.setId(objectID);
        discountItem.setCode(txtCode.getValue());
        discountItem.setName(txtName.getValue());
        discountItem.setDescription(txtDescription.getText());
        discountItem.setActive(isActiveCheckBox.getValue());
        discountItem.setType(discountType.getSelectedId());

        if (TYPE_PERCENTAGE.equals(simpleDiscountType.getSelectedItem())) {
            discountItem.setPercentage(utils.parseToBigDecimal(simpleDiscountTxt.getText(), AccountingUtils.discountNumberFormat));
        } else if (TYPE_FIXED_AMOUNT.equals(simpleDiscountType.getSelectedItem())) {
            discountItem.setFixedAmount(utils.parseToBigDecimal(simpleDiscountTxt.getText(), AccountingUtils.discountNumberFormat));
        }

        if (discountType.getSelectedId() != null && discountType.getSelectedId().equals(MULTI_RANGE_DISCOUNT)) {
            discountItem.setMultiRangeItems(getmMultiRangeData());
        } else {
            discountItem.setMultiRangeItems(null);
        }

        discountItem.setAppliedProductIDs(getSelectedProducts());
        discountItem.setAppliedClients(getSelectedClients());

        return discountItem;
    }

    private DiscountMultiRangeItem[] getmMultiRangeData() {

        List<DiscountMultiRangeItem> multiRangeItems = new ArrayList<>();

        for (WidgetsMap map : multiRangeDiscountTable.getWidgetsMaps()) {

            DiscountMultiRangeItem multiRangeItem = new DiscountMultiRangeItem();
            multiRangeItem.setType(multiRangeType.getSelectedId());

            TextBox from = (TextBox) map.getWidget("from");
            TextBox to = (TextBox) map.getWidget("to");
            DataListBox type = (DataListBox) map.getWidget("type");
            TextBox amount = (TextBox) map.getWidget("amount");

            if (multiRangeItem.getType().equals(TOTAL_PURCHASE_AMOUNT)) {
                multiRangeItem.setFromAmount((from.getText() != null && !from.getText().isEmpty()) ? utils.parseToBigDecimal(from.getText()) : null);
                multiRangeItem.setToAmount((to.getText() != null && !to.getText().isEmpty()) ? utils.parseToBigDecimal(to.getText()) : null);
            } else {
                multiRangeItem.setFromQty((from.getText() != null && !from.getText().isEmpty()) ? Integer.valueOf(from.getText()) : null);
                multiRangeItem.setToQty((to.getText() != null && !to.getText().isEmpty()) ? Integer.valueOf(to.getText()) : null);
            }

            if (TYPE_PERCENTAGE.equals(type.getSelectedItem())) {
                multiRangeItem.setPercentage((amount.getText() != null && !amount.getText().isEmpty()) ? utils.parseToBigDecimal(amount.getText(), AccountingUtils.discountNumberFormat) : null);
            } else {
                multiRangeItem.setFixedAmount((amount.getText() != null && !amount.getText().isEmpty()) ? utils.parseToBigDecimal(amount.getText(), AccountingUtils.discountNumberFormat) : null);
            }
            multiRangeItems.add(multiRangeItem);
        }

        return multiRangeItems.toArray(new DiscountMultiRangeItem[]{});
    }

    private Integer[] getSelectedProducts() {
        List<Integer> returnItems = new ArrayList<>();
        if (applyToProduct.getValue()) {
            if (productTree.getItemCount() > 0) {
                for (int i = 0; i < productTree.getItemCount(); i++) {
                    TreeItem parent = productTree.getItem(i);
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        KpiCheckBox item = (KpiCheckBox) parent.getChild(j).getWidget();
                        if (item.getValue()) {
                            returnItems.add(Integer.valueOf(item.getName()));
                        }
                    }
                }
            }
            if (productID != null && !returnItems.contains(productID)) {
                returnItems.add(productID);
            }
        }
        return returnItems.toArray(new Integer[]{});
    }

    private SelectItem[] getSelectedClients() {
        List<SelectItem> appliedClients = new ArrayList<>();
        if (applyToClient.getValue()) {
            appliedClients = clientsTree.getSelectItems();
        }
        return appliedClients.toArray(new SelectItem[]{});
    }

    private Boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(txtCode)) {
            errors++;
        }

        if (!Validation.validateTextBoxRequired(txtName)) {
            errors++;
        }

        if (!Validation.validateListBoxRequired(discountType)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (discountType.getSelectedId().equals(MULTI_RANGE_DISCOUNT)) {
            for (WidgetsMap map : multiRangeDiscountTable.getWidgetsMaps()) {
                TextBox from = (TextBox) map.getWidget("from");
                TextBox to = (TextBox) map.getWidget("to");

                if (!Validation.validateTextBoxRequired(from) || !Validation.validateTextBoxRequired(to)) {
                    Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                    return false;
                }

                BigDecimal fromValue = utils.parseToBigDecimal(from.getText());
                BigDecimal toValue = utils.parseToBigDecimal(to.getText());

                if (fromValue.compareTo(toValue) > 0) {
                    Utils.scrollIntoView(from.getElement());
                    Info.show(accountingStrings.fromCantBeLessThanTo(), Info.Type.WARNING);
                    from.addStyleName("x-form-invalid");
                    to.addStyleName("x-form-invalid");
                    return false;
                }
            }
        }

        return true;
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
}
