package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "botimage")
public class EdsBotImage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    private String botcode;
    private String contentType;

    @Transient

    public boolean canAccess(EdsUser user) {
        return true;
    }

    public String getBotcode() {
        return botcode;
    }

    public void setBotcode(String botcode) {
        this.botcode = botcode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
