package com.edatasite.workforce.gwt.crm.client.ui.view.kanban;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;

/**
 * Created by Anvar Akramov on 4/25/18.
 */
public class OpportunityMaterialCard extends Composite implements PermissionConstants {

    interface OpportunityMaterialCardUiBinder extends UiBinder<Widget, OpportunityMaterialCard> {
    }

    private static final OpportunityMaterialCardUiBinder ourUiBinder = GWT.create(OpportunityMaterialCardUiBinder.class);

    OpportunityListItem kanbanItem;

    protected CRMServiceAsync crmService = CRMService.App.get();
    protected ContactServiceAsync contactService = ContactService.App.get();

    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    //    
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    @UiField
    MaterialLink opportunityName;
    @UiField
    MaterialPanel info2;
    @UiField
    MaterialPanel entry_photo;
    @UiField
    ListItem liCall;
    @UiField
    ListItem liEmail;
    @UiField
    MaterialPanel contactsPanel;
    @UiField
    MaterialLink actionsLink;
    @UiField
    MaterialPanel notesPanel;
    @UiField
    MaterialLink activitiesLink;
    @UiField
    MaterialPanel activitiesPanel;
    @UiField
    MaterialLabel opportunityAssigneeName;
    @UiField
    MaterialPanel amountPanel;
    @UiField
    MaterialPanel actionPanel;

    NotePopupCommand onNoteSavedCommand = new NotePopupCommand() {

        @Override
        public void onSave(String note) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setOpportunityID(kanbanItem.getObjectId());

            LoadingPanel.loading(true);
            CRMService.App.get().saveCrmNote(fp, note, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(final Void o) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.note()), Info.Type.INFO);
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_ADD, noteTextArea.getText(), NotePopup.this);
                    onSaved(note);
                }
            });
        }

        @Override
        public void onSaved(String note) {
            kanbanItem.setNote(note);
            initNotesIcon();
        }
    };

    Map<String, KanbanItemColumnConfigs> reletedFieldsMap = new HashMap<>();

    public OpportunityMaterialCard(OpportunityListItem kanbanItem) {
        super();
        this.kanbanItem = kanbanItem;
        initWidget(ourUiBinder.createAndBindUi(this));
        fillData();
    }

    public OpportunityMaterialCard(OpportunityListItem kanbanItem, Map<String, KanbanItemColumnConfigs> reletedFieldsMap) {
        super();
        this.kanbanItem = kanbanItem;
        this.reletedFieldsMap = reletedFieldsMap;
        initWidget(ourUiBinder.createAndBindUi(this));
        fillData();
    }

    private void fillData() {

        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode()) != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode()).isSelected()) {
                String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode()).getRelatedFieldCode(), kanbanItem.getOpportunityName());
                opportunityName.setText(val);
//                if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode()).getRelatedFieldCode() == null) {
                    opportunityName.addClickHandler(clickEvent -> {
                        if (kanbanItem != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + kanbanItem.getObjectId() + "/" + kanbanItem.isConvertedLead() + "/" + kanbanItem.getContactId() + "/" + kanbanItem.getAccountId(), kanbanItem.getNumberData() != null ? kanbanItem.getNumberData().getNumberString() : kanbanItem.getOpportunityName(), kanbanItem.getOpportunityName());
                        }
                    });
//                }
            } else {
                opportunityName.setVisible(false);
            }
        } else {
            opportunityName.setText((!Utils.isNullOrEmpty(kanbanItem.getOpportunityName()) ? kanbanItem.getOpportunityName() : kanbanItem.getCrmAccountItem() != null && !Utils.isNullOrEmpty(kanbanItem.getCrmAccountItem().getName()) ? kanbanItem.getCrmAccountItem().getName() : kanbanItem.getContact() != null ? kanbanItem.getContact() : ""));
            opportunityName.addClickHandler(clickEvent -> {
                if (kanbanItem != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + kanbanItem.getObjectId() + "/" + kanbanItem.isConvertedLead() + "/" + kanbanItem.getContactId() + "/" + kanbanItem.getAccountId(), kanbanItem.getNumberData() != null ? kanbanItem.getNumberData().getNumberString() : kanbanItem.getOpportunityName(), kanbanItem.getOpportunityName());
                }
            });
        }
        String crmAccount = "";
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode()) != null) {
            boolean isValid = kanbanItem.getCrmAccountItem() != null && !Utils.isNullOrEmpty(kanbanItem.getCrmAccountItem().getName());
            if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode()).isSelected() && isValid) {
                crmAccount = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode()).getRelatedFieldCode(), kanbanItem.getCrmAccountItem().getName());
            }
        } else {
            crmAccount = kanbanItem.getCrmAccountItem() != null ? kanbanItem.getCrmAccountItem().getName() : "";
        }
        if (!crmAccount.isEmpty()) {
            MaterialLabel company = new MaterialLabel(crmAccount);
            company.setStyleName("wg_canban__entry-company");
            info2.add(company);
        }
        /**
         *<div class="wg_canban__card-amount" style=""><span class="wg_canban__card-amount-value">$20000000</span><span class="wg_canban__card-amount-date">apr 12</span></div>
         */
//        MaterialPanel amountPanel = new MaterialPanel("wg_canban__card-amount");
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode()) != null && kanbanItem.getAmount() != null) {
            String currency = (!"".equals(kanbanItem.getCurrency()) && kanbanItem.getCurrency() != null) ? "  " + " (" + kanbanItem.getCurrency() + ")" : "";
            if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_AMOUT.getCode()).isSelected()) {
                String currencyValue = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_AMOUT.getCode()).getRelatedFieldCode(), numberFormat.format(kanbanItem.getAmount() != null ? kanbanItem.getAmount() : 0d) + currency);
                MaterialLabel amount = new MaterialLabel(currencyValue);
                amount.setStyleName("wg_canban__card-amount-value txt-elem--ellipsis");
                amountPanel.add(amount);
            }

        }
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_CLOSEDATE.getCode()) != null && kanbanItem.getClosingDate() != null) {
            if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_CLOSEDATE.getCode()).isSelected()) {
                String dateValue = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_CLOSEDATE.getCode()).getRelatedFieldCode(), DateUtils.format(kanbanItem.getClosingDate()));
                MaterialLabel closingDate = new MaterialLabel(dateValue);
                closingDate.setStyleName("wg_canban__card-amount-date");
                amountPanel.add(closingDate);
            }
        }
//        info2.add(amountPanel);
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ENTRY_PHOTO.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ENTRY_PHOTO.getCode()).isSelected()) {
            if (kanbanItem.getOpportunityImageUrl() != null && !kanbanItem.getOpportunityImageUrl().trim().equals("")) {
                MaterialImage photo = new MaterialImage(kanbanItem.getOpportunityImageUrl());
                photo.setWidth("40px");
                photo.setHeight("40px");
                photo.setBorder("0");
                photo.setCircle(true);
                entry_photo.add(photo);
            } else if (kanbanItem.getAssignee() != null) {
                MaterialLabel initials = new MaterialLabel(Utils.getFirstTwoLetters(kanbanItem.getAssignee()));
                initials.setStyleName("wg_canban__entry-inits");
                entry_photo.add(initials);
            }
        } else {
            entry_photo.setVisible(false);
        }


        //calls
        Element iCall = DOM.createElement("i");
        iCall.setClassName("ficon--phone2");

        boolean isVisiblePhone = reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_PHONE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_PHONE.getCode()).isSelected();
        if (isVisiblePhone) {
            Integer relationId = kanbanItem != null ? kanbanItem.getObjectId() : null;
            String relationType = RelationItem.TYPE_OPPORTUNITY;
            String relationName = kanbanItem != null ? kanbanItem.getOpportunityName() : null;
            if ((kanbanItem.getContactPrimaryPhone() != null && kanbanItem.getContactPrimaryPhone().length() > 4) || kanbanItem.getAccountId() != null) {
                MaterialLink callLink = new MaterialLink();
                callLink.getElement().appendChild(iCall);
                final ContactListItem[] accountItem = new ContactListItem[1];
                if (kanbanItem.getCrmAccountItem() != null) {
                    ContactListItem contactListItem = new ContactListItem();
                    contactListItem.setContactType(TYPE_ACCOUNT);
                    contactListItem.setPrimaryPhone(kanbanItem.getContactPrimaryPhone());
                    contactListItem.setOwner(kanbanItem.getCrmAccountItem().getName());
                    contactListItem.setCrmAccount(kanbanItem.getCrmAccountItem());
                    if (Utils.isNullOrEmpty(kanbanItem.getContactPrimaryPhone())) {
                        new KpiToolTip(liCall, wfmStrings.noPhone());
                        Span callLabel = new Span();
                        callLabel.getElement().appendChild(iCall);
                        liCall.add(callLabel);
                    } else {
                        accountItem[0] = contactListItem;
                    }
                }
                callLink.addClickHandler(clickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                        if (kanbanItem.getContactId() != null && !Utils.isNullOrEmpty(kanbanItem.getContactPrimaryPhone())) {
                            contactService.getContact(kanbanItem.getContactId(), false, new AsyncCallback<ContactListItem>() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(ContactListItem contactListItem) {
                                    crmService.getOpportunity(kanbanItem.getObjectId(), new AsyncCallback<OpportunityListItem>() {
                                        @Override
                                        public void onFailure(Throwable throwable) {

                                        }

                                        @Override
                                        public void onSuccess(OpportunityListItem opportunityListItem) {
                                            contactListItem.setOpportunity(opportunityListItem);
                                            Integer finalRelationId = relationId;
                                            new ActivityQuickAddForm(Appointment.CALL_LOG, kanbanItem.getContactPrimaryPhone(), contactListItem, RelationItem.newEventRelation(relationType, finalRelationId, relationName));
                                        }
                                    });

                                }
                            });
                        } else if (kanbanItem.getAccountId() != null && accountItem[0] != null) {
                            LoadingPanel.loading(true);
                            crmService.getOpportunity(kanbanItem.getObjectId(), new AsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem opportunityListItem) {
                                    LoadingPanel.loading(false);
                                    accountItem[0].setOpportunity(opportunityListItem);
                                    Integer finalRelationId = relationId;
                                    new ActivityQuickAddForm(Appointment.CALL_LOG, accountItem[0].getPrimaryPhone(), accountItem[0], RelationItem.newEventRelation(relationType, finalRelationId, relationName));
                                }
                            });
                        } else {
                            Integer finalRelationId = relationId;
                            new ActivityQuickAddForm(Appointment.CALL_LOG, kanbanItem.getContactPrimaryPhone(), null, RelationItem.newEventRelation(relationType, finalRelationId, relationName));
                        }
                    } else if (!Utils.isNullOrEmpty(kanbanItem.getContactPrimaryPhone())) {
                        Info.warn(crmStrings.youDontHavePermissiontoCall());
                    }
                });
                liCall.add(callLink);
            } else {
                new KpiToolTip(liCall, wfmStrings.noPhone());
                Span callLabel = new Span();
                callLabel.getElement().appendChild(iCall);
                liCall.add(callLabel);
            }
        } else {
            liCall.setVisible(false);
        }

        boolean isVisibleEmail = reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_EMAIL.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_EMAIL.getCode()).isSelected();
        Element iEmail = DOM.createElement("i");
        iEmail.setClassName("ficon--at");

        if (isVisibleEmail) {
            if (Utils.hasPermission(CRM_MESSAGE_CENTER) && !Utils.isNullOrEmpty(kanbanItem.getContactPrimaryEmail())) {
                MaterialLink emailLink = new MaterialLink();
                emailLink.getElement().appendChild(iEmail);
                emailLink.addClickHandler(clickEvent -> {
                    if (!kanbanItem.isContactEmailOptOut()) {
                        //new ComposeView(kanbanItem.getContactPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, kanbanItem.getContactId(), kanbanItem.getContact()));
                        SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + kanbanItem.getContactPrimaryEmail() + "/" + RelationItem.TYPE_OPPORTUNITY + "/" + kanbanItem.getObjectId() + "/" + kanbanItem.getOpportunityName());
                    } else {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.open();
                    }
                });
                liEmail.add(emailLink);
            } else {
                new KpiToolTip(liEmail, crmStrings.noEmail());
                Span emailLabel = new Span();
                emailLabel.getElement().appendChild(iEmail);
                liEmail.add(emailLabel);
            }
        } else {
            liEmail.setVisible(false);
        }

        if (!(isVisibleEmail || isVisiblePhone)) {
            contactsPanel.setVisible(false);
        }

        //Footer Of Card
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ACTION.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ACTION.getCode()).isSelected()) {
            initKanbanActions(kanbanItem);
        } else {
            actionPanel.setVisible(false);
        }
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NOTE.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_NOTE.getCode()).isSelected()) {
            initNotesIcon();
        }

        //Activities
        initKanbanActivities(kanbanItem);
        //Activities menu
        /*if("OVERDUE".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--overdue");
        } else if("AVAILABLE".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--available");
        } else if("NO_TASKS".equalsIgnoreCase(kanbanItem.getStatus())) {
            activitiesPanel.addStyleName("wg_canban__user-activities--notasks");
        }*/

        //Assignee
        if (reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ASSIGNE_NAME.getCode()) != null && reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ASSIGNE_NAME.getCode()).isSelected()) {
            String assigneValue = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.OPPORTUNITY_ASSIGNE_NAME.getCode()).getRelatedFieldCode(), kanbanItem.getAssignee());
            opportunityAssigneeName.setStyleName("wg_canban__entry-title");
            opportunityAssigneeName.setText(assigneValue);
        } else {
            opportunityAssigneeName.setVisible(false);
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
            notesKitPanel.addClickHandler(event -> {
                if (Window.getClientHeight() / (2) < Utils.getElementTop(notesLink.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                    notesKitPanel.addStyleName("dropdown-kit--arrow--below--reverse");
                } else {
                    notesKitPanel.removeStyleName("dropdown-kit--arrow--below--reverse");
                }
            });

            notesPanel.clear();
            notesPanel.add(notesKitPanel);
        }
    }

    private void initKanbanActions(final OpportunityListItem kanbanItem) {
        MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);
        menuContainer.setBelowOrigin(true);
        actionsLink.add(menuContainer);

        actionsLink.addClickHandler(clickEvent -> {
            menuContainer.clear();

            if (Window.getClientHeight() / 1.5 < Utils.getElementTop(actionsLink.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                actionsLink.getParent().addStyleName("dropdown-kit--arrow--below--reverse");
            } else {
                actionsLink.getParent().removeStyleName("dropdown-kit--arrow--below--reverse");
            }
            //Summary View
            MaterialLink summaryLink = new MaterialLink(wfmStrings.summaryView());
            summaryLink.ensureDebugId("opportunity-view-" + kanbanItem.getObjectId());
            summaryLink.addClickHandler(clickEvent1 -> {
                if (kanbanItem != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + kanbanItem.getObjectId() + "/" + kanbanItem.isConvertedLead() + "/" + kanbanItem.getContactId() + "/" + kanbanItem.getAccountId(), kanbanItem.getNumberData() != null ? kanbanItem.getNumberData().getNumberString() : kanbanItem.getOpportunityName(), kanbanItem.getOpportunityName());
                }
            });
            menuContainer.add(summaryLink);
            //Opportunity Edit
            if (Utils.hasPermission(PermissionConstants.CRM_EDIT_OPPORTUNITIES) && kanbanItem.isAllowEdit()) {
                MaterialLink editLink = new MaterialLink(wfmStrings.edit());
                editLink.ensureDebugId("opportunity-edit-" + kanbanItem.getObjectId());
                editLink.addClickHandler(clickEvent12 -> {
                    if (kanbanItem != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + kanbanItem.getObjectId(), kanbanItem.getNumberData() != null ? kanbanItem.getNumberData().getNumberString() : kanbanItem.getOpportunityName(), kanbanItem.getOpportunityName());
                    }
                });
                menuContainer.add(editLink);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_COPY_OPPORTUNITIES)) {
                MaterialLink copyLink = new MaterialLink(wfmStrings.copy());
                copyLink.ensureDebugId("opportunity-copy-" + kanbanItem.getObjectId());
                copyLink.addClickHandler(clickEvent12 -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + kanbanItem.getObjectId() + "/" + Constants.COPY));
                menuContainer.add(copyLink);
            }
            //Write Note
            if (Utils.hasPermission(CRM_ADD_OPPORTUNITY_NOTE, CRM_EDIT_OPPORTUNITIES)) {
                MaterialLink writeNoteLink = new MaterialLink(wfmStrings.addNote());
                writeNoteLink.ensureDebugId("write-note-" + kanbanItem.getObjectId());
                writeNoteLink.addClickHandler(clickEvent14 -> {
                    if (kanbanItem != null) {
                        new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_OPPORTUNITY, onNoteSavedCommand);
                    }
                });
                menuContainer.add(writeNoteLink);
            }
            //Delete
            if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_OPPORTUNITIES)) {
                MaterialLink deleteLink = new MaterialLink(wfmStrings.delete());
                deleteLink.ensureDebugId("delete-" + kanbanItem.getObjectId());
                deleteLink.addClickHandler(clickEvent13 -> {
                    if (kanbanItem != null) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ArrayList<Integer> objectIDs = new ArrayList<>();
                                objectIDs.add(kanbanItem.getObjectId());
                                CRMService.App.get().deleteOpportunity(objectIDs, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(ArrayList<Integer> result) {
                                        Info.show(Property.get(Constants.Opportunities, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.opportunity()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_DELETED, result, OpportunityMaterialCard.this);
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

    private MaterialLink getKanbanNotes(final OpportunityListItem kanbanItem) {
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
            addNote.addClickHandler(clickEvent12 -> {
                if (kanbanItem != null) {
                    new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_OPPORTUNITY, onNoteSavedCommand);
                }
            });
            Span addNoteLabel = new Span(wfmStrings.addNote());
            addNoteLabel.setStyleName("cp_btn-list-item-title");
            addNoteLabel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            addNoteLabel.addClickHandler(clickEvent1 -> new NotePopup(kanbanItem.getObjectId(), RelationItem.TYPE_OPPORTUNITY, onNoteSavedCommand));
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

    private void initKanbanActivities(final OpportunityListItem kanbanItem) {
        MaterialDropDown menuContainer = new MaterialDropDown(activitiesLink);
        menuContainer.setBelowOrigin(true);
        activitiesLink.add(menuContainer);

        activitiesLink.addClickHandler(clickEvent -> {
            menuContainer.clear();
            OpportunityActivitiesDropdown activitiesDropdown = new OpportunityActivitiesDropdown(kanbanItem);
            menuContainer.add(activitiesDropdown);

            crmService.getLastActivities(kanbanItem.getObjectId(), RelationItem.TYPE_OPPORTUNITY, new AbstractAsyncCallback<ListResult<Appointment>>() {
                @Override
                public void success(ListResult<Appointment> result) {
                    activitiesDropdown.setActivities(result);
                }
            });
        });

    }

    private String getRealValueByCode(String fieldCode, String defaultValue) {
        if (fieldCode == null) {
            return defaultValue;
        }
        String result = "";
        if (fieldCode.contains("string_value") || fieldCode.contains("date_value") || fieldCode.contains("double_value")) {
            return kanbanItem.getCustomFieldsMap() != null ? Utils.getKanbanItemValueFromObject(fieldCode, kanbanItem.getCustomFieldsMap().get(fieldCode)) : "";
        }
        if (KanbanItemSettingEnum.OPPORTUNITY_CLOSEDATE.getCode().equals(fieldCode)) {
            result = DateUtils.format(kanbanItem.getClosingDate());
        } else if (KanbanItemSettingEnum.OPPORTUNITY_AMOUT.getCode().equals(fieldCode)) {
            String currency = (!"".equals(kanbanItem.getCurrency()) && kanbanItem.getCurrency() != null) ? "  " + " (" + kanbanItem.getCurrency() + ")" : "";
            result = numberFormat.format(kanbanItem.getAmount() != null ? kanbanItem.getAmount() : 0d) + currency;
        } else if (KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode().equals(fieldCode)) {
            result = kanbanItem.getCrmAccountItem() != null && kanbanItem.getCrmAccountItem().getName() != null ? kanbanItem.getCrmAccountItem().getName() : "";
        } else if (KanbanItemSettingEnum.OPPORTUNITY_ASSIGNE_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getAssignee() != null ? kanbanItem.getAssignee() : "";
        } else if (KanbanItemSettingEnum.OPPORTUNITY_BACKUP_ASSIGNE_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getBackupAssignee() != null ? kanbanItem.getBackupAssignee() : "";
        } else if (KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getOpportunityName() != null ? kanbanItem.getOpportunityName() : "";
        } else if (KanbanItemSettingEnum.OPPORTUNITY_CONTACT.getCode().equals(fieldCode)) {
            result = kanbanItem.getContact() != null ? kanbanItem.getContact() : "";
        } else if (KanbanItemSettingEnum.OPPORTUNITY_CONTACT_PHONE.getCode().equals(fieldCode)) {
            result = kanbanItem.getContactPrimaryPhone() != null ? kanbanItem.getContactPrimaryPhone() : "";
        } else {
            result = defaultValue;
        }
        return result == null || result.isEmpty() ? "" : result;
    }
}
