package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public class PaymentCallbackServlet extends HttpServlet {
    InvoiceService invoiceService;
    InvoiceManager invoiceManager;
    InvoicingSettingsManager invoicingSettingsManager;
    UsagePlanManager usagePlanManager;
    CompanyManager companyManager;
    ReferenceManager referenceManager;

    @Override
    public void init() throws ServletException {
        invoiceService = ApplicationContextProvider.applicationContext.getBean(InvoiceService.class);
        invoicingSettingsManager = ApplicationContextProvider.applicationContext.getBean(InvoicingSettingsManager.class);
        invoiceManager = ApplicationContextProvider.applicationContext.getBean(InvoiceManager.class);
        usagePlanManager = ApplicationContextProvider.applicationContext.getBean(UsagePlanManager.class);
        companyManager = ApplicationContextProvider.applicationContext.getBean(CompanyManager.class);
        referenceManager = ApplicationContextProvider.applicationContext.getBean(ReferenceManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] uriValues = uri.split("/");
        String encodedData = uriValues[uriValues.length - 1];

        HashMap<String, String> data = parseDataToMap(EncryptionHelper.decodeBase64(encodedData));
        String session = data.get("session");
        String type = data.get("type");
        Integer invoiceId = Integer.valueOf(data.get("invoiceId"));

        SecurityContext.getInstance().setSessionId(session);
        EdsUser user = invoicingSettingsManager.getUser();
        EdsCompany company = user.getCompany();


        NewInvoice invoice = invoiceService.getInvoiceSummaryData(invoiceId);
        if (!Constants.PAID.equals(invoice.getStatusCode())) {

            ReceivePaymentData receivePaymentData = new ReceivePaymentData();
            receivePaymentData.setBatchPayment(true);
            receivePaymentData.setCrmAccount(invoice.getTypeItem());
            receivePaymentData.setAccount(getBankAccount(type, company));
            receivePaymentData.setExRate(invoice.getExchageRate());
            receivePaymentData.setCurrency(new CurrencyItem(invoice.getCurrencyID(), null, null));
            receivePaymentData.setReference(invoice.getInvoiceNumber());
            receivePaymentData.setDate(new DateNonConvertable());
            receivePaymentData.setTotalAmount(invoice.getDueAmount());
            receivePaymentData.setValidateReferences(false);
            receivePaymentData.setType(Constants.RECEIVABLE);
            receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
            receivePaymentData.setPayments(new PaymentData[]{getPaymentData(invoice, receivePaymentData.getAccount(), null)});

            invoiceService.saveReceivePaymentData(receivePaymentData, true);
        }

        resp.sendRedirect("https://www.kpi.com/");
    }

    PaymentData getPaymentData(NewInvoice invoice, SelectItem account, BigDecimal amount) {
        PaymentData paymentData = new PaymentData();
        paymentData.setInvoiceID(invoice.getID());
        paymentData.setPaymentAmount(amount != null ? amount : invoice.getDueAmount());
        paymentData.setDate(new DateNonConvertable());
        paymentData.setPaymentAccount(account);
        paymentData.setReferenceNumber(invoice.getInvoiceNumber());
        paymentData.setCurrency(new SelectItem(invoice.getCurrencyID()));
        paymentData.setExchangeRate(invoice.getExchageRate());
        paymentData.setType(invoice.getType());
        paymentData.setTotal(invoice.getTotalInInvoiceCurrency());
        paymentData.setCrmAccount(new SelectItem(invoice.getClientID(), invoice.getClientName()));
        return paymentData;
    }

    private HashMap<String, String> parseDataToMap(String data) {
        HashMap<String, String> result = new HashMap<>();
        String[] values = data.split("&");
        for (String value : values) {
            String[] keyValues = value.split("=");
            result.put(keyValues[0], keyValues[1]);
        }
        return result;
    }

    SelectItem getBankAccount(String type, EdsCompany company) {
        if (Constants.PAYMENT_TYPES.PAYME.equals(type)) {
            EdsInvoicingSettings edsInvoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (StringUtils.isBlank(edsInvoicingSettings.getPayMeMerchantId())) {
                throw new RuntimeException("Payme me is not setup");
            }
            return edsInvoicingSettings.getPaymePaymentAccount().getAsSelectItem();
        } else if (Constants.PAYMENT_TYPES.CLICK.equals(type)) {
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (StringUtils.isBlank(invoiceSettings.getClickMerchantId())) {
                throw new RuntimeException("Click is not setup");
            }
            return invoiceSettings.getClickPaymentAccount().getAsSelectItem();
        } else if (Constants.PAYMENT_TYPES.REVOLUT_BANK.equals(type)) {
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (StringUtils.isBlank(invoiceSettings.getRevolutSecretApiKey())) {
                throw new RuntimeException("Revolut is not setup");
            }
            return invoiceSettings.getRevolutPaymentAccount().getAsSelectItem();
        } else if (Constants.PAYMENT_TYPES.REVOLUT_EXPENSE.equals(type)) {
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (StringUtils.isBlank(invoiceSettings.getRevolutSecretApiKey())) {
                throw new RuntimeException("Revolut is not setup");
            }
            return invoiceSettings.getRevolutExpenseAccount().getAsSelectItem();
        }
        return null;
    }
}
