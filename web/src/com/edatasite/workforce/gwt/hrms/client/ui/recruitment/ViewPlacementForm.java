package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CandidatePercentageStageModal;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Ilhombek
 * Date: 7/5/12
 * Time: 8:09 PM
 */
public class ViewPlacementForm extends CustomForm2 implements Constants, Colapse, HasLinksInterface {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    private final Integer objectID;
    private HTML dateOffered, project, placementCode;
    private Div vacancies;
    private MaterialLink candidate;
    private MaterialLink location, department, position;
    private GeneralFileUpload fileUpload;
    private NoteWidget noteWidget;
    private final boolean isEditable;
    private FooterInformer link;
    private SplitButton printPdfSplitButton;
    public PlacementItem item;
    public ContactListItem item_contact;
    private Div inputGroup;
    private TextBox headCount, plannedPlaceCount;
    private HasLinks linkingUtil;
    private WfmButton2 submitButton, approveButton, declineButton, editButton;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    private NoteHistoryWidget noteHistoryWidget;

    public ViewPlacementForm(Integer objectID, boolean isEditable) {
        super("summary");
        this.objectID = objectID;
        this.isEditable = isEditable;
    }


    //    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_CANDIDATE;
    }

    public Widget onInitialize() {
        if (container != null && isEditable) {
            setCollapse(true);
        }
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Placement, LayoutRPC.PLACEMENT_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                ViewPlacementForm.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                ViewPlacementForm.super.onInitialize();
            }

        });

        CommonService.App.get().getCompanyCustomFields(ViewName.PlacementItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                ViewPlacementForm.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
//                if (result != null) {
//                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
//                }
                drawItemTable();
            }
        });
        return null;
    }

    @Override
    protected void addButtons() {
        //Footer=> Links=> on left side
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        //Footer=> History&Notes widget=> on left side
        noteHistoryWidget = new NoteHistoryWidget(callback -> RecruitmentService.App.get().loadPlacementNoteAndHistory(objectID, callback), false);
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);


        //Footer=> Options Button=> on right side
        customizeButton.setVisible(false);
        if (Utils.hasRole(Constants.ADMIN) || isEditable) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            if (isEditable) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    //register remove placement logic
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    wfmMessageBox.setTitle(wfmStrings.warning());
                    wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            RecruitmentService.App.get().deletePlacement(objectID, new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    LoadingPanel.loading(false);
                                    if (result) {
                                        Info.show(wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.placement()));
                                        closeTab();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_DELETE, result, ViewPlacementForm.this);
                                    }
                                }
                            });
                        }
                    });
                    wfmMessageBox.open();
                });
                options.add(deleteButton);
            }
            if (Utils.hasPermission(HRMS_PRINT_PDF_PLACEMENT)) {
                printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
                addRightButton(printPdfSplitButton);
            }
        }
        //Footer=> Edit Button=> on right side
        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("placement|editPlacement/" + objectID + "/" + isEditable, item.getCandidateName());
        });
        editButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> save(Constants.PLACEMENT_STATUS_APPROVED, null));
        approveButton.setVisible(false);

        declineButton = addButton(wfmStrings.reject(), BTN_REJECT, clickEvent -> {
            CandidatePercentageStageModal modal = new CandidatePercentageStageModal(objectID, true);
            modal.setModalCompleteListener(() -> {
                // This will be called when the modal completes its logic
                closeTab();
            });
        });
        declineButton.setVisible(false);

        submitButton = addButton(wfmStrings.submitForApproval(), wfmStrings.submit(), BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.PAYMENT_STATUS_SUBMITTED, null);
        });
        submitButton.setVisible(false);

        MaterialLink addButton = new MaterialLink(wfmStrings.add());
        MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);
        boolean callQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_PLACEMENT_QUICK_ADD_LOG_CALL);
        boolean callAddPermission = Utils.hasPermission(PermissionConstants.HRMS_PLACEMENT_LOG_CALL);
        if (callQuickAddPermission || callAddPermission) {
            MaterialLink button = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
            button.getElement().setId("candidate_summary_view_log_a_call_button");
            button.addClickHandler(event -> {
                if (callQuickAddPermission) {
                    if (item_contact.getCrmAccount() != null && item_contact.getCrmAccount().getObjectId() != null && item_contact.getCrmAccount().getName() != null) {
                        new ActivityQuickAddForm(Appointment.CALL_LOG,
                                RelationItem.newEventRelation(getRelationType(), item.getCandidateID(), item_contact.getName()),
                                RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT,
                                        item_contact.getCrmAccount().getObjectId(), item_contact.getCrmAccount().getName()));
                    } else {
                        new ActivityQuickAddForm(Appointment.CALL_LOG,
                               RelationItem.newEventRelation(getRelationType(), item_contact.getObjectId(), item_contact.getName()));
                    }
                } else {
                   SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.CALL_LOG + "/" + item_contact.getObjectId() + "/" + getRelationType());  //+ getRelationType()
               }
            });
            addSplitButton.addItem(button);

        }
        boolean interviewQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_QUICK_INTERVIEW_CANDIDATE);
        boolean interviewAddPermission = Utils.hasPermission(PermissionConstants.HRMS_INTERVIEW_CANDIDATE);
        if (interviewAddPermission || interviewQuickAddPermission) {
            MaterialLink interviewButton = new MaterialLink(wfmStrings.interview());
            interviewButton.getElement().setId("candidate_summary_view_interview_button");
            interviewButton.addClickHandler(event -> {
                if (interviewQuickAddPermission) {
                    if (item_contact.getCrmAccount() != null && item_contact.getCrmAccount().getObjectId() != null && item_contact.getCrmAccount().getName() != null) {
                        new ActivityQuickAddForm(Appointment.INTERVIEW,
                               RelationItem.newEventRelation(getRelationType(), item.getCandidateID(), item_contact.getName()),
                                RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT,
                                        item_contact.getCrmAccount().getObjectId(), item_contact.getCrmAccount().getName()));
                    } else {   new ActivityQuickAddForm(Appointment.INTERVIEW,
                               RelationItem.newEventRelation(getRelationType(), item.getCandidateID(), item_contact.getName()));
                    }
                } else {
                   SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.INTERVIEW + "/" + item_contact.getObjectId() + "/" + getRelationType()); //+ getRelationType()
                }
            });
            addSplitButton.addItem(interviewButton);
        }

       if (Utils.hasPermission(PermissionConstants.HRMS_CONDIDATE_SMS_SEND)) {
           MaterialLink addSms = new MaterialLink(wfmStrings.sms());
           addSms.addClickHandler(event -> {
            new ActivityQuickAddForm(Appointment.SMS, item_contact, null, RelationItem.newEventRelation(getRelationType(), item_contact.getObjectId(), item_contact.getName()));
           });
           addSplitButton.addItem(addSms);
        }  addRightButton(addSplitButton);
    }
    protected void hire() {
        SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add/" + FROM_HRMS + "/true/" + objectID);
    }

    private void renderButtons() {
        if (item.isApproveProcessEnabled()) {
            Integer currentApproverId = item.getApprover() != null ? item.getApprover().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (PLACEMENT_STATUS_SUBMITTED.equals(item.getStatusCode()) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            editButton.setVisible(Utils.hasPermission(HRMS_EDIT_PLACEMENT)
                    && isEditable
                    && (item.getCreator() != null && currentUserId.equals(item.getCreator().getId()) || currentUserId.equals(currentApproverId)));


            if (PAYMENT_STATUS_REJECTED.equals(item.getStatusCode()) && item.getCreator() != null && currentUserId.equals(item.getCreator().getId())) {
                submitButton.setVisible(true);
            }
        } else {
            editButton.setVisible(Utils.hasPermission(HRMS_EDIT_PLACEMENT) && isEditable);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_HIRE_PLACEMENT) && PLACEMENT_STATUS_APPROVED.equals(item.getStatusCode())) {
            WfmButton2 hireButton = addButton(wfmStrings.hireOnly(), WfmButton2.BTN_PRIMARY, event -> hire());
            hireButton.getElement().setId("placement_summary_view_hire_button");
        }

    }

    public void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.PLACEMENT_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        CustomFormItemGrid itemView = new CustomFormItemGrid(objectID, configMap.getKey(), LayoutRPC.PLACEMENT_FORM, configMap.getValue(), 1000);
                        ColumnConfigs[] value = configMap.getValue();
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    public void pdfTool(PlacementItem result) {
        if (printPdfSplitButton == null) {
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
        String pdfURL = CommandConstants.PDF_URL + "/placementInfoPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }


    @Override
    protected void getDataToFillFields() {
        RecruitmentService.App.get().getPlacementItem(objectID, null, null, new AbstractAsyncCallback<PlacementItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final PlacementItem result) {
                Scheduler.get().scheduleDeferred(() -> {

                    item = result;
                    setItemContact();
                    renderButtons();
                    fillFormWithData();
                    pdfTool(item);
                    setDefaultValues();
                });
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PLACEMENT_FORM;
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

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void fillFormWithData() {
//		super.fillFormWithData();
        //candidate
        candidate.setText(item.getCandidateName() != null ? item.getCandidateName() : "");
        candidate.addClickHandler(event -> {
            if (Constants.PLACEMENT_STATUS_HIRED.equals(item.getStatusCode())) {
                SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|employeeProfileView/" + item.getEmployeeProfileId());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + item.getCandidateID(), item.getCandidateName(), item.getCandidateName());
            }
        });

        //date offered
        dateOffered.setHTML(item.getDateOffed() != null ? DateUtils.format(item.getDateOffed()) + Utils.getHijriDate(item.getDateOffed()) : "");

        //location
        location.addClickHandler(event -> {
            if (hasPermissionToLocationSummary()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("location|summary/" + item.getLocationID(), item.getLocationName(), item.getLocationName());
            }
        });
        location.setText(item.getLocationName() != null ? item.getLocationName() : "");

        //department
        department.addClickHandler(event -> {
            if (hasPermissionToDepartmentSummary()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + item.getDepartmentID(), item.getDepartmentName(), item.getDepartmentName());
            }
        });
        department.setText(item.getDepartmentName() != null ? item.getDepartmentName() : "");

        //position
        position.addClickHandler(event -> {
            if (hasPermissionToPositionSummary()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|positionsummaryview/" + item.getPositionID(), item.getPositionName(), item.getPositionName());
            }
        });
        position.setText(item.getPositionName() != null ? item.getPositionName() : "");

        //project
        project.setHTML(item.getProjectName() != null ? item.getProjectName() : "");
        //placementCode
        setInnerHTML(placementCode, item.getNumberData() != null ? item.getNumberData().getFirstNumberString() : "");
        //vacancies
        setVacancyItems();
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);

        AtomicBoolean firstClick = new AtomicBoolean(true);
        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }

        });

        link.setBadgeCount(item.getRelations().size());
        plannedPlaceCount.setText(item.getPlannedPlaceCount() != null ? item.getPlannedPlaceCount() : "0");
        headCount.setText(item.getHeadCount() != null ? item.getHeadCount() : "0");
        inputGroup.addClickHandler(event -> {
            if (hasPermissionToPositionSummary()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|positionsummaryview/" + item.getPositionID(), item.getPositionName(), item.getPositionName());
            }
        });
    }

    @Override
    protected void registerFields() {
//		super.initialize();
        String placement_summary_view = "placement_summary_view_";
        //candidate
        candidate = new MaterialLink();
        candidate.addStyleName(DEFAULT_WIDTH);
        candidate.getElement().setId(placement_summary_view + "candidate");
        //date offered
        dateOffered = new HTML();
        dateOffered.addStyleName(DEFAULT_WIDTH);
        dateOffered.getElement().setId(placement_summary_view + "date_offered");
        //department
        department = new MaterialLink();
        department.addStyleName(DEFAULT_WIDTH);
        department.getElement().setId(placement_summary_view + "department");
        //location
        location = new MaterialLink();
        location.addStyleName(DEFAULT_WIDTH);
        location.getElement().setId(placement_summary_view + "location");
        //position
        position = new MaterialLink();
        position.addStyleName(DEFAULT_WIDTH);
        position.getElement().setId(placement_summary_view + "position");
        //project
        project = new HTML();
        project.addStyleName(DEFAULT_WIDTH);
        project.getElement().setId(placement_summary_view + "project");
        //placementCode
        placementCode = new HTML();
        placementCode.addStyleName(DEFAULT_WIDTH);
        placementCode.getElement().setId(placement_summary_view + "placementCode");
        //placement attachment
        fileUpload = new GeneralFileUpload(F_PLACEMENT, objectID, objectID);
        fileUpload.getPanel().getElement().setId(placement_summary_view + "attachment");
        //placement notes
        noteWidget = new NoteWidget(objectID, PLACEMENT);
        noteWidget.getTextBox().getTextArea().getElement().setId(placement_summary_view + "notes");
        //vacancies
        vacancies = new Div();
        vacancies.addStyleName(DEFAULT_WIDTH);
        vacancies.getElement().setId(placement_summary_view + "vacancies");

        // planned place count, head count
        inputGroup = new Div("input-group");

        Div placeCount = new Div("input-group-append");
        Span vacantPlaceCountTxt = new Span(wfmStrings.planned());
        vacantPlaceCountTxt.addStyleName("input-group-text");
        placeCount.add(vacantPlaceCountTxt);
        inputGroup.add(placeCount);

        plannedPlaceCount = new TextBox();
        plannedPlaceCount.addStyleName(DEFAULT_WIDTH);
        plannedPlaceCount.setEnabled(false);
        inputGroup.add(plannedPlaceCount);

        Div headCount = new Div("input-group-append");
        Span headCountTxt = new Span(wfmStrings.headCount());
        headCountTxt.addStyleName("input-group-text");
        headCount.add(headCountTxt);
        inputGroup.add(headCount);

        this.headCount = new TextBox();
        this.headCount.addStyleName(DEFAULT_WIDTH);
        this.headCount.setEnabled(false);
        inputGroup.add(this.headCount);

        addTitleField(CustomFormConstants.PLACEMENT.BASIC_INFORMATION, wfmStrings.basicDetails());
        addField(CustomFormConstants.PLACEMENT.CANDIDATE, candidate, getTitle(wfmStrings.candidate()));
        addField(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES, vacancies, getTitle(wfmStrings.matchedVacancies()));
        addField(CustomFormConstants.PLACEMENT.DATE_OFFERED, dateOffered, getTitle(hrmsStrings.dateOffered()));
        addTitleField(CustomFormConstants.PLACEMENT.PLACEMENT, wfmStrings.placementDetails());
        addField(CustomFormConstants.PLACEMENT.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        addField(CustomFormConstants.PLACEMENT.LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        addField(CustomFormConstants.PLACEMENT.POSITION, position, getTitle(wfmStrings.position()));
        addField(CustomFormConstants.PLACEMENT.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        addField(CustomFormConstants.PLACEMENT.PLACEMENT_FILE, fileUpload, wfmStrings.attachments());
        addField(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE, noteWidget, wfmStrings.notes());
        addField(CustomFormConstants.PLACEMENT.PLACEMENT_CODE, placementCode, wfmStrings.number());
        addField(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT, inputGroup, wfmStrings.planned() + "/ " + wfmStrings.headCount());
        addTitleField(CustomFormConstants.PLACEMENT.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void save(String status, String rejectionReason) {
        enableButton(false);
        LoadingPanel.loading(true);
        RecruitmentService.App.get().updateStatusPlacement(objectID, status, rejectionReason, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                enableButton(true);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, result, ViewPlacementForm.this);
                closeTab();
            }
        });
    }

    private void setVacancyItems() {
        Integer candidateID = item.getCandidateID();
        RecruitmentService.App.get().getPlacementVacancies(objectID, candidateID, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                if (result != null && result.size() > 0) {
                    int i = 0;
                    for (SelectItem selectItem : result) {
                        Span delimiter = new Span(", ");
                        String link = "vacancy|summary/" + selectItem.getId();
                        SimpleLink name = new SimpleLink(selectItem.getName());
                        name.addClickHandler(clickEvent -> {
                            if (hasPermissionToVacancySummary()) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(link);
                            } else {
                                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                            }
                        });
                        vacancies.add(name);
                        if (i != item.getVacancies().size() - 1) {
                            vacancies.add(delimiter);
                        }
                        i++;
                    }
                }
            }
        });
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private boolean hasPermissionToLocationSummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_LOCATION);
    }

    private boolean hasPermissionToDepartmentSummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_SUMMARY_VIEW);
    }

    private boolean hasPermissionToPositionSummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY);
    }

    private boolean hasPermissionToVacancySummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW);
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(ViewPlacementForm.this) {
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
                    return RelationItem.TYPE_PLACEMENT;
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

    public void setItemContact() {
        ContactService.App.get().getContact(item.getCandidateID(), false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                item_contact = o;
                LoadingPanel.loading(false);
            }
        });
    }
}