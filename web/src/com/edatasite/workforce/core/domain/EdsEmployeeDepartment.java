package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA. User: Slizer3D Date: 22.03.2007 Time: 16:07:55
 * Software Team
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "teamEmployee",
        indexes = {
                @Index(columnList = "teamId", name="teamEmployee_teamId_idx"),
                @Index(columnList = "employeeId", name="teamEmployee_employeeId_idx")
        })
public class EdsEmployeeDepartment extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeDepartmentId")
    private Set<EdsProjectEmployee> projects = new TreeSet<>();

    @Transient
    private Set<EdsJobFunction> jobFunctions = new HashSet<>();

    private Date startDate;
    private Date endDate;

    @Column(name = "isdeleted")
    private Boolean deleted = false;

    @Column
    private String employeeTaskColor;

    public EdsEmployeeDepartment() {

    }

    public EdsEmployeeDepartment(EdsDepartment department, EdsEmployee employee) {
        this.department = department;
        this.employee = employee;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDepartment getTeam() {
        return department;
    }

    public void setTeam(EdsDepartment team) {
        this.department = team;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Set<EdsJobFunction> getJobFunctions() {
        return jobFunctions;
    }

    public void setJobFunctions(Set<EdsJobFunction> jobFunctions) {
        this.jobFunctions = jobFunctions;
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

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<EdsProjectEmployee> getProjects() {
        return projects;
    }

    public void setProjects(Set<EdsProjectEmployee> projects) {
        this.projects = projects;
    }

    public String getEmployeeTaskColor() {
        return employeeTaskColor;
    }

    public void setEmployeeTaskColor(String employeeTaskColor) {
        this.employeeTaskColor = employeeTaskColor;
    }
}
