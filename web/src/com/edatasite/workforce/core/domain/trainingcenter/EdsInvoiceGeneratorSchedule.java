package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Normurod on 3/23/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoice_generator_schedule")
public class EdsInvoiceGeneratorSchedule extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private InvoiceGeneratorStatus status;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public InvoiceGeneratorStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceGeneratorStatus status) {
        this.status = status;
    }
}
