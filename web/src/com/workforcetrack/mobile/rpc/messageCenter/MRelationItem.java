package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 14.09.11
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MRelationItem {

    private Integer objectID;
    private Integer fromID;
    private String fromType;
    private String fromName;
    private Integer toID;
    private String toType;
    private String toName;

    private Boolean remove;
/*    private Date createdDate;
    private Date lastModifiedDate;*/

    public MRelationItem() {

    }

    public MRelationItem(RelationItem item) {
        if (item != null) {
            this.objectID = item.getObjectID();
            this.fromID = item.getFromID();
            this.fromType = item.getFromType();
            this.fromName = item.getFromName();
            this.toID = item.getToID();
            this.toType = item.getToType();
            this.toName = item.getToName();
        }
    }

    public static List<MRelationItem> convertToMobile(ArrayList<RelationItem> items) {
        if (items != null && items.size() > 0) {
            List<MRelationItem> relationItems = new ArrayList<>();
            for (RelationItem item : items) {
                relationItems.add(new MRelationItem(item));
            }
            return relationItems;
        }
        return null;
    }

    public RelationItem convertFromMobile(RelationItem item) {
        if (item == null) {
            item = new RelationItem();
        }
        item.setObjectID(getObjectID());
        item.setFromID(getFromID());
        item.setFromType(getFromType());
        item.setFromName(getFromName());
        item.setToID(getToID());
        item.setToType(getToType());
        item.setToName(getToName());
        item.setRemove(getRemove() != null ? getRemove() : false);

        return item;
    }

    public static ArrayList<RelationItem> convertFromToMobile(List<MRelationItem> items) {
        if (items != null && items.size() > 0) {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (MRelationItem item : items) {
                relationItems.add(item.convertFromMobile(null));
            }
            return relationItems;
        }
        return null;
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
