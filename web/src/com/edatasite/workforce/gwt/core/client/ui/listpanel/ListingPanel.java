package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.KpiSelectBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetFilterPopup;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.CheckBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimeCustomFieldsCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.HTMLTextAreaCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.LookUpCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.RadioButtonCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextAreaCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ListingPanelSettingsPopup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.table.DataSourceModel;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanSettingsPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.gen2.table.client.AbstractScrollTable;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.gen2.table.client.TableModelHelper;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.TableEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Aug-2010
 * Time: 13:58:56
 * <p/>
 * <b> WFM new listing companent </b>
 */

@SuppressWarnings("unchecked")
public class ListingPanel<T> extends Composite {
    public static final String PENCIL_CSS = "cell-edit-icon";

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DateTimeFormat dateTimeShortFormat = DateTimeFormat.getFormat(Utils.userSettings.get(Constants.LONG_DATE_FORMAT) == null ?
            "YYYY-MMM-dd" : Utils.userSettings.get(Constants.SHORT_DATE_FORMAT));
    private static final NumberFormat numberFomat = Utils.getNumberFormat();
    private static final String STATUS = "status";
    // Top and Bottom Panels Design Settings
    public ListingPanelDesign listingPanelDesign;
    protected Div layoutPanel = new Div();
    boolean hasKanban = false;
    ActionButton moreItem;
    ActionButton printItem;
    ActionButton moreItemActions;
    private MaterialLink portrait;
    private MaterialLink landscape;
    // Listing Table
    private ListingPagingScrollTable<T> pagingScrollTable;
    // List Panel typeId
    private final Integer typeId;
    // Custom Fields category ID
    private final Integer customFieldCategoryID;
    private final Integer stepID;
    // List Panel type
    private final ListPanelType type;
    // Simple Filter type value
    private long simpleFilterValue = -1;
    //Custom page size
    private int customPageSize = -1;
    //List Panel draw row data
    private ListResult<T> rowsData;
    // Data Source Model Callback
    private ListingCallback<T> listingCallback;
    // Data Model Request Provider
    private final ListingRequestProvider<T> listingRequestProvider;
    // Listing Table Column Configs
    private final List<CustomColumnDefinitionConfig> columnConfigs;
    // Empty Table
    private FlowPanel emptyTable;
    // Paging Total
    private HTML totalLabel;
    // Search Box
    private final TextBox searchBox = new TextBox();
    // Search Button
    private final ActionButton search = new ActionButton("", "searchForm__btn");
    // Header Tool Bar
    private Div headerToolBar;
    // Excell Export option
    private MaterialLink xlsVersion;
    // PDF Export option
    private MaterialLink pdfVersion;
    //delete Button
    //private ActionButton deleteButton;
    // Refresh clicked functionality
    private Command command;
    // Owner Table
    private final TablePanel flexTable = new TablePanel();
    // Listing Settings Parametrs
    private ListPanelToolRpc listPanelTools;
    // User Checked data repository
    private ListingFilterParameter filterParametrs;
    // Listing Panel save changes tools
    private final ListingPanelSaveChanges listingPanelSaveChanges;
    // Listing Panel Facet Filter
    private ListingFacetFilter listingFacetFilter;
    // Listing Panel Facet Filer Popup
    private FacetFilterPopup facetFilterPopup;
    // Export option Panel
    private HTMLPanel exportPanel;
    // Column Code Name Maping with ColumnDefinitionConfig
    private final LinkedHashMap<String, CustomColumnDefinitionConfig> mapColumn = new LinkedHashMap<>();
    // Selection Police type
    private final SelectionGrid.SelectionPolicy selectionPolicy;
    // Selected Rows
    private final HashSet<T> selectedRows = new HashSet<>();
    // Page Change
    private boolean isPageChange = false;
    // Click Event Top Left menu
    private final boolean clickEvent = false;
    // Add Selection Hanlder
    private final ArrayList<ListingPanelRowSelectionHandler<T>> rowSelectionHandler = new ArrayList<>();
    // Cell Changes Save
    private CellChange cellChangesSave;
    // Facet Filter Lis Box
    private KpiSelectBox facetFilterSavedList;
    private final FlowPanel advancedFilterPanel;
    private int scrollTopPosition = 0;
    // Facet Filter configure
    private FacetContentConfigure facetContenConfigure;
    //Enable/Disable Start-End Date
    private Boolean EnableDate = true;
    // Simple Filter Poup
    private ListingChooseFilter chooseFilter;
    // After search box add widgets container
    private final FlowPanel searchPanel = new FlowPanel();
    private final FlowPanel listPagingPanel = new FlowPanel();
    // on Reset Button...
    private Command onReset;
    private T defaultOne;
    //    private int lastSortColumn = -1;
    private boolean isNowSearchButtonClicked = false;
    private boolean isShowFooterRow = false;
    private QuickViewPanel quickViewPanel = null;
    private boolean hasAdditionalInformation = false;
    private ClickHandler deleteHandler;
    private KanbanBoard<T> kanbanBoardView;
    private boolean isListingPage = true;
    private SvgIcon listKanbanSwitch;
    private MaterialTooltip switchTooltip;
    private final String loaderClass = "kpi-listingtable--loader";
    private Div filtersWrap;
    private final boolean reloadPageFilter = true;
    private final String formID;
    private boolean showCustomize = true;
    private boolean showAddInFilter = true;
    private boolean showFilters = true;
    private MaterialMenuBar importExportMenu;
    private ActionButton newItem;

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, SelectionGrid.SelectionPolicy.ONE_ROW);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, boolean showCustomize, boolean showAddInFilter, boolean showFilters) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, SelectionGrid.SelectionPolicy.ONE_ROW);
        this.showCustomize = showCustomize;
        this.showAddInFilter = showAddInFilter;
        this.showFilters = showFilters;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider,
                        ListingPanelDesign listingPanelDesign, ListingFilterParameter listingFilterParameter) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, SelectionGrid.SelectionPolicy.ONE_ROW);
        filterParametrs = listingFilterParameter;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, Integer customFieldCategoryID) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, SelectionGrid.SelectionPolicy.ONE_ROW, -1, false, customFieldCategoryID, null);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, Integer customFieldCategoryID, Integer stepID) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, SelectionGrid.SelectionPolicy.ONE_ROW, -1, false, customFieldCategoryID, stepID);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, -1, false, null, null);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider,
                        ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, ListingFilterParameter listingFilterParameter) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, -1, false, null, null);
        filterParametrs = listingFilterParameter;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, -1, false, null, null);
        this.hasAdditionalInformation = hasAdditionalInformation;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation, boolean showCustomize) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, -1, false, null, null);
        this.showCustomize = showCustomize;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation, boolean showCustomize, boolean showAddInFilter, boolean showFilters) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, -1, false, null, null);
        this.showCustomize = showCustomize;
        this.showAddInFilter = showAddInFilter;
        this.showFilters = showFilters;
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize) {
        this(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize, false, null, null);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID) {
        this(type, null, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize, false, customFieldCategoryID, stepID, false);
    }

    public ListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID, boolean showCustomize, boolean showFilters) {
        this(type, null, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize, false, customFieldCategoryID, stepID, false, showCustomize, showFilters);
    }

    public ListingPanel(ListPanelType type, String formID, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID, boolean hasKanban) {
        this.type = type;
        this.formID = formID;
        this.typeId = listingPanelDesign.getTypeParentId();
        this.columnConfigs = new ArrayList<>(Arrays.asList(columnConfigs));
        this.listingRequestProvider = listingRequestProvider;
        this.listingPanelDesign = listingPanelDesign;
        this.selectionPolicy = selectionPolicy;
        this.customPageSize = customPageSize;
        this.isShowFooterRow = isShowFooter;
        this.customFieldCategoryID = customFieldCategoryID;
        this.stepID = stepID;
        this.advancedFilterPanel = new FlowPanel();
        this.advancedFilterPanel.addStyleName("operPanel__advanced-filter");
        this.advancedFilterPanel.setVisible(false);
        this.hasKanban = hasKanban;
        initWidget(layoutPanel);


        // When user save Listing Panel Settings come in this interface
        listingPanelSaveChanges = listPanelTools -> {
            setListPanelTools(listPanelTools);
            getFilterParametrs().setSortField(listPanelTools.getSortBy());
            getFilterParametrs().setSortFieldType(listPanelTools.getSortByType());
            refreshTable();
        };

        initializeListingCallback();
        getListingPanelSettings();
    }

    public ListingPanel(ListPanelType type, String formID, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider<T> listingRequestProvider, ListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID, boolean hasKanban, boolean showCustomize, boolean showFilters) {
        this.type = type;
        this.formID = formID;
        this.typeId = listingPanelDesign.getTypeParentId();
        this.columnConfigs = new ArrayList<>(Arrays.asList(columnConfigs));
        this.listingRequestProvider = listingRequestProvider;
        this.listingPanelDesign = listingPanelDesign;
        this.selectionPolicy = selectionPolicy;
        this.customPageSize = customPageSize;
        this.isShowFooterRow = isShowFooter;
        this.customFieldCategoryID = customFieldCategoryID;
        this.stepID = stepID;
        this.advancedFilterPanel = new FlowPanel();
        this.advancedFilterPanel.addStyleName("operPanel__advanced-filter");
        this.advancedFilterPanel.setVisible(false);
        this.hasKanban = hasKanban;
        this.showCustomize = showCustomize;
        this.showFilters = showFilters;
        initWidget(layoutPanel);

        initializeListingCallback();

        getListingPanelSettings();

        // When user save Listing Panel Settings come in this interface
        listingPanelSaveChanges = listPanelTools -> {
            setListPanelTools(listPanelTools);
            getFilterParametrs().setSortField(listPanelTools.getSortBy());
            getFilterParametrs().setSortFieldType(listPanelTools.getSortByType());
            refreshTable();
        };
    }

    private void initializeListingCallback() {
        listingCallback = new ListingCallback<T>() {
            @Override
            public void onFailure(Throwable caught) {
                facetFilterSavedList.setEnabled(true);
                getCallback().onFailure(caught);

                loading(false);
            }

            @Override
            public void onSuccess(ListResult<T> data) {
                if (data == null) {
                    data = new ListResult<>();
                } else {
                    setDefaultOne(data.getDefaultOne());
                }
                final ListResult<T> dataList = data;
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess() {
                        onDataLoaded(dataList);
                        getCallback().onRowsReady(getRequest(), new TableModelHelper.SerializableResponse(dataList.getList() != null ? dataList.getList() : new ArrayList<T>()));
                        loading(false);
                    }
                });

            }
        };
    }

    protected void onDataLoaded(ListResult<T> dataList) {
        rowsData = dataList;
        setTotal(dataList.getTotal() != null ? dataList.getTotal() : 0);
        facetFilterSavedList.setEnabled(true);
        pagingScrollTable.getTableModel().setRowCount(dataList.getTotal() != null ? dataList.getTotal() : 0);
        Scheduler.get().scheduleFixedDelay(() -> {
            Utils.scrollTables_afterUpdate2();
            return false;
        }, 200);
        addTableToLayout();
    }

    /**
     * Get Listing Panel Tools
     */
    private void getListingPanelSettings() {
        LoadingPanel.loading(true);
        CommonService.App.get().getUserListPanelSettings(type, formID, customFieldCategoryID, stepID, new AbstractAsyncCallback<ListPanelToolRpc>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListPanelToolRpc settings) {
                LoadingPanel.loading(false);
                onPanelSettingsLoaded(settings);
            }
        });
    }

    protected void onPanelSettingsLoaded(ListPanelToolRpc settings) {
        initialization(settings);
        if (hasAdditionalInformation) {
            pagingScrollTable.setShowPopups(settings != null && settings.isShowPopup());
        }

        //Set last view state
        if ("kanban".equalsIgnoreCase(settings.getViewstate())) {
            setListingPage(!isListingPage());
            //set id to null otherwise it will have value of listingview filter
            getFilterParametrs().getFacetFilter().setObjectID(null);
            initListOrKanban();
        } else {
            //if list view (not kanban)
            reloadPage();
        }
    }

    /**
     * Change Listing Panel settings refresh all table
     */
    private void refreshTable() {
        pagingScrollTable.setTableDefinition(createTableDefinition());
        pagingScrollTable.setPageSize(listPanelTools.getPageSize());
        pagingScrollTable.setHeaderGenerated(true);
    }

    /**
     * Initialization Table widgets
     */
    private void initialization(ListPanelToolRpc listPanelTools) {
        addTableToLayout();
        checkedIsNotNull(listPanelTools);
        customFieldsColumnConfigs(listPanelTools.getListViewCustomFields());
        initMap();

        pagingScrollTable = createPagingScrollTable();

        if (getListingPanelDesign().getPageScrollTableId() != null) {
            pagingScrollTable.getElement().setId(getListingPanelDesign().getPageScrollTableId());
        }

        Div headerToolBarWrapper = new Div("operPanel__wrapper");
        headerToolBar = new Div("operPanel");
        headerToolBarWrapper.add(headerToolBar);

        createtHeaderToolBarCompanents();

        flexTable.setStyleName("listing-table-container file--ListingPanel");
        flexTable.add(headerToolBarWrapper);
        flexTable.add(pagingScrollTable);
    }

    /**
     * Set Custom Fields
     *
     * @param listViewCustomFields
     */
    private void customFieldsColumnConfigs(List<CompanyCustomFieldItem> listViewCustomFields) {

        if (listViewCustomFields != null) {
            for (final CompanyCustomFieldItem fieldItem : listViewCustomFields) {
                ColumnDefinitionConfig colConfig = null;

                if (Constants.DATA_TYPE_DATE.equals(fieldItem.getDataType())) {// registraction Date Custom Column config
                    colConfig = new ColumnDefinitionConfig<T, String>(fieldItem.getFieldName(), fieldItem.getColumnCode(), 80) {
                        @Override
                        public String getCellValue(T rowValue) {

                            if (rowValue instanceof ListingCustomFields) {
                                try {
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    DateNonConvertable date;

                                    if (customFields.getCustomFieldsValue(getCodeName()) instanceof Date) {
                                        date = new DateNonConvertable((Date) customFields.getCustomFieldsValue(getCodeName()));
                                    } else {
                                        date = (DateNonConvertable) customFields.getCustomFieldsValue(getCodeName());
                                    }
                                    if (date != null) {
                                        if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                                            return DateUtils.formatInternal(date.getNonConvertedDate());
                                        } else {
                                            return dateTimeShortFormat.format(date.getNonConvertedDate());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    if (customFields.getCustomFieldsValue(getCodeName()) != null) {
                                        DateNonConvertable date = new DateNonConvertable(DateTimeFormat.getFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(customFields.getCustomFieldsValue(getCodeName()).toString()));
                                        date = new DateNonConvertable(new Date(date.getNonConvertedDate().getTime() - (long) new Date().getTimezoneOffset() * 60 * 1000));

                                        if (date.getNonConvertedDate() != null) {
                                            if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                                                return DateUtils.formatInternal(date.getNonConvertedDate());
                                            } else {
                                                return dateTimeShortFormat.format(date.getNonConvertedDate());
                                            }
                                        }
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        public void setCellValue(T rowValue, String cellValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                try {
                                    if (cellValue != null && !"".equals(cellValue)) {
                                        if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                                            customFields.setCustomFieldsValue(getCodeName(), new DateNonConvertable(DateUtils.parseLongFormat(cellValue)));
                                        } else {
                                            customFields.setCustomFieldsValue(getCodeName(), new DateNonConvertable(DateUtils.parse(cellValue, dateTimeShortFormat)));
                                        }
                                    } else {
                                        customFields.setCustomFieldsValue(getCodeName(), null);
                                    }
                                } catch (DateFormatException e) {
                                    e.printStackTrace();
                                }
                                saveCellValue(rowValue);
                            }
                        }
                    };
                    if (getListingPanelDesign().isEditCustomFieldCell()) {
                        if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                            initDateTimeCellEditor(colConfig);
                        } else {
                            initDatePickerCellEditor(colConfig);
                        }
                    }
                } else if (Constants.DATA_TYPE_NUMBER.equals(fieldItem.getDataType())) {// registraction Number Custom Column config
                    colConfig = new ColumnDefinitionConfig<T, String>(fieldItem.getFieldName(), fieldItem.getColumnCode(), 80) {
                        @Override
                        public String getCellValue(T rowValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                try {
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    Double number = (Double) customFields.getCustomFieldsValue(getCodeName());
                                    if (number != null) {
                                        return Utils.getCalculationNumberFormatWithCustomScale(fieldItem.getScale() != null ? fieldItem.getScale() : 2).format(number);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        public void setCellValue(T rowValue, String cellValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                if (cellValue != null && !"".equals(cellValue)) {
                                    customFields.setCustomFieldsValue(getCodeName(), cellValue);
                                } else {
                                    customFields.setCustomFieldsValue(getCodeName(), null);
                                }
                                saveCellValue(rowValue);
                            }
                        }
                    };
                    if (getListingPanelDesign().isEditCustomFieldCell()) {
                        initColumnCellEditor(colConfig, fieldItem);
                    }
                } else if (Constants.UI_TYPE_AUTONUMBER.equals(fieldItem.getUiType())) {
                    colConfig = new ColumnDefinitionConfig<T, Widget>(fieldItem.getFieldName(), fieldItem.getColumnCode(), 80) {
                        @Override
                        public Widget getCellValue(T rowValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                try {
                                    FormItems formItems = (FormItems) rowValue;
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    String number = (String) customFields.getCustomFieldsValue(getCodeName());
                                    if (number != null) {
                                        Label autoNumber = new Label(number);
                                        if (Utils.hasPermission(formItems.getFormID() + "_SUMMARY_" + Utils.getCompanyID()) && !"Draft".equals(formItems.getStatus())) {
                                            autoNumber.setStyleName("uploadLinkStyle2");
                                            autoNumber.addClickHandler(clickEvent -> {
                                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + formItems.getObjectID() + "/" + stepID + "/" + formItems.getFormID() + "/" + formItems.getFormName(), number);
                                            });
                                        } else if (Utils.hasPermission(formItems.getFormID() + "_EDIT_" + Utils.getCompanyID()) && "Draft".equals(formItems.getStatus())) {
                                            autoNumber.setStyleName("uploadLinkStyle2");
                                            autoNumber.addClickHandler(clickEvent -> {
                                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + formItems.getObjectID() + "/" + stepID + "/" + formItems.getFormID() + "/" + formItems.getFormName(), number);
                                            });
                                        }
                                        return autoNumber;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        public void setCellValue(T rowValue, Widget cellValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                if (cellValue != null && !"".equals(((Label) cellValue).getText())) {
                                    customFields.setCustomFieldsValue(getCodeName(), Double.valueOf(((Label) cellValue).getText()));
                                } else {
                                    customFields.setCustomFieldsValue(getCodeName(), null);
                                }
                                saveCellValue(rowValue);
                            }
                        }
                    };
                    if (getListingPanelDesign().isEditCustomFieldCell()) {
                        initColumnCellEditor(colConfig, fieldItem);
                    }
                } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                    colConfig = new ColumnDefinitionConfig<T, Widget>(fieldItem.getFieldName(), fieldItem.getColumnCode(), 80) {
                        @Override
                        public Widget getCellValue(T rowValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                try {
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    String urlString = (String) customFields.getCustomFieldsValue(getCodeName());
                                    if (urlString != null) {
                                        Label urlLabel = new Label(urlString);
                                        urlLabel.setStyleName("uploadLinkStyle2");
                                        urlLabel.addClickHandler(clickEvent -> {
                                            String url = "";
                                            if (urlString.contains("https://")) {
                                                url = urlString.split("https://")[1];
                                            } else if (urlString.contains("http://")) {
                                                url = urlString.split("http://")[1];
                                            } else {
                                                url = urlString;
                                            }

                                            Window.open("//" + url, "_blank", null);
                                        });

                                        return urlLabel;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        public void setCellValue(T rowValue, Widget cellValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                customFields.setCustomFieldsValue(getCodeName(), cellValue);
                                saveCellValue(rowValue);
                            }

                        }
                    };
                    if (getListingPanelDesign().isEditCustomFieldCell()) {
                        initColumnCellEditor(colConfig, fieldItem);
                    }
                } else { // registraction String Custom Column config
                    colConfig = new ColumnDefinitionConfig<T, String>(fieldItem.getFieldName(), fieldItem.getColumnCode(), 80) {
                        @Override
                        public String getCellValue(T rowValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                try {
                                    ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                    String stringValue = (String) customFields.getCustomFieldsValue(getCodeName());
                                    if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType()) && fieldItem.getRelationFieldId() != null) {
                                        CompanyCustomFieldItem parentItem = listViewCustomFields.stream().filter(customFieldItem -> customFieldItem.getObjectId().equals(fieldItem.getRelationFieldId())).findAny().orElse(null);
                                        if (parentItem != null) {
                                            String parentValue = (String) customFields.getCustomFieldsValue(parentItem.getColumnCode());
                                            if (parentValue != null && !parentValue.isEmpty()) {
                                                setItems(fieldItem.getChildItemsFromParent(parentValue).toArray(new SelectItem[]{}));
                                            } else {
                                                setItems(new SelectItem[]{});
                                            }
                                        }
                                    }
                                    if (stringValue != null) {
                                        if ((Constants.UI_TYPE_ENTITY_DROPDOWN.equals(fieldItem.getUiType())
                                                || Constants.TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType()))
                                                && fieldItem.getQueryItems() != null) {
                                            for (SelectItem selectItem : fieldItem.getQueryItems()) {
                                                if (stringValue.equals(String.valueOf(selectItem.getId()))) {
                                                    stringValue = selectItem.getName();
                                                    break;
                                                }
                                            }
                                            return stringValue;
                                        } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                                            if (stringValue.contains("->")) {
                                                Integer index = stringValue.indexOf("->");
                                                if (index != null) {
                                                    stringValue = stringValue.substring(index + 2);
                                                }
                                            }
                                            return stringValue;
                                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                                            String value = "";
                                            JSONValue jsonValue = JSONParser.parseStrict(stringValue);
                                            JSONArray jsonArray = jsonValue.isArray();

                                            if (jsonArray != null && jsonArray.size() > 0) {
                                                for (int i = 0; i < jsonArray.size(); i++) {
                                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                                    if (jsonObject != null) {
                                                        JSONObject key = (JSONObject) jsonObject.get("valueMap");
                                                        if (key != null) {
                                                            JSONValue name = key.get("name");
                                                            if (name != null && !Utils.isNullOrEmpty(String.valueOf(name))) {
                                                                value += String.valueOf(name).substring(1, String.valueOf(name).length() - 1) + "; ";
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            return value;
                                        } else {
                                            return stringValue.replace("-:-", ", ");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        @Override
                        public void setCellValue(T rowValue, String cellValue) {
                            if (rowValue instanceof ListingCustomFields) {
                                ListingCustomFields customFields = (ListingCustomFields) rowValue;
                                customFields.setCustomFieldsValue(getCodeName(), cellValue);
                                saveCellValue(rowValue);
                            }
                        }
                    };
                    if (getListingPanelDesign().isEditCustomFieldCell()) {
                        initColumnCellEditor(colConfig, fieldItem);
                    }
                }
                colConfig.setShow(fieldItem.isShow());
                colConfig.setColumnSortable(true);
                colConfig.setIsClickable(fieldItem.isClickable());
                colConfig.setMinimumColumnWidth(40);
                columnConfigs.add(colConfig);
            }
        }
    }

    /**
     * Init Column Cell Editor
     *
     * @param colConfig
     * @param fieldItem
     */
    private void initColumnCellEditor(ColumnDefinitionConfig colConfig, CompanyCustomFieldItem fieldItem) {
        if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
            initTextBoxCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
            initPercentageCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_RADIOBUTTON.equals(fieldItem.getUiType())) {
            initRadioButtonCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
            initDropDownCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_CHECKBOX.equals(fieldItem.getUiType())) {
            initCheckBoxCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
            initTextAreaCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
            initHTMLTextAreaCellEditor(colConfig, fieldItem);
        } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
            initLookUpCellEditor(colConfig, fieldItem);
        }
    }

    /**
     * DropDown Cell Editor for custom fields
     *
     * @param colConfig
     * @param customFieldItem
     */
    private void initDropDownCellEditor(ColumnDefinitionConfig colConfig, final CompanyCustomFieldItem customFieldItem) {
        DropDownCellEditor<String> listBoxCellEditor = new DropDownCellEditor<String>() {
            protected String getValue() {
                return getSelectName();
            }

            protected void setValue(String cellValue) {
                if (cellValue != null && !"".equals(cellValue)) {
                    if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                        setSelectDoubleValueName(cellValue);
                    } else {
                        setSelectName(cellValue);
                    }
                } else {
                    setDefaultValue();
                }
            }
        };
        if (customFieldItem.getPredefinedValues() != null) {
            String[] splitValue = customFieldItem.getPredefinedValues();
            SelectItem[] items = new SelectItem[splitValue.length];
            for (int i = 0; i < splitValue.length; i++) {
                items[i] = new SelectItem(splitValue[i].hashCode(), splitValue[i]);
            }
            listBoxCellEditor.setItems(items);
        }
        colConfig.setCellEditor(listBoxCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * LookUp Cell Editor for custom fields
     *
     * @param colConfig
     * @param customFieldItem
     */
    private void initLookUpCellEditor(ColumnDefinitionConfig colConfig, final CompanyCustomFieldItem customFieldItem) {
        CustomFieldLookUp lookUp = new CustomFieldLookUp(customFieldItem);
        LookUpCellEditor<String> listBoxCellEditor = new LookUpCellEditor<String>(lookUp) {
            protected String getValue() {
                return getText();
            }

            protected void setValue(String cellValue) {
                if (cellValue != null && !"".equals(cellValue)) {
                    setText(cellValue);
                }
            }
        };
        colConfig.setCellEditor(listBoxCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * CheckBoc Cell Editor for custom fields
     *
     * @param colConfig
     * @param customFieldItem
     */
    private void initCheckBoxCellEditor(ColumnDefinitionConfig colConfig, final CompanyCustomFieldItem customFieldItem) {
        CheckBoxCellEditor<String> checkBoxCellEditor = new CheckBoxCellEditor<String>() {
            //            @Override
            protected String getValue() {
                return getCheckedValues();
            }

            protected void setValue(String cellValue) {
                if (cellValue != null && !"".equals(cellValue)) {
                    if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                        setCheckBoxDoubleValues(cellValue);
                    } else {
                        setCheckedBoxValues(cellValue);
                    }
                } else {
                    clearPrev();
                }
            }
        };
//        checkBoxCellEditor.getVp().setWidth(80 + "px");
        if (customFieldItem.getPredefinedValues() != null) {
            String[] splitValue = customFieldItem.getPredefinedValues();
            for (String value : splitValue) {
                KpiCheckBox checkBox = new KpiCheckBox(value, true);
                checkBoxCellEditor.addCheckBox(checkBox);
            }
        }
        colConfig.setCellEditor(checkBoxCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * TextBox Cell Editor for custom fields
     *
     * @param colConfig
     */
    private void initTextBoxCellEditor(ColumnDefinitionConfig colConfig, CompanyCustomFieldItem customFieldItem) {
        TextBoxCellEditor<String> textBoxCellEditor = new TextBoxCellEditor<String>() {
            //            @Override
            protected String getValue() {
                return getText();
            }

            //            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
            textBoxCellEditor.addNumberValidation(false);
        }
        colConfig.setCellEditor(textBoxCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(customFieldItem.getUiType())) {
                    if (textBoxCellEditor.validateField(customFieldItem.getUiType())) {
                        Info.warn(wfmStrings.mustBeEmailFormat(), Info.Position.BOTTOM_RIGHT);
                    } else {
                        cellChangesSave.saveCell(rowValue, columnCodeName);
                    }
                } else if (Constants.UI_TYPE_URL.equals(customFieldItem.getUiType())) {
                    if (textBoxCellEditor.validateField(customFieldItem.getUiType())) {
                        Info.warn(wfmStrings.mustBeUrlFormat(), Info.Position.BOTTOM_RIGHT);
                    } else {
                        cellChangesSave.saveCell(rowValue, columnCodeName);
                    }
                } else {
                    cellChangesSave.saveCell(rowValue, columnCodeName);
                }
            }
        });
    }

    /**
     * TextBox Cell Editor for custom fields
     *
     * @param colConfig
     */
    private void initPercentageCellEditor(ColumnDefinitionConfig colConfig, CompanyCustomFieldItem customFieldItem) {
        TextBoxCellEditor<String> textBoxCellEditor = new TextBoxCellEditor<String>() {
            //            @Override
            protected String getValue() {
                return getText();
            }

            //            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        textBoxCellEditor.addPercentageNumberValidation(2, 100);
        colConfig.setCellEditor(textBoxCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                if (textBoxCellEditor.validatePercentage()) {
                    cellChangesSave.saveCell(rowValue, columnCodeName);
                } else {
                    Info.warn(wfmStrings.mustBeLessThan100(), Info.Position.BOTTOM_RIGHT);
                }
            }
        });
    }

    /**
     * TextArea Cell Editor for custom fields
     *
     * @param colConfig
     */
    private void initTextAreaCellEditor(ColumnDefinitionConfig colConfig, CompanyCustomFieldItem customFieldItem) {
        TextAreaCellEditor<String> textAreaCellEditor = new TextAreaCellEditor<String>() {
            protected String getValue() {
                return getText();
            }

            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        colConfig.setCellEditor(textAreaCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * HTMLTextArea Cell Editor for custom fields
     *
     * @param colConfig
     */
    private void initHTMLTextAreaCellEditor(ColumnDefinitionConfig colConfig, CompanyCustomFieldItem customFieldItem) {
        HTMLTextAreaCellEditor<String> htmlTextAreaCellEditor = new HTMLTextAreaCellEditor<String>() {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        colConfig.setCellEditor(htmlTextAreaCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * RadioButton  Cell Editor for custom fields
     *
     * @param colConfig
     * @param customFieldItem
     */
    private void initRadioButtonCellEditor(ColumnDefinitionConfig colConfig, final CompanyCustomFieldItem customFieldItem) {
        RadioButtonCellEditor<String> radioBtnCellEditor = new RadioButtonCellEditor<String>() {
            protected String getValue() {
                return getCheckedValue();
            }

            protected void setValue(String cellValue) {
                if (cellValue != null && !"".equals(cellValue)) {
                    if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                        setCheckedDoubleValue(cellValue);
                    } else {
                        setCheckedValue(cellValue);
                    }
                } else {
                    clearPrev();
                }
            }
        };
        if (customFieldItem.getPredefinedValues() != null) {
            String[] splitValue = customFieldItem.getPredefinedValues();
            for (String value : splitValue) {
                KpiRadioButton radioBtn = new KpiRadioButton("radioBtnEdit", value);
                radioBtnCellEditor.addRadio(radioBtn);
            }
        }
        colConfig.setCellEditor(radioBtnCellEditor);
        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    /**
     * Date Picker Cell Editor for custom fields
     *
     * @param colConfig
     */
    private void initDatePickerCellEditor(ColumnDefinitionConfig colConfig) {
        DateTimePickerCellEditor<String> datePickerCellEditor = new DateTimePickerCellEditor<String>() {
            protected String getValue() {
                if (getDate() != null) {
                    return dateTimeShortFormat.format(getDate());
                }
                return null;
            }

            protected void setValue(String cellValue) {
                try {
                    DateNonConvertable date;
                    if (cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equals(cellValue)) {
                        date = new DateNonConvertable();
                        setDefaultValue(true);
                    } else {
                        date = new DateNonConvertable(DateUtils.parse(cellValue));
                        setDefaultValue(false);
                    }
                    setDate(date.getNonConvertedDate(), false);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setCellEditor(datePickerCellEditor);

        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
    }

    private void initDateTimeCellEditor(ColumnDefinitionConfig colConfig) {
        DateTimeCustomFieldsCellEditor<String> datePickerCellEditor = new DateTimeCustomFieldsCellEditor<String>() {
            protected String getValue() {
                if (getDate() != null) {
                    return DateUtils.formatInternal(getDate());
                }
                return null;
            }

            protected void setValue(String cellValue) {
                try {
                    Date date;
                    if (cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equals(cellValue)) {
                        date = new Date();
                        setDefaultValue(true);
                    } else {
                        date = DateUtils.parseLongFormat(cellValue);

                        setDefaultValue(false);
                    }
                    setDateTime(date);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        colConfig.setCellEditor(datePickerCellEditor);

        colConfig.setCellChangesSave((rowValue, columnCodeName) -> {
            if (cellChangesSave != null) {
                cellChangesSave.saveCell(rowValue, columnCodeName);
            }
        });
        this.addStyleName("test--initDateTimeCellEditor");
    }

    /**
     * Repository Column Config Map
     */
    private void initMap() {
        for (CustomColumnDefinitionConfig columnConfig : columnConfigs) {
            mapColumn.put(columnConfig.getCodeName(), columnConfig);
        }

        /* Saved column code name not put mapColumn that delete this is
         *  column code in columnCodes Name list
         */
        List<String> columnCodesName = new ArrayList<>();
        for (String columnCode : listPanelTools.getColumnCodeName()) {
            if (!mapColumn.containsKey(columnCode)) {
                columnCodesName.add(columnCode);
            }
        }
        for (String codeName : columnCodesName) {
            listPanelTools.removeColumnTools(codeName);
        }
    }

    public void gotoPageing(boolean forced) {
        pagingScrollTable.gotoPage(0, forced);
    }

    private void reset() {
        getFilterParametrs().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
        pagingScrollTable.gotoPage(0, false);
        if (getFilterParametrs().getFacetFilter() != null) {
            getFilterParametrs().getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
        }
        dropSorting();
        reloadPage();
    }

    private void dropSorting() {
        if (searchBox.getText() != null && !"".equals(searchBox.getText())) {
            isNowSearchButtonClicked = true;
        }
        getFilterParametrs().setSortField(null);
        if (getListingPanelDesign().isShowTableHeaders()) {
            pagingScrollTable.setHeaderGenerated(true);
        }

        if (isShowFooterRow) {
            pagingScrollTable.setFooterGenerated(true);
        }
    }

    private String snakeCaseToCamelCaseConverter(String text) {
        StringBuilder camelCaseBuilder = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);

            if (currentChar == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    camelCaseBuilder.append(Character.toUpperCase(currentChar));
                    capitalizeNext = false;
                } else {
                    camelCaseBuilder.append(currentChar);
                }
            }
        }

        return camelCaseBuilder.toString();
    }

    /**
     * Set header Tool Bar Companents
     */
    private void createtHeaderToolBarCompanents() {
        listingFacetFilter = getListingPanelDesign().initFacetFilter();

        if (listingFacetFilter != null) {// initialization facet filter contents configure
            simpleFilterValue = listingFacetFilter.initSimpleFilterType();
            facetContenConfigure = listingFacetFilter.getFacetFilterContentconfigure();

            if (facetContenConfigure != null) {

                //Add facetable custom fields in facet filter
                if (listPanelTools.getListViewCustomFields() != null && !listPanelTools.getListViewCustomFields().isEmpty()) {
                    for (final CompanyCustomFieldItem item : listPanelTools.getListViewCustomFields()) {

                        if (item.isFacetable()) {
                            if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                                facetContenConfigure.addContentConfigureDateListBox(item.getColumnCode(), item.getFieldName());
                            } else {
                                facetContenConfigure.addContentConfigure(item.getColumnCode(), item.getFieldName(), new FacetFieldConfigure() {
                                    @Override
                                    public String getSolrFieldCriteriaName() {
                                        return item.getColumnCode();
                                    }

                                    @Override
                                    public String getSolrFacetFieldName() {
                                        return item.getColumnCode();
                                    }

                                    @Override
                                    public boolean isConditionItemId() {
                                        return false;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
        headerToolBar.addStyleName("operPanel--header");

        FlowPanel pnlOperActions = new FlowPanel();
        pnlOperActions.setStyleName("operPanel__actions");
        headerToolBar.add(pnlOperActions);

        FlowPanel pnlOperSettings = new FlowPanel();
        pnlOperSettings.setStyleName("");

        //to hide searchPanel
        MaterialIcon hideIcon = new MaterialIcon();
        hideIcon.setClass("ficon--chevron-left");
        hideIcon.setVisible(false);
        hideIcon.addClickHandler(e -> {
            hideIcon.setVisible(false);
            if (searchPanel.getStyleName().contains("active")) {
                searchPanel.removeStyleName("active");
            } else {
                searchPanel.addStyleName("active");
                searchBox.setFocus(true);
            }
        });

        searchPanel.addStyleName("searchForm");
        search.ensureDebugId("search_button");
        search.add(new SvgIcon(SvgEnum.search));
        search.addClickHandler(event -> {
            hideIcon.setVisible(true);
            if (searchPanel.getStyleName().contains("active")) {
                if (isListingPage()) {
                    reset();
                } else {
                    getFilterParametrs().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                    if (getFilterParametrs().getFacetFilter() != null) {
                        getFilterParametrs().getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                    }
                    requestKanbanData();
                }
            }
            searchPanel.addStyleName("active");
            searchBox.setFocus(true);
        });
        searchBox.setMaxLength(255);
        searchBox.ensureDebugId("searchBox");
        searchBox.setPlaceHolder(wfmStrings.search());
        searchBox.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (isListingPage()) {
                    reset();
                } else {
                    getFilterParametrs().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                    if (getFilterParametrs().getFacetFilter() != null) {
                        getFilterParametrs().getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                    }
                    requestKanbanData();
                }
            }
        });
        Span inputWrapper = new Span();
        inputWrapper.addStyleName("searchForm__control");
        inputWrapper.add(searchBox);
        searchPanel.add(search);
        searchPanel.add(inputWrapper);

        /**
         * <span class="searchForm__x">
         *   <svg class=" icon--search"><use href="mainStyles/new-ui/icons/sprite__panels.svg#x"></use></svg>
         * </span>
         */

        Span xSearch = new Span();
        xSearch.addStyleName("searchForm__x");
        xSearch.add(new SvgIcon(SvgEnum.x));
        xSearch.addClickHandler(e -> {
            searchBox.setValue("");

            //T5519
            if (isListingPage()) {
                reset();
            } else {
                getFilterParametrs().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                if (getFilterParametrs().getFacetFilter() != null) {
                    getFilterParametrs().getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                }
                requestKanbanData();
            }
        });
        searchPanel.add(xSearch);
        searchPanel.add(hideIcon);

        // List Panel Saved Facet Filter List Items
        facetFilterSavedList = getFacetFilterSavedList();
//        facetFilterSavedList.setWidth("150px");
        facetFilterSavedList.addStyleName("listingPanelPseudoSelect");
        facetFilterSavedList.addChangeHandler(changeEvent -> changeFacetFilterSavedList());
        facetFilterSavedList.setVisible(false);

        //Create new one
        newItem = getListingPanelDesign().initTopToolBarNew();

        //More actions
        moreItem = getListingPanelDesign().initTopToolBarMore();
        if (moreItem != null) {
            moreItem.setVisible(false);
        }
        printItem = getListingPanelDesign().initTopToolBarPrint();
        if (printItem != null) {
            printItem.setVisible(false);
        }

        moreItemActions = getListingPanelDesign().initTopToolBarMoreActions();
        if (moreItemActions != null && ListPanelType.ProductServiceListPanel.equals(type)) {
            moreItemActions.setVisible(false);
        }

        if (getListingPanelDesign().getFirstAdditionalPanel() != null) {
            pnlOperActions.add(getListingPanelDesign().getFirstAdditionalPanel());
        }

        //listing column settings
        ActionButton customizeButton = new ActionButton("", "btn btn--icon");
        customizeButton.ensureDebugId("customise_button");
        customizeButton.addClickHandler(clickEvent -> {
            if (isListingPage()) {
                ListingPanelSettingsPopup settingsPopup = new ListingPanelSettingsPopup(columnConfigs, listPanelTools, mapColumn, pagingScrollTable.getHeaderTable(), selectionPolicy, hasAdditionalInformation, stepID, formID);
                settingsPopup.setListingPanelSaveChanges(listingPanelSaveChanges);
            } else {
                KanbanSettingsPopup kanbanSettingsPopup = new KanbanSettingsPopup(kanbanBoardView);
            }
        });
        customizeButton.add(new SvgIcon(SvgEnum.sliders));

        /*=============== FILTERING PART ===================================================================*/
        /*=============== ACTIONS PART =========================================*/
        if (newItem != null) {
//            newItem.setStyleName("btn btn--success hasicon--right");
            pnlOperActions.add(newItem);
        }
        if (printItem != null) {
            if (!ActionButton.Type.LINK.equals(printItem.getType())) {
                printItem.setStyleName("btn btn--default hasicon--right");
            }
            pnlOperActions.add(printItem);
        }
        if (moreItem != null) {
            if (!ActionButton.Type.LINK.equals(moreItem.getType())) {
                moreItem.setStyleName("btn btn--default hasicon--right");
            }
            pnlOperActions.add(moreItem);
        }
        if (moreItemActions != null) {
            if (!ActionButton.Type.LINK.equals(moreItemActions.getType())) {
                moreItemActions.setStyleName("btn btn--default hasicon--right");
            }
            pnlOperActions.add(moreItemActions);
        }

//        if (showFilters) {
        //SEARCH box
//        new MaterialTooltip(searchPanel, wfmStrings.search()).setPosition(Position.TOP);
            pnlOperActions.add(searchPanel);
//        }
        MaterialPanel pnlButtons = new MaterialPanel("operPanel__btn-groups");
        if (showFilters) {
            // Add to Top Panel Facet Filter Widget
            if (facetContenConfigure != null) {
                //if normal filters which supposed to be displayed on right
                ActionButton filterBtn = initalizationFilterButton();
                new MaterialTooltip(filterBtn, wfmStrings.filter()).setPosition(Position.TOP);
                filtersWrap = new Div("filter-group");
                if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type)) {
                    if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                        filtersWrap.add(filterBtn);
                    }
                } else {
                    filtersWrap.add(filterBtn);
                }
                new MaterialTooltip(filterBtn, wfmStrings.filter()).setPosition(Position.TOP);
                pnlButtons.add(filtersWrap);

                // Get List Panel Saved Facet Filter List Items
//                initFacetFilterItems(null);
            } else if (simpleFilterValue != -1) {
                //filters will be displayed in popup
                if (ChooseFilter.INVOICE_FILTER == simpleFilterValue || ListingChooseFilter.TIMESHEET_APPROVAL_LIST == simpleFilterValue ||
                        ViewName.EmployeeHistoryList.equals(listingFacetFilter.getView()) || ChooseFilter.WAREHOUSE == simpleFilterValue || ChooseFilter.TASK_EMPLOYEE == simpleFilterValue) {
                    chooseFilter = new ListingChooseFilter(getFilterParametrs(), listingFacetFilter.getCustomFacetFilterFields(), listingFacetFilter.getView(), mapColumn);
                } else {
                    chooseFilter = new ListingChooseFilter(getFilterParametrs(), simpleFilterValue);
                }
                chooseFilter.initListBoxes();
                chooseFilter.addApplyButtonHandler(event -> {
                    chooseFilter.close();
                    reloadPage();
                });

                ActionButton filterBtn = initalizationSimpleFilterButton();
                new MaterialTooltip(filterBtn, wfmStrings.filter()).setPosition(Position.TOP);
                filtersWrap = new Div("filter-group");
                filtersWrap.add(filterBtn);
                pnlButtons.add(filtersWrap);
            }
        }
        // Get List Panel Saved Facet Filter List Items
        if (facetContenConfigure != null) {
            initFacetFilterItems(null);
        }

        // Add to Top Panel reset button
        if (getListingPanelDesign().isShowResetButton() && showFilters) {
            ActionButton resetBtn = initializationResetButton();
            new MaterialTooltip(resetBtn, wfmStrings.reset()).setPosition(Position.TOP);
            if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type)) {
                if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                    pnlButtons.add(resetBtn);
                }
            } else {
                pnlButtons.add(resetBtn);
            }
        }
        // Add to Top Panel reload button
        ActionButton refreshBtn = getListingPanelDesign().initializationReloadButton();
        if (refreshBtn == null) {
            refreshBtn = initializationReloadButton();
            new MaterialTooltip(refreshBtn, wfmStrings.refresh()).setPosition(Position.TOP);
        }
        pnlButtons.add(refreshBtn);

        //import/export menu widget in the top panel
        generateImportExportTool(pnlButtons);
        pnlOperActions.add(pnlButtons);


        // Add to Top Panel saved facet filter list
        if (facetContenConfigure != null) {
            facetFilterSavedList.removeStyleName("gwt-ListBox");
            if (filtersWrap != null) {
                filtersWrap.add(facetFilterSavedList);
            } else {
                advancedFilterPanel.add(facetFilterSavedList);
            }
        }
        //headerToolBar.add(advancedFilterPanel);
        pnlOperActions.add(advancedFilterPanel);

        if (getListingPanelDesign().getAddAdditionalPanel() != null) {
            //headerToolBar.add(getListingPanelDesign().getAddAdditionalPanel());
            pnlOperActions.add(getListingPanelDesign().getAddAdditionalPanel());
        }
        if (getListingPanelDesign().initTopToolBarWidgets() != null) {
            //headerToolBar.add(getListingPanelDesign().initTopToolBarWidgets());
            pnlOperActions.add(getListingPanelDesign().initTopToolBarWidgets());
        }

        //Paging generator
        createPagingToolBarCompanents();

        if (showCustomize) {
            // Add to Top Panel customise button
            if (getListingPanelDesign().isShowCustomiseButton()) {
                new MaterialTooltip(customizeButton, wfmStrings.customize()).setPosition(Position.TOP);
                pnlOperSettings.add(customizeButton);
            }
        }

        MaterialPanel pnlSettingsContainer = new MaterialPanel("operPanel__settings");
        pnlSettingsContainer.add(listPagingPanel);

        if (getKanbanBoardView() != null) {
            ActionButton kanbanViewSwither = new ActionButton("", "btn btn--icon");
            kanbanViewSwither.ensureDebugId("kanban_switcher");

            listKanbanSwitch = new SvgIcon(SvgEnum.barChart);
            switchTooltip = new MaterialTooltip(kanbanViewSwither, wfmStrings.kanbanView());
            switchTooltip.setPosition(Position.TOP);
            kanbanViewSwither.add(listKanbanSwitch);

            setSwitchStyle(true);

            kanbanViewSwither.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if (kanbanBoardView != null) {
                        setListingPage(!isListingPage());

                        initListOrKanban();

                        //Save View State for next visit (Kanban or Listing)
                        CommonService.App.get().saveUserSettings(UserSettingsTypeEnum.ItemsDisplayOptions, type.getViewName() + "_viewstate",
                                isListingPage ? "list" : "kanban",
                                new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false, MainLayout.get().getMainContent());
                                    }

                                    @Override
                                    public void success(Boolean result) {

                                        if (!(Boolean.TRUE.equals(result))) {
                                            GWT.log("Viewstate was not changed: " + type.getViewName() + "_viewstate");
                                        }
                                    }
                                });
                    }
                }
            }, ClickEvent.getType());

            pnlSettingsContainer.add(kanbanViewSwither);
        }

        pnlSettingsContainer.add(pnlOperSettings);
        headerToolBar.add(pnlSettingsContainer);
    }

    private void initListOrKanban() {
        if (!isListingPage()) {
            listPanelTools.setType(kanbanBoardView.getViewType());

            listPagingPanel.setVisible(false);
            flexTable.remove(pagingScrollTable);
            flexTable.add(kanbanBoardView);

            setSwitchStyle(false);

            switchTooltip.setText(wfmStrings.listView());
            if (kanbanBoardView.getColumnsMap().size() > 0) {
                requestKanbanData();
            } else {
                kanbanBoardView.setFilterParameter(getFilterParametrs());
                kanbanBoardView.init();
            }
        } else {
            kanbanBoardView.clearAllColumns();

            listPagingPanel.setVisible(true);
            flexTable.remove(kanbanBoardView);
            flexTable.add(pagingScrollTable);
            reloadPage();
            setSwitchStyle(true);
            switchTooltip.setText(wfmStrings.kanbanView());
        }


    }

    private void setSwitchStyle(boolean active) {
        if (active) {
            listKanbanSwitch.getElement().setAttribute("class", "icon--barChart active");
        } else {
            listKanbanSwitch.getElement().setAttribute("class", "icon--barChart");
        }

    }

    private void generateImportExportTool(MaterialPanel pnlOperActions) {
        importExportMenu = new MaterialMenuBar();
        importExportMenu.setClass("btn-group dropdown-kit--arrow--below");

        MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
        new MaterialTooltip(ieLink, wfmStrings.importExport()).setPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon");

        ieLink.add(new SvgIcon(SvgEnum.downloadCloud));
        ieLink.ensureDebugId("import_export_button_id");

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);
        ieLink.add(menuContainer);

        importExportMenu.add(ieLink);

        ExportImportOption exportImportOption = new ExportImportOption() {
            @Override
            public void initExport(FlowPanel additionalContent) {
                initExport(additionalContent, true);
            }

            @Override
            public void initExport(FlowPanel additionalContent, Boolean showExport) {

                if (additionalContent != null) {
                    pnlOperActions.add(additionalContent);
                }

                if (showExport) {
                    pdfVersion = getPdfVersion();
                    pdfVersion.ensureDebugId("pdf_button");
                    if (pdfVersion.isVisible()) {
                        Div wrapper = new Div("java-wrap");
                        wrapper.add(pdfVersion);
                        menuContainer.add(wrapper);

                        MaterialLink pdfLink = getPdfVersion();

                        MaterialDropDown mdp = new MaterialDropDown(pdfLink);
                        mdp.setHover(true);
                        mdp.setHoverable(true);


                        mdp.add(ListingPanel.this::getPortraitLink);
                        mdp.add(ListingPanel.this::getLandscapeLink);

                        wrapper.add(mdp);

                    }

                    xlsVersion = getXlsVersion();
                    xlsVersion.ensureDebugId("excel_button");
                    if (xlsVersion.isVisible()) {
                        menuContainer.add(xlsVersion);
                    }
                }

                if (!menuContainer.getItems().isEmpty()) {
                    pnlOperActions.add(importExportMenu);
                }
                //DO NOT DELETE THIS ELEMENT It is working for submit form data to the server when transfering data!
                exportPanel = new HTMLPanel("");
                exportPanel.getElement().getStyle().setDisplay(Style.Display.NONE);
                pnlOperActions.add(exportPanel);
            }
        };
        getListingPanelDesign().initImportExportToolBarWidgets(exportImportOption, menuContainer);
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    /**
     * <i>... When Saved Facet Filter changed running this is nethod ....</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {13:5103/08/2011} ...</i>
     */
    private void changeFacetFilterSavedList() {
        FacetFilterRpc facetFilter = new FacetFilterRpc(facetContenConfigure.getShowCodeNameCloneList(), facetContenConfigure.getShowSolrCloneFields(),
                facetContenConfigure.getHideSolrCloneFields(),
                facetContenConfigure.getSolrDateFields());
        if (isListingPage()) {
            facetFilter.setType(type);
        } /*else {
            facetFilter.setType(kanbanBoardView.getViewType());
        }*/
        facetFilter.setTypeId(typeId);
        if (facetFilterSavedList.getSelectedId() != null) {
            facetFilter.setObjectID(facetFilterSavedList.getSelectedId());
        } else {
            facetFilter.setObjectID(null);
            facetFilter.setFilterChanges(true);
        }
        getFilterParametrs().setFacetFilter(facetFilter);
        if (isListingPage()) {
            reloadPage();
        } else {
            requestKanbanData();
        }
    }

    private void initFacetFilterItems(Command command) {
        FacetFilterRpc facetFilter = new FacetFilterRpc(facetContenConfigure.getShowCodeNameCloneList(),
                facetContenConfigure.getShowSolrCloneFields(),
                facetContenConfigure.getHideSolrCloneFields(),
                facetContenConfigure.getSolrDateFields());
        if (isListingPage()) {
            facetFilter.setType(type);
        } /*else {
            facetFilter.setType(kanbanBoardView.getViewType());
        }*/
        facetFilter.setTypeId(typeId);
        getFilterParametrs().setFacetFilter(facetFilter);

        getListPanelSaveFacetFilterItems(true, command);
    }

    /**
     * <i>... Get List Panel Saved Facet Filter List Items ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {12:54 03/08/2011} ...</i>
     */
    public void getListPanelSaveFacetFilterItems(final boolean defaultSelect, Command command) {
        ListPanelType listingType = type;
        /*if (!isListingPage()) {
            listingType = kanbanBoardView.getViewType();
        }*/

        CommonService.App.get().getSavedFacetFilterList(listingType, typeId, new AbstractAsyncCallback<SaveFilterSelectItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SaveFilterSelectItems saveFilterSelectItems) {

                facetFilterSavedList.setItems(saveFilterSelectItems.getItems());

                if (defaultSelect && saveFilterSelectItems.getDefaultFilterID() != null) {
                    facetFilterSavedList.setSelected(saveFilterSelectItems.getDefaultFilterID());
                    if (getFilterParametrs().getFacetFilter() != null) {
                        getFilterParametrs().getFacetFilter().setObjectID(saveFilterSelectItems.getDefaultFilterID());
                    }
                    if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type)) {
                        if (!Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                            reloadPage();
                        }
                    }

                } else {
                    if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type)) {
                        if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                            facetFilterSavedList.setSelectedNullLabel();
                        } else {
                            reloadPage();
                        }
                    } else {
                        facetFilterSavedList.setSelectedNullLabel();
                    }
                }
                if (facetFilterSavedList.getItems() != null && facetFilterSavedList.getItems().size() > 0) {
                    facetFilterSavedList.setVisible(true);
                    advancedFilterPanel.setVisible(true);
                } else {
                    facetFilterSavedList.setVisible(false);
                    advancedFilterPanel.setVisible(false);
                }
                //If default selected and callback is not null then run it
                if (command != null) {
                    command.execute();
                }
            }
        });
    }

    /**
     * <i>... Registration Facet Filter Button Widget ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {16:45 21/06/2011} ...</i>
     *
     * @return Filter Button
     */
    private ActionButton initalizationFilterButton() {
        ActionButton filter = new ActionButton("", "btn btn--icon");
        filter.ensureDebugId("filter_button");
        filter.addClickHandler(event -> {
            FacetFilterRpc facetFilterRpc = null;

            if (getFilterParametrs().getFacetFilter() != null) {
                facetFilterRpc = getFilterParametrs().getFacetFilter();
            }
            if (facetFilterRpc != null) {
                if (isListingPage()) {
                    facetFilterRpc.setType(type);
                }
            }
            facetFilterPopup = new FacetFilterPopup(facetFilterRpc,
                    listingFacetFilter,
                    getListingPanel(),
                    type,
                    typeId,
                    getEnableDate(),
                    facetContenConfigure, showAddInFilter);
            facetFilterPopup.open();
        });

        filter.add(new SvgIcon(SvgEnum.filter));

        return filter;
    }

    /**
     * <i>... Registration Simple Filter Button Widget ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {22:55 22/09/2011} ...</i>
     *
     * @return Filter Button
     */
    private ActionButton initalizationSimpleFilterButton() {
        ActionButton filter = new ActionButton("", "btn btn--icon");
        filter.ensureDebugId("filter_button");
        filter.addClickHandler(event -> chooseFilter.open());

        filter.add(new SvgIcon(SvgEnum.filter));
        return filter;
    }

    /**
     * @return Currrent Listing Panel Widget
     */
    private ListingPanel getListingPanel() {
        return this;
    }

    /**
     * <i>... Registration Reset Button Widget ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {16:54 21/06/2011} ...</i>
     *
     * @return Reset Button
     */
    private ActionButton initializationResetButton() {
        ActionButton reset = new ActionButton("", "btn btn--icon");
        reset.getElement().setId("reset_button");
        final long[] lastClick = {0};
        reset.addClickHandler(event -> {
            if (pagingScrollTable.isPageLoading())
                return;

            if (onReset != null) {
                onReset.execute();
            }
            searchBox.setText("");
            if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type)) {
                if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                    facetFilterSavedList.setSelectedNullLabel();
                }
            } else {
                facetFilterSavedList.setSelectedNullLabel();
            }
            getFilterParametrs().setSearchKey(searchBox.getText());
            if (getListingPanelDesign().isShowTableHeaders()) {
                pagingScrollTable.setHeaderGenerated(true);
            }

            if (isShowFooterRow) {
                pagingScrollTable.setFooterGenerated(true);
            }

            if (facetContenConfigure != null) {
                FacetFilterRpc facetFilter = new FacetFilterRpc(facetContenConfigure.getShowCodeNameCloneList(),
                        facetContenConfigure.getShowSolrCloneFields(),
                        facetContenConfigure.getHideSolrCloneFields(),
                        facetContenConfigure.getSolrDateFields());

                facetFilter.setType(type);
                facetFilter.setTypeId(typeId);
                facetFilter.setFilterChanges(true);
                facetFilter.setSearchKey(getFilterParametrs().getSearchKey());
                getFilterParametrs().setFacetFilter(facetFilter);
            } else if (simpleFilterValue != -1) {  // reset for Simply ListingFilterParameter
                filterParametrs = null;
                getFilterParametrs().setListPanelTool(listPanelTools);
                chooseFilter.setFilterParameters(getFilterParametrs());
            }
            if (isListingPage()) {
                reloadPage();
            } else {
                if (lastClick[0] < System.currentTimeMillis() - 1000) {
                    requestKanbanData();
                }
                lastClick[0] = System.currentTimeMillis();
            }
        });

        reset.add(new SvgIcon(SvgEnum.repeat));
        return reset;
    }

    /**
     * <i>... Registration Reload Button Widget ...</i>
     * <br/>
     * <i>... Writen by developer {Anvar Akramov} ...</i>
     * <br/>
     * <i>... Created date {16:54 8/05/2018} ...</i>
     *
     * @return Reload Button
     */
    private ActionButton initializationReloadButton() {
        ActionButton reload = new ActionButton("", "btn btn--icon");
        reload.getElement().setId("reload_button");
        final long[] lastClick = {0};
        reload.addClickHandler(event -> {
            if (isListingPage()) {
                reloadPage();
            } else {
                if (lastClick[0] < System.currentTimeMillis() - 1000) {
                    requestKanbanData();
                }
                lastClick[0] = System.currentTimeMillis();
            }
        });
        reload.add(new SvgIcon(SvgEnum.rotateCw));
        return reload;
    }

    /**
     * @param listPanelTools
     */
    private void checkedIsNotNull(ListPanelToolRpc listPanelTools) {
        if (listPanelTools != null && listPanelTools.getColumnCodeName() != null && listPanelTools.getColunmsTool() != null) {
            this.listPanelTools = listPanelTools;
        } else {
            this.listPanelTools = new ListPanelToolRpc();
            ArrayList<String> columnCode = new ArrayList<>();
            LinkedHashMap<String, ColumnTool> columnToolMap = new LinkedHashMap<>();
            for (CustomColumnDefinitionConfig columnConfig : columnConfigs) {

                if (columnConfig.isShow()) {
                    columnCode.add(columnConfig.getCodeName());
                    ColumnTool columnTool = new ColumnTool();
                    columnTool.setColumnWidth(columnConfig.getPreferredColumnWidth());
                    for (Object o : columnConfig.getColors().values()) {
                        columnTool.addColor((ColumnColor) o);
                    }
                    columnToolMap.put(columnConfig.getCodeName(), columnTool);
                }
                if (this.listingRequestProvider instanceof SortableListingRequestProvider) {
                    this.listPanelTools.setSortBy(((SortableListingRequestProvider) this.listingRequestProvider).getSortableColumn());
                    this.listPanelTools.setSortByType(((SortableListingRequestProvider) this.listingRequestProvider).getColumnOrder());
                }
            }
            this.listPanelTools.setType(type);
            this.listPanelTools.setTypeId(typeId);
            this.listPanelTools.setColumnCodeName(columnCode);
            this.listPanelTools.setColunmsTool(columnToolMap);
            this.listPanelTools.setShowPopup(false);
        }
        getFilterParametrs().setListPanelTool(this.listPanelTools);
    }

    /**
     * @return Table Columnc Settings
     */
    public DefaultTableDefinition<T> createTableDefinition() {
        //Create the table definition
        DefaultTableDefinition<T> tableDefinition = new DefaultTableDefinition<>();

        for (int i = 0; i < listPanelTools.getColumnCodeName().size(); i++) {
            final String codeName = listPanelTools.getColumnCodeName().get(i);

            if (mapColumn.containsKey(codeName)) {
                final CustomColumnDefinitionConfig columnConfig = mapColumn.get(codeName);

                if (codeName.equals(STATUS)) {
                    columnConfig.setHtml(true);
                }
                columnConfig.setPreferredColumnWidth(listPanelTools.getColunmsTool().get(codeName).getColumnWidth());
                columnConfig.setHeaderTruncatable(false);

                columnConfig.setCellRenderer((rowValue, columnDef, view) -> {
                    Span outerWrapper = new Span();
                    Span innerWrapper = new Span();
                    innerWrapper.addStyleName("text-holder");

                    if (columnConfig.getStyleAttributeMap().containsKey("wrap")) {
                        innerWrapper.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
                        innerWrapper.getElement().getStyle().setProperty("wordBreak", "break-word");
                    }
                    outerWrapper.add(innerWrapper);
                    Span editWrapper = null;
                    if (columnConfig.getCellEditor() != null) {
                        editWrapper = new Span();
                        Span editSpan = new Span();
                        editSpan.addStyleName(PENCIL_CSS);
                        editSpan.setTitle(wfmStrings.edit());
//                        MaterialIcon iEdit = new MaterialIcon();
//                        iEdit.addStyleName("ficon--edit");
                        editWrapper.add(editSpan);
//                        editSpan.add(iEdit);
                        editWrapper.add(outerWrapper);
                    }
                    outerWrapper.addStyleName("cell-decorator");
                    Object cellValue = columnDef.getCellValue(rowValue);
                    String condition = null;
                    if (cellValue == null || "".equals(cellValue.toString())) {
                        view.setHTML("<span class=\"cell-decorator\">&nbsp;</span>");
                        condition = "";
                    } else if (cellValue instanceof Widget) {
                        innerWrapper.add((Widget) cellValue);
                        view.setWidget(editWrapper != null ? editWrapper : outerWrapper);
                        condition = cellValue.toString();
                    } else {
                        String value = cellValue instanceof SelectItem ? (Utils.hasOnlyRole(Constants.CLIENT) && cellValue instanceof ReferenceItem ? ((ReferenceItem) cellValue).getAntonym() : ((SelectItem) cellValue).getName()) : cellValue.toString();
                        CustomColumnDefinitionConfig cconfig = ((CustomColumnDefinitionConfig) columnDef);

                        if (cconfig.isClickable()) {
                            final CustomColumnDefinitionConfig columnConfig1 = mapColumn.get(codeName);
                            String action = "";
                            if ("".equals(action) && columnConfig1.isHasLink()) {
                                action = columnConfig1.getLinkUrl();
                            }
                            if ("".equals(action)) {
                                for (String currency : mapColumn.keySet()) {
                                    Object cellValue2 = mapColumn.get(currency).getCellValue(rowValue);
                                    SimpleLink cellValue3 = cellValue2 instanceof SimpleLink ? (SimpleLink) cellValue2 : new SimpleLink("");
                                    String linUrl = mapColumn.get(currency).getLinkUrl();
                                    action = linUrl != null ? linUrl : (cellValue3.getAction() != null ? cellValue3.getAction() : "");
                                    if (!"".equals(action)) {
                                        break;
                                    }

                                }
                            }
                            innerWrapper.add(new SimpleLink(value, action));
                            view.setWidget(editWrapper != null ? editWrapper : outerWrapper);
                            condition = value;
                        } else {
                            innerWrapper.setText(value);
                            view.setWidget(editWrapper != null ? editWrapper : outerWrapper);
                            condition = value;
                        }
                    }
                    if (columnConfig.getHorizontalAlign() != null) {// set column horizontal aligmnet
                        view.setHorizontalAlignment(columnConfig.getHorizontalAlign());
                    }
                    LinkedHashMap<String, String> styleAttributeMap = columnConfig.getStyleAttributeMap();
                    if (styleAttributeMap.size() > 0) {// set column style atribute
                        for (String keyAttr : styleAttributeMap.keySet()) {
                            view.setStyleAttribute(keyAttr, styleAttributeMap.get(keyAttr));
                        }
                    }
                    boolean hasColor = condition != null && listPanelTools.getColunmsTool().containsKey(codeName) && listPanelTools.getColunmsTool().get(codeName).hasColor(condition);
                    if (hasColor) {
                        String paintColor = listPanelTools.getColunmsTool().get(codeName).getColor(condition);
                        String color = paintColor.contains("#") ? paintColor : "#" + paintColor;
                        if (listPanelTools.getColunmsTool().get(codeName).isRowColor(condition)) {
                            view.setRowColor(color);
                        }
                        if (listPanelTools.getColunmsTool().get(codeName).isColumnColor(condition)) {
                            Icon iColor = new Icon();
                            iColor.getElement().getStyle().setBackgroundColor(color);
                            outerWrapper.insert(iColor, 0);
                        }
                        if (listPanelTools.getColunmsTool().get(codeName).isWordColor(condition)) {
                            outerWrapper.getElement().getStyle().setColor(color);
//                            wrapper.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);//munir so'radi
                        }
                    }
                });
                if (isShowFooterRow) {
                    if (columnConfig.getFooterName() != null) {
                        columnConfig.setFooter(0, columnConfig.getFooterName());

                    } else {
                        columnConfig.setFooter(0, columnConfig.getColumnName());

                    }
                }
                tableDefinition.addColumnDefinition(columnConfig);
            }
        }

        //Added by Anvar
        final CustomColumnDefinitionConfig columnConfigResizer = new ColumnDefinitionConfig<Object, String>("", "resizer", 1) {

            @Override
            public String getCellValue(Object rowValue) {
                return " ";
            }
        };
        columnConfigResizer.setMinimumColumnWidth(1);
        columnConfigResizer.setColumnSortable(false);
        columnConfigResizer.setPreferredColumnWidth(1);
        columnConfigResizer.setHeaderTruncatable(false);

        columnConfigResizer.setCellRenderer((rowValue, columnDef, view) -> {
            Span wrapper = new Span();
            wrapper.addStyleName("cell-decorator");
            view.setHTML("<span class=\"cell-decorator\">&nbsp;</span>");
        });
        if (isShowFooterRow) {
            if (columnConfigResizer.getFooterName() != null) {
                columnConfigResizer.setFooter(0, columnConfigResizer.getFooterName());

            } else {
                columnConfigResizer.setFooter(0, columnConfigResizer.getColumnName());

            }
        }
        tableDefinition.addColumnDefinition(columnConfigResizer);
        //Added by Anvar
        return tableDefinition;
    }

    /**
     * @return PagingScrollTable
     */
    public ListingPagingScrollTable<T> createPagingScrollTable() {
        // create our own table model // Data Source Model
        DataSourceModel<T> tableModel = createTableModel();
        ListingDataTable dataTable = new ListingDataTable();
        dataTable.setSelectionPolicy(selectionPolicy);
        // create the table definition
        DefaultTableDefinition<T> tableDef = createTableDefinition();

        FixedWidthFlexTable header = new FixedWidthFlexTable();

        // create the paging scroll table
        pagingScrollTable = new ListingPagingScrollTable<>(tableModel, dataTable, header, tableDef);
        //As per T1528 we want to save column width everytime user resizes it
        pagingScrollTable.addOnStopColumnResize(new AbstractScrollTable.ExecuteCommand() {
            @Override
            public void execute() {
                //Code to save settings

                ArrayList<String> allcolumns = new ArrayList<>(listPanelTools.getColumnCodeName());
                listPanelTools.setApplySettingsToAll(Boolean.FALSE);

                int n = SelectionGrid.SelectionPolicy.CHECKBOX.equals(selectionPolicy) || SelectionGrid.SelectionPolicy.RADIO.equals(selectionPolicy) ? 1 : 0;
                for (String _column : allcolumns) {
                    listPanelTools.getColunmsTool().get(_column).setColumnWidth(pagingScrollTable.getHeaderTable().getColumnWidth(n));
                    n++;
                }
                listPanelTools.setStepID(stepID);
                CommonService.App.get().saveListPanelSettings(listPanelTools, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                    }

                    @Override
                    public void success(Void result) {
                    }
                });

            }
        });

        //T1528 - Set saved column width by Anvar
        for (int i = 0; i < listPanelTools.getColumnCodeName().size(); i++) {
            final String codeName = listPanelTools.getColumnCodeName().get(i);
            if (mapColumn.containsKey(codeName)) {
                pagingScrollTable.setColumnWidth(i, listPanelTools.getColunmsTool().get(codeName).getColumnWidth());
            }
        }
        //End of T1528 - Set saved column width by Anvar
        // for wfp customise listing paging
        if (customPageSize > 0) {
            pagingScrollTable.setPageSize(customPageSize);
        } else {
            pagingScrollTable.setPageSize(listPanelTools.getPageSize());
        }
        pagingScrollTable.setHeaderGenerated(getListingPanelDesign().isShowTableHeaders());
        if (isShowFooterRow) {
            pagingScrollTable.setFooterGenerated(true);
        }
        pagingScrollTable.setScrollPolicy(AbstractScrollTable.ScrollPolicy.HORIZONTAL);
        dataTable.setParentTable(pagingScrollTable);

        emptyTable = new FlowPanel();
        emptyTable.setStyleName("emptyTable-content");

        pagingScrollTable.setEmptyTableWidget(emptyTable);
        // Setup the bulk renderer
        FixedWidthGridBulkRenderer<T> bulkRenderer = new FixedWidthGridBulkRenderer<>(pagingScrollTable.getDataTable(), pagingScrollTable);
        pagingScrollTable.setBulkRenderer(bulkRenderer);

        pagingScrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);
        pagingScrollTable.setColumnResizePolicy(AbstractScrollTable.ColumnResizePolicy.MULTI_CELL);
        pagingScrollTable.setSortPolicy(AbstractScrollTable.SortPolicy.MULTI_CELL);

        pagingScrollTable.setAllRowSelectedCommand(() -> {
            Info.show(wfmStrings.youHaveSelectedAllRowsOnCurrentPage());
        });

        registrationPageSrollListener();
        return pagingScrollTable;
    }

    private void initializeEmptyDataTable(boolean isEmptyMessage) {
        emptyTable.clear();

        if (isEmptyMessage) {
            getListingPanelDesign().initDataEmptyTable(emptyData -> {
                if (emptyData != null) {
                    VerticalPanel wholeMessage = emptyData.getWholeMessage();
                    if (wholeMessage != null) {
                        emptyTable.add(wholeMessage);
                    }
                }
            });
        } else {
            emptyTable.add(new HTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria()));
        }
    }

    private void registrationPageSrollListener() {
        pagingScrollTable.addPageChangeHandler(event -> {
            isPageChange = true;
            if (event.getNewPage() != event.getOldPage() && selectionPolicy == SelectionGrid.SelectionPolicy.CHECKBOX) {
                pagingScrollTable.getHeaderCheckBox().setValue(false);
                selectedRows.clear();
            }
        });

        pagingScrollTable.addPageLoadHandler(event -> {
            isPageChange = false;
            selectAllRows();
        });


        pagingScrollTable.getDataTable().addRowSelectionHandler(event -> {
            if (quickViewPanel != null) {
                if (pagingScrollTable.getSelectedRowValues().iterator().hasNext()) {
                    quickViewPanel.preview(pagingScrollTable.getSelectedRowValues().iterator().next());
                }
            }
            if ((rowSelectionHandler.size() != 0 || clickEvent) && !isPageChange) {
                doRowSelection(event);
            }
        });
    }

    private void selectAllRows() {
        if (selectionPolicy == SelectionGrid.SelectionPolicy.CHECKBOX) {
            if (pagingScrollTable.getHeaderCheckBox().getValue()) {
                pagingScrollTable.getDataTable().selectAllRows();
            }
        }
    }

    /**
     * Row Selection or CheckBox or RadioButton
     *
     * @param event
     */
    private void doRowSelection(RowSelectionEvent event) {
        if (event.getSelectedRows().size() == 0 && selectionPolicy == SelectionGrid.SelectionPolicy.CHECKBOX && rowSelectionHandler.size() == 0) {
            pagingScrollTable.getHeaderCheckBox().setValue(false);
        }
        for (TableEvent.Row row : event.getSelectedRows()) {
            selectedRows.add(rowsData.getList().get(row.getRowIndex()));
        }
        for (TableEvent.Row row : event.getDeselectedRows()) {
            selectedRows.remove(rowsData.getList().get(row.getRowIndex()));
        }
        if (selectionPolicy == SelectionGrid.SelectionPolicy.CHECKBOX && selectedRows.size() == 0) {
            if (moreItem != null) {
                moreItem.setVisible(false);
            }
            if (printItem != null) {
                printItem.setVisible(false);
            }
            if (moreItemActions != null) {
                moreItemActions.setVisible(false);
            }
        }
        if (selectionPolicy == SelectionGrid.SelectionPolicy.CHECKBOX && selectedRows.size() > 0) {
            if (moreItem != null) {
                moreItem.setVisible(true);
            }
            if (printItem != null) {
                printItem.setVisible(true);
            }
            if (moreItemActions != null) {
                moreItemActions.setVisible(true);
            }
        }
        for (ListingPanelRowSelectionHandler rowHandler : rowSelectionHandler) {
            rowHandler.onSelectedRows(selectedRows);
        }


    }

    /**
     * set Footer Tool Bar Components
     */
    private void createPagingToolBarCompanents() {
        // Paging option
        PagingOptions pagingOptions = new PagingOptions(pagingScrollTable);

        totalLabel = new HTML();
        totalLabel.addStyleName("pagingStat");

        MaterialLink totalLink = new MaterialLink();
        totalLink.add(totalLabel);
        totalLink.ensureDebugId("total_link_id");

        MaterialDropDown flPages = new MaterialDropDown(totalLink);
        flPages.setBelowOrigin(true);
        totalLink.add(flPages);

        pagingOptions.getFirstPage().setText(wfmStrings.newest());
        pagingOptions.getFirstPage().ensureDebugId("newest_page_id");
        pagingOptions.getLastPage().setText(wfmStrings.oldest());
        pagingOptions.getLastPage().ensureDebugId("oldest_page_id");
        flPages.add(pagingOptions.getFirstPage());
        flPages.add(pagingOptions.getLastPage());

        MaterialPanel pnlTotalsContainer = new MaterialPanel("pagingStat__wrapper");
        pnlTotalsContainer.add(totalLink);

        listPagingPanel.setStyleName("paging-group");
        listPagingPanel.addStyleName("opers-2 operPanel--paging");
        listPagingPanel.add(pnlTotalsContainer);
        listPagingPanel.add(pagingOptions);
    }

    /**
     * @return Get empty table
     */
    public FlowPanel getEmptyTable() {
        return emptyTable;
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

                if (isNowSearchButtonClicked) {
                    isNowSearchButtonClicked = false;
                    getFilterParametrs().setSearchButton(true);
                } else {
                    getFilterParametrs().setSearchButton(false);
                }
                int currentPage = request.getStartRow() > 0 ? (request.getStartRow() / request.getNumRows()) : 0;
                getFilterParametrs().setStart(request.getStartRow());
                getFilterParametrs().setLimit(request.getNumRows());
                getFilterParametrs().setCurrentPage(currentPage);

                if (request.getColumnSortList().getPrimaryColumn() != -1) {

                    if (listPanelTools.getColumnCodeName().size() > request.getColumnSortList().getPrimaryColumn()) {
                        getFilterParametrs().setSortField(listPanelTools.getColumnCodeName().get(request.getColumnSortList().getPrimaryColumn()));
                        getFilterParametrs().setAscending(request.getColumnSortList().isPrimaryAscending());
                    } else {
                        getFilterParametrs().setSortField(listPanelTools.getColumnCodeName().get(0));
                        getFilterParametrs().setAscending(true);
                    }
                } else if (listPanelTools.getSortBy() != null) {
                    getFilterParametrs().setSortField(listPanelTools.getSortBy());
                    if (listPanelTools.getSortByType() != null && listPanelTools.getSortByType().equals("asc")) {
                        getFilterParametrs().setAscending(true);
                    } else getFilterParametrs().setAscending(listPanelTools.getSortByType() == null);
                }

                listingCallback.setRequest(request);
                listingCallback.setCallback(callback);

                facetFilterSavedList.setEnabled(false);

                loading(true);

                listingRequestProvider.getRequest(getFilterParametrs(), listingCallback);
            }
        };
    }

    public void loading(boolean enable) {
        if (isListingPage()) {
            if (enable) {
                pagingScrollTable.addStyleName(loaderClass);
            } else {
                pagingScrollTable.removeStyleName(loaderClass);
            }
        }
        LoadingPanel.loading(enable);
    }

    public void requestKanbanData() {
        kanbanBoardView.setFilterParameter(getFilterParametrs());
        kanbanBoardView.reloadAllColumns();
    }

    /**
     * Refresh Page
     */
    public void reloadPage() {
        if (pagingScrollTable != null) {
            /*Widget selectAllWidget = pagingScrollTable.getSelectAllWidget();
            if (selectAllWidget != null && selectAllWidget instanceof KpiCheckBox) {
                ((KpiCheckBox) selectAllWidget).setValue(false, true);
            }*/
            if (getPagingScrollTable().getHeaderCheckBox() != null) {
                getPagingScrollTable().getHeaderCheckBox().setValue(false);
            }
            pagingScrollTable.reloadPage();
        }
    }

    /**
     * Refresh if opening facet filter widget
     */
    public void refreshFacetFilter() {
        if (facetFilterPopup != null) {
            facetFilterPopup.refreshFilter();
        }
    }

    /**
     * @return FacetFilterPopup widget
     */
    public FacetFilterPopup getFacetPopup() {
        if (facetFilterPopup != null) {
            return facetFilterPopup;
        }
        return null;
    }

    public void onRefresh(Command command) {
        this.command = command;
    }

    /**
     * Show Total Label
     *
     * @param total count
     */
    private void setTotal(Integer total) {
        if (total != null && total != 0) {
            /*totalLabel.setHTML(wfmStrings.displayItems() + ": <span>" + (filterParametrs.getStart() + 1) + " - " +
                    ((filterParametrs.getStart() + filterParametrs.getLimit()) < total ? (filterParametrs.getStart() + filterParametrs.getLimit()) : total) + " " + wfmStrings.of() + " " + total.toString() + "</span>");*/
            totalLabel.setHTML("<span>" + (filterParametrs.getStart() + 1) + " - " +
                    ((filterParametrs.getStart() + filterParametrs.getLimit()) < total ? (filterParametrs.getStart() + filterParametrs.getLimit()) : total) + " " + wfmStrings.of() + " " + total + "</span>");
        } else {
            /*totalLabel.setHTML(wfmStrings.displayItems() + ": <span>0 - 0 " + wfmStrings.of() + " 0 </span>");*/
            totalLabel.setHTML("<span>0 - 0 " + wfmStrings.of() + " 0 </span>");
            //initialize empty message
            initializeEmptyDataTable(searchBox.getText() == null || "".equals(searchBox.getText()));
        }
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
    public void setPDFListener(final ClickHandler clickEvent) {
        getPortraitLink().addClickHandler((event) -> {
            getListingPanel().getFilterParametrs().setLandscape(false);
            clickEvent.onClick(event);
        });
        getLandscapeLink().addClickHandler((event) -> {
            getListingPanel().getFilterParametrs().setLandscape(true);
            clickEvent.onClick(event);
        });
    }

    /**
     * @param pdfURL
     * @param filterParametrs
     */
    public void callListPDF(String pdfURL, ListingFilterParameter filterParametrs) {
        Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, getFilterRequestParam(filterParametrs), "_blank");
    }

    public void callListPDF(String pdfURL, HashMap<String, String> map) {
        Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, map, "_blank");
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
     * @param pdfURL
     * @param requestObject
     */
    public void callItemPDF(String pdfURL, RequestObject requestObject) {
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, parametrs, "_blank");
    }

    /**
     * List Panel Settings Rpc Object
     *
     * @param listPanelTools
     */
    public void setListPanelTools(ListPanelToolRpc listPanelTools) {
        this.listPanelTools = listPanelTools;
        pagingScrollTable.setShowPopups(hasAdditionalInformation && listPanelTools != null && listPanelTools.isShowPopup());
        getFilterParametrs().setListPanelTool(listPanelTools);
    }

    /**
     * Xsl toolItem Button
     *
     * @return
     */
    public MaterialLink getXlsVersion() {

        if (xlsVersion == null) {
            xlsVersion = new MaterialLink();
            MaterialIcon xlsIcon = new MaterialIcon();
            xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
            xlsVersion.add(xlsIcon);
            xlsVersion.setText(wfmStrings.excel());
        }
        return xlsVersion;
    }

    /**
     * Pdf toolItem Button
     *
     * @return
     */
    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    /**
     * search filter button not shown
     */

    public void hideSearchButton() {
        searchPanel.setVisible(false);
    }

    /**
     * Listing Panel Row Selected Handler
     * Uses row selected or CheckBox selected or RadioButton selected
     *
     * @param handler
     */
    public void addSelectionRowHandler(ListingPanelRowSelectionHandler<T> handler) {
        if (handler != null) {
            rowSelectionHandler.add(handler);
        }
    }

    /**
     * Save Custom Fields Cell Changes
     */
    public void setCustomFieldsEditCellSaveChanges(CellChange cellChangesSave) {
        this.cellChangesSave = cellChangesSave;
    }

    /**
     * This is method return
     * in listing panel row items count
     */
    public int getItemCount() {
        return rowsData != null && rowsData.getTotal() != null ? rowsData.getTotal() : 0;
    }

    public ListingPagingScrollTable<T> getPagingScrollTable() {
        return pagingScrollTable;
    }

    public boolean hasCheckedAllTableItems() {
        return pagingScrollTable.hasCheckedAllTableItems();
    }

    public T getDefaultOne() {
        return defaultOne;
    }

    public void setDefaultOne(T defaultOne) {
        this.defaultOne = defaultOne == null ? this.defaultOne : defaultOne;
    }

    public void showSelectOneMessage() {

    }

    public Integer getFacetFilterSelectedId() {
        return facetFilterSavedList.getSelectedItem() != null ? facetFilterSavedList.getSelectedItem().getId() : null;
    }

    public void setFacetFilterSelectedId(Integer id) {
        if (id != null) {
            facetFilterSavedList.setSelected(id);
        } else {
            facetFilterSavedList.setSelectedNullLabel();
        }
    }

    public void setFacetFilterListItems(SelectItem[] items) {
        if (items != null && items.length != 0) {
            facetFilterSavedList.setItems(items);
            facetFilterSavedList.setVisible(true);
            advancedFilterPanel.setVisible(true);
        } else {
            facetFilterSavedList.setVisible(false);
            advancedFilterPanel.setVisible(false);
        }
    }

    public KpiSelectBox getFacetFilterSavedList() {
        if (facetFilterSavedList == null) {
            facetFilterSavedList = new KpiSelectBox(type);
        }
        return facetFilterSavedList;
    }

    public FlowPanel getAdvancedFilterPanel() {
        return advancedFilterPanel;
    }

    public void setOnReset(Command onReset) {
        this.onReset = onReset;
    }

    public void setQuickViewPanel(QuickViewPanel quickViewPanel) {
        this.quickViewPanel = quickViewPanel;
    }

    public Boolean getEnableDate() {
        return EnableDate;
    }

    public void setEnableDate() {
        EnableDate = false;
    }

    public ListingChooseFilter getChooseFilter() {
        return chooseFilter;
    }

    public void setPopupWidgets(ArrayList<ArrayList<Widget>> widgets) {
        pagingScrollTable.setPopupWidgets(widgets);
    }

    public KanbanBoard<T> getKanbanBoardView() {
        return kanbanBoardView;
    }

    public void setKanbanBoardView(KanbanBoard<T> kanbanBoardView) {
        this.kanbanBoardView = kanbanBoardView;
    }

    public boolean isListingPage() {
        return isListingPage;
    }

    public void setListingPage(boolean listingPage) {
        isListingPage = listingPage;
    }

    public void putFooterValue(String columnCode, String footerValue) {
        if (isShowFooterRow && mapColumn.containsKey(columnCode)) {
            CustomColumnDefinitionConfig columnConfig = mapColumn.get(columnCode);
            columnConfig.setFooter(0, footerValue);
        }
    }

    protected void addTableToLayout() {
        layoutPanel.add(flexTable);
    }

    @Override
    protected void onDetach() {
        MainLayout.get().considerBodyHasOperPanel(false);

//        MainLayout.get().considerPagerOpersEmptiness(false);
        super.onDetach();
        if (this.pagingScrollTable != null && this.pagingScrollTable.getDataWrapper() != null) {
            scrollTopPosition = this.pagingScrollTable.getDataWrapper().getScrollTop();
        }
    }

    @Override
    protected void onAttach() {

//        MainLayout.get().considerPagerOpersEmptiness(true);
        super.onAttach();
        if (this.pagingScrollTable != null && this.pagingScrollTable.getDataWrapper() != null) {
            this.pagingScrollTable.getDataWrapper().setScrollTop(scrollTopPosition);
        }
    }

    protected ListingPanelDesign getListingPanelDesign() {
        return listingPanelDesign;
    }

    public HTMLPanel getExportPanel() {
        return exportPanel;
    }

    private class TablePanel extends MaterialPanel {
        @Override
        protected void onAttach() {
            super.onAttach();
            MainLayout.get().considerBodyHasFittedContent(true);
            MainLayout.get().considerBodyHasOperPanel(true);
        }

        @Override
        protected void onDetach() {
            super.onDetach();
            MainLayout.get().considerBodyHasFittedContent(false);
            MainLayout.get().considerBodyHasOperPanel(false);
        }
    }

    public ActionButton getAddNewButton() {
        return newItem;
    }
}
