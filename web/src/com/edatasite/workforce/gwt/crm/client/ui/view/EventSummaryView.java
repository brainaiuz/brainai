package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;


/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 11:48:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSummaryView extends CustomForm2 implements HasLinksInterface, Colapse {

    private final Integer objectId;
    private boolean isCall;
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private EventItem item;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public interface Images extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/crm/client/bundles/icons/edit.gif")
        ImageResource edit();

        @Source("com/edatasite/workforce/gwt/core/client/bundles/images/remove-icon-small.gif")
        ImageResource remove();
    }

    public EventSummaryView(Integer id) {
        super("summary", Property.get(Constants.EVENT_LIST, wfmStrings.summaryView(), wfmStrings.event()));
        this.objectId = id;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, EventSummaryView.this, (sender, args) -> getDataToFillFields());
    }

    public EventSummaryView(Integer id, boolean isCall) {
        super("summary", Property.get(Constants.EVENT_LIST, wfmStrings.summaryView(), wfmStrings.event()));
        this.objectId = id;
        this.isCall = isCall;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, EventSummaryView.this, (sender, args) -> getDataToFillFields());
    }

    public String getDescription() {
        return Property.get(Constants.EVENT_LIST, wfmStrings.summaryView(), wfmStrings.event());
    }

    public String getIconStyle() {
        return "event event-list";
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(isCall ? ViewName.LogACall : ViewName.Activity, isCall ? LayoutRPC.LOGACALL_FORM_VIEW : LayoutRPC.ACTIVITY_VIEW_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                initialize();
                getDataToFillFields();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, EventSummaryView.this, (sender, args) -> getDataToFillFields());
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

    private HTML eventSubject, when, createdBy, createdDate, updatedBy, updatedDate, callType, callDetails, callDuration, invitationResponse;
    private HTML description;
    private VerticalPanel records;
    protected GeneralFileUpload attachment = null;
    private SharedWith sharedWith;
    private FooterInformer link;

    private void initialize() {
        eventSubject = initHTML();
        callType = initHTML();
        callDetails = initHTML();
        callDuration = initHTML();
        when = initHTML();
        records = new VerticalPanel();

        description = new HTML();

        createdBy = initHTML();
        createdDate = initHTML();
        updatedBy = initHTML();
        updatedDate = initHTML();
        attachment = new GeneralFileUpload(Constants.F_EVENT, objectId, objectId);
        attachment.setWidth("100%");
        sharedWith = new SharedWith();

        invitationResponse = initHTML();
        addFieldsToForm();

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);
    }

    @Override
    protected String getFormID() {
        return isCall ? LayoutRPC.LOGACALL_FORM_VIEW : LayoutRPC.ACTIVITY_VIEW_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void getDataToFillFields() {
        CRMService.App.get().getEvent(objectId, new AbstractAsyncCallback<EventItem>() {
            public void failure(Throwable throwable) {

            }

            public void success(EventItem eventItem) {
                item = eventItem;
//                addField(CustomFormConstants.LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), coreStrings.links(), true);
                fillFields();
                if (item.getTwilioCallSID() != null) {
                    TwilioService.App.get().getRecordUrls(item.getTwilioCallSID(), new AsyncCallback<ArrayList<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(ArrayList<String> result) {
                            records.clear();
                            if (result != null && result.size() > 0) {
                                for (String uri : result) {
                                    records.add(getRecordAsAudioable("https://api.twilio.com" + uri));
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private Widget getRecordAsAudioable(String s) {
        SimpleLink link = new SimpleLink("Call");
        link.addClickHandler(event -> Utils.openURL(s));
        return link;
    }

    private void fillFields() {
        eventSubject.setHTML(item.getSubject());
        callType.setHTML(item.isCallLog() ? (item.isInboundCall() ? wfmStrings.inbound() : item.isOutboundCall() ? wfmStrings.outbound() : wfmStrings.missed()) : wfmStrings.notAvailable());
        callDetails.setHTML(item.isCallLog() ? (item.isCurrentCall() ? wfmStrings.current() : item.isComplatedCall() ? wfmStrings.completed() : wfmStrings.schedule()) : wfmStrings.notAvailable());
        when.setHTML(DateUtils.formatInternal(item.getStartDate()) + Utils.getHijriDate(item.getStartDate())
                + " - " + DateUtils.formatInternal(item.getEndDate()) + Utils.getHijriDate(item.getEndDate()));
        if (item.isAllDay()) {// added this if cause it was showing time even it was all day selected!
            when.setHTML(DateUtils.format(item.getStartDate()) + Utils.getHijriDate(item.getStartDate())
                    + " - " + DateUtils.format(item.getEndDate()) + Utils.getHijriDate(item.getEndDate()));
        }
        callDuration.setHTML(item.getCallDuration() > 0 ? (item.getCallDuration() / 60) + " : " + (item.getCallDuration() % 60) + "" : "");
        description.setHTML(item.getDescription());
        createdBy.setHTML(item.getCreatedBy());
        createdDate.setHTML(DateUtils.formatInternal(item.getCreatedDate()) + Utils.getHijriDate(item.getCreatedDate()));
        updatedBy.setHTML(item.getLastModifiedBy());
        updatedDate.setHTML(DateUtils.formatInternal(item.getLastModifiedDate()) + Utils.getHijriDate(item.getLastModifiedDate()));
        sharedWith.refresher();
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
        invitationResponse.setHTML(item.getInvitationResponse());

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);

    }

    private void addFieldsToForm() {
        addTitleField(EVENT_INFORMATION, isCall ? wfmStrings.logaCallInformation() : Property.get(Constants.EVENT_LIST, wfmStrings.basicDetails(), wfmStrings.event()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(SUBJECT, eventSubject, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject()));
        } else {
            addField(SUBJECT, eventSubject, getTitle(wfmStrings.subject()));
        }

        if (isCall) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_TYPE) != null) {
                addField(CALL_TYPE, callType, getTitle(formPropertyMap.get(CustomFormConstants.CALL_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_TYPE).getTitle() : wfmStrings.callType()));
            } else {
                addField(CALL_TYPE, callType, getTitle(wfmStrings.callType()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_DETAILS) != null) {
                addField(CALL_DETAILS, callDetails, getTitle(formPropertyMap.get(CustomFormConstants.CALL_DETAILS).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_DETAILS).getTitle() : wfmStrings.callDetails()));
            } else {
                addField(CALL_DETAILS, callDetails, getTitle(wfmStrings.callDetails()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_DURATION) != null) {
                addField(CALL_DURATION, callDuration, getTitle(formPropertyMap.get(CustomFormConstants.CALL_DURATION).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_DURATION).getTitle() : wfmStrings.callDuration()));
            } else {
                addField(CALL_DURATION, callDuration, getTitle(wfmStrings.callDuration()));
            }

            addField(CALL_RECORDS, records, getTitle(crmStrings.records()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WHEN) != null) {
            addField(WHEN, when, getTitle(formPropertyMap.get(CustomFormConstants.WHEN).isChanged() ? formPropertyMap.get(CustomFormConstants.WHEN).getTitle() : wfmStrings.when()));
        } else {
            addField(WHEN, when, getTitle(wfmStrings.when()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INVITATION_RESPONSE) != null) {
            addField(INVITATION_RESPONSE, invitationResponse, getTitle(formPropertyMap.get(CustomFormConstants.INVITATION_RESPONSE).isChanged() ? formPropertyMap.get(CustomFormConstants.INVITATION_RESPONSE).getTitle() : wfmStrings.invitationResponse()));
        } else {
            addField(INVITATION_RESPONSE, invitationResponse, getTitle(wfmStrings.invitationResponse()));
        }

        addField(CREATED_BY, createdBy, getTitle(wfmStrings.createdBy()));
        addField(CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(UPDATED_BY, updatedBy, getTitle(wfmStrings.modifiedBy()));
        addField(UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));
        addField(SHARED_WITH, sharedWith, null, true);
//        if(Utils.hasPermission(PermissionConstants.CRM_ACTIVITY_SEE_ATTACHMENTS)) {
        addField(ATTACHMENTS, attachment, null, true);
//        }
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
    }


    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

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

        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_ACTIVITY)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.ensureDebugId("delete");
            delete.addClickHandler(event -> {
                if (item != null) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            ArrayList<Integer> objectIDs = new ArrayList<>();
                            objectIDs.add(item.getObjectID());
                            CRMService.App.get().deleteEvent(objectIDs, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                @Override
                                public void failure(Throwable caught) {
                                }

                                @Override
                                public void success(ArrayList<Integer> result) {
                                    Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.event()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, result, EventSummaryView.this);
                                    closeTab();
                                }
                            });
                        }
                    });
                    messageBox.open();
                }
            });
            optionsButton.addItem(delete);
        }
        addRightButton(optionsButton);

        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_ACTIVITY)) {
            WfmButton2 edit = new WfmButton2(wfmStrings.edit(), BTN_DEFAULT_OUTLINE);
            edit.ensureDebugId("edit");
            edit.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + item.getObjectID() + "/" + item.getActivityType(), item.getSubject()));
            Div editWrapper = new Div();
            editWrapper.add(edit);
            footer.addToRightSide(editWrapper);
        }

    }

    private class SharedWith extends AbstractDataGrid<PositionsSelectItem> {

        private SharedWith() {
            super();
            initialize();
        }

        @Override
        protected void addColums() {
            //To change body of implemented methods use File | Settings | File Templates.
            Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem item) {
                    return refactor(item.getName());
                }
            };
            addColumn(employee, wfmStrings.employee());
            setColumnWidth(employee, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            //position
            Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem item) {
                    return refactor(item.getPositionName());
                }
            };
            addColumn(position, wfmStrings.position());
            setColumnWidth(position, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            //department
            Column<PositionsSelectItem, String> department = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem item) {
                    return refactor(item.getDepartmentName());
                }
            };
            addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
            setColumnWidth(department, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        }

        @Override
        public void refresher() {
            if (item != null) {
                supplyProvider(item.getSharedEmployees().toArray(new PositionsSelectItem[]{}));
                reDrawItems();
            }
            LoadingPanel.loading(false);
        }

    }

    private String refactor(String s) {
        if (!Utils.isNullOrEmpty(s)) {
            return s;
        }
        return "";
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

    HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(EventSummaryView.this) {
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
                    return RelationItem.TYPE_EVENT;
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

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }
}
