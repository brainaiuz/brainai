package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Aug-2010
 * Time: 17:01:43
 * <p/>
 * <b> Companent Facet Filter Popup </b>
 * <p/>
 * If You Uses Companet Not Understanding Please Read This is File
 * <p/>
 * com/edatasite/workforce/gwt/core/client.ui/facetfilter/instruction.txt
 * <p/>
 * or TaskListView class
 */
public class FacetFilterPopup extends KpiSideNavBox implements ClickHandler {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    private int columnCount;
    private final String title;
    private String name;
    private final Integer typeParentId;
    private Integer saveFilterId;
    private Integer defaultFilterId;
    private boolean filterDataNotSaved = false;

    private MaterialDropDown menu;
    private VerticalPanelDiv content;
    private DataListBox savedList;
    private final ListingPanel listingPanel;
    private MaterialLink addFacetContent;

    private FacetFilterRpc facetFilterRpc;
    private final FacetApplySave facetApplySave;
    private final FacetCallbackProvider callbackProvider;
    private final FacetContentConfigure facetContentConfigure;
    private final DatePeriodFacetContent datePeriodFacetContent;
    private FacetDataCallback callback;
    private FacetSaveCallback saveCallback;
    private FacetSaveListCallback saveListCallback;

    private WfmButton2 close;
    private WfmButton2 apply;
    private WfmButton2 reset;
    private WfmButton2 saveFilter;
    private MaterialLink delete;
    private final ListPanelType listPanelType;
    private DataListBox filterByDate;
    private DatePicker startDate;
    private DatePicker endDate;
    private DataListBox terms;
    private TextBox less;
    private TextBox more;
    private FacetContentPanel datePeriodPanel;
    private FacetContentPanel quantityRangePanel;
    private PickupDragController dragController;
    private VerticalPanelWithSpacer[] verticalPanel;
    private VerticalPanelDropController[] verticalPanelDropControllers;
    private HashMap<String, FacetContentPanel> contentsMap;
    private FacetFilterSavePopup saveFilterPopup;

    private Boolean enableDate = true;
    private boolean canAddNewFilter = true;

    /**
     * <i>... Create Facet Filter save,savedList,delete,applyFiler methods ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {18:11 21/06/2011} ...</i>
     *
     * @return
     */
    private static FacetApplySave getFacetApplySave(final ListingPanel listingPanel, final ListPanelType type, final Integer typeId) {
        return new FacetApplySave() {
            @Override
            public void getSaveFilterListItem(final FacetSaveListCallback saveCallback) {
                CommonService.App.get().getSavedFacetFilterList(type, typeId, new AbstractAsyncCallback<SaveFilterSelectItems>() {
                    @Override
                    public void failure(Throwable throwable) {
                        saveCallback.onFailure(throwable);
                    }

                    @Override
                    public void success(SaveFilterSelectItems saveFilterSelectItems) {
                        saveCallback.onSuccess(saveFilterSelectItems);
                    }
                });
            }

            @Override
            public void saveFilter(FacetFilterRpc facetFilterRpc, final FacetSaveCallback saveCallback) {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("facetFilterRpc", facetFilterRpc);
                paramMap.put("type", type);
                CommonService.App.get().saveFacetFilter(paramMap, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        saveCallback.onFailure(throwable);
                    }

                    @Override
                    public void success(Integer id) {
                        saveCallback.onSuccess(id);
                    }
                });
            }

            @Override
            public void applyFilter(FacetFilterRpc taskFacetFilter) {
                listingPanel.getFilterParametrs().setFacetFilter(taskFacetFilter);
                if (listingPanel.isListingPage()) {
                    listingPanel.gotoPageing(true);
                } else {
                    listingPanel.requestKanbanData();
                }
            }

            @Override
            public void deleteFilter(Integer deleleFilterId, AsyncCallback<Void> callback) {
                CommonService.App.get().deleteFacetFilter(deleleFilterId, callback);
            }
        };
    }

    public FacetFilterPopup(FacetFilterRpc facetFilterRpc, ListingFacetFilter listingFacetFilter, ListingPanel listingPanel, ListPanelType type, Integer typeId, Boolean enableDate, FacetContentConfigure facetContentConfigure, boolean canAddNewFilter) {
        super(WIDE_FORM_WIDTH);
        addStyleName("quick-add");

        this.facetFilterRpc = facetFilterRpc;
        this.facetApplySave = getFacetApplySave(listingPanel, type, typeId);
        this.callbackProvider = listingFacetFilter.getFacetCallbackProvider();
        this.datePeriodFacetContent = listingFacetFilter.getPeriodDateContent();
        this.facetContentConfigure = facetContentConfigure;
        this.title = facetContentConfigure.getFacetFilterTitle();
        this.listPanelType = type;
        this.typeParentId = typeId;
        this.listingPanel = listingPanel;
        //this.setTitle(title);
        this.setEnableDate(enableDate);
        this.canAddNewFilter = canAddNewFilter;

        //setSize(1000, 1000);
        initialization();
        drawFacetFilter();
        buttonsEvent();
        saveListEvent();
        datesEvents();
        initCallback();
    }

    public void resetFacetFilter() {
        resetFilter();
        applyFilter();
    }

    private void applyFilter() {
//        listingPanel.gotoPageing(0, true);
        if (facetFilterRpc.isFilterChanges()) {
            setSelectedData();
            facetFilterRpc.setApplyFilter(true);
        }
        if (savedList.getSelectedId() != null) {
            facetFilterRpc.setObjectID(savedList.getSelectedId());
        }
        facetApplySave.applyFilter(facetFilterRpc);
        listingPanel.setFacetFilterSelectedId(facetFilterRpc.getObjectID());
        remove();
    }

    private void resetFilter() {
        clearOldSaveData(true);
        setDefaultValueDate();
        facetFilterRpc.setFilterChanges(true);
        setAllButtonsEanbled(true);
        getRequestData(facetFilterRpc);
    }

    /**
     * Initialization all Widgets
     */
    private void initialization() {
        assert facetContentConfigure.getFacetFilterTitle() != null : "Exception: " + title + " Contents Title Must be Not Empty";
        contentsMap = new HashMap<>();

        for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
            contentsMap.put(facetCodeName, new FacetContentPanel(facetCodeName, facetContentConfigure.getContentTitle(facetCodeName)));
        }
        savedList = new DataListBox();
        if (ListPanelType.GoodsDeliveredNoteListPanel.equals(listPanelType)) {
            if (!Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                savedList.setWithoutNullLabel(true);
            }
        }
//        savedList.setWidth("200px");

        filterByDate = new DataListBox();
//        filterByDate.setWidth("100px");
        terms = new DataListBox();
        terms.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.custom()),
                new SelectItem(1, wfmStrings.today()),
                new SelectItem(2, wfmStrings.yesterday()),
                new SelectItem(3, wfmStrings.thisWeek()),
                new SelectItem(4, wfmStrings.thisMonth()),
                new SelectItem(5, wfmStrings.sinceToday()),
                new SelectItem(6, wfmStrings.sinceYesterday()),
                new SelectItem(7, wfmStrings.sinceThisWeek()),
                new SelectItem(8, wfmStrings.sinceThisMonth())
        });
        terms.setSelected(0);
        startDate = new DatePicker();
//        startDate.setWidth("100px");
        // startDate.setDefaultValue();
        endDate = new DatePicker();
//        endDate.setWidth("100px");
        //endDate.setDefaultValue();

        less = new TextBox();
        less.setWidth("100px");
        Validation.addNumericKeyboardListener(less);

        more = new TextBox();
        more.setWidth("100px");
        Validation.addNumericKeyboardListener(more);

        close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.ensureDebugId("close_button");
        apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
        apply.ensureDebugId("apply_button");
        reset = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        reset.ensureDebugId("reset_button");
        saveFilter = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveFilter.ensureDebugId("saveFilter_button");
        delete = new MaterialLink();
        delete.addStyleName("btn btn--icon btn--white");
        delete.ensureDebugId("delete_button");

        MaterialPanel facetHeaderTitle = new MaterialPanel("side-nav__title-group");
        Span titleText = new Span(this.title);

        facetHeaderTitle.add(titleText);
        this.addHeader(facetHeaderTitle);

        content = new VerticalPanelDiv();
        /*content = new FlexTable();
        content.setCellPadding(5);
        content.setCellSpacing(5);*/
        this.addBody(content);

        if (facetFilterRpc == null) {
            facetFilterRpc = new FacetFilterRpc(facetContentConfigure.getShowCodeNameCloneList(),
                    facetContentConfigure.getShowSolrCloneFields(),
                    facetContentConfigure.getHideSolrCloneFields(),
                    facetContentConfigure.getSolrDateFields());
            facetFilterRpc.setType(listPanelType);
            facetFilterRpc.setTypeId(typeParentId);
            delete.setVisible(false);
        } else {
            if (facetFilterRpc.getObjectID() == null) {
                for (String key : facetFilterRpc.getShowSolrFieldMap().keySet()) {
                    if (facetFilterRpc.getFacetContentMap().get(key).getFacetItems().length != 0) {
                        filterDataNotSaved = true;
                    }
                }
                delete.setVisible(false);
            }
            setSaveFilterId(facetFilterRpc.getObjectID());
            if (facetFilterRpc.getDateFilterByList().size() != 0) {
                filterByDate.setItems(facetFilterRpc.getDateFilterByList().toArray(new SelectItem[]{}));
                if (!Utils.isNullOrEmpty(facetFilterRpc.getSelectedDateSolrCodeName())) {
                    filterByDate.setSelected(facetFilterRpc.getSelectedDateSolrCodeName().hashCode());
                }
            }
            if (facetFilterRpc.getStartDate() != null) {
                startDate.setDate(facetFilterRpc.getStartDate());
            }
            if (facetFilterRpc.getEndDate() != null) {
                endDate.setDate(facetFilterRpc.getEndDate());
            }

            if (facetFilterRpc.getMore() != null) {
                more.setValue(String.valueOf(facetFilterRpc.getMore()));
            }
            if (facetFilterRpc.getLess() != null) {
                less.setValue(String.valueOf(facetFilterRpc.getLess()));
            }
            if (facetFilterRpc.getDateTermId() != null) {
                terms.setSelected(facetFilterRpc.getDateTermId());
                if (!facetFilterRpc.getDateTermId().equals(0)) {
                    startDate.setEnabled(false);
                    endDate.setEnabled(false);
                }
            }
        }
    }

    /**
     * Draw filter popup
     */
    private void drawFacetFilter() {
        assert facetContentConfigure.getHowMuchColumn() != 0 : "Exception: " + title + " Table Columns Must be Not Zero";
        initFacetTopField();
        initFacetContentField();
        initFacetBottomField();
    }

    /**
     * Facet Top Widgets
     */
    private void initFacetTopField() {
        if (facetContentConfigure.isRangePanelEnabled()) { //used Product/Service list
            FlexTable rangeFilter = new FlexTable();
            rangeFilter.setCellPadding(5);
            rangeFilter.setCellSpacing(5);
            int row = 0;

            rangeFilter.setHTML(row, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.less() + ":</b>");
            rangeFilter.setWidget(row++, 1, less);
            rangeFilter.setHTML(row, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.more() + ":</b>");
            rangeFilter.setWidget(row++, 1, more);
            quantityRangePanel = new FacetContentPanel(wfmStrings.qtyOnHand(), rangeFilter, FacetSettingRpc.FACET_QUANTITY_RANGE);
        }


        //Date filter container
        FlexTable dateFilter = new FlexTable();
        dateFilter.setCellPadding(5);
        dateFilter.setCellSpacing(5);
        dateFilter.setStyleName("spacing5-padding5");
        int rowIndex = 0;
        if (facetFilterRpc.getDateFilterByList().size() != 0) {
            dateFilter.setHTML(rowIndex, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.filterBy() + ":</b>");
            dateFilter.setWidget(rowIndex++, 1, filterByDate);
        }
        dateFilter.setHTML(rowIndex, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.condition() + ":</b>");
        dateFilter.setWidget(rowIndex++, 1, terms);
        dateFilter.setHTML(rowIndex, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.whenFrom() + ":</b>");
        dateFilter.setWidget(rowIndex++, 1, startDate);
        dateFilter.setHTML(rowIndex, 0, "<b style='color:#1f4f8f;white-space:nowrap;'>" + wfmStrings.to() + ":</b>");
        dateFilter.setWidget(rowIndex, 1, endDate);

        datePeriodPanel = new FacetContentPanel(wfmStrings.period(), dateFilter, FacetSettingRpc.FACET_DATE_PERIOD);
        datePeriodFacetContent.getDateFacetContent(dateFilter);


        //saved filter dropdown container
        MaterialPanel headerActionsContainer = new MaterialPanel("filter-base-settings");

        //saved filter drowdown container
        MaterialPanel pnlSavedList = new MaterialPanel("input-group fullWidth");
        MaterialPanel slCotainer = new MaterialPanel("input-group-prepend");
        delete.add(new SvgIcon(SvgEnum.trash2));
        slCotainer.add(delete);
        pnlSavedList.add(slCotainer);

        pnlSavedList.add(savedList);

        gwt.material.design.client.ui.html.Label savedListTitle = new gwt.material.design.client.ui.html.Label(wfmStrings.viewSavedFilters());
        savedListTitle.setClass("form-group__label");

        MaterialPanel pnlSavedListFormGroup = new MaterialPanel("filter-base-settings__half");
        pnlSavedListFormGroup.add(savedListTitle);
        pnlSavedListFormGroup.add(pnlSavedList);
        headerActionsContainer.add(pnlSavedListFormGroup);
        if (canAddNewFilter) {
            addFacetContent = new MaterialLink(wfmStrings.add());
            addFacetContent.setStyleName("btn dropdown-button hasicon--right btn--success");
            MaterialIcon addIcon = new MaterialIcon();
            addIcon.setClass("ficon--more-horiz");

            addFacetContent.add(addIcon);
            menu = new MaterialDropDown(addFacetContent);
            menu.setBelowOrigin(true);
//        addFacetContent.add(menu);
            addHideFacetContentMenu();

            MaterialPanel savedFilterActionContainer = new MaterialPanel("filter-base-settings__half filter-base-setting__actions");
            //savedFilterActionContainer.add(delete);
            MaterialPanel menuPanel = new MaterialPanel("dropdown-select dropdown-kit--below");
            menuPanel.add(addFacetContent);
            menuPanel.add(menu);
            savedFilterActionContainer.add(menuPanel);

            headerActionsContainer.add(savedFilterActionContainer);
        }

        content.add(headerActionsContainer);
    }

    private void addHideFacetContentMenu() {
        menu.clear();

        int i = 1;
        for (final String hideFacetCodeName : facetFilterRpc.getHideSolrFieldMap().keySet()) {
            MaterialLink facetContent = new MaterialLink(facetContentConfigure.getContentTitle(hideFacetCodeName));
            facetContent.addClickHandler(ch -> {
                addFacetContent(hideFacetCodeName);
                facetFilterRpc.removeHideFacetContent(hideFacetCodeName);
                refreshFilter();
                addHideFacetContentMenu();
            });
            facetContent.ensureDebugId("Related_Project_" + i++);
            menu.add(facetContent);
        }
        addFacetContent.setVisible(facetFilterRpc.getHideSolrFieldMap().size() != 0);
    }

    private void addFacetContent(final String facetCodeName) {
        FacetSettingRpc facetSettingRpc = facetFilterRpc.getFacetSettingMap().get(facetCodeName);
        if (facetSettingRpc == null) {
            facetSettingRpc = getNewFacetSetting();
            facetFilterRpc.getFacetSettingMap().put(facetCodeName, facetSettingRpc);
            contentsMap.put(facetCodeName, new FacetContentPanel(facetCodeName, facetContentConfigure.getContentTitle(facetCodeName)));
            HashSet<String> facetCodeSet = new HashSet<>();
            facetCodeSet.add(facetCodeName);
            captionCheckBoxEvent(facetCodeSet);
            facetListEvent(facetCodeSet);
        }
        if (FacetSettingRpc.FACET_DATE_PERIOD.equals(facetCodeName)) {
            datePeriodPanel.setSetting(facetSettingRpc);
            verticalPanel[facetSettingRpc.getCell() - 1].insert(datePeriodPanel, facetSettingRpc.getRow());
            dragController.makeDraggable(datePeriodPanel, datePeriodPanel.getCaptionLabel());
        } else if (FacetSettingRpc.FACET_QUANTITY_RANGE.equals(facetCodeName)) {
            quantityRangePanel.setSetting(facetSettingRpc);
            verticalPanel[facetSettingRpc.getCell() - 1].insert(quantityRangePanel, facetSettingRpc.getRow());
            dragController.makeDraggable(quantityRangePanel, quantityRangePanel.getCaptionLabel());
        } else {
            final FacetContentPanel contentPanel = contentsMap.get(facetCodeName);
            contentPanel.setSetting(facetSettingRpc);
            contentPanel.addCloseHandler(clickEvent -> {
                contentPanel.removeFromParent();
                facetFilterRpc.removeShowFacetCodeName(facetCodeName);
                addHideFacetContentMenu();
                contentsMap.remove(facetCodeName);
            });
            verticalPanel[facetSettingRpc.getCell() - 1].insert(contentPanel, facetSettingRpc.getRow());
            dragController.makeDraggable(contentPanel, contentPanel.getCaptionLabel());
        }
    }

    private FacetSettingRpc getNewFacetSetting() {
        FacetSettingRpc facetSettingRpc = new FacetSettingRpc();
        int row = 1000000;
        int cell = 0;
        for (int i = 0; i < facetContentConfigure.getHowMuchColumn(); i++) {
            if (verticalPanel[i].getWidgetCount() < row) {
                row = verticalPanel[i].getWidgetCount();
                cell = i + 1;
            }
        }
        facetSettingRpc.setCell(cell);
        facetSettingRpc.setRow(row);
        return facetSettingRpc;
    }

    /**
     * Facet Body Widgets
     */
    private void initFacetContentField() {
        int cellCount = facetContentConfigure.getHowMuchColumn();
        int rowCount = facetContentConfigure.getContentTitle().size() + 1 > cellCount ? 2 : 1;

        AbsolutePanel targetPanel = new AbsolutePanel();
        targetPanel.setSize("100%", "100%");

        MaterialPanel scrollPanel = new MaterialPanel();
        scrollPanel.getElement().addClassName("facetFiltersBody");
        scrollPanel.add(targetPanel);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.getElement().addClassName("table_2col facetFilters");
        targetPanel.add(horizontalPanel);

        dragController = new PickupDragController(targetPanel, false);
        dragController.setBehaviorMultipleSelection(false);
        dragController.setBehaviorDragProxy(true);
        verticalPanel = new VerticalPanelWithSpacer[cellCount];
        verticalPanelDropControllers = new VerticalPanelDropController[cellCount];

        for (int i = 0; i < cellCount; i++) {
            verticalPanel[i] = new VerticalPanelWithSpacer(i + 1);
            verticalPanelDropControllers[i] = new VerticalPanelDropController(verticalPanel[i]);
            horizontalPanel.add(verticalPanel[i]);
            horizontalPanel.setCellHorizontalAlignment(verticalPanel[i], HasAlignment.ALIGN_LEFT);
            dragController.registerDropController(verticalPanelDropControllers[i]);
        }
        if ((facetFilterRpc.getObjectID() == null) || (facetFilterRpc.getObjectID() != null && facetFilterRpc.isFilterChanges())) {
            initFacetContentWidgets(true);
        }
        content.add(scrollPanel);
        initBodyScrollContent();
    }

    private void initFacetContentWidgets(boolean isFirstOpen) {
        int cellCount = facetContentConfigure.getHowMuchColumn();
        if (facetFilterRpc.getFacetSettingMap().size() > 0) {
            for (final String facetCodeName : facetFilterRpc.getFacetSettingMap().keySet()) {
                if (isFirstOpen) {
                    facetFilterRpc.getFacetSettingMap().get(facetCodeName).setOriginalRow(facetFilterRpc.getFacetSettingMap().get(facetCodeName).getRow());
                }
                addFacetContent(facetCodeName);
            }
            if (isFirstOpen) {
                for (VerticalPanelWithSpacer aVerticalPanel : verticalPanel) {
                    aVerticalPanel.regenerateContent();
                }
            }
        } else {
            int cell = 0;
            int row = 0;
            for (final String facetCodeName : facetFilterRpc.getShowFacetCodeName()) {
                if (cell >= cellCount) {
                    cell = 0;
                    row++;
                }
                FacetSettingRpc facetSettingRpc = new FacetSettingRpc();
                facetFilterRpc.getFacetSettingMap().put(facetCodeName, facetSettingRpc);

                final FacetContentPanel contentPanel = contentsMap.get(facetCodeName);
                contentPanel.setSetting(facetSettingRpc);
                contentPanel.addCloseHandler(clickEvent -> {
                    contentPanel.removeFromParent();
                    facetFilterRpc.removeShowFacetCodeName(facetCodeName);
                    addHideFacetContentMenu();
                    contentsMap.remove(facetCodeName);
                });

                verticalPanel[cell].insert(contentPanel, row);
                dragController.makeDraggable(contentPanel, contentPanel.getCaptionLabel());
                cell++;
            }
            if (facetContentConfigure.isRangePanelEnabled()) {
                if (cell >= cellCount) {
                    cell = 0;
                    row++;
                }
                FacetSettingRpc fSettingRpc = new FacetSettingRpc();
                fSettingRpc.setCell(cell);
                fSettingRpc.setRow(row);
                facetFilterRpc.getFacetSettingMap().put(FacetSettingRpc.FACET_QUANTITY_RANGE, fSettingRpc);
                quantityRangePanel.setSetting(fSettingRpc);
                verticalPanel[cell].insert(quantityRangePanel, row);
                dragController.makeDraggable(quantityRangePanel, quantityRangePanel.getCaptionLabel());
                cell++;
            }

            if (facetContentConfigure.isDatePeriodPanelEnabled()) {
                if (cell >= cellCount) {
                    cell = 0;
                    row++;
                }
                FacetSettingRpc facetSettingRpc = new FacetSettingRpc();
                facetSettingRpc.setCell(cell);
                facetSettingRpc.setRow(row);
                facetFilterRpc.getFacetSettingMap().put(FacetSettingRpc.FACET_DATE_PERIOD, facetSettingRpc);

                datePeriodPanel.setSetting(facetSettingRpc);
                verticalPanel[cell].insert(datePeriodPanel, row);
                dragController.makeDraggable(datePeriodPanel, datePeriodPanel.getCaptionLabel());
            }
        }
        HashSet<String> facetCodeSet = new HashSet<>(facetFilterRpc.getShowSolrFieldMap().keySet());
        captionCheckBoxEvent(facetCodeSet);
        facetListEvent(facetCodeSet);
    }

    /**
     * Facet Bottom Widgets
     */
    private void initFacetBottomField() {
        MaterialPanel rightContainer = new MaterialPanel();
        rightContainer.add(apply);
        if (ListPanelType.GoodsDeliveredNoteListPanel.equals(listPanelType)) {
            if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                rightContainer.add(saveFilter);
            }
            if (Utils.hasPermission(PermissionConstants.RESET_FILTER)) {
                rightContainer.add(reset);
            }
        } else {
            rightContainer.add(saveFilter);
            rightContainer.add(reset);

        }

//        rightContainer.add(remove);

        this.addFooter(rightContainer);
//        content.setWidget(2, 0, reset);
//        content.getFlexCellFormatter().setAlignment(2, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT(), VerticalPanel.ALIGN_MIDDLE);
//        content.setWidget(2, 1, exportPanel);
//        content.getFlexCellFormatter().setAlignment(2, 1, Utils.HORIZONTAL_ALIGNMENT_RIGHT(), VerticalPanel.ALIGN_MIDDLE);
    }

    /**
     * Caption checkbox event
     */
    private void captionCheckBoxEvent(HashSet<String> facetCodeNameSet) {
        for (String facetCodeName : facetCodeNameSet) {
            final FacetContentPanel facetContentPanel = contentsMap.get(facetCodeName);
            facetContentPanel.addCapCheckBoxClickHandler(event -> {
                facetFilterRpc.setFilterChanges(true);
                facetContentPanel.setSelectedIndex(facetContentPanel.getCapCheck().getValue());
                facetContentPanel.getFacetList().setAllClick(facetContentPanel.isSelectedIndex());
                facetFilterRpc.getFacetContentMap().get(facetContentPanel.getFacetCodeName()).setFacetItems(facetContentPanel.getFacetList().getSelectItems());
                getRequestData(facetFilterRpc);
            });
        }
    }

    /**
     * Facet List events
     */
    private void facetListEvent(HashSet<String> facetCodeNameSet) {
        for (String facetCodeName : facetCodeNameSet) {
            final FacetContentPanel facetContentPanel = contentsMap.get(facetCodeName);
            facetContentPanel.getFacetList().addClickCheckBox(value -> {
                facetFilterRpc.setFilterChanges(true);
                facetContentPanel.setSelectedIndex(!(!value && facetContentPanel.getFacetList().getSelectItems().length == 0));
                facetFilterRpc.getFacetContentMap().get(facetContentPanel.getFacetCodeName()).setFacetItems(facetContentPanel.getFacetList().getSelectItems());
                getRequestData(facetFilterRpc);
            });
        }
    }

    /**
     * Button events
     */
    private void buttonsEvent() {
        apply.addClickHandler(be -> applyFilter());

        reset.addClickHandler(be -> resetFilter());

        saveFilter.addClickHandler(be -> openFacetFilter());

        delete.addClickHandler(baseEvent -> {
            WfmMessageBox deleteMessage = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, wfmMessages.sureYouWantToDelete(wfmMessages.selectedFiltr(), "?"),
                    new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            deletedSelectedFilter();
                        }
                    });
            deleteMessage.center();
        });
        close.addClickHandler(event -> remove());
    }

    private void deletedSelectedFilter() {
        LoadingPanel.loading(true);
        facetApplySave.deleteFilter(savedList.getSelectedId(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                facetFilterRpc = new FacetFilterRpc(facetContentConfigure.getShowCodeNameCloneList(),
                        facetContentConfigure.getShowSolrCloneFields(),
                        facetContentConfigure.getHideSolrCloneFields(),
                        facetContentConfigure.getSolrDateFields());
                facetFilterRpc.setType(listPanelType);
                facetFilterRpc.setTypeId(typeParentId);
                savedList.setSelectedNullLabel();
                listingPanel.getListPanelSaveFacetFilterItems(false, null);
                changeFacetFilterStructure(facetFilterRpc);
                initFacetContentWidgets(false);
                resetFacetFilter();
            }
        });
    }

    /**
     * set Selected Data
     */
    private void setSelectedData() {
        for (String key : facetFilterRpc.getShowSolrFieldMap().keySet()) {
            if (facetFilterRpc.getFacetContentMap().get(key) != null) {
                facetFilterRpc.getFacetContentMap().get(key).setSavedItems(putSelectItemToMap(facetFilterRpc.getFacetContentMap().get(key).getFacetItems()));
            }
        }
    }

    /**
     * Put SelectItem by Id to Map
     *
     * @param facetItems
     * @return
     */
    private HashMap<Integer, Integer> putSelectItemToMap(SelectItem[] facetItems) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (SelectItem facetItem : facetItems) {
            map.put(facetItem.getId(), facetItem.getId());
        }
        return map;
    }

    /**
     * Open Save Popup
     */
    private void openFacetFilter() {
        if (savedList.getSelectedId() != null) {
            facetFilterRpc.setObjectID(savedList.getSelectedItem().getId());
            facetFilterRpc.setName(savedList.getSelectedItem().getName());
            facetFilterRpc.setPublicFilter(publicFields.get(savedList.getSelectedItem().getId()));
        }
        saveFilterPopup = new FacetFilterSavePopup(defaultFilterId, facetFilterRpc, saveCallback);
        saveFilterPopup.setSaveFacetFilter(facetApplySave);
//        saveFilterPopup.center();
        saveFilterPopup.open();
    }

    /**
     * Request Data
     *
     * @param facetFilterRpc
     */
    private void getRequestData(FacetFilterRpc facetFilterRpc) {
        LoadingPanel.loading(true, FacetFilterPopup.this);
        callbackProvider.getFacetFilterData(facetFilterRpc, callback);
    }

    /**
     * Set Dates to Rpc Object
     */
    private void setDates() {
        if (facetFilterRpc.getDateFilterByList().size() != 0 && filterByDate.getSelectedId() != null) {
            facetFilterRpc.setSelectedDateSolrCodeName(filterByDate.getSelectedItem().getDescription());
        } else {
            facetFilterRpc.setSelectedDateSolrCodeName(null);
        }
        if (!wfmStrings.pleaseSelect().equals(startDate.getText()) && startDate.getDate() != null) {
            facetFilterRpc.setStartDate(DateUtil.resetTime(startDate.getDate()));
        } else {
            facetFilterRpc.setStartDate(null);
        }
        if (!wfmStrings.pleaseSelect().equals(endDate.getText()) && endDate.getDate() != null) {
            facetFilterRpc.setEndDate(DateUtil.getDayLastTime(endDate.getDate()));
        } else {
            facetFilterRpc.setEndDate(null);
        }
        if (!"".equals(more.getValue()) && more.getValue() != null) {
            facetFilterRpc.setMore(Integer.valueOf(more.getValue()));
        } else {
            facetFilterRpc.setMore(null);
        }
        if (!"".equals(less.getValue()) && less.getValue() != null) {
            facetFilterRpc.setLess(Integer.valueOf(less.getValue()));
        } else {
            facetFilterRpc.setLess(null);
        }

        facetFilterRpc.setDateTermId(terms.getSelectedId());
    }

    /**
     * Set Default Value
     */
    private void setDefaultValueDate() {
        filterByDate.clearSelected();
        terms.setSelected(0);
        if (facetContentConfigure.isRangePanelEnabled()) {
            more.setValue("");
            less.setValue("");
        }
    }

    /**
     * Saved List event
     */
    private Integer lastSelectedFilter = null;

    private void saveListEvent() {
        savedList.addValueChangeHandler(event -> {
            if (lastSelectedFilter == null || event.getValue() == null || !lastSelectedFilter.equals(event.getValue().getId())) {
                lastSelectedFilter = event.getValue() != null && event.getValue().getId() != null ? event.getValue().getId() : null;
                clearOldSaveData(false);
                setDefaultValueDate();
                if (savedList.getSelectedId() != null) {
                    facetFilterRpc.setFilterChanges(false);
                    facetFilterRpc.setObjectID(savedList.getSelectedId());
                    facetFilterRpc.setPublicFilter(publicFields.get(savedList.getSelectedId()));
                    delete.setVisible(true);
                } else {
                    setDefaultValueDate();
                    facetFilterRpc.setName(null);
                    facetFilterRpc.setObjectID(null);
                    facetFilterRpc.setFilterChanges(true);
                    delete.setVisible(false);
                    changeFacetFilterStructure(facetFilterRpc);
                    initFacetContentWidgets(false);
                }
                setAllButtonsEanbled(savedList.getSelectedItem() == null || !savedList.getSelectedItem().isNewItem() || Utils.hasPermission(PermissionConstants.ADD_SYSTEM_FILTER));
                getRequestData(facetFilterRpc);
            }
        });
    }

    public void setAllButtonsEanbled(boolean b) {
        delete.setEnabled(b);
        saveFilter.setEnabled(true);
    }

    /**
     * Initialization Callback
     */
    private HashMap<Integer, Boolean> publicFields;

    private void initCallback() {
        saveListCallback = new FacetSaveListCallback() {
            public void onSuccess(SaveFilterSelectItems saveItems) {
                LoadingPanel.loading(false, FacetFilterPopup.this);

                if (saveItems != null) {
                    savedList.setItems(saveItems.getItems());
                    publicFields = saveItems.getPublicFilds();
                    Integer selectedId = listingPanel.getFacetFilterSelectedId();
                    listingPanel.setFacetFilterListItems(saveItems.getItems());
                    listingPanel.setFacetFilterSelectedId(selectedId);
                    if (saveFilterId != null) {
                        if (!filterDataNotSaved) {
                            savedList.setSelected(saveFilterId);
                            delete.setVisible(true);
                        } else {
                            filterDataNotSaved = false;
                        }
                        setSaveFilterId(null);
                    } else if (saveItems.getDefaultFilterID() != null) {
                        if (!filterDataNotSaved && !facetFilterRpc.isFilterChanges()) {
                            savedList.setSelected(saveItems.getDefaultFilterID());
                            delete.setVisible(true);
                        } else {
                            filterDataNotSaved = false;
                        }
                    }
                    defaultFilterId = saveItems.getDefaultFilterID();
                    if (savedList.getSelectedItem() != null && savedList.getSelectedItem().isNewItem() && !Utils.hasPermission(PermissionConstants.ADD_SYSTEM_FILTER)) {
                        setAllButtonsEanbled(false);
                    }
                } else {
                    listingPanel.setFacetFilterListItems(new SelectItem[0]);
                }
            }

            public void onFailure(Throwable t) {

            }
        };

        saveCallback = new FacetSaveCallback() {
            public void onSuccess(Integer saveFilterId) {
                if (facetFilterRpc.isDefaultFilter()) {
                    defaultFilterId = saveFilterId;
                }
                saveFilterPopup.close();
                facetFilterRpc.setFilterChanges(false);
                setSaveFilterId(saveFilterId);
                facetApplySave.getSaveFilterListItem(saveListCallback);
            }

            public void onFailure(Throwable t) {

            }
        };


        callback = new FacetDataCallback() {
            public void onSuccess(FacetFilterRpc data) {
                fillData(data);
                datePeriodFacetContent.refreshFacetFilter(data);
                LoadingPanel.loading(false, FacetFilterPopup.this);
            }

            public void onFailure(Throwable t) {
                LoadingPanel.loading(false, FacetFilterPopup.this);
            }
        };

        getRequestData(facetFilterRpc);
        getSaveFilterList();
    }

    /**
     * Dates Event
     */
    private void datesEvents() {

        filterByDate.addValueChangeHandler(changeEvent -> {
            if (filterByDate.getSelectedId() != null && endDate.getDate() != null && startDate.getDate() != null
                    && !wfmStrings.pleaseSelect().equals(endDate.getText())
                    && !wfmStrings.pleaseSelect().equals(startDate.getText())) {

                facetFilterRpc.setFilterChanges(true);
                setDates();
                getRequestData(facetFilterRpc);
            } else {
                setDates();
            }
        });
        startDate.addChangeHandler(changeEvent -> {
            facetFilterRpc.setFilterChanges(true);
            if (endDate.getDate() != null && startDate.getDate() != null && endDate.getDate().before(startDate.getDate())) {
                startDate.clearSelected();
            } else if (endDate.getDate() != null && !wfmStrings.pleaseSelect().equals(endDate.getText())) {
                setDates();
                getRequestData(facetFilterRpc);
            } else {
                setDates();
            }
        });
        endDate.addChangeHandler(changeEvent -> {
            facetFilterRpc.setFilterChanges(true);
            if (startDate.getDate() != null && endDate.getDate() != null && endDate.getDate().before(startDate.getDate())) {
                endDate.clearSelected();
            } else if (startDate.getDate() != null && !wfmStrings.pleaseSelect().equals(startDate.getText())) {
                setDates();
                getRequestData(facetFilterRpc);
            } else {
                setDates();
            }
        });

        more.addChangeHandler(changeEvent -> {
            facetFilterRpc.setFilterChanges(true);
            if (more.getValue() != null && !"".equals(more.getValue())) {
                setDates();
//                    getRequestData(facetFilterRpc);
            } else {
                setDates();
            }
        });

        less.addChangeHandler(changeEvent -> {
            facetFilterRpc.setFilterChanges(true);
            if (less.getValue() != null && !"".equals(less.getValue())) {
                setDates();
//                    getRequestData(facetFilterRpc);
            } else {
                setDates();
            }
        });
        terms.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null && valueChangeEvent.getValue().getId() > 0) {
                startDate.setEnabled(false);
                endDate.setEnabled(false);
                if (valueChangeEvent.getValue().getId() == 1) {
                    startDate.setDate(new Date());
                    endDate.setDate(new Date());
                } else if (valueChangeEvent.getValue().getId() == 2) {
                    startDate.setDate(DateUtil.minusDays(new Date(), 1));
                    endDate.setDate(DateUtil.minusDays(new Date(), 1));
                } else if (valueChangeEvent.getValue().getId() == 3) {
                    startDate.setDate(DateUtil.getWeekFirstDay());
                    endDate.setDate(DateUtil.getWeekLastDay());
                } else if (valueChangeEvent.getValue().getId() == 4) {
                    startDate.setDate(DateUtil.getMonthFirstDay(new Date()));
                    endDate.setDate(DateUtil.getMonthLastDate(new Date()));
                } else if (valueChangeEvent.getValue().getId() == 5) {
                    startDate.setDate(new Date());
                    endDate.setText(wfmStrings.pleaseSelect());
                } else if (valueChangeEvent.getValue().getId() == 6) {
                    startDate.setDate(DateUtil.minusDays(new Date(), 1));
                    endDate.setText(wfmStrings.pleaseSelect());
                } else if (valueChangeEvent.getValue().getId() == 7) {
                    startDate.setDate(DateUtil.getWeekFirstDay());
                    endDate.setText(wfmStrings.pleaseSelect());
                } else if (valueChangeEvent.getValue().getId() == 8) {
                    startDate.setDate(DateUtil.getMonthFirstDay(new Date()));
                    endDate.setText(wfmStrings.pleaseSelect());
                }
            } else {
                startDate.setEnabled(true);
                endDate.setEnabled(true);
                startDate.setDefaultFormatText();
                endDate.setDefaultFormatText();
            }
            setDates();
            facetFilterRpc.setFilterChanges(true);
            getRequestData(facetFilterRpc);
        });

    }

    /**
     * clear old save data in facet list object
     */
    private void clearOldSaveData(boolean isReset) {
        for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
            contentsMap.get(facetCodeName).getFacetList().clearOldSelectedData();
        }
        for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
            contentsMap.get(facetCodeName).getCapCheck().setValue(false);
        }
        if (isReset) {
            Integer objectID = facetFilterRpc.getObjectID();
            facetFilterRpc = new FacetFilterRpc(facetFilterRpc.getShowFacetCodeName(),
                    facetFilterRpc.getShowSolrFieldMap(), facetFilterRpc.getHideSolrFieldMap(), facetFilterRpc.getDateFilterByList());
            facetFilterRpc.setObjectID(objectID);
        } else {
            facetFilterRpc = new FacetFilterRpc(facetContentConfigure.getShowCodeNameCloneList(),
                    facetContentConfigure.getShowSolrCloneFields(), facetContentConfigure.getHideSolrCloneFields(), facetFilterRpc.getDateFilterByList());
        }
        facetFilterRpc.setType(listPanelType);
        facetFilterRpc.setTypeId(typeParentId);
        datePeriodFacetContent.reset();
    }

    /**
     * Get Save Filtrs Inter
     */
    private void getSaveFilterList() {
        facetApplySave.getSaveFilterListItem(saveListCallback);
    }

    /**
     * Fill Facet Filter Table
     *
     * @param data
     */
    private void fillData(FacetFilterRpc data) {
        if (data.getObjectID() != null && !data.isFilterChanges()) {
            changeFacetFilterStructure(data);
            initFacetContentWidgets(true);
            for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
                if (data.getFacetContentMap().containsKey(facetCodeName)) {
                    SelectItem[] items = data.getFacetContentMap().get(facetCodeName).getFacetItems();
                    Map<Integer, Integer> map = data.getFacetContentMap().get(facetCodeName).getSavedItems();
                    FacetContentPanel contentPanel = contentsMap.get(facetCodeName);
                    contentPanel.getFacetList().setItems(items, map);
                    //facetFilterRpc.getFacetContentMap().get(facetCodeName).setFacetItems(contentPanel.getFacetList().getSelectItems());
                    if (!facetFilterRpc.getFacetContentMap().isEmpty() && facetFilterRpc.getFacetContentMap().get(facetCodeName) != null) {
                        facetFilterRpc.getFacetContentMap().get(facetCodeName).setFacetItems(contentPanel.getFacetList().getSelectItems());
                    }
                    data.getFacetContentMap().get(facetCodeName).getSavedItems().clear();

                    contentPanel.showWarningImage(items.length > 250);
                }
            }
            if (!Utils.isNullOrEmpty(data.getSelectedDateSolrCodeName())) {
                filterByDate.setSelected(data.getSelectedDateSolrCodeName().hashCode());
            }
            if (data.getStartDate() != null) {
                startDate.setDate(data.getStartDate());
            }
            if (data.getEndDate() != null) {
                endDate.setDate(data.getEndDate());
            }
            if (data.getMore() != null) {
                more.setValue(String.valueOf(data.getMore()));
            }
            if (data.getLess() != null) {
                less.setValue(String.valueOf(data.getLess()));
            }
            if (facetFilterRpc.isDefaultFilter()) {
                defaultFilterId = facetFilterRpc.getObjectID();
            }
        } else {
            for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
                FacetContentPanel contentPanel = contentsMap.get(facetCodeName);
                FacetContentRpc facetContentRpc = data.getFacetContentMap().get(facetCodeName);
                if (!contentPanel.isSelectedIndex() && facetContentRpc != null) {
                    SelectItem[] items = facetContentRpc.getFacetItems();
                    if (facetFilterRpc.isApplyFilter()) {
                        Map<Integer, Integer> map = facetFilterRpc.getFacetContentMap().get(facetCodeName).getSavedItems();
                        contentPanel.getFacetList().setItems(items, map);
                    } else {
                        contentPanel.getFacetList().setItems(items, null);
                    }

                    contentPanel.showWarningImage(items.length > 250);
                }
                contentPanel.setSelectedIndex(false);
            }
            facetFilterRpc.setApplyFilter(false);
        }
    }

    private void changeFacetFilterStructure(FacetFilterRpc data) {
        facetFilterRpc.setCustomData(data.getCustomData());
        facetFilterRpc.setObjectID(data.getObjectID());
        facetFilterRpc.setStartDate(data.getStartDate());
        facetFilterRpc.setEndDate(data.getEndDate());
        facetFilterRpc.setMore(data.getMore());
        facetFilterRpc.setLess(data.getLess());
        facetFilterRpc.setShowFacetCodeName(data.getShowFacetCodeName());
        facetFilterRpc.setShowSolrFieldMap(data.getShowSolrFieldMap());
        facetFilterRpc.setHideSolrFieldMap(data.getHideSolrFieldMap());
        facetFilterRpc.setFacetSettingMap(data.getFacetSettingMap());
        facetFilterRpc.setSelectedDateSolrCodeName(data.getSelectedDateSolrCodeName());

        for (String facetCodeName : facetFilterRpc.getShowSolrFieldMap().keySet()) {
            if (!contentsMap.containsKey(facetCodeName)) {
                contentsMap.put(facetCodeName, new FacetContentPanel(facetCodeName, facetContentConfigure.getContentTitle(facetCodeName)));
                facetFilterRpc.getFacetContentMap().put(facetCodeName, new FacetContentRpc());
            }
        }
        if (quantityRangePanel != null) {
            quantityRangePanel.removeFromParent();
        }
        datePeriodPanel.removeFromParent();
        ArrayList<String> removeFacetContentWidget = new ArrayList<>();
        for (String facetCodeName : contentsMap.keySet()) {
            if (!facetFilterRpc.getShowSolrFieldMap().containsKey(facetCodeName)) {
                removeFacetContentWidget.add(facetCodeName);
                facetFilterRpc.getFacetContentMap().remove(facetCodeName);
            }
            contentsMap.get(facetCodeName).removeAllListeners();
            contentsMap.get(facetCodeName).removeFromParent();
        }
        for (String key : removeFacetContentWidget) {
            contentsMap.remove(key);
        }
        addHideFacetContentMenu();
    }

    public void setSaveFilterId(Integer saveFilterId) {
        this.saveFilterId = saveFilterId;
    }

    public void setFacetFilterRpc(FacetFilterRpc facetFilterRpc) {
        this.facetFilterRpc = facetFilterRpc;
    }

    public void refreshFilter() {
        facetFilterRpc.setFilterChanges(true);
        getRequestData(facetFilterRpc);
    }

    public FacetFilterRpc getFacetFilterRpc() {
        return facetFilterRpc;
    }

    public void setEnableDate(Boolean enableDate) {
        this.enableDate = enableDate;
    }

    @Override
    public void onClick(ClickEvent event) {
        // hidePopup();
    }
}
