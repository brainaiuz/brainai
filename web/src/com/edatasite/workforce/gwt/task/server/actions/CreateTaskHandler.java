//package com.edatasite.workforce.gwt.task.server.actions;
//
//import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
//import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
//
//public class CreateTaskHandler extends WfmCommandHandler {
//
//	private TaskServiceLocal taskService;
//
//	public TaskServiceLocal getTaskService() {
//		return taskService;
//	}
//
//	public void setTaskService(TaskServiceLocal taskService) {
//		this.taskService = taskService;
//	}
//
//	public void execute(Object command) throws Throwable {
//		CreateTaskCommand c = (CreateTaskCommand) command;
//		taskService.createTask(c);
//	}
//
//}
