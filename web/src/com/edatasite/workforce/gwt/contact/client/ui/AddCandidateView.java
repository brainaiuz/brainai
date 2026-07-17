package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.CandidateStatusHistoryGrid;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.SpokenLanguagesWidget;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectVacancyLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.CandidatePercentageStageModal;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldEntityLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_NUMBER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.TYPE_ENTITY_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_CURRENCY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DROPDOWN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_ITEM_WITH_DESCRIPTION;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_MULTI_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_PERCENTAGE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX_EMAIL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_URL;


/**
 * User: hayot
 * Date: 7/3/12
 * Time: 10:30 AM
 */
public class AddCandidateView extends AddContactView implements HasLinksInterface {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private DatePicker createdDate;
    private TextBox currentEmployer;
    private TextBox expectedSalary;
    private Numbering number;
    private HasLinks linkingUtil;
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private TextArea2 skills;
    private DataListBox source;
    private DataListBox status;
    private MultiSelectVacancyLookUp vacanciesLookUp;
    private TextBox workExperience;
    private DataListBox workExperienceMonthYearBox;
    private CRMLookUp project;
    private EditableTable allowancesTable;
    private SpokenLanguagesWidget languagesWidget;

    public DataListBox martialStatus;
    private KpiRadioButton male;
    private KpiRadioButton female;
    private final String test_code_ID_name = "employee_";
    private FlexTable gender;
    protected ProfileImage profileImage;
    protected CandidateStatusHistoryGrid candidateStatusHistoryGrid;
    private LocationLookUpWithCode preferredLocation;
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private boolean isStatusChanged = false;

    private boolean isItemTableRequared = false;

    private CRMLookUp timeslotLookUp;
    private TextBox startSalary;
    private TextBox passportNumber;
    private PositionLookUp position;
    private DepartmentLookUp department;
    private Div locationContainer;
    private Div departmentContainer;
    private Div positionContainer;
    private Integer departmentId;

    public AddCandidateView(String name, String description) {
        super(name, description);
        setContactType(ContactListItem.CANDIDATE);
    }

    public AddCandidateView() {
        super("addCandidate", hrmsStrings.addCandidate());
        setContactType(ContactListItem.CANDIDATE);
        isAddView = true;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void initialize() {
        super.initialize();
        String candidate_add_view = "candidate_add_view_";
        profileImage = new ProfileImage(objectId, LayoutRPC.CONTACT_FORM);
        //languages widget
        languagesWidget = new SpokenLanguagesWidget(null);
        languagesWidget.addStyleName(DEFAULT_WIDTH);
        languagesWidget.getElement().setId(candidate_add_view + "language");
        //martial status
        martialStatus = new DataListBox();
        martialStatus.ensureDebugId("employee_maritalStatus");
        martialStatus.addStyleName(DEFAULT_WIDTH);
        martialStatus.setAllowFirstItem(true);
        martialStatus.addStyleName(test_code_ID_name + "martial_status");

        male = new KpiRadioButton("gender", wfmStrings.male());
        male.addStyleName(test_code_ID_name + "gender_male");
        //gender: female
        female = new KpiRadioButton("gender", wfmStrings.female());
        female.addStyleName(test_code_ID_name + "gender_female");

        gender = new FlexTable();
        gender.addStyleName(DEFAULT_WIDTH + " " + "options-row");
        gender.setWidget(0, 0, male);
        gender.setWidget(0, 1, female);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null) {
            male.setEnabled(!formPropertyMap.get(CustomFormConstants.GENDER).isDisabled());
            female.setEnabled(!formPropertyMap.get(CustomFormConstants.GENDER).isDisabled());
        }
        //numbering
        number = new Numbering();
        number.addStyleName(DEFAULT_WIDTH);
        number.getElement().setId(candidate_add_view + "number");
        //created date
        createdDate = new DatePicker();
        createdDate.addStyleName(DEFAULT_WIDTH);
        createdDate.getElement().setId(candidate_add_view + "created_date");
        //source
        source = new DataListBox();
        source.addStyleName(DEFAULT_WIDTH);
        source.getElement().setId(candidate_add_view + "source");
        //work experience
        workExperience = new TextBox();
        workExperience.setWidth("80px");
        workExperience.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        workExperience.setMaxLength(2);
        workExperience.addKeyUpHandler(event -> {
            Validation.numberValidation(workExperience);
            Validation.addNumericKeyboardListener(workExperience);
        });
        workExperience.getElement().setId(candidate_add_view + "work_experience");
        //work experience month/year box
        workExperienceMonthYearBox = new DataListBox();
        workExperienceMonthYearBox.setAllowFirstItem(true);
        workExperienceMonthYearBox.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.month()), new SelectItem(2, wfmStrings.years())});
        workExperienceMonthYearBox.getElement().setId(candidate_add_view + "work_experience_month_year_box");
        //current employer
        currentEmployer = new TextBox();
        currentEmployer.addStyleName(DEFAULT_WIDTH);
        currentEmployer.getElement().setId(candidate_add_view + "current_employer");
        //expected salary
        expectedSalary = new TextBox();
        expectedSalary.addStyleName(DEFAULT_WIDTH);
        expectedSalary.getElement().setId(candidate_add_view + "expected_salary");
        Validation.addNumericKeyboardListener(expectedSalary);
        expectedSalary.addChangeHandler(changeEvent -> {
            String salary = (expectedSalary.getValue() != null && !expectedSalary.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(expectedSalary.getValue())).replace(".00", "") :
                    null;
            expectedSalary.setText(salary);
        });

        startSalary = new TextBox();
        startSalary.addStyleName(DEFAULT_WIDTH);
        startSalary.getElement().setId(candidate_add_view + "start_salary");
        Validation.addNumericKeyboardListener(startSalary);
        startSalary.addChangeHandler(changeEvent -> {
            String salary = (startSalary.getValue() != null && !startSalary.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(startSalary.getValue())).replace(".00", "") :
                    null;
            startSalary.setText(salary);
        });

        passportNumber = new TextBox();
        passportNumber.addStyleName(DEFAULT_WIDTH);
        passportNumber.getElement().setId(candidate_add_view + "passport_number");


        //candidate status
        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.getElement().setId(candidate_add_view + "candidate_status");

        //matched vacancies
        vacanciesLookUp = new MultiSelectVacancyLookUp();
        vacanciesLookUp.getFilterParametrs().setType(LookUpConstants.VACANCIES);
        vacanciesLookUp.addStyleName(Constants.DEFAULT_WIDTH);

        departmentContainer = new Div();
        department = new DepartmentLookUp();
        department.getSuggestBox().addSelectionHandler( event -> updateLocation(department.getSelectedItemID()));
        department.addStyleName(Constants.DEFAULT_WIDTH);
        departmentContainer.add(department);

        timeslotLookUp = new CRMLookUp(LookUpConstants.TIMESLOT);
        timeslotLookUp.addStyleName(Constants.DEFAULT_WIDTH);


        positionContainer = new Div();
        position = new PositionLookUp();
        position.getSuggestBox().addSelectionHandler(event -> setPositionItems());
        position.addStyleName(Constants.DEFAULT_WIDTH);
        positionContainer.add(position);


        //preferred location
        preferredLocation = new LocationLookUpWithCode();
        locationSelectionHandler(preferredLocation);
        preferredLocation.addStyleName(DEFAULT_WIDTH);
        preferredLocation.getElement().setId(candidate_add_view + "preferred_location");
        locationContainer = new Div();
        locationContainer.add(preferredLocation);

        allowancesTable = new EditableTable(getAllowancesTableColumn());
        allowancesTable.ensureDebugId("allowances_");
        allowancesTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null);
            }

            @Override
            public void removeRow() {

            }
        });
        addAllowanceEmptyRows(0);
        allowancesTable.getElement().setId("allowances_");


        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.showClearButton();
        project.setFullSearch(true);
        project.addStyleName(DEFAULT_WIDTH);
        project.ensureDebugId(candidate_add_view + "project");
//        project.getSuggestBox().addSelectionHandler(selectionEvent -> {
//            if (project.isSelected()) {
//                refreshVacancyList();
//            }
//        });
//        project.setClearCommand(() -> {
//            if (item.getVacancies() != null) {
//                setVacancies(item.getVacancies());
//            }
//        });

        drawItemTable1();
    }

    private void addAllowanceEmptyRows(int length) {
        while (length < 5) {
            addItem(null);
            length++;
        }
    }

    private void addItem(PaymentDeductionObject paymentDeduction) {
        final EditableTextBox type = new EditableTextBox();
        final PayrollCategoryLookUp categoryLookUp = new PayrollCategoryLookUp(wfmStrings.payment());
//        categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
        }

        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCategorySelected(categoryLookUp));

        categoryLookUp.getSuggestBox().addKeyUpHandler(event -> onCategorySelected(categoryLookUp));
        type.setText(wfmStrings.fixed());
        type.setEnabled(false);
        EditableTextBox amount = new EditableTextBox();
        Validation.checkToFocusTextBox(amount, Utils.getCalculationNumberFormat().format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(amount, 2);
        if (paymentDeduction != null) {
            amount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getPaymentAmount()));
            amount.setAdditionalId(paymentDeduction.getId());
        }
        allowancesTable.addRow(new Widget[]{categoryLookUp, type, amount});
    }

    private void onCategorySelected(PayrollCategoryLookUp categoryLookUp) {
        int selectedCategoryCount = 0;
        PaymentDeductionSelectItem selectedCategory = categoryLookUp.getSelectedData();
        if (selectedCategory != null) {
            for (int i = 0; i < allowancesTable.getGrid().getRowCount(); i++) {
                PaymentDeductionSelectItem selectedItem = ((PayrollCategoryLookUp) allowancesTable.getColumnById(i, "category")).getSelectedData();
                if (selectedItem != null && selectedItem.getCode() != null && selectedCategory.getCode() != null && selectedItem.getCode().equals(selectedCategory.getCode())) {
                    selectedCategoryCount++;
                }
            }
            if (selectedCategoryCount >= 2) {
                categoryLookUp.clear();
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, selectedCategory.getName() + " is already selected.");
                messageBox.open();
            }
        }
    }


    public ColumnConfig[] getAllowancesTableColumn() {
        ColumnConfig[] columns = new ColumnConfig[3];
        columns[0] = new ColumnConfig(LookUpCell.class, "category", wfmStrings.allowances(), 220, true, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, "center-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 120, true, "right-align-Cell");
        return columns;
    }

    protected void refreshVacancyList() {
        LoadingPanel.loading(true);
        ContactService.App.get().getProjectVacancyItem(objectId, project.getSelectedItemID(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ArrayList<SelectItem> result) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    setVacancies(result);
                });
            }
        });
    }

    public void setVacancies(ArrayList<SelectItem> vacancyList) {
        vacanciesLookUp.clear();
        if (vacancyList != null && vacancyList.size() > 0) {
            vacanciesLookUp.setSelectedItems(vacancyList);
        }
    }

    @Override
    protected void drawForm() {
        super.drawForm();
        addTitleField(CANDIDATE.OTHER_INFORMATION, wfmStrings.otherInformation());
        addTitleField(CANDIDATE.ALLOWANCE_INFORMATION, wfmStrings.allowanceInformation());
        addField(CANDIDATE_PICTURE, profileImage, null, true);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()), false, formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }
        } else {
            addField(NUMBER, number, wfmStrings.number());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE) != null) {
            addField(CREATED_DATE, createdDate, getTitle(formPropertyMap.get(CustomFormConstants.CREATED_DATE).isChanged() ?
                                    formPropertyMap.get(CustomFormConstants.CREATED_DATE).getTitle() : wfmStrings.createdDate(),
                            formPropertyMap.get(CustomFormConstants.CREATED_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CREATED_DATE).isInformation());
            createdDate.setEnabled(!formPropertyMap.get(CustomFormConstants.CREATED_DATE).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.CREATED_DATE).isInformation()) {
                new KpiToolTip(createdDate, formPropertyMap.get(CustomFormConstants.CREATED_DATE).getInformationText());
            }

        } else {
            addField(CREATED_DATE, createdDate, wfmStrings.createdDate());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null) {
            addField(LEAD_SOURCE, source, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isChanged() ?
                                    formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getTitle() : wfmStrings.source(),
                            formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isInformation());
            source.setEnabled(!formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isInformation()) {
                new KpiToolTip(source, formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getInformationText());
            }

        } else {
            addField(LEAD_SOURCE, source, wfmStrings.source());
        }
        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT) != null) {
            addField(CANDIDATE.CANDIDATE_PROJECT, project, getTitle(formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isChanged() ?
                                    formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()),
                            formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isRequired()), false,
                    formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isInformation());
            project.setEnabled(!formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isDisabled());
            if (formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isInformation()) {
                new KpiToolTip(project, formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).getInformationText());
            }
        } else {
            addField(CANDIDATE.CANDIDATE_PROJECT, project, Property.get(Constants.PROJECT, wfmStrings.project()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LANGUAGE) != null) {
            addField(LANGUAGE, languagesWidget, getTitle(formPropertyMap.get(CustomFormConstants.LANGUAGE).isChanged() ?
                                    formPropertyMap.get(CustomFormConstants.LANGUAGE).getTitle() : wfmStrings.spokenLanguages(),
                            formPropertyMap.get(CustomFormConstants.LANGUAGE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LANGUAGE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LANGUAGE).isInformation()) {
                new KpiToolTip(languagesWidget, formPropertyMap.get(CustomFormConstants.LANGUAGE).getInformationText());
            }
        } else {
            addField(LANGUAGE, languagesWidget, wfmStrings.spokenLanguages());
        }
        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE) != null) {
            addField(CANDIDATE.WORK_EXPERIENCE, new InputGroup(workExperience, workExperienceMonthYearBox),
                    getTitle(formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isChanged() ? formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).getTitle()
                            : wfmStrings.workExperience(), formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isRequired()), false,
                    formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isInformation());
            workExperience.setEnabled(!formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isDisabled());
            workExperienceMonthYearBox.setEnabled(!formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isDisabled());

            if (formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).isInformation()) {
                new KpiToolTip(workExperience, formPropertyMap.get(CANDIDATE.WORK_EXPERIENCE).getInformationText());
            }

        } else {
            addField(CANDIDATE.WORK_EXPERIENCE, new InputGroup(workExperience, workExperienceMonthYearBox), wfmStrings.workExperience());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS) != null) {
            addField(CustomFormConstants.MARTIAL_STATUS, martialStatus, getTitle(formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getTitle() :
                            wfmStrings.maritalStatus(), formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isInformation());
            martialStatus.setEnabled(!formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isInformation()) {
                new KpiToolTip(martialStatus, formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.MARTIAL_STATUS, martialStatus, getTitle(wfmStrings.maritalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null) {
            addField(GENDER, gender, getTitle(formPropertyMap.get(CustomFormConstants.GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.GENDER).getTitle()
                            : wfmStrings.gender(), formPropertyMap.get(CustomFormConstants.GENDER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.GENDER).isInformation());

            if (formPropertyMap.get(CustomFormConstants.GENDER).isInformation()) {
                new KpiToolTip(gender, formPropertyMap.get(CustomFormConstants.GENDER).getInformationText());
            }

        } else {
            addField(GENDER, gender, getTitle(wfmStrings.gender()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CURRENT_EMPLOYER) != null) {
            addField(CURRENT_EMPLOYER, currentEmployer, getTitle(formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isChanged()
                                    ? formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).getTitle() : wfmStrings.currentEmployer(),
                            formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isInformation());
            currentEmployer.setEnabled(!formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isInformation()) {
                new KpiToolTip(currentEmployer, formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).getInformationText());
            }
        } else {
            addField(CANDIDATE.CURRENT_EMPLOYER, currentEmployer, wfmStrings.currentEmployer());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY) != null) {
            addField(EXPECTED_SALARY, expectedSalary, getTitle(formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isChanged()
                                    ? formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).getTitle() : wfmStrings.expectedSalary(),
                            formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isInformation());
            expectedSalary.setEnabled(!formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isInformation()) {
                new KpiToolTip(expectedSalary, formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).getInformationText());
            }
        } else {
            addField(CANDIDATE.EXPECTED_SALARY, expectedSalary, wfmStrings.expectedSalary());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_SALARY) != null) {
            addField(CustomFormConstants.START_SALARY, startSalary, getTitle(formPropertyMap.get(CustomFormConstants.START_SALARY).isChanged()
                                    ? formPropertyMap.get(CustomFormConstants.START_SALARY).getTitle() : wfmStrings.startSalary(),
                            formPropertyMap.get(CustomFormConstants.START_SALARY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.START_SALARY).isInformation());
            startSalary.setEnabled(!formPropertyMap.get(CustomFormConstants.START_SALARY).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.START_SALARY).isInformation()) {
                new KpiToolTip(startSalary, formPropertyMap.get(CustomFormConstants.START_SALARY).getInformationText());
            }
        } else {
            addField(CANDIDATE.STARTSALARY, startSalary, wfmStrings.startSalary());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER) != null) {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isChanged()
                                    ? formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).getTitle() : wfmStrings.passportNumber(),
                            formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isInformation());
            passportNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isInformation()) {
                new KpiToolTip(passportNumber, formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, wfmStrings.passportNumber());
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SKILLS) != null) {
            skills = new TextArea2(1000, getTitle(formPropertyMap.get(CustomFormConstants.SKILLS).isChanged() ? formPropertyMap.get(CustomFormConstants.SKILLS).getTitle() : wfmStrings.skills()));
            addField(SKILLS, skills, null, formPropertyMap.get(CustomFormConstants.SKILLS).isRequired());
            skills.setEnabled(!formPropertyMap.get(CustomFormConstants.SKILLS).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.SKILLS).isInformation()) {
                new KpiToolTip(skills, formPropertyMap.get(CustomFormConstants.SKILLS).getInformationText());
            }
        } else {
            skills = new TextArea2(1000, getTitle(wfmStrings.skills()));
            addField(CANDIDATE.SKILLS, skills, null);
        }
        skills.addStyleName("file--AddCandidateView");
//        skills.addStyleName(DEFAULT_WIDTH);
        skills.getTextArea().getElement().setId("candidate_add_view_competencies");

        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.LOCATION) != null) {
            addField(CANDIDATE.LOCATION, locationContainer, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()),
                    formPropertyMap.get(CANDIDATE.LOCATION).isRequired()), false, formPropertyMap.get(CANDIDATE.LOCATION).isInformation());
            preferredLocation.setEnabled(!formPropertyMap.get(CANDIDATE.LOCATION).isDisabled());
            if (formPropertyMap.get(CANDIDATE.LOCATION).isInformation()) {
                new KpiToolTip(locationContainer, formPropertyMap.get(CANDIDATE.LOCATION).getInformationText());
            }
        } else {
            addField(CANDIDATE.LOCATION, locationContainer, wfmStrings.location());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle()
                    : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()), false, formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
            status.setEnabled(!formPropertyMap.get(CustomFormConstants.FIRST_NAME).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                new KpiToolTip(status, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
            }
        } else {
            addField(STATUS, status, wfmStrings.status());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCIES) != null) {
            addField(VACANCIES, vacanciesLookUp, getTitle(formPropertyMap.get(CustomFormConstants.VACANCIES).isChanged() ? formPropertyMap.get(CustomFormConstants.VACANCIES).getTitle() : wfmStrings.matchedVacancies(), formPropertyMap.get(CustomFormConstants.VACANCIES).isRequired()));
            vacanciesLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.VACANCIES).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCIES).isInformation()) {
                new KpiToolTip(vacanciesLookUp, formPropertyMap.get(CustomFormConstants.VACANCIES).getInformationText());
            }
        } else {
            addField(CANDIDATE.VACANCIES, vacanciesLookUp, wfmStrings.matchedVacancies());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LINKS) != null) {
            addField(LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), getTitle(formPropertyMap.get(CustomFormConstants.LINKS).isChanged() ? formPropertyMap.get(CustomFormConstants.LINKS).getTitle() : wfmStrings.links(), formPropertyMap.get(CustomFormConstants.LINKS).isRequired()), true);

        } else {
            //links
            addField(LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), wfmStrings.links(), true);
        }
        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.ALLOWANCES) != null) {
            addField(CANDIDATE.ALLOWANCES, allowancesTable, getTitle(formPropertyMap.get(CANDIDATE.ALLOWANCES).isChanged() ? formPropertyMap.get(CANDIDATE.ALLOWANCES).getTitle() : "", formPropertyMap.get(CANDIDATE.ALLOWANCES).isRequired()), true);
        } else {
            addField(CANDIDATE.ALLOWANCES, allowancesTable, null, true);
        }
        addField(CustomFormConstants.CANDIDATE.CANDIDATE_STATUS_HISTORY, candidateStatusHistoryGrid, "Salooom", true);

        addField(CANDIDATE.DEPARTMENT, departmentContainer, wfmStrings.department());
        addField(CANDIDATE.TIMESLOT, timeslotLookUp, wfmStrings.timeslot());
        addField(CANDIDATE.POSITION, positionContainer, wfmStrings.position());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CANDIDATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddCandidateView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return null;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_CANDIDATE;
                }

                @Override
                protected String getRelationName() {
                    return null;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        if (objectId == null && !Utils.isWebForm()) {
            service.generateCandidateNumber(objectId, new AbstractAsyncCallback<NumberData>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(NumberData result) {
                    number.setNumberData(result);
                }
            });
        }
        ContactService.App.get().editContact(contactType, objectId, null, Utils.isWebForm() ? Utils.getWebFormID() : null, false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    departmentId = item.getDepartmentItem() != null ? item.getDepartmentItem().getId() : null;

                    profileImage.initialize(o.getContactImageUrl(), o.getFirstName(), o.getLastName(), true, o.getGender() != null ? o.getGender() : Constants.VACANT);

                    setContactItem();
                    if (objectId == null) {
                        setDefaultValuesByFormProperty();
                    }
                });
            }
        });
    }

    @Override
    public void setContactItem() {
        super.setContactItem();
        if (item.getObjectId() != null) {
            if (Utils.hasGenericAccess(ENABLE_EMPLOYEE_CODE_INTEGER)) {
                NumberData numberData = new NumberData();
                numberData.setNumberString(item.getNumberData().getNumberString());
                numberData.setNumberFormat("");
                numberData.setIntNumber(0);
                number.setNumberData(numberData);
            } else {
                number.setNumberData(item.getNumberData());
            }
        }

        department.setSelected(item.getDepartmentItem());
        position.setSelected(item.getPositionItem());

        timeslotLookUp.setSelected(item.getTimeSlotItem());

        //links
        getLinkingUtil().getTaggingView().setFromName(item.getName());
        getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
        getLinkingUtil().drawLinks();

        createdDate.setDate(item.getCreatedDate());
        source.setItems(item.getCandidateSources());
        source.setSelected(item.getCandidateSource());
        workExperience.setText(item.getWorkExperience() == null ? "" : item.getWorkExperience().toString());
        if (item.getWorkExperienceMonthOrYear() != null) {
            workExperienceMonthYearBox.setSelected(item.getWorkExperienceMonthOrYear());
        }

        if (item.getMartialStatusList() != null) {

            martialStatus.setItems(item.getMartialStatusList());
        }
        //martial status
        if (item.getMartialStatusId() != null) {
            martialStatus.setSelected(item.getMartialStatusId());
        }
        //gender
        if (item.getGender() != null) {
            if (item.getGender().equals(Constants.MALE)) {
                male.setValue(true);
            } else {
                female.setValue(true);
            }
        }
        expectedSalary.setText(item.getExpectedSalary() != null ? AccountingUtils.get().formatUnitPrice(BigDecimal.valueOf(item.getExpectedSalary())).replace(".00", "") : "");
        startSalary.setText(item.getStartSalary() != null ? AccountingUtils.get().formatUnitPrice(BigDecimal.valueOf(item.getStartSalary())).replace(".00", "") : "");
        passportNumber.setText(item.getPassportNumber() != null ? item.getPassportNumber() : "");
        skills.setText(item.getSkills());
        //   preferredLocation.setItems(item.getLocations());
        preferredLocation.setSelected(item.getPreferredLocation());
        status.setItems(item.getCandidateStatuses());
        if (item.getCandidateStatus() != null) {
            status.setSelected(item.getCandidateStatus());
            if (!Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
                status.setReadOnly(true);
            }
        } else {
            for (SelectItem st : item.getCandidateStatuses()) {
                if (ContactListItem.C_S_NEW.equals(st.getDescription())) {
                    if (!Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE) && !isAddView) {
                        status.setReadOnly(true);
                    } else {
                        status.setSelected(st.getId());
                    }
                    break;
                }
            }
        }
        if (item.getProjectItem() != null) {
            project.setSelected(item.getProjectItem());
//            refreshVacancyList();
        }
//        else {
//            setVacancies(item.getVacancies());
//        }
        languagesWidget.setLanguages(item.getSpokingLanguages());
        setItemTableValues1(item.getCandidateCustomTableItems());
    }

    @Override
    public void setValues() {
        super.setValues();
        if (item.getObjectId() == null) {
            item.setCreatedDate(createdDate.getDate());
        }
        item.setPhotoId(profileImage.getImageID());
        //gender: male
        if (male.getValue()) {
            item.setGender(Constants.MALE);
        }
        if (female.getValue()) {
            item.setGender(Constants.FEMALE);
        }

        //martial status
        if (martialStatus.getSelectedItem() != null) {
            item.setMartialStatusId(martialStatus.getSelectedItem().getId());
        }
        //links
        item.setRelations(getLinkingUtil().getTaggingView().getSelectedRelations());

        if (Utils.hasGenericAccess(ENABLE_EMPLOYEE_CODE_INTEGER)) {
            NumberData numberData = new NumberData();
            numberData.setNumberFormat("");
            numberData.setIntNumber(0);
            numberData.setNumberString(number.getNumberData(true).getNumberString());
            item.setNumberData(numberData);
        } else {
            item.setNumberData(number.getNumberData(false));
        }
        item.setCandidateSource(source.getSelectedItem());
        if (workExperience.getText() != null && workExperience.getText().matches(Constants.REGEX_INTEGER)) {
            item.setWorkExperience(Integer.valueOf(workExperience.getText()));
        }
        if (workExperienceMonthYearBox.isSomethingSelected()) {
            item.setWorkExperienceMonthOrYear(workExperienceMonthYearBox.getSelectedItem().getId());
        }

        if (expectedSalary.getText() != null && !expectedSalary.getText().isEmpty()) {
            item.setExpectedSalary(Double.valueOf(expectedSalary.getText().replaceAll(",", "")));
        }

        if (startSalary.getText() != null && !startSalary.getText().isEmpty()) {
            item.setStartSalary(Double.valueOf(startSalary.getText().replaceAll(",", "")));
        }
        if (passportNumber.getText() != null && !passportNumber.getText().isEmpty()) {
            item.setPassportNumber(passportNumber.getText());
        }
        item.setCurrentEmployer(currentEmployer.getText());
        item.setSkills(skills.getText());
        item.setPreferredLocation(preferredLocation.getSelectedItem());
        item.setDepartmentItem(department.getSelectedItem());
        item.setTimeSlotItem(timeslotLookUp.getSelectedItem());
        item.setPositionItem(position.getSelectedItem());

        if (item.getCandidateStatus() != null && item.getCandidateStatus().getId() != null &&
                status.getSelectedItem() != null && status.getSelectedItem().getId() != null && item.getCandidateStatus().getId() != status.getSelectedItem().getId()) {
            isStatusChanged = true;
        }

        item.setCandidateStatus(status.getSelectedItem());
        item.setProjectItem(project.getSelectedItem());
        item.setVacancies(vacanciesLookUp.getSelectedItems());
        if (allowancesTable != null) {
            ArrayList<PaymentDeductionObject> allowanceList = new ArrayList<>();
            for (int i = 0; i < allowancesTable.getRowCount(); i++) {
                PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) allowancesTable.getColumnById(i, "category");
                EditableTextBox amount = (EditableTextBox) allowancesTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() != null && amount.getText() != null && !amount.getText().equals(Utils.getCalculationNumberFormat().format(BigDecimal.ZERO))) {
                    PaymentDeductionObject object = new PaymentDeductionObject();
                    object.setCategoryItem(categoryLookUp.getSelectedData());
                    object.setType(0);
                    object.setPaymentAmount(BigDecimal.valueOf(Utils.getCalculationNumberFormat().parse(amount.getText())));

                    object.setId(amount.getAdditionalId());
                    allowanceList.add(object);
                }
            }
            item.setAllowanceCategories(allowanceList);
        }
        if (languagesWidget != null) {
            item.setSpokingLanguages(languagesWidget.getLanguages());
        }

        if (getCustomObjectData1() != null) {
            item.setCandidateCustomTableItems(getCustomObjectData1());
        }
    }

    @Override
    protected boolean validate() {
        boolean validate = super.validate();
        int errors = 0;
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()) {
            TextBox comperator = new TextBox();
            comperator.setText(number.getTxtNumber().getText() + number.getTxtPrefix().getText());
            errors += markAsError(CustomFormConstants.NUMBER, number, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), comperator, formPropertyMap.get(CustomFormConstants.NUMBER).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE) != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.CREATED_DATE, createdDate, createdDate.getDate() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SOURCE) != null && formPropertyMap.get(CustomFormConstants.SOURCE).isRequired()) {
            errors += markAsError(CustomFormConstants.SOURCE, source, source.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT) != null && formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).isRequired()) {
            errors += markAsError(CANDIDATE.CANDIDATE_PROJECT, project, project.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LANGUAGE) != null && formPropertyMap.get(CustomFormConstants.LANGUAGE).isRequired()) {
            errors += markAsError(CustomFormConstants.LANGUAGE, languagesWidget, !(languagesWidget.getLanguages() != null && languagesWidget.getLanguages().size() > 0));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WORK_EXPERIENCE) != null && formPropertyMap.get(CustomFormConstants.WORK_EXPERIENCE).isRequired()) {
            errors += markAsError(CustomFormConstants.WORK_EXPERIENCE, workExperience, Utils.isNullOrEmpty(workExperience.getText()));
            errors += markAsError(CustomFormConstants.WORK_EXPERIENCE, workExperienceMonthYearBox, workExperienceMonthYearBox.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER) != null && formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).isRequired()) {
            errors += markAsError(CustomFormConstants.CURRENT_EMPLOYER, currentEmployer, Utils.isNullOrEmpty(currentEmployer.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY) != null && formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).isRequired()) {
            errors += markAsError(CustomFormConstants.EXPECTED_SALARY, expectedSalary, Utils.isNullOrEmpty(expectedSalary.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_SALARY) != null && formPropertyMap.get(CustomFormConstants.START_SALARY).isRequired()) {
            errors += markAsError(CustomFormConstants.START_SALARY, startSalary, Utils.isNullOrEmpty(startSalary.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.PASSPORT_NUMBER, passportNumber, Utils.isNullOrEmpty(passportNumber.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SKILLS) != null && formPropertyMap.get(CustomFormConstants.SKILLS).isRequired()) {
            errors += markAsError(CustomFormConstants.SKILLS, skills, Utils.isNullOrEmpty(skills.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.LOCATION) != null && formPropertyMap.get(CANDIDATE.LOCATION).isRequired()) {
            errors += markAsError(CANDIDATE.LOCATION, preferredLocation, preferredLocation.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            if (Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE) || isAddView) {
                errors += markAsError(CustomFormConstants.STATUS, status, status.getSelectedItem() == null);
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS) != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.MARTIAL_STATUS, martialStatus, !Validation.validateListBoxRequired(martialStatus));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null && formPropertyMap.get(CustomFormConstants.GENDER).isRequired()) {
            errors += markAsError(CustomFormConstants.GENDER, gender, !male.getValue() && !female.getValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCIES) != null && formPropertyMap.get(CustomFormConstants.VACANCIES).isRequired()) {
            errors += !Validation.validateVacancyMultiSelectLookUpRequired(vacanciesLookUp) ? 1 : 0;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return validate && errors == 0;
    }

    public void saveContact() {
        setValues();
        item.setCheckForDuplicates(!forceToSave);
        LoadingPanel.loading(true);
        ReferenceItem candidateStatus = item.getCandidateStatus();
        if (isStatusChanged) {
            LoadingPanel.loading(false);
            enableButton(true);
            if ((candidateStatus.getDescription()).equals("0")) {
                CandidatePercentageStageModal widgets1 = new CandidatePercentageStageModal(candidateStatus, item.getObjectId());
                widgets1.save((o) -> {
                    LoadingPanel.loading(true);
                    enableButton(false);
                    item.setCandidateStatus((SelectItem) o);
                    saveMethod();
                });
                widgets1.cancel((o) -> {
                    LoadingPanel.loading(false);
                    enableButton(true);
                });
            } else if ((candidateStatus).isSelected()) {
                NotesWidget notesPanel = new NotesWidget();
                notesPanel.setNoteListener(() -> {
                    LoadingPanel.loading(true);
                    enableButton(false);
                    String comment = notesPanel.getLastHistoryItem().getComment();
                    candidateStatus.setCategory(comment);
                    saveMethod();
                });
                notesPanel.setCloseListener(() -> {
                    LoadingPanel.loading(false);
                    enableButton(true);
                });

                notesPanel.noteShell();
            } else {
                LoadingPanel.loading(true);
                enableButton(false);
                saveMethod();
            }
        } else {
            saveMethod();
        }

    }

    private void saveMethod() {
        ContactService.App.get().saveCandidate(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButton(true);
            }


            @Override
            public void success(Integer objectID) {
                LoadingPanel.loading(false);
                if (Utils.isWebForm()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.WEB_FORM_SAVED, objectID, AddCandidateView.this);
                }
                enableButton(true);
                if (objectID != null && objectID > 0) {
                    if (!Utils.isWebForm()) {
                        Info.show(successMessage, Info.Type.INFO);
                    }
                    if (item.getObjectId() == null) {
                        item.setObjectId(objectID);
                    }
                    if (item.isLeadContact()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_ADD_EDIT, item, AddCandidateView.this);
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_ADD_EDIT, item, AddCandidateView.this);
                    if (item.getCrmAccount().getName() != null && !"".equals(item.getCrmAccount().getName())) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, item, AddCandidateView.this);
                    }
                    onShellOk();
                } else if (objectID != null && objectID.intValue() != Constants.ANTIBOT_ERROR) {
                    showDuplicatePopup(objectID != null && objectID == -1);
                }
            }

        });

    }

    public void setDefaultValuesByFormProperty() {
        super.setDefaultValuesByFormProperty();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue() != null) {
            number.setNumberData(new NumberData(formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS) != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getDefaultValue() != null) {
            martialStatus.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE) != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE).getDefaultValue() != null) {
            createdDate.setText(formPropertyMap.get(CustomFormConstants.CREATED_DATE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getDefaultValue() != null) {
            source.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getSelectedId(), formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT) != null && formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).getDefaultValue() != null) {
            project.setSelected(new SelectItem(formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).getSelectedId(), formPropertyMap.get(CANDIDATE.CANDIDATE_PROJECT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER) != null && formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).getDefaultValue() != null) {
            currentEmployer.setText(formPropertyMap.get(CustomFormConstants.CURRENT_EMPLOYER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY) != null && formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).getDefaultValue() != null) {
            expectedSalary.setText(formPropertyMap.get(CustomFormConstants.EXPECTED_SALARY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_SALARY) != null && formPropertyMap.get(CustomFormConstants.START_SALARY).getDefaultValue() != null) {
            startSalary.setText(formPropertyMap.get(CustomFormConstants.START_SALARY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SKILLS) != null && formPropertyMap.get(CustomFormConstants.SKILLS).getDefaultValue() != null) {
            skills.setText(formPropertyMap.get(CustomFormConstants.SKILLS).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CANDIDATE.LOCATION) != null && formPropertyMap.get(CANDIDATE.LOCATION).getDefaultValue() != null) {
            preferredLocation.setSelected(new SelectItem(formPropertyMap.get(CANDIDATE.LOCATION).getSelectedId(), formPropertyMap.get(CANDIDATE.LOCATION).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue() != null) {
            number.setNumberData(new NumberData(formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue()));
        }
    }


    public void drawItemTable1() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.CANDIDATE_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (HashMap.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        AddCandidateView.this.configMap = result;

                        String fieldID = configMap.getKey();
                        ColumnConfigs[] configs = configMap.getValue();
                        if (configs != null && configs.length == 0) {
                            continue;
                        }

                        Map<String, ColumnConfigs> columnsMap = Stream.of(configs)
                                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

                        EditableTable editableTable = new EditableTable(getCustomColumns(columnsMap), true, true);

                        editableTableMap.put(fieldID, editableTable);

                        editableTable.setLayoutData(fieldID);
                        editableTable.setDraggable(true);
                        editableTable.setWidth("100%");
                        editableTable.setListener(new EditableTableListener() {
                            @Override
                            public void addRow() {
                                editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                            }

                            @Override
                            public void removeRow() {

                            }
                        });
                        for (int i = 0; i < 3; i++) {
                            editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                        }
                        addField(fieldID, editableTable, null, true);
                        getItemTableWebhooks(editableTable, ItemTableEnum.CANDIDATE_CUSTOM_ITEM, fieldID);
                    }
                }
            }
        });
    }


    private Widget[] getCustomWidgets(CustomTableRpc item, String fieldID) {
        int index = 0;

        Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(fieldID))
                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (itemCustomCFs.containsKey(fieldID)) {

                CompanyCustomFieldItem cfItem = getCustomFieldItem(itemCustomCFs.get(fieldID), columnCode);

                if (UI_TYPE_TEXTBOX.equals(cfItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType()) || UI_TYPE_URL.equals(cfItem.getUiType())) {
                    CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");
                    if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(t, 5, true);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    CustomPercentageField t = new CustomPercentageField(cfItem);
                    t.setWidth("100%");
                    t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
                    CustomDropDownField d = new CustomDropDownField(cfItem);
                    d.setWidth("100%");
                    if (cfItem.getPredefinedValues() != null) {
                        SelectItem[] sItems = new SelectItem[cfItem.getPredefinedValues().length];
                        int x = 0;
                        for (String s : cfItem.getPredefinedValues()) {
                            sItems[x] = new SelectItem(x, s);
                            x++;
                        }
                        d.setItems(sItems);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        d.setSelectedByValue(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
                    com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker d = new com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker(cfItem);
                    d.setWidth("100%");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        d.setDate(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfItem.getUiType())) {
                    CustomDateTime customDateTime = new CustomDateTime(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        customDateTime.setDateTime(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    customDateTime.setTitle(columnCode);
                    widgets[index++] = customDateTime;

                } else if (Constants.UI_TYPE_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        textAreaField.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    textAreaField.setTitle(columnCode);
                    widgets[index++] = textAreaField;
                } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomHTMLTextAreaField htmlTextAreaField = new CustomHTMLTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        htmlTextAreaField.setData(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    htmlTextAreaField.setTitle(columnCode);
                    widgets[index++] = htmlTextAreaField;
                } else if (Constants.UI_TYPE_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }
                    lookup.setTitle(columnCode);
                    widgets[index++] = lookup;
                } else if (Constants.UI_TYPE_CURRENCY.equals(cfItem.getUiType())) {
                    CustomFieldCurrencyWidget currencyWidget = new CustomFieldCurrencyWidget(cfItem, "CustomForm");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            currencyWidget.setCurrency(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }

                    currencyWidget.setTitle(columnCode);
                    widgets[index++] = currencyWidget;
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldMultiLookUpField multiLookUp = new CustomFieldMultiLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        ArrayList<SelectItem> list = new ArrayList<>();
                        if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                            multiLookUp.setSelectedItems(list);
                        }
                    }

                    multiLookUp.setTitle(columnCode);
                    widgets[index++] = multiLookUp;
                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cfItem.getUiType())) {

                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getItem() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getItem().getId(), customFieldItem.getItem().getName()));
                            textAreaField.setText(customFieldItem.getItem().getDescription());
                        }
                    }
                    lookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                        if (lookup.getSelectedItem() != null && lookup.getSelectedItem().getId() != null) {
                            AllInOneService.App.get().getProductDescription(lookup.getSelectedItem().getId(), new AbstractAsyncCallback<String>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    super.failure(throwable);
                                }

                                @Override
                                public void success(String result) {
                                    if (result != null) {
                                        textAreaField.setText(result);
                                        lookup.getSelectedItem().setDescription(result);
                                        int currentRowId = editableTableMap.get(fieldID).getGrid().getCurrentRow();
                                        CustomCell cel = (CustomCell) editableTableMap.get(fieldID).getColumnCellWidgetById(currentRowId, columnCode + "_DESCRIPTION");
                                        cel.InActive();
                                    }
                                }
                            });
                        }
                    });

                    lookup.setTitle(columnCode);

                    textAreaField.setTitle(wfmStrings.description());
                    widgets[index++] = lookup;
                    widgets[index++] = textAreaField;

                } else if (TYPE_ENTITY_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldEntityLookUpField entityLookUp = new CustomFieldEntityLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem != null && customFieldItem.getFieldStringValue() != null) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(customFieldItem.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && customFieldItem.getQueryItems() != null) {
                                for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        entityLookUp.setSelected(new SelectItem(id, selectItem.getName()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    entityLookUp.setTitle(columnCode);
                    widgets[index++] = entityLookUp;
                }
            }
        }
        return widgets;
    }

    private ColumnConfig[] getCustomColumns(Map<String, ColumnConfigs> columnsMap) {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columns[i++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 100, columnsMap.get(cc).isRequired());
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columns[i++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 100, columnsMap.get(cc).isRequired());
                    break;
                default:
                    ColumnConfig columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        ColumnConfig columnConfigItem = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigItem.setPixel(false);
                        columnConfigItem.setForceWidthInPercent(true);
                        columns[i++] = columnConfigItem;

                        ColumnConfig columnConfigDescription = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigDescription.setPixel(false);
                        columnConfigDescription.setForceWidthInPercent(true);
                        columns[i++] = columnConfigDescription;
                    } else {
                        columns[i++] = columnConfig;
                    }
                    break;
            }
        }
        return columns;
    }

    private CompanyCustomFieldItem getCustomFieldItem(List<CompanyCustomFieldItem> companyCustomFieldItems, String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
    }


    private Map<String, List<CustomTableRpc>> getCustomObjectData1() {
        Map<String, List<CustomTableRpc>> map = new HashMap<>();
        for (Map.Entry<String, EditableTable> mapTable : editableTableMap.entrySet()) {

            String uuid = mapTable.getKey();

            List<CompanyCustomFieldItem> itemCustom = itemCustomCFs.get(uuid);

            Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            EditableTable productTable = mapTable.getValue();
            ArrayList<CustomTableRpc> tableItem = new ArrayList<>();
            ArrayList<CompanyCustomFieldItem> resultItemList;
            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                CustomTableRpc result = new CustomTableRpc();
                resultItemList = new ArrayList<>();
                for (String columnCode : columnsMap.keySet()) {
                    if (itemCustomCFs.containsKey(uuid)) {
                        Object customFieldValue = null;
                        Integer customFieldValueId = null;
                        SelectItem itemValue = null;
                        if (getCustomFieldItem(itemCustom, columnCode).isRequired()) {
                            isItemTableRequared = true;
                        }
                        if (UI_TYPE_TEXTBOX.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomTextBoxField t = (CustomTextBoxField) productTable.getColumnById(i, columnCode);
                            if (t.getText() != null && !t.getText().isEmpty()) {
                                customFieldValue = t.getText();
                            }
                        }
                        if (Constants.UI_TYPE_PERCENTAGE.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomPercentageField percentageField = (CustomPercentageField) productTable.getColumnById(i, columnCode);
                            if (percentageField != null && !percentageField.getText().isEmpty()) {
                                customFieldValue = percentageField.getText();
                            }

                        } else if (UI_TYPE_DROPDOWN.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDropDownField t = (CustomDropDownField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                            }
                        } else if (UI_TYPE_DATEPICKER.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker t = (com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker) productTable.getColumnById(i, columnCode);
                            if (t.getDate() != null) {
                                customFieldValue = t.getDate();
                            }
                        } else if (UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                                customFieldValueId = t.getSelectedItem().getId();
                            }
                        } else if (UI_TYPE_CURRENCY.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldCurrencyWidget t = (CustomFieldCurrencyWidget) productTable.getColumnById(i, columnCode);
                            if (t.getCurrencyID() != null) {
                                customFieldValue = t.getCurrencyName();
                                customFieldValueId = t.getCurrencyID();
                            }
                        } else if (UI_TYPE_MULTI_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItems() != null && t.getSelectedItems().size() > 0) {
                                customFieldValue = t.getSelectedItems();
                            }
                        } else if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUp item = (CustomFieldLookUp) productTable.getColumnById(i, columnCode);
                            CustomTextAreaField desc = (CustomTextAreaField) productTable.getColumnById(i, columnCode + "_DESCRIPTION");
                            if (item.getSelectedItem() != null) {
                                itemValue = new SelectItem(item.getSelectedItemID(), item.getSelectedItem().getName(), desc.getText());
                            }
                        } else if (TYPE_ENTITY_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldEntityLookUpField lookUp = (CustomFieldEntityLookUpField) productTable.getColumnById(i, columnCode);
                            if (lookUp.getSelectedItem() != null) {
                                customFieldValue = lookUp.getSelectedItem().getId().toString();
                            }
                        }
                        CompanyCustomFieldItem companyCustomFieldItem = getCustomFieldItem(itemCustom, columnCode);
                        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
                        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                        resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());

                        if (customFieldValue != null) {
                            if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                                resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            } else {
                                resultItem.setFieldStringValue((String) customFieldValue);
                            }
                            if (customFieldValueId != null) {
                                resultItem.setSelectedId(customFieldValueId);
                            }
                        }
                        if (itemValue != null) {
                            resultItem.setItem(itemValue);
                        }
                        resultItemList.add(resultItem);
                    }
                }
                result.setUuid(uuid);
                result.setItemCustomFields(resultItemList);
                tableItem.add(result);
            }
            map.put(uuid, tableItem);
        }
        return map;
    }

    protected void setItemTableValues1(Map<String, List<CustomTableRpc>> tableItems) {
        if (tableItems != null && tableItems.size() > 0) {
            for (Map.Entry map : tableItems.entrySet()) {
                String uuid = (String) map.getKey();
                if (editableTableMap.get(uuid) != null) {
                    editableTableMap.get(uuid).removeAllRows();
                }
                for (CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                    if (editableTableMap.get(uuid) != null) {
                        editableTableMap.get(uuid).addRow(getCustomWidgets(item, uuid));
                    }
                }
            }
        }
    }

    protected Widget onInitialize1() {
        CommonService.App.get().getCompanyCustomFields(ViewName.CandidateCustomItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddCandidateView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                drawItemTable1();
            }
        });
        return this;
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
                callback.onSuccess(onInitialize1());
            }
        });
    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode) {
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            department.removeFromParent();
            departmentContainer.remove(department);
            department = new DepartmentLookUp();
            department.getSuggestBox().addSelectionHandler(event -> {
                updateLocation(department.getSelectedItemID());
            });
            department.getFilterParametrs().setLocationId(locationLookUpWithCode.getSelectedItemID());
            departmentContainer.add(department);
        });
        locationLookUpWithCode.getTextBox().addKeyDownHandler(e -> {
            department.removeFromParent();
            departmentContainer.remove(department);
            department = new DepartmentLookUp();
            departmentContainer.add(department);
            position.removeFromParent();
            positionContainer.remove(position);
            position = new PositionLookUp();
            position.getSuggestBox().addSelectionHandler(event -> {
                setPositionItems();
            });
            positionContainer.add(position);
        });
        department.getTextBox().addKeyDownHandler(e -> {
            position.removeFromParent();
            positionContainer.remove(position);
            position = new PositionLookUp();
            position.getSuggestBox().addSelectionHandler(event -> {
                setPositionItems();
            });
            position.getFilterParametrs().setDepartmentId(departmentId);
            positionContainer.add(position);
        });


    }

    private void updateLocation(Integer departmentId) {
        AllInOneService.App.get().getLocationByDepartmentId(departmentId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                //
                preferredLocation.removeFromParent();
                locationContainer.remove(preferredLocation);
                preferredLocation = new LocationLookUpWithCode();
                locationSelectionHandler(preferredLocation);
                preferredLocation.setSelected(result);
                locationContainer.add(preferredLocation);
                position.removeFromParent();
                positionContainer.remove(position);
                position = new PositionLookUp();
                position.getFilterParametrs().setDepartmentId(departmentId);
                position.getSuggestBox().addSelectionHandler(e -> {
                    setPositionItems();
                });
                positionContainer.add(position);
            }
        });



    }

    private void setPositionItems() {
        AllInOneService.App.get().getPositionItems(position.getSelectedItemID(), new AsyncCallback<PositionsSelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(PositionsSelectItem result) {
                preferredLocation.setSelected(result.getLocation());
                department.setSelected(result.getDepartment());
            }
        });
    }
}