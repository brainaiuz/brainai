package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 3, 2010
 * Time: 5:47:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryItem implements ListingCustomFields, IsSerializable {

    public static String ACTION = "action";
    public static String NAME = "name";
    public static String DESCRIPTION = "description";
    public static String PARENT = "parent";
    public static String STATUS = "status";
    public static String NUMBER = "number";
    private Integer id;
    private String code;
    private boolean active;
    private Integer codeId;
    private String prefix;
    private Integer intNumber;
    private String name;
    private Integer nameId;
    private String description;
    private Integer parentCategoryID;
    private String parentCategoryName;
    private Integer[] storeFrontIDs;
    private Integer[] websiteIDs;
    private Integer storeFrontID;
    private BigDecimal price;
    private Integer order = 0;
    private HashMap<String, String> nameLocalize;
    private HashMap<String, String> descriptionLocalize;

    private Boolean copyCustomFieldsFromParent = false;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;

    private String imageUrl;
    private Integer imageID;
    private ArrayList<CompanyCustomFieldItem> categoryCustomFields;

    public ProductCategoryItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(Integer parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public Integer[] getStoreFrontIDs() {
        return storeFrontIDs;
    }

    public void setStoreFrontIDs(Integer[] storeFrontIDs) {
        this.storeFrontIDs = storeFrontIDs;
    }

    public Integer getStoreFrontID() {
        return storeFrontID;
    }

    public void setStoreFrontID(Integer storeFrontID) {
        this.storeFrontID = storeFrontID;
    }

    public Integer[] getWebsiteIDs() {
        return websiteIDs;
    }

    public void setWebsiteIDs(Integer[] websiteIDs) {
        this.websiteIDs = websiteIDs;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Boolean isCopyCustomFieldsFromParent() {
        return copyCustomFieldsFromParent != null;
    }

    public void setCopyCustomFieldsFromParent(Boolean copyCustomFieldsFromParent) {
        this.copyCustomFieldsFromParent = copyCustomFieldsFromParent;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public ArrayList<CompanyCustomFieldItem> getCategoryCustomFields() {
        return this.categoryCustomFields;
    }

    public void setCategoryCustomFields(final ArrayList<CompanyCustomFieldItem> categoryCustomFields) {
        this.categoryCustomFields = categoryCustomFields;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap() != null ? getCustomFieldsMap().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = (HashMap<String, Object>) customFieldsMap;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public HashMap<String, String> getNameLocalize() {
        return nameLocalize;
    }

    public void setNameLocalize(HashMap<String, String> nameLocalize) {
        this.nameLocalize = nameLocalize;
    }

    public HashMap<String, String> getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(HashMap<String, String> descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }
}
