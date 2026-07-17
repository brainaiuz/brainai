package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.core.domain.settings.EdsChanges;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "labour_period")
public class EdsLabourPeriod extends EdsTraceable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date startDate;
    private Date endDate;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "allowance")
    private Double allowance;

    @Column(name = "out_of_system_days")
    private Double outOfSystemDays;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeid")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modified_by")
    private EdsUser modifiedBy;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "period_history")
    private Set<EdsChanges> periodHistory = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "labourPeriod", fetch = FetchType.LAZY)
    @OrderBy(value = "creationDate DESC")
    private final List<EdsLaborPeriodHistory> historyList = new ArrayList<>();

    /// bu column 14 kundan kup Б/С reasonli leave request olsa allowance dan 1 ayirib tashlash uchun
    @Column(name = "actualAllowanceDays", columnDefinition = "Decimal(10,2) default 0.00")
    private Double actualAllowanceDays = 0d;

    public Double getOutOfSystemDays() {
        return outOfSystemDays;
    }

    public void setOutOfSystemDays(Double outOfSystemDays) {
        if (!ServerUtils.equalsDouble(this.outOfSystemDays, outOfSystemDays)) {
            addHistoryChange("OutOfSystemDays", this.outOfSystemDays, outOfSystemDays);
        }
        this.outOfSystemDays = outOfSystemDays;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        if (!ServerUtils.equalsDouble(this.allowance, allowance)) {
            addHistoryChange("Allowance", this.allowance, allowance);
        }
        this.allowance = allowance;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public EdsUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(EdsUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Double getActualAllowanceDays() {
        return actualAllowanceDays;
    }

    public void setActualAllowanceDays(Double actualAllowanceDays) {
        this.actualAllowanceDays = actualAllowanceDays;
    }

    public LaborPeriodRequest toRpc() {
        LaborPeriodRequest rpc = new LaborPeriodRequest();
        rpc.setObjectID(getObjectID());
        if (employee != null) {
            rpc.setEmployeeID(getEmployee().getObjectID());
        }
        rpc.setStartDate(getStartDate());
        rpc.setEndDate(getEndDate());
        rpc.setLeavePeriodCreatedDate(getCreatedDate());
        rpc.setOutOfSystemDays(getOutOfSystemDays() != null ? getOutOfSystemDays() : 0d);
        return rpc;
    }

    public Set<EdsChanges> getPeriodHistory() {
        return periodHistory;
    }

    public void setPeriodHistory(Set<EdsChanges> periodHistory) {
        this.periodHistory = periodHistory;
    }

    public List<EdsLaborPeriodHistory> getHistoryList() {
        return historyList;
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            EdsChanges change = new EdsChanges();
            change.setField(field);
            change.setHistoryType(HistoryType.LABOR_PERIOD);
            change.setEntityID(getModifiedBy() != null ? getModifiedBy().getObjectID() : ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID());
            change.setEntityName("labour_period");
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setModificationDate(new Date());
            change.setUpdater((EdsUser) SecurityContext.getInstance().getUser());
            change.setSuperUser(ServerUtils.isSuperUser());
            getPeriodHistory().add(change);
            addChange(field);
        }
    }
}
