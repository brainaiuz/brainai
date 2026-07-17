package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.*;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.client.ui.JobFamilyPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.VACANCY.*;

public class AddVacancyView extends CustomForm2 implements Colapse, Constants, HasLinksInterface {

    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private final Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();
    public VacancyItem item;
    protected String formType = null;
    protected Integer convertedFormId = null;
    protected NoteWidget noteWidget;
    protected SplitButton printPdfSplitButton;
    protected WfmButton2 submit, approve, draftButton;
    FormHasCustomField customFieldUtil;
    private Integer objectID, positionId;
    private EmployeeLookUp manager;
    private PositionPanel position;
    private LocationLookUpWithCode location;
    private AdvancedInputGroup locationPanel1;
    private DepartmentLookUp department;
    private CRMLookUp project;
    private FlexTable genderTable;
    private RadioButton male, female, irrelevantGender;
    private TextAreaWithSuggestionPopup jobRequirement;
    private DateTimePicker dateTime;
    private DataListBox requiredDegree, vacancyType, status, jobType;
    private DatePicker startDate, endDate;
    private TextBox jobTitle, proposedSalary;
    private TextBox actualPlaceCount, plannedPlaceCount;
    private TextBox requestPlaceCount;
    private Div inputGroup;
    private JobFamilyPanel jobFamily;
    private TextAreaWithSuggestionPopup responsibilities;
    private GeneralFileUpload attachments;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private ChosenApproversWidget approvers;
    private MultiLanguageTextAreaWidget jobTitleLocalize;
    private MultiLanguageRichEditorWidget jobRequirementLocalize, descriptionLocalize, responsibilityLocalize;
    private SpokenLanguagesWidget languagesWidget;
    private VacancyQuestionsWidget vacancyQuestionsWidget;
    private WfmButton2 locale;
    private AddEditLocaleView localeView;
    private ReferenceLocale localeItem;
    private FlexTable localedNameBox;
    private CurrencyWidget currencyWidget;
    private Numbering number;
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private TextAreaWithSuggestionPopup description;
    private boolean isItemTableRequared = false;
    private HasLinks linkingUtil;
    private FooterInformer link;
    private boolean isCopying;
    private String editNote = "";

    public AddVacancyView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

    public AddVacancyView(String formType, Integer convertedFormId) {
        super("vacancyadd", wfmStrings.add() + " " + Property.get(VACANCY, wfmStrings.vacancy()));
        this.formType = formType;
        this.convertedFormId = convertedFormId;
        this.objectID = null;
    }

    public AddVacancyView(Integer objectId, boolean isCopying) {
        super("vacancyadd", wfmStrings.add() + " " + Property.get(VACANCY, wfmStrings.vacancy()));
        this.isCopying = isCopying;
        if (!(objectId == null || isCopying)) {
            setDescription(hrmsStrings.editVacancy());
        }
        this.objectID = objectId;
    }

    public AddVacancyView(boolean isFromPositionm, Integer positionId) {
        super("vacancyadd", wfmStrings.add() + " " + Property.get(VACANCY, wfmStrings.vacancy()));
        this.positionId = positionId;
    }

    public AddVacancyView(Integer objectID) {
        super("vacancyadd", objectID == null ? wfmStrings.add() + " " + Property.get(VACANCY, wfmStrings.vacancy()) : wfmStrings.edit() + " " + Property.get(VACANCY, wfmStrings.vacancy()));
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, event -> save(Constants.VACANCY_APPROVAL_STATUS_DRAFT));
        submit = addButton(wfmStrings.submit(), event -> {
            if (objectID != null && (Constants.VACANCY_APPROVAL_STATUS_SUBMITTED.equals(item.getApprovalStatusCode()) || Constants.VACANCY_APPROVAL_STATUS_REJECTED.equals(item.getApprovalStatusCode()))) {
                NotesWidget notesPanel = new NotesWidget(false);
                notesPanel.setNoteListener(() -> {
                    editNote = "editReason: ";
                    if (notesPanel.getLastHistoryItem() != null && notesPanel.getLastHistoryItem().getComment() != null
                            && !"".equals(notesPanel.getLastHistoryItem().getComment().trim())) {
                        editNote += notesPanel.getLastHistoryItem().getComment();
                    }
                    save(Constants.VACANCY_APPROVAL_STATUS_SUBMITTED);

                });
                notesPanel.noteShell();
            } else {
                save(Constants.VACANCY_APPROVAL_STATUS_SUBMITTED);
            }
        });
        approve = addButton(wfmStrings.approve(), event -> save(Constants.VACANCY_APPROVAL_STATUS_APPROVED));
        approve.setVisible(false);
        GWT.log(Utils.hasPermission(PermissionConstants.HRMS_DRAFT_VACANCY) + "1");
        draftButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_DRAFT_VACANCY));

        if (Utils.hasPermission(PermissionConstants.VACANCY_LINKS)) {
            createLinkButton();
        }
        initApproverLoadHandler();
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        RecruitmentService.App.get().getVacancyItem(objectID, formType, convertedFormId, new AbstractAsyncCallback<VacancyItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final VacancyItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    item = result;
                    LoadingPanel.loading(false);
                    if (isCopying) {
                        item.setObjectID(null);
                        objectID = null;
                        generateNewVacancyNumber();
                    }
                    fillFormWithData();
                    initPredefinedValues();
                    if (objectID == null) {
                        setDefaultValues();
                    }
                    pdfTool(result);
                    if (objectID == null) {
                        setDefaultValuesByFormProperty();
                    }
                    if (objectID != null) {
                        vacancyQuestionsWidget.setData(result.getVacancyQiestionItems());
                    }
                    pdfTool(result);
                    jobTitleLocalize.setValueMap(item.getJobTitleLocalize());
                    descriptionLocalize.setValueMap(item.getDescriptionLocalize());
                    jobRequirementLocalize.setValueMap(item.getJobRequirementLocalize());
                    responsibilityLocalize.setValueMap(item.getResponsibilitiesLocalize());
                    setItemTableValues(item.getCustomTableItems());
                });
            }
        });
    }

    private void generateNewVacancyNumber() {
        RecruitmentService.App.get().generateVacancyNumber(null, new AbstractAsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(NumberData result) {
                if (result != null) {
                    number.setNumberData(result);
                }
            }
        });
    }

    private void setItemTableValues(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
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

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER) != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).getDefaultValue() != null) {
            number.setNumberData(new NumberData(formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER) != null && formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).getDefaultValue() != null) {
            manager.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION) != null && formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).getDefaultValue() != null) {
            location.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.POSITION) != null && formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).getDefaultValue() != null) {
            position.setSelectedPosition(formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).getSelectedId());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getDefaultValue() != null) {
            jobTitle.setText(formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).getDefaultValue() != null) {
            department.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT) != null && formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).getDefaultValue() != null) {
            project.setSelected(formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY) != null && formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).getDefaultValue() != null) {
            proposedSalary.setText(formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT) != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).getDefaultValue() != null) {
            requiredDegree.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).getDefaultValue() != null) {
            vacancyType.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).getDefaultValue() != null) {
            description.getMaterialRichEditor().setData(formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                startDate.setDate(currentDate);
            } else {
                startDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getSelectedId()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                endDate.setDate(currentDate);
            } else {
                endDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).getSelectedId()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.STATUS) != null && formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT) != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).getDefaultValue() != null) {
            plannedPlaceCount.setText(formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).getDefaultValue() != null) {
            jobType.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES) != null && formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).getDefaultValue() != null) {
            responsibilities.getMaterialRichEditor().setData(formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE) != null && formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).getDefaultValue() != null) {
            requiredDegree.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).getSelectedId(), formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).getDefaultValue()));
        }
    }

    public void pdfTool(final VacancyItem result) {
        if (this.printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/vacancyViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.VACANCY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Vacancy, this.getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                AddVacancyView.super.onInitialize();
            }

            @Override
            public void success(final CompanyCfAndPropertyItems result) {
                if (result != null) {
                    AddVacancyView.this.getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                formPropertyMap = result.getFormPropertyMap();
                AddVacancyView.super.onInitialize();
            }
        });

        CommonService.App.get().getCompanyCustomFields(ViewName.VacancyItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddVacancyView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                drawItemTable();
            }
        });
        return null;
    }

    public void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.VACANCY_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    AddVacancyView.this.configMap = result;
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        String fieldID = configMap.getKey();
                        ColumnConfigs[] configs = configMap.getValue();
                        if (configs == null || configs.length == 0) {
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
                    }
                }
            }
        });
    }

    private ColumnConfig[] getCustomColumns(Map<String, ColumnConfigs> columnsMap) {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {
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
        }
        return columns;
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

                } else if (Constants.TYPE_ENTITY_LOOKUP.equals(cfItem.getUiType())) {
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

    private CompanyCustomFieldItem getCustomFieldItem(List<CompanyCustomFieldItem> companyCustomFieldItems, String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CustomFormConstants.VACANCY.POSITION, item.getPositions());
        addPredefinedValues(CustomFormConstants.VACANCY.MANAGER, item.getManagers());
        addPredefinedValues(CustomFormConstants.VACANCY.JOB_TYPE, item.getTimeTypes());
        addPredefinedValues(CustomFormConstants.VACANCY.STATUS, item.getStatuses());
        addPredefinedValues(CustomFormConstants.VACANCY.DEPARTMENT, item.getDepartmentItems());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected void fillFormWithData() {
        //numbering
        number.setNumberData(item.getNumberData());

        if (item.getProjectId() != null) {
            project.setSelected(item.getProjectId(), item.getProjectName());
        }

        if (item.getGender() != null) {
            if (item.getGender().equals(wfmStrings.male())) {
                male.setValue(true);
            } else if (item.getGender().equals(wfmStrings.female())) {
                female.setValue(true);
            } else {
                irrelevantGender.setValue(true);
            }
        }
        setFormattedValueToPropsedSalary(item.getProposedSalary());
        String userLanguage = Utils.getUserLanguage();
        jobRequirement.getMaterialRichEditor().setData(item.getJobRequirements());
        if (mapHasValueForLang(item.getJobRequirementLocalize(), userLanguage)) {
            jobRequirement.getMaterialRichEditor().setData(item.getJobRequirementLocalize().get(userLanguage));
        }
        jobRequirement.getElement().addClassName("jobRequirement");
        jobRequirement.setHeight("400");
        if (item.getContractFrom() != null) {
            dateTime.getStartDatePicker().setDate(item.getContractFrom());
            dateTime.setStartDate(item.getContractFrom());
        }
        if (item.getEndDate() != null) {
            dateTime.getDueDatePicker().setDate(item.getContractTo());
            dateTime.setDueDate(item.getContractTo());
        }
        vacancyType.setSelected(item.getVacancyType());

        if (item.getDepartment() != null) {
            department.setSelected(item.getDepartment());
        }
        ReferenceLocale refLocale = item.getReferenceLocale();
        if (refLocale != null) {
            localeItem.setUzbek(refLocale.getUzbek());
            localeItem.setArabic(refLocale.getArabic());
            localeItem.setEnglish(refLocale.getEnglish());
            localeItem.setRussian(refLocale.getRussian());
        }

        manager.setSelected(item.getManagers() != null ? new SelectItem(item.getManagerID(), item.getManagers()) : new SelectItem(Utils.getUserID(), Utils.getUserFullName()));

        manager.setSelected(Utils.getUserID());
        if (item.getManager() != null) {
            manager.setSelected(item.getManager());
        }

        //position
        position.setProvider(new PositionPanel.PositionProvider() {
            @Override
            public void employeePositions(PositionPanel.PositionCallback callback) {
                callback.onSuccess(item.getPositions());
            }

            @Override
            public void createPosition(NewPosition position, final PositionPanel.PositionCallback callback) {
                ReportService.App.get().createPosition(position, new AbstractAsyncCallback<Integer>() {
                    public void failure(Throwable caught) {
                        callback.onFailure();
                    }

                    public void success(Integer result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
        if (positionId != null) {
            position.setSelectedPosition(positionId);
            setPositionItems();
        }
        position.getPosition().getSuggestBox().addSelectionHandler(e -> {
            setPositionItems();
        });
        PositionItem positionItem = item.getPositionItem();
        if (positionItem != null) {
            position.setSelectedPosition(positionItem.getObjectID());

        }

        if (position.getSelectedPosition() != null && item.getObjectID() != null) {
            setPositionItems();
        }

        //job type
        jobType.setItems(item.getTimeTypes());
        jobType.setSelected(item.getJobType());
        //job family
        if (item.getJobfamily() != null) {
            jobFamily.setSelectedJobFamily(item.getJobfamily().getId());
        }
        //status
        SelectItem[] statuses = item.getStatuses();
        status.setItems(statuses);
        for (SelectItem st : statuses) {
            if (VacancyItem.VS_OPEN.equals(st.getDescription())) {
                status.setSelected(st.getId());
                break;
            }
        }
        //status id
        if (item.getStatus() != null) {
            status.setSelected(item.getStatus());
        }

        //set data- by default autos-elect CreatorUser's location
        if (objectID == null && !isCopying) {
            location.setSelected(item.getCreatorLocation());
        } else {
            location.setSelected(item.getLocation());
        }

        if (item.getCurrency() != null) {
            currencyWidget.setCurrency(item.getCurrency().getId());
        }

        //set data- by default autos-elect CreatorUser's department
        if (objectID == null) {
            department.setSelected(item.getCreatorDepatment());
        } else {
            department.setSelected(item.getDepartment());
        }

        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            department.getFilterParametrs().setLocationId(location.getSelectedItemID());
            position.getPosition().getFilterParametrs().setDepartmentId(department.getSelectedItemID());
        }

        //job title
        jobTitle.setText(item.getJobTitle() != null ? item.getJobTitle() : "");

        //description
        description.getMaterialRichEditor().setData(item.getDescription() != null ? item.getDescription() : "");
        if (mapHasValueForLang(item.getDescriptionLocalize(), userLanguage)) {
            description.getMaterialRichEditor().setData(item.getDescriptionLocalize().get(userLanguage));
        }

        //start date
        if (item.getStartDate() != null) {
            startDate.setDate(item.getStartDate());
        }

        //end date
        if (item.getEndDate() != null) {
            endDate.setDate(item.getEndDate());
        }

        //Plan-Active Employees-Open places
        requestPlaceCount.setText(item.getVacantPlaces() != null ? item.getVacantPlaces().toString() : "");

        //responsibilities
        responsibilities.getMaterialRichEditor().setData(item.getResponsibility() != null ? item.getResponsibility() : "");
        if (mapHasValueForLang(item.getResponsibilitiesLocalize(), userLanguage)) {
            responsibilities.getMaterialRichEditor().setData(item.getResponsibilitiesLocalize().get(userLanguage));
        }

        //required degree
        requiredDegree.setItems(item.getRequiredDegrees());
        requiredDegree.setSelected(item.getRequiredDegree() != null ? item.getRequiredDegree().getObjectID() : null);
        if (objectID == null && item.getCurrentApprover() != null && item.getCurrentApprover().getExactEmployee() != null && Utils.getUserID().equals(item.getCurrentApprover().getExactEmployee().getId())) {
            approve.setVisible(true);
            submit.setVisible(false);
        }
        if (item.getSpokenLanguages() != null && item.getSpokenLanguages().size() > 0) {
            languagesWidget.setLanguages(item.getSpokenLanguages());
        }
        if (convertedFormId != null) {
            noteWidget.addConvertedNotes(item.getVacancyNotes());
            attachments.addAdditionalAttachments(item.getConvertedFileResources(), false);
        }
        addRelationBadgeCount();

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());
    }

    protected void addRelationBadgeCount() {
        if (this.objectID != null) {
            if (this.item.getRelations() != null) {
                this.link.setBadgeCount(this.item.getRelations().size());
            }
        }
    }

    protected void createLinkButton() {

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }

        });
    }

    //draw vacancy form
    protected void registerFields() {

        String vacancy_add_edit_view = "vacancy_add_edit_view_";
        //Internal details
        //numbering
        number = new Numbering();
        number.addStyleName(DEFAULT_WIDTH);
        number.getElement().setId(vacancy_add_edit_view + "number");
        number.setEnabled(false);

        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.showClearButton();
        project.setFullSearch(true);
        project.getElement().setId("project_dropdown");
        project.addStyleName(DEFAULT_WIDTH);
        project.getElement().setId(vacancy_add_edit_view + "project");

        //gender table male
        male = new KpiRadioButton("gender", wfmStrings.male());
        male.getElement().setId(vacancy_add_edit_view + "gender_male");
        //gender: female
        female = new KpiRadioButton("gender", wfmStrings.female());
        female.getElement().setId(vacancy_add_edit_view + "gender_female");
        irrelevantGender = new KpiRadioButton("gender", wfmStrings.irrelevantgender());
        irrelevantGender.getElement().setId(vacancy_add_edit_view + "gender_irrelevant");

        //gender table
        genderTable = new FlexTable();
        genderTable.setCellSpacing(3);
        genderTable.setWidget(0, 0, male);
        genderTable.setWidget(0, 1, female);
        genderTable.setWidget(0, 2, irrelevantGender);
        genderTable.getElement().setId(vacancy_add_edit_view + "genderTable");

        //Proposed Salary
        proposedSalary = new TextBox();
        proposedSalary.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(proposedSalary);
        proposedSalary.addValueChangeHandler(handler -> {
            setFormattedValueToPropsedSalary(handler.getValue());
        });
        proposedSalary.getElement().setId(vacancy_add_edit_view + "proposedSalary");
        proposedSalary.setWidth("50%");

        //Description
        jobRequirement = new TextAreaWithSuggestionPopup(wfmStrings.jobRequirements());
        jobRequirement.getMaterialRichEditor().setTitle(wfmStrings.jobRequirements());

        //Date Time
        dateTime = new DateTimePicker();
        dateTime.setAllDay(true);
        dateTime.getStartDatePicker().setDate(DateUtil.resetTime(new Date()));
        dateTime.setStartDate(DateUtil.resetTime(new Date()));
        dateTime.getDueDatePicker().setDate(DateUtil.getDayLastTime(new Date()));
        dateTime.setDueDate(DateUtil.getDayLastTime(new Date()));
        dateTime.getStartTime().setVisible(false);
        dateTime.getEndTime().setVisible(false);
        dateTime.getStartDatePicker().addValueChangeHandler(event -> dateTime.getStartDatePicker().removeStyleName(ERROR_FORM_STYLE));
        dateTime.getStartDatePicker().getElement().setId(vacancy_add_edit_view + "period_from");
        dateTime.getDueDatePicker().getElement().setId(vacancy_add_edit_view + "period_to");

        KpiDatePicker startDatePicker = dateTime.getStartDatePicker();
        KpiDatePicker dueDatePicker = dateTime.getDueDatePicker();
        InputGroup periodTab = new InputGroup(startDatePicker, dueDatePicker);
        periodTab.addStyleName(DEFAULT_WIDTH);

        addField(CustomFormConstants.VACANCY.CONTRACT_PERIOD, periodTab, getTitle(wfmStrings.contractPeriod()));

        vacancyType = new DataListBox();
        vacancyType.addStyleName(DEFAULT_WIDTH);
        vacancyType.getElement().setId(vacancy_add_edit_view + "vacancyType");

        CommonService.App.get().getVacancyTypeItems(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(final SelectItem[] vacancyTypeItems) {
                Scheduler.get().scheduleDeferred(() -> {
                    vacancyType.setItems(vacancyTypeItems);
                    addPredefinedValues(CustomFormConstants.VACANCY.VACANCY_TYPE, vacancyTypeItems);
                    if (item != null && item.getVacancyType() != null) {
                        vacancyType.setSelected(item.getVacancyType());
                    }
                });
            }
        });


        manager = new EmployeeLookUp(true, PermissionConstants.HRMS_ADD_VACANCY);  //TO DO NEED TO CREATE A new permission and then change HRMS_ADD_VACANCY
        String nickDebugId = "add_vacancy_view";
        manager.ensureDebugId(nickDebugId + "assignee_list");

        //Position
        position = new PositionPanel();
        position.addStyleName(DEFAULT_WIDTH);
        position.getElement().setId(vacancy_add_edit_view + "position");

        //Location
        location = new LocationLookUpWithCode();
        location.addStyleName(DEFAULT_WIDTH);
        location.getSuggestBox().addSelectionHandler(e -> {
            position.getPosition().clear();
            plannedPlaceCount.setValue(null);
            if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                department.clear();
                department.getFilterParametrs().setLocationId(location.getSelectedItemID());
                clearPositionFilter();
            }
        });
        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            location.setClearCommand(this::clearDepartmentAndPosition);
            location.getTextBox().addKeyDownHandler(e -> clearDepartmentAndPosition());
        }

        locationPanel1 = new AdvancedInputGroup(location);
        locationPanel1.ensureDebugId("employee_location");
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_LOCATION)) {
            locationPanel1.setAppender("ficon--plus");
            locationPanel1.appenderClickHandler(() -> goTo("location|add/add"));
        }

        //job title
        jobTitle = new TextBox();
        jobTitle.addStyleName(DEFAULT_WIDTH);
        jobTitle.getElement().setId(vacancy_add_edit_view + "job_title");
        localeItem = new ReferenceLocale();

        locale = new WfmButton2(wfmStrings.vacancyLocale());
        locale.setStyleName("font-style: italic;", true);
        locale.addClickHandler(event -> {
            if (localeView == null) {
                localeView = new AddEditLocaleView(jobTitle.getText(), localeItem);
            } else {
                localeView.setLocaleItem(localeItem);
                localeView.setNameValue(jobTitle.getText());
                localeView.showView();
            }
        });
        locale.ensureDebugId(jobTitle + "locale");
        localedNameBox = new FlexTable();
        localedNameBox.addStyleName("formLine-table");
        localedNameBox.setWidget(0, 0, jobTitle);
        localedNameBox.setWidget(0, 1, locale);
        localedNameBox.getCellFormatter().addStyleName(0, 1, "formLine-table__act");
        localedNameBox.ensureDebugId(jobTitle + "localedNameBox");

        //Department
        department = new DepartmentLookUp();
        department.ensureDebugId(vacancy_add_edit_view + "employee_department");
        department.addStyleName(DEFAULT_WIDTH);
        department.getElement().setId(vacancy_add_edit_view + "department");
        department.getSuggestBox().addSelectionHandler(e -> {
            position.getPosition().clear();
            plannedPlaceCount.setValue(null);
            if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                position.getPosition().getFilterParametrs().setDepartmentId(department.getSelectedItemID());
            }
        });
        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            department.setClearCommand(this::clearPositionFilter);
            department.getTextBox().addKeyDownHandler(e -> clearPositionFilter());
        }

        //description
        description = new TextAreaWithSuggestionPopup(wfmStrings.description());
        description.addStyleName("AddVacancyView--desctiption");
        description.setHeight("400");
        description.getMaterialRichEditor().getElement().setId(vacancy_add_edit_view + "description");

        //start date
        startDate = new DatePicker(DateUtil.resetTime(new Date())/*, true*/);
        startDate.setDate(DateUtil.resetTime(new Date()));
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.getElement().setId(vacancy_add_edit_view + "start_date");

        //end date
        endDate = new DatePicker(/*true*/);
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.getElement().setId(vacancy_add_edit_view + "end_date");

        //status
        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.getElement().setId(vacancy_add_edit_view + "status");

        //planned cell
        inputGroup = new Div("input-group");
        Div append = new Div("input-group-append");
        Span appendedText = new Span(wfmStrings.request());
        appendedText.addStyleName("input-group-text");
        append.add(appendedText);

        //planned cell
        plannedPlaceCount = new TextBox();
        new KpiToolTip(plannedPlaceCount, hrmsStrings.plannedPosition());
        Validation.addNumericKeyboardListener(plannedPlaceCount);
        plannedPlaceCount.getElement().setId(vacancy_add_edit_view + "vacant_place_count");
        plannedPlaceCount.addStyleName(DEFAULT_WIDTH);
        plannedPlaceCount.setEnabled(false);
        inputGroup.add(plannedPlaceCount);
        plannedPlaceCount.setPlaceHolder(wfmStrings.vacantPlaceCount());
        inputGroup.add(plannedPlaceCount);

        //actual cell
        actualPlaceCount = new TextBox();
        new KpiToolTip(actualPlaceCount, hrmsStrings.actualHeadcount());
        actualPlaceCount.setPlaceHolder(wfmStrings.permissible());
        Validation.addNumericKeyboardListener(actualPlaceCount);
        actualPlaceCount.setEnabled(false);
        inputGroup.add(actualPlaceCount);

        //requested cell
        requestPlaceCount = new TextBox();
        new KpiToolTip(requestPlaceCount, hrmsStrings.requestQuantity());
        Validation.addPositiveNonZeroNumericKeyboardListener(requestPlaceCount);
        inputGroup.add(requestPlaceCount);
        inputGroup.add(append);

        //job type
        jobType = new DataListBox();
        jobType.addStyleName(DEFAULT_WIDTH);
        jobType.getElement().setId(vacancy_add_edit_view + "job_type");

        //currency
        currencyWidget = new CurrencyWidget();
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.getElement().setId(vacancy_add_edit_view + "cerrency");

        //job family
        jobFamily = new JobFamilyPanel(new JobFamilyPanel.JobFamilyProvider() {
            @Override
            public void jobFamilies(final JobFamilyPanel.JobFamilyCallback callback) {
                HrmsService.App.get().getJobFamilies(new AbstractAsyncCallback<SelectItem[]>() {
                    public void failure(Throwable caught) {
                        callback.onFailure();
                    }

                    public void success(SelectItem[] result) {
                        callback.onSuccess(result);
                    }
                });
            }

            @Override
            public void createJobFamily(SelectItem newJobFamily, final JobFamilyPanel.JobFamilyCallback callback) {
                HrmsService.App.get().createJobFamily(newJobFamily, new AbstractAsyncCallback<Integer>() {
                    public void failure(Throwable caught) {
                        callback.onFailure();
                    }

                    public void success(Integer result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
        jobFamily.getElement().setId(vacancy_add_edit_view + "job_family");

        //responsibilities
        responsibilities = new TextAreaWithSuggestionPopup(wfmStrings.responsibilities());
        responsibilities.getElement().setId(vacancy_add_edit_view + "responsibilities");
        responsibilities.setHeight("400");

        //required degree
        requiredDegree = new DataListBox();
        requiredDegree.addStyleName(DEFAULT_WIDTH);
        requiredDegree.getElement().setId(vacancy_add_edit_view + "required_degree");

        //vacancy attachments
        attachments = new GeneralFileUpload(F_VACANCY, objectID, objectID);
        attachments.getElement().setId(vacancy_add_edit_view + "attachment");

        //notes
        noteWidget = new NoteWidget(objectID, VACANCY);
        noteWidget.getTextBox().getElement().setId(vacancy_add_edit_view + "notes");

        vacancyQuestionsWidget = new VacancyQuestionsWidget(false);
        vacancyQuestionsWidget.getElement().setId(vacancy_add_edit_view + "vacancy_questions");



        languagesWidget = new SpokenLanguagesWidget(null);
        languagesWidget.addStyleName(DEFAULT_WIDTH);
        languagesWidget.getElement().setId("vacancy_add_view_language");

        this.addTitleField(CustomFormConstants.VACANCY.BASIC_INFORMATION, wfmStrings.meetingNotesAttachments());
        this.addTitleField(CustomFormConstants.VACANCY.DETAILED_INFORMATION, wfmStrings.detailedInformation());
        this.addTitleField(CustomFormConstants.VACANCY.INTERNAL_DETAILS, AddVacancyView.wfmStrings.internalDetails());
        this.addTitleField(CustomFormConstants.VACANCY.POSITION_INFORMATION, AddVacancyView.wfmStrings.positionInformation());
        if (formPropertyMap != null && formPropertyMap.get(LANGUAGE) != null) {
            addField(LANGUAGE, languagesWidget, getTitle(formPropertyMap.get(CustomFormConstants.LANGUAGE).isChanged() ? formPropertyMap.get(CustomFormConstants.LANGUAGE).getTitle() : wfmStrings.spokenLanguages(), formPropertyMap.get(CustomFormConstants.LANGUAGE).isRequired()));
        } else {
            addField(LANGUAGE, languagesWidget, wfmStrings.spokenLanguages());
        }
        addField(VACANCY_QUESTIONS, vacancyQuestionsWidget,"",true);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS) != null) {
            this.addField(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS, this.attachments, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS).getTitle() : wfmStrings.attachments(), this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS).isRequired()));
        } else {
            addField(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS, attachments, wfmStrings.attachments());
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES) != null) {
            this.addField(CustomFormConstants.VACANCY.VACANCY_NOTES, this.noteWidget, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES).getTitle() : wfmStrings.notes(), this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES).isRequired()));
            this.noteWidget.getTextBox().setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES).isDisabled());
        } else {
            this.addField(CustomFormConstants.VACANCY.VACANCY_NOTES, this.noteWidget, wfmStrings.notes());
        }

        //Number
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER) != null) {
            this.addField(CustomFormConstants.VACANCY.VACANCY_NUMBER, number, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).getTitle() : AddVacancyView.wfmStrings.number(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isInformation());
            this.number.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isDisabled());
            if (formPropertyMap.get(VACANCY_NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(VACANCY_NUMBER).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.VACANCY_NUMBER, this.number, AddVacancyView.wfmStrings.number());
        }

        //Manager
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER) != null) {
            this.addField(CustomFormConstants.VACANCY.MANAGER, manager, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).getTitle() : hrmsStrings.orderedBy(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isInformation());
            this.manager.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isInformation()) {
                new KpiToolTip(manager, formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.MANAGER, this.manager, wfmStrings.manager());
        }

        //Position
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION) != null) {
            this.addField(CustomFormConstants.VACANCY.POSITION, position, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).getTitle() : AddVacancyView.wfmStrings.position(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isInformation());
            this.position.getPosition().setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isInformation()) {
                new KpiToolTip(position, formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).getInformationText());
            }
            if (formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isApprovalRelated()) {
                position.getPosition().getSuggestBox().addSelectionHandler(event -> {
                    approvers.getFilterParameter().setPositionID(position.getSelectedPosition());
                    approvers.reloadApproverWidgets(Constants.VACANCY_FORM, objectID);
                });
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.POSITION, this.position, AddVacancyView.wfmStrings.position());
        }

        //Department
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT) != null) {
            this.addField(CustomFormConstants.VACANCY.DEPARTMENT, department, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).getTitle() : wfmStrings.department(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isInformation());
            this.department.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isInformation()) {
                new KpiToolTip(department, formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.DEPARTMENT, this.department, getTitle(wfmStrings.department()));
        }

        //Location
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION) != null) {
            this.addField(CustomFormConstants.VACANCY.LOCATION, locationPanel1, this.getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).isInformation());
            location.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).isDisabled());
            if (formPropertyMap.get(LOCATION).isInformation()) {
                new KpiToolTip(locationPanel1, formPropertyMap.get(LOCATION).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.LOCATION, locationPanel1, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).isRequired());
        }

        //Job Title
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE) != null) {
            this.addField(CustomFormConstants.VACANCY.JOB_TITLE, localedNameBox, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getTitle() : wfmStrings.name(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isInformation());
            this.jobTitle.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isInformation()) {
                new KpiToolTip(localedNameBox, formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.JOB_TITLE, this.localedNameBox, wfmStrings.name());
        }

        //Project
        if (this.formPropertyMap != null & this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT) != null) {
            this.addField(CustomFormConstants.VACANCY.PROJECT, project, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).getTitle() : wfmStrings.project(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isInformation());
            this.project.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isInformation()) {
                new KpiToolTip(project, formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.PROJECT, this.project, wfmStrings.project());
        }

        //Gender
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER) != null) {
            this.addField(CustomFormConstants.VACANCY.GENDER, genderTable, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).getTitle() : CustomForm2.wfmStrings.sexDesire(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isInformation());
            this.male.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isDisabled());
            this.female.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isDisabled());
            this.irrelevantGender.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isInformation()) {
                new KpiToolTip(genderTable, formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.GENDER, this.genderTable, CustomForm2.wfmStrings.sexDesire());
        }

        //Proposed Salary
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY) != null) {
            this.addField(CustomFormConstants.VACANCY.PROPOSED_SALARY, new InputGroup(proposedSalary, currencyWidget), this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).getTitle() : wfmStrings.proposedSalary(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).isRequired()), false,
                    formPropertyMap.get(PROPOSED_SALARY).isInformation());
            this.proposedSalary.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).isDisabled());
            if (formPropertyMap.get(PROPOSED_SALARY).isInformation()) {
                new KpiToolTip(new InputGroup(proposedSalary, currencyWidget), formPropertyMap.get(PROPOSED_SALARY).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.PROPOSED_SALARY, new InputGroup(proposedSalary, currencyWidget), wfmStrings.proposedSalary());
        }

        //Job Requirement
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT) != null) {
            this.jobRequirement = new TextAreaWithSuggestionPopup(this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).getTitle() : wfmStrings.jobRequirements());
            this.addField(CustomFormConstants.VACANCY.JOB_REQUIREMENT, jobRequirement, null);
            this.jobRequirement.getMaterialRichEditor().setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).isInformation()) {
                new KpiToolTip(jobRequirement, formPropertyMap.get(CustomFormConstants.VACANCY.JOB_REQUIREMENT).getInformationText());
            }

        } else {
            this.jobRequirement = new TextAreaWithSuggestionPopup(wfmStrings.jobRequirements());

        }
        this.jobRequirement.addStyleName("jobRequirement");
        jobRequirement.getMaterialRichEditor().getElement().setId(vacancy_add_edit_view + "description");

        //Vacancy Type
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE) != null) {
            this.addField(CustomFormConstants.VACANCY.VACANCY_TYPE, vacancyType, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).getTitle() : AddVacancyView.wfmStrings.vacancyType(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).isRequired()), false,
                    formPropertyMap.get(VACANCY_TYPE).isInformation());
            this.vacancyType.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).isDisabled());
            if (formPropertyMap.get(VACANCY_TYPE).isInformation()) {
                new KpiToolTip(vacancyType, formPropertyMap.get(VACANCY_TYPE).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.VACANCY_TYPE, this.vacancyType, AddVacancyView.wfmStrings.vacancyType());
        }

        //Description
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION) != null) {
            //description
            this.description = new TextAreaWithSuggestionPopup(this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).getTitle() : wfmStrings.description());
            this.addField(CustomFormConstants.VACANCY.DESCRIPTION, description, null);
            this.description.getMaterialRichEditor().setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).getInformationText());
            }

        } else {
            this.description = new TextAreaWithSuggestionPopup(wfmStrings.description());

        }
        description.addStyleName("AddVacancyView--desctiption");
        description.setHeight("400");
        description.getMaterialRichEditor().getElement().setId(vacancy_add_edit_view + "description");

        //Start Date
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE) != null) {
            InputGroup period = new InputGroup(startDate, endDate);
            this.addField(CustomFormConstants.VACANCY.START_DATE, period, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getTitle() : wfmStrings.closePeriod(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isInformation());
            this.startDate.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isDisabled());
            this.endDate.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isInformation()) {
                new KpiToolTip(period, formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).getInformationText());
            }
        } else {
            this.addField(CustomFormConstants.VACANCY.START_DATE, new InputGroup(this.startDate, this.endDate), wfmStrings.period());
        }

        //Status
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS) != null) {
            this.addField(CustomFormConstants.VACANCY.STATUS, status, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isChanged()
                            ? this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).getTitle() : wfmStrings.status(),
                    this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isRequired()), false, formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isInformation()) {
                new KpiToolTip(status, formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).getInformationText());
            }
            this.status.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isDisabled());
        } else {
            this.addField(CustomFormConstants.VACANCY.STATUS, this.status, wfmStrings.status());
        }

        //Place Count
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT) != null) {
            this.addField(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT, inputGroup, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).getTitle() : AddVacancyView.wfmStrings.vacantPlaceCount(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).isRequired()), false,
                    formPropertyMap.get(VACANCY_PLACE_COUNT).isInformation());
            if (formPropertyMap.get(VACANCY_PLACE_COUNT).isInformation()) {
                new KpiToolTip(inputGroup, formPropertyMap.get(VACANCY_PLACE_COUNT).getInformationText());
            }

        } else {
            this.addField(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT, this.inputGroup, AddVacancyView.wfmStrings.vacantPlaceCount() + "/ " + AddVacancyView.wfmStrings.planned() + "/ " + AddVacancyView.wfmStrings.actual() + "/ " + AddVacancyView.wfmStrings.request());
        }

        //Job Type
        if (this.formPropertyMap != null & this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE) != null) {
            this.addField(CustomFormConstants.VACANCY.JOB_TYPE, jobType, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).getTitle() : AddVacancyView.wfmStrings.jobType(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isInformation()) {
                new KpiToolTip(jobType, formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).getInformationText());
            }
            this.jobType.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isDisabled());
        } else {
            this.addField(CustomFormConstants.VACANCY.JOB_TYPE, this.jobType, AddVacancyView.wfmStrings.jobType());
        }

        //Responsibilities
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES) != null) {
            this.responsibilities = new TextAreaWithSuggestionPopup(this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).getTitle() : AddVacancyView.wfmStrings.responsibilities());
            this.addField(CustomFormConstants.VACANCY.RESPONSIBILITIES, responsibilities, null);
            this.responsibilities.getMaterialRichEditor().setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).isInformation()) {
                new KpiToolTip(responsibilities, formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).getInformationText());
            }
        } else {
            this.responsibilities = new TextAreaWithSuggestionPopup(AddVacancyView.wfmStrings.responsibilities());
        }
        this.responsibilities.getElement().setId(vacancy_add_edit_view + "responsibilities");
        responsibilities.setHeight("400");
        responsibilities.getMaterialRichEditor().getElement().setId(vacancy_add_edit_view + "description");

        //Required Degree
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE) != null) {
            this.addField(CustomFormConstants.VACANCY.REQUIRED_DEGREE, requiredDegree, this.getTitle(this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).getTitle() : AddVacancyView.wfmStrings.requiredDegree(),
                            this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).isRequired()), false,
                    formPropertyMap.get(REQUIRED_DEGREE).isInformation());
            if (formPropertyMap.get(REQUIRED_DEGREE).isInformation()) {
                new KpiToolTip(requiredDegree, formPropertyMap.get(REQUIRED_DEGREE).getInformationText());
            }
            this.requiredDegree.setEnabled(!this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).isDisabled());
        } else {
            this.addField(CustomFormConstants.VACANCY.REQUIRED_DEGREE, this.requiredDegree, AddVacancyView.wfmStrings.requiredDegree());
        }

        jobTitleLocalize = new MultiLanguageTextAreaWidget(this.jobTitle.getTitle());
        responsibilityLocalize = new MultiLanguageRichEditorWidget(this.responsibilities);
        jobRequirementLocalize = new MultiLanguageRichEditorWidget(this.jobRequirement);
        descriptionLocalize = new MultiLanguageRichEditorWidget(this.description);

        //Current approver
        approvers = new ChosenApproversWidget(Constants.VACANCY_FORM, objectID);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER) != null) {
            addField(CustomFormConstants.APPROVER, approvers, getTitle(formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isChanged()
                                    ? formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).getTitle() : wfmStrings.approver(),
                            formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isInformation()) {
                new KpiToolTip(approvers, formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).getInformationText());
            }
            approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isDisabled());
        } else {
            addField(CustomFormConstants.VACANCY.APPROVER, approvers, getTitle(wfmStrings.approver(), false));
        }

        this.getCustomFieldUtil().drawCustomFields(this, this.objectID);
        this.addTitleField(CustomFormConstants.VACANCY.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        this.show();
    }

    private void setFormattedValueToPropsedSalary(String value) {
        double numericValue = 0.0;
        if (value == null || value.isEmpty()) {
            value = "0.0";
        }
        numericValue = Utils.universalParse(Utils.getNumberFormat(), value);
        String formattedValue = Utils.getNumberFormat().format(numericValue).replace(".00", "");
        proposedSalary.setText(formattedValue);
    }

    @Override
    protected void enableButton(boolean enable) {
        approve.setEnabled(enable);
        draftButton.setEnabled(enable);
        submit.setEnabled(enable);
    }

    private void save(String status) {


        enableButton(false);
        if (!validate(status)) {
            enableButton(true);
            return;
        }
        setValues();
        item.setApprovalStatusCode(status);
        if (firstClick.get()) {
            item.setRelations(item != null ? item.getRelations() : null);
        } else {
            item.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        //save vacancy logic
        LoadingPanel.loading(true);
        RecruitmentService.App.get().saveVacancy(item, new AbstractAsyncCallback<Integer>() {


            public void success(Integer result) {
                LoadingPanel.loading(false);
                if (!("".equals(editNote))) {
                    RecruitmentService.App.get().createVacancyHistory(objectID, new HistoryListItem(editNote), new AbstractAsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Integer result) {

                        }
                    });
                }
                enableButton(true);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_ADDED, result, AddVacancyView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.vacancy()), Info.Type.INFO);
                closeTab();
                if (objectID != null && !Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(status)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|summary/" + objectID, item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getJobTitle(), item.getJobTitle());
                }
                if (formType != null && convertedFormId != null) {
                    saveConvertedRelations(result);
                }
            }

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }
        });
    }

    private void saveConvertedRelations(final Integer _objectId) {
        final ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_VACANCY, wfmStrings.vacancy() + " - " + item.getNumberData().getNumberString(), this.convertedFormId, this.formType, this.item.getRelationName() + " - " + convertedFormId));
        AllInOneService.App.get().saveRelations(RelationItem.TYPE_VACANCY, _objectId, wfmStrings.vacancy() + " - " + item.getNumberData().getNumberString(), relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
            @Override
            public void failure(final Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ArrayList<RelationItem> selectItems) {
                LoadingPanel.loading(false);
            }
        });
    }

    private void setValues() {
        if (item == null) {
            item = new VacancyItem();
        }
        item.setObjectID(this.objectID);
        item.setNumberData(number.getNumberData(false));

        item.setProjectId(project.getSelectedItemID());

        if (male.getValue()) {
            item.setGender(Constants.MALE);
        }
        if (female.getValue()) {
            item.setGender(Constants.FEMALE);
        }
        if (irrelevantGender.getValue()) {
            item.setGender(Constants.IRRELEVANT_GENDER);
        }
//        Validation.numberValidation(proposedSalary);
        item.setProposedSalary(String.valueOf(Utils.universalParse(Utils.getNumberFormat(), proposedSalary.getText())));
        item.setJobRequirements(jobRequirement.getMaterialRichEditor().getData());
        item.setJobRequirementLocalize(jobRequirementLocalize.getValueMap());

        item.setVacancyQuestionItems(vacancyQuestionsWidget.getTableItems());


        if (dateTime.getStartDatePicker().getDate() != null && dateTime.getStartDate() != null) {
            item.setContractFrom(dateTime.getStartDate());
        }
        if (dateTime.getDueDatePicker().getDate() != null && dateTime.getDueDate() != null) {
            item.setContractTo(dateTime.getDueDate());
        }
        if (vacancyType.getSelectedId() != null) {
            item.setVacancyType(vacancyType.getSelectedId());
            item.setVacancyTypeName(vacancyType.getSelectedItem().getName());
        }

        item.setManager(manager.getSelectedItem());

        item.setCurrency(currencyWidget.getCurrency());

        PositionItem positionItem = new PositionItem();
        positionItem.setObjectID(position.getSelectedPosition());
        item.setPositionItem(positionItem);
        item.setJobfamily(jobFamily.getSelectedJobFamily());
        item.setJobType(jobType.getSelectedItem());
        if (location.getSelectedItem() != null) {
            item.setLocationItem(new LocationItem(location.getSelectedItem().getId().toString(), null, location.getSelectedItem().getName()));
        }
        item.setJobTitle(jobTitle.getText());
        item.setJobTitleLocalize(jobRequirementLocalize.getValueMap());
        item.setDescription(description.getMaterialRichEditor().getData());
        item.setDescriptionLocalize(descriptionLocalize.getValueMap());
        item.setStartDate(startDate.getDate());
        item.setEndDate(endDate.getDate());
        localeItem = localeView != null ? localeView.getLocaleItem() : localeItem;
        item.setReferenceLocale(localeItem);
        //department
        if (department.getSelectedItem() != null) {
            item.setDepartment(department.getSelectedItem());
        }

        if (status.getSelectedItem() != null) {
            item.setStatus(new ReferenceItem(status.getSelectedItem().getId(), status.getSelectedItem().getName()));
        }
        if (requestPlaceCount.getText() != null && !"".equals(requestPlaceCount.getText())) {
            item.setVacantPlaces(Integer.valueOf(requestPlaceCount.getText()));
        }
        item.setResponsibility(responsibilities.getMaterialRichEditor().getData());
        item.setResponsibilitiesLocalize(responsibilityLocalize.getValueMap());
        if (requiredDegree.getSelectedItem() != null) {
            item.setRequiredDegree(new ReferenceItem(requiredDegree.getSelectedItem().getId(), requiredDegree.getSelectedItem().getName()));
        }
        item.setApprovers(approvers.getChosenApprovers());
        item.setVacancyNotes(noteWidget.getNewNotesToSave());
        item.setAttachments(attachments.getAttachedFiles());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        if (languagesWidget != null) {
            item.setSpokenLanguages(languagesWidget.getLanguages());
        }
        if (getCustomObjectData().size() > 0) {
            item.setCustomTableItems(getCustomObjectData());
        }
    }

    private HashMap<String, ArrayList<CustomTableRpc>> getCustomObjectData() {
        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
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

    private boolean validate(String status) {
        int errors = 0;
        this.clearErrorStyle();

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.JOB_TITLE, this.jobTitle, !Validation.validateTextBoxRequiredAndCharLimit(this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isChanged() ?
                    this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), this.jobTitle, this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getMinChar()));
        }

        if (Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(status)) {
            if ("".equals(jobTitle.getText())) {
                markAsError(jobTitle, !Validation.validateTextBoxRequired(jobTitle));
                return false;
            }
            return true;
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isRequired()) {
            TextBox comperator = new TextBox();
            comperator.setText(this.number.getTxtNumber().getText() + this.number.getTxtPrefix().getText());
            errors += this.markAsError(CustomFormConstants.VACANCY.VACANCY_NUMBER, this.number, !Validation.validateTextBoxRequiredAndCharLimit(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).isChanged() ? this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).getTitle() : wfmStrings.number(), comperator, this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NUMBER).getMinChar()));
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.POSITION).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.POSITION, this.position.getPosition(), position.getSelectedPosition() == null);
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.JOB_TITLE, this.jobTitle, !Validation.validateTextBoxRequiredAndCharLimit(this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).isChanged() ?
                    this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), this.jobTitle, this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TITLE).getMinChar()));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DEPARTMENT).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.DEPARTMENT, this.department, !Validation.validateLookUpRequired(this.department));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.START_DATE, this.startDate, !Validation.validateDate(this.startDate, new HTML(), true));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.END_DATE, this.endDate, !Validation.validateDate(this.endDate, new HTML(), true));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS, this.attachments, this.attachments.getAttachedFiles() == null);
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_NOTES).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.VACANCY_NOTES, this.noteWidget, (this.noteWidget.getTextBox() == null));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.MANAGER).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.MANAGER, this.manager, this.manager.getSelectedItem() == null);
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.LOCATION).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.LOCATION, this.location, this.location.getSelectedItem() == null);
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.PROJECT).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.PROJECT, this.project, this.project.getSelectedItem() == null);
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.GENDER).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.GENDER, this.genderTable, !this.male.getValue() && !this.female.getValue() && !this.irrelevantGender.getValue());
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.PROPOSED_SALARY, this.proposedSalary, !Validation.validateTextBoxRequiredAndCharLimit(this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).isRequired() ?
                    this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).getTitle() : wfmStrings.proposedSalary(), this.proposedSalary, this.formPropertyMap.get(CustomFormConstants.VACANCY.PROPOSED_SALARY).getMinChar()));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(JOB_REQUIREMENT) != null && this.formPropertyMap.get(JOB_REQUIREMENT).isRequired()) {
            errors += this.markAsError(JOB_REQUIREMENT, this.jobRequirement, Utils.isNullOrEmpty(this.jobRequirement.getMaterialRichEditor().getData()));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_TYPE).isRequired()) {
            errors += this.markAsError(CustomFormConstants.VACANCY.VACANCY_TYPE, this.vacancyType, Utils.isNullOrEmpty(this.vacancyType.getName()));
        }


        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.DESCRIPTION).isRequired()) {

        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.STATUS).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.STATUS, this.status, this.status.getSelectedItem() == null);
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT, this.plannedPlaceCount, !Validation.validateTextBoxRequiredAndCharLimit(this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).isChanged() ?
                    this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).getTitle() : wfmStrings.vacantPlaceCount(), this.plannedPlaceCount, this.formPropertyMap.get(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT).getMinChar()));
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.JOB_TYPE).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.JOB_TYPE, this.jobType, this.jobType.getSelectedItem() == null);
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.RESPONSIBILITIES).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.RESPONSIBILITIES, this.responsibilities, Utils.isNullOrEmpty(this.responsibilities.getMaterialRichEditor().getData()));
        }
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.REQUIRED_DEGREE).isRequired()) {

            errors += this.markAsError(CustomFormConstants.VACANCY.REQUIRED_DEGREE, this.requiredDegree, this.requiredDegree.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER) != null && formPropertyMap.get(CustomFormConstants.VACANCY.APPROVER).isRequired() && !approvers.isValid()) {
            errors++;
        }

        String actualValue = actualPlaceCount.getValue();
        String requestValue = requestPlaceCount.getValue();
        String plannedValue = plannedPlaceCount.getValue();

        int actualCount = (actualValue != null && !actualValue.isEmpty()) ? Integer.parseInt(actualValue) : 0;
        int requestCount = (requestValue != null && !requestValue.isEmpty()) ? Integer.parseInt(requestValue) : 0;
        int plannedCount = (plannedValue != null && !plannedValue.isEmpty()) ? Integer.parseInt(plannedValue) : 0;

        if (actualCount + requestCount > plannedCount || requestCount == 0) {
            errors++;
            this.markAsError(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT, this.requestPlaceCount, true);
        }

        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.START_DATE).isRequired()
                && this.formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE) != null && this.formPropertyMap.get(CustomFormConstants.VACANCY.END_DATE).isRequired()) {
            if (this.markAsError(this.startDate, !Validation.validateDateEqualOrAfter(this.startDate.getDate(), this.endDate.getDate(), true)) > 0) {
                errors++;
            }
            if (this.formPropertyMap != null && this.dateTime.getStartDatePicker().getDate() != null && this.dateTime.getDueDatePicker().getDate() != null) {
                errors += this.markAsError(CustomFormConstants.PERIOD, this.dateTime.getStartDatePicker(), !Validation.validateDateOrder(this.dateTime.getStartDatePicker().getDate(), this.dateTime.getDueDatePicker().getDate(), null, this.dateTime.isAllDay()));
            }
        }
        errors += this.getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddVacancyView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (objectID == null && item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approve.setVisible(true);
                        submit.setVisible(false);
                    } else {
                        approve.setVisible(false);
                        submit.setVisible(true);
                    }
                });
                if (approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (objectID == null && item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approve.setVisible(true);
                        submit.setVisible(false);
                    } else {
                        approve.setVisible(false);
                        submit.setVisible(true);
                    }
                }
            } else {
                approve.setVisible(false);
                submit.setVisible(true);
            }
        });
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

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddVacancyView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_VACANCY;
                }

                @Override
                public String getRelationName() {
                    return item.getRelationName();
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void clearPositionFilter() {
        position.getPosition().clear();
        position.getPosition().getFilterParametrs().setDepartmentId(null);
    }

    private void clearDepartmentAndPosition() {
        department.clear();
        department.getFilterParametrs().setLocationId(null);
        clearPositionFilter();
    }

    private void setPositionItems() {
        HrmsService.App.get().getPositionForEdit(position.getSelectedPosition(), null, new AsyncCallback<PositionItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(PositionItem result) {

                plannedPlaceCount.setValue(wfmStrings.vacantPlaceCount() + ": " + result.getCount());
                actualPlaceCount.setValue(wfmStrings.actual() + ": " + result.getHeadCount().toString());

                plannedPlaceCount.setValue(result.getCount());
                actualPlaceCount.setValue(result.getHeadCount().toString());

                if (!(Integer.parseInt(result.getCount()) > result.getHeadCount())) {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(hrmsStrings.actualMorePlan());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                            closeTab();
                        }
                    });
                    message.open();
                }

                location.setSelected(result.getLocation());
                department.setSelected(result.getDepartment());
                if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                    department.getFilterParametrs().setLocationId(location.getSelectedItemID());
                }

//  Due to changes in positon form this is no longer valid to be filled for vacancy form.

//
//                String userLanguage = Utils.getUserLanguage();
//
//                if (mapHasValueForLang(result.getJobRequirementLocalize(), userLanguage)) {
//                    jobRequirement.getMaterialRichEditor().setData(result.getJobRequirementLocalize().get(userLanguage));
//                    jobRequirementLocalize.setValueMap(result.getJobRequirementLocalize());
//                } else if (result.getJobRequirements() != null) {
//                    jobRequirement.getMaterialRichEditor().setData(result.getJobRequirements());
//                } else {
//                    jobRequirement.getMaterialRichEditor().setData("");
//                    jobRequirementLocalize.setValueMap(new HashMap<>());
//                }
//
//                if (mapHasValueForLang(result.getDescriptionLocalize(), userLanguage)) {
//                    description.getMaterialRichEditor().setData(result.getDescriptionLocalize().get(userLanguage));
//                    descriptionLocalize.setValueMap(result.getDescriptionLocalize());
//                } else if (result.getPositionDescription() != null) {
//                    description.getMaterialRichEditor().setData(result.getPositionDescription());
//                } else {
//                    description.getMaterialRichEditor().setData("");
//                    descriptionLocalize.setValueMap(new HashMap<>());
//                }
//
//                if (mapHasValueForLang(result.getResponsibilitiesLocalize(), userLanguage)) {
//                    responsibilities.getMaterialRichEditor().setData(result.getResponsibilitiesLocalize().get(userLanguage));
//                    responsibilityLocalize.setValueMap(result.getResponsibilitiesLocalize());
//                } else if (result.getResponsibility() != null) {
//                    responsibilities.getMaterialRichEditor().setData(result.getResponsibility());
//                } else {
//                    responsibilities.getMaterialRichEditor().setData("");
//                    responsibilityLocalize.setValueMap(new HashMap<>());
//                }
            }
        });
    }

}
