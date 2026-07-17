package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25-Jan-2010
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class MessageListView extends BaseListView implements Constants, NoColapse {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<MailMessageItem> list;
    private Integer campaignID;
    private String campaignName;

    public MessageListView() {
        super("scheduled_messages", crmStrings.scheduledMessages());
        setPlusIcon();
    }

    public MessageListView(Integer campaignID, String campaignName) {
        this();
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        setPlusIcon();
    }

    private void setPlusIcon() {
        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MESSAGE)) {
            String campaignURL = campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "";
            setAddNew("message|add/add//false/" + campaignURL);
        }
    }

    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MESSAGE_ADD, MessageListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MESSAGE_EDIT, MessageListView.this, (sender, args) -> list.reloadPage());
        list = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListData(), getDesign());
        list.getPdfVersion().setVisible(false);
        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadMailMessagesViewExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs() != null ? list.getFilterParametrs() : new ListingFilterParameter();
            filterParameter.setMessageStatus("WAITING");
            list.callListExcel(excelURL, filterParameter);
        });

        add(list);
        return null;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.MessageListPanel;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_QUEUED_MESSAGES_LIST);
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<MailMessageItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final MailMessageItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem mailListSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-fulldetails", () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|summary/" + item.getObjectID() + "/" + (item.isSmsMessage() ? "sms" : "") + "/false", item.getSubject()));
                mailListSummary.ensureDebugId("messageView");
                actionItemCount++;
                menuBar.addItem(mailListSummary);
                if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_EDIT) && !MessageStatusEnum.IN_PROGRESS.equals(item.getStatus())) {
                    MenuPopItem mailListEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add/" + item.getObjectID() + "/" + item.isSmsMessage(), item.getSubject()));
                    mailListEdit.ensureDebugId("mailListEdit");
                    actionItemCount++;
                    menuBar.addItem(mailListEdit);
                }
                if (MessageStatusEnum.IN_PROGRESS.equals(item.getStatus())) {
                    MenuPopItem cancelItem = new MenuPopItem(wfmStrings.cancel(), "icon-cancel", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(crmStrings.messAreCancel());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                MassMailService.App.get().cancelSchedule(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(crmStrings.messageSucCancelled(), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    cancelItem.ensureDebugId("cancelItem");
                    menuBar.addItem(cancelItem);
                }

                if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_REMOVE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                MassMailService.App.get().deleteMailMessage(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.message()), Info.Type.INFO);
                                        list.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    removeItem.ensureDebugId("removeItem");
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Subject
        column = new ColumnDefinitionConfig<MailMessageItem, SimpleLink>(wfmStrings.subject(), MailMessageItem.SUBJECT, 80) {
            @Override
            public SimpleLink getCellValue(MailMessageItem item) {
                return getLink(item.getSubject(), "message|summary/" + item.getObjectID() + "/" + (item.isSmsMessage() ? "true" : "") + "/false", item.getSubject(), item.getSubject());
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //From
        column = new ColumnDefinitionConfig<MailMessageItem, String>(wfmStrings.from(), MailMessageItem.FROM, 70) {
            @Override
            public String getCellValue(MailMessageItem item) {
                return item.getFrom();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Scheduled
        column = new ColumnDefinitionConfig<MailMessageItem, String>(crmStrings.scheduledDate(), MailMessageItem.SCHEDULED, 40) {
            @Override
            public String getCellValue(MailMessageItem item) {
                return item.getScheduled() != null ? DateUtils.formatInternal1(item.getScheduled()) : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Status
        column = new ColumnDefinitionConfig<MailMessageItem, String>(wfmStrings.status(), MailMessageItem.STATUS, 30) {
            @Override
            public String getCellValue(MailMessageItem item) {
                return item.getStatus() != null ? item.getStatus().getCode() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //SMS
        column = new ColumnDefinitionConfig<MailMessageItem, String>(wfmStrings.type(), MailMessageItem.IS_SMS_MESSAGE, 30) {
            @Override
            public String getCellValue(MailMessageItem item) {
                return item.isSmsMessage() ? crmStrings.smsMessage() : crmStrings.mailMessage();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        ///Created
        column = new ColumnDefinitionConfig<MailMessageItem, String>(wfmStrings.created(), MailMessageItem.CREATED, 40) {
            @Override
            public String getCellValue(MailMessageItem item) {
                return DateUtils.formatInternal(item.getCreationTime());
            }
        };
        column.setMinimumColumnWidth(40);
        column.setShow(false);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<MailMessageItem> getListData() {
        return (filterParametrs, callback) -> initMessageList(filterParametrs, callback, null);
    }

    private void initMessageList(ListingFilterParameter filterParametrs, ListingCallback<MailMessageItem> callback, Span container) {
        filterParametrs.setCampaignID(campaignID);
        filterParametrs.setActive(false);//bu sent messagemasligini bilish uchun
        MassMailService.App.get().getMailMessageList(filterParametrs, new AsyncCallback<ListResult<MailMessageItem>>() {
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void onSuccess(ListResult<MailMessageItem> list1) {
                if (callback != null) {
                    callback.onSuccess(list1);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (list1.getTotal() != null && list1.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(list1.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private GuideListingPanelDesign getDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MESSAGE)) {
                    String campaignURL = campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "";
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add//false/" + campaignURL);
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MESSAGE)) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    addNew.ensureDebugId("newMessage");
                    MenuBar menuBar = new MenuBar(true);
                    menuBar.setAutoOpen(true);

                    String campaignURL = campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "";
                    final MenuPopItem mailMessage = new MenuPopItem(crmStrings.mailMessage(), null, () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add//false/" + campaignURL));
                    mailMessage.ensureDebugId("mailMessage");
                    final MenuPopItem smsMessage = new MenuPopItem(crmStrings.smsMessage(), null, () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add//true/" + campaignURL));
                    smsMessage.ensureDebugId("smsMessage");

                    menuBar.addItem(mailMessage);
                    menuBar.addItem(smsMessage);
                    addNew.setMenu(menuBar);
                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, Utils.hasPermission(PermissionConstants.CRM_MESSAGES_EXPORT));
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.waitingMessCurrentlyMessages() + " ");
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MESSAGE)) {
                    String campaignURL = campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "";
                    message.setHref("message|add/add//false/" + campaignURL);
                    message.setTextBeforeLink(" " + crmStrings.messStartAddingMessagesClicking());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    public String getIconStyle() {
        return "crm queued-message-list";
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initMessageList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return "scheduled_messages";
    }
}
