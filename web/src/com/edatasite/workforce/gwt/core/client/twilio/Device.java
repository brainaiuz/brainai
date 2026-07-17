package com.edatasite.workforce.gwt.core.client.twilio;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.twilio.event.ConnectionCanceledEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.ConnectionCanceledHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.ConnectionEstablishedEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.ConnectionEstablishedHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.DeviceOfflineEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.DeviceOfflineHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.DeviceReadyEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.DeviceReadyHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.DisconnectEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.DisconnectHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.ErrorEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.ErrorHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.HasDeviceHandlers;
import com.edatasite.workforce.gwt.core.client.twilio.event.IncomingConnectionEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.IncomingConnectionHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.PresenceEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.PresenceHandler;
import com.edatasite.workforce.gwt.core.client.twilio.event.VolumeEvent;
import com.edatasite.workforce.gwt.core.client.twilio.event.VolumeHandler;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.TD;
import gwt.material.design.client.ui.html.Table;
import gwt.material.design.client.ui.html.TableHeadCell;
import gwt.material.design.client.ui.html.TableRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device implements HasDeviceHandlers {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private String lastNumber;
    private Integer relationId;
    private String relationType;
    private String relationName;
    private TwilioPopup2 popup;
    private Date callStart;
    private Connection connection;
    private boolean muted = false;
    private Appointment appointmentItem;
    private boolean isFirstClick = true;
    private final Map<String, String> countryCodes = new HashMap<>();

    public TwilioPopup2 getPopup() {
        if (popup == null) {
            popup = new TwilioPopup2();
            popup.addStyleName("wg_dial__popup");
            if (!Utils.isNullOrEmpty(Device.getInstance().getLastNumber())) {
                popup.showCall(Device.getInstance().lastNumber);
            } else {
                popup.showPad();
            }
        } else if (popup.getParent() != null) {
            popup.getElement().getParentElement().removeClassName("modal-holder");
            popup.getElement().getParentElement().addClassName("wg_dial__parent");
//            popup.getElement().getParentElement().removeFromParent();
        }
        return popup;
    }

    public void showPopup(String type) {
        if (!getPopup().isShowing()) {
            getPopup().open();
        }
        if (type != null && type.equalsIgnoreCase("sms")) {
            getPopup().showSmsDiv(Device.getInstance().getLastNumber());
        }
    }

    public void tryToGetContactByNumber() {
        TwilioService.App.get().getContactByContactNumber(Device.getInstance().getPopup().phoneNumber.getText(), new AbstractAsyncCallback<ArrayList<TwilioContactItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<TwilioContactItem> contacts) {
                if (contacts != null && contacts.size() > 0) {
//                    for (TwilioContactItem item : contacts) {
//                    }
                    if (contacts.get(0).getPrimaryPhone() != null && contacts.get(0).getPrimaryPhone().replaceAll("[^0-9]", "").equalsIgnoreCase(Device.getInstance().getLastNumber())) {
                        Device.getInstance().setRelationId(contacts.get(0).getObjectID());
                        Device.getInstance().setRelationName(contacts.get(0).getName());
                        Device.getInstance().setRelationType(contacts.get(0).getContactType() != null && contacts.get(0).getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT) ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT);
                    }
                }
            }
        });
    }

    public void createCallLog(int activityType, String twilioCallSID, String description) {
        long duration = 0;

        if (callStart != null) {
            duration = (new Date().getTime() - callStart.getTime()) / 1000;
        }

        if (duration > 0 || activityType == Appointment.SMS) {
            Appointment appointment = new Appointment();
            String type = RelationItem.TYPE_LEAD.equalsIgnoreCase(relationType) ? "Lead" : "Contact";
            String subject = wfmMessages.callTo(getRelationNameOrNumber());
            if (activityType == Appointment.SMS) {
                subject = wfmMessages.smsTo(getRelationNameOrNumber());
            }
            appointment.setSubject(subject);
            String descriptions = getLastNumber() + " \n ";
            descriptions += Device.getInstance().getPopup().note.getText() != null ? Device.getInstance().getPopup().note.getText() : "";
            appointment.setDescription(descriptions);
            Device.getInstance().getPopup().note.setText(descriptions);
            appointment.setInboundCall(false);
            appointment.setOutboundCall(true);
            appointment.setActivityType(activityType);
            appointment.setAllDay(false);
            appointment.setCallDuration(duration);
            appointment.setComplatedCall(true);
            appointment.setStartDate(activityType == Appointment.SMS ? new Date() : callStart);
            appointment.setEndDate(new Date());
            appointment.setTwilioCallSID(twilioCallSID);
            appointment.addRelations(new RelationItem(null, relationId, relationType, relationName, null, null, null));
            if (activityType != Appointment.SMS && Device.getInstance().getPopup().hasNote() && relationType != null && relationId != null) {
                HistoryListItem noteItem = new HistoryListItem();
                noteItem.setComment(Device.getInstance().getPopup().note.getText());
                AllInOneService.App.get().saveCrmNote(relationType, relationId, noteItem, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Device.getInstance().getPopup().note.setText("");
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        Device.getInstance().getPopup().note.setText("");
                        WfmUiEventsBus.fireWfmUiEvent(result != null && !"".equals(result) ? WfmUiEventType.ON_NOTE_EDIT : WfmUiEventType.ON_NOTE_ADD, result, null);
                    }
                });
            }
            saveAppointment(appointment);
        } else {
            Utils.log("no success call here!");
        }
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_AFTER_LOG_A_CALL_SET_VALUES, result, null);
            }
        });
    }

    private String getRelationNameOrNumber() {
        if (getRelationName() != null && !"".equalsIgnoreCase(getRelationName()) && !"null".equalsIgnoreCase(getRelationName())) {
            return getRelationName();
        }
        if (getPopup().phoneNumber.getText() != null && !"".equalsIgnoreCase(getPopup().phoneNumber.getText())) {
            return getPopup().phoneNumber.getText();
        }
        if (getLastNumber() != null && !"".equalsIgnoreCase(getLastNumber())) {
            return getLastNumber();
        }
        return "";
    }

    public void setCallStart(Date callStart) {
        this.callStart = callStart;
    }

    public void initialize() {
        lastNumber = null;
        popup = null;
        relationId = null;
        relationType = null;
        relationName = null;
        callStart = null;
        connection = null;
        muted = false;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        muted = false;
    }

    public Connection getConnection() {
        return connection;
    }

    public enum Status {OFFLINE, READY, BUSY, FINISHED, CONVERSATION, CALLING}

    private static final Device instance = new Device();
    private HandlerManager handlerManager;
    private SoundsConfiguration sounds;
    private boolean isReady = false;

    private Device() {
    }

    public static Device getInstance() {
        return instance;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public native void setup(String token)
        /*-{
            $wnd.Twilio.Device.setup(token);
        }-*/;

    public Connection connect(Map<String, String> params) {
        return new Connection(connect(convert(params)));
    }

    private native JavaScriptObject connect(JavaScriptObject params)
        /*-{
            return $wnd.Twilio.Device.connect(params);
        }-*/;

    public Connection connect() {
        return new Connection(connectNoParams());
    }

    private native JavaScriptObject connectNoParams()
        /*-{
            return $wnd.Twilio.Device.connect();
        }-*/;

    public native void disconnectAll()
        /*-{
            $wnd.Twilio.Device.disconnectAll();
        }-*/;

    public Status getStatus() {
        return Status.valueOf(getStatusJS().toUpperCase());
    }

    private native String getStatusJS() /*-{
        return $wnd.Twilio.Device.status()
    }-*/;

    public SoundsConfiguration getSoundsConfiguration() {
        return sounds == null ? sounds = JavaScriptObject.createObject().cast() : sounds;
    }

    static JavaScriptObject convert(Map<String, String> map) {
        JSONObject obj = new JSONObject();
        if (map == null)
            return obj.getJavaScriptObject();
        for (String key : map.keySet())
            obj.put(key, new JSONString(map.get(key)));
        return obj.getJavaScriptObject();
    }

    private HandlerManager ensureHandlers() {
        if (handlerManager != null)
            return handlerManager;
        registerEvents();
        return handlerManager = new HandlerManager(this);
    }

    private native void registerEvents()
        /*-{
            var self = this;
            $wnd.Twilio.Device.ready(function (device) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireDeviceReadyEvent()();
            });
            $wnd.Twilio.Device.offline(function (device) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireDeviceOfflineEvent()();
            });
            $wnd.Twilio.Device.incoming(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireIncomingConnectionEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.cancel(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireConnectionCanceledEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.connect(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireConnectionEstablishedEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.disconnect(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireDisconnectEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
//            $wnd.Twilio.Device.audio.on('deviceChange', updateAllDevices);
//            $wnd.Twilio.Device.audio(function (pe) {
//                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::firePresenceEvent(Ljava/lang/String;Z)(pe.from, pe.available);
//            });
            $wnd.Twilio.Device.error(function (err) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireErrorEvent(Ljava/lang/String;ILcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)
                (err.message.message, err.code, err.info, err.connection);
            });
        }-*/;

    private void fireDeviceReadyEvent() {
        fireEvent(new DeviceReadyEvent());
    }

    private void fireDeviceOfflineEvent() {
        fireEvent(new DeviceOfflineEvent());
    }

    private void fireIncomingConnectionEvent(JavaScriptObject jso) {
        fireEvent(new IncomingConnectionEvent(new Connection(jso)));
    }

    private void fireConnectionCanceledEvent(JavaScriptObject jso) {
        fireEvent(new ConnectionCanceledEvent(new Connection(jso)));
    }

    private void fireConnectionEstablishedEvent(JavaScriptObject jso) {
        fireEvent(new ConnectionEstablishedEvent(new Connection(jso)));
    }

    private void fireDisconnectEvent(JavaScriptObject jso) {
        fireEvent(new DisconnectEvent(new Connection(jso)));
    }

    private void firePresenceEvent(String from, boolean available) {
        fireEvent(new PresenceEvent(from, available));
    }

    private void fireErrorEvent(String message, int code, JavaScriptObject info,
                                JavaScriptObject connection) {
        fireEvent(new ErrorEvent(message, code, info, new Connection(connection)));
    }

    public HandlerRegistration addDeviceReadyHandler(DeviceReadyHandler handler) {
        return addHandler(DeviceReadyEvent.getType(), handler);
    }

    public HandlerRegistration addDeviceOfflineHandler(DeviceOfflineHandler handler) {
        return addHandler(DeviceOfflineEvent.getType(), handler);
    }

    public HandlerRegistration addIncomingConnectionHandler(IncomingConnectionHandler handler) {
        return addHandler(IncomingConnectionEvent.getType(), handler);
    }

    public HandlerRegistration addConnectionCanceledHandler(ConnectionCanceledHandler handler) {
        return addHandler(ConnectionCanceledEvent.getType(), handler);
    }

    public HandlerRegistration addConnectionEstablishedHandler(ConnectionEstablishedHandler handler) {
        return addHandler(ConnectionEstablishedEvent.getType(), handler);
    }

    public HandlerRegistration addDisconnectHandler(DisconnectHandler handler) {
        return addHandler(DisconnectEvent.getType(), handler);
    }

    public HandlerRegistration addPresenceHandler(PresenceHandler handler) {
        return addHandler(PresenceEvent.getType(), handler);
    }

    public HandlerRegistration addErrorHandler(ErrorHandler handler) {
        return addHandler(ErrorEvent.getType(), handler);
    }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) handlerManager.fireEvent(event);
    }

    private <H extends EventHandler> HandlerRegistration
    addHandler(GwtEvent.Type<H> type, H handler) {
        return ensureHandlers().addHandler(type, handler);
    }

    public String getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(String lastNumber) {
        this.lastNumber = lastNumber;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        getPopup().setRelationName(relationName);
        this.relationName = relationName;
    }

    public void mute() {
        if (getConnection() != null) {
            if (!muted) {
                getConnection().mute();
            } else {
                getConnection().unmute();
            }
            muted = getConnection().isMuted();
        }
    }


    class TwilioPopup2 extends MaterialDialog {
        boolean timerAdded = false;
        private String location;

        private final Div contentDiv = new Div("wg_dial wg_dial--calling");
        private final Div padDiv = new Div("wg_dial__pad");
        private final Div notesDiv = new Div("wg_dial__notes");
        private final Div callDiv = new Div("wg_dial__call");
        private final Div smsDiv = new Div("wg_dial__sms");
        private final Div smsItemsDiv = new Div("wg_dial__sms-content");
        private Div callActionButtonsDiv = Utils.div("wg_dial__call-actions__buttons");
        Div smsFlag = Utils.div("wg_dial__flag");
        Div callFlag = Utils.div("wg_dial__flag");
        final Div listTable = new Div("wg_dial__contacts");
        TextArea note = new TextArea();

        private final Div callDurationDiv = new Div("wg_dial__call-duration");
        public Div toName = new Div("wg_dial__call-name");

        public TextBox phoneNumber = new TextBox();

        public TwilioPopup2() {
            if (getWidgetCount() == 0) {
                contentDiv.setId("content-div");
                drawPadDiv();
                contentDiv.add(padDiv);
                drawNoteDiv();
                contentDiv.add(notesDiv);
                contentDiv.add(callDiv);
                callDiv.setId("call-div");
                contentDiv.add(smsDiv);
                add(contentDiv);
                phoneNumber.setMaxLength(16);
                phoneNumber.addChangeHandler(event -> {
                    phoneNumber.setText("+" + phoneNumber.getText().replaceAll("[^0-9]+", ""));
                });
                phoneNumber.addKeyDownHandler(event -> {
                    phoneNumber.setText("+" + phoneNumber.getText().replaceAll("[^0-9]+", ""));
                    int keyCode = event.getNativeKeyCode();
                    int keyValue = -1;
                    if (keyCode >= 48 && keyCode <= 57) {
                        keyValue = keyCode - 48;
                    }
                    if (keyCode >= 96 && keyCode <= 105) {
                        keyValue = keyCode - 96;
                    }
                    if (keyValue > -1) {
                        hoverPadKey(keyValue);
                        if (phoneNumber.getText().length() > 6) {
                            tryToGetContactByNumber();
                        } else {
                            getLocationByNumber(phoneNumber.getText());
                        }
                    }
                });
                WfmUiEventsBus.addWfmUiListenerWithoutWidget(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, (sender, args) -> {
                    getSmsesByNumber(phoneNumber.getText());
                    getCallLogs();
                });
            }
            showPad();
        }

        public void showSmsDiv(String number) {
            smsDiv.clear();
            contentDiv.setStyleName("wg_dial wg_dial--sms");
            Image flag = new Image();
            toName.removeStyleName("wg_dial__call-name");
            toName.addStyleName("wg_dial__sms-number");
            smsDiv.add(Utils.div("wg_dial__sms-header", Utils.div("wg_dial__sms-to-info", toName, Utils.div("wg_dial__sms-number", number)), smsFlag));
            getLocationByNumber(number);
            getSmsesByNumber(number);
            smsDiv.add(smsItemsDiv);
            final TextArea smsTextArea = new TextArea();
            smsTextArea.addStyleName("wg_dial__sms-input");
            final Div smsTextAreaCharCount = Utils.div("wg_dial__sms-chars");
            smsTextArea.addKeyPressHandler(event -> {
                int smsLength = 150 - smsTextArea.getText().length();
                if (smsLength <= 0) {
                    smsTextArea.setText(smsTextArea.getText().substring(0, 150));
                    smsLength = 0;
                }
                smsTextAreaCharCount.getElement().setInnerHTML("" + (150 - smsTextArea.getText().length()));
            });
            Div smsSender = Utils.div("wg_dial__sms-send", Utils.icon("ficon--keyboard-arrow-up"));
            smsSender.addClickHandler(event -> {
                if (smsTextArea.getText().length() > 0) {
                    final String content = "" + smsTextArea.getText();
                    smsTextArea.setText("");
                    TwilioService.App.get().sendSms(number, content, new RelationItem(null, Device.getInstance().getRelationId(), Device.getInstance().getRelationType(), Device.getInstance().getRelationName(), null, null, null), new AsyncCallback<SmsSendItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            smsTextArea.setText(content);
                        }

                        @Override
                        public void onSuccess(SmsSendItem sms) {
                            if (sms == null) {
                                smsTextArea.setText(content);
                                Info.warn(wfmStrings.setTwilioAccount());
                            } else {
                                createCallLog(Appointment.SMS, sms.getSid(), content);
                            }
                        }
                    });
                }
            });
            smsDiv.add(Utils.div("wg_dial__sms-footer", smsTextArea, smsSender, smsTextAreaCharCount));
        }

        private void getSmsesByNumber(String number) {
            TwilioService.App.get().getRecentCallLogs(Appointment.SMS, Device.getInstance().relationType, Device.getInstance().getRelationId(), new AbstractAsyncCallback<ArrayList<Appointment>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                    smsItemsDiv.clear();
                }

                @Override
                public void onSuccess(ArrayList<Appointment> result) {
                    super.onSuccess(result);
                    smsItemsDiv.clear();
                    if (result != null) {
                        for (Appointment sms : result) {
                            smsItemsDiv.add(Utils.div("wg_dial__sms-content-item", Utils.div("wg_dial__sms-content-item-date", DateUtils.getDateAndTimeFormatShort(sms.getStartDate())), Utils.div("wg_dial__sms-content-item-message", sms.getDescription())));
                        }
                    }
                }
            });
//            TwilioService.App.get().getSmsByNumber(number, new AsyncCallback<List<SmsSendItem>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//
//                }
//
//                @Override
//                public void onSuccess(List<SmsSendItem> result) {
//                    smsItemsDiv.clear();
//                    if (result != null) {
//                        for (SmsSendItem sms : result) {
//                            smsItemsDiv.add(Utils.div("wg_dial__sms-content-item", Utils.div("wg_dial__sms-content-item-date", sms.getDate()), Utils.div("wg_dial__sms-content-item-message", sms.getMessageText())));
//                        }
//                    }
//                }
//            });
        }

        private void hoverPadKey(int keyValue) {
            final String lastNumber = "" + keyValue;
            if (numbersMap.get(lastNumber) != null) {
                numbersMap.get(lastNumber).addStyleName("active");
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        numbersMap.get(lastNumber).removeStyleName("active");
                    }
                };
                timer.schedule(200);
            }
        }

        @Override
        public void open() {
            this.setLayoutPosition(Style.Position.ABSOLUTE);
            this.setRight(0);
            this.setBottom(0);
            RootPanel.get().add(this);
            Utils.makeDraggable(contentDiv.getId());
        }

        public void center() {
            if (!isShowing()) {
                RootPanel.get().add(this);

                super.open();
            }
        }

        @Override
        public void close() {
            super.close();
            removeFromParent();
        }

        public boolean isShowing() {
            return Display.BLOCK.getCssName().equals(getElement().getStyle().getDisplay());
        }

        public TwilioPopup2 showCall(String number) {
            contentDiv.setStyleName("wg_dial wg_dial--calling");
            toName.removeStyleName("wg_dial__sms-number");
            toName.addStyleName("wg_dial__call-name");
            if (Utils.isNullOrEmpty(number)) {
                return showPad();
            }
            drawCallDiv(Status.CALLING, number);
            return this;
        }

        private void drawCallDiv(Status state, String number) {
            contentDiv.setStyleName("wg_dial wg_dial--" + state.name().toLowerCase());
            callDiv.clear();
            getLocationByNumber(number);
            Div muteDiv = Utils.div("btn-small btn--lightgrey", "Mute");
            muteDiv.addClickHandler(event -> {
                Device.getInstance().mute();
                muteDiv.setStyleName("btn-small " + (Device.getInstance().muted ? "btn--lightgrey mic-disabled" : "btn--success mic-enabled"));
            });

            Div hangupDiv = Utils.div("btn-small btn--danger", "End");
            hangupDiv.addClickHandler(event -> {
                Device.getInstance().disconnectAll();
            });

            Div keypadDiv = Utils.div("btn-small btn--icon", new SvgIcon(SvgEnum.keypad));
            keypadDiv.addClickHandler(clickEvent -> {
                if (contentDiv.getStyleName() != null && contentDiv.getStyleName().contains("wg_dial--keypadOn")) {
                    contentDiv.removeStyleName("wg_dial--keypadOn");
                } else {
                    contentDiv.addStyleName("wg_dial--keypadOn");
                }
            });

            callActionButtonsDiv = Utils.div("wg_dial__call-actions__buttons", hangupDiv, muteDiv, keypadDiv);
            Div callActionIconDiv = new Div("wg_dial__call-actions__icon");
            SvgIcon svgIcon = new SvgIcon(SvgEnum.callAction);

            callDiv.add(Utils.div("wg_dial__call-actions", callActionIconDiv, Utils.div("", toName, callDurationDiv, callActionButtonsDiv)));

            if (Status.FINISHED.equals(state)) {
                svgIcon = new SvgIcon(SvgEnum.callActionEnd);
                callActionButtonsDiv.removeFromParent();
                callDiv.add(this::drawCallResultsTable);

                WfmButton2 save = new WfmButton2(wfmStrings.save(), "btn-small btn--primary", clickEvent -> {
                    appointmentItem.setDescription(note.getText());
                    saveAppointment(appointmentItem);
                    Device.getInstance().disconnectAll();
                    close();
                });
                WfmButton2 cancel = new WfmButton2(wfmStrings.close(), "btn-small btn--default", clickEvent -> {
                    Device.getInstance().disconnectAll();
                    close();
                });

                Div footer = Utils.div("wg_dial__call-footer");
                footer.add(save);
                footer.add(cancel);
                callDiv.add(footer);
            } else if (Status.CONVERSATION.equals(state)) {
                countCallDuration();
                callDiv.add(drawCallInfoTable(number, location));
            } else {
                callDurationDiv.getElement().setInnerHTML("calling...");
                callDiv.add(drawCallInfoTable(number, location));
            }
            callActionIconDiv.add(svgIcon);
            note.setFocus(true);
        }

        private Table drawCallInfoTable(String phone, String location) {
            Table table = new Table();
            table.addStyleName("wg_dial__call-info");
//            table.add(drawCallInfoTableRow(wfmStrings.contactOwner(), contact.getOwner()));
//            table.add(drawCallInfoTableRow(wfmStrings.email(), contact.getPrimaryEmail()));
//            table.add(drawCallInfoTableRow(wfmStrings.phone(), phone));
//            table.add(drawCallInfoTableRow(wfmStrings.mobile(), contact.getPrimaryPhone()));
//            table.add(drawCallInfoTableRow(wfmStrings.location(), location));
            return table;
        }

        private TableRow drawCallInfoTableRow(String title, String value) {
            return new TableRow(new TableHeadCell(title), new TD(value != null ? value : "--"));
        }

        private DL drawCallResultsTable() {
            DL dl = new DL();
            dl.setStyleName("wg_dial__call-result");
            dl.add(new DT(wfmStrings.create()));
            dl.add(drawCallResultsTableRow(wfmStrings.task(), null));
            dl.add(drawCallResultsTableRow(wfmStrings.call(), null));
            dl.add(drawCallResultsTableRow(Property.get(Constants.EVENT_LIST, wfmStrings.event()), null));
            dl.add(drawCallResultsTableRow(wfmStrings.sms(), null));
            return dl;
        }

        private DD drawCallResultsTableRow(String name, Command command) {
            MaterialLink html = new MaterialLink(name);
            html.addClickHandler(clickEvent -> {
                if (isFirstClick) {
                    appointmentItem.setDescription(note.getText());
                    saveAppointment(appointmentItem);
                    isFirstClick = false;
                    getElement().getStyle().setLeft(500, Style.Unit.PX);
                    getElement().getStyle().setBottom(1000, Style.Unit.PX);
                    contentDiv.getElement().getStyle().setTop(1000, Style.Unit.PX);
                }
                if (command != null) {
                    command.execute();
                }
            });
            return new DD(html);
        }

        private void getLocationByNumber(String number) {
            TwilioService.App.get().getCountryNameByPhoneNumber(number, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(String s) {
                    location = s;
                }
            });
        }


        private void drawPadDiv() {
            padDiv.clear();
            Div callDurationDiv = new Div("wg_dial__call-duration");
            Div padButtons = new Div("wg_dial__pad-buttons");
            padDiv.add(Utils.div("wg_dial__number", Utils.div("wg_dial__number-input", callFlag, phoneNumber)));
            //buttons
            padButtons.add(padButton(1));
            padButtons.add(padButton(2));
            padButtons.add(padButton(3));
            padButtons.add(padButton(4));
            padButtons.add(padButton(5));
            padButtons.add(padButton(6));
            padButtons.add(padButton(7));
            padButtons.add(padButton(8));
            padButtons.add(padButton(9));
            padButtons.add(padButton(-1));
            padButtons.add(padButton(0));
            padButtons.add(padButton(-2));
            padDiv.add(callDurationDiv);
            padDiv.add(padButtons);
            padDiv.add(Utils.div("wg_dial__pad-footer", Utils.div("wg_dial__text", wfmStrings.callYourCustomers())));
        }

        public TwilioPopup2 showPad() {
            drawCallDiv(Status.FINISHED, getLastNumber());
            return this;
        }

        private void drawNoteDiv() {
            notesDiv.clear();
            MaterialLink link = new MaterialLink(wfmStrings.addNote());
            link.setHref("#wg-dial--rencent");
            notesDiv.add(Utils.div("wg_dial__info-tabs", Utils.div("wg_dial__info-tabs-item", link)));
            note.addStyleName("wg_dial__notes-textarea");
            notesDiv.add(Utils.div("wg_dial__notes-content", note));
            note.getElement().setPropertyString("placeholder", wfmStrings.doNotMiss());
        }

        Map<String, Div> numbersMap = new HashMap<String, Div>();

        private IsWidget padButton(final int i) {
            String _class = "wg_dial__pad-button";
            if (i < 0) {
                _class += " wg_dial__pad-button--function";
            }
            Div n = new Div(_class);
            if (i >= 0) {

                n.getElement().setInnerHTML("<span>" + i + "</span>");
                n.addClickHandler(event -> {
                    phoneNumber.setText("+" + phoneNumber.getText().replaceAll("[^0-9]+", "") + i);
                    if (phoneNumber.getText().length() > 6) {
                        tryToGetContactByNumber();
                    } else {
                        getLocationByNumber(phoneNumber.getText());
                    }
                });
                numbersMap.put("" + i, n);
            } else {
                Icon icon = new Icon();
                icon.setStyleName(i == -1 ? "ficon--phone" : "ficon--sms");
                Span span = new Span();
                span.add(icon);
                n.add(span);
                n.addClickHandler(event -> {
                    if (i == -1) {
                        tryToGetContactByNumber();
                        showCall(phoneNumber.getValue()).call();
                    } else if (i == -2) {
                        showSmsDiv(phoneNumber.getValue());
                    }
                });
            }
            return n;
        }

//        public void dail(String lastNumber) {
//            showCall(lastNumber).call();
//        }

        private void showContacts(Div... tabs) {
            clearTabs(tabs);
            //clear *--active from tabs and activate the first tab in array.
            TwilioService.App.get().getContactList(new ListingFilterParameter(), new ListLoadConfig(10), new AbstractAsyncCallback<TwilioContactItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                    listTable.clear();
                }

                @Override
                public void onSuccess(TwilioContactItem[] result) {
                    super.onSuccess(result);
                    listTable.clear();
                    drawContacts(result);
                }
            });

            //todo
        }

        private void clearTabs(Div... tabs) {
            if (tabs != null) {
                for (int i = 0; i < tabs.length; i++) {
                    if (tabs[0] != null) {
                        if (i == 0) {
                            tabs[i].addStyleName("wg_dial__info-tabs-item--active");
                        } else {
                            tabs[i].removeStyleName("wg_dial__info-tabs-item--active");
                        }
                    }
                }
            }
        }

        private void showCallLogs(Div... tabs) {
            clearTabs(tabs);
            getCallLogs();
        }

        private void getCallLogs() {
            TwilioService.App.get().getRecentCallLogs(Appointment.CALL_AND_SMS, Device.getInstance().relationType, Device.getInstance().getRelationId(), new AbstractAsyncCallback<ArrayList<Appointment>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                    listTable.clear();
                }

                @Override
                public void onSuccess(ArrayList<Appointment> result) {
                    super.onSuccess(result);
                    listTable.clear();
                    drawCallLogs(result);
                }
            });
        }

        private void drawCallLogs(List<Appointment> listItems) {
            if (listItems != null && listItems.size() > 0) {
                for (Appointment item : listItems) {
                    Div row = Utils.div("widget-row widget-row--favourite");
                    String callStart = Utils.getTimeAsHourAndMinute(item.getStartDate());
//                    row.add(Utils.div("widget-row__icon", Utils.icon("ficon--star")));

                    Div profileTitleDiv = Utils.div("cp_profile-min__title");
                    profileTitleDiv.add(Utils.div("cp_profile-min__name", item.getSubject()));
                    profileTitleDiv.add(Utils.div("cp_profile-min__company", ""));
                    row.add(Utils.div("widget-row__item widget-row__item--grow", Utils.div("cp_profile-min", profileTitleDiv)));

                    Div buttons = Utils.div("widget-row__button-group");
                    MaterialWidget spanOrLink = new MaterialLink("");
                    spanOrLink.add(Utils.icon("ficon--phone2"));

                    MaterialLink callLogs = new MaterialLink();
                    callLogs.add(Utils.icon("ficon--info2"));
                    callLogs.addClickHandler(event -> openCallLog(item.getObjectID()));

                    buttons.add(Utils.div("widget-row__button", callLogs));
                    buttons.add(Utils.div("widget-row__button", Utils.span(null, Utils.icon(item.getActivityType() == Appointment.SMS ? "ficon--sms" : "ficon--phone2"))));
                    buttons.add(Utils.div("widget-row__button", Utils.span("widget-row__button--duration", callStart)));
                    row.add(Utils.div("widget-row__end", buttons));
                    listTable.add(row);
                }
            }
        }

        private void drawContacts(TwilioContactItem[] listItems) {

            if (listItems != null) {
                for (TwilioContactItem contact : listItems) {
                    Div row = Utils.div("widget-row widget-row--favourite");

                    row.add(Utils.div("widget-row__icon", Utils.icon("ficon--star")));

                    Div profileTitleDiv = Utils.div("cp_profile-min__title");
                    profileTitleDiv.add(Utils.div("cp_profile-min__name", contact.getName()));
                    profileTitleDiv.add(Utils.div("cp_profile-min__company", contact.getPrimaryPhone()));
                    row.add(Utils.div("widget-row__item widget-row__item--grow", Utils.div("cp_profile-min", profileTitleDiv)));

                    Div buttons = Utils.div("widget-row__button-group");
                    MaterialLink callLogs = new MaterialLink();
                    callLogs.add(Utils.icon("ficon--info2"));
                    callLogs.addClickHandler(event -> openContact(contact.getObjectID(), contact.getContactType()));
                    buttons.add(callLogs);
                    MaterialWidget spanOrLink = Utils.span(null);

                    if (contact.getMobile() != null && contact.getMobile().size() > 0 && !"".equalsIgnoreCase(contact.getMobile().get(0))) {
                        spanOrLink = new MaterialLink("");
                        spanOrLink.addClickHandler(event -> {
//                            TwilioHelper.initiate("sms", contact.getObjectID(), relationType, contact.getMobile().get(0));
                        });
                    }
                    spanOrLink.add(Utils.icon("ficon--sms"));
                    buttons.add(spanOrLink);
                    spanOrLink = Utils.span(null);

                    if (contact.getPrimaryPhone() != null && !"".equalsIgnoreCase(contact.getPrimaryPhone())) {
                        spanOrLink = new MaterialLink("");
                        spanOrLink.addClickHandler(event -> {
                            String relationType = RelationItem.TYPE_CONTACT;
                            if (contact.getContactType() != null && contact.getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                                relationType = RelationItem.TYPE_LEAD;
                            }
//                            TwilioHelper.initiate("call", contact.getObjectID(), relationType, contact.getPrimaryPhone());
                        });
                    }
                    spanOrLink.add(Utils.icon("ficon--phone2"));
                    buttons.add(spanOrLink);

                    row.add(Utils.div("widget-row__end", buttons));
                    listTable.add(row);
                }
            }
        }

        public void call() {
            if (phoneNumber.getValue() != null && !"".equalsIgnoreCase(phoneNumber.getValue())) {
                Device.getInstance().setLastNumber(phoneNumber.getValue());
            } else {
                phoneNumber.setValue(lastNumber);
            }
            if (lastNumber != null && !"".equalsIgnoreCase(lastNumber)) {
                Map<String, String> map = new HashMap<String, String>();//        map.put("From", "+12133440101");
                map.put("To", lastNumber);
                map.put("record", "record-on-answer");
                Connection connection = Device.getInstance().connect(map);
                connection.addAcceptHandler(evt -> {
                    drawCallDiv(Status.CONVERSATION, getLastNumber());
                });
            }
        }

        public void countCallDuration() {
            if (!timerAdded) {
                Scheduler.get().scheduleFixedDelay(() -> {
                    String time = "00:00";
                    if (Device.getInstance().callStart != null) {
                        time = Utils.getLongAsMinuteAndSecond((new Date().getTime() - Device.getInstance().callStart.getTime()));
                    } else {
                        return false;
                    }
                    callDurationDiv.getElement().setInnerHTML(time);
                    return true;
                }, 1000);
                timerAdded = true;
            }
        }

        public void setRelationName(String result) {
            toName.getElement().setInnerHTML(result != null ? result : "");

        }

        public boolean hasNote() {
            return !Utils.isNullOrEmpty(note.getText());
        }

        public void showByType(String type, String lastNumber) {
            if (!Utils.isNullOrEmpty(lastNumber)) {
                if ("call".equalsIgnoreCase(type)) {
                    Device.getInstance().getPopup().showCall(Device.getInstance().getLastNumber()).call();
                } else {
                    Device.getInstance().getPopup().showSmsDiv(Device.getInstance().getLastNumber());
                }
            } else {
                Device.getInstance().getPopup().showPad();
            }

        }
    }

    private void openCallLog(Integer itemID) {
        if (Utils.isCRM()) {
            SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + itemID);
        } else {
            String url = GWT.getHostPageBaseURL() + "/Crm.html#event|summary/" + itemID;
            Window.open(url, "_blank", null);
        }
    }

    private void openContact(Integer itemID, Integer contactType) {
        String type = "contact";
        if (contactType != null && contactType.equals(CrmConstants.TYPE_LEAD_CONTACT)) {
            type = "lead";
        }
        if (Utils.isCRM()) {
            SinksContainerFactory.entryPoint.onHistoryChanged(type + "|summary/" + itemID);
        } else {
            String url = GWT.getHostPageBaseURL() + "/Crm.html#" + type + "|summary/" + itemID;
            Window.open(url, "_blank", null);
        }
    }

    private void closeAndDisconnect() {
        Device.getInstance().disconnectAll();
        Device.getInstance().getPopup().showPad();
        Device.getInstance().getPopup().close();
    }

    public static void bindVolumeIndicators(Connection connection) {
        connection.addVolumeHandler(new VolumeHandler() {
            @Override
            public void onVolume(VolumeEvent evt) {
                String inputColor = "red";
                if (evt.getIn() < .50) {
                    inputColor = "green";
                } else if (evt.getIn() < .75) {
                    inputColor = "yellow";
                }

                String outputColor = "red";
                if (evt.getOut() < .50) {
                    outputColor = "green";
                } else if (evt.getOut() < .75) {
                    outputColor = "yellow";
                }
//                Device.getInstance().getPopup().micVolume.getElement().getStyle().setWidth(Math.floor(evt.getIn() * 300), Style.Unit.PX);//todo
//                Device.getInstance().getPopup().micVolume.getElement().getStyle().setBackgroundColor(inputColor);//todo
//                Device.getInstance().getPopup().speakerVolume.getElement().getStyle().setWidth(Math.floor(evt.getOut() * 300), Style.Unit.PX);//todo
//                Device.getInstance().getPopup().speakerVolume.getElement().getStyle().setBackgroundColor(outputColor);//todo
            }
        });
    }

//    public void setTaskCommand(final Command taskCommand) {
//        this.taskCommand = taskCommand;
//    }
//
//    public void setCallCommand(final Command callCommand) {
//        this.callCommand = callCommand;
//    }
//
//    public void setEventCommand(final Command eventCommand) {
//        this.eventCommand = eventCommand;
//    }
//
//    public void setSmsCommand(final Command smsCommand) {
//        this.smsCommand = smsCommand;
//    }

//    public interface Command {
//        void execute(ContactListItem contact);
//    }
}