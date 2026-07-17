package com.edatasite.workforce.core.domain.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 1:04:55 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myactivity")
public class EdsMyActivity extends EdsObject {
    //Activity related entities
    public static final String ENTITY_TASK = "_TASK";
    public static final String ENTITY_PROJECT = "_PROJECT";
    public static final String ENTITY_EMPLOYEE = "_EMPLOYEE";

    /// Activity Event Types
    public static final String CREATE_BUSINESS_ENTITY = "CREATE_BUSINESS_ENTITY";
    public static final String UPDATE_BUSINESS_ENTITY = "UPDATE_BUSINESS_ENTITY";
    public static final String DELETE_BUSINESS_ENTITY = "DELETE_BUSINESS_ENTITY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activitytype")
    private EdsMyActivityType myActivityType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activityid")
    private Set<EdsMyActivityLog> myActivityLog = new HashSet<>();

    @Column(name = "entityid")
    private Integer entityID;

    @Column(name = "eventDate")
    private Date activityDate;

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Set<EdsMyActivityLog> getMyActivityLog() {
        return myActivityLog;
    }

    public void setMyActivityLog(Set<EdsMyActivityLog> myActivityLog) {
        this.myActivityLog = myActivityLog;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsMyActivityType getMyActivityType() {
        return myActivityType;
    }

    public void setMyActivityType(EdsMyActivityType myActivityType) {
        this.myActivityType = myActivityType;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
