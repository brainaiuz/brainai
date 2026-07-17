package com.edatasite.workforce.core.domain.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 12:27:33 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myactivitymessage")
public class EdsMyActivityMessage extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activitytype")
    private EdsMyActivityType myActivityType;

    @Column
    private String broadMessageCode;

    @Column
    private String myMessageCode;

    public EdsMyActivityType getMyActivityType() {
        return myActivityType;
    }

    public void setMyActivityType(EdsMyActivityType myActivityType) {
        this.myActivityType = myActivityType;
    }

    public String getBroadMessageCode() {
        return broadMessageCode;
    }

    public void setBroadMessageCode(String broadMessageCode) {
        this.broadMessageCode = broadMessageCode;
    }

    public String getMyMessageCode() {
        return myMessageCode;
    }

    public void setMyMessageCode(String myMessageCode) {
        this.myMessageCode = myMessageCode;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
