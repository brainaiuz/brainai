package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.user.client.Command;

/**
 * Created By Omonullo
 */

public class ReceivePaymentListView extends BatchPaymentsListView {

    public ReceivePaymentListView() {
        super("receivePaymentsList");
        setDescription(property.getPlural(accountingStrings.receivePayments()));
        if (hasPermissionToAdd()) {
            setAddNew("receivepayment|add/add/" + Constants.RECEIVABLE);
        }
    }

    @Override
    protected ListPanelType getListPanelType() {
        return ListPanelType.BatchInvoicePaymentListPanel;
    }

    @Override
    protected String getPdfTemplateType() {
        return AccountingConstants.BATCH_RECEIVE_PAYMENT;
    }

    @Override
    protected String getBatchPaymentDataType() {
        return Constants.RECEIVABLE;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.BatchInvoicePaymentView;
    }

    protected String getEditPermission() {
        return ReceivePaymentPermissionConstants.RECEIVE_PAYMENT_EDIT;
    }

    protected String getDeletePermission() {
        return ReceivePaymentPermissionConstants.RECEIVE_PAYMENT_DELETE;
    }

    @Override
    protected String getVoidPermission() {
        return ReceivePaymentPermissionConstants.RECEIVE_PAYMENT_VOID;
    }

    @Override
    protected String getPdfPermission() {
        return ReceivePaymentPermissionConstants.RECEIVE_PAYMENT_PDF;
    }

    @Override
    protected String getSummaryPermission() {
        return ReceivePaymentPermissionConstants.RECEIVE_PAYMENT_SUMMARY;
    }

    @Override
    protected Command getNewItemAddCommand() {
        return hasPermissionToAdd() ? this::addNewItem : null;
    }

    public ActionButton initTopToolBarNew() {
        if (hasPermissionToAdd()) {
            ActionButton addNew = getAddNewButton();
            addNew.addClickHandler(clickEvent -> addNewItem());
            return addNew;
        }
        return null;
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT);
    }

    private SinksContainer addNewItem() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + getBatchPaymentDataType());
    }

    public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
        DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyInvoicePayments());
        emptyDataTable.initEmptyDataTable(message);
    }

    protected String getPdfUrl() {
        return "/batchReceivePaymentViewPDFHandler";
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.BATCH_RECEIVE_PAYMENT;
    }
}
