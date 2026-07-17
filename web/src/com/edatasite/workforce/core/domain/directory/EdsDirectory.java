package com.edatasite.workforce.core.domain.directory;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Oct 6, 2010
 * Time: 12:07:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "directory")
public class EdsDirectory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String description;
    private Integer sortOrder;
    private boolean isMarketBased;
    private Integer parentId;


    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrption() {
        return description;
    }

    public void setDescrption(String descrption) {
        this.description = descrption;
    }

    public Integer getOrder() {
        return sortOrder;
    }

    public void setOrder(Integer order) {
        this.sortOrder = order;
    }

    public boolean getisMarketBased() {
        return this.isMarketBased;
    }

    public void setisMarketBased(Boolean isMarketBased) {
        this.isMarketBased = isMarketBased;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
