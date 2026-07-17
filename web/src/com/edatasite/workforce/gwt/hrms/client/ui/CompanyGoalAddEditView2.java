package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ValidityPeriodsPopup;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * User: Halim Kamolov
 * Date: 5/25/12
 * Time: 11:14 AM
 */
public class CompanyGoalAddEditView2 extends CustomForm2 implements HasLinksInterface, FormHasCustomFieldInterface, Constants, Colapse {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected Integer objectId;
    protected String viewName;
    protected String type;
    protected GoalItem item;
    protected NoteWidget noteWidget;
    protected GeneralFileUpload attachment;
    int errors = 0;
    private TextBox title;
    private DataListBox validityPeriod;
    private AdvancedInputGroup validityPeriodPanel;
    private TextArea2 description;
    private TextArea2 outcome;
    private DatePicker startDate;
    private DatePicker endDate;
    private DataListBox status;
    private FormHasCustomField customFieldUtil;
    private boolean saveAndClose = false;
    private final String companyGoalAddEditView = "company_goal_add_edit_view_";
    private HasLinks linkingUtil;
    private final String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), hrmsStrings.companyGoal());
    private VerticalPanel addLinkAndLinks;
    private LinkedHashMap<String, FormProperty> formProperty;
    private final Integer MAX_LENGTH = 1000;

    public CompanyGoalAddEditView2(String viewName, String description) {
        super(viewName, description);
    }

    public CompanyGoalAddEditView2(Integer objectId) {
        super("addcompanygoal", hrmsStrings.addCompanyGoal());
        this.viewName = hrmsStrings.companyGoal();
        this.type = CustomFormConstants.COMPANY_GOAL;
        if (objectId != null) {
            this.objectId = objectId;
            setDescription(hrmsStrings.editCompanyGoal());
        }
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COMPANY_GOAL_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });
        save.getElement().setId(companyGoalAddEditView + "save_and_close_button");

        MaterialLink saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
        saveAndNewButton.addClickHandler(event -> {
            saveAndClose = false;
            save();
        });
        saveAndNewButton.getElement().setId(companyGoalAddEditView + "save_and_new_button");

        splitButton.addItem(saveAndNewButton);
        addButton(splitButton);

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CompanyGoal, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formProperty = result.getFormPropertyMap();
                initialize();
            }
        });
//        initialize();
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onShellOk() {
        if (saveAndClose) {
            closeTab();
            if (objectId != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|summary/" + objectId, item.getTitle());
            }
        } else {
            if (objectId != null) {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|editcompanygoal/", item.getTitle());
            } else {
                reInit();
            }
        }
    }

    private void reInit() {
        objectId = null;
        initForm();
        initialize();
    }

    public void initialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VALIDITY_PERIOD_CHANGED, CompanyGoalAddEditView2.this, (sender, args) -> {
            final Integer validityPeriodId = Integer.parseInt(args.toString());
            AssessmentService.App.get().getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_GOAL, new AsyncCallback<ValidityPeriodItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ValidityPeriodItem[] validityPeriodItems) {
                    validityPeriod.clear();
                    validityPeriod.setItems(validityPeriodItems);
                    validityPeriod.setSelected(validityPeriodId);
                }
            });
        });

        LoadingPanel.loading(true);
        //company goal title
        title = new TextBox();
        title.getElement().setId(companyGoalAddEditView + "title");
        title.addStyleName(DEFAULT_WIDTH);
        //company goal description
        description = new TextArea2(1000, wfmStrings.description());
        description.getTextArea().getElement().setId(companyGoalAddEditView + "description");
//        description.addStyleName(DEFAULT_WIDTH);

        //company goal validity period
        validityPeriod = new DataListBox();
        validityPeriod.ensureDebugId(companyGoalAddEditView + "validityPeriod");
        validityPeriod.addStyleName(DEFAULT_WIDTH);
        validityPeriod.addValueChangeHandler(event -> {
            if (validityPeriod.isSomethingSelected()) {
                ValidityPeriodItem selectedItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
                Date date = selectedItem.getFromDate();
                date = DateUtil.addDays(date, 1);
                startDate.setDate(date);
            }
        });

        validityPeriodPanel = new AdvancedInputGroup(validityPeriod);
        validityPeriodPanel.setAppender("ficon--plus");
        validityPeriodPanel.appenderClickHandler(() -> {
            ValidityPeriodItem item = new ValidityPeriodItem();
            HashSet<SelectItem> list = new HashSet<>();
            list.add(new SelectItem(0, "", ValidityPeriodItem.VALIDITY_PERIOD_GOAL));
            item.setPeriodTypeItems(list);
            new ValidityPeriodsPopup(item);
        });

        //company goal out come
        outcome = new TextArea2(1000, wfmStrings.outcome());
        outcome.getTextArea().getElement().setId(companyGoalAddEditView + "outcome");
        outcome.addStyleName("file--CompanyGoalAddEditView2-outcome");
        //company goal status
        status = new DataListBox();
        status.getElement().setId(companyGoalAddEditView + "status");
        status.addStyleName(DEFAULT_WIDTH);
        //company goal start date
        startDate = new DatePicker();
        startDate.getElement().setId(companyGoalAddEditView + "fromDate");
        startDate.addStyleName(DEFAULT_WIDTH);
        //company goal to/end date
        endDate = new DatePicker();
        endDate.getElement().setId(companyGoalAddEditView + "toDate");
        endDate.addStyleName(DEFAULT_WIDTH);
        //company goal attachments
        attachment = new GeneralFileUpload(F_COMP_GOAL, objectId, objectId);
        attachment.getElement().setId(companyGoalAddEditView + "attachments");
        //company goal notes
        noteWidget = new NoteWidget(objectId, Constants.COMPANY_GOAL);
        noteWidget.getTextBox().getElement().setId(companyGoalAddEditView + "notes");

        addLinkAndLinks = new VerticalPanel();
        addLinkAndLinks.add(getLinkingUtil().getAddLink());
        addLinkAndLinks.add(getLinkingUtil().getLinksPanel());
        addLinkAndLinks.getElement().setId(companyGoalAddEditView + "addLinkAndLinks");

        LoadingPanel.loading(true);
        getCustomFieldUtil().drawCustomFields(this, objectId);
        addFieldsToForm();
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.details());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null) {
            addField(GOAL_TITLE, title, getTitle(formProperty.get(CustomFormConstants.GOAL_TITLE).isChanged() ? formProperty.get(CustomFormConstants.GOAL_TITLE).getTitle() : wfmStrings.title(), formProperty.get(CustomFormConstants.GOAL_TITLE).isRequired()));
            title.setEnabled(!formProperty.get(CustomFormConstants.GOAL_TITLE).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null) {
            description = new TextArea2(1000, formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isChanged() ? formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getTitle() : wfmStrings.description());
            description.setEnabled(!formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isDisabled());
        } else {
            description = new TextArea2(MAX_LENGTH, wfmStrings.description());
        }
        addField(CustomFormConstants.GOAL_DESCRIPTION, description, null);

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_OUTCOME) != null) {
            outcome = new TextArea2(1000, formProperty.get(CustomFormConstants.GOAL_OUTCOME).isChanged() ? formProperty.get(CustomFormConstants.GOAL_OUTCOME).getTitle() : wfmStrings.outcome());
            outcome.setEnabled(!formProperty.get(CustomFormConstants.GOAL_OUTCOME).isDisabled());
        } else {
            outcome = new TextArea2(MAX_LENGTH, wfmStrings.outcome());
        }
        addField(CustomFormConstants.GOAL_OUTCOME, outcome, null);

        GColumn column1 = new GColumn(GColumnEnum.COL_6, startDate);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, endDate);

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_START_DATE) != null) {
            addField(GOAL_START_DATE, new GRow(column1, column2), getTitle(formProperty.get(CustomFormConstants.GOAL_START_DATE).isChanged() ? formProperty.get(CustomFormConstants.GOAL_START_DATE).getTitle() : wfmStrings.period(), formProperty.get(CustomFormConstants.GOAL_START_DATE).isRequired()));
            startDate.setEnabled(!formProperty.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
            endDate.setEnabled(!formProperty.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_START_DATE, new GRow(column1, column2), getTitle(wfmStrings.period(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null) {
            addField(GOAL_STATUS, status, getTitle(formProperty.get(CustomFormConstants.GOAL_STATUS).isChanged() ? formProperty.get(CustomFormConstants.GOAL_STATUS).getTitle() : wfmStrings.status(), formProperty.get(CustomFormConstants.GOAL_STATUS).isRequired()));
            status.setEnabled(!formProperty.get(CustomFormConstants.GOAL_STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_STATUS, status, getTitle(wfmStrings.status(), true));
        }
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());
        addField(CustomFormConstants.ATTACHMENTS, attachment, null);
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addField(CustomFormConstants.CRM_NOTE, noteWidget, null);
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null) {
            addField(GOAL_VALIDITY_PERIOD, validityPeriodPanel, getTitle(formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isChanged() ? formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getTitle() : wfmStrings.validityPeriod(), formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isRequired()));
            validityPeriod.setEnabled(!formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriodPanel, getTitle(wfmStrings.validityPeriod()));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            addTitleField(CustomFormConstants.LINKS2, wfmStrings.links());
            showSection(CustomFormConstants.LINKS2);
            addField(CustomFormConstants.LINKS, addLinkAndLinks, null);
        } else {
            hideSection(CustomFormConstants.LINKS2);
        }
        show();
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().editCompanyGoal(objectId, new AbstractAsyncCallback<GoalItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingPanel.loading(false);
            }

            public void success(final GoalItem o) {
                LoadingPanel.loading(false);
                item = o;
                LoadingPanel.loading(false);
                fillFieldWithValue();
                if (objectId == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    public void fillFieldWithValue() {

        title.setText(item.getTitle());

        description.setText(item.getDescription());

        if (item.getFromDate() != null) {
            startDate.setDate(item.getFromDate().getNonConvertedDate());
        }

        if (item.getToDate() != null) {
            endDate.setDate(item.getToDate().getNonConvertedDate());
        }

        outcome.setText(item.getOutcome());

        status.setItems(Utils.sortSelectItemByName(item.getStatuss()));

        validityPeriod.setItems(item.getValidityPeriodItems());
        validityPeriod.setSelected(item.getValidityPeriodItem());
        if (item.getObjectId() == null && validityPeriod.isSomethingSelected()) {
            ValidityPeriodItem selectedItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
            startDate.setDate(selectedItem.getFromDate());
        }

        if (item.getStatusId() != null) {
            status.setSelected(item.getStatusId());
        } else {
            for (int i = 0; i < item.getStatuss().length; i++) {
                if (item.getStatuss()[i].getName().equals("Not Started")) {
                    status.setSelected(item.getStatuss()[i].getId());
                }
            }
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());

        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            getLinkingUtil().getTaggingView().setFromName(item.getTitle());
            getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
            getLinkingUtil().drawLinks();
        }
    }

    protected void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        enableButton(false);
        item = setValuesToRPC(item);
        item.setRelations(getLinkingUtil().getTaggingView().getSelectedRelations());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        HrmsService.App.get().saveCompanyGoal(item, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final Integer result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(successMessage, Info.Type.INFO);
                onShellOk();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COMPANY_GOAL_ADD, result, CompanyGoalAddEditView2.this);
            }
        });
    }

    private GoalItem setValuesToRPC(GoalItem item) {
        if (objectId != null) {
            item.setObjectId(objectId);
        }
        item.setTitle(title.getText());
        item.setDescription(description.getText());
        item.setOutcome(outcome.getText());
        item.setFromDate(Utils.getStartDateNC(startDate.getDate()));
        item.setToDate(Utils.getEndDateNC(endDate.getDate()));
        SelectItem selectedItem = status.getSelectedItem();
        if (selectedItem != null) {
            item.setStatusId(selectedItem.getId());
            item.setStatus(selectedItem.getName());
        }

        if (validityPeriod.getSelectedItem() != null) {
            ValidityPeriodItem validityPeriodItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
            validityPeriodItem.setId(validityPeriodItem.getId());
            validityPeriodItem.setName(validityPeriodItem.getName());
            validityPeriodItem.setFromDate(validityPeriodItem.getFromDate());
            validityPeriodItem.setToDate(validityPeriodItem.getToDate());
            validityPeriodItem.setDescription(validityPeriodItem.getDescription());
            item.setValidityPeriodItem(validityPeriodItem);
        }

        item.setAttachments(attachment.getAttachedFiles());

        item.setNotes(noteWidget.getNewNotesToSave());

        return item;
    }

    private boolean validate() {
        clearErrorStyle();
        errors = 0;

        StringBuilder message = new StringBuilder(wfmStrings.sureEnteredAllData());

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null && formProperty.get(CustomFormConstants.GOAL_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_TITLE, title, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_TITLE).isChanged() ?
                    formProperty.get(CustomFormConstants.GOAL_TITLE).getTitle() : wfmStrings.title(), title, formProperty.get(CustomFormConstants.GOAL_TITLE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null && formProperty.get(CustomFormConstants.GOAL_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_STATUS, status, !Validation.validateListBoxRequired(status));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriodPanel, !Validation.validateListBoxRequired(validityPeriod));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_START_DATE) != null && formProperty.get(CustomFormConstants.GOAL_START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, startDate, startDate.getDate() == null);
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, endDate, endDate.getDate() == null);
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_DESCRIPTION, description, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isChanged() ? formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getTitle() : wfmStrings.description(), description.getTextArea(), formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_OUTCOME) != null && formProperty.get(CustomFormConstants.GOAL_OUTCOME).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_OUTCOME, outcome, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_OUTCOME).isChanged() ? formProperty.get(CustomFormConstants.GOAL_OUTCOME).getTitle() : wfmStrings.outcome(), outcome.getTextArea(), formProperty.get(CustomFormConstants.GOAL_OUTCOME).getMinChar()));
        }

        if (validityPeriod.getSelectedItem() != null) {
            boolean dateValidation = false;
            ValidityPeriodItem validityPeriodItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
            if (markAsError(startDate, !Validation.validateDateEqualOrAfter(validityPeriodItem.getFromDate(), startDate.getDate(), true)) == 1) {
                message.append("<br>* " + wfmStrings.goalDatesValidate());
                dateValidation = true;
                errors++;
            }
            if (markAsError(endDate, !Validation.validateDateOrder(endDate.getDate(), validityPeriodItem.getToDate())) == 1) {
                errors++;
                if (!dateValidation) {
                    message.append("<br>* " + wfmStrings.goalDatesValidate());
                    dateValidation = true;
                }
            }
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(message.toString(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void setDefaultValuesByFormProperty() {
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null && formProperty.get(CustomFormConstants.GOAL_TITLE).getDefaultValue() != null) {
            title.setText(formProperty.get(CustomFormConstants.GOAL_TITLE).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null && formProperty.get(CustomFormConstants.GOAL_STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_STATUS).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_STATUS).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getDefaultValue() != null) {
            validityPeriod.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getDefaultValue() != null) {
            description.setText(formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_OUTCOME) != null && formProperty.get(CustomFormConstants.GOAL_OUTCOME).getDefaultValue() != null) {
            outcome.setText(formProperty.get(CustomFormConstants.GOAL_OUTCOME).getDefaultValue());
        }
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(CompanyGoalAddEditView2.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                protected Integer getRelationID() {
                    return objectId;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_COMPANY_GOAL;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getTitle() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public String getIconStyle() {
        return "hrms hrms-edit";
    }

    @Override
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