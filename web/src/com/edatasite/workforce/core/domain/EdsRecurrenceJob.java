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
 * User: Ilxom Lutfullaev
 * Date: Apr 15, 2010
 * Time: 11:08:53 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "recurrenceJob")
public class EdsRecurrenceJob extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String jobName;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }
}
