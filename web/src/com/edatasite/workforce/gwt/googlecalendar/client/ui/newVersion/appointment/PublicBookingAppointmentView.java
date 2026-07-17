package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.core.client.Validation;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.view.OvalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Apr 6, 2011
 * Time: 2:18:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class PublicBookingAppointmentView implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmForm.Field firstNameField;
    private WfmForm.Field lastNameField;
    private WfmForm.Field emailAddressField;
    private WfmForm.Field phoneField;
    private WfmForm.Field attendantsField;
    private WfmForm form;

    private TextBox firstName;
    private TextBox lastName;
    private TextBox emailAddress;
    private TextBox phone;
    private TextBox attendants;
    private WfmButton2 btnBook;
    private WfmButton2 btnCancel;

    private PopupPanel popup;
    private Appointment appointment;
    private SaveAppointmentHandler handler;
    private ReservationItem reservationItem = new ReservationItem();
    private Integer objectID;

    public PublicBookingAppointmentView(Appointment appointment) {
        this.appointment = appointment;
        initComponents();
        draw();
    }

    private void drawOvalPanel(Panel panel) {
        OvalPanel ovalPanel = new OvalPanel();
        ovalPanel.add(panel);
        ovalPanel.addCloseButtonClickHandler(event -> popup.hide());

        popup.setWidget(ovalPanel);
        popup.show();
    }

    private void initComponents() {
        popup = new PopupPanel(false, false);
        popup.setStyleName("cal-popup");
        popup.setAnimationEnabled(true);
    }

    private void draw() {
        firstName = new TextBox();
        lastName = new TextBox();
        emailAddress = new TextBox();
        phone = new TextBox();
        attendants = new TextBox();
        attendants.setValue("1");
        Validation.addNumericKeyboardListener(attendants, 4);

        //btnSave
        btnBook = new WfmButton2(wfmStrings.save(), (ClickHandler) sender -> save());

        btnCancel = new WfmButton2(wfmStrings.cancel(), (ClickHandler) sender -> popup.hide());

        form = new WfmForm();
        firstNameField = form.addField("First Name", firstName, true);
        lastNameField = form.addField("Last Name", lastName, true);
        emailAddressField = form.addField("Email", emailAddress, true);
        phoneField = form.addField("Phone", phone, true);
        attendantsField = form.addField("Attendants", attendants, true);

        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.add(btnBook);
        btnPanel.add(btnCancel);

        VerticalPanel panel = new VerticalPanel();
        panel.add(form);
        panel.add(btnPanel);

        drawOvalPanel(panel);
    }

    private void save() {
        if (validate()) {
            reservationItem = getReservation();
            Integer companyId = Integer.parseInt(Cookies.getCookie("COMPANY_ID"));
            //LoadingPanel.loading(true);
            AccountingService.App.get().makeReservation(reservationItem, companyId, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(Integer id) {
                    popup.hide();
                }
            });
//            ReserveProductAction reserveAction = new ReserveProductAction();
//            reserveAction.setReservation(reservationItem);
//            reserveAction.setCompanyID(Integer.parseInt(Cookies.getCookie("COMPANY_ID")));
//            reserveAction.setSubject("EVENT BOOKING");
//            reserveAction.setText("Dear Administrator,\n" +
//                    "There was a new registration:\n\n" +
//                    "First Name: " + firstName.getText() + "\n" +
//                    "Last Name: " + lastName.getText() + "\n" +
//                    "Email Address: " + emailAddress.getText() + "\n" +
//                    (!phone.getText().equals("") ? "Phone Number: " + phone.getText() : ""));
//
//            WfpEntryPoint.injector.getDispatcher().execute(reserveAction, new AbstractAsyncCallback<ReserveProductResult>() {
//                @Override
//                public void failure(Throwable throwable) {
//                    setEnabledButtons(true);
//                    LoadingPanel.loading(false);
//                    GWT.log(throwable.getMessage());
//                }
//
//                @Override
//                public void success(ReserveProductResult result) {
//                    setEnabledButtons(true);
//                    LoadingPanel.loading(false);
//                    if (result != null) {
//                         Info.show("", "You have successfully booked the event.", "");
//                    } else {
//                         Info.show("", could not book the event.", "");
//                    }
//                    popup.hide();
//                }
//            });
        }
    }

    private boolean validate() {
        int errors = 0;
        form.cleanupErrors();
        errors += !Validation.validateTextBoxRequired(firstName, firstNameField) ? 1 : 0;
        errors += !Validation.validateTextBoxRequired(lastName, lastNameField) ? 1 : 0;
        errors += !Validation.validateTextBoxRequired(emailAddress, emailAddressField) ? 1 : 0;
        errors += !Validation.validateTextBoxRequired(phone, phoneField) ? 1 : 0;
        errors += !Validation.validateTextBoxRequired(attendants, attendantsField) ? 1 : 0;

        return errors <= 0;
    }

    private ReservationItem getReservation() {
        reservationItem.setId(objectID);
        reservationItem.setStatus(RESERVATION_STATUS_PENDING);
        reservationItem.setEventId(appointment.getObjectID());
        reservationItem.setQty(Integer.parseInt(attendants.getValue()));
        ContactItem contactItem = new ContactItem();
        contactItem.setFirstname(firstName.getValue());
        contactItem.setLastname(lastName.getValue());
        contactItem.setEmail(emailAddress.getValue());
        contactItem.setPhone(phone.getValue());
        reservationItem.setContact(contactItem);
        reservationItem.setFromDate(appointment.getStartDate());
        reservationItem.setToDate(appointment.getEndDate());
        return reservationItem;
    }

    private void setEnabledButtons(boolean b) {
        if (btnBook != null) {
            btnBook.setEnabled(b);
        }
        if (btnCancel != null) {
            btnCancel.setEnabled(b);
        }
    }

    public void onSaveAppointment(SaveAppointmentHandler handler) {
        this.handler = handler;
    }


}