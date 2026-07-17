package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.widgets.TabDataGrid;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

public class WPLPerProductLarge extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final int PAGE_SIZE = 30;
    private static final int ROW_HEIGHT = 24;

    private final Integer priceLevelId;
    private ListDataProvider<PriceLevelPPItem> dataProvider = null;
    private TabDataGrid<PriceLevelPPItem> cellTable = null;
    private final ProvidesKey<PriceLevelPPItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    private Div container;
    private TextBox txtSearch;
    private List<PriceLevelPPItem> productList;

    public WPLPerProductLarge(Integer priceLevelId, boolean isGettingDataOrDrawingData) {
        this.priceLevelId = priceLevelId;
        if (isGettingDataOrDrawingData) getDataForBarcode();
        else onInitialize();
    }

    private void onInitialize() {
        dataProvider = new ListDataProvider<>();
        cellTable = new TabDataGrid<>(PAGE_SIZE, KEY_PROVIDER);
        dataProvider.addDataDisplay(cellTable);
        cellTable.setStyleName("cellBasedWidget-mod mt-3");
        cellTable.setHeight("200px");

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, PAGE_SIZE, true);
        pager.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        pager.setDisplay(cellTable);
        pager.setStyleName("pg_fingetprintlist_pager");

        txtSearch = new TextBox();
        txtSearch.setPlaceHolder(wfmStrings.searchTypeMessage());
        txtSearch.setWidth("250px");
        txtSearch.addChangeHandler(ch -> loadData());

        WfmButton2 btnSearch = new WfmButton2(wfmStrings.search(), WfmButton2.BTN_WHITE_OUTLINE);
        btnSearch.addClickHandler(ch -> loadData());

        WfmButton2 btnAddProduct = new WfmButton2("Add Product", WfmButton2.BTN_PRIMARY);
        btnAddProduct.addClickHandler(ch -> new CustomPricePerProductModal(priceLevelId, new Command() {
            @Override
            public void execute() {
                loadData();
            }
        }));

        container = new Div();
        container.setClass("p-2, mt-3");
        container.add(new HorizontalPanelDiv(4, txtSearch, btnSearch, btnAddProduct));
        container.add(cellTable);
        container.add(pager);

        initWidget(container);

        initsializationStructure();
        loadData();
    }

    private void loadData() {
        LoadingPanel.loading(true, container);
        PriceLevelService.App.get().getPriceLevelPPItemList(priceLevelId, txtSearch.getText(), new AsyncCallback<ArrayList<PriceLevelPPItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<PriceLevelPPItem> priceLevelPPItems) {
                LoadingPanel.loading(false);
                cellTable.setHeight(((priceLevelPPItems.size() <= PAGE_SIZE ? priceLevelPPItems.size() : PAGE_SIZE)*ROW_HEIGHT+50) + "px");
                dataProvider.getList().clear();
                dataProvider.getList().addAll(priceLevelPPItems);
                dataProvider.refresh();
            }
        });
    }

 private void getDataForBarcode(){
     PriceLevelService.App.get().getPriceLevelPPItemList(priceLevelId, "", new AsyncCallback<ArrayList<PriceLevelPPItem>>() {
         @Override
         public void onFailure(Throwable throwable) {

         }

         @Override
         public void onSuccess(ArrayList<PriceLevelPPItem> priceLevelPPItems) {
             productList = priceLevelPPItems;
         }
     });
 }

    private void initsializationStructure() {
        Column<PriceLevelPPItem, SafeHtml> productColumn = new Column<PriceLevelPPItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(PriceLevelPPItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(item.getProductName());

                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(productColumn, wfmStrings.product());
        cellTable.setColumnWidth(productColumn, 200, Style.Unit.PX);

        Column<PriceLevelPPItem, SafeHtml> standardPriceColumn = new Column<PriceLevelPPItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(PriceLevelPPItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(AccountingUtils.get().formatUnitPrice(item.getStandarPrice() != null ? item.getStandarPrice() : 0));

                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(standardPriceColumn, wfmStrings.standardPrice());
        cellTable.setColumnWidth(standardPriceColumn, 100, Style.Unit.PX);

        Column<PriceLevelPPItem, SafeHtml> customPriceColumn = new Column<PriceLevelPPItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(PriceLevelPPItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(AccountingUtils.get().format(item.getCustomPrice(), AccountingUtils.getFractionLength(item.getCustomPrice())));
                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(customPriceColumn, wfmStrings.customPrice());
        cellTable.setColumnWidth(customPriceColumn, 100, Style.Unit.PX);

        Column<PriceLevelPPItem, String> editColumn = new Column<PriceLevelPPItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(PriceLevelPPItem priceLevelPPItem) {
                return wfmStrings.edit();
            }
        };
        editColumn.setFieldUpdater((index, object, value) -> {
            new CustomPricePerProductModal(object, new Command() {
                @Override
                public void execute() {
                    loadData();
                }
            });
        });
        cellTable.addColumn(editColumn, "");
        cellTable.setColumnWidth(editColumn, 60, Style.Unit.PX);

        Column<PriceLevelPPItem, String> removeColumn = new Column<PriceLevelPPItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(PriceLevelPPItem priceLevelPPItem) {
                return wfmStrings.delete();
            }
        };
        removeColumn.setFieldUpdater((index, object, value) -> {
            WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            message.setTitle(wfmStrings.confirmationMessage());
            message.setMessage(AccountingStrings.App.get().messDeleteThisItem());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true, container);
                    PriceLevelService.App.get().deletePriceLevelPPItem(object.getObjectId(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            loadData();
                        }
                    });
                }
            });
            message.open();
        });
        cellTable.addColumn(removeColumn, "");
        cellTable.setColumnWidth(removeColumn, 60, Style.Unit.PX);
        cellTable.setKeyboardSelectionPolicy(HasKeyboardPagingPolicy.KeyboardSelectionPolicy.DISABLED);
    }

    public List<PriceLevelPPItem> getProductList(){
        return productList;
    }
}
