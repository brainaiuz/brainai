package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 18.11.2010
 * Time: 17:43:41
 * To change this template use File | Settings | File Templates.
 */
public class NumberData implements Serializable {
    private String numberString;
    private String firstNumberString;
    private String lastNumberString;
    private Integer intNumber;
    private String savedNumberFormula;
    private String numberFormat;
    private String delimiter;

    public NumberData() {
    }

    public NumberData(String numberString) {
        this(numberString, Integer.parseInt(numberString.replaceAll("\\D", "")));
    }

    public NumberData(String numberString, Integer intNumber) {
        this.numberString = numberString;
        this.intNumber = intNumber;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getSavedNumberFormula() {
        return savedNumberFormula;
    }

    public void setSavedNumberFormula(String savedNumberFormula) {
        this.savedNumberFormula = savedNumberFormula;
    }

    public String getLastNumberString() {
        return lastNumberString;
    }

    public void setLastNumberString(String lastNumberString) {
        this.lastNumberString = lastNumberString;
    }

    public String getFirstNumberString() {
        return firstNumberString;
    }

    public void setFirstNumberString(String firstNumberString) {
        this.firstNumberString = firstNumberString;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
