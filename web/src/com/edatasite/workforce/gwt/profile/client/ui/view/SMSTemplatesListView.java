package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowRuleListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Azazello on 4/21/15.
 */
public class SMSTemplatesListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private ListingPanel listPanel;

    public SMSTemplatesListView() {
        super("smsTemplateList", wfmStrings.smsTemplates());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel(ListPanelType.SMSTemplatesListPanel, getColumnConfig(), getListProvider(), getListDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SMS_TEMPLATE_ADD_EDIT, SMSTemplatesListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SMS_TEMPLATE_DELETE, SMSTemplatesListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[4];

        columnConfig[0] = new ColumnDefinitionConfig<SMSTemplateItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SMSTemplateItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem templateSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                templateSummary.ensureDebugId("summary");
                templateSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("smstemplate|summary/" + item.getObjectID(), item.getName()));
                actionItemCount++;
                menuBar.addItem(templateSummary);

                MenuPopItem templateEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                templateEdit.ensureDebugId("edit");
                templateEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("smstemplate|add/add/" + item.getObjectID(), item.getName()));
                actionItemCount++;
                menuBar.addItem(templateEdit);
                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(wfmMessages.sureYouWantToDelete("<b>" + item.getName() + "</b>", ""));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            ProfileService.App.get().deleteSMSTemplate(item.getObjectID(), new AsyncCallback<Void>() {
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void onSuccess(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.smsTemplates()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_TEMPLATE_DELETE, result, SMSTemplatesListView.this);
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuBar.addItem(removeItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<SMSTemplateItem, SimpleLink>(wfmStrings.template(), SMSTemplateItem.NAME, 150) {
            @Override
            public SimpleLink getCellValue(SMSTemplateItem item) {
                return getLink(item.getName(), "smstemplate|summary/" + item.getObjectID());
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);

        columnConfig[2] = new ColumnDefinitionConfig<SMSTemplateItem, String>(wfmStrings.apps(), SMSTemplateItem.MODULE, 150) {
            @Override
            public String getCellValue(SMSTemplateItem item) {
                return item.getModuleName() != null ? WorkflowRuleListView.localize(item.getModuleName()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[2].setMinimumColumnWidth(200);

        columnConfig[3] = new ColumnDefinitionConfig<SMSTemplateItem, String>(wfmStrings.isDefault(), SMSTemplateItem.DEFAULT, 50) {
            @Override
            public String getCellValue(SMSTemplateItem item) {
                return item.isDefault() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[3].setMinimumColumnWidth(80);
        columnConfig[3].setMaximumColumnWidth(120);

        return columnConfig;
    }

    private ListingRequestProvider<SMSTemplateItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> ProfileService.App.get().getSMSTemplateList(listingFilterParameter, new AsyncCallback<ListResult<SMSTemplateItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<SMSTemplateItem> smsTemplateItemListResult) {
                listingCallback.onSuccess(smsTemplateItemListResult);
            }
        });
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return -1;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("smstemplate|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.noSMSTemplateText());
                message.setHref("smstemplate|add/add");
                message.setTextBeforeLink(settingsStrings.noSMSTemplateLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }


    public String getIconStyle() {
        return "icon-sms";
    }

    public ImageResource getIconImage() {
        return null;
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
