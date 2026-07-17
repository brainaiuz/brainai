package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.fakeContainer.PseudoContainerServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.Timer.TimerSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.handlers.CallCommand;
import com.edatasite.workforce.gwt.core.client.ui.communication.phone.PhonePopup;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.sampleData.RemoveSDModal;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.email.EmailSideBarUtil;
import com.edatasite.workforce.gwt.core.client.ui.emailAccount.EmailAccountQuickAdd;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notification.NotificationMenuUtil;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.*;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CRM_CASE;

public class UserMenu extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private static UserMenuUIBinder ourUiBinder = GWT.create(UserMenuUIBinder.class);

    interface UserMenuUIBinder extends UiBinder<MaterialPanel, UserMenu> {
    }


    @UiField
    Div removeSampleContainer;
    @UiField
    Div iconPanel;
    @UiField
    Div panel;
    @UiField
    Div subscribeContent;
    @UiField
    Div subscribeWrapper;


    private ContactDetailsItem contactDetailsItem;
    private CallCommand callCommand;
    private TimerSideNavBox timer;
    private WfmButton2 removeSampleData;
    private Span daysLeft;

    private KpiSideNavBox notificationBox;
    private KpiSideNavBox emailBox;
    private KpiSideNavBox kiaBox;
    private KpiSideNavBox emailAccountSetupBox;

    private Span notificationGrandTotalCountSpan;
    private Span emailGrandTotalCountSpan;
    private KpiSideNavBox settingsBox;
    private Div offsetContent;
    private MaterialPanel settingsContainer;
    private MaterialLink settings;
    private Span settingsIconContainer;
    private MaterialPanel notificationContainer;
    private MaterialLink notificationLink;
    private Span notificationIconContainer;
    private MaterialPanel emailContainer;
    private MaterialLink emailLink;
    private Span emailIconContainer;
    private MaterialPanel kiaContainer;
    private MaterialLink kiaLink;
    private Span kiaIconContainer;


    public UserMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void init() {
        settingsContainer = new MaterialPanel();
        settingsContainer.getElement().addClassName("user-menu-item");
        settings = new MaterialLink();
        settings.getElement().addClassName("button-collapse");
        settingsIconContainer = new Span();
        settingsIconContainer.addStyleName("user-menu-item__icon");
        settingsContainer.add(settings);
        settings.add(settingsIconContainer);

        notificationContainer = new MaterialPanel();
        notificationContainer.getElement().addClassName("user-menu-item");
        notificationLink = new MaterialLink();
        notificationLink.getElement().addClassName("button-collapse");
        notificationIconContainer = new Span();
        notificationIconContainer.addStyleName("user-menu-item__icon");
        notificationContainer.add(notificationLink);
        notificationLink.add(notificationIconContainer);

        emailContainer = new MaterialPanel();
        emailContainer.getElement().addClassName("user-menu-item");
        emailLink = new MaterialLink();
        emailLink.getElement().addClassName("button-collapse");
        emailIconContainer = new Span();
        emailIconContainer.addStyleName("user-menu-item__icon");
        emailContainer.add(emailLink);
        emailLink.add(emailIconContainer);

        kiaContainer = new MaterialPanel();
        kiaContainer.getElement().addClassName("user-menu-item");
        kiaLink = new MaterialLink();
        kiaLink.getElement().addClassName("button-collapse");
        kiaIconContainer = new Span();
        kiaIconContainer.addStyleName("user-menu-item__icon");
        kiaContainer.add(kiaLink);
        kiaLink.add(kiaIconContainer);
        initKia();

        if (!Utils.isClientContact()) {
            showWidgets();
            initNotifications();
            if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
                initEmails();
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_LIST_CHANGE, UserMenu.this, (sender, args) -> reInitEmails());
            }
        }
        initRemoveSD();
        initIfTrialPeriodLeft();
        if (Utils.hasPermission(PermissionConstants.SETTINGS_MAIN_MENU)) {
            initSettings();
        }
    }

    private void initSettings() {

        offsetContent = new Div();

        settingsIconContainer.add(new SvgIcon(SvgEnum.settings));
        settings.setTooltip(wfmStrings.settings());
        settings.setTooltipPosition(Position.BOTTOM);
        settingsBox = new KpiSideNavBox(true);
        settingsBox.addStyleName("user-menu-settings quick-add");
        settingsBox.addHeader(createSettingsHeader());

        settingsBox.addBody(createSettingsbody());
        settings.addClickHandler((e) -> settingsBox.open());
        settingsBox.addOpeningHandler(event -> {
            if (settingsBox.getParent() != null && settingsBox.getParent().getElement().getChildCount() == 1) {
                settingsBox.overlayRemoveFromParent();
                settingsBox = new KpiSideNavBox(true);
                settingsBox.addStyleName("user-menu-settings");
                settingsBox.addHeader(createSettingsHeader());

                settingsBox.addBody(createSettingsbody());
                settingsBox.open();
            }
        });
        panel.add(settingsContainer);
    }

    private Widget createSettingsbody() {
        offsetContent = new Div();
        loadSettingsMenuItems();
        return offsetContent;
    }

    private Widget createSettingsHeader() {
        Div titleGroup = new Div("side-nav__title-group");
        Icon settingsIcon = new Icon();
//        settingsIcon.addStyleName("ficon--settings-bold");
        Span textSpan = new Span();
        textSpan.setText(wfmStrings.settings());
//        titleGroup.add(settingsIcon);
        titleGroup.add(textSpan);
        return titleGroup;
    }


    private void loadSettingsMenuItems() {
        List<PseudoMenuItem> settingsMenuItems = Utils.getPseudoContainer().getSettingsPseudoMenuItems();
        if (settingsMenuItems == null || settingsMenuItems.size() == 0) {
            PseudoContainerServiceAsync.App.get().getSettingsMenuItems(new AsyncCallback<ArrayList<PseudoMenuItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.warn(wfmStrings.error());
                }

                @Override
                public void onSuccess(ArrayList<PseudoMenuItem> result) {
                    Utils.getPseudoContainer().setSettingsPseudoMenuItems(result);
                    offsetContent.add(MainLayout.get().modulesBar.generatePseudoSettingsMenus(Utils.getPseudoContainer().getSettingsPseudoMenuItems()));
                    if (JQuery.$(".collapsible-nested") != null) {
                        JQuery.$(".collapsible-nested").removeAttr("style");
                    }
                }
            });
        } else {
            offsetContent.add(MainLayout.get().modulesBar.generatePseudoSettingsMenus(Utils.getPseudoContainer().getSettingsPseudoMenuItems()));
            if (JQuery.$(".collapsible-nested") != null) {
                JQuery.$(".collapsible-nested").removeAttr("style");
            }
        }
    }


    private void showWidgets() {
        initSwitchCompany();

    }

    private void companyPanel(List<UserCompanyDTO> companyList, MaterialLink companiesLink) {
        if (companyList != null && companyList.size() > 0) {
            PopupPanel panel = new PopupPanel(true);
            panel.setStyleName("gwt-PopupPanel", false);
            panel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                int textboxAbsolTop = companiesLink.getAbsoluteTop();

                public void setPosition(int offsetWidth, int offsetHeight) {
                    if (offsetHeight + companiesLink.getOffsetHeight() < Window.getClientHeight() - textboxAbsolTop) {
                        panel.setPopupPosition(companiesLink.getAbsoluteLeft(), textboxAbsolTop + companiesLink.getOffsetHeight());
                    } else {
                        panel.setPopupPosition(companiesLink.getAbsoluteLeft(), textboxAbsolTop - offsetHeight);
                    }
                }
            });
            if (companyList.size() > 1) {
                CompanySwitcher switcher = new CompanySwitcher(companyList);
                panel.add(switcher);
                panel.show();
            }
        }
    }

    private List<UserCompanyDTO> companySortAction(List<UserCompanyDTO> companyList) {
        return companyList.stream()
                .sorted(Comparator.comparing(UserCompanyDTO::getCompanyName))
                .collect(Collectors.toList());
    }


    private void initSwitchCompany() {
        Scheduler.get().scheduleDeferred(() -> LoginService.App.get().getUserCompanyList(new AsyncCallback<LinkedHashMap<String, ArrayList<UserCompanyDTO>>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(LinkedHashMap<String, ArrayList<UserCompanyDTO>> companyList) {
                List<List<UserCompanyDTO>> companies = new ArrayList<>();
                companies.add(companyList.get("active"));
                companies.add(companyList.get("free"));
                companies.add(companyList.get("expired"));

                List<UserCompanyDTO> comList = new ArrayList<>();
                for (List<UserCompanyDTO> list : companies) {
                    if (list != null) {
                        comList.addAll(companySortAction(list));
                    }
                }
                MaterialLink companiesLink = new MaterialLink();
                companiesLink.setId("compLink");
                companiesLink.addStyleName("dropdown-button");
                companiesLink.setMaxWidth("287px");
                companiesLink.setTooltip(wfmStrings.switchCompany());
                companiesLink.setTooltipPosition(Position.BOTTOM);
                Span span = new Span();
                for (UserCompanyDTO company : comList) {
                    if (company.isCurrent()) {
                        span.setStyle("vertical-align: super");
                        span.setText(company.getCompanyName());
                    }
                }
                SvgIcon chevronIcon = new SvgIcon(SvgEnum.chevronRight);
                companiesLink.add(span);
                companiesLink.add(chevronIcon);
                companiesLink.addClickHandler(event -> {
                    companyPanel(comList, companiesLink);
                });
                if (comList.size() > 1) {
                    iconPanel.add(companiesLink);
                }

                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK) && Utils.getAsteriskSettings() != null && !Utils.getAsteriskSettings().isEmpty()) {
                    initPhone();
                }

                if (Utils.hasPermission(!Utils.isCRM()
                        ? PermissionConstants.PM_TASKS_TIMER
                        : PermissionConstants.CRM_TASKS_TIMER)) {
                    initTimer();
                }
                if (Utils.hasPermission(PermissionConstants.CRM_Calendar)) {
                    initCalendar();
                }
            }
        }));

    }

    private void initRemoveSD() {
        removeSampleData = new WfmButton2(wfmStrings.removeSampleData(), "btn btn--remove-light");
        removeSampleContainer.add(removeSampleData);
    }

    public void initRemoveSampleLink() {
        if (Utils.isTestCompany()) {
            removeSampleContainer.setVisible(true);
            removeSampleData.addClickHandler(c -> {
                RemoveSDModal sdModal = new RemoveSDModal();
                sdModal.setCmdRemoveData(() -> {
                    Utils.userSettings.put(Constants.IS_TEST_COMPANY, "false");
                    removeSampleContainer.removeFromParent();
                    Window.Location.replace(Cookies.getCookie(Constants.SECTION_HTML));
                });
                sdModal.setWidth("690px");
                sdModal.open();
            });
        } else {
            removeSampleContainer.removeFromParent();
        }
    }

    public Widget getRemoveSampleLink() {
        return removeSampleData;
    }

    public void drawEmails(ListResult<Email> listResult) {
        emailBox.clear();
        //figure-heading
        FigureWidget figureHeading = new FigureWidget();
        figureHeading.setStyleName("figure-heading");
        emailBox.addHeaderContainer(figureHeading);

        //figure-image
        Div figureImage = new Div();
        figureImage.setStyleName("figure-image");
        figureImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        figureHeading.add(figureImage);

        //***********************************************************************
        figureImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                emailBox.close();
                Utils.redirect(Utils.getHostURL() + "MessageCenter.html");
            }
        });
        //***********************************************************************

        SvgIcon iconBell = new SvgIcon(SvgEnum.mail);
        figureImage.add(iconBell);

        Span iconCountSpan = new Span();
        iconCountSpan.setStyleName("badge");
        if (listResult.getTotal() > 0) {
            iconCountSpan.setText("" + listResult.getTotal());
            figureImage.add(iconCountSpan);
        }

        //FigCaption
        FigCaption figCaption = new FigCaption();
        figCaption.setStyleName("fs-2");
        figCaption.setText(wfmStrings.messageCenter());
        figureHeading.add(figCaption);

        //Empty panel
        if (listResult.getTotal() == 0 || listResult.getList().size() == 0) {
            Div emptyPanel = new Div();
            notificationBox.addStyleName("no-new-notifications");

            Div emptyBodyPanel = new Div();
            emptyBodyPanel.setStyleName("panel__body");

            Image emptyImage = new Image("/mainStyles/new-ui/images/empty-notification.svg");
            Paragraph emptyText = new Paragraph();
            emptyText.setStyleName("fs-3 mt-4");
            emptyText.setText(wfmStrings.noNewEmails());

            Anchor goToInboxLink = new Anchor();
            goToInboxLink.getElement().setInnerText(wfmStrings.goToInbox());
            goToInboxLink.addClickHandler(clickEvent -> {
                emailBox.close();
                Utils.redirect(Utils.getHostURL() + "MessageCenter.html");
            });

            emptyBodyPanel.add(emptyImage);
            emptyBodyPanel.add(emptyText);
            emptyBodyPanel.add(goToInboxLink);
            emptyPanel.add(emptyBodyPanel);
            emailBox.addBody(emptyPanel);
            emailBox.getContentFooter().addStyleName("side-nav__footer--empty");

            setEmailCount(0L);

            return;
        }

        WfmButton2 clearEmailsButton = new WfmButton2(wfmStrings.clear(), new SvgIcon(SvgEnum.xBold));
        clearEmailsButton.setStyleName("btn btn--clear btn-block");
        clearEmailsButton.addClickHandler(clickEvent -> {
            emailBox.clear();
            iconCountSpan.setText("");

            ArrayList<String> emailIDs = new ArrayList<>();
            listResult.getList().forEach(email -> emailIDs.add(email.getObjectID()));
            MessageCenterService.App.get().setEmailFlags(emailIDs, null, Constants.FLAG_READ, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Void result) {
                    setEmailCount(0L);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, null, null);

                    if (Utils.isMC()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, null, null);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAILS_CLEARED, null, null);
                    }
                }
            });
        });
        emailBox.addHeaderContainer(clearEmailsButton);

        //Body
        ArrayList<MaterialPanel> emailContentList = EmailSideBarUtil.generateEmailList(listResult.getList(), () -> emailBox.hide(), iconCountSpan, emailGrandTotalCountSpan);
        emailContentList.forEach(item -> emailBox.addBody(item));
        ///Footer
        WfmButton2 btnViewAll = new WfmButton2(wfmStrings.viewAll(), new SvgIcon(SvgEnum.menu));
        btnViewAll.setStyleName("btn btn--clear btn-block");
        btnViewAll.addClickHandler(ch -> {
            emailBox.hide();
            Utils.redirect(Utils.getHostURL() + "MessageCenter.html");
        });
        emailBox.addFooter(btnViewAll);

        setEmailCount(Long.valueOf(listResult.getTotal()));
    }

    public void drawNotifications(ListResult<NotificationItem> listResult) {
        notificationBox.clear();

        //figure-heading
        FigureWidget figureHeading = new FigureWidget();
        figureHeading.setStyleName("figure-heading");
        notificationBox.addHeaderContainer(figureHeading);

        //figure-image
        Div figureImage = new Div();
        figureImage.setStyleName("figure-image");
        figureImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        figureHeading.add(figureImage);

        SvgIcon iconBell = new SvgIcon(SvgEnum.bell);
        figureImage.add(iconBell);

        Span iconCountSpan = new Span();
        iconCountSpan.setStyleName("badge");
        if (listResult.getTotal() > 0) {
            iconCountSpan.setText("" + listResult.getTotal());
            figureImage.add(iconCountSpan);
        }

        //FigCaption
        FigCaption figCaption = new FigCaption();
        figCaption.setStyleName("fs-2");
        figCaption.setText(wfmStrings.notifications());
        figureHeading.add(figCaption);

        //Empty panel
        if (listResult.getTotal() == 0 || listResult.getList().size() == 0) {
            Div emptyPanel = new Div();
            notificationBox.addStyleName("no-new-notifications");

            Div emptyBodyPanel = new Div();
            emptyBodyPanel.setStyleName("panel__body");

            Image emptyImage = new Image("/mainStyles/new-ui/images/empty-notification.svg");
            Paragraph emptyText = new Paragraph();
            emptyText.setStyleName("fs-3 mt-4");
            emptyText.setText(wfmStrings.noNewNotifications());

            emptyBodyPanel.add(emptyImage);
            emptyBodyPanel.add(emptyText);
            emptyPanel.add(emptyBodyPanel);

            notificationBox.addBody(emptyPanel);
            notificationBox.getContentFooter().addStyleName("side-nav__footer--empty");

            setNotificaitonCount(0L);

            return;
        }


        WfmButton2 clearButton = new WfmButton2(wfmStrings.clear(), new SvgIcon(SvgEnum.xBold));
        clearButton.setStyleName("btn btn--clear btn-block");
        clearButton.addClickHandler(clickEvent -> {
            notificationBox.clear();
            iconCountSpan.setText("");

            NotificationMsgService.App.get().clearAll(new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Void result) {
                    setNotificaitonCount(0L);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);

                    if (Utils.isHRMS()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, null, null);
                    }
                }
            });
        });
        notificationBox.addHeaderContainer(clearButton);

        ArrayList<MaterialPanel> notificationContentList = NotificationMenuUtil.generateMenuList(listResult.getList(), () -> notificationBox.hide(), iconCountSpan, notificationGrandTotalCountSpan);
        notificationContentList.forEach(item -> notificationBox.addBody(item));

        if (Utils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            WfmButton2 btnViewAll = new WfmButton2(wfmStrings.viewAll(), new SvgIcon(SvgEnum.menu));
            btnViewAll.setStyleName("btn btn--clear btn-block");
            btnViewAll.addClickHandler(ch -> {
                notificationBox.hide();
                if (Utils.isHRMS()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("#hrmsMain|notifications");
                } else {
                    String notificationUrl = GWT.getHostPageBaseURL() + Constants.HRMS_URL + "#hrmsMain|notifications";
                    Window.open(notificationUrl, "", "");
                }
            });
            notificationBox.addFooter(btnViewAll);
        } else {
            notificationBox.getContentFooter().addStyleName("side-nav__footer--empty");
        }

        setNotificaitonCount(Long.valueOf(listResult.getTotal()));
    }

    private void setEmailCount(Long totalCount) {
        if (totalCount == null || totalCount == 0L) {
            emailGrandTotalCountSpan.setText("");
            emailGrandTotalCountSpan.setVisible(false);
            return;
        }

        String prefix = "";
        String suffix = "";
        if (totalCount >= 1000L) {
            totalCount /= 1000L;
            prefix = "+";
            suffix = "k";
        } else if (totalCount > 99L) {
            totalCount = 99L;
            prefix = "+";
        }

        totalCount = totalCount > 1000 ? totalCount / 1000L : totalCount > 100 ? 99 : totalCount;
        emailGrandTotalCountSpan.setText(prefix + totalCount + suffix);
        emailGrandTotalCountSpan.setVisible(true);
        //emailIconContainer.add(emailGrandTotalCountSpan);
    }

    private void setNotificaitonCount(Long totalCount) {
        if (totalCount == null || totalCount == 0L) {
            notificationGrandTotalCountSpan.setText("");
            notificationGrandTotalCountSpan.setVisible(false);
            return;
        }

        String prefix = "";
        String suffix = "";
        if (totalCount >= 1000L) {
            totalCount /= 1000L;
            prefix = "+";
            suffix = "k";
        } else if (totalCount > 99L) {
            totalCount = 99L;
            prefix = "+";
        }

        totalCount = totalCount > 1000 ? totalCount / 1000L : totalCount > 100 ? 99 : totalCount;
        notificationGrandTotalCountSpan.setText(prefix + totalCount + suffix);
        notificationGrandTotalCountSpan.setVisible(true);
        //notificationIconContainer.add(notificationGrandTotalCountSpan);
    }

    private void initNotifications() {

        notificationIconContainer.add(new SvgIcon(SvgEnum.bell));

        notificationLink.setTooltip(wfmStrings.notifications());
        notificationLink.setTooltipPosition(Position.BOTTOM);

        notificationLink.addClickHandler(clickEvent -> {
            notificationBox.open();
            notificationBox.enableScrollUpDownEventAction();
        });

        notificationGrandTotalCountSpan = new Span();
        notificationGrandTotalCountSpan.addStyleName("badge");
        notificationGrandTotalCountSpan.setVisible(false);
        notificationIconContainer.add(notificationGrandTotalCountSpan);

        notificationBox = new KpiSideNavBox(true);
        notificationBox.addStyleName("quick-add--notifications");
        notificationBox.addClosedHandler(closeEvent -> notificationBox.disableScrollUpDownEventAction());
        notificationBox.addOpeningHandler(o -> {

            if (notificationBox.getBody().getWidgetCount() == 0) {
                HTML noNewNotifications = new HTML(wfmStrings.noNewNotifications());
                notificationBox.addStyleName("alert-block__no-new-notifications");
                notificationBox.addBody(noNewNotifications);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, UserMenu.this, (sender, args) -> loadNotificationsCount(args));
        panel.add(notificationContainer);
    }

    private void reInitEmails() {
        Scheduler.get().scheduleDeferred(this::loadEmailCount);
    }

    private void initEmails() {

        emailIconContainer.add(new SvgIcon(SvgEnum.mail));

        emailLink.setTooltip(wfmStrings.emails());
        emailLink.setTooltipPosition(Position.BOTTOM);

        emailGrandTotalCountSpan = new Span();
        emailGrandTotalCountSpan.addStyleName("badge");
        emailGrandTotalCountSpan.setVisible(false);
        emailIconContainer.add(emailGrandTotalCountSpan);

        if (Utils.isEmailAccountSetup()) {
            Scheduler.get().scheduleDeferred(this::loadEmailCount);
            emailBox = new KpiSideNavBox(true);
            emailBox.addStyleName("quick-add--messages");
            emailLink.addClickHandler(clickEvent -> {
                emailBox.open();
                emailBox.enableScrollUpDownEventAction();
            });
            emailBox.addClosedHandler(closeEvent -> emailBox.disableScrollUpDownEventAction());
        } else {
            emailLink.addClickHandler(clickEvent -> new EmailAccountQuickAdd());
        }
        panel.add(emailContainer);
    }

    private void initKia() {

        kiaIconContainer.clear();
        kiaIconContainer.add(new SvgIcon(SvgEnum.aiIcon));

        kiaLink.setTooltip("AI Reporting Engine");
        kiaLink.setTooltipPosition(Position.BOTTOM);

        kiaLink.addClickHandler(clickEvent -> {
            kiaBox.open();

            AIReportWidget aiReportWidget = new AIReportWidget();
            aiReportWidget.setOnCloseListener(() -> kiaBox.close());
            CommonService.App.get().getEmployeeProfile(new AsyncCallback<EmployeeProfileItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    aiReportWidget.setEmployeeProfileItem(null);
                }

                @Override
                public void onSuccess(EmployeeProfileItem item) {
                    aiReportWidget.setEmployeeProfileItem(item);
                }
            });
            kiaBox.addBody(aiReportWidget);
        });

        kiaBox = new KpiSideNavBox(true,500);
        kiaBox.addStyleName("hasChat");
        kiaBox.removeHeader();
        kiaBox.removeFooter();

        panel.add(kiaContainer);
    }

    private void loadNotificationsCount(Object data) {

        NotificationMsgService.App.get().getNotificationsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Long totalCount) {
                setNotificaitonCount(totalCount);
            }
        });
        if (data != null) {
            Info.show(data.toString(), Info.Type.INFO, Info.Position.BOTTOM_RIGHT, 6000);
        } else {
            GWT.log("data is null");
        }
    }

    private void loadEmailCount() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setParams(Constants.FLAG_UNREAD);
        filterParameter.setShowActive(true);
        MessageCenterService.App.get().getEmailsCountToTop(filterParameter, new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable throwable) {
                setEmailCount(0L);
            }

            public void onSuccess(Integer count) {
                setEmailCount(Long.valueOf(count));
            }
        });
    }

    private class IconFigure extends Div {
        private Span badge;

        public IconFigure(SvgIcon icon) {
            super("user-menu-item");
            MaterialLink link = new MaterialLink();
            link.addStyleName("button-collapse");
            link.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            add(link);
            Span umicon = new Span();
            umicon.addStyleName("user-menu-item__icon");
            umicon.add(icon);
            badge = createBadge();
            umicon.add(badge);
            link.add(umicon);
        }

        public Span createBadge() {
            Span badge = new Span();
            badge.addStyleName("badge");
            badge.setVisible(false);
            return badge;
        }

        public void setBadge(Integer count) {
            if (count == null || count == 0) {
                badge.setText("");
                badge.setVisible(false);
                return;
            }
            badge.setText("" + count);
            badge.setVisible(true);
        }

        public void setBadge(Long count) {
            if (count == null || count == 0L) {
                badge.setText("");
                badge.setVisible(false);
                return;
            }
            String prefix = "";
            String suffix = "";
            if (count >= 1000L) {
                count /= 1000L;
                prefix = "+";
                suffix = "k";
            } else if (count > 99L) {
                count = 99L;
                prefix = "+";
            }
            badge.setText(prefix + "" + count);
            badge.setVisible(true);
        }

    }


    private void initPhone() {
        SvgIcon phoneSvg = new SvgIcon(SvgEnum.phone);
        IconFigure phoneLink = new IconFigure(phoneSvg);
        phoneLink.setTooltip(wfmStrings.call());
        phoneLink.setTooltipPosition(Position.BOTTOM);
        phoneLink.addClickHandler(event -> {
            getCallPopup(phoneLink);
        });
        iconPanel.add(phoneLink);
    }

    public void getCallPopup(IconFigure phoneLink) {
        PopupPanel callPanel = new PopupPanel(true);
        callPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            int phoneLinkAbsoluteTop = phoneLink.getAbsoluteTop();

            //
            public void setPosition(int offsetWidth, int offsetHeight) {
                if (offsetHeight + phoneLink.getOffsetHeight() < Window.getClientHeight() - phoneLinkAbsoluteTop) {
                    callPanel.setPopupPosition(phoneLink.getAbsoluteLeft() - 305, phoneLinkAbsoluteTop + phoneLink.getOffsetHeight());
                } else {
                    callPanel.setPopupPosition(phoneLink.getAbsoluteLeft() - 305, phoneLinkAbsoluteTop - offsetHeight);
                }
            }
        });
        PhonePopup phonePopup = new PhonePopup();
        callPanel.add(phonePopup);
        callPanel.getElement().getStyle().setPadding(0, Style.Unit.PX);
        callPanel.show();
    }

    SvgIcon tIcon = new SvgIcon(SvgEnum.clock);

    private void initTimer() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMER_STARTED, UserMenu.this,
                (sender, args) -> {
                    updateTimer();
                });

        IconFigure timerLink = new IconFigure(tIcon);
        timerLink.setTooltip(wfmStrings.timer());
        timerLink.setTooltipPosition(Position.BOTTOM);
        timerLink.addClickHandler(clickEvent -> openTimer());
        iconPanel.add(timerLink);
        //Will set animated icon if timer is active
        updateTimer();
    }

    private void initCalendar() {
        SvgIcon calendarSvg = new SvgIcon(SvgEnum.calendar);
        IconFigure calendarLink = new IconFigure(calendarSvg);
        calendarLink.setTooltip(wfmStrings.calendar());
        calendarLink.setTooltipPosition(Position.BOTTOM);
        calendarLink.addClickHandler(event -> {
            getCalendarPopup(calendarLink);
        });
        iconPanel.add(calendarLink);
    }

    private void getCalendarPopup(IconFigure calendarLink) {
        PopupPanel panel = new PopupPanel(true);
        panel.addStyleName("calendarPopup");
        panel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            int textboxAbsolTop = calendarLink.getAbsoluteTop();

            public void setPosition(int offsetWidth, int offsetHeight) {
                if (offsetHeight + calendarLink.getOffsetHeight() < Window.getClientHeight() - textboxAbsolTop) {
                    panel.setPopupPosition(calendarLink.getAbsoluteLeft() - 285, textboxAbsolTop + calendarLink.getOffsetHeight());
                } else {
                    panel.setPopupPosition(calendarLink.getAbsoluteLeft() - 285, textboxAbsolTop - offsetHeight);
                }
            }
        });

        MaterialPanel mainPanel = new MaterialPanel();

        MaterialPanel calendarPanel = new MaterialPanel();
        CalendarDatePicker calendarView = new CalendarDatePicker(false);
        calendarView.addValueChangeHandler(changeEvent -> {
            panel.hide();
            DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy");
            String selectedDate = dateFormat.format(calendarView.getValue());
            SinksContainerFactory.entryPoint.onHistoryChanged("calendar|add/view/" + 2 + "/" + selectedDate, wfmStrings.calendar());
        });
        calendarPanel.add(calendarView);
        mainPanel.add(calendarPanel);

        MaterialPanel footerPanel = new MaterialPanel("pg_leave__calendar-marks main-datepicker-links");
        MaterialLink day = new MaterialLink(wfmStrings.day());
        day.addClickHandler(event -> {
            panel.hide();
            SinksContainerFactory.entryPoint.onHistoryChanged("calendar|add/view/" + 0, wfmStrings.calendar());
        });
        MaterialLink week = new MaterialLink(wfmStrings.week());
        week.addClickHandler(event -> {
            panel.hide();
            SinksContainerFactory.entryPoint.onHistoryChanged("calendar|add/view/" + 1, wfmStrings.calendar());
        });
        MaterialLink month = new MaterialLink(wfmStrings.month());
        month.addClickHandler(event -> {
            panel.hide();
            SinksContainerFactory.entryPoint.onHistoryChanged("calendar|add/view/" + 2, wfmStrings.calendar());
        });
        footerPanel.add(day);
        footerPanel.add(week);
        footerPanel.add(month);
        mainPanel.add(footerPanel);

        panel.add(mainPanel);
        panel.show();
    }

    public void updateTimer() {
        ClockWidgetService.App.get().getHistoryClockItem(new AbstractAsyncCallback<ClockItem>() {
            @Override
            public void success(ClockItem result) {
                if (result != null) {
                    tIcon.addClassName("rotation");
                } else {
                    tIcon.removeClassName("rotation");
                }
            }
        });
    }

    private void openTimer() {
        if (timer == null) {
            timer = Utils.isPM() ? new TimerSideNavBox() : new TimerSideNavBox(null, null, CRM_CASE);
            timer.open();
        } else {
            timer.open();
        }
    }

    private void initIfTrialPeriodLeft() {
        initSubscribeContent();
        Scheduler.get().scheduleDeferred(this::calculateTrialPeriod);
    }

    private void calculateTrialPeriod() {
        Boolean isPaidCompany = Utils.getParam(Constants.IS_PAID_COMPANY) != null
                ? Boolean.valueOf(Utils.getParam(Constants.IS_PAID_COMPANY))
                : Boolean.FALSE;
        String daysString = Utils.getParam(Constants.FREE_TRIAL_DAYS_LEFT);

        if (daysString != null && !daysString.trim().isEmpty()) {
            Integer days = Integer.valueOf(daysString);
            if (isPaidCompany) {
                if (days != null && days != 0 && days <= 7 && (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.HR) || Utils.hasRole(Constants.ACCOUNTANT))) {//max 1 week (7 days)
                    generateTrialMessage(days, true);
                }
            } else {
                if (days != null && days != 0) {
                    if (Utils.adminOrDirector()) {
                        generateTrialMessage(days, false);
                    }
                }
            }
        }
    }

    private void generateTrialMessage(Integer days, boolean paidCompany) {
        if (days < 30 || !paidCompany) {
            subscribeWrapper.setVisible(true);
        }
        if (paidCompany) {
            daysLeft.setText(coreMessages.paidDaysLeft("" + days));
        } else {
            daysLeft.setText(coreMessages.xDaysLeft("" + days));
        }
    }


    private void initSubscribeContent() {
        daysLeft = new Span();
        WfmButton2 trial = new WfmButton2(wfmStrings.upgrade(), "btn btn--trial");
        trial.addClickHandler(e -> {
            Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html");
        });
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            subscribeContent.add(daysLeft);
            subscribeContent.add(trial);
        }
    }

    public void setTimerData(Integer busObjectId, Integer type, Integer projectID) {

        if (timer == null) {
            timer = new TimerSideNavBox(projectID, busObjectId, type);
        } else {
            //Clear Form fields
            timer.clearForm();

            if (type != Constants.CRM_CASE) {
                timer.setProjectID(projectID);
            }
            timer.setBusObjectId(busObjectId);
            timer.setEntityType(type);

        }
        timer.open();

    }

}
