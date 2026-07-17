package com.edatasite.workforce.core.domain.analyzer;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Oct 15, 2010
 * Time: 2:31:08 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "solrdbconsistency")
public class EdsSolrDbConsistency extends EdsObject {
    public static final String TASK = "TASK";
    public static final String PROJECT = "PROJECT";
    public static final String NEWS = "NEWS";
    public static final String CONTACT = "CONTACT";
    public static final String CRM_ACCOUNT = "CRM_ACCOUNT";
    public static final String CASE = "CASE";
    public static final String LEAD = "LEAD";
    public static final String CANDIDATE = "CANDIDATE";
    public static final String NETWORK = "NETWORK";
    public static final String NETWORK_NEWS = "NETWORK NEWS";
    public static final String FILE = "FILE";
    public static final String INVOICE = "INVOICE";
    public static final String QUOTE = "QUOTE";
    public static final String PURCHASE_ORDER = "PURCHASE_ORDER";
    public static final String OPPORTUNITY = "OPPORTUNITY";
    public static final String EVENT = "EVENT";
    public static final String PRODUCTS_SERVICES = "PRODUCTS_SERVICES";
    public static final String PURCHASE_INVOICE = "PURCHASE_INVOICE";
    public static final String EXPENSE_REPORT_CLAIMS = "EXPENSE_REPORT_CLAIMS";
    public static final String COURSE_BOOKING = "COURSE_BOOKING";
    public static final String COURSE_SCHEDULE = "COURSE_SCHEDULE";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String SINGLE_PAYRUN = "SINGLE_PAYRUN";
    public static final String GROUP_PAYRUN = "GROUP_PAYRUN";
    public static final String CASH_ADVANCE = "CASH_ADVANCE";
    public static final String ADDITIONAL_PAYMENT = "ADDITIONAL_PAYMENT";
    public static final String VACANCY = "VACANCY";
    public static final String EMPLOYEE_STEP = "EMPLOYEE_STEP";
    public static final String CHART_OF_ACCOUNT = "CHART_OF_ACCOUNT";
    public static final String LEAVE_REQUEST = "LEAVE_REQUEST";
    public static final String CUSTOM_FORM = "CUSTOM_FORM";
    public static final String SHIPPING_DATA = "SHIPPING_DATA";
    public static final String RFQ = "RFQ";
    public static final String CERTIFICATE = "CERTIFICATE";
    public static final String POSITION = "POSITION";
    public static final String DEPARTMENT = "DEPARTMENT";

    public static final String STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB = "Exist in Solr but missed in DB";
    public static final String STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR = "Exist in DB but missed in Solr";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String entityName;

    private Integer entityID;

    private String status;

    private String entityType;

    private Integer companyid;

    private String companyName;

    private Date analizedate;

    private boolean fixed = false;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getAnalizedate() {
        return analizedate;
    }

    public void setAnalizedate(Date analizedate) {
        this.analizedate = analizedate;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
