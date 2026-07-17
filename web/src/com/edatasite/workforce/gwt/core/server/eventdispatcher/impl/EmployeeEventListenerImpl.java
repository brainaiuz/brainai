package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.solr.component.CashAdvanceSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.db.AttendanceHoursManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * User: admin
 * Date: Jan 6, 2010
 * Time: 4:44:17 PM
 */
@Transactional
public class EmployeeEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.employeeIndexEventListener);
    public static final String SOLR_UPDATE = "SOLR_UPDATE";
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private AttendanceHoursManager attendanceHoursManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private CashAdvanceSolrComponent cashAdvanceSolrComponent;

    public void onAddEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                employeeSolrComponent.index(employee);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }

        if (!event.isMyUpdatesItemAdd()) {
            try {
                List<EdsMyUpdate> updates = myUpdateManager.getUpdates(employee.getObjectID(), MyUpdateTypeManager.USER, EdsMyUpdate.ADD);
                boolean anythingExists = (updates != null && !updates.isEmpty());
                if (!anythingExists) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeAddUpdate(employee, employee, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
            }
        }
        if (event.isSolrIndexed() && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                employeeSolrComponent.index(employee);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeEditUpdate(employee, employee, user, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        if (event.isSolrIndexed() && event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        deleteTerminateEmployee(event, true);
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (SOLR_UPDATE.equals(event.getEventType())) {
            cashAdvanceSolrReindexByEmployee(event);
        } else {
            String employeeIDs = event.getCustomStringField();
            if (employeeIDs != null && !"".equals(employeeIDs)) {
                List<EdsEmployee> employees = employeeManager.getEmployeesByIds(employeeIDs);
                if (employees != null && !employees.isEmpty()) {
                    try {
                        employeeSolrComponent.indexes(employees);
                    } catch (Exception e) {
                        e.printStackTrace();
                        event.setStatus(EventStatus.FAILED.name());
                    }
                }
            }
        }
    }

    private void deleteTerminateEmployee(EdsBusinessEvent event, boolean isDelete) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeEmployeesByIds(employee.getObjectID());
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
            }
        }
        if (!event.isMyUpdatesItemDelete()) {
            try {
                if (isDelete) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeDeleteUpdate(employee, employee, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                } else {
                    EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeTerminateUpdate(employee, employee, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void cashAdvanceSolrReindexByEmployee(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        if (employee != null) {
            List<EdsCashAdvance> cashAdvances = cashAdvanceManager.getCashAdvanceListByEmployeeId(employee.getObjectID());
            if (cashAdvances != null && !cashAdvances.isEmpty()) {
                for (EdsCashAdvance cashAdvance : cashAdvances) {
                    try {
                        cashAdvanceSolrComponent.index(cashAdvance);

                        WebSocketServerObject message = new WebSocketServerObject();
                        message.setEventType(WfmUiEventType.ON_CASH_SAVED);
                        try {
                            Integer userId = ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
                            message.setUserId(userId);
                            rabbitMQService.sendWebPushNotification(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (SolrServerException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
