package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeItem;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeTableListener;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeTablePagingOptions;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmToolBar;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableChildProvider;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableColumn;
import com.edatasite.workforce.gwt.documents.client.table.DataSourceModel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/25/12
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeListingPanel<T extends WfmTreeItem> extends Composite {

    public static int ITEMS_PER_PAGE = 20;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    // tree table with childs
    private boolean isSelectGetTreeChilds;
    // Listing Table
    private PagingTreeTable<T> pagingScrollTable;
    // Columns Configs
    private DefaultTableDefinition<T> tableDefinition;
    // List Panel type
    private ListPanelType type;
    // Custom page size
    private int customPageSize = -1;
    // Listing Table Column Configs
    public List<WfmTreeTableColumn> columnConfigs;
    // Listing Table Column Configs for render
    public WfmTreeTableColumn[] columns;
    // Top and Bottom Panels Design Settings
    private ListingPanelDesign listingPanelDesign;
    // Listing panel provider
    private TreeListingRequestProvider listingRequestProvider;
    // Child provider
    private WfmTreeTableChildProvider childProvider;
    //List Panel draw row data
    private ListResult<T> rowsData;
    // Empty Table
    private DockPanel emptyTable;
    // Paging Total
    private HTML totalLabel;
    // Search Box
    private TextBox searchBox = new TextBox();
    // Search Button
    private ActionButton search = new ActionButton("&nbsp;", "btnSearch");
    // Header Tool Bar
    private WfmToolBar headerToolBar;
    // Footer Tool Bar
    private WfmToolBar footerToolBar;
    //Paging Options
    private TreeTablePagingOptions pagingOptions;
    // Excell Export option
    private ActionButton xlsVersion;
    // PDF Export option
    private ActionButton pdfVersion;
    // Customise Button
    private ActionButton customise;
    //delete Button
    private ActionButton deleteButton;
    // Refresh
    private ActionButton refresh;
    // sorting functionality
    private Command sortingCommand;
    // Owner Table
    private DockLayoutPanel flexTable = new DockLayoutPanel(Style.Unit.PX);
    // User Checked data repository
    private ListingFilterParameter filterParametrs;
    // Page Change
    private boolean isPageChange = false;
    // Actions Menu tooltip
    private ToolItem tooltip;
    FlowPanel searchPanel = new FlowPanel();
    // on Reset Button...
    private Command onReset;
    // Export option Panel
    private HTMLPanel exportPanel;

    private TreeListingCallBack listingCallback;

    private TableModel.Callback treeListingCallback;


    public TreeListingPanel(ListPanelType type, WfmTreeTableColumn[] columns, TreeListingRequestProvider listingRequestProvider, WfmTreeTableChildProvider childProvider, ListingPanelDesign listingPanelDesign) {
        this(type, columns, listingRequestProvider, childProvider, listingPanelDesign, false);
    }

    public TreeListingPanel(ListPanelType type, WfmTreeTableColumn[] columns, TreeListingRequestProvider listingRequestProvider, WfmTreeTableChildProvider childProvider, ListingPanelDesign listingPanelDesign, boolean isSelectGetTreeChilds) {
        this.type = type;
        columnConfigs = new ArrayList<>(Arrays.asList(columns));
        this.columns = columns;
        this.listingRequestProvider = listingRequestProvider;
        this.childProvider = childProvider;
        this.listingPanelDesign = listingPanelDesign;
        this.isSelectGetTreeChilds = isSelectGetTreeChilds;
        initWidget(flexTable);
        listingCallback = new TreeListingCallBack<T>() {
            @Override
            public void onFailure(Throwable throwable, TreeItem item) {
                pagingScrollTable.onPagingFailure();
            }

            @Override
            public void onSuccess(ListResult<T> data, TreeItem item) {
                pagingScrollTable.onPageLoad();
                if (item != null) {
                    if ((data == null || (data.getList() == null || data.getList().size() == 0))) {
                        item.setHaveChilds(false);
                    }
                    item.updateState();
                    item.setClickedTree(true);
                } else {
                    rowsData = data;
                    setTotal(data.getTotal());
                    pagingScrollTable.getTableModel().setRowCount(data.getTotal());
                    pagingScrollTable.changePageCount();
                    pagingScrollTable.getTreeTable().removeItems();
                }
                fillTreeTable(data, item);
            }
        };

        getListingPanelSettings();
    }

    /**
     * Get Listing Panel Tools
     */
    private void getListingPanelSettings() {
        LoadingPanel.loading(true);
        CommonService.App.get().getUserListPanelSettings(type, null, null, null, new AbstractAsyncCallback<ListPanelToolRpc>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListPanelToolRpc settings) {
                LoadingPanel.loading(false);

                if (settings != null && settings.getPageSize() > 0) {
                    ITEMS_PER_PAGE = settings.getPageSize();
                }
                initialization(settings);
                reloadPage();
            }
        });
    }

    private void fillTreeTable(ListResult<T> data, TreeItem treeItem) {
        if (data != null && data.getList() != null) {
            for (T item : data.getList()) {
                if (treeItem != null) {
                    treeItem.addItem(item, childProvider.isHaveChilds(item));
                } else {
                    pagingScrollTable.getTreeTable().addItem(item, childProvider.isHaveChilds(item));
                }
            }
        }
    }

    private void initialization(ListPanelToolRpc listPanelTools) {
        initMap();

        headerToolBar = new WfmToolBar();
        createtHeaderToolBarCompanents();

        createPagingScrollTable();

        pagingScrollTable.getTreeTable().addTreeTableListener(new TreeTableListener() {
            @Override
            public void onTreeItemSelected(TreeItem item) {
                Object itemObject = item.getUserObject();
                if (item.isHaveChilds() && !item.isClickedTree()) {
                    item.updateToLoading();
                    listingRequestProvider.getRequest(itemObject, item, getFilterParametrs(), listingCallback);
                } else {
                    item.updateToLoading();
                }
            }

            @Override
            public void onTreeItemStateChanged(TreeItem item) {

            }
        });

        sortingCommand = () -> {
            getFilterParametrs().setSortField(pagingScrollTable.getSortField());
            getFilterParametrs().setSortDir(pagingScrollTable.isAscending() ? 1 : 2);
            reloadPage();

        };

        pagingScrollTable.setSortingListener(sortingCommand);


        footerToolBar = new WfmToolBar();
        createFooterToolBarCompanents();

        //if (!isHideToolMenu) {
        flexTable.addNorth(headerToolBar, 35);
        //}
        flexTable.addSouth(footerToolBar, 35);
        flexTable.add(pagingScrollTable);
        flexTable.setHeight("100%");
        flexTable.setWidth("100%");
    }


    public PagingTreeTable createPagingScrollTable() {
        // create our own table model // Data Source Model
        DataSourceModel<T> tableModel = createTableModel();

        pagingScrollTable = new PagingTreeTable(tableModel, columns);
        pagingScrollTable.setPageSize(ITEMS_PER_PAGE);

        emptyTable = new DockPanel();
        emptyTable.setSize("100%", "100%");
        emptyTable.getElement().getStyle().setMarginTop(12, Style.Unit.PCT);
        emptyTable.getElement().getStyle().setPaddingTop(10, Style.Unit.PX);
        emptyTable.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        emptyTable.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        pagingScrollTable.setEmptyTableWidget(emptyTable);
        pagingScrollTable.setHeight("100%");
        return pagingScrollTable;
    }


    /**
     * All data set
     *
     * @return Data Source Model
     */
    private DataSourceModel<T> createTableModel() {
        return new DataSourceModel<T>() {
            @Override
            public void requestRows(TableModelHelper.Request request, Callback<T> callback) {
                getFilterParametrs().setStart(request.getStartRow());
                getFilterParametrs().setLimit(request.getNumRows());

                listingCallback.setRequest(request);
                listingCallback.setCallback(callback);


                listingRequestProvider.getRequest(null, null, getFilterParametrs(), listingCallback);
            }
        };
    }

    /**
     * List Panel ListingFilterParameter
     *
     * @return ListingPanel checked data
     */
    public ListingFilterParameter getFilterParametrs() {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        return filterParametrs;
    }

    /**
     * Set header Tool Bar Companents
     */
    private void createtHeaderToolBarCompanents() {

        headerToolBar.addStyleName("group");
        searchPanel.addStyleName("searchForm");

        search.ensureDebugId("search_button");
        search.addClickHandler(event -> reset());
        searchBox.setMaxLength(255);
        searchBox.ensureDebugId("searchBox");
        searchBox.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                reset();
            }
        });
        searchPanel.add(searchBox);
        searchPanel.add(search);

        ActionButton newItem = listingPanelDesign.initTopToolBarNew();

        ActionButton moreItem = listingPanelDesign.initTopToolBarMore();

        ActionButton printItem = listingPanelDesign.initTopToolBarPrint();

        customise = new ActionButton("&nbsp;", "btnCustomize");
        customise.ensureDebugId("customise_button");
        customise.setTitle(wfmStrings.customize());
        customise.addClickHandler(clickEvent -> {
//                if (!settingsPopup.isShowing()) {
//                    settingsPopup.center();
//                    settingsPopup.show();
//                }
        });

        if (newItem != null) {
            headerToolBar.add(newItem, HorizontalPanel.ALIGN_LEFT);
        }
        if (moreItem != null) {
            headerToolBar.add(moreItem, HorizontalPanel.ALIGN_LEFT);
        }
        if (printItem != null) {
            headerToolBar.add(printItem, HorizontalPanel.ALIGN_LEFT);
        }
        headerToolBar.add(searchPanel, HorizontalPanel.ALIGN_LEFT);

        // Add to Top Panel reset button
        if (listingPanelDesign.isShowResetButton()) {
            ActionButton resetBtn = initializationResetButton();
            headerToolBar.add(resetBtn);
        }

        if (listingPanelDesign.getAddAdditionalPanel() != null) {
            headerToolBar.add(listingPanelDesign.getAddAdditionalPanel());
        }
        if (listingPanelDesign.isShowDeleteButton()) {
            headerToolBar.add(deleteButton, HorizontalPanel.ALIGN_RIGHT, "100%");
        }
        // Add to Top Panel customise button
        if (listingPanelDesign.isShowCustomiseButton()) {
            //  headerToolBar.add(customise, HorizontalPanel.ALIGN_RIGHT, "100%");
        } else {
            headerToolBar.add("&nbsp;", HorizontalPanel.ALIGN_RIGHT, "100%");
        }
    }

    /**
     * set Footer Tool Bar Components
     */
    private void createFooterToolBarCompanents() {
        // Paging option
        pagingOptions = new TreeTablePagingOptions(pagingScrollTable);
        pagingOptions.setWidth("150px");
        refresh = new ActionButton("&nbsp;", "btnReload");
        refresh.ensureDebugId("refresh_button");
        refresh.setTitle(wfmStrings.refresh());
        refresh.addClickHandler(clickEvent -> reloadPage());
        totalLabel = new HTML();
        totalLabel.addStyleName("pagingStat");
        totalLabel.setWidth("120px");
        footerToolBar.addStyleName("opers-2 group");
        footerToolBar.add(pagingOptions);
        footerToolBar.add(totalLabel);
        footerToolBar.add(refresh);
        final FlowPanel footerRigtBar = new FlowPanel();
        footerRigtBar.addStyleName("convertResults left-all");
        /*ExportImportOption exportImportOption = new ExportImportOption() {
            @Override
            public void initExport(FlowPanel bottomPanel) {
                initExport(bottomPanel, true);
            }

            @Override
            public void initExport(FlowPanel bottomPanel, Boolean showExport) {
                if (bottomPanel != null && bottomPanel.getWidgetCount() != 0) {
                    bottomPanel.addStyleName("left-all");
                    footerRigtBar.add(bottomPanel);
                }
                if (showExport) {
                    pdfVersion = getPdfVersion();
                    pdfVersion.ensureDebugId("pdf_button");

                    xlsVersion = getXlsVersion();
                    xlsVersion.ensureDebugId("excel_button");

                    HTMLPanel export = new HTMLPanel("dt", wfmStrings.export() + " " + wfmStrings.as() + " ");
                    exportPanel = new HTMLPanel("dl", "");
                    exportPanel.addStyleName("exportAs");
                    exportPanel.add(export);
                    HTMLPanel pdf = new HTMLPanel("dd", "");
                    pdf.add(pdfVersion);
                    HTMLPanel xls = new HTMLPanel("dd", "");
                    xls.add(xlsVersion);
                    exportPanel.add(pdf);
                    exportPanel.add(xls);

                    footerRigtBar.add(exportPanel);
                } else if (bottomPanel == null || bottomPanel.getWidgetCount() == 0) {
                    footerRigtBar.add(new HTML("&nbsp;"));
                }
            }
        };
        listingPanelDesign.initBottomToolBarWidgets(exportImportOption);*/
        footerToolBar.add(footerRigtBar);
    }

    private void reset() {
        getFilterParametrs().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
        pagingScrollTable.gotoPage(0, false);
        if (getFilterParametrs().getFacetFilter() != null) {
            getFilterParametrs().getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
        }
        getFilterParametrs().setSortField(null);
        reloadPage();
    }


    /**
     * Xsl set generate link
     *
     * @param clickEvent
     */
    public void setExcelListener(ClickHandler clickEvent) {
        getXlsVersion().addClickHandler(clickEvent);
    }

    /**
     * Pdf set generate link
     *
     * @param clickEvent
     */
    public void setPDFListener(ClickHandler clickEvent) {
        getPdfVersion().addClickHandler(clickEvent);
    }

    /**
     * @param pdfURL
     * @param filterParametrs
     */
    public void callListPDF(String pdfURL, ListingFilterParameter filterParametrs) {
        Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, getFilterRequestParam(filterParametrs), "_blank");
    }

    /**
     * @param excelURL
     * @param filterParametrs
     */
    public void callListExcel(String excelURL, ListingFilterParameter filterParametrs) {
        Utils.sendPDFOrExcelRequest(exportPanel, excelURL, getFilterRequestParam(filterParametrs), "_blank");
    }

    /**
     * <i> This is method uses in exprot option,generation excel and pdf  </i>
     *
     * @param filterParametrs
     * @return
     */
    private HashMap<String, String> getFilterRequestParam(ListingFilterParameter filterParametrs) {
        if (filterParametrs != null) {
            filterParametrs.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParametrs.getFacetFilter()));
            filterParametrs.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParametrs.getListPanelTool()));
            return filterParametrs.getRequestParams();
        }
        return null;
    }

    /**
     * Xsl toolItem Button
     *
     * @return
     */
    public ActionButton getXlsVersion() {
        if (xlsVersion == null) {
            xlsVersion = new ActionButton(wfmStrings.excel(), "markExcel");
        }
        return xlsVersion;
    }

    /**
     * Pdf toolItem Button
     *
     * @return
     */
    public ActionButton getPdfVersion() {
        if (pdfVersion == null) {
            pdfVersion = new ActionButton(wfmStrings.pdf(), "markPDF");
        }
        return pdfVersion;
    }

    /**
     * @param pdfURL
     * @param requestObject
     */
    public void callItemPDF(String pdfURL, RequestObject requestObject) {
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, parametrs, "_blank");
    }

    private ActionButton initializationResetButton() {
        ActionButton reset = new ActionButton("&nbsp", "btnRepair");
        reset.ensureDebugId("reset_button");
        reset.setTitle(wfmStrings.reset());
        reset.addClickHandler(event -> {
            if (onReset != null) {
                onReset.execute();
            }
            searchBox.setText("");
            getFilterParametrs().setSearchKey(searchBox.getText());
            reloadPage();
        });
        return reset;
    }

    /**
     * This is method return
     * in listing panel row items count
     */
    public int getItemCount() {
        return rowsData != null && rowsData.getList() != null ? rowsData.getList().size() : 0;
    }

    /**
     * Refresh Page
     */
    public void reloadPage() {
        if (pagingScrollTable != null) {
            pagingScrollTable.reloadPage();
        }
    }

    /**
     * Repository Column Config Map
     */
    private void initMap() {
//        for (TreeTableColumnConfig columnConfig : columnConfigs) {
//            mapColumn.put(columnConfig.getColumnCode(), columnConfig);
//        }

    }


    /**
     * Show Total Label
     *
     * @param total count
     */
    private void setTotal(Integer total) {
        if (total != 0) {
            totalLabel.setHTML(wfmStrings.displayItems() + ": <span>" + (filterParametrs.getStart() + 1) + " - " +
                    ((filterParametrs.getStart() + filterParametrs.getLimit()) < total ? (filterParametrs.getStart() + filterParametrs.getLimit()) : total) + " " + wfmStrings.of() + " " + total.toString() + "</span>");
            pagingScrollTable.setEmptyTableWidgetVisible(false);
        } else {
            totalLabel.setHTML(wfmStrings.displayItems() + ": <span>0 - 0 " + wfmStrings.of() + " 0 </span>");
            //initialize empty message
            initializeEmptyDataTable(searchBox.getText() == null || "".equals(searchBox.getText()));
            pagingScrollTable.setEmptyTableWidgetVisible(true);
        }
    }

    private void initializeEmptyDataTable(boolean isEmptyMessage) {
        emptyTable.clear();
        if (isEmptyMessage) {
            listingPanelDesign.initDataEmptyTable(emptyData -> {
                if (emptyData != null && emptyData.getWholeMessage() != null) {
                    emptyTable.add(emptyData.getWholeMessage(), DockPanel.CENTER);
                }
            });
        } else {
            emptyTable.add(new HTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria()), DockPanel.CENTER);
        }
    }

}



