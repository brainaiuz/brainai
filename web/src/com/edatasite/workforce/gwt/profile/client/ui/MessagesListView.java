package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Azazello on 2/5/16.
 */
public class MessagesListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync service = ProfileService.App.get();
    private ListingPanel<MessageItem> list;
    private boolean isWorkflowMessages;

    public MessagesListView() {
        super("systemMessages", settingsStrings.systemNotifications());
    }

    public MessagesListView(boolean isWorkflowMessages) {
        super("workflowMessages", settingsStrings.workflowNotifications());
        this.isWorkflowMessages = isWorkflowMessages;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.MessagesListPanel, getColumnConfigs(), getListData(), getDesign());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        //////////////////////////---------(1)----------
        columns[0] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.from(), MessageItem.FROM, 80) {
            @Override
            public String getCellValue(MessageItem item) {
                return item.getFrom();
            }
        };
        columns[0].setMinimumColumnWidth(40);
        columns[0].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(2)----------
        columns[1] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.to(), MessageItem.TO, 80) {
            @Override
            public String getCellValue(MessageItem item) {
                return item.getTo();
            }
        };
        columns[1].setMinimumColumnWidth(40);
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(3)----------
        columns[2] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.subject(), MessageItem.SUBJECT, 100) {
            @Override
            public String getCellValue(MessageItem item) {
                return item.getSubject();
            }
        };
        columns[2].setMinimumColumnWidth(60);
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(4)----------
        columns[3] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.status(), MessageItem.STATUS, 50) {
            @Override
            public String getCellValue(MessageItem item) {
                return item.getStatus() != null ? item.getStatus().name() : wfmStrings.notAvailable();
            }
        };
        columns[3].setMinimumColumnWidth(30);
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(5)----------
        columns[4] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.createdDate(), MessageItem.CREATION_DATE, 50) {
            @Override
            public String getCellValue(MessageItem item) {
                return DateUtils.formatInternal(item.getCreationDate());
            }
        };
        columns[4].setMinimumColumnWidth(20);
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(6)----------
        columns[5] = new ColumnDefinitionConfig<MessageItem, String>(wfmStrings.sent(), MessageItem.SENT_DATE, 50) {
            @Override
            public String getCellValue(MessageItem item) {
                return DateUtils.formatInternal(item.getCreationDate());
            }
        };
        columns[5].setMinimumColumnWidth(20);
        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(7)----------
        columns[6] = new ColumnDefinitionConfig<MessageItem, Integer>(settingsStrings.attempts(), MessageItem.ATTEMPTS, 30) {
            @Override
            public Integer getCellValue(MessageItem item) {
                return item.getAttempts();
            }
        };
        columns[6].setMinimumColumnWidth(10);
        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.EmailFilterListPanel;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoNotifications());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<MessageItem> getListData() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            service.getMessages(filterParametrs, isWorkflowMessages, new AbstractAsyncCallback<ListResult<MessageItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ListResult<MessageItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    public String getIconStyle() {
        return "icon-message";
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
