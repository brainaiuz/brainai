package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 3/28/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslipemployeebonus")
public class EdsPayslipEmployeeBonus extends EdsSimpleRate {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private EdsEmployee employee;

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }
}
