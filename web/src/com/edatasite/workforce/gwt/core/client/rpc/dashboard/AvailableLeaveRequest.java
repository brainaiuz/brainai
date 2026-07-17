package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02.07.2009
 * Time: 14:36:28
 * To change this template use File | Settings | File Templates.
 */
public class AvailableLeaveRequest implements IsSerializable {

    private String employeeName;
    private Date from;
	private DateNonConvertable fromNonConvertable;
    private Date to;
	private DateNonConvertable toNonConvertable;
    private String status;
    private String reason;
    private String approver;
    private String holidayName;
    private static int nextId = 0;

    public AvailableLeaveRequest() {
         nextId++;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

	public DateNonConvertable getFromNonConvertable() {
		return fromNonConvertable;
	}

	public void setFromNonConvertable(DateNonConvertable fromNonConvertable) {
		this.fromNonConvertable = fromNonConvertable;
	}

	public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

	public DateNonConvertable getToNonConvertable() {
		return toNonConvertable;
	}

	public void setToNonConvertable(DateNonConvertable toNonConvertable) {
		this.toNonConvertable = toNonConvertable;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public static int getNextId() {
        return nextId;
    }
}
