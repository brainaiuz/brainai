package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 21/03/12
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class FacetSettingRpc implements IsSerializable {
    public static final String FACET_DATE_PERIOD = "facetDatePeriod";
    public static final String FACET_QUANTITY_RANGE = "facetQuantityRange";
    private int cell;
    private int row;
    private int originalRow;

    public FacetSettingRpc() {
    }

    public FacetSettingRpc(int row, int cell) {
        this.row = row;
        this.cell = cell;
        this.originalRow = row;
    }

    public int getCell() {
        return cell > 2 ? 2 : cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getOriginalRow() {
        return originalRow;
    }

    public void setOriginalRow(int originalRow) {
        this.originalRow = originalRow;
    }
}
