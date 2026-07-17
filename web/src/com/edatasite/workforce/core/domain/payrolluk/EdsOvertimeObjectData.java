package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObjectData;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "overtime_object_data")
public class EdsOvertimeObjectData extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "date")
    private Date date;

    @Column(name = "overtimeHours")
    private BigDecimal overtimeHours;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private EdsPayrollCategory category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overtimeObjectId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsOvertimeObject overtimeObject;

    public OvertimeObjectData toRpc(boolean withOvertimeObject) {
        OvertimeObjectData objectData = new OvertimeObjectData();
        objectData.setId(getObjectID());
        objectData.setCategory(getCategory().getAsSelectItem());
        objectData.setOvertimeHours(getOvertimeHours());
        objectData.setDate(new DateNonConvertable(getDate()));
        objectData.setEmployee(getEmployee().getAsSelectItem());
        if (withOvertimeObject) {
            objectData.setOvertimeObject(getOvertimeObject().toRpc(false));
        }
        return objectData;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        this.category = category;
    }

    public EdsOvertimeObject getOvertimeObject() {
        return overtimeObject;
    }

    public void setOvertimeObject(EdsOvertimeObject overtimeObject) {
        this.overtimeObject = overtimeObject;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }
}
