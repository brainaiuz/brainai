package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WebhookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.*;
import java.util.stream.Collectors;

public class AddWorkflowWebHook extends CustomForm2 implements Constants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private final Integer objectId;
    private Integer workflowId;
    private String formId;
    private String uuid;
    private boolean itemTable;
    private boolean isPublic;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private TextBox name, responseQuery, responsedValue;
    private TextArea2 description;
    private DataListBox fieldCodes;
    private HTML attribute;
    private DataListBox method;
    private TextBox requestUrl;
    private MultiTableNewUI headers;
    private DataListBox bodyType;
    private DataListBox rawDataTypes;
    private MultiTableNewUI formDataParams;
    private TextArea2 rawDataBox;
    private WfmButton2 btnSave;
    private DataListBox dateFormat;
    private KpiCheckBox saveIntegrationId;
    private TextBox tableFieldName;
    private LookUp2 template;
    private WorkflowWebHookItem webHook;
    private Localize localize;
    private String[] additionalFields;
    private final SelectItem[] methods = new SelectItem[]{new SelectItem(0, "POST"), new SelectItem(1, "PUT"), new SelectItem(2, "DELETE"), new SelectItem(3, "PATCH"), new SelectItem(4, "GET")};
    private final SelectItem[] bodyTypes = new SelectItem[]{new SelectItem(0, "NONE", "NONE"), new SelectItem(1, "FORM-DATA", "FORMDATA"), new SelectItem(2, "RAW", "RAW")};
    private final SelectItem[] rawDataTypeItems = new SelectItem[]{new SelectItem(0, "XML"), new SelectItem(1, "Text"), new SelectItem(2, "JSON"), new SelectItem(3, "HTML")};
    private final HashMap<String, TextBox> widgetMap = new HashMap<>();

    private final HashMap<String, TextBox> queryMap = new HashMap<>();

    public AddWorkflowWebHook(Integer objectId, Integer workflowId, String formId, String uuid, boolean itemTable) {
        super("workflowwebhook", wfmStrings.telegramAlert());
        this.objectId = objectId;
        this.workflowId = workflowId;
        this.formId = formId;
        this.uuid = uuid;
        this.itemTable = itemTable;
    }

    public AddWorkflowWebHook(Integer objectId) {
        super("workflowwebhook", wfmStrings.webHook());
        this.objectId = objectId;
        this.isPublic = true;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        name = new TextBox();

        MaterialPanel attributesPanel = new MaterialPanel("grid-row");

        MaterialPanel dropdownPanel = new MaterialPanel("col-9");
        MaterialPanel attributePanel = new MaterialPanel("col-3");
        attributesPanel.add(dropdownPanel);
        attributesPanel.add(attributePanel);

        fieldCodes = new DataListBox();
        fieldCodes.addStyleName(DEFAULT_WIDTH);
        fieldCodes.addValueChangeHandler(changeEvent -> {
            if (changeEvent.getValue() != null && changeEvent.getValue().getDescription() != null) {
                attribute.setHTML(changeEvent.getValue().getDescription());
            } else {
                attribute.setHTML(wfmStrings.noAttributesSelected());
            }
        });
        dropdownPanel.add(fieldCodes);

        attribute = new HTML(wfmStrings.noAttributesSelected());
        attribute.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        attribute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        attributePanel.add(attribute);

        description = new TextArea2(4096, wfmStrings.description());
        description.setWidth("100%");
        description.setHeight(230);

        method = new DataListBox();
        method.setWithoutNullLabel(true);
        method.setItems(methods);

        requestUrl = new TextBox();

        headers = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getParameterWidget(null, null);

            }

            @Override
            public boolean isFilled() {
//                for (Map<String, Widget> widgetsMap : headers.getWidgets()) {
//                    PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
//                    if (!"".equals(phoneNumber.toString())) {
//                        return true;
//                    }
//                }
                return false;
            }
        }, false);

        bodyType = new DataListBox();
        bodyType.setWithoutNullLabel(true);
        bodyType.setItems(bodyTypes);
        bodyType.addValueChangeHandler(changeEvent -> onBodyTypeChange());

        formDataParams = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getParameterWidget(null, null);

            }

            @Override
            public boolean isFilled() {
//                for (Map<String, Widget> widgetsMap : headers.getWidgets()) {
//                    PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
//                    if (!"".equals(phoneNumber.toString())) {
//                        return true;
//                    }
//                }
                return false;
            }
        }, false);
        formDataParams.setVisible(false);

        rawDataTypes = new DataListBox();
        rawDataTypes.setWithoutNullLabel(true);
        rawDataTypes.setItems(rawDataTypeItems);
        rawDataTypes.setEnabled(false);

        rawDataBox = new TextArea2(5000);
        rawDataBox.setEnabled(false);
        rawDataBox.setHeight(500);

        MaterialPanel dateFormatPanel = new MaterialPanel("grid-row");

        MaterialPanel formatPanel = new MaterialPanel("col-9");
        MaterialPanel textPanel = new MaterialPanel("col-3");
        attributesPanel.add(formatPanel);
        attributesPanel.add(textPanel);

        dateFormat = new DataListBox();
        dateFormat.setWithoutNullLabel(true);
        formatPanel.add(dateFormat);

        HTML dateFormatText = new HTML();
        dateFormatText.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        dateFormatText.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dateFormatText.setHTML(settingsStrings.useDateFormatAttributeInBody());
        textPanel.add(dateFormatText);

        responseQuery = new TextBox();
        responsedValue = new TextBox();


        saveIntegrationId = new KpiCheckBox(settingsStrings.saveIntegrationId());

        template = new LookUp2();

        addField(WEB_HOOK_FORM.NAME, name, wfmStrings.name());
        addField(WEB_HOOK_FORM.DESCRIPTION, description, null);
        if (!isPublic) {
            addField(WEB_HOOK_FORM.ATTRIBUTES, attributesPanel, wfmStrings.attributes());
            addField(WEB_HOOK_FORM.DATE_FORMAT, dateFormatPanel, wfmStrings.dateFormat());
            addField(WEB_HOOK_FORM.TEMPLATE, template, wfmStrings.template());
        }

        addField(WEB_HOOK_FORM.METHOD, method, wfmStrings.method());
        addField(WEB_HOOK_FORM.REQUEST_URL, requestUrl, wfmStrings.urlname());
        addField(WEB_HOOK_FORM.HEADER, headers, wfmStrings.header());

        addField(WEB_HOOK_FORM.BODY_TYPE, bodyType, wfmStrings.type());
        addField(WEB_HOOK_FORM.FORM_DATA_PARAMS, formDataParams, wfmStrings.parameters());
        addField(WEB_HOOK_FORM.RAW_DATA_FORMAT, rawDataTypes, wfmStrings.pageFormat());
        addField(WEB_HOOK_FORM.RAW_DATA_BOX, rawDataBox, wfmStrings.textBox());
        addField(WEB_HOOK_FORM.SAVE_INTEGRATION_ID, saveIntegrationId, null);
        addField(WEB_HOOK_FORM.RESPONSE_QUERY, responseQuery, "Response query");
        addField(WEB_HOOK_FORM.RESPONSED_VALUE, responsedValue, "Responsed Value");

        show();
    }

    private WidgetsMap getParameterWidget(String name, String value) {
        WidgetsMap widgetsMap = new WidgetsMap();

        TextBox nameBox = new TextBox();
        if (name != null) {
            nameBox.setText(name);
        }

        TextBox valueBox = new TextBox();
        valueBox.setMaxLength(500);
        if (value != null) {
            valueBox.setText(value);
        }

        widgetsMap.add("NAME", nameBox);
        widgetsMap.add("VALUE", valueBox);
        return widgetsMap;
    }

    private void onBodyTypeChange() {
        int id = bodyType.getSelectedId();
        if (id == 0) {
            formDataParams.setVisible(false);
            rawDataBox.setEnabled(false);
//            rawDataBox.setText(null);
            rawDataTypes.setEnabled(false);
        } else if (id == 1) {
            formDataParams.setVisible(true);
            rawDataBox.setEnabled(false);
//            rawDataBox.setText(null);
            rawDataTypes.setEnabled(false);
        } else {
            formDataParams.setVisible(false);
            rawDataBox.setEnabled(true);
            rawDataTypes.setEnabled(true);
        }
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        btnSave = new WfmButton2(objectId == null ? wfmStrings.save() : wfmStrings.update(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        btnSave.ensureDebugId("save_button");
        addButton(btnSave);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        WebhookRequestItem item = new WebhookRequestItem();
        item.setId(objectId);
        item.setWorkflowId(workflowId);
        item.setFormId(formId);
        item.setUuid(uuid);
        item.setItemTable(itemTable);
        item.setPublic(isPublic);
        ProfileService.App.get().getWorkflowWebHook(item, new AsyncCallback<WorkflowWebHookItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowWebHookItem result) {
                LoadingPanel.loading(false);
                webHook = result;
                if (isPublic) {
                    setValues(result, true);
                } else {
                    initColumns(result.getWorkflow() != null ? result.getWorkflow().getModule() : result.getForm().getName(), result.getWorkflow() != null);
                }
            }
        });
    }

    private void setValues(WorkflowWebHookItem item, boolean isPublic) {
        name.setText(item.getName());
        description.setText(item.getDescription());
        responseQuery.setText(item.getQueryResponse());
        responsedValue.setText(item.getResponsedValue());
        method.setSelectedByTheBestValue(item.getMethod());
        requestUrl.setText(item.getRequestUrl());
        dateFormat.setItems(item.getDateFormats());
        saveIntegrationId.setValue(item.isSaveIntegrationId());
        if (!isPublic) {
            template.setItems(item.getTemplates().toArray(new SelectItem[]{}));
            template.getSuggestBox().addSelectionHandler(event -> {
                if (template.getSelectedItemID() != null) {
                    LoadingPanel.loading(true);
                    ProfileService.App.get().getWorkflowWebHook(new WebhookRequestItem(template.getSelectedItemID(), true), new AbstractAsyncCallback<WorkflowWebHookItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            super.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(WorkflowWebHookItem result) {
                            LoadingPanel.loading(false);
                            super.onSuccess(result);
                            setValues(result, true);
                        }
                    });
                }
            });
        }
        if (item.getDateFormat() != null) {
            dateFormat.setSelectedByDescription(item.getDateFormat());
        }

        if (item.getHeaders() != null && !item.getHeaders().isEmpty()) {
            headers.clear();
            for (String key : item.getHeaders().keySet()) {
                headers.addWidgets(getParameterWidget(key, item.getHeaders().get(key)));
            }
        } else {
            headers.clear();
            for (int i = 0; i < 3; i++) {
                headers.addWidgets(getParameterWidget(null, null));
            }
        }

        bodyType.setSelectedByDescription(item.getBodyType());
        onBodyTypeChange();
        if (item.getFormDataParams() != null && !item.getFormDataParams().isEmpty()) {
            formDataParams.clear();
            for (String key : item.getFormDataParams().keySet()) {
                formDataParams.addWidgets(getParameterWidget(key, item.getFormDataParams().get(key)));
            }
        } else {
            formDataParams.clear();
            for (int i = 0; i < 3; i++) {
                formDataParams.addWidgets(getParameterWidget(null, null));
            }
        }
        rawDataTypes.setSelectedByTheBestValue(item.getRawDataFormat());
        rawDataBox.setText(item.getRawDataText());
        if (item.getResponseAttributes() != null) {
            for (String key : item.getResponseAttributes().keySet()) {
                widgetMap.get(key).setText(item.getResponseAttributes().get(key));
            }
        }
        if (tableFieldName != null) {
            tableFieldName.setText(item.getTableFieldName());
        }
    }

    private void getValues() {
        webHook.setPublic(isPublic);
        webHook.setName(name.getText());
        webHook.setDescription(description.getText());

        webHook.setMethod(method.getSelectedItem(true).getName());
        webHook.setRequestUrl(requestUrl.getText());
        SelectItem format = dateFormat.getSelectedItem(true);
        if (format != null) {
            webHook.setDateFormat(format.getDescription());
        }
        webHook.setSaveIntegrationId(saveIntegrationId.getValue());
        webHook.setQueryResponse(responseQuery.getText());
        webHook.setResponsedValue(responsedValue.getText());

        Map<String, String> headersMap = new LinkedHashMap<>();
        headers.getWidgets().forEach(header -> {
            TextBox nameBox = (TextBox) header.get("NAME");
            TextBox valueBox = (TextBox) header.get("VALUE");
            if (nameBox.getText() != null && !"".equals(nameBox.getText()) &&
                    valueBox.getText() != null && !"".equals(valueBox.getText())) {
                headersMap.put(nameBox.getText(), valueBox.getText());
            }
        });
        webHook.setHeaders(headersMap);

        webHook.setBodyType(bodyType.getSelectedItem(true).getDescription());
        if (webHook.getBodyType().equals("NONE")) {
            webHook.setFormDataParams(null);
            webHook.setRawDataFormat(null);
            webHook.setRawDataText(null);
        } else if (webHook.getBodyType().equals("FORMDATA")) {
            Map<String, String> formDataParamsMap = new LinkedHashMap<>();
            formDataParams.getWidgets().forEach(header -> {
                TextBox nameBox = (TextBox) header.get("NAME");
                TextBox valueBox = (TextBox) header.get("VALUE");
                if (nameBox.getText() != null && !"".equals(nameBox.getText()) &&
                        valueBox.getText() != null && !"".equals(valueBox.getText())) {
                    formDataParamsMap.put(nameBox.getText(), valueBox.getText());
                }
            });
            webHook.setFormDataParams(formDataParamsMap);
            webHook.setRawDataText(null);
            webHook.setRawDataFormat(null);
        } else {
            webHook.setFormDataParams(null);
            webHook.setRawDataFormat(rawDataTypes.getSelectedItem(true).getName());
            webHook.setRawDataText(rawDataBox.getText());
        }
        webHook.setWorkflowId(workflowId);
        webHook.setFormId(formId);
        webHook.setUuid(uuid);
        webHook.setItemTable(itemTable);
        if (itemTable) {
            webHook.setTableFieldName(tableFieldName.getText());
        }
        if (formId != null) {
            for (String key : widgetMap.keySet()) {
                if (widgetMap.get(key).getText() != null) {
                    webHook.addResponseAttribute(key, widgetMap.get(key).getText());
                }
            }

            for (String s : queryMap.keySet()) {
                if (queryMap.get(s).getText() != null) {
                    webHook.addQueryAtributes(s, queryMap.get(s).getText());
                }
            }
        }
    }

    private void save() {
        if (!isPublic && !validate()) {
            return;
        }
        btnSave.setEnabled(false);
        getValues();
        LoadingPanel.loading(true);
        ProfileService.App.get().saveWorkflowWebHook(webHook, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
                Info.show(wfmMessages.webHookSuccessfully(objectId == null ? wfmStrings.saved() : wfmStrings.updated()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(objectId == null ? WfmUiEventType.ON_WORKFLOW_WEB_HOOK_ADD : WfmUiEventType.ON_WORKFLOW_WEB_HOOK_UPDATE, result, AddWorkflowWebHook.this);
                closeTab();
            }
        });
    }


    public boolean validate() {
        int errors = 0;

        errors += markAsError(name, !Validation.validateTextBoxRequired(name));
        errors += markAsError(requestUrl, !Validation.validateTextBoxRequired(requestUrl));
        if (itemTable) {
            errors += markAsError(tableFieldName, !Validation.validateTextBoxRequired(tableFieldName));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void initColumns(String code, boolean isWorkflow) {
        if (code != null) {
            AllInOneService.App.get().getDefaultModelForm(getFormIDOfModule(code, isWorkflow), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    fields.clear();
                    if (modelForm != null) {
                        if (modelForm.getFields() != null && modelForm.getFields().size() > 0) {
                            fields.putAll(modelForm.getFields().stream().filter(ModelField::isEntityField).collect(Collectors.toMap(ModelField::getField_ID, x -> x, (existing, replacement) -> replacement)));
                        }
                        int index = 1;
                        if (!isWorkflow) {
                            if (itemTable) {
                                tableFieldName = new TextBox();
                                addField(WEB_HOOK_FORM.TABLE_FIELD_NAME, tableFieldName, wfmStrings.tableOfFields());
                                if (webHook.getItemTableColumns() != null) {
                                    for (String col : webHook.getItemTableColumns()) {
                                        drawWidget(col, index);
                                        index++;
                                    }
                                }
                            } else if (modelForm.getFields() != null) {
                                for (ModelField field : modelForm.getFields()) {
                                    if (index <= modelForm.getFields().size()) {
                                        drawWidget(field.getField_ID(), index);
                                        index++;
                                    }
                                }
                            }
                        }
                        additionalFields = modelForm.getAdditionalFields();
                        notifyAllFieldRelateds(modelForm.getAttributes());
                        setValues(webHook, false);
                    }
                }
            });
        }
    }

    private void drawWidget(String fieldId, int index) {
        Div inputGroup = new Div();
        inputGroup = new Div("input-group");
        TextBox box = new TextBox();
        inputGroup.add(box);
        TextBox query = new TextBox();
        inputGroup.add(query);
        CheckBox checkBox = new CheckBox();
        inputGroup.add(checkBox);

        query.setEnabled(false);

        checkBox.addValueChangeHandler(event -> {
            if (checkBox.getValue()) {
                Info.warn(fieldId);
                queryMap.put(fieldId, query);
                query.setEnabled(true);
            } else {
                query.setText("");
                query.setEnabled(false);
            }
        });

        widgetMap.put(fieldId, box);
        addField("FIELD_" + index, inputGroup, fieldId);
    }

    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        fieldCodes.setItems(items);
        fieldCodes.addValueChangeHandler(changeEvent -> {
            attribute.setText("");
            if (fieldCodes.getSelectedItem() != null && fieldCodes.getSelectedItem().getDescription() != null) {
                attribute.setText(fieldCodes.getSelectedItem().getDescription());
            }
        });
    }

    private SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID().split(",")[0];
                description = description != null ? ("${" + description.toLowerCase() + "}") : null;
                result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
            }
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            for (SelectItem item : additionalAttributes) {
                result.removeIf(selectItem -> Objects.equals(item.getName(), selectItem.getName()));
            }
            result.addAll(Arrays.asList(additionalAttributes));
        }
        int i = 0;
        if (additionalFields != null) {
            for (String s : additionalFields) {
                SelectItem item = new SelectItem(i, localize.localizeByCode(s), "${" + s.toLowerCase() + "}");
                result.add(item);
                i++;
            }
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }

    private String getFormIDOfModule(String code, boolean isWorkflow) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        if (!isWorkflow) {
            return code;
        }
        if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(code)) {
            return LayoutRPC.MANUAL_JOURNAL_FORM;
        } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(code)) {
            return LayoutRPC.TASK_MAX_FORM;
        } else if (WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(code)) {
            return LayoutRPC.PRODUCT_FORM;
        } else if (WorkflowRule._WORKFLOW_MODULE_PRODUCT_CATEGORY.equals(code)) {
            return LayoutRPC.PRODUCT_CATEGORY_FORM;
        } else if (WorkflowRule._WORKFLOW_MODULE_COMPANY_SETTINGS.equals(code)) {
            return LayoutRPC.COMPANY_SETTINGS_FORM;
        } else if (WorkflowRule._WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS.equals(code)) {
            return LayoutRPC.EMPLOYEE_DOCUMENTS_FORM;
        }
        //don't forget the code of the module reference must be build like this. "_WORKFLOW_MODULE_" + formID.replaceAll("_FORM", "");
        //for example :module for lead. "_WORKFLOW_MODULE_" + LEAD_FORM.replaceAll("_FORM", "") = "_WORKFLOW_MODULE_LEAD";
        //why we need formID? Because all fields for this form is in form(database.tablename = model).
        return code.replace("_WORKFLOW_MODULE_", "") + "_FORM";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_WEB_HOOK_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
