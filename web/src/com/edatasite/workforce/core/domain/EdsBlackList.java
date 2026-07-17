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
 * User: admin
 * Date: Jan 5, 2010
 * Time: 3:43:01 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "blackList")
public class EdsBlackList extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "blackEmail")
    private String email;//Black emails

    @Column(name = "hostName")
    private String hostName; //Host name - black emails shown to User like that host name
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}