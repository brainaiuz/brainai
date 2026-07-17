package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 5/16/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public final class RelatedFilterOptions implements IsSerializable {

    private ArrayList<Integer> sets;
    private LinkedList<ColumnRpc> fields;
    private ArrayList<String> operators;
    private ArrayList<String> values;
    private ArrayList<String> boolTypes;

    public void setSets(ArrayList<Integer> sets) {
        this.sets = sets;
    }

    public ArrayList<Integer> getSets() {
        if (sets == null) {
            sets = new ArrayList<>();
        }
        return this.sets;
    }

    public void setFields(LinkedList<ColumnRpc> fields) {
        this.fields = fields;
    }

    public LinkedList<ColumnRpc> getFields() {
        if (fields == null) {
            fields = new LinkedList<>();
        }
        return this.fields;
    }

    public void setOperators(ArrayList<String> operators) {
        this.operators = operators;
    }

    public ArrayList<String> getOperators() {
        if (operators == null) {
            operators = new ArrayList<>();
        }
        return this.operators;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getValues() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return this.values;
    }

    public void setBoolTypes(ArrayList<String> boolTypes) {
        this.boolTypes = boolTypes;
    }

    public ArrayList<String> getBoolTypes() {
        if (boolTypes == null) {
            boolTypes = new ArrayList<>();
        }
        return boolTypes;
    }
}
