package com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Azazello on 2/5/16.
 */
public class TwilioSettingsListView extends BaseListView implements Constants, Colapse {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private static final TwilioServiceAsync service = TwilioService.App.get();
    private ListingPanel<TwilioSettings> list;

    public TwilioSettingsListView() {
        super(TWILIO_SETTINGS_LIST, settingsStrings.twilioAccounts());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.TwilioSettingListPanel, getColumnConfigs(), getListData(), getDisagn());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TWILIO_SETTINGS_ADD_EDIT, TwilioSettingsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];
        //////////////////////////---------(0)----------
        columns[0] = new ColumnDefinitionConfig<TwilioSettings, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TwilioSettings item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("Twilio_setting_edit_button");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("twilioSetting|add/add/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem MenuItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                MenuItem.getElement().setId("Twilio_setting_delete_button");
                MenuItem.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(profileMessages.messAreDeleteSMSAccount(item.getNumber()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            service.delete(item.getObjectID(), new AbstractAsyncCallback() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(Object result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TWILIO_SETTINGS_ADD_EDIT, result, TwilioSettingsListView.this);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                menuBar.addItem(MenuItem);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        //////////////////////////---------(1)----------
        columns[1] = new ColumnDefinitionConfig<TwilioSettings, String>(wfmStrings.number(), TwilioSettings.NUMBER, 100) {
            @Override
            public String getCellValue(TwilioSettings item) {
                return item.getNumber();
            }
        };
        columns[1].setMinimumColumnWidth(40);
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(2)----------
//        columns[2] = new ColumnDefinitionConfig<TwilioSettings, String>(wfmStrings.accountSID(), TwilioSettings.SID, 100) {
//            @Override
//            public String getCellValue(TwilioSettings item) {
//                return item.getAccountSid();
//            }
//        };
//        columns[2].setMinimumColumnWidth(40);
//        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//
//        columns[3] = new ColumnDefinitionConfig<TwilioSettings, String>(wfmStrings.accessToken(), TwilioSettings.TOKEN, 100) {
//            @Override
//            public String getCellValue(TwilioSettings item) {
//                return item.getAccountSid();
//            }
//        };
//        columns[3].setMinimumColumnWidth(40);
//        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//
//        columns[5] = new ColumnDefinitionConfig<TwilioSettings, String>(wfmStrings.applicationSID(), TwilioSettings.TOKEN, 100) {
//            @Override
//            public String getCellValue(TwilioSettings item) {
//                return item.getAccountSid();
//            }
//        };
//        columns[5].setMinimumColumnWidth(40);
//        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.EmailFilterListPanel;
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("twilioSetting|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoTwilioAccounts());
                message.setHref("twilioSetting|add/add");
                message.setTextBeforeLink(settingsStrings.addingTwilioAccountClicking());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }


    private ListingRequestProvider<TwilioSettings> getListData() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            service.list(filterParametrs, new AbstractAsyncCallback<ListResult<TwilioSettings>>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<TwilioSettings> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    public String getIconStyle() {
        return "icon-sms";
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
