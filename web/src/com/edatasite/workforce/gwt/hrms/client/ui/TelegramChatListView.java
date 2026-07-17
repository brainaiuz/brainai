package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TelegramConstants;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.finnetlimited.reportservice.core.client.gwtrpc.AbstractAsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 31.07.2017 21:00
 */
public class TelegramChatListView extends BaseListView implements TelegramConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsString = HrmsStrings.App.get();
    private final TelegramChatServiceAsync telegramChatService = TelegramChatService.App.get();

    private ListingPanel<TelegramChatListItem> listingPanel;

    public TelegramChatListView() {
        super("telegramChatList", "Telegram chats");
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<TelegramChatListItem>(ListPanelType.HrmsExpenceReportListPanel, getColumns(), getProvider(), getDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TELEGRAM_CHAT_EDIT, TelegramChatListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column;

        column = new ColumnDefinitionConfig<TelegramChatListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(TelegramChatListItem rowValue) {
                int count = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setScheduledCommand(() -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("telegramchatedit|edittelegramchat/" + rowValue.getObjectId(), rowValue.getChatName());
                });
                count++;
                menuBar.addItem(edit);

                MenuPopItem remove = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                remove.setScheduledCommand(() -> {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.messAreDelete() + " " + rowValue.getChatName() + " " + "chat");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            telegramChatService.deleteChat(rowValue.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                                }

                                @Override
                                public void success(Boolean result) {
                                    LoadingPanel.loading(false);
                                    listingPanel.reloadPage();
                                    if (result != null && result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                    } else {
                                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                                    }
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                count++;
                menuBar.addItem(remove);

                ToolItem toolItem = new ToolItem(count);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<TelegramChatListItem, SimpleLink>(wfmStrings.name(), TelegramChatListItem.CHAT_NAME, 120) {
            @Override
            public SimpleLink getCellValue(TelegramChatListItem rowValue) {
                SimpleLink name = new SimpleLink(rowValue.getChatName());
                name.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("telegramchatedit|edittelegramchat/" + rowValue.getObjectId(), rowValue.getChatName()));
                return name;
            }
        };
        column.setMinimumColumnWidth(110);
        columns.add(column);

        column = new ColumnDefinitionConfig<TelegramChatListItem, String>(wfmStrings.type(), TelegramChatListItem.CHAT_TYPE, 60) {
            @Override
            public String getCellValue(TelegramChatListItem rowValue) {
                return getChatType(rowValue.getChatType());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TelegramChatListItem, String>(wfmStrings.createdBy(), TelegramChatListItem.CREATOR, 100) {
            @Override
            public String getCellValue(TelegramChatListItem rowValue) {
                return rowValue.getCreator() != null ? rowValue.getCreator().getName() : "N/A";
            }
        };
        column.setMinimumColumnWidth(90);
        columns.add(column);

        column = new ColumnDefinitionConfig<TelegramChatListItem, String>(wfmStrings.status(), TelegramChatListItem.ACTIVE, 50) {
            @Override
            public String getCellValue(TelegramChatListItem rowValue) {
                return rowValue.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        column.setMinimumColumnWidth(45);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsString.currentlyNoChats());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider getProvider() {
        return (ListingRequestProvider<TelegramChatListItem>) (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            telegramChatService.getChatList(filterParametrs, new AbstractAsyncCallback<ListResult<TelegramChatListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<TelegramChatListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private String getChatType(String chatType) {
        if (chatType != null) {
            if (GROUP_TYPE.equals(chatType)) {
                return "Group";
            } else if (MEMBER_TYPE.equals(chatType)) {
                return "Member";
            }
        }
        return "N/A";
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
