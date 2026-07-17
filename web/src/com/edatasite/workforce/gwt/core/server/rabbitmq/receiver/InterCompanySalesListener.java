package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.MultiCurrencyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.InterCompanyDataMQ;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/21/12
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterCompanySalesListener extends BaseAmqpListener<InterCompanyDataMQ> {

    private static final Logger log = LoggerFactory.getLogger(InterCompanySalesListener.class);

    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("productService")
    private ProductServiceLocal productServiceLocal;
    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private MultiCurrencyManager multiCurrencyManager;

    @Override
    public void receiveMessage(InterCompanyDataMQ data) {
        log.info("INTERCOMPANY_TRANSACTION_LISTENER_START");
        Integer companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        EdsCompany company = companyManager.get(companyID);
        ServerSecurityContext.getInstance().setStaticUserID(company.getCreator().getObjectID());

        if (company.getParentCompanyId() != null) {
            HashMap<Integer, Integer> currencyMap = multiCurrencyManager.getSubsidiaryCurrenciesAsMap();
            if (!currencyMap.containsKey(data.getTransaction().getCurrencyID())) {
                log.info("Invoice Currency doesn't match to this subsidiary.COMPANY_ID:" + company.getObjectID() + ";CURRENCY_ID:" + data.getTransaction().getCurrencyID());
                return;
            }
        }

        Integer crmAccountID = crmServiceLocal.saveSubsidiaryCrmAccount(data.getCrmAccountItem(), data.getTransaction().getCompanyID(), data.getTransaction().getType());
        TypeItem crmAccountData = crmServiceLocal.getInterCompanyCrmAccountAsTypeItem(crmAccountID);

        HashMap<Integer, Integer> accountConversionIDs = accountingServiceLocal.convertInterCompanyAccounts(data.getAccounts());
        HashMap<Integer, Integer> taxConversionIDs = invoiceServiceLocal.convertInterCompanyTaxes(data.getTaxes());
        HashMap<Integer, Integer> discountConversionIDs = invoiceServiceLocal.convertInterCompanyDiscounts(data.getDiscounts());
        HashMap<Integer, Integer> productConversionIDs = productServiceLocal.convertInterCompanyProducts(data.getProducts());


        if (crmAccountData == null) {
            log.info("CANNOT FIND/CREATE CRM_ACCOUNT");
            return;
        }

        if (accountConversionIDs == null) {
            log.info("CANNOT FIND/CREATE CHART_OF_ACCOUNTS");
            return;
        }

        if (productConversionIDs == null) {
            log.info("CANNOT FIND/CREATE PRODUCTS");
            return;
        }

//        data.getTransaction().setType(Constants.PAYABLE);
        data.getTransaction().setTypeItem(crmAccountData);
        data.getTransaction().setBillAddressID(crmAccountData.getBillAddressID());
        data.getTransaction().setMailAddressID(null);
        data.getTransaction().setClientID(crmAccountData.getId());
        data.getTransaction().setInterCompanySales(true);
        invoiceServiceLocal.saveInterCompanySales(data, productConversionIDs, discountConversionIDs, taxConversionIDs);

        log.info("INTERCOMPANY_TRANSACTION_LISTENER_END");
    }

    @Override
    protected DataMQ<InterCompanyDataMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<InterCompanyDataMQ>>() {
        }.getType());
    }
}
