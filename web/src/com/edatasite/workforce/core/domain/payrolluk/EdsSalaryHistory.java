package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;

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
import java.util.Date;

import static com.edatasite.shared.db.EdsScope.PRIVATE_SCHEMA;

@Entity
@Table(schema = PRIVATE_SCHEMA, name = "salaryhistory")
public class EdsSalaryHistory extends EdsObject {

    public static final String TYPE_MROT = "TYPE_MROT";
    public static final String TYPE_ROTATION = "TYPE_ROTATION";
    public static final String TYPE_PROFILE = "TYPE_PROFILE";
    public static final String TYPE_GRADE = "TYPE_GRADE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeID")
    private EdsEmployee employee;

    private Date effectiveDate;

    @Column(name = "salary", precision = 14, scale = 4)
    private BigDecimal salary;

    private String relationType;
    private Integer relationId;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return deleted != null && deleted;
    }

    public SalaryHistory getRPC() {
        SalaryHistory history = new SalaryHistory();
        history.setEmployeeId(getEmployee().getObjectID());
        history.setSalary(getSalary());
        history.setEffectiveDate(new DateNonConvertable(getEffectiveDate()));
        history.setRelationId(getRelationId());
        history.setRelationType(getRelationType());
        return history;
    }
}
