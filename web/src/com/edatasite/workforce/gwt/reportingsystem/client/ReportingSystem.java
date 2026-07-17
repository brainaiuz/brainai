package com.edatasite.workforce.gwt.reportingsystem.client;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.reportingsystem.client.factory.ReportingSystemSinksContainerFactory;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Virus on 9/10/14.
 */
public class ReportingSystem extends WorkforceEntryPoint {
    public static List<ReportingCategoryRPC> categories = new ArrayList<>();

    @Override
    public void initSinksContainerFactory() {
        this.containerFactory = new ReportingSystemSinksContainerFactory(this);
        String urlParams = Cookies.getCookie("urlParams");
        if (!Utils.isNullOrEmpty(urlParams)) {
            Cookies.removeCookie("urlParams");
            SinksContainerFactory.entryPoint.onHistoryChanged(urlParams.replace("\"", ""));
        }
    }

    @Override
    protected void initUserSettings() {
        loadOnboardingCustomSteps();
    }

    @Override
    protected void loadOnboardingCustomSteps() {
        ReportingService.App.get().getCategories(new AsyncCallback<ArrayList<ReportingCategoryRPC>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<ReportingCategoryRPC> reportingCategoryRPCS) {
                ReportingSystem.this.categories = reportingCategoryRPCS;
                initDefaultUserSettings();
            }
        });
    }

    public void onModuleLoad() {

        Cookies.removeCookie(LAST_REQUEST_TIME);//There should be no rpc before this!
        initWfmCustomParams(); //initialize wfp params(by normurod)
        initSinksContainerFactory();
        mainLayout = MainLayout.get();
        mainLayout.mutateBodyWithFrameContent2(true);
        String sessionId = Cookies.getCookie(SESSION_ID_COOKIE);
        if (sessionId != null && sessionId.length() > 0) {
            ClientSecurityContext.get().setSessionId(sessionId);

            obtainUserSettings();
            loadUserPermissions();

            String[] uriArray = Utils.getPathName().split("/");
            if (!"/Myaccount.html".equals(Utils.getPathName())) {
                Cookies.setCookie(SECTION_HTML, uriArray[uriArray.length - 1]);
            }
        } else {
            Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
        }
    }

    @Override
    public void onLogin() {
//        urls = UiSettings.getInstance();
        LoginService.App.get().setTimeZone(ClientSecurityContext.get().getSessionId(), Utils.getParam(SESSION_TRACK_ID),
                (new Date().getTimezoneOffset()), new AbstractAsyncCallback<Void>() {
                });
        containerFactory.initDefaultContainers();
        containerFactory.registerMenuItems();
        addTabListeners();

        mainLayout.initialize();

        if (historyToken == null || historyToken.equals(PRM + "/") || historyToken.equals("null")) {//temporary hack if we further use PRM
            historyToken = null;
        }
        RootLayoutPanel.get().add(getMainLayout());
        getNewNotificationMsg();
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            getNewEmailList();
        }

        SinksContainer defaultContainer = containerFactory.getSinksContainer(historyToken, true); // activate default tab
        if (defaultContainer != null) {
            if (historyToken == null && (defaultContainer.getPreparedView() != null)) {
                defaultContainer.showPrepared();
            }
        }

        mainLayout.setSideNavResizeCommand(() -> {

            Timer timer = new Timer() {
                @Override
                public void run() {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDENAV_RESIZE, null, null);
                }
            };
            timer.schedule(320);
        });
        //scroll bar
        logger();
        scrollbarEvent();
        followHScroll();
//        initWebSocket();
    }

    protected void loadUserPermissions() {
        //Load current user's permissions, all permissions of his roles are accumulated
        RolePermissionService.App.get().getPermissionSettings(PermissionConstants.REPORTING, new AbstractAsyncCallback<PermissionSettings>() {
            @Override
            public void failure(Throwable t) {
                loadOnboardingCustomSteps();
            }

            @Override
            public void success(PermissionSettings settings) {
                Utils.setSettings(settings);
                if (!Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                    Utils.redirect(GWT.getHostPageBaseURL() + Constants.DEFAULT_SECTION + ".html");
                }
                loadOnboardingCustomSteps();
            }
        });
    }

    public SinksContainer onHistoryChanged(String historyToken) {
        SinksContainer container = containerFactory.getSinksContainer(historyToken);

        String[] params = Utils.parseAnchorParam(historyToken).getTokens();

        if (params.length > 2 && !Utils.isNullOrEmpty(params[2])) {
            container.setDescription(params[2]);
        }
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.UPDATE_TAB_TITLE, container, mainLayout.getNavToolBar());
        return container;
    }

    public AsteriskCallHandler initializeAsteriskCallHandler(List<AsteriskSettings> asteriskSettings, String userFullName) {
        AsteriskCallHandler asteriskCallHandler = new AsteriskCallHandler(asteriskSettings, userFullName);
        asteriskCallHandler.setIncommingCallCommand((incomingNumber) -> {
            CommonService.App.get().getIncomingCallerDetails(incomingNumber, new AsyncCallback<TwilioContactItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    GWT.log("Error: ", throwable);
                }

                @Override
                public void onSuccess(TwilioContactItem twilioContactItem) {

                    if (twilioContactItem != null) {
                        ContactDetailsItem incomingCallerDetails = new ContactDetailsItem();
                        incomingCallerDetails.setId(twilioContactItem.getObjectID());
                        incomingCallerDetails.setOwner(twilioContactItem.getOwner());
                        incomingCallerDetails.setOwnerId(twilioContactItem.getOwnerId());
                        if (twilioContactItem.getMobile() != null && !twilioContactItem.getMobile().isEmpty()) {
                            incomingCallerDetails.setMobile(twilioContactItem.getMobile().get(0));
                        }
                        incomingCallerDetails.setName(twilioContactItem.getName() != null ? twilioContactItem.getName() : incomingNumber);
                        incomingCallerDetails.setPhoneNumber(incomingNumber);
                        incomingCallerDetails.setPrimaryEmail(twilioContactItem.getEmail());
                        incomingCallerDetails.setContactType(twilioContactItem.getContactType());
                        incomingCallerDetails.setOtherFields(twilioContactItem.getOtherTypes());
                        if (twilioContactItem.getVacancy() != null) {
                            incomingCallerDetails.setVacancy(twilioContactItem.getVacancy());
                        }
                        if (twilioContactItem.getStatus() != null) {
                            incomingCallerDetails.setStatus(twilioContactItem.getStatus());
                        }
                        if (twilioContactItem.getEmployee() != null) {
                            incomingCallerDetails.setEmployee(twilioContactItem.getEmployee());
                        }
                        if (twilioContactItem.getCompany() != null) {
                            incomingCallerDetails.setCompany(twilioContactItem.getCompany());
                        }
                        if (twilioContactItem.getCompanyId() != null) {
                            incomingCallerDetails.setCompanyId(twilioContactItem.getCompanyId());
                        }
                        if (twilioContactItem.getOpportunity() != null) {
                            incomingCallerDetails.setOpportunity(twilioContactItem.getOpportunity());
                        }
                        if (twilioContactItem.getAccountIndustry() != null) {
                            incomingCallerDetails.setAccountIndustry(twilioContactItem.getAccountIndustry());
                        }
                        if (twilioContactItem.getEmployee() != null) {
                            incomingCallerDetails.setEmployee(twilioContactItem.getEmployee());
                        }

                        RelationItem relationItem = RelationItem.newEventRelation(twilioContactItem.getContactType() == null || !twilioContactItem.getContactType().equals(5) //LEAD_CONTACT=5
                                        ? RelationItem.TYPE_CONTACT
                                        : RelationItem.TYPE_LEAD,
                                twilioContactItem.getObjectID(),
                                twilioContactItem.getName() != null ? twilioContactItem.getName() : incomingNumber);

                        ContactListItem contactListItem = new ContactListItem();
                        contactListItem.setObjectId(twilioContactItem.getObjectID());
                        contactListItem.setContactType(twilioContactItem.getContactType());
                        contactListItem.setContactName(twilioContactItem.getName());


                        incomingCallerDetails.setTaskCommand((s) -> new TaskQuickAddView(s, relationItem));
                        incomingCallerDetails.setSmsCommand((s) -> new ActivityQuickAddForm(Appointment.SMS, contactListItem, s, relationItem));
                        incomingCallerDetails.setCallCommand((s) -> new ActivityQuickAddForm(Appointment.CALL_LOG, incomingNumber, contactListItem, s, relationItem));
                        incomingCallerDetails.setEventCommand((s) -> new ActivityQuickAddForm(Appointment.EVENT, incomingNumber, contactListItem, s, relationItem));

                        asteriskCallHandler.setIncomingCallerDetails(incomingCallerDetails);
                    }
                }
            });
        });
        return asteriskCallHandler;
    }
}
