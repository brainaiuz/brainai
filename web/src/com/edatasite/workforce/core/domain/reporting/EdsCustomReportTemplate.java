package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Virus on 3/25/2014.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "customReportTemplate")
public class EdsCustomReportTemplate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumns(value = {@JoinColumn(name = "reportCode", referencedColumnName = "code")}, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EdsReportTemplate reportTemplate;

    private Integer companyId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public EdsReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(EdsReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
