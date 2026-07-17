package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.CollapsiblePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.01.2009
 * Time: 12:53:20
 * To change this template use File | Settings | File Templates.
 */

public class CompanyEmailSettings extends View implements EmailNotificationConstants {

    private Map<String, Map<String, KpiSwitcher>> companyAllCheckBoxes = new HashMap<>();
    private UnorderedList mainPanel;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private ProfileServiceAsync profileService = ProfileService.App.get();
    private boolean notification = true;

    public CompanyEmailSettings() {
        super("companyEmailSettings", settingsStrings.emailNotifications());
    }

    @Override
    public FlowPanel getHelpContainer() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "icon-settings-e-mail-notification";
    }

    @Override
    protected Widget onInitialize() {
        Div generalPanel = new Div("contentBar companyEmailSettings--genPanel");
        generalPanel.setStyle("padding:0 10px;");
        mainPanel = new UnorderedList("collapsible--panels collapsible--arrows-left collapsible collapsible--gwt");

        final KpiRadioButton myNotification = new KpiRadioButton(settingsStrings.myEmailNotification());
        myNotification.setValue(notification);
        myNotification.setText(settingsStrings.myEmailNotification());
        myNotification.addValueChangeHandler(booleanValueChangeEvent -> {
            clear();
            notification = true;
            onInitialize();
        });
        final KpiRadioButton companyNotification = new KpiRadioButton(wfmStrings.companyEmailNotifications());
        companyNotification.setValue(!notification);
        companyNotification.setText(settingsStrings.companyEmailNotification());
        companyNotification.addValueChangeHandler(booleanValueChangeEvent -> {
            clear();
            notification = false;
            onInitialize();
        });
        if (Utils.hasRole(Constants.ADMIN)) {
            generalPanel.add(asOffsetRadio(myNotification, companyNotification));
        }

        initialize(notification);
        generalPanel.add(mainPanel);

        add(generalPanel);
        add(createFooter());
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return CompanyEmailSettings.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return CompanyEmailSettings.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save(false));
        saveButton.ensureDebugId("save");
        WfmButton2 applyExistUsers = new WfmButton2(wfmStrings.applyToExistingUsers(), (ClickHandler) clickEvent -> {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,
                    wfmStrings.wouldYouLikeToApplyTheseSettingsToAllOfYourExistingUsersToo(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    save(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.setWidth("300px");
            wfmMessageBox.center();
        });
        applyExistUsers.ensureDebugId("applyExistUsers");

        Div saveWrapper = new Div();
        saveWrapper.add(saveButton);

        Div applyExistUsersWrapper = new Div();
        applyExistUsersWrapper.add(applyExistUsers);

        if (notification) {
            rightSideWidgets.add(saveWrapper);
        }
        if (Utils.hasRole(Constants.ADMIN) && !notification) {
            rightSideWidgets.add(applyExistUsersWrapper);
        }

        return rightSideWidgets;
    }

    private Div asOffsetRadio(Widget... widgets) {
        GColumn col = new GColumn(GColumnEnum.COL_6);
        GRow row = new GRow();
        for (Widget widget : widgets) {
            GColumn field = new GColumn(GColumnEnum.COL_6);
            field.add(widget);
            row.add(field);
        }
        col.add(row);
        return col;
    }

    private Div asOffsetSwitcher(String text, Widget widget) {
        GRow div = new GRow();
        div.addStyleName("margin-bottom");
        GColumn label = new GColumn(GColumnEnum.COL_6);
//        label.addStyleName("offset-3");
        label.add(new HTML(text));
        GColumn field = new GColumn(GColumnEnum.COL_3);
        field.add(widget);
        div.add(label);
        div.add(field);
        return div;
    }

    private void initialize(boolean isUserNotification) {
        LoadingPanel.loading(true);
        if (Utils.hasRole(Constants.CLIENT) || isUserNotification) {
            profileService.getUserEmailNotificationSettings(new AbstractAsyncCallback<HashMap<String, HashSet<SelectItem>>>() {
                @Override
                public void success(HashMap<String, HashSet<SelectItem>> result) {
                    LoadingPanel.loading(false);
                    redraw(result);
                }
            });
        } else {
            profileService.getCompanyEmailNotificationSettings(new AbstractAsyncCallback<HashMap<String, HashSet<SelectItem>>>() {
                @Override
                public void success(HashMap<String, HashSet<SelectItem>> result) {
                    LoadingPanel.loading(false);
                    redraw(result);
                }
            });
        }
    }

    private void redraw(HashMap<String, HashSet<SelectItem>> result) {
        if (result != null && result.size() > 0) {
            String categories[] = (new String[]{CATEGORY_PM, CATEGORY_HRMS, CATEGORY_ACCOUNTING});
            for (final String key : categories) {
                if (result.get(key) != null) {
                    String categoryName = key;
                    if (CATEGORY_CALENDAR.equals(key)) {
                        categoryName = wfmStrings.calendarNotifications();
                    } else if (CATEGORY_PM.equals(key)) {
                        categoryName = wfmStrings.projectManagementNotifications();
                    } else if (CATEGORY_CRM.equals(key)) {
                        categoryName = wfmStrings.crmNotifications();
                    }
//                    else if (CATEGORY_COO.equals(key)) {
//                        categoryName = wfmStrings.cooNotifications();
//                    }
                    else if (CATEGORY_HRMS.equals(key)) {
                        categoryName = wfmStrings.hrmsNotifications();
                    } else if (CATEGORY_ACCOUNTING.equals(key)) {
                        categoryName = wfmStrings.accountingNotifications();
                    }
                    CollapsiblePanel slideBox = new CollapsiblePanel(categoryName);
                    slideBox.setActive(true);
                    GColumn column1 = new GColumn(GColumnEnum.COL_6);
                    GColumn column2 = new GColumn(GColumnEnum.COL_6);

                    int i = 0;
                    int size = result.get(key).size();
                    Map<String, KpiSwitcher> categoryCheckBoxMap = new HashMap<>();
                    for (SelectItem selectItem : result.get(key)) {
                        KpiSwitcher switcher = new KpiSwitcher();
                        switcher.setValue(selectItem.isNewItem());
                        categoryCheckBoxMap.put(selectItem.getName(), switcher);
                        if (i < size / 2) {
                            column1.add(asOffsetSwitcher(selectItem.getDescription(), switcher));
                        } else {
                            column2.add(asOffsetSwitcher(selectItem.getDescription(), switcher));
                        }
                        i++;
                    }
                    companyAllCheckBoxes.put(key, categoryCheckBoxMap);
                    slideBox.addColumn(column1);
                    slideBox.addColumn(column2);
                    mainPanel.add(slideBox);
                }
            }
        }
    }

    private void save(boolean isExistingUsers) {
        HashMap<String, HashSet<SelectItem>> checkBoxes = new HashMap<>();
        for (Map.Entry<String, Map<String, KpiSwitcher>> category : companyAllCheckBoxes.entrySet()) {
            HashSet<SelectItem> checkedItems = new HashSet<>();
            String categoryName = category.getKey();
            for (Map.Entry<String, KpiSwitcher> entry : category.getValue().entrySet()) {
                KpiSwitcher checkBox = entry.getValue();
                SelectItem s = new SelectItem();
                s.setName(entry.getKey());
                if (checkBox != null) {
                    s.setNewItem(checkBox.getValue());
                }
                checkedItems.add(s);
            }
            checkBoxes.put(categoryName, checkedItems);
        }
        LoadingPanel.loading(true);
        if (Utils.hasRole(Constants.CLIENT) || !isExistingUsers) {
            profileService.saveUserEmailNotifications(checkBoxes, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void aVoid) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.email()), Info.Type.INFO);
                }
            });
        } else {
            ProfileService.App.get().saveCompanyEmailNotifications(checkBoxes, isExistingUsers, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void aVoid) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SAVE_COMPANY_EMAIL_NOTIFICATIONS, aVoid, CompanyEmailSettings.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.email()), Info.Type.INFO);
                }
            });
        }
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
