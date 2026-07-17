package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsBrandCustomFields;

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

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 2:27:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "brand")
public class EdsBrand extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EdsBrand parentBrand;

    @Column(name = "imageid")
    private Integer imageID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsBrandCustomFields customFields;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsBrand getParentBrand() {
        return parentBrand;
    }

    public void setParentBrand(EdsBrand parentBrand) {
        this.parentBrand = parentBrand;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsBrandCustomFields getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final EdsBrandCustomFields customFields) {
        this.customFields = customFields;
    }
}
