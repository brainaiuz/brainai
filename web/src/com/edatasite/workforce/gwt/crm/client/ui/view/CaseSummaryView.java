package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CaseHistoryTab;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.CaseStatusHistoryGrid;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class CaseSummaryView extends CustomForm2 implements Constants, HasLinksInterface, FormHasCustomFieldInterface {

    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private final Integer objectId;
    private CaseItem item;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML caseID, subject, reportBy, origin, internalStatus, internalUpdateDate;
    private VerticalPanel descriptionTable;
    private DataListBox priority, status;
    private EmployeeLookUp assignedTo, resolver;
    private NoteWidget noteBar;
    private CaseStatusHistoryGrid caseStatusHistoryGrid;
    private CaseHistoryTab caseHistory;
    private GeneralFileUpload attachments;
    private String content;
    private DataListBox type;
    private DataListBox reason;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);

    private Email lastCaseRepliedFromReporterEmail;

    private final boolean isClientView = !Utils.hasCrmRole() && Utils.hasRole(CLIENT);

    public CaseSummaryView(Integer id) {
        super("summary");
        setDescription(wfmStrings.summaryView());
        this.objectId = id;
    }

    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_REPLY_TO_REPORTER, CaseSummaryView.this, (sender, args) -> {
            if (objectId.equals(args)) {
                status.setSelectedByCode(CaseItem.CASE_STATUS_REPLIED);
            }
        });

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CrmCase, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                CaseSummaryView.super.onInitialize();
            }
        });
        return null;
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

    protected void registerFields() {
        drawForm();
    }

    private void drawForm() {

        caseID = initHTML();
        caseID.addStyleName(DEFAULT_WIDTH);

        subject = initHTML();
        subject.addStyleName(DEFAULT_WIDTH);

        descriptionTable = new VerticalPanel();
        descriptionTable.setSpacing(3);
        descriptionTable.setWidth("100%");
        descriptionTable.addStyleName("caseSummaryInfo spacing2-padding2");
        descriptionTable.getElement().getStyle().setPadding(3, Style.Unit.PX);

        reportBy = initHTML();
        reportBy.addStyleName(DEFAULT_WIDTH);

        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);
        type.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
            type.setEnabled(true);
            type.addValueChangeHandler(valueChangeEvent -> onChangeField(CaseItem.CASE_TYPE));
        }

        origin = initHTML();
        origin.addStyleName(DEFAULT_WIDTH);

        reason = new DataListBox();
        reason.addStyleName(DEFAULT_WIDTH);
        reason.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
            reason.setEnabled(true);
            reason.addValueChangeHandler(valueChangeEvent -> onChangeField(CaseItem.CASE_REASON));
        }

        priority = new DataListBox();
        priority.addStyleName(DEFAULT_WIDTH);
        priority.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_PRIORITY_CASE)) {
            priority.setEnabled(!isClientView);
            priority.addValueChangeHandler(event -> onChangeField(CaseItem.PRIORITY));
        }

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_STATUS_CASE)) {
            status.setEnabled(!isClientView);
            status.addValueChangeHandler(event -> changeStatusTo(event.getValue()));
        }

        internalStatus = initHTML();
        internalStatus.addStyleName(DEFAULT_WIDTH);
        internalUpdateDate = initHTML();
        internalUpdateDate.addStyleName(DEFAULT_WIDTH);

        assignedTo = new EmployeeLookUp(true, true, false);
        assignedTo.addStyleName(DEFAULT_WIDTH);
        assignedTo.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_ASSIGNEE_CASE)) {
            assignedTo.setEnabled(!isClientView);
            assignedTo.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (suggestionSelectionEvent.getSelectedItem() != null) {
                    SelectItem selected = assignedTo.getSelectedItem();
                    changeAssigneeTo(selected);
                }
            });
        }

        resolver = new EmployeeLookUp(true, false, false);
        resolver.addStyleName(DEFAULT_WIDTH);
        resolver.setEnabled(false);
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_ASSIGNEE_CASE)) {
            resolver.setEnabled(!isClientView);
            resolver.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (suggestionSelectionEvent.getSelectedItem() != null) {
                    SelectItem selected = resolver.getSelectedItem();
                    changeResolverTo(selected);
                }
            });
        }

        noteBar = new NoteWidget(objectId, RelationItem.TYPE_CASE);
        caseStatusHistoryGrid = new CaseStatusHistoryGrid(objectId);
        caseHistory = new CaseHistoryTab(objectId);
        attachments = new GeneralFileUpload(Constants.F_CASE, objectId, objectId);

        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        addFieldsToForm();
    }

    private void addFieldsToForm() {
        addField(CASE_DESCRIPTION, descriptionTable, getTitle(wfmStrings.description()));
        addTitleField(CASE_INFORMATION, property.getSingular(wfmStrings.basicDetails(), wfmStrings.caseID()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject()));
        } else {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(wfmStrings.subject()));
        }
        if (!isClientView) {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null) {
                addField(REPORTED_BY, reportBy, getTitle(formPropertyMap.get(CustomFormConstants.REPORTED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTED_BY).getTitle() : wfmStrings.reportedBy()));
            } else {
                addField(REPORTED_BY, reportBy, getTitle(wfmStrings.reportedBy()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_ID) != null) {
            addField(CustomFormConstants.CASE_ID, caseID, getTitle(formPropertyMap.get(CustomFormConstants.CASE_ID).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_ID).getTitle() : property.getSingular(wfmStrings.caseID(), wfmStrings.caseID())));
        } else {
            addField(CASE_ID, caseID, getTitle(property.getSingular(wfmStrings.caseID(), wfmStrings.caseID())));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(TYPE, type, getTitle(wfmStrings.type()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_ORIGIN) != null) {
            addField(CustomFormConstants.CASE_ORIGIN, origin, getTitle(formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_ORIGIN).getTitle() : crmStrings.origin()));
        } else {
            addField(CASE_ORIGIN, origin, getTitle(crmStrings.origin()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CASE_REASON) != null) {
            addField(CustomFormConstants.CASE_REASON, reason, getTitle(formPropertyMap.get(CustomFormConstants.CASE_REASON).isChanged() ? formPropertyMap.get(CustomFormConstants.CASE_REASON).getTitle() : wfmStrings.reason()));
        } else {
            addField(CASE_REASON, reason, getTitle(wfmStrings.reason()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority()));
        } else {
            addField(PRIORITY, priority, getTitle(wfmStrings.priority()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, status, getTitle(wfmStrings.status()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
            addField(CustomFormConstants.ASSIGNEE, assignedTo, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignedTo()));
        } else {
            addField(ASSIGNEE, assignedTo, getTitle(wfmStrings.assignedTo()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null) {
            addField(CustomFormConstants.RESOLVER, resolver, getTitle(formPropertyMap.get(CustomFormConstants.RESOLVER).isChanged() ? formPropertyMap.get(CustomFormConstants.RESOLVER).getTitle() : wfmStrings.resolver()));
        } else {
            addField(RESOLVER, resolver, getTitle(wfmStrings.resolver()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS) != null) {
            addField(CustomFormConstants.INTERNAL_STATUS, internalStatus, getTitle(formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.INTERNAL_STATUS).getTitle() : wfmStrings.internalStatus()));
        } else {
            addField(INTERNAL_STATUS, internalStatus, getTitle(wfmStrings.internalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INTERNAL_UPDATED_DATE) != null) {
            addField(CustomFormConstants.INTERNAL_UPDATED_DATE, internalUpdateDate, getTitle(formPropertyMap.get(CustomFormConstants.INTERNAL_UPDATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.INTERNAL_UPDATED_DATE).getTitle() : wfmStrings.internalUpdatedDate()));
        } else {
            addField(INTERNAL_UPDATED_DATE, internalUpdateDate, getTitle(wfmStrings.internalUpdatedDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CustomFormConstants.CRM_NOTE, noteBar, formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), true);
        } else {
            addField(CRM_NOTE, noteBar, wfmStrings.notes(), true);
        }

        addField(STATUS_HISTORY, caseStatusHistoryGrid, wfmStrings.statusHistory(), true);
        addField(CASE_HISTORY_LOG, caseHistory, wfmStrings.historyLog(), true);
        addField(ATTACHMENTS, attachments, wfmStrings.attachments(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        show();
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        crmService.getCase(objectId, true, new AbstractAsyncCallback<CaseItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(CaseItem object) {
                LoadingPanel.loading(false);
                item = object;
                lastCaseRepliedFromReporterEmail = object.getLastEmail();
                fillFieldsWithData();
                initCaseRelatedAttachments();
            }
        });
    }

    private void fillFieldsWithData() {
        initPredefinedValues();

        caseID.setHTML(item.getCaseNumber());
        subject.setHTML(item.getSubject());

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SHOW_CASE_RELATED_ALL_EMAILS)) {
            if (item.getCaseEmails() != null && item.getCaseEmails().size() > 0 && item.getDescription() != null) {
                StringBuilder caseEmailsContent = new StringBuilder();
                for (Email email : item.getCaseEmails()) {
                    caseEmailsContent.append(email.getContent()).append("\n\n");
                }
                content = item.getDescription() + "\n" + caseEmailsContent;
            } else if (item.getCaseEmails() != null && item.getCaseEmails().size() > 0) {
                StringBuilder caseEmailsContent = new StringBuilder();
                for (Email email : item.getCaseEmails()) {
                    caseEmailsContent.append(email.getContent()).append("\n\n");
                }
                content = caseEmailsContent.toString();
            } else {
                content = item.getDescription();
            }
        } else {
            if (lastCaseRepliedFromReporterEmail != null && !Utils.isNullOrEmpty(lastCaseRepliedFromReporterEmail.getContent()) && item.getDescription() != null) {
                content = item.getDescription() + "\n" + lastCaseRepliedFromReporterEmail.getContent();
            } else if (lastCaseRepliedFromReporterEmail != null && !Utils.isNullOrEmpty(lastCaseRepliedFromReporterEmail.getContent())) {
                content = lastCaseRepliedFromReporterEmail.getContent();
            } else {
                content = item.getDescription();
            }
        }

        //content panel
        FlowPanel contentPanel = new FlowPanel();
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

        String reportedBy = "";
        if (item.getLeadId() != null && item.getLead() != null) {
            reportedBy = Property.get(Constants.LEADS, wfmStrings.lead()) + " -> " + item.getLead();
            reportBy.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + item.getLeadId()));
            reportBy.addStyleName("uploadLinkStyle2");

        } else if (item.getAccountId() != null && item.getAccountName() != null) {
            reportedBy = wfmStrings.crmAccount() + " -> " + item.getAccountName();
        } else if (item.getCrmContactID() != null && item.getCrmContact() != null) {
            String contact = Property.get(Constants.Contacts, wfmStrings.contact());
            if (item.isAnonim()) {
                reportedBy = contact + " -> " + wfmStrings.anonymous();
            } else {
                reportedBy = contact + " -> " + item.getCrmContact();
                reportBy.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getCrmContactID()));
                reportBy.addStyleName("uploadLinkStyle2");
            }
        }
        if (item.getReportEmail() != null && !item.isAnonim()) {
            reportedBy += "(" + item.getReportEmail() + ")";
        }
        reportBy.setHTML(reportedBy);


        type.setItems(item.getTypes());
        type.setSelected(item.getTypeId());

        origin.setHTML(item.getCaseOrigin());

        reason.setItems(item.getCaseReasons());
        reason.setSelected(item.getCaseReasonId());

        priority.setItems(item.getPriorities());
        if (item.getPriorityId() != null) {
            priority.setSelected(item.getPriorityId());
        }

        status.setItems(item.getStatusItems());
        if (item.getStatusItems() != null) {
            for (SelectItem it : item.getStatusItems()) {
                if (CaseItem.CASE_STATUS_REPLIED.equals(it.getReferenceCode())) {
                    break;
                }
            }
        }
        if (item.getStatus() != null && item.getStatus().getId() != null) {
            status.setSelected(item.getStatus().getId());
        }

        internalStatus.setHTML(item.getInternalStatusName());
        if (item.getInternalUpdatedDate() != null) {
            internalUpdateDate.setHTML(DateUtils.format(item.getInternalUpdatedDate()));
        }

        if (item.getCaseAssigneeId() != null && item.getCaseAssigneeName() != null) {
            String assigneeName = item.getCaseAssigneeName();
            if (Utils.getUserID().equals(item.getCaseAssigneeId())) {
                assigneeName += " (" + wfmStrings.myself() + ")";
            }
            assignedTo.setSelected(item.getCaseAssigneeId(), assigneeName);
        } else if (item.getDepartmentID() != null) {
            assignedTo.setSelected(item.getDepartmentID(), item.getDepartment() != null ? item.getDepartment() + " (Department)" : "");
        }

        if (item.getResolverId() != null) {
            String resolverName = item.getResolverName();
            if (Utils.getUserID().equals(item.getResolverId())) {
                resolverName += " (" + wfmStrings.myself() + ")";
            }
            resolver.setSelected(item.getResolverId(), resolverName);
        }

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

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        reDrawIFrame();
    }

    private void onChangeField(String field) {
        switch (field) {
            case CaseItem.CASE_TYPE:
                item.setTypeId(type.getSelectedId());
                break;
            case CaseItem.CASE_REASON:
                item.setCaseReasonId(reason.getSelectedId());
                break;
            case CaseItem.PRIORITY:
                item.setPriorityId(priority.getSelectedId());
                break;
        }
        changeCase();
    }

    private void initCaseRelatedAttachments() {
        crmService.getTrackerAttachments(item.getTrackerID(), new AbstractAsyncCallback<FileResource[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(FileResource[] result) {
                attachments.addAdditionalAttachments(result, false);
            }
        });
    }

    @Override
    public void reInitialize() {
        reDrawIFrame();
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (objectId != null) {
            footer.addToLeftSide(link);
        }

        MaterialLink options = new MaterialLink(wfmStrings.options());
        MaterialSplitButton optionsButton = new MaterialSplitButton(options, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            optionsButton.addItem(customize);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CASE)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(clickEvent -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        crmService.deleteCase(objectId, new AsyncCallback() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Object result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_DELETE, result, CaseSummaryView.this);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            optionsButton.addItem(delete);
        }
        addRightButton(optionsButton);


        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/caseViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(objectId);
                return requestObject.getRequestParams();
            }
        });
        addRightButton(pdf);


        if (Utils.hasPermission(PermissionConstants.CRM_MC_CONVERT_TO_TASK)) {
            MaterialDropDown addButton = addMoreSplitButton(wfmStrings.add());
            MaterialLink task = new MaterialLink(Property.get(Constants.TASK, wfmStrings.task()));
            task.addClickHandler(clickEvent -> goTo("task|add/add/" + CrmConstants.CRM_TASK + "/" + item.getObjectId() + "/" + RelationItem.TYPE_CASE
                    + "/" + item.getSubject().replace("\\/", "\\ ") + "/" + item.getSubject().replace("\\/", "\\ ")
                    + "/" + CONVERT_TO_TASK_FROM_CASE));
            task.ensureDebugId("addTask");
            addButton.add(task);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/" + item.getObjectId(), item.getCaseNumber(), item.getSubject()));
        }

        if (!isClientView) {
            MaterialLink reply = new MaterialLink(wfmStrings.reply());
            MaterialSplitButton splitButton = new MaterialSplitButton(reply);
            //reply.addClickHandler(event -> new ComposeView(item.getObjectId(), lastCaseRepliedFromReporterEmail, true, false, RelationItem.newEventRelation(RelationItem.TYPE_CASE, item.getObjectId(), item.getSubject())));
            reply.addClickHandler(clickEvent -> {
                RelationItem.emailItem = lastCaseRepliedFromReporterEmail;
                goTo("emailcompose|add/add/" + item.getObjectId() + "/" + Boolean.TRUE + "/" + Boolean.FALSE + "/" + RelationItem.TYPE_CASE + "/" + item.getTrackerID() + "/" + item.getSubject().replace("/", "&") + "/" + Boolean.FALSE + "/" + Boolean.FALSE);
            });

            MaterialLink replyToAll = new MaterialLink(wfmStrings.replyToAll());
            //replyToAll.addClickHandler(event -> new ComposeView(item.getObjectId(), lastCaseRepliedFromReporterEmail, false, false));
            replyToAll.addClickHandler(clickEvent -> {
                RelationItem.emailItem = lastCaseRepliedFromReporterEmail;
                goTo("emailcompose|add/add/" + item.getObjectId() + "/" + Boolean.TRUE + "/" + Boolean.FALSE + "/" + RelationItem.TYPE_CASE + "/" + item.getTrackerID() + "/" + item.getSubject().replace("/", "&") + "/" + Boolean.TRUE + "/" + Boolean.TRUE);
            });
            splitButton.addItem(replyToAll);

            MaterialLink forward = new MaterialLink(wfmStrings.forward());
            //forward.addClickHandler(event -> new ComposeView(item.getObjectId(), lastCaseRepliedFromReporterEmail, false, true));
            forward.addClickHandler(clickEvent -> {
                RelationItem.emailItem = lastCaseRepliedFromReporterEmail;
                goTo("emailcompose|add/add/" + item.getObjectId() + "/" + Boolean.FALSE + "/" + Boolean.TRUE);
            });
            splitButton.addItem(forward);

            addButton(splitButton);
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CASE_FORM;
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
    public boolean isCollapse() {
        return !isClientView;
    }

    private void reDrawIFrame() {
        if (content != null) {
            content = content.replaceAll("<script[^>]*>(.*?)</script>|<script[^>]*src=[\\\"'][^\\\"']*[\\\"'][^>]*></script>", "");
            Utils.setContentToIFrame("caseSummary" + this.hashCode(), content);
        }
    }

    private void changeStatusTo(SelectItem status) {
        if (status != null) {
            if (status.isSelected()) {
                new CaseStatusModal(objectId, status.getId(), true);
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_LOAD_STAGE_HISTORY, CaseSummaryView.this, (sender, args) -> {
                    caseStatusHistoryGrid.refresher();
                });
            } else {
                changeCaseStatus(status.getId());
            }
        } else {
            changeCaseStatus(null);
        }
    }

    private void changeCaseStatus(Integer statusId) {
        status.setEnabled(false);
        LoadingPanel.loading(true);
        crmService.updateCaseStatus(objectId, statusId, null, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                status.setEnabled(true);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                status.setEnabled(true);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.caseID()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, item, CaseSummaryView.this);
            }
        });
    }


    private void changeAssigneeTo(SelectItem assignee) {
        if (assignee != null && !assignee.getName().equals(item.getCaseAssigneeName())) {
            if (!assignee.getName().contains("(Department)")) {
                item.setCaseAssigneeName(assignee.getName());
                item.setCaseAssigneeId(assignee.getId());
            } else {
                item.setCaseAssigneeName(null);
                item.setCaseAssigneeId(null);
                item.setDepartmentID(assignee.getId());
                item.setDepartment(assignee.getName());
            }
            changeCase();
        } else {
            item.setCaseAssigneeId(null);
            item.setCaseAssigneeName("");
        }

    }

    private void changeResolverTo(SelectItem resolver) {
        if (resolver != null && !resolver.getName().equals(item.getResolverName())) {
            item.setResolverName(resolver.getName());
            item.setResolverId(resolver.getId());
            changeCase();
        } else {
            item.setCaseAssigneeId(null);
            item.setCaseAssigneeName("");
        }
    }

    private void changeCase() {
        LoadingPanel.loading(true);
        crmService.saveCase(item, false, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, item, CaseSummaryView.this);
            }
        });
    }


    public String getIconStyle() {
        return "casesList cases-list";
    }

    @Override
    public FlowPanel getHelpContainer() {
        return null;
    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(CaseSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
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

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public String getPropertyCode() {
        return CASE_LIST;
    }

    private FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
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
