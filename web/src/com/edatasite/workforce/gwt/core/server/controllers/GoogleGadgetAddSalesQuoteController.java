package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 10/16/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/addSalesQuote")
public class GoogleGadgetAddSalesQuoteController implements Constants {


    private static final String SALES_QUOTE_ITEM_DROP = "salesQuoteItemDrop";
    private static final String SALES_QUOTE_ITEM_TEXT = "salesQuoteItemText";
    private static final String SALES_QUOTE_QTY = "salesQuoteQty";
    private static final String SALES_QUOTE_PRICE = "salesQuotePrice";
    private static final String SALES_QUOTE_TAX = "salesQuoteTax";
    private static final String SALES_QUOTE_DATE = "salesQuoteDate";
    private static final String SALES_QUOTE_VALID_UNTIL = "salesQuoteValidUntil";
    private static final String SALES_QUOTE_CUSTOMER = "salesQuoteCustomer";
    private static final String SALES_QUOTE_CURRENCY = "salesQuoteCurrency";
    private static final String SALES_QUOTE_CURRENCY_VALUE = "salesQuoteCurrencyValue";
    private static final String SALES_QUOTE_TOTAL = "salesQuoteTotalValue";
    private static final String SALES_QUOTE_SUBTOTAL = "salesQuoteSubTotalValue";
    private static final String SALES_QUOTE_TAXTOTAL = "salesQuoteTaxTotalValue";
    private static final String SALES_QUOTE_TOTAL_IN_INVOICE_CURRENCY = "salesQuoteTotalInInvoiceCurrency";
    private static final String SALES_QUOTE_QTY_VALUE = "salesQuoteQtyValue";
    private static final String SALES_QUOTE_PRICE_VALUE = "salesQuotePriceValue";
    private static final String SALES_QUOTE_TAX_VALUE = "salesQuoteTaxValue";
    private static final String ITEM_ITEMS = "itemItems";
    private static final String TAX_ITEMS = "taxItems";
    private static final String BASE_CURRENCY = "baseCurrency";
    private static final String CUSTOMER_ITEMS = "customerItems";
    private static final String CURRENCY_ITEMS = "currencyItems";
    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String GET_CURRENCY_AND_EXCHANGE_RATE = "getCurrencyAndExchangeRate";
    private static final String GET_EXCHANGE_RATE = "getExchangeRate";
    private static final String GET_CURRENCY_VALUE = "getCurrencyValue";
    private static final String GET_EXCHANGE_RATE_VALUE = "getExchangeRateValue";
    private static final String SALES_QUOTE_ITEM_ID = "getSalesQuoteItemId";
    private static final String SAVE_SALES_QUOTE = "saveSalesQuote";
    private static final String SALES_QUOTE_EXIST = "Sales Quote already exist in system";
    private static final String SEARCH_KEYWORD = "searchKeyword";
    private static final String DATA_LIST = "dataList";
    private static final String LOOK_UP_TYPE = "lookUpType";
    private static final Integer LIMIT = 200;
    @Autowired
    private GoogleGadgetService googleGadgetService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;

    @RequestMapping(value = "/googleGadget/addSalesQuote")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));

        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (!isInvalid(request.getParameter(DEFAULT_FORM)) && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm();
                } else if (!isInvalid(request.getParameter(SALES_QUOTE_CUSTOMER)) && !isInvalid(request.getParameter(GET_CURRENCY_AND_EXCHANGE_RATE)) && request.getParameter(GET_CURRENCY_AND_EXCHANGE_RATE).equals(GoogleGadgetService.TRUE)) {
                    Integer customerId = Integer.parseInt(request.getParameter(SALES_QUOTE_CUSTOMER));
                    if (customerId != null) {
                        writer.write(getCurrencyAndExchangeRateForCustomer(customerId));
                    }
                } else if (!isInvalid(request.getParameter(SALES_QUOTE_CURRENCY)) && !isInvalid(request.getParameter(GET_EXCHANGE_RATE)) && request.getParameter(GET_EXCHANGE_RATE).equals(GoogleGadgetService.TRUE)) {
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put(GET_EXCHANGE_RATE_VALUE, getExchangeRateForCurrency(request.getParameter(SALES_QUOTE_CURRENCY)));
                    writer.write(jsonResponse.toJSONString());
                } else if (!isInvalid(request.getParameter(LOOK_UP_TYPE)) && request.getParameter(LOOK_UP_TYPE).equals(SALES_QUOTE_CUSTOMER)) {
                    String keyword = "";
                    if (!isInvalid(request.getParameter(SEARCH_KEYWORD))) {
                        keyword = request.getParameter(SEARCH_KEYWORD);
                    }
                    writer.write(getJsonString(getCustomers(keyword)));
                } else if (!isInvalid(request.getParameter(SALES_QUOTE_ITEM_ID))) {
                    Integer productId = Integer.parseInt(request.getParameter(SALES_QUOTE_ITEM_ID));
                    if (productId != null) {
                        writer.write(getDataForForm(productId));
                    }
                } else if (!isInvalid(request.getParameter(SAVE_SALES_QUOTE)) && request.getParameter(SAVE_SALES_QUOTE).equals(GoogleGadgetService.TRUE)) {
                    writer.write(save(request));
                }
            } else {
                writer.write(GoogleGadgetService.YOU_ARE_NOT_AUTHORIZED);
            }
        } else {
            writer.write(GoogleGadgetService.YOUR_REQUEST_IS_NOT_SIGNED);
        }
        writer.close();
        return null;
    }

    private String getDataForForm(Integer productId) {
        JSONObject jsonResponse = new JSONObject();
        NewProduct product = productServiceLocal.getProductBaseData(productId);
        jsonResponse.put(SALES_QUOTE_QTY_VALUE, 1);
        jsonResponse.put(SALES_QUOTE_PRICE_VALUE, product.getSellingPrice());
        jsonResponse.put(SALES_QUOTE_TAX_VALUE, product.getTaxItem() != null ? product.getTaxItem().getId() : "");
        return jsonResponse.toJSONString();
    }

    private ModelAndView getForm() {
        CurrencyItem[] currencyItems = invoiceService.getCurrencies(null);

        CurrencyItem baseCurrency = currencyService.getBaseCurrency();

        TaxItem[] taxItems = accountingService.getCompanyTaxes();

        ListingFilterParameter productsParameters = new ListingFilterParameter();
        productsParameters.setLookUp(true);
        productsParameters.setInvoiceType(RECEIVABLE);
        productsParameters.setLimit(LIMIT);
        productsParameters.setListLoadConfig(new ListLoadConfig(LIMIT));
        ProductSelectItem[] productSelectItems = productServiceLocal.getCompanyProductsByType(productsParameters);  //set limit 20 items


        ModelAndView modelAndView = new ModelAndView("googleGadgetAddSalesQuote");
        modelAndView.addObject(ITEM_ITEMS, productSelectItems);
        modelAndView.addObject(TAX_ITEMS, taxItems);
        modelAndView.addObject(CURRENCY_ITEMS, currencyItems);
        modelAndView.addObject(BASE_CURRENCY, baseCurrency);

        return modelAndView;
    }

    private String getCurrencyAndExchangeRateForCustomer(Integer customerId) {
        if (customerId == null || customerId < 0) {
            return "";
        }
        EdsCrmAccount crmAccount = crmAccountManager.get(customerId);
        if (crmAccount == null || crmAccount.isDeleted()) {
            return "";
        }

        JSONObject jsonResponse = new JSONObject();
        TypeItem typeItem = invoiceService.getClientOrSupplier(customerId, Constants.RECEIVABLE);
        if (typeItem.getCurrency() != null) {
            jsonResponse.put(GET_CURRENCY_VALUE, typeItem.getCurrencyID());
            jsonResponse.put(GET_EXCHANGE_RATE_VALUE, getExchangeRateForCurrency(typeItem.getCurrency()));
        }

        return jsonResponse.toJSONString();
    }

    private String getExchangeRateForCurrency(String currencyName) {
        return invoiceService.getExchangeRate(currencyName).toString();
        // return rate.setScale(AccountingUtils.customExRateScale, BigDecimal.ROUND_HALF_UP).toString();
    }


    private SelectItem[] getCustomers(String searchKey) {

        ListingFilterParameter accountParameters = new ListingFilterParameter();
        accountParameters.setAccountType(CrmAccountLookUp.CUSTOMER);
        accountParameters.setSearchByParent(true);
        accountParameters.setLimit(LIMIT);
        accountParameters.setSearchKey(searchKey);

        return allInOneService.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, accountParameters).getList().toArray(new SelectItem[]{});
    }


    private String save(HttpServletRequest data) {
        JSONObject jsonResponse = new JSONObject();
        if (validate(data)) {
            TaxItem[] taxItems = accountingService.getCompanyTaxes();


            String[] salesQuoteItem = data.getParameterValues(SALES_QUOTE_ITEM_DROP);
            String salesQuoteItemName = data.getParameter(SALES_QUOTE_ITEM_TEXT);
            String[] salesQuoteQty = data.getParameterValues(SALES_QUOTE_QTY);
            String[] salesQuotePrice = data.getParameterValues(SALES_QUOTE_PRICE);
            String[] salesQuoteTax = data.getParameterValues(SALES_QUOTE_TAX);

            String salesQuoteDate = data.getParameter(SALES_QUOTE_DATE);
            String salesQuoteValidUntil = data.getParameter(SALES_QUOTE_VALID_UNTIL);
            Integer salesQuoteCustomer = Integer.parseInt(data.getParameter(SALES_QUOTE_CUSTOMER).split("::")[0]);
            Integer salesQuoteCurrency = Integer.parseInt(data.getParameter(SALES_QUOTE_CURRENCY));

            BigDecimal total = BigDecimal.valueOf(Double.parseDouble(data.getParameter(SALES_QUOTE_TOTAL)));
            BigDecimal subTotal = BigDecimal.valueOf(Double.parseDouble(data.getParameter(SALES_QUOTE_SUBTOTAL)));
            BigDecimal taxTotal = BigDecimal.valueOf(Double.parseDouble(data.getParameter(SALES_QUOTE_TAXTOTAL)));
            BigDecimal totalInInvoiceCurrency = BigDecimal.valueOf(Double.parseDouble(data.getParameter(SALES_QUOTE_TOTAL_IN_INVOICE_CURRENCY)));

            BigDecimal salesQuoteCurrencyValue = new BigDecimal(Double.parseDouble(data.getParameter(SALES_QUOTE_CURRENCY_VALUE)));
            InvoiceNumberData invoiceNumberData = quoteService.getQuoteNumber();


            NewInvoice item = new NewInvoice();
            NewInvoiceItem[] invoiceItems = new NewInvoiceItem[salesQuoteQty.length];
            TaxItem taxItem = null;

            for (int i = 0; i < salesQuoteQty.length; i++) {
                invoiceItems[i] = new NewInvoiceItem();
                if (salesQuoteItem != null && salesQuoteItem.length > 0) {
                    invoiceItems[i].setItemID(Integer.parseInt(salesQuoteItem[i]));
                } else {
                    invoiceItems[i].setItemName(salesQuoteItemName);
                }

                if (salesQuoteTax[i] != null && !"".equals(salesQuoteTax[i])) {
                    invoiceItems[i].setTaxItem(new TaxItem(Integer.parseInt(salesQuoteTax[i]), ""));
                }
                invoiceItems[i].setQuantity(new BigDecimal(Double.parseDouble(salesQuoteQty[i])));
                invoiceItems[i].setUnitPrice(new BigDecimal(Double.parseDouble(salesQuotePrice[i])));

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(GoogleGadgetService.DATE_PATTERN, Locale.US);
            Date dateInvoice = new Date();
            try {
                dateInvoice = dateFormat.parse(salesQuoteDate);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Date dueDate = new Date();
            try {
                dueDate = dateFormat.parse(salesQuoteValidUntil);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            item.setClientID(salesQuoteCustomer);
            item.setCurrencyID(salesQuoteCurrency);
            item.setExchageRate(salesQuoteCurrencyValue);
            item.setInvoiceDate(new DateNonConvertable(dateInvoice));
            item.setDueDate(new DateNonConvertable(dueDate));
            item.setInvoiceNumber(invoiceNumberData.getInvoiceNumber());
            item.setStatusCode(Constants.APPROVE);
            item.setTotal(total);
            item.setSubtotal(subTotal);
            item.setTotalTaxes(taxTotal);
            item.setTotalInInvoiceCurrency(totalInInvoiceCurrency);

            item.setItems(invoiceItems);

            SaveResult result = quoteService.saveSaleQuote(item);

            if (result != null) {
                if (result.isInvoiceExist()) {
                    jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, SALES_QUOTE_EXIST);
                    jsonResponse.put(GoogleGadgetService.SAVED, false);
                } else {
                    jsonResponse.put(GoogleGadgetService.SAVED, true);
                }
            } else {
                jsonResponse.put(GoogleGadgetService.SAVED, false);
            }
        } else {
            jsonResponse.put(GoogleGadgetService.SAVED, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.VALIDATION_FAILED);
        }


        return jsonResponse.toJSONString();
    }

    private boolean validate(HttpServletRequest data) {
        int errors = 0;

        if (isInvalidArray(data.getParameterValues(SALES_QUOTE_ITEM_DROP))) {
            if (isInvalid(data.getParameter(SALES_QUOTE_ITEM_TEXT))) {
                errors++;
            }
        }

        if (isInvalidArray(data.getParameterValues(SALES_QUOTE_QTY))) {
            errors++;
        }

        if (isInvalidArray(data.getParameterValues(SALES_QUOTE_PRICE))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_DATE))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_VALID_UNTIL))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_CUSTOMER))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_CURRENCY))) {
            errors++;
        }
        if (isInvalid(data.getParameter(SALES_QUOTE_CURRENCY_VALUE))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_TOTAL))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_SUBTOTAL))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_TAXTOTAL))) {
            errors++;
        }

        if (isInvalid(data.getParameter(SALES_QUOTE_TOTAL_IN_INVOICE_CURRENCY))) {
            errors++;
        }

        if (data.getParameterValues(SALES_QUOTE_QTY).length != data.getParameterValues(SALES_QUOTE_PRICE).length) {
            errors++;
        }

        return errors <= 0;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }

    private boolean isInvalidArray(String[] params) {
        if (params != null && params.length > 0) {
            for (String param : params) {
                if (param == null || "".equals(param)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private String getJsonString(SelectItem[] items) {
        JSONObject jsonResponse = new JSONObject();

        jsonResponse.put(DATA_LIST, wrapToArrayList(items));

        return jsonResponse.toJSONString();
    }

    private ArrayList<String> wrapToArrayList(SelectItem[] selectItems) {
        ArrayList<String> wrap = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                wrap.add(item.getId() + "::" + item.getName());
            }
        }
        return wrap;
    }


}
