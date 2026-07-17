package com.edatasite.workforce.gwt.core.client.rpc.emailmessage;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: Aug 18, 2010
 * Time: 1:04:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailFolder implements IsSerializable {
    private Integer objectID;
    private Integer parentID;
    private String name;
    private MCFolderType type;
    private boolean fetchable;

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

    public MCFolderType getType() {
        return type;
    }

    public void setType(MCFolderType type) {
        this.type = type;
    }

    public boolean isFetchable() {
        return fetchable;
    }

    public void setFetchable(boolean fetchable) {
        this.fetchable = fetchable;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }
}
