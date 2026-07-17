package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethodsList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 5:42:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditReservationView extends View implements Constants, AccountingConstants {

    //String settings properties
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

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
    private Label cannotSelectProduct;
    //Contact form
    private Label firstname;
    private Label lastname;
    private Label email;
    private Label phone;

    private DynamicTable calcTable;

    private WfmButton2 saveAndCloseButton;
    private WfmButton2 closeButton;
    private WfmButton2 sendInvoiceButton;

    private boolean saveAndClose = false;
    private boolean sendInvoiceClicked = false;

    private ReservationItem reservationItem = new ReservationItem();
    private final Integer objectID;
    private Integer contactID;
    private Integer selectedProductId;
    private String selectedProductName;

    private ShippingMethodsList shippingList;
    private NewProduct[] productList;

    public EditReservationView(Integer objectID) {
        super("edit", wfmStrings.editReservation());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        initForm();
        loadData();
        return null;
    }

    private void initForm() {
        form = new WfmForm();
        form.setStyleName("padding10");
        form.setWidth("100%");
        form.setLabelSize("200px");

        rentalDate = new DateTimePicker();
        rentalDate.startDate.addValueChangeHandler(dateValueChangeEvent -> loadProducts());

        rentalDate.dueDate.addValueChangeHandler(dateValueChangeEvent -> loadProducts());

        rentalDate.startTime.addValueChangeHandler(stringValueChangeEvent -> loadProducts());

        rentalDate.endTime.addValueChangeHandler(stringValueChangeEvent -> loadProducts());

        shippingMethod = new DataListBox();
        shippingMethod.addStyleName(DEFAULT_WIDTH);
        shippingMethod.addValueChangeHandler(changeEvent -> calculate());

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);

        category = new DataListBox();
        category.addStyleName(DEFAULT_WIDTH);
        category.addValueChangeHandler(changeEvent -> loadProducts());

        product = new WfmDropdown();
        product.addStyleName(DEFAULT_WIDTH);
        product.addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                calculate();
            }

            @Override
            public void saveNewItem() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        cannotSelectProduct = new Label();

        form.addTitleField(wfmStrings.reservationInformation(), true);
        form.addHorizontalLine();
        fromDateField = form.addField(wfmStrings.fromDate(), new Widget[]{rentalDate.startDate, rentalDate.startTime}, true);
        toDateField = form.addField(wfmStrings.toDate(), new Widget[]{rentalDate.dueDate, rentalDate.endTime}, true);
        shippingMethodField = form.addField(accountingStrings.shippingMethod(), shippingMethod);
        statusField = form.addField(wfmStrings.status(), status, true);
        categoryField = form.addField(accountingStrings.rentalGroup(), category, true);
        productField = form.addField(accountingStrings.rentalItem(), product, true);
        productField.setVisible(false);
        form.addField(null, cannotSelectProduct, false);
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

        DynamicTableColumn[] columns = new DynamicTableColumn[2];
        columns[0] = new DynamicTableColumn(wfmStrings.name(), "name", 100);
        columns[1] = new DynamicTableColumn(wfmStrings.value(), "value", 100);

        calcTable = new DynamicTable(columns, false);
        calcTable.setHeight("20px");

        drawButtonsPanel();

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(new HTML("<div style='width:270px'></div>"));
        hPanel.add(calcTable);
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

        sendInvoiceButton = new WfmButton2(accountingStrings.saveAndSendInovice(), clickEvent -> {
            setEnabledButtons(false);
            sendInvoice();
        });

        buttonsPanel.add(saveAndCloseButton);
        buttonsPanel.add(sendInvoiceButton);
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
        if (sendInvoiceButton != null) {
            sendInvoiceButton.setEnabled(b);
        }
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
                        rentalDate.setStartDate(reservationItem.getFromDate());
                        rentalDate.setDueDate(reservationItem.getToDate());
                        rentalDate.startTime.setValue(DateUtils.getTimeFormatShort(reservationItem.getFromDate()));
                        rentalDate.endTime.setValue(DateUtils.getTimeFormatShort(reservationItem.getToDate()));
                        rentalDate.startTime.setVisible(true);
                        rentalDate.endTime.setVisible(true);

                        InvoiceService.App.get().getShippingMethodList(new AbstractAsyncCallback<ShippingMethodsList>() {
                            @Override
                            public void success(ShippingMethodsList result) {
                                shippingList = result;
                                if (result != null && result.getResults() != null) {
                                    shippingMethod.setItems(result.getResults());
                                }
                                if (reservationItem.getShippingMethodId() != null) {
                                    shippingMethod.setSelected(reservationItem.getShippingMethodId());
                                }
                            }
                        });

                        status.setItems(new SelectItem[]{new SelectItem(RESERVATION_STATUS_PENDING, wfmStrings.pending()),
                                new SelectItem(RESERVATION_STATUS_RESERVED, accountingStrings.reserved()),
                                new SelectItem(RESERVATION_STATUS_STARTED, accountingStrings.started()),
                                new SelectItem(RESERVATION_STATUS_CLOSED, wfmStrings.closed()),
                                new SelectItem(RESERVATION_STATUS_CANCELED, accountingStrings.canceled())});

                        status.setSelected(reservationItem.getStatus());
                        selectedProductId = reservationItem.getItemId();
                        selectedProductName = reservationItem.getItemName();
                        AccountingService.App.get().getCategoriesAsSelectItem(new AsyncCallback<SelectItem[]>() {
                            public void onFailure(Throwable caught) {
                                GWT.log(caught.getMessage());
                            }

                            public void onSuccess(SelectItem[] result) {
                                category.setItems(result);

                                category.setSelected(reservationItem.getCategoryId());
                                loadProducts();
                            }
                        });

                        if (reservationItem.getContact() != null) {
                            contactID = reservationItem.getContact().getId();
                            firstname.setText(reservationItem.getContact().getFirstname());
                            lastname.setText(reservationItem.getContact().getLastname());
                            email.setText(reservationItem.getContact().getEmail());
                            phone.setText(reservationItem.getContact().getPhone());
                        }

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
        reservationItem.setFromDate(rentalDate.getStartDate());
        reservationItem.setToDate(rentalDate.getDueDate());
        reservationItem.setShippingMethodId(shippingMethod.getSelectedId());
        reservationItem.setStatus(status.getSelectedId());
        reservationItem.setCategoryId(category.getSelectedId());
        reservationItem.setItemId(product.getSelectedId());

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

//        if ((firstnameField != null) && !Validation.validateTextBoxRequired(firstname, firstnameField)) {
//            error = true;
//        }
//
//        if ((lastnameField != null) && !Validation.validateTextBoxRequired(lastname, lastnameField)) {
//            error = true;
//        }
//
//        if ((emailField != null) && !Validation.validateEmailRequired(email, emailField)) {
//            error = true;
//        }

        return !error;
    }

    private void loadProducts() {
        if (category.getSelectedId() != null && category.getSelectedId() > 0) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setCategoryID(category.getSelectedId());
            filterParametrs.setStartDate(rentalDate.getStartDate());
            filterParametrs.setEndDate(rentalDate.getDueDate());
            filterParametrs.setIgnoreID(objectID);
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

                        if (selectedProductId != null && !product.contain(selectedProductId)) {
                            cannotSelectProduct.setText(accountingMessages.thereWasReservedANonAvialableItem(selectedProductName));
                        } else {
                            product.setSelected(selectedProductId);
                        }
                        productField.setVisible(true);

                    } else {
                        productField.setVisible(false);
                        Info.show(accountingStrings.noItemFound(), Info.Type.INFO);
                    }
                    productList = results;
                    calculate();
                }
            });
        } else {
            product.clear();
            productField.setVisible(false);
            calculate();
        }
    }

    private void calculate() {
        calcTable.clear();
        BigDecimal subtotal = new BigDecimal(0);
        BigDecimal shipping = new BigDecimal(0);
        Integer rentalPeriod = 0;

        if (product.getSelectedId() != null && product.getSelectedId() > 0) {
            for (NewProduct p : productList) {
                if (p.getObjectId().equals(product.getSelectedId())) {
                    subtotal = p.getRentalRate();
                    rentalPeriod = p.getRentalPeriod();
                    break;
                }
            }
        }


        if (shippingMethod.getSelectedId() != null && shippingMethod.getSelectedId() > 0) {
            for (ShippingMethod sh : shippingList.getResults()) {
                if (sh.getId().equals(shippingMethod.getSelectedId())) {
                    shipping = sh.getPrice();
                    break;
                }
            }
        }

        long millisecond = rentalDate.getDueDate().getTime() - rentalDate.getStartDate().getTime();
        long qty = 0;

        if (PRODUCT_RENTAL_PERIOD_HOUR.equals(rentalPeriod)) {
            qty = millisecond / (60 * 60 * 1000);
        } else if (PRODUCT_RENTAL_PERIOD_DAY.equals(rentalPeriod)) {
            qty = millisecond / (24 * 60 * 60 * 1000);
        } else if (PRODUCT_RENTAL_PERIOD_WEEK.equals(rentalPeriod)) {
            qty = millisecond / (7 * 24 * 60 * 60 * 1000);
        } else if (PRODUCT_RENTAL_PERIOD_MONTH.equals(rentalPeriod)) {
            qty = millisecond / (30L * 24 * 60 * 60 * 1000);
        } else {
            qty = 0;
        }
        BigDecimal total = subtotal.multiply(BigDecimal.valueOf(qty)).add(shipping);
        calcTable.addRow(getItems(new Label(wfmStrings.subtotal()), new HTML("<b>" + subtotal + "</b>")));
        calcTable.addRow(getItems(new Label(accountingStrings.shippingMethod()), new HTML("<b>" + shipping + "</b>")));
        calcTable.addRow(getItems(new HTML("<b>" + wfmStrings.total() + "</b>"), new HTML("<b>" + total + "</b>")));


    }

    private Widget[] getItems(Widget item1, Widget item2) {
        Widget[] items = new Widget[2];
        items[0] = item1;
        items[1] = item2;
        return items;
    }

    private void sendInvoice() {
        sendInvoiceClicked = true;
        save();
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";  //To change body of implemented methods use File | Settings | File Templates.
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
