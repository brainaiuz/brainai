/*
package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Where;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 20.10.11
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 *//*


@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "roleReportTemplate")
public class EdsRoleReportTemplate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Index(name="rolereporttemplate_index_reportTemplateCode")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reportTemplateCode", referencedColumnName = "code")
    private EdsReportTemplate reportTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyID", nullable = true)
    private EdsCompany company;

    private String role;
    private Integer deny;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(EdsReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }

    public Integer isDeny() {
        return deny;
    }

    public void setDeny(Integer deny) {
        this.deny = deny;
    }
}*/
