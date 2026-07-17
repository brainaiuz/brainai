package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 02.05.14.
 */
public class GanttChartTaskEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsTask> TYPE = new WfmType<>(EventTypes.ganttchartTaskEventListener);

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskServiceLocal taskService;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        event.setStatus(EventStatus.COMPLETED.name());
        taskService.updateTaskDailyLoad(task);
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }
}
