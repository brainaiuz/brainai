package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: admin
 * Date: Jan 5, 2010
 * Time: 5:21:20 PM
 */
public class BlackListView extends BaseListView implements Constants {

    public static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<SelectItem> list;

    public BlackListView() {
        super(BLACK_LIST, backendStrings.blackList());
    }

    @Override
    public String getIconStyle() {
        return "backend blackListView";
    }

    public void refresh() {
        list.reloadPage();
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.BlackListPanel, drawColumns(), getListProvider(), getListDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_TO_BLACK_LIST, BlackListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("blackList|add/add"));
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNotBlackListItems());
                message.setTextBeforeLink(backendStrings.youCanAddToBlackListByClicking());
                message.setHref("blackList|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<SelectItem> getListProvider() {
        return (filterParametrs, listingCallback) -> BackendService.App.get().getBlackLists(filterParametrs, new AsyncCallback<ListResult<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<SelectItem> discountListListResult) {
                listingCallback.onSuccess(discountListListResult);
            }
        });
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        //action
        columns[0] = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SelectItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "icon-remove-directory");
                deletePage.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(backendStrings.areUSureToDeleteThisEmail());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            BackendService.App.get().deleteBlackListById(rowValue.getId(), new AsyncCallback<Void>() {
                                public void onFailure(Throwable throwable) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), backendStrings.blackList()), Info.Type.INFO);
                                    list.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                });
                actionItemCount++;
                menuBar.addItem(deletePage);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        //email
        columns[1] = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.email(), "Email", 300) {
            @Override
            public String getCellValue(SelectItem rowValue) {
                return rowValue.getName() != null ? rowValue.getName() : "";
            }
        };
        columns[1].setMinimumColumnWidth(100);
        //host
        columns[2] = new ColumnDefinitionConfig<SelectItem, String>(backendStrings.hostName(), "HostName", 100) {
            @Override
            public String getCellValue(SelectItem rowValue) {
                return rowValue.getDescription() != null ? rowValue.getDescription() : "";
            }
        };
        columns[2].setMinimumColumnWidth(80);
        columns[2].setColumnSortable(false);
        return columns;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}