package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sahrarov
 * Date: 05.03.2009
 * Time: 14:40:32
 * To change this template use File | Settings | File Templates.
 */
public class TaskTimeEntriesItem implements IsSerializable, Key {

    public static final String EMPLOYEE_CODE = "employee_code";
    public static final String EMPLOYEE = "employee";
    public static final String EMPLOYEE_COMMENT = "employee_comment";
    public static final String MANAGER_COMMENT = "manager_comment";
    public static final String TIMESPENT = "timespent";
    public static final String HOURS_TYPE = "hours_type";
    public static final String DATE = "date";
    public static final String STATUS = "status";

    private String taskName;
    private String emloyee;
    private String emloyeeCode;
    private Integer employeeId;
    private String comment;
    private String managerComment;
    private Integer objectID;
	private String status;
	private DateNonConvertable date;
	private Integer timeSpent;
    private String hourType;
    private BigDecimal rate;
    private BigDecimal discount;
    private Date entryDate;
    private Integer taskId;
    private String invoiceID;
    private String invoiceNumber;
    private boolean billable;
    private boolean fixed;

    public TaskTimeEntriesItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public TaskTimeEntriesItem(String employee, String comment, Integer timeSpent, DateNonConvertable date) {
        this.emloyee = employee;
        this.comment = comment;
		this.timeSpent = timeSpent;
		this.date = date;
    }


    public TaskTimeEntriesItem(String employee, String comment, Integer timeSpent) {
        this.emloyee = employee;
        this.comment = comment;
		this.timeSpent = timeSpent;
    }

    public String getEmloyee() {
        return emloyee;
    }

    public void setEmloyee(String emloyee) {
        this.emloyee = emloyee;
    }

    public String getEmloyeeCode() {
        return emloyeeCode;
    }

    public void setEmloyeeCode(String emloyeeCode) {
        this.emloyeeCode = emloyeeCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DateNonConvertable getDate() {
		return date;
	}

	public void setDate(DateNonConvertable date) {
		this.date = date;
	}

	public Integer getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Integer timeSpent) {
		this.timeSpent = timeSpent;
	}

    public String getHourType() {
        return hourType;
    }

    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    @Override
    public String getKey() {
        return getObjectID() + "_" + getHourType();
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDiscount() {
        return discount != null ? discount : BigDecimal.valueOf(0);
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;

        if (entryDate != null) {
            date = new DateNonConvertable(entryDate);
        }
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
