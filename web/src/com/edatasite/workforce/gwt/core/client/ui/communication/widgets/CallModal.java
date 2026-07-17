package com.edatasite.workforce.gwt.core.client.ui.communication.widgets;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ContactTypeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.twilio.Device;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.communication.CallState;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.CallCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.TD;
import gwt.material.design.client.ui.html.Table;
import gwt.material.design.client.ui.html.TableHeadCell;
import gwt.material.design.client.ui.html.TableRow;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_CONTACT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_CONTACT_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ADD_NEW_CASE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ADD_NEW_LEAD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_EDIT_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_LEAD_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_TASKS_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_TASKS_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_CANDIDATE;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_CLIENT_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_CRM_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_EMPLOYEE_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_LEAD_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_STUDENT_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_SUPPLIER_CONTACT;

public class CallModal extends Composite {

    @UiTemplate("com.edatasite.workforce.gwt.core.client.ui.communication.widgets.CallModal.ui.xml")
    interface CallModalUiBinder extends UiBinder<HTMLPanel, CallModal> {
    }

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();

    @UiField
    HTMLPanel mainPanel;
    @UiField
    InputElement phoneNumberInput;
    @UiField
    HTMLPanel padButtons;
    @UiField
    HTMLPanel callActionsIcon;
    @UiField
    MaterialLink moreIcon;
    @UiField
    HTMLPanel toLabel;
    @UiField
    HTMLPanel callStats;
    @UiField
    Table callInfoTable;
    @UiField
    WfmButton2 endCallButton;
    @UiField
    WfmButton2 muteButton;
    @UiField
    WfmButton2 keypadButton;
    @UiField
    HTMLPanel actionButtons;
    @UiField
    HTMLPanel callPanel;
    @UiField
    TextArea note;
    @UiField
    HTMLPanel dialPad;
    @UiField
    HTMLPanel dialText;
    @UiField
    Label notelabel;


    private ContactDetailsItem contactDetailsItem;
    private final String phoneNumber;
    private Date callStart;
    private final CallCommand callCommand;
    private Appointment appointmentItem;


    public CallModal(String username, ContactDetailsItem contactDetailsItem, String phoneNumber, CallCommand callCommand) {
        this.callCommand = callCommand;
        this.contactDetailsItem = contactDetailsItem;
        this.phoneNumber = phoneNumber;

        CallModalUiBinder callModalUiBinder = GWT.create(CallModalUiBinder.class);
        initWidget(callModalUiBinder.createAndBindUi(this));

        mainPanel.getElement().getStyle().setBottom(0, Style.Unit.PX);
        mainPanel.getElement().getStyle().setRight(0, Style.Unit.PX);

        SvgIcon callIcon = new SvgIcon(SvgEnum.callAction);
        callActionsIcon.add(callIcon);
        drawPadButtons(username);
        if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && contactDetailsItem.getContactType().equals(TYPE_LEAD_CONTACT)) {
            drawCallInfoTableForLead();
        } else if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && (contactDetailsItem.getContactType().equals(TYPE_EMPLOYEE_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_CLIENT_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_SUPPLIER_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_CRM_CONTACT))) {
            if (contactDetailsItem.getOpportunity() != null) {
                drawCallInfoTableForOpportunity();
            } else if (contactDetailsItem.getEmployee() != null) {
                drawCallInfoTableForEmployee();

            } else {
                drawCallInfoTableForContact();
            }
        } else if (contactDetailsItem != null && contactDetailsItem.getOwner() != null && contactDetailsItem.getContactType().equals(TYPE_ACCOUNT)) {
            drawCallInfoTableForAccount();
        } else if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && contactDetailsItem.getContactType().equals(TYPE_CANDIDATE)) {
            drawCallInfoTableForCandidate();
        } else {
            drawCallInfoTable();
        }
        note.getElement().setPropertyString("placeholder", wfmStrings.doNotMiss());
        note.addClickHandler(clickEvent -> note.setFocus(true));
        notelabel.setText(wfmStrings.addNote());
        callStats.getElement().setInnerHTML(wfmStrings.calling());
        dialText.getElement().setInnerHTML(wfmStrings.callYourCustomers());

        toLabel.getElement().setInnerHTML(phoneNumber);
        if (this.contactDetailsItem != null) {
            /*if (this.contactDetailsItem.getName() != null && !this.contactDetailsItem.getName().isEmpty()) {
                toLabel.getElement().setInnerHTML(this.contactDetailsItem.getName());
            }*/
            if (contactDetailsItem.getId() != null) {
                HTML callerName = new HTML(contactDetailsItem.getName() != null ? "<a href=\"javascript:\">" + contactDetailsItem.getName() + "</a>" : "");
                callerName.addClickHandler(click -> {
                    //if contact type is not null then its contact
                    onClickContact(contactDetailsItem);

                });
                toLabel.clear();
                toLabel.getElement().setInnerHTML("");
                toLabel.add(callerName);
            } else {
                toLabel.getElement().setInnerHTML(this.contactDetailsItem.getName());
            }
        }


        endCallButton.setText(wfmStrings.end());
        endCallButton.addClickHandler(clickEvent -> {
            callCommand.disconnect(username);
            mainPanel.setStyleName("wg_dial wg_dial--conversation");
            if (!mainPanel.getElement().getStyle().getRight().isEmpty()) {
                mainPanel.getElement().getStyle().setRight(0, Style.Unit.PX);
            } else {
                mainPanel.getElement().getStyle().setLeft(Window.getClientWidth() - getOffsetWidth(), Style.Unit.PX);
            }
        });
        muteButton.add(new SvgIcon(SvgEnum.microphone));
        muteButton.addStyleName("mic");
        muteButton.setEnabled(false);
        muteButton.addClickHandler(clickEvent -> {
            if (callCommand.mute(username)) {
                muteButton.removeStyleName("mic-on");
                muteButton.addStyleName("mic-off");
            } else {
                muteButton.removeStyleName("mic-off");
                muteButton.addStyleName("mic-on");
            }
        });

        SvgIcon keypadIcon = new SvgIcon(SvgEnum.keypad);
        keypadButton.add(keypadIcon);
        keypadButton.addClickHandler(clickEvent -> {
            phoneNumberInput.setValue("");
            if (mainPanel.getStyleName().contains("wg_dial--keypadOn")) {
                mainPanel.removeStyleName("wg_dial--keypadOn");
                if (!mainPanel.getElement().getStyle().getRight().isEmpty()) {
                    mainPanel.getElement().getStyle().setRight(0, Style.Unit.PX);
                } else {
                    mainPanel.getElement().getStyle().setLeft(mainPanel.getElement().getOffsetLeft() + dialPad.getOffsetWidth(), Style.Unit.PX);
                }
            } else {
                mainPanel.addStyleName("wg_dial--keypadOn");
                if (!mainPanel.getElement().getStyle().getRight().isEmpty()) {
                    mainPanel.getElement().getStyle().setRight(dialPad.getOffsetWidth(), Style.Unit.PX);
                } else {
                    mainPanel.getElement().getStyle().setLeft(mainPanel.getElement().getOffsetLeft() - dialPad.getOffsetWidth(), Style.Unit.PX);
                }
            }
        });

        open();
    }

    private void drawPadButtons(String username) {
        for (int i = 1; i < 15; i++) {
            padButtons.add(createPadButton(username, i));
        }
    }

    private Div createPadButton(String username, int number) {
        Div div = new Div("wg_dial__pad-button");
        if (number == 13 || number == 14) {
            div.addStyleName("wg_dial__pad-button--function");

            Icon icon = new Icon();
            Span span = new Span();


            icon.setStyleName(number == 13 ? "ficon--phone" : "ficon--sms");

            span.add(icon);
            div.add(span);
            if (number == 13) {
                div.addClickHandler((clickEvent) -> {
                    if (!phoneNumberInput.getValue().equals("") && phoneNumberInput.getValue().startsWith("#")) {
                        callCommand.forwardCall(username, phoneNumberInput.getValue().substring(1), null);
                        callCommand.disconnect(username);
                    } else {
                        callCommand.call(username, phoneNumberInput.getValue(), contactDetailsItem);
                    }
                });

            } else if (contactDetailsItem != null && contactDetailsItem.getSmsCommand() != null) {
                div.addClickHandler(clickEvent -> contactDetailsItem.getSmsCommand().execute(note != null ? note.getText() : null));
            }
        } else if (number == 10 || number == 12) {
            Span span = new Span();
            span.setText(number == 10 ? "*" : "#");

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + span.getText());
                callCommand.sendDigits(username, span.getText());
            });
        } else if (number == 11) {
            Span span = new Span();
            span.setText("0 +");

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + "0");
                callCommand.sendDigits(username, "0");
            });
            div.addDoubleClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
//                value = value.replace(value.substring(value.length()-1), "");
                value = value.substring(0, value.length() - 2);
                phoneNumberInput.setValue(value + "+");
                callCommand.sendDigits(username, "+");
            });
        } else {
            Span span = new Span();
            span.setText(String.valueOf(number));

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + span.getText());
                callCommand.sendDigits(username, span.getText());
            });
        }
        return div;
    }

    private void drawCallInfoTable() {
        callInfoTable.clear();
        callInfoTable.add(getContactOwnerInfoTableRowAsHtml());
//        callInfoTable.add(drawCallInfoTableRow(wfmStrings.owner(), contactDetailsItem != null ? contactDetailsItem.getOwner() : null));
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
//        callInfoTable.add(drawCallInfoTableRow(wfmStrings.mobile(), contactDetailsItem != null ? contactDetailsItem.getPhoneNumber() : null));
//        getLocationByNumber(phoneNumber);
    }

    private void drawCallInfoTableForLead() {
        callInfoTable.clear();
        callInfoTable.add(getContactOwnerInfoTableRowAsHtml());
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.status(), contactDetailsItem != null ? contactDetailsItem.getStatus() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
    }

    private void drawCallInfoTableForContact() {
        callInfoTable.clear();
        callInfoTable.add(getContactOwnerInfoTableRowAsHtml());
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.company(), contactDetailsItem != null ? contactDetailsItem.getCompany() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
    }

    private void drawCallInfoTableForOpportunity() {
        callInfoTable.clear();
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.owner(), contactDetailsItem.getOpportunity() != null ? contactDetailsItem.getOpportunity().getAssignee() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.stage(), contactDetailsItem.getOpportunity() != null ? contactDetailsItem.getOpportunity().getStage() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.amount(), contactDetailsItem.getOpportunity() != null ? contactDetailsItem.getOpportunity().getCurrency() + " " + contactDetailsItem.getOpportunity().getAmount() : null));
    }

    private void drawCallInfoTableForAccount() {
        callInfoTable.clear();
        callInfoTable.add(getContactOwnerInfoTableRowAsHtml());
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.industry(), contactDetailsItem.getAccountIndustry()));
    }

    private void drawCallInfoTableForCandidate() {
        callInfoTable.clear();
        callInfoTable.add(getContactOwnerInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.vacancy(), contactDetailsItem.getVacancy()));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.status(), contactDetailsItem.getStatus()));
    }

    private void drawCallInfoTableForEmployee() {
        callInfoTable.clear();
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.owner(), contactDetailsItem != null ? contactDetailsItem.getEmployee().getSupervisor() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.phone(), phoneNumber));
        callInfoTable.add(getEmailInfoTableRowAsHtml());
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.department(), contactDetailsItem != null ? contactDetailsItem.getEmployee().getDepartment() : null));
        callInfoTable.add(drawCallInfoTableRow(wfmStrings.position(), contactDetailsItem != null ? contactDetailsItem.getEmployee().getPosition() : null));
    }

    private TableRow drawCallInfoTableRow(String title, String value) {
        return new TableRow(new TableHeadCell(title), new TD(value != null ? value : "--"));
    }

    private TableRow getContactOwnerInfoTableRowAsHtml() {
        TD td = new TD();
        if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null) {
            HTML ownerName = new HTML(contactDetailsItem.getOwner() != null ? "<a href=\"javascript:\">" + contactDetailsItem.getOwner() + "</a>" : "");
            ownerName.addClickHandler(click -> {

                Utils.openURL("Hrms.html#employeeProfile|employeeProfileView/" + contactDetailsItem.getOwnerId());

            });
            td.add(ownerName);
        } else if (contactDetailsItem != null && contactDetailsItem.getAccountId() != null) {
            HTML ownerName = new HTML(contactDetailsItem.getOwner() != null ? "<a href=\"javascript:\">" + contactDetailsItem.getOwner() + "</a>" : "");
            ownerName.addClickHandler(click -> {

                Utils.openURL("Crm.html#account|summary/" + contactDetailsItem.getAccountId());

            });
            td.add(ownerName);
        } else {
            td.setText("--");
        }
        return new TableRow(new TableHeadCell(wfmStrings.owner()), td);
    }

    private TableRow getEmailInfoTableRowAsHtml() {
        TD td = new TD();
        if (contactDetailsItem != null && contactDetailsItem.getPrimaryEmail() != null) {
            HTML email = new HTML(contactDetailsItem.getPrimaryEmail() != null ? contactDetailsItem.getPrimaryEmail() : "");
            email.addClickHandler(clickEvent -> {
                if (contactDetailsItem.getCompany() != null && !contactDetailsItem.getContactType().equals(TYPE_ACCOUNT)) {
                    Utils.openURL("Crm.html#emailcomposeadd/add/" + contactDetailsItem.getPrimaryEmail() + "/" + RelationItem.getByContactType(contactDetailsItem.getContactType())
                            + "/" + contactDetailsItem.getId() + "/" + contactDetailsItem.getName() + "/account/" + contactDetailsItem.getCompanyId() + "/" + contactDetailsItem.getCompany());
                } else if (!contactDetailsItem.getContactType().equals(TYPE_ACCOUNT)) {
                    Utils.openURL("Crm.html#emailcomposeadd/add/" + contactDetailsItem.getPrimaryEmail() + "/" + RelationItem.getByContactType(contactDetailsItem.getContactType())
                            + "/" + contactDetailsItem.getId() + "/" + contactDetailsItem.getName());
                } else {
                    Utils.openURL("Crm.html#emailcomposeadd/add/" + contactDetailsItem.getPrimaryEmail() + "/CrmAccount"
                            + "/" + contactDetailsItem.getId() + "/" + contactDetailsItem.getName());
                }
            });
            td.add(email);
        } else {
            td.setText("--");
        }
        return new TableRow(new TableHeadCell(wfmStrings.email()), td);
    }

    private void getLocationByNumber(String number) {
        TwilioService.App.get().getCountryNameByPhoneNumber(number, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String s) {
                callInfoTable.add(drawCallInfoTableRow(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), s));
            }
        });
    }

    public void countCallDuration() {
        Scheduler.get().scheduleFixedDelay(() -> {
            String time;
            if (callStart != null && !CallState.FINISHED.equals(currentState)) {
                time = Utils.getLongAsMinuteAndSecond((new Date().getTime() - callStart.getTime()));
            } else {
                return false;
            }
            callStats.getElement().setInnerHTML(time);
            return true;
        }, 1000);
    }

    public void open() {
        this.removeFromParent();
        RootPanel.get().add(this);
        RootPanel.getBodyElement().addClassName("has-modal-open");
        Utils.makeDraggable(mainPanel.getElement().getId());
    }

    public void close() {
        this.removeFromParent();
        RootPanel.getBodyElement().removeClassName("has-modal-open");
    }

    CallState currentState;

    public void changeState(String username, CallState state, boolean fromTwilio) {
        note.setFocus(true);
        this.currentState = state;
        initCallMoreIcon();
        if (CallState.CONVERSATION.equals(state)) {
            mainPanel.setStyleName("wg_dial wg_dial--conversation");
            callStats.getElement().setInnerHTML(wfmStrings.connecting());
            callStart = new Date();
            actionButtons.clear();
            actionButtons.add(endCallButton);
            muteButton.setEnabled(true);
            actionButtons.add(muteButton);
            actionButtons.add(keypadButton);
            countCallDuration();
        } else if (CallState.FINISHED.equals(state)) {
            mainPanel.setStyleName("wg_dial wg_dial--finished");

            if (!mainPanel.getElement().getStyle().getRight().isEmpty()) {
                mainPanel.getElement().getStyle().setRight(0, Style.Unit.PX);
            } else {
                mainPanel.getElement().getStyle().setLeft(Window.getClientWidth() - getOffsetWidth(), Style.Unit.PX);
            }

            actionButtons.removeFromParent();
            callInfoTable.removeFromParent();
            callActionsIcon.clear();
            SvgIcon svgIcon = new SvgIcon(SvgEnum.callActionEnd);
            callActionsIcon.add(svgIcon);
            callPanel.add(this::drawCallResultsTable);
            WfmButton2 save = new WfmButton2(wfmStrings.save(), "btn-small btn--primary", clickEvent -> {
                if (fromTwilio) {
                    if (appointmentItem != null) {
                        appointmentItem.setDescription(note.getText());
                        appointmentItem.getRelations().clear();
                        if (note.getText() != null && !note.getText().equals("")) {
                            appointmentItem.setDescription(note.getText());
                        }
                    }
                    saveAppointment(appointmentItem);
                }
                //T12039 - Save notes in Lead, contacts, candidate notes
                if (note.getText() != null && !note.getText().equals("")) {
                    saveCrmNote(note.getText());
                }

                close();
            });
            WfmButton2 cancel = new WfmButton2(wfmStrings.close(), "btn-small btn--default", clickEvent -> {
                Device.getInstance().disconnectAll();
                close();
            });
            GWT.log(callStats.getElement().getInnerHTML());
            if (callStats.getElement().getInnerHTML().contains("Calling")) {
                callStats.getElement().setInnerHTML(wfmStrings.callEnded());
            }
            Div footer = Utils.div("wg_dial__call-footer");
            footer.add(save);
            footer.add(cancel);
            callPanel.add(footer);
        } else if (CallState.INCOMING.equals(state)) {
            mainPanel.setStyleName("wg_dial wg_dial--calling");

            if (!mainPanel.getElement().getStyle().getRight().isEmpty()) {
                mainPanel.getElement().getStyle().setRight(0, Style.Unit.PX);
            } else {
                mainPanel.getElement().getStyle().setLeft(Window.getClientWidth() - getOffsetWidth(), Style.Unit.PX);
            }

            actionButtons.clear();
            WfmButton2 accept = new WfmButton2(wfmStrings.answer(), "btn-small btn--success");
            accept.addClickHandler(clickEvent -> {
                accept.setEnabled(false);
                accept.setText(wfmStrings.pleaseWait());
                callCommand.accept(username);
            });
            WfmButton2 reject = new WfmButton2(wfmStrings.reject(), "btn-small btn--danger", clickEvent -> {
                callCommand.reject(username);
                close();
            });
            actionButtons.add(accept);
            actionButtons.add(reject);
        }
    }


    private DL drawCallResultsTable() {
        PropertyItem propertyItem = Utils.getProperTy("logCall");
        DL dl = new DL();
        dl.setStyleName("wg_dial__call-result");
        dl.add(new DT(wfmStrings.create()));
        if (propertyItem != null) {
            if (propertyItem.getConvertItems() != null) {
                propertyItem.getConvertItems();
                for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                    if (convertItem != null) {
                        switch (convertItem.getCode()) {
                            case RelationItem.TYPE_TASK:
                                if (Utils.hasPermission(!Utils.isCRM() ? PM_TASKS_ADD : CRM_TASKS_ADD)) {
                                    dl.add(drawCallResultsTableRow(wfmStrings.task(), contactDetailsItem != null ? contactDetailsItem.getTaskCommand() : null));
                                }
                                break;
                            case CrmConstants.CRM_EVENT_CALLOG:
                                if (Utils.hasPermission(Utils.isHRMS() ? HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL : CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                                    dl.add(drawCallResultsTableRow(Property.get(Constants.LOGACALL, wfmStrings.call()), contactDetailsItem != null ? contactDetailsItem.getCallCommand() : null));
                                }
                                break;
                            case RelationItem.TYPE_EVENT:
                                if (Utils.hasPermission(Utils.isHRMS() ? HRMS_ADD_NEW_ACTIVITY_EVENT : CRM_ADD_NEW_ACTIVITY_EVENT)) {
                                    dl.add(drawCallResultsTableRow(Property.get(Constants.EVENT_LIST, wfmStrings.event()), contactDetailsItem != null ? contactDetailsItem.getEventCommand() : null));
                                }
                                break;
                            case RelationItem.TYPE_SMS:
                                dl.add(drawCallResultsTableRow(wfmStrings.sms(), contactDetailsItem != null ? contactDetailsItem.getSmsCommand() : null));
                                break;
                            case RelationItem.TYPE_SALEORDER:
                                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)) {
                                    MaterialLink html = new MaterialLink(Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()));
                                    html.addClickHandler(clickEvent -> {
                                        if (appointmentItem != null && appointmentItem.getObjectID() != null) {
                                            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|edit/CONVERT/" + CrmConstants.CRM_EVENT_CALLOG + "/" + appointmentItem.getObjectID());
                                            } else {
                                                Utils.openURL("Accounting.html#saleorder|edit/CONVERT/" + CrmConstants.CRM_EVENT_CALLOG + "/" + appointmentItem.getObjectID());
                                            }
                                        } else {
                                            Info.show(wfmMessages.youCantConvert(Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder())), Info.Type.WARNING);
                                        }
                                    });
                                    dl.add(new DD(html));
                                }
                                break;
                            case RelationItem.TYPE_CASE:
                                if (Utils.hasPermission(ADD_NEW_CASE)) {
                                    MaterialLink html = new MaterialLink(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()));
                                    html.addClickHandler(clickEvent -> {
                                        if (appointmentItem != null && appointmentItem.getObjectID() != null) {
                                            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/CONVERT/" + CrmConstants.CRM_EVENT_CALLOG + "/" + appointmentItem.getObjectID());
                                            } else {
                                                Utils.openURL("Crm.html#case|add/add/CONVERT/" + CrmConstants.CRM_EVENT_CALLOG + "/" + appointmentItem.getObjectID());
                                            }
                                        } else {
                                            Info.show(wfmMessages.youCantConvert(Property.get(Constants.CASE_LIST, wfmStrings.crmCase())), Info.Type.WARNING);
                                        }
                                    });
                                    dl.add(new DD(html));
                                }
                                break;
                        }
                    }
                }
            }
        } else {
            if (Utils.hasPermission(!Utils.isCRM() ? PM_TASKS_ADD : CRM_TASKS_ADD)) {
                dl.add(drawCallResultsTableRow(wfmStrings.task(), contactDetailsItem != null ? contactDetailsItem.getTaskCommand() : null));
            }
            if (Utils.hasPermission(Utils.isHRMS() ? HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL : CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                dl.add(drawCallResultsTableRow(Property.get(Constants.LOGACALL, wfmStrings.call()), contactDetailsItem != null ? contactDetailsItem.getCallCommand() : null));
            }
            if (Utils.hasPermission(Utils.isHRMS() ? HRMS_ADD_NEW_ACTIVITY_EVENT : CRM_ADD_NEW_ACTIVITY_EVENT)) {
                dl.add(drawCallResultsTableRow(Property.get(Constants.EVENT_LIST, wfmStrings.event()), contactDetailsItem != null ? contactDetailsItem.getEventCommand() : null));
            }

            dl.add(drawCallResultsTableRow(wfmStrings.sms(), contactDetailsItem != null ? contactDetailsItem.getSmsCommand() : null));
        }
        return dl;
    }

    private DD drawCallResultsTableRow(String name, Command command) {
        MaterialLink html = new MaterialLink(name);
        html.addClickHandler(clickEvent -> {
            if (command != null) {
                command.execute(note != null ? note.getText() : null);
            }
        });
        return new DD(html);
    }

    public void createCallLog(int activityType, String twilioCallSID) {
        long duration = 0;

        if (callStart != null) {
            duration = (new Date().getTime() - callStart.getTime()) / 1000;
        }

        Appointment appointment = new Appointment();
        String subject = wfmMessages.callTo(this.contactDetailsItem.getName() != null ? this.contactDetailsItem.getName() : phoneNumber);
        if (activityType == Appointment.SMS) {
            subject = wfmMessages.smsTo(this.contactDetailsItem.getName() != null ? this.contactDetailsItem.getName() : phoneNumber);
        }
        appointment.setSubject(subject);
        String descriptions = note.getText() != null ? note.getText() : "";
        appointment.setDescription(descriptions);
        note.setText(descriptions);
        appointment.setInboundCall(false);
        appointment.setOutboundCall(true);
        appointment.setActivityType(activityType);
        appointment.setAllDay(false);
        appointment.setCallDuration(duration);
        appointment.setComplatedCall(true);
        appointment.setStartDate(activityType == Appointment.SMS || callStart == null ? new Date() : callStart);
        appointment.setEndDate(new Date());
        appointment.setTwilioCallSID(twilioCallSID);
        appointment.addRelations(contactDetailsItem.getRelations() != null ? contactDetailsItem.getRelations().toArray(new RelationItem[]{}) : null);
        int candidateCount = 0;
        for (RelationItem relationItem : appointment.getRelations()) {
            if (RelationItem.TYPE_CANDIDATE.equals(relationItem.getToType())) {
                candidateCount++;
            }
        }
        if (candidateCount == appointment.getRelations().size()) {
            appointment.setCreatedFrom(Appointment.FROM_HRMS);
        } else if (candidateCount > 0) {
            appointment.setCreatedFrom(Appointment.FROM_BOTH);
        }
        saveAppointment(appointment);
    }

    private void saveAppointment(Appointment appointment) {
        appointmentItem = appointment;
        AllInOneService.App.get().saveCallLog(appointment, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Integer result) {
                appointmentItem.setObjectID(result);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, result, null);
            }
        });
    }

    private void saveCrmNote(String notes) {

        HistoryListItem noteHistory = new HistoryListItem(notes);
        noteHistory.setEmployeeID(null);
        String entityType;
        if (contactDetailsItem.getOtherFields() != null) {
            for (ContactTypeForTwilio item : contactDetailsItem.getOtherFields()) {
                entityType = RelationItem.getByContactType(item.getContactType());
                AllInOneService.App.get().saveCrmNote(entityType, item.getId(), noteHistory, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("Couldnt save Notes");
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        note.setText(notes);
                        WfmUiEventsBus.fireWfmUiEvent(result != null && !"".equals(result) ? WfmUiEventType.ON_NOTE_EDIT : WfmUiEventType.ON_NOTE_ADD, result, null);
                    }
                });
            }
        }
    }

    public void setIncomingCallerDetails(ContactDetailsItem incomingCallerDetails) {
        this.contactDetailsItem = incomingCallerDetails;
        if (this.contactDetailsItem != null) {
            moreIcon.setVisible(this.contactDetailsItem.getId() == null);
            if (this.contactDetailsItem.getName() != null && !this.contactDetailsItem.getName().isEmpty()) {
                if (contactDetailsItem.getId() != null) {
                    HTML callerName = new HTML(contactDetailsItem.getName() != null ? "<a href=\"javascript:\">" + contactDetailsItem.getName() + "</a>" : "");
                    callerName.addClickHandler(click -> {
                        //if contact type is not null then its contact
                        /*if (contactDetailsItem.getContactType() != null) {
                            if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + contactDetailsItem.getId());
                            } else {
                                Utils.openURL("Crm.html#contact|summary/" + contactDetailsItem.getId());
                            }
                        } else {
                            //If contact type is null then its company
                            Utils.openURL("Crm.html#account|summary/" + contactDetailsItem.getId());
                        }*/

                        onClickContact(contactDetailsItem);

                    });
                    toLabel.clear();
                    toLabel.getElement().setInnerHTML("");
                    toLabel.add(callerName);
                } else {
                    toLabel.getElement().setInnerHTML(this.contactDetailsItem.getName());
                }
            }
        }
        if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && contactDetailsItem.getContactType().equals(TYPE_LEAD_CONTACT)) {
            drawCallInfoTableForLead();
        } else if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && (contactDetailsItem.getContactType().equals(TYPE_EMPLOYEE_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_CLIENT_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_SUPPLIER_CONTACT)
                || contactDetailsItem.getContactType().equals(TYPE_CRM_CONTACT))) {
            if (contactDetailsItem.getOpportunity() != null) {
                drawCallInfoTableForOpportunity();
            } else if (contactDetailsItem.getEmployee() != null) {
                drawCallInfoTableForEmployee();
            } else {
                drawCallInfoTableForContact();
            }
        } else if (contactDetailsItem != null && contactDetailsItem.getOwner() != null && contactDetailsItem.getContactType().equals(TYPE_ACCOUNT)) {
            drawCallInfoTableForAccount();
        } else if (contactDetailsItem != null && contactDetailsItem.getOwnerId() != null && contactDetailsItem.getContactType().equals(TYPE_CANDIDATE)) {
            drawCallInfoTableForCandidate();
        } else {
            drawCallInfoTable();
        }
    }

    private void onClickContact(ContactDetailsItem contactDetailsItem) {
        String page = "Crm.html";
        String historyToken = "contact|summary/" + contactDetailsItem.getId();
        if (contactDetailsItem.getContactType() != null) {
            switch (contactDetailsItem.getContactType()) {
                case TYPE_LEAD_CONTACT:
                    historyToken = "lead|summary/" + contactDetailsItem.getId();
                    break;
                case TYPE_CLIENT_CONTACT:
                    historyToken = "client|summary/" + contactDetailsItem.getId();
                    break;
                case TYPE_SUPPLIER_CONTACT:
                    historyToken = "suppliersummary|summary/" + contactDetailsItem.getId();
                    break;
                case TYPE_EMPLOYEE_CONTACT:
                    historyToken = "employee|summary/" + contactDetailsItem.getId();
                    break;
                case TYPE_CANDIDATE:
                    historyToken = "candidate|summary/" + contactDetailsItem.getId();
                    break;
                case TYPE_STUDENT_CONTACT:
                    historyToken = "lead|summary/" + contactDetailsItem.getId();
                    break;
            }

            if (!historyToken.startsWith("candiate")) {

                if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
                } else {
                    Utils.openURL("Crm.html#" + historyToken);
                }

            } else if (historyToken.startsWith("candiate") || historyToken.startsWith("employee")) {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
                } else {
                    Utils.openURL("Hrms.html#" + historyToken);
                }
            }
        } else {
            //If contact type is null then its company
            Utils.openURL("Crm.html#account|summary/" + contactDetailsItem.getId());
        }
    }

    private void initCallMoreIcon() {

        MaterialDropDown menuContainer = new MaterialDropDown(moreIcon);

        menuContainer.setBelowOrigin(true);
        moreIcon.add(menuContainer);

        moreIcon.addClickHandler(clickEvent -> {
            menuContainer.clear();

            if (Window.getClientHeight() / 2 < Utils.getElementTop(moreIcon.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                moreIcon.getParent().addStyleName("wg_dial_more--up");
            } else {
                moreIcon.getParent().removeStyleName("wg_dial_more--up");
            }
            //create Contact
            MaterialLink createContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
            createContact.addClickHandler(clickEvent1 -> {
                if (Utils.isCRM()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add/" + phoneNumber + "/phone");
                } else {
                    Utils.openURL("Crm.html#contact|add/add/" + phoneNumber + "/phone");
                }
            });
            if (Utils.hasPermission(CRM_ADD_NEW_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
                menuContainer.add(createContact);
            }
            //update Contact
            MaterialLink updateContact = new MaterialLink("Update Contact");
            updateContact.addClickHandler(clickEvent1 -> {
                KpiModal updateContactModal = new KpiModal();
                CRMLookUp relatedContact = new CRMLookUp(CRMLookUp.CRM_CONTACT);
                updateContactModal.setTitle(wfmStrings.choose() + " " + Property.get(Constants.Contacts, wfmStrings.contact()));
                updateContactModal.add(relatedContact);
                WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
                WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
                cancel.addClickHandler(cl -> updateContactModal.close());
                updateContactModal.addButton(cancel);
                updateContactModal.addButton(ok);
                ok.addClickHandler(okClick -> {
                    if (Validation.validateLookUpRequired(relatedContact)) {
                        if (Utils.isCRM()) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + relatedContact.getSelectedItemID() + "/" + phoneNumber, relatedContact.getSelectedItem() != null ? relatedContact.getSelectedItem().getName() : null, relatedContact.getSelectedItem() != null ? relatedContact.getSelectedItem().getName() : null);
                        } else {
                            Utils.openURL("Crm.html#contactedit|editcontact/" + relatedContact.getSelectedItemID() + "/" + phoneNumber);
                        }
                    }
                });
                updateContactModal.open();
            });
            if (Utils.hasPermission(CRM_EDIT_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_EDIT))) {
                menuContainer.add(updateContact);
            }
            //create Lead
            MaterialLink createLead = new MaterialLink("Create Lead");
            createLead.addClickHandler(clickEvent1 -> {
                if (Utils.isCRM()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("lead|add/add/" + phoneNumber + "/phone");
                } else {
                    Utils.openURL("Crm.html#lead|add/add/" + phoneNumber + "/phone");
                }
            });
            if (Utils.hasPermission(ADD_NEW_LEAD)) {
                menuContainer.add(createLead);
            }
            //update Contact
            MaterialLink updateLead = new MaterialLink("Update Lead");
            updateLead.addClickHandler(clickEvent1 -> {
                KpiModal updateLeadModal = new KpiModal();
                CRMLookUp relatedContact = new CRMLookUp(CRMLookUp.CRM_LEAD);
                updateLeadModal.setTitle(wfmStrings.choose() + " " + Property.get(Constants.LEADS, wfmStrings.lead()));
                updateLeadModal.add(relatedContact);
                WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
                WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
                cancel.addClickHandler(cl -> updateLeadModal.close());
                updateLeadModal.addButton(cancel);
                updateLeadModal.addButton(ok);
                ok.addClickHandler(okClick -> {
                    if (Validation.validateLookUpRequired(relatedContact)) {
                        if (Utils.isCRM()) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("leadedit|editlead/" + relatedContact.getSelectedItemID() + "/" + phoneNumber + "/phone", relatedContact.getSelectedItem() != null ? relatedContact.getSelectedItem().getName() : null, relatedContact.getSelectedItem() != null ? relatedContact.getSelectedItem().getName() : null);
                        } else {
                            Utils.openURL("Crm.html#leadedit|editlead/" + relatedContact.getSelectedItemID() + "/" + phoneNumber + "/phone");
                        }
                    }
                });
                updateLeadModal.open();
            });
            if (Utils.hasPermission(CRM_LEAD_EDIT)) {
                menuContainer.add(updateLead);
            }
        });
    }

    public interface Command {
        void execute(String note);
    }

    private HandlerRegistration handlerRegistration;

    @Override
    protected void onAttach() {
        super.onAttach();
        handlerRegistration = RootPanel.get().addHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                int eventCode = keyDownEvent.getNativeKeyCode();
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                if (eventCode > 95 && eventCode < 106) {
                    phoneNumberInput.setValue(value + (keyDownEvent.getNativeKeyCode() - 96));
                } else if (eventCode == 107 && value.isEmpty()) {
                    phoneNumberInput.setValue(value + "+");
                } else if (eventCode == 8 && value.length() > 0) {
                    phoneNumberInput.setValue(value.substring(0, value.length() - 1));
                } else if (eventCode > 47 && eventCode < 58) {
                    phoneNumberInput.setValue(value + (keyDownEvent.getNativeKeyCode() - 48));
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        handlerRegistration.removeHandler();
    }


}
