package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.lucene.Indexable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Abdulaziz
 * Date: 01.05.2009
 * Time: 17:39:03
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timesheetapprovalsession",
        indexes = {
                @Index(columnList = "projectid", name = "timesheetapprovalsession_projectid_idx"),
                @Index(columnList = "employee", name = "timesheetapprovalsession_employee_idx"),
                @Index(columnList = "statusId", name = "timesheetapprovalsession_statusid_idx")
        })
public class EdsTimeSheetApprovalSession extends EdsObject implements Indexable {
    public static String TIME_SHEET_APPROVAL_SESSION_STATUS = "_TIME_SHEET_APPROVAL_SESSION_STATUS";
    public static String BILLED = "_BILLED";
    public static String APPROVED = "_APPROVED";
    public static String WAITING_FOR_APPROVAL = "_WAITING_FOR_APPROVAL";
    public static String REJECTED = "_REJECTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "timesheetapprovalsession_TimeSheet",
            joinColumns = {@JoinColumn(name = "timesheetapprovalsession_id")},
            inverseJoinColumns = {@JoinColumn(name = "timeentries_id")}
    )
    Set<EdsTimeSheet> timeentries = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver")
    private EdsEmployee approver;

    @Column(name = "indexed")
    private Boolean indexed;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "submittedDate")
    private Date submittedDate;

    @Column(name = "approvalDate")
    private Date approvalDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Set<EdsTimeSheet> getTimeentries() {
        return timeentries;
    }

    public void setTimeentries(Set<EdsTimeSheet> timeentries) {
        this.timeentries = timeentries;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
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

    public EdsEmployee getApprover() {
        return approver;
    }

    public void setApprover(EdsEmployee approver) {
        this.approver = approver;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
}
