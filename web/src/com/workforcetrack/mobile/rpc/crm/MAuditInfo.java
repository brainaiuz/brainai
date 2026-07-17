package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.07.12
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MAuditInfo {

    private Date creationDate;
    private Date modificationDate;
    private String createdBy;
    private String modifiedBy;

    private String status;

    public MAuditInfo() {
    }

    public static MAuditInfo convertToMobileCaseInfo(CaseItem item) {
        if (item == null) {
            return null;
        }
        MAuditInfo auditInfo = new MAuditInfo();
        if (item.getAuditInfoResource() != null) {
            auditInfo.setCreationDate(item.getAuditInfoResource().getCreationDate());
            auditInfo.setCreatedBy(item.getAuditInfoResource().getCreatedBy() != null ? item.getAuditInfoResource().getCreatedBy().getFullName() : null);
            auditInfo.setModifiedBy(item.getAuditInfoResource().getModifiedBy() != null ? item.getAuditInfoResource().getModifiedBy().getFullName() : null);
            auditInfo.setModificationDate(item.getAuditInfoResource().getModificationDate());
        }
        auditInfo.setStatus(item.getStatus() != null ? item.getStatus().getName() : null);


        return auditInfo;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
