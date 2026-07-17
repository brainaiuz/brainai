package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Abror Abdukadirov
 * Date: 14.12.2016 19:58
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "overdue_invoice_reminder_settings")
public class EdsOverdueInvoiceReminderSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private EdsRole role;

    @Column(name = "recurrence_id")
    private Integer recurrenceId;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsRole getRole() {
        return role;
    }

    public void setRole(EdsRole role) {
        this.role = role;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }
}
