package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
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
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/3/14
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("customInvoiceImportService")
public class CustomInvoiceImportServiceImpl implements CustomInvoiceImportService {

    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private TaxComponentManager taxComponentManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ItemManager itemManager;
    /*@Autowired
    private CountryManager countryManager;
    @Autowired
    private AddressManager addressManager;*/
    @Autowired
    private ProjectManager projectManager;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String importCustomInvoices(ImportFile importFile, List<String[]> dataBank) {
        Integer FIELD_INVOICE_NUMBER = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_NUMBER);
        Integer FIELD_INVOICE_TYPE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_TYPE);
        Integer FIELD_INVOICE_DATE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_DATE);
        Integer FIELD_DUE_DATE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_DUE_DATE);

        Integer FIELD_CUSTOMER_NAME = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_NAME);
        /*Integer FIELD_CUSTOMER_STR_ADDRESS = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_STR_ADDRESS);
        Integer FIELD_CUSTOMER_CITY = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_CITY);
        Integer FIELD_CUSTOMER_COUNTRY = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_COUNTRY);
        Integer FIELD_CUSTOMER_POSTCODE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_POSTCODE);
        Integer FIELD_CUSTOMER_VAT = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_VAT);*/

        Integer FIELD_PRODUCT_NAME = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_NAME);
        Integer FIELD_PRODUCT_QTY = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_QTY);
        Integer FIELD_PRODUCT_PRICE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_PRICE);
        Integer FIELD_PRODUCT_DISCOUNT = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_DISCOUNT);
        Integer FIELD_PRODUCT_TAX = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_TAX);
        Integer FIELD_BENEFICIARY_ACCOUNT = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_BENEFICIARY_ACCOUNT);
        Integer FIELD_PROJECT = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PROJECT);
        Integer FIELD_DESCRIPTION = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_DESCRIPTIOIN);
        Integer FIELD_REFERENCE = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_REFERENCe);
        Integer FIELD_PARENT_PROJECT = importFile.getColumnID(ImportField.CustomInvoiceImportFields.FIELD_PARENT_PROJECT);

        boolean hasHeader = importFile.isHasHeader();

        EdsUser user = accountingManager.getUser();
        //EdsCompany company = user.getCompany();

        DecimalFormat format = new DecimalFormat("0000");
        String companyDateFormatStr = user.getCompany().getCompanySettings().getShortDateFormat();
        String dateFormatStr = companyDateFormatStr != null && !companyDateFormatStr.isEmpty() ? companyDateFormatStr : "dd.MM.yyyy";
        SimpleDateFormat customInvoiceDateFormat = new SimpleDateFormat(dateFormatStr);
        //SimpleDateFormat customInvoiceDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();

        StringBuilder notImportInvoices = new StringBuilder();
        StringBuilder notImportInvoiceList = new StringBuilder();

        //EdsCountry companyCountry = company.getCountryZone() != null ? company.getCountryZone().getCountry() : null;
        HashMap<String, Integer> crmAccountsMap = clientManager.getNimbleCrmAccountsMap();
        HashMap<String, Integer> chartAccountsMap = accountingManager.getAccountsMapForCustomInvoiceImport();
        //HashMap<String, Integer> productsMap = itemManager.getProductsMapForCustomInvoiceImport();
        Map<String, Integer> projectMap = projectManager.getProjectAsMap();
        int rowIndex = 0;
        for (String[] row : dataBank) {
            if (!hasHeader) {
                Integer columnID = 0;

                Date invoiceDate = null, dueDate = null;
                String invoiceNumber = null, invoiceType = null, customerName = null,/*customerAddress = null, customerCity = null, customerCountry = null, customerPostCode = null, customerTax = null,*/ productName = null, beneficiaryAccountCode = null, parentProjectName = null, projectName = null, reference = null, desription = null;
                BigDecimal productQty = null, productPrice = null, productDiscount = null, productTaxPercent = null;
                for (String columnValue : row) {
                    if (columnValue != null && !"".equals(columnValue.trim())) {
                        columnValue = columnValue.trim();

                        if (columnID.equals(FIELD_INVOICE_NUMBER)) {
                            invoiceNumber = columnValue;
                        }
                        if (columnID.equals(FIELD_INVOICE_TYPE)) {
                            invoiceType = columnValue;
                        }
                        if (columnID.equals(FIELD_INVOICE_DATE)) {
                            try {
                                invoiceDate = customInvoiceDateFormat.parse(columnValue);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Invoice Date " + columnValue + " error. Date format should be " + dateFormatStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (columnID.equals(FIELD_DUE_DATE)) {
                            try {
                                dueDate = customInvoiceDateFormat.parse(columnValue);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Due Date " + columnValue + " error. Date format should be " + dateFormatStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (columnID.equals(FIELD_CUSTOMER_NAME)) {
                            customerName = columnValue;
                            if (ServerUtils.isNullOrEmpty(customerName)) {
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Customer Name should not be null");
                            }
                        }
                        if (columnID.equals(FIELD_PARENT_PROJECT)) {
                            parentProjectName = columnValue;
                        }
                        if (columnID.equals(FIELD_PROJECT)) {
                            projectName = columnValue;
                        }
                        if (columnID.equals(FIELD_REFERENCE)) {
                            reference = columnValue;
                        }
                        /*if (columnID.equals(FIELD_CUSTOMER_STR_ADDRESS)) {
                            customerAddress = columnValue;
                        }
                        if (columnID.equals(FIELD_CUSTOMER_CITY)) {
                            customerCity = columnValue;
                        }
                        if (columnID.equals(FIELD_CUSTOMER_COUNTRY)) {
                            customerCountry = columnValue;
                        }
                        if (columnID.equals(FIELD_CUSTOMER_POSTCODE)) {
                            customerPostCode = columnValue;
                        }
                        if (columnID.equals(FIELD_CUSTOMER_VAT)) {
                            customerTax = columnValue;
                        }*/

                        if (columnID.equals(FIELD_PRODUCT_NAME)) {
                            productName = columnValue;
                            if (ServerUtils.isNullOrEmpty(productName)) {
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Product Name should not be null");
                            }
                        }
                        if (columnID.equals(FIELD_DESCRIPTION)) {
                            desription = columnValue;
                        }
                        if (columnID.equals(FIELD_PRODUCT_QTY)) {
                            productQty = parseBigDecimal(columnValue);
                        }
                        if (columnID.equals(FIELD_PRODUCT_PRICE)) {
                            productPrice = parseBigDecimal(columnValue);
                            if (!isValid(productPrice)) {
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Price should not be null");
                            }
                        }
                        if (columnID.equals(FIELD_PRODUCT_DISCOUNT)) {
                            productDiscount = parseBigDecimal(columnValue);
                        }
                        if (columnID.equals(FIELD_PRODUCT_TAX)) {
                            productTaxPercent = parseBigDecimal(columnValue);
                        }
                        if (columnID.equals(FIELD_BENEFICIARY_ACCOUNT)) {
                            beneficiaryAccountCode = columnValue;
                            if (!isValid(beneficiaryAccountCode)) {
                                notImportInvoiceList.append("\n Row - " + rowIndex + " Account should not be null");
                            }
                        }
                    }
                    columnID++;
                }


                if (isValid(customerName) && isValid(productName) && isValid(beneficiaryAccountCode) && isValid(productQty) && isValid(productPrice) && invoiceDate != null && dueDate != null) {

                    Integer invoiceTypeConstant = AccountingConstants.SERVICE_INVOICE_TYPE;
                    if (invoiceType != null) {
                        invoiceType = invoiceType.trim().toLowerCase();
                        if ("product".equals(invoiceType) || "product invoice".equals(invoiceType)) {
                            invoiceTypeConstant = AccountingConstants.PRODUCT_INVOICE_TYPE;
                        }
                    }

                    customerName = customerName.trim();
                    Integer customerID = crmAccountsMap.get(customerName.toLowerCase());
                    //Integer productID = productsMap.get(productName);
                    Integer productAccountID = chartAccountsMap.get(beneficiaryAccountCode);
                    Integer projectID = projectName != null && parentProjectName != null ? projectMap.get(parentProjectName.trim().toLowerCase() + "_" + projectName.trim().toLowerCase()) : null;


                    Integer billAddressID = null, malingAddressID = null;
                    if (customerID == null) {
                        continue;
                        /*CrmAccountItem crmAccountItem = new CrmAccountItem();
                        crmAccountItem.setName(customerName);
                        crmAccountItem.setCurrencyId(baseCurrency.getId());
                        customerID = clientService.createClient(crmAccountItem, null);

                        if (customerID > 0) {
                            crmAccountsMap.put(customerName.toLowerCase(), customerID);
                        }*/
                    } else {
                        EdsCrmAccount crmAccount = clientManager.get(customerID);
                        if (crmAccount != null && !crmAccount.isClient()) {
                            crmAccount.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                            clientManager.update(crmAccount, true);
                        }
                        billAddressID = crmAccount.getPrimaryBillingAddressId();
                        malingAddressID = crmAccount.getPrimaryMalingAddressId();
                    }


                    /*if (isValid(customerAddress) || isValid(customerCity) || isValid(customerCountry) || isValid(customerPostCode) || companyCountry != null) {
                        EdsAddress billAddress = null;
                        EdsCrmAccount crmAccount = clientManager.get(customerID);
                        List<EdsAddress> billAddressList = addressManager.getAddressesByEntityIdAndType(customerID, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);

                        EdsCountry billingCountry = isValid(customerCountry) ? countryManager.getCountryByName(customerCountry) : null;
                        if (billingCountry == null) {
                            billingCountry = companyCountry;
                        }

                        for (EdsAddress ba : billAddressList) {
                            if (isSameValue(customerAddress, ba.getAddress())
                                    && isSameValue(customerCity, ba.getCity())
                                    && isSameValue((billingCountry != null ? billingCountry.getName() : ""), (ba.getCountry() != null ? ba.getCountry().getName() : ""))
                                    && isSameValue(customerPostCode, ba.getZipCode())) {
                                billAddress = ba;
                                break;
                            }
                        }
                        if (billAddress == null) {
                            billAddress = new EdsAddress();
                            billAddress.setAddress(customerAddress);
                            billAddress.setCity(customerCity);
                            billAddress.setCountry(billingCountry);
                            billAddress.setZipCode(customerPostCode);

                            billAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
                            billAddress.setCrmAccount(crmAccount);
                            addressManager.create(billAddress);
                        }
                        billAddressID = billAddress.getObjectID();
                    }*/

                    if (productAccountID == null) {
                        AddAccountItem addAccountItem = new AddAccountItem();
                        addAccountItem.setAccountTypeId(accountingManager.getAccountTypeByCode(EdsAccountType.SALES).getObjectID());
                        addAccountItem.setName(beneficiaryAccountCode);
                        addAccountItem.setCode(beneficiaryAccountCode);
                        addAccountItem.setShowInExpense(false);
                        addAccountItem.setEnablePayments(false);
                        addAccountItem.setCurrencyID(baseCurrency.getId());
                        productAccountID = accountingService.createAccount(addAccountItem).getId();

                        chartAccountsMap.put(beneficiaryAccountCode, productAccountID);
                    }

                    /*if (productID == null) {
                        NewProduct product = new NewProduct();
                        product.setType(AccountingConstants.SERVICE);
                        product.setNumberData(productService.generateProductNumber());
                        product.setItemName(productName);
                        product.setAccountId(productAccountID);
                        product.setSellingPrice(productPrice);
                        product.setActive(true);

                        productID = productService.saveProduct(product).getId();
                        productsMap.put(itemManager.get(productID).getName().trim(), productID);
                    }*/

                    EdsAccount productAccount = accountingManager.get(productAccountID);

                    try {
                        createSalesInvoice(format, baseCurrency, user, customerID, billAddressID, malingAddressID,
                                invoiceNumber, invoiceDate, dueDate, projectID, reference, desription,
                                productName, productQty, productPrice, productAccount, productDiscount, productTaxPercent, invoiceTypeConstant);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notImportInvoices.append("Customer: ").append(customerName).append(", Main Project: ").append(parentProjectName).append(", Sub Project: ").append(projectName).append("\n");
                    }

                } else {

                    notImportInvoices.append("Customer: ").append(customerName).append(", Main Project: ").append(parentProjectName).append(", Sub Project: ").append(projectName).append("\n");
                }
            }
            hasHeader = false;
            rowIndex++;
        }

        System.out.println("Not Imported Invoices: ");
        System.out.println(notImportInvoices);

        return notImportInvoiceList.toString();
    }

    private SaveResult createSalesInvoice(DecimalFormat format, CurrencyItem baseCurrency, EdsUser user, Integer customerID, Integer billAddressID, Integer mailingAddressID,
                                          String invoiceNumber, Date invoiceDate, Date dueDate, Integer projectID, String reference, String description,
                                          String productName, BigDecimal productQty, BigDecimal productPrice, EdsAccount productAccount, BigDecimal discountPercent, BigDecimal taxPercent, Integer invoiceTypeConstant) {
        NewInvoice salesInvoice = new NewInvoice();

        InvoiceNumberData numberData = new InvoiceNumberData();
        if (invoiceNumber != null && invoiceNumber.trim().length() > 0) {
            try {
                if (invoiceNumber.length() > 4) {
                    Integer fourDigitNumber = Integer.parseInt(invoiceNumber.substring(invoiceNumber.length() - 4));
                    numberData.setFourDigitNumber(format.format(fourDigitNumber));
                    numberData.setPrefix(invoiceNumber.substring(0, invoiceNumber.length() - 4));
                } else {
                    Integer fourDigitNumber = Integer.parseInt(invoiceNumber);
                    numberData.setFourDigitNumber(format.format(fourDigitNumber));
                    numberData.setPrefix("");
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
            numberData = invoiceServiceLocal.generateAndGetSaleInvoiceNumber(user.getCompany());
            salesInvoice.setNumberData(numberData);
        }

        salesInvoice.setInvoiceType(invoiceTypeConstant);
        salesInvoice.setNumberData(numberData);
        salesInvoice.setInvoiceNumber(numberData.getInvoiceNumber());
        salesInvoice.setFourDigitNumber(salesInvoice.getNumberData().getFourDigitNumber());
        salesInvoice.setReference(reference);

        salesInvoice.setClientID(customerID);
        salesInvoice.setBillAddressID(billAddressID);
        salesInvoice.setMailAddressID(mailingAddressID);
        salesInvoice.setClientContactID(null);
        salesInvoice.setBaseCurrency(baseCurrency);
        salesInvoice.setCurrencyID(baseCurrency.getId());
        salesInvoice.setExchageRate(BigDecimal.ONE);

        Calendar invoiceDueDateCal = new GregorianCalendar();
        invoiceDueDateCal.setTime(invoiceDate);
        ServerUtils.setEndOfTheDay(invoiceDueDateCal);

        salesInvoice.setInvoiceDate(new DateNonConvertable(invoiceDate));
        salesInvoice.setDueDate(new DateNonConvertable(dueDate != null ? dueDate : invoiceDueDateCal.getTime()));


        BigDecimal netAmount = productQty.multiply(productPrice);
        BigDecimal taxAmount = BigDecimal.ZERO;

        NewInvoiceItem[] items = new NewInvoiceItem[1];
        items[0] = new NewInvoiceItem();
        items[0].setItemName(productName);
        items[0].setQuantity(productQty);
        items[0].setUnitPrice(productPrice);
        items[0].setNet(netAmount);
        items[0].setAccountID(productAccount.getObjectID());
        items[0].setDescription(description);

        if (projectID != null) {
            items[0].setProject(new SelectItem(projectID, ""));
        }

        /*BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountPercent != null && (discountPercent = discountPercent.setScale(2, BigDecimal.ROUND_HALF_UP)).compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = netAmount.multiply(discountPercent.divide(AccountingConstants.HUNDRED, 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        items[0].setDiscountPercent(discountPercent);
        items[0].setDiscountAmount(discountAmount);*/

        BigDecimal discountedNetAmount = netAmount.subtract(BigDecimal.ZERO);

        if (taxPercent != null && (taxPercent = taxPercent.setScale(2, RoundingMode.HALF_UP)).compareTo(BigDecimal.ZERO) > 0) {
            EdsVat customInvoiceTax = vatManager.getTaxForCustomInvoiceImport(taxPercent);
            if (customInvoiceTax == null) {
                customInvoiceTax = new EdsVat();
                customInvoiceTax.setName("Tax " + taxPercent);
                customInvoiceTax.setPermissionType(2);
                customInvoiceTax.setVatAmount(taxPercent);
                vatManager.create(customInvoiceTax);

                EdsTaxComponent taxComponent = new EdsTaxComponent();
                taxComponent.setName("Tax");
                taxComponent.setRate(taxPercent);
                taxComponent.setTax(customInvoiceTax);
                taxComponentManager.create(taxComponent);
            }

            items[0].setTaxItem(customInvoiceTax.createTaxItem());

            taxAmount = discountedNetAmount.multiply(taxPercent.divide(AccountingConstants.HUNDRED, 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);

            TotalTaxItem totalTaxItem = new TotalTaxItem();
            totalTaxItem.setTaxItem(items[0].getTaxItem());
            totalTaxItem.setTaxAmount(taxAmount);
            salesInvoice.setTotalTaxItems(new TotalTaxItem[]{totalTaxItem});
        }

        items[0].setTaxAmount(taxAmount);
        items[0].setTotalAmount(items[0].getNet());
        salesInvoice.setItems(items);

        BigDecimal totalAmount = discountedNetAmount.add(taxAmount);

        salesInvoice.setSubtotal(netAmount);
        salesInvoice.setTotal(totalAmount);
        salesInvoice.setTotalInInvoiceCurrency(totalAmount);
        salesInvoice.setTotalDiscount(BigDecimal.ZERO);
        salesInvoice.setTotalTaxes(taxAmount);
        salesInvoice.setBillableExpenseAmount(BigDecimal.ZERO);
        salesInvoice.setTaxCalculationType(2);
        salesInvoice.setStatusCode(Constants.APPROVE);
        salesInvoice.setType(Constants.RECEIVABLE);
        salesInvoice.setBookkeep(Boolean.TRUE);

        return invoiceServiceLocal.saveSaleInvoice(salesInvoice);
    }

    private BigDecimal parseBigDecimal(String columnValue) {
        try {
            return new BigDecimal(columnValue.trim().replace(",", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isSameValue(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 == null ? "" : s1.trim()).equalsIgnoreCase(s2 == null ? "" : s2.trim());
    }

    private boolean isValid(String s) {
        return s != null && s.trim().length() > 0;
    }

    private boolean isValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
