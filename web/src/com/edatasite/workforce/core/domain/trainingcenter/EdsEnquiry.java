package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/16/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "enquiry")
public class EdsEnquiry extends EdsObject {

    public static final String ENQUIRY_MODE_PARENT = "ENQUIRY_MODE_PARENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private EdsCrmAccount customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private EdsCrmContact contact;

    private Date enquiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquirymode_id")
    private EdsReference enquiryMode;

    @Type(type = "text")
    private String refInfo;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enquiry")
    private List<EdsEnquiryItem> items = new ArrayList<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsCrmAccount getCustomer() {
        return customer;
    }

    public void setCustomer(EdsCrmAccount customer) {
        this.customer = customer;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public EdsReference getEnquiryMode() {
        return enquiryMode;
    }

    public void setEnquiryMode(EdsReference enquiryMode) {
        this.enquiryMode = enquiryMode;
    }

    public String getRefInfo() {
        return refInfo;
    }

    public void setRefInfo(String refInfo) {
        this.refInfo = refInfo;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsEnquiryItem> getItems() {
        return items;
    }

    public void setItems(List<EdsEnquiryItem> items) {
        this.items = items;
    }
}
