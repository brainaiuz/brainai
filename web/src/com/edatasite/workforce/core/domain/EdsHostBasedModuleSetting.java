package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 30.06.14
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class EdsHostBasedModuleSetting extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "host", unique = true)
    private String hostName;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
