package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddSubscriptiontypeView extends View implements CommandConstants, Constants, Colapse {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private TextBox appraisalsLimit;
	private TextBox appraisals360Limit;
	private TextBox attachmentsFileSizePerUserLimit;
	private TextBox attachmentsSizePerCompany;
	private WfmForm.Field appraisalsLimitField;
	private WfmForm.Field appraisals360LimitField;
	private WfmForm.Field attachmentsFileSizePerUserLimitField;
	private WfmForm.Field attachmentsSizePerCompanyField;
	private TextBox departmentLimit;
	private WfmForm.Field departmentLimitField;
	private TextBox employeeLimit;
	private WfmForm.Field employeeLimitField;
	private TextBox invoiceLimit;
	private WfmForm.Field invoiceLimitField;
	private WfmFormPanel form;
	private TextBox name;
	private WfmForm.Field nameField;
	private TextBox projectLimit;
	private WfmForm.Field projectLimitField;
	private TextBox taskLimit;
	private WfmForm.Field taskLimitField;
	private boolean saveAndClose = false;

	public AddSubscriptiontypeView() {
		super("add", wfmStrings.add());
	}

	public String getIconStyle() {
		return "icon-subscriptionTypeAdd";
	}

	@Override
	protected Widget onInitialize() {
		initInternal();
		return null;
	}

	private void initInternal() {
		name = new TextBox();
		name.addStyleName(DEFAULT_WIDTH);
		taskLimit = new TextBox();
		taskLimit.addStyleName(DEFAULT_WIDTH);
		projectLimit = new TextBox();
		projectLimit.addStyleName(DEFAULT_WIDTH);
		employeeLimit = new TextBox();
		employeeLimit.addStyleName(DEFAULT_WIDTH);
		departmentLimit = new TextBox();
		departmentLimit.addStyleName(DEFAULT_WIDTH);
		attachmentsFileSizePerUserLimit = new TextBox();
        attachmentsFileSizePerUserLimit.addStyleName(DEFAULT_WIDTH);
        attachmentsSizePerCompany = new TextBox();
        attachmentsSizePerCompany.addStyleName(DEFAULT_WIDTH);
        appraisalsLimit = new TextBox();
        appraisalsLimit.addStyleName(DEFAULT_WIDTH);
        appraisals360Limit = new TextBox();
        appraisals360Limit.addStyleName(DEFAULT_WIDTH);
        invoiceLimit = new TextBox();
        invoiceLimit.addStyleName(DEFAULT_WIDTH);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.saveAndNew(), WfmButton2.BTN_PRIMARY);
        WfmButton2 saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        form = new WfmFormPanel("/CreateSubscription");
        WfmForm table = new WfmForm();
        table.setStyleName("bknd-addSubsriptionType-table file--AddSubscriptiontypeView");

        saveButton.addClickHandler(event -> save());
        saveAndCloseButton.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

		name.setName("name");
		name.addStyleName(DEFAULT_WIDTH);
		nameField = table.addField(wfmStrings.name(), name, true);

		taskLimit.setName("taskLimit");
		taskLimit.addStyleName(DEFAULT_WIDTH);
		taskLimitField = table.addField(Property.get(Constants.TASK, wfmStrings.taskLimitField(), wfmStrings.task()), taskLimit, true);


		projectLimit.setName("projectLimit");
		projectLimit.addStyleName(DEFAULT_WIDTH);
		projectLimitField = table.addField(wfmStrings.projectLimitField(), projectLimit, true);

		employeeLimit.setName("employeeLimit");
		employeeLimit.addStyleName(DEFAULT_WIDTH);
		employeeLimitField = table.addField(wfmStrings.employeeLimitField(), employeeLimit, true);

		departmentLimit.setName("departmentLimit");
		departmentLimit.addStyleName(DEFAULT_WIDTH);
		departmentLimitField = table.addField(wfmStrings.departmentLimitField(), departmentLimit, true);

		attachmentsFileSizePerUserLimit.setName("attachmentsFileSizePerUserLimit");
		attachmentsFileSizePerUserLimit.addStyleName(DEFAULT_WIDTH);
		attachmentsFileSizePerUserLimitField = table.addField(wfmStrings.attachmentsFileSizePerUserLimitField(), attachmentsFileSizePerUserLimit, true);

		attachmentsSizePerCompany.setName("attachmentsSizePerCompany");
		attachmentsSizePerCompany.addStyleName(DEFAULT_WIDTH);
		attachmentsSizePerCompanyField = table.addField(wfmStrings.attachmentsSizePerCompanyField(), attachmentsSizePerCompany, true);

		appraisalsLimit.setName("appraisalsLimit");
		appraisalsLimit.addStyleName(DEFAULT_WIDTH);
		appraisalsLimitField = table.addField(wfmStrings.appraisalsLimitField(), appraisalsLimit, true);

		appraisals360Limit.setName("appraisals360Limit");
		appraisals360Limit.addStyleName(DEFAULT_WIDTH);
		appraisals360LimitField = table.addField(wfmStrings.appraisals360LimitField(), appraisals360Limit, true);

		invoiceLimit.setName("invoiceLimit");
		invoiceLimit.addStyleName(DEFAULT_WIDTH);
		invoiceLimitField = table.addField(wfmStrings.invoiceLimitField(), invoiceLimit, true);


		table.addButton(saveButton);
		table.addButton(saveAndCloseButton);
		form.setWidget(table);

		form.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);
            WfmMessageBox messageBox;
            if (form.isSuccess()) {
                messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                messageBox.setTitle(wfmStrings.information());
                messageBox.setMessage(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), backendStrings.subscription()));
                messageBox.addCloseHandler(popupPanelCloseEvent -> onShellOk());
                messageBox.open();
            } else {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
}
        });
		add(form);
	}

	private void onShellOk() {
		if (saveAndClose) {
			closeTab();
		} else {
			reInit();
		}
	}

	private void reInit() {
		clear();
		initInternal();
	}

	private void save() {
		if (validate()) {
			return;
		}
		try {

			/*
						   form.setParameter("tasklimit",taskLimit.getText());
						   form.setParameter("employeelimit",employeeLimit.getText());
						   form.setParameter("projectlimit",projectLimit.getText());
						   */
			/*	String task=taskLimit.getText();
						   SubscriptionListItem[] task=taskLimit.getText();
						   for(int i=0;i<task.length;i++){
							   if(task[i] != null)
							   form.setParameter("tasklimit", task[i].getTaskLimit().toString(),true);
						   }
						   //String employee =employeeLimit.getText();
						   //form.setParameter("project",project.getText());
						   SubscriptionListItem[] employee =employeeLimitField.getText();
						   for (int i = 0; i < employee .length; i++){
							   form.setParameter("employeelimit", employee[i].getEmployeeLimit().toString(),true);
						  }
						 //String project =projectLimit.getText();
						   //form.setParameter("project",project.getText());
						   SubscriptionListItem[] project =projectLimit.getText();
						   for (int i = 0; i < project.length; i++)
							   form.setParameter("projectlimit", project[i].getProjectLimit().toString(),true);
					   */
		} catch (Throwable t) {
			t.getMessage();
		}

		form.submit();
		LoadingPanel.loading(true);
//		refreshOnDemand(new String[]{TASK_LIST, TIMESHEET});
	}

	private boolean validate() {
		int errors = 0;

		if (!Validation.validateTextBoxRequired(name, nameField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(taskLimit, taskLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(projectLimit, projectLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(employeeLimit, employeeLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(departmentLimit, departmentLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(attachmentsFileSizePerUserLimit, attachmentsFileSizePerUserLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(attachmentsSizePerCompany, attachmentsSizePerCompanyField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(appraisalsLimit, appraisalsLimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(appraisals360Limit, appraisals360LimitField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(invoiceLimit, invoiceLimitField)) {
			errors++;
		}

		return errors > 0;
	}

	public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess() {
				callback.onSuccess(onInitialize());
			}
		});
	}
}