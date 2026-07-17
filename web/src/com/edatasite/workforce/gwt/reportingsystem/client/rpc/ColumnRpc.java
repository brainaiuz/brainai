package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractRpcMap;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 18-Mar-2010
 * Time: 16:53:09
 */
public class ColumnRpc extends AbstractRpcMap implements IsSerializable, Serializable {

    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String LOOK_UP_FIELD = "lookUpField";
    private static final String LOOK_UP_SQL = "lookUpSql";
    private static final String CUSTOM_DATE_FORMAT = "customDateFormat";
    private static final String FILTER_WIDGET_TYPE = "filterWidgetType";
    private static final String RELETED_COLUMN = "reletedcolumn";
    private static final String RELETED_PROJECT_COLUMN = "reletedprojectcolumn";
    private static final String TABLE = "table";
    private static final String ID = "id";
    private static final String PARENT = "parent";
    private static final String WHERE = "where";
    private static final String COLUMN = "column";
    private static final String SPLITTER = "splitter";
    private static final String PREFIX = "prefix";
    private static final String TYPE = "type";                         // like:  string, number (core types)
    private static final String COLUMN_FORMAT = "columnFormat";                // visibility formats like: percent, time, date, etc
    private static final String FIELD_NAME = "fieldName";                   //Field name (Label) that would be displayed near component
    private static final String DISPLAY_ITEMS = "displayItems";                //items that would be displayed on the screen
    private static final String RETURNING_ITEMS = "returningItems";              //items that would be returned. Relates to displayed items
    private static final String CUSTOM_QUERY = "customQuery";                 //Custom query that return items to display and return
    private static final String CUSTOM_FIELD = "customField";                 //Custom field: Textbox (user type himself required value), Lookup (return available items (displayItems, returningItems), Date
    private static final String FIRST_VALUE = "firstValue";                  //First variable that would be replaced inside subquery
    private static final String SECOND_VALUE = "secondValue";                 //Second variable that would be replace inside subquery used for DateBox
    private static final String CUSTOM_FIELD_JOIN = "customFieldJoin";            //sql query for left join customField Data table
    private static final String CHECKED = "checked";
    private static final String SUM = "sum";
    private static final String AVG = "avg";
    private static final String LARGEST = "largest";
    private static final String SMALLEST = "smallest";
    private static final String COUNT = "count";
    private static final String SAMEPERIODLASTYEAR = "sameperiodlastyear";

    //drill down report params
    private static final String IS_DRILL_DOWN_REPORT = "isDrillDownReport";
    private static final String LINKED_REPORT_ID = "linkedReportId";
    private static final String FILTER_PARAMETR = "filterParametr";

    //CustomField
    private static final String IS_CUSTOM_FIELD = "isCustomField";
    private static final String LIST_FILTER = "listFilter";

    //TreeLookUp uchun kerak, kerakli narsa
    private static final String TREE_SELECT = "treeSelect";


    private HashMap<String, String> valueMap = null;
    private ColumnRpc reletedColumn;

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public ColumnRpc() {
        setPrefix("t");
    }

    public ColumnRpc(String name) {
        this();
        setName(name);
    }

    public ColumnRpc(ColumnRpc columnRpc) {
        this();
        apply(columnRpc);
    }

    public void apply(ColumnRpc columnRpc) {
        if (columnRpc != null) {

            this.setName(columnRpc.getName());
            this.setTitle(columnRpc.getTitle());
            this.setType(columnRpc.getType());
            this.setColumnFormat(columnRpc.getColumnFormat());
            this.setChecked(columnRpc.isChecked());
            this.setSum(columnRpc.isSum());
            this.setAvg(columnRpc.isAvg());
            this.setLargest(columnRpc.isLargest());
            this.setSmallest(columnRpc.isSmallest());
            this.setCount(columnRpc.isCount());
            this.setLookUpField(columnRpc.getLookUpField());
            this.setLookupSql(columnRpc.getLookupSql());
            this.setFilterWidgetType(columnRpc.getFilterWidgetType());
            this.setDrillDownReport(columnRpc.getDrillDownReport());
            this.setLinkedReportId(columnRpc.getLinkedReportId());
            this.setFilterParametr(columnRpc.getFilterParametr());
            this.setCustomDateFormat(columnRpc.getCustomDateFormat());
            this.setFieldName(columnRpc.getFieldName());
            this.setDisplayItems(columnRpc.getDisplayItems());
            this.setReturningItems(columnRpc.getReturningItems());
            this.setCustomQuery(columnRpc.getCustomQuery());
            this.setCustomField(columnRpc.getCustomField());
            this.setFirstValue(columnRpc.getFirstValue());
            this.setSecondValue(columnRpc.getSecondValue());
            this.setTreeSelect(columnRpc.isTreeSelect());
            this.setTable(columnRpc.getTable());
            this.setId(columnRpc.getId());
            this.setParent(columnRpc.getParent());
            this.setWhere(columnRpc.getWhere());
            this.setColumn(columnRpc.getColumn());
            this.setSplitter(columnRpc.getSplitter());
            this.setIsCustomField(columnRpc.getIsCustomField());
            this.setCustomFieldJoin(columnRpc.getCustomFieldJoin());
            this.setPrefix(columnRpc.getPrefix());
            this.setReletedColumn(columnRpc.getReletedColumn());
            this.setSameperiodlastyear(columnRpc.isSamePeriodLastYear());
        }
    }

    public String getAlias() {
        if (getName() != null) {
            return getName().replace(".", "_");
        }
        return getName();
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        addString(NAME, name);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public void setTitle(String title) {
        addString(TITLE, title);
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setType(String type) {
        addString(TYPE, type);
    }

    public String getColumnFormat() {
        return getString(COLUMN_FORMAT);
    }

    public void setColumnFormat(String columnFormat) {
        addString(COLUMN_FORMAT, columnFormat);
    }

    public boolean isChecked() {
        return getBool(CHECKED);
    }

    public void setChecked(boolean checked) {
        addBool(CHECKED, checked);
    }

    public boolean isSum() {
        return getBool(SUM);
    }

    public boolean onlySum() {
        return !(isAvg() || isCount() || isLargest() || isSmallest()) && isSum();
    }

    public void setSum(boolean sum) {
        addBool(SUM, sum);
    }

    public boolean isAvg() {
        return getBool(AVG);
    }

    public void setAvg(boolean avg) {
        addBool(AVG, avg);
    }

    public boolean isLargest() {
        return getBool(LARGEST);
    }

    public void setLargest(boolean largest) {
        addBool(LARGEST, largest);
    }

    public boolean isSmallest() {
        return getBool(SMALLEST);
    }

    public void setSmallest(boolean smallest) {
        addBool(SMALLEST, smallest);
    }

    public boolean isSamePeriodLastYear() {
        return getBool(SAMEPERIODLASTYEAR);
    }

    public void setSameperiodlastyear(boolean sameperiodlastyear) {
        addBool(SAMEPERIODLASTYEAR, sameperiodlastyear);
    }

    public boolean isCount() {
        return getBool(COUNT);
    }

    public void setCount(boolean count) {
        addBool(COUNT, count);
    }

    public String getLookUpField() {
        return getString(LOOK_UP_FIELD);
    }

    public void setLookUpField(String lookUpField) {
        addString(LOOK_UP_FIELD, lookUpField);
    }

    public String getLookupSql() {
        return getString(LOOK_UP_SQL);
    }

    public void setLookupSql(String lookupSql) {
        addString(LOOK_UP_SQL, lookupSql);
    }

    public String getFilterWidgetType() {
        return getString(FILTER_WIDGET_TYPE);
    }

    public void setFilterWidgetType(String filterWidgetType) {
        addString(FILTER_WIDGET_TYPE, filterWidgetType);
    }

    public Boolean getDrillDownReport() {
        return getBoolean(IS_DRILL_DOWN_REPORT);
    }

    public void setDrillDownReport(Boolean drillDownReport) {
        addBoolean(IS_DRILL_DOWN_REPORT, drillDownReport);
    }

    public Integer getLinkedReportId() {
        return getInteger(LINKED_REPORT_ID);
    }

    public void setLinkedReportId(Integer linkedReportId) {
        addInteger(LINKED_REPORT_ID, linkedReportId);
    }

    public Integer getFilterParametr() {
        return getInteger(FILTER_PARAMETR);
    }

    public void setFilterParametr(Integer filterParametr) {
        addInteger(FILTER_PARAMETR, filterParametr);
    }

    public String getCustomDateFormat() {
        return getString(CUSTOM_DATE_FORMAT);
    }

    public void setCustomDateFormat(String customDateFormat) {
        addString(CUSTOM_DATE_FORMAT, customDateFormat);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ColumnRpc) {
            return ((ColumnRpc) obj).getName().equals(getName());
        }
        return super.equals(obj);
    }


    public String getFieldName() {
        return getString(FIELD_NAME);
    }

    public void setFieldName(String fieldName) {
        addString(FIELD_NAME, fieldName);
    }

    public String getDisplayItems() {
        return getString(DISPLAY_ITEMS);
    }

    public void setDisplayItems(String displayItems) {
        addString(DISPLAY_ITEMS, displayItems);
    }

    public String getReturningItems() {
        return getString(RETURNING_ITEMS);
    }

    public void setReturningItems(String returningItems) {
        addString(RETURNING_ITEMS, returningItems);
    }

    public String getCustomQuery() {
        return getString(CUSTOM_QUERY);
    }

    public void setCustomQuery(String customQuery) {
        addString(CUSTOM_QUERY, customQuery);
    }

    public String getCustomField() {
        return getString(CUSTOM_FIELD);
    }

    public void setCustomField(String customField) {
        addString(CUSTOM_FIELD, customField);
    }

    public String getFirstValue() {
        return getString(FIRST_VALUE);
    }

    public void setFirstValue(String firstValue) {
        addString(FIRST_VALUE, firstValue);
    }

    public String getSecondValue() {
        return getString(SECOND_VALUE);
    }

    public void setSecondValue(String secondValue) {
        addString(SECOND_VALUE, secondValue);
    }

    public void setTreeSelect(boolean treeSelect) {
        addBool(TREE_SELECT, treeSelect);
    }

    public boolean isTreeSelect() {
        return getBool(TREE_SELECT);
    }

    public void setTable(String table) {
        addString(TABLE, table);
    }

    public String getTable() {
        return getString(TABLE);
    }

    public void setId(String id) {
        addString(ID, id);
    }

    public String getId() {
        return getString(ID);
    }

    public void setParent(String parent) {
        addString(PARENT, parent);
    }

    public String getParent() {
        return getString(PARENT);
    }

    public void setWhere(String where) {
        addString(WHERE, where);
    }

    public String getWhere() {
        return getString(WHERE);
    }

    public void setColumn(String column) {
        addString(COLUMN, column);
    }

    public String getColumn() {
        return getString(COLUMN);
    }

    public String getSplitter() {
        return getString(SPLITTER);
    }

    public void setSplitter(String splitter) {
        addString(SPLITTER, splitter);
    }

    public boolean getIsCustomField() {
        return getBool(IS_CUSTOM_FIELD);
    }

    public void setIsCustomField(boolean customField) {
        addBool(IS_CUSTOM_FIELD, customField);
    }

    public void setCustomFieldJoin(String customFieldJoin) {
        addString(CUSTOM_FIELD_JOIN, customFieldJoin);
    }

    public String getCustomFieldJoin() {
        return getString(CUSTOM_FIELD_JOIN);
    }

    public void setPrefix(String prefix) {
        addString(PREFIX, prefix);
    }

    public String getPrefix() {
        return getString(PREFIX);
    }

    public void setListFilter(boolean listFilter) {
        addBool(LIST_FILTER, listFilter);
    }

    public boolean isListFilter() {
        return getBool(LIST_FILTER);
    }

    public String getReletedColumn() {
        return getString(RELETED_COLUMN);
    }

    public void setReletedColumn(String reletedColumn) {
        addString(RELETED_COLUMN, reletedColumn);
    }

    public String getReletedProjectColumn() {
        return getString(RELETED_PROJECT_COLUMN);
    }

    public void setReletedProjectColumn(String reletedProjectColumn) {
        addString(RELETED_PROJECT_COLUMN, reletedProjectColumn);
    }
}
