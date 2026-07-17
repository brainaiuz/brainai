package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsStudentCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 1:46 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "student")
public class EdsStudent extends EdsObject implements ObjectHistory {

    //student card types
    //parent
    public static final String _STUDENT_CARD_TYPES = StudentItem._STUDENT_CARD_TYPES;                    //Student card types
    //children
    public static final String S_CARD_TYPE_DRIVING_LICENSE = StudentItem.S_CARD_TYPE_DRIVING_LICENSE;   //Driving license
    public static final String S_CARD_TYPE_EMIRATES_ID = StudentItem.S_CARD_TYPE_EMIRATES_ID;           //Emirates ID
    public static final String S_CARD_TYPE_LABOUR_CARD = StudentItem.S_CARD_TYPE_LABOUR_CARD;           //Labour card
    public static final String S_CARD_TYPE_PASSPORT = StudentItem.S_CARD_TYPE_PASSPORT;                 //Passport

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "number")
    private String number;

    @Column(name = "comp_empl_number")
    private String compEmplNumber;

    @Column(name = "departmentCode")
    private String departmentCode;

    @Column(name = "active")
    private Boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private EdsCrmContact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private EdsCrmAccount customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCard")
    private EdsReference IDCard;

    @Column(name = "IDCardNumber")
    private String IDCardNumber;

    @Column(name = "safetyPPNumber")
    private String safetyPPNumber;

    @Column(name = "gender")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private EdsUpload photo;

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsStudentCustomFields customFields;


    private String nationality;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompEmplNumber() {
        return compEmplNumber;
    }

    public void setCompEmplNumber(String compEmplNumber) {
        this.compEmplNumber = compEmplNumber;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public String getEmail() {
        if (getContact() != null) {
            return getContact().getPrimaryEmail();
        }
        return null;
    }

    public String getPhone() {
        if (getContact() != null) {
            return getContact().getPrimaryPhoneFromAll();
        }

        return null;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public EdsCrmAccount getCustomer() {
        return customer;
    }

    public void setCustomer(EdsCrmAccount customer) {
        this.customer = customer;
    }

    public EdsReference getIDCard() {
        return IDCard;
    }

    public void setIDCard(EdsReference IDCard) {
        this.IDCard = IDCard;
    }

    public String getIDCardNumber() {
        return IDCardNumber;
    }

    public void setIDCardNumber(String IDCardNumber) {
        this.IDCardNumber = IDCardNumber;
    }

    public String getFullName() {
        return getContact().getFirstName() + " " + getContact().getLastName();
    }

    public String getSafetyPPNumber() {
        return safetyPPNumber;
    }

    public void setSafetyPPNumber(String safetyPPNumber) {
        this.safetyPPNumber = safetyPPNumber;
    }


    @Override
    public void setLastUpdateTime(Date value) {
//      contact.getAuditInfo().getModificationDate();
    }

    @Override
    public void setUpdater(EdsUser user) {
    }

    @Override
    public void setCreationTime(Date value) {
//       contact.getAuditInfo().getCreationDate();
    }

    @Override
    public void setCreator(EdsUser value) {
    }

    public StudentItem getRPC(StudentItem... studentItems) {
        StudentItem studentItem = new StudentItem();
        if (studentItems != null && studentItems.length > 0 && studentItems[0] != null) {
            studentItem = studentItems[0];
        }

        if (getContact() != null) {
            studentItem = (StudentItem) getContact().getRPC(new ListingFilterParameter(false), studentItem);

            studentItem.setContactID(getContact().getObjectID());
            studentItem.setContactName(getContact().getName());
        }
        studentItem.setObjectId(getObjectID());

        if (getCustomer() != null) {
            studentItem.setCustomerID(getCustomer().getObjectID());
            studentItem.setCustomerName(getCustomer().getName());
        }

        studentItem.setActive(getActive() != null ? getActive() : false);
        if (getNumber() != null) {
            studentItem.setNumber(getNumber());
        }
        studentItem.setNumberData(new NumberData(getNumber(), getIntNumber()));
        studentItem.setCompEmpNum(getCompEmplNumber());
        studentItem.setDepartmentCode(getDepartmentCode());
        studentItem.setRefIndNumber(getContact().getRefIndNumber());
        studentItem.setDepartmentCode(getDepartmentCode());
        studentItem.setFirstName(getContact().getFirstName());
        studentItem.setLastName(getContact().getLastName());
        studentItem.setMiddleName(getContact().getMiddleName());
        studentItem.setCreatedDate(getContact().getAuditInfo().getCreationDate());
        studentItem.setUpdatedDate(getContact().getAuditInfo().getModificationDate());

        studentItem.setGender(getGender());
        studentItem.setNationality(getNationality());
        if (getContact().getAuditInfo() != null) {
            if (getContact().getAuditInfo().getModificationDate() != null) {
                studentItem.setUpdatedDate(getContact().getAuditInfo().getModificationDate());
            }
        }

        studentItem.setSafetyPPNumber(getSafetyPPNumber());

        if (getIDCard() != null) {
            studentItem.setCardTypeID(getIDCard().getObjectID());
            studentItem.setCardTypeName(getIDCard().getName());
        }
        studentItem.setCardNumber(getIDCardNumber());
        return studentItem;
    }

    public EdsUpload getPhoto() {
        return photo;
    }

    public void setPhoto(EdsUpload photo) {
        this.photo = photo;
    }

    @Override
    public SelectItem getAsSelectItem() {
        if (getContact() != null) {
            return new SelectItem(getObjectID(), getContact().getFirstName() + " " + getContact().getLastName() + " (" + getNumber() + ")");
        } else {
            return new SelectItem(getObjectID(), getNumber());
        }
    }
    public EdsStudentCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsStudentCustomFields customFields) {
        this.customFields = customFields;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
