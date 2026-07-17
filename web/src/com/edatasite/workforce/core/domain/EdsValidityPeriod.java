package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Sher
 * Date: 7/26/12
 * Time: 5:48 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "validity_period")
public class EdsValidityPeriod extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    /**
     * The validity period name.
     */
    private String name;
    /**
     * The validity period description.
     */
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date fromDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date toDate;

    /**
     * Is this validityPeriod temporarily deleted?
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "validity_period_reference",
            joinColumns = {@JoinColumn(name = "validity_period_id")},
            inverseJoinColumns = {@JoinColumn(name = "periodtypeitems_id")})
    private Set<EdsReference> periodTypeItems = new HashSet<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EdsReference> getPeriodTypeItems() {
        return periodTypeItems;
    }

    public void setPeriodTypeItems(Set<EdsReference> periodTypeItems) {
        this.periodTypeItems = periodTypeItems;
    }

    public ValidityPeriodItem getDTO() {

        ValidityPeriodItem item = new ValidityPeriodItem();
        item.setId(getObjectID());
        String name = null;
        if (StringUtils.isEmpty(getName()) || StringUtils.isEmpty(getDescription())) {
            EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();
            String datePattern = ServerUtils.getShortDateFormat(user);
            name = ServerUtils.dateFormat(getFromDate(), datePattern) + " - " + ServerUtils.dateFormat(getToDate(), datePattern);
        }
        if (StringUtils.isEmpty(getName())) {
            item.setName(name);
        } else {
            item.setName(getName());
        }
        if (StringUtils.isEmpty(getDescription())) {
            item.setDescription(name);
        } else {
            item.setDescription(getDescription());
        }

        item.setFromDate(getFromDate());
        item.setToDate(getToDate());
        for (EdsReference periodType : getPeriodTypeItems()) {
            item.getPeriodTypeItems().add(periodType.getAsSelectItem());
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= getFromDate().getTime() && currentTime <= getToDate().getTime()) {
            item.setDefault(true);
        }

        return item;
    }

    public SelectItem getAsSelectItem(EdsUser user) {
        if (user == null) {
            user = (EdsUser) SecurityContext.getInstance().getUser();
        }
        String name = ServerUtils.shortDateFormat(getFromDate(), user, true) + " - " + ServerUtils.shortDateFormat(getToDate(), user, true);
        return new SelectItem(getObjectID(), name, getDescription());
    }
}
