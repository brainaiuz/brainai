package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsSolution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 14, 2009
 * Time: 12:03:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmCaseSolution")
public class EdsCaseSolution extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmCaseId")
    private EdsCase crmCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solutionId")
    private EdsSolution solution;

    @Column(name = "isdeleted")
    private Boolean deleted = false;

    public EdsCaseSolution() {

    }

    public EdsCaseSolution(EdsCase crmCase, EdsSolution solution) {
        this.crmCase = crmCase;
        this.solution = solution;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCase getCrmCase() {
        return crmCase;
    }

    public void setCrmCase(EdsCase crmCase) {
        this.crmCase = crmCase;
    }

    public EdsSolution getSolution() {
        return solution;
    }

    public void setSolution(EdsSolution solution) {
        this.solution = solution;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
