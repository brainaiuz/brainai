package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ExpenseMarkupPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ViewInterface;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/2/12
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SalesInvoiceViewInterface extends ViewInterface {

    void setQuotePercent(BigDecimal quotePercent);

    void setQuoteAmount(BigDecimal quoteAmount);

    void setProgressInvoiceDialogBoxType(String type);

    void applyProgressInvoicingParameters(NewInvoice result);

    void setProgressInvoicingByItem(boolean value);

    void setProgressInvoiciningMap(HashMap<Integer, BigDecimal> valuesMap);

    BigDecimal getTotalInBaseCurrency();

    TextArea2 getPaymentInstruction();

    FormGroup getPriceLevel();

    FormGroup getClientDiscountField();

    WfmDropdown getClientDiscountDropdown();

    TextBox getPreviousBalance();

    TextBox getPaymentsReceived();

    HTML getPayAdjusmentLabel();

    HTML getMessageToUser();

    TextArea getIntroduction();

    DataListBox getBankAccountListBox();

    /*SimpleLink getBankAccountDetailLink();*/

//    FormGroup getAddBillableExpenseField();

    FooterInformer getBillableExpenseButton();

    ExpenseMarkupPopup getExpenseMarkupPopup();

    ShippingLabelDialogBox getShippingLabelDialogBox();

    void setShippingLabelDialogBox(ShippingLabelDialogBox dialogBox);

    boolean isReccuringInvoice();

    RecurringWidget getRecurringWidget();

    DatePicker getPeriodStart();

    DatePicker getPeriodEnd();

    HorizontalPanel getTermsAndDueDateLabel();

    TermsAndDuePanel getTermsAndDuePanel();

    SplitButton getApproveSplitButton();

    String getInvoiceType();

    MaterialLink getCustomerBalanceLink();

    AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp();

    Command onProjectBaseInvoiceInit();

    Property getProperty();

    ChosenApproversWidget getApproverLookUp();

    WfmButton2 getSubmitButton();

    void initWidgetMap(NewInvoice data);
}
