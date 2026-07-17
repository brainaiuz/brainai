package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 20.07.11
 * Time: 12:01
 */
@Transactional
public class ProjectStatusEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectStatusEventListener);

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        EdsUser updater = userManager.get(event.getSourceID());
        ServerSecurityContext.getInstance().setCompanyId(updater.getCompany().getObjectID());
        EdsReference reference = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
        taskManager.updateTasksStatus(reference, project.getObjectID());
        List<EdsTask> tasks = taskManager.getProjectTasks(project);
        StringBuilder buf = new StringBuilder();
        for (EdsTask task : tasks) {
            buf.append(task.getObjectID().toString());
            if (!task.equals(tasks.get(tasks.size() - 1))) {
                buf.append(", ");
            }

        }
        if ("".equals(buf.toString())) {
            buf.append("0");
        }
        employeeTaskManager.updateEmployeeTasksStatus(reference, buf.toString());
        taskRbacManager.batchIndexTask(tasks);
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.indexes(tasks);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                e.printStackTrace();
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

    }
}
