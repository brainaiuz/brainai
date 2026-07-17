package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 18, 2009
 * Time: 4:51:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "vattemplate")
public class EdsVatTemplate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Column(precision = 11, scale = 2)
    private BigDecimal vatAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCountry country;

    //This is for UK company only
    // SALES = 1, PURCHASES = 2, EXEMPT_SALES = 3, EXEMPT_PURCHASES = 4, EC_SALES = 5, EC_PURCHASES = 6
    private Integer taxType;

    //This is for edit/delete taxes  1 = NON_DELETABLE, 2 = NON_EDITABLE/NON_DELETABLE, null = EDITABLE/DELETABLE
    private Integer permissionType;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "vattemplateid")
    private Set<EdsVatTemplateComponent> components = new HashSet<>();

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
        this.name = name;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public Set<EdsVatTemplateComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<EdsVatTemplateComponent> components) {
        this.components = components;
    }
}
