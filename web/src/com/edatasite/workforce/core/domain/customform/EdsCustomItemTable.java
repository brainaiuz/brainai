package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCustomItemTableCF;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import org.hibernate.annotations.Type;

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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "custom_item_table")
public class EdsCustomItemTable extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "product_number")
    private String productNumber;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "name", length = 1000)
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    private String uuid;

    private Integer sorder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_item_id")
    private EdsCustomFormItems formItem;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsCustomItemTableCF customFields;

    public CustomTableRpc getRpc() {
        CustomTableRpc rpc = new CustomTableRpc();
        rpc.setId(getObjectID());
        rpc.setSorder(getSorder());
        rpc.setUuid(getUuid());
        rpc.setItemNumber(getProductNumber());
        rpc.setItemName(getName());
        rpc.setDescription(getDescription());
        return rpc;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    @Override
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EdsCustomFormItems getFormItem() {
        return formItem;
    }

    public void setFormItem(EdsCustomFormItems formItem) {
        this.formItem = formItem;
    }

    public EdsCustomItemTableCF getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCustomItemTableCF customFields) {
        this.customFields = customFields;
    }

    public Integer getSorder() {
        return sorder == null ? 1 : sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
