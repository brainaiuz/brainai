package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethodsList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 5:52:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddReservationView extends View implements Constants, AccountingConstants, Colapse {

    //String settings properties
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    WfmForm form;
    WfmForm contactForm;

    WfmForm.Field fromDateField;
    WfmForm.Field toDateField;
    WfmForm.Field shippingMethodField;
    WfmForm.Field statusField;
    WfmForm.Field categoryField;
    WfmForm.Field productField;

    WfmForm.Field firstnameField;
    WfmForm.Field lastnameField;
    WfmForm.Field emailField;
    WfmForm.Field phoneField;

    private DateTimePicker rentalDate;
    private DataListBox shippingMethod;
    private DataListBox status;
    private DataListBox category;
    private WfmDropdown product;
    //Contact form
    private TextBox firstname;
    private TextBox lastname;
    private TextBox email;
    private TextBox phone;

    private WfmButton2 saveButton;
    private WfmButton2 saveAndCloseButton;
    private WfmButton2 closeButton;
    private boolean saveAndClose = false;
    private final String addReservationView = "add_reservation_view_";

    private ReservationItem reservationItem = new ReservationItem();

    public AddReservationView() {
        super("addreservation", wfmStrings.addReservation());
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

        rentalDate = new DateTimePicker();
//        rentalDate.ensureDebugId(addReservationView+"rentalDate");
        rentalDate.startDate.addValueChangeHandler(dateValueChangeEvent -> loadProducts());

        rentalDate.dueDate.addValueChangeHandler(dateValueChangeEvent -> loadProducts());

        rentalDate.startTime.addValueChangeHandler(stringValueChangeEvent -> loadProducts());

        rentalDate.endTime.addValueChangeHandler(stringValueChangeEvent -> loadProducts());

        shippingMethod = new DataListBox();
        shippingMethod.ensureDebugId(addReservationView+"shippingMethod");
        shippingMethod.addStyleName(DEFAULT_WIDTH);

        status = new DataListBox();
        status.ensureDebugId(addReservationView+"status");
        status.addStyleName(DEFAULT_WIDTH);

        category = new DataListBox();
        category.ensureDebugId(addReservationView+"category");
        category.addStyleName(DEFAULT_WIDTH);
        category.addValueChangeHandler(changeEvent -> loadProducts());

        product = new WfmDropdown();
        product.ensureDebugId(addReservationView+"product");
        product.addStyleName(DEFAULT_WIDTH);

        form.addTitleField(wfmStrings.reservationInformation(), true);
        form.addHorizontalLine();
        fromDateField = form.addField(wfmStrings.fromDate(), new Widget[]{rentalDate.startDate, rentalDate.startTime}, true);
        toDateField = form.addField(wfmStrings.toDate(), new Widget[]{rentalDate.dueDate, rentalDate.endTime}, true);
        shippingMethodField = form.addField(accountingStrings.shippingMethod(), shippingMethod);
        statusField = form.addField(wfmStrings.status(), status, true);
        categoryField = form.addField(accountingStrings.rentalGroup(), category, true);
        productField = form.addField(accountingStrings.rentalItem(), product, true);
        productField.setVisible(false);

        //Contact form
        contactForm = new WfmForm();
        contactForm.setStyleName("padding10");
        contactForm.setWidth("100%");
        contactForm.setLabelSize("200px");

        firstname = new TextBox();
        firstname.ensureDebugId(addReservationView+"firstname");
        firstname.addStyleName(DEFAULT_WIDTH);

        lastname = new TextBox();
        lastname.ensureDebugId(addReservationView+"lastname");
        lastname.addStyleName(DEFAULT_WIDTH);

        email = new TextBox();
        email.ensureDebugId(addReservationView+"email");
        email.addStyleName(DEFAULT_WIDTH);

        phone = new TextBox();
        phone.ensureDebugId(addReservationView+"phone");
        phone.addStyleName(DEFAULT_WIDTH);

        contactForm.addTitleField(wfmStrings.clientInformation(), true);
        contactForm.addHorizontalLine();
        firstnameField = contactForm.addField(wfmStrings.firstName(), firstname, true);
        lastnameField = contactForm.addField(wfmStrings.lastName(), lastname, true);
        emailField = contactForm.addField(wfmStrings.email(), email, true);
        phoneField = contactForm.addField(wfmStrings.phone(), phone);

        add(form);
        add(contactForm);
    }

    private void drawButtonsPanel() {
        MaterialPanel buttonsPanel = new MaterialPanel("btns-group");
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            setEnabledButtons(false);
            save();
        });
        saveButton.ensureDebugId(addReservationView+"saveButton");

        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            setEnabledButtons(false);
            saveAndClose = true;
            save();
        });
        saveAndCloseButton.ensureDebugId(addReservationView+"saveAndCloseButton");

        closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            setEnabledButtons(false);
            closeTab();
        });
        closeButton.ensureDebugId(addReservationView+"closeButton");

        buttonsPanel.add(saveAndCloseButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(closeButton);
        MainLayout.get().addToActionsContainer(buttonsPanel);
        MainLayout.get().makeFrameContainerHaveTabsStyle(true);
    }

    private void setEnabledButtons(boolean b) {
        if (saveAndCloseButton != null) {
            saveAndCloseButton.setEnabled(b);
        }
        if (saveButton != null) {
            saveButton.setEnabled(b);
        }
        if (closeButton != null) {
            closeButton.setEnabled(b);
        }
    }

    private void loadData() {
        rentalDate.setStartDate(new Date());
        rentalDate.setDueDate(new Date());
        rentalDate.startTime.setValue("9:00 AM");
        rentalDate.endTime.setValue("9:00 AM");
        rentalDate.startTime.setVisible(true);
        rentalDate.endTime.setVisible(true);

        InvoiceService.App.get().getShippingMethodList(new AbstractAsyncCallback<ShippingMethodsList>() {
            @Override
            public void success(ShippingMethodsList result) {
                if (result != null && result.getResults() != null) {
                    shippingMethod.setItems(result.getResults());
                }
            }
        });

        status.setItems(new SelectItem[]{new SelectItem(RESERVATION_STATUS_PENDING, wfmStrings.pending()),
                new SelectItem(RESERVATION_STATUS_RESERVED, accountingStrings.reserved()),
                new SelectItem(RESERVATION_STATUS_STARTED, accountingStrings.started())});

        AccountingService.App.get().getCategoriesAsSelectItem(new AsyncCallback<SelectItem[]>() {
            public void onFailure(Throwable caught) {
                GWT.log(caught.getMessage());
            }

            public void onSuccess(SelectItem[] result) {
                category.setItems(result);
            }
        });
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
                    } else {
                        Info.show(wfmStrings.reservationNoSaved(), Info.Type.INFO);
                    }
                }
            });
        } else {
            setEnabledButtons(true);
        }
    }

    private ReservationItem getReservation() {
        reservationItem.setFromDate(rentalDate.getStartDate());
        reservationItem.setToDate(rentalDate.getDueDate());
        reservationItem.setShippingMethodId(shippingMethod.getSelectedId());
        reservationItem.setStatus(status.getSelectedId());
        reservationItem.setCategoryId(category.getSelectedId());
        reservationItem.setItemId(product.getSelectedId());

        ContactItem contactItem = new ContactItem();
        contactItem.setFirstname(firstname.getText());
        contactItem.setLastname(lastname.getText());
        contactItem.setEmail(email.getText());
        contactItem.setPhone(phone.getText());
        reservationItem.setContact(contactItem);

        return reservationItem;
    }

    private Boolean validate() {
        boolean error = false;
        if (!Validation.validateDateOrder(rentalDate.getStartDate(), rentalDate.getDueDate(), wfmStrings.startTimeValidation(), false)) {
            if (toDateField != null) {
                toDateField.setErrorMessage(wfmStrings.startTimeValidation(), "");
            }
            error = true;
        }

        if ((statusField != null) && !Validation.validateListBoxRequired(status, statusField, accountingStrings.plsChooseStatus())) {
            error = true;
        }

        if ((categoryField != null) && !Validation.validateListBoxRequired(category, categoryField, accountingStrings.plsChooseGroup())) {
            error = true;
        }

        if ((productField != null) && !Validation.validateWfmDropdownRequired(product, productField, accountingStrings.plsChooseItem())) {
            error = true;
            Info.show(accountingStrings.plsChooseItem(), Info.Type.INFO);
        }

        if ((firstnameField != null) && !Validation.validateTextBoxRequired(firstname, firstnameField)) {
            error = true;
        }

        if ((lastnameField != null) && !Validation.validateTextBoxRequired(lastname, lastnameField)) {
            error = true;
        }

        if ((emailField != null) && !Validation.validateEmailRequired(email, emailField)) {
            error = true;
        }

        return !error;
    }

    private void loadProducts() {
        if (category.getSelectedId() != null && category.getSelectedId() > 0) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setCategoryID(category.getSelectedId());
            filterParametrs.setStartDate(rentalDate.getStartDate());
            filterParametrs.setEndDate(rentalDate.getDueDate());
            //filterParametrs.setStatusID(PRODUCT_RENTAL_STATUS_AVAILABLE);

            AccountingService.App.get().getRentalItems(filterParametrs, new AsyncCallback<NewProduct[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(NewProduct[] results) {
                    product.clear();
                    if (results != null && results.length > 0) {
                        for (NewProduct result : results) {
                            product.addItem(new SelectItem(result.getObjectId(), result.getItemName()));
                        }
                        productField.setVisible(true);
                    } else {
                        productField.setVisible(false);
                        Info.show(accountingStrings.noItemFound(), Info.Type.INFO);
                    }

                }
            });
        } else {
            product.clear();
            productField.setVisible(false);
        }
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
