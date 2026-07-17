package com.edatasite.workforce.gwt.meetingMinutes.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicDiscussionItem;
import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicItem;
import com.edatasite.workforce.gwt.core.client.rpc.MeetingAttendeesItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.FileUploadPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/30/12
 * Time: 12:23 PM
 */
public class ViewMeetingMinutesForm extends AddMeetingMinutesView implements FormHasCustomFieldInterface, Colapse, Constants {


    private VerticalPanel agendaPanel;
    private VerticalPanel absentPanel;
    private VerticalPanel attendeesPanel;
    private HTML title, meetingNumber, calledBy, startDate, endDate, location, meetingType, project, purpose, preparedBy, nextMeetingDate;
    private final Integer objectID;
    private NoteWidget noteWidget;
    private FileUploadPanel uploadPanel;
    private WfmButton2 edit;
    private String editPermission;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private MaterialLink pdfVersion;



    public ViewMeetingMinutesForm(Integer objectID) {
        super("summary", wfmStrings.summaryView(), objectID);
        this.objectID = objectID;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (this.customFieldUtil == null) {
            this.customFieldUtil = new FormHasCustomField();
        }
        return this.customFieldUtil;
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    @Override
    protected void addButtons() {

        //export_import button
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--top");

        MaterialLink showLink = new MaterialLink(wfmStrings.print());
        showLink.addStyleName(BTN_DEFAULT_OUTLINE);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2");
        showMenuContainer.setBelowOrigin(true);

        showLink.add(showMenuContainer);

        //pdf button
        MaterialLink pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");

        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);

        mdp.add(ViewMeetingMinutesForm.this::getPortraitLink);
        mdp.add(ViewMeetingMinutesForm.this::getLandscapeLink);

        wrapper.add(mdp);
        setPDFListener();
        pdfVersion.add(wrapper);
        showMenuContainer.add(pdfVersion);

        //excel button
        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {

            String URL = CommandConstants.COMMON_URL + "/meetingMinuteViewExcelHandler";
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setObjectId(meetingMinutesItem.getObjectID());
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(panel, URL, parametrs, "_blank");
        });

        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);
        addRightButton(showMenuBar);

        //edit button
        if (Utils.hasPermission(editPermission)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|edit/" + objectID, meetingMinutesItem.getMeetingNumber(), meetingMinutesItem.getName()));
        }
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        String URL = CommandConstants.PDF_URL + "/meetingMinutViewPDFHandler";
        RequestObject requestObject = new RequestObject(meetingMinutesItem.getObjectID());
        requestObject.setIS_LANDSCAPE(landscape);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, URL, parametrs, "_blank");
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected Widget onInitialize() {
        initializeViewForm();
        editPermission = Utils.isHRMS() ? PermissionConstants.EDIT_MEETING_MINUTES : PermissionConstants.EDIT_MEETING_MINUTES_WORKSPACE;
        return null;
    }

    private void initializeViewForm() {
        if (container != null) {
            container.setDescription(wfmStrings.summaryView());
        }
        super.onInitialize();
    }

    @Override
    protected void fillFormWithData() {
        if (meetingMinutesItem.getPreparedBy() != null && Utils.getUserID() != meetingMinutesItem.getPreparedBy().getId() && !Utils.hasPermission(editPermission)) {
            edit.setVisible(false);
        }
        //title
        title.setHTML(meetingMinutesItem.getName() != null ? meetingMinutesItem.getName() : "");
        //numbering
        meetingNumber.setHTML(meetingMinutesItem.getMeetingNumber() != null ? meetingMinutesItem.getMeetingNumber() : "");
        //called by
        calledBy.setHTML(meetingMinutesItem.getCalledBy() != null ? meetingMinutesItem.getCalledBy().getName() : "");
        //start date
        startDate.setHTML(DateUtils.formatInternal(meetingMinutesItem.getStartdate()));
        //end date
        endDate.setHTML(DateUtils.formatInternal(meetingMinutesItem.getEnddate()));
        //location
        location.setHTML(meetingMinutesItem.getLocation() != null ? meetingMinutesItem.getLocation() : "");
        //type
        meetingType.setHTML(meetingMinutesItem.getType() != null ? meetingMinutesItem.getType().getName() : "");
        //projectName
        String projectName = "";
        ProjectItem projectItem = null;
        projectItem = meetingMinutesItem.getProjectItem();
        if(projectItem != null && projectItem.getName() != null){
            projectName = projectItem.getName();
        }
        project.setHTML(projectName);
        //purpose
        String purposeText = meetingMinutesItem.getPurpose() != null ? meetingMinutesItem.getPurpose().replace("\n", "<br/>") : "";
        purpose.setHTML(purposeText);
        //prepared by
        preparedBy.setHTML(meetingMinutesItem.getPreparedBy() != null ? meetingMinutesItem.getPreparedBy().getName() : "");
        //next meeting date
        if (meetingMinutesItem.getNextMeetingDate() != null) {
            nextMeetingDate.setHTML(meetingMinutesItem.getNextMeetingDate() != null ? DateUtils.formatInternal(meetingMinutesItem.getNextMeetingDate().getDate()) : "");
        }
        //agenda panel
        if (meetingMinutesItem.getAgendaTopicItems() != null && meetingMinutesItem.getAgendaTopicItems().size() > 0) {
            meetingTopicsTable.removeAllRows();
            for (AgendaTopicItem topicItem : meetingMinutesItem.getAgendaTopicItems()) {
                if (topicItem.getDiscussionItems() != null && !topicItem.getDiscussionItems().isEmpty()) {
                    final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(null, false);
                    meetingTopicsTable.addWidgets(getMeetingTopicsTableWidgets(topicItem, agendaTopicView, false));
                    topicItem.setObjectID(agendaTopicView.getObjectID());
                    agendaTopicView.setValues(topicItem.getDiscussionItems().toArray(new AgendaTopicDiscussionItem[]{}), true);
                } else {
                    initializeDefaultAgend(topicItem);
                }
            }
            generateAgendaTopicNumbering(false);
        } else {
            meetingTopicsTable.removeAllRows();
            initializeDefaultAgend(null);
        }
        //recipient
        if (meetingMinutesItem.getNonCompanyAttendees() != null && !meetingMinutesItem.getNonCompanyAttendees().isEmpty()) {
            for (String recipient : meetingMinutesItem.getNonCompanyAttendees().split(",")) {
                attendeesPanel.add(new HTML(recipient));
            }
        } else {
            showHideSendNotificationItems(false);
        }
        //absent
        List<MeetingAttendeesItem> meetingAbsentsItem = meetingMinutesItem.getMeetingAbsentItem();
        if (meetingAbsentsItem != null && meetingAbsentsItem.size() > 0) {

            for (MeetingAttendeesItem aMeetingAbsentsItem : meetingAbsentsItem) {
                if (aMeetingAbsentsItem != null && aMeetingAbsentsItem.getAbsentEmployee() != null) {
                    absentPanel.add(new HTML(aMeetingAbsentsItem.getAbsentEmployee().getName()));
                }
            }
        }
        getCustomFieldUtil().fillCustomFieldsWithData(meetingMinutesItem.getCustomFieldItems(), true);

    }

    private void initializeDefaultAgend(AgendaTopicItem topicItem) {
        ArrayList<AgendaTopicDiscussionItem> discussionItems = new ArrayList<>();
        final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(null, false);
        meetingTopicsTable.addWidgets(getMeetingTopicsTableWidgets(topicItem, agendaTopicView, false));
        for (int i = 0; i < 3; i++) {
            AgendaTopicDiscussionItem discussionItem = new AgendaTopicDiscussionItem();
            discussionItem.setDiscussionPoints(" ");
            discussionItem.setActionPoints(" ");
            discussionItems.add(discussionItem);
        }
        agendaTopicView.setValues(discussionItems.toArray(new AgendaTopicDiscussionItem[]{}), false);
        for (int i = 0; i < 3; i++) {
            agendaTopicView.getAgendaTopicTable().getGrid().getWidget(i, 0).setHeight("15px");
        }
        generateAgendaTopicNumbering(false);
    }

    @Override
    protected void initialize() {
        String meeting_minutes_summary_view = "meeting_minutes_summary_view_";
        //title
        title = new HTML();
        title.addStyleName(DEFAULT_WIDTH);
        title.ensureDebugId(meeting_minutes_summary_view + "title");
        //numbering
        meetingNumber = new HTML();
        meetingNumber.addStyleName(DEFAULT_WIDTH);
        meetingNumber.ensureDebugId(meeting_minutes_summary_view + "number");
        //called by
        calledBy = new HTML();
        calledBy.addStyleName(DEFAULT_WIDTH);
        calledBy.ensureDebugId(meeting_minutes_summary_view + "called_by");
        //start date
        startDate = new HTML();
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.ensureDebugId(meeting_minutes_summary_view + "start_date");
        //end date
        endDate = new HTML();
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.ensureDebugId(meeting_minutes_summary_view + "end_date");
        //location
        location = new HTML();
        location.addStyleName(DEFAULT_WIDTH);
        location.ensureDebugId(meeting_minutes_summary_view + "location");
        //type
        meetingType = new HTML();
        meetingType.addStyleName(DEFAULT_WIDTH);
        meetingType.ensureDebugId(meeting_minutes_summary_view + "type");
        //project name
        project = new HTML();
        project.addStyleName(DEFAULT_WIDTH);
        project.ensureDebugId(meeting_minutes_summary_view + "project");
        //purpose
        purpose = new HTML();
        purpose.setWidth("300px");
        purpose.ensureDebugId(meeting_minutes_summary_view + "purpose");
        purpose.setHeight("100px");
        //prepared by
        preparedBy = new HTML();
        preparedBy.addStyleName(DEFAULT_WIDTH);
        preparedBy.ensureDebugId(meeting_minutes_summary_view + "prepared_by");
        //next meetin date
        nextMeetingDate = new HTML();
        nextMeetingDate.addStyleName(DEFAULT_WIDTH);
        nextMeetingDate.ensureDebugId(meeting_minutes_summary_view + "next_meeting_date");
        //absent
        absentPanel = new VerticalPanel();
        absentPanel.addStyleName(DEFAULT_WIDTH);
        absentPanel.ensureDebugId(meeting_minutes_summary_view + "absent");

        //pdfPanel = new VerticalPanel();
        //pdfPanel.addStyleName(DEFAULT_WIDTH);
        //pdfPanel.ensureDebugId(meeting_minutes_summary_view + "pdf");

        //attendees
        attendeesPanel = new VerticalPanel();
        attendeesPanel.addStyleName(DEFAULT_WIDTH);
        attendeesPanel.ensureDebugId(meeting_minutes_summary_view + "attendees");
        //agenda topic
        agendaPanel = new VerticalPanel();
        meetingTopicsTable = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(null, false);
                return getMeetingTopicsTableWidgets(null, agendaTopicView, false);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });
        meetingTopicsTable.addStyleName("meeting-minutes-summary-view-table");
        meetingTopicsTable.removeAddButton();
        meetingTopicsTable.setViewMode(true);
        agendaPanel.add(meetingTopicsTable);
        meetingTopicsTable.ensureDebugId(meeting_minutes_summary_view + "agenda_topic");
        //notes
        noteWidget = new NoteWidget(objectID, Constants.MEETING_MINUTES);
        noteWidget.ensureDebugId(meeting_minutes_summary_view + "notes");
        //attachments
        uploadPanel = new FileUploadPanel(F_MEETING_MINUTES, objectID, true, true, null);
        uploadPanel.ensureDebugId(meeting_minutes_summary_view + "attachments");

        initializeFields();
    }

    @Override
    protected void initializeFields() {
        addTitleField(DETAILS, wfmStrings.details());
        addTitleField(ATTACHMENTS, wfmStrings.attachments());
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addTitleField(AGENDA, wfmStrings.agendaTopic());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        //meeting title
        addField(MEETING_M_TITLE, title, getTitle(wfmStrings.title()));
        //meeting number
        addField(MEETING_M_NUMBER, meetingNumber, getTitle(wfmStrings.number()));
        //meeting called by
        addField(MEETING_M_CALLED_BY, calledBy, getTitle(wfmStrings.calledBy()));
        //meeting prepared by
        addField(MEETING_M_PREPARED_BY, preparedBy, getTitle(wfmStrings.preparedBy()));
        //next meeting date
        addField(MEETING_M_NEXT_DATE, nextMeetingDate, getTitle(wfmStrings.nextstr() + " " + wfmStrings.meetingMinutes()));
        //meeting start date
//        addField(MEETING_M_START_DATE, startDate, getTitle(wfmStrings.startDate()));
        //meeting end date
        addField(MEETING_M_MEETING_PERIOD, new InputGroup(endDate, startDate), getTitle(wfmStrings.meetingPeriod()));
        //meeting location
        addField(MEETING_M_LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        //meeting type
        addField(MEETING_M_TYPE, meetingType, getTitle(wfmStrings.type()));
        //project name
        addField(PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        //meeting attendees
        addField(MEETING_M_ATTENDEES, attendeesPanel, getTitle(wfmStrings.attendees()));
        //meeting absent
        addField(MEETING_M_ABSENT, absentPanel, getTitle(wfmStrings.absent()));

        //meeting purpose
        addField(MEETING_M_PURPOSE, purpose, getTitle(wfmStrings.purpose()));
        //meeting attachments
        addField(MEETING_M_ATTACHMENTS, uploadPanel, getTitle(wfmStrings.attachments()));
        //meeting notes
        addField(MEETING_M_NOTES, noteWidget, getTitle(wfmStrings.notes()));
        //meeting agenda topic
        addField(MEETING_M_AGENDA_PANEL, agendaPanel);

        getCustomFieldUtil().drawCustomFields(this, objectID, true);

        show();
    }

    private void showHideSendNotificationItems(boolean visible) {
        Element recipientsField = DOM.getElementById("recipientsField");
        if (recipientsField != null) {
            recipientsField.getStyle().setDisplay(visible ? Style.Display.BLOCK : Style.Display.NONE);
        }
    }

    @Override
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
}
