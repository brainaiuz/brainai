package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: faxriddin Taslimov Date: 27.08.2015
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contracttotalchargeformula")
public class EdsContractTotalChargeFormula extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "formula_byHour")
    private String formula_byHour;

    @Column(name = "formula_byMonth")
    private String formula_byMonth;

    @Column(name = "formula_byMarkup")
    private String formula_byMarkup;

    @Column(name = "formula_byMarkupAndOT")
    private String formula_byMarkupAndOT;

    @Column(name = "formula_byDayAndOT")
    private String formula_byDayAndOT;

    @Column(name = "formula_byDayAndOTSpec")
    private String formula_byDayAndOTSpec;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFormula_byHour() {
        return formula_byHour;
    }

    public void setFormula_byHour(String formula_byHour) {
        this.formula_byHour = formula_byHour;
    }

    public String getFormula_byMonth() {
        return formula_byMonth;
    }

    public void setFormula_byMonth(String formula_byMonth) {
        this.formula_byMonth = formula_byMonth;
    }

    public String getFormula_byMarkup() {
        return formula_byMarkup;
    }

    public void setFormula_byMarkup(String formula_byMarkup) {
        this.formula_byMarkup = formula_byMarkup;
    }

    public String getFormula_byMarkupAndOT() {
        return formula_byMarkupAndOT;
    }

    public void setFormula_byMarkupAndOT(String formula_byMarkupAndOT) {
        this.formula_byMarkupAndOT = formula_byMarkupAndOT;
    }

    public String getFormula_byDayAndOT() {
        return formula_byDayAndOT;
    }

    public void setFormula_byDayAndOT(String formula_byDayAndOT) {
        this.formula_byDayAndOT = formula_byDayAndOT;
    }

    public String getFormula_byDayAndOTSpec() {
        return formula_byDayAndOTSpec;
    }

    public void setFormula_byDayAndOTSpec(String formula_byDayAndOTSpec) {
        this.formula_byDayAndOTSpec = formula_byDayAndOTSpec;
    }
}
