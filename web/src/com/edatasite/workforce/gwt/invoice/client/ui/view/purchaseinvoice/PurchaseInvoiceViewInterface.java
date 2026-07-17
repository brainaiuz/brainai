package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ViewInterface;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/3/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PurchaseInvoiceViewInterface extends ViewInterface {
    Integer getDefaultLocationsId(Map<Integer, String> map);

    boolean isReccuringInvoice();

    RecurringWidget getRecurringWidget();

    HorizontalPanel getTermsAndDueDateLabel();

    TermsAndDuePanel getTermsAndDueDatePanel();

    MaterialLink getSupplierBalanceLink();

    AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp();

    FormGroup getPriceLevel();

    Property getProperty();

    ChosenApproversWidget getApproverLookUp();

    WfmButton2 getSubmitButton();

    void initWidgetMap(NewInvoice data);

    WfmButton2 getManagerApproveButton();
}
