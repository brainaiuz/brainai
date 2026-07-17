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

public class PayInvoiceListView extends BatchPaymentsListView {

    public PayInvoiceListView() {
        super("payBillsList");
        setDescription(property.getPlural(accountingStrings.payInvoices()));
        if (hasPermissionToAdd()) {
            setAddNew("receivepayment|add/add/" + Constants.PAYABLE);
        }
    }

    @Override
    protected ListPanelType getListPanelType() {
        return ListPanelType.BatchPayBillListPanel;
    }

    @Override
    protected String getPdfTemplateType() {
        return AccountingConstants.BATCH_PAY_BILL;
    }

    @Override
    public Object getLayoutData() {
        return Constants.PAYABLE;
    }

    @Override
    protected String getBatchPaymentDataType() {
        return Constants.PAYABLE;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.BatchPayBillView;
    }

    protected String getEditPermission() {
        return PayInvoicePermissionConstants.PAY_INVOICE_EDIT;
    }

    protected String getDeletePermission() {
        return PayInvoicePermissionConstants.PAY_INVOICE_DELETE;
    }

    @Override
    protected String getVoidPermission() {
        return PayInvoicePermissionConstants.PAY_INVOICE_VOID;
    }

    @Override
    protected String getPdfPermission() {
        return PayInvoicePermissionConstants.PAY_INVOICE_PDF;
    }

    @Override
    protected String getSummaryPermission() {
        return PayInvoicePermissionConstants.PAY_INVOICE_SUMMARY;
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

    private SinksContainer addNewItem() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + getBatchPaymentDataType());
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(ACCOUNTING_PAY_BILL);
    }

    public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
        DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYoDoNotHaveAnyPaidBills());
        if (hasPermissionToAdd()) {
            message.setTextBeforeLink(accountingStrings.noPayBillBeforeLinkMessage());
            message.setHref("receivepayment|add/add/" + Constants.PAYABLE);
        }
        emptyDataTable.initEmptyDataTable(message);
    }

    protected String getPdfUrl() {
        return "/batchPayBillViewPDFHandler";
    }

    @Override
    public String getPropertyCode() {
        return Constants.PAYBILLS_LIST;
    }
}
