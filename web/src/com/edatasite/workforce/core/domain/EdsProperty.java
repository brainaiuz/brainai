package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.google.gson.Gson;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "property")
public class EdsProperty extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String objectName;

    private String defaultName;
    private String singular;
    private String plural;
    private String shortcut;

    //pm, accounting, crm
    private String moduleCode;

    @Column(name = "fid")
    private Integer fid;

    @Column(name = "form_id")
    private String formID;

    @Column(name = "isActive", columnDefinition = "boolean default false")
    private Boolean active = Boolean.FALSE;

    @Column(name = "isCustom", columnDefinition = "boolean default false")
    private Boolean isCustom = Boolean.FALSE;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private EdsUser user;

    @Column(name = "convertItems")
    @Type(type = "text")
    private String convertItems;

    @Column(name = "form_type")
    private String formType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lnameId")
    private EdsCustomFormLocalization lName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lPluralId")
    private EdsCustomFormLocalization lPlural;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lShortId")
    private EdsCustomFormLocalization lShort;

    public PropertyItem toItem(boolean brief) {
        PropertyItem item = new PropertyItem();
        item.setId(getObjectID());
        item.setObjectName(getObjectName());
        item.setSingular(getSingular());
        item.setPlural(getPlural());
        item.setShortcut(getShortcut());
        item.setfID(getFid());
        item.setFormID(getFormID());
        item.setCustom(getCustom() != null ? getCustom() : false);
        item.setModule(getModuleCode());
        item.setType(getFormType());

        if (getConvertItems() != null) {
            Gson gson = new Gson();
            item.setConvertItems(gson.fromJson(getConvertItems(), ConvertItem[].class));
        }
        if (brief) {
            item.setDefaultName(getDefaultName());
            item.setModifiedDate(getLastModifiedDate());
            item.setActive(getActive() != null ? getActive() : false);
            if (getUser() != null) {
                item.setModifier(getUser().getName());
            }
        }
        if (getLName() != null) {
            item.setlName(getLName().getRPC());
        }
        if (getlPlural() != null) {
            item.setlPlural(getlPlural().getRPC());
        }
        if (getlShort() != null) {
            item.setlShort(getlShort().getRPC());
        }
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getConvertItems() {
        return convertItems;
    }

    public void setConvertItems(String convertItems) {
        this.convertItems = convertItems;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public EdsCustomFormLocalization getLName() {
        return lName;
    }

    public void setLName(EdsCustomFormLocalization lName) {
        this.lName = lName;
    }

    public EdsCustomFormLocalization getlPlural() {
        return lPlural;
    }

    public void setlPlural(EdsCustomFormLocalization lPlural) {
        this.lPlural = lPlural;
    }

    public EdsCustomFormLocalization getlShort() {
        return lShort;
    }

    public void setlShort(EdsCustomFormLocalization lShort) {
        this.lShort = lShort;
    }
}
