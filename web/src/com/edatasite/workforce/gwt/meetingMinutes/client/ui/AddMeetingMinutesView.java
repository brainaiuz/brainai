package com.edatasite.workforce.gwt.meetingMinutes.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.ui.view.FileUploadPanel;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.*;

/**
 * User: developer
 * Date: 4/18/12
 * Time: 4:03 PM
 */
public class AddMeetingMinutesView extends CustomForm2 implements Colapse, Constants, MeetingMinutesWidgetKeys {

//    public static final MeetingMinutesString meetingMinutesString = MeetingMinutesString.App.get();

    
    private static final String MEETING_AGENDA_TOPIC = "MEETING_AGENDA_TOPIC";
    private static final String MEETING_AGENDA_NAME_LABEL = "MEETING_AGENDA_NAME_LABEL";
    private static final String MEETING_AGENDA_NAME = "MEETING_AGENDA_NAME";

    protected MeetingMinutesItem meetingMinutesItem;
    protected MultiTable meetingTopicsTable;
    private MultiSelectEmployeeLookUp absentLookUp;
    private VerticalPanel agendaPanel;
    private MultiSelectContactLookUp emailSelectBox;
    private  DataListBox emailTemplate;
    private HorizontalPanel attendeesHPanel;
    private KpiCheckBox sendNotif;
    private EmployeeLookUp calledBy;
    private DateTimePicker dateTime;
    private TextBox meetingName;
    private TextBox meetingNumber;
    private NumberData numberData;
    private TextBox location;
    private EmployeeLookUp preparedBy;
    private DateTimePicker nextMeetingDate;
    private TextArea2 purpose;
    private Numbering productNumberWidget;
    private NoteWidget noteWidget;
    private Integer objectID;
    private Integer basicMeetingMinutesID;
    private HashMap<Integer, MeetingAgentaTopicView> topicViews;
    private DataListBox type;
    private CRMLookUp project;
    private FileUploadPanel uploadPanel;
    protected FormHasCustomField customFieldUtil;

    private MeetingMinutesItem meetingMinutes = new MeetingMinutesItem();
    private final DateTimeFormat timeFormat = DateUtils.getTimeFormatInternal();
    DateTimeFormat timeFormatHour = DateUtils.getTimeFormatInternal();

    private final ArrayList<SelectItem> employeesList = new ArrayList<>();


    public AddMeetingMinutesView() {
        super("addmeetingMinutes", wfmStrings.add());
    }

    public AddMeetingMinutesView(Integer basicMeetingMinutesID) {
        super("addmeetingMinutes", wfmStrings.add());
        this.basicMeetingMinutesID = basicMeetingMinutesID;
    }

    public AddMeetingMinutesView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save(true));

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(false);
        Integer meetingMinutesID = (basicMeetingMinutesID != null ? basicMeetingMinutesID : objectID);
        MeetingMinutesService.App.get().getMeetingMinutesData(meetingMinutesID, new AbstractAsyncCallback<MeetingMinutesItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(MeetingMinutesItem item) {
                LoadingPanel.loading(false);
                meetingMinutesItem = item;
                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MEETING_MINUTES;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFields(ViewName.MeetingMInutesView, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                initialize();
            }
        });
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
        return null;
    }

    protected void fillFormWithData() {
        
        if (meetingMinutesItem.getEmployees() != null && meetingMinutesItem.getEmployees().size() > 0) {
            employeesList.addAll(meetingMinutesItem.getEmployees());
        }
        if (meetingMinutesItem.getEmailTemplates() != null && meetingMinutesItem.getEmailTemplates().length > 0) {
            emailTemplate.setItems(meetingMinutesItem.getEmailTemplates());
        }
        if (meetingMinutesItem.getEmailTemplate() != null) {
            emailTemplate.setSelected(meetingMinutesItem.getEmailTemplate());
        }
        meetingName.setText(meetingMinutesItem.getName());
        location.setText(meetingMinutesItem.getLocation());

        type.setItems(meetingMinutesItem.getTypes());
        type.setSelected(meetingMinutesItem.getType());

        SelectItem[] employees = meetingMinutesItem.getEmployees().toArray(new SelectItem[]{});
        if (meetingMinutesItem.getCalledBy() != null) {
            calledBy.setSelected(meetingMinutesItem.getCalledBy());
        }

        preparedBy.addItem(meetingMinutesItem.getPreparedBy() != null ? meetingMinutesItem.getPreparedBy() : new SelectItem(Utils.getUserID(), Utils.getUserFullName()));
        preparedBy.setSelected(meetingMinutesItem.getPreparedBy() != null ? meetingMinutesItem.getPreparedBy() : new SelectItem(Utils.getUserID(), Utils.getUserFullName()));
        if (meetingMinutesItem.getNextMeetingDate() != null) {
            nextMeetingDate.getStartDatePicker().setDate(meetingMinutesItem.getNextMeetingDate().getDate());
            nextMeetingDate.setStartTime(new StartEndTime(timeFormatHour.format(meetingMinutesItem.getNextMeetingDate().getDate())).time);
        }
        setSendNotificationCheckBoxValue(meetingMinutesItem.isSendNotifToAttendees());
        topicViews.clear();
        if (basicMeetingMinutesID != null) {
            meetingMinutesItem.setObjectID(null);
            meetingMinutesItem.setStartdate(null);
            meetingMinutesItem.setEnddate(null);
            meetingMinutesItem.setHistoryListItem(new HistoryListItem[]{});
        }

        if (meetingMinutesItem.getAgendaTopicItems() != null && meetingMinutesItem.getAgendaTopicItems().size() > 0) {
            meetingTopicsTable.removeAllRows();
            for (AgendaTopicItem topicItem : meetingMinutesItem.getAgendaTopicItems()) {
                final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(employeesList.toArray(new SelectItem[]{}), true);
                meetingTopicsTable.addWidgets(getMeetingTopicsTableWidgets(topicItem, agendaTopicView, true));
                agendaTopicView.setObjectID(topicItem.getObjectID());
                if (topicItem.getDiscussionItems() != null && !topicItem.getDiscussionItems().isEmpty()) {
                    ArrayList<AgendaTopicDiscussionItem> discussionItems = new ArrayList<>();
                    for (AgendaTopicDiscussionItem discussionItem : topicItem.getDiscussionItems()) {
                        discussionItem.setAssignedToItems(employeesList.toArray(new SelectItem[]{}));
                        discussionItem.setAssignedTo(discussionItem.getAssignedTo());
                        discussionItems.add(discussionItem);
                    }
                    agendaTopicView.setValues(discussionItems.toArray(new AgendaTopicDiscussionItem[]{}), false);
                }
            }
            generateAgendaTopicNumbering(true);
        } else {
            meetingTopicsTable.removeAllRows();
            final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(employeesList.toArray(new SelectItem[]{}), true);
            meetingTopicsTable.addWidgets(getMeetingTopicsTableWidgets(null, agendaTopicView, true));
            EditableGrid grid = agendaTopicView.getAgendaTopicTable().getGrid();
            //note widget
            noteWidget.drawOldNotes();
            generateAgendaTopicNumbering(true);
        }

        type.setItems(meetingMinutesItem.getTypes());
        ProjectItem projectItem = null;
        projectItem = meetingMinutesItem.getProjectItem();
        if(projectItem != null && projectItem.getId() != null && projectItem.getName() != null){
            project.setSelected(meetingMinutesItem.getProjectItem().getId(), meetingMinutesItem.getProjectItem().getName());
        }

        purpose.setText(meetingMinutesItem.getPurpose());
        if (meetingMinutesItem.getStartdate() != null) {
            dateTime.getStartDatePicker().setDate(meetingMinutesItem.getStartdate());
            dateTime.setStartTime(new StartEndTime(timeFormatHour.format(meetingMinutesItem.getStartdate())).time);
        }
        if (meetingMinutesItem.getEnddate() != null) {
            dateTime.getDueDatePicker().setDate(meetingMinutesItem.getEnddate());
            dateTime.setEndTime(new StartEndTime(timeFormatHour.format(meetingMinutesItem.getEnddate())).time);
        }
        if (meetingMinutesItem.getObjectID() != null) {
            meetingNumber = new TextBox();
            meetingNumber.setValue(meetingMinutesItem.getMeetingNumber());
            if (meetingMinutesItem.getMeetingNumber() != null && !meetingMinutesItem.getMeetingNumber().equals("")) {
                productNumberWidget.setNumberData(meetingMinutesItem.getNumberData());
            }
        } else {
            if (meetingMinutesItem.getObjectID() == null) {
                generateMeetingMinutesNumber();
            }
        }

        ArrayList<SelectItem> absentSelectItems = new ArrayList<>();
        ArrayList<SelectItem> nonCompanyAttendes= new ArrayList<>();
        for (int i = 0; i < meetingMinutesItem.getMeetingAbsentItem().size(); i++) {
            absentSelectItems.add(meetingMinutesItem.getMeetingAbsentItem().get(i).getAbsentEmployee());
        }
        if (meetingMinutesItem.getNonCompanyAttendees() != null && !meetingMinutesItem.getNonCompanyAttendees().isEmpty()) {
            for (String email : meetingMinutesItem.getNonCompanyAttendees().split(",")) {
                nonCompanyAttendes.add(new SelectItem(null, email));
            }
        }
        emailSelectBox.setSelectedItems(nonCompanyAttendes);
        absentLookUp.setSelectedItems(absentSelectItems);

        getCustomFieldUtil().fillCustomFieldsWithData(meetingMinutesItem.getCustomFieldItems());
    }

    protected void initialize() {
        String meeting_minutes_add_edit_view = "meeting_minutes_add_edit_view_";
        employeesList.add(new SelectItem(0, wfmStrings.pleaseSelect()));

        meetingName = new TextBox();
        meetingName.addStyleName(DEFAULT_WIDTH);
        meetingName.ensureDebugId(meeting_minutes_add_edit_view + "meeting_name");
        //numbering
        productNumberWidget = new Numbering();
        productNumberWidget.addStyleName(DEFAULT_WIDTH);
        productNumberWidget.ensureDebugId(meeting_minutes_add_edit_view + "number");
        //called by
        calledBy = new EmployeeLookUp(true, false, false);
        calledBy.addStyleName(DEFAULT_WIDTH);
        calledBy.ensureDebugId(meeting_minutes_add_edit_view + "called_by");
        //prepared by
        preparedBy = new EmployeeLookUp(true, false, false);
        preparedBy.ensureDebugId(meeting_minutes_add_edit_view + "prepared_by");
        //next meeting date
        nextMeetingDate = new DateTimePicker();
        nextMeetingDate.setAllDay(false);
        nextMeetingDate.getStartTime().setVisible(true);
        nextMeetingDate.getEndTime().setVisible(false);
        nextMeetingDate.getStartTime().setText(timeFormat.format(DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 1))));
        //date
        dateTime = new DateTimePicker();
        dateTime.setAllDay(false);
        dateTime.setStartDate(DateUtil.resetTime(new Date()));
        dateTime.setDueDate(new Date());
        dateTime.getStartTime().setVisible(true);
        dateTime.getEndTime().setVisible(true);
        dateTime.getStartTime().setText(timeFormat.format(DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 1))));
        dateTime.getEndTime().setText(timeFormat.format(DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 2))));
        dateTime.getStartTime().setWidth("100%");
        dateTime.getEndTime().setWidth("100%");
        dateTime.getStartTime().addStyleName("getStartTime");
        dateTime.getEndTime().addStyleName("getEndTime");

        dateTime.getStartDatePicker().addValueChangeHandler(event -> dateTime.getStartDatePicker().removeStyleName(ERROR_FORM_STYLE));
        dateTime.getStartDatePicker().ensureDebugId(meeting_minutes_add_edit_view + "start_date");
        dateTime.getDueDatePicker().ensureDebugId(meeting_minutes_add_edit_view + "end_date");
        dateTime.getStartTime().ensureDebugId(meeting_minutes_add_edit_view + "start_time");
        dateTime.getEndTime().ensureDebugId(meeting_minutes_add_edit_view + "end_time");
        //purpose
        purpose = new TextArea2(5000, wfmStrings.purpose());
        purpose.ensureDebugId(meeting_minutes_add_edit_view + "purpose");
        purpose.setHeight(100);
        //location
        location = new TextBox();
        location.addStyleName(DEFAULT_WIDTH);
        location.ensureDebugId(meeting_minutes_add_edit_view + "location");
        //meeting type
        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);
        type.ensureDebugId(meeting_minutes_add_edit_view + "type");
        //project
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.ensureDebugId("Task_project");
//        project.setAllowFirstItem(true);
        project.addStyleName(DEFAULT_WIDTH);

        //upload panel
        uploadPanel = new FileUploadPanel(F_MEETING_MINUTES, objectID);
        uploadPanel.ensureDebugId(meeting_minutes_add_edit_view + "upload_panel");
        //notes
        noteWidget = new NoteWidget(objectID, Constants.MEETING_MINUTES);
        noteWidget.ensureDebugId("meeting_minutes-notes");
        //attendees
        attendeesHPanel = new HorizontalPanel();

        VerticalPanel attendeesVPanel = new VerticalPanel();

        emailSelectBox = new MultiSelectContactLookUp(BY_BOTH);
        emailSelectBox.ensureDebugId(meeting_minutes_add_edit_view + "attendees");
        attendeesVPanel.add(emailSelectBox);

        emailTemplate = new DataListBox();
        emailTemplate.setWithoutNullLabel(true);

        AdvancedInputGroup emailTemplateGroup = new AdvancedInputGroup(emailTemplate);
        emailTemplateGroup.ensureDebugId("meeting_minutes-chooseEmailTemplate");
        emailTemplateGroup.setAppender("ficon--plus");
        emailTemplateGroup.appenderClickHandler(() -> Utils.openURL(GWT.getHostPageBaseURL() + UiSettings.getInstance().SETTINGS + "#emailSettingsHome|emailTemplateList/"));
        emailTemplate.setEnabled(false);
        addField(MEETING_M_EMAIL_TEMPLATES, emailTemplateGroup, getTitle(wfmStrings.template()));

        HTML html = new HTML();
        html.setHeight("5px");
        sendNotif = new KpiCheckBox(wfmStrings.sendNotification());
        setSendNotificationCheckBoxValue(false);
        sendNotif.addValueChangeHandler(booleanValueChangeEvent -> {
            onSendNotificationValueChange();
        });

        attendeesVPanel.add(sendNotif);
        attendeesVPanel.setSpacing(5);
        attendeesHPanel.add(attendeesVPanel);
        //absent
        absentLookUp = new MultiSelectEmployeeLookUp();
        absentLookUp.getFilterParametrs().setHRMS(true);
        absentLookUp.addStyleName(DEFAULT_WIDTH);
        absentLookUp.ensureDebugId(meeting_minutes_add_edit_view + "absent");
        //agenda Topic
        topicViews = new HashMap<>();
        agendaPanel = new VerticalPanel();
        meetingTopicsTable = new MultiTable(false, false, wfmStrings.areYouSureYouWantToDeleteThisAgendaTopic(), new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                final MeetingAgentaTopicView agendaTopicView = new MeetingAgentaTopicView(employeesList.toArray(new SelectItem[]{}), true);
                return getMeetingTopicsTableWidgets(null, agendaTopicView, true);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        meetingTopicsTable.setAddLabel(wfmStrings.addAgenda());
        meetingTopicsTable.setLinksLeftPanel();

        meetingTopicsTable.setOnLinesAdded(() -> generateAgendaTopicNumbering(true));
        agendaPanel.add(meetingTopicsTable);
        meetingTopicsTable.ensureDebugId(meeting_minutes_add_edit_view + "agenda_topic");

        initializeFields();
    }

    private void onSendNotificationValueChange() {
        emailTemplate.setEnabled(sendNotif.getValue());
    }

    private void setSendNotificationCheckBoxValue(Boolean value) {
        sendNotif.setValue(value != null && value);
        onSendNotificationValueChange();
    }

    protected void initializeFields() {
        //meeting number
        addTitleField(BASIC_INFORMATION, wfmStrings.basicDetails());
        addField(MEETING_M_NUMBER, meetingNumber != null ? meetingNumber : productNumberWidget, getTitle(wfmStrings.number()));
        //meeting title
        addField(MEETING_M_TITLE, meetingName, getTitle(wfmStrings.title(), true));
        //meeting called by
        addField(MEETING_M_CALLED_BY, calledBy, getTitle(wfmStrings.calledBy(), true));
        //meeting prepared by
        addField(MEETING_M_PREPARED_BY, preparedBy, getTitle(wfmStrings.preparedBy()));
        //next meeting date
        FlexTable nextMeetingPanel = new FlexTable();
        nextMeetingPanel.ensureDebugId("nextMeeting");
        nextMeetingPanel.setWidget(0, 0, nextMeetingDate.getStartDatePicker());
        nextMeetingPanel.setWidget(0, 1, nextMeetingDate.getStartTime());
        addField(MEETING_M_NEXT_DATE, nextMeetingPanel, getTitle(wfmStrings.nextMeeting()));
        //meeting start date
        FlexTable startTable = new FlexTable();
        startTable.setWidget(0, 0, dateTime.getStartDatePicker());
        startTable.setWidget(0, 1, dateTime.getStartTime());
        startTable.getFlexCellFormatter().setWidth(0, 0,"70%");
//        addField(MEETING_M_START_DATE, startTable, getTitle(wfmStrings.startDate(), true));
        //meeting end date
        FlexTable endTable = new FlexTable();
        endTable.setWidget(0, 0, dateTime.getDueDatePicker());
        endTable.setWidget(0, 1, dateTime.getEndTime());
        endTable.getFlexCellFormatter().setWidth(0, 0,"70%");
        addField(MEETING_M_MEETING_PERIOD, new InputGroup(startTable, endTable), getTitle(wfmStrings.meetingPeriod(), true));
        //meeting location
        addField(MEETING_M_LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        //meeting type
        addField(MEETING_M_TYPE, type, getTitle(wfmStrings.type()));
        //project
        addField(PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        //meeting attendees
        addField(MEETING_M_ATTENDEES, attendeesHPanel, getTitle(wfmStrings.attendees()));
        //meeting absent
        addField(MEETING_M_ABSENT, absentLookUp, getTitle(wfmStrings.absent()));
        //meeting purpose
        addField(MEETING_M_PURPOSE, purpose, null);
        //meeting attachments
        addTitleField(NOTES_AND_ATTACHMENTS, wfmStrings.meetingNotesAttachments());
        addField(MEETING_M_ATTACHMENTS, uploadPanel, null);
        //meeting notes
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addField(MEETING_M_NOTES, noteWidget, null);
        //meeting agenda topic
        addTitleField(AGENDA, wfmStrings.agendaTopic());
        addField(MEETING_M_AGENDA_PANEL, agendaPanel, null);
        //meeting email templates

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);

        show();
    }

    /**
     * Generate agenda topic numbering
     *
     * @param editable - editable
     */
    public void generateAgendaTopicNumbering(boolean editable) {
        int kNumber = 1;
        int size = meetingTopicsTable.size();
        for (Map<String, Widget> widgets : meetingTopicsTable.getWidgets()) {
            Label label = (Label) widgets.get(MEETING_AGENDA_NAME_LABEL);
            String nonEditableText = "";
            if (!editable) {
                label.addStyleName("customTitle");
                TextBox textBox = (TextBox) widgets.get(MEETING_AGENDA_NAME);
                if (textBox != null && textBox.getText() != null && !"".equals(textBox.getText())) {
                    nonEditableText += ": " + textBox.getText();
                }
            }
            if (size > 1) {
                label.setText(wfmStrings.agendaTopic() + " # " + kNumber + nonEditableText);
                kNumber++;
            } else {
                label.setText(wfmStrings.agendaTopic() + nonEditableText);
            }
        }
    }

    /**
     * Generate meeting topic table widgets
     *
     * @param topicItem       - topic item
     * @param agendaTopicView - agenda topic view
     * @param editable        - editable
     * @return - widgets map
     */
    public WidgetsMap getMeetingTopicsTableWidgets(AgendaTopicItem topicItem, final MeetingAgentaTopicView agendaTopicView, boolean editable) {
        WidgetsMap meetingTopicsMap = new WidgetsMap();
        final VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

        Label agendaTopicLabel = new Label();
        final TextBox agendaName = new TextBox();
        agendaName.setWidth("300px");
        if (topicItem != null) {
            agendaName.setText(topicItem.getName());
            agendaTopicView.setName(topicItem.getName());
        }
        agendaName.addKeyUpHandler(event -> {
            if (agendaName.getText() != null && !"".equals(agendaName.getText())) {
                agendaTopicView.setName(agendaName.getText());
            }
        });
        FlexTable agendaNamePanel = new FlexTable();
        agendaNamePanel.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        agendaNamePanel.getFlexCellFormatter().setWidth(0, 0, "130px");
        agendaNamePanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        agendaNamePanel.setStyleName("pairDate");
        agendaNamePanel.setWidget(0, 0, agendaTopicLabel);
        if (editable) {
            agendaNamePanel.setWidget(0, 1, agendaName);
        }

        if (agendaTopicView.getAgendaTopicTable().getGrid().getRowCount() <= 0) {
            ArrayList<AgendaTopicDiscussionItem> discussionItems = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                AgendaTopicDiscussionItem discussionItem = new AgendaTopicDiscussionItem();
                discussionItem.setAssignedToItems(employeesList.toArray(new SelectItem[]{}));
                discussionItems.add(discussionItem);
            }
            agendaTopicView.setValues(discussionItems.toArray(new AgendaTopicDiscussionItem[]{}), false);
        }

        mainContainer.add(agendaNamePanel);
        mainContainer.add(agendaTopicView);
        meetingTopicsMap.addWidgets(mainContainer);
        meetingTopicsMap.addWidgetToMap(MEETING_AGENDA_NAME_LABEL, agendaTopicLabel);
        meetingTopicsMap.addWidgetToMap(MEETING_AGENDA_NAME, agendaName);
        meetingTopicsMap.addWidgetToMap(MEETING_AGENDA_TOPIC, agendaTopicView);

        return meetingTopicsMap;
    }

    private void generateMeetingMinutesNumber() {
        MeetingMinutesService.App.get().generateMeetingMinutesNumber(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(NumberData result) {
                numberData = result;
                productNumberWidget.setNumberData(numberData);
            }
        });
    }

    private void save(final boolean saveAndClose) {
        int intNumber = 1;
        for (Map<String, Widget> widgets : meetingTopicsTable.getWidgets()) {
            if (widgets != null) {
                MeetingAgentaTopicView agendaTopicView = (MeetingAgentaTopicView) widgets.get(MEETING_AGENDA_TOPIC);
                if (agendaTopicView != null) {
                    topicViews.put(intNumber, agendaTopicView);
                }
                intNumber++;
            }
        }
        if (validate()) {
            enableButton(false);
            LoadingPanel.loading(true);
            meetingMinutes = setMeetingMinutesValues();
            MeetingMinutesService.App.get().saveMeetingMinutes(meetingMinutes, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Integer result) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    if (saveAndClose) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MEETING_MINUTES_SAVED, result, AddMeetingMinutesView.this);
                        closeTab();
                        if (objectID != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + objectID, meetingMinutes.getMeetingNumber(), meetingMinutes.getName());
                        }
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MEETING_MINUTES_SAVED, result, AddMeetingMinutesView.this);
                        closeTab();
                        SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|add/add");
                    }
                }
            });
        }
    }

    private MeetingMinutesItem setMeetingMinutesValues() {
        meetingMinutes.setObjectID(objectID);
        meetingMinutes.setName(meetingName.getText());
        meetingMinutes.setCalledBy(calledBy.getSelectedItem());
        meetingMinutes.setLocation(location.getText());
        meetingMinutes.setType(type.getSelectedItem());
        meetingMinutes.setProjectItem(new ProjectItem(project.getSelectedItemID(), project.getValue()));
        meetingMinutes.setPurpose(purpose.getText());
        meetingMinutes.setStartdate(dateTime.getStartDate());
        meetingMinutes.setEnddate(dateTime.getDueDate());
        meetingMinutes.setPreparedBy(preparedBy.getSelectedItem());
        meetingMinutes.setNextMeetingDate(nextMeetingDate.getStartDate());
        if (meetingNumber != null && meetingNumber.getText() != null) {
            meetingMinutes.setMeetingNumber(meetingNumber.getText());
        } else {
            meetingMinutes.setMeetingNumber(productNumberWidget.getNumberData(false).getNumberString());
        }
        meetingMinutes.setNumberData(productNumberWidget.getNumberData(false));
        ArrayList<MeetingAttendeesItem> meetingAbsentItems = new ArrayList<>();

        meetingMinutes.setSendNotifToAttendees(sendNotif.getValue());
        StringBuilder nonCompanyAttendees = new StringBuilder();
        for (SelectItem item : emailSelectBox.getSelectedItems()) {
            nonCompanyAttendees.append(item.getName()).append(",");
        }
        meetingMinutes.setNonCompanyAttendees(nonCompanyAttendees.toString());
        if (sendNotif.getValue()) {
            meetingMinutes.setEmailTemplate(emailTemplate.getSelectedItem(true));
        }
        //Absent larni set qiladi
        for (int i = 0; i < absentLookUp.getSelectedItems().size(); i++) {
            MeetingAttendeesItem meetingAbsentItem = new MeetingAttendeesItem();
            meetingAbsentItem.setObjectID(absentLookUp.getSelectedItems().get(i).getId());
            meetingAbsentItem.setAbsentEmployee(absentLookUp.getSelectedItems().get(i));
            meetingAbsentItem.setMeetingMinutesId(objectID);
            meetingAbsentItem.setAttendees(false);
            meetingAbsentItems.add(meetingAbsentItem);
        }
        meetingMinutes.setMeetingAbsentItem(meetingAbsentItems);

        meetingMinutes.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        ArrayList<AgendaTopicItem> topicItems = new ArrayList<>();
        if (topicViews != null && !topicViews.isEmpty()) {
            for (int topicN = 1; topicN <= topicViews.size(); topicN++) {
                MeetingAgentaTopicView topicView = topicViews.get(topicN);
                if (topicView != null) {
                    ArrayList<AgendaTopicDiscussionItem> topicdiscussionItems = new ArrayList<>();
                    AgendaTopicDiscussionItem[] data = topicView.getData();
                    if (data != null && data.length > 0) {
                        Collections.addAll(topicdiscussionItems, data);
                    }
                    AgendaTopicItem topicItem = new AgendaTopicItem(objectID != null ? topicView.getObjectID() : null, topicView.getName());
                    topicItem.setDiscussionItems(topicdiscussionItems);
                    topicItems.add(topicItem);
                }
            }
        }
        meetingMinutes.setAgendaTopicItems(topicItems);
        //Set Attachments
        if (uploadPanel.getAttachedFiles() != null) {
            meetingMinutes.setAttachments(uploadPanel.getAttachedFiles());
        }
        //Set Notes
        if (objectID == null) {
            for (int i = 0; i < noteWidget.getNewNotesToSave().size(); i++) {
                meetingMinutes.setHistoryListItem(noteWidget.getNewNotesToSave().toArray(new HistoryListItem[]{}));
            }
        }
        return meetingMinutes;
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(meetingName)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(calledBy)) {
            errors++;
        }
        if (!Validation.validateDate(dateTime.getStartDatePicker(), new HTML(wfmStrings.pleaseChooseValidDate()), true)) {
            errors++;
        }
        if (!Validation.validateDate(dateTime.getDueDatePicker(), new HTML(wfmStrings.pleaseChooseValidDate()), true)) {
            errors++;
        }
        if (!Validation.validateDateOrder(dateTime.getStartDate(), dateTime.getDueDate(), null, dateTime.isAllDay())) {
            errors++;
            dateTime.getStartDatePicker().addStyleName(ERROR_FORM_STYLE);
        } else {
            dateTime.getStartDatePicker().removeStyleName(ERROR_FORM_STYLE);
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (topicViews != null && topicViews.size() > 0) {
            for (int topicN = 1; topicN <= topicViews.size(); topicN++) {
                MeetingAgentaTopicView topicView = topicViews.get(topicN);
                if (topicView != null) {
                    int[] validRes = topicView.validation();
                    if (validRes[2] > 0) {
                        errors++;
                    }
                }
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }

        if (sendNotif.getValue() && emailSelectBox.getSelectedItems().isEmpty()) {
            Info.show(wfmStrings.pleaseSelect() + " " + wfmStrings.attendees(), Info.Type.WARNING);
            errors++;
        }
        if (sendNotif.getValue() && !Validation.validateListBoxRequired(emailTemplate)) {
            Info.show(wfmStrings.template(), Info.Type.WARNING);
            errors++;
        }

        return errors == 0;
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