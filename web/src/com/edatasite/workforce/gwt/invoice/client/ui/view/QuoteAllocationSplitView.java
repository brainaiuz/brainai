package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.SalesManLookUp;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateComissionItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/19/12
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuoteAllocationSplitView extends SimpleLink implements AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final String COMISSION = "COMISSION";
    private static final String SALESMAN = "SALESMAN";
    private static final String TOTAL = "TOTAL";
    private static final Integer splitColumnCount = 3;
    private static final Integer DEFAULT_ROWS = 2;


    private KpiModal splitDialogBox;
    private EditableTable splitTable;
    private EditableGrid splitGrid;
    private HTML helpLabel;
    private final HTML comissionAmountLabel;
    private final HTML remainingLabel;
    private Button apply;
    private Button cancel;

    private BigDecimal comissionAmount;
    private BigDecimal remainingTotal;
    private final boolean isEditForm;
    private final List<AllocateComissionItem> allocateItems;
    private Command listener;

    public QuoteAllocationSplitView(List<AllocateComissionItem> allocateItems, boolean isVisible, boolean isEditForm) {
        super(wfmStrings.split());
        setVisible(isVisible);
        this.allocateItems = allocateItems;
        this.isEditForm = isEditForm;
        comissionAmountLabel = new HTML();
        remainingLabel = new HTML();
        addClickHandler(event -> {
            if (listener != null) {
                listener.execute();
                comissionAmountLabel.setHTML("<b>" + accountingStrings.comissionAmount() + ": " + AccountingUtils.get().formatPrice(comissionAmount) + "</b>");
            }
            if (splitDialogBox == null) {
                initializeSplitDialogBox();
            }
            calculateTotal();
            splitDialogBox.open();
        });
        if (isEditForm) {
            initializeSplitDialogBox();
        }
    }

    private void initializeSplitDialogBox() {
        splitDialogBox = new KpiModal();
        splitDialogBox.setTitle(accountingStrings.splitComissionAmount());
        splitDialogBox.setWidth("450px");
        ScrollPanel scrollPanel = new ScrollPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);
        helpLabel = new HTML("<br>" + accountingStrings.comissionSplitHelpMessage() + "</b>");
        verticalPanel.add(helpLabel);
        verticalPanel.setCellHorizontalAlignment(helpLabel, HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.add(comissionAmountLabel);
        verticalPanel.setCellHorizontalAlignment(comissionAmountLabel, HasHorizontalAlignment.ALIGN_RIGHT);
        splitTable = new EditableTable(drawColumns());
        splitTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                splitTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                updateTotal();
            }
        });
        splitGrid = splitTable.getGrid();
        if (!isEditForm) {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                splitTable.addRow(getWidgets(null));
            }
        } else {
            if (allocateItems != null && allocateItems.size() > 0) {
                for (AllocateComissionItem item : allocateItems) {
                    splitTable.addRow(getWidgets(item));
                }
            } else {
                for (int i = 0; i < DEFAULT_ROWS; i++) {
                    splitTable.addRow(getWidgets(null));
                }
            }
        }

        verticalPanel.add(splitTable);
        verticalPanel.add(remainingLabel);
        verticalPanel.setCellHorizontalAlignment(splitTable, HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setCellVerticalAlignment(splitTable, HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellHorizontalAlignment(remainingLabel, HasHorizontalAlignment.ALIGN_RIGHT);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        apply = new Button(wfmStrings.apply());
        apply.addClickHandler(event -> {
            if (validateForPercent()) {
                if (validate()) {
                    splitDialogBox.close();
                }
            } else {
                Info.show(accountingStrings.comissionValidateMessage(), Info.Type.WARNING);
            }
        });

        cancel = new Button(wfmStrings.cancel());
        cancel.addClickHandler(event -> splitDialogBox.close());

        buttonPanel.setStyleName("workforce");
        buttonPanel.setSpacing(10);
        buttonPanel.add(apply);
        buttonPanel.add(cancel);
        verticalPanel.add(buttonPanel);
        verticalPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        scrollPanel.add(verticalPanel);

        splitDialogBox.add(scrollPanel);

    }

    private Object[] getWidgets(Object object) {
        List<Object> widgets = new ArrayList<>();
        if (object != null) {
            AllocateComissionItem allocateQuoteItem = (AllocateComissionItem) object;
            SalesManLookUp salesManLookUp = new SalesManLookUp();
            salesManLookUp.getSuggestBox().setWidth("148px");
            if (allocateQuoteItem.getSalesMan() != null) {
                salesManLookUp.setSelected(allocateQuoteItem.getSalesMan());
            }
            widgets.add(salesManLookUp);
            CustomCellTextBox amountTextBox = new CustomCellTextBox();
            amountTextBox.setWidth("98");
            amountTextBox.setMaxLength(16);
            Validation.addNumericKeyboardListener(amountTextBox, AccountingUtils.calculationScale);
            Validation.checkToFocusTextBox(amountTextBox, AccountingUtils.get().formatPrice(ZERO));
            amountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            amountTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                    if (validateForPercent()) {
                        calculateTotal();
                    } else {
                        Info.show(accountingStrings.comissionValidateMessage(), Info.Type.WARNING);
                    }
                }
            });
            amountTextBox.setText(AccountingUtils.get().formatPrice(allocateQuoteItem.getAllocatePercent()));
            widgets.add(amountTextBox);
            CustomCellLabel totalLabel = getTotalLabel(allocateQuoteItem.getAllocateTotal());
            totalLabel.setWidth("98px");
            widgets.add(totalLabel);
        } else {
            SalesManLookUp salesManLookUp = new SalesManLookUp();
            salesManLookUp.getSuggestBox().setWidth("148px");
            widgets.add(salesManLookUp);
            CustomCellTextBox amountTextBox = new CustomCellTextBox();
            amountTextBox.setWidth("98");
            amountTextBox.setMaxLength(16);
            Validation.addNumericKeyboardListener(amountTextBox, AccountingUtils.calculationScale);
            Validation.checkToFocusTextBox(amountTextBox, AccountingUtils.get().formatPrice(ZERO));
            amountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            amountTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                    if (validateForPercent()) {
                        calculateTotal();
                    } else {
                        Info.show(accountingStrings.comissionValidateMessage(), Info.Type.WARNING);
                    }
                }
            });
            amountTextBox.setText(AccountingUtils.get().formatPrice(ZERO));
            widgets.add(amountTextBox);
            CustomCellLabel totalLabel = getTotalLabel(null);
            totalLabel.setWidth("98px");
            widgets.add(totalLabel);
        }
        return widgets.toArray(new Object[]{});
    }

    private void calculateTotal() {
        BigDecimal comissionPercent, totalComission, total = ZERO;
        for (int i = 0; i < splitGrid.getRowCount(); i++) {
            CustomCellTextBox comissionTextBox = (CustomCellTextBox) splitTable.getColumnById(i, COMISSION);
            CustomCell totalLabel = (CustomCell) splitTable.getColumnCellWidgetById(i, TOTAL);
            comissionPercent = AccountingUtils.get().parseToBigDecimal(comissionTextBox.getText());
            totalComission = comissionAmount.multiply(comissionPercent.divide(HUNDRED));
            total = total.add(totalComission);
            totalLabel.setValue(AccountingUtils.get().formatPrice(totalComission));
            totalLabel.InActive();
        }
        remainingTotal = comissionAmount.subtract(total);
        remainingLabel.setHTML("<b>" + accountingMessages.remainingTotal(AccountingUtils.get().formatPrice(remainingTotal)) + "</b>");
    }


    private void updateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < splitGrid.getRowCount(); i++) {
            CustomCell totalLabel = (CustomCell) splitTable.getColumnCellWidgetById(i, TOTAL);
            total = total.add(AccountingUtils.get().parseToBigDecimal((String) totalLabel.getValue()));
        }
        remainingTotal = comissionAmount.subtract(total);
        remainingLabel.setHTML("<b>" + accountingMessages.remainingTotal(AccountingUtils.get().formatPrice(remainingTotal)) + "</b>");
    }

    private boolean validateForPercent() {
        BigDecimal percentTotal = ZERO;
        CustomCellTextBox comissionTextBox;
        for (int rowId = 0; rowId < splitGrid.getRowCount(); rowId++) {
            comissionTextBox = (CustomCellTextBox) splitTable.getColumnById(rowId, COMISSION);
            percentTotal = percentTotal.add(AccountingUtils.get().parseToBigDecimal(comissionTextBox.getText()));
            if (percentTotal.compareTo(HUNDRED) > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean validate() {
        int errors = 0;
        SalesManLookUp salesManLookUp;
        CustomCellTextBox comissionTextBox;
        CustomCellLabel totalCellLabel;
        splitTable.setValidRows(0);
        for (int rowId = 0; rowId < splitGrid.getRowCount(); rowId++) {
            splitTable.resetValidation(rowId);
            salesManLookUp = (SalesManLookUp) splitTable.getColumnById(rowId, SALESMAN);
            comissionTextBox = (CustomCellTextBox) splitTable.getColumnById(rowId, COMISSION);
            if (LookUp.wfmStrings.searchTypeMessage().equals(salesManLookUp.getTextBox().getText().trim())) {
                splitTable.setColumnValid(SALESMAN);
                errors++;
            }

            if (!validateComissionPercent(comissionTextBox.getText(), true)) {
                splitTable.setColumnValid(COMISSION);
                errors++;
            }

            if (errors > 0) {
                if (errors == splitTable.getRequiredFieldCount()) {
                    splitTable.setItemValid(rowId, false);
                    errors = 0;
                } else {
                    Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                    return false;
                }
            } else {
                splitTable.setItemValid(rowId, true);
                splitTable.incValidRow();
            }

        }
        return true;
    }

    private boolean validateComissionPercent(String text, boolean validateZero) {
        if (text == null || text.equals("")) {
            return false;
        } else return !validateZero || AccountingUtils.get().parseToBigDecimal(text).compareTo(ZERO) != 0;
    }

    public ArrayList<AllocateComissionItem> getAllocateComissionItems() {
        AllocateComissionItem allocateComissionItem;
        SalesManLookUp salesManLookUp;
        CustomCellTextBox comissionTextBox;
        CustomCellLabel totalCellLabel;
        ArrayList<AllocateComissionItem> allocateComissionItems = new ArrayList<>();
        if (splitGrid != null) {
            for (int rowId = 0; rowId < splitGrid.getRowCount(); rowId++) {
                if (splitTable.isItemValid(rowId)) {
                    allocateComissionItem = new AllocateComissionItem();
                    salesManLookUp = (SalesManLookUp) splitTable.getColumnById(rowId, SALESMAN);
                    comissionTextBox = (CustomCellTextBox) splitTable.getColumnById(rowId, COMISSION);
                    totalCellLabel = (CustomCellLabel) splitTable.getColumnById(rowId, TOTAL);
                    allocateComissionItem.setSalesMan(salesManLookUp.getSelectedItem());
                    allocateComissionItem.setAllocatePercent(numberFormat.parse(comissionTextBox.getText()));
                    allocateComissionItem.setAllocateTotal(AccountingUtils.get().parseToBigDecimal(totalCellLabel.getDisplayValue()));
                    allocateComissionItems.add(allocateComissionItem);
                }
            }
        }
        return allocateComissionItems;
    }


    ColumnConfig[] drawColumns() {
        Integer index = 0;
        ColumnConfig[] columns = new ColumnConfig[splitColumnCount];
        columns[index] = new ColumnConfig(LookUpCell.class, SALESMAN, 150, true);
        columns[index].setTitle(accountingStrings.salesMan());
        columns[++index] = new ColumnConfig(CustomCell.class, COMISSION, 100, true);
        columns[index].setTitle(accountingStrings.comissionPercent());
        columns[++index] = new ColumnConfig(CustomCell.class, TOTAL, 100, false);
        columns[index].setTitle(wfmStrings.total());
        return columns;
    }


    public CustomCellLabel getTotalLabel(BigDecimal total) {
        if (total != null) {
            return new CustomCellLabel(AccountingUtils.get().formatPrice(total));
        }
        return new CustomCellLabel(AccountingUtils.getZero());
    }

    public void setComissionAmount(BigDecimal comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public void setListener(Command listener) {
        this.listener = listener;
    }


}
