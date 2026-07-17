package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentNote;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface AdditionalPaymentNoteManager extends Manager<EdsAdditionalPaymentNote> {
    List<EdsAdditionalPaymentNote> getAdditionalPaymentNote(EdsAdditionalPayment additionalPayment);
}
