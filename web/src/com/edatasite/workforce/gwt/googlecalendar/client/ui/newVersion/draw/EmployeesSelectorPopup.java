package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.AppointmentShareView;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Nov 26, 2010
 * Time: 9:30:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class EmployeesSelectorPopup {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private GoogleCalendarView calendarInstance;
    private FlexTable content;
    private AppointmentShareView tree;
    private KpiModal shell;
    private WfmButton2 showButton;
    private WfmButton2 closeButton;

    public EmployeesSelectorPopup(GoogleCalendarView calendarInstance) {
        this.calendarInstance = calendarInstance;
        initComponents();
    }

    private void initComponents() {
        initTree();

        showButton = new WfmButton2(wfmStrings.show(),WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            shell.close();
            calendarInstance.setIdList(getCheckedEmployeeIDs());
        });

        closeButton = new WfmButton2(wfmStrings.close(), (ClickHandler) event -> shell.close());

        content = new FlexTable();
        content.setWidget(0, 0, tree);
        content.getFlexCellFormatter().setColSpan(0, 0, 2);

        shell = new KpiModal();
        shell.setWidth("750px");
        shell.setHeight("560px");
        shell.setTitle(wfmStrings.selectCompanyEmpoyees());
//        shell.center();
        shell.add(content);
        shell.addButton(closeButton);
        shell.addButton(showButton);
    }

    private void initTree() {
        tree = new AppointmentShareView(true);
        tree.setHeight("290px");
    }

    public void show() {
        shell.open();
    }

    private ArrayList<Integer> getCheckedEmployeeIDs() {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Attendee> attendeeList = tree.getCheckedEmployees();
        if (attendeeList != null && !attendeeList.isEmpty()) {
            for (Attendee attendee : attendeeList) {
                result.add(attendee.getID());
            }
        }
        return result;
    }
}
