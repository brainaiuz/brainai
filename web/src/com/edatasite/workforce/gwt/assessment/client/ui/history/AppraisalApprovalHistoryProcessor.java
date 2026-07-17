package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.AppraisalApprovalSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Sherali Pirnafasov
 */
public class AppraisalApprovalHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AppraisalApprovalSinksContainer(containerName + strings[0], "Appraisal Approval", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}