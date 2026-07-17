package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.LeaveRequestSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: Jan 6, 2010
 * Time: 4:44:17 PM
 */
@Transactional
public class EmployeeSupervisorChangeEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.employeeSupervisorChangeEventListener);
    public static final String SOLR_UPDATE = "SOLR_UPDATE";

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private LeaveRequestSolrComponent leaveRequestSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (SOLR_UPDATE.equals(event.getEventType())) {
            EdsEmployee employee = employeeManager.get(event.getEntityID());
            if (employee != null) {
                List<EdsSickRequest> leaveRequestList = sickRequestManager.getLeaveRequestListByEmployee(employee.getObjectID());
                if (leaveRequestList != null && leaveRequestList.size() > 0) {
                    for (EdsSickRequest leaveRequest : leaveRequestList) {
                        try {
                            leaveRequestSolrComponent.index(leaveRequest);
                        } catch (InterruptedException | SolrServerException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }
    }

}