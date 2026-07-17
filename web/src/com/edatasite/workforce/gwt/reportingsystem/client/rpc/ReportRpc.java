package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractRpcMap;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 19:57:39
 */
public class ReportRpc extends AbstractRpcMap implements IsSerializable {

    private static final String IS_MODIFIED = "isModified";
    private static final String FOR_PRINT = "forPrint";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SHEET_NAME = "sheet_name";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORYID = "categoryid";
    private static final String FOLDER_ID = "folderId";
    private static final String FOLDER_TYPE = "folderType";
    private static final String IS_TRANSPOSED = "isTransposed";

    /*Save to Data Base parametrs*/
    private static final String SNT_FILTER_NAME = "sntFilterName";
    private static final String SNT_FILTER_TITLE = "sntFilterTitle";
    private static final String DURATION_TYPE = "durationType";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String ROLE_ID = "roleId";

    /*This is users paramerts for change view period*/
    private static final String SNT_FILTER_NAME_CHANGE = "sntFilterNameChange";
    private static final String DURATION_TYPE_CHANGE = "durationTypeChange";
    private static final String START_DATE_CHANGE = "startDateChange";
    private static final String END_DATE_CHANGE = "endDateChange";
    private static final String ROLE_CHANGE = "roleChange";
    private static final String DASHLET = "dashlet";


    private static final String VIEW_NAME = "viewName";
    private static final String SHOW_ROW_COUNT = "showRowCount";
    private static final String FOLDER_NAME = "folderName";
    private static final String IS_LIBRARY = "isLibrary";
    private static final String VIEW_CODE = "viewCode";
    private static final String CODE = "code";
    private static final String OWNER = "owner";
    private static final String ALL_COUNT = "allCount";
    private static final String NOW_LAST_POSITION = "nowLastPosition";
    private static final String MAX_EXCEL_ROW_COUNT = "maxExcelRowCount";
    private static final String FILTER_OPTION_CHANGED = "filterOptionChanged";
    private static final String XML_TEMPLATE_ID = "xmlTemplateId";
    private static final String EXCEL_TEMPLATE_ID = "excelTemplateId";
    private static final String PDF_TEMPLATE_ID = "pdfTemplateId";
    private static final String SORT_TABLE_BY_COLUMN_TYPE = "sortTableByColumnType";
    private static final String SORT_TABLE_BY_COLUMN = "sortTableByColumn";
    private static final String ENABLE_ADD_NEW_ACTION = "enableAddNewAction";
    private static final String ENABLE_VIEW_ACTION = "enableViewAction";
    private static final String ENABLE_EDIT_ACTION = "enableEditAction";
    private static final String ENABLE_DELETE_ACTION = "enableDeleteAction";
    private static final String SHOW_ACTIONS = "showActions";
    private static final String SHOW_DRILL_REPORTS = "showDrillReports";
    private static final String SHOW_ACTIONS_ICON = "showActionsIcon";
    private static final String NO_TIME_ZONE = "noTimeZone";
    private static final String BROWSER_TIME_ZONE = "browserTimeZone";
    private static final String RUN_FROM_FIRST_STEP = "runFromFirstStep";
    private static final String IS_ENABLED_FILTER_WIDGET = "isEnabledFilterWidget";
    private static final String RECURRENCE_ID = "recurrenceId";
    private static final String USER_ID = "userID";
    private static final String isLANDSCAPE = "islandscape";

    private static final String COMPANY_ID = "companyId";
    private static final String CLONABLE = "clonable";
    private static final String PERMISSION_CODE = "permissionCode";
    private static final String FILTER_PATTERN = "filterPattern";
    private static final String FILTER_COLUMN = "filterColumn";
    private static final String FILTER_OPERATION = "filterOperation";
    private static final String FILTER_VALUE = "filterValue";
    private static final String ADD_PROJECT = "addproject";
    private static final String RELETED_PROJECT_ID = "reletedprojectid";
    private HashMap<String, ColumnRpc> columnMap = new LinkedHashMap<>();
    private HashMap<String, String> valueMap = null;
    private boolean showMailingList;
    private String tableType = ReportType.TABULAR.name();

    private ArrayList<String> sortTypes;
    private ArrayList<String> rangeType;
    private ArrayList<Integer> sett;
    private LinkedList<ColumnRpc> fieldd;
    private ArrayList<String> operators;
    private ArrayList<String> values;
    private ArrayList<String> boolType;
    private ArrayList<Integer> promtList;
    private LinkedList<ColumnRpc> groupColumns;
    private LinkedHashMap<String, String> columnsByGroupMap;
    private LinkedList<ColumnRpc> sumaries;
    private LinkedList<ColumnRpc> selectedColumns;
    private ArrayList<ColumnRpc> filterColumns;
    private ArrayList<String> viewTypes;
    private ArrayList<String> ruleNames = new ArrayList<>();

    private HashMap<Integer, RelatedFilterOptions> relatedFilters;

    private ReportingCustomizeFilter customizeFilter;

    private ChartConfItem chartConf;

    private KpiWidgetItem kpiWidgetItem;


    private boolean isDetailed = true;
    private boolean isFromKpi;
    private boolean sqlServer;
    private int limit = 20;
    private int position = 1;

    private int nowPosition = 1;

    private RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();

    //chosen Email Template in Reporting System Recurrence
    private SelectItem emailTemplateItem;
    //chosen target users
    private ArrayList<Integer> targetUsers;

    //For custom filters
    private HashMap<String, String> customFilter;           //HashMap<Name, Value> name=code name used in xml, value=resulting value that has to be replaced
    private Integer filterType;
    private boolean fromRunButton;
    private boolean isSaveAs = false;

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public Boolean isModified() {
        return getBoolean(IS_MODIFIED);
    }

    public void setModified(Boolean modified) {
        addBoolean(IS_MODIFIED, modified);
    }

    public Boolean getForPrint() {
        return getBool(FOR_PRINT);
    }

    public void setForPrint(Boolean print) {
        addBoolean(FOR_PRINT, print);
    }

    public Integer getId() {
        return getInteger(ID);
    }

    public void setId(Integer id) {
        addInteger(ID, id);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        addString(NAME, name);
    }

    public String getSheetName() {
        return getString(SHEET_NAME);
    }

    public void setSheetName(String name) {
        addString(SHEET_NAME, name);
    }


    public String getDiscreption() {
        return getString(DESCRIPTION);
    }

    public void setDiscreption(String discreption) {
        addString(DESCRIPTION, discreption);
    }

    public Integer getCategoryId() {
        return getInteger(CATEGORYID);
    }

    public void setCategoryId(Integer categoryid) {
        addInteger(CATEGORYID, categoryid);
    }

    public Integer getFolderId() {
        return getInteger(FOLDER_ID);
    }

    public void setFolderId(Integer folderId) {
        addInteger(FOLDER_ID, folderId);
    }

    public Integer getRoleId() {
        return getInteger(ROLE_ID);
    }

    public void setRoleId(Integer roleId) {
        addInteger(ROLE_ID, roleId);
    }

    public Boolean getIsDetailed() {
        return isDetailed;
    }

    public void setIsDetailed(Boolean isDetailed) {
        this.isDetailed = isDetailed;
    }

    public boolean isFromKpi() {
        return isFromKpi;
    }

    public void setFromKpi(boolean fromKpi) {
        isFromKpi = fromKpi;
    }

    public Boolean isShowRowCount() {
        return getBool(SHOW_ROW_COUNT);
    }

    public void setShowRowCount(boolean showRowCount) {
        addBool(SHOW_ROW_COUNT, showRowCount);
    }

    public String getViewName() {
        return getString(VIEW_NAME);
    }

    public void setViewName(String viewName) {
        addString(VIEW_NAME, viewName);
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getSntFilterName() {
        return getString(SNT_FILTER_NAME);
    }

    public void setSntFilterName(String sntFilterName) {
        addString(SNT_FILTER_NAME, sntFilterName);
    }

    public String getSntFilterTitle() {
        return getString(SNT_FILTER_TITLE);
    }

    public void setSntFilterTitle(String sntFilterTitle) {
        addString(SNT_FILTER_TITLE, sntFilterTitle);
    }

    public String getDurationType() {
        return getString(DURATION_TYPE);
    }

    public void setDurationType(String durationType) {
        addString(DURATION_TYPE, durationType);
    }

    public String getStartDate() {
        return getString(START_DATE);
    }

    public void setStartDate(String startDate) {
        addString(START_DATE, startDate);
    }

    public String getEndDate() {
        return getString(END_DATE);
    }

    public void setEndDate(String endDate) {
        addString(END_DATE, endDate);
    }

    public String getSntFilterNameChange() {
        return getString(SNT_FILTER_NAME_CHANGE);
    }

    public void setSntFilterNameChange(String sntFilterNameChange) {
        addString(SNT_FILTER_NAME_CHANGE, sntFilterNameChange);
    }

    public String getDurationTypeChange() {
        return getString(DURATION_TYPE_CHANGE);
    }

    public void setDurationTypeChange(String durationTypeChange) {
        addString(DURATION_TYPE_CHANGE, durationTypeChange);
    }

    public String getStartDateChange() {
        return getString(START_DATE_CHANGE);
    }

    public void setStartDateChange(String startDateChange) {
        addString(START_DATE_CHANGE, startDateChange);
    }

    public String getEndDateChange() {
        return getString(END_DATE_CHANGE);
    }

    public void setEndDateChange(String endDateChange) {
        addString(END_DATE_CHANGE, endDateChange);
    }

    public String getRoleChange() {
        return getString(ROLE_CHANGE);
    }

    public void setRoleChange(String roleChange) {
        addString(ROLE_CHANGE, roleChange);
    }

    public ArrayList<String> getSortTypes() {
        if (sortTypes == null) {
            sortTypes = new ArrayList<>();
        }
        return sortTypes;
    }

    public void setSortTypes(ArrayList<String> sortTypes) {
        this.sortTypes = sortTypes;
    }

    public ArrayList<String> getRangeType() {
        if (rangeType == null) {
            rangeType = new ArrayList<>();
        }
        return rangeType;
    }

    public void setRangeType(ArrayList<String> rangeType) {
        this.rangeType = rangeType;
    }

    public ArrayList<Integer> getSett() {
        if (sett == null) {
            sett = new ArrayList<>();
        }
        return sett;
    }

    public void setSett(ArrayList<Integer> sett) {
        this.sett = sett;
    }

    public LinkedList<ColumnRpc> getFieldd() {
        if (fieldd == null) {
            fieldd = new LinkedList<>();
        }
        return fieldd;
    }

    public void setFieldd(LinkedList<ColumnRpc> fieldd) {
        this.fieldd = fieldd;
    }

    public ArrayList<String> getOperators() {
        if (operators == null) {
            operators = new ArrayList<>();
        }
        return operators;
    }

    public void setOperators(ArrayList<String> operators) {
        this.operators = operators;
    }

    public void setOperatorAtIndex(int index, String operator) {
        getOperators().set(index, operator);
    }

    public void addOperator(String operator) {
        getOperators().add(operator);
    }

    public void removeOperator(int index) {
        getOperators().remove(index);
    }

    public ArrayList<String> getValues() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<Integer> getPromtList() {
        return promtList;
    }

    public void setPromtList(ArrayList<Integer> promtList) {
        this.promtList = promtList;
    }

    public ArrayList<String> getBoolType() {
        if (boolType == null) {
            boolType = new ArrayList<>();
        }
        return boolType;
    }

    public void setBoolType(ArrayList<String> boolType) {
        this.boolType = boolType;
    }

    public String getBoolTypeAt(int index) {
        return getBoolType().get(index);
    }

    public void setBoolTypeAt(int index, String value) {
        getBoolType().set(index, value);
    }

    public void clearBoolType() {
        getBoolType().clear();
    }

    public void removeBoolAt(int index) {
        getBoolType().remove(index);
    }

    public void addToBoolType(String bool) {
        if (boolType == null) {
            boolType = new ArrayList<>();
        }
        boolType.add(bool);
    }

    public LinkedList<ColumnRpc> getGroupColumns() {
        if (groupColumns == null) {
            groupColumns = new LinkedList<>();
        }
        return groupColumns;
    }

    public void setGroupColumns(LinkedList<ColumnRpc> groupColumns) {
        this.groupColumns = groupColumns;
    }

    public LinkedList<ColumnRpc> getSumaries() {
        if (sumaries == null) {
            sumaries = new LinkedList<>();
        }
        return sumaries;
    }

    public void setSumaries(LinkedList<ColumnRpc> sumaries) {
        this.sumaries = sumaries;
    }

    public LinkedList<ColumnRpc> getSelectedColumns() {
        if (selectedColumns == null) {
            selectedColumns = new LinkedList<>();
        }
        return selectedColumns;
    }

    public void setSelectedColumns(LinkedList<ColumnRpc> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    /*public LinkedList<ColumnRpc> getAllColumn() {
        if (allColumn == null) {
            allColumn = new LinkedList<ColumnRpc>();
        }
        return allColumn;
    }

    public void setAllColumn(LinkedList<ColumnRpc> allColumn) {
        this.allColumn = allColumn;
    }*/

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFolderType() {
        return getString(FOLDER_TYPE);
    }

    public void setFolderType(String folderType) {
        addString(FOLDER_TYPE, folderType);
    }

    public ChartConfItem getChartConf() {
        return chartConf;
    }

    public void setChartConf(ChartConfItem chartConf) {
        this.chartConf = chartConf;
    }

    public KpiWidgetItem getKpiWidgetItem() {
        if (kpiWidgetItem == null) {
            kpiWidgetItem = new KpiWidgetItem();
        }
        return kpiWidgetItem;
    }

    public void setKpiWidgetItem(KpiWidgetItem kpiWidgetItem) {
        this.kpiWidgetItem = kpiWidgetItem;
    }

    public String getSortTableByColumn() {
        if (!(customizeFilter == null || customizeFilter.getSortType() == null || "".equals(customizeFilter.getSortType())
                || null == customizeFilter.getSortColumnName() || "".equals(customizeFilter.getSortColumnName()))) {
            for (ColumnRpc rpc : getColumnMap().values()) {
                if (customizeFilter.getSortColumnName().equals(rpc.getName())) {
                    setSortTableByColumn(customizeFilter.getSortColumnName());
                    setSortTableByColumnType(customizeFilter.getSortType());
                    break;
                }
            }
        }
        return getString(SORT_TABLE_BY_COLUMN);
    }

    public void setSortTableByColumn(String sortTableByColumn) {
        addString(SORT_TABLE_BY_COLUMN, sortTableByColumn);
    }

    public String getSortTableByColumnType() {
        getSortTableByColumn();
        return getString(SORT_TABLE_BY_COLUMN_TYPE);
    }

    public void setSortTableByColumnType(String sortTableByColumnType) {
        addString(SORT_TABLE_BY_COLUMN_TYPE, sortTableByColumnType);
    }

    public Integer getPdfTemplateId() {
        return getInteger(PDF_TEMPLATE_ID);
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        addInteger(PDF_TEMPLATE_ID, pdfTemplateId);
    }

    public Integer getExcelTemplateId() {
        return getInteger(EXCEL_TEMPLATE_ID);
    }

    public void setExcelTemplateId(Integer excelTemplateId) {
        addInteger(EXCEL_TEMPLATE_ID, excelTemplateId);
    }

    public Integer getXmlTemplateId() {
        return getInteger(XML_TEMPLATE_ID);
    }

    public void setXmlTemplateId(Integer xmlTemplateId) {
        addInteger(XML_TEMPLATE_ID, xmlTemplateId);
    }

    public boolean isFilterOptionChanged() {
        return getBool(FILTER_OPTION_CHANGED);
    }

    public void setFilterOptionChanged(boolean filterOptionChanged) {
        addBool(FILTER_OPTION_CHANGED, filterOptionChanged);
    }

    public Boolean enableAddNewAction() {
        return getBool(ENABLE_ADD_NEW_ACTION);
    }

    public void setEnableAddNewAction(Boolean enableAddNewAction) {
        addBoolean(ENABLE_ADD_NEW_ACTION, enableAddNewAction);
    }

    public Boolean enableViewAction() {
        return getBool(ENABLE_VIEW_ACTION);
    }

    public void setEnableViewAction(Boolean enableViewAction) {
        addBoolean(ENABLE_VIEW_ACTION, enableViewAction);
    }

    public Boolean enableEditAction() {
        return getBool(ENABLE_EDIT_ACTION);
    }

    public void setEnableEditAction(Boolean enableEditAction) {
        addBoolean(ENABLE_EDIT_ACTION, enableEditAction);
    }

    public Boolean enableDeleteAction() {
        return getBool(ENABLE_DELETE_ACTION);
    }

    public void setEnableDeleteAction(Boolean enableDeleteAction) {
        addBoolean(ENABLE_DELETE_ACTION, enableDeleteAction);
    }

    public Boolean getShowActions() {
        return getBool(SHOW_ACTIONS);
    }

    public void setShowActions(Boolean showActions) {
        addBoolean(SHOW_ACTIONS, showActions);
    }

    public Boolean getShowDrillReports() {
        return getBool(SHOW_DRILL_REPORTS);
    }

    public void setShowDrillReports(Boolean showDrillReports) {
        addBoolean(SHOW_DRILL_REPORTS, showDrillReports);
    }

    public Boolean getShowActionsIcon() {
        return getBool(SHOW_ACTIONS_ICON);
    }

    public void setShowActionsIcon(Boolean showActionsIcon) {
        addBoolean(SHOW_ACTIONS_ICON, showActionsIcon);
    }

    public Boolean getRunFromFirstStep() {
        return getBool(RUN_FROM_FIRST_STEP);
    }

    public void setRunFromFirstStep(Boolean runFromFirstStep) {
        addBoolean(RUN_FROM_FIRST_STEP, runFromFirstStep);
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Integer getRecurrenceId() {
        return getInteger(RECURRENCE_ID);
    }

    public void setRecurrenceId(Integer recurrenceId) {
        addInteger(RECURRENCE_ID, recurrenceId);
    }

    public Integer getUserID() {
        return getInteger(USER_ID);
    }

    public void setUserID(Integer userID) {
        addInteger(USER_ID, userID);
    }

    public Boolean getEnabledFilterWidget() {
        return getBoolean(IS_ENABLED_FILTER_WIDGET);
    }

    public void setEnabledFilterWidget(Boolean enabledFilterWidget) {
        addBoolean(IS_ENABLED_FILTER_WIDGET, enabledFilterWidget);
    }

    public boolean isTransposed() {
        return getBool(IS_TRANSPOSED);
    }

    public void setTransposed(boolean transposed) {
        addBool(IS_TRANSPOSED, transposed);
    }

    public String getBrowserTimeZone() {
        return isNoTimeZone() ? "" : getString(BROWSER_TIME_ZONE);
    }

    public void setBrowserTimeZone(String browserTimeZone) {
        addString(BROWSER_TIME_ZONE, browserTimeZone);
    }

    public SelectItem getEmailTemplateItem() {
        return emailTemplateItem;
    }

    public void setEmailTemplateItem(SelectItem emailTemplateItem) {
        this.emailTemplateItem = emailTemplateItem;
    }

    public ArrayList<Integer> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(ArrayList<Integer> targetUsers) {
        this.targetUsers = targetUsers;
    }

    public String getFolderName() {
        return getString(FOLDER_NAME);
    }

    public void setFolderName(String folderName) {
        addString(FOLDER_NAME, folderName);
    }

    public boolean isLibrary() {
        return getBool(IS_LIBRARY);
    }

    public void setLibrary(boolean library) {
        addBool(IS_LIBRARY, library);
    }

    public String getViewCode() {
        return getString(VIEW_CODE);
    }

    public void setViewCode(String viewCode) {
        addString(VIEW_CODE, viewCode);
    }

    public HashMap<Integer, RelatedFilterOptions> getRelatedFilters() {
        if (relatedFilters == null) {
            relatedFilters = new HashMap<>();
        }
        return this.relatedFilters;
    }

    public void setRelatedFilters(HashMap<Integer, RelatedFilterOptions> relatedFilters) {
        this.relatedFilters = relatedFilters;
    }

    public HashMap<String, String> getCustomFilter() {
        return customFilter;
    }

    public void setCustomFilter(HashMap<String, String> customFilter) {
        this.customFilter = customFilter;
    }

    public String getCode() {
        return getString(CODE);
    }

    public void setCode(String code) {
        addString(CODE, code);
    }

    public boolean isDashlet() {
        return getBool(DASHLET);
    }

    public void setDashlet(boolean dashlet) {
        addBool(DASHLET, dashlet);
    }

    public boolean isOwner() {
        return getBool(OWNER);
    }

    public void setOwner(boolean owner) {
        addBool(OWNER, owner);
    }

    public ArrayList<ColumnRpc> getFilterColumns() {
        return filterColumns;
    }

    public void setFilterColumns(ArrayList<ColumnRpc> filterColumns) {
        this.filterColumns = filterColumns;
    }

    public int getNowPosition() {
        return nowPosition;
    }

    public void setNowPosition(int nowPosition) {
        this.nowPosition = nowPosition;
    }

    public int getNowLastPosition() {
        return getInt(NOW_LAST_POSITION);
    }

    public void setNowLastPosition(int nowLastPosition) {
        addInt(NOW_LAST_POSITION, nowLastPosition);
    }

    public int getAllCount() {
        return getInt(ALL_COUNT);
    }

    public void setAllCount(int allCount) {
        addInt(ALL_COUNT, allCount);
    }

    public Integer getCompanyId() {
        return getInteger(COMPANY_ID);
    }

    public void setCompanyId(Integer companyId) {
        addInteger(COMPANY_ID, companyId);
    }

    public Integer getMaxExcelRowCount() {
        return getInteger(MAX_EXCEL_ROW_COUNT);
    }

    public void setMaxExcelRowCount(Integer maxrowcount) {
        addInteger(MAX_EXCEL_ROW_COUNT, maxrowcount);
    }

    public ReportingCustomizeFilter getCustomizeFilter() {
        return customizeFilter;
    }

    public void setCustomizeFilter(ReportingCustomizeFilter customizeFilter) {
        this.customizeFilter = customizeFilter;
    }

    public boolean isNoTimeZone() {
        return getBool(NO_TIME_ZONE);
    }

    public void setNoTimeZone(boolean noTimeZone) {
        addBool(NO_TIME_ZONE, noTimeZone);
    }

    public boolean getClonable() {
        return getBool(CLONABLE);
    }

    public void setClonable(Boolean clonable) {
        addBoolean(CLONABLE, clonable);
    }

    public HashMap<String, ColumnRpc> getColumnMap(int... tmp) {
        return columnMap;
    }

    public void setColumnMap(HashMap<String, ColumnRpc> columnMap) {
        this.columnMap = columnMap;
    }

    public String getPermissionCode() {
        return getString(PERMISSION_CODE);
    }

    public void setPermissionCode(String permissionCode) {
        addString(PERMISSION_CODE, permissionCode);
    }

    public String getFilterPattern() {
        return getString(FILTER_PATTERN);
    }

    public void setFilterPattern(String filterPattern) {
        addString(FILTER_PATTERN, filterPattern);
    }

    public String getFilterColumn() {
        return getString(FILTER_COLUMN);
    }

    public void setFilterColumn(String filterColumn) {
        addString(FILTER_COLUMN, filterColumn);
    }

    public String getFilterOperation() {
        return getString(FILTER_OPERATION);
    }

    public void setFilterOperation(String filterOperation) {
        addString(FILTER_OPERATION, filterOperation);
    }

    public String getFilterValue() {
        return getString(FILTER_VALUE);
    }

    public void setFilterValue(String filterValue) {
        addString(FILTER_VALUE, filterValue);
    }

    public Boolean getAddProject() {
        return getBoolean(ADD_PROJECT);
    }

    public void setAddProject(Boolean addProject) {
        addBoolean(ADD_PROJECT, addProject);
    }

    public Integer getReletedProjectID() {
        return getInteger(RELETED_PROJECT_ID);
    }

    public void setReletedProjectID(Integer reletedProjectID) {
        addInteger(RELETED_PROJECT_ID, reletedProjectID);
    }

    public boolean isSqlServer() {
        return sqlServer;
    }

    public void setSqlServer(boolean sqlServer) {
        this.sqlServer = sqlServer;
    }

    public boolean isShowMailingList() {
        return showMailingList;
    }

    public void setShowMailingList(boolean showMailingList) {
        this.showMailingList = showMailingList;
    }

    public Boolean isLandscape() {
        return getBoolean(isLANDSCAPE);
    }

    public void setLandscape(Boolean landscape) {
        addBoolean(isLANDSCAPE, landscape);
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    public ArrayList<String> getRuleNames() {
        return ruleNames;
    }

    public void setRuleNames(ArrayList<String> ruleNames) {
        this.ruleNames = ruleNames;
    }

    public ArrayList<String> getViewTypes() {
        if (viewTypes == null) {
            viewTypes = new ArrayList<>();
        }
        return viewTypes;
    }

    public void setViewTypes(ArrayList<String> viewTypes) {
        this.viewTypes = viewTypes;
    }

    public boolean isFromRunButton() {
        return fromRunButton;
    }

    public void setFromRunButton(boolean fromRunButton) {
        this.fromRunButton = fromRunButton;
    }

    public boolean isSaveAs() {
        return isSaveAs;
    }

    public void setSaveAs(boolean saveAs) {
        isSaveAs = saveAs;
    }

    public LinkedHashMap<String, String> getColumnsByGroupMap() {
        return columnsByGroupMap;
    }

    public void setColumnsByGroupMap(LinkedHashMap<String, String> columnsByGroupMap) {
        this.columnsByGroupMap = columnsByGroupMap;
    }
}