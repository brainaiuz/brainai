package com.edatasite.workforce.gwt.core.client.form.formbuild;

import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.*;

public class CustomFormQuickAddForm extends Composite {
    interface CustomFormQuickAddFormUiBinder extends UiBinder<Widget, CustomFormQuickAddForm> {
    }

    private static final CustomFormQuickAddForm.CustomFormQuickAddFormUiBinder ourUiBinder = GWT.create(CustomFormQuickAddForm.CustomFormQuickAddFormUiBinder.class);


    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;

    @UiField
    Label pluralLabel;
    @UiField
    TextBox plural;

    @UiField
    Label shortLabel;
    @UiField
    TextBox shortName;

    @UiField
    HTMLPanel moduleDiv;
    @UiField
    Label moduleLabel;
    @UiField
    DataListBox module;
    @UiField
    Label sectionLabel;
    @UiField
    DataListBox section;
    @UiField
    Label typeLabel;
    @UiField
    DataListBox type;
    @UiField
    FlowPanel convertField;
    @UiField
    FlowPanel quizForm;
    @UiField
    FlowPanel anonymousForm;
    @UiField
    Div convertFormPanel;
    @UiField
    FlowPanel quotaPanel;
    @UiField
    Label quotaPerUserLabel;
    @UiField
    TextBox quotaPerUser;
    @UiField
    Label quotaPerFormLabel;
    @UiField
    TextBox quotaPerForm;
    @UiField
    FlowPanel timerPanel;
    @UiField
    Label welcomeMessageLabel;
    @UiField
    TextArea welcomeMessage;
    @UiField
    Label endTimeMessageLabel;
    @UiField
    TextArea endTimeMessage;
    @UiField
    Div timerFormPanel;
    @UiField
    FlowPanel availabilityPanel;
    @UiField
    Label filterBy;
    @UiField
    DataListBox filterList;
    @UiField
    Label condition;
    @UiField
    DataListBox conditionList;
    @UiField
    Div conditionWidgetFormPanel;

    private ICommand command;
    private final Integer objectId;
    private CustomFormItem item;
    private String moduleCode;
    private KpiCheckBox convert;
    private MatrixTable convertForms;
    private KpiCheckBox quota;
    private KpiCheckBox quiz;
    private KpiCheckBox anonymous;
    private KpiCheckBox timer;
    private KpiTimePicker timePicker;
    private KpiCheckBox availability;
    private Div dateAndTimePanel;
    private DatePicker startDate;
    private KpiTimePicker startTime;
    private DatePicker endDate;
    private KpiTimePicker endTime;
    private GRow customDateRow;
    private CustomFormFilterTable filterTable;
    private final boolean copy;

    ArrayList<SelectItem> convertFormItems = new ArrayList<>();

    public CustomFormQuickAddForm(Integer objectId, String moduleCode, boolean copy) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.objectId = objectId;
        this.moduleCode = moduleCode;
        this.copy = copy;
        initialize();
        addListener();
    }

    private void timerPanel() {
        timer = new KpiCheckBox(wfmStrings.timer());
        timerPanel.add(timer);
        timer.addValueChangeHandler(click -> showTimerSettings(timer.getValue(), null));
        showTimerSettings(false, null);

        welcomeMessageLabel.setText(wfmStrings.welcomeMessage() + " " + (200 - welcomeMessage.getText().length()));
        endTimeMessageLabel.setText(wfmStrings.confirmationMessage() + " " + (200 - endTimeMessage.getText().length()));
    }

    private void initialize() {
        nameLabel.setText(wfmStrings.name());
        pluralLabel.setText(wfmStrings.plural());
        shortLabel.setText(wfmStrings.shortName());

        name.setMaxLength(30);
        plural.setMaxLength(30);
        shortName.setMaxLength(30);

        moduleLabel.setText(wfmStrings.apps());
        sectionLabel.setText(wfmStrings.section());
        section.setWithoutNullLabel(true);

        typeLabel.setText(wfmStrings.type());
        type.setWithoutNullLabel(true);
        type.setItems(getFormTypes());
        if (!type.getSelectedItemText().equals("PAGE")) {
            timerPanel();
        } else {
            showTimerSettings(false, null);
        }
        type.addValueChangeHandler(event -> {
            if (type.getSelectedItemText().equals("Listing")) {
                timerPanel.clear();
                timerPanel();
            } else {
                timerPanel.clear();
            }
        });

        convert = new KpiCheckBox(wfmStrings.convert());
        convertField.add(convert);
        convert.addClickHandler(click -> {
            convertFormPanel.setVisible(convert.getValue());
        });
        module.addValueChangeHandler(click -> {

            if (module.getSelectedItem() != null && (ModuleEnum.CRM.getCode().equals(module.getSelectedItem().getDescription()) ||
                    ModuleEnum.HRMS.getCode().equals(module.getSelectedItem().getDescription()) ||
                    ModuleEnum.ACCOUNTING.getCode().equals(module.getSelectedItem().getDescription()) ||
                    ModuleEnum.PAYROLL.getCode().equals(module.getSelectedItem().getDescription()) ||
                    ModuleEnum.PM.getCode().equals(module.getSelectedItem().getDescription()))) {
                section.setItems(getSectionsByModule());
                if (getSectionsByModule() != null && getSectionsByModule().length > 0) {
                    section.setSelected(getSectionsByModule()[0]);
                }
                sectionLabel.setVisible(true);
                section.setVisible(true);
            } else {
                sectionLabel.setVisible(false);
                section.setVisible(false);
            }

        });

        convertForms = new MatrixTable(3);
        convertFormPanel.add(convertForms);
        convertFormItems.add(new SelectItem(null, Property.get(Constants.Opportunities, wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), RelationItem.TYPE_SALEQUOTE));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), RelationItem.TYPE_PURCHASE_ORDER));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), RelationItem.TYPE_PURCHASE_INVOICE));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.EMLOYEE_LIST, wfmStrings.employee()), RelationItem.TYPE_EMPLOYEE));
        convertFormItems.add(new SelectItem(null, Property.get(Constants.VACANCY, wfmStrings.vacancy()), RelationItem.TYPE_VACANCY));

        convertForms.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, convertFormItems.toArray(new SelectItem[]{})), true);

        quota = new KpiCheckBox(wfmStrings.quotaSettings());
        quotaPanel.add(quota);
        quota.addValueChangeHandler(click -> showHideQuotaSettings(quota.getValue()));
        showHideQuotaSettings(false);

        quotaPerUserLabel.setText(wfmStrings.quotaPerUser());
        quotaPerFormLabel.setText(wfmStrings.quotaPerForm());
        Validation.addNumericKeyboardListener(quotaPerUser, 0);
        Validation.addNumericKeyboardListener(quotaPerForm, 0);


        quiz = new KpiCheckBox(wfmStrings.quiz());
        quizForm.add(quiz);
        anonymous = new KpiCheckBox(wfmStrings.anonymous());
        anonymous.setEnabled(false); //formalarni anonymous qilish databasedan boshqarishga kelishildi ! ui uchun enabled qilinmasin !
        anonymousForm.add(anonymous);
        //// Custom Date widgets
        filterBy.setText(wfmStrings.dependOn());
        condition.setText(wfmStrings.condition());
        startDate = new DatePicker(new Date());
        startDate.setWidth("65%");
        startTime = new KpiTimePicker(true);
        startTime.setWidth("30%");
        endDate = new DatePicker(new Date());
        endDate.setWidth("65%");
        endTime = new KpiTimePicker(true);
        endTime.setWidth("30%");
        customDateRow = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.startDate(), new InputGroup(startDate, startTime))),
                new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.endDate(), new InputGroup(endDate, endTime))));
        customDateRow.setVisible(false);

        /// filterTable Widgets
        filterTable = new CustomFormFilterTable(null);
        filterTable.setVisible(false);

        conditionWidgetFormPanel.add(customDateRow);
        conditionWidgetFormPanel.add(filterTable);

        setFilterAndConditionListBoxItems();

//        KpiCustomToolTip toolTip = new KpiCustomToolTip(availabilityMessages.infoAboutCustomFormAvaibility("https://dev.kpi.com/","Click here"));
        availability = new KpiCheckBox(wfmStrings.availableDays());
        Span toolTip = createInfoButton();
//        availability.setWidth("33%");availability
        availabilityPanel.add(new InputGroup(availability, toolTip));

        showAvaibilityForm(false);
        availability.addClickHandler(click -> showAvaibilityForm(availability.getValue()));
    }

    private void setFilterAndConditionListBoxItems() {
        filterList.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.hireDate(), CustomFormConstants.HIRE_DATE)});
        conditionList.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.custom(), CustomFormConstants.CUSTOM)});

        filterList.addValueChangeHandler(event -> {
            if (CustomFormConstants.HIRE_DATE.equals(event.getValue().getDescription())) {
                conditionList.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.custom(), CustomFormConstants.CUSTOM), new SelectItem(2, wfmStrings.ageInDays(), CustomFormConstants.AGE_IN_DAYS)});
            } else {
                conditionList.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.custom(), CustomFormConstants.CUSTOM)});
            }
            if (event.getValue().getId() == -1) {
                customDateRow.setVisible(false);
                filterTable.setVisible(false);
                conditionList.setSelectedIndex(-1);
            }
        });

        conditionList.addValueChangeHandler(event -> {
            conditionWidgetFormPanel.setVisible(true);
            if (CustomFormConstants.CUSTOM.equals(event.getValue().getDescription())) {
                customDateRow.setVisible(true);
                filterTable.setVisible(false);
            } else if (CustomFormConstants.AGE_IN_DAYS.equals(event.getValue().getDescription())) {
                customDateRow.setVisible(false);
                filterTable.setVisible(true);
            } else {
                customDateRow.setVisible(false);
                filterTable.setVisible(false);
            }
        });
    }

    private void showAvaibilityForm(Boolean value) {
        filterBy.setVisible(value);
        filterList.setVisible(value);
        condition.setVisible(value);
        conditionList.setVisible(value);
        if (!value) {
            clearAvaibilityWidgets();
        }
    }

    private void clearAvaibilityWidgets() {
        customDateRow.setVisible(false);
        filterTable.setVisible(false);
        conditionList.setSelectedIndex(-1);
        filterList.setSelectedIndex(-1);
//        item.setRuleItem(null);
    }

    public void getData() {
        LoadingPanel.loading(true, panel);
        CommonService.App.get().getCustomForm(objectId, new AbstractAsyncCallback<CustomFormItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(CustomFormItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                if (objectId != null) {
                    moduleCode = item.getContext();
                }
                fillFields();
            }
        });
    }

    private void fillFields() {
        if (!copy) {
            if (item.getObjectId() != null) {

                nameLabel.setText("");
                pluralLabel.setText("");
                shortLabel.setText("");

                // ===== NAME =====
                {
                    MaterialIcon plusIcon = new MaterialIcon();
                    plusIcon.setStyleName("ficon--plus-circle");

                    MaterialLink link = new MaterialLink(wfmStrings.name());
                    link.addStyleName("btn-small btn--default mb-1 mt-5");
                    link.add(plusIcon);

                    Span span = new Span();
                    span.add(link);

                    link.addClickHandler(e -> {
                        LocalizationCFModal modal =
                                new LocalizationCFModal(item.getlName(), LocalizationTypeEnum.FORM);
                        modal.center();
                    });

                    nameLabel.add(span);
                }

                // ===== PLURAL =====
                {
                    MaterialIcon plusIcon = new MaterialIcon();
                    plusIcon.setStyleName("ficon--plus-circle");

                    MaterialLink link = new MaterialLink(wfmStrings.plural());
                    link.addStyleName("btn-small btn--default mb-1");
                    link.add(plusIcon);

                    Span span = new Span();
                    span.add(link);

                    link.addClickHandler(e -> {
                        LocalizationCFModal modal =
                                new LocalizationCFModal(item.getlPlural(), LocalizationTypeEnum.FORM);
                        modal.center();
                    });

                    pluralLabel.add(span);
                }

                // ===== SHORT NAME =====
                {
                    MaterialIcon plusIcon = new MaterialIcon();
                    plusIcon.setStyleName("ficon--plus-circle");

                    MaterialLink link = new MaterialLink(wfmStrings.shortName());
                    link.addStyleName("btn-small btn--default mb-1");
                    link.add(plusIcon);

                    Span span = new Span();
                    span.add(link);

                    link.addClickHandler(e -> {
                        LocalizationCFModal modal =
                                new LocalizationCFModal(item.getlShort(), LocalizationTypeEnum.FORM);
                        modal.center();
                    });

                    shortLabel.add(span);
                }
            }


            name.setText(item.getName());
            plural.setText(item.getPlural());
            shortName.setText(item.getShortName());
            if (item.getConvertItems() != null && item.getConvertItems().length > 0) {
                convert.setValue(true);
                convertFormPanel.setVisible(true);
                for (ConvertItem convertItem : item.getConvertItems()) {
                    for (SelectItem item : convertFormItems) {
                        if (convertItem != null && item != null && convertItem.getCode().equals(item.getDescription())) {
                            item.setSelected(true);
                            item.setEntityId(convertItem.getEntityId());
                            break;
                        }
                    }
                }
                convertForms.clear();
                convertForms.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, convertFormItems.toArray(new SelectItem[]{})), true);
            }
            quotaPerUser.setText(item.getQuotaPerUser() != null ? item.getQuotaPerUser().toString() : "");
            quotaPerForm.setText(item.getQuotaPerForm() != null ? item.getQuotaPerForm().toString() : "");
            if (item.getQuotaPerUser() != null || item.getQuotaPerForm() != null) {
                quota.setValue(true);
                showHideQuotaSettings(true);
            }

            if (item.getRuleItem() != null) {
                setAvailibilityValues(item.getRuleItem());
            }

            quiz.setValue(item.isQuizForm());
            anonymous.setValue(item.isAnonymousForm());
            if (item.getTimer() != null) {
                timer.setValue(true);
                timePicker.setValue(item.getTimer());
                showTimerSettings(true, item.getTimer());
            }
            if (item.getWelcomeMessage() != null) {
                welcomeMessage.setText(item.getWelcomeMessage());
                if (welcomeMessageLabel != null) {
                    welcomeMessageLabel.setText(wfmStrings.welcomeMessage() + " " + (200 - welcomeMessage.getText().length()));
                }
            }
            if (item.getEndTimeMessage() != null) {
                endTimeMessage.setText(item.getEndTimeMessage());
                if (endTimeMessageLabel != null) {
                    endTimeMessageLabel.setText(wfmStrings.confirmationMessage() + " " + (200 - endTimeMessage.getText().length()));
                }
            }
        }
        module.setItems(getModules());
        if (item.isCustom() || item.getObjectId() == null) {
            item.setCustom(true);
        }
        if (item.getModule() != null) {
            module.setSelectedByCode(item.getModule().getCode());
        } else {
            module.setSelectedByCode(moduleCode);
        }
        if (item.getType() != null) {
            if (item.getType().equals("PAGE")) {
                timerPanel.clear();
            }
            type.setSelectedByDescription(item.getType());
            type.setEnabled(false);
        }
        if (module.getSelectedItem() != null && (ModuleEnum.CRM.getCode().equals(module.getSelectedItem().getDescription()) ||
                ModuleEnum.ACCOUNTING.getCode().equals(module.getSelectedItem().getDescription()) ||
                ModuleEnum.HRMS.getCode().equals(module.getSelectedItem().getDescription()) ||
                ModuleEnum.PAYROLL.getCode().equals(module.getSelectedItem().getDescription()) ||
                ModuleEnum.TRAINING_CENTER.getCode().equals(module.getSelectedItem().getDescription()) ||
                ModuleEnum.PM.getCode().equals(module.getSelectedItem().getDescription()))) {
            section.setItems(getSectionsByModule());
            if (item.getContainer() != null) {
                section.setSelected(item.getContainer());
            } else {
                section.setSelected(getSectionsByModule() != null && getSectionsByModule().length > 0 ? getSectionsByModule()[0] : null);
            }
            sectionLabel.setVisible(true);
            section.setVisible(true);
        }
    }

    private void setAvailibilityValues(CustomFormRuleItem ruleItem) {
        availability.setValue(true);
        conditionWidgetFormPanel.setVisible(true);
        showAvaibilityForm(true);
        if (CustomFormConstants.AGE_IN_DAYS.equals(ruleItem.getConditionType())) {
            conditionList.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.custom(), CustomFormConstants.CUSTOM), new SelectItem(2, wfmStrings.ageInDays(), CustomFormConstants.AGE_IN_DAYS)});
            filterList.setSelectedByDescription(CustomFormConstants.HIRE_DATE);
            conditionList.setSelectedByDescription(CustomFormConstants.AGE_IN_DAYS);
            filterTable.setRuleITem(ruleItem);
            filterTable.setVisible(true);
            customDateRow.setVisible(false);
        } else if (CustomFormConstants.CUSTOM.equals(ruleItem.getConditionType())) {
            filterList.setSelectedIndex(-1);
            conditionList.setSelectedByDescription(CustomFormConstants.CUSTOM);
            Date sDate = new Date(ruleItem.getStartDate());
            startDate.setDate(sDate);
            int[] sArr = new int[]{sDate.getHours(), sDate.getMinutes()};
            startTime.setValue(sArr);
            Date eDate = new Date(ruleItem.getEndDate());
            endDate.setDate(eDate);
            int[] eArr = new int[]{eDate.getHours(), eDate.getMinutes()};
            endTime.setValue(eArr);
            filterTable.setVisible(false);
            customDateRow.setVisible(true);
        }
    }

    private SelectItem[] getModules() {
        List<SelectItem> modules = new ArrayList<>();
        modules.add(new SelectItem(1, localizeModuleName(ModuleEnum.ACCOUNTING), ModuleEnum.ACCOUNTING.getCode()));
        modules.add(new SelectItem(2, localizeModuleName(ModuleEnum.CRM), ModuleEnum.CRM.getCode()));
        modules.add(new SelectItem(3, localizeModuleName(ModuleEnum.HRMS), ModuleEnum.HRMS.getCode()));
        modules.add(new SelectItem(4, localizeModuleName(ModuleEnum.PM), ModuleEnum.PM.getCode()));
//        modules.add(new SelectItem(5, localizeModuleName(ModuleEnum.DOCUMENTS), ModuleEnum.DOCUMENTS.getCode()));
        modules.add(new SelectItem(6, localizeModuleName(ModuleEnum.PAYROLL), ModuleEnum.PAYROLL.getCode()));
        modules.add(new SelectItem(7, localizeModuleName(ModuleEnum.SETTINGS), ModuleEnum.SETTINGS.getCode()));
        /*if (objectId != null) {
            modules.add(new SelectItem(5, localizeModuleName(ModuleEnum.MYWORKSPACE), ModuleEnum.MYWORKSPACE.getCode()));
        }*/

        return modules.toArray(new SelectItem[]{});
    }

    private SelectItem[] getFormTypes() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(1, wfmStrings.showInListing(), Constants.LISTING));
        types.add(new SelectItem(2, wfmStrings.page(), Constants.PAGE));

        return types.toArray(new SelectItem[]{});
    }

    private SelectItem[] getSectionsByModule() {
        List<SelectItem> sections = new ArrayList<>();
        if (module != null && module.getSelectedItem() != null) {
            sections = item.getSection().get(module.getSelectedItem().getDescription());

            if (sections != null && sections.size() > 0) {
                return sections.toArray(new SelectItem[]{});
            }
        }
        return null;
    }

    private String localizeModuleName(ModuleEnum moduleEnum) {
        if (moduleEnum != null) {
            switch (moduleEnum) {
                case PM:
                    return wfmStrings.projects();
                case HRMS:
                    return wfmStrings.hrms();
                case ACCOUNTING:
                    return wfmStrings.accounts();
                case CRM:
                    return wfmStrings.crm();
                case DOCUMENTS:
                    return wfmStrings.documents();
                case PAYROLL:
                    return wfmStrings.payroll();
            }
        }
        return "";
    }

    public void save() {
        LoadingPanel.loading(true, panel);

        setValuesToRPC();
        if (availability.getValue() && item.getRuleItem() == null && conditionList.getSelectedIndex() != -1) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            LoadingPanel.loading(false, panel);
            return;
        }
        CommonService.App.get().saveCustomForm(item, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.warn(wfmStrings.withTheSameNameAlreadyExist(), Info.Position.TOP_RIGHT);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_SAVE_BUTTON_ENABLE, null, CustomFormQuickAddForm.this);
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(String result) {
                LoadingPanel.loading(false, panel);
                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(plural)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(shortName)) {
            errors++;
        }
        if (item.isCustom() && !Validation.validateDataListBoxRequired(module)) {
            errors++;
        }
        if (timer.getValue()) {
            if (!Validation.validateKpiTimePickerRequired(timePicker)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(welcomeMessage)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(endTimeMessage)) {
                errors++;
            }
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    private void setValuesToRPC() {
        item.setName(name.getText());
        item.setPlural(plural.getText());
        item.setShortName(shortName.getText());
        item.setContext(moduleCode);
        item.setType(type.getSelectedItem() != null ? type.getSelectedItem().getDescription() : Constants.LISTING);
        item.setCopy(copy);
        if (module.getSelectedItem() != null) {
            item.setModule(ModuleEnum.getModule(module.getSelectedItem().getDescription()));
        }
        if (section.getSelectedItem() != null) {
            item.setContainer(section.getSelectedItem());
        }
        if (convert != null && convert.getValue() && convertForms.getValuesMap() != null && convertForms.getValuesMap().size() > 0) {
            LinkedList<ConvertItem> convertItems = new LinkedList<>();
            SelectItem[] items = convertForms.getValuesMap().keySet().toArray(new SelectItem[]{});
            for (SelectItem item : items) {
                if (item != null && item.isSelected()) {
                    ConvertItem convertItem = new ConvertItem();
                    convertItem.setCode(item.getDescription());
                    convertItem.setName(item.getName());
                    convertItem.setEntityId(item.getEntityId());
                    convertItems.add(convertItem);
                }

            }
            item.setConvertItems(convertItems.toArray(new ConvertItem[]{}));
        } else {
            item.setConvertItems(null);
        }
        if (quotaPerUser.getText() != null && !"".equals(quotaPerUser.getText())) {
            item.setQuotaPerUser(Integer.parseInt(quotaPerUser.getText()));
        }
        if (quotaPerForm.getText() != null && !"".equals(quotaPerForm.getText())) {
            item.setQuotaPerForm(Integer.parseInt(quotaPerForm.getText()));
        }

        if (availability.getValue()) {
            CustomFormRuleItem ruleItem;
            if (CustomFormConstants.CUSTOM.equals(conditionList.getSelectedItem(true).getDescription())) {
                ruleItem = new CustomFormRuleItem();
                ruleItem.setConditionType(CustomFormConstants.CUSTOM);
                int[] startTimeArr = startTime.getValue();
                startDate.getDate().setHours(startTimeArr[0]);
                startDate.getDate().setMinutes(startTimeArr[1]);
                startDate.getDate().setSeconds(0);
                int[] endTimeArr = endTime.getValue();
                endDate.getDate().setHours(endTimeArr[0]);
                endDate.getDate().setMinutes(endTimeArr[1]);
                endDate.getDate().setSeconds(0);
                ruleItem.setStartDate(startDate.getDate().getTime());
                ruleItem.setEndDate(endDate.getDate().getTime());
                GWT.log("Date and time (Start date ) " + startDate.getDate().toString());
                GWT.log("Date and time (end date ) " + endDate.getDate().toString());
                if (startDate.getDate().getTime() >= endDate.getDate().getTime()) {
                    startDate.setStyleName(Constants.ERROR_FORM_STYLE);
                    endDate.setStyleName(Constants.ERROR_FORM_STYLE);
                    startTime.setStyleName(Constants.ERROR_FORM_STYLE);
                    endTime.setStyleName(Constants.ERROR_FORM_STYLE);
                    ruleItem = null;
                }
                item.setRuleItem(ruleItem);
            } else if (CustomFormConstants.AGE_IN_DAYS.equals(conditionList.getSelectedItem(true).getDescription())) {
                ruleItem = filterTable.getRuleITem();
                if (ruleItem != null) {
                    ruleItem.setConditionType(CustomFormConstants.AGE_IN_DAYS);
                }
                item.setRuleItem(ruleItem);
            }
        } else {
            item.setRuleItem(null);
        }
        item.setQuizForm(quiz.getValue());
        if (timer.getValue()) {
            if (timePicker.getValue() != null && !Arrays.equals(timePicker.getValue(), new int[]{0, 0})) {
                item.setTimer(timePicker.getValue());
            }
            if (welcomeMessage.getText() != null && !"".equals(welcomeMessage.getText())) {
                item.setWelcomeMessage(welcomeMessage.getText());
            }
            if (endTimeMessage.getText() != null && !"".equals(endTimeMessage.getText())) {
                item.setEndTimeMessage(endTimeMessage.getText());
            }
        } else {
            item.setTimer(null);
            item.setWelcomeMessage(null);
            item.setEndTimeMessage(null);
        }
    }

    public void clearForm() {
        name.setText("");
        module.clearSelected();
        module.clear();
        removeErrorStyle();
    }

    private void removeErrorStyle() {
        name.removeStyleName(Constants.ERROR_FORM_STYLE);
        module.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    private void showHideQuotaSettings(boolean b) {
        quotaPerFormLabel.setVisible(b);
        quotaPerForm.setVisible(b);
        quotaPerUserLabel.setVisible(b);
        quotaPerUser.setVisible(b);
    }

    private void addListener() {
        welcomeMessage.addKeyUpHandler(keyUpEvent -> welcomeMessageLabel.setText(wfmStrings.welcomeMessage() + " " + (200 - welcomeMessage.getText().length())));

        welcomeMessage.addKeyPressHandler(keyPressEvent -> {
            if (welcomeMessage.getText().length() >= 200) {
                keyPressEvent.preventDefault();
            }
        });

        endTimeMessage.addKeyUpHandler(keyUpEvent -> endTimeMessageLabel.setText(wfmStrings.confirmationMessage() + " " + (200 - endTimeMessage.getText().length())));

        endTimeMessage.addKeyPressHandler(keyPressEvent -> {
            if (endTimeMessage.getText().length() >= 200) {
                keyPressEvent.preventDefault();
            }
        });
    }

    private void showTimerFormPanel(boolean b, int[] value) {
        int[] initialValue = {0, 0};
        timerFormPanel.addStyleName("input-group");
        timerFormPanel.clear();
        timePicker = new KpiTimePicker(true);
        timePicker.setStyleName("form-control disabled");
        timePicker.setWidth("25%");
        timePicker.setTextAlign(TextAlign.CENTER);
        timePicker.setHoverable(true);
        timePicker.setValue(value != null ? value : initialValue);
        timerFormPanel.add(timePicker);
        timerFormPanel.setVisible(b);
    }

    private void showTimerSettings(boolean b, int[] value) {
        showTimerFormPanel(b, value);
        welcomeMessageLabel.setVisible(b);
        welcomeMessage.setVisible(b);
        endTimeMessageLabel.setVisible(b);
        endTimeMessage.setVisible(b);
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }

    private Span createInfoButton() {
//        Span adAsDashboardTitle = new Span(wfmStrings.addAsDashboard());
        Span tooltipWrapper = new Span();

        setTooltipClass(tooltipWrapper);
//        Window.addResizeHandler(e -> setTooltipClass(tooltipWrapper));

        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);
        String activation = "infoDropDown";
        iconLink.setActivates(activation);

        MaterialDropDown dropDown = new MaterialDropDown(activation);
        dropDown.addStyleName("dropdown-content dropdown-content-tooltip tooltip-long-text");
        dropDown.getElement().setInnerHTML("<li>" + "<span>" + wfmMessages.infoAboutCustomFormAvaibility() + "</span></li>");
        dropDown.setHover(true);

        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(dropDown);

        return tooltipWrapper;

    }

    private void setTooltipClass(Span tooltipWrapper) {
        int frameWidth = JQuery.$(".frame__content__body.scroll-content").outerWidth();
        if (frameWidth < 960) {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--right");
        } else {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--left");
        }
    }
}
