package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Nov 2, 2009
 * Time: 6:28:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiTaxChangesListItem implements IsSerializable {

    public static String METHODOFCHANGE= "methodOfChange";
    public static String DATE= "date";
    public static String OLDCODE= "oldcode";
    public static String NEWCODE= "newcode";
    public static String EMPLOYEENAME= "employeeName";

    private Integer objectId;
    private String employeeName;
    private String oldCode;
    private String newCode;
    private Date date;
    private String methodOfChange;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMethodOfChange() {
        return methodOfChange;
    }

    public void setMethodOfChange(String methodOfChange) {
        this.methodOfChange = methodOfChange;
    }
}
