package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 5:42:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditEventReservationView extends View implements Constants, AccountingConstants {

    //String settings properties
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    WfmForm form;
    WfmForm contactForm;

    WfmForm.Field nameField;
    WfmForm.Field whenField;
    WfmForm.Field statusField;

    WfmForm.Field firstnameField;
    WfmForm.Field lastnameField;
    WfmForm.Field emailField;
    WfmForm.Field phoneField;

    private Label name;
    private Label when;
    private DataListBox status;
    //Contact form
    private Label firstname;
    private Label lastname;
    private Label email;
    private Label phone;

    private WfmButton2 saveAndCloseButton;
    private WfmButton2 closeButton;
    //private WfmButton2 sendInvoiceButton;

    private boolean saveAndClose = false;
    private boolean sendInvoiceClicked = false;

    private ReservationItem reservationItem = new ReservationItem();
    private final Integer objectID;
    private Integer eventId;
    private Integer contactID;

    public EditEventReservationView(Integer objectID) {
        super("edit", wfmStrings.editReservation());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        initForm();
        drawButtonsPanel();
        loadData();
        return null;
    }

    private void initForm() {
        form = new WfmForm();
        form.setStyleName("padding10");
        form.setWidth("100%");
        form.setLabelSize("200px");

        name = new Label();
        when = new Label();

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);

        form.addTitleField(wfmStrings.reservationInformation(), true);
        form.addHorizontalLine();
        nameField = form.addField(wfmStrings.name(), name, true);
        whenField = form.addField(wfmStrings.when(), when, true);
        statusField = form.addField(wfmStrings.status(), status, true);
        form.addHorizontalLine();

        //Contact form
        contactForm = new WfmForm();
        contactForm.setStyleName("padding10");
        contactForm.setWidth("100%");
        contactForm.setLabelSize("200px");

        firstname = new Label();
        firstname.addStyleName(DEFAULT_WIDTH);

        lastname = new Label();
        lastname.addStyleName(DEFAULT_WIDTH);

        email = new Label();
        email.addStyleName(DEFAULT_WIDTH);

        phone = new Label();
        phone.addStyleName(DEFAULT_WIDTH);

        contactForm.addTitleField(wfmStrings.clientInformation());
        contactForm.addHorizontalLine();
        firstnameField = contactForm.addField(wfmStrings.firstName(), firstname);
        lastnameField = contactForm.addField(wfmStrings.lastName(), lastname);
        emailField = contactForm.addField(wfmStrings.email(), email);
        phoneField = contactForm.addField(wfmStrings.phone(), phone);

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(new HTML("<div style='width:270px'></div>"));
        add(form);
        add(hPanel);
        add(contactForm);
    }

    private void drawButtonsPanel() {
        MaterialPanel buttonsPanel = new MaterialPanel("btns-group");
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            setEnabledButtons(false);
            saveAndClose = true;
            save();
        });
        closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            setEnabledButtons(false);
            closeTab();
        });

        buttonsPanel.add(saveAndCloseButton);
        buttonsPanel.add(closeButton);

        MainLayout.get().addToActionsContainer(buttonsPanel);
        MainLayout.get().makeFrameContainerHaveTabsStyle(true);
    }

    private void setEnabledButtons(boolean b) {
        if (saveAndCloseButton != null) {
            saveAndCloseButton.setEnabled(b);
        }
        if (closeButton != null) {
            closeButton.setEnabled(b);
        }
//        if (sendInvoiceButton != null) {
//            sendInvoiceButton.setEnabled(b);
//        }
    }

    private void loadData() {
        if (objectID != null) {
            AccountingService.App.get().getReservation(objectID, new AsyncCallback<ReservationItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(final ReservationItem reservationItem) {
                    if (reservationItem != null) {
                        final DateTimeFormat shortDateFormat = DateTimeFormat.getShortDateFormat();
                        name.setText(reservationItem.getEventName());
                        when.setText(shortDateFormat.format(reservationItem.getFromDate()) + " - " + shortDateFormat.format(reservationItem.getToDate()));


                        status.setItems(new SelectItem[]{new SelectItem(RESERVATION_STATUS_RESERVED, accountingStrings.reserved()),
                                new SelectItem(RESERVATION_STATUS_PENDING, wfmStrings.pending()),
                                new SelectItem(RESERVATION_STATUS_CANCELED, accountingStrings.canceled())});

                        status.setSelected(reservationItem.getStatus());

                        if (reservationItem.getContact() != null) {
                            contactID = reservationItem.getContact().getId();
                            firstname.setText(reservationItem.getContact().getFirstname());
                            lastname.setText(reservationItem.getContact().getLastname());
                            email.setText(reservationItem.getContact().getEmail());
                            phone.setText(reservationItem.getContact().getPhone());
                        }

                        eventId = reservationItem.getEventId();
                    }
                }
            });

        }
    }

    private void save() {
        reservationItem = getReservation();
        if (validate()) {
            LoadingPanel.loading(true);
            AccountingService.App.get().makeReservation(reservationItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    setEnabledButtons(true);
                    LoadingPanel.loading(false);
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(Integer result) {
                    setEnabledButtons(true);
                    LoadingPanel.loading(false);
                    if (result != null && result > 0) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.reservations()), Info.Type.INFO);
                        closeTab();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RESERVATION_SAVED, null, null);
                        if (sendInvoiceClicked) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add/fromClientList/" + contactID + "/reservationID/" + objectID);
                        }
                    } else {
                        Info.show(wfmStrings.reservationNoSaved(), Info.Type.INFO);
                    }
                }
            });
        } else {
            sendInvoiceClicked = false;
            setEnabledButtons(true);
        }
    }

    private ReservationItem getReservation() {
        reservationItem.setId(objectID);
        reservationItem.setEventId(eventId);
        reservationItem.setStatus(status.getSelectedId());

        ContactItem contactItem = new ContactItem();
        contactItem.setId(contactID);
        contactItem.setFirstname(firstname.getText());
        contactItem.setLastname(lastname.getText());
        contactItem.setEmail(email.getText());
        contactItem.setPhone(phone.getText());
        reservationItem.setContact(contactItem);

        return reservationItem;
    }

    private Boolean validate() {
        boolean error = (statusField != null) && !Validation.validateListBoxRequired(status, statusField, accountingStrings.plsChooseStatus());

        return !error;
    }

    private void sendInvoice() {
        sendInvoiceClicked = true;
        save();
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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