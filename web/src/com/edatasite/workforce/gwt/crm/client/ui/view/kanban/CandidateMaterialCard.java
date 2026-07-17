package com.edatasite.workforce.gwt.crm.client.ui.view.kanban;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NotePopup;
import com.edatasite.workforce.gwt.core.client.ui.NotePopupCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CandidateMaterialCard extends Composite implements PermissionConstants {
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    //    
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final CandidateMaterialCard.CandidateMaterialCardUiBinder ourUiBinder = GWT.create(CandidateMaterialCard.CandidateMaterialCardUiBinder.class);
    protected CRMServiceAsync crmService = CRMService.App.get();
    protected ContactServiceAsync contactService = ContactService.App.get();
    ContactListItem kanbanItem;

    Map<String, KanbanItemColumnConfigs> reletedFieldsMap = new HashMap<>();
    @UiField
    MaterialLink leadName;
    //    @UiField
//    MaterialPanel info2;
    @UiField
    ListItem liCall;
    @UiField
    ListItem liEmail;
    @UiField
    MaterialLink actionsLink;
    @UiField
    MaterialLabel position;
    @UiField
    MaterialLabel department;
    @UiField
    MaterialPanel notesPanel;
    @UiField
    MaterialLink activitiesLink;
    @UiField
    MaterialPanel activitiesPanel;
    @UiField
    MaterialPanel contactPanel;
    @UiField
    MaterialLabel leadAssigneeName;

    public CandidateMaterialCard(ContactListItem kanbanItem) {
        super();
        this.kanbanItem = kanbanItem;
        initWidget(ourUiBinder.createAndBindUi(this));

        fillData();
    }

    public CandidateMaterialCard(ContactListItem kanbanItem, Map<String, KanbanItemColumnConfigs> reletedFieldsMap) {
        super();
        this.kanbanItem = kanbanItem;
        this.reletedFieldsMap = reletedFieldsMap;
        initWidget(ourUiBinder.createAndBindUi(this));

        fillData();
    }

    private void fillData() {
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NAME.getCode()) != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NAME.getCode()).isSelected()) {
                String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NAME.getCode()).getRelatedFieldCode(), kanbanItem.getName());
                leadName.setText(val);
                if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NAME.getCode()).getRelatedFieldCode() == null) {
                    leadName.addClickHandler(clickEvent -> {
                        if (kanbanItem != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + kanbanItem.getObjectId() + "/" + (kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getObjectId() : ""), kanbanItem.getName(), kanbanItem.getName());
                        }
                    });
                }
            } else {
                leadName.setVisible(false);
            }
        } else {
            leadName.setText(kanbanItem.getName());
            leadName.addClickHandler(clickEvent -> {
                if (kanbanItem != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + kanbanItem.getObjectId() + "/" + (kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getObjectId() : ""), kanbanItem.getName(), kanbanItem.getName());
                }
            });
        }
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_POSITION.getCode()) != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_POSITION.getCode()).isSelected()) {
                String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_POSITION.getCode()).getRelatedFieldCode(), kanbanItem.getPosition());
                position.setText(val);
            }
        } else {
            position.setText(kanbanItem.getPosition());
        }

        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode()) != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode()).isSelected()) {
                String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode()).getRelatedFieldCode(), kanbanItem.getDepartment());
                department.setText(val);
            }
        } else {
            department.setText(kanbanItem.getDepartment());
        }


//        if (kanbanItem.getCrmAccount().getName() != null && !kanbanItem.getCrmAccount().getName().trim().equals("")) {
//            MaterialLabel company = new MaterialLabel(kanbanItem.getCrmAccount().getName());
//            company.setStyleName("wg_canban__entry-company");
//            info2.add(company);
//        }


        //calls
        Element iCall = DOM.createElement("i");
        iCall.setClassName("ficon--phone2");
        boolean isVisiblePhone = reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_PHONE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_PHONE.getCode()).isSelected();

        if (isVisiblePhone) {
            if (kanbanItem.getPrimaryPhone() != null && kanbanItem.getPrimaryPhone().length() > 4) {
                MaterialLink callLink = new MaterialLink();
                callLink.getElement().appendChild(iCall);


                callLink.addClickHandler(clickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL) && !Utils.isNullOrEmpty(kanbanItem.getPrimaryPhone())) {
                        Integer relationId = kanbanItem != null ? kanbanItem.getObjectId() : null;
                        String relationType = kanbanItem.isLeadContact() ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT;
                        String relationName = kanbanItem != null ? kanbanItem.getRelationName() : null;

                        Integer finalRelationId = relationId;
                        new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(kanbanItem.isLeadContact() ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT, kanbanItem.getObjectId(), kanbanItem.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, kanbanItem.getCrmAccount().getObjectId(), kanbanItem.getCrmAccount().getName()));

                    } else if (!Utils.isNullOrEmpty(kanbanItem.getPrimaryPhone())) {
                        Info.warn(crmStrings.youDontHavePermissiontoCall());
                    }
                });
                liCall.add(callLink);
            } else {
                Span callLabel = new Span();
                callLabel.getElement().appendChild(iCall);
                new KpiToolTip(callLabel, wfmStrings.noPhone());
                liCall.add(callLabel);
            }
        } else {
            liCall.setVisible(false);
        }
        boolean isVisibleEmail = reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_EMAIL.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_EMAIL.getCode()).isSelected();
        Element iEmail = DOM.createElement("i");
        iEmail.setClassName("ficon--at");

        if (isVisibleEmail) {
            if (Utils.hasPermission(CRM_MESSAGE_CENTER) && !Utils.isNullOrEmpty(kanbanItem.getPrimaryEmail())) {

                MaterialLink emailLink = new MaterialLink();
                emailLink.getElement().appendChild(iEmail);
                emailLink.addClickHandler(clickEvent -> {
                    if (!kanbanItem.isEmailOptOut()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + kanbanItem.getPrimaryEmail() + "/" + RelationItem.TYPE_CANDIDATE + "/" + kanbanItem.getObjectId() + "/" + kanbanItem.getName());
                    } else {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.open();
                    }
                });
                liEmail.add(emailLink);
            } else {
                Span emailLabel = new Span();
                emailLabel.getElement().appendChild(iEmail);
                new KpiToolTip(emailLabel, crmStrings.noEmail());
                liEmail.add(emailLabel);
            }
        } else {
            liEmail.setVisible(false);
        }

        if (!(isVisiblePhone || isVisibleEmail)) {
            contactPanel.setVisible(false);
        }
        //Footer Of Card
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_ACTION.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_ACTION.getCode()).isSelected()) {
            initKanbanActions(kanbanItem);
        }

        //Notes
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NOTE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_NOTE.getCode()).isSelected()) {
            initNotesIcon();
        }

        //Activities
        initKanbanActivities(kanbanItem);
        //Activities menu
        if ("OVERDUE".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--overdue");
        } else if ("AVAILABLE".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--available");
        } else if ("NO_TASKS".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--notasks");
        }

        //Assignee
        if (reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_LEAD_ASSIGNEE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_LEAD_ASSIGNEE.getCode()).isSelected()) {
            String vacancyValue = kanbanItem.getVacancies() != null ? kanbanItem.getVacancies().get(0).getName() : "";
            String assigneName = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.CANDIDATE_LEAD_ASSIGNEE.getCode()).getRelatedFieldCode(), vacancyValue);
            leadAssigneeName.setText(assigneName);
        } else {
            leadAssigneeName.setVisible(false);
        }


        //Build card item
        this.ensureDebugId(kanbanItem.getObjectId().toString());
    }

    private void initNotesIcon() {
        if (!Utils.isNullOrEmpty(kanbanItem.getNote())) {
            //Notes
            MaterialLink notesLink = getKanbanNotes(kanbanItem)/*.getAction()*/;
            /*Icon actionsLinkIcon = new Icon();
            actionsLinkIcon.setStyleName("ficon--more-horiz");*/
            Element notesLinkIcon = DOM.createElement("i");
            notesLinkIcon.setClassName("ficon--tag-down");
            notesLink.getElement().appendChild(notesLinkIcon);
            MaterialPanel notesKitPanel = new MaterialPanel("dropdown-kit--arrow--below");
            notesKitPanel.add(notesLink);

            notesPanel.clear();
            notesPanel.add(notesKitPanel);
        }
    }

    private void initKanbanActions(final ContactListItem kanbanItem) {

        MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);

        menuContainer.setBelowOrigin(true);
        actionsLink.add(menuContainer);

        actionsLink.setId("candidatecardaction_" + kanbanItem.getObjectId());

        actionsLink.addClickHandler(clickEvent -> {
            menuContainer.clear();

            if (Window.getClientHeight() / 2 < Utils.getElementTop(actionsLink.getElement())) {
                actionsLink.getParent().addStyleName("dropdown-kit--arrow--below--reverse");
            } else {
                actionsLink.getParent().removeStyleName("dropdown-kit--arrow--below--reverse");
            }
            //Summary View
            MaterialLink summaryLink = new MaterialLink(wfmStrings.summaryView());
            summaryLink.ensureDebugId("candidate-view-" + kanbanItem.getObjectId());
            summaryLink.addClickHandler(clickEvent1 -> {
                if (kanbanItem != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + kanbanItem.getObjectId() + "/" + (kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getObjectId() : ""), kanbanItem.getLastName());
                }
            });
            menuContainer.add(summaryLink);
            //Lead Edit
            if (Utils.hasPermission(HRMS_EDIT_CANDIDATE) && kanbanItem.getCandidateStatus() != null && kanbanItem.getCandidateStatus().isAllowEdit()) {
                MaterialLink editLink = new MaterialLink(wfmStrings.edit());
                editLink.ensureDebugId("candidate-edit-" + kanbanItem.getObjectId());
                editLink.addClickHandler(clickEvent12 -> {
                    if (kanbanItem != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("candidateedit|editcandidate/" + kanbanItem.getObjectId() + "/" + (kanbanItem.getCrmAccount() != null ? kanbanItem.getCrmAccount().getObjectId() : ""), kanbanItem.getLastName());
                    }
                });
                menuContainer.add(editLink);
            }
            //Write Note
            MaterialLink writeNoteLink = new MaterialLink(wfmStrings.addNote());
            writeNoteLink.ensureDebugId("write-note-" + kanbanItem.getObjectId());
            writeNoteLink.addClickHandler(clickEvent13 -> {
                if (kanbanItem != null) {
                    new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_CANDIDATE, onNoteSavedCommand);
                }
            });
            menuContainer.add(writeNoteLink);

            if (Utils.hasPermission(PermissionConstants.HRMS_MAKE_PLACEMENT)) {
                MaterialLink makePlacement = new MaterialLink(hrmsStrings.makePlacement());
                makePlacement.addClickHandler(event  -> SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add/" + kanbanItem.getObjectId()));
                menuContainer.add(makePlacement);

            }
            //Lead Convert
//            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_CONVERT)) {
//                MaterialLink convertLink = new MaterialLink(crmStrings.convert());
//                convertLink.ensureDebugId("convert-" + kanbanItem.getObjectId());
//                convertLink.addClickHandler(clickEvent14 -> {
//                    if (kanbanItem != null) {
//                        new ConvertLeadView(kanbanItem, null);
//                    }
//                });
//                menuContainer.add(convertLink);
//            }
            //Edit Subscriptions
//            if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
//                MaterialLink editSubscriptionsLink = new MaterialLink(crmStrings.editSubscriptions());
//                editSubscriptionsLink.ensureDebugId("edit-subscriptions-" + kanbanItem.getObjectId());
//                editSubscriptionsLink.addClickHandler(clickEvent15 -> {
//                    if (kanbanItem != null) {
//                        showEditSubscriptionsShell(kanbanItem.getObjectId());
//                    }
//                });
//                menuContainer.add(editSubscriptionsLink);
//            }
            //Delete
            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_DELETE)) {
                MaterialLink deleteLink = new MaterialLink(wfmStrings.delete());
                deleteLink.ensureDebugId("delete-" + kanbanItem.getObjectId());
                deleteLink.addClickHandler(clickEvent16 -> {
                    if (kanbanItem != null) {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ArrayList<Integer> ids = new ArrayList<>();
                                ids.add(kanbanItem.getObjectId());
                                contactService.deleteContacts(ids, null, false, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(ArrayList<Integer> result) {
                                        Info.show(Property.get(Constants.CANDIDATE, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.lead()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_DELETE, result, CandidateMaterialCard.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    }
                });
                menuContainer.add(deleteLink);
            }
        });
    }

    NotePopupCommand onNoteSavedCommand = new NotePopupCommand() {
        @Override
        public void onSaved(String note) {
            kanbanItem.setNote(note);
            initNotesIcon();
        }

        @Override
        public void onSave(String note) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setContactID(kanbanItem.getObjectId());

            LoadingPanel.loading(true);
            CRMService.App.get().saveCrmNote(fp, note, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(final Void o) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.note()), Info.Type.INFO);
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_ADD, note, NotePopup.this);
                    onSaved(note);

                }
            });
        }
    };

    private MaterialLink getKanbanNotes(final ContactListItem kanbanItem) {

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
            wg_canban__dropdown_text.getElement().setInnerText(kanbanItem.getNote());
            MaterialPanel wg_canban__dropdown_content = new MaterialPanel("wg_canban__dropdown-content");
            wg_canban__dropdown_content.add(wg_canban__dropdown_title);
            wg_canban__dropdown_content.add(wg_canban__dropdown_text);

            MaterialPanel wg_canban__dropdown_footer = new MaterialPanel("wg_canban__dropdown-footer");
            MaterialPanel cp_btn_list = new MaterialPanel("cp_btn-list");
            MaterialPanel cp_btn_list_item = new MaterialPanel("cp_btn-list-item");
            MaterialLink addNote = new MaterialLink();
            addNote.setStyleName("elm_btn elm_btn--add");
            addNote.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if (kanbanItem != null) {
                        new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_LEAD, onNoteSavedCommand);
                    }
                }
            });
            Span addNoteLabel = new Span(wfmStrings.addNote());
            addNoteLabel.setStyleName("cp_btn-list-item-title");
            addNoteLabel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            addNoteLabel.addClickHandler(clickEvent1 -> new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_CANDIDATE));
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

    private void initKanbanActivities(final ContactListItem kanbanItem) {

        MaterialDropDown menuContainer = new MaterialDropDown(activitiesLink);
        menuContainer.setBelowOrigin(true);
        activitiesLink.add(menuContainer);

        activitiesLink.addClickHandler(clickEvent -> {

            menuContainer.clear();
            LeadActivitiesDropdown activitiesDropdown = new LeadActivitiesDropdown(kanbanItem);
            menuContainer.add(activitiesDropdown);

            crmService.getLastActivities(kanbanItem.getObjectId(), RelationItem.TYPE_CANDIDATE, new AbstractAsyncCallback<ListResult<Appointment>>() {
                @Override
                public void success(ListResult<Appointment> result) {
                    activitiesDropdown.setActivities(result);
                }
            });


        });

    }

    public void showEditSubscriptionsShell(final Integer contactId) {
        KpiModal editSubscriptionsShell = new KpiModal();
        editSubscriptionsShell.setTitle(wfmStrings.subscriptionLists());
        editSubscriptionsShell.setSize("350px", "400px");

        CheckboxMailingListDataGrid mailListTable = new CheckboxMailingListDataGrid(contactId, false, null);
        mailListTable.setSize("300px", "150px");
        editSubscriptionsShell.add(mailListTable);
        editSubscriptionsShell.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> editSubscriptionsShell.close()));
        editSubscriptionsShell.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setContactID(contactId);
            LoadingPanel.loading(true);
            MassMailService.App.get().updateCrmEntityMailLists(fp, mailListTable.getSelectedIdsList(), new AbstractAsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.subscriptions()), Info.Type.INFO);
                    editSubscriptionsShell.close();
                }
            });
        }));
        editSubscriptionsShell.open();
    }

    interface CandidateMaterialCardUiBinder extends UiBinder<Widget, CandidateMaterialCard> {
    }

    private String getRealValueByCode(String fieldCode, String defaultValue) {
        if (fieldCode == null) {
            return defaultValue;
        }
        if (fieldCode.contains("string_value") || fieldCode.contains("date_value") || fieldCode.contains("double_value")) {
            return Utils.getKanbanItemValueFromObject(fieldCode, kanbanItem.getCustomFieldsMap().get(fieldCode));
        }
        String result = "";
        if (KanbanItemSettingEnum.CANDIDATE_LEAD_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getVacancies().get(0).getName();
        } else if (KanbanItemSettingEnum.CANDIDATE_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getName() != null ? kanbanItem.getName() : "";
        } else if (KanbanItemSettingEnum.CANDIDATE_LEAD_ASSIGNEE.getCode().equals(fieldCode)) {
            result = Utils.isNullOrEmpty(kanbanItem.getLeadAssignee()) ? kanbanItem.getLeadAssignee() : "";
        } else if (KanbanItemSettingEnum.CANDIDATE_POSITION.getCode().equals(fieldCode)) {
            result = kanbanItem.getPosition() != null ? kanbanItem.getPosition() : "";
        } else if (KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode().equals(fieldCode)) {
            result = kanbanItem.getDepartment() != null ? kanbanItem.getDepartment() : "";
        } else if (KanbanItemSettingEnum.CANDIDATE_LOCATION.getCode().equals(fieldCode)) {
            result  = kanbanItem.getCandidateLocation() != null ? kanbanItem.getCandidateLocation() :"";
        } else {
            result = defaultValue;
        }
        return result == null || result.isEmpty() ? "" : result;
    }

}
