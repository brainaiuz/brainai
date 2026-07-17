package com.finnetlimited.reportservice.core.server.domain.schema;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.reporting.EdsChartConfig;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.rpc.RpcMap;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ListItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 15:46:08
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "reporting", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class EdsReport extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folderid")
    private EdsFolders folderid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdftemplateid")
    @ForeignKey(name = "none")
    private EdsCompanyPdfTemplate pdftemplateid;

    @Column(name = "excelTemplateId")
    private Integer excelTemplateId;

    @Column(name = "xmlTemplateId")
    private Integer xmlTemplateId;

    @Index(name = "reporting_index_viewcode")
    private String viewCode;

    @Column(name = "viewName")
    private String viewName;

    @Column(name = "reportType")
    private String reportType;// Tabular or Summary

    @Column(name = "selectColumns")
    @Type(type = "text")
    private String selectColumns;

    @Column(name = "columnformats")
    @Type(type = "text")
    private String columnFormats;

    @Column(name = "sumValues")
    @Type(type = "text")
    private String sumValues;

    @Column(name = "avgValues")
    @Type(type = "text")
    private String avgValues;

    @Column(name = "largestValues")
    private String largestValues;

    @Column(name = "smallestValues")
    @Type(type = "text")
    private String smallestValues;

    @Column(name = "countValues")
    @Type(type = "text")
    private String countValues;

    @Column(name = "filterName")
    private String filterName;

    @Column(name = "duration")
    private String duration;

    @Column(name = "startdate")
    private String startDate;

    @Column(name = "enddate")
    private String endDate;

    @Column(name = "arraybinds")
    @Type(type = "text")
    private String arraybinds;

    @Column(name = "arraycolumns")
    @Type(type = "text")
    private String arraycolumns;

    @Column(name = "arraycomparators")
    @Type(type = "text")
    private String arraycomparators;

    @Column(name = "arrayvalues")
    @Type(type = "text")
    private String arrayvalues;

    @Column(name = "arrayoperator")
    @Type(type = "text")
    private String arrayoperator;

    @Column(name = "arraypromtbyinputs")
    @Type(type = "text")
    private String arraypromtbyinputs;

    @Column(name = "orderbycolumn", length = 200)
    @Type(type = "text")
    private String orderbycolumn;

    @Column(name = "orderbycolumntype", length = 50)
    private String orderbycolumntype;

    @Basic
    private String groupColumns;

    @Basic
    private String sortOrders;// Asc or Desc

    @Basic
    private String groupRange;

    @Basic
    private Integer queryLimit;

    @Column(name = "showHide")
    private Boolean showHide = true; // show report {show=true} else not show

    @Column(name = "showActions")
    private Boolean showActions = false;

    private Boolean enableAddNewAction = false;
    private Boolean enableViewAction = false;
    private Boolean enableEditAction = false;
    private Boolean enableDeleteAction = false;

    @Column(name = "showDrillReport")
    private Boolean showDrillReport = false;

    @Column(name = "showActionsIcon")
    private Boolean showActionsIcon = false;

    @Column(name = "isDetailed")
    private Boolean isDetailed = true;

    @Column(name = "isShowRowCount")
    private Boolean isShowRowCount = false;

    @Column(name = "isTransposed")
    private Boolean isTransposed = false;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chartconfig_id")
    @ForeignKey(name = "none")
    private EdsChartConfig chartConfig;

    @Transient
    private Integer temp;

    @Transient
    private Integer tempWidgetId;

    @Column(name = "drilldownreport")
    @Type(type = "text")
    private String drilldownreport;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailTemplate")
    @ForeignKey(name = "none")
    private EdsEmailTemplate emailTemplate;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "reportingemployee",
            joinColumns = {@JoinColumn(name = "reportId")},
            inverseJoinColumns = {@JoinColumn(name = "employeeId")}
    )
    private Set<EdsUser> targetUsers = new HashSet<>();

    @Index(name = "reporting_index_code")
    @Column(unique = true)
    private String code;

    @Column(name = "customVariable")
    private String customVariable;

    @Column(name = "customValue")
    private String customValue;

    @Column(name = "maxExcelRowCount")
    private Integer maxExcelRowCount;

    @Column(name = "conditionCode")
    private String conditionCode;

    @Column(name = "deleted")
    private Boolean deleted;

    @Embedded
    private EdsAuditInfo auditInfo;

    @Column(name = "permissionCode")
    private String permissionCode;

    private Boolean fakeReport;//redirect to other Pages

    private String targetLink;

    private Integer sorder;

    @Column(name = "filterPattern", length = 5000)
    @Type(type = "text")

    private String filterPattern;

    private Boolean synchronization;

    @Column(name = "addproject")
    private Boolean addProject;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kpiwidget_id")
    @ForeignKey(name = "none")
    private EdsKpiWidget kpiWidget;

    @Column(name = "recurrence_settings_id")
    private Integer recurrenceSettingsId;
    @Column(name = "issuccess")
    private Boolean isSuccess;
    @Column(name = "last_exception")
    @Type(type = "text")
    private String lastException;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean issuccess) {
        this.isSuccess = issuccess;
    }

    public String getLastException() {
        return lastException;
    }

    public void setLastException(String last_exception) {
        this.lastException = last_exception;
    }

    @Basic
    private String viewTypes;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static String wrapper() {
        StringBuilder builder = new StringBuilder();
        for (Field field : EdsReport.class.getDeclaredFields()) {
            if (!",objectID,objectid,objectId,tempWidgetId,recurrenceSettingsId,excelTemplateId,folderid,chartid,pdftemplateid,emailTemplate,targetUsers,dashlets,dashboardDownloadLinks,chartId,auditInfo,deleted,template,permission,chartConfig,kpiWidget".contains(field.getName())) {
                builder.append(field.getName() + ", ");
            }
        }
        return builder.delete(builder.length() - 2, builder.length()).toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsFolders getFolder() {
        return folderid;
    }

    public void setFolder(EdsFolders folder) {
        this.folderid = folder;
    }

    public EdsCompanyPdfTemplate getPdftemplate() {
        return pdftemplateid;
    }

    public void setPdftemplate(EdsCompanyPdfTemplate pdftemplate) {
        this.pdftemplateid = pdftemplate;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getViewCode() {
        return viewCode;
    }

    public String getTableType() {
        return reportType;
    }

    public void setTableType(String tableType) {
        this.reportType = tableType;
    }

    public Integer getRecurrenceSettingsId() {
        return recurrenceSettingsId;
    }

    public void setRecurrenceSettingsId(Integer recurrenceSettingsId) {
        this.recurrenceSettingsId = recurrenceSettingsId;
    }

    public String getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
    }

    public String getColumnFormats() {
        return columnFormats;
    }


    public void setColumnFormats(String columnFormats) {
        this.columnFormats = columnFormats;
    }

    public String getSumValues() {
        return sumValues;
    }

    public void setSumValues(String sumValues) {
        this.sumValues = sumValues;
    }

    public String getAvgValues() {
        return avgValues;
    }

    public void setAvgValues(String avgValues) {
        this.avgValues = avgValues;
    }

    public String getLargestValues() {
        return largestValues;
    }

    public void setLargestValues(String largestValues) {
        this.largestValues = largestValues;
    }

    public String getSmallestValues() {
        return smallestValues;
    }

    public void setSmallestValues(String smallestValues) {
        this.smallestValues = smallestValues;
    }

    public String getCountValues() {
        return countValues;
    }

    public void setCountValues(String countValues) {
        this.countValues = countValues;
    }

    public String getStandartFilterColumn() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBinds() {
        return arraybinds;
    }

    public void setBinds(String binds) {
        this.arraybinds = binds;
    }

    public String getArrColumn() {
        return arraycolumns;
    }

    public void setArrColumn(String arrColumn) {
        this.arraycolumns = arrColumn;
    }

    public String getComparators() {
        return arraycomparators;
    }

    public void setComparators(String comparators) {
        this.arraycomparators = comparators;
    }

    public String getArrValues() {
        return arrayvalues;
    }

    public void setArrValues(String arrValues) {
        this.arrayvalues = arrValues;
    }

    public String getArrOperators() {
        return arrayoperator;
    }

    public void setArrOperators(String arrOperators) {
        this.arrayoperator = arrOperators;
    }

    public String getArrPromtByInputs() {
        return arraypromtbyinputs;
    }

    public void setArrPromtByInputs(String arrPromtByInputs) {
        this.arraypromtbyinputs = arrPromtByInputs;
    }

    public String getGroupColumns() {
        return groupColumns;
    }

    public void setGroupColumns(String groupColumns) {
        this.groupColumns = groupColumns;
    }

    public String getSortOrders() {
        return sortOrders;
    }

    public void setSortOrders(String sortOrders) {
        this.sortOrders = sortOrders;
    }

    public String getGroupRange() {
        return groupRange;
    }

    public void setGroupRange(String groupRange) {
        this.groupRange = groupRange;
    }

    public Integer getQueryLimit() {
        return queryLimit;
    }

    public void setQueryLimit(Integer queryLimit) {
        this.queryLimit = queryLimit;
    }

    public Boolean isShow() {
        return showHide;
    }

    public void setShow(Boolean show) {
        this.showHide = show;
    }

    public Boolean getShowDrillReport() {
        return showDrillReport;
    }

    public void setShowDrillReport(Boolean showDrillReport) {
        this.showDrillReport = showDrillReport;
    }

    public Boolean getShowActions() {
        return showActions;
    }

    public void setShowActions(Boolean showActions) {
        this.showActions = showActions;
    }

    public Boolean enableAddNewAction() {
        return enableAddNewAction != null ? enableAddNewAction : false;
    }

    public void setEnableAddNewAction(Boolean enableAddNewAction) {
        this.enableAddNewAction = enableAddNewAction;
    }

    public Boolean enableViewAction() {
        return enableViewAction != null ? enableViewAction : false;
    }

    public void setEnableViewAction(Boolean enableViewAction) {
        this.enableViewAction = enableViewAction;
    }

    public Boolean enableEditAction() {
        return enableEditAction != null ? enableEditAction : false;
    }

    public void setEnableEditAction(Boolean enableEditAction) {
        this.enableEditAction = enableEditAction;
    }

    public Boolean enableDeleteAction() {
        return enableDeleteAction != null ? enableDeleteAction : false;
    }

    public void setEnableDeleteAction(Boolean enableDeleteAction) {
        this.enableDeleteAction = enableDeleteAction;
    }

    public Boolean getShowActionsIcon() {
        return showActionsIcon;
    }

    public void setShowActionsIcon(Boolean showActionsIcon) {
        this.showActionsIcon = showActionsIcon;
    }

    public String getOrderbycolumn() {
        return orderbycolumn;
    }

    public void setOrderbycolumn(String orderbycolumn) {
        this.orderbycolumn = orderbycolumn;
    }

    public String getOrderbycolumntype() {
        return orderbycolumntype;
    }

    public void setOrderbycolumntype(String orderbycolumntype) {
        this.orderbycolumntype = orderbycolumntype;
    }

    public Boolean getDetailed() {
        return isDetailed;
    }

    public void setDetailed(Boolean detailed) {
        isDetailed = detailed;
    }

    public Boolean getShowRowCount() {
        return isShowRowCount;
    }

    public void setShowRowCount(Boolean showRowCount) {
        isShowRowCount = showRowCount;
    }

    public Boolean getTransposed() {
        return isTransposed;
    }

    public void setTransposed(Boolean transposed) {
        isTransposed = transposed;
    }

    public String getDrilDownReport() {
        return drilldownreport;
    }

    public void setDrilDownReport(String drilDownReport) {
        this.drilldownreport = drilDownReport;
    }

    public Integer getExcelTemplateId() {
        return excelTemplateId;
    }

    public void setExcelTemplateId(Integer excelTemplateId) {
        this.excelTemplateId = excelTemplateId;
    }


    public EdsEmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EdsEmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public Set<EdsUser> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(Set<EdsUser> targetUsers) {
        this.targetUsers = targetUsers;
    }


    public Integer getXmlTemplateId() {
        return xmlTemplateId;
    }

    public void setXmlTemplateId(Integer xmlTemplateId) {
        this.xmlTemplateId = xmlTemplateId;
    }

    public String getCustomVariable() {
        return customVariable;
    }

    public void setCustomVariable(String customVariable) {
        this.customVariable = customVariable;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public EdsChartConfig getChartConfig() {
        return chartConfig;
    }

    public void setChartConfig(EdsChartConfig chartConfig) {
        this.chartConfig = chartConfig;
    }

    public EdsKpiWidget getKpiWidget() {
        return kpiWidget;
    }

    public void setKpiWidget(EdsKpiWidget kpiWidget) {
        this.kpiWidget = kpiWidget;
    }

    /* @Override
    public boolean equals(Object o) {
        if (!(o instanceof EdsReport)) {
            return false;
        }
        EdsReport reporting = (EdsReport) o;
        if (this.getObjectID().equals(reporting.getObjectID())) {
            return true;
        }
        return false;
    }*/

    public ReportRpc toRPC() {
        return toRPC(null);
    }

    public ReportRpc toRPC(ReportRpc report) {
        if (report == null) {
            report = new ReportRpc();
        }

        ViewRpc viewRpc = SqlQueryUtil.getViewParser(getViewCode());
        HashMap<String, ColumnRpc> allColumns = report.getColumnMap();
        if (viewRpc != null && viewRpc.getTables() != null) {
            for (int j = 0; j < viewRpc.getTables().size(); j++) {
                for (int i = 0; i < viewRpc.getTables().get(j).getColumns().size(); i++) {
                    allColumns.put(viewRpc.getTables().get(j).getColumns().get(i).getName(), viewRpc.getTables().get(j).getColumns().get(i));
                }
            }
            report.setNoTimeZone(viewRpc.isNoTimezone());
        }
        //Default Filter Column
        report.setFilterColumn(viewRpc.getFilterColumn());
        report.setFilterOperation(viewRpc.getFilterOperation());
        report.setFilterValue(viewRpc.getFilterValue());

        report.setId(getObjectID());
        report.setFolderId(getFolder().getObjectID());
        report.setFolderName(getFolder().getName());
        report.setFolderType(getFolder().getType());
        report.setName(getName());
        report.setCode(getCode());
        report.setDiscreption(getDescription());
        report.setViewName(getViewName());
        report.setTableType(getTableType() != null ? getTableType() : "TABULAR");
        report.setShowActions(getShowActions());
        report.setMaxExcelRowCount(getMaxExcelRowCount());

        report.setEnableAddNewAction(enableAddNewAction());
        report.setEnableViewAction(enableViewAction());
        report.setEnableEditAction(enableEditAction());
        report.setEnableDeleteAction(enableDeleteAction());

        report.setShowDrillReports(getShowDrillReport());
        report.setShowActionsIcon(getShowActionsIcon());

        if (getShowRowCount() != null) {
            report.setShowRowCount(getShowRowCount());
        } else {
            report.setShowRowCount(false);
        }
        if (getDetailed() != null) {
            report.setIsDetailed(getDetailed());
        } else {
            report.setIsDetailed(true);
        }

        if (getTransposed() != null) {
            report.setTransposed(getTransposed());
        } else {
            report.setTransposed(false);
        }

        if (getPdftemplate() != null) {
            report.setPdfTemplateId(getPdftemplate().getObjectID());
        }

        if (getExcelTemplateId() != null) {
            report.setExcelTemplateId(getExcelTemplateId());
        }

        if (getXmlTemplateId() != null) {
            report.setXmlTemplateId(getXmlTemplateId());
        }

        if (getEmailTemplate() != null) {
            SelectItem emailTemplateItem = new SelectItem(getEmailTemplate().getObjectID(), getEmailTemplate().getName(), getEmailTemplate().getCode());
            report.setEmailTemplateItem(emailTemplateItem);
        }

        if (getChartConfig() != null) {
            report.setChartConf(getChartConfig().getRPC());
        }
        if (getKpiWidget() != null) {
            report.setKpiWidgetItem(getKpiWidget().getRPC(report));
        }

        report.setViewCode(getViewCode());

        if (!StrUtils.isEmpty(getDrilDownReport())) {
            String[] drillDownReportValues = getDrilDownReport().split("#");
            for (String drillDownReportValue : drillDownReportValues) {
                String[] tokens = drillDownReportValue.split("<->");
                Integer columnIndex = Integer.parseInt(tokens[0]);
                report.getSelectedColumns().get(columnIndex).setDrillDownReport(true);
                report.getSelectedColumns().get(columnIndex).setLinkedReportId(Integer.parseInt(tokens[1]));
                report.getSelectedColumns().get(columnIndex).setFilterParametr(Integer.parseInt(tokens[2]));
            }
        }
        /* Summaries columns */
        Map<String, ColumnRpc> summariesMap = new HashMap<>();
        // sum
        if (!StrUtils.isEmpty(getSumValues())) {
            String[] sumArr = getSumValues().split("[#]");
            for (String aSumArr : sumArr) {
                if (allColumns.containsKey(aSumArr)) {
                    ColumnRpc column = createColumn(aSumArr, allColumns);
                    column.setSum(true);
                    summariesMap.put(column.getName(), column);
                    allColumns.get(aSumArr).setSum(true);
                }
            }
        }
        // avg
        if (getAvgValues() != null && !getAvgValues().isEmpty()) {
            String[] avgArr = getAvgValues().split("[#]");
            for (String anAvgArr : avgArr) {
                if (summariesMap.containsKey(anAvgArr)) {
                    summariesMap.get(anAvgArr).setAvg(true);
                } else {
                    ColumnRpc column = createColumn(anAvgArr, allColumns);
                    column.setAvg(true);
                    summariesMap.put(column.getName(), column);
                }
                if (allColumns.containsKey(anAvgArr)) {
                    allColumns.get(anAvgArr).setAvg(true);
                }
            }
        }
        // largest
        if (getLargestValues() != null && !getLargestValues().isEmpty()) {
            String[] largestArr = getLargestValues().split("[#]");
            for (String aLargestArr : largestArr) {
                if (summariesMap.containsKey(aLargestArr)) {
                    summariesMap.get(aLargestArr).setLargest(true);
                } else {
                    ColumnRpc column = createColumn(aLargestArr, allColumns);
                    column.setLargest(true);
                    summariesMap.put(column.getName(), column);
                }
                if (allColumns.containsKey(aLargestArr)) {
                    allColumns.get(aLargestArr).setLargest(true);
                }
            }
        }
        // smallest
        if (getSmallestValues() != null && !getSmallestValues().isEmpty()) {
            String[] smallestArr = getSmallestValues().split("[#]");
            for (String aSmallestArr : smallestArr) {
                if (summariesMap.containsKey(aSmallestArr)) {
                    summariesMap.get(aSmallestArr).setSmallest(true);
                } else {
                    ColumnRpc column = createColumn(aSmallestArr, allColumns);
                    column.setSmallest(true);
                    summariesMap.put(column.getName(), column);
                }
                if (allColumns.containsKey(aSmallestArr)) {
                    allColumns.get(aSmallestArr).setSmallest(true);
                }
            }
        }
        //count
        if (getCountValues() != null && !getCountValues().isEmpty()) {
            String[] countArr = getCountValues().split("[#]");
            for (String aCountArr : countArr) {
                if (summariesMap.containsKey(aCountArr)) {
                    summariesMap.get(aCountArr).setCount(true);
                } else {
                    ColumnRpc column = createColumn(aCountArr, allColumns);
                    column.setCount(true);
                    summariesMap.put(column.getName(), column);
                }
                if (allColumns.containsKey(aCountArr)) {
                    allColumns.get(aCountArr).setCount(true);
                }
            }
        }

        report.setSumaries(new LinkedList<>(summariesMap.values()));

        /* Select columns */
        LinkedList<ColumnRpc> selectedColumns = new LinkedList<>();
        String[] selectText = getSelectColumns().split("[#]");
        for (String columnName : selectText) {
            ColumnRpc aSummary = summariesMap.get(columnName);
            ColumnRpc column = createColumn(columnName, allColumns);
            column.setChecked(true);
            column.setDrillDownReport(false);
            if (aSummary != null) {
                column.setAvg(aSummary.isAvg());
                column.setSum(aSummary.isSum());
                column.setSmallest(aSummary.isSmallest());
                column.setCount(aSummary.isCount());
                column.setLargest(aSummary.isLargest());
            }
            selectedColumns.add(column);
        }
        report.setSelectedColumns(selectedColumns);

        if (getOrderbycolumn() != null && !"".equals(getOrderbycolumn())) {
            report.setSortTableByColumn(getOrderbycolumn());
        }
        if (getOrderbycolumntype() != null && !"".equals(getOrderbycolumntype())) {
            report.setSortTableByColumnType(getOrderbycolumntype());
        }

        if (ReportType.SUMMARY.name().equals(getTableType())) {
            LinkedList<ColumnRpc> groups = new LinkedList<>();
            ArrayList<String> sortable = new ArrayList<>();
            ArrayList<String> range = new ArrayList<>();
            if (getGroupColumns() != null && !"".equals(getGroupColumns())) {
                String[] groupsSplit = getGroupColumns().split("[#]");
                for (String aGroupsSplit : groupsSplit) {
                    ColumnRpc columnRpc = createColumn(aGroupsSplit, allColumns);
                    groups.add(columnRpc);
                }
                report.setGroupColumns(groups);
            }
            if (!StrUtils.isEmpty(getSortOrders())) {
                String[] sortableSplit = getSortOrders().split("[#]");
                Collections.addAll(sortable, sortableSplit);
                report.setSortTypes(sortable);
            }
            if (!StrUtils.isEmpty(getGroupRange())) {
                String[] rangeSplit = getGroupRange().split("[#]");
                for (String aRangeSplit : rangeSplit) {
                    if (!"".equals(aRangeSplit.trim())) {
                        range.add(aRangeSplit);
                    } else {
                        range.add("");
                    }
                }
                report.setRangeType(range);
            }

            ArrayList<String> types = new ArrayList<>();
            if (!StrUtils.isEmpty(getViewTypes())) {
                String[] viewTypes = getViewTypes().split("[#]");
                for (String viewType : viewTypes) {
                    if (!"".equals(viewType.trim())) {
                        types.add(viewType);
                    } else {
                        types.add("");
                    }
                }
                report.setViewTypes(types);
            }
        }

        if (!StrUtils.isEmpty(getArrColumn())) {
            LinkedList<ColumnRpc> field = new LinkedList<>();
            String[] fieldSplit = getArrColumn().split("[#]");
            for (String aFieldSplit : fieldSplit) {
                ColumnRpc columnRpc = createColumn(aFieldSplit, allColumns);
                field.add(columnRpc);
            }
            report.setFieldd(field);
        }

        if (!StrUtils.isEmpty(getBinds())) {
            ArrayList<Integer> sett = new ArrayList<>();
            String[] settSplit = getBinds().split("[#]");
            for (String aSettSplit : settSplit) {
                sett.add(Integer.valueOf(aSettSplit));
            }
            report.setSett(sett);
        }

        if (!StrUtils.isEmpty(getArrOperators())) {
            ArrayList<String> operator = new ArrayList<>();
            String[] operatorSplit = getArrOperators().split("[#]");
            Collections.addAll(operator, operatorSplit);
            report.setOperators(operator);
        }

        if (!StrUtils.isEmpty(getArrValues())) {
            ArrayList<String> value = new ArrayList<>();
            String[] valueSplit = getArrValues().split("[#]");
            Collections.addAll(value, valueSplit);
            report.setValues(value);
        }
        if (!StrUtils.isEmpty(getComparators())) {
            ArrayList<String> boolType = new ArrayList<>();
            String[] comparatorSplit = getComparators().split("[#]");
            Collections.addAll(boolType, comparatorSplit);
            if (boolType.size() > 0) {
                boolType.add("");
            }
            report.setBoolType(boolType);
        } else if (report.getValues().size() > 0) {
            report.addToBoolType("");
        }

        if (!StrUtils.isEmpty(getArrPromtByInputs())) {
            ArrayList<Integer> promtList = new ArrayList<>();
            String[] promtSplit = getArrPromtByInputs().split("[#]");
            for (String aPromtSplit : promtSplit) {
                promtList.add(Integer.parseInt(aPromtSplit));
            }
            if (promtList.size() > 0) {
                promtList.add(0);
            }
            report.setPromtList(promtList);
        }

        if (getStandartFilterColumn() != null) {
            report.setSntFilterName(getStandartFilterColumn());
            report.setDurationType(getDuration());
            report.setStartDate(getStartDate());
            report.setEndDate(getEndDate());
        }

        if (getQueryLimit() != null) {
            report.setLimit(getQueryLimit());
        } else {
            report.setLimit(-1);
        }

        //Values and Variables kept in database with separator = |
        if (!StrUtils.isEmpty(getCustomValue()) && !StrUtils.isEmpty(getCustomVariable())) {
            HashMap<String, String> customFilterParameters = new HashMap<>();
            String[] customValues = getCustomValue().split("\\|");
            String[] customVariables = getCustomVariable().split("\\|");
            for (int i = 0; i < customValues.length; i++) {
                customFilterParameters.put(customVariables[i], customValues[i]);
            }
            report.setCustomFilter(customFilterParameters);
        }
        report.setFilterPattern(getFilterPattern());
        try {
            report.setCompanyId(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        } catch (Exception e) {
        }
        report.setPermissionCode(getPermissionCode());
        report.setAddProject(getAddProject());
        return report;
    }

    public static ColumnRpc createColumn(final String columnName, final HashMap<String, ColumnRpc> columns) {
        if (columns == null || columns.isEmpty() || !columns.containsKey(columnName)) {
            return new ColumnRpc(columnName);
        }
        return new ColumnRpc(columns.get(columnName));
    }

    public EdsReport getNew(EdsReport fld) {
        EdsReport edsReport = fld == null ? new EdsReport() : fld;
        edsReport.name = name;
        edsReport.code = code;
        edsReport.description = description;
        edsReport.excelTemplateId = excelTemplateId;
        edsReport.xmlTemplateId = xmlTemplateId;
        edsReport.viewCode = viewCode;
        edsReport.viewName = viewName;
        edsReport.reportType = reportType;
        edsReport.selectColumns = selectColumns;
        edsReport.columnFormats = columnFormats;
        edsReport.sumValues = sumValues;
        edsReport.avgValues = avgValues;
        edsReport.largestValues = largestValues;
        edsReport.smallestValues = smallestValues;
        edsReport.countValues = countValues;
        edsReport.filterName = filterName;
        edsReport.duration = duration;
        edsReport.startDate = startDate;
        edsReport.endDate = endDate;
        edsReport.arraybinds = arraybinds;
        edsReport.arraycolumns = arraycolumns;
        edsReport.arraycomparators = arraycomparators;
        edsReport.arrayvalues = arrayvalues;
        edsReport.arrayoperator = arrayoperator;
        edsReport.arraypromtbyinputs = arraypromtbyinputs;
        edsReport.orderbycolumn = orderbycolumn;
        edsReport.orderbycolumntype = orderbycolumntype;
        edsReport.groupColumns = groupColumns;
        edsReport.sortOrders = sortOrders;
        edsReport.groupRange = groupRange;
        edsReport.queryLimit = queryLimit;
        edsReport.showHide = showHide;
        edsReport.showActions = showActions;
        edsReport.enableAddNewAction = enableAddNewAction;
        edsReport.enableViewAction = enableViewAction;
        edsReport.enableEditAction = enableEditAction;
        edsReport.enableDeleteAction = enableDeleteAction;
        edsReport.showDrillReport = showDrillReport;
        edsReport.showActionsIcon = showActionsIcon;
        edsReport.isDetailed = isDetailed;
        edsReport.isShowRowCount = isShowRowCount;
        edsReport.isTransposed = isTransposed;
        edsReport.drilldownreport = drilldownreport;
        if (fld == null || getAuditInfo().getCreationDate() == null) {
            getAuditInfo().setCreationDate(new Date());
        }
        edsReport.targetLink = targetLink;
        getAuditInfo().setModificationDate(new Date());
        edsReport.conditionCode = conditionCode;
        edsReport.sorder = sorder;
        edsReport.filterPattern = filterPattern;
        edsReport.fakeReport = fakeReport;
        edsReport.addProject = addProject;
        return edsReport;
    }

    public String getCode() {
        return code;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getTempWidgetId() {
        return tempWidgetId;
    }

    public void setTempWidgetId(Integer tempWidgetId) {
        this.tempWidgetId = tempWidgetId;
    }

    public Integer getMaxExcelRowCount() {
        return maxExcelRowCount;
    }

    public void setMaxExcelRowCount(Integer rowcount) {
        this.maxExcelRowCount = rowcount;
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public boolean getFakeReport() {
        return Boolean.TRUE.equals(fakeReport);
    }

    public void setFakeReport(Boolean fakeReport) {
        this.fakeReport = fakeReport;
    }

    public String getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(String targetLink) {
        this.targetLink = targetLink;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public Boolean getSynchronization() {
        return synchronization;
    }

    public void setSynchronization(Boolean synchronization) {
        this.synchronization = synchronization;
    }


    public Boolean getAddProject() {
        return addProject;
    }

    public void setAddProject(Boolean addProject) {
        this.addProject = addProject;
    }

    public ListItem toListItem() {
        ListItem item = new ListItem();
        item.setId(getObjectID());
        item.setName(getName());
        item.setXmlTemplateId(getXmlTemplateId());
        item.setViewCode(getViewCode());
        item.setDescription(getDescription());
        if (getFolder() != null) {
            item.setFolderName(getFolder().getName());
        }
        return item;
    }

    public SelectListRpc toSelectListRpc() {
        SelectListRpc item = new SelectListRpc();
        item.setId(getObjectID());
        item.setName(getName());
        item.setDescription(getDescription() != null ? getDescription() : "");
        item.setFakeReport(getFakeReport());
        item.setTargetLink(getTargetLink());
        item.setCode(getCode());
        item.setSynchronization(getSynchronization());
        if (folderid != null) {
            item.setFolder(getFolder().getName());
        }
        return item;
    }

    public RpcMap getMap() {
        try {
            RpcMap rpcMap = RpcMap.get(this);
            rpcMap.addChild("folderid", RpcMap.get(folderid));
            rpcMap.addChild("emailTemplate", RpcMap.get(emailTemplate));

            if (excelTemplateId != null) {
                UploadManager<EdsUpload> uploadManager = (UploadManager<EdsUpload>) ApplicationContextProvider.applicationContext.getBean("uploadManager");
                EdsUpload edsUpload = uploadManager.get(excelTemplateId);
                rpcMap.addChild("excelTemplate", RpcMap.get(edsUpload));
                rpcMap.addChild("uploadAmazonSettings", RpcMap.get(uploadManager.getUploadSettings(edsUpload)));
            }
            return rpcMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getViewTypes() {
        return viewTypes;
    }

    public void setViewTypes(String viewTypes) {
        this.viewTypes = viewTypes;
    }
}
