package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;

/**
 * User: faxriddin * Date: 27.01.2016
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "embassy")
public class EdsEmbassy extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCountry country;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    private String code;

    private Integer sorder;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public static ArrayList<TreeSelectItem> getEmbassyAsSelectItems(ArrayList<EdsEmbassy> embasies) {
        if (embasies != null && embasies.size() > 0) {
            ArrayList<TreeSelectItem> result = new ArrayList<>();
            for (EdsEmbassy embassy : embasies) {
                result.add(embassy.getAsTreeSelectItem());
            }
            if (result.size() > 0) {
                return result;
            }
        }
        return null;

    }

    public ReferenceItem getRPC() {
        ReferenceItem item = new ReferenceItem(getObjectID(), getName(), getDescription());
        item.setCode(getCode());
        if (getCountry() != null) {
            item.setParentID(getCountry().getObjectID());
            item.setParent(getCountry().getName());
        }
        item.setOrder(getSorder());
        return item;
    }
}
