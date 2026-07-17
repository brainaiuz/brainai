package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingEmployeeLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ViewInterface;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/3/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PurchaseOrderViewInterface extends ViewInterface {

    WfmDropdown getPaymentTypeDropdown();

    TextBox getShippingTerms();

    TextBox getPaymentTerms();

    TextArea2 getTermsConditions();

    Integer getDefaultLocationsId(Map<Integer, String> map);

    AccountingEmployeeLookUp getManagerLookUp();

    Widget getManagerBox();

    WfmButton2 getSubmitToManagerButton();

    SplitButton getApproveSplitButton();

    HorizontalPanel getTermsAndDueDateLabel();

    TermsAndDuePanel getTermsAndDueDatePanel();

    FormGroup getPriceLevel();

    MaterialLink getSupplierBalanceLink();

    FormGroup getReverseChargeField();

    Property getProperty();

    ChosenApproversWidget getApproverLookUp();

    void initWidgetMap(NewInvoice data);

    FooterInformer getLinkWidget();

    HasLinks getLinkingUtils();

    HTML getMessageToUser();

}
