package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.LinkedList;

public class PropertyItem implements IsSerializable {

    public static final String DEFAULT_NAME = "default_name";
    public static final String CUSTOM_NAME = "custom_name";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String MODIFIER = "modifier";
    public static final String STATUS = "STATUS";
    public static final String SINGULAR = "SINGULAR";
    public static final String PLURAL = "PLURAL";
    public static final String SHORT_NAME = "SHORT_NAME";
    private Integer id;
    private String defaultName;
    private String singular;
    private String plural;
    private String shortcut;
    private Date modifiedDate;
    private String modifier;
    private boolean active;
    private Integer fID;
    private String formID;
    private String objectName;
    private boolean isCustom;
    private ConvertItem[] convertItems;
    private SelectItem container;
    private Integer containerItemId;
    private Integer sorder;
    private boolean activeModule;
    private String module;
    private String type;
    private Integer selectedItemID;
    private LinkedList<SelectItem> sections;
    private String link;
    private String linkWithAccess;
    private CustomFormLocalization lName;
    private CustomFormLocalization lPlural;
    private CustomFormLocalization lShort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getSingular() {
        String result = null;
        if (getlName() != null) {
            result = getlName().getLocalizedName();
        }
        return result != null && !result.isEmpty() ? result : singular;
    }

    public String getSingularMain() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        String result = null;
        if (getlPlural() != null) {
            result = getlPlural().getLocalizedName();
        }
        return result != null && !result.isEmpty() ? result : plural;
    }

    public String getPluralMain() {
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getfID() {
        return fID;
    }

    public void setfID(Integer fID) {
        this.fID = fID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public ConvertItem[] getConvertItems() {
        return this.convertItems;
    }

    public void setConvertItems(final ConvertItem[] convertItems) {
        this.convertItems = convertItems;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    public Integer getSorder() {
        return this.sorder;
    }

    public void setSorder(final Integer sorder) {
        this.sorder = sorder;
    }

    public boolean isActiveModule() {
        return this.activeModule;
    }

    public void setActiveModule(final boolean activeModule) {
        this.activeModule = activeModule;
    }

    public SelectItem getContainer() {
        return this.container;
    }

    public void setContainer(final SelectItem container) {
        this.container = container;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public Integer getContainerItemId() {
        return this.containerItemId;
    }

    public void setContainerItemId(final Integer containerItemId) {
        this.containerItemId = containerItemId;
    }

    public LinkedList<SelectItem> getSections() {
        return this.sections;
    }

    public void setSections(final LinkedList<SelectItem> sections) {
        this.sections = sections;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getSelectedItemID() {
        return this.selectedItemID;
    }

    public void setSelectedItemID(final Integer selectedItemID) {
        this.selectedItemID = selectedItemID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkWithAccess() {
        return linkWithAccess;
    }

    public void setLinkWithAccess(String linkWithAccess) {
        this.linkWithAccess = linkWithAccess;
    }

    public CustomFormLocalization getlName() {
        return lName;
    }

    public void setlName(CustomFormLocalization lName) {
        this.lName = lName;
    }

    public CustomFormLocalization getlPlural() {
        return lPlural;
    }

    public void setlPlural(CustomFormLocalization lPlural) {
        this.lPlural = lPlural;
    }

    public CustomFormLocalization getlShort() {
        return lShort;
    }

    public void setlShort(CustomFormLocalization lShort) {
        this.lShort = lShort;
    }
}
