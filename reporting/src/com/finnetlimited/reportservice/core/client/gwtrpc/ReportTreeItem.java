package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 29, 2011
 * Time: 8:08:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportTreeItem implements IsSerializable {

    private ArrayList<String> cells;
    private int depth;
    private Boolean isSummaryRow;
    private ArrayList<ReportTreeItem> childs;
    private int countOfAllChilds;

    public ReportTreeItem() {
    }

    public ReportTreeItem(Integer countOfCells) {
        cells = new ArrayList<>();
        for (int i = 0; i < countOfCells; i++) {
            cells.add("");
        }
    }

    public ArrayList<String> getCells() {
        return cells;
    }

    public void setCells(ArrayList<String> cells) {
        this.cells = cells;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Boolean getSummaryRow() {
        return isSummaryRow;
    }

    public void setSummaryRow(Boolean summaryRow) {
        isSummaryRow = summaryRow;
    }

    public ArrayList<ReportTreeItem> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ReportTreeItem> childs) {
        this.childs = childs;
    }

    public int getCountOfAllChilds() {
        return countOfAllChilds;
    }

    public void setCountOfAllChilds(int countOfAllChilds) {
        this.countOfAllChilds = countOfAllChilds;
    }
}
