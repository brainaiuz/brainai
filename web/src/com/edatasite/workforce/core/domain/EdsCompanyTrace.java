package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "companyTrace")
public class EdsCompanyTrace extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjetcID(Integer id) {
        this.objectID = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCompany company;

    private Integer taskCount = 0;
    private Integer projectCount = 0;
    private Integer employeeCount = 0;


    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskConunt) {
        this.taskCount = taskConunt;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }
}
