package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.rbac.history.EdsTaskRbacHistory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 04.05.2007
 * Time: 19:04:02
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "TimeSheet", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "employeetaskId"}),
        indexes = {
                @Index(columnList = "employeetaskid", name = "timesheet_employeetaskid_idx"),
                @Index(columnList = "taskid", name = "timesheet_taskid_idx"),
                @Index(columnList = "employeeId", name = "timesheet_employeeid_idx")
        })
public class EdsTimeSheet extends EdsObject {

    public static final String _WAITING = "_WAITING";
    public static final String _REJECT = "_REJECT";
    public static final String _APPROVE = "_APPROVE";
    public static final String _TIMESHEET_TYPE = "_TIMESHEET_TYPE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "comment", length = 2000)
    private String comment = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskrbachistoryid")
    private EdsTaskRbacHistory taskRbacHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeetaskId")
    private EdsEmployeeTask employeeTask;

    @Column(name = "managerComment", length = 1000)
    private String managerComment = "";

    @Column(name = "date")
    private Date date;

    @Column(name = "approvalDate")
    private Date approvalDate;
    
    @Column(name = "entryDate")//timesheet entered/updated date
    private Date entryDate;

    @Column(name = "submittedDate")
    private Date submittedDate;
    
    @Column(name = "rejectedDate")
    private Date rejectedDate;

    @Column(name = "autoApproved", columnDefinition = "boolean default false")
    private Boolean autoApproved = false;

    @Column(name = "quickbook_timesheet_id")
    private String quickbookTimesheetId;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    private Integer timeSpent;
	/**
	 * Employee's Daily planned time in this task
	 */
	private Integer dailyEstimatedTime;

    private Boolean usedInInvoice;

    @Column(columnDefinition = "boolean default false")
    private Boolean usedInExpense = Boolean.FALSE;

    private Integer invoiceItemID;

    private Integer expenseID;

    private Integer payslipID;

    private Integer teamID;

    private Integer employeeID;

    private Integer projectID;

    private Integer taskID;

    //EmployeeWageClientRateHistory
    private Double wageRate;

    private Double clientChargeRate;

    @Transient
    private String timeSpentHM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private EdsReference type;

    private String reference;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployeeTask getEmployeeTask() {
        return employeeTask;
    }

    public void setEmployeeTask(EdsEmployeeTask employeeTask) {
        this.employeeTask = employeeTask;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
//        Integer diff = timeSpent - getTimeSpent();
//        task.setTimeSpent(task.getTimeSpent() + diff);
        this.timeSpent = timeSpent;

    }

	public Integer getDailyEstimatedTime() {
		return dailyEstimatedTime;
	}

	public void setDailyEstimatedTime(Integer dailyEstimatedTime) {
		this.dailyEstimatedTime = dailyEstimatedTime;
	}

	public Boolean getUsedInInvoice() {
        return usedInInvoice;
    }

    public void setUsedInInvoice(Boolean usedInInvoice) {
        this.usedInInvoice = usedInInvoice;
    }

    public Integer getInvoiceItemID() {
        return invoiceItemID;
    }

    public void setInvoiceItemID(Integer invoiceItemID) {
        this.invoiceItemID = invoiceItemID;
    }

    public Integer getPayslipID() {
        return payslipID;
    }

    public void setPayslipID(Integer payslipID) {
        this.payslipID = payslipID;
    }

    public String getTimeSpentHM() {
        if (timeSpent == null) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timeSpent / 60 < 10) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpent % 60 < 10) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpent % 60;
        return timeSpentHM;
    }

    public void setTimeSpentHM(String timeSpentHM) {
        this.timeSpentHM = timeSpentHM;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public Boolean getAutoApproved() {
        return autoApproved;
    }

    public void setAutoApproved(Boolean autoApproved) {
        this.autoApproved = autoApproved;
    }

    public EdsTaskRbacHistory getTaskRbacHistory() {
        return taskRbacHistory;
    }

    public void setTaskRbacHistory(EdsTaskRbacHistory taskRbacHistory) {
        this.taskRbacHistory = taskRbacHistory;
    }

    public String getQuickbookTimesheetId() {
        return quickbookTimesheetId;
    }

    public void setQuickbookTimesheetId(String quickbookTimesheetId) {
        this.quickbookTimesheetId = quickbookTimesheetId;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Double getWageRate() {
        return wageRate != null ? wageRate : 0;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public Boolean getUsedInExpense() {
        return usedInExpense;
    }

    public void setUsedInExpense(Boolean usedInExpense) {
        this.usedInExpense = usedInExpense;
    }

    public Integer getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(Integer expenseID) {
        this.expenseID = expenseID;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal calculateWageAmount(List<EdsProjectEmployeeWageClientRateHistory> wagesHistory) {
        BigDecimal wageAmount = BigDecimal.ZERO;
        for (int j = 0; j < wagesHistory.size(); j++) {
            EdsProjectEmployeeWageClientRateHistory current = wagesHistory.get(j);

            //if timesheet date lays before first salary entry
            if ((getDate().before(current.getChangeDate()) || getDate().equals(current.getChangeDate())) && j == 0) {
                wageAmount = wageAmount.add(new BigDecimal(current.getWageRate() * ((double) getTimeSpent() / 60))); //should take the difference, correct
                break;
            }

            EdsProjectEmployeeWageClientRateHistory next = null;

            if (j != (wagesHistory.size() - 1)) {
                next = wagesHistory.get(j + 1);
            }

            //if timesheet date between salaries change time range, take the lowest salariy change date
            if (getDate().after(current.getChangeDate()) && ((next == null || getDate().before(next.getChangeDate())))) {
                wageAmount = wageAmount.add(new BigDecimal(current.getWageRate() * ((double) getTimeSpent() / 60))); //should take the difference, correct
                break;
            }
        }

        return wageAmount;
    }
}
