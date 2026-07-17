package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * User: Aziz
 * Date: 29.06.2011
 */
@Service("invoiceWebService")
public class InvoiceWebServiceImpl implements InvoiceWebService, Constants {

    @Autowired
    private QuoteService quoteService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;


    @Override
    public MInvoiceNumberData getSaleInvoiceNumber() {
        return new MInvoiceNumberData(invoiceServiceLocal.getSaleInvoiceNumber());
    }

    @Override
    public MTypeItemList getSuppliers(String searchKey) {
        return new MTypeItemList(invoiceService.getSuppliers(searchKey));
    }

    @Override
    public MTypeItemList getClients(String searchKey) {
        return new MTypeItemList(invoiceService.getClients(searchKey));
    }

    @Override
    public MClientList getClients(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            mFilterParametrs = new MFilterParametrs();
        }

        ListingFilterParameter fp = mFilterParametrs.convertToFilterParametrs();
        SelectItem[] clientItems = projectService.searchClientsByProjectId(null, fp.getSearchKey());

       /* fp.setAccountType(CrmAccountItem.CUSTOMER);
        fp.setSearchByParent(true);
        SelectItem[] clientItems = allInOneService.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, fp);
*/

        return new MClientList(clientItems);
    }

    @Override
    public MClientSupplierAddressData getClientAddressData(Integer clientID) {
        //TypeItem clientTypeItem = invoiceService.getClientOrSupplier(clientID);
        if (clientID == null || clientID < 0) {
            return null;
        }

        EdsCrmAccount crmAccount = crmAccountManager.get(clientID);
        if (crmAccount == null || crmAccount.isDeleted()) {
            return null;
        }
        ClientSupplierAddressData clientAddressData = clientService.getAddressData(clientID, true, Address.EntityType.CrmAccount);
        MClientSupplierAddressData addressData = new MClientSupplierAddressData(clientAddressData);
        TypeItem typeItem = invoiceService.getClientOrSupplier(clientID, Constants.RECEIVABLE);
        addressData.setCode(typeItem.getCode());
        return addressData;
    }

    @Override
    public MCurrencyList getCurrencies() {
        return new MCurrencyList(invoiceService.getCurrencies(null));
    }

    @Override
    public MTaxList getCompanyTaxList(MFilterParametrs mFilterParametrs) {
        return new MTaxList(invoiceService.getCompanyTaxList());
    }

    @Override
    public MTaxList getCompanyTaxList() {
        return new MTaxList(invoiceService.getCompanyTaxList());
    }

    @Override
    public BigDecimal getExchangeRate(String to) {
        return invoiceService.getExchangeRate(to);
    }

    @Override
    public MProductsByTypeList getProducts(MFilterParametrs mFilterParametrs) {
        ListingFilterParameter filterParametrs = mFilterParametrs.convertToFilterParametrs();
        filterParametrs.setListLoadConfig(mFilterParametrs.convertToListLoadConfig());
        filterParametrs.setLookUp(true);
        filterParametrs.setInvoiceType(RECEIVABLE);
        //filterParametrs.setShowOnOpportunity(true);
        SelectItem[] products = productServiceLocal.getCompanyProductsByType(filterParametrs);

        return new MProductsByTypeList(products);
    }

    @Override
    public Boolean sendToClient(MMessageItem messageItem) {
        try {
            MessageItem message = messageItem.convertToMessageItem();
            message.setType(SALES_INVOICE_CATEGORY);
            invoiceService.sendToClient(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MInvoiceList getPurchaseInvoiceList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        ListLoadConfig listLoadConfig = new ListLoadConfig();
        MFilterParametrs.convert(filterParametrs, mFilterParametrs, false);
        MFilterParametrs.convertToListLoadConfig(listLoadConfig, mFilterParametrs, false);
        ListResult<NewInvoice> invoiceList = invoiceService.getPurchaseInvoiceData(filterParametrs);
        return new MInvoiceList(invoiceList);
    }

    @Override
    public MInvoiceList getSaleInvoiceList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter listingFilterParameter = mFilterParametrs.convertToListingFilterParameter(null);
        InvoiceList invoiceList = invoiceService.getSaleInvoiceData(listingFilterParameter);
        return new MInvoiceList(invoiceList);
    }

    @Override
    public MInvoiceListItem get(Integer objectID) {
        if (objectID == null) {
            return null;
        }

        NewInvoice invoiceItem = invoiceService.getInvoice(objectID);
        return new MInvoiceListItem(invoiceItem);
    }

    @Override
    public MInvoiceListItem editSaleInvoice(Integer objectID) {
        Params params = new Params();
        params.setFromGettingStarted(false);
        params.setType(RECEIVABLE);
        params.setObjectID(objectID);
        NewInvoice newInvoice = invoiceService.getAllInvoiceData(params);
        MInvoiceListItem invoiceListItem = new MInvoiceListItem(newInvoice);
        invoiceListItem.setClientAddressData(getClientAddressData(invoiceListItem.getClientID()));

        return invoiceListItem;
    }

    @Override
    public MInvoiceListItem editSaleInvoice() {
        return editSaleInvoice(null);
    }

    @Override
    public Boolean delete(Integer objectID, String type) {
        if (objectID == null || type == null) {
            return false;
        }

        try {
            invoiceService.deleteInvoice(objectID, type);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MSelectItem saveSaleInvoice(MInvoiceListItem mItem) {
        NewInvoice newInvoice;
        try {
            newInvoice = new NewInvoice();

            if (mItem.getBillAddressID() == null) {
                Address addressData = new Address();
                mItem.setBillAddressID(clientService.saveAddress(mItem.getAdressData().convertToAD(addressData), mItem.getClientID(), false, true, Address.EntityType.CrmAccount));
            }

            if (mItem.getObjectID() != null && !mItem.getObjectID().equals(0)) {

                SaveResult saveResult = invoiceService.updateSaleInvoice(mItem.convertToNewInvoice(newInvoice));
                return new MSelectItem(mItem.getObjectID(), mItem.getInvoiceNumber());
            } else {
                SaveResult result = invoiceService.saveSaleInvoice(mItem.convertToNewInvoice(newInvoice));
                return new MSelectItem(result.getId(), mItem.getInvoiceNumber());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new MSelectItem(-1, "");
    }

    @Override
    public Integer saveSaleInvoiceID(MInvoiceListItem mItem) {
        Integer result = 0;
        try {
            if (mItem.getBillAddressID() == null) {
                Address addressData = new Address();
                mItem.setBillAddressID(clientService.saveAddress(mItem.getAdressData().convertToAD(addressData), mItem.getClientID(), false, true, Address.EntityType.CrmAccount));
            }

            if (mItem.getObjectID() != null && !mItem.getObjectID().equals(0)) {
                result = invoiceService.updateSaleInvoice(mItem.convertToNewInvoice(null)).getId();
            } else {
                result = invoiceService.saveSaleInvoice(mItem.convertToNewInvoice(null)).getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean savePurchaseInvoice(MInvoiceListItem mItem) {
        NewInvoice newInvoice;
        try {
            if (mItem.getObjectID() != null && !mItem.getObjectID().equals(0)) {
                newInvoice = invoiceService.getInvoice(mItem.getObjectID());
            } else {
                newInvoice = new NewInvoice();
            }

            invoiceService.savePurchaseInvoice(mItem.convertToNewInvoice(newInvoice));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public MInvoiceListItem getAllInvoiceData() {
        //NewInvoice editInvoice = invoiceService.getAllInvoiceData()
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MEmailTemplateList getEmailTemplates(String templcatCategoryCode) {
        SelectItem[] templates = profileService.getEmailTemplates(templcatCategoryCode);
        return new MEmailTemplateList(templates);
    }

    @Override
    public MEmailTemplateItem generateEmailTemplateData(MEntityToEmailTemplate emailTemplate) {
        if (emailTemplate == null) {
            return null;
        }
        EntityToEmailTemplate entityToEmailTemplate = new EntityToEmailTemplate();
        EmailTemplateItem emailTemplateItem = emailTemplateService.generateEmailTemplateData(emailTemplate.convertToEntityToEmailTemplate(entityToEmailTemplate), null);
        return new MEmailTemplateItem(emailTemplateItem);
    }

    @Override
    public MInvoiceList getSaleQuoteList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter listingFilterParameter = mFilterParametrs.convertToListingFilterParameter(null);
        InvoiceList invoiceList = quoteService.getSaleQuoteData(listingFilterParameter);
        return new MInvoiceList(invoiceList);
    }

    @Override
    public MInvoiceListItem getSaleQuoteItem(Integer objectId) {
        if (objectId == null) {
            return null;
        }

        NewInvoice invoiceItem = quoteService.getQuote(objectId, null);
        return new MInvoiceListItem(invoiceItem);
    }

    @Override
    public Boolean deleteSalesQuote(Integer objectID) {
        if (objectID == null) {
            return false;
        }
        try {
            quoteService.deleteQuote(objectID, Constants.SALE_QUOTE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MInvoiceNumberData getQuoteNumber() {
        return new MInvoiceNumberData(quoteService.getQuoteNumber());
    }

    @Override
    public MSelectItem saveSalesQuote(MInvoiceListItem mItem) {
        NewInvoice newInvoice;
        try {
            newInvoice = new NewInvoice();

            if (mItem.getBillAddressID() == null) {
                Address addressData = new Address();
                mItem.setBillAddressID(clientService.saveAddress(mItem.getAdressData().convertToAD(addressData), mItem.getClientID(), false, true, Address.EntityType.CrmAccount));
            }

            if (mItem.getObjectID() != null && !mItem.getObjectID().equals(0)) {
                SaveResult result = quoteService.updateSaleQuote(mItem.convertToNewInvoice(newInvoice));
                return new MSelectItem(mItem.getObjectID(), mItem.getInvoiceNumber());
            } else {
                SaveResult result = quoteService.saveSaleQuote(mItem.convertToNewInvoice(newInvoice));
                return new MSelectItem(result.getId(), mItem.getInvoiceNumber());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new MSelectItem(-1, "");
    }

    @Override
    public MInvoiceListItem editSalesQuote(Integer objectID) {
        Params params = new Params();
        params.setFromGettingStarted(false);
        params.setType(RECEIVABLE);
        params.setObjectID(objectID);
        NewInvoice newInvoice = quoteService.getAllQuoteData(params);
        MInvoiceListItem invoiceListItem = new MInvoiceListItem(newInvoice);
        invoiceListItem.setClientAddressData(getClientAddressData(invoiceListItem.getClientID()));

        return invoiceListItem;
    }

    @Override
    public Integer convertQuoteToInvoice(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        try {
           return quoteService.convertToInvoice(objectID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
