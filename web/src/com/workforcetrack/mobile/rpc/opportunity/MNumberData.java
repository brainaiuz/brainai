package com.workforcetrack.mobile.rpc.opportunity;

import com.edatasite.workforce.gwt.core.client.rpc.NumberData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/19/11
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MNumberData  {
    private String numberString;
    private Integer intNumber;
    private String numberFormat;

    public MNumberData(){

    }

    public MNumberData(NumberData numberData){
        this.numberString = numberData.getNumberString();
        this.intNumber = numberData.getIntNumber();
        this.numberFormat = numberData.getNumberFormat();
    }

    public MNumberData(String numberString, Integer intNumber){
        this.numberString = numberString;
        this.intNumber = intNumber;
    }


    public NumberData convertToNumberData(NumberData numberData){
        if(numberData == null){
            numberData = new NumberData();
        }
        numberData.setNumberString(this.numberString);
        numberData.setIntNumber(this.intNumber);
        numberData.setNumberFormat(this.numberFormat);

        return numberData;
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
}

