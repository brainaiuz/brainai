package com.edatasite.workforce.core.domain.fifo;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "processed_event")
public class EdsProcessedEvent extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "entity_id")
    private Integer entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private EntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    public EdsProcessedEvent() {
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer id) {
        this.objectID = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
