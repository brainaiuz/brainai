package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
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
public class SentMessageListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<MailMessageItem> list;
    private Integer campaignID;
    private String campaignName;

    public SentMessageListView() {
        super("SentMessages", crmStrings.sentMessages());
    }

    public SentMessageListView(Integer campaignID, String campaignName) {
        this();
        this.campaignID = campaignID;
        this.campaignName = campaignName;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getDisagn());
        list.getPdfVersion().setVisible(false);
        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadMailMessagesViewExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs() != null ? list.getFilterParametrs() : new ListingFilterParameter();
            filterParameter.setMessageStatus("SENT");
            filterParameter.setCampaignID(campaignID);
            list.callListExcel(excelURL, filterParameter);
        });

        add(list);
        list.reloadPage();
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<MailMessageItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final MailMessageItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem mailListSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-fulldetails", () -> SinksContainerFactory.entryPoint.onHistoryChanged("message|summary/" + item.getObjectID() + "/" + (item.isSmsMessage() ? "sms" : "") + "/true", item.getSubject()));
                mailListSummary.ensureDebugId("viewSummary");
                actionItemCount++;
                menuBar.addItem(mailListSummary);
                if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_EDIT)) {
                    MenuPopItem cloneTheMessage = new MenuPopItem(wfmStrings.copy(), "icon-copy",()->{
                        String campaignURL = campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "";
                        SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add/" + item.getObjectID() + "/" + (item.isSmsMessage() ? "true" : "") + "/copy" + campaignURL);
                    });
                    cloneTheMessage.ensureDebugId("editMessage");
                    actionItemCount++;
                    menuBar.addItem(cloneTheMessage);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_REMOVE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.messAreDelete() + "&nbsp <font color='#15428B'><b> \"" + item.getSubject() + "\"</b></font> " + crmStrings.mesSms() + "?");
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
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);
        //Subject
        column = new ColumnDefinitionConfig<MailMessageItem, SimpleLink>(wfmStrings.subject(), MailMessageItem.SUBJECT, 80) {

            @Override
            public SimpleLink getCellValue(MailMessageItem item) {
                return getLink(item.getSubject(), "message|summary/" + item.getObjectID() + "/" + (item.isSmsMessage() ? "sms" : "") + "/true", item.getSubject());
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
        column = new ColumnDefinitionConfig<MailMessageItem, String>(wfmStrings.createdDate(), MailMessageItem.CREATED, 40) {
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

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.messCurrentlyDoNotHaveMessages());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_SENT_MESSAGES_LIST);
    }

    private ListingRequestProvider<MailMessageItem> getListData() {
        return (filterParametrs, callback) -> initSentMessagesList(filterParametrs, callback, null);
    }

    private void initSentMessagesList(ListingFilterParameter filterParametrs, ListingCallback<MailMessageItem> callback, Span container) {
        filterParametrs.setCampaignID(campaignID);
        filterParametrs.setActive(true);
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

    protected ListPanelType getPanelType() {
        return ListPanelType.SentMessageListPanel;
    }

    public String getIconStyle() {
        return "crm sent-message-list";
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
        initSentMessagesList(fp, null, container);
    }


    @Override
    public String getPropertyCode() {
        return "SentMessages";
    }
}
