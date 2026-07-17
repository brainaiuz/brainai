package com.edatasite.workforce.core.domain.certificate;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Khasan on 11.09.14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificateofemploymenttype")
public class EdsCertificateOfEmploymentType extends EdsObject {

    public static final String CERTIFICATE_TEMPLATE_TYPE = "CERTIFICATE_TEMPLATE_TYPE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Column(name = "formID")
    private String formID;

    @Column(name = "defaulthtml")
    @Type(type = "text")
    private String defaultHTML;

    @Column(name = "customhtml")
    @Type(type = "text")
    private String customHTML;

    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatirid")
    private EdsUser createrBy;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @Column(name = "headerfooter")
    private Boolean headerFooter= Boolean.FALSE;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getDefaultHTML() {
        return defaultHTML;
    }

    public void setDefaultHTML(String defaultHTML) {
        this.defaultHTML = defaultHTML;
    }

    public String getCustomHTML() {
        return customHTML;
    }

    public void setCustomHTML(String customHTML) {
        this.customHTML = customHTML;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsUser getCreaterBy() {
        return createrBy;
    }

    public void setCreaterBy(EdsUser createrBy) {
        this.createrBy = createrBy;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public Boolean getHeaderFooter() {
        return headerFooter != null ? headerFooter : Boolean.FALSE;
    }

    public void setHeaderFooter(Boolean headerFooter) {
        this.headerFooter = headerFooter;
    }

    public CertificateItem createCertificateTypeData() {
        CertificateItem certificateItem = new CertificateItem();
        certificateItem.setObjectId(getObjectID());
        certificateItem.setName(getName());
        certificateItem.setFormID(getFormID());
        certificateItem.setDescription(getDescription());
        certificateItem.setCreationDate(getCreationDate());
        certificateItem.setType(getType() != null ? getType().getRPC() : null);
        certificateItem.setPdfHeaderFooter(getHeaderFooter());
        certificateItem.setUpdatedDate(getCreationDate());
        if (getCreaterBy() != null) {
            certificateItem.setUpdatedBy(new SelectItem(getCreaterBy().getObjectID(), getCreaterBy().getFullName()));
        }
        certificateItem.setContent(getDefaultHTML());
        certificateItem.setCustomHTMLcontent(getCustomHTML());
        return certificateItem;
    }
}
