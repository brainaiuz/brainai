package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.EditSubscription;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class SubscriptiontypeEditView extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private TextBox appraisalsLimit;
	private TextBox appraisals360Limit;
	private WfmForm.Field appraisalsLimitField;
	private WfmForm.Field appraisals360LimitField;
	private TextBox attachmentsFileSizePerUserLimit;
	private TextBox attachmentsSizePerCompany;
	private WfmForm.Field attachmentsFileSizePerUserLimitField;
	private WfmForm.Field attachmentsSizePerCompanyField;
	private TextBox departmentLimit;
	private WfmForm.Field departmentLimitField;
	private TextBox employeeLimit;
	private WfmForm.Field employeeLimitField;
	private TextBox invoiceLimit;
	private WfmForm.Field invoiceLimitField;
	private TextBox name;
	private WfmForm.Field nameField;
	private TextBox projectLimit;
	private WfmForm.Field projectLimitField;
	private Integer objectID;
	private TextBox taskLimit;
	private WfmForm.Field taskLimitField;

	public SubscriptiontypeEditView(Integer objectID) {
		super("edit", wfmStrings.edit());
		this.objectID = objectID;
	}

	public String getIconStyle() {
		return "icon-subscriptionTypeEdit";
	}

	@Override
	protected Widget onInitialize() {
		initInternal();
		return null;
	}

	private void initInternal() {
		name = new TextBox();
		taskLimit = new TextBox();
		projectLimit = new TextBox();
		employeeLimit = new TextBox();
		departmentLimit = new TextBox();
		attachmentsFileSizePerUserLimit = new TextBox();
		attachmentsSizePerCompany = new TextBox();
		appraisalsLimit = new TextBox();
		appraisals360Limit = new TextBox();
		invoiceLimit = new TextBox();

        LoadingPanel.loading(true);
        BackendService.App.get().SubscriptiontypeById(objectID, new AbstractAsyncCallback<String[]>() {

			public void failure(Throwable arg0) {
                LoadingPanel.loading(false);
            }

			public void success(String[] s) {
                LoadingPanel.loading(false);
                taskLimit.setText(s[0]);
				projectLimit.setText(s[1]);
				employeeLimit.setText(s[2]);
				name.setText(s[3]);
				departmentLimit.setText(s[4]);
				attachmentsFileSizePerUserLimit.setText(s[5]);
				attachmentsSizePerCompany.setText(s[6]);
				appraisalsLimit.setText(s[7]);
				appraisals360Limit.setText(s[8]);
				invoiceLimit.setText(s[9]);
			}
		});
		WfmFormPanel form = new WfmFormPanel("");
		WfmForm table = new WfmForm();
		table.setStyleName("padding10");

		name.addStyleName(DEFAULT_WIDTH);
		nameField = table.addField(wfmStrings.name(), name, true);

		taskLimit.addStyleName(DEFAULT_WIDTH);
		taskLimitField = table.addField(Property.get(Constants.TASK, wfmStrings.taskLimitField(), wfmStrings.task()), taskLimit, true);

		projectLimit.addStyleName(DEFAULT_WIDTH);
		projectLimitField = table.addField(wfmStrings.projectLimitField(), projectLimit, true);

		employeeLimit.addStyleName(DEFAULT_WIDTH);
		employeeLimitField = table.addField(wfmStrings.employeeLimitField(), employeeLimit, true);

		departmentLimit.addStyleName(DEFAULT_WIDTH);
		departmentLimitField = table.addField(wfmStrings.departmentLimitField(), departmentLimit, true);

		attachmentsFileSizePerUserLimit.addStyleName(DEFAULT_WIDTH);
		attachmentsFileSizePerUserLimitField = table.addField(wfmStrings.attachmentsFileSizePerUserLimitField(), attachmentsFileSizePerUserLimit, true);

		attachmentsSizePerCompany.addStyleName(DEFAULT_WIDTH);
		attachmentsSizePerCompanyField = table.addField(wfmStrings.attachmentsSizePerCompanyField(), attachmentsSizePerCompany, true);

		appraisalsLimit.addStyleName(DEFAULT_WIDTH);
		appraisalsLimitField = table.addField(wfmStrings.appraisalsLimitField(), appraisalsLimit, true);

		appraisals360Limit.addStyleName(DEFAULT_WIDTH);
		appraisals360LimitField = table.addField(wfmStrings.appraisals360LimitField(), appraisals360Limit, true);

		invoiceLimit.addStyleName(DEFAULT_WIDTH);
		invoiceLimitField = table.addField(wfmStrings.invoiceLimitField(), invoiceLimit, true);

		WfmButton2 updateButton = new WfmButton2(wfmStrings.update());
		updateButton.addClickHandler(event -> {
            if (validate()) {
                return;
            }
            EditSubscription list = new EditSubscription();
            list.setObjectID(objectID);
            list.setName(name.getText());
            list.setTaskLimit(Integer.valueOf(taskLimit.getText()));
            list.setProjectLimit(Integer.valueOf(projectLimit.getText()));
            list.setEmployeeLimit(Integer.valueOf(employeeLimit.getText()));
            list.setDepartmentLimit(Integer.valueOf(departmentLimit.getText()));
            list.setAttachmentsFileSizePerUserLimit(Integer.valueOf(attachmentsFileSizePerUserLimit.getText()));
            list.setAttachmentsSizePerCompany(Integer.valueOf(attachmentsSizePerCompany.getText()));
            list.setAppraisalsLimit(Integer.valueOf(appraisalsLimit.getText()));
            list.setAppraisals360Limit(Integer.valueOf(appraisals360Limit.getText()));
            list.setInvoiceLimit(Integer.valueOf(invoiceLimit.getText()));
            LoadingPanel.loading(true);
            BackendService.App.get().editSubscription(list, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable arg0) {
                    LoadingPanel.loading(false);
                    Info.show(backendStrings.couldNotUpdate(), Info.Type.WARNING);
                    closeTab();
                }

                public void success(Void arg0) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.successfullyUploaded(), Info.Type.WARNING);
                    closeTab();
                }
            });
        });

		table.addButton(updateButton);
		form.setWidget(table);
		add(form);
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