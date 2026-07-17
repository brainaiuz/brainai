package com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote;

import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ViewInterface;
import com.google.gwt.user.client.ui.TextArea;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/16/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CreditNoteViewInterface extends ViewInterface {
    SplitButton getApproveSplitButton();

    TextArea getIntroduction();

    SimpleLink getBankAccountDetailLink();

    DataListBox getBankAccountListBox();

    AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp();

    MaterialLink getSupplierBalanceLink();

    FormGroup getReverseChargeField();

    FormGroup getPriceLevel();

    Integer validateReason();

    Integer validatePaymentTypeCode();

    void initWidgetMap(NewInvoice data);

    WfmButton2 getSubmitButton();

    ChosenApproversWidget getApproverLookUp();

}
