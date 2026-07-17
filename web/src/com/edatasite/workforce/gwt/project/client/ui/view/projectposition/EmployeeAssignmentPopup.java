package com.edatasite.workforce.gwt.project.client.ui.view.projectposition;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.DatePickerCellCustom;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Normurod on 8/14/15.
 */
public class EmployeeAssignmentPopup extends KpiModal {

    protected final ProjectStrings projectStrings = ProjectStrings.App.get();

    private Integer projectID;
    private Integer positionID;
    private Date contractStartDate;
    private Date contractEndDate;
    private Integer numberOfWorker;
    private Map<Integer, HashMap<String, KpiTreeInfo>> map;

    private KpiDataGrid dataGrid;
    private ListDataProvider<KpiTreeInfo> listDataProvider;
    private SimplePager pager;
    private ListLoadConfig listLoadConfig;

    private ScrollPanel pnlContainer;
    private GBox headerContainer;
    private VerticalPanelDiv container;

    private TextBox txtSearch;
    private WfmButton2 btnSearch;
    private WfmButton2 btnFilter;
    private WfmButton2 btnSave;

    private KpiModal pnlFilterPopup;
    private FlexTable filterTable;

    private DatePicker availableFrom;
    private TextBox txtAge;
    private CustomList languageList;
    private CustomList positionList;

    private WfmButton2 btnApplyFilter;
    private WfmButton2 btnResetFilter;

    private SelectItem[] languages;
    private SelectItem[] skills;
    private SelectItem[] positions;

    private boolean isIDincluded = false;

    private final String employeeAssignmentPopup = "employeeAssignmentPopup";

    public static final ProvidesKey<KpiTreeInfo> KEY_PROVIDER = item -> item == null ? null : item.getKey();

    public EmployeeAssignmentPopup() {
        this(null);
    }

    public EmployeeAssignmentPopup(Integer projectID) {
        super();
        setCloseButton(true);
        this.projectID = projectID;
        init();
    }

    private void init() {
//        setWidth(1240);
        addStyleName("add-employee-modal");
        map = new HashMap<>();

        txtSearch = new TextBox();
        txtSearch.ensureDebugId("search_id");
        txtSearch.addKeyPressHandler(event_ -> {
            boolean enterPressed = KeyCodes.KEY_ENTER == event_.getNativeEvent().getKeyCode();
            if (enterPressed) {
                loadData(getFilterParams(true));
            }
        });

        btnSearch = new WfmButton2(wfmStrings.search(), WfmButton2.BTN_WHITE);
        btnSearch.addClickHandler(clickEvent -> loadData(getFilterParams(true)));

        btnFilter = new WfmButton2("", WfmButton2.BTN_WHITE, "ficon--filter");
        btnFilter.removeHasiconLeftStyle();
        btnFilter.addStyleName("btn--icon");
        btnFilter.addClickHandler(clickEvent -> {
            pnlFilterPopup.open();
        });

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(clickEvent -> {

        });

        headerContainer = new GBox();
        headerContainer.setStyleUnited(true);
        headerContainer.setStyleWidthFree(true);
        headerContainer.setStyleNoPadding(true);

        GBoxRow headerRow = new GBoxRow();

        GBoxItem employeeTitle = new GBoxItem(new Span(wfmStrings.searchEmployee() + ":"));
        employeeTitle.setStyleSplitRight(true);
        employeeTitle.setStyleNoBorder(true);
        employeeTitle.removeBoxItemLabel();
        headerRow.add(employeeTitle);

        GBoxItem searchItem = new GBoxItem(txtSearch);
        searchItem.removeBoxItemLabel();
        headerRow.add(searchItem);

        GBoxItem searchBtnItem = new GBoxItem(btnSearch);
        searchBtnItem.removeBoxItemLabel();
        searchBtnItem.setStyleSplitRight(true);
        headerRow.add(searchBtnItem);

        GBoxItem filterBtnItem = new GBoxItem(btnFilter);
        filterBtnItem.removeBoxItemLabel();
        headerRow.add(filterBtnItem);
        headerContainer.add(headerRow);

        container = new VerticalPanelDiv();

        //container of the table
        pnlContainer = new ScrollPanel();
        pnlContainer.addStyleName("add-employee-modal__pnlContainer file--EmployeeAssignmentPopup");

        initFilter();

        add(headerContainer);
        add(container);
        addButton(btnSave);
    }

    public void loadData(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
            fp.setPositionID(positionID);
            isIDincluded = false;
        }
        fp.setProjectId(projectID);
        fp.setSearchKey(txtSearch.getText());
        fp.setSearchButton(!"".equals(txtSearch.getText()) && txtSearch.getText() != null);
        fp.setLimit(4000);
        fp.setModule(LayoutRPC.PM_SECTION);

        if (isIDincluded) {
            loadFromCash(fp);
        } else {
            LoadingPanel.loading(true);
            EmployeeService.App.get().getPositionEmployees(fp, map, new AsyncCallback<ArrayList<KpiTreeInfo>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ArrayList<KpiTreeInfo> kpiTreeInfos) {
                    LoadingPanel.loading(false);
                    initTable(kpiTreeInfos);
                }
            });
        }
    }

    private void loadFromCash(ListingFilterParameter fp) {
        HashMap<String, KpiTreeInfo> hashMap = map.get(positionID);
        ArrayList<KpiTreeInfo> list = new ArrayList<>();

        if (hashMap != null && hashMap.size() > 0) {
            for (KpiTreeInfo info : hashMap.values()) {
                if (info.getName().toLowerCase().contains(fp.getSearchKey().toLowerCase()) || info.getEmployeeNumber().toLowerCase().contains(fp.getSearchKey().toLowerCase())) {
                    list.add(info);
                }
            }
        }

        initTable(list);
    }

    public void loadPositionAssignedEmployee(Integer positionID) {
        ArrayList<KpiTreeInfo> items = new ArrayList<>();
        if (map.get(positionID) != null) {
            items.addAll(map.get(positionID).values());
        }

        isIDincluded = true;
        initTable(items);
    }

    private void initTable(ArrayList<KpiTreeInfo> kpiTreeInfos) {

        dataGrid = new KpiDataGrid(KEY_PROVIDER);
        dataGrid.setStyleName("cellBasedWidget-mod");
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);
        pager.setPageSize(50);
        listLoadConfig = new ListLoadConfig();
        dataGrid.addRangeChangeHandler(event -> {
            Range range = dataGrid.getVisibleRange();
            int start = range.getStart();
            int length = range.getLength();

            listLoadConfig.setStart(start);
            listLoadConfig.setLimit(length);
        });
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));

        initColumns();

        listDataProvider = new ListDataProvider<>();
        listDataProvider.addDataDisplay(dataGrid);

        pnlContainer.clear();
        container.clear();
        pnlContainer.add(dataGrid);
        container.add(pnlContainer);
        container.add(pager);

        pager.getDisplay().fireEvent(new RangeChangeEvent(new Range(listLoadConfig.getStart(), listLoadConfig.getLimit())) {
            @Override
            protected void dispatch(Handler handler) {
                super.dispatch(handler);
            }
        });

        listDataProvider.getList().removeAll(listDataProvider.getList());
        listDataProvider.getList().clear();
        listDataProvider.getList().addAll(kpiTreeInfos);
        listDataProvider.refresh();
        dataGrid.setHeight("350px");
    }

    private void initColumns() {
        int index = 0;

        dataGrid.addColumn(new Column<KpiTreeInfo, Boolean>(new CheckBoxCell()) {
            @Override
            public Boolean getValue(KpiTreeInfo item) {
                if (!isIDincluded) {
                    return false;
                }
                return item.isSelected();
            }
        }, "");
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 20, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getEmployeeNumber();
            }
        }, wfmStrings.employeeId());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 80, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getName();
            }
        }, wfmStrings.employee());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 220, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getPositionName() != null ? kpiTreeInfo.getPositionName() : "N/A";
            }
        }, wfmStrings.position());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getSkills() != null ? kpiTreeInfo.getSkills() : "N/A";
            }
        }, wfmStrings.competency());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getAvailableFrom() != null ? DateUtils.format(kpiTreeInfo.getAvailableFrom()) : "N/A";
            }
        }, projectStrings.availableFrom());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(Utils.getShortDateFormat());
        Column<KpiTreeInfo, Date> startDateColumn = new Column<KpiTreeInfo, Date>(new DatePickerCell(dateFormat)) {
            @Override
            public Date getValue(KpiTreeInfo kpiTreeInfo) {
                if (kpiTreeInfo.getContractStart() == null) {
                    kpiTreeInfo.setContractStart(new DateNonConvertable(contractStartDate));
                }
                return kpiTreeInfo.getContractStart().getNonConvertedDate();
            }
        };
        startDateColumn.setFieldUpdater((i, kpiTreeInfo, date) -> {
            kpiTreeInfo.setContractStart(new DateNonConvertable(date));

            if (map.get(positionID) != null && map.get(positionID).get(kpiTreeInfo.getKey()) != null) {
                map.get(positionID).get(kpiTreeInfo.getId()).setContractStart(new DateNonConvertable(DateUtil.resetTime(date)));
                map.get(positionID).get(kpiTreeInfo.getId()).setRejected(false);

                if (!validateContract(kpiTreeInfo)) {
                    map.get(positionID).get(kpiTreeInfo.getKey()).setRejected(true);
                }
            } else {
                if (kpiTreeInfo.isSelected() && !validateContract(kpiTreeInfo)) {
                    return;
                }
                assignmentApplyToMap(kpiTreeInfo, kpiTreeInfo.isSelected());
            }
        });
        dataGrid.addColumn(startDateColumn, wfmStrings.contractStart());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        Column<KpiTreeInfo, Date> endDateColumn = new Column<KpiTreeInfo, Date>(new DatePickerCellCustom(dateFormat)) {
            @Override
            public Date getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getContractEnd() != null ? kpiTreeInfo.getContractEnd().getNonConvertedDate() : contractEndDate;
            }
        };
        endDateColumn.setFieldUpdater((i, kpiTreeInfo, date) -> kpiTreeInfo.setContractEnd(new DateNonConvertable(date)));
        dataGrid.addColumn(endDateColumn, wfmStrings.contractEnd());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getCurrenctProjecs() != null ? kpiTreeInfo.getCurrenctProjecs() : "N/A";
            }
        }, projectStrings.currentProjects());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getLastContractDate() != null ? DateUtils.format(kpiTreeInfo.getLastContractDate()) : "N/A";
            }
        }, "Assigned Date");
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<KpiTreeInfo>() {
            @Override
            public String getValue(KpiTreeInfo kpiTreeInfo) {
                return kpiTreeInfo.getAssignedPositionName() != null ? kpiTreeInfo.getAssignedPositionName() : "N/A";
            }
        }, projectStrings.assignedPosition());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 100, Style.Unit.PX);

        Column<KpiTreeInfo, Boolean> checkbox = (Column<KpiTreeInfo, Boolean>) dataGrid.getColumn(0);

        checkbox.setFieldUpdater((index1, item, value) -> {
            item.setSelected(value);

            if (value && !validateContract(item)) {
                return;
            }
            assignmentApplyToMap(item, value);
        });
    }

    private void initFilter() {
        Label languageLabel = new Label(wfmStrings.language());
        languageLabel.setStyleName("form-group__label");
        languageLabel.setId(employeeAssignmentPopup + "languageList");

        languageList = new CustomList(Design.CHECK, true);
        languageList.ensureDebugId(employeeAssignmentPopup + "languageList");
        languageList.setSearchText(wfmStrings.search());
        languageList.setHeight(139);

        MaterialPanel languagePanel = new MaterialPanel();
        languagePanel.setStyleName("form-group");
        languagePanel.add(languageLabel);
        languagePanel.add(languageList);

        Label positionLabel = new Label(wfmStrings.positions());
        positionLabel.setStyleName("form-group__label");
        positionLabel.setId(employeeAssignmentPopup + "positionList");

        positionList = new CustomList(Design.CHECK, true);
        positionList.ensureDebugId(employeeAssignmentPopup + "positionList");
        positionList.setSearchText(wfmStrings.search());
        positionList.setHeight(115);

        MaterialPanel positionPanel = new MaterialPanel();
        positionPanel.setStyleName("form-group");
        positionPanel.add(positionLabel);
        positionPanel.add(positionList);

        Label availableLabel = new Label(projectStrings.availableFrom());
        availableLabel.setStyleName("form-group__label");
        availableLabel.setId(employeeAssignmentPopup + "availableFrom");

        availableFrom = new DatePicker();
        availableFrom.ensureDebugId(employeeAssignmentPopup + "availableFrom");

        MaterialPanel availablePanel = new MaterialPanel();
        availablePanel.setStyleName("form-group");
        availablePanel.add(availableLabel);
        availablePanel.add(availableFrom);

        Label ageLabel = new Label(wfmStrings.age());
        ageLabel.setStyleName("form-group__label");
        ageLabel.setId(employeeAssignmentPopup + "txtAge");

        txtAge = new TextBox();
        Validation.addNumericKeyboardListener(txtAge);
        txtAge.ensureDebugId(employeeAssignmentPopup + "txtAge");

        MaterialPanel agePanel = new MaterialPanel();
        agePanel.setStyleName("form-group");
        agePanel.add(ageLabel);
        agePanel.add(txtAge);

        btnApplyFilter = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        btnApplyFilter.addClickHandler(clickEvent -> {
            pnlFilterPopup.close();
            loadData(getFilterParams(false));
        });
        btnApplyFilter.getElement().getStyle().setFloat(Style.Float.RIGHT);

        btnResetFilter = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        btnResetFilter.addClickHandler(clickEvent -> resetFilter());

        int row = 0;
        filterTable = new FlexTable();
        filterTable.addStyleName("position-filter-table");

        filterTable.setWidget(row, 0, availablePanel);
        filterTable.setWidget(row, 1, positionPanel);
        filterTable.getFlexCellFormatter().setRowSpan(row, 1, 3);

        filterTable.setWidget(++row, 0, agePanel);

        filterTable.setWidget(++row, 0, languagePanel);

        pnlFilterPopup = new KpiModal();
        pnlFilterPopup.setCloseButton(true);
        pnlFilterPopup.addStyleName("position-filter-panel");
        pnlFilterPopup.setWidth(700);
        pnlFilterPopup.add(filterTable);
        pnlFilterPopup.addButton(btnResetFilter);
        pnlFilterPopup.addButton(btnApplyFilter);

        loadFilterDefaultData();
    }

    private void loadFilterDefaultData() {
        CommonService.App.get().getLanguages(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] items) {
                languages = items;
                initLanguageList();
            }
        });

        CommonService.App.get().getPositions(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] items) {
                positions = items;
                initPositionList();
            }
        });
    }

    private void initLanguageList() {
        if (languages != null && languages.length != 0) {
            if (languageList.getItems() != null) {
                languageList.removeItems();
            }

            for (SelectItem ln : languages) {
                CustomListItem item = new CustomListItem(ln);
                languageList.add(item);
            }
        }
    }

    private void initPositionList() {
        if (positions != null && positions.length != 0) {
            if (positionList.getItems() != null) {
                positionList.removeItems();
            }

            for (SelectItem ln : positions) {
                CustomListItem item = new CustomListItem(ln);
                positionList.add(item);
            }
        }
    }

    public void resetFilter() {
        initLanguageList();
        initPositionList();
        txtAge.setText(null);
        availableFrom.setDate(null);
    }

    private ListingFilterParameter getFilterParams(boolean isSearchButton) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(availableFrom.getDate());
        fp.setIDsOnly(isIDincluded);
        fp.setSearchButton(isSearchButton);
        if (languageList != null && !languageList.getSelectItems().isEmpty()) {
            StringBuilder ids = new StringBuilder();
            for (SelectItem item : languageList.getSelectItems()) {

                if (!ids.toString().isEmpty()) {
                    ids.append(",");
                }
                ids.append(item.getId());
            }
            fp.setLanguageIDs(ids.toString());
        }

        if (positionList != null && !positionList.getSelectItems().isEmpty()) {
            StringBuilder ids = new StringBuilder();
            for (SelectItem item : positionList.getSelectItems()) {

                if (!ids.toString().isEmpty()) {
                    ids.append(",");
                }
                ids.append(item.getId());
            }
            fp.setPositionIDs(ids.toString());
        }

        return fp;
    }

    private void assignmentApplyToMap(KpiTreeInfo item, boolean isSelected) {
        item.setSelected(isSelected);
        if (isSelected) {
            if (numberOfWorker != null && map.get(positionID) != null && map.get(positionID).size() == numberOfWorker) {
                item.setSelected(false);
                listDataProvider.refresh();
                Info.show("You cannot assign employees to the position more than number of workers", Info.Type.WARNING);
                return;
            }

            if (map.get(positionID) == null) {
                HashMap<String, KpiTreeInfo> ef = new HashMap<>();
                ef.put(item.getKey(), item);
                map.put(positionID, ef);
            } else {
                map.get(positionID).put(item.getKey(), item);
            }
        } else {
            if (map.get(positionID) != null) {
                map.get(positionID).remove(item.getKey());
            }
        }
    }

    public boolean validateContract(KpiTreeInfo kpiTreeInfo) {
        kpiTreeInfo.setRejected(false);
        if (kpiTreeInfo.getLastContractDate() != null) {
            if (DateUtil.resetTime(kpiTreeInfo.getLastContractDate().getNonConvertedDate()).compareTo(DateUtil.resetTime(kpiTreeInfo.getContractStart().getNonConvertedDate())) >= 0) {
                Info.show("Please select other date for the contract, because this employee has a contract on " + DateUtils.format(kpiTreeInfo.getLastContractDate().getNonConvertedDate()) + " date!", Info.Type.WARNING);
                kpiTreeInfo.setRejected(true);
                return false;
            }
        }
        for (HashMap<String, KpiTreeInfo> hashMap : map.values()) {
            for (KpiTreeInfo i : hashMap.values()) {
                if (i.getId().equals(kpiTreeInfo.getId())) {
                    if (DateUtil.resetTime(i.getContractStart().getNonConvertedDate()).compareTo(DateUtil.resetTime(kpiTreeInfo.getContractStart().getNonConvertedDate())) >= 0) {
                        Info.show("Please select other date for the contract, because this employee has a contract on " + DateUtils.format(i.getContractStart().getNonConvertedDate()) + " date!", Info.Type.WARNING);
                        kpiTreeInfo.setRejected(true);
                        return false;
                    } else if (i.getContractEnd() == null ||
                            i.getContractEnd() != null && DateUtil.resetTime(i.getContractEnd().getNonConvertedDate()).compareTo(kpiTreeInfo.getContractStart().getNonConvertedDate()) >= 0) {
                        i.setContractEnd(new DateNonConvertable(DateUtil.addDays(kpiTreeInfo.getContractStart().getNonConvertedDate(), -1)));
                    }
                }
            }
        }

        return true;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public void setPositionID(Integer positionID) {
        this.positionID = positionID;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public void setNumberOfWorker(Integer numberOfWorker) {
        this.numberOfWorker = numberOfWorker;
    }

    public Map<Integer, HashMap<String, KpiTreeInfo>> getMap() {
        return map;
    }

    public void setAvailableFrom(Date date) {
        if (availableFrom != null) {
            availableFrom.setDate(date);
        }
    }

    public WfmButton2 getBtnSaveAndClose() {
        return btnSave;
    }
}