package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBankTransferNote;
import com.edatasite.workforce.gwt.core.server.db.BankTransferNoteManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by User on 04.04.16.
 */
@Repository("bankTransferNoteManager")
public class BankTransferNoteManagerImpl extends BaseManager<EdsBankTransferNote> implements BankTransferNoteManager {
    public BankTransferNoteManagerImpl() {
        super(EdsBankTransferNote.class);
    }

    @Override
    public List<EdsBankTransferNote> getBankTransferNotes(Integer ObjectID) {
        return find("select btn from EdsBankTransferNote btn where btn.bankTransfer.objectID=? order by btn.date desc", ObjectID);
    }

    @Override
    public void deleteBankTransferNotes(Integer ObjectID) {
        update("delete from EdsBankTransferNote where bankTransfer.objectID = ?", ObjectID);
    }
}
