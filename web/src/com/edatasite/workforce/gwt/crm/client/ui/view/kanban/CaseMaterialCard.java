package com.edatasite.workforce.gwt.crm.client.ui.view.kanban;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NotePopup;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.view.AssigneePopup;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Azazello
 * Date: 5/1/2018
 * Time: 11:59 AM
 */
public class CaseMaterialCard extends Composite {
    interface CaseMaterialCardUiBinder extends UiBinder<Widget, CaseMaterialCard> {
    }

    private static final CaseMaterialCardUiBinder ourUiBinder = GWT.create(CaseMaterialCardUiBinder.class);

    private final CRMServiceAsync crmService = CRMService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private final CaseItem kanbanItem;

    //    @UiField
//    MaterialPanel assignee_photo;
    @UiField
    MaterialLabel number;
    @UiField
    MaterialLink subject;
    @UiField
    MaterialLink reporter;
    @UiField
    ListItem phone;
    @UiField
    ListItem email;
    @UiField
    MaterialLink actionsLink;
    @UiField
    MaterialPanel notesPanel;
    @UiField
    MaterialPanel activitiesPanel;
    private final KanbanBoard kanbanBoard;
    @UiField
    MaterialLink activitiesLink;
    @UiField
    MaterialLabel assigneeName;
    @UiField
    MaterialPanel contactsPanel;
    @UiField
    MaterialPanel actionsLinkpanel;
    private Map<String, KanbanItemColumnConfigs> reletedFieldsMap = new HashMap<>();
    public static Email emailItem;

    public CaseMaterialCard(CaseItem kanbanItem, KanbanBoard kanbanBoard) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.kanbanItem = kanbanItem;
        this.kanbanBoard = kanbanBoard;
        initialize();
    }

    public CaseMaterialCard(CaseItem kanbanItem, KanbanBoard kanbanBoard, Map<String, KanbanItemColumnConfigs> reletedFieldsMap) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.reletedFieldsMap = reletedFieldsMap;
        this.kanbanItem = kanbanItem;
        this.kanbanBoard = kanbanBoard;
        initialize();
    }

    private void initialize() {
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_NUMBER.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_NUMBER.getCode()).isSelected()) {
            String numberVal = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CASE_NUMBER.getCode()).getRelatedFieldCode(), kanbanItem.getCaseNumber());
            number.setText(numberVal);
        } else {
            number.setVisible(false);
        }
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_SUBJECT.getCode()) != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_SUBJECT.getCode()).isSelected()) {
                String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CASE_SUBJECT.getCode()).getRelatedFieldCode(), kanbanItem.getSubject());
                subject.setText(val);
            } else {
                subject.setVisible(false);
            }
        } else {
            subject.setText(kanbanItem.getSubject());
        }
        subject.addClickHandler(clickEvent -> {
            if (kanbanItem != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("case|summary/" + kanbanItem.getObjectId(), kanbanItem.getCaseNumber(), kanbanItem.getSubject());
            }
        });

        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_REPORTER.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_REPORTER.getCode()).isSelected()) {
            String numberVal = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CASE_REPORTER.getCode()).getRelatedFieldCode(), kanbanItem.getReportedBy());
            number.setText(numberVal);
        } else {
            number.setVisible(false);
        }

        reporter.setText(kanbanItem.getReportedBy());
        if (kanbanItem.getAccountId() != null) {
            reporter.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + kanbanItem.getAccountId()));
        } else if (kanbanItem.getLeadId() != null) {
            reporter.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + kanbanItem.getLeadId()));
        } else if (kanbanItem.getCrmContactID() != null) {
            reporter.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + kanbanItem.getCrmContactID()));
        }

//        if (!Utils.isNullOrEmpty(kanbanItem.getCaseAssigneeImageUrl())) {
//            MaterialImage photo = new MaterialImage(kanbanItem.getCaseAssigneeImageUrl());
//            photo.setWidth("40px");
//            photo.setHeight("40px");
//            photo.setBorder("0");
//            photo.setCircle(true);
//            assignee_photo.add(photo);
//        } else if(kanbanItem.getCaseAssigneeName()!=null) {
//            String s[] = kanbanItem.getCaseAssigneeName().split(" ");
//            String t = null;
//            if(s.length>1) {
//                t = (s[0].substring(0,1) + s[1].substring(0,1)).toUpperCase();
//            } else if(s[0]!=null && s[0].length()>0) {
//                t = s[0].substring(0,1).toUpperCase();
//            }
//
//            if(t!=null) {
//                MaterialLabel initials = new MaterialLabel( t );
//                initials.setStyleName("wg_canban__entry-inits");
//                assignee_photo.add(initials);
//            }
//        }
        //calls
        Icon iCall = new Icon();
        iCall.setStyleName("ficon--phone2");
        boolean isVisiblePhone = reletedFieldsMap.get(KanbanItemSettingEnum.CASE_PHONE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_PHONE.getCode()).isSelected();

        if (isVisiblePhone) {
            if (kanbanItem.getPhone() != null && kanbanItem.getPhone().length() > 4) {
                MaterialLink callLink = new MaterialLink();
                callLink.add(iCall);
                if (Utils.hasGenericAccess(GenericSettingsEnum.SWITCHVOX_ENABLED)) {
                    callLink.addClickHandler(clickEvent -> {
                        Info.show("Dialing...");
                        crmService.switchvoxCall(kanbanItem.getPhone(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void success(Void result) {
                            }
                        });
                    });
                } else {
                    callLink.setHref("callto://" + kanbanItem.getPhone());
                }
                phone.add(callLink);
            } else {
                Span callLabel = new Span();
                callLabel.add(iCall);
                phone.add(callLabel);
            }
        } else {
            phone.setVisible(false);
        }
        boolean isVisibleEmail = reletedFieldsMap.get(KanbanItemSettingEnum.CASE_EMAIL.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_EMAIL.getCode()).isSelected();

        //Email

        Icon iEmail = new Icon();
        iEmail.setStyleName("ficon--at");
        if (isVisibleEmail) {
            MaterialLink emailLink = new MaterialLink();
            emailLink.add(iEmail);
            emailLink.addClickHandler(clickEvent -> {
                if (kanbanItem.getTrackerID() != null) {
                    LoadingPanel.loading(true);
                    crmService.getCaseEmail(kanbanItem.getEmailID(), kanbanItem.getTrackerID(), new AbstractAsyncCallback<Email>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            //new ComposeView(kanbanItem.getObjectId(), null, true, false);
                            SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + kanbanItem.getObjectId() + "/" + Boolean.TRUE + "/" + Boolean.FALSE);
                        }

                        @Override
                        public void success(Email email) {
                            LoadingPanel.loading(false);
                            emailItem = email;
                            //new ComposeView(kanbanItem.getObjectId(), email, true, false);
                            SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + kanbanItem.getObjectId() + "/" + Boolean.TRUE + "/" + Boolean.FALSE);
                        }
                    });
                } else {
                    //new ComposeView(kanbanItem.getObjectId(), null, true, false);
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + kanbanItem.getObjectId() + "/" + Boolean.TRUE + "/" + Boolean.FALSE);
                }
            });
            email.add(emailLink);
        } else {
            email.setVisible(false);
        }
        if (!(isVisibleEmail || isVisiblePhone)) {
            contactsPanel.setVisible(false);
        }

//        if (!Utils.isNullOrEmpty(kanbanItem.getEmail())) {
//        } else {
//            Span emailLabel = new Span();
//            emailLabel.add(iEmail);
//            email.add(emailLabel);
//        }
        //Footer Of Card
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_ACTION.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_ACTION.getCode()).isSelected()) {
            initKanbanActions(kanbanItem);
        } else {
            actionsLinkpanel.setVisible(false);
        }
        //Notes
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_NOTE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_NOTE.getCode()).isSelected()) {
            if (!Utils.isNullOrEmpty(kanbanItem.getLastNote())) {
                MaterialLink notesLink = getKanbanNotes(kanbanItem);
                Icon notesLinkIcon = new Icon();
                notesLinkIcon.setStyleName("ficon--tag-down");
                notesLinkIcon.setTooltip(wfmStrings.lastNote());
                notesLink.add(notesLinkIcon);
                MaterialPanel notesKitPanel = new MaterialPanel("dropdown-kit--arrow--below");
                notesKitPanel.add(notesLink);
                notesPanel.add(notesKitPanel);
            }
        }

//        //Activities
//        initKanbanActivities(kanbanItem);
        //Activities menu
        if (!Utils.isNullOrEmpty(kanbanItem.getPriority())) {
            activitiesLink.setTooltip(kanbanItem.getPriority());
        }
        activitiesLink.getElement().getStyle().setBackgroundColor(!Utils.isNullOrEmpty(kanbanItem.getPriorityColor()) ? kanbanItem.getPriorityColor() : "#f5a623");
        //Assignee
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CASE_ASSIGNE_NAME.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CASE_ASSIGNE_NAME.getCode()).isSelected()) {
            String assigneName = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CASE_ASSIGNE_NAME.getCode()).getRelatedFieldCode(), kanbanItem.getCaseAssigneeName());
            assigneeName.setText(assigneName);
        }
        //Build card item
        ensureDebugId("case_material_card_" + kanbanItem.getObjectId());
    }

    private void initKanbanActions(final CaseItem kanbanItem) {
        MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);
        menuContainer.setBelowOrigin(true);
        actionsLink.add(menuContainer);

        actionsLink.addClickHandler(clickEvent -> {
            menuContainer.clear();
            if (Window.getClientHeight() / 2 < Utils.getElementTop(actionsLink.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                actionsLink.getParent().addStyleName("dropdown-kit--arrow--below--reverse");
            } else {
                actionsLink.getParent().removeStyleName("dropdown-kit--arrow--below--reverse");
            }
            //Summary
            MaterialLink summaryLink = new MaterialLink(wfmStrings.summaryView());
            summaryLink.ensureDebugId("case-view-" + kanbanItem.getObjectId());
            summaryLink.addClickHandler(clickEvent1 -> SinksContainerFactory.entryPoint.onHistoryChanged("case|summary/" + kanbanItem.getObjectId(), kanbanItem.getCaseNumber(), kanbanItem.getSubject()));
            menuContainer.add(summaryLink);
            //Edit
            if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
                MaterialLink editLink = new MaterialLink(wfmStrings.edit());
                editLink.ensureDebugId("case-edit-" + kanbanItem.getObjectId());
                editLink.addClickHandler(clickEvent12 -> SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/" + kanbanItem.getObjectId(), kanbanItem.getCaseNumber(), kanbanItem.getSubject()));
                menuContainer.add(editLink);
            }
            //Copy
            if (Utils.hasPermission(PermissionConstants.CRM_COPY_CASE, PermissionConstants.CRM_EDIT_CASE)) {
                MaterialLink copyCase = new MaterialLink(wfmStrings.copy());
                copyCase.ensureDebugId("case-copy-" + kanbanItem.getObjectId());
                copyCase.addClickHandler(clickEvent1 -> SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/" + kanbanItem.getObjectId() + "/" + Constants.COPY));
                menuContainer.add(copyCase);
            }
            //Change Assignee
            if (Utils.hasPermission(PermissionConstants.CRM_CHANGE_ASSIGNEE_CASE, PermissionConstants.CRM_EDIT_CASE)) {
                MaterialLink changeAssignee = new MaterialLink(crmStrings.changeAssignee());
                changeAssignee.ensureDebugId("case-change-assignee-" + kanbanItem.getObjectId());
                changeAssignee.addClickHandler(clickEvent1 -> {
                    AssigneePopup assigneePopup = new AssigneePopup(RelationItem.TYPE_CASE);
                    assigneePopup.setListRefresh(() -> kanbanBoard.reloadAllColumns());
                    assigneePopup.getItemIDs().clear();
                    assigneePopup.addItemID(kanbanItem.getObjectId());
                    assigneePopup.open();
                });
                menuContainer.add(changeAssignee);
            }
            //Close
            /*
            Munir asked to remove this option
            if (Utils.hasPermission(PermissionConstants.CRM_CLOSE_CASE, PermissionConstants.CRM_EDIT_CASE)) {
                MaterialLink closeCase = new MaterialLink(crmStrings.closeCase());
                closeCase.ensureDebugId("case-close-" + kanbanItem.getObjectId());
                closeCase.addClickHandler(clickEvent1 -> new CloseCaseView(kanbanItem, () -> kanbanBoard.reloadAllColumns()));
                menuContainer.add(closeCase);
            }*/
            //Delete
            if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CASE)) {
                MaterialLink deleteLink = new MaterialLink(wfmStrings.delete());
                deleteLink.ensureDebugId("delete-" + kanbanItem.getObjectId());
                deleteLink.addClickHandler(clickEvent13 -> {
                    if (kanbanItem != null) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.messAreDelete() + "&nbsp <font color='#15428B'><b> \"" + kanbanItem.getSubject() + "\"</b></font> ?");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CRMService.App.get().deleteCase(kanbanItem.getObjectId(), new AbstractAsyncCallback() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.error());
                                    }

                                    @Override
                                    public void success(Object result) {
                                        WfmWindow.confirm(Property.get(Constants.CASE_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.caseID()));
                                        kanbanBoard.reloadAllColumns();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    }
                });
                menuContainer.add(deleteLink);
            }
        });
    }

    private MaterialLink getKanbanNotes(final CaseItem kanbanItem) {
        MaterialLink notesLink = new MaterialLink();

        MaterialDropDown menuContainer = new MaterialDropDown(notesLink);
        menuContainer.setBelowOrigin(true);
        notesLink.add(menuContainer);

        notesLink.addClickHandler(clickEvent -> {
            menuContainer.clear();
            MaterialPanel wg_canban__dropdown = new MaterialPanel("wg_canban__dropdown");
            MaterialPanel wg_canban__dropdown_header = new MaterialPanel("wg_canban__dropdown-header");
            MaterialPanel wg_canban__dropdown_notes_icon = new MaterialPanel("wg_canban__dropdown-notes-icon");
            Icon ficon__tag_down = new Icon();
            ficon__tag_down.setStyleName("ficon--tag-down");
            wg_canban__dropdown_notes_icon.add(ficon__tag_down);
            wg_canban__dropdown_header.add(wg_canban__dropdown_notes_icon);
            wg_canban__dropdown.add(wg_canban__dropdown_header);

            //Content (Last note text)
            MaterialPanel wg_canban__dropdown_title = new MaterialPanel("wg_canban__dropdown-title");
            wg_canban__dropdown_title.getElement().setInnerText(wfmStrings.lastNote());
            wg_canban__dropdown_title.setStyleName("wg_canban__dropdown-title");
            MaterialPanel wg_canban__dropdown_text = new MaterialPanel("wg_canban__dropdown-text");
            wg_canban__dropdown_text.getElement().setInnerText(kanbanItem.getLastNote());
            MaterialPanel wg_canban__dropdown_content = new MaterialPanel("wg_canban__dropdown-content");
            wg_canban__dropdown_content.add(wg_canban__dropdown_title);
            wg_canban__dropdown_content.add(wg_canban__dropdown_text);

            MaterialPanel wg_canban__dropdown_footer = new MaterialPanel("wg_canban__dropdown-footer");
            MaterialPanel cp_btn_list = new MaterialPanel("cp_btn-list");
            MaterialPanel cp_btn_list_item = new MaterialPanel("cp_btn-list-item");
            MaterialLink addNote = new MaterialLink();
            addNote.setStyleName("elm_btn elm_btn--add");
            addNote.addClickHandler(clickEvent1 -> new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_CASE));
            Span addNoteLabel = new Span(wfmStrings.addNote());
            addNoteLabel.setStyleName("cp_btn-list-item-title");
            addNoteLabel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            addNoteLabel.addClickHandler(clickEvent1 -> new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_CASE));
            cp_btn_list_item.add(addNote);
            cp_btn_list_item.add(addNoteLabel);
            cp_btn_list.add(cp_btn_list_item);
            wg_canban__dropdown_footer.add(cp_btn_list);

            wg_canban__dropdown.add(wg_canban__dropdown_content);
            wg_canban__dropdown.add(wg_canban__dropdown_footer);

            menuContainer.add(wg_canban__dropdown);
        });
        return notesLink;
    }

    private void initKanbanActivities(final CaseItem kanbanItem) {
        MaterialDropDown menuContainer = new MaterialDropDown(activitiesLink);
        menuContainer.setBelowOrigin(true);
        activitiesLink.add(menuContainer);

        activitiesLink.addClickHandler(clickEvent -> {

            menuContainer.clear();
//            LeadActivitiesDropdown activitiesDropdown = new LeadActivitiesDropdown(kanbanItem, kanbanBoard);
//            menuContainer.add(activitiesDropdown);

            crmService.getLastActivities(kanbanItem.getObjectId(), RelationItem.TYPE_CASE, new AbstractAsyncCallback<ListResult<Appointment>>() {
                @Override
                public void success(ListResult<Appointment> result) {
//                    activitiesDropdown.setActivities(result);
                }
            });
        });
    }

    private String getRealValueByCode(String fieldCode, String defaultValue) {
        if (fieldCode == null) {
            return defaultValue;
        }
        if (fieldCode.contains("string_value") || fieldCode.contains("date_value") || fieldCode.contains("double_value")) {
            return Utils.getKanbanItemValueFromObject(fieldCode, kanbanItem.getCustomFieldsMap().get(fieldCode));
        }
        String result = "";
        if (KanbanItemSettingEnum.CASE_NUMBER.getCode().equals(fieldCode)) {
            result = kanbanItem.getCaseNumber();
        } else if (KanbanItemSettingEnum.CASE_ASSIGNE_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getCaseAssigneeName();
        } else if (KanbanItemSettingEnum.CASE_SUBJECT.getCode().equals(fieldCode)) {
            result = kanbanItem.getSubject();
        } else if (KanbanItemSettingEnum.CASE_REPORTER.getCode().equals(fieldCode)) {
            result = kanbanItem.getReportedBy();
        } else {
            result = defaultValue;
        }
        return result == null || result.isEmpty() ? "" : result;
    }
}