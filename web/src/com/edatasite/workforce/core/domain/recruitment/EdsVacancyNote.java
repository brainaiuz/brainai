package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Muxriddin Raimov.
 * Date: 2/16/2022 12:30 PM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vacancyNote")
public class EdsVacancyNote extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "creationDate")
    private Date creationDate;

    @Column(name = "text", length = 1000)
    private String text;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancyId")
    private EdsVacancy vacancy;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EdsVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(EdsVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }
}
