package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Aziz
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ats_job_salary")
public class EdsATSJobSalary extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "salaryAmount")
    private String salaryAmount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(String salaryAmount) {
        this.salaryAmount = salaryAmount;
    }
}
