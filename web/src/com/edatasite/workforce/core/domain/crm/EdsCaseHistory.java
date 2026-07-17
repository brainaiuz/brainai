package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 1, 2010
 * Time: 7:08:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "caseHistory")
public class EdsCaseHistory extends EdsHistory implements CrmHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseid")
    private EdsCase crmCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "message")
    @Type(type = "text")
    private String message;

    @Override
    public Integer getEntityID() {
        return getCrmCase() != null ? getCrmCase().getObjectID() : null;
    }

    public EdsCase getCrmCase() {
        return crmCase;
    }

    public void setCrmCase(EdsCase crmCase) {
        this.crmCase = crmCase;
    }

    @Override
    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
