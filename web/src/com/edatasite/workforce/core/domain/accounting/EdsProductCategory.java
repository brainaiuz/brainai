package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.customfields.EdsProductCategoryCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import org.hibernate.annotations.Type;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 3:45:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "productcategory")
public class EdsProductCategory extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Integer objectID;

    @Column(name = "code")
    private String code;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "active", columnDefinition = " boolean default true")
    private boolean active = true;

    private String name;
    private String description;

    @Type(type = "text")
    @Column(name = "name_localize")
    private String nameLocalize;

    @Type(type = "text")
    @Column(name = "description_localize")
    private String descriptionLocalize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EdsProductCategory parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false'")
    private List<EdsProductCategory> childList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsItem> products = new ArrayList<>();

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @Column(name = "sorder")
    private Integer order = 0;

    @Column(name = "imageid")
    private Integer imageID;

    @Column(name = "lastUpdateDate")
    private Date lastUpdateDate;

    @Column(name = "magentoEntityID")
    private Integer magentoEntityID;

    @Column(name = "magentoSyncDate")
    private Date magentoSyncDate;

    private Boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsProductCategoryCustomFields customFields;


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
        if (!ServerUtils.equalsString(this.name, name)) {
            addChange(CustomFormConstants.NAME);
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsProductCategory getParent() {
        return parent;
    }

    public void setParent(EdsProductCategory parent) {
        this.parent = parent;
    }

    public List<EdsProductCategory> getChildList() {
        return childList;
    }

    public void setChildList(List<EdsProductCategory> childList) {
        this.childList = childList;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsItem> getProducts() {
        return products;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Integer getMagentoEntityID() {
        return magentoEntityID;
    }

    public void setMagentoEntityID(Integer magentoEntityID) {
        this.magentoEntityID = magentoEntityID;
    }

    public Date getMagentoSyncDate() {
        return magentoSyncDate;
    }

    public void setMagentoSyncDate(Date magentoSyncDate) {
        this.magentoSyncDate = magentoSyncDate;
    }

    public EdsProductCategoryCustomFields getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final EdsProductCategoryCustomFields customFields) {
        this.customFields = customFields;
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

    public boolean isActive() {
        return this.active;
    }

    public void setActive(final boolean active) {
        if (!ServerUtils.equalsBoolean(this.active, active)) {
            addChange(CustomFormConstants.ACTIVE);
        }
        this.active = active;
    }

    public String getNameLocalize() {
        return nameLocalize;
    }

    public void setNameLocalize(String nameLocalize) {
        this.nameLocalize = nameLocalize;
    }

    public String getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(String descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }

    public ProductCategoryItem getRPC() {
        ProductCategoryItem items = new ProductCategoryItem();
        items.setId(getObjectID());
        items.setCode(getCode());
        items.setActive(isActive());
        items.setIntNumber(getIntNumber());
        items.setName(getName());
        items.setDescription(getDescription());
        items.setParentCategoryID(getParent() != null ? getParent().getObjectID() : null);
        items.setParentCategoryName(getParent() != null ? getParent().getName() : "");
        items.setPrice(getPrice());
        items.setNameLocalize(WfmJsonUtils.jsonStringConvertToObject(nameLocalize, HashMap.class));
        items.setDescriptionLocalize(WfmJsonUtils.jsonStringConvertToObject(descriptionLocalize, HashMap.class));
        //........
        return items;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    public void addCustomFieldChanges(String changes) {
        if (changes != null && !"".equals(changes)) {
            for (String change : changes.split(",")) {
                if (!"".equals(change.trim())) {
                    addChange(change);
                }
            }
        }
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals("NAME")) {
            return this.getName();
        } else if (fieldID.equals("ORDER")) {
            return this.getOrder();
        } else if (fieldID.equals("CODE")) {
            return this.getCode();
        } else if (fieldID.equals("ACTIVE")) {
            return this.isActive();
        } else if (fieldID.equals("DESCRIPTION")) {
            return this.getDescription();
        } else if (!fieldID.contains("string_value") && !fieldID.contains("double_value") && !fieldID.contains("date_value")) {
            return super.getRealValue(fieldID);
        } else {
            return this.getCustomFields() != null ? CustomFieldsUtils.getObjectValue(this.getCustomFields(), fieldID) : "";
        }
    }
}
