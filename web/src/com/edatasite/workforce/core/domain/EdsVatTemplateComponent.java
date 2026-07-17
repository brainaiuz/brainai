package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 21.06.2010
 * Time: 21:21:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "vattemplatecomponent")
public class EdsVatTemplateComponent extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private Boolean compound;
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vattemplateid")
    private EdsVatTemplate vatTemplate;

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

    public Boolean getCompound() {
        return compound;
    }

    public void setCompound(Boolean compound) {
        this.compound = compound;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public EdsVatTemplate getVatTemplate() {
        return vatTemplate;
    }

    public void setVatTemplate(EdsVatTemplate vatTemplate) {
        this.vatTemplate = vatTemplate;
    }
}
