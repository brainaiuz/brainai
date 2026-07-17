package com.edatasite.workforce.gwt.accounting.client.ui.view.consignment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.DateBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Normurod on 1/22/15.
 */
public class ConsignmentAddEditView extends View implements AccountingConstants, AccountingCustomFormConstants, Constants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    public static final int DEFAULT_ROWS = 5;
    private final Integer objectID;

    private DateBox datePicker;
    private Numbering numberData;
    private TextBox nameTextBox;
    private TextBox referenceTextBox;

    private WfmButton2 saveButton;
    private WfmButton2 cancel;

    private EditableTable itemsTable;
    private EditableGrid grid;


    private HashMap<String, Widget> widgetsMap;
    private HashMap<String, BigDecimal> productQtyMap;
    private HashMap<String, BigDecimal> allocatedProductQtyMap;
    private HashMap<String, String> productMap;

    private final String addConsignmentView = "addConsignmentView_";

    private Consignment consignment;

    public ConsignmentAddEditView(Integer objectID) {
        super((objectID != null ? "edit" : "add"), wfmStrings.consignments());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        widgetsMap = new HashMap<>();
        productQtyMap = new HashMap<>();
        allocatedProductQtyMap = new HashMap<>();
        productMap = new HashMap<>();

        nameTextBox = new TextBox();
        nameTextBox.ensureDebugId(addConsignmentView + "nameTextBox");

        referenceTextBox = new TextBox();
        referenceTextBox.ensureDebugId(addConsignmentView + "referenceTextBox");

        datePicker = new DateBox();
        datePicker.ensureDebugId(addConsignmentView + "availableFrom");
        datePicker.setFormat(new DateBox.DefaultFormat(DateUtils.getFormatInternal()));
        datePicker.setValue(new Date());

        numberData = new Numbering();
        numberData.getTxtNumber().setMaxLength(4);
        numberData.ensureDebugId(addConsignmentView + "numberDate");
        numberData.setWidth(MIN_DEFAULT_WIDTH);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("saveAndClose-button");
        saveButton.addClickHandler(event -> {
            setButtonEnable(false);

            if (!validate()) {
                setButtonEnable(true);
                return;
            }
            saveTransaction();
        });
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.ensureDebugId("cancel-button");
        cancel.addClickHandler(event -> closeView());

        MaterialPanel buttonsPanel = new MaterialPanel("btns-group");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancel);
        MainLayout.get().addToActionsContainer(buttonsPanel);
        MainLayout.get().makeFrameContainerHaveTabsStyle(true);

        createItemTable();
        initWidgetsMap();
        loadData();

        return null;
    }

    private void createItemTable() {
        itemsTable = new EditableTable(getColumns(), true, true);
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
            }
        });
        grid = itemsTable.getGrid();
    }

    private Object[] getWidgets(ConsignmentItem consignmentItem) {
        Integer index = 0;
        Object[] objects = new Object[4];

        final CRMLookUp fromCompany = new CRMLookUp(CRMLookUp.CLIENT_ID);
        fromCompany.setShowHeadOffice(true);
        if (consignmentItem != null && consignmentItem.getFromCompany() != null) {
            fromCompany.addItem(consignmentItem.getFromCompany());
        }

        final CRMLookUp toCompany = new CRMLookUp(CRMLookUp.CLIENT_ID);
        toCompany.setShowHeadOffice(true);
        if (consignmentItem != null && consignmentItem.getToCompany() != null) {
            toCompany.addItem(consignmentItem.getToCompany());
        }

        final ProductLookUp product = new ProductLookUp(Constants.RECEIVABLE);
        if (consignmentItem != null && consignmentItem.getProduct() != null) {
            product.addItem(consignmentItem.getProduct());
        }

        final ExtendedTextBox quantity = new ExtendedTextBox();
        Validation.addNumericKeyboardListener(quantity);
        quantity.addKeyUpHandler(keyUpEvent -> calculate());

        if (consignmentItem != null && consignmentItem.getQuantity() != null) {
            quantity.setText(AccountingUtils.get().formatQty(consignmentItem.getQuantity()));
        }

        product.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (product.getSelectedItemID() != null) {
                if (fromCompany.getSelectedItemID() == null) {
                    Info.show( wfmStrings.pleaseSelectFormCompanyChoosingTemplate(), Info.Type.WARNING);
                    product.clear();
                } else {
                    ConsignmentItem item = new ConsignmentItem();
                    item.setFromCompany(fromCompany.getSelectedItem());
                    item.setToCompany(toCompany.getSelectedItem());
                    item.setProduct(product.getSelectedItem());

                    onChangeProduct(fromCompany.getSelectedItemID(), product.getSelectedItemID(), itemsTable.getGrid().getCurrentRow(), item);
                }
            }
        });
        objects[index++] = fromCompany;
        objects[index++] = toCompany;
        objects[index++] = product;
        objects[index] = quantity;
        return objects;
    }

    private void onChangeProduct(Integer clientID, Integer productID, final Integer position, final ConsignmentItem consignmentItem) {
        final String key = clientID + "_" + productID;
        if (productQtyMap.get(key) != null) {
            BigDecimal allocatedQty = allocatedProductQtyMap.get(key) != null ? allocatedProductQtyMap.get(key) : BigDecimal.ZERO;
            BigDecimal remainingQty = productQtyMap.get(key).subtract(allocatedQty);

            if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                consignmentItem.setQuantity(remainingQty);
                setRowData(consignmentItem, position);
                calculate();
            }

        } else {
            LoadingPanel.loading(true);
            ConsignmentService.App.get().getAvailableStock(clientID, productID, objectID, new AsyncCallback<BigDecimal>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(BigDecimal qty) {
                    LoadingPanel.loading(false);
                    productQtyMap.put(key, qty);

                    if (qty.compareTo(BigDecimal.ZERO) > 0) {
                         consignmentItem.setQuantity(qty);
                        setRowData(consignmentItem, position);
                        calculate();
                    }
                }
            });
        }
    }

    private void calculate() {
        allocatedProductQtyMap.clear();
        productMap.clear();

        for (int i = 0; i < grid.getRowCount(); i++) {

            CRMLookUp fromCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.from());
            CRMLookUp toCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.to());
            ProductLookUp product = (ProductLookUp) itemsTable.getColumnById(i, wfmStrings.product());
            ExtendedTextBox quantity = (ExtendedTextBox) itemsTable.getColumnById(i, wfmStrings.qty());

            if (fromCompany.getSelectedItemID() != null && product.getSelectedItemID() != null) {
                String key = fromCompany.getSelectedItemID() + "_" + product.getSelectedItemID();

                allocatedProductQtyMap.merge(key, AccountingUtils.get().parseToBigDecimal(quantity.getText()), BigDecimal::add);
                productMap.put(key, product.getSelectedItem().getName());
            }
        }
    }

    private void setRowData(ConsignmentItem object, Integer position) {
        itemsTable.addRow(position, getWidgets(object));
    }

    private ColumnConfig[] getColumns() {
        Integer index = 0;
        ColumnConfig[] columns = new ColumnConfig[4];

        columns[index] = new ColumnConfig(LookUpCell.class, wfmStrings.from() , 300, true);
        columns[index].setTitle(wfmStrings.from() + " " + wfmStrings.company());

        columns[++index] = new ColumnConfig(LookUpCell.class, wfmStrings.to(), 300, true);
        columns[index].setTitle(accountingStrings.toCompany());

        columns[++index] = new ColumnConfig(LookUpCell.class, wfmStrings.product(), 250, true);
        columns[index].setTitle(wfmStrings.product());

        columns[++index] = new ColumnConfig(CustomCell.class, wfmStrings.qty(), 100, false);
        columns[index].setTitle(wfmStrings.qty());

        return columns;
    }

    class ExtendedTextArea extends TextArea implements CustomCellInterface {

        public ExtendedTextArea() {
            super();
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    public class ExtendedTextBox extends TextBox implements CustomCellInterface {
        public ExtendedTextBox() {
            super();
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    private void initWidgetsMap() {

        HTML nameLabel = new HTML(wfmStrings.name());
        nameLabel.setStyleName(STYLE_LABEL);

        HTML referenceLabel = new HTML(wfmStrings.reference());
        referenceLabel.setStyleName(STYLE_LABEL);

        HTML dateLabel = new HTML(wfmStrings.date());
        dateLabel.setStyleName(STYLE_LABEL);

        HTML numberLabel = new HTML(wfmStrings.number());
        numberLabel.setStyleName(STYLE_LABEL);

        datePicker.setStyleName(STYLE_DATE_PICKER);
        numberData.getTxtPrefix().setWidth("43px");
        numberData.getTxtPrefix().setStyleName(STYLE_EXPENSE_PREFIX);
        numberData.getTxtNumber().setStyleName(STYLE_EXPENSE_NUMBER);

        //set labels
        widgetsMap.put(LABEL_NAME, nameLabel);
        widgetsMap.put(LABEL_DATE, dateLabel);
        widgetsMap.put(LABEL_REFERENCE, referenceLabel);
        widgetsMap.put(LABEL_NUMBER, numberLabel);

        // set inputs
        widgetsMap.put(INPUT_NAME, nameTextBox);
        widgetsMap.put(INPUT_DATE, datePicker);
        widgetsMap.put(INPUT_REFERENCE, referenceTextBox);
        widgetsMap.put(INPUT_NUMBER, numberData);

        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
    }

    private void loadData() {
        LoadingPanel.loading(true);
        ConsignmentService.App.get().getConsignmentData(objectID, new AsyncCallback<Consignment>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(Consignment result) {
                LoadingPanel.loading(false);
                consignment = result;

                add(new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer());

                if (consignment.getItems() == null) {
                    ConsignmentItem item = new ConsignmentItem();
                    ConsignmentItem[] items = new ConsignmentItem[1];
                    item.setQuantity(BigDecimal.ZERO);
                    items[0] = item;
                    consignment.setItems(items);
                }
                setFormData();
            }
        });
    }


    public void setFormData() {
        nameTextBox.setText(consignment.getName());
        referenceTextBox.setText(consignment.getReference());

        if (consignment.getDate() != null) {
            datePicker.setValue(consignment.getDate().getNonConvertedDate());
        }
        numberData.setNumberData(consignment.getNumberData());
        itemsTable.removeAllRows();

        if (consignment.getItems() != null && consignment.getItems().length > 0) {
            for (int i = 0; i < consignment.getItems().length; i++) {
                itemsTable.addRow(getWidgets(consignment.getItems()[i]));
            }
            if (consignment.getItems().length < DEFAULT_ROWS) {
                for (int i = consignment.getItems().length; i < DEFAULT_ROWS; i++) {
                    itemsTable.addRow(getWidgets(null));
                }
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                itemsTable.addRow(getWidgets(null));
            }
        }
    }

    private void saveTransaction() {

        final Consignment transaction = new Consignment();
        transaction.setObjectID(objectID);
        transaction.setName(nameTextBox.getText());
        transaction.setDate(new DateNonConvertable(datePicker.getValue()));
        transaction.setNumberData(numberData.getNumberData(false));
        transaction.setReference(referenceTextBox.getText());

        ArrayList<ConsignmentItem> items = new ArrayList<>();

        for (int i = 0; i < grid.getRowCount(); i++) {

            CRMLookUp fromCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.from());
            CRMLookUp toCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.to());
            ProductLookUp product = (ProductLookUp) itemsTable.getColumnById(i, wfmStrings.product());
            ExtendedTextBox quantity = (ExtendedTextBox) itemsTable.getColumnById(i, wfmStrings.qty());

            if (fromCompany.getSelectedItemID() != null && toCompany.getSelectedItemID() != null) {
                ConsignmentItem item = new ConsignmentItem();
                item.setFromCompany(fromCompany.getSelectedItem());
                item.setToCompany(toCompany.getSelectedItem());
                item.setProduct(product.getSelectedItem());
                item.setQuantity(AccountingUtils.get().parseToBigDecimal(quantity.getText()));
                items.add(item);
            }
        }
        transaction.setItems(items.toArray(new ConsignmentItem[]{}));

        LoadingPanel.loading(true);
        ConsignmentService.App.get().save(transaction, new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                setButtonEnable(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                setButtonEnable(true);

                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.transaction()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONSIGNMENT_UPDATE, result, ConsignmentAddEditView.this);
                closeView();
            }
        });
    }

    private void closeView() {
        closeTab();
    }

    private boolean validate() {
        int errors = 0;


        if (!Validation.validateTextBoxRequired(nameTextBox)) {
            errors++;
        }

        for (int i = 0; i < grid.getRowCount(); i++) {
            itemsTable.resetValidation(i);
            CRMLookUp fromCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.from());
            CRMLookUp toCompany = (CRMLookUp) itemsTable.getColumnById(i, wfmStrings.to());
            ProductLookUp product = (ProductLookUp) itemsTable.getColumnById(i, wfmStrings.product());

            if (fromCompany.getSelectedItemID() != null || toCompany.getSelectedItemID() != null || product.getSelectedItemID() != null) {
                if (!Validation.validateLookUpRequired(fromCompany)) {
                    itemsTable.notValid(i, wfmStrings.from());
                    errors++;
                }
                if (!Validation.validateLookUpRequired(toCompany)) {
                    itemsTable.notValid(i, wfmStrings.to());
                    errors++;
                }
                if (!Validation.validateLookUpRequired(product)) {
                    itemsTable.notValid(i, wfmStrings.product());
                    errors++;
                }
                if (errors > 0) {
                    return false;
                }  else {
                    itemsTable.setItemValid(i, true);
                    itemsTable.incValidRow();
                }
            }
            if (itemsTable.getValidRows() == 0) {
                itemsTable.notValid(0, wfmStrings.from());
                itemsTable.notValid(0, wfmStrings.to());
                itemsTable.notValid(0, wfmStrings.product());
                return false;
            }
        }

        StringBuilder p = new StringBuilder();
        for (String key : allocatedProductQtyMap.keySet()) {
            if (productQtyMap.get(key) != null && allocatedProductQtyMap.get(key).compareTo(productQtyMap.get(key)) > 0) {
                if (!p.toString().isEmpty()) {
                    p.append(", ");
                }

                p.append(productMap.get(key));
            }
        }

        if (!p.toString().isEmpty()) {
            errors++;
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
            //messageBox.setSize(560, 150);
            messageBox.setTitle(accountingStrings.notEnoughQuantity());
            messageBox.setMessage(accountingMessages.youDoNotHaveEnoughQuantityForConsignment(p.toString()));
            messageBox.open();
        }

        if (errors > 0) {

            if (p.toString().isEmpty())
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);

            return false;
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

    private void setButtonEnable(boolean b) {
        saveButton.setEnabled(b);
    }
}