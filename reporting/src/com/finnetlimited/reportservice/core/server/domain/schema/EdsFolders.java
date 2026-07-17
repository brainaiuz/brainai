package com.finnetlimited.reportservice.core.server.domain.schema;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 15:45:24
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "folders")
public class EdsFolders extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Index(name = "folders_index_code")
    @Column(unique = true)
    private String code;

    private String name;

    private String type;

    @Column(name = "domainName")
    private String domainName;

    @Column(name = "companyid")
    private Integer companyid;

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "createdate")
    private Date date;

    @Column(name = "showhide")
    private Boolean show = true;

    @Column(name = "deleted")
    private Boolean deleted;

    @Embedded
    private EdsAuditInfo auditInfo;

    @Index(name = "folders_index_categorycode")
    private String categoryCode;


    @OneToMany(mappedBy = "folderid", fetch = FetchType.LAZY)
    @OrderBy(value = "objectID")
    @ForeignKey(name = "none")
    private List<EdsReport> reports = new ArrayList<>();

    private String description;

    private Integer sorder;

    private String icon;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (getObjectID() == null) {
            code = name.replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase();
        }
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean isShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo = auditInfo == null ? new EdsAuditInfo() : auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public List<EdsReport> getReports() {
        return reports;
    }

    public void setReports(List<EdsReport> reports) {
        this.reports = reports;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public FolderRpc toRpc() {
        FolderRpc rpc = new FolderRpc();
        rpc.setId(getObjectID());
        rpc.setName(getName());
        rpc.setCode(getCode());
        rpc.setDescription(getDescription());
        rpc.setType(getType());
        if (getAuditInfo().getCreatedBy() != null) {
            rpc.setCreatedBy(getAuditInfo().getCreatedBy().getName());
        }
        if (getAuditInfo().getCreationDate() != null) {
            rpc.setCreatedDate(getAuditInfo().getCreationDate());
        }
        rpc.setCategoryCode(getCategoryCode());
        rpc.setIcon(getIcon());
        return rpc;
    }

    public EdsFolders getNew(EdsFolders folder) {
        folder = folder == null ? new EdsFolders() : folder;
        folder.setName(getName());
        folder.getAuditInfo().setCreationDate(getAuditInfo().getCreationDate());
        folder.getAuditInfo().setModificationDate(new Date());
        folder.setCategoryCode(getCategoryCode());
        folder.setDeleted(getDeleted());
        folder.setDate(getDate());
        folder.setDescription(getDescription());
        folder.setDomainName(getDomainName());
        folder.setShow(isShow());
        folder.setType(getType());
        folder.setSorder(getSorder());
        folder.setIcon(getIcon());
        return folder;
    }
}
