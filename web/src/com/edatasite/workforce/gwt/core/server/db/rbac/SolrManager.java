package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrException;

import java.io.IOException;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Oct 30, 2009
 * Time: 9:06:57 PM
 */
public interface SolrManager {

    void addTaskRbacEntryToSolr(EdsTaskRbac tRbac, EdsCompany company) throws SolrServerException, SolrException, IOException;

    void removeTask(EdsTask task, EdsCompany company) throws SolrServerException, SolrException, IOException;

    void removeCompanyTasks(Integer companyID) throws SolrServerException, SolrException, IOException;

    void removeCompanyTasksbyIds(Integer... taskIDs) throws SolrServerException, SolrException, IOException;

    void removeCompanyPurchaseOrdersbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanySaleQuotesbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanyPurchaseInvoicebyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanyShippingDatasbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanyCertificatesbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanyDepartmentsbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCompanyPositionsbyIds(Integer... ids) throws SolrServerException, SolrException, IOException;

//    void removeTasksAllRbacRecord(EdsTask task, EdsCompany company, EdsTrustee trustee, int trusteeType) throws SolrServerException, SolrException, IOException;

    void removeEmployeeAllRbacRecord(Integer companyID, int assigneeID) throws SolrServerException, SolrException, IOException;

    void removeProjectRelatedAllTaskRbacRecords(EdsProject project, EdsCompany company) throws SolrServerException, SolrException, IOException;

    void addTaskToIndex(EdsTask task, Integer companyId) throws SolrServerException, IOException;

    void addTaskToIndex(EdsTask task) throws SolrServerException, IOException;

    void addTaskToIndex(List<EdsTask> tasks, Integer companyId) throws SolrServerException, SolrException, IOException;

    void addFolderToIndex(EdsFolder folder, EdsCompany company) throws SolrServerException, SolrException, IOException;

    void addFolderToIndex(List<EdsFolder> folder, EdsCompany company) throws SolrServerException, SolrException, IOException;

    void removeFolder(Integer folderId) throws SolrServerException, SolrException, IOException;

    void removeFolders(List<EdsFolder> folders) throws SolrServerException, SolrException, IOException;

    void addFileToIndex(EdsFileHeader file) throws SolrServerException, SolrException, IOException;

    void addFileToIndex(List<EdsFileHeader> files) throws SolrServerException, SolrException, IOException;

    void addFileToIndex(List<EdsFileHeader> files, EdsUser user) throws SolrServerException, SolrException, IOException;

    void removeFile(Integer fileId) throws SolrServerException, SolrException, IOException;

    void removeFiles(List<EdsFileHeader> file) throws SolrServerException, SolrException, IOException;

    void removeCompanyFilesIds(Integer... fileIds) throws SolrServerException, SolrException, IOException;

    void removeCompanyFiles(Integer companyId) throws SolrServerException, SolrException, IOException;

    void removeGroupEntries(Integer groupId) throws SolrServerException, SolrException, IOException;

    void removeUserEntries(Integer userId) throws SolrServerException, SolrException, IOException;

    void removeEntity(String removeQuery, String coreName) throws SolrServerException, SolrException, IOException;

    void addLeadToIndex(EdsCrmContact... lead) throws SolrServerException, SolrException, IOException;

    void addCrmAccountToIndex(EdsCrmAccount... accounts) throws SolrServerException, SolrException, IOException;

    void addCrmAccountWithContactToIndex(EdsCrmAccount... accounts) throws SolrServerException, SolrException, IOException;

    void addNewsToIndex(EdsNews... lead) throws SolrServerException, SolrException, IOException;

    void removeCompanyLeadByIds(Integer... leadId) throws SolrServerException, SolrException, IOException;

    void removeCompanyNewsByIds(Integer... leadId) throws SolrServerException, SolrException, IOException;

    void addContactToIndex(EdsCrmContact... contact) throws SolrServerException, SolrException, IOException;

//    void addContactToIndexWithOwnSession(org.hibernate.Session session, EdsCrmContact... contact) throws SolrServerException, SolrException, IOException;

    void removeCompanyCrmContactBuIds(Integer... contactId) throws SolrServerException, SolrException, IOException;

    void deleteEvents(Integer... eventIDs) throws SolrServerException, SolrException, IOException;

    void removeOpportunitiesByIds(Integer... contactId) throws SolrServerException, SolrException, IOException;

    void removeExpenseReportByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCrmAccountByIds(Integer... accountId) throws SolrServerException, SolrException, IOException;

    void removeCompanyCrmContact(Integer companyId) throws IOException, SolrServerException;

    void removeCompanyCrmAccount(Integer companyId) throws IOException, SolrServerException;

    void removeCompanyNews(Integer companyId) throws IOException, SolrServerException;

    void removeAllLead(Integer companyId) throws IOException, SolrServerException;

    void removeAllCandidate(Integer companyId) throws IOException, SolrServerException;

    void indexAddSaleInvoice(List<EdsSaleInvoice> invoiceList, Integer companyID) throws IOException, SolrServerException;

    void indexAddSaleInvoice(EdsSaleInvoice invoice, Integer companyID) throws IOException, SolrServerException;

    void indexAddSaleInvoice(EdsSaleInvoice invoice) throws IOException, SolrServerException;

    void indexAddSaleQuote(List<EdsSaleQuote> quoteList, Integer companyID, List<EdsPickList> pickList) throws IOException, SolrServerException;

    void indexAddShippingData(List<EdsShippingData> quoteList, Integer companyID) throws IOException, SolrServerException;

    void indexAddCertificate(List<EdsCertificateOfEmployment> certificateList, Integer companyID) throws IOException, SolrServerException;

    void indexAddPosition(List<EdsPosition> positionList, Integer companyID) throws IOException, SolrServerException;

    void indexAddDepartment(List<EdsDepartment> departmentList, Integer companyID) throws IOException, SolrServerException;

    void indexAddPurchaseOrder(EdsPurchaseOrder purchaseOrder, Integer companyID) throws IOException, SolrServerException;

    void indexAddPurchaseOrder(EdsPurchaseOrder purchaseOrder) throws IOException, SolrServerException;

    void indexAddPurchaseOrder(List<EdsPurchaseOrder> purchaseList, Integer companyID) throws IOException, SolrServerException;

    void indexAddPurchaseInvoice(List<EdsPurchaseInvoice> purchaseInvoiceList, Integer companyID) throws IOException, SolrServerException;

    void indexAddPurchaseInvoice(EdsPurchaseInvoice invoice, Integer companyID) throws IOException, SolrServerException;

    void indexAddPurchaseInvoice(EdsPurchaseInvoice purchaseInvoice) throws IOException, SolrServerException;

    void addOpportunityToIndex(EdsOpportunity... opportunities) throws IOException, SolrServerException;

    void addEmployeeStepToIndex(EdsStepEmployee step) throws IOException, SolrServerException;

    void addEventToIndex(EdsEvent... event) throws SolrServerException, SolrException, IOException;

    void addProductsServicesToIndex(EdsItem... items) throws SolrServerException, SolrException, IOException;

    void addCourseBookingToIndex(EdsCourseBooking... courseBookings) throws SolrServerException, SolrException, IOException;

    void addCourseScheduleToIndex(EdsCourseSchedule... courseSchedule) throws SolrServerException, SolrException, IOException;

    void addEmployeeToIndex(EdsEmployee... employee) throws SolrServerException, SolrException, IOException;

    void addEmployeeToIndex(Boolean isIntegerEmployeeCodeEnabled, EdsEmployee... employee) throws SolrServerException, SolrException, IOException;

    void addleaveRequestToIndex(EdsSickRequest... requests) throws SolrServerException, SolrException, IOException;

    void addSinglePayrunToIndex(EdsPayslipTableItem... item) throws SolrServerException, SolrException, IOException;

    void addGroupPayrunToIndex(EdsPayslipTable... item) throws SolrServerException, SolrException, IOException;

    void addCashAdvanceToIndex(EdsCashAdvance... item) throws SolrServerException, SolrException, IOException;

    void addAdditionalPaymentToIndex(EdsAdditionalPayment... items) throws SolrServerException, SolrException, IOException;

    void removeAdditionalPaymentByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void addExpenseReportToIndex(EdsExpenseReport... expenseReport) throws SolrServerException, SolrException, IOException;

    void indexVacancy(EdsVacancy edsVacancy) throws IOException, SolrServerException;

    void indexVacancyList(List<EdsVacancy> edsVacancyList, Integer companyID) throws IOException, SolrServerException;

    void indexEmployeeStepList(List<EdsStepEmployee> edsStepEmployees, Integer companyID) throws IOException, SolrServerException;

    void removeProductsServicesByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCourseBookingByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCourseSchedulesByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeEmployeesByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

//    void removeShippingDataByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeLeaveRequestByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeSinglePayrunByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeGroupPayrunByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeCashAdvanceByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeSaleInvoice(Integer saleInvoiceId, Integer companyId) throws IOException, SolrServerException;

//    void removeCompanySaleQuoteByIds(Integer... ids) throws IOException, SolrServerException;

    void removeCompanyShippingDataByIds(Integer... ids) throws IOException, SolrServerException;

    void removeSaleQuote(Integer saleQuoteId, Integer companyId);

    void removeOpportunity(Integer opportunityId, Integer companyId) throws IOException, SolrServerException;

    void removeCompanySaleInvoice(Integer companyId) throws IOException, SolrServerException;

    void removeCompanySaleQuote(Integer companyId) throws IOException, SolrServerException;

//    void removeCompanyPurchaseOrderByIds(Integer... ids) throws IOException, SolrServerException;

    void removeCompanyPurchaseOrder(Integer companyId);

    void removePurchaseOrder(Integer purchaseOrderID, Integer companyID);

    void removeCompanyOpportunity(Integer companyId) throws IOException, SolrServerException;

    void removeCompanyEvents(Integer companyId) throws IOException, SolrServerException;

    void removeCompanyProductsServices(Integer companyId) throws IOException, SolrServerException;

    void removeCompanyCourseSchedule(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyEmployee(Integer companyID) throws IOException, SolrServerException;

    void removeLeaveRequests(Integer companyID) throws IOException, SolrServerException;

    void removeCustomFormItems(Integer companyID) throws IOException, SolrServerException;

    void removeCompanySinglePayrun(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyGroupPayrun(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyCashAdvance(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyAdditionalPayment(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyPurchaseInvoice(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyExpenseReportClaims(Integer companyId) throws IOException, SolrServerException;

    void removeShippingData(Integer shippingDataId, Integer companyId) throws IOException, SolrServerException;

    void removeCertificate(Integer certificateId, Integer companyId) throws IOException, SolrServerException;

    void removePosition(Integer positionId, Integer companyId) throws IOException, SolrServerException;

    void removeDepartment(Integer positionId, Integer companyId) throws IOException, SolrServerException;

    void removeCompanyCourseBooking(Integer companyId) throws IOException, SolrServerException;

    void rollbackEvent(SolrEvent event) throws SolrServerException, SolrException, IOException;

    void removeCompanyFolders(Integer companyId);

    void indexAddProject(EdsProject project, Integer company) throws SolrServerException, SolrException, IOException;

    void indexAddProject(EdsProject project) throws SolrServerException, SolrException, IOException;

    void indexAddProject(List<EdsProject> projectList, Integer companyID) throws SolrServerException, SolrException, IOException;

    void removeCompanyNews(EdsCompany company) throws SolrServerException, SolrException, IOException;

    void indexAddCase(List<EdsCase> caseList, Integer companyID) throws IOException, SolrServerException;

    void indexAddCase(EdsCase edsCase, Integer companyID) throws IOException, SolrServerException;

    void indexAddCase(EdsCase edsCase) throws IOException, SolrServerException;

    void removeCompanyCaseByIds(Integer... caseID) throws IOException, SolrServerException;

    void removeCompanyEventByIds(Integer... eventID) throws IOException, SolrServerException;

    void removeCompanyCase(Integer companyID);

    void removeCase(Integer caseID, Integer companyID);

//    void removeCompanyPurchaseInvoiceByIds(Integer... ids) throws IOException, SolrServerException;

    void removePurchaseInvoice(Integer purchaseInvoiceId, Integer companyId) throws IOException, SolrServerException;

    void addChartOfAccountToIndex(EdsAccount... accounts) throws SolrServerException, SolrException, IOException;

    void removeCompanyChartOfAccount(Integer companyID) throws IOException, SolrServerException;

    void removeChartOfAccountByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void removeChartOfAccount(Integer accountID) throws IOException, SolrServerException;
    void removeChartOfAccount(Integer accountID, Integer companyId) throws IOException, SolrServerException;

    void removeCompanyProjects(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyProject(Integer projectID, Integer companyID) throws IOException, SolrServerException;

    void removeCompanyProjectsById(Integer... projectIDs) throws IOException, SolrServerException;

    void removeVacances(Integer... vacancyIds) throws IOException, SolrServerException;

    void removeEmployeeSteps(Integer... stepIds) throws IOException, SolrServerException;

    void removeCompanyVacancy(Integer companyID) throws IOException, SolrServerException;

    void removeCompanyEmployeeStep(Integer companyID) throws IOException, SolrServerException;

    void addCustomFormItemToIndex(EdsCustomFormItems... edsItem) throws IOException, SolrServerException;

    void removeCustomFormByIds(Integer... ids) throws SolrServerException, SolrException, IOException;

    void indexAddRFQ(EdsRFQ rfq) throws IOException, SolrServerException;

    void indexAddRFQ(List<EdsRFQ> rfqList, Integer companyID) throws IOException, SolrServerException;

    void removeRFQSolr(Integer shippingDataId, Integer companyId) throws IOException, SolrServerException;

    void indexDynamicEntity(String type, Integer id) throws Exception;

    void removeDynamicEntity(String type, Integer id) throws IOException, SolrServerException;
}
