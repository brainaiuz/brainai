package com.edatasite.workforce.gwt.chart.client.rpc;


import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

public class KpiWidgetFilterItem implements IsSerializable {

    private Integer id;

    private LinkedList<ColumnRpc> fieldd;

    private ArrayList<Integer> sett;

    private ArrayList<String> operators;

    private ArrayList<String> values;

    private ArrayList<String> boolType;

    private String filterPattern;

    private Integer filterType;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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

    public ArrayList<Integer> getSett() {
        if (sett == null) {
            sett = new ArrayList<>();
        }
        return sett;
    }

    public void setSett(ArrayList<Integer> sett) {
        this.sett = sett;
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

    public void addOperator(int index, String operator) {
        getOperators().add(index, operator);
    }

    public void addOperator(String operator) {
        getOperators().add(operator);
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

    public void addToBoolType(String bool) {
        if (boolType == null) {
            boolType = new ArrayList<>();
        }
        boolType.add(bool);
    }

    public void clearBoolType() {
        getBoolType().clear();
    }


    public String getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    public void addFilter(Integer index, ColumnRpc fieldd, String operator, String value, Integer sett) {
        if (index != null) {
            this.fieldd.set(index, fieldd);
            this.operators.set(index, operator);
            this.values.set(index, value);
            this.sett.set(index, sett);
        } else {
            this.fieldd.add(fieldd);
            this.operators.add(operator);
            this.values.add(value);
            this.sett.add(sett);

        }
    }
}
