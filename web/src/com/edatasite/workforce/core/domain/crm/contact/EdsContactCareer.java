package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsReference;
import org.hibernate.annotations.ForeignKey;

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
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 01.12.2010
 * Time: 19:36:42
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contactCareer")
public class EdsContactCareer extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @Column(name = "city")
    private String city;

    @Column(name = "companyName")
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactId")
    private EdsCrmContact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industryId")
    private EdsReference industry;

    @Column(name = "isCurrentYear")
    private Boolean isCurrentYear;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "fromYear")
    private Date fromYear;

    @Column(name = "toYear")
    private Date toYear;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public EdsReference getIndustry() {
        return industry;
    }

    public void setIndustry(EdsReference industry) {
        this.industry = industry;
    }

    public Boolean getCurrentYear() {
        return isCurrentYear;
    }

    public void setCurrentYear(Boolean currentYear) {
        isCurrentYear = currentYear;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getFromYear() {
        return fromYear;
    }

    public void setFromYear(Date fromYear) {
        this.fromYear = fromYear;
    }

    public Date getToYear() {
        return toYear;
    }

    public void setToYear(Date toYear) {
        this.toYear = toYear;
    }
}
