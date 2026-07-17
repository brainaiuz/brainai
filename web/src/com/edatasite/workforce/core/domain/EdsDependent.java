package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsDependentCustomFields;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/**
 * User: unni
 * Date: Oct 22, 2009
 * Time: 10:40:35 AM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "dependent")
public class EdsDependent extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EdsEmployee user;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "address")
    private String address;

    @Column(name = "addressb")
    private String addressb;

    @Column(name = "city")
    private String city;

    @Column(name = "town")
    private String town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @Column(name = "phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateID")
    private EdsCrmContact candidate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsDependentCustomFields customFields;

    @Column(columnDefinition = " boolean DEFAULT false")
    private Boolean system = false;
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getUser() {
        return user;
    }

    public void setUser(EdsEmployee user) {
        this.user = user;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddressb() {
        return addressb;
    }

    public void setAddressb(String addressb) {
        this.addressb = addressb;
    }

    public EdsDependentCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsDependentCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        this.candidate = candidate;
    }

    public boolean isSystem() {
        return system == null ? false : system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public DependentItem getRPC() {
        DependentItem item = new DependentItem();
        //dependent ID
        item.setObjectId(getObjectID());
        //dependent first name
        item.setFirstName(getFirstName());
        //dependent middle name
        item.setMiddleName(getMiddleName());
        //dependent last name
        item.setLastName(getLastName());
        //dependent relationship
        item.setRelationship(getRelationship());
        //dependent address 1
        item.setAddress(getAddress());
        //dependent address 2
        item.setAddressb(getAddressb());
        //dependent city
        item.setCity(getCity());
        //dependent town
        item.setTown(getTown());
        //dependent country
        if (getCountry() != null) {
            item.setCountryId(getCountry().getObjectID());
            item.setCountryName(getCountry().getName());
        }
        //dependent phone 1
        item.setPhone1(getPhone1());
        //dependent phone 2
        item.setPhone2(getPhone2());

        return item;
    }
}
