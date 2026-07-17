package com.edatasite.workforce.gwt.task.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateTaskCommentHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        CreateTaskCommentCommand c = (CreateTaskCommentCommand) command;

        wfmCommandServiceLocal.createTaskCommentHandler(c);
    }
}
