package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "approverHistory")
public class EdsApprovableHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realApprover")
    private EdsUser realApprover;

    @Column(name = "approveDate")
    private Date approveDate;

    private String entityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    public void setRealApprover(EdsUser realApprover) {
        this.realApprover = realApprover;
    }


    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getApproveDate() {
        return approveDate;
    }
}
