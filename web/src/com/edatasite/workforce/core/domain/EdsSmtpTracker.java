package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "smtpTracker")
public class EdsSmtpTracker extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String login;

    private Integer sentCount = 0;


    public EdsSmtpTracker(String login) {
        this.login = login;
    }

    public EdsSmtpTracker() {

    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}
