package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrashBinListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "thrashbin")
public class EdsTrashBin extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer entityID;
    private String entityType;
    private String reference;

    private String status = Constants.TRASH_BIN_PENDING;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TrashBinListItem createRPCItem() {
        TrashBinListItem item = new TrashBinListItem();
        item.setObjectID(getObjectID());
        item.setReference(getReference());
        item.setType(getEntityType());
        item.setStatus(getStatus());
        return item;
    }
}
