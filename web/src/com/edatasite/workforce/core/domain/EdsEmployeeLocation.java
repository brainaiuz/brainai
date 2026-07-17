package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeLocationItem;

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
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:04:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeelocation")
public class EdsEmployeeLocation extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid")
    private EdsLocation location;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Date startDate;
    private Date endDate;

    public EmployeeLocationItem getRPC() {
        EmployeeLocationItem item = new EmployeeLocationItem();
        item.setId(getObjectID());
        item.setLocation(getLocation().getAsSelectItem());
        item.setEmployee(getUser().getAsSelectItem());
        item.setStartDate(getStartDate() != null ? new DateNonConvertable(getStartDate()) : null);
        item.setEndDate(getEndDate() != null ? new DateNonConvertable(getEndDate()) : null);
        return item;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
