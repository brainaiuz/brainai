package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jul 13, 2010
 * Time: 7:27:02 PM
 */
@Transactional
public class ProjectSolrSensitiveDataChangeListener implements BusinessEventListener {
    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectSolrSensitiveDataChangeListener);
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        List<EdsTask> tasks = taskManager.getProjectTasks(project);
        EdsCompany company = companyManager.get(event.getCompanyId());
        if (!event.isRbacIndexed()) {
            taskRbacManager.updateRbacEntriesOnProjectClientChange(project);
            event.setRbacIndexed(true);
        }
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeProjectRelatedAllTaskRbacRecords(project, company);
                taskSolrComponent.indexes(tasks);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isRbacIndexed() && event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        EdsCompany company = companyManager.get(event.getCompanyId());
//        try{
        if (!event.isRbacIndexed()) {
            taskRbacManager.removeProjectRelatedEntries(project);
            event.setRbacIndexed(true);
        }
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeProjectRelatedAllTaskRbacRecords(project, company);
                event.setSolrIndexed(true);
            } catch (SolrServerException | IOException e) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isRbacIndexed() && event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

    }
}
