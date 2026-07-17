package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBankTransferNote;

import java.util.List;

/**
 * Created by User on 04.04.16.
 */
public interface BankTransferNoteManager extends Manager<EdsBankTransferNote> {
    List<EdsBankTransferNote> getBankTransferNotes(Integer ObjectID);
    void deleteBankTransferNotes(Integer ObjectID);
}
