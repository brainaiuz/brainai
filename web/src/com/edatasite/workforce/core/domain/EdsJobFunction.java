package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by IntelliJ IDEA.
 * User: Slizer3D
 * Date: 23.03.2007
 * Time: 16:13:56
 * Software Team
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "jobfunction")
public class EdsJobFunction extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "jobfamily")
    private String jobfamily;

    @Column(name = "regtemp")
    private String regtemp;

    @Column(name = "fullparttime")
    private String fullparttime;

    @Column(name = "salarygrade")
    private String salarygrade;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobfamily() {
        return jobfamily;
    }

    public void setJobfamily(String jobfamily) {
        this.jobfamily = jobfamily;
    }

    public String getRegtemp() {
        return regtemp;
    }

    public void setRegtemp(String regtemp) {
        this.regtemp = regtemp;
    }

    public String getFullparttime() {
        return fullparttime;
    }

    public void setFullparttime(String fullparttime) {
        this.fullparttime = fullparttime;
    }

    public String getSalarygrade() {
        return salarygrade;
    }

    public void setSalarygrade(String salarygrade) {
        this.salarygrade = salarygrade;
    }
}

