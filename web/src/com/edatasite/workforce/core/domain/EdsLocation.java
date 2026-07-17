package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsLocationCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.LocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 16:30:46
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "location")
public class EdsLocation extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EdsLocation parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsLocation> childList;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "countryid")
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCountry country;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "stateid")
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsRegion state;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "city_district")
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCityDistrict cityDistrict;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "fax", length = 50)
    private String fax;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Integer timeSlotID;

    @Column(name = "zipCode")
    private String zipCode; //ZIP/Post code

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsLocationCustomFields customFields;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "code")
    private String code;

    @Column(name = "ownersId")
    private String ownersId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "radius")
    private Integer radius;

    public EdsRegion getState() {
        return state;
    }

    public void setState(EdsRegion state) {
        this.state = state;
    }

    public EdsLocation getParent() {
        return parent;
    }

    public void setParent(EdsLocation parent) {
        this.parent = parent;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public SelectItem getAsSelectItem() {
        SelectItem item = new SelectItem(getObjectID());
        item.setName(getName());
        item.setCode(getCode());
        return item;
    }

    @Override
    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return name;
    }
    public String getLocationRealName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTimeSlotID() {
        return timeSlotID;
    }

    public void setTimeSlotID(Integer timeSlotID) {
        this.timeSlotID = timeSlotID;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LocationItem getRPC() {
        return new LocationItem(getObjectID().toString(), null, getCountry() != null ? getCountry().getName() : "");
    }

    public EdsLocationCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsLocationCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EdsCityDistrict getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(EdsCityDistrict city_district) {
        this.cityDistrict = city_district;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
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

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
