package com.edatasite.workforce.gwt.core.server.gwd;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/22/13
 * Time: 4:54 PM
 */
@Service("gwdWebService")
public class GWDWebService {


    private static final String GWD_API_URL = "http://www.grantonworlddev.com/kpi/view?asec=qweasdzxc&token=e5h779&country=**&action=sales";
    private static final Integer ADMIN_USER_ID = 1;
    private static final Integer BATCH_LIMIT = 50;
    private static Logger log = LoggerFactory.getLogger(GWDWebService.class);
    @Autowired
    private ClientService clientService;// = (ClientService) ApplicationContextProvider.applicationContext.getBean("clientService");
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;// = (InvoiceService) ApplicationContextProvider.applicationContext.getBean("invoiceService");
    @Autowired
    private ClientManager clientManager;// = (ClientManager) ApplicationContextProvider.applicationContext.getBean("clientManager");
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;// = (GlobalAuthJdbcSpringManager) ApplicationContextProvider.applicationContext.getBean("globalAuthJdbcSpringManager");
    @Autowired
    private UserManager userManager;// = (UserManager) ApplicationContextProvider.applicationContext.getBean("userManager");
    @Autowired
    private AccountingManager accountingManager;// = (AccountingManager) ApplicationContextProvider.applicationContext.getBean("accountingManager");
    @Autowired
    private InvoiceManager invoiceManager;// = (InvoiceManager) ApplicationContextProvider.applicationContext.getBean("invoiceManager");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void startSyncData() {

        for (GWDCompany gwdCompany : GWDCompany.values()) {

            SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(gwdCompany.getCompanyID()));
            SecurityContext.getInstance().setCompanyId(gwdCompany.getCompanyID());
            EdsUser user = userManager.getUserByUserID(ADMIN_USER_ID);
            SecurityContext.getInstance().setStaticUserID(user.getObjectID());

            EdsAccount salesAccount = accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES);
            Integer currencyID = accountingManager.getCurrencyFromFinancialSettings(user.getCompany().getObjectID());
            try {
                Gson gson = new Gson();
                String apiUrl = GWD_API_URL.replace("**", gwdCompany.getCountryCode());
                GWDData[] syncItems = gson.fromJson(readJsonAsString(apiUrl), GWDData[].class);
                HashMap<String, Integer> savedClientMap = saveNewCustomers(syncItems, user.getObjectID());
                log.info("User is null-> " + user == null ? "true" : "false");
                log.info("SalesAccount is null -> " + salesAccount == null ? "true" : "false");
                log.info("CompanyID ->" + gwdCompany.getCompanyID());
                log.info("CurrencyID ->" + currencyID);
                createAndSaveNewInvoices(syncItems, savedClientMap, user.getObjectID(), salesAccount.getObjectID(), currencyID);
            } catch (Exception e) {
                log.error("Error when parse GWD Json Data");
                e.printStackTrace();
            }
        }
    }

    private void createAndSaveNewInvoices(GWDData[] syncItems, HashMap<String, Integer> savedClientMap, Integer userID, Integer salesAccountID, Integer currencyID) {
        List<NewInvoice> invoiceList = new ArrayList<>();
        NewInvoice newInvoice;
        NewInvoiceItem newInvoiceItem;
        Date invoiceDate;
        BigDecimal totalAmount;
        InvoiceNumberData numberData;
        boolean checkInvoiceForExisting = false;
        log.info("Start create invoice from GWDItems");
        for (GWDData gwdData : syncItems) {
            try {
                //log.info("GWDItem invoice manager  -> " + invoiceManager == null ? "true" : "false");
                checkInvoiceForExisting = invoiceManager.checkInvoiceForExisting(gwdData.getRef());
                if (!checkInvoiceForExisting) {
                    newInvoice = new NewInvoice();
                    newInvoice.setUserID(userID);
                    newInvoice.setClientID(savedClientMap.get(gwdData.getFullName()));
                    newInvoice.setCurrencyID(currencyID);
                    newInvoice.setReference(gwdData.getRef());
                    invoiceDate = dateFormat.parse(gwdData.getCreationdate());
                    newInvoice.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(invoiceDate)));
                    newInvoice.setDueDate(new DateNonConvertable(DateUtil.getDayLastTime(invoiceDate)));
                    /* start create invoice items*/
                    newInvoiceItem = new NewInvoiceItem();
                    newInvoiceItem.setItemName(gwdData.getTitle());
                    newInvoiceItem.setQuantity(gwdData.getQuantity());
                    newInvoiceItem.setUnitPrice(gwdData.getPrice());
                    newInvoiceItem.setAccountID(salesAccountID);
                    totalAmount = gwdData.getPrice().multiply(gwdData.getQuantity());
                    newInvoiceItem.setNet(totalAmount);
                    newInvoiceItem.setTotalAmount(totalAmount);
                    newInvoice.setItems(new NewInvoiceItem[]{newInvoiceItem});
                    /* end */
                    newInvoice.setSubtotal(totalAmount);
                    newInvoice.setTotalInInvoiceCurrency(totalAmount);
                    newInvoice.setTotal(totalAmount);
                    newInvoice.setType("RECEIVABLE");
                    newInvoice.setInvoiceCustomType("PRODUCT_INVOICE");
                    newInvoice.setStatusCode("APPROVE");
                    newInvoice.setExchageRate(BigDecimal.ONE);
                    newInvoice.setReference(gwdData.getRef());
                    numberData = invoiceServiceLocal.getSaleInvoiceNumber();
                    if (numberData != null) {
                        newInvoice.setFourDigitNumber(numberData.getFourDigitNumber());
                        newInvoice.setInvoiceNumber(numberData.getInvoiceNumber());
                    }

                    invoiceServiceLocal.saveSaleInvoice(newInvoice);
                }
            } catch (Exception e) {
                log.error("Error when create invoices from  GWD Json data");
                e.printStackTrace();
            }
        }
        log.info("GWDItem create invoices end");
    }

    private HashMap<String, Integer> saveNewCustomers(GWDData[] syncItems, Integer userID) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        List<String> uniqList = new ArrayList<>();
        CrmAccountItem crmAccountItem;
        Integer savedClientId;
        for (GWDData data : syncItems) {
            if (data.validateForCreateNewCustomer()) {
                if (!uniqList.contains(data.getFullName())) {
                    uniqList.add(data.getFullName());
                    savedClientId = clientManager.checkGWDCustomerForExists(data.getFullName());
                    if (savedClientId != null) {
                        resultMap.put(data.getFullName(), savedClientId);
                    } else {
                        crmAccountItem = new CrmAccountItem();
                        crmAccountItem.setName(data.getFullName());
                        crmAccountItem.setEmail(data.getEmail());
                        crmAccountItem.setPhone(data.getPhone() != null ? data.getPhone() : "");
                        savedClientId = clientService.createClient(crmAccountItem, userID);
                        resultMap.put(data.getFullName(), savedClientId);
                    }
                }
            }
        }
        return resultMap;
    }

    public String readJsonAsString(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

}
