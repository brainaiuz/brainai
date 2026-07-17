package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WebhookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User : Akhror
 * Date : 14.01.2022
 */
public class WorkflowWebHookSummaryView extends CustomForm2 implements NoColapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private final Integer objectID;
    private HTML name, method, requestUrl, bodyType, rawDataType, dateFormat;
    private TextArea2 description, rawDataText;
    private MultiTableNewUI headers, formDataParams;
    private KpiCheckBox saveIntegrationId;
    private String formId;
    private boolean isPublic;

    public WorkflowWebHookSummaryView(Integer objectID, String formId) {
        super("summary");
        setDescription(wfmStrings.summaryView());
        this.objectID = objectID;
        this.formId = formId;
    }

    public WorkflowWebHookSummaryView(Integer objectID) {
        super("summary");
        setDescription(wfmStrings.summaryView());
        this.objectID = objectID;
        this.isPublic = true;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        name = new HTML();
        description = new TextArea2();
        description.hideCharacterLimitPanel();
        description.setEnabled(false);
        method = new HTML();
        requestUrl = new HTML();
        bodyType = new HTML();
        rawDataType = new HTML();
        dateFormat = new HTML();
        rawDataText = new TextArea2(5000);
        rawDataText.hideCharacterLimitPanel();
        rawDataText.setEnabled(false);
        rawDataText.setHeight(500);

        headers = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getParameterWidget(null, null);

            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, true);

        formDataParams = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getParameterWidget(null, null);

            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, true);
        saveIntegrationId = new KpiCheckBox(settingsStrings.saveIntegrationId());
        addFieldsToForm();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void addFieldsToForm() {
        addField(WEB_HOOK_FORM.NAME, name, wfmStrings.name());
        addField(WEB_HOOK_FORM.DESCRIPTION, description, null);
        addField(WEB_HOOK_FORM.DATE_FORMAT, dateFormat, wfmStrings.dateFormat());

        addField(WEB_HOOK_FORM.METHOD, method, wfmStrings.method());
        addField(WEB_HOOK_FORM.REQUEST_URL, requestUrl, wfmStrings.urlname());
        addField(WEB_HOOK_FORM.HEADER, headers, wfmStrings.header());

        addField(WEB_HOOK_FORM.BODY_TYPE, bodyType, wfmStrings.type());
        addField(WEB_HOOK_FORM.FORM_DATA_PARAMS, formDataParams, wfmStrings.parameters());
        addField(WEB_HOOK_FORM.RAW_DATA_FORMAT, rawDataType, wfmStrings.pageFormat());
        addField(WEB_HOOK_FORM.RAW_DATA_BOX, rawDataText, wfmStrings.textBox());
        addField(WEB_HOOK_FORM.SAVE_INTEGRATION_ID, saveIntegrationId, null);
    }

    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged(isPublic ? "publicwebhook|add/add/" + objectID : "webhookEdit|edit/" + objectID + "/null/" + formId));
        addButton(editButton);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getWorkflowWebHook(new WebhookRequestItem(objectID, isPublic), new AsyncCallback<WorkflowWebHookItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowWebHookItem result) {
                LoadingPanel.loading(false);

                name.setHTML(result.getName());
                description.setText(result.getDescription());
                method.setHTML(result.getMethod());
                requestUrl.setHTML(result.getRequestUrl());
                dateFormat.setHTML(result.getDateFormat());

                bodyType.setHTML(result.getBodyType());
                rawDataType.setHTML(result.getRawDataFormat());
                rawDataText.setText(result.getRawDataText());
                saveIntegrationId.setValue(result.isSaveIntegrationId());

                if (result.getHeaders() != null && !result.getHeaders().isEmpty()) {
                    headers.clear();
                    for (String key : result.getHeaders().keySet()) {
                        headers.addWidgets(getParameterWidget(key, result.getHeaders().get(key)));
                    }
                }

                if (result.getFormDataParams() != null && !result.getFormDataParams().isEmpty()) {
                    formDataParams.clear();
                    for (String key : result.getFormDataParams().keySet()) {
                        formDataParams.addWidgets(getParameterWidget(key, result.getFormDataParams().get(key)));
                    }
                }
            }
        });
    }

    private WidgetsMap getParameterWidget(String name, String value) {
        WidgetsMap widgetsMap = new WidgetsMap();

        TextBox nameBox = new TextBox();
        if (name != null) {
            nameBox.setText(name);
        }

        TextBox valueBox = new TextBox();
        if (value != null) {
            valueBox.setText(value);
        }

        widgetsMap.add("NAME", nameBox);
        widgetsMap.add("VALUE", valueBox);
        return widgetsMap;
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_WEB_HOOK_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void onSuccess() {
                if (callback != null) {
                    callback.onSuccess(onInitialize());
                }
            }
        });
    }
}
