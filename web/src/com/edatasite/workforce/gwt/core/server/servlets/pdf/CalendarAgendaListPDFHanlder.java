package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilterParameters;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.WorkforceEvents;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 23.12.2009
 * Time: 20:42:19
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CalendarAgendaListPDFHanlder extends PostPDFHandler {

    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;


    protected String getFileRepository() {
        return "/WEB-INF/xslts/calendar_list.xslt";
    }

    public void writePDF(Object object) {
        try {
            EdsUser user = uploadManager.getUser();


            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", commonLocalizer.initializeUserLocale());
            format.setTimeZone(user.getUserTimezone());

            ListLoadConfig config = new ListLoadConfig();
            config.setLimit(5000);

            super.setFileName(user.getFirstName() + "_" + user.getLastName() + "_CalendarAgendaList_" + dateFormat(user.getUserDate()));
            super.startDocument();

            String logoPath = getRealPath(EdsContextParams.getPdfLogo());
            String pdfLofoUrl = EdsContextParams.getHelpHost();
            String companyLOGO = getCompanyLogoUrl(user.getCompany());
            if (companyLOGO != null) {
                writeElement("logoPath", companyLOGO);
            } else if (user.getCompany().getShowWorkforceLogoOnPDF()) {
                writeElement("logoPath", logoPath);
            }

            if(pdfLofoUrl !=null){
                 writeElement("pdfLofoUrl", pdfLofoUrl);
            }

            String url = getCompanyLogoUrl(user.getCompany());
            if (url != null) {
                startElement("company_logo");
                writeElement("companyLogo", url);
                endElement("company_logo");
            }


            writeElement("calendarAgenda", commonLocalizer.localize(PdfLocalizationName.calendarAgenda));
            writeElement("company", user.getCompany().getName());
            writeElement("user", user.getFullName() + "'s ");
            writeElement("poweredBy", commonLocalizer.localize(PdfLocalizationName.poweredBy));
            writeElement("pdfPageNumber", commonLocalizer.localize(PdfLocalizationName.page) + " ");

            writeElement("company", user.getCompany().getName() != null ? user.getCompany().getName() : "");
            writeElement("companyStreet", user.getCompany().getAddress1() != null ? user.getCompany().getAddress1() : "");
            writeElement("companyCity", user.getCompany().getCity() + " " != null ? user.getCompany().getCity() : "");
            writeElement("companyPostCode", user.getCompany().getPostCode() != null ? user.getCompany().getPostCode() : "");
            writeElement("companyCountry", user.getCompany().getCountryZone().getCountry().getName() != null
                    ? countryLocalizer.localize(user.getCompany().getCountryZone().getCountry().getCode(), user.getCompany().getCountryZone().getCountry().getName()) : "");




            CalendarFilterParameters parameters = (CalendarFilterParameters) object;
            Calendar start = new GregorianCalendar();
            start.setTimeInMillis(parameters.getDueDate());

            Calendar end = new GregorianCalendar();
            end.setTimeInMillis(parameters.getDueDate() + 1000 * 60 * 60 * 24 - 1);

            int j = 0;
            do {
                startElement("events");

                WorkforceEvents wftEvents = googleCalendarServiceLocal.getWorkforceTrackEventsForPDF(start.getTime(), end.getTime(), true);
                if ((wftEvents.getEvents() != null && wftEvents.getEvents().size() != 0 && parameters.isShowEvent()) || (wftEvents.getProjects() != null && wftEvents.getProjects().size() != 0 && parameters.isShowProject()) ||
                        (wftEvents.getTasks() != null && wftEvents.getTasks().size() != 0 && parameters.isShowTasks()) || (wftEvents.getIssues() != null && wftEvents.getIssues().size() != 0 && parameters.isShowIssues()) ||
                        (wftEvents.getLeaveRequests() != null && wftEvents.getLeaveRequests().size() != 0 && parameters.isShowLeaveRequest()) || (wftEvents.getPerformanceAppraisals() != null && wftEvents.getPerformanceAppraisals().size() != 0 && parameters.isShowPA()) ||
                        (wftEvents.getHolidays() != null && wftEvents.getHolidays().size() != 0 && parameters.isShowHolidays())) {
                    writeElement("date", format.format(start.getTime()));
                }
                if (wftEvents.getEvents() != null && wftEvents.getEvents().size() != 0) {
                    writeElementsEvents("calendarEvent0", wftEvents.getEvents(), parameters.isShowEvent(), user);
                }
                if (wftEvents.getProjects() != null && wftEvents.getProjects().size() != 0) {
                    writeElements("projects0", wftEvents.getProjects(), parameters.isShowProject(), "MMM dd, yyyy", user);
                }
                if (wftEvents.getTasks() != null && wftEvents.getTasks().size() != 0) {
                    writeElements("tasks0", wftEvents.getTasks(), parameters.isShowTasks(), "MMM dd, yyyy", user);
                }
                if (wftEvents.getIssues() != null && wftEvents.getIssues().size() != 0) {
                    writeElements("issues0", wftEvents.getIssues(), parameters.isShowIssues(), "MMM dd, yyyy", user);
                }
                if (wftEvents.getLeaveRequests() != null && wftEvents.getLeaveRequests().size() != 0) {
                    writeElements("leaveRequests0", wftEvents.getLeaveRequests(), parameters.isShowLeaveRequest(), "MMM dd, yyyy HH:mm", user);
                }
                if (wftEvents.getPerformanceAppraisals() != null && wftEvents.getPerformanceAppraisals().size() != 0) {
                    writeElements("performanceAppraisals0", wftEvents.getPerformanceAppraisals(), parameters.isShowPA(), "MMM dd, yyyy", user);
                }
                if (wftEvents.getHolidays() != null && wftEvents.getHolidays().size() != 0) {
                    writeElements("holidays0", wftEvents.getHolidays(), parameters.isShowHolidays(), "MMM dd, yyyy", user);
                }

                writeElement("Atabek", commonLocalizer.localize(PdfLocalizationName.calendarAgenda));


                endElement("events");

                start.add(Calendar.DAY_OF_YEAR, 1);
                end.add(Calendar.DAY_OF_YEAR, 1);

                j++;
            } while (j < parameters.getDays());

            endDocument();
        } catch (SAXException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeElements(String calendarName, ArrayList<Appointment> appointments, boolean show, String dateFormat, EdsUser user)
            throws SAXException, IOException {
        if (appointments != null && appointments.size() > 0 && show) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, commonLocalizer.initializeUserLocale());
            HTMLParser html = new HTMLParser();

            startElement(calendarName);

            for (Appointment appointment : appointments) {
                startElement("calendarEvent");
                writeElement("calenradTaskLocalize", commonLocalizer.localize(PdfLocalizationName.tasks));
                writeElement("calenradStartDateLocalize", commonLocalizer.localize(PdfLocalizationName.startDateField));
                writeElement("calenradEndDateLocalize", commonLocalizer.localize(PdfLocalizationName.endDateField));
                writeElement("calenradDescriptionLocalize", commonLocalizer.localize(PdfLocalizationName.description));
                writeElement("calenradCreaterLocalize", commonLocalizer.localize(PdfLocalizationName.creator));
                writeElement("calenradWhereLocalize", commonLocalizer.localize(PdfLocalizationName.calendarWhere));
                writeElement("calenradWhenLocalize", commonLocalizer.localize(PdfLocalizationName.when));

                writeElement("title", appointment.getSubject() != null ? appointment.getSubject() : "");
                if (!appointment.isAllDay()) {
                    format = new SimpleDateFormat("MMM dd, yyyy HH:mm", commonLocalizer.initializeUserLocale());
                    writeElement("startDate", format.format(user.getUserDate(appointment.getStartDate())));
                    writeElement("endDate", format.format(user.getUserDate(appointment.getEndDate())));
                } else {
                    format = new SimpleDateFormat(dateFormat, commonLocalizer.initializeUserLocale());
                    writeElement("startDate", format.format(user.getUserDate(appointment.getStartDate())));
                    writeElement("endDate", format.format(user.getUserDate(appointment.getEndDate())));
                }

                writeElement("description", html.performParse(appointment.getDescription() != null ? appointment.getDescription() : ""));
                writeElement("creater", appointment.getCreatedBy());

                endElement("calendarEvent");
            }

            endElement(calendarName);
        }
    }

    private void writeElementsEvents(String calendarName, ArrayList<Appointment> appointments, boolean show, EdsUser user)
            throws SAXException, IOException {
        if (appointments != null && appointments.size() > 0 && show) {
//            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            HTMLParser html = new HTMLParser();

            startElement(calendarName);

            for (Appointment appointment : appointments) {
                startElement("Event");
                writeElement("calenradEventsLocalize", commonLocalizer.localize(PdfLocalizationName.calendarEvents));
                writeElement("calenradStartEndDateLocalize", commonLocalizer.localize(PdfLocalizationName.endDateField));
                writeElement("calenradWhereLocalize", commonLocalizer.localize(PdfLocalizationName.calendarWhere));
                writeElement("calenradWhenLocalize", commonLocalizer.localize(PdfLocalizationName.when));
                writeElement("calenradDescriptionLocalize", commonLocalizer.localize(PdfLocalizationName.description));
                writeElement("calenradCreaterLocalize", commonLocalizer.localize(PdfLocalizationName.creator));

                writeElement("title", appointment.getSubject() != null ? appointment.getSubject() : "");
                writeElement("startEndDate", getDate(user, appointment));
                writeElement("where", appointment.getLocation() != null ? appointment.getLocation() : "No location is appointed to this event");
                writeElement("description", html.performParse(appointment.getDescription() != null ? appointment.getDescription() : ""));
                writeElement("creater", appointment.getCreatedBy());

                endElement("Event");
            }

            endElement(calendarName);
        }
    }

    private String getDate(EdsUser owner, Appointment appointment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", commonLocalizer.initializeUserLocale());
        SimpleDateFormat hourFormat = new SimpleDateFormat("k:mm", commonLocalizer.initializeUserLocale());
        String startDate = dateFormat.format(owner.getUserDate(appointment.getStartDate()));
        String startHour = hourFormat.format(owner.getUserDate(appointment.getStartDate()));
        startHour = startHour.equals("24:00") ? "00:00" : startHour;
        String endDate = dateFormat.format(owner.getUserDate(appointment.getEndDate()));
        String endHour = hourFormat.format(owner.getUserDate(appointment.getEndDate()));
        endHour = endHour.equals("24:00") ? "00:00" : endHour;
        String timeZone = owner.getUserTimezone().getID();
        String date = "";

        if (!appointment.isAllDay()) {
            if (startDate.equals(endDate)) {
                date = startDate + " " + startHour + " - " + endHour + " (" + timeZone + ")";
            } else {
                if (startHour.equals(endHour)) {
                    date = startDate + " - " + endDate + " " + " (" + timeZone + ")";
                } else {
                    date = startDate + " " + startHour + " - " + endDate + " " + endHour + " (" + timeZone + ")";
                }
            }
        } else {
            date = startDate + " (" + timeZone + ")";
        }
        return date;
    }

    protected Object getDataClass() {
        return new CalendarFilterParameters();
    }
}
