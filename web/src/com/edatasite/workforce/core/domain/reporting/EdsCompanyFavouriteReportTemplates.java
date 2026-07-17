package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;

import javax.persistence.CascadeType;
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
 * User: Power
 * Date: 4/14/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companyFavouriteReportTemplates")
public class EdsCompanyFavouriteReportTemplates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private EdsUser user;
    @Column(insertable = false, updatable = false)
    private Integer userid;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "reportingid")
    private EdsReport reporting;

    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(Integer objectid) {
        this.objectID = objectid;
    }

    public EdsUser getUserId() {
        return this.user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsReport getReporting() {
        return this.reporting;
    }

    public void setReporting(EdsReport reporting) {
        this.reporting = reporting;
    }
}
