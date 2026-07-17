package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.core.client.rpc.StatusServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceUser;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.table.Table;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.table.TableItem;
import com.edatasite.workforce.gwt.core.client.ui.table.TableItemValue;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 1:30:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMarketPlaceUsersView extends Composite implements Errors {

    private final StatusServiceAsync statusService = StatusService.App.get();

    private final String errorStyle = "x-form-invalid";
    private final String nameStyle = "search-textbox";

    private VerticalPanel panel = new VerticalPanel();
    private HorizontalPanel buttonPanel = new HorizontalPanel();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DialogBox dialogBox;
    private Table table;
    private TextBox phone;
    private TextBox company;
    private DataListBox country;
    private HTML phoneNumber;
    private HTML companyName;
    private HTML countryName;

    private boolean showPopup;

    public GoogleMarketPlaceUsersView(boolean showPopup) {
        this.showPopup = showPopup;

        getData();

        if (!showPopup) {
            initWidget(panel);
        }
    }

    private void getData() {
        if (showPopup) {
            statusService.getGoogleMarketPlaceUsersFirstTime(new AbstractAsyncCallback<GoogleMarketPlaceUser>() {
                @Override
                public void success(GoogleMarketPlaceUser user) {

                    if (user != null) {
                        build(user);
                    }
                }
            });
        } else {
            statusService.getGoogleMarketPlaceUsers(new AbstractAsyncCallback<GoogleMarketPlaceUser>() {
                @Override
                public void success(GoogleMarketPlaceUser user) {
                    if (user != null) {
                        build(user);
                    }
                }

                @Override
                public void failure(Throwable throwable) {
                    panel.setSpacing(10);
                    panel.add(new HTML("Seems like you have revoked access to our application."));
                    panel.add(new HTML("Please sign in to your Google App domain account on https://www.google.com/a/" + throwable.getMessage() + "/"));
                    panel.add(new HTML("and enable access to " + Utils.getProductName() + "  application."));
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private void build(GoogleMarketPlaceUser users) {
        TableColumn[] columns = new TableColumn[4];
        columns[0] = new TableColumn("firstname", "First Name", 120);
        columns[1] = new TableColumn("lastname", "Last Name", 120);
        columns[2] = new TableColumn("email", "E-mail", 110);
        columns[3] = new TableColumn("photo", "Photo", 50);

        TableItem[] items = new TableItem[users.getEmployees().length];
        int index = 0;
        for (GoogleMarketPlaceEmployee user : users.getEmployees()) {
            TableItemValue[] values = new TableItemValue[4];
            values[0] = new TableItemValue<>(user.getFname());
            values[1] = new TableItemValue<>(user.getLname());
            values[2] = new TableItemValue<>(user.getEmail());
            values[3] = new TableItemValue<>("<img width='48px' src='" + user.getPhotoURL() + "'>");

            items[index++] = new TableItem(values);
        }

        table = new Table(columns);
        table.add(items);

        for (int i = 0; i < table.getItemCount(); i++) {
            final TableItem item = table.getItem(i);
            item.getCheckbox().addClickHandler(event -> item.getCheckbox().removeStyleName(errorStyle));
        }

        Button saveButton = new Button("Save", (ClickHandler) event -> saveEmployees());

        phone = new TextBox();
//        phone.addKeyPressHandler(new KeyPressHandler() {
//            public void onKeyPress(KeyPressEvent event) {
//                char key = event.getCharCode();
//                if ((!Character.isDigit(key))
//                        && (key != '(') && (key != ')')
//                        && (key != '+') && (key != '-')
//                        && (key != (char) KeyCodes.KEY_TAB)
//                        && (key != (char) KeyCodes.KEY_BACKSPACE)
//                        && (key != (char) KeyCodes.KEY_DELETE)
//                        && (key != (char) KeyCodes.KEY_ENTER)
//                        && (key != (char) KeyCodes.KEY_HOME)
//                        && (key != (char) KeyCodes.KEY_END)
//                        && (key != (char) KeyCodes.KEY_LEFT)
//                        && (key != (char) KeyCodes.KEY_UP)
//                        && (key != (char) KeyCodes.KEY_RIGHT)
//                        && (key != (char) KeyCodes.KEY_DOWN)) {
//                    ((TextBox) event.getSource()).cancelKey();
//                }
//            }
//        });
        phone.addFocusHandler(sender -> phone.removeStyleName(errorStyle));

        company = new TextBox();
        if (users.getCompanyName() != null) {
            company.removeStyleName(nameStyle);
            company.setText(users.getCompanyName());
        }

        company.addFocusHandler(sender -> company.removeStyleName(errorStyle));

        country = new DataListBox();
        country.setWidth("150px");
        /*country.setNullLabel("Country");*/
        country.addFocusHandler(event -> country.removeStyleName(errorStyle));

        CommonService.App.get().getCountries(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                country.setItems(items);
            }
        });
        companyName = new HTML("<b class=customTitle>Company Name<font color='red'>*</font>:</b>");
        phoneNumber = new HTML("<b class=customTitle>Company Phone<font color='red'>*</font>:</b>");
        countryName = new HTML("<b class=customTitle>Country<font color='red'>*</font>:</b>");

        HorizontalPanel mandatoryHtml = new HorizontalPanel();
        mandatoryHtml.setWidth("100%");
        mandatoryHtml.setSpacing(4);
        mandatoryHtml.add(companyName);
        mandatoryHtml.add(phoneNumber);
        mandatoryHtml.add(countryName);

        HorizontalPanel mandatory = new HorizontalPanel();
        mandatory.setWidth("100%");
        mandatory.setSpacing(4);
        mandatory.add(company);
        mandatory.add(phone);
        mandatory.add(country);

        panel.setSpacing(7);
        if (showPopup) {
            panel.add(mandatoryHtml);
            panel.add(mandatory);
        }
        panel.add(table);
        buttonPanel.setSpacing(2);
        buttonPanel.setStyleName("workforce");
        buttonPanel.add(saveButton);
        buttonPanel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(buttonPanel);
        panel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

        if (showPopup) {
            table.setSize(510, 333);

            dialogBox = new DialogBox();
            dialogBox.setText("Please provide additional information and import your google apps domain users");
            Button closeButton = new Button("Close", (ClickHandler) event -> dialogBox.hide());
            /*exportPanel.add(closeButton);
            exportPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);*/
            dialogBox.setWidget(panel);
//            dialogBox.setSize("500px", "530px");
            dialogBox.show();
        } else {
            table.setSize("560px", "100%");
        }
    }

    private void saveEmployees() {
        if (showPopup) {
            if (!validate()) {
                return;
            }
        }

        GoogleMarketPlaceUser result = new GoogleMarketPlaceUser();

        if (showPopup) {
            result.setCompanyName(company.getText());
            result.setCompanyPhone(phone.getText());
            result.setCountryID(country.getSelectedId());
        }

        ArrayList<GoogleMarketPlaceEmployee> employees = new ArrayList<>();
        for (int i = 0; i < table.getItemCount(); i++) {
            TableItem item = table.getItem(i);
            if (item.getCheckbox().getValue()) {
                GoogleMarketPlaceEmployee user = new GoogleMarketPlaceEmployee();
                user.setFname(item.getValues()[0].getValue().toString());
                user.setLname(item.getValues()[1].getValue().toString());
                user.setEmail(item.getValues()[2].getValue().toString());

                employees.add(user);
            }
        }
        if (showPopup) {
            result.setEmployees(employees.toArray(new GoogleMarketPlaceEmployee[]{}));
            saveGoogleMarketPlaceUsers(result);
        } else {
            if (employees.size() > 0) {
                result.setEmployees(employees.toArray(new GoogleMarketPlaceEmployee[]{}));
                saveGoogleMarketPlaceUsers(result);
            } else {
                Info.show("Please select employee", Info.Type.INFO);
            }
        }
    }

    private void saveGoogleMarketPlaceUsers(final GoogleMarketPlaceUser result1) {
        LoadingPanel.loading(true);
        statusService.saveEmployees(result1, showPopup, new AbstractAsyncCallback<Integer[]>() {
            public void success(Integer[] result) {
                LoadingPanel.loading(false);
                Widget sender;

                if (showPopup) {
                    sender = dialogBox.getWidget();
                    dialogBox.hide();
                } else {
                    sender = GoogleMarketPlaceUsersView.this;
                }
                boolean errors = false;
                final DialogBox dialogBox = new DialogBox();
                dialogBox.setHTML("<b class=customTitle>" + wfmStrings.notice() + "</b>");
                dialogBox.setAnimationEnabled(true);
                dialogBox.setGlassEnabled(true);
                VerticalPanel panel = new VerticalPanel();
                panel.setSpacing(10);
                panel.setStyleName("workforce");
                HTML messageHtml = new HTML();
                Button closeButton = new Button(wfmStrings.ok());
                closeButton.addClickHandler(event -> dialogBox.hide());
                panel.add(messageHtml);
                panel.setCellHorizontalAlignment(messageHtml, HasHorizontalAlignment.ALIGN_LEFT);
                panel.add(closeButton);
                panel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
                dialogBox.setWidget(panel);
                for (int i = 0; i < result.length; i++) {
                    if (result[i] == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                        errors = true;
                        setErrorValue(result[i], result1.getEmployees()[i]);
                        messageHtml.setHTML(wfmStrings.sorryEmailWithThisNameAlreadyExists());
                        dialogBox.show();
                        break;

                    } else if (result[i] == EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST) {
                        errors = true;
                        setErrorValue(result[i], result1.getEmployees()[i]);
                        messageHtml.setHTML("Invalid Email Address");
                        dialogBox.show();
                        break;
                    } else if (result[i] == CAN_NOT_CREATE_EMPLOYEE) {
                        errors = true;
                        setErrorValue(result[i], result1.getEmployees()[i]);
                        messageHtml.setHTML(wfmStrings.canNotCreateAnEmployee());
                        dialogBox.show();
                        break;
                    }
                }
                if (!errors) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, result, sender);
                    Info.show("Company with employees have been saved successfully", Info.Type.INFO);
                }
            }
        });
    }

    private void setErrorValue(Integer result, GoogleMarketPlaceEmployee users) {
        for (int i = 0; i < table.getItemCount(); i++) {
            TableItem item = table.getItem(i);
            if (result != null) {
                if (item.getCheckbox().getValue()) {
                    if (item.getValues()[2].getValue().toString().equals(users.getEmail())) {
                        item.getCheckbox().setStyleName(errorStyle);
                    } else {
                        table.removeItem(item);
                    }
                }
            }
        }
    }

    private boolean validate() {
        boolean correct = true;

        if (phone.getText() == null || phone.getText().equals("") || phone.getStyleName().contains(nameStyle)) {
            phone.addStyleName(errorStyle);
            correct = false;
        }

        if (company.getText() == null || company.getText().equals("") || company.getStyleName().contains(nameStyle)) {
            company.addStyleName(errorStyle);
            correct = false;
        }

        if (country.getSelectedItem() == null || country.getSelectedItem().getName().equals(country.getNullLabel())) {
            country.addStyleName(errorStyle);
            correct = false;
        }

        return correct;
    }
}
