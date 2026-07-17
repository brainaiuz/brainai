package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 24.04.12
 * Time: 16:22
 */
@Transactional
public class WorkflowActionDetectedEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsObject> TYPE = new WfmType<>(EventTypes.workflowActionDetectedEventListener);
    public static final String FROM_WORKFLOW = "FROM_WORKFLOW";

    @Autowired
    private UserManager userManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;

    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        if (creator != null) {
            SecurityContext.getInstance().setStaticUserID(creator.getObjectID());
        }
        System.out.println("WorkflowActionDetectedEventListenerImpl: Start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("" + SecurityContext.getInstance().getCompanyId() + "-" + SecurityContext.getInstance().getDatabase() + "-" + SecurityContext.getInstance().getStaticUserID());
        System.out.println(userManager.getUser());
        System.out.println("WorkflowActionDetectedEventListenerImpl: End !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        allInOneServiceLocal.approvedOrRejected(event.getEntityType(), event.getEntityID(), event);
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        doForAll(event, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
    }

    private void doForAll(EdsBusinessEvent event, WorkflowExecutionCriteriaEnum... actions) {
        String module = null;
        if (event.getEntityType() != null) {
            switch (event.getEntityType()) {
                case RelationItem.TYPE_CASE -> module = WorkflowRule._WORKFLOW_MODULE_CASE;
                case RelationItem.TYPE_LEAD -> module = WorkflowRule._WORKFLOW_MODULE_LEAD;
                case RelationItem.TYPE_CANDIDATE -> module = WorkflowRule._WORKFLOW_MODULE_CANDIDATE;
                case RelationItem.TYPE_CONTACT -> module = WorkflowRule._WORKFLOW_MODULE_CONTACT;
                case RelationItem.TYPE_EVENT -> {
                    if (event.getEntityID() != null) {
                        EdsEvent eventDomain = eventManager.getEventByID(event.getEntityID());
                        if (eventDomain != null) {
                            if (Appointment.CALL_LOG == eventDomain.getActivityType()) {
                                module = WorkflowRule._WORKFLOW_MODULE_LOGACALL;
                            } else {
                                module = WorkflowRule._WORKFLOW_MODULE_ACTIVITY;
                            }
                        }
                    }
                }
                case RelationItem.TYPE_COURCE_SCHEDULE -> module = WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE;
                case RelationItem.TYPE_CS_STUDENT -> module = WorkflowRule._WORKFLOW_MODULE_CS_STUDENT;
                case RelationItem.TYPE_EMPLOYEE -> module = WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE;
                case RelationItem.TYPE_SALEINVOICE -> module = WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE;
                case RelationItem.TYPE_SALEQUOTE -> module = WorkflowRule._WORKFLOW_MODULE_SALEQUOTE;
                case RelationItem.REQUEST_FOR_PURCHASE -> module = WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE;
                case RelationItem.TYPE_PURCHASE_ORDER -> module = WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER;
                case RelationItem.TYPE_LEAVE_REQUEST -> module = WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST;
                case RelationItem.TYPE_OPPORTUNITY -> module = WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY;
                case RelationItem.TYPE_CASH_ADVANCE -> module = WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE;
                case RelationItem.TYPE_EXPENSE_CLAIM -> module = WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM;
                case RelationItem.TYPE_ADDITIONAL_PAYMENT -> module = WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT;
                case RelationItem.TYPE_CERTIFICATE -> module = WorkflowRule._WORKFLOW_MODULE_CERTIFICATE;
                case RelationItem.TYPE_PROJECT -> module = WorkflowRule._WORKFLOW_MODULE_PROJECT;
                case RelationItem.TYPE_PRODUCT -> module = WorkflowRule._WORKFLOW_MODULE_PRODUCT;
                case RelationItem.TYPE_GDN -> module = WorkflowRule._WORKFLOW_MODULE_GDN;
                case RelationItem.TYPE_SHIPPING_DATA -> module = WorkflowRule._WORKFLOW_MODULE_PICKLIST;
                case RelationItem.TYPE_CRM_ACCOUNT -> module = WorkflowRule._WORKFLOW_MODULE_ACCOUNT;
                case RelationItem.TYPE_MANUAL_JOURNAL -> module = WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL;
                case RelationItem.TYPE_TASK -> module = WorkflowRule._WORKFLOW_MODULE_TASK;
                case RelationItem.TYPE_REQUEST_FOR_QUOTE -> module = WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE;
                case RelationItem.TYPE_PURCHASE_INVOICE -> module = WorkflowRule._WORKFLOW_MODULE_PURCHASE_INVOICE;
                case RelationItem.TYPE_GROUP_GOAL -> module = WorkflowRule._WORKFLOW_MODULE_GROUP_GOAL;
                case RelationItem.TYPE_SALEORDER -> module = WorkflowRule._WORKFLOW_MODULE_SALEORDER;
                case RelationItem.TYPE_STOCK_TRANSFER -> module = WorkflowRule._WORKFLOW_MODULE_STOCK_TRANSFER;
                case RelationItem.TYPE_STOCK_ADJUSTMENT -> module = WorkflowRule._WORKFLOW_MODULE_STOCK_ADJUSTMENT;
                case RelationItem.TYPE_VACANCY -> module = WorkflowRule._WORKFLOW_MODULE_VACANCY;
                case RelationItem.TYPE_DEPARTMENT -> module = WorkflowRule._WORKFLOW_MODULE_DEPARTMENT;
                case RelationItem.TYPE_POSITION -> module = WorkflowRule._WORKFLOW_MODULE_POSITION;
                case RelationItem.TYPE_RENTAL_ORDER -> module = WorkflowRule._WORKFLOW_MODULE_RENTAL_ORDER;
                case RelationItem.TYPE_RENTAL_PRODUCT -> module = WorkflowRule._WORKFLOW_MODULE_RENTAL_PRODUCT;
                case RelationItem.TYPE_BUILD_ASSEMBLY -> module = WorkflowRule._WORKFLOW_MODULE_BUILD_ASSEMBLY;
                case RelationItem.TYPE_BATCH_PAYMENT_RECEIVABLE ->
                        module = WorkflowRule._WORKFLOW_MODULE_RECEIVE_PAYMENT;
                case RelationItem.TYPE_CREDIT_NOTE -> module = WorkflowRule._WORKFLOW_MODULE_CREDIT_NOTE;
                case RelationItem.TYPE_DEBIT_NOTE -> module = WorkflowRule._WORKFLOW_MODULE_DEBIT_NOTE;
                case RelationItem.TYPE_BACKUPS_EMPLOYEE -> module = WorkflowRule._WORKFLOW_MODULE_BACKUP_EMPLOYEE;
                case RelationItem.TYPE_BATCH_PAYMENT_PAYABLE -> module = WorkflowRule._WORKFLOW_MODULE_PAY_INVOICE;
                case RelationItem.TYPE_INCIDENT -> module = WorkflowRule._WORKFLOW_MODULE_INCIDENT;
                case RelationItem.TYPE_PLACEMENT -> module = WorkflowRule._WORKFLOW_MODULE_PLACEMENT;
                case RelationItem.TYPE_ROTATION -> module = WorkflowRule._WORKFLOW_MODULE_ROTATION;
                case RelationItem.TYPE_GROUP_PLACEMENT -> module = WorkflowRule._WORKFLOW_MODULE_GROUP_PLACEMENT;
                case RelationItem.TYPE_PRODUCT_CATEGORY -> module = WorkflowRule._WORKFLOW_MODULE_PRODUCT_CATEGORY;
                case RelationItem.TYPE_COMPANY_SETTINGS -> module = WorkflowRule._WORKFLOW_MODULE_COMPANY_SETTINGS;
                case RelationItem.TYPE_EMPLOYEE_DOCUMENTS -> module = WorkflowRule._WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS;
                case RelationItem.TYPE_CUSTOM_FORM_ITEM -> {
                    if (event.getEntityID() != null) {
                        EdsCustomFormItems customFormItem = customFormItemManager.get(event.getEntityID());
                        if (customFormItem != null) {
                            module = WorkflowRule._WORKFLOW_MODULE + "_" + customFormItem.getCustomForm().getFormID().replace("_FORM", "");
                        }
                    }
                }
                default -> module = WorkflowRule._WORKFLOW_MODULE + "_" + event.getEntityType().replace("_FORM", "");
            }
        }
        EdsUser creator = userManager.get(event.getSourceID());
        if (creator != null) {
            SecurityContext.getInstance().setStaticUserID(creator.getObjectID());
        }
        event.setStatus(EventStatus.COMPLETED.name());
        allInOneServiceLocal.runWorkflows(event.getEntityID(), event.getEntityType(), module, actions, FROM_WORKFLOW.equals(event.getCustomStringField()), event.isSendEmailNotification());
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        doForAll(event, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_REMOVE);
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        doForAll(event, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_EDIT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD);
    }
}
