package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.interfaces.LinkedLinkableCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.BulkAddCategoriesItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class LinkedTypeWidget extends FlexTable implements LinkedLinkableCellInterface {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final Integer POPUP_INDEX = 2;
    private final Map<Integer, PaymentDeductionSelectItem> existingItems = new HashMap<>();
    private final ArrayList<PaymentDeductionSelectItem> selectedRows = new ArrayList<>();
    private final ArrayList<PaymentDeductionSelectItem> loadedCategories = new ArrayList<>();
    private Anchor link;
    private DataListBox type;
    private WfmButton2 addNew;
    private FlexTable flexTable;
    private Command clickHandler;
    private Command changeHandler;
    private KpiCheckBox selectAll;
    private WfmButton2 bulkAddBtn;
    private KpiModal bulkAddModal;
    private TextBox categorySearchBox;
    private Command copyFromBoxHandler;
    private WfmButton2 applyBulkAddBtn;
    private WfmButton2 closeBulkAddBtn;
    private KpiModal categoriesDialogBox;
    private EditableTable categoriesTable;
    private KpiCheckBox copyFromPaymentTable;
    private ListingFilterParameter filterParameter;
    private DataListBox tableListLimitBox;
    private TextBox tableCurrentBox;
    private MaterialLink tablePagingResult;
    private Integer totalTableItems = 0;
    private Integer tableCurrent = 0;
    private Integer tableStart = 0;


    public LinkedTypeWidget() {
        super();
        initialize();
    }

    private void initialize() {
        initDeductionModal();
        initBulkAddModal();
    }

    private void initDeductionModal() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setTitle(wfmStrings.deductionDetails());
        categoriesDialogBox.setWidth(690);

        copyFromPaymentTable = new KpiCheckBox(wfmStrings.copyFromPaymentsTable());
        copyFromPaymentTable.addStyleName("copyfromPaymentTable mt-2");

        type = new DataListBox();
        type.setWithoutNullLabel(true);

        type.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed()),
                new SelectItem(1, wfmStrings.basicOfPersentage()),
                new SelectItem(2, wfmStrings.ofBasicAllowances())
        });

        link = new Anchor(getLinkValue());
        link.getElement().addClassName("right"); //https://prnt.sc/r8etr7
        link.addClickHandler(clickEvent -> categoriesDialogBox.open());
        type.addValueChangeHandler(event -> {
            if (changeHandler != null)
                changeHandler.execute();
        });

        copyFromPaymentTable.addValueChangeHandler(event -> {
            if (copyFromPaymentTable.getValue() && copyFromBoxHandler != null) {
                copyFromBoxHandler.execute();
                addNew.setEnabled(true);
                categoriesTable.getGrid().getModel().removeAll();
                categoriesTable.getGrid().getModel().clearRemovedRows();
                existingItems.clear();
            } else {
                for (int i = 0; i < categoriesTable.getRowCount(); i++) {
                    CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
                    categoryLookUp.setEnabled(true);
                }
            }
        });

        ColumnConfig[] columns = new ColumnConfig[1];
        columns[0] = new ColumnConfig(LookUpCell.class, "category", wfmStrings.allowance(), 200, true, "left-align-Cell");

        categoriesTable = new EditableTable(columns);
        categoriesTable.addStyleName("categoriesTable");

        addItem(null, false);

        VerticalPanel vp = new VerticalPanel();
        vp.add(categoriesTable);
        vp.add(copyFromPaymentTable);
        vp.setSpacing(3);

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.getElement().getStyle().setProperty("maxHeight", "300px");
        scrollPanel.add(vp);

        categoriesDialogBox.getElement().getStyle().setProperty("minWidth", "400px");
        categoriesDialogBox.addStyleName("deductionDetailsModal");
        categoriesDialogBox.add(scrollPanel);

        WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        apply.addClickHandler(clickEvent -> categoriesDialogBox.close());

        categoriesTable.setRemoveRowListener(() -> {
            if (categoriesTable.getRowCount() <= 1) {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            } else {
                CategoryLookUp category = (CategoryLookUp) categoriesTable.getColumnById(categoriesTable.getGrid().getCurrentRow(), "category");
                if (category.getSelectedData() != null) {
                    existingItems.remove(category.getSelectedData().getId());
                }
                categoriesTable.getGrid().getModel().removeRow(categoriesTable.getGrid().getCurrentRow());
            }
        });

        addNew = new WfmButton2(wfmStrings.add(), WfmButton2.BTN_SUCCESS);
        addNew.addClickHandler(event -> {
            for (int rowID = 0; rowID < categoriesTable.getRowCount(); ) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(rowID, "category");
                if (categoryLookUp.getSelectedData() == null) {
                    categoriesTable.getGrid().getModel().removeRow(rowID);
                    rowID = 0;
                } else {
                    rowID++;
                }
            }
            addItem(null, false);
        });

        bulkAddBtn = new WfmButton2(wfmStrings.bulkAdd(), WfmButton2.BTN_DEFAULT);
        bulkAddBtn.addClickHandler(click -> {
            clearData();
            loadCategories();
            bulkAddModal.open();
        });

        categoriesDialogBox.addButton(bulkAddBtn);
        categoriesDialogBox.addButton(addNew);
        categoriesDialogBox.addButton(apply);

        clickHandler = () -> categoriesDialogBox.open();

        setWidget(0, 0, type);
        showOrRemoveLink();
    }


    private void initBulkAddModal() {
        flexTable = new FlexTable();
        RootPanel.get().add(flexTable);
        flexTable.addStyleName("colums");
        flexTable.setHTML(0, 0, "");
        flexTable.setHTML(0, 1, "<b>" + wfmStrings.name() + "</b>");
        flexTable.setHTML(0, 2, "<b>" + wfmStrings.code() + "</b>");
        flexTable.setHTML(0, 3, "<b>" + wfmStrings.type() + "</b>");

        categorySearchBox = new TextBox();
        categorySearchBox.addStyleName("gwt-SuggestBox mb-3");
        categorySearchBox.setPlaceHolder(wfmStrings.search());
        categorySearchBox.setWidth("300px");
        categorySearchBox.addKeyDownHandler((event) -> {
            if (event.getNativeKeyCode() == 13) {
                if (!categorySearchBox.getValue().isEmpty()) {
                    filterParameter.setSearchKey(categorySearchBox.getValue().toLowerCase());
                }
                loadCategories();
            }
        });

        selectAll = new KpiCheckBox();
        selectAll.addValueChangeHandler(event -> {
            if (event.getValue())
                selectAll();
            else
                unSelectAll();
        });

        bulkAddModal = new KpiModal();
        bulkAddModal.setDismissible(false);
        bulkAddModal.setTitle(wfmStrings.bulkAdd() + " " + wfmStrings.categories());
        bulkAddModal.setWidth("1000px");
        bulkAddModal.setHeight("750px");

        HorizontalPanel navigation = new HorizontalPanel();
        navigation.add(selectAll);
        navigation.add(categorySearchBox);

        bulkAddModal.add(navigation);
        bulkAddModal.add(flexTable);

        ViewFooter footer = new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return null;
            }
        });
        footer.addToLeftSide(drawPaginationPanel());

        closeBulkAddBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        closeBulkAddBtn.addClickHandler(event -> {
            clearData();
            bulkAddModal.close();
        });

        applyBulkAddBtn = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        applyBulkAddBtn.addClickHandler(clickEvent -> {
            applyData();
            bulkAddModal.close();
            if (selectedRows.size() > 0) {
                Info.show(wfmStrings.categories() + " " + wfmStrings.successfully() + " " + wfmStrings.applied());
            }
        });

        footer.addToRightSide(closeBulkAddBtn);
        footer.addToRightSide(applyBulkAddBtn);
        footer.setWidth("1000px");

        bulkAddModal.addButton(footer);
    }

    private void loadCategories() {
        LoadingPanel.loading(true);
        ListingFilterParameter fp = getFilterParameter();

        AllInOneService.App.get().getCategoriesForBulkAdd(fp, new AsyncCallback<BulkAddCategoriesItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BulkAddCategoriesItem result) {
                LoadingPanel.loading(false);
                flexTable.clear();
                loadedCategories.clear();
                setPaginationData(result);
                initCategoryItems(result.getPaymentDeductionSelectItems());
            }
        });
    }

    private void initCategoryItems(PaymentDeductionSelectItem[] categories) {
        if (categories != null)
            loadedCategories.addAll(Arrays.asList(categories));

        initData();
    }

    private void initData() {
        AtomicInteger row = new AtomicInteger(1);
        loadedCategories.forEach(item -> {
            CheckBox check = new CheckBox();
            check.addValueChangeHandler(event -> {
                if (event.getValue())
                    selectedRows.add(item);
                else
                    selectedRows.remove(item);
            });

            TextBox name = new TextBox();
            name.setReadOnly(true);
            name.setValue(item.getName());

            TextBox code = new TextBox();
            code.setReadOnly(true);
            code.setValue(item.getCode());

            TextBox type = new TextBox();
            type.setReadOnly(true);
            type.setValue(item.getType());

            check.ensureDebugId("check_" + row);
            name.ensureDebugId("name_" + row);
            code.ensureDebugId("code_" + row);
            type.ensureDebugId("type_" + row);

            flexTable.setWidget(row.get(), 0, check);
            flexTable.setWidget(row.get(), 1, name);
            flexTable.setWidget(row.get(), 2, code);
            flexTable.setWidget(row.get(), 3, type);
            row.getAndIncrement();
        });
    }

    private void selectAll() {
        selectedRows.clear();
        for (int i = 1; i <= loadedCategories.size(); i++) {
            CheckBox box = getCheckBoxAtCell(i, 0);
            if (box != null)
                box.setValue(true);
        }
        selectedRows.addAll(loadedCategories);
    }

    private void unSelectAll() {
        for (int i = 1; i <= loadedCategories.size(); i++) {
            CheckBox box = getCheckBoxAtCell(i, 0);
            if (box != null)
                box.setValue(false);
        }
        selectedRows.clear();
    }

    private CheckBox getCheckBoxAtCell(int rowIndex, int cellIndex) {
        Widget widget = flexTable.getWidget(rowIndex, cellIndex);
        if (widget instanceof CheckBox)
            return (CheckBox) widget;

        return null;
    }

    private void initPaginationWidgets() {
        if (tableListLimitBox == null) {
            tableListLimitBox = new DataListBox();
            tableListLimitBox.setWithoutNullLabel(true);
            tableListLimitBox.setItems(new SelectItem[]{
                    new SelectItem(20, "20"),
                    new SelectItem(40, "40"),
                    new SelectItem(60, "60"),
                    new SelectItem(80, "80"),
                    new SelectItem(100, "100"),
            });
            tableListLimitBox.setSelected(new SelectItem(40, "40"));
        }

        if (tableCurrentBox == null) {
            tableCurrentBox = new TextBox();
            tableCurrentBox.setStyleName("currLoc form-control");
            tableCurrentBox.setValue(tableCurrent.toString());
        }

        if (tablePagingResult == null) {
            tablePagingResult = new MaterialLink();
            tablePagingResult.setHref("javascript:void(0)");
            tablePagingResult.setClass("btn btn--white");
            tablePagingResult.setText("0 - 0 of 0");
        }
    }

    private Widget drawPaginationPanel() {
        this.initPaginationWidgets();

        GBoxItem limitField = new GBoxItem(tableListLimitBox);
        limitField.setWidth("100px");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        MaterialLink prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);

        GBoxItem currentItem = new GBoxItem(tableCurrentBox);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        MaterialLink nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);

        GBoxRow row = new GBoxRow();
        row.add(new GBoxItem(tablePagingResult));
        row.add(limitField);
        row.add(new GBoxItem(prevLink));
        row.add(currentItem);
        row.add(new GBoxItem(nextLink));
        row.addStyleName("ml-5");
        prevLink.addClickHandler((event) -> {
            Integer totalPagesSize = Optional.ofNullable(tableListLimitBox.getSelectedId()).orElse(20);
            tableStart -= totalPagesSize;
            if (tableStart <= 0)
                tableStart = 0;

            loadCategories();
            selectAll.setValue(false);
        });

        nextLink.addClickHandler((event) -> {
            Integer selectedLimit = Optional.ofNullable(tableListLimitBox.getSelectedId()).orElse(20);
            int totalPages = totalTableItems / selectedLimit + (totalTableItems % selectedLimit > 0 ? 1 : 0);
            int currentPage = tableStart / selectedLimit + 1;

            if (currentPage >= totalPages)
                return;

            tableStart += selectedLimit;
            loadCategories();
            selectAll.setValue(false);
        });

        tableListLimitBox.addValueChangeHandler(event -> {
            clearData();
            loadCategories();
        });

        return row;
    }

    void setPaginationData(BulkAddCategoriesItem result) {
        PaymentDeductionSelectItem[] items = result.getPaymentDeductionSelectItems();
        if (items == null || items.length == 0) {
            clearData();
            return;
        }
        totalTableItems = result.getTotal();

        int pageSize = Optional.ofNullable(tableListLimitBox.getSelectedId()).orElse(20);
        int position = Optional.ofNullable(tableStart).orElse(0);

        tableCurrent = position / pageSize + 1;

        tableCurrentBox.setValue(tableCurrent.toString());
        tablePagingResult.setText((position + 1) + " - " + ((position + pageSize) < totalTableItems ? (position + pageSize) : totalTableItems) + " " + wfmStrings.of() + " " + totalTableItems);
    }

    public void addItem(PaymentDeductionObject item, boolean fromPaymentsTable) {
        CategoryLookUp categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        categoryLookUp.setEnabled(!fromPaymentsTable);
        if (item != null && item.getCategoryItem() != null) {
            if (isNotInTable(item.getCategoryItem().getId())) {
                categoryLookUp.addCategoryItem(item.getCategoryItem());
                existingItems.put(item.getCategoryItem().getId(), item.getCategoryItem());
            }
        }
        categoriesTable.addRow(new Object[]{categoryLookUp});
    }

    private void addItemToAllowance(PaymentDeductionSelectItem item) {
        CategoryLookUp categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        if (item != null) {
            categoryLookUp.addCategoryItem(item);
            existingItems.put(item.getId(), item);
            categoriesTable.addRow(new Object[]{categoryLookUp});
        }
    }

    private void applyData() {
        CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(0, "category");
        if (categoryLookUp != null && categoryLookUp.getSelectedData() == null) {
            categoriesTable.getGrid().getModel().removeRow(0);
        }

        selectAll.setValue(false);
        selectedRows.stream()
                .filter(category -> isNotInTable(category.getId()))
                .forEach(this::addItemToAllowance);
    }

    private boolean isNotInTable(Integer categoryId) {
        return !existingItems.containsKey(categoryId);
    }


    protected ListingFilterParameter getFilterParameter() {
        if (filterParameter == null) filterParameter = new ListingFilterParameter();
        filterParameter.setStart(Optional.ofNullable(tableStart).orElse(0));
        filterParameter.setLimit(Optional.ofNullable(tableListLimitBox.getSelectedId()).orElse(20));
        return filterParameter;
    }

    private void clearData() {
        tableStart = 0;
        tableCurrent = 0;
        totalTableItems = 0;
        selectedRows.clear();
        loadedCategories.clear();
        selectAll.setValue(false);
        tableCurrentBox.setValue("0");
        tablePagingResult.setText("0");
        categorySearchBox.setValue("");
        filterParameter = new ListingFilterParameter();
    }

    public void showOrRemoveLink() {
        if (type.getSelectedId() != null && type.getSelectedId().equals(POPUP_INDEX)) {
            setWidget(0, 1, link);
        } else if (isCellPresent(0, 1)) {
            removeCell(0, 1);
        }
    }

    public Boolean isFromAllAllowances() {
        return copyFromPaymentTable.getValue();
    }

    public List<PaymentDeductionObject> getLinkedCategories() {
        List<PaymentDeductionObject> linkedCategories = new ArrayList<>();
        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
            if (categoryLookUp.getSelectedData() != null) {
                PaymentDeductionObject object = new PaymentDeductionObject();
                object.setCategoryItem(categoryLookUp.getSelectedData());
                linkedCategories.add(object);
            }
        }
        return linkedCategories;
    }

    public void setLinkedItems(List<PaymentDeductionObject> linkedCategories) {
        categoriesTable.removeAllRows();
        for (PaymentDeductionObject item : linkedCategories) {
            addItem(item, false);
        }
    }

    public void setSelected(Integer id) {
        type.setSelected(id);
    }

    public Integer getSelectedId() {
        return type.getSelectedId();
    }


    @Override
    public String getDisplayValue() {
        return type.getSelectedItem().getName();
    }

    @Override
    public String getLinkValue() {
        return wfmStrings.allowance();
    }

    @Override
    public void setItemValue(Object value) {
        setSelected((Integer) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        type.setFocus(focused);
    }

    @Override
    public Command getClickHandler() {
        return clickHandler;
    }

    public Command getChangeHandler() {
        return changeHandler;
    }

    public void setChangeHandler(Command changeHandler) {
        this.changeHandler = changeHandler;
    }

    public void setCopyFromBoxHandler(Command copyFromBoxHandler) {
        this.copyFromBoxHandler = copyFromBoxHandler;
    }

    @Override
    public boolean isShowLink() {
        return type.getSelectedId().equals(POPUP_INDEX);
    }

    public void clearCategoriesTable() {
        categoriesTable.removeAllRows();
    }

    public void setValue(boolean fromAllAllowances) {
        copyFromPaymentTable.setValue(fromAllAllowances);
    }

    public void setEnabled(boolean enable) {
        type.setEnabled(enable);
        link.setEnabled(enable);
    }
}
