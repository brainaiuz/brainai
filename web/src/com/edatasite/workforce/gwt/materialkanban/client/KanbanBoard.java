package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanColumn;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anvar Akramov on 8/26/17.
 */
public class KanbanBoard<T> extends Composite {

    interface KanbanViewBinder extends UiBinder<Widget, KanbanBoard> {
    }

    private static final UiBinder<Widget, KanbanBoard> ourUiBinder = GWT.create(KanbanViewBinder.class);

    @UiField
    AbsolutePanel absolutePanel;

    //We will set this object (ColumnData) when user start dragging item
    private Object fromColumnMetadata;
    //Dragging card initial index
    private Integer widgetIndex;
    //Source column from where movement started
    private KanbanVerticalPanel fromColumn;

    private final HashMap<ScrollPanel, Integer> scrollPositions = new HashMap<>();

    private ArrayList<KanbanColumn> visibleColumns = new ArrayList<>();
    private HorizontalPanel horizontalPanel;
    private PickupDragController columnDragController;
    private MyPickupDragController widgetDragController;

    private KanbanDataLoader kanbanDataLoader;
    private KanbanBoardDesign kanbanBoardDesign;
    private final HashMap<Integer, DraggableColumn> columnsMap = new HashMap<>();
    private int pageSize = 10;
    private boolean isDropable = true;
    private ListPanelType viewType;
    private AbstractAsyncCallback<List<T>> requestCallback;
    private ListingFilterParameter filterParameter;
    private final KanbanServiceAsync kanbanService = KanbanService.App.get();
    private final String lose = "Сделка провалена";
    private final String win = "Сделка успешна";
    private Map<String, Map<String, KanbanItemColumnConfigs>> relatedFieldsMapBySectionName = new HashMap<>();
    private KanbanItemSettingEnum kanbanItemSettingsType;

    private static final String COLUMN_COMPOSITE = "dnd_scrollbar";
    private static final String CSS_KANBAN_DND_PANEL = "kanban-dnd-panel";
    private static final String CSS_KANBAN_DND_PANEL_HEADING = "kanban-dnd-panel-heading";
    private static final String CSS_KANBAN_DND_PANEL_CONTAINER = "kanban-dnd-panel-container";
    private final String LOSE = "   <td align=\"left\">\n" +
            "                                                                    <table cellspacing=\"5\" cellpadding=\"0\" class=\"dragdrop-draggable kanban-col-lost\">\n" +
            "                                                                        <tbody>\n" +
            "                                                                        <tr>\n" +
            "                                                                            <td>\n" +
            "                                                                                <div class=\"quickActionTarget--lost\">\n" +
            "                                                                                    <span>" + lose + "</span>\n" +
            "                                                                                </div>\n" +
            "                                                                            </td>\n" +
            "                                                                        </tr>\n" +
            "                                                                        </tbody>\n" +
            "                                                                    </table>\n" +
            "                                                                </td>";
    private final String WIN = " <td align=\"right\">\n" +
            "                                                                    <table cellspacing=\"5\" cellpadding=\"0\" class=\"dragdrop-draggable kanban-col-won\">\n" +
            "                                                                        <tbody>\n" +
            "                                                                        <tr>\n" +
            "                                                                            <td>\n" +
            "                                                                                <div class=\"quickActionTarget--won\">\n" +
            "                                                                                    <span>" + win + "</span>\n" +
            "                                                                                </div>\n" +
            "                                                                            </td>\n" +
            "                                                                        </tr>\n" +
            "                                                                        </tbody>\n" +
            "                                                                    </table>\n" +
            "                                                                </td>";
    private HorizontalPanel horizont;
    private boolean showFooter;


    private static final int SPACING = 0;


    public KanbanBoard() {
        initWidget(ourUiBinder.createAndBindUi(this));
//        MaterialDesignBase.injectCss(KanbanClientBundle.INSTANCE.injectKanbanCSS());
        addStyleName(CSS_KANBAN_DND_PANEL);
    }
//
//    public KanbanBoard(ListPanelType listPanelType, KanbanDataLoader<T> kanbanDataLoader, KanbanBoardDesign<T> kanbanBoardDesign) {
//        this(listPanelType, kanbanDataLoader, kanbanBoardDesign);
//    }

    public KanbanBoard(ListPanelType listPanelType, KanbanDataLoader<T> kanbanDataLoader, KanbanBoardDesign<T> kanbanBoardDesign, boolean showFooter) {
        this(listPanelType, kanbanDataLoader, kanbanBoardDesign);
        this.showFooter = showFooter;
        getKanbanItemRelatedFieldsMap();
    }

    public KanbanBoard(ListPanelType listPanelType, KanbanDataLoader<T> kanbanDataLoader, KanbanBoardDesign<T> kanbanBoardDesign) {
        this.viewType = listPanelType;
        this.kanbanBoardDesign = kanbanBoardDesign;
        this.kanbanDataLoader = kanbanDataLoader;
        initWidget(ourUiBinder.createAndBindUi(this));
//        MaterialDesignBase.injectCss(KanbanClientBundle.INSTANCE.injectKanbanCSS());
        addStyleName(CSS_KANBAN_DND_PANEL);
        /// get Releted fields map
        getKanbanItemRelatedFieldsMap();
    }

    public void setKanbanItemSettingsType(KanbanItemSettingEnum kanbanItemSettingsType) {
        this.kanbanItemSettingsType = kanbanItemSettingsType;
    }

    public void init() {
        absolutePanel.clear();

        columnDragController = new PickupDragController(absolutePanel, false);

        columnDragController.setBehaviorMultipleSelection(false);
        columnDragController.setBehaviorScrollIntoView(false);
        if (isDropable()) {
            columnDragController.addDragHandler(new DragHandlerAdapter() {

                @Override
                public void onDragStart(DragStartEvent event) {
                    super.onDragStart(event);
                    RootPanel.getBodyElement().addClassName("has-dnd-drag");
                }

                @Override
                public void onDragEnd(DragEndEvent event) {
                    RootPanel.getBodyElement().removeClassName("has-dnd-drag");
                    //Save Order of columns
                    saveKanbanboardSettings();

                }

            });
        }

        widgetDragController = new MyPickupDragController(absolutePanel, false, this);
        widgetDragController.setBehaviorMultipleSelection(false);
        widgetDragController.setBehaviorScrollIntoView(false);

        widgetDragController.setBehaviorDragStartSensitivity(1);

        horizontalPanel = new HorizontalPanel();
        horizontalPanel.addStyleName(CSS_KANBAN_DND_PANEL_CONTAINER);
        horizontalPanel.setSpacing(SPACING);
        absolutePanel.add(horizontalPanel);

        HorizontalPanelDropController columnDropController = new HorizontalPanelDropController(horizontalPanel);
        columnDragController.registerDropController(columnDropController);

        if (!isDropable()){//avval register qilinmasa ui yeb qolyabdi
            columnDragController.unregisterDropControllers();
        }

        //Init initial filter
//        filterParameter = new ListingFilterParameter();
//        filterParameter.setStart(0);
        //Load columns
        loadColumns();
    }

    private void saveKanbanboardSettings() {
        visibleColumns.clear();

        //
        for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {


            if (horizontalPanel.getWidget(i) instanceof VerticalPanel) {
                VerticalPanel column = (VerticalPanel) horizontalPanel.getWidget(i);
                if (column != null) {
                    KanbanColumn kanbanColumn = (KanbanColumn) column.getLayoutData();
                    visibleColumns.add(kanbanColumn);
                }
            }


//                    KanbanColumn kanbanColumn = new KanbanColumn(layoutData.getId(), layoutData.getName(), layoutData.getDescription(), false);

        }

        //Save Settings
        kanbanService.saveKanbanBoardSettings(viewType, pageSize, visibleColumns,false, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void v) {
                LoadingPanel.loading(false);
            }
        });
    }

    private void loadColumns() {
        LoadingPanel.loading(true);
        kanbanService.getKanbanBoardSettings(viewType, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String settings) {
                LoadingPanel.loading(false);
                visibleColumns.clear();
                //If there is saved state of kanban view then take columns of kanban from that stored state (FacetFilter)
                if (!Utils.isNullOrEmpty(settings)) {
                    try {
                        JSONValue jsonValue = JSONParser.parseStrict(settings);
                        JSONObject jsonObject = jsonValue.isObject();
                        pageSize = Double.valueOf(jsonObject.get("pageSize").isNumber().doubleValue()).intValue();
                        JSONObject columns = jsonObject.get("columns").isObject();
                        if (columns != null && columns.keySet().size() > 0) {
                            for (String columnOrderIndex : columns.keySet()) {

                                JSONObject column = columns.get(columnOrderIndex).isObject();
                                if (column.get("hide") == null || (column.get("hide") != null) && !column.get("hide").isBoolean().booleanValue()) {
                                    KanbanColumn col = new KanbanColumn(Double.valueOf(column.get("id").isNumber().doubleValue()).intValue(),
                                            column.get("name").isString().stringValue());

                                    if (column.get("description") != null && column.get("description").isString() != null) {
                                        col.setDescription(column.get("description").isString().stringValue());
                                    }
                                    if (column.get("selected") != null) {
                                        col.setSelected(column.get("selected").isBoolean().booleanValue());
                                    }
                                    if (column.get("draggable") != null) {
                                        col.setDraggable(column.get("draggable").isBoolean().booleanValue());
                                    } else {
                                        col.setDraggable(true);
                                    }

                                    if (column.get("allowEdit") != null) {
                                        col.setAllowEdit(column.get("allowEdit").isBoolean().booleanValue());
                                    } else {
                                        col.setAllowEdit(false);
                                    }
                                    if (column.get("code") != null && column.get("code").isString() != null) {
                                        col.setCode(column.get("code").isString().stringValue());
                                    }

                                    if (column.get("minimized") != null && column.get("minimized").isBoolean() != null) {
                                        col.setMinimized(column.get("minimized").isBoolean().booleanValue());
                                    }

                                    if (column.get("colorid") != null && column.get("colorid").isNumber() != null) {
                                        try {
                                            col.setColorId(Double.valueOf(column.get("colorid").isNumber().doubleValue()).intValue());
                                            //                                    col.setColorName(column.get("colorname").isString().stringValue());
                                            col.setColorHex(column.get("colorhex").isString().stringValue());
                                        } catch (Exception e1) {
                                            GWT.log("Status color is empty, statusid:" + col.getId());
                                            col.setColorHex("#536577");
                                        }
                                    }

                                    if (column.get("visible") != null && column.get("visible").isBoolean().booleanValue()) {
                                        visibleColumns.add(col);
                                    }
                                }
                            }


                            GWT.runAsync(new RunAsyncCallback() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess() {

                                    for (KanbanColumn item : visibleColumns) {
                                        addColumn(item);
                                    }
//                                    if (showFooter){
//                                        addWinAndLose();
//                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        GWT.log(e.getMessage());
                    }
                }
                //If columns are empty then use default columns
                if (visibleColumns.isEmpty()) {
                    if (kanbanBoardDesign != null) {
                        requestCallback = new AbstractAsyncCallback<List<T>>() {
                            @Override
                            public void failure(Throwable throwable) {
                                super.failure(throwable);
                            }

                            @Override
                            public void success(List<T> result) {
                                super.success(result);
                                visibleColumns = (ArrayList<KanbanColumn>) result;

                                GWT.runAsync(new RunAsyncCallback() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        for (KanbanColumn column : visibleColumns) {
                                            addColumn(column);
                                        }
                                    }
                                });

                            }
                        };

                        kanbanBoardDesign.loadDefaultColumns(requestCallback);
                    }
                }
            }
        });
    }

    Object getFromColumnMetadata() {
        return fromColumnMetadata;
    }

    void setFromColumnMetadata(Object fromColumnMetadata) {
        this.fromColumnMetadata = fromColumnMetadata;
    }

    KanbanVerticalPanel getFromColumn() {
        return fromColumn;
    }

    void setFromColumn(KanbanVerticalPanel fromColumn) {
        this.fromColumn = fromColumn;
    }

    public Integer getWidgetIndex() {
        return widgetIndex;
    }

    public void setWidgetIndex(Integer widgetIndex) {
        this.widgetIndex = widgetIndex;
    }

    public Widget getColumnAddButton(SelectItem columnMetadata) {
        return null;
    }

    public void addWinAndLose() {

        horizont = new HorizontalPanel();
        HTML left = new HTML(LOSE);
        HTML right = new HTML(WIN);

        VerticalPanel verticalPanel = new VerticalPanel();
        VerticalPanel verticalPanel2 = new VerticalPanel();

        verticalPanel.add(left);
        verticalPanel2.add(right);

        LoseDropController l = new LoseDropController(verticalPanel, this);
        widgetDragController.registerDropController(l);

        WonDropController w = new WonDropController(verticalPanel2, this);
        widgetDragController.registerDropController(w);


        horizont.add(verticalPanel);
        horizont.add(verticalPanel2);

        horizont.addStyleName("kanban-quickActions fixed-content");

        horizontalPanel.add(horizont);

        showWinAndLose(false);

    }

    public void showWinAndLose(boolean isVisible) {
        if (horizont != null) {
            horizont.setVisible(isVisible);
        }
    }

    public void addColumn(KanbanColumn columnMetadata) {
        VerticalPanel column = new VerticalPanel();
        column.setLayoutData(columnMetadata);
        column.setSpacing(5);
        horizontalPanel.add(column);

        MaterialLabel totalCountLabel = new MaterialLabel("0");
        KanbanVerticalPanel kanbanVerticalPanel = new KanbanVerticalPanel(totalCountLabel);
        //Add Column data
        kanbanVerticalPanel.setLayoutData(columnMetadata);
        kanbanVerticalPanel.addStyleName(COLUMN_COMPOSITE);
        //kanbanVerticalPanel.setHeight(Window.getClientHeight() - 265 + "px");
        kanbanVerticalPanel.setSpacing(SPACING);


        MaterialPanel headerWrapper = new MaterialPanel("kanban_header_wrapper");
        MaterialPanel header = getColumnHeader(columnMetadata, totalCountLabel);
        headerWrapper.add(header);
        //Add (+) button for column
        Widget columnAddButton = getColumnAddButton(columnMetadata);
        if (columnAddButton != null) {
            headerWrapper.add(columnAddButton);
        }
        column.add(headerWrapper);
        //End of Add (+) button for column

        ScrollPanel scrollPanel = new ScrollPanel(kanbanVerticalPanel);
        scrollPanel.setStyleName("kanban-column-scrollpanel");


        MaterialPanel columnBody = new MaterialPanel("wg_canban__column-body");
//        if (hasTabs) {
//            if kanbanboard inside views within tab like Project view then make heigh less
//            columnBody.setHeight(Window.getClientHeight() - 345 + "px");
//        } else {
//            columnBody.setHeight(Window.getClientHeight() - 305 + "px");
//        }

        //For Opportunity
        MaterialPanel totalAmountPanel = getColumnHeaderTotal(columnMetadata);
        if (totalAmountPanel != null) {
            columnBody.add(totalAmountPanel);
        }

        columnBody.add(scrollPanel);
        column.add(columnBody);
        //make column draggable
        columnDragController.makeDraggable(column, header);

        filterParameter.setLimit(pageSize);

        //Init and load data of column
        DraggableColumn<T> draggableColumn = new DraggableColumn<T>(columnMetadata, kanbanDataLoader, kanbanBoardDesign,
                widgetDragController, kanbanVerticalPanel, this, totalAmountPanel,
                kanbanItemSettingsType != null ? relatedFieldsMapBySectionName.get(kanbanItemSettingsType.getCode()) : null);
        draggableColumn.init();

        //Button to minimize column
        MaterialLink minimizeColumnLink = new MaterialLink();
        minimizeColumnLink.setStyleName("wg_canban__collapse-toggle");
        minimizeColumnLink.getElement().setAttribute("style", "background-color: " + columnMetadata.getColorHex());
        Icon minimize = new Icon();
        minimize.setStyleName("ficon--upload");
        minimizeColumnLink.add(minimize);
        minimizeColumnLink.addClickHandler(clickEvent -> {
            if (column.getStyleName().contains("wg_canban__column--hide")) {
                column.removeStyleName("wg_canban__column--hide");
                ((KanbanColumn) column.getLayoutData()).setMinimized(false);
                draggableColumn.reloadData();
            } else {
                column.addStyleName("wg_canban__column--hide");
                ((KanbanColumn) column.getLayoutData()).setMinimized(true);
            }
            //Save Columns State (minimized or expanded)
            saveKanbanboardSettings();
        });
        if (columnMetadata.isMinimized()) {
            column.addStyleName("wg_canban__column--hide");
        }
        headerWrapper.add(minimizeColumnLink);
        //End Button to minimize column

        scrollPanel.addScrollHandler(scrollEvent -> {
//                MaterialToast.fireToast(scrollEvent + "");

            //Store scroll position
            scrollPositions.put(scrollPanel, scrollPanel.getVerticalScrollPosition());

            if (scrollPanel.getVerticalScrollPosition() == scrollPanel.getMaximumVerticalScrollPosition() - 1) {
                if (kanbanVerticalPanel.getTotalCount() > kanbanVerticalPanel.getWidgetCount() - 1) {
//                        MaterialToast.fireToast("Dynamic data loader Event triggered :" + (draggableColumn.getPosition() + pageSize));
                    draggableColumn.setPosition(draggableColumn.getPosition() + pageSize);
                    draggableColumn.loadData();
                }
            }
        });

        //Register onDROP behaviour
        KanbanDropController widgetDropController = new KanbanDropController(kanbanVerticalPanel, kanbanDataLoader, this);
        widgetDragController.registerDropController(widgetDropController);

        columnsMap.put(columnMetadata.getId(), draggableColumn);

//        MaterialScrollfire.apply(scrollPanel.getElement(), () -> {
//            MaterialToast.fireToast("1 ScrollFire Event");
//            draggableColumn.setPosition(draggableColumn.getPosition() + pageSize);
//            draggableColumn.loadData();
//            MaterialToast.fireToast("2 ScrollFile Event");
//        });
       /* MaterialScrollfire.apply(listContainer.getElement(), () -> {
  MaterialAnimator.animate(Transition.SHOW_STAGGERED_LIST, listContainer, 0);
        });
        MaterialScrollfire.apply(image.getElement(), () -> {
  MaterialAnimator.animate(Transition.FADE_IN_IMAGE, image, 0);
        });*/
    }

    public MaterialPanel getColumnHeaderTotal(SelectItem columnMetadata) {
        return null;
    }

    private MaterialPanel getColumnHeader(SelectItem columnMetadata, MaterialLabel totalCount) {
        MaterialPanel header = new MaterialPanel();
        header.addStyleName(CSS_KANBAN_DND_PANEL_HEADING);
//        header.addStyleName(CSS_KANBAN_DND_PANEL_HEADING + "-" + Random.nextInt(6));
        header.getElement().setAttribute("style", "border-color: " + columnMetadata.getColorHex());
        MaterialLabel wg_canban__column_header_title = new MaterialLabel(columnMetadata.getName());
        wg_canban__column_header_title.setStyleName("wg_canban__column-header-title");
        wg_canban__column_header_title.getElement().setAttribute("title", columnMetadata.getName());
        header.add(wg_canban__column_header_title);
        header.add(new MaterialLabel(" "));
        Span wg_canban__column_header_totalcount = new Span();
        wg_canban__column_header_totalcount.setStyleName("wg_canban__column-header-totalcount");
//        header.add(totalCount);
        wg_canban__column_header_totalcount.add(totalCount);
        header.add(wg_canban__column_header_totalcount);
        return header;
    }

    public void reloadColumn(Integer id) {
        DraggableColumn draggableColumn = columnsMap.get(id);
        if (draggableColumn != null) {
            draggableColumn.reloadData();
        }
    }

    public void reloadAllColumns() {
        for (HashMap.Entry<Integer, DraggableColumn> column : columnsMap.entrySet()) {
            reloadColumn(column.getKey());
        }
    }

    public void clearAllColumns() {
        for (HashMap.Entry<Integer, DraggableColumn> column : columnsMap.entrySet()) {
            DraggableColumn draggableColumn = columnsMap.get(column.getKey());
            if (draggableColumn != null) {
                draggableColumn.getItemContainer().clear();
            }
        }
    }

    public HashMap<Integer, DraggableColumn> getColumnsMap() {
        return columnsMap;
    }

    public ListingFilterParameter getFilterParameter() {
        if (filterParameter != null && filterParameter.getFacetFilter() != null) {
            filterParameter.getFacetFilter().setType(getViewType());
        }
        if (filterParameter != null) {
            filterParameter.setLimit(pageSize);
        }
        return filterParameter;
    }

    public void setFilterParameter(ListingFilterParameter filterParameter) {
        this.filterParameter = filterParameter;
    }

    public ListPanelType getViewType() {
        return viewType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isDropable() {
        return isDropable;
    }

    public void setDropable(boolean dropable) {
        isDropable = dropable;
    }

    KanbanBoardDesign getKanbanBoardDesign() {
        return kanbanBoardDesign;
    }

    ArrayList<KanbanColumn> getVisibleColumns() {
        return visibleColumns;
    }

    public void resetScrollPositions() {
        for (Map.Entry<ScrollPanel, Integer> entry : scrollPositions.entrySet()) {
            entry.getKey().setVerticalScrollPosition(entry.getValue());
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-customScrollArea");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-customScrollArea");
    }

    public interface OnDropCard {

        void onDropCard();
    }

    private void getKanbanItemRelatedFieldsMap() {
        CommonService.App.get().getKanbanItemFieldsAsMap(new AsyncCallback<HashMap<String, KanbanItemColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(HashMap<String, KanbanItemColumnConfigs[]> stringMap) {
                if (stringMap == null) {
                    relatedFieldsMapBySectionName = null;
                    return;
                }
                stringMap.forEach((k, v) -> {
                    HashMap<String, KanbanItemColumnConfigs> reletedMap = new HashMap<>();
                    for (KanbanItemColumnConfigs item : v) {
                        if (item == null) continue;
                        reletedMap.put(item.getCode(), item);

                    }
                    relatedFieldsMapBySectionName.put(k, reletedMap);
                });
            }
        });
    }
}
