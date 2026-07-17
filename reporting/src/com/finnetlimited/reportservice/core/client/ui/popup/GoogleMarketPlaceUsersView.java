package com.finnetlimited.reportservice.core.client.ui.popup;

import com.finnetlimited.reportservice.core.client.gwtrpc.*;
import com.finnetlimited.reportservice.core.client.ui.table.Table;
import com.finnetlimited.reportservice.core.client.ui.table.TableColumn;
import com.finnetlimited.reportservice.core.client.ui.table.TableItem;
import com.finnetlimited.reportservice.core.client.ui.table.TableItemValue;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 20:38:14
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMarketPlaceUsersView extends Composite {

    private final StatusServiceAsync statusService = StatusService.App.get();

    private final String errorStyle = "x-form-invalid";
    private final String nameStyle = "search-textbox";
    private boolean showPopup;

    private VerticalPanel panel = new VerticalPanel();
    private DialogBox dialogBox;
    private Table table;
    private TextBox phone;
    private TextBox company;
    private HTML phoneNumber;
    private HTML companyName;
    private HTML countryName;
    private HorizontalPanel buttonPanel = new HorizontalPanel();

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

                @Override
                public void failure(Throwable throwable) {
                    Window.alert("To be able to add your domain users you should register Reporting System in your Google App domain!");
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
                    Window.alert("To be able to add your domain users you should register Reporting System in your Google App domain!");
//                    panel.setSpacing(10);
//                    panel.add(new HTML("Please enable google apps provisioning API"));
//                    panel.add(new HTML("1. Sign in to your Google App domain account on https://www.google.com/a/" + throwable.getMessage() + "/"));
//                    panel.add(new HTML("2. Then click on manage this domain link so you could update your settings."));
//                    panel.add(new HTML("3. Now you are on dashboard section page and under service settings choose the marketplace link."));
//                    panel.add(new HTML("4. Finally it will open the page on which you can set up the settings on APP status and Data Access if it disabled."));
//                    panel.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;You just click on Enable link on app status consequently on grant data access link on data access."));
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private void build(GoogleMarketPlaceUser users) {
        TableColumn[] columns = new TableColumn[3];
        columns[0] = new TableColumn("firstname", "First Name", 120);
        columns[1] = new TableColumn("lastname", "Last Name", 120);
        columns[2] = new TableColumn("email", "E-mail", 110);

        TableItem[] items = new TableItem[users.getEmployees().length];
        int index = 0;
        for (NewEmployee user : users.getEmployees()) {
            TableItemValue[] values = new TableItemValue[3];
            values[0] = new TableItemValue<>(user.getFname());
            values[1] = new TableItemValue<>(user.getLname());
            values[2] = new TableItemValue<>(user.getEmail());

            items[index++] = new TableItem(values);
        }

        table = new Table(columns);
        table.add(items);

        Button saveButton = new Button("Save", (ClickHandler) event -> saveEmployees());

//        phone = new TextBox();
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
//        phone.addFocusHandler(new FocusHandler() {
//            public void onFocus(FocusEvent sender) {
//                phone.removeStyleName(errorStyle);
//            }
//        });
//
//        company = new TextBox();
//        if (users.getCompanyName() != null) {
//            company.removeStyleName(nameStyle);
//            company.setText(users.getCompanyName());
//        }
//
//        company.addFocusHandler(new FocusHandler() {
//            public void onFocus(FocusEvent sender) {
//                company.removeStyleName(errorStyle);
//            }
//        });
//
//        country = new DataListBox();
//        country.setWidth("150px");
//        /*country.setNullLabel("Country");*/
//        country.addFocusHandler(new FocusHandler() {
//            public void onFocus(FocusEvent event) {
//                country.removeStyleName(errorStyle);
//            }
//        });
//
//        statusService.getCountries(new AbstractAsyncCallback<List<SelectItem>>() {
//            public void success(List<SelectItem> items) {
//                country.setItems(items);
//            }
//        });
//        companyName = new HTML("<b class=customTitle>Company Name<font color='red'>*</font>:</b>");
//        phoneNumber = new HTML("<b class=customTitle>Company Phone<font color='red'>*</font>:</b>");
//        countryName = new HTML("<b class=customTitle>Country<font color='red'>*</font>:</b>");
//
//        HorizontalPanel mandatoryHtml = new HorizontalPanel();
//        mandatoryHtml.setWidth("100%");
//        mandatoryHtml.setSpacing(4);
//        mandatoryHtml.add(companyName);
//        mandatoryHtml.add(phoneNumber);
//        mandatoryHtml.add(countryName);
//
//        HorizontalPanel mandatory = new HorizontalPanel();
//        mandatory.setWidth("100%");
//        mandatory.setSpacing(4);
//        mandatory.add(company);
//        mandatory.add(phone);
//        mandatory.add(country);
//
//        panel.setSpacing(7);
//        if (showPopup) {
//            panel.add(mandatoryHtml);
//            panel.add(mandatory);
//        }
        panel.add(table);
        buttonPanel.setSpacing(2);
        buttonPanel.setStyleName("workforce");
        buttonPanel.add(saveButton);
        buttonPanel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(buttonPanel);
        panel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

        if (showPopup) {
            table.setSize(450, 333);

            dialogBox = new DialogBox();
            dialogBox.setText("Please provide additional information and import your google apps domain users");
            Button closeButton = new Button("Close", (ClickHandler) event -> dialogBox.hide());
            /*exportPanel.add(closeButton);
            exportPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);*/
            dialogBox.setWidget(panel);
//            dialogBox.setSize("500px", "530px");
            dialogBox.show();
        } else {
            table.setSize(500, 360);
        }
    }

    private void saveEmployees() {
//        if (showPopup) {
//            if (!validate()) {
//                return;
//            }
//        }

        GoogleMarketPlaceUser result = new GoogleMarketPlaceUser();

//        if (showPopup) {
//            result.setCompanyName(company.getText());
//            result.setCompanyPhone(phone.getText());
//            result.setCountryID(country.getSelectedId());
//        }

        ArrayList<NewEmployee> employees = new ArrayList<>();
        for (int i = 0; i < table.getItemCount(); i++) {
            TableItem item = table.getItem(i);
            if (item.getCheckbox().getValue()) {
                NewEmployee user = new NewEmployee();
                user.setFname(item.getValues()[0].getValue().toString());
                user.setLname(item.getValues()[1].getValue().toString());
                user.setEmail(item.getValues()[2].getValue().toString());

                employees.add(user);
            }
        }
        if (showPopup) {
            result.setEmployees(employees.toArray(new NewEmployee[]{}));
            saveGoogleMarketPlaceUsers(result);
        } else {
            if (employees.size() > 0) {
                result.setEmployees(employees.toArray(new NewEmployee[]{}));
                saveGoogleMarketPlaceUsers(result);
            } else {
                // Info.show("", "Please select employee", "");
                Window.alert("Please select employee");
            }
        }
    }

    private void saveGoogleMarketPlaceUsers(GoogleMarketPlaceUser result) {
        //BillboardPanel.get().show("Saving Employees, please wait...");
        statusService.saveEmployees(result, showPopup, new AbstractAsyncCallback<Integer[]>() {
            public void success(Integer[] result) {
                //BillboardPanel.get().hide();
                Widget sender;

                if (showPopup) {
                    sender = dialogBox.getWidget();
                    dialogBox.hide();
                } else {
                    sender = GoogleMarketPlaceUsersView.this;
                }
                Window.alert("Company with employees have been successfully saved");
                //WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, result, sender);
                // Info.show("", "Company with employees have been successfully saved", "");
            }
        });
    }

//    private boolean validate() {
//        boolean correct = true;
//
//        if (phone.getText() == null || phone.getText().equals("") || phone.getStyleName().contains(nameStyle)) {
//            phone.addStyleName(errorStyle);
//            correct = false;
//        }
//
//        if (company.getText() == null || company.getText().equals("") || company.getStyleName().contains(nameStyle)) {
//            company.addStyleName(errorStyle);
//            correct = false;
//        }
//
//        if (country.getSelectedItem() == null || country.getSelectedItem().getName().equals(country.getNullLabel())) {
//            country.addStyleName(errorStyle);
//            correct = false;
//        }
//
//        return correct;
//    }
}
