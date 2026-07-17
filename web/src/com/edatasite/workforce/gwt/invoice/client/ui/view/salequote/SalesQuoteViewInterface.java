package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ViewInterface;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/1/12
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SalesQuoteViewInterface extends ViewInterface {

    boolean isSalesOrder();

    ChosenApproversWidget getApproverLookUp();

    TextArea2 getPaymentInstruction();

    TextBox getPONumberTxtBox();

    FormGroup getPriceLevel();

    TextArea getIntroduction();

    WfmButton2 getSubmitToManagerButton();

    WfmButton2 getSalesOrderButton();

    SplitButton getApproveSplitButton();

    void createComissionAllocateItem();

    LookUp getLeadLookUp();

    SimpleLink getBankAccountDetailLink();

    MaterialLink getCustomerBalanceLink();

    Double getCustomerBalance();

    void setCustomerBalance(Double balance);

    DataListBox getBankAccountListBox();

    HorizontalPanel getTermsAndDueDateLabel();

    TermsAndDuePanel getTermsAndDuePanel();

    Property getProperty();

    void initWidgetMap(NewInvoice data);

    LookUp getSupplierLookUp();

    KpiCheckBox progressInvoicing();

    HasLinks getLinkingUtils();

    FooterInformer getLinkWidget();
}
