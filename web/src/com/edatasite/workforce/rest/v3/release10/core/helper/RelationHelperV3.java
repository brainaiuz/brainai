package com.edatasite.workforce.rest.v3.release10.core.helper;

import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAlert;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowSMSAlert;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowTelegramAlert;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowAlertManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowRuleManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowSMSAlertManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowTelegramAlertManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.rest.v3.release10.core.to.RelationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.*;

@Service
public class RelationHelperV3 {
    private final ProjectManager projectManager;
    private final CrmContactManager contactManager;
    private final TaskManager taskManager;
    private final IssueManager issueManager;
    private final EventManager eventManager;
    private final ContractManager contractManager;
    private final CrmAccountManager crmAccountManager;
    private final OpportunityManager opportunityManager;
    private final CaseManager caseManager;
    private final QuoteManager quoteManager;
    private final RFPManager rfpManager;
    private final StockTransferManager stockTransferManager;
    private final RFQManager rfqManager;
    private final InvoiceManager invoiceManager;
    private final ItemManager itemManager;
    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;
    private final ShippingDataManager shippingDataManager;
    private final StudentManager studentManager;
    private final SickRequestManager sickRequestManager;
    private final AdditionalPaymentManager additionalPaymentManager;
    private final CampaignManager campaignManager;
    private final WorkflowRuleManager workflowRuleManager;
    private final WorkflowAlertManager workflowAlertManager;
    private final WorkflowSMSAlertManager workflowSMSAlertManager;
    private final WorkflowTelegramAlertManager workflowTelegramAlertManager;
    private final VacancyManager vacancyManager;
    private final PlacementManager placementManager;
    private final ManualJournalManager manualJournalManager;
    private final BatchPaymentManager batchPaymentManager;
    private final InvoicePaymentManager invoicePaymentManager;
    private final CustomFormManager customFormManager;
    private final GoalManager goalManager;

    @Autowired
    public RelationHelperV3(ProjectManager projectManager, CrmContactManager contactManager, TaskManager taskManager, IssueManager issueManager, EventManager eventManager, ContractManager contractManager, CrmAccountManager crmAccountManager, OpportunityManager opportunityManager, CaseManager caseManager, QuoteManager quoteManager, RFPManager rfpManager, StockTransferManager stockTransferManager, RFQManager rfqManager, InvoiceManager invoiceManager, ItemManager itemManager, EmployeeManager employeeManager, DepartmentManager departmentManager, ShippingDataManager shippingDataManager, StudentManager studentManager, SickRequestManager sickRequestManager, AdditionalPaymentManager additionalPaymentManager, CampaignManager campaignManager, WorkflowRuleManager workflowRuleManager, WorkflowAlertManager workflowAlertManager, WorkflowSMSAlertManager workflowSMSAlertManager, WorkflowTelegramAlertManager workflowTelegramAlertManager, VacancyManager vacancyManager, PlacementManager placementManager, ManualJournalManager manualJournalManager, BatchPaymentManager batchPaymentManager, InvoicePaymentManager invoicePaymentManager, CustomFormManager customFormManager, GoalManager goalManager) {
        this.projectManager = projectManager;
        this.contactManager = contactManager;
        this.taskManager = taskManager;
        this.issueManager = issueManager;
        this.eventManager = eventManager;
        this.contractManager = contractManager;
        this.crmAccountManager = crmAccountManager;
        this.opportunityManager = opportunityManager;
        this.caseManager = caseManager;
        this.quoteManager = quoteManager;
        this.rfpManager = rfpManager;
        this.stockTransferManager = stockTransferManager;
        this.rfqManager = rfqManager;
        this.invoiceManager = invoiceManager;
        this.itemManager = itemManager;
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;
        this.shippingDataManager = shippingDataManager;
        this.studentManager = studentManager;
        this.sickRequestManager = sickRequestManager;
        this.additionalPaymentManager = additionalPaymentManager;
        this.campaignManager = campaignManager;
        this.workflowRuleManager = workflowRuleManager;
        this.workflowAlertManager = workflowAlertManager;
        this.workflowSMSAlertManager = workflowSMSAlertManager;
        this.workflowTelegramAlertManager = workflowTelegramAlertManager;
        this.vacancyManager = vacancyManager;
        this.placementManager = placementManager;
        this.manualJournalManager = manualJournalManager;
        this.batchPaymentManager = batchPaymentManager;
        this.invoicePaymentManager = invoicePaymentManager;
        this.customFormManager = customFormManager;
        this.goalManager = goalManager;
    }

    public RelationItem convertRelation(RelationDto relationDto, Integer fromId, String fromName, String fromType) {
        RelationItem relationItem = new RelationItem();
        relationItem.setFromID(fromId);
        relationItem.setFromName(fromName);
        relationItem.setFromType(fromType);

        relationItem.setToType(relationDto.getType());
        switch (relationDto.getType()) {
            case TYPE_PROJECT:
                EdsProject edsProject = null;
                if (relationDto.getItem().getId() != null) {
                    edsProject = projectManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    List<EdsProject> edsProjects = projectManager.getProjectByName(relationDto.getItem().getName());
                    if (edsProjects != null && !edsProjects.isEmpty()) {
                        edsProject = edsProjects.get(0);
                    }
                } else if (relationDto.getItem().getCode() != null) {
                    edsProject = projectManager.getProjectByNumber(relationDto.getItem().getCode());
                }
                if (edsProject != null) {
                    relationItem.setToID(edsProject.getObjectID());
                    relationItem.setToName(edsProject.getName());
                }
                break;
            case TYPE_CONTACT:
            case TYPE_CANDIDATE:
                EdsCrmContact edsCrmContact = null;
                if (relationDto.getItem().getId() != null) {
                    edsCrmContact = contactManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getCode() != null) {
                    List<EdsCrmContact> edsCrmContacts = contactManager.getByPrimaryEmail(relationDto.getItem().getCode());
                    if (edsCrmContacts != null && !edsCrmContacts.isEmpty()) {
                        edsCrmContact = edsCrmContacts.get(0);
                    }
                }
                if (edsCrmContact != null) {
                    relationItem.setToID(edsCrmContact.getObjectID());
                    relationItem.setToName(edsCrmContact.getName());
                }
                break;
            case TYPE_TASK:
                EdsTask edsTask = null;
                if (relationDto.getItem().getId() != null) {
                    edsTask = taskManager.get(relationDto.getItem().getId());
                }
                if (edsTask != null) {
                    relationItem.setToID(edsTask.getObjectID());
                    relationItem.setToName(edsTask.getName());
                }
                break;
            case TYPE_ISSUE:
                EdsIssue edsIssue = null;
                if (relationDto.getItem().getId() != null) {
                    edsIssue = issueManager.get(relationDto.getItem().getId());
                }
                if (edsIssue != null) {
                    relationItem.setToID(edsIssue.getObjectID());
                    relationItem.setToName(edsIssue.getName());
                }
                break;
            case TYPE_EVENT:
                EdsEvent edsEvent = null;
                if (relationDto.getItem().getId() != null) {
                    edsEvent = eventManager.get(relationDto.getItem().getId());
                }
                if (edsEvent != null) {
                    relationItem.setToID(edsEvent.getObjectID());
                    relationItem.setToName(edsEvent.getName());
                }
                break;
            case TYPE_CONTRACT:
                EdsContract edsContract = null;
                if (relationDto.getItem().getId() != null) {
                    edsContract = contractManager.get(relationDto.getItem().getId());
                }
                if (edsContract != null) {
                    relationItem.setToID(edsContract.getObjectID());
                    relationItem.setToName(edsContract.getName());
                }
                break;
            case TYPE_LEAD:
                List<EdsCrmContact> edsCrmContacts;
                edsCrmContacts = contactManager.getLeadByEntityID(relationDto.getItem().getId());
                if (edsCrmContacts != null && !edsCrmContacts.isEmpty()) {
                    edsCrmContact = edsCrmContacts.get(0);
                    relationItem.setToID(edsCrmContact.getObjectID());
                    relationItem.setToName(edsCrmContact.getName());
                }
                break;
            case TYPE_CRM_ACCOUNT:
            case TYPE_SUPPLIER:
            case TYPE_CLIENT:
                EdsCrmAccount edsCrmAccount = null;
                if (relationDto.getItem().getId() != null) {
                    edsCrmAccount = crmAccountManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    edsCrmAccount = crmAccountManager.getCrmAccountByName(relationDto.getItem().getName());
                } else if (relationDto.getItem().getCode() != null) {
                    edsCrmAccount = crmAccountManager.getCrmAccountByNumber(relationDto.getItem().getCode());
                }
                if (edsCrmAccount != null) {
                    relationItem.setToID(edsCrmAccount.getObjectID());
                    relationItem.setToName(edsCrmAccount.getName());
                }
                break;
            case TYPE_OPPORTUNITY:
                EdsOpportunity edsOpportunity = null;
                if (relationDto.getItem().getId() != null) {
                    edsOpportunity = opportunityManager.get(relationDto.getItem().getId());
                }

                if (edsOpportunity != null) {
                    relationItem.setToID(edsOpportunity.getObjectID());
                    relationItem.setToName(edsOpportunity.getName());
                }
                break;
            case TYPE_CASE:
                EdsCase edsCase = null;
                if (relationDto.getItem().getId() != null) {
                    edsCase = caseManager.get(relationDto.getItem().getId());
                }

                if (edsCase != null) {
                    relationItem.setToID(edsCase.getObjectID());
                    relationItem.setToName(edsCase.getName());
                }
                break;
            case TYPE_SALEQUOTE:
            case TYPE_SALEORDER:
                EdsSaleQuote edsSaleQuote = null;
                if (relationDto.getItem().getId() != null) {
                    edsSaleQuote = quoteManager.getSaleQuote(relationDto.getItem().getId());
                } else if (relationDto.getItem().getCode() != null) {
                    List<EdsSaleQuote> saleQuotes = quoteManager.getQuoteByNumber(relationDto.getItem().getCode());
                    if (saleQuotes != null && !saleQuotes.isEmpty()) {
                        edsSaleQuote = saleQuotes.get(0);
                    }
                }

                if (edsSaleQuote != null) {
                    relationItem.setToID(edsSaleQuote.getObjectID());
                    relationItem.setToName(edsSaleQuote.getNumber());
                }
                break;
            case REQUEST_FOR_PURCHASE:
                EdsRFP edsRFP = null;
                if (relationDto.getItem().getId() != null) {
                    edsRFP = rfpManager.get(relationDto.getItem().getId());
                }

                if (edsRFP != null) {
                    relationItem.setToID(edsRFP.getObjectID());
                    relationItem.setToName(edsRFP.getNumber());
                }
                break;
            case TYPE_STOCK_TRANSFER:
                EdsStockTransfer edsStockTransfer = null;
                if (relationDto.getItem().getId() != null) {
                    edsStockTransfer = stockTransferManager.get(relationDto.getItem().getId());
                }

                if (edsStockTransfer != null) {
                    relationItem.setToID(edsStockTransfer.getObjectID());
                    relationItem.setToName(edsStockTransfer.getNumber());
                }
                break;
            case TYPE_REQUEST_FOR_QUOTE:
                EdsRFQ edsRFQ = null;
                if (relationDto.getItem().getId() != null) {
                    edsRFQ = rfqManager.get(relationDto.getItem().getId());
                }

                if (edsRFQ != null) {
                    relationItem.setToID(edsRFQ.getObjectID());
                    relationItem.setToName(edsRFQ.getNumber());
                }
                break;
            case TYPE_SALEINVOICE:
                EdsSaleInvoice edsSaleInvoice = null;
                if (relationDto.getItem().getId() != null) {
                    edsSaleInvoice = invoiceManager.getSaleInvoice(relationDto.getItem().getId());
                }

                if (edsSaleInvoice != null) {
                    relationItem.setToID(edsSaleInvoice.getObjectID());
                    relationItem.setToName(edsSaleInvoice.getNumber());
                }
                break;
            case TYPE_PRODUCT:
                EdsItem edsItem = null;
                if (relationDto.getItem().getId() != null) {
                    edsItem = itemManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    edsItem = itemManager.getItemByName(relationDto.getItem().getName());
                } else if (relationDto.getItem().getCode() != null) {
                    edsItem = itemManager.getItemByNumber(relationDto.getItem().getCode());
                }

                if (edsItem != null) {
                    relationItem.setToID(edsItem.getObjectID());
                    relationItem.setToName(edsItem.getName());
                }
                break;
            case TYPE_EMPLOYEE:
                EdsEmployee edsEmployee = null;
                if (relationDto.getItem().getId() != null) {
                    edsEmployee = employeeManager.get(relationDto.getItem().getId());
                }

                if (edsEmployee != null) {
                    relationItem.setToID(edsEmployee.getObjectID());
                    relationItem.setToName(edsEmployee.getFullName());
                }
                break;
            case TYPE_DEPARTMENT:
                EdsDepartment edsDepartment = null;
                if (relationDto.getItem().getId() != null) {
                    edsDepartment = departmentManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    List<EdsDepartment> departments = departmentManager.getDepartmentByName(relationDto.getItem().getName());
                    if (departments != null && !departments.isEmpty()) {
                        edsDepartment = departments.get(0);
                    }
                }

                if (edsDepartment != null) {
                    relationItem.setToID(edsDepartment.getObjectID());
                    relationItem.setToName(edsDepartment.getName());
                }
                break;
            case TYPE_PURCHASE_ORDER:
                EdsPurchaseOrder edsPurchaseOrder = null;
                if (relationDto.getItem().getId() != null) {
                    edsPurchaseOrder = quoteManager.getPurchaseOrderByID(relationDto.getItem().getId());
                } else if (relationDto.getItem().getCode() != null) {
                    List<EdsPurchaseOrder> purchaseOrders = quoteManager.getPurchaseOrderByNumber(relationDto.getItem().getCode(), null);
                    if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
                        edsPurchaseOrder = purchaseOrders.get(0);
                    }
                }

                if (edsPurchaseOrder != null) {
                    relationItem.setToID(edsPurchaseOrder.getObjectID());
                    relationItem.setToName(edsPurchaseOrder.getNumber());
                }
                break;
            case TYPE_GDN:
            case TYPE_SHIPPING_DATA:
                EdsShippingData edsShippingData = null;
                if (relationDto.getItem().getId() != null) {
                    edsShippingData = shippingDataManager.get(relationDto.getItem().getId());
                }

                if (edsShippingData != null) {
                    relationItem.setToID(edsShippingData.getObjectID());
                    relationItem.setToName(edsShippingData.getNumber());
                }
                break;
            case TYPE_PURCHASE_INVOICE:
                EdsPurchaseInvoice edsPurchaseInvoice = null;
                if (relationDto.getItem().getId() != null) {
                    edsPurchaseInvoice = invoiceManager.getPurchaseInvoice(relationDto.getItem().getId());
                } else if (relationDto.getItem().getCode() != null) {
                    List<EdsPurchaseInvoice> purchaseInvoices = invoiceManager.getPurchaseInvoiceByNumber(relationDto.getItem().getCode(), null, null);
                    if (purchaseInvoices != null && !purchaseInvoices.isEmpty()) {
                        edsPurchaseInvoice = purchaseInvoices.get(0);
                    }
                }

                if (edsPurchaseInvoice != null) {
                    relationItem.setToID(edsPurchaseInvoice.getObjectID());
                    relationItem.setToName(edsPurchaseInvoice.getNumber());
                }
                break;
            case TYPE_STUDENT:
                EdsStudent edsStudent = null;
                if (relationDto.getItem().getId() != null) {
                    edsStudent = studentManager.get(relationDto.getItem().getId());
                }

                if (edsStudent != null) {
                    relationItem.setToID(edsStudent.getObjectID());
                    relationItem.setToName(edsStudent.getFullName());
                }
                break;
            case TYPE_SICK_REQUEST:
                EdsSickRequest edsSickRequest = null;
                if (relationDto.getItem().getId() != null) {
                    edsSickRequest = sickRequestManager.get(relationDto.getItem().getId());
                }

                if (edsSickRequest != null) {
                    relationItem.setToID(edsSickRequest.getObjectID());
                    relationItem.setToName(edsSickRequest.getTitle());
                }
                break;
            case TYPE_ADDITIONAL_PAYMENT:
                EdsAdditionalPayment edsAdditionalPayment = null;
                if (relationDto.getItem().getId() != null) {
                    edsAdditionalPayment = additionalPaymentManager.get(relationDto.getItem().getId());
                }

                if (edsAdditionalPayment != null) {
                    relationItem.setToID(edsAdditionalPayment.getObjectID());
                    relationItem.setToName(edsAdditionalPayment.getReference());
                }
                break;
            case TYPE_CAMPAIGN:
                EdsCampaign edsCampaign = null;
                if (relationDto.getItem().getId() != null) {
                    edsCampaign = campaignManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    edsCampaign = campaignManager.getCampaignByName(relationDto.getItem().getName());
                }

                if (edsCampaign != null) {
                    relationItem.setToID(edsCampaign.getObjectID());
                    relationItem.setToName(edsCampaign.getName());
                }
                break;
            case TYPE_WORKFLOW:
                EdsWorkflowRule edsWorkflowRule = null;
                if (relationDto.getItem().getId() != null) {
                    edsWorkflowRule = workflowRuleManager.get(relationDto.getItem().getId());
                }

                if (edsWorkflowRule != null) {
                    relationItem.setToID(edsWorkflowRule.getObjectID());
                    relationItem.setToName(edsWorkflowRule.getName());
                }
                break;
            case TYPE_WORKFLOW_ALERT:
                EdsWorkflowAlert edsWorkflowAlert = null;
                if (relationDto.getItem().getId() != null) {
                    edsWorkflowAlert = workflowAlertManager.get(relationDto.getItem().getId());
                }

                if (edsWorkflowAlert != null) {
                    relationItem.setToID(edsWorkflowAlert.getObjectID());
                    relationItem.setToName(edsWorkflowAlert.getSubject());
                }
                break;
            case TYPE_WORKFLOW_SMS_ALERT:
                EdsWorkflowSMSAlert edsWorkflowSMSAlert = null;
                if (relationDto.getItem().getId() != null) {
                    edsWorkflowSMSAlert = workflowSMSAlertManager.get(relationDto.getItem().getId());
                }

                if (edsWorkflowSMSAlert != null) {
                    relationItem.setToID(edsWorkflowSMSAlert.getObjectID());
                    relationItem.setToName(edsWorkflowSMSAlert.getContent());
                }
                break;
            case TYPE_WORKFLOW_TELEGRAM_ALERT:
                EdsWorkflowTelegramAlert edsWorkflowTelegramAlert = null;
                if (relationDto.getItem().getId() != null) {
                    edsWorkflowTelegramAlert = workflowTelegramAlertManager.get(relationDto.getItem().getId());
                }

                if (edsWorkflowTelegramAlert != null) {
                    relationItem.setToID(edsWorkflowTelegramAlert.getObjectID());
                    relationItem.setToName(edsWorkflowTelegramAlert.getMessage());
                }
                break;
            case TYPE_VACANCY:
                EdsVacancy edsVacancy = null;
                if (relationDto.getItem().getId() != null) {
                    edsVacancy = vacancyManager.get(relationDto.getItem().getId());
                }

                if (edsVacancy != null) {
                    relationItem.setToID(edsVacancy.getObjectID());
                    relationItem.setToName(edsVacancy.getName());
                }
                break;
            case TYPE_PLACEMENT:
                EdsPlacement edsPlacement = null;
                if (relationDto.getItem().getId() != null) {
                    edsPlacement = placementManager.get(relationDto.getItem().getId());
                }

                if (edsPlacement != null) {
                    relationItem.setToID(edsPlacement.getObjectID());
                    relationItem.setToName(edsPlacement.getName());
                }
                break;
            case TYPE_MANUAL_JOURNAL:
                EdsManualJournal edsManualJournal = null;
                if (relationDto.getItem().getId() != null) {
                    edsManualJournal = manualJournalManager.get(relationDto.getItem().getId());
                }

                if (edsManualJournal != null) {
                    relationItem.setToID(edsManualJournal.getObjectID());
                    relationItem.setToName(edsManualJournal.getNumber());
                }
                break;
            case TYPE_BATCH_PAYMENT:
                EdsBatchPayment edsBatchPayment = null;
                if (relationDto.getItem().getId() != null) {
                    edsBatchPayment = batchPaymentManager.getPayment(relationDto.getItem().getId());
                }

                if (edsBatchPayment != null) {
                    relationItem.setToID(edsBatchPayment.getObjectID());
                    relationItem.setToName(edsBatchPayment.getNumber());
                }
                break;
            case TYPE_PRE_PAYMENT:
                EdsInvoicePayment edsInvoicePayment = null;
                if (relationDto.getItem().getId() != null) {
                    edsInvoicePayment = invoicePaymentManager.get(relationDto.getItem().getId());
                }

                if (edsInvoicePayment != null) {
                    relationItem.setToID(edsInvoicePayment.getObjectID());
                    relationItem.setToName(edsInvoicePayment.getNumber());
                }
                break;
            case TYPE_CUSTOM_FORM_ITEM:
                EdsCustomForm edsCustomForm = null;
                if (relationDto.getItem().getId() != null) {
                    edsCustomForm = customFormManager.get(relationDto.getItem().getId());
                } else if (relationDto.getItem().getName() != null) {
                    edsCustomForm = customFormManager.findByName(relationDto.getItem().getName());
                }

                if (edsCustomForm != null) {
                    relationItem.setToID(edsCustomForm.getObjectID());
                    relationItem.setToName(edsCustomForm.getName());
                }
                break;
            case TYPE_PERSONAL_GOAL:
            case TYPE_DEPARTMENT_GOAL:
            case TYPE_BUSINESS_GOAL:
            case TYPE_PROJECT_GOAL:
            case TYPE_COMPANY_GOAL:
            case TYPE_GROUP_GOAL:
                EdsGoal edsGoal = null;
                if (relationDto.getItem().getId() != null) {
                    edsGoal = goalManager.get(relationDto.getItem().getId());
                }

                if (edsGoal != null) {
                    relationItem.setToID(edsGoal.getObjectID());
                    relationItem.setToName(edsGoal.getTitle());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + relationDto.getType());
        }

        return relationItem;
    }
}
