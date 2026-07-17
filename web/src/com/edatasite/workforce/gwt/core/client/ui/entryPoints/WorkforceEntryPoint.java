package com.edatasite.workforce.gwt.core.client.ui.entryPoints;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationReloadEvent;
import com.edatasite.workforce.gwt.core.client.rpc.notification.PushNotificationRemoteListener;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketClientObject;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketContactObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleMarketPlaceUsersView;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler;
import com.edatasite.workforce.gwt.core.client.ui.components.sampleData.RemoveSDInfo;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.draggable.IncomingAsteriskCallModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notification.KpiEventServiceCreator;
import com.edatasite.workforce.gwt.core.client.ui.notification.KpiRemoteEventConnector;
import com.edatasite.workforce.gwt.core.client.ui.notifications.FeaturesNotification;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.DefaultDomain;
import org.realityforge.gwt.websockets.client.WebSocket;
import org.realityforge.gwt.websockets.client.WebSocketListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 23, 2007 Time: 7:03:29 PM To
 * change this template use File | Settings | File Templates.
 */
public abstract class WorkforceEntryPoint extends GeneralEntryPoint {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    public final StatusServiceAsync statusService = StatusService.App.get();
    public final NotificationMsgServiceAsync notificationService = NotificationMsgService.App.get();

    public static AsteriskCallHandler asteriskCallHandler;

    private Timer refreshTimer;
    private TextBox searchBox;
    //    protected UiSettings urls;
    private int userSpentTime = 0;
    int switchvoxErrorCount = 0;
    private Timer inOutTimer;

    public static native void registerScripts(String script) /*-{
        $wnd.eval(script);
    }-*/;

    public AsteriskCallHandler initializeAsteriskCallHandler(List<AsteriskSettings> asteriskSettings, String userFullName) {
        AsteriskCallHandler asteriskCallHandler = new AsteriskCallHandler(asteriskSettings, userFullName);
        return asteriskCallHandler;
    }

    protected void initPushServerEventNotification() {
        //get the RemoteEventService for registration of RemoteEventListener instances
        RemoteEventService theRemoteEventService = RemoteEventServiceFactory.getInstance()
                .getRemoteEventService(new KpiRemoteEventConnector(new KpiEventServiceCreator()));

        //add a listener to the SERVER_MESSAGE_DOMAIN
        theRemoteEventService.addListener(new DefaultDomain(Utils.getUserName()), new PushNotificationRemoteListener() {

            @Override
            public void reloadNotificationToolBar(NotificationReloadEvent reloadEvent) {
                getNewNotificationMsg();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, null, null);
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, mainLayout, (sender, args) -> getNewNotificationMsg());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PHONE_CALLED, mainLayout, (sender, args) -> {
            WebSocketContactObject contactDetails = JsonUtils.safeEval(args+"");
            SwitchvoxContactItem convertedContact = new SwitchvoxContactItem();
            convertedContact.setObjectId(contactDetails.getItem_id());
            convertedContact.setName(contactDetails.getName());
            convertedContact.setWorkPhone(contactDetails.getPhone());
            convertedContact.setContactType(contactDetails.getContactType());
//            convertedContact.setObjectId();
            //Show Popup to show incoming call details
            showIncomingCaller(convertedContact);
            GWT.log(args + "");
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PUSH_NOTIFICATION_POPUP, mainLayout, (sender, args) -> {
            Info.show(args.toString());
            GWT.log(args + "");
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CHECKING_CHECKOUT_NOTIFICATION, mainLayout, (sender, args) -> {
            GWT.log("----------------------------------------------KELDI-----------------------------");
            String[] data =args.toString().split("-#-");
            String status= data[0];
            switch (status){
                case "Paid":
                case "Оплачено":
                case "To'langan":
                    Info.show(data[1] + " " + wfmStrings.successfullyLogIn(),Info.Type.INFO, Info.Position.BOTTOM_RIGHT, 6000);
                    playSound("sounds/success.mp3");
                    break;
                case "Expired":
                case "Просрочен":
                case "Muddati tugagan":
                    Info.warn(data[1] + " " + wfmStrings.notAuthorized(),6000);
                    playSound("sounds/error.mp3");
                    break;
                case "Not Paid":
                case "Не оплачено":
                case "To'lanmagan":
                    Info.warn(wfmMessages.notPiadMember(data[1]),6000);
                    playSound("sounds/error.mp3");
                    break;
                case "Pending":
                case "В ожидании":
                case "Kutilmoqda":
                    Info.warn(wfmMessages.pendingMember(data[1]),6000);
                    playSound("sounds/error.mp3");
                    break;
            }
            GWT.log(args + "");
        });
    }

    private native void playSound(String soundFile) /*-{
        var audio = new Audio(soundFile);
        audio.play();
    }-*/;

    protected void initDefaultUserSettings() {
        super.initDefaultUserSettings();
        removeLoadingBar();
        removeFakeModules();
        if (TRUE.equals(Utils.userSettings.get(ACCESS_GRANTED))) {
            historyToken = Utils.userSettings.get(INITIAL_URL);
            String anchor = Utils.getAnchorString();

            if ((!"".equals(anchor)) && anchor.length() > 1) {
                historyToken = Utils.getAnchorString().substring(1);
            }
            onLogin();
        } else {
            if (Utils.hasRole(ADMIN)) {

                if (!Utils.getPathName().equals("/Myaccount.html")) {
                    Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html");
                } else {

                    if (Utils.getPathName().equals("/Myaccount.html") && !Utils.getAnchorString().equals("")) {
                        historyToken = Utils.userSettings.get(INITIAL_URL);
                        String anchor = Utils.getAnchorString();

                        if ((!"".equals(anchor)) && anchor.length() > 1) {
                            historyToken = Utils.getAnchorString().substring(1);
                        }
                    }
                    onLogin();
                }
            } else {
                Utils.redirect(GWT.getHostPageBaseURL() + "accountExpiration.html");
            }
            Cookies.removeCookie(HASH_LINK_COOKIE);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK) && Utils.getAsteriskSettings() != null && !Utils.getAsteriskSettings().isEmpty()) {
            GWT.log("Asterisk enabled");
            asteriskCallHandler = initializeAsteriskCallHandler(Utils.getAsteriskSettings(), Utils.getUserFullName());
        }
        initPushServerEventNotification();

        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, mainLayout, (sender, args) -> getNewEmailList());
        }

    }

    public static HashMap<String, IncomingAsteriskCallModal> asterisktCallPopups = new HashMap<>();
    HashMap<String, KpiModal> phoneCalls = new HashMap<>();

    private void showIncomingCaller(SwitchvoxContactItem result) {

        if (result.getWorkPhone() != null) {
            phoneCalls.remove(result.getWorkPhone());

            String contactType = null;
            switch (Integer.valueOf(result.getContactType())){
                case CrmConstants.TYPE_CRM_CONTACT:
                    contactType = "contactedit";
                    break;
                case CrmConstants.TYPE_LEAD_CONTACT:
                    contactType = "leadedit";
                    break;
                case CrmConstants.TYPE_CANDIDATE:
                    contactType = "candidateedit";
                    break;
                case CrmConstants.TYPE_ACCOUNT:
                    contactType = "accountedit";
                    break;
            }
            String link = contactType + "/" + result.getObjectId() + "/" + result.getAccountObjectId();


            if (Utils.isCRM() && (contactType.equals("contactedit") || contactType.equals("leadedit")) || contactType.equals("accountedit")) {
                SinksContainerFactory.entryPoint.onHistoryChanged(link);
            } else if (contactType.equals("contactedit") || contactType.equals("leadedit") || contactType.equals("accountedit")){
                Utils.openURL("Crm.html#" + link);
            } else if (Utils.isHRMS() && contactType.equals("candidateedit")) {
                SinksContainerFactory.entryPoint.onHistoryChanged(link);
            } else if (contactType.equals("candidateedit")) {
                Utils.openURL("Hrms.html#" + link);
            }
        }
    }

    protected void initWebSocket() {
        try {
            final WebSocket webSocket = WebSocket.newWebSocketIfSupported();
            if (null != webSocket) {
                webSocket.setListener(new WebSocketListenerAdapter() {
                    @Override
                    public void onOpen(final WebSocket webSocket) {
                        // After we have connected we can send

                    }

                    @Override
                    public void onMessage(final WebSocket webSocket, final String data) {
                        WebSocketClientObject wo = JsonUtils.safeEval(data);
                        if (WfmUiEventType.CONNECTED == wo.getEventType()) {
                        } else {
                            onWebSocketMessage(wo);
                        }
                    }

                    @Override
                    public void onError(@Nonnull WebSocket webSocket) {
                    }

                    @Override
                    public void onClose(WebSocket webSocket, boolean wasClean, int code, String reason) {
                        Timer timer = new Timer() {
                            @Override
                            public void run() {
                                if (!webSocket.isConnected()) {
                                    String protocol = Window.Location.getProtocol().contains("https") ? "wss" : "ws";
                                    String host = Window.Location.getHost();
                                    webSocket.connect(getWebSocketConnectionUrl());
                                }
                            }
                        };
                        timer.schedule(3000);
                    }
                });
                String protocol = Window.Location.getProtocol().contains("https") ? "wss" : "ws";
                String host = Window.Location.getHost();
                webSocket.connect(getWebSocketConnectionUrl());

            }
        } catch (Exception e) {
            Info.warn(wfmStrings.checkYourInternetConnection());
        } finally {
            return;
        }
    }

    private String getWebSocketConnectionUrl() {
        String protocol = Window.Location.getProtocol().contains("https") ? "wss" : "ws";
        String host = Window.Location.getHost();
        return protocol + "://" + host + "/echo?sessionId=" + getCompanyId() + "__" + Utils.getUserID();
    }

    private native String getCompanyId() /*-{
        return $wnd.document.cookie.split("$")[1];
    }-*/;

    public void onWebSocketMessage(WebSocketClientObject messageObject) {
        WfmUiEventsBus.fireWfmUiEvent(messageObject.getEventType(), messageObject.getData(), null);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, messageObject.getData(), null);
    }

    public static native void followHScroll() /*-{
        $wnd.follow_h_scroll__init();
    }-*/;

    public static native void logger() /*-{
        $wnd.scrollLogger = function (x, y) {
            @com.edatasite.workforce.gwt.core.client.Utils::logScroll(II)(x, y);
        }
    }-*/;

    public static native void scrollbarEvent() /*-{
        $wnd.contentScroll('.scrollbar-external', '.frame__content')
        $wnd.contentScroll('.frame__nav__scroll', '.frame__nav')
    }-*/;

    private static boolean checkBrowser(String browserName) {
        return (Window.Navigator.getUserAgent().toLowerCase().contains(browserName.toLowerCase()));
    }

    private void refreshPage() {
        /**
         * By default it will show the path name with '/' that is redundant in this case,
         * substring  removes first '/' character  from the section name. So, we receive
         * a section name as '/Backend.html' and send it as 'Backend.html'.
         */
        String section = Utils.getPathName().substring(1) + (Utils.isLocalhost() ? Utils.getParamString() : "");
        String url = GWT.getHostPageBaseURL() + section;
        Window.open(url, "_self", "");
    }

    public String getTime() {
        userSpentTime++;
        return Utils.formatMinutes(userSpentTime);
    }

    private void showAndStartTimer(final LIElement timer, String spent, boolean startTimerRequired) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_TIMER)) {
            if (spent != null) {
                if (!Utils.hasRole(CLIENT)) {
                    timer.getStyle().setVisibility(com.google.gwt.dom.client.Style.Visibility.VISIBLE);
                }
                userSpentTime = Integer.valueOf(spent);
                timer.setInnerText(Utils.formatMinutes(userSpentTime));
            }

            if (startTimerRequired) {
                if (inOutTimer == null) {
                    inOutTimer = new Timer() {
                        public void run() {
                            timer.setInnerText(getTime());
                        }
                    };
                }
                inOutTimer.scheduleRepeating(60000);
            }
        }
    }

    protected void getNewNotificationMsg() {

        notificationService.getNewNotifications(new AsyncCallback<ListResult<NotificationItem>>() {

            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Failed to update push notifications.");
            }

            public void onSuccess(ListResult<NotificationItem> result) {
                mainLayout.getNavToolBar().drawNotifications(result);
            }
        });
    }

    protected void getNewEmailList() {
        if (Utils.isEmailAccountSetup()) {
            drawEmails();
        }
    }

    private void drawEmails() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(5);
        filterParameter.setParams(Constants.FLAG_UNREAD);
        filterParameter.setShowActive(true);
        MessageCenterService.App.get().getEmailsToTop(filterParameter, new AsyncCallback<ListResult<Email>>() {

            @Override
            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(ListResult<Email> result) {
                mainLayout.getNavToolBar().drawEmails(result);
            }
        });
    }

    protected void initRemoveSampleData() {
        if (Utils.adminOrDirector()) {
            Scheduler.get().scheduleDeferred(() -> {
                if (Utils.isTestCompany()) {
                    RemoveSDInfo infoModal = new RemoveSDInfo();
                    FeaturesNotification.showModal(Constants.ENABLE_TO_SHOW_SAMPLE_DATA, infoModal);
                }
                mainLayout.initRemoveSampleLink();
            });
        }
    }

    public TextBox getSearchBox() {
        return searchBox;
    }

    public void onLogin() {
        LoginService.App.get().setTimeZone(ClientSecurityContext.get().getSessionId(), Utils.getParam(SESSION_TRACK_ID),
                (new Date().getTimezoneOffset()), new AbstractAsyncCallback<Void>() {
                });
        initSinksContainerFactory();
        addTabListeners();
        containerFactory.initDefaultContainers();
        containerFactory.registerMenuItems();

        mainLayout.initialize();

        getNewNotificationMsg();
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            getNewEmailList();
        }
        initRemoveSampleData();

        if (historyToken == null || historyToken.equals(PRM + "/") || historyToken.equals("null")) {//temporary hack if we further use PRM
            historyToken = null;
        }

        SinksContainer defaultContainer = null;
        if (historyToken != null) {
            defaultContainer = containerFactory.getSinksContainer(historyToken, true); // activate default tab

            if (defaultContainer != null) {
                mainLayout.getSideNavBar().setSelection(defaultContainer);

                if (defaultContainer.getPreparedView() == null) {
                    defaultContainer.setPreparedView(Utils.getSinkName(historyToken));
                }
                if (historyToken == null && (defaultContainer.getPreparedView() != null)) {
                    defaultContainer.showPrepared();
                }
            }
        }
        RootLayoutPanel.get().add(mainLayout);

        if (defaultContainer != null) {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, defaultContainer, mainLayout.getNavToolBar());
        }

        if (checkBrowser("Firefox")) {
            DOM.sinkEvents(RootPanel.get().getElement(), Event.ONKEYPRESS);
        } else {
            DOM.sinkEvents(RootPanel.get().getElement(), Event.ONKEYDOWN);
        }

        Scheduler.get().scheduleDeferred(() -> {

            if (Utils.adminOrDirector()) {

                if (Utils.userSettings.get(GOOGLE_APP_DOMAIN) != null && !"null".equals(Utils.userSettings.get(GOOGLE_APP_DOMAIN)) && !"".equals(Utils.userSettings.get(GOOGLE_APP_DOMAIN))) {
                    new GoogleMarketPlaceUsersView(true);
                }
            }
        });

        if (Utils.hasGenericAccess(GenericSettingsEnum.SWITCHVOX_ENABLED)) {
            Timer switchvoxRefresh = new Timer() {
                @Override
                public void run() {
                    if (switchvoxErrorCount > 10) {
                        return;
                    }
                    statusService.getIncomingCallerID(new AsyncCallback<SwitchvoxContactItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            switchvoxErrorCount++;
                            if (switchvoxErrorCount == 10) {
                                Info.show(wfmStrings.unableToConnectToSwitchvoxCheckSetting(), Info.Position.BOTTOM_CENTER);
                            }
                        }

                        @Override
                        public void onSuccess(final SwitchvoxContactItem result) {
                            if (result == null) {
                                return;
                            }
                            showIncomingCaller(result);
                        }
                    });
                }
            };
            switchvoxRefresh.scheduleRepeating(1000 * 12);//Check for incoming calls every n seconds
        }

        // It will check for server upload version every 45 minutes.
        final int uploadCheckingPeriod = 1000 * 60 * 45;

        refreshTimer = new Timer() {
            @Override
            public void run() {
                statusService.getLatestServerUploadVersion(new AbstractAsyncCallback<String>() {
                    public void onFailure(Throwable t) {
                    }

                    public void success(String latestUploadVersion) {
                        // If currently no upload versions have been set in DB there is no need to check it.
                        // Thus in else condition we are cancelling the timer.
                        if (latestUploadVersion != null && Utils.userSettings.get(LATEST_SERVER_UPLOAD_VERSION) != null) {
                            String currentUploadVersion = Utils.userSettings.get(LATEST_SERVER_UPLOAD_VERSION);
                            if (!latestUploadVersion.equals(currentUploadVersion)) {

                                WfmMessageBox dialogBox = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, true);
                                dialogBox.setMessage(wfmStrings.theLatestUpgradeessages());
                                dialogBox.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onCancel() {
                                        refreshTimer.scheduleRepeating(uploadCheckingPeriod);
                                    }

                                    @Override
                                    public void onSubmit() {
                                        refreshPage();
                                    }
                                });
                                dialogBox.open();
                            }
                        } else {
                            refreshTimer.cancel();
                        }
                    }
                });
            }
        };

        refreshTimer.scheduleRepeating(uploadCheckingPeriod);
        logger();
        //@TODO To fix JQuery's .scroollbar() issue
        try {
            scrollbarEvent();
        } catch (Exception e) {
            GWT.log("Error Occured: " + e.getMessage());
        }
        followHScroll();
        try {
            Utils.hideDropDownLookUp();
        } catch (Exception ignored) {
        }
        initWebSocket();
        registerConnectivityListeners();
        initScripts();
        obtainPublicIpAddress();
    }

    public static native void registerConnectivityListeners() /*-{
        $wnd.addEventListener('offline', function (ev1) {
            console.log("Internet : Off");
            @com.edatasite.workforce.gwt.core.client.Utils::onOffline()();
        });
        $wnd.addEventListener('online', function (ev1) {
            console.log("Internet : On");
            @com.edatasite.workforce.gwt.core.client.Utils::onOnline()();
        });
    }-*/;

    private static native void obtainPublicIpAddress() /*-{
        var self = this;
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'https://api.ipify.org/?format=json');
        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var publicIPAddress = response.ip;
                localStorage.setItem('public-ip', publicIPAddress);
            } else {
                console.log('Failed to obtain public IP address');
            }
        };
        xhr.send();
    }-*/;

    public void initScripts() {
        AllInOneService.App.get().getScripts(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<String> strings) {
                if (strings != null && !strings.isEmpty()) {
                    for (String script : strings) {
                        if (script != null && !"".equals(script)) {
                            registerScripts(script);
                        }
                    }
                }
            }
        });
    }
}
