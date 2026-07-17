package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.*;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractExpenseAllocation extends KpiSideNavBox implements AccountingConstants {
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ProvidesKey<ExpenseListItem> KEY_PROVIDER = item -> item == null ? null : item.getId();
    private static final int MAX_HEIGHT = 420;
    protected ExpenseListItem[] expenseItems;
    protected Div container;
    protected ReceiptTable totalsTable;
    protected DataListBox allocationTypeListBox;
    private KpiDataGrid<ExpenseListItem> dataGrid;
    private ListDataProvider<ExpenseListItem> dataProvider;
    private BigDecimal totalExpenses = AccountingConstants.ZERO;
    private final BigDecimal totalReceivedExpenses;

    public AbstractExpenseAllocation(BigDecimal totalReceivedExpenses) {
        super(WIDE_FORM_WIDTH);
        this.totalReceivedExpenses = totalReceivedExpenses != null ? totalReceivedExpenses : BigDecimal.ZERO;
        onInitialize();
    }

    private void onInitialize() {

        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setHeight("200px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);

        allocationTypeListBox = new DataListBox();
        allocationTypeListBox.setWithoutNullLabel(true);
        allocationTypeListBox.setItems(getAllocationTypes());
        allocationTypeListBox.addValueChangeHandler(event -> {
            if (allocationTypeListBox.getSelectedId() != null) {
                calculate(allocationTypeListBox.getSelectedId());
            }
        });
        setAllocationType();

        totalsTable = new ReceiptTable();
        totalsTable.removeShippingBody();

        container = new Div();
        container.add(new GRow(new GColumn(GColumnEnum.COL_12, dataGrid)));

        GColumn typeColumn = new GColumn(new FormGroup(wfmStrings.type(), allocationTypeListBox));
        GColumn totalsColumn = new GColumn(GColumnEnum.COL_AUTO, totalsTable);
        totalsColumn.setOffset(GColumnOffsetEnum.OFFSET_1);
        container.add(new GRow(typeColumn, totalsColumn));
        addBody(container);
    }

    protected abstract SelectItem[] getAllocationTypes();

    protected abstract void setAllocationType();

    protected abstract void calculate(Integer allocationType);

    public BigDecimal getRemainingBalance() {
        return totalExpenses.subtract(totalReceivedExpenses);
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public Integer getAllocationType() {
        Integer acclocationType = allocationTypeListBox.getSelectedId();
        return acclocationType != null ? allocationTypeListBox.getSelectedId() : DONOT_ALLOCATE;
    }

    public void setExpenseItems(ExpenseListItem[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        expenseItems = items;

        int height = items.length * 45 + 50;
        if (height > MAX_HEIGHT) {
            height = MAX_HEIGHT;
        }
        dataGrid.setHeight(height + "px");
        initColumns();

        dataProvider.getList().clear();
        Collections.addAll(dataProvider.getList(), items);
        dataProvider.refresh();

        if (items != null && items.length > 0) {
            totalExpenses = Stream.of(items).reduce(BigDecimal.ZERO, (total, itm) -> total.add(itm.getBaseSubtotal()), BigDecimal::add);
            initTotalsTable();
        }
    }

    private void initTotalsTable() {
        totalsTable.clear();
        setTotalData(wfmStrings.total(), totalExpenses);
        setTotalData(accountingStrings.allocated(), totalReceivedExpenses);
        totalsTable.addGrossItem(new HTML(wfmStrings.remaining()), new HTML(AccountingUtils.get().formatPrice(totalExpenses.subtract(totalReceivedExpenses))));
    }

    private void setTotalData(String text, BigDecimal value) {
        HTML labelHTML = new HTML(text);
        HTML valueHTML = new HTML(AccountingUtils.get().formatPrice(value));
        totalsTable.addItem(labelHTML, valueHTML);
    }

    private void initColumns() {
        //Number
        final SimpleLinkCell[] cell = {new SimpleLinkCell()};
        Column<ExpenseListItem, String> number = new Column<ExpenseListItem, String>(cell[0]) {
            @Override
            public String getValue(ExpenseListItem data) {
                cell[0].setClickHandler(e -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + data.getReportId() + "/EXPENSE_VIEW/ACCOUNTING" + data.getId(), data.getExpenseReportNumber(), data.getExpenseReportNumber());
                    remove();
                });
                return data.getExpenseReportNumber();
            }
        };
        dataGrid.addColumn(number, wfmStrings.number());
        dataGrid.setColumnWidth(number, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //Date
        Column<ExpenseListItem, String> date = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getDate() != null ? DateUtils.format(data.getDate()) : "";
            }
        };
        dataGrid.addColumn(date, wfmStrings.date());
        dataGrid.setColumnWidth(date, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //Reporter
        Column<ExpenseListItem, String> reporter = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getReportReporter() != null ? data.getReportReporter().getName() : "";
            }
        };
        dataGrid.addColumn(reporter, wfmStrings.reporter());
        dataGrid.setColumnWidth(reporter, 30, com.google.gwt.dom.client.Style.Unit.PCT);


        //Category
        Column<ExpenseListItem, String> category = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getCategoryName();
            }
        };
        dataGrid.addColumn(category, wfmStrings.category());
        dataGrid.setColumnWidth(category, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //BaseSubTotal
        Column<ExpenseListItem, String> baseSubTotal = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return AccountingUtils.get().formatPrice(data.getBaseSubtotal());
            }
        };
        dataGrid.addColumn(baseSubTotal, wfmStrings.amount());
        dataGrid.setColumnWidth(baseSubTotal, 30, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    enum AllocationType {
        /**
         * Allocate expense amount(landed cost) by qty
         */
        ALLOCATE_BY_QTY(AccountingConstants.ALLOCATE_BY_QTY) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal totalRemaining, DynamicTable itemsTable) {
                //TODO not clear
                BigDecimal totalQty = BigDecimal.ZERO;
                if (data instanceof NewInvoice) {
                    NewInvoice invoiceData = (NewInvoice) data;
                    totalQty = Stream.of(invoiceData.getItems()).reduce(BigDecimal.ZERO, (substotal, item) -> substotal.add(item.getRemainingQty()), BigDecimal::add);
                }

                BigDecimal finalTotalQty = totalQty;
                allocateAmount(itemsTable, (object, index) -> {
                    TextBox txtReceiveBox = object.getTxtReceiveBox();
                    AllocationTextBox txtAllocationBox = object.getTxtAllocationBox();
                    BigDecimal receiveQty = txtReceiveBox != null ? AccountingUtils.get().parseToBigDecimal(txtReceiveBox.getText()) : BigDecimal.ZERO;

                    if (txtAllocationBox != null && finalTotalQty.compareTo(BigDecimal.ZERO) != 0) {
                        txtAllocationBox.setEnabled(false);
                        BigDecimal shareAmount = totalRemaining.multiply(receiveQty).divide(finalTotalQty, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        txtAllocationBox.allocateAmount(shareAmount);
                    }
                });
            }
        },

        /**
         * Allocate expense amount(landed cost) by receive qty
         */
        ALLOCATE_RECEIVE_ITEM_BY_QTY(AccountingConstants.ALLOCATE_RECEIVE_ITEM_BY_QTY) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal totalRemaining, DynamicTable itemsTable) {
                BigDecimal totalReceiveQty = BigDecimal.ZERO;
                for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                    DynamicTableItem tableItem = itemsTable.getItem(i);
                    TextBox receive = null;
                    if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) instanceof MaterialPanel) {
                        receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                    } else if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) instanceof Div) {
                        receive = (TextBox) ((Div) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                    } else {
                        receive = (TextBox) tableItem.getColumnById(ProductsTable.RECEIVED_QTY);
                    }
                    BigDecimal receiveQty = receive != null ? AccountingUtils.get().parseToBigDecimal(receive.getText()) : BigDecimal.ZERO;
                    totalReceiveQty = totalReceiveQty.add(receiveQty);
                }

                BigDecimal finalTotalRemainingQty = totalReceiveQty;
                allocateAmount(itemsTable, (object, index) -> {
                    TextBox txtReceiveBox = object.getTxtReceiveBox();
                    AllocationTextBox txtAllocationBox = object.getTxtAllocationBox();
                    BigDecimal receiveQty = txtReceiveBox != null ? AccountingUtils.get().parseToBigDecimal(txtReceiveBox.getText()) : BigDecimal.ZERO;
                    if (txtAllocationBox != null && finalTotalRemainingQty.compareTo(BigDecimal.ZERO) != 0) {
                        txtAllocationBox.setEnabled(false);
                        BigDecimal shareAmount = totalRemaining.multiply(receiveQty.divide(finalTotalRemainingQty, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                        txtAllocationBox.allocateAmount(shareAmount);
                    }
                });
            }
        },

        /**
         * Allocate expense amount(landed cost) by amount
         */
        ALLOCATE_BY_AMOUNT(AccountingConstants.ALLOCATE_BY_AMOUNT) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal totalRemaining, DynamicTable itemsTable) {
                NewInvoice invoiceData = (NewInvoice) data;
                //TODO not clear
                NewInvoiceItem[] items = invoiceData.getItems();
                BigDecimal totalAmount = Stream.of(items).reduce(BigDecimal.ZERO, (substotal, item) -> substotal.add(item.getRemainingAmount()), BigDecimal::add);

                allocateAmount(itemsTable, ((object, index) -> {
                    TextBox txtReceiveBox = object.getTxtReceiveBox();
                    AllocationTextBox txtAllocationBox = object.getTxtAllocationBox();
                    BigDecimal receiveQty = txtReceiveBox != null ? AccountingUtils.get().parseToBigDecimal(txtReceiveBox.getText()) : BigDecimal.ZERO;
                    if (txtAllocationBox != null && items[index].getRemainingQty().compareTo(BigDecimal.ZERO) > 0 && receiveQty != null && receiveQty.compareTo(BigDecimal.ZERO) > 0) {
                        txtAllocationBox.setEnabled(false);
                        BigDecimal shareAmount = items[index].getRemainingAmount().multiply(receiveQty).divide(items[index].getRemainingQty(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        shareAmount = totalRemaining.multiply(shareAmount).divide(totalAmount, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        txtAllocationBox.allocateAmount(shareAmount);
                    } else {
                        txtAllocationBox.setEnabled(false);
                        txtAllocationBox.allocateAmount(BigDecimal.ZERO);
                    }
                }));
            }
        },

        /**
         * Allocate expense amount(landed cost) by receive amount
         */
        ALLOCATE_RECEIVE_ITEM_BY_AMOUNT(AccountingConstants.ALLOCATE_RECEIVE_ITEM_BY_AMOUNT) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal totalRemaining, DynamicTable itemsTable) {
                BigDecimal totalNotReceivedAmount = BigDecimal.ZERO;
                if (data instanceof NewInvoice) {
                    NewInvoice invoiceData = (NewInvoice) data;
                    NewInvoiceItem[] items = invoiceData.getItems();
                    for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                        DynamicTableItem tableItem = itemsTable.getItem(i);
                        TextBox receive = null;
                        if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) instanceof MaterialPanel) {
                            receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                        } else {
                            receive = (TextBox) tableItem.getColumnById(ProductsTable.RECEIVED_QTY);
                        }

                        BigDecimal receiveQty = receive != null ? AccountingUtils.get().parseToBigDecimal(receive.getText()) : BigDecimal.ZERO;

                        if (items[i].getRemainingQty().compareTo(BigDecimal.ZERO) > 0 && receiveQty.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal shareAmount = items[i].getRemainingAmount().multiply(receiveQty.divide(items[i].getRemainingQty(), 10, RoundingMode.HALF_UP));
                            totalNotReceivedAmount = totalNotReceivedAmount.add(shareAmount);
                        }
                    }

                    BigDecimal finalTotalNotReceivedAmount = totalNotReceivedAmount;
                    allocateAmount(itemsTable, ((object, index) -> {
                        TextBox txtReceiveBox = object.getTxtReceiveBox();
                        AllocationTextBox txtAllocationBox = object.getTxtAllocationBox();
                        BigDecimal receiveQty = txtReceiveBox != null ? AccountingUtils.get().parseToBigDecimal(txtReceiveBox.getText()) : BigDecimal.ZERO;

                        if (txtAllocationBox != null && finalTotalNotReceivedAmount.compareTo(BigDecimal.ZERO) != 0 && items[index].getRemainingQty().compareTo(BigDecimal.ZERO) > 0) {
                            txtAllocationBox.setEnabled(false);
                            BigDecimal shareAmount = items[index].getRemainingAmount().multiply(receiveQty.divide(items[index].getRemainingQty(), 10, RoundingMode.HALF_UP));
                            shareAmount = totalRemaining.multiply(shareAmount).divide(finalTotalNotReceivedAmount, 10, RoundingMode.HALF_UP);
                            txtAllocationBox.allocateAmount(shareAmount);
                        } else {
                            txtAllocationBox.setEnabled(false);
                            txtAllocationBox.allocateAmount(BigDecimal.ZERO);
                        }
                    }));
                } else if (data instanceof ShippingData) {
                    ShippingData shippingData = (ShippingData) data;
                    totalNotReceivedAmount = shippingData.getItems().stream().reduce(BigDecimal.ZERO, (total, itm) -> total.add(itm.getNet()), BigDecimal::add);
                    ShippingDataItem[] items = shippingData.getItems().toArray(new ShippingDataItem[]{});

                    BigDecimal finalTotalNotReceivedAmount = totalNotReceivedAmount;
                    allocateAmount(itemsTable, ((object, index) -> {
                        TextBox txtReceiveBox = object.getTxtReceiveBox();
                        AllocationTextBox txtAllocationBox = object.getTxtAllocationBox();
                        BigDecimal receiveQty = txtReceiveBox != null ? AccountingUtils.get().parseToBigDecimal(txtReceiveBox.getText()) : BigDecimal.ZERO;

                        if (txtAllocationBox != null && finalTotalNotReceivedAmount.compareTo(BigDecimal.ZERO) != 0 && items[index].getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            txtAllocationBox.setEnabled(false);
                            BigDecimal shareAmount = items[index].getNet().multiply(receiveQty.divide(items[index].getAmount(), 10, RoundingMode.HALF_UP));
                            shareAmount = totalRemaining.multiply(shareAmount).divide(finalTotalNotReceivedAmount, 10, RoundingMode.HALF_UP);
                            txtAllocationBox.allocateAmount(shareAmount);
                        } else {
                            txtAllocationBox.setEnabled(false);
                            txtAllocationBox.allocateAmount(BigDecimal.ZERO);
                        }
                    }));
                }
            }
        },

        ALLOCATE_MANUALLY(AccountingConstants.ALLOCATE_MANUALLY) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal remainingBalance, DynamicTable itemsTable) {
                for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                    DynamicTableItem tableItem = itemsTable.getItem(i);

                    AllocationTextBox allocationTxtBox = null;
                    if (tableItem.getColumnById(ProductsTable.ALLOCATION) instanceof MaterialPanel) {
                        MaterialPanel container = (MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION);
                        allocationTxtBox = (AllocationTextBox) container.getWidget(0);
                    } else {
                        allocationTxtBox = (AllocationTextBox) tableItem.getColumnById(ProductsTable.ALLOCATION);
                    }

                    if (allocationTxtBox != null) {
                        allocationTxtBox.setEnabled(true);
                    }
                }
            }
        },

        DONOT_ALLOCATE(AccountingConstants.DONOT_ALLOCATE) {
            @Override
            public <T> void calculateAllocatedAmount(T data, BigDecimal remainingBalance, DynamicTable itemsTable) {
                for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                    DynamicTableItem tableItem = itemsTable.getItem(i);

                    AllocationTextBox allocationTxtBox = null;
                    if (tableItem.getColumnById(ProductsTable.ALLOCATION) instanceof MaterialPanel) {
                        MaterialPanel container = (MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION);
                        allocationTxtBox = (AllocationTextBox) container.getWidget(0);
                    } else {
                        allocationTxtBox = (AllocationTextBox) tableItem.getColumnById(ProductsTable.ALLOCATION);
                    }

                    if (allocationTxtBox != null) {
                        allocationTxtBox.setEnabled(false);
                        allocationTxtBox.allocateAmount(BigDecimal.ZERO);
                    }
                }
            }
        };

        static Map<Integer, AllocationType> map;

        static {
            map = new HashMap<>();
            Stream.of(values()).forEach(type -> {
                map.put(type.getId(), type);
            });
        }

        int id;

        AllocationType(int id) {
            this.id = id;
        }

        public static AllocationType getById(Integer id) {
            return map.get(id);
        }

        public abstract <T> void calculateAllocatedAmount(T data, BigDecimal remaningAllocationAmount, DynamicTable itemsTable);

        protected void allocateAmount(DynamicTable itemsTable, ICalculateAllocation calculateAllocation) {
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getItem(i);

                AllocationTextBox txtAllocationBox = null;
                if (tableItem.getColumnById(ProductsTable.ALLOCATION) instanceof MaterialPanel) {
                    txtAllocationBox = (AllocationTextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION)).getWidget(0);
                } else {
                    txtAllocationBox = (AllocationTextBox) tableItem.getColumnById(ProductsTable.ALLOCATION);
                }

                TextBox txtReceiveBox = null;
                if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) instanceof MaterialPanel) {
                    txtReceiveBox = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                } else if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) instanceof Div) {
                    txtReceiveBox = (TextBox) ((Div) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                } else {
                    txtReceiveBox = (TextBox) tableItem.getColumnById(ProductsTable.RECEIVED_QTY);
                }
                calculateAllocation.calculate(new AllocationObject(txtAllocationBox, txtReceiveBox), i);
            }
        }

        public int getId() {
            return id;
        }

        interface ICalculateAllocation {
            void calculate(AllocationObject object, int index);
        }

        class AllocationObject {
            AllocationTextBox txtAllocationBox;
            TextBox txtReceiveBox;

            public AllocationObject() {
            }

            public AllocationObject(AllocationTextBox txtAllocationBox, TextBox txtReceiveBox) {
                this.txtAllocationBox = txtAllocationBox;
                this.txtReceiveBox = txtReceiveBox;
            }

            public AllocationTextBox getTxtAllocationBox() {
                return txtAllocationBox;
            }

            public TextBox getTxtReceiveBox() {
                return txtReceiveBox;
            }
        }
    }
}
