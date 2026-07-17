package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Hayot
 * Date: 8/12/11
 * Time: 5:21 AM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "relation")
public class EdsRelation extends EdsObject {
    public static final String TYPE_PROJECT = RelationItem.TYPE_PROJECT;
    public static final String TYPE_TASK = RelationItem.TYPE_TASK;
    public static final String TYPE_EVENT = RelationItem.TYPE_EVENT;
    public static final String TYPE_CONTACT = RelationItem.TYPE_CONTACT;
    public static final String TYPE_CANDIDATE = RelationItem.TYPE_CANDIDATE;
    public static final String TYPE_LEAD = RelationItem.TYPE_LEAD;
    public static final String TYPE_CRM_ACCOUNT = RelationItem.TYPE_CRM_ACCOUNT;
    public static final String TYPE_OPPORTUNITY = RelationItem.TYPE_OPPORTUNITY;
    public static final String TYPE_CASE = RelationItem.TYPE_CASE;
    public static final String TYPE_EMAIL_TRACKER = RelationItem.TYPE_EMAIL_TRACKER;
    public static final String TYPE_ISSUE = RelationItem.TYPE_ISSUE;
    public static final String TYPE_SALE_QUOTE = RelationItem.TYPE_SALEQUOTE;
    public static final String TYPE_SALE_INVOICE = RelationItem.TYPE_SALEINVOICE;
    public static final String TYPE_PRODUCT = RelationItem.TYPE_PRODUCT;
    public static final String TYPE_CLIENT = RelationItem.TYPE_CLIENT;
    public static final String TYPE_SUPPLIER = RelationItem.TYPE_SUPPLIER;
    public static final String TYPE_EMPLOYEE = RelationItem.TYPE_EMPLOYEE;
    public static final String TYPE_DEPARTMENT = RelationItem.TYPE_DEPARTMENT;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer fromID;

    private String fromType;

    private String fromName;

    private Integer toID;

    private String toType;

    private String toName;

    @Column(name = "entityid")
    private Integer entityID;

    private Date createdDate = new Date();

    private Date lastModifiedDate = new Date();

    public EdsRelation(RelationItem relationItem) {
        this();
        wrapRpcTo(relationItem);
    }

    public EdsRelation() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer relationID) {
        this.fromID = relationID;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String relationType) {
        this.fromType = relationType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        if (fromName != null && fromName.length() > 255) {
            fromName = fromName.substring(0, 254);
        }
        this.fromName = fromName;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer entityID) {
        this.toID = entityID;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String entityType) {
        this.toType = entityType;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        if (toName != null && toName.length() > 255) {
            toName = toName.substring(0, 254);
        }
        this.toName = toName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public RelationItem wrapToRPC() {
        RelationItem item = new RelationItem(getObjectID(), getToID(), getToType(), getToName(), getFromID(), getFromType(), getFromName());
        item.setCreatedDate(getCreatedDate());
        item.setLastModifiedDate(getLastModifiedDate());
        return item;
    }

    public static ArrayList<RelationItem> wrapToRPCs(List<EdsRelation> list) {
        ArrayList<RelationItem> items = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (EdsRelation relation : list) {
                if (relation != null) {
                    items.add(relation.wrapToRPC());
                }
            }
        }
        return items;
    }

    public void wrapRpcTo(RelationItem relationItem) {
        setToID(relationItem.getToID());
        setToType(relationItem.getToType());
        setFromID(relationItem.getFromID());
        setFromType(relationItem.getFromType());
        setFromName(relationItem.getFromName());
        setToName(relationItem.getToName());
    }

    public static ArrayList<RelationItem> asRPCs(List<EdsRelation> relations) {
        ArrayList<RelationItem> rpcs = new ArrayList<>();
        if (relations != null && relations.size() > 0) {
            for (EdsRelation relation : relations) {
                rpcs.add(relation.wrapToRPC());
            }
        }
        return rpcs;
    }

    public static String getTrackerIDsByRelationQuery(Integer relationID, String relationType, String neededRelationType) {
        String companyID = "\"" + SecurityContext.getCompanyID() + "\"";
        return "select distinct r1.fromid trackerID from " + companyID + ".relation r1 where r1.fromtype = '" + neededRelationType + "' and r1.toid = " + relationID + " and r1.totype ='" + relationType + "' " +
                "    union select distinct r2.toid trackerID from " + companyID + ".relation r2 where r2.totype = '" + neededRelationType + "' and r2.fromid = " + relationID + " and r2.fromtype ='" + relationType + "'";
    }

    public String getNameByType(String relationType, boolean viceVersa) {
        if (relationType == null) {
            return null;
        }
        if (relationType.equalsIgnoreCase(getToType())) {
            return viceVersa ? getFromName() : getToName();
        }
        if (relationType.equalsIgnoreCase(getFromType())) {
            return viceVersa ? getToName() : getFromName();
        }
        return null;
    }

    public Integer getIDByType(String relationType, boolean viceVersa) {
        if (relationType == null) {
            return null;
        }
        if (relationType.equalsIgnoreCase(getToType())) {
            return viceVersa ? getFromID() : getToID();
        }
        if (relationType.equalsIgnoreCase(getFromType())) {
            return viceVersa ? getToID() : getFromID();
        }
        return null;
    }
}
