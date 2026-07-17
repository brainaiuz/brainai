package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 15:46:08
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "kpiWidgetFilter")
public class EdsKpiWidgetFilter extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpiWidgetId")
    @ForeignKey(name = "none")
    private EdsKpiWidget kpiWidgetId;

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

    @Column(name = "filterPattern", length = 5000)
    @Type(type = "text")
    private String filterPattern;

    @Column(name = "filterType")
    private Integer filterType;

    private Integer lastYearIndex;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public EdsKpiWidget getKpiWidgetId() {
        return kpiWidgetId;
    }

    public void setKpiWidgetId(EdsKpiWidget kpiWidgetId) {
        this.kpiWidgetId = kpiWidgetId;
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filtertype) {
        this.filterType = filtertype;
    }

    public KpiWidgetFilterItem toRPC(ReportRpc report) {
        KpiWidgetFilterItem kpiWidgetFilterItem = new KpiWidgetFilterItem();

        kpiWidgetFilterItem.setId(getObjectID());
        kpiWidgetFilterItem.setFilterType(getFilterType());
        HashMap<String, ColumnRpc> allColumns = report.getColumnMap();

        if (!StrUtils.isEmpty(getArrColumn())) {
            LinkedList<ColumnRpc> field = new LinkedList<>();
            String[] fieldSplit = getArrColumn().split("[#]");
            for (String aFieldSplit : fieldSplit) {
                ColumnRpc columnRpc = createColumn(aFieldSplit, allColumns);
                field.add(columnRpc);
            }
            kpiWidgetFilterItem.setFieldd(field);
        }
        if (getLastYearIndex() != null && kpiWidgetFilterItem.getFieldd() != null && kpiWidgetFilterItem.getFieldd().size() > 0) {
            kpiWidgetFilterItem.getFieldd().get(getLastYearIndex()).setSameperiodlastyear(true);
        }

        if (!StrUtils.isEmpty(getBinds())) {
            ArrayList<Integer> sett = new ArrayList<>();
            String[] settSplit = getBinds().split("[#]");
            for (String aSettSplit : settSplit) {
                sett.add(Integer.valueOf(aSettSplit));
            }
            kpiWidgetFilterItem.setSett(sett);
        }

        if (!StrUtils.isEmpty(getArrOperators())) {
            ArrayList<String> operator = new ArrayList<>();
            String[] operatorSplit = getArrOperators().split("[#]");
            Collections.addAll(operator, operatorSplit);
            kpiWidgetFilterItem.setOperators(operator);
        }

        if (!StrUtils.isEmpty(getArrValues())) {
            ArrayList<String> value = new ArrayList<>();
            String[] valueSplit = getArrValues().split("[#]");
            Collections.addAll(value, valueSplit);
            kpiWidgetFilterItem.setValues(value);
        }
        if (!StrUtils.isEmpty(getComparators())) {
            ArrayList<String> boolType = new ArrayList<>();
            String[] comparatorSplit = getComparators().split("[#]");
            Collections.addAll(boolType, comparatorSplit);
            if (boolType.size() > 0) {
                boolType.add("");
            }
            kpiWidgetFilterItem.setBoolType(boolType);
        } else if (kpiWidgetFilterItem.getValues().size() > 0) {
            kpiWidgetFilterItem.addToBoolType("");
        }

        kpiWidgetFilterItem.setFilterPattern(getFilterPattern());

        return kpiWidgetFilterItem;
    }

    public static ColumnRpc createColumn(final String columnName, final HashMap<String, ColumnRpc> columns) {
        if (columns == null || columns.isEmpty() || !columns.containsKey(columnName)) {
            return new ColumnRpc(columnName);
        }
        return new ColumnRpc(columns.get(columnName));
    }


    public String getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public EdsKpiWidgetFilter getNew(EdsKpiWidgetFilter kpiWidgetFilter) {
        kpiWidgetFilter = kpiWidgetFilter != null ? kpiWidgetFilter : new EdsKpiWidgetFilter();
        kpiWidgetFilter.arraybinds = arraybinds;
        kpiWidgetFilter.arraycolumns = arraycolumns;
        kpiWidgetFilter.arraycomparators = arraycomparators;
        kpiWidgetFilter.arrayvalues = arrayvalues;
        kpiWidgetFilter.arrayoperator = arrayoperator;
        kpiWidgetFilter.filterPattern = filterPattern;
        kpiWidgetFilter.filterType = filterType;
        return kpiWidgetFilter;
    }

    public Integer getLastYearIndex() {
        return lastYearIndex;
    }

    public void setLastYearIndex(Integer lastYearIndex) {
        this.lastYearIndex = lastYearIndex;
    }
}
