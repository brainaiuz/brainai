package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentNote;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentNoteManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("additionalPaymentManagerNoteImpl")
public class AdditionalPaymentNoteImpl extends BaseManager<EdsAdditionalPaymentNote> implements AdditionalPaymentNoteManager {

    public AdditionalPaymentNoteImpl() {
        super(EdsAdditionalPaymentNote.class);
    }

    @Override
    public List<EdsAdditionalPaymentNote> getAdditionalPaymentNote(EdsAdditionalPayment additionalPayment) {
        if (additionalPayment == null) {
            return new ArrayList<>();
        }
        return find("select adp from EdsAdditionalPaymentNote adp where adp.payment=? order by adp.date desc", additionalPayment);
    }
}
