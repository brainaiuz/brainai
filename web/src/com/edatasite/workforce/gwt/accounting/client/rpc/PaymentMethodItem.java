package com.edatasite.workforce.gwt.accounting.client.rpc;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Omonullo Abdullaev on 08.04.16.
 */
public class PaymentMethodItem implements Serializable{

    public static final String ACTION = "action";

    public static final String OBJECT_ID = "objectID";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CODE = "code";
    public static final String SORT_ORDER = "sortOrder";
    public static final String WEIGTH = "weigth";           //I know WEIGTH is written with a mistake, but that's not my fault
    public static final String LAST_USED = "lastUsed";

    private Integer objectID;
    private String name;
    private String description;
    private String code;
    private Integer sortOrder;
    private Integer weigth;
    private Date lastused;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getWeigth() {
        return weigth;
    }

    public void setWeigth(Integer weigth) {
        this.weigth = weigth;
    }

    public Date getLastused() {
        return lastused;
    }

    public void setLastused(Date lastused) {
        this.lastused = lastused;
    }
}
