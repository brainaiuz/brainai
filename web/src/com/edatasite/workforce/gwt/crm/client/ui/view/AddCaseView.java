package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.ChooseCRMItemAndSearch;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.ChooseProductDetailsWithBrand;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CaseHistoryTab;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.CaseStatusHistoryGrid;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class AddCaseView extends CustomForm2 implements Constants, FormHasCustomFieldInterface, HasLinksInterface, Colapse {
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();

    public static final String FROM_OUTLOOK = "fromOutlook";

    private ChooseCRMItemAndSearch reportedBy;
    private ChooseProductDetailsWithBrand productDetailsWithBrand;
    private KpiEditor description;
    private TextBox subject;
    private DataListBox type;
    private DataListBox caseOrigin;
    private DataListBox caseReason;
    private TextBox otherReason;
    private DataListBox priority;
    private DataListBox status;
    private DataListBox sla;
    private EmployeeLookUp assignee;
    private EmployeeLookUp resolver;
    private DataListBox internalStatus;
    private NoteWidget noteBar;
    private GeneralFileUpload attachments;
    protected CaseStatusHistoryGrid caseStatusHistoryGrid;
    protected CaseHistoryTab caseHistory;

    protected Integer objectId;
    protected ArrayList<RelationItem> relationItems;
    protected CaseItem item;
    protected Integer convertFormId;
    protected String formType;
    private Email lastCaseRepliedFromReporterEmail;
    private String content;
    private VerticalPanel descriptionTable;
    private TextArea2 emailDescription;

    private Integer relationID;
    private String relationType;
    private String relationName;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    private SelectItem[] caseReasons;
    private String successMessage = property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.caseID());
    protected boolean saveAndClose = false;
    private final boolean isClientView = !Utils.hasCrmRole() && Utils.hasRole(CLIENT);
    private boolean isCopying;
    private HashMap<String, String> pluginParams;

    public AddCaseView(Integer objectId, boolean isCopying) {
        this(objectId, null, null, null);
        this.isCopying = isCopying;
        if (isCopying) {
            setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.caseID()));
        }
    }

    public AddCaseView(Integer objectId, Integer relationID, String relationType, String relationName) {
        super("addcase");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.caseID()));
        this.relationID = relationID;
        this.relationType = relationType;
        this.relationName = relationName;
        if (objectId != null) {
            setDescription(property.getSingular(crmStrings.editCase(), wfmStrings.caseID()));
            this.objectId = objectId;
        }
        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }
    }

    public AddCaseView(String params) {
        super("addcase");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.caseID()));
        parsePluginParams(params);
        if (this.objectId != null) {
            setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.caseID()));
        }
    }

    public AddCaseView(Integer convertFormId, String formType) {
        super("addcase");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.caseID()));
        this.convertFormId = convertFormId;
        this.formType = formType;
    }

    private void parsePluginParams(String paramStr) {
        if (paramStr != null && !"".equals(paramStr)) {
            try {
                String[] params = paramStr.split("&\\$&");
                if (params != null && params.length > 0) {
                    pluginParams = new HashMap<>();
                    for (String param : params) {
                        String[] pair = param.split("=");
                        if (pair != null && pair.length > 1) {
                            pluginParams.put(pair[0], pair[1]);
                        }
                    }
                    if (pluginParams.get("contactType") != null && !pluginParams.get("contactType").equals(String.valueOf(CrmConstants.TYPE_LEAD_CONTACT))) {
                        this.relationID = pluginParams.get("contactID") != null ? Integer.parseInt(pluginParams.get("contactID")) : null;
                    }
                    if (pluginParams.get("caseID") != null) {
                        this.objectId = Integer.parseInt(pluginParams.get("caseID"));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void setPluginParams(CaseItem caseItem) {
        if (caseItem != null && pluginParams != null && !pluginParams.isEmpty()) {
            caseItem.setDescription(pluginParams.get("description"));
            caseItem.setSubject(pluginParams.get("subject"));
            String caseOriginName = pluginParams.get("caseOrigin") != null ? pluginParams.get("caseOrigin") : wfmStrings.email();
            if (caseItem.getCaseOrigins() != null) {
                caseItem.getCaseOrigins();
                for (SelectItem item : caseItem.getCaseOrigins()) {
                    if (item != null && caseOriginName.equalsIgnoreCase(item.getName())) {
                        caseItem.setCaseOriginId(item.getId());
                        break;
                    }
                }
            }
            if (pluginParams.get("contactType") != null && !pluginParams.get("contactType").equals(String.valueOf(CrmConstants.TYPE_LEAD_CONTACT))) {
                caseItem.setCrmContactID(pluginParams.get("contactID") != null ? Integer.parseInt(pluginParams.get("contactID")) : null);
                caseItem.setCrmContact(pluginParams.get("contactName"));
            } else {
                caseItem.setLeadId(pluginParams.get("contactID") != null ? Integer.parseInt(pluginParams.get("contactID")) : null);
                caseItem.setLead(pluginParams.get("contactName"));
            }
        }
    }

    public String getIconStyle() {
        return "casesList cases-list";
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CrmCase, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(CompanyCfAndPropertyItems result) {
                super.onSuccess(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddCaseView.super.onInitialize();
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {
        drawForm();
    }

    private void drawForm() {
        subject = new TextBox();
        subject.addStyleName(DEFAULT_WIDTH);
        subject.ensureDebugId("subject");

        reportedBy = new ChooseCRMItemAndSearch(this);
        reportedBy.addStyleName(DEFAULT_WIDTH);
        reportedBy.ensureDebugId("reportedBy");

        description = new KpiEditor(true);
        description.setWidth("70%");
        description.ensureDebugId("description");
        description.getRichEditor().addValueChangeHandler(valueChangeEvent -> item.setDescription(description.getData()));

        type = new DataListBox();
        type.ensureDebugId("type");
        type.addStyleName(DEFAULT_WIDTH);
        type.addValueChangeHandler(event -> {
            if (type.getSelectedId() == null && caseReasons != null) {
                caseReason.setItems(caseReasons);
            } else {
                if (caseReasons != null && caseReasons.length > 0) {
                    ArrayList<SelectItem> list = new ArrayList<>();
                    for (SelectItem val : caseReasons) {
                        ReferenceItem rItem = (ReferenceItem) val;
                        if (rItem.getRelative() == null || "".equals(rItem.getRelative())) {
                            list.add(val);
                        } else {
                            String[] ids = rItem.getRelative().split(",");
                            for (String id : ids) {
                                if (type.getSelectedId().toString().equals(id)) {
                                    list.add(val);
                                    break;
                                }
                            }
                        }
                    }
                    caseReason.setItems(list.toArray(new SelectItem[]{}));
                }
            }
        });

        caseOrigin = new DataListBox();
        caseOrigin.addStyleName(DEFAULT_WIDTH);
        caseOrigin.ensureDebugId("origin");

        caseReason = new DataListBox();
        caseReason.addStyleName(DEFAULT_WIDTH);
        caseReason.ensureDebugId("reason");
        caseReason.addValueChangeHandler(event -> otherReason.setVisible(caseReason.getSelectedItem() != null &&
                CaseItem.OTHER_REASON.equals(caseReason.getSelectedItem().getName())));
        otherReason = new TextBox();
        otherReason.addStyleName(DEFAULT_WIDTH);

        priority = new DataListBox();
        priority.addStyleName(DEFAULT_WIDTH);
        priority.ensureDebugId("priority");

        status = new DataListBox();
        status.setWithoutNullLabel(true);
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId("status");

        assignee = new EmployeeLookUp(true, true, false);
        assignee.addStyleName(DEFAULT_WIDTH);
        assignee.ensureDebugId("assignee");

        resolver = new EmployeeLookUp(true, false, false);
        resolver.addStyleName(DEFAULT_WIDTH);
        resolver.ensureDebugId("resolver");

        internalStatus = new DataListBox();
        internalStatus.addStyleName(DEFAULT_WIDTH);
        internalStatus.ensureDebugId("internalStatus");

        noteBar = new NoteWidget(isCopying ? null : objectId, RelationItem.TYPE_CASE);
        attachments = new GeneralFileUpload(Constants.F_CASE, isCopying ? null : objectId, isCopying ? null : objectId);

        addTitleField(DESCRIPTION, property.getSingular(wfmStrings.caseDescription(), wfmStrings.caseID()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), true), false,
                    formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation()) {
                new KpiToolTip(subject, formPropertyMap.get(CustomFormConstants.SUBJECT).getInformationText());
            }

            subject.setEnabled(!formPropertyMap.get(CustomFormConstants.SUBJECT).isDisabled());
        } else {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(wfmStrings.subject(), true));
        }
        if (!isClientView) {
            MaterialPanel reportedByDiv = new MaterialPanel();
            reportedByDiv.add(reportedBy);
            reportedByDiv.add(reportedBy.getOtherFields());

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null) {
                addField(REPORTED_BY, reportedByDiv, getTitle(formPropertyMap.get(CustomFormConstants.REPORTED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTED_BY).getTitle() : wfmStrings.reportedBy(), formPropertyMap.get(CustomFormConstants.REPORTED_BY).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation());
                if (formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation()) {
                    new KpiToolTip(reportedByDiv, formPropertyMap.get(CustomFormConstants.REPORTED_BY).getInformationText());
                }
            } else {
                addField(REPORTED_BY, reportedByDiv, getTitle(wfmStrings.reportedBy()));
            }
        }


        addTitleField(CASE_INFORMATION, property.getSingular(wfmStrings.basicDetails(), wfmStrings.caseID()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TYPE).isInformation()) {
                new KpiToolTip(type, formPropertyMap.get(CustomFormConstants.TYPE).getInformationText());
            }

            type.setEnabled(!formPropertyMap.get(CustomFormConstants.TYPE).isDisabled());
        } else {
            addField(TYPE, type, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN) != null) {
            addField(CASE_ORIGIN, caseOrigin, getTitle(formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getTitle() : crmStrings.origin(), formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isInformation()) {
                new KpiToolTip(caseOrigin, formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getInformationText());
            }

            caseOrigin.setEnabled(!formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isDisabled());
        } else {
            addField(CASE_ORIGIN, caseOrigin, getTitle(crmStrings.origin()));
        }


        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PRODUCT_DETAILS_TO_CRM)) {
            productDetailsWithBrand = new ChooseProductDetailsWithBrand(this);
            addField(BRAND_BY, productDetailsWithBrand, getTitle(wfmStrings.product()));
        }

        FlexTable f = new FlexTable();
        f.setWidget(0, 0, caseReason);
        f.setWidget(0, 1, otherReason);
        otherReason.setVisible(false);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_REASON) != null) {
            addField(CASE_REASON, f, getTitle(formPropertyMap.get(CustomFormConstants.CASE_REASON).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_REASON).getTitle() : wfmStrings.reason(), formPropertyMap.get(CustomFormConstants.CASE_REASON).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CASE_REASON).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CASE_REASON).isInformation()) {
                new KpiToolTip(f, formPropertyMap.get(CustomFormConstants.CASE_REASON).getInformationText());
            }
            caseReason.setEnabled(!formPropertyMap.get(CustomFormConstants.CASE_REASON).isDisabled());
        } else {
            addField(CASE_REASON, f, getTitle(wfmStrings.reason()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(PRIORITY, priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority(), formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation()) {
                new KpiToolTip(priority, formPropertyMap.get(CustomFormConstants.PRIORITY).getInformationText());
            }

            priority.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIORITY).isDisabled());
        } else {
            addField(PRIORITY, priority, getTitle(wfmStrings.priority()));
        }
        if (!isClientView) {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
                addField(STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), true), false,
                        formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
                if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                    new KpiToolTip(status, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
                }

                status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
            } else {
                addField(STATUS, status, getTitle(wfmStrings.status(), true));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
                addField(ASSIGNEE, assignee, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignedTo(), formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.ASSIGNEE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.ASSIGNEE).isInformation()) {
                    new KpiToolTip(assignee, formPropertyMap.get(CustomFormConstants.ASSIGNEE).getInformationText());
                }

                assignee.setEnabled(!formPropertyMap.get(CustomFormConstants.ASSIGNEE).isDisabled());
            } else {
                addField(ASSIGNEE, assignee, getTitle(wfmStrings.assignedTo()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null) {
                addField(RESOLVER, resolver, getTitle(formPropertyMap.get(CustomFormConstants.RESOLVER).isChanged() ? formPropertyMap.get(CustomFormConstants.RESOLVER).getTitle() : wfmStrings.resolver(), formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation());
                if (formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation()) {
                    new KpiToolTip(resolver, formPropertyMap.get(CustomFormConstants.RESOLVER).getInformationText());
                }

                resolver.setEnabled(!formPropertyMap.get(CustomFormConstants.RESOLVER).isDisabled());
            } else {
                addField(RESOLVER, resolver, getTitle(wfmStrings.resolver()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS) != null) {
            addField(INTERNAL_STATUS, internalStatus, getTitle(formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).getTitle() : wfmStrings.internalStatus(), formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).isRequired()));
            internalStatus.setEnabled(!formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).isDisabled());
        } else {
            addField(INTERNAL_STATUS, internalStatus, getTitle(wfmStrings.internalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && noteBar != null) {
            addField(CRM_NOTE, noteBar, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()) {
                new KpiToolTip(noteBar, formPropertyMap.get(CustomFormConstants.CRM_NOTE).getInformationText());
            }

            noteBar.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_NOTE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_NOTE, noteBar, wfmStrings.notes(), true);
        }
        addField(CustomFormConstants.ATTACHMENTS, attachments, wfmStrings.attachments(), true);
//        if (relationID != null && (RelationItem.TYPE_TASK.equals(relationType) || RelationItem.TYPE_PROJECT.equals(relationType))) {
//            setPredefinedTags(new RelationItem[]{new RelationItem(null, relationID, relationType, relationName, null, RelationItem.TYPE_CASE, null)});
//        }
        getCustomFieldUtil().drawCustomFields(this, objectId);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        if (!(objectId == null || isCopying)) {
            setEditValues();
        }
        show();
    }

    @Override
    protected void addButtons() {

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        if (!Utils.isWebForm()) {
            saveAdd.addClickHandler(event -> {
                saveAndClose = false;
                save();
            });
            splitButton.addItem(saveAdd);
            addButton(splitButton);

        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        crmService.editCase(objectId, formType, convertFormId, true, new AbstractAsyncCallback<CaseItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                drawForm();
            }

            public void success(final CaseItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    lastCaseRepliedFromReporterEmail = o.getLastEmail();
                    if (isCopying) {
                        item.getRelations().clear();
                        item.setSubject("");
                        item.setCaseNumber("");
                        item.setTrackerID(null);
                        item.setEmailID(null);
                    }

                    if (objectId == null) {
                        setDefaultValues();
                    }

                    fillFields();
                });
            }
        });
    }

    protected void fillFields() {
        initPredefinedValues();
        if (objectId != null) {
            Utils.registrRelation(item);
        }
        setPluginParams(item);

        if (!Utils.isNullOrEmpty(item.getSubject())) {
            subject.setText(item.getSubject());
        }

        if (objectId == null && relationID != null && relationType != null && relationName != null) {
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType) || RelationItem.TYPE_CONTACT.equals(relationType) || RelationItem.TYPE_LEAD.equals(relationType)) {
                reportedBy.setValues(relationType, new SelectItem(relationID, relationName));
            }
        } else if (item.getLeadId() != null && item.getLead() != null) {
            reportedBy.setValues(CrmConstants.CRM_LEAD, new SelectItem(item.getLeadId(), item.getLead()));
        } else if (item.getAccountId() != null && item.getAccountName() != null) {
            reportedBy.setValues(CrmConstants.CRM_ACCOUNT, new SelectItem(item.getAccountId(), item.getAccountName()));
        } else if (item.getCrmContactID() != null && item.getCrmContact() != null) {
            if (item.isAnonim()){
                reportedBy.setValues(CrmConstants.CRM_CONTACT, new SelectItem(item.getCrmContactID(), wfmStrings.anonymous()));
                reportedBy.disableIfAnonim();
            }else {
                reportedBy.setValues(CrmConstants.CRM_CONTACT, new SelectItem(item.getCrmContactID(), item.getCrmContact()));
            }
        }

        if (lastCaseRepliedFromReporterEmail != null && !Utils.isNullOrEmpty(lastCaseRepliedFromReporterEmail.getContent())) {
            descriptionTable = new VerticalPanel();
            descriptionTable.setSpacing(3);
            descriptionTable.setWidth("100%");
            descriptionTable.addStyleName("caseSummaryInfo spacing2-padding2");
            descriptionTable.getElement().getStyle().setPadding(3, Style.Unit.PX);

            FlowPanel contentPanel = new FlowPanel();
            emailDescription = new TextArea2(3000);
            emailDescription.setText(item.getDescription());
            contentPanel.add(emailDescription);
            content = lastCaseRepliedFromReporterEmail.getContent();
            if (content != null) {
                content = content.replaceAll("<a\\s*[a-z0-9A-Z_-]*\\s*href=", "<a target=\"_blank\" href=");
            }
            Element iFrame = DOM.createIFrame();
            iFrame.setAttribute("id", "caseSummary" + this.hashCode());
            iFrame.setAttribute("width", "100%");
            iFrame.setAttribute("scrolling", "yes");
            iFrame.setAttribute("class", "email-summary__iframe");
            contentPanel.getElement().appendChild(iFrame);
            descriptionTable.add(contentPanel);

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION) != null) {
                addField(CASE_DESCRIPTION, descriptionTable, getTitle(formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isRequired()), true,
                        formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isInformation());
                if (formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isInformation()) {
                    new KpiToolTip(descriptionTable, formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getInformationText());
                }
            } else {
                addField(CASE_DESCRIPTION, descriptionTable, getTitle(wfmStrings.description()), true);
            }
        } else {
            description.setData(item.getDescription());

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION) != null) {
                addField(CASE_DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isRequired()), true,
                formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isInformation());
                if (formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isInformation()) {
                    new KpiToolTip(descriptionTable, formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getInformationText());
                }

                description.setEnabled(!formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isDisabled());
            } else {
                addField(CASE_DESCRIPTION, description, getTitle(wfmStrings.description()), true);
            }
        }

        type.setItems(item.getTypes());
        if (item.getTypeId() != null) {
            type.setSelected(item.getTypeId());
        }

        caseOrigin.setItems(item.getCaseOrigins());
        if (item.getCaseOriginId() != null) {
            caseOrigin.setSelected(item.getCaseOriginId());
        }

        caseReason.setItems(item.getCaseReasons());
        caseReasons = item.getCaseReasons();
        if (item.getCaseReasonId() != null) {
            caseReason.setSelected(item.getCaseReasonId());
            if ("Other Reason".equals(caseReason.getSelectedItem().getName()) && !Utils.isNullOrEmpty(item.getOtherReason())) {
                otherReason.setText(item.getOtherReason());
            }
        }

        if (productDetailsWithBrand != null) {
            productDetailsWithBrand.setDataItems(item);
        }

        priority.setItems(item.getPriorities());
        if (item.getPriorityId() != null) {
            priority.setSelected(item.getPriorityId());
        }

        status.setItems(item.getStatusItems());
        if (item.getStatus().getId() != null) {
            status.setSelected(item.getStatus().getId());
        }

        if (item.getCaseAssigneeId() != null && item.getCaseAssigneeName() != null) {
            String assigneeName = item.getCaseAssigneeName();
            if (Utils.getUserID().equals(item.getCaseAssigneeId())) {
                assigneeName += " (" + wfmStrings.myself() + ")";
            }
            assignee.setSelected(item.getCaseAssigneeId(), assigneeName);
        } else if (item.getDepartmentID() != null) {
            assignee.setSelected(item.getDepartmentID(), item.getDepartment() != null ? item.getDepartment() + " (Department)" : "");
        }

        if (item.getResolverId() != null && item.getResolverName() != null) {
            String resolverName = item.getResolverName();
            if (Utils.getUserID().equals(item.getResolverId())) {
                resolverName += " (" + wfmStrings.myself() + ")";
            }
            resolver.setSelected(item.getResolverId(), resolverName);
        }

        internalStatus.setItems(item.getInternalStatusItems());
        if (item.getInternalStatusId() != null) {
            internalStatus.setSelected(item.getInternalStatusId());
        }

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());

        if (link != null) {
            if (objectId == null) {
                relationItems = new ArrayList<>();
                RelationItem relationItem = RelationItem.newEventRelation(relationType, relationID, relationName);
                relationItems.add(relationItem);
            }
            link.addClickHandler(event -> {
                if (firstClick.get()) {
                    getLinkingUtil().getAddLinkSideNavBox();
                    if (objectId == null) {
                        if (relationItems != null) {
                            getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);
                        }
                    } else {
                        getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                    }
                    firstClick.set(false);
                } else {
                    getLinkingUtil().getAddLinkSideNavBox().show();
                }
            });
            link.setBadgeCount(item.getRelations().size());
        }
        if (objectId == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue().contains("###")) {
            String[] values = formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue().split("###");
            if (values != null && values.length > 0) {
                int type = Integer.valueOf(values[0]);
                String name = values[1];
                if (CrmConstants.CRM_LEAD_ID == type) {
                    reportedBy.setValues(CrmConstants.CRM_LEAD, new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTED_BY).getSelectedId(), name));
                } else if (CrmConstants.CRM_ACCOUNT_ID == type) {
                    reportedBy.setValues(CrmConstants.CRM_ACCOUNT, new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTED_BY).getSelectedId(), name));
                } else if (CrmConstants.CRM_CONTACT_ID == type) {
                    reportedBy.setValues(CrmConstants.CRM_CONTACT, new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTED_BY).getSelectedId(), name));
                }
            }

            subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null) {
            subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getDefaultValue() != null) {
            description.setData(formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null && formPropertyMap.get(CustomFormConstants.TYPE).getDefaultValue() != null) {
            type.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.TYPE).getDefaultValue()));

        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN) != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getDefaultValue() != null) {
            caseOrigin.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getSelectedId(), formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_REASON) != null && formPropertyMap.get(CustomFormConstants.CASE_REASON).getDefaultValue() != null) {
            caseReason.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CASE_REASON).getSelectedId(), formPropertyMap.get(CustomFormConstants.CASE_REASON).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue() != null) {
            priority.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PRIORITY).getSelectedId(), formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue()));

        }
        if (!isClientView) {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
                status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE).getDefaultValue() != null) {
                assignee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ASSIGNEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.ASSIGNEE).getDefaultValue()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue() != null) {
                resolver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.RESOLVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS) != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).getDefaultValue() != null) {
            internalStatus.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && noteBar != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue() != null) {
            noteBar.getTextBox().setText(formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue());
        }
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = customValidate();
        errors += markAsError(STATUS, status, !isClientView && status.getSelectedId() == null);
        errors += markAsError(CustomFormConstants.SUBJECT, subject, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ?
                formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), subject, formPropertyMap.get(CustomFormConstants.SUBJECT).getMinChar()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.CASE_DESCRIPTION).isRequired()) {
            if (lastCaseRepliedFromReporterEmail != null) {
                errors += markAsError(CustomFormConstants.CASE_DESCRIPTION, descriptionTable, Utils.isNullOrEmpty(lastCaseRepliedFromReporterEmail.getContent()) && emailDescription != null);
            } else {
                errors += markAsError(CustomFormConstants.CASE_DESCRIPTION, description, Utils.isNullOrEmpty(description.getData()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null && formPropertyMap.get(CustomFormConstants.TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.TYPE, type, type.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN) != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isRequired()) {
            errors += markAsError(CustomFormConstants.CASE_ORIGIN, caseOrigin, caseOrigin.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_REASON) != null && formPropertyMap.get(CustomFormConstants.CASE_REASON).isRequired()) {
            errors += markAsError(CustomFormConstants.CASE_REASON, caseReason, caseReason.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()) {
            errors += markAsError(CustomFormConstants.PRIORITY, priority, priority.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()) {
            errors += markAsError(CustomFormConstants.PRIORITY, priority, priority.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS) != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.INTERNAL_STATUS, internalStatus, internalStatus.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()) {
            if (noteBar != null && !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), noteBar.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).getMinChar())) {
                errors++;
            }
        }


        if (!isClientView) {
            errors += reportedBy.validate();


            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
                errors += markAsError(CustomFormConstants.STATUS, status, status.getSelectedId() == null);
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired()) {
                errors += markAsError(CustomFormConstants.ASSIGNEE, assignee, assignee.getSelectedItemID() == null);
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()) {
                errors += markAsError(CustomFormConstants.RESOLVER, resolver, resolver.getSelectedItemID() == null);
            }
        }
        if (productDetailsWithBrand != null && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PRODUCT_DETAILS_TO_CRM)) {
            errors = productDetailsWithBrand.validate(errors);
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void save() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        if (isCopying) {
            item.setObjectId(null);
        }
        item.setSubject(subject.getText());
        item.setAccountId(null);
        item.setCrmContactID(null);
        item.setLeadId(null);
        if ((reportedBy.isLeadChecked() || reportedBy.isAccountChecked() || reportedBy.isContactChecked()) && reportedBy != null) {
            SelectItem reporter = reportedBy.getReporter();

            if (reporter != null && reporter.getId() != null) {
                if (reportedBy.isLeadChecked()) {
                    item.setLeadId(reporter.getId());
                    item.setLead(reporter.getName());
                } else {
                    item.setLead(null);
                    item.setLeadId(null);
                }
                if (reportedBy.isAccountChecked()) {
                    item.setAccountId(reporter.getId());
                    item.setAccountName(reporter.getName());
                } else {
                    item.setAccountId(null);
                    item.setAccountName(null);
                }
                if (reportedBy.isContactChecked()) {
                    item.setCrmContactID(reporter.getId());
                    item.setCrmContact(reporter.getName());
                } else {
                    item.setCrmContact(null);
                    item.setCrmContactID(null);
                }
            }
        } else if (reportedBy.isOtherChecked()) {
            Map<String, String> newReporter = reportedBy.getOtherReporterInformation();
            if (newReporter != null && newReporter.size() > 0) {
                item.setFirstName(newReporter.get("firstName"));
                item.setLastName(newReporter.get("lastName"));
                item.setCompany(newReporter.get("company"));
                item.setEmail(newReporter.get("email"));
                item.setPhone(newReporter.get("phone"));
                item.setFax(newReporter.get("fax"));
            }
        }
        if (lastCaseRepliedFromReporterEmail != null && !Utils.isNullOrEmpty(lastCaseRepliedFromReporterEmail.getContent()) && emailDescription != null) {
            item.setDescription(emailDescription.getText());
        } else {
            item.setDescription(description.getData());
        }
        item.setTypeId(type.getSelectedId());
        item.setCaseOriginId(caseOrigin.getSelectedId());
        item.setCaseReasonId(caseReason.getSelectedId());
        if (caseReason.getSelectedItem() != null && CaseItem.OTHER_REASON.equals(caseReason.getSelectedItem().getName())) {
            item.setOtherReason(otherReason.getText());
        }
        item.setPriorityId(priority.getSelectedId());
        item.setStatus(status.getSelectedItem(true));
        item.setCaseAssigneeId(null);
        item.setDepartmentID(null);
        if (assignee.getSelectedItem() != null) {
            if (assignee.getSelectedItem().getName().contains("(Department)")) {
                item.setDepartmentID(assignee.getSelectedItemID());
                item.setDepartment(assignee.getSelectedItem().getName());
            } else {
                item.setCaseAssigneeId(assignee.getSelectedItemID());
                item.setCaseAssigneeName(assignee.getSelectedItem().getName());
            }
        }
        item.setResolverId(resolver.getSelectedItemID());
        item.setInternalStatusId(internalStatus.getSelectedId());
        if (productDetailsWithBrand != null && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PRODUCT_DETAILS_TO_CRM)) {
            Map<String, Integer> selectedItems = productDetailsWithBrand.getSelectedItems();
            item.setBrandId(selectedItems.get("brand"));
            item.setProductCategoryId(selectedItems.get("productCategory"));
            item.setProductId(selectedItems.get("product"));
        }
        item.setNotes(noteBar.getNewNotesToSave());
        if (firstClick.get()) {
            if (objectId == null && relationItems != null && relationItems.size() > 0) {
                item.setRelations(relationItems);
            } else {
                item.setRelations(item.getRelations());
            }
        } else {
            item.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.setAttachments(attachments.getAttachedFiles());
        LoadingPanel.loading(true);
        CRMService.App.get().saveCase(item, false, new AbstractAsyncCallback<SelectItem>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final SelectItem o) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (Utils.isWebForm()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.WEB_FORM_SAVED, o, AddCaseView.this);
                }
                if (formType != null && convertFormId != null) {
                    saveConvertedRelations(o.getId(), o.getNumber());
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, item, AddCaseView.this);
                item.setObjectId(o.getId());
                Info.show(successMessage, Info.Type.INFO);
                onShellOk();
            }
        });
    }

    @Override
    public void reInitialize() {
        super.reInitialize();
        if (item != null && !Utils.isNullOrEmpty(item.getDescription())) {
            description.setData(item.getDescription());
        }
        if (item != null && item.getLastEmail() != null && content != null) {
            Utils.setContentToIFrame("caseSummary" + this.hashCode(), content);
        }
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(TYPE, item.getTypes());
        addPredefinedValues(CASE_ORIGIN, item.getCaseOrigins());
        addPredefinedValues(CASE_REASON, item.getCaseReasons());
        addPredefinedValues(PRIORITY, item.getPriorities());
        addPredefinedValues(STATUS, item.getStatusItems());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private Localize localize;

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CASE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_CASE_ADD;
    }

    private void setEditValues() {
        successMessage = property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.caseID());
    }

    protected void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            linkingUtil = null;
            reinit();
        }
    }

    //    public void setPredefinedTags(RelationItem[] predefinedTags) {
//        if (predefinedTags != null && predefinedTags.length > 0) {
//            getLinkingUtil().getTaggingView().setSelectedRelations(predefinedTags);
//            getLinkingUtil().drawLinks();
//        }
//    }
//
    public void reinit() {
        registerFields();
        initForm();
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddCaseView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                public Integer getRelationID() {
                    return objectId;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_CASE;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getSubject() : null;
                }

            };
        }
        return linkingUtil;
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

    @Override
    public String getPropertyCode() {
        return CASE_LIST;
    }

    private void getRelationName(final Integer relationID, final String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                relationName = result;
            }
        });
    }

    private void saveConvertedRelations(Integer _objectId, String caseNumber) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_CASE, caseNumber != null ? caseNumber : item.getSubject(), convertFormId, formType, item.getFromName() != null ? item.getFromName() : caseNumber));
        AllInOneService.App.get().getAdditionalRelations(_objectId, RelationItem.TYPE_CASE, caseNumber != null ? caseNumber : item.getSubject(), convertFormId, formType, item.getFromName() != null ? item.getFromName() : caseNumber, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<RelationItem> result) {
                if (result != null && result.size() > 1) {
                    result.remove(0);
                    for (RelationItem item_ : result) {
                        boolean haveRelationItem = false;

                        if (item.getConvertedRelations() != null) {
                            for (RelationItem item : item.getConvertedRelations()) {

                                if (item.getToType().equals(item_.getToType()) && item.getToID().equals(item_.getToID())) {
                                    haveRelationItem = true;
                                    break;
                                }
                            }
                        }
                        if (!haveRelationItem) {
                            relationItems.add(item_);
                        }
                    }
                }
                if (relationItems != null && relationItems.size() > 0) {
                    AllInOneService.App.get().saveRelations(RelationItem.TYPE_CASE, _objectId, item.getSubject(), relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<RelationItem> selectItems) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            }
        });
    }
}