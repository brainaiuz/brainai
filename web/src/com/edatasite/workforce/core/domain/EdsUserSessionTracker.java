package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 15.07.2009
 * Time: 16:34:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myUserSessionTrack")
public class EdsUserSessionTracker extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String sectionName;
    private String parameters;
    private Date accessTime;

    private Date moduleLoadedTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "userssessionId")
    private EdsUserSession userSession;

    public EdsUserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(EdsUserSession userSession) {
        this.userSession = userSession;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public Date getModuleLoadedTime() {
        return moduleLoadedTime;
    }

    public void setModuleLoadedTime(Date moduleLoadedTime) {
        this.moduleLoadedTime = moduleLoadedTime;
    }
}
