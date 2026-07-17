package com.finnetlimited.reportservice.core.server.domain.schema;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FilterRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCustomizeFilter;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 3/22/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "reportingCustomize")
public class EdsCustomizeReport extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    String dtype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportCode", referencedColumnName = "code")
    @ForeignKey(name = "none")
    private EdsReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @ForeignKey(name = "none")
    private EdsUser userid;

    private String orderbycolumntype;//ASC:DESC
    private String orderbycolumn;//X column
    private String filter;
    private Integer rowCount;

    @Column(name = "arraycolumns", length = 5000)
    @Type(type = "text")
    private String arraycolumns;
    @Column(name = "arrayoperator")
    private String arrayoperator;
    @Column(name = "arrayvalues", length = 5000)
    private String arrayvalues;
    @Column(name = "arraycomparators")
    private String arraycomparators;

    public EdsReport getReport() {
        return report;
    }

    public void setReport(EdsReport report) {
        this.report = report;
    }

    public String getOrderbycolumntype() {
        return orderbycolumntype;
    }

    public void setOrderbycolumntype(String orderbycolumntype) {
        this.orderbycolumntype = orderbycolumntype;
    }

    public String getOrderbycolumn() {
        return orderbycolumn;
    }

    public void setOrderbycolumn(String orderbycolumn) {
        this.orderbycolumn = orderbycolumn;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public String getArrColumn() {
        return arraycolumns;
    }

    public void setArrColumn(String arrColumn) {
        this.arraycolumns = arrColumn;
    }

    public String getArrOperators() {
        return arrayoperator;
    }

    public void setArrOperators(String arrOperators) {
        this.arrayoperator = arrOperators;
    }


    public String getArrValues() {
        return arrayvalues;
    }

    public void setArrValues(String arrValues) {
        this.arrayvalues = arrValues;
    }

    public String getComparators() {
        return arraycomparators;
    }

    public void setComparators(String comparators) {
        this.arraycomparators = comparators;
    }

    public EdsUser getUserid() {
        return userid;
    }

    public void setUserid(EdsUser userid) {
        this.userid = userid;
    }

    public ReportingCustomizeFilter toRPC() {
        ReportingCustomizeFilter filter = new ReportingCustomizeFilter();
        if (!(getReport() == null || ServerUtils.isNullOrEmpty(getReport().getCode()))) {
            filter.setReportCode(getReport().getCode());
            filter.setName(getReport().getName());
            filter.setId(getReport().getObjectID());
        }
        filter.setWidget_GUID(getDtype());
        filter.setRowCount(getRowCount());
        filter.setSelectedViewAsName(getFilter());
        filter.setSortType(getOrderbycolumntype());
        filter.setSortColumnName(getOrderbycolumn());

        return filter;
    }

    public static void appendFilterRpc(ReportRpc reportRpc, ReportingCustomizeFilter filter, EdsCustomizeReport edsCustomizeReport) {
        int i = filter.getFilterRpcs().size();
        if (edsCustomizeReport != null && !StrUtils.isEmpty(edsCustomizeReport.getArrColumn())) {
            filter.getFilterRpcs().clear();
            i = 0;
            LinkedList<ColumnRpc> field = new LinkedList<>();
            String[] fieldSplit = edsCustomizeReport.getArrColumn().split("[#]");
            for (String aFieldSplit : fieldSplit) {

                ColumnRpc columnRpc = reportRpc.getColumnMap().get(aFieldSplit);
                if (columnRpc != null) {
                    FilterRpc filterRpc = new FilterRpc();
                    filterRpc.setColumn(columnRpc.getName());
                    filterRpc.setValue(edsCustomizeReport.getArrValues().split("#")[i]);
                    filterRpc.setOperation(edsCustomizeReport.getArrOperators().split("#")[i]);
                    if (edsCustomizeReport.getComparators().split("#").length > i) {
                        filterRpc.setAndOr(edsCustomizeReport.getComparators().split("#")[i]);
                    }
                    i++;
                    filter.getFilterRpcs().add(filterRpc);
                }
            }
        }
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }
}
