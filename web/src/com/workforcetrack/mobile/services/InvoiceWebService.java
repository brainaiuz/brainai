package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.accounting.MClientSupplierAddressData;
import com.workforcetrack.mobile.rpc.accounting.MInvoiceList;
import com.workforcetrack.mobile.rpc.accounting.MInvoiceListItem;
import com.workforcetrack.mobile.rpc.accounting.MInvoiceNumberData;
import com.workforcetrack.mobile.rpc.accounting.MMessageItem;
import com.workforcetrack.mobile.rpc.accounting.MProductsByTypeList;
import com.workforcetrack.mobile.rpc.accounting.MTaxList;
import com.workforcetrack.mobile.rpc.accounting.MTypeItemList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.expense.MCurrencyList;
import com.workforcetrack.mobile.rpc.expense.MEmailTemplateItem;
import com.workforcetrack.mobile.rpc.expense.MEmailTemplateList;
import com.workforcetrack.mobile.rpc.expense.MEntityToEmailTemplate;
import com.workforcetrack.mobile.rpc.project.MClientList;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 5/23/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InvoiceWebService {

    MInvoiceNumberData getSaleInvoiceNumber();

    MTypeItemList getSuppliers(String searchKey);

    MTypeItemList getClients(String searchKey);

    MClientList getClients(MFilterParametrs mFilterParametrs);

    MClientSupplierAddressData getClientAddressData(Integer clientID);

    MCurrencyList getCurrencies();

    MTaxList getCompanyTaxList(MFilterParametrs mFilterParametrs);

    MTaxList getCompanyTaxList();

    BigDecimal getExchangeRate(String to);

    MProductsByTypeList getProducts(MFilterParametrs mFilterParametrs);

    Boolean sendToClient(MMessageItem messageItem);

    //CRUD
    MInvoiceList getPurchaseInvoiceList(MFilterParametrs mFilterParametrs);

    MInvoiceList getSaleInvoiceList(MFilterParametrs mFilterParametrs);

    MInvoiceListItem get(Integer objectID);

    MInvoiceListItem editSaleInvoice(Integer objectID);

    MInvoiceListItem editSaleInvoice();

    //The type may be one of these: Constants.SALE_INVOICE, Constants.PURCHASE_INVOICE, Constants.RECURRING_INVOICE
    Boolean delete(Integer objectID, String type);

    MSelectItem saveSaleInvoice(MInvoiceListItem data);

    Integer saveSaleInvoiceID(MInvoiceListItem data);

    Boolean savePurchaseInvoice(MInvoiceListItem data);

    MInvoiceListItem getAllInvoiceData();

    MEmailTemplateList getEmailTemplates(String templcatCategoryCode);

    MEmailTemplateItem generateEmailTemplateData(MEntityToEmailTemplate emailTemplate);


//    Integer updateSaleInvoice(MInvoiceListItem data);
//
//    Integer updatePurchaseInvoice(MInvoiceListItem data);

    MInvoiceList getSaleQuoteList(MFilterParametrs mFilterParametrs);

    MInvoiceListItem getSaleQuoteItem(Integer objectId);

    Boolean deleteSalesQuote(Integer objectID);

    MInvoiceNumberData getQuoteNumber();

    MSelectItem saveSalesQuote(MInvoiceListItem data);

    MInvoiceListItem editSalesQuote(Integer objectID);

    Integer convertQuoteToInvoice(Integer quoteId);
}
