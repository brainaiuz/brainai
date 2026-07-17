package com.edatasite.workforce.gwt.core.client.ui.communication.phone;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ContactTypeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.CallCommand;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

public class PhonePopup extends Composite {

    @UiTemplate("com.edatasite.workforce.gwt.core.client.ui.communication.phone.PhonePopup.ui.xml")
    interface PhonePopupUiBinder extends UiBinder<HTMLPanel, PhonePopup> {
    }

    private ContactDetailsItem contactDetailsItem = new ContactDetailsItem();
    private static TwilioContactItem contact;
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    TextBox phoneNumberInput;
    @UiField
    HTMLPanel padButtons;
    @UiField
    HTMLPanel dialPad;
    @UiField
    HTMLPanel dialText;


    public PhonePopup() {
        PhonePopupUiBinder ourUiBinder = GWT.create(PhonePopupUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
//        phoneNumberInput.setDisabled(true);
        drawPadButtons();
        dialText.getElement().setInnerHTML(wfmStrings.callYourCustomers());
        dialText.getElement().getStyle().setWidth(250, Style.Unit.PX);
        dialText.getElement().getStyle().setProperty("textAlign", "justify");
        Validation.addPhoneNumberKeyboardListener(phoneNumberInput);
//        phoneNumberInput.setValue(addHandler(new KeyPressHandler() {
//            @Override
//            public void onKeyPress(KeyPressEvent keyPressEvent) {
//                int eventCode = keyPressEvent.getNativeEvent().getKeyCode();
//                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
//                if (eventCode > 95 && eventCode < 106) {
//                    phoneNumberInput.setValue(value + (keyPressEvent.getNativeEvent().getKeyCode() - 96));
//                } else if (eventCode == 107 && value.isEmpty()) {
//                    phoneNumberInput.setValue(value + "+");
//                } else if (eventCode == 8 && value.length() > 0) {
//                    phoneNumberInput.setValue(value.substring(0, value.length() - 1));
//                }
//            }
//        }, KeyPressEvent.getType()));

    }

    private void drawPadButtons() {
        for (int i = 1; i < 15; i++) {
            padButtons.add(createPadButton(i));
        }
    }

    private void callViaProvider(String username, CallCommand callCommand) {
        String phoneNumber = phoneNumberInput.getText();
        if (phoneNumber != null && phoneNumber.length() > 0) {
            if (contact != null) {
                contactDetailsItem.setId(contact.getObjectID());
                contactDetailsItem.setContactType(contact.getContactType());
                contactDetailsItem.setName(contact.getName());
                contactDetailsItem.setMobile(contact.getPrimaryPhone());
                contactDetailsItem.setOwnerId(contact.getOwnerId());
                contactDetailsItem.setOwner(contact.getOwner());
                contactDetailsItem.setPhoneNumber(contact.getPrimaryPhone());
                contactDetailsItem.setPrimaryEmail(contact.getEmail());
                CommonService.App.get().getOtherContactTypes(phoneNumber, new AsyncCallback<ArrayList<ContactTypeForTwilio>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(ArrayList<ContactTypeForTwilio> contactTypeForTwilios) {
                        contactDetailsItem.setOtherFields(contactTypeForTwilios);
                        callCommand.call(username, phoneNumber, contactDetailsItem);
                    }
                });
            }


        } /*else {
            if ("twilio".equalsIgnoreCase(provider)) {
                AllInOneService.App.get().getAccountItemByRelation(firstPhoneRelationItem, new AsyncCallback<SelectItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(SelectItem selectItem) {
                        TwilioHelper.initiate("call", selectItem.getId(), selectItem.getName(), selectItem.getDescription(), contact);
                    }
                });
            } else if("asterisk".equalsIgnoreCase(provider)) {

            }
        }*/
    }

    public void hide() {
        dialPad.getElement().getStyle().setProperty("display", "none");
    }

    Div createPadButton(int number) {
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
                    startCall(phoneNumberInput.getValue());
                });
            } else if (contactDetailsItem != null && contactDetailsItem.getSmsCommand() != null) {
//                div.addClickHandler(clickEvent -> contactDetailsItem.getSmsCommand().execute(note != null ? note.getText() : null));
            }
        } else if (number == 10 || number == 12) {
            Span span = new Span();
            span.setText(number == 10 ? "*" : "#");

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + span.getText());
//                callCommand.sendDigits(contact.getName(), span.getText());
            });
        } else if (number == 11) {
            Span span = new Span();
            span.setText("0 +");

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + "0");
//                callCommand.sendDigits(contact.getName(), "0");
            });
            div.addDoubleClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                value = value.replace(value.substring(value.length() - 1), "");
                value = value.substring(0, value.length() - 2);
                phoneNumberInput.setValue(value + "+");
//                callCommand.sendDigits(contact.getName(), "+");
            });
        } else {
            Span span = new Span();
            span.setText(String.valueOf(number));

            div.add(span);
            div.addClickHandler(clickEvent -> {
                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
                phoneNumberInput.setValue(value + span.getText());
//                callCommand.sendDigits(contact.getName(), span.getText());
            });
        }
        return div;
    }

    private HandlerRegistration handlerRegistration;

    @Override
    protected void onAttach() {
        super.onAttach();

//        handlerRegistration = RootPanel.get().addHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent keyDownEvent) {
//                int eventCode = keyDownEvent.getNativeKeyCode();
//                String value = phoneNumberInput.getValue() != null ? phoneNumberInput.getValue() : "";
//                if (eventCode > 95 && eventCode < 106) {
//                    phoneNumberInput.setValue(value + (keyDownEvent.getNativeKeyCode() - 96));
//                } else if (eventCode == 107 && value.isEmpty()) {
//                    phoneNumberInput.setValue(value + "+");
//                } else if (eventCode == 8 && value.length() > 0) {
//                    phoneNumberInput.setValue(value.substring(0, value.length() - 1));
//                } else if (eventCode > 47 && eventCode < 58) {
//                    phoneNumberInput.setValue(value + (keyDownEvent.getNativeKeyCode() - 48));
//                } else if (eventCode == 13 && phoneNumberInput.getValue() != null && phoneNumberInput.getValue().length() > 0) {
//                    startCall(phoneNumberInput.getValue());
//                }
//            }
//        }, KeyDownEvent.getType());
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        handlerRegistration.removeHandler();
    }

    private void startCall(String phoneNumber) {
        CommonService.App.get().getIncomingCallerDetails(phoneNumber, new AsyncCallback<TwilioContactItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TwilioContactItem twilioContactItem) {
                contact = twilioContactItem;
                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK) && Utils.getAsteriskSettings() != null && !Utils.getAsteriskSettings().isEmpty()) {
                    for (AsteriskSettings asteriskSettings : Utils.getAsteriskSettings()) {
                        //@TODO will call only from one number we must split
                        GWT.log("111111111111111: " + asteriskSettings.getAsteriskUsername());
                        callViaProvider(asteriskSettings.getAsteriskUsername(), WorkforceEntryPoint.asteriskCallHandler);
                        hide();
                    }
                }

            }
        });
    }

}