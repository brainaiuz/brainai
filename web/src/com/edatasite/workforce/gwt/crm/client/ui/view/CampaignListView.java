package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:14:38
 * To change this template use File | Settings | File Templates.
 */
public class CampaignListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private ListingPanel<CampaignItem> list;
    private int totalCount = 0;
    private HashSet<CampaignItem> selectedItems = new HashSet<>();

    public CampaignListView() {
        super(CAMPAIGN_LIST, wfmStrings.campaigns());
        setDescription(property.getPlural(wfmStrings.campaigns()));
        if ((Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN) && Utils.hasPermission(PermissionConstants.CRM_CAMPAIGN_QUICK_ADD)) || Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN)) {
            setAddNew((() -> SinksContainerFactory.entryPoint.onHistoryChanged("campaign|add/add")));
        } else if (Utils.hasPermission(PermissionConstants.CRM_CAMPAIGN_QUICK_ADD)) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.CAMPAIGN_FORM));
        }
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.CampaignListPanel, getColumnConfigs(), getListData(), getDisagn(), SelectionGrid.SelectionPolicy.CHECKBOX);
        list.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/campaignListPDFHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParameter);
        });
        list.setExcelListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadCrmCampaignsExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParameter);
        });
        list.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CAMPAIGN_ADD_EDIT, CampaignListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CAMPAIGN_DELETED, CampaignListView.this, (sender, args) -> refresh());

        add(list);
        return null;
    }

    public void refresh() {
        list.reloadPage();
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_CAMPAIGNS_LIST);
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;

        columnConfig = new ColumnDefinitionConfig<CampaignItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CampaignItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem campaignSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-campaign-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("campaign|summary/" + item.getObjectId() + "/" + item.getName(), item.getName()));
                actionItemCount++;
                menuBar.addItem(campaignSummary);
                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CAMPAIGN)) {
                    MenuPopItem campaignEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("campaignedit|edit/" + item.getObjectId() + "/" + item.getName(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(campaignEdit);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CAMPAIGN)) {
                    MenuPopItem removeCampaign = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CRMService.App.get().deleteCampaign(item.getObjectId(), new AbstractAsyncCallback() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(Object result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.campaign()), Info.Type.INFO);
                                        refresh();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CAMPAIGN_DELETED, result, CampaignListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeCampaign);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);
        //Name
        columnConfig = new ColumnDefinitionConfig<CampaignItem, SimpleLink>(wfmStrings.name(), CampaignItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(CampaignItem item) {
                return getLink(item.getName(), "campaign|summary/" + item.getObjectId() + "/" + item.getName(), item.getName());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Owner
        columnConfig = new ColumnDefinitionConfig<CampaignItem, String>(wfmStrings.owner(), CampaignItem.OWNER, 100) {
            @Override
            public String getCellValue(CampaignItem item) {
                return item.getAssignee();
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Type
        columnConfig = new ColumnDefinitionConfig<CampaignItem, String>(wfmStrings.type(), CampaignItem.TYPE, 40) {
            public String getCellValue(CampaignItem item) {
                return item.getType();
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Status
        columnConfig = new ColumnDefinitionConfig<CampaignItem, String>(wfmStrings.status(), CampaignItem.STATUS, 50) {
            @Override
            public String getCellValue(CampaignItem item) {
                return item.getStatus();
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Start Date
        columnConfig = new ColumnDefinitionConfig<CampaignItem, String>(wfmStrings.startDate(), CampaignItem.START_DATE, 60) {
            @Override
            public String getCellValue(CampaignItem item) {
                return DateUtils.format(item.getStartDate());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //End Date
        columnConfig = new ColumnDefinitionConfig<CampaignItem, String>(wfmStrings.endDate(), CampaignItem.END_DATE, 60) {
            @Override
            public String getCellValue(CampaignItem item) {
                return DateUtils.format(item.getEndDate());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);

        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private GuideListingPanelDesign getDisagn() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN) ? (() -> new CrmQuickAdd(LayoutRPC.CAMPAIGN_FORM)) : null;
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
                ActionButton addNew = getAddNewButton();
                if ((Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN) && Utils.hasPermission(PermissionConstants.CRM_CAMPAIGN_QUICK_ADD)) || Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN)) {
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("campaign|add/add"));
                    return addNew;
                } else if (Utils.hasPermission(PermissionConstants.CRM_CAMPAIGN_QUICK_ADD)) {
                    addNew.addClickHandler(clickEvent -> new CrmQuickAdd(LayoutRPC.CAMPAIGN_FORM));
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CAMPAIGN)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, Utils.hasPermission(PermissionConstants.CRM_CAMPAIGNS_EXPORT));
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.messCurrentlyCampaigns());
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN)) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.CAMPAIGN_FORM));
                    message.setTextBeforeLink(crmStrings.messAddingCampaignsClicking());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow("Campaign"), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        CampaignItem item = selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = CampaignItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    crmService.deleteCampaigns(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            list.reloadPage();
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.campaign()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }


    private ListingRequestProvider<CampaignItem> getListData() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            crmService.getCampaigns(filterParametrs, new AsyncCallback<ListResult<CampaignItem>>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<CampaignItem> campaignItemListResult) {
                    totalCount = campaignItemListResult.getTotal();
                    callback.onSuccess(campaignItemListResult);
                }
            });
        };
    }

    public String getIconStyle() {
        return "crm campaign-list";
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
    public String getPropertyCode() {
        return CAMPAIGN_LIST;
    }
}
