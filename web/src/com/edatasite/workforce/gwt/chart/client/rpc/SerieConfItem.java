package com.edatasite.workforce.gwt.chart.client.rpc;

import com.edatasite.workforce.gwt.chart.client.enums.SerieAggrTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.LinkedList;

public class SerieConfItem implements IsSerializable, Serializable {

    /**
     * serie name, it is a Y axis value
     */
    private String serieName;

    /**
     * serie column config
     */
    private SerieColumn serieColumn;
    /**
     * aggregation funtion type {sum, count, avg, max., min.}
     * <p>
     * Exm: You have selected column lets say t.paidAmout then you can select sum, max, avg function for aggrType field
     */
    private SerieAggrTypeEnum aggrType;

    private LinkedList<ColumnColor> colorList;

    private boolean isUnique;

    public SerieConfItem() {
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public SerieColumn getSerieColumn() {
        return serieColumn;
    }

    public void setSerieColumn(SerieColumn column) {
        this.serieColumn = column;
    }

    public String getAlias() {

        if (aggrType == null) {
            return serieColumn.getColumn();
        }

        return serieColumn.getColumn() + "_" + aggrType.getFunction();
    }

    public SerieAggrTypeEnum getAggrType() {
        return aggrType;
    }

    public void setAggrType(SerieAggrTypeEnum aggrType) {
        this.aggrType = aggrType;
    }

    public boolean getUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public LinkedList<ColumnColor> getColorList() {
        return colorList;
    }

    public void setColorList(LinkedList<ColumnColor> colorList) {
        this.colorList = colorList;
    }
}
