package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 15:25:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmContactDetails")
public class EdsCrmContactDetails extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "googlenote")
    @Type(type = "text")
    private String googlenote;

    @Column(name = "backgroundInformation")
    @Type(type = "text")
    private String backgroundInformation;

    @Column(name = "disclaimer")
    @Type(type = "text")
    private String disclaimer;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getGooglenote() {
        return googlenote;
    }

    public void setGooglenote(String googlenote) {
        this.googlenote = googlenote;
    }

    public String getBackgroundInformation() {
        return backgroundInformation;
    }

    public void setBackgroundInformation(String backgroundInformation) {
        this.backgroundInformation = backgroundInformation;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
