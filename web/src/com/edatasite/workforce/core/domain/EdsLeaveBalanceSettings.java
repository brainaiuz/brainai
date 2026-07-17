package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveBalanceSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "leavebalancesettings")
public class EdsLeaveBalanceSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private BigDecimal daysAmount;
    private Integer fromDays;
    private Integer toDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leavetypeid")
    private EdsReference leaveType;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsReference getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(EdsReference leaveType) {
        this.leaveType = leaveType;
    }

    public BigDecimal getDaysAmount() {
        return daysAmount;
    }

    public void setDaysAmount(BigDecimal daysAmount) {
        this.daysAmount = daysAmount;
    }

    public Integer getFromDays() {
        return fromDays;
    }

    public void setFromDays(Integer fromDays) {
        this.fromDays = fromDays;
    }

    public Integer getToDays() {
        return toDays;
    }

    public void setToDays(Integer toDays) {
        this.toDays = toDays;
    }

    public LeaveBalanceSettings getRPC() {
        LeaveBalanceSettings result = new LeaveBalanceSettings();
        result.setId(getObjectID());
        result.setDays(getDaysAmount());
        result.setFrom(getFromDays());
        result.setTo(getToDays());
        result.setLeaveReason(getLeaveType().getAsSelectItem());
        return result;
    }

}
