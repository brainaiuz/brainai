package com.edatasite.workforce.core.domain.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 1:55:03 PM
 * <p/>
 * User Activity log.
 * Logs user activity by registering activity type
 * changes by activity
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myactivitylog")
public class EdsMyActivityLog extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activitytype")
    private EdsMyActivityType myActivityType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activityid")
    private EdsMyActivity activity;

    private String previousValue;

    private String currentValue;

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public EdsMyActivityType getMyActivityType() {
        return myActivityType;
    }

    public void setMyActivityType(EdsMyActivityType myActivityType) {
        this.myActivityType = myActivityType;
    }

    public EdsMyActivity getActivity() {
        return activity;
    }

    public void setActivity(EdsMyActivity activity) {
        this.activity = activity;
    }
}
