package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.OvalPanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jan 29, 2010
 * Time: 5:30:07 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class is specified for showing view for new appointment and for existing
 * appointment. It checks the title of the appointment and iether shows add new
 * view or existing view depending on the title's value.
 */
public class ShortAppointmentView {

    private GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private SaveAppointmentHandler saveHandler;
    private DeleteAppointmentHandler deleteHandler;

    private Appointment appointment;
    private FlexTable details;
    private PopupPanel popup;
    private Label venueTime;
    private DatePicker startDate;
    private DatePicker endDate;
    private KpiTimePicker from;
    private KpiTimePicker to;

    public ShortAppointmentView(Appointment appointment) {
        this.appointment = appointment;

        initialize();

        if (appointment.getSubject() == null) {
            drawNewAppointmentView();
        } else {
            popup.setAutoHideEnabled(true);
            popup.setModal(true);
            drawExistingAppointmentView();
        }
    }

    /**
     * ***************************************** General parts of the views *********************************
     */

    private void initialize() {
        popup = new DecoratedPopupPanel(true, true);
        popup.setStyleName("cal-popup event-det-popup");
        venueTime = new Label();

        details = new FlexTable();
        details.setBorderWidth(0);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_RELATION_OPENED, popup, (sender, args) -> closePopup());
    }

    private void drawOvalPanel(Panel panel) {
        OvalPanel ovalPanel = new OvalPanel();
        ovalPanel.add(panel);
        ovalPanel.addCloseButtonClickHandler(event -> popup.hide());

        popup.setWidget(ovalPanel);

        setAppointmentAndShow(appointment);
    }

    public void setAppointmentAndShow(Appointment newAppointment) {
        this.appointment = newAppointment;

        DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEE, MMMM dd");
        DateTimeFormat timeFormat = DateUtils.getFormatInternal().getShortTimeFormat();


        String startTime = timeFormat.format(appointment.getStartDate()).toLowerCase();
        String endTime = "";
        if (appointment.getEndDate() != null) {
            endTime = timeFormat.format(appointment.getEndDate()).toLowerCase();
        }

        if (appointment.isMultiDay()) {
            String start = DateUtils.formatInternal(appointment.getStartDate());// + "," + startTime;
            String end = DateUtils.formatInternal(appointment.getEndDate());// + "," + endTime;

            if (!Appointment.BLUE.equals(appointment.getStyle()) && !(appointment.isTask() || Appointment.GREEN.equals(appointment.getStyle())) && !Appointment.RED.equals(appointment.getStyle())) {
                start = DateUtils.format(appointment.getStartDate());
                end = DateUtils.format(appointment.getEndDate());
            }
            if (appointment.isAllDay()) {
                venueTime.setText(DateUtils.format(appointment.getStartDate()) + Utils.getHijriDate(appointment.getStartDate())
                        + " - " + DateUtils.format(appointment.getEndDate()) + Utils.getHijriDate(appointment.getEndDate()));
            } else {
                venueTime.setText(start + Utils.getHijriDate(appointment.getStartDate()) + " - " + end + Utils.getHijriDate(appointment.getEndDate()));
            }
        } else {
            venueTime.setText(DateUtils.format(appointment.getStartDate()) + Utils.getHijriDate(appointment.getStartDate()));
            if (!appointment.isAllDay() && appointment.getObjectID() != null) {
                venueTime.setText(DateUtils.format(appointment.getStartDate()) + Utils.getHijriDate(appointment.getStartDate()) + ", " + startTime + " - " + endTime);
            }
        }

        popup.show();

        //For the view of existing appointment whatText doesn't exist, so we should check it to null.
        if (whatText != null) {
            DeferredCommand.addCommand(() -> {
                whatText.setText(appointment.getSubject() != null ? appointment.getSubject() : "");
                whatText.setFocus(true);
            });
        }
    }

    public void closePopup() {
        popup.hide();
    }

    public void onSaveOrUpdateAppointment(SaveAppointmentHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    public void onDeleteAppointment(DeleteAppointmentHandler deleteHandler) {
        this.deleteHandler = deleteHandler;
    }

    public void setPosition(int left, int top) {
        popup.setPopupPosition(left, top);
    }

    public void setPopupPositionAndShow(PopupPanel.PositionCallback callback) {
        popup.setPopupPositionAndShow(callback);
    }

    public void onClosePopup(CloseHandler<PopupPanel> closeHandler) {
        popup.addCloseHandler(closeHandler);
    }

    public void center() {
        popup.center();
    }

    /**
     * ************************************* Below is the drawing of the existing appointment view ******************
     */

    private final String eventDetailsStyle = "event-det-table__footer";

    private void drawExistingAppointmentView() {
        details.addStyleName("event-det-table file--drawExixtingAppointmentView");
        details.getColumnFormatter().setWidth(0, "");
        details.getColumnFormatter().setWidth(1, "");
        details.setCellSpacing(0);
        details.setCellPadding(0);

        FlexTable table = new FlexTable();
        table.getElement().getStyle().setTableLayout(Style.TableLayout.FIXED);
        table.setWidth("400px");

        details.setHTML(0, 0, "<span>" + appointment.getSubject() + "</span>");
        details.getFlexCellFormatter().setColSpan(0, 0, 2);
        details.getFlexCellFormatter().setStyleName(0, 0, "event-det-table__heading");

        details.setText(1, 0, wfmStrings.description() + ":");
        String description = appointment.getDescription();
        if (description == null || description.equals("")) {
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                description = "<i>" + wfmStrings.thereIsNoDescriptionForThisTask() + "</i>";
            } else {
                description = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.thereIsNoDescriptionForThisEvent(), wfmStrings.event()) + "</i>";
            }
        }
        if (description.length() > 60) {
            Div descriptionScrollBoxPanel = new Div("scroll-box");
            descriptionScrollBoxPanel.getElement().getStyle().setProperty("wordBreak", "break-word");
            descriptionScrollBoxPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
            descriptionScrollBoxPanel.setHeight("100px");
            descriptionScrollBoxPanel.add(new HTML(description));
            details.setWidget(1, 1, descriptionScrollBoxPanel);
        } else {
            details.setHTML(1, 1, description);
        }
        if (Appointment.ORANGE.equals(appointment.getStyle()) || Appointment.PURPLE.equals(appointment.getStyle()) || Appointment.RED.equals(appointment.getStyle()) || Appointment.YELLOW.equals(appointment.getStyle())) {
            details.setText(2, 0, wfmStrings.period() + ":");
            details.setWidget(2, 1, venueTime);
        } else {
            details.setText(2, 0, wfmStrings.when() + ":");
            details.setWidget(2, 1, venueTime);
        }

        if (!Appointment.ORANGE.equals(appointment.getStyle()) && !Appointment.PURPLE.equals(appointment.getStyle()) &&
                !Appointment.DARK_PURPLE.equals(appointment.getStyle()) && !Appointment.RED.equals(appointment.getStyle())) {
            String location = appointment.getLocation();
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {  //condition for task
                details.setText(3, 0, wfmStrings.project() + ":");
                if (location == null || location.equals("")) {
                    location = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.noLocationAppointedToThisEvent(), wfmStrings.event()) + "</i>";
                }
            } else {
                details.setText(3, 0, wfmStrings.where() + ":");
                if (location == null || location.equals("")) {
                    location = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.noLocationAppointedToThisEvent(), wfmStrings.event()) + "</i>";
                }
            }
            Div locationPanel = new Div();
            locationPanel.getElement().getStyle().setProperty("wordBreak", "break-word");
            locationPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
            locationPanel.add(new HTML(location));
            details.setWidget(3, 1, locationPanel);
        }
        /* add shared user(s)*/
        if (appointment.getObjectID() != null) {
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {

                calendarService.getTaskAssignees(appointment.getObjectID(), new AbstractAsyncCallback<ArrayList<IdTime>>() {
                    @Override
                    public void failure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void success(ArrayList<IdTime> result) {
                        details.setHTML(4, 0, wfmStrings.assignedTo() + ": ");
                        StringBuilder assignees = new StringBuilder("");
                        int i = 1;
                        if (result != null && result.size() > 0) {
                            ArrayList<Attendee> attendees = new ArrayList<>();
                            for (IdTime idTime : result) {
                                Attendee attendee = new Attendee();
                                attendee.setID(idTime.getId());
                                attendee.setName(idTime.getEmployeeName());
                                attendees.add(attendee);
                                assignees.append(idTime.getEmployeeName());
                                if (result.size() != i) {
                                    assignees.append(", ");
                                }
                                i++;
                            }
                            appointment.setAttendees(attendees);
                        }
                        Div assigneesPanel = new Div();
                        assigneesPanel.getElement().getStyle().setProperty("wordBreak", "break-word");
                        assigneesPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
                        assigneesPanel.add(new Span(assignees.toString()));
                        details.setWidget(4, 1, assigneesPanel);
                    }
                });
            } else if (Appointment.YELLOW.equals(appointment.getStyle())) {
                if (appointment.getOwnerName() != null) {
                    details.setHTML(4, 0, wfmStrings.instructor());
                    Div instructorPanel = new Div();
                    instructorPanel.add(new Span(appointment.getOwnerName()));
                    details.setWidget(4, 1, instructorPanel);
                }
                if (appointment.getMaxAttendents() != null) {
                    details.setHTML(5, 0, wfmStrings.numberOfSeats());
                    Div attendancesPanel = new Div();
                    attendancesPanel.add(new Span(appointment.getMaxAttendents().toString()));
                    details.setWidget(5, 1, attendancesPanel);
                }
            } else {
                calendarService.getEventSharedEmployees(appointment.getObjectID(), appointment.getCreatedBy(), new AbstractAsyncCallback<String>() {
                    @Override
                    public void success(String result) {
                        if (!result.equals("")) {
                            details.setHTML(4, 0, wfmStrings.sharedWith());
                            Div sharedPanel = new Div();
                            sharedPanel.getElement().getStyle().setProperty("wordBreak", "break-word");
                            sharedPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
                            sharedPanel.add(new Span(result));
                            details.setWidget(4, 1, sharedPanel);
                        }
                    }
                });
            }
        }

        details.getCellFormatter().setVerticalAlignment(1, 0, HasAlignment.ALIGN_TOP);
        details.getCellFormatter().setWordWrap(1, 1, true);

        int index = 5;
        if (appointment.getRelations().size() > 0) {
            VerticalPanelDiv crmLinkTable = AddTaggingView.drawRelationTags(RelationItem.TYPE_EVENT, appointment.getObjectID(), appointment.getRelations().toArray(new RelationItem[]{}));
            details.setHTML(index, 0, wfmStrings.relatedTo() + ":");
            details.setWidget(index++, 1, crmLinkTable);
        }

        // Event Guests
        if (appointment.getGuests() != null && !appointment.getGuests().isEmpty()) {
            details.setHTML(index, 0, "Guests" + ":");
            StringBuilder buffer = new StringBuilder();
            for (SelectItem guest : appointment.getGuests()) {
                String[] guestName = guest.getName().split("[<>]");
                buffer.append(guestName[0] + "[" + guest.getDescription() + "]").append(", ");
            }
            details.setText(index++, 1, buffer.length() >= 2 ? buffer.substring(0, buffer.length() - 2) : "");
        }

        // Attachments related
        final Integer newIndex = index++;
        calendarService.getEventAttachments(appointment.getObjectID(), new AbstractAsyncCallback<FileResource[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(FileResource[] fileResources) {
                VerticalPanel filesPanel = new VerticalPanel();
                filesPanel.setWidth("300px");
                StringBuilder fileNames = new StringBuilder("");
                if (fileResources != null && fileResources.length > 0) {
                    details.setHTML(newIndex, 0, "Attachments: ");
                    for (final FileResource file : fileResources) {
                        SimpleLink downloadLink = new SimpleLink(file.getName());
                        downloadLink.addClickHandler(sender -> {
                            if (file.getBodyId() != null) {
                                Utils.showImageOrDownloadFile(file, false);
                            }
                        });
                        filesPanel.add(downloadLink);
                    }
                    details.setWidget(newIndex, 1, filesPanel);
                }
            }
        });

        if (appointment.isEditable()) {
            index = newIndex + 1;
            details.setHTML(index, 0, wfmStrings.createdBy() + ":");
            Div createdByPanel = new Div();
            createdByPanel.getElement().getStyle().setProperty("wordBreak", "break-word");
            createdByPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
            createdByPanel.add(new Span(appointment.getCreatedBy()));
            details.setWidget(index++, 1, createdByPanel);
        }

        HorizontalPanel panel = new HorizontalPanel();
        panel.getElement().getStyle().setProperty("width", "auto");

        MaterialLink viewLink = new MaterialLink();
        viewLink.setStyleName("btn--circle");
        viewLink.getElement().setInnerHTML("<svg class=\"icon--view\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#view\"></use></svg>");
        viewLink.setTooltip(wfmStrings.summaryView());
        viewLink.addClickHandler(event -> {
            if (Appointment.RED.equals(appointment.getStyle())) {
                popup.hide();
                SinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + appointment.getObjectID());
            } else if (Appointment.GREEN.equals(appointment.getStyle())) {
                popup.hide();
                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + appointment.getObjectID());
            } else if (Appointment.YELLOW.equals(appointment.getStyle())) {
                popup.hide();
                Utils.openURL(appointment.getLinkURL());
            } else if (!(Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask())) {
                popup.hide();
                SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + appointment.getObjectID() + "/" + appointment.getActivityType());
            } else {
                popup.hide();
                Utils.openURL(appointment.getLinkURL());
            }
        });
        if (appointment.isEditable() && ((appointment.getOwnerID() != null && Utils.getUserID().equals(appointment.getOwnerID())) || (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.CALENDAR_EDITOR)))) {
            MaterialLink editLink = new MaterialLink();
            editLink.setStyleName("btn--circle");
            editLink.getElement().setInnerHTML("<svg class=\"icon--edit\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#edit\"></use></svg>");
            editLink.setTooltip(wfmStrings.edit());
            editLink.addClickHandler(event -> {
                if (Appointment.YELLOW.equals(appointment.getStyle())) {
                    popup.hide();
                    Utils.openURL("TrainingCenter.html#scheduledcourse|edit/" + appointment.getObjectID());
                } else if (!(Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask())) {
                    popup.hide();
                    SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + appointment.getObjectID() + "/" + appointment.getActivityType());
                } else {
                    popup.hide();
                    SinksContainerFactory.entryPoint.onHistoryChanged("task|edit/" + appointment.getObjectID());
                }
            });

            MaterialLink deleteLink = new MaterialLink();
            deleteLink.setStyleName("btn--circle");
            deleteLink.getElement().setInnerHTML("<svg class=\"icon--trash2\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#trash2\"></use></svg>");
            deleteLink.setTooltip(wfmStrings.delete());
            deleteLink.addClickHandler(event -> {
                if (appointment.getRecurrenceId() != null) {
                    updateOrDeleteEventItem(appointment, "delete", null, deleteHandler);
                    popup.hide();
                } else {
                    popup.hide();
                    deleteHandler.onDelete(appointment);
                }
            });
            panel.add(viewLink);
            panel.setCellHorizontalAlignment(viewLink, HasAlignment.ALIGN_LEFT);
//            if (Appointment.ORANGE.equals(appointment.getStyle()) || (Appointment.GREEN.equals(appointment.getStyle()) || (appointment.isTask() && !appointment.isNoTask())) || Appointment.PURPLE.equals(appointment.getStyle())) {
            panel.add(deleteLink);
            panel.setCellHorizontalAlignment(deleteLink, HasAlignment.ALIGN_CENTER);
//            }

            panel.add(editLink);
            panel.setCellHorizontalAlignment(editLink, HasAlignment.ALIGN_RIGHT);
        } else {
//            if (Appointment.ORANGE.equals(appointment.getStyle()) || (Appointment.GREEN.equals(appointment.getStyle()) || (appointment.isTask() && !appointment.isNoTask())) || Appointment.PURPLE.equals(appointment.getStyle())) {
            panel.add(viewLink);
            panel.setCellHorizontalAlignment(viewLink, HasAlignment.ALIGN_CENTER);
//            }
        }

        details.setWidget(index, 0, panel);
        details.getFlexCellFormatter().setColSpan(index, 0, 2);
        details.getCellFormatter().setStyleName(index, 0, eventDetailsStyle);

        MaterialPanel detailsPanel = new MaterialPanel();
        detailsPanel.add(details);
        table.setWidget(1, 0, detailsPanel);
        table.getFlexCellFormatter().setStyleName(1, 0, "event-det-table__wrapper");

        drawOvalPanel(table);
    }

    public static Boolean updateOrDeleteEventItem(final Appointment appointment, final String action, final SaveAppointmentHandler saveHandler, final DeleteAppointmentHandler deleteHandler) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setAnimationEnabled(true);
        dialogBox.setGlassEnabled(true);
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(5);
        String appointmentType = "";
        if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
            appointmentType = "task";
        } else {
            appointmentType = "event";
        }

        String cancelBtnText = "";
        if ("delete".equals(action)) {
            dialogBox.setText("Delete recurring " + appointmentType);
            vp.add(new HTML("Would you like to delete only this event, all events in the series, or this and all future events in the series?".replace("event", appointmentType)));
            cancelBtnText = "Don't delete";
        } else {
            dialogBox.setText("Edit recurring " + appointmentType);
            vp.add(new HTML("Would you like to change only this event, all events in the series, or this and all future events in the series?".replace("event", appointmentType)));
            cancelBtnText = "Cancel this change";
        }
        Button thisInstance = new Button("Only this instance", (ClickHandler) event -> {
            dialogBox.hide();
            if ("delete".equals(action)) {
                appointment.setAction(Constants.DELETE_THIS_INSTANCE);
                deleteHandler.onDelete(appointment);
            } else {
                appointment.setAction(Constants.EDIT_THIS_INSTANCE);
                if (saveHandler != null) {
                    if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                        saveHandler.onSaveOrUpdateTask(appointment);
                    } else {
                        saveHandler.onSaveOrUpdate(appointment);
                    }
                }
            }
        });
        Button allInThisSeries = new Button("All " + appointmentType + "s in the series", (ClickHandler) event -> {
            dialogBox.hide();
            if ("delete".equals(action)) {
                appointment.setAction(Constants.DELETE_ALL_SERIES);
                deleteHandler.onDelete(appointment);
            } else {
                appointment.setAction(Constants.EDIT_ALL_SERIES);
                if (saveHandler != null) {
                    if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                        saveHandler.onSaveOrUpdateTask(appointment);
                    } else {
                        saveHandler.onSaveOrUpdate(appointment);
                    }
                }
            }
        });
        Button allFollowing = new Button("All following", (ClickHandler) event -> {
            dialogBox.hide();
            if ("delete".equals(action)) {
                appointment.setAction(Constants.DELETE_ALL_FOLLOWING);
                deleteHandler.onDelete(appointment);
            } else {
                appointment.setAction(Constants.EDIT_ALL_FOLLOWING);
                if (saveHandler != null) {
                    if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                        saveHandler.onSaveOrUpdateTask(appointment);
                    } else {
                        saveHandler.onSaveOrUpdate(appointment);
                    }
                }
            }
        });
        Button doNotDelete = new Button(cancelBtnText, (ClickHandler) event -> dialogBox.hide());

        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.setSpacing(3);
        buttonsPanel.add(thisInstance);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        buttonsPanel.add(allInThisSeries);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        buttonsPanel.add(allFollowing);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        buttonsPanel.add(doNotDelete);

        vp.add(buttonsPanel);
        dialogBox.setWidget(vp);
        dialogBox.show();
        return null;
    }

    private void reloadNewView() {
        if (popup.getWidget() != null) {
            popup.clear();
        }

        details.clear();
        for (int i = details.getRowCount() - 1; i >= 0; i--) {
            details.removeRow(i);
        }

        drawNewAppointmentView();

        whatText.setText(appointment.getSubject());
        createEvent.setText((Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask())
                ? wfmStrings.updateTask()
                : Property.get(Constants.EVENT_LIST, wfmStrings.updateEvent(), wfmStrings.event()));
        eventDetails.setHTML("<span style='text-decoration: underline; cursor: pointer;'>" + wfmStrings.updateMoreDetails() + "</span>");
    }

    private final String validationStyle = "x-form-invalid";

    private WfmButton2 createEvent;
    private TextBox whatText;
    private SimpleLink eventDetails;

    private void drawNewAppointmentView() {

        details.setWidth("100%");
        details.setCellSpacing(2);
        details.setCellPadding(2);
        details.setHeight("20px");

        startDate = new DatePicker();
        startDate.ensureDebugId("event_start_date");
        startDate.setDate(appointment.getStartDate());
        from = new KpiTimePicker(true, popup);
        from.setValue(appointment.isAllDay() ? getNextFromTime(from.getValue()) : KpiTimePicker.getHoursAndMinutes(startDate.getDate()));
        from.addStyleName("form-control");
        from.setWidth("55px");
        from.setChangeCommand(() -> {
            if (from.getValue() != null) {
                int[] fromtime = from.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                to.setValue(getToTime(hour, minutes));
            }
        });

        endDate = new DatePicker();
        endDate.ensureDebugId("event_end_date");
        endDate.setDate(appointment.isAllDay() ? DateUtil.getDayLastTime(appointment.getEndDate()) : appointment.getEndDate());
        endDate.ensureDebugId("event_end_date");
        to = new KpiTimePicker(true, popup);
        to.setValue(KpiTimePicker.getHoursAndMinutes(endDate.getDate()));
        to.addStyleName("form-control");
        to.setWidth("55px");

        KpiCheckBox allDay = new KpiCheckBox(wfmStrings.allDay());
        allDay.setValue(appointment.isAllDay());
        allDay.addClickHandler(event -> {
            from.setEnabled(!allDay.getValue());
            to.setEnabled(!allDay.getValue());
            if (appointment.isAllDay() && !allDay.getValue()) {
                int[] fromtime = from.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                to.setValue(getToTime(hour, minutes));
            } else {
                to.setValue(KpiTimePicker.getHoursAndMinutes(endDate.getDate()));
            }
        });
        from.setEnabled(!allDay.getValue());
        to.setEnabled(!allDay.getValue());

        details.setWidget(0, 0, venueTime);
        details.setWidget(0, 1, from);
        details.getFlexCellFormatter().setStyleName(0, 1, "enevt-date-start");
        details.setWidget(0, 2, to);
        details.getFlexCellFormatter().setStyleName(0, 2, "enevt-date-end");
        details.setWidget(0, 3, allDay);
        details.getRowFormatter().addStyleName(0, "enevt-date");

        whatText = new TextBox();
        whatText.addKeyDownHandler(event -> {
            TextBox textbox = (TextBox) event.getSource();
            textbox.removeStyleName(validationStyle);

            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                save(startDate, endDate, allDay.getValue());
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                popup.hide();
            }
        });

        whatText.addFocusHandler(event -> ((TextBox) event.getSource()).removeStyleName(validationStyle));
        whatText.setPlaceHolder(wfmStrings.eg7pmMeetingAtOffice());
        whatText.addStyleName("event-name");
        whatText.setFocus(true);

        FlexTable name = new FlexTable();
        name.setWidget(0, 0, whatText);

        createEvent = new WfmButton2(wfmStrings.create(), WfmButton2.BTN_PRIMARY);
        createEvent.setEnabled(true);
        createEvent.addClickHandler(event -> {
            if (createEvent.isEnabled()) {
                createEvent.setEnabled(false);
                save(startDate, endDate, allDay.getValue());
            }
        });

        eventDetails = new SimpleLink(wfmStrings.moreDetails(), null, "", "", "btn-flat btn-link");
        eventDetails.addClickHandler(event -> {
            popup.hide();
            appointment.setSubject(whatText.getText());
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + appointment.getObjectID());
            } else {
                ActivityQuickAddForm appointmentView = new ActivityQuickAddForm(appointment);
                appointmentView.setHandler(saveHandler);
            }
        });


        VerticalPanel pnlEventDetails = new VerticalPanel();
        pnlEventDetails.add(eventDetails);

        DockPanel dock = new DockPanel();
        dock.setWidth("100%");
        dock.add(pnlEventDetails, DockPanel.WEST);
        dock.setCellHorizontalAlignment(pnlEventDetails, DockPanel.ALIGN_RIGHT);
        dock.add(createEvent, DockPanel.EAST);
        dock.setCellHorizontalAlignment(createEvent, DockPanel.ALIGN_RIGHT);
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("cal-popup-table");
        vp.setSpacing(7);
        vp.add(name);
        vp.add(details);
        vp.add(dock);

        drawOvalPanel(vp);
    }

    private int[] getNextFromTime(int[] fromtime) {
        int hour = fromtime[0];
        int minutes = fromtime[1];
        return getToTime(hour, minutes);
    }

    private int[] getToTime(int hour, int minutes) {
        int[] result = new int[2];
        if (minutes < 30) {
            minutes = 30;
        } else {
            minutes = 00;
            hour++;
        }
        result[0] = hour;
        result[1] = minutes;
        return result;
    }

    private void save(DatePicker startDate, DatePicker endDate, boolean isAllDay) {
        if (!validate(startDate, endDate, isAllDay)) {
            return;
        }
        appointment.setSubject(whatText.getText());
        appointment.setCreatedBy(Utils.getFullName());

        Date start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(), from.getValue()[0], from.getValue()[1]);
        Date end = new Date(endDate.getDate().getYear(), endDate.getDate().getMonth(), endDate.getDate().getDate(), to.getValue()[0], to.getValue()[1]);
        appointment.setStartDate(start);
        appointment.setEndDate(end);
        appointment.setAllDay(isAllDay);

        ArrayList<Attendee> attendeeList = new ArrayList<>();
        Attendee attendee = new Attendee();
        attendee.setID(Utils.getUserID());
        attendeeList.add(attendee);
        calendarService.isAssigneeOnHoliday(attendeeList, appointment.getStartDate(), appointment.getEndDate(), appointment.isAllDay(), new AbstractAsyncCallback<String>() {
            public void success(String result) {
                if (result != null && !result.equals("")) {
                    String haveAholidayContinueAnyway = wfmStrings.youHaveHolidayOnDate();
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, haveAholidayContinueAnyway, new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            saveHandler.onSaveOrUpdate(appointment);
                        }
                    });
                    wfmMessageBox.setTitle(wfmStrings.confirmation());
                    wfmMessageBox.open();
                } else {
                    saveHandler.onSaveOrUpdate(appointment);
                }
            }
        });
        popup.hide();
        createEvent.setEnabled(true);
    }

    public boolean validate(DatePicker startDate, DatePicker endDate, boolean allDay) {
        int errors = 0;
        boolean dateValid = true;
        if (!Validation.validateTextBoxRequired(whatText) && saveHandler != null) {
            errors++;
        }
        from.removeStyleName(Constants.ERROR_FORM_STYLE);
        to.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!allDay) {
            if (com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils.areOnTheSameDay(startDate.getDate(), endDate.getDate())) {
                Integer fromHour = from.getValue()[0];
                Integer fromMinute = from.getValue()[1];
                Integer toHour = to.getValue()[0];
                Integer toMinute = to.getValue()[1];
                if ((fromHour.equals(toHour) && fromMinute >= toMinute) || fromHour > toHour) {
                    from.addStyleName(Constants.ERROR_FORM_STYLE);
                    to.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            } else if (endDate.getDate().before(startDate.getDate())) {
                startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                endDate.addStyleName(Constants.ERROR_FORM_STYLE);
                dateValid = false;
            }
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            createEvent.setEnabled(true);
            return false;
        } else if (!dateValid) {
            Info.warn(wfmStrings.pleaseChooseValidDate());
            createEvent.setEnabled(true);
            return false;
        }
        return true;
    }
}
