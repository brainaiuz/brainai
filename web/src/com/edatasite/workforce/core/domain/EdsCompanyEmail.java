package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by Azazello on 6/24/2017.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "companyEmail", uniqueConstraints = {@UniqueConstraint(columnNames = {"companyid"})})
public class EdsCompanyEmail extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCompany company;

    @Column(name = "email")
    private String email;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }
}
