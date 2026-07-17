package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 04.03.2009
 * Time: 18:16:03
 * To change this template use File | Settings | File Templates.
 */
public class NITaxObject extends PaymentDeductionObject {

    private BigDecimal employeeNI;
    private BigDecimal employerNI;
    private BigDecimal totalNI;

    private BigDecimal rebateEE;
    private BigDecimal rebateER;

    private BigDecimal toLEL;//1a
    private BigDecimal LELtoET;//1b
    private BigDecimal ETtoST;//1c
    private BigDecimal STtoUAP;//1c
    private BigDecimal UAPtoUEL;//1d
    private BigDecimal aboveUEL;

    private String niTableLetter;

    public NITaxObject() {
    }

    public NITaxObject(BigDecimal employeeNI, BigDecimal employerNI) {
        this.employeeNI = employeeNI;
        this.employerNI = employerNI;
    }

    public BigDecimal getEmployeeNI() {
        return employeeNI;
    }

    public void setEmployeeNI(BigDecimal employeeNI) {
        this.employeeNI = employeeNI;
    }

    public BigDecimal getEmployerNI() {
        return employerNI;
    }

    public void setEmployerNI(BigDecimal employerNI) {
        this.employerNI = employerNI;
    }

    public String getNiTableLetter() {
        return niTableLetter;
    }

    public void setNiTableLetter(String niTableLetter) {
        this.niTableLetter = niTableLetter;
    }

    public BigDecimal getToLEL() {
        return toLEL;
    }

    public void setToLEL(BigDecimal toLEL) {
        this.toLEL = toLEL;
    }

    public BigDecimal getLELtoET() {
        return LELtoET;
    }

    public void setLELtoET(BigDecimal LELtoET) {
        this.LELtoET = LELtoET;
    }

    public BigDecimal getETtoST() {
        return ETtoST;
    }

    public void setETtoST(BigDecimal ETtoST) {
        this.ETtoST = ETtoST;
    }

    public BigDecimal getSTtoUAP() {
        return STtoUAP;
    }

    public void setSTtoUAP(BigDecimal STtoUAP) {
        this.STtoUAP = STtoUAP;
    }

    public BigDecimal getUAPtoUEL() {
        return UAPtoUEL;
    }

    public void setUAPtoUEL(BigDecimal UAPtoUEL) {
        this.UAPtoUEL = UAPtoUEL;
    }

    public BigDecimal getAboveUEL() {
        return aboveUEL;
    }

    public void setAboveUEL(BigDecimal aboveUEL) {
        this.aboveUEL = aboveUEL;
    }

    public BigDecimal getRebateEE() {
        return rebateEE;
    }

    public void setRebateEE(BigDecimal rebateEE) {
        this.rebateEE = rebateEE;
    }

    public BigDecimal getRebateER() {
        return rebateER;
    }

    public void setRebateER(BigDecimal rebateER) {
        this.rebateER = rebateER;
    }

    public BigDecimal getTotalNI() {
        return totalNI;
    }

    public void setTotalNI(BigDecimal totalNI) {
        this.totalNI = totalNI;
    }
}
