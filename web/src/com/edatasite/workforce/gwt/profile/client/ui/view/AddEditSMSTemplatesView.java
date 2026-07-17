package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Azazello on 4/21/15.
 */
public class AddEditSMSTemplatesView extends CustomForm2 implements Constants, Colapse, CustomFormConstants {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    private String test_code_ID_name = "add_sms_template_view_";
    protected Integer objectID;
    protected SMSTemplateItem item;
    private TextBox name;
    private DataListBox modules;
    private DataListBox attributes;
    private VerticalPanel attributePanel;
    private RadioButton defaultYes;
    private RadioButton defaultNo;
    private TextArea2 content;
    private FlexTable isDefaultTable;
    private LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private HTML attribute;

    public AddEditSMSTemplatesView(Integer objectID) {
        this("addsmstemplate", objectID == null ? settingsStrings.addSMSTemplate() : settingsStrings.editSMSTemplate());
        this.objectID = objectID;
    }

    public AddEditSMSTemplatesView(String name, String description) {
        super(name, description);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void drawForm() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.addStyleName("smsemail_content");
        horizontalPanel.add(content);
        horizontalPanel.add(attributePanel);
        horizontalPanel.setSpacing(5);
        addTitleField(SMS_INFORMATION, wfmStrings.smsInformation());
        addField(NAME, name, getTitle(wfmStrings.name(), true));
        addField(MODULE, modules, getTitle(wfmStrings.apps(), true));
        addField(IS_DEFAULT, isDefaultTable, getTitle(wfmStrings.isDefault()));
        addField(CONTENT, horizontalPanel, null);
        show();
    }

    protected void registerFields() {
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(test_code_ID_name + "name");

        modules = new DataListBox();
        modules.addStyleName(DEFAULT_WIDTH);
        modules.ensureDebugId(test_code_ID_name + "modules");
        modules.addValueChangeHandler(changeEvent -> initColumns(modules.getSelectedItem()));

        attributes = new DataListBox();
        attributes.addStyleName(DEFAULT_WIDTH);
        attributes.ensureDebugId(test_code_ID_name + "attributes");

        attributePanel = new VerticalPanel();
        attributePanel.ensureDebugId(test_code_ID_name + "attributePanel");

        attribute = new HTML();
        attribute.ensureDebugId(test_code_ID_name + "attribute");
        attribute.addStyleName("smsemail_attribute");

        attributePanel.add(attributes);
        attributePanel.add(attribute);
        attributePanel.setSpacing(5);
        attributePanel.addStyleName("cellitem-margin");

        defaultYes = new KpiRadioButton("default", ("&nbsp;" + wfmStrings.yes() + "&nbsp;"), true);
        defaultYes.ensureDebugId(test_code_ID_name + "default_yes");

        defaultNo = new KpiRadioButton("default", ("&nbsp;" + wfmStrings.no() + "&nbsp;"), true);
        defaultNo.ensureDebugId(test_code_ID_name + "default_no");

        content = new TextArea2(500, wfmStrings.content());
        content.setHeight("150px");
        content.setWidth("500px");
        content.ensureDebugId(test_code_ID_name + "content");

        isDefaultTable = new FlexTable();
        isDefaultTable.addStyleName(DEFAULT_WIDTH);
        isDefaultTable.setWidget(0, 0, defaultYes);
        isDefaultTable.setWidget(0, 1, defaultNo);

        drawForm();
    }

    private void initColumns(SelectItem module) {
        if (module != null && module.getReferenceCode() != null) {
            if (SMS_TEMPLATE_CUSTOMER_BALANSE.equals(module.getReferenceCode()) || SMS_TEMPLATE_SUPPLIER_BALANSE.equals(module.getReferenceCode())) {
                ProfileService.App.get().getEmailTemplateCategoryFields(module.getId(), new AsyncCallback<ArrayList<String>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(ArrayList<String> result) {
                        fields.clear();
                        if (result != null) {
                            SelectItem[] attributes = new SelectItem[result.size()];
                            int i = 0;
                            for (String key : result) {
                                attributes[i] = new SelectItem(i, key, key);
                                i++;
                            }
                            notifyAllFieldRelateds(attributes);
                        }
                    }
                });
            } else {
                AllInOneService.App.get().getDefaultModelForm(getFormIDOfModule(module.getReferenceCode()), new AsyncCallback<ModelForm>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(ModelForm modelForm) {
                        fields.clear();
                        if (modelForm != null && modelForm.getFields().size() > 0) {
                            for (ModelField field : modelForm.getFields()) {
                                if (field.isEntityField()) {
                                    fields.put(field.getField_ID(), field);
                                }
                            }
                        }
                        notifyAllFieldRelateds(modelForm.getAttributes());
                    }
                });
            }
        } else {
            attribute.setHTML("");
            attributes.clear();
        }
    }

    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        attributes.setItems(items);
        attributes.addValueChangeHandler(changeEvent -> {
            attribute.setText("");
            if (attributes.getSelectedItem() != null && attributes.getSelectedItem().getDescription() != null) {
                attribute.setText(attributes.getSelectedItem().getDescription());
            }
        });
    }

    private SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = new Localize().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID() != null ? ("${" + entry.getValue().getField_ID().toLowerCase() + "}") : entry.getValue().getField_ID();
                result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
            }
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            result.addAll(Arrays.asList(additionalAttributes));
        }
        result.sort((item1, item2) -> item1.getName().compareTo(item2.getName()));
        return result.toArray(new SelectItem[]{});
    }

    private String getFormIDOfModule(String code) {
        if (code != null && !"".equals(code)) {
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(code)) {
                return LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(code)) {
                return LayoutRPC.TASK_MAX_FORM;
            }
            //dont' forget the code of the module reference must be build like this. "_WORKFLOW_MODULE_" + formID.replaceAll("_FORM", "");
            //for example :module for lead. "_WORKFLOW_MODULE_" + LEAD_FORM.replaceAll("_FORM", "") = "_WORKFLOW_MODULE_LEAD";
            //why we need formID? Because all fields for this form is in form(database.tablename = model).
            return code.replace("_WORKFLOW_MODULE_", "") + "_FORM";
        }
        return null;
    }

    @Override
    protected void initPredefinedValues() {
        if (item != null) {
            addPredefinedValues(MODULE, item.getModules());
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected void addButtons() {
        if (objectID == null) {
            addButton(wfmStrings.save(), null, test_code_ID_name.concat("save_and_close_button"), (ClickHandler) event -> save());
        } else {
            addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, null, test_code_ID_name.concat("save_and_close_button"), (ClickHandler) event -> save());
        }

    }

    private void save() {
        if (!validate()) {
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        ProfileService.App.get().saveSMSTemplate(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                closeTab();
                Info.show(wfmMessages.successSMSTemplateSaved(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_TEMPLATE_ADD_EDIT, result, AddEditSMSTemplatesView.this);
            }
        });
    }

    private void setValues() {
        item = item == null ? new SMSTemplateItem() : item;
        item.setName(name.getText());
        if (modules.getSelectedItem() != null) {
            item.setModuleID(modules.getSelectedId());
        }
        item.setContent(content.getText());
        item.setDefault(defaultYes.getValue());
    }

    private boolean validate() {
        int errors = 0;
        errors += markAsError(name, !Validation.validateTextBoxRequired(name));
        errors += markAsError(modules, !Validation.validateListBoxRequired(modules, new HTML(), wfmStrings.pleaseSelect()));
        errors += markAsError(content, !Validation.validateTextAreaRequired(content));
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getSMSTemplate(objectID, new AbstractAsyncCallback<SMSTemplateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SMSTemplateItem result) {
                LoadingPanel.loading(false);
                item = result;
                initPredefinedValues();
                fillFields();
            }
        });
    }

    protected void fillFields() {
        modules.setItems(item.getModules());
        if (item.getModuleID() != null) {
            modules.setSelected(item.getModuleID());
            initColumns(modules.getSelectedItem());
        }
        if (item.getName() != null) {
            name.setText(item.getName());
        }
        if (item.getContent() != null) {
            content.setText(item.getContent());
        }
        defaultYes.setValue(item.isDefault());
        defaultNo.setValue(!item.isDefault());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SMS_TEMPLATE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
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
