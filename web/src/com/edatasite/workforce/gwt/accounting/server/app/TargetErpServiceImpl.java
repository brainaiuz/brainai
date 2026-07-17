package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.target.BindingStub;
import com.edatasite.workforce.gwt.core.server.target.LoginResponse;
import com.edatasite.workforce.gwt.core.server.target.OmtypeLocator;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationSettingsItem;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shohruh on 24-Jan-17.
 */
@Transactional
@Service("targetErpService")
public class TargetErpServiceImpl implements TargetErpService {

    private static Logger log = LoggerFactory.getLogger(TargetErpServiceImpl.class);

    private static final String TABLE_CLIENT = "CF", KEY_CLIENT = "COD_CF";
    private static final String TABLE_CONTACT = "CF_CNT_CRM", KEY_CONTACT = "COD_CNT";
    private static final String TABLE_INVOICE = "FATT_CLI", KEY_INVOICE = "DOC_ID";
    private static final String TABLE_INVOICE_ITEM = "FATT_CLI_RIGHE", KEY_INVOICE_ITEM = "DOC_RIGA_ID";

    @Autowired
    private CRMService crmService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    @Qualifier("profileService")
    private ProfileServiceLocal profileService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;

    @Override
    public String sendClientToTarget(Integer id, Boolean isClient) {
        IntegrationSettingsItem integrationSettings = profileService.getIntegrationSettingsWithPass();
        CrmAccountItem item = crmService.getAccount(id, null);

        String msg = validateCrmAccount(item);
        if (!msg.isEmpty()) return msg;

        BindingStub binding = null;
        String connectionid = null;

        try {
            binding = getBinding(integrationSettings.getTgUrl().replaceFirst("\\.wsdl$", ""));

            LoginResponse value = binding.login(integrationSettings.getTgUsername(), integrationSettings.getTgPassword(), integrationSettings.getTgController(), "");
            if (!"CONNESSIONE AVVENUTA".equals(value.get_return())) {
                return value.get_return();
            }

            connectionid = value.get_connectionid();

            String current = checkClientExistense(binding, connectionid, item, isClient);

            msg = insertOrUpdateClient(binding, connectionid, item, current, isClient);
        } catch (Exception e) {
            e.printStackTrace();
            msg += e.getMessage();
        } finally {
            try {
                if (binding != null && connectionid != null) {
                    binding.logout(connectionid);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    private String checkClientExistense(BindingStub binding, String connectionId, CrmAccountItem item, Boolean isClient) throws RemoteException {
        String flag = isClient ? "CF.FLAG_CLI=1" : "CF.FLAG_FOR=1";
        String where = String.format("CF.OLD_COD_CF='%s' AND %s", item.getCode(), flag);
        String resp = binding.getListRecords(connectionId, TABLE_CLIENT, "", 0, where, "", 0, "", "", "");
        resp = resp.replaceAll("(<!\\[CDATA\\[|]]>)", "");
        if (resp.contains("NUMERO_RECORD=\"1\"")) item.setTargetId(getClientCode(resp));
        return resp;
    }

    private String insertOrUpdateClient(BindingStub binding, String connectionid, CrmAccountItem item, String current, boolean isClient) throws IOException, SolrServerException {
        String recCF = wrapXml(buildClientRecordXml(item, isClient, current), 41, 1);
        log.info("REQUEST: " + recCF);

        String resp;StringBuilder msg = new StringBuilder();
        if (item.getTargetId() == null || item.getTargetId().isEmpty()) {
            resp = binding.insertRecord(connectionid, TABLE_CLIENT, recCF);
        } else {
            resp = binding.updateRecord(connectionid, TABLE_CLIENT, recCF, item.getTargetId());
//                binding.deleteRecord(connectionid, TABLE_CONTACT, getKey(FIELD_CLIENT_ID, item.getTargetId()));
        }

        log.info("RESPONSE: " + resp);
        msg.append(getMessage(resp));

        item.setTargetId(getCode(resp));

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCrmAccountId(item.getObjectId());

        item.setContacts(contactService.getNewContactList(fp).getList());

        for (ContactListItem contact : item.getContacts()) {
            String recCNT = buildContactRecordXml(item, contact);
            recCNT = wrapXml(recCNT, 22, item.getContacts().size());
            log.info("REQUEST: " + recCNT);
            resp = binding.insertRecord(connectionid, TABLE_CONTACT, recCNT);
            log.info("RESPONSE: " + resp);
            msg.append(getMessage(resp));
        }

        if (item.getTargetId() != null && !item.getTargetId().isEmpty() && (msg.isEmpty())) {
            EdsCrmAccount account = crmAccountManager.get(item.getObjectId());
            account.setInTarget(true);
            account.setTargetId(item.getTargetId());
            crmAccountManager.update(account);

            try {
                crmAccountSolrComponent.index(account);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (msg.length() == 0) {
                msg = new StringBuilder(String.format("OK:%s = %s", KEY_CLIENT, item.getTargetId()));
            }
        }
        return msg.toString();
    }

    @Override
    public String sendInvoiceToTarget(Integer id) {
        IntegrationSettingsItem integrationSettings = profileService.getIntegrationSettingsWithPass();
        NewInvoice invoice = invoiceService.getInvoiceSummaryData(id);
        CrmAccountItem client = crmService.getAccount(invoice.getClientID(), null);

        StringBuilder msg = new StringBuilder(validateInvoice(invoice, client));
        if (msg.length() > 0) return msg.toString();

        BindingStub binding = null;
        String connectionid = null, resp;
        try {
            binding = getBinding(integrationSettings.getTgUrl().replaceFirst("\\.wsdl$", ""));

            LoginResponse value = binding.login(integrationSettings.getTgUsername(), integrationSettings.getTgPassword(), integrationSettings.getTgController(), "");

            if (!"CONNESSIONE AVVENUTA".equals(value.get_return())) {
                return value.get_return();
            }

            connectionid = value.get_connectionid();

            checkClientExistense(binding, connectionid, client, true);
            if (client.getTargetId() == null || client.getTargetId().isEmpty()) {
                msg = new StringBuilder(insertOrUpdateClient(binding, connectionid, client, null, true));
                if (!msg.toString().startsWith("OK")) return msg.toString();
                msg = new StringBuilder();
            }

            String current = checkInvoiceExistense(binding, connectionid, invoice);

            String recInv = wrapXml(buildInvoiceRecordXml(invoice, client, current), "EUR".equals(invoice.getCurrencyName()) ? 54 : 56, 1);
            log.info("REQUEST: " + recInv);

            if (invoice.getTargetId() == null || invoice.getTargetId().isEmpty()) {
                resp = binding.insertRecord(connectionid, TABLE_INVOICE, recInv);
            } else {
                deleteInvoiceItems(binding, connectionid, invoice.getTargetId());
                resp = binding.updateRecord(connectionid, TABLE_INVOICE, recInv, invoice.getTargetId());
            }

            log.info("RESPONSE: " + resp);
            msg.append(getMessage(resp));

            invoice.setTargetId(getCode(resp));

            for (int i = 0; i < invoice.getItems().length; i++) {
                String recItem = buildInvoiceItemRecordXml(invoice.getItems()[i], invoice.getTargetId(), i + 1, "EUR".equals(invoice.getCurrencyName()), invoice.isCreditNote());
                recItem = wrapXml(recItem, 56, 1);
                log.info("REQUEST: " + recItem);
                resp = binding.insertRecord(connectionid, TABLE_INVOICE_ITEM, recItem);
                log.info("RESPONSE: " + resp);

                msg.append(getMessage(resp));
            }

            if (invoice.getTargetId() != null && !invoice.getTargetId().isEmpty() && ((msg.length() == 0) || msg.toString().startsWith("OK"))) {
                EdsSaleInvoice inv = (EdsSaleInvoice) invoiceManager.get(id);
                inv.setInTarget(true);
                inv.setTargetId(invoice.getTargetId());
                invoiceManager.update(inv);

                saleInvoiceSolrComponent.index(inv);
                if (msg.isEmpty()) {
                    msg.append(String.format("OK:%s = %s", KEY_INVOICE, invoice.getTargetId()));
                }
            }

            return msg.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        } finally {
            try {
                if (binding != null && connectionid != null) {
                    binding.logout(connectionid);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }

    private String checkInvoiceExistense(BindingStub binding, String connectionId, NewInvoice invoice) throws RemoteException {
        String resp = null;
        if (invoice.getTargetId() != null && !invoice.getTargetId().isEmpty()) {
            resp = binding.getListRecords(connectionId, TABLE_INVOICE, "", 0, "DOC_ID = '" + invoice.getTargetId() + "'", "", 0, "", "", "");
            resp = resp.replaceAll("(<!\\[CDATA\\[|]]>)", "");
            if (!resp.contains("NUMERO_RECORD=\"1\"")) {
                invoice.setTargetId(null);
                resp = null;
            }
        }
        return resp;
    }

    private String validateInvoice(NewInvoice invoice, CrmAccountItem client) {

        /*if (!isValid(invoice.getReference())) msg += "Missing Reference; ";
        if (!isValid(invoice.getPoNumber())) msg += "Missing PO Number; ";
        if (!isValid(client.getPaymentMethod())) msg += "Missing Payment Method; ";*/

        return "";
    }

    private String validateCrmAccount(CrmAccountItem item) {
        String msg = "";

        /*if (!isValid(item.getVatNumber())) msg += "Missing VAT Number; ";
        if (!isValid(item.getPaymentMethod())) msg += "Missing Payment Method; ";*/

        return "";
    }

    private boolean isValid(String s) {
        return s != null && !s.isEmpty();
    }

    private String deleteInvoiceItems(BindingStub binding, String connectionId, String targetId) throws RemoteException {
        String resp = binding.getListRecords(connectionId, TABLE_INVOICE_ITEM, "", 0, "DOC_ID = '" + targetId + "'", "", 0, "", "", "");
        List<String> itemKeys = extractInvoiceItemKeys(resp);
        for (String key : itemKeys) {
            binding.deleteRecord(connectionId, TABLE_INVOICE_ITEM, key);
        }
        return "";
    }

    private List<String> extractInvoiceItemKeys(String resp) {
        ArrayList<String> keys = new ArrayList<>();
        String[] strings = resp.replaceAll("(<!\\[CDATA\\[|]]>)", "").split("<DOC_RIGA_ID>");
        for (int i = 1; i < strings.length; i++) {
            keys.add(strings[i].split("</DOC_RIGA_ID>")[0]);
        }
        return keys;
    }

    private BindingStub getBinding(String url) throws ServiceException {
        BindingStub binding = (BindingStub) new OmtypeLocator().getPort(url);

        if (binding != null) {
            // Time out after a minute
            binding.setTimeout(60000);
        }

        return binding;
    }

    private String buildClientRecordXml(CrmAccountItem account, Boolean isClient, String current) {
        StringBuilder s = new StringBuilder("<CF>");

        s.append(getTaggedString("COD_CF", account.getTargetId()));
        s.append(getTaggedString("RAG_SOC_CF", account.getName()));
        s.append(getTaggedString("RAG_SOC_CF_INT", "", current));
        s.append(getTaggedString("INDI_CF", account.getDefaultAddress(true).getAddress()));
        s.append(getTaggedString("CAP_CF", account.getDefaultAddress(true).getZipCode()));
        s.append(getTaggedString("COMUNE_CF", account.getDefaultAddress(true).getCity()));
        s.append(getTaggedString("PROVINCIA_CF", account.getDefaultAddress(true).getState()));//province
        s.append(getTaggedString("STATO_CF", account.getDefaultAddress(true).getCountryCode()));
        s.append(getTaggedString("TEL_CF", account.getPhone()));
        s.append(getTaggedString("FAX_CF", account.getFax()));
        s.append(getTaggedString("E_MAIL_CF", account.getEmail()));
        s.append(getTaggedString("NOTE_CF", "", current));//notes
        s.append(getTaggedString("P_IVA_CF", account.getVatNumber(), current));
        s.append(getTaggedString("COD_FISC_CF", "", current));//Tax
        s.append(getTaggedString("FLAG_CLI", isClient ? 1 : 0));
        s.append(getTaggedString("FLAG_FOR", isClient ? 0 : 1));
        s.append(getTaggedString("TEK_FLAG_CLI_POTENZIALE", 0, current));
        s.append(getTaggedString("TEK_FLAG_FOR_POTENZIALE", 0, current));
        s.append(getTaggedString("TITOLO_CF", "", current));//title
        s.append(getTaggedString("COD_LINGUA", "", current));//language code
        s.append(getTaggedString("COD_CAT", 0, current));//category code
        s.append(getTaggedString("DES_CAT", "", current));//category desc
        s.append(getTaggedString("COD_ZONA", "", current));//area code
        s.append(getTaggedString("TEK_INDI_WEB", "", current));//web address
        s.append(getTaggedString("COD_PAGA", "", current));//payment code
        s.append(getTaggedString("DES_PAGA", ""/*account.getPaymentMethod()*/, current));//payment description
        s.append(getTaggedString("COD_LIST_STD", "", current));//regular standard
        s.append(getTaggedString("COD_LIST", "", current));//regular staff
        s.append(getTaggedString("COD_AGE", "", current));//agent code
        s.append(getTaggedString("NOME_AGE", "", current));//agent name
        s.append(getTaggedString("MAPPA_WEB", "", current));//map
        s.append(getTaggedString("ITINERARIO_WEB", "", current));//map direction
        s.append(getTaggedString("WEB_CF", "", current));//client website
        s.append(getTaggedString("LATITUDINE", "", current));//latitude
        s.append(getTaggedString("LONGITUDINE", "", current));//longitude
        s.append(getTaggedString("FLAG_NO_ORDINI", 0, current));//no orders
        s.append(getTaggedString("FLAG_NO_CONS", 0, current));//delivery blocked
        s.append(getTaggedString("FLAG_CONTROLLI_CF", 0, current));//check while inserting
        //s.append(getTaggedString("CAMPO_DESC_AGG_WEB_1", account.getOwnerName()));
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_1", account.getOwnerNames()));
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_2", isClient ? account.getBankAccount() : account.getIbanCode()));//IBAN
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_3", account.getPaymentMethod()));

        s.append("</CF>");
        return s.toString();
    }


    private String buildContactRecordXml(CrmAccountItem account, ContactListItem contact) {
        StringBuilder s = new StringBuilder("<CF_CNT_CRM>");

        s.append(getTaggedString("COD_CF_CNT", ""));
        s.append(getTaggedString("COD_CNT", ""));
        s.append(getTaggedString("DES_CNT", contact.getName()));
        s.append(getTaggedString("COD_CNT_PADRE", ""));
        s.append(getTaggedString("COD_CF", account.getTargetId()));
        s.append(getTaggedString("E_MAIL_CNT", contact.getPrimaryEmail()));
        s.append(getTaggedString("SOCIETA_CONTATTO", account.getName()));//company name
        s.append(getTaggedString("NUM_SEDE", ""));//location code
        s.append(getTaggedString("POSIZIONE_CONTATTO", contact.getPosition()));//contact location
        s.append(getTaggedString("TEL_CONTATTO", contact.getPrimaryPhone()));
        s.append(getTaggedString("FAX_CONTATTO", getValFromList(contact.getHomeFax(), contact.getWorkFax())));//fax
        s.append(getTaggedString("CELL_CONTATTO", getValFromList(contact.getMobile())));//mobile
        s.append(getTaggedString("NOTE_CONTATTO", contact.getNote()));//notes
        s.append(getTaggedString("RUOLO", ""));//contact role
        s.append(getTaggedString("FLAG_NODO_CNT", 0));//has sub-branches
        s.append(getTaggedString("RAG_SOC_CF", account.getName()));//business name
        s.append(getTaggedString("INDI_CF", contact.getPrimaryAddress() != null ? contact.getPrimaryAddress().getAddress() : ""));//address
        s.append(getTaggedString("COMUNE_CF", ""));//common
        s.append(getTaggedString("PROVINCIA_CF", contact.getPrimaryAddress() != null ? contact.getPrimaryAddress().getState() : ""));//province
        s.append(getTaggedString("CAP_CF", contact.getPrimaryAddress() != null ? contact.getPrimaryAddress().getZipCode() : ""));//zip code
        s.append(getTaggedString("MAPPA_WEB", ""));//map
        s.append(getTaggedString("ITINERARIO_WEB", ""));//direction

        s.append("</CF_CNT_CRM>");
        return s.toString();
    }

    private String buildInvoiceRecordXml(NewInvoice invoice, CrmAccountItem customer, String current) {
        Date invoiceDate = invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().getDate() : null;

        StringBuilder s = new StringBuilder("<FATT_CLI>");

        s.append(getTaggedString("DOC_ID", invoice.getTargetId()));
        s.append(getTaggedString("ANNO_DOC", "", current));//year
        s.append(getTaggedString("NUM_DOC", invoice.getInvoiceNumber().replaceAll("\\D", "")));//invoice number
        s.append(getTaggedString("SERIE_DOC", "", current));//invoice series
        s.append(getTaggedString("DATA_DOC", ServerUtils.getTimeFormatted(invoiceDate, "dd/MM/yyyy")));//invoice date
        s.append(getTaggedString("COD_CF", customer.getTargetId()));//customerID
        s.append(getTaggedString("RAG_SOC_CF", invoice.getClientName()));
        s.append(getTaggedString("TOT_MERCE_V1", "", current));//items total
        s.append(getTaggedString("IMPONIBILE_V1", "", current));//taxable
        s.append(getTaggedString("IVA_V1", "", current));//VAT
        s.append(getTaggedString("TOTALE_V1", "", current));//invoice total
        s.append(getTaggedString("COD_REG_IVA", "", current));//VAT registration code
        s.append(getTaggedString("COD_IVA", "", current));//VAT code
        s.append(getTaggedString("COD_LIST", "", current));//list payment method
        s.append(getTaggedString("COD_CAUS_DOC", invoice.isCreditNote() ? "NAC" : "", current));//reason code
        s.append(getTaggedString("FLAG_REGISTRATA", 0, current));//recorded in Accounting
        s.append(getTaggedString("DES_CAUS_DOC", "", current));//causal description
        s.append(getTaggedString("DES_STATO_DOC", invoice.getStatusCode()));//invoice status
        s.append(getTaggedString("FLAG_MODIFICABILE", 1, current));//isEditable
        s.append(getTaggedString("COD_PAGA", "", current));//payment code
        s.append(getTaggedString("SCONTO_PAG", "", current));//payment discount
        s.append(getTaggedString("DATA_INIZIO_PAG", "", current));//payment date
        s.append(getTaggedString("COD_DEP", "", current));//deposite date
        s.append(getTaggedString("COD_DEP_2", "", current));//deposite date2
        s.append(getTaggedString("NUM_SEDE", "", current));//commercial office
        s.append(getTaggedString("NUM_DEST", "", current));//destination number
        s.append(getTaggedString("DES_DEST_MERCE", "", current));//destination catalog
        s.append(getTaggedString("INDI_DEST_MERCE", "", current));//address destination goods
        s.append(getTaggedString("COMUNE_DEST_MERCE", "", current));//common destination catalog
        s.append(getTaggedString("CAP_DEST_MERCE", "", current));//postcode
        s.append(getTaggedString("PROVINCIA_DEST_MERCE", "", current));//province
        s.append(getTaggedString("STATO_DEST_MERCE", "", current));//country
        s.append(getTaggedString("TEL_DEST_MERCE", "", current));//tel
        s.append(getTaggedString("FAX_DEST_MERCE", "", current));//fax
        s.append(getTaggedString("E_MAIL_DEST_MERCE", "", current));//email
        s.append(getTaggedString("P_IVA_CF_DEST_MERCE", "", current));//VAT, receipent
        s.append(getTaggedString("COD_FISC_CF_DEST_MERCE", "", current));//tax code receipent
        s.append(getTaggedString("DES_PAGA", "", current));//payment desc
        s.append(getTaggedString("COD_AGE", "", current));//agent code
        s.append(getTaggedString("NOME_AGE", "", current));//agent name
        s.append(getTaggedString("FLAG_FATTURA_ACC", "", current));//isInvoice
        s.append(getTaggedString("FLAG_NO_ORDINI", "", current));//notAcceptOrders
        s.append(getTaggedString("FLAG_NO_CONS", "", current));//deliveryBlocked
        s.append(getTaggedString("FLAG_CLI_O_FOR", "", current));//0=Ciente,1=Fornitore,2=Contatto
        s.append(getTaggedString("NOTE_CONSEGNA", "", current));//delivery notes
        s.append(getTaggedString("NOTE_TRAS", invoice.getShippingMethod() != null ? invoice.getShippingMethod().getDescription() : "", current));//shipping notes
        s.append(getTaggedString("NOTE_INT", "", current));//internal notes
        s.append(getTaggedString("NOTE_STAMPA", "", current));//delivery notes
        s.append(getTaggedString("UTENTE_INS", "", current));//user input
        s.append(getTaggedString("UTENTE_MOD", "", current));//user input
        s.append(getTaggedString("IMPORTO_SPESA_V1", "", current));//amount spent
        if (!"EUR".equals(invoice.getCurrencyName())) {
            s.append(getTaggedString("COD_DIVISA", invoice.getCurrencyName()));//currency
            s.append(getTaggedString4("CAMBIO", invoice.getExchageRate() != null ? 1.0 / invoice.getExchageRate().doubleValue() : 1.0));//currency
        }
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_1", invoice.getReference(), current));//reference
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_2", invoice.getPoNumber(), current));//PO code
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_3", customer.getPaymentMethod(), current));//pay method

        s.append("</FATT_CLI>");
        return s.toString();
    }

    String buildInvoiceItemRecordXml(NewInvoiceItem item, String invoiceId, Integer rowNum, boolean inEUR, boolean isCreditNote) {
        StringBuilder s = new StringBuilder("<FATT_CLI_RIGHE>");

        s.append(getTaggedString("DOC_ID", invoiceId));
        s.append(getTaggedString("DOC_RIGA_ID", ""));//row_id
        s.append(getTaggedString("NUM_RIGA", rowNum));//row num
        s.append(getTaggedString("NUM_SORT", ""));//num of ordering line
        s.append(getTaggedString("COD_ART", item.getAccountItem() != null ? item.getAccountItem().getCode() : item.getAccountName()));//sales account
        s.append(getTaggedString("COD_VAR", ""));//sales account?
        s.append(getTaggedString("DES_RIGA", item.getDescription()));//line description
        s.append(getTaggedString("UM", ""));//unit of measure
        s.append(getTaggedString4("QUANT_RIGA", (isCreditNote ? -1.0 : 1.0) * item.getQuantity().doubleValue()));//qty
        s.append(getTaggedString4("PREZZO_LORDO_VU1", inEUR ? item.getUnitPrice().doubleValue() : 0.0));//gross price in base
        s.append(getTaggedString4("PREZZO_LORDO_VU2", inEUR ? 0.0 : item.getUnitPrice().doubleValue()));//gross price in invoice
        s.append(getTaggedString("PREZZO_NETTO_VU1", 0.0));//net price in base
        s.append(getTaggedString("PREZZO_NETTO_VU2", 0.0));//net price in invoice
        s.append(getTaggedString("IMPORTO_V1", 0.0));//amount in base
        s.append(getTaggedString("IMPORTO_V2", 0.0));//amount in invoice
        s.append(getTaggedString("PREZZO_LISTINO_VU1", 0.0));//list price
        s.append(getTaggedString("NO_RICALC_PRZ", 0));//notRecalculate
        s.append(getTaggedString("UM_BASE", ""));//base unit of measure
        s.append(getTaggedString("QUANT_UM_BASE", 0.0));//qty in base unit
        s.append(getTaggedString("COD_LIST", ""));//list
        s.append(getTaggedString("COD_DEP", ""));//deposite code
        s.append(getTaggedString("COD_DEP_2", ""));//Secondary storage code
        s.append(getTaggedString("DES_DEP", ""));//store Description
        s.append(getTaggedString4("SCONTO_1", item.getDiscountPercent().doubleValue()));//discount 1
        s.append(getTaggedString("SCONTO_2", 0.0));//discount 2
        s.append(getTaggedString("SCONTO_3", 0.0));//discount 3
        s.append(getTaggedString("SCONTO_4", 0.0));//discount 4
        s.append(getTaggedString("SCONTO_5", 0.0));//discount 5
        s.append(getTaggedString("SCONTO_PAG", 0.0));//financial discount
        s.append(getTaggedString("NOTE", ""));//note
        s.append(getTaggedString("PESO", 0.0));//line weight
        s.append(getTaggedString("FLAG_OMAGGIO", 0));//isRowTribute
        s.append(getTaggedString("FLAG_ADDEBITO_IVA", 0));//isDebitTaxRow
        s.append(getTaggedString("ORD_RIGA_ID", ""));//id-line order
        s.append(getTaggedString("DDT_RIGA_ID", ""));//id-DDT row
        s.append(getTaggedString("COD_CF", ""));//customer id
        s.append(getTaggedString("RAG_SOC_CF", ""));//customer name
        s.append(getTaggedString("DATA_DOC", ""));//invoice date
        s.append(getTaggedString("GIACENZA", 0.0));//stock
        s.append(getTaggedString("DISPONIBILE", 0.0));//available
        s.append(getTaggedString("DISPONIBILE_FUTURO", 0.0));//future available
        s.append(getTaggedString("FLAG_SCARICA_DISTINTA", 0));//movesBill
        s.append(getTaggedString("FLAG_PROVV_RIGA_MAN", 0));//manualCommissions
        s.append(getTaggedString("COD_AGE", ""));//agent code
        s.append(getTaggedString("PERC_PROVV", 0.0));//commission percentage
        s.append(getTaggedString("LISTA_LOTTI", ""));//list batches separated by
        s.append(getTaggedString("LISTA_MATRICOLE", ""));//list separate from numbers
        s.append(getTaggedString("LISTA_TAGLIE", ""));//sizes separated by the list
        s.append(getTaggedString("LISTA_LOCAZIONI", ""));//separate locations from the list
        s.append(getTaggedString("NOTE_INT", ""));//for internal use notes
        s.append(getTaggedString("COMP_ECON_DATA_INIZ", ""));//start date accrual
        s.append(getTaggedString("COMP_ECON_DATA_FINE", ""));//end date accrual
        s.append(getTaggedString("DOC_RIGA_ID_VIS", ""));//Reference DDT account overview
        s.append(getTaggedString("COD_SECONDARIO_ART", item.getItemNumber()));//subcode
        s.append(getTaggedString("DES_COD_SECONDARIO_ART", ""));//Description Sub code
        s.append(getTaggedString("CAMPO_DESC_AGG_WEB_1", item.getItemName()));

        s.append("</FATT_CLI_RIGHE>");
        return s.toString();
    }

    private String getTaggedString(String tag, int value) {
        return getTaggedString(tag, value, new String[]{});
    }

    private String getTaggedString(String tag, String value, String... current) {
        if (current.length > 0 && current[0] != null && !current[0].isEmpty()) {
            String val = extractValue(current[0], tag);
            value = val.isEmpty() ? value : val;
        }
        return value != null && !value.isEmpty() ? String.format("<%1$s><![CDATA[%2$s]]></%1$s>", tag, value) : String.format("<%1$s></%1$s>", tag);
    }

    private String getTaggedString(String tag, int value, String... current) {
        if (current.length > 0 && current[0] != null && !current[0].isEmpty()) {
            String val = extractValue(current[0], tag);
            value = val.isEmpty() ? value : Integer.parseInt(val);
        }
        return String.format("<%1$s>%2$d</%1$s>", tag, value);
    }

    private String getTaggedString(String tag, double value) {
        return String.format(Locale.ITALY, "<%1$s>%2$.2f</%1$s>", tag, value);
    }

    private String getTaggedString4(String tag, double value) {
        return String.format(Locale.ITALY, "<%1$s>%2$.4f</%1$s>", tag, value);
    }

    private String wrapXml(String xml, Integer columns, Integer records) {
        return String.format("<?xml version='1.0' encoding='UTF-16' ?><DATI COLONNE=\"%2$d\" NUMERO_RECORD=\"%3$d\" RECORD=\"%3$d\">%1$s</DATI>", xml, columns, records);
    }

    private String extractValue(String resp, String tag) {
        String startTag = String.format("<%s>", tag);
        String endTag = String.format("</%s>", tag);
        return resp.contains(startTag) ? resp.split(startTag)[1].split(endTag)[0].replaceAll("(<!\\[CDATA\\[|]]>)", "") : "";
    }

    private String getCode(String resp) {
        return extractValue(resp, "CODICE");
    }

    private String getClientCode(String resp) {
        return extractValue(resp, "COD_CF");
    }

    private String getMessage(String resp) {
        String msg = extractValue(resp, "MESSAGGIO");
        return msg.isEmpty() ? msg : msg + "; ";
    }

    private String getValFromList(ArrayList<String>... vals) {
        for (ArrayList<String> val : vals) {
            if (val != null && val.size() > 0) {
                return val.get(0);
            }
        }
        return "";
    }

    private String getRefCode(SelectItem[] refs) {
        if (refs != null) {
            for (SelectItem ref : refs) {
                if (ref.isSelected()) return ref.getName();
            }
        }
        return "";
    }
}
