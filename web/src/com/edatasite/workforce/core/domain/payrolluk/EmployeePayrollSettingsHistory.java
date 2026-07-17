package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 1:51:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeepayrollsettingshistory")
public class EmployeePayrollSettingsHistory extends EdsObject {

    public static final String HISTORY_STATUS = "EMP_PAYROLL_SETTINGS_HISTORY_STATUS";
    public static final String NINO_CHANGED = "NINO_CHANGED";
    public static final String NICATEGORY_CHANGED = "NICATEGORY_CHANGED";
    public static final String TAXCODE_CHANGED = "TAXCODE_CHANGED";

    public static final String METHOD_OF_CHANGE = "EMP_PAYROLL_SETTINGS_METHODOFCHANGE";
    public static final String SYSTEM_CREATED = "SYSTEM_CREATED";
    public static final String USER_MODIFIED = "USER_MODIFIED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne
    @JoinColumn(name = "employeepayrollsettings")
    private EdsEmployeePayrollSettings employeePayrollSettings;

    @ManyToOne
    @JoinColumn(name = "status")
    private EdsReference status;

    @ManyToOne
    @JoinColumn(name = "methodOfChange")
    private EdsReference methodOfChange;

    private String oldValue;
    private String newValue;

    private Date date;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployeePayrollSettings getEmployeePayrollSettings() {
        return employeePayrollSettings;
    }

    public void setEmployeePayrollSettings(EdsEmployeePayrollSettings employeePayrollSettings) {
        this.employeePayrollSettings = employeePayrollSettings;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsReference getMethodOfChange() {
        return methodOfChange;
    }

    public void setMethodOfChange(EdsReference methodOfChange) {
        this.methodOfChange = methodOfChange;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
