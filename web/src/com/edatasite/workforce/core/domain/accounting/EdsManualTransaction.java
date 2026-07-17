package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 12, 2009
 * Time: 4:12:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "manualtransaction")
public class EdsManualTransaction extends EdsTransaction {

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manualjournalid")
    private EdsManualJournal manualJournal;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsManualJournal getManualJournal() {
        return manualJournal;
    }

    public void setManualJournal(EdsManualJournal manualJournal) {
        this.manualJournal = manualJournal;

        setCurrencyID(manualJournal.getCurrency() != null ? manualJournal.getCurrency().getObjectID() : null);
        setExchangeRate(manualJournal.getExchangeRate());
    }

    public Integer getKeyId() {
        if (getManualJournal() != null){
            return getManualJournal().getObjectID();
        } else{
            return getObjectID();
        }
    }

    public String getKeyType() {
        return MANUAL_TRANSACTION;
    }
}
