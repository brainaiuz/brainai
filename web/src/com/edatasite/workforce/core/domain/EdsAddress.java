package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.09.2010
 * Time: 15:11:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "address",
        indexes = {
                @Index(columnList = "contactID", name = "address_contactid_idx"),
                @Index(columnList = "accountID", name = "address_accountid_idx")
        })
public class EdsAddress extends EdsObject {

    //Address Types
    public static final Integer BILLING_ADDRESS = 0;
    public static final Integer MAILING_ADDRESS = 1;
    public static final int HOME = Constants.G_HOME;
    public static final int WORK = Constants.G_WORK;
    public static final int OTHER = Constants.G_OTHER;
    public static final String ENTITY_TYPE_CRM_ACCOUNT = CrmConstants.CRM_ACCOUNT;
    public static final String ENTITY_TYPE_CONTACT = CrmConstants.CRM_CONTACT;
    public static final String ENTITY_TYPE_COMPANY = "company";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String address; //Address 1
    private String addressb; //Address 2
    private String city; //City
    private String countryName; //countryName
    private String zipCode; //ZIP/Post code
    private Integer entityID; //Entity ID
    private Integer accountID;
    private Integer contactID;
    private String entityType;
    private Integer relationType; //Billing/Mailing Address for Account, HOME/WORK/OTHER addresses for Contact
    private Double latitude;
    private Double longitude;
    @Column(name = "isprimary", columnDefinition = "boolean default false")
    private boolean primary = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateid")
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsRegion state;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    @Column(name = "isLinkedAddress", columnDefinition = "boolean default false")
    private boolean isLinkedAddress = false;

    private Integer linkedAddressID;

    /// this fields for KSA companies
    private String citySubdivisionName;
    private String plotIdentification;
    private String buildingNumber;


    @Transient
    private boolean changed = false;

    public void setAddressData(Address data) {
        if (data != null) {
            setName(data.getName());
            setAddress(data.getAddress());
            setAddressb(data.getAddressb());
            setCity(data.getCity());
            setCountryName(data.getCountry());
            setZipCode(data.getZipCode());
            setPrimary(data.isPrimary());
            setLinkedAddress(data.isLinkedAddress());
            setLinkedAddressID(data.getLinkedAddressID());
            setPlotIdentification(data.getPlotIdentification());
            setCitySubdivisionName(data.getCitySubdivisionName());
            setBuildingNumber(data.getBuildingNumber());
        }
    }

    public void setAddressData(Address data, EdsCountry country, EdsRegion state) {
        if (data != null) {
            setAddressData(data);
            setLatitude(data.getLatitude());
            setLongitude(data.getLongitude());
        }
        setCountry(country);
        setState(state);
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isLinkedAddress() {
        return isLinkedAddress;
    }

    public void setLinkedAddress(boolean linkedAddress) {
        isLinkedAddress = linkedAddress;
    }

    public Integer getLinkedAddressID() {
        return linkedAddressID;
    }

    public void setLinkedAddressID(Integer linkedAddressID) {
        this.linkedAddressID = linkedAddressID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 255) {
            name = name.substring(0, 255);
        }
        changed = changed ? changed : ServerUtils.equalsString(name, this.name);
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null && address.length() > 255) {
            address = address.substring(0, 255);
        }
        changed = changed ? changed : ServerUtils.equalsString(address, this.address);
        this.address = address;
    }

    public String getAddressb() {
        return addressb;
    }

    public void setAddressb(String addressb) {
        if (addressb != null && addressb.length() > 255) {
            addressb = addressb.substring(0, 255);
        }
        changed = changed ? changed : ServerUtils.equalsString(addressb, this.addressb);
        this.addressb = addressb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city != null && city.length() > 255) {
            city = city.substring(0, 255);
        }
        changed = changed ? changed : ServerUtils.equalsString(city, this.city);
        this.city = city;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        if (countryName != null && countryName.length() > 255) {
            countryName = countryName.substring(0, 255);
        }
        changed = changed ? changed : ServerUtils.equalsString(countryName, this.countryName);
        this.countryName = countryName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        changed = changed ? changed : ServerUtils.equalsString(zipCode, this.zipCode);
        this.zipCode = zipCode;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        if (entityType != null && !"".equals(entityType)) {
            if (entityType.equals(EdsAddress.ENTITY_TYPE_CONTACT)) {
                this.contactID = entityID;
            } else if (entityType.equals(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT)) {
                this.accountID = entityID;
            }
        }
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        changed = changed ? changed : ServerUtils.equalsEdsObject(country, this.country);
        this.country = country;
    }

    public EdsRegion getState() {
        return state;
    }

    public void setState(EdsRegion state) {
        changed = changed ? changed : ServerUtils.equalsEdsObject(state, this.state);
        this.state = state;
    }

    public String getStateName() {
        return getState() == null ? null : getState().getName();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Address getRPC() {
        Address data = new Address(objectID, name, address, addressb, city, zipCode);
        data.setRelationType(getRelationType());
        data.setEntityID(getEntityID());
        data.setEntityType(getEntityType());
        data.setPrimary(isPrimary());
        data.setLinkedAddress(isLinkedAddress());
        data.setLinkedAddressID(getLinkedAddressID());
        data.setLatitude(getLatitude());
        data.setLongitude(getLongitude());
        data.setBuildingNumber(getBuildingNumber());
        data.setPlotIdentification(getPlotIdentification());
        data.setCitySubdivisionName(getCitySubdivisionName());
        if (country != null) {
            data.setCountryId(country.getObjectID());
            data.setCountry(country.getName());
            data.setCountryCode(country.getCode());
            if (state != null) {
                try {
                    data.setStateId(state.getObjectID());
                    data.setState(state.getName());
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public boolean isNotEmpty() {
        if (getAddress() != null && !"".equals(getAddress())) {
            return true;
        }
        if (getAddressb() != null && !"".equals(getAddressb())) {
            return true;
        }
        if (getCity() != null && !"".equals(getCity())) {
            return true;
        }
        if (getCountry() != null) {
            return true;
        }
        if (getState() != null) {
            return true;
        }
        return getZipCode() != null && !"".equals(getZipCode());
    }

    public static EdsAddress getFirstAddress(List<EdsAddress> addresses, boolean ifNotExistReturnFirst, Boolean isPrimary, int... relations) {
        if (addresses != null && addresses.size() > 0) {
            if (relations != null && relations.length > 0) {
                for (int relation : relations) {
                    for (EdsAddress address : addresses) {
                        if (address.getRelationType().equals(relation)) {
                            return address;
                        }
                    }
                }

                if (ifNotExistReturnFirst) {
                    return addresses.get(0);
                }
            } else if (Boolean.TRUE.equals(isPrimary)) {
                for (EdsAddress address : addresses) {
                    if (address != null && address.isPrimary()) {
                        return address;
                    }
                }
            } else {
                return addresses.get(0);
            }
        }
        return null;
    }

    public void setEntity(EdsObject obj) {
        if (obj instanceof EdsCrmContact) {
            setContact((EdsCrmContact) obj);
        } else if (obj instanceof EdsCrmAccount) {
            setCrmAccount((EdsCrmAccount) obj);
        } else if (obj instanceof EdsCompany) {
            setCompany((EdsCompany) obj);
        }
    }

    public void setContact(EdsCrmContact contact) {
        setEntityType(ENTITY_TYPE_CONTACT);
        if (contact != null) {
            setEntityID(contact.getObjectID());
            setContactID(contact.getObjectID());
        }
    }

    public void setCrmAccount(EdsCrmAccount account) {
        setEntityType(ENTITY_TYPE_CRM_ACCOUNT);
        if (account != null) {
            setEntityID(account.getObjectID());
            setAccountID(account.getObjectID());
        }
    }

    public void setCompany(EdsCompany company) {
        setEntityType(ENTITY_TYPE_COMPANY);
        if (company != null) {
            setEntityID(company.getObjectID());
        }
    }

    public String getCitySubdivisionName() {
        return citySubdivisionName;
    }

    public void setCitySubdivisionName(String citySubdivisionName) {
        this.citySubdivisionName = citySubdivisionName;
    }

    public String getPlotIdentification() {
        return plotIdentification;
    }

    public void setPlotIdentification(String plotIdentification) {
        this.plotIdentification = plotIdentification;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressDataAsHTML() {
        StringBuilder dataBuffer = new StringBuilder();
        if (getAddress() != null && !"".equals(getAddress().trim())) {
            dataBuffer.append(getAddress());
        }
        if (getAddressb() != null && !"".equals(getAddressb().trim())) {
            dataBuffer.append(StringUtils.isNotBlank(dataBuffer.toString()) ? ", ": "").append(getAddressb());
        }
        if (getCity() != null && !"".equals(getCity().trim())) {
            dataBuffer.append(StringUtils.isNotBlank(dataBuffer.toString()) ? ", ": "").append(getCity());
        }
        if (getState() != null) {
            dataBuffer.append(StringUtils.isNotBlank(dataBuffer.toString()) ? ", ": "").append(getState().getName());
        }
        if (getCountry() != null) {
            dataBuffer.append(StringUtils.isNotBlank(dataBuffer.toString()) ? ", ": "").append(getCountry().getName());
        }
        if (getZipCode() != null && !"".equals(getZipCode().trim())) {
            dataBuffer.append(StringUtils.isNotBlank(dataBuffer.toString()) ? ", ": "").append(getZipCode());
        }
        return dataBuffer.toString();
    }
    public String getAddressData() {
        List<String> parts = new ArrayList<>();

        if (StringUtils.isNotBlank(getName())) {
            parts.add(getName().trim());
        }
        if (StringUtils.isNotBlank(getAddress())) {
            parts.add(getAddress().trim());
        }
        if (StringUtils.isNotBlank(getAddressb())) {
            parts.add(getAddressb().trim());
        }
        if (StringUtils.isNotBlank(getCity())) {
            parts.add(getCity().trim());
        }
        if (getState() != null && StringUtils.isNotBlank(getState().getName())) {
            parts.add(getState().getName().trim());
        }
        if (StringUtils.isNotBlank(getZipCode())) {
            parts.add(getZipCode().trim());
        }
        if (getCountry() != null && StringUtils.isNotBlank(getCountry().getName())) {
            parts.add(getCountry().getName().trim());
        }

        return String.join(", ", parts);
    }
    public boolean isChanged() {
        return changed;
    }
}
