package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaxComponentManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/16/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Service("nimbleService")
public class NimbleServiceImpl implements NimbleService, AccountingConstants {

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private TaxComponentManager taxComponentManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private ClientService clientService;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void importNimbleCommerceData(ImportFile importFile, List<String[]> dataBank) {
        Integer FIELD_OFFER_ID = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_OFFER_ID);
        Integer FIELD_OFFER_NAME = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_OFFER_NAME);
        Integer FIELD_OFFER_PRICE = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_OFFER_PRICE);
        Integer FIELD_FIRST_NAME = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_FIRST_NAME);
        Integer FIELD_LAST_NAME = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_LAST_NAME);
        Integer FIELD_EMAIL = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_EMAIL);
        Integer FIELD_PHONE = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_PHONE);
        Integer FIELD_ORDER_NUMBER = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_ORDER_NUMBER);
        Integer FIELD_TRANSACTION_DATE = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_TRANSACTION_DATE);
        Integer FIELD_TRANSACTION_TIME = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_TRANSACTION_TIME);
        Integer FIELD_QUANTITY = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_QUANTITY);
        Integer FIELD_MERCHANT_ID = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_MERCHANT_ID);
        Integer FIELD_TAX = importFile.getColumnID(ImportField.NimbleCommerceFields.FIELD_TAX);

        boolean hasHeader = importFile.isHasHeader();

        HashMap<String, EdsItem> nimbleOffersMap = itemManager.getOffersMapForNimble();
        HashMap<String, Integer> crmAccountsMap = clientManager.getNimbleCrmAccountsMap();
        HashMap<String, Integer> nimbleUniqueIDsMap = clientManager.getNimbleUniqueIDsMap();

        SimpleDateFormat nimbleDateFormat = new SimpleDateFormat("EEEEE, dd MMMM yyyy");
        SimpleDateFormat uniqueIDFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        DecimalFormat format = new DecimalFormat("0000");

        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();

        EdsUser user = itemManager.getUser();
        EdsAccount salesAccount = accountingManager.getAccountByCode("4000");

        for (String[] row : dataBank) {
            if (!hasHeader) {
                Integer columnID = 0;
                String offerID = null, offerName = null, firstName = null, lastName = null, email = null, phone = null, orderNumber = null, merchantID = null, transactionTime = null;
                BigDecimal offerPrice = null, quantity = null, taxAmount = null;
                Calendar transDateCal = null;
                for (String columnValue : row) {
                    if (columnValue != null && !"".equals(columnValue.trim())) {
                        columnValue = columnValue.trim();
                        if (columnID.equals(FIELD_OFFER_ID)) {
                            offerID = columnValue;
                        }
                        if (columnID.equals(FIELD_OFFER_NAME)) {
                            offerName = columnValue;
                        }
                        if (columnID.equals(FIELD_OFFER_PRICE)) {
                            offerPrice = parseBigDecimal(columnValue);
                        }
                        if (columnID.equals(FIELD_FIRST_NAME)) {
                            firstName = columnValue;
                        }
                        if (columnID.equals(FIELD_LAST_NAME)) {
                            lastName = columnValue;
                        }
                        if (columnID.equals(FIELD_EMAIL)) {
                            email = columnValue;
                        }
                        if (columnID.equals(FIELD_PHONE)) {
                            phone = columnValue;
                        }
                        if (columnID.equals(FIELD_ORDER_NUMBER)) {
                            orderNumber = columnValue;
                        }
                        if (columnID.equals(FIELD_TRANSACTION_DATE)) {
                            try {
                                Date date = nimbleDateFormat.parse(columnValue);
                                if (date != null) {
                                    transDateCal = new GregorianCalendar();
                                    transDateCal.setTime(date);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (columnID.equals(FIELD_TRANSACTION_TIME)) {
                            transactionTime = columnValue;
                        }
                        if (columnID.equals(FIELD_QUANTITY)) {
                            quantity = parseBigDecimal(columnValue);
                        }
                        if (columnID.equals(FIELD_MERCHANT_ID)) {
                            merchantID = columnValue;
                        }
                        if (columnID.equals(FIELD_TAX)) {
                            taxAmount = parseBigDecimal(columnValue);
                        }
                    }
                    columnID++;
                }

                if (transactionTime != null && transactionTime.length() > 0 && transDateCal != null) {
                    try {
                        String[] time = transactionTime.split(":");
                        transDateCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                        transDateCal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                        transDateCal.set(Calendar.SECOND, Integer.parseInt(time[2]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (isValid(offerID) && isValid(offerName) && (isValid(firstName) || isValid(lastName)) && isValid(quantity) && isValid(offerPrice)) {

                    String customerName = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();

                    String nimbleUniqueID = customerName + "_" + offerID + (transDateCal != null ? "_" + uniqueIDFormat.format(transDateCal.getTime()) : "");

                    EdsItem product = nimbleOffersMap.get(offerID);
                    if (product == null) {
                        product = createNimbleProduct(salesAccount, offerID, offerName, offerPrice);
                        nimbleOffersMap.put(product.getNimbleOfferID(), product);
                    }

                    Integer customerID = crmAccountsMap.get(customerName.toLowerCase()), contactID = null;
                    if (customerID == null) {
                        CrmAccountItem crmAccountItem = new CrmAccountItem();
                        crmAccountItem.setName(customerName);
                        crmAccountItem.setCurrencyId(baseCurrency.getId());
                        customerID = clientService.createClient(crmAccountItem, null);

                        if (customerID > 0) {
                            crmAccountsMap.put(customerName.toLowerCase(), customerID);

                            ClientContact clientContact = new ClientContact();
                            clientContact.setFirstName(firstName);
                            clientContact.setLastName(lastName);
                            clientContact.setEmail(email);
                            clientContact.setPhone(phone);
                            clientContact.setClientId(customerID);

                            contactID = clientService.createContact(clientContact);
                        }
                    } else {
                        EdsCrmAccount crmAccount = clientManager.get(customerID);
                        if (crmAccount != null && !crmAccount.isClient()) {
                            crmAccount.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                            clientManager.update(crmAccount, true);
                        }
                    }

                    if (customerID > 0) {
                        SaveResult result = createSalesInvoice(format, baseCurrency, user,
                                salesAccount, orderNumber, merchantID, offerPrice, quantity, taxAmount,
                                transDateCal, product, customerID, contactID, nimbleUniqueID);

                        nimbleUniqueIDsMap.put(nimbleUniqueID, result.getId());
                    }

                }
            }
            hasHeader = false;
        }
    }

    private EdsItem createNimbleProduct(EdsAccount salesAccount, String offerID, String offerName, BigDecimal offerPrice) {
        NewProduct newProduct = new NewProduct();
        newProduct.setType(NON_INVENTORY_ITEM);

        NumberData numberData = new NumberData();
        if (offerID != null && offerID.length() >= 4) {
            try {
                numberData.setIntNumber(Integer.parseInt(offerID.substring(offerID.length() - 4)));
            } catch (NumberFormatException e) {
                System.out.println("Can't parse product last 4 number: " + offerID);
            } finally {
                numberData.setNumberString(offerID);
            }
        } else {
            numberData.setNumberString(offerID);
        }
        newProduct.setNumberData(numberData);

        newProduct.setItemName(offerName);
        newProduct.setNimbleOfferID(offerID);
        newProduct.setSellingPrice(offerPrice);
        newProduct.setAccountId(salesAccount.getObjectID());
        newProduct.setAsOf(new DateNonConvertable(itemManager.getUser().getUserDate()));

        Integer productID = productService.saveProduct(newProduct).getId();
        return itemManager.get(productID);
    }

    private SaveResult createSalesInvoice(DecimalFormat format, CurrencyItem baseCurrency, EdsUser user, EdsAccount salesAccount,
                                          String orderNumber, String merchantID, BigDecimal offerPrice, BigDecimal quantity, BigDecimal taxAmount,
                                          Calendar transDateCal, EdsItem product, Integer customerID, Integer contactID, String nimbleUniqueID) {
        NewInvoice salesInvoice = new NewInvoice();
        salesInvoice.setNimbleUniqueID(nimbleUniqueID);
        salesInvoice.setClientID(customerID);
        salesInvoice.setClientContactID(contactID);
        salesInvoice.setBaseCurrency(baseCurrency);
        salesInvoice.setCurrencyID(baseCurrency.getId());
        salesInvoice.setExchageRate(BigDecimal.ONE);
        salesInvoice.setInvoiceDate(transDateCal.getTime() != null ? new DateNonConvertable(transDateCal.getTime()) : null);

        Calendar endDateCal = new GregorianCalendar();
        endDateCal.setTime(transDateCal.getTime());
        ServerUtils.setEndOfTheDay(endDateCal);
        salesInvoice.setDueDate(endDateCal.getTime() != null ? new DateNonConvertable(endDateCal.getTime()) : null);

        salesInvoice.setReference(merchantID);
        if (orderNumber != null && orderNumber.trim().length() > 0) {
            InvoiceNumberData numberData = new InvoiceNumberData();
            try {
                if (orderNumber.length() > 4) {
                    Integer fourDigitNumber = Integer.parseInt(orderNumber.substring(orderNumber.length() - 4));
                    numberData.setFourDigitNumber(format.format(fourDigitNumber));
                    numberData.setPrefix(orderNumber.substring(0, orderNumber.length() - 4));
                } else {
                    Integer fourDigitNumber = Integer.parseInt(orderNumber);
                    numberData.setFourDigitNumber(format.format(fourDigitNumber));
                    numberData.setPrefix(orderNumber.substring(0, orderNumber.length() - 4));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                numberData = null;
            }
            if (numberData == null) {
                numberData = invoiceServiceLocal.generateAndGetSaleInvoiceNumber(user.getCompany());
            }

            salesInvoice.setNumberData(numberData);
        } else {
            salesInvoice.setNumberData(invoiceServiceLocal.generateAndGetSaleInvoiceNumber(user.getCompany()));
        }
        salesInvoice.setInvoiceNumber(salesInvoice.getNumberData().getInvoiceNumber());
        salesInvoice.setFourDigitNumber(salesInvoice.getNumberData().getFourDigitNumber());

        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        } else {
            taxAmount = taxAmount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal netAmount = quantity.multiply(offerPrice);

        NewInvoiceItem[] items = new NewInvoiceItem[1];
        items[0] = new NewInvoiceItem();
        items[0].setItemID(product.getObjectID());
        items[0].setQuantity(quantity);
        items[0].setUnitPrice(offerPrice);
        items[0].setNet(netAmount);
        items[0].setAccountID(salesAccount.getObjectID());

        if (taxAmount.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal taxPercent = taxAmount.multiply(new BigDecimal(100)).divide(netAmount, 2, RoundingMode.HALF_UP);
            EdsVat nimbleTax = vatManager.getNimbleTax(taxPercent);
            if (nimbleTax == null) {
                nimbleTax = new EdsVat();
                nimbleTax.setName("Nimble Tax");
                nimbleTax.setPermissionType(2);
                nimbleTax.setVatAmount(taxPercent);
                vatManager.create(nimbleTax);

                EdsTaxComponent taxComponent = new EdsTaxComponent();
                taxComponent.setName("Tax");
                taxComponent.setRate(taxPercent);
                taxComponent.setTax(nimbleTax);
                taxComponentManager.create(taxComponent);
            }

            items[0].setTaxItem(nimbleTax.createTaxItem());

            TotalTaxItem totalTaxItem = new TotalTaxItem();
            totalTaxItem.setTaxItem(items[0].getTaxItem());
            totalTaxItem.setTaxAmount(taxAmount);
            salesInvoice.setTotalTaxItems(new TotalTaxItem[]{totalTaxItem});
        }

        items[0].setTaxAmount(taxAmount);
        items[0].setTotalAmount(items[0].getNet());
        salesInvoice.setItems(items);

        BigDecimal totalAmount = netAmount.add(taxAmount);

        salesInvoice.setSubtotal(netAmount);
        salesInvoice.setTotal(totalAmount);
        salesInvoice.setTotalInInvoiceCurrency(totalAmount);
        salesInvoice.setTotalDiscount(BigDecimal.ZERO);
        salesInvoice.setTotalTaxes(taxAmount);
        salesInvoice.setBillableExpenseAmount(BigDecimal.ZERO);
        salesInvoice.setTaxCalculationType(2);
        salesInvoice.setStatusCode(Constants.APPROVE);
        salesInvoice.setType(Constants.RECEIVABLE);

        return invoiceServiceLocal.saveSaleInvoice(salesInvoice);
    }

    private BigDecimal parseBigDecimal(String columnValue) {
        try {
            return new BigDecimal(columnValue.trim().replace(",", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValid(String s) {
        return s != null && s.trim().length() > 0;
    }

    private boolean isValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
