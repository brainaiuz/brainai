package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SalesInvoiceAddTO;
import ezvcard.VCard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 1/13/2016.
 */
public interface ImportingServiceLocal {

    ArrayList<RejectedImportRecord[]> importAccounts(ImportFile importFile, List dataBank, String from);

    void importVCardAccounts(ImportFile importFile, List<VCard> items, String type, String from);

    ArrayList<RejectedImportRecord[]> importContacts(ImportFile importFile, List listOfRows, int contactType, Integer mailListId) throws Exception;

    Set<Integer> importVCardContacts(ImportFile importFile, List<VCard> listOfRows, Set<Integer> savedLeadsHashCodes, int contactType) throws Exception;

    ArrayList<RejectedImportRecord[]> importOpportunities(ImportFile importFile, List<String[]> list);

    ArrayList<RejectedImportRecord[]> importEmployees(ImportFile importFile, List<String[]> list, String employee, Integer companyId, Integer parentEmployeeId);

    ArrayList<RejectedImportRecord[]> importExpense(ImportFile importFile, List<String[]> lists, Integer companyId, Integer sourceID);

    int createEntityMailList(EdsMailList mailList, List<Integer> iDs);

    LinkedList<RejectedImportRecord[]> importManualTransaction(ImportFile importFile, List<String[]> listOfRows);

    LinkedHashMap<NewManualTransaction, NewManualTransaction> importManualTransactionTally(ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importAdditionalPayment(ImportFile importFile, List<String[]> listOfRows);

    StringBuilder batchImportInvoices(SalesInvoiceAddTO[] items, EdsImportFile file);

    ArrayList<RejectedImportRecord[]> importBatchInvoiceWithoutPayment(ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importSalesOrder(ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importBatchInvoicePayment(ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importProducts(ImportFile importFile, List<String[]> dataBank);

    ArrayList<RejectedImportRecord[]> importProductCategories(ImportFile importFile, List<String[]> data);

    ArrayList<RejectedImportRecord[]> importBrands(ImportFile importFile, List<String[]> data);

    ArrayList<RejectedImportRecord[]> importPurchaseOrder(ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importEmployeeLeaveAllowances(ImportFile importFile, List<String[]> data);

    ArrayList<RejectedImportRecord[]> importPosition(ImportFile importFile, List<String[]> data, Integer companyId);

    ArrayList<RejectedImportRecord[]> importDepartment(ImportFile importFile, List<String[]> data, Integer companyId);
}
