package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportAction;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingRolePermissionItem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 18-Mar-2010
 * Time: 16:50:15
 */

public final class ViewRpc implements IsSerializable {

    private static final Integer IS_ADMIN = 1;
    private static final Integer USER_ID = 2;
    private static final Integer LEADER_ID = 3;
    private static final Integer MANAGER_ID = 4;
    private static final Integer A_COMPANY_ID = 5;
    private static final Integer U_COMPANY_ID = 6;
    private static final Integer L_COMPANY_ID = 7;
    private static final Integer M_COMPANY_ID = 8;
    private static final Integer QUERIES = 9;
    private static final Integer WHERE_BASE = 10;
    private static final Integer GROUP = 11;
    private static final Integer CLIENT_ID = 12;
    private static final Integer C_COMPANY_ID = 13;
    private static final Integer REPLACEMENT_USER_ID = 14;

    //Connection parameters
    private static final Integer CUSTOM_USER_NAME = 15;
    private static final Integer CUSTOM_PASSWORD = 16;
    private static final Integer CUSTOM_URL = 17;

    private static final Integer AGREGATE_FUNCTION = 18;
    private static final Integer ID = 19;
    private static final Integer CONDITION_VALUE = 20;
    private static final Integer CONDITION_CODE = 21;
    private static final Integer CONDITION_TYPE = 22;
    private HashMap<Integer, String> map = new HashMap<>();

    private ArrayList<TableRpc> tables;
    private HashMap<String, String> joins;

    private ReportAction addNewAction;
    private ReportAction viewAction;
    private ReportAction editAction;
    private ReportAction deleteAction;
    private ReportAction drillReportAction;
    private Boolean isEnabledFilterWidget = false;
    private Boolean showDetails = false;
    private ArrayList<ColumnRpc> hiddenColumns;

    //Custom filter properties
    private LinkedList<ColumnRpc> customFilterColumns;
    private boolean isCustomFilterEnabled;
    private boolean isFromKpi;
    private HashMap<String, String> customReplacements;                     //example <replacement id="<=" value = "#!start_date!#"/> HashMap<"<=", "#!start_date!#">

    private LinkedList<ColumnRpc> filterColumns;
    private ArrayList<ColumnRpc> defaultSelectedColumns;
    private LinkedHashMap<String, ReportingRolePermissionItem> rolePermissionFilterString = new LinkedHashMap<>();
    private boolean noTimezone = false;
    private String filterColumn;
    private String filterOperation;
    private String filterValue;
    private String entityName;

    public ArrayList<TableRpc> getTables() {
        return tables;
    }

    public void setTables(ArrayList<TableRpc> tables) {
        this.tables = tables;
    }

    public String getIsAdmin() {
        return map.get(IS_ADMIN);
    }

    public void setIsAdmin(String isAdmin) {
        map.put(IS_ADMIN, isAdmin);
    }

    public String getUserId() {
        return map.get(USER_ID);
    }

    public void setUserId(String userId) {
        map.put(USER_ID, userId);
    }

    public String getLeaderId() {
        return map.get(LEADER_ID);
    }

    public void setLeaderId(String leaderId) {
        map.put(LEADER_ID, leaderId);
    }

    public String getManagerId() {
        return map.get(MANAGER_ID);
    }

    public void setManagerId(String managerId) {
        map.put(MANAGER_ID, managerId);
    }

    public String getACompanyId() {
        return map.get(A_COMPANY_ID);
    }

    public void setACompanyId(String aCompanyId) {
        map.put(A_COMPANY_ID, aCompanyId);
    }

    public String getUCompanyId() {
        return map.get(U_COMPANY_ID);
    }

    public void setUCompanyId(String uCompanyId) {
        map.put(U_COMPANY_ID, uCompanyId);
    }

    public String getLCompanyId() {
        return map.get(L_COMPANY_ID);
    }

    public void setLCompanyId(String lCompanyId) {
        map.put(L_COMPANY_ID, lCompanyId);
    }

    public String getMCompanyId() {
        return map.get(M_COMPANY_ID);
    }

    public void setMCompanyId(String mCompanyId) {
        map.put(M_COMPANY_ID, mCompanyId);
    }

    public String getQueries() {
        return map.get(QUERIES);
    }

    public void setQueries(String queries) {
        map.put(QUERIES, queries);
    }

    public String getWhereBase() {
        return map.get(WHERE_BASE);
    }

    public void setWhereBase(String whereBase) {
        map.put(WHERE_BASE, whereBase);
    }

    public String getGroup() {
        return map.get(GROUP);
    }

    public void setGroup(String group) {
        map.put(GROUP, group);
    }

    public String getClientId() {
        return map.get(CLIENT_ID);
    }

    public void setClientId(String clientId) {
        map.put(CLIENT_ID, clientId);
    }

    public String getcCompanyId() {
        return map.get(C_COMPANY_ID);
    }

    public void setcCompanyId(String cCompanyId) {
        map.put(C_COMPANY_ID, cCompanyId);
    }

    public ReportAction getAddNewAction() {
        return addNewAction;
    }

    public void setAddNewAction(ReportAction addNewAction) {
        this.addNewAction = addNewAction;
    }

    public ReportAction getViewAction() {
        return viewAction;
    }

    public void setViewAction(ReportAction viewAction) {
        this.viewAction = viewAction;
    }

    public ReportAction getEditAction() {
        return editAction;
    }

    public void setEditAction(ReportAction editAction) {
        this.editAction = editAction;
    }

    public ReportAction getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(ReportAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    public ReportAction getDrillReportAction() {
        return drillReportAction;
    }

    public void setDrillReportAction(ReportAction drillReportAction) {
        this.drillReportAction = drillReportAction;
    }

    public Boolean getEnabledFilterWidget() {
        return isEnabledFilterWidget;
    }

    public void setEnabledFilterWidget(Boolean enabledFilterWidget) {
        this.isEnabledFilterWidget = enabledFilterWidget;
    }

    public Boolean getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(Boolean showDetails) {
        this.showDetails = showDetails;
    }

    public String getReplacementUserId() {
        return map.get(REPLACEMENT_USER_ID);
    }

    public void setReplacementUserId(String replacementUserId) {
        map.put(REPLACEMENT_USER_ID, replacementUserId);
    }

    public String getCustomUsername() {
        return map.get(CUSTOM_USER_NAME);
    }

    public void setCustomUsername(String customUsername) {
        map.put(CUSTOM_USER_NAME, customUsername);
    }

    public String getCustomPassword() {
        return map.get(CUSTOM_PASSWORD);
    }

    public void setCustomPassword(String customPassword) {
        map.put(CUSTOM_PASSWORD, customPassword);
    }

    public String getCustomUrl() {
        return map.get(CUSTOM_URL);
    }

    public void setCustomUrl(String customUrl) {
        map.put(CUSTOM_URL, customUrl);
    }

    public HashMap<String, String> getJoins() {
        return joins;
    }

    public void setJoins(HashMap<String, String> joins) {
        this.joins = joins;
    }

    public String getAgregateFunction() {
        return map.get(AGREGATE_FUNCTION);
    }

    public void setAgregateFunction(String agregateFunction) {
        map.put(AGREGATE_FUNCTION, agregateFunction);
    }

    public String getId() {
        return map.get(ID);
    }

    public void setId(String id) {
        map.put(ID, id);
    }

    public LinkedList<ColumnRpc> getCustomFilterColumns() {
        return customFilterColumns;
    }

    public void setCustomFilterColumns(LinkedList<ColumnRpc> customFilterColumns) {
        this.customFilterColumns = customFilterColumns;
    }

    public boolean isCustomFilterEnabled() {
        return isCustomFilterEnabled;
    }

    public void setCustomFilterEnabled(boolean customFilterEnabled) {
        isCustomFilterEnabled = customFilterEnabled;
    }

    public boolean isFromKpi() {
        return isFromKpi;
    }

    public void setFromKpi(boolean fromKpi) {
        isFromKpi = fromKpi;
    }

    public HashMap<String, String> getCustomReplacements() {
        return customReplacements;
    }

    public void setCustomReplacements(HashMap<String, String> customReplacements) {
        this.customReplacements = customReplacements;
    }

    public LinkedList<ColumnRpc> getFilterColumns() {
        return filterColumns;
    }

    public void setFilterColumns(LinkedList<ColumnRpc> filterColumns) {
        this.filterColumns = filterColumns;
    }

    public ArrayList<ColumnRpc> getDefaultSelectedColumns() {
        return defaultSelectedColumns;
    }

    public void setDefaultSelectedColumns(ArrayList<ColumnRpc> defaultSelectedColumns) {
        this.defaultSelectedColumns = defaultSelectedColumns;
    }

    public ArrayList<ColumnRpc> getHiddenColumns() {
        return hiddenColumns;
    }

    public void setHiddenColumns(ArrayList<ColumnRpc> hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }

    public int getHiddenColumnCount() {
        return (getId() != null ? 1 : 0) + ((hiddenColumns == null || hiddenColumns.size() < 1) ? 0 : hiddenColumns.size());
    }

    public LinkedHashMap<String, ReportingRolePermissionItem> getRolePermissionFilterString() {
        return rolePermissionFilterString;
    }

    public void setRolePermissionFilterString(LinkedHashMap<String, ReportingRolePermissionItem> rolePermissionFilterString) {
        this.rolePermissionFilterString = rolePermissionFilterString;
    }

    public void setNoTimezone(boolean noTimezone) {
        this.noTimezone = noTimezone;
    }

    public boolean isNoTimezone() {
        return noTimezone;
    }

    public String getConditionValue() {
        return map.get(CONDITION_VALUE);
    }

    public void setConditionValue(String conditionValue) {
        map.put(CONDITION_VALUE, conditionValue);
    }

    public String getConditionCode() {
        return map.get(CONDITION_CODE);
    }

    public void setConditionCode(String conditionCode) {
        map.put(CONDITION_CODE, conditionCode);
    }

    public String getConditionType() {
        return map.get(CONDITION_TYPE);
    }

    public void setConditionType(String conditionType) {
        map.put(CONDITION_TYPE, conditionType);
    }

    public void setFilterColumn(String filterColumn) {
        this.filterColumn = filterColumn;
    }

    public String getFilterColumn() {
        return filterColumn;
    }

    public void setFilterOperation(String filterOperation) {
        this.filterOperation = filterOperation;
    }

    public String getFilterOperation() {
        return filterOperation;
    }


    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
