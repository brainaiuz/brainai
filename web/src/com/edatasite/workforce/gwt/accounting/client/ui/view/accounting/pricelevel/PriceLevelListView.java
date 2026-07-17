package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:26:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelListView extends BaseListView implements Constants {
    private static final PriceLevelServiceAsync priceLevelService = PriceLevelService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel list;

    public void refresh() {
        list.reloadPage();
    }

    public PriceLevelListView() {
        super(PRICE_LEVEL_LIST, wfmStrings.priceLevel());
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRICE_LEVEL_ADD)) {
            setAddNew("priceLevel|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel(ListPanelType.PriceLevelListPanel, getColumns(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRICE_LEVEL_SAVED, PriceLevelListView.this, (sender, args) -> refresh());

        add(list);
        return null;
    }

    private int actionItemCount;

    private CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> result = new ArrayList<>();
        boolean editPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_PRICE_LEVEL_EDIT);
        boolean deletePermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_PRICE_LEVEL_DELETE);
        ColumnDefinitionConfig action = new ColumnDefinitionConfig<PriceLevelItem, Widget>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Widget getCellValue(final PriceLevelItem item) {
                actionItemCount = 0;
                final MenuBar menuBar = new MenuBar(true);
                if (editPermission) {
                    MenuPopItem priceLevelEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    priceLevelEdit.getElement().setId("Price_level_edit_button");
                    priceLevelEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("priceLevel|edit/" + item.getId(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(priceLevelEdit);
                }


                MenuPopItem priceLevelCopy = new MenuPopItem(wfmStrings.copy(), "icon-copy");
                priceLevelCopy.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("priceLevel|copy/copy/" + item.getId()));
                actionItemCount++;
                menuBar.addItem(priceLevelCopy);


                if (deletePermission) {
                    MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deletePage.getElement().setId("Price_level_delete_button");
                    deletePage.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                priceLevelService.deletePriceLevel(item.getId(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.priceLevel()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            Info.show(accountingStrings.priceLevelDeletedFailure(), Info.Type.INFO);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(deletePage);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        action.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        action.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        action.setColumnSortable(false);
        if (editPermission || deletePermission) {
            result.add(action);
        }
        ColumnDefinitionConfig name = new ColumnDefinitionConfig<PriceLevelItem, Widget>(wfmStrings.name(), PriceLevelItem.NAME, 150) {

            @Override
            public Widget getCellValue(PriceLevelItem priceLevelItem) {
                //return priceLevelItem.getName();
                Label label = new Label(priceLevelItem.getName());
                label.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("priceLevel|edit/" + priceLevelItem.getId(), priceLevelItem.getName());
                });
                return label;
            }
        };
        name.setMinimumColumnWidth(100);
        result.add(name);
        ColumnDefinitionConfig taxType = new ColumnDefinitionConfig<PriceLevelItem, String>(wfmStrings.type(), PriceLevelItem.TYPE, 150) {

            @Override
            public String getCellValue(PriceLevelItem priceLevelItem) {
                String value = "";
                if (priceLevelItem.getType().equals(FIXED_PERCENTAGE)) {
                    value = accountingStrings.fixedPercentage();
                } else if (priceLevelItem.getType().equals(PER_PRODUCT)) {
                    value = accountingStrings.perProduct();
                } else {
                    value = accountingStrings.byBrand();
                }
                return value;
            }
        };
        taxType.setMinimumColumnWidth(100);
        result.add(taxType);

        ColumnDefinitionConfig plCase = new ColumnDefinitionConfig<PriceLevelItem, String>(accountingStrings.plcase(), PriceLevelItem.PLCASE, 150) {

            @Override
            public String getCellValue(PriceLevelItem priceLevelItem) {
                return priceLevelItem.getPLCase().equals(DECREASE) ? accountingStrings.decrease() : accountingStrings.increase();
            }
        };
        plCase.setMinimumColumnWidth(100);
        result.add(plCase);

        return result.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<PriceLevelItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            filterParametrs.setFromListing(true);
            priceLevelService.getPriceLevelListForListing(filterParametrs, new AsyncCallback<ListResult<PriceLevelItem>>() {
                public void onFailure(Throwable caught) {
                    listingCallback.onFailure(caught);
                }

                public void onSuccess(ListResult<PriceLevelItem> list) {
                    listingCallback.onSuccess(list);
                }
            });

        };
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRICE_LEVEL_ADD)) {
                    addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("priceLevel|add/add"));
                }
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoPriceLevel());
                if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingPriceLevel());
                    message.setHref("priceLevel|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "accountMark price-livel-list";
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
