package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 4:40:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountListView extends BaseListView implements Constants, AccountingConstants {

    private static final DiscountServiceAsync discountService = DiscountService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel list;

    public void refresh() {
        list.reloadPage();
    }

    public DiscountListView() {
        super(DISCOUNT_LIST, accountingStrings.discounts());
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_DISCOUNT_ADD)) {
            setAddNew("discount|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel(ListPanelType.DiscountListPanel, getColumns(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DISCOUNT_SAVED, DiscountListView.this, (sender, args) -> refresh());

        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];

        columns[0] = new ColumnDefinitionConfig<DiscountItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final DiscountItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("Discount_edit_button");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("discount|edit/" + item.getId(), item.getCode(), item.getName()));
                actionItemCount++;
                menuBar.addItem(edit);

                if (Utils.hasRole(ADMIN)) {
                    MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deletePage.getElement().setId("Discount_delete_button");
                    deletePage.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                discountService.deleteDiscount(item.getId(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable throwable) {
                                        Info.show(accountingStrings.discountCouldnotBeDeleted(), Info.Type.INFO);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.discount()), Info.Type.INFO);
                                            list.reloadPage();
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DISCOUNT_DELETED, item.getId(), null);
                                        } else {
                                            Info.show(accountingStrings.discountCouldnotBeDeleted(), Info.Type.INFO);
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
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<DiscountItem, String>(wfmStrings.code(), DiscountItem.CODE_COLUMN, 150) {


            @Override
            public String getCellValue(DiscountItem discountItem) {
                return discountItem.getCode();
            }
        };
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<DiscountItem, SimpleLink>(wfmStrings.name(), NAME_COLUMN, 150) {

            @Override
            public SimpleLink getCellValue(DiscountItem discountItem) {
                return getLink(discountItem.getName(), "discount|edit/" + discountItem.getId(), discountItem.getCode(), discountItem.getName());
            }
        };
        columns[2].setMinimumColumnWidth(100);


        columns[3] = new ColumnDefinitionConfig<DiscountItem, String>(wfmStrings.type(), TYPE_COLUMN, 150) {

            @Override
            public String getCellValue(DiscountItem item) {

                if (item.getType().equals(SIMPLE_DISCOUNT)) {
                    return accountingStrings.simpleDiscount();
                } else if (item.getType().equals(MULTI_RANGE_DISCOUNT)) {
                    return accountingStrings.multiRangeDiscount();
                }
                return "N/A";
            }
        };
        columns[3].setMinimumColumnWidth(100);

        columns[4] = new ColumnDefinitionConfig<DiscountItem, String>(wfmStrings.active(), ACTIVE_COLUMN, 50) {

            @Override
            public String getCellValue(DiscountItem item) {
                if (item.isActive()) {
                    return "Yes";
                } else {
                    return "No";
                }
            }
        };
        columns[4].setMinimumColumnWidth(100);

        return columns;
    }

    private ListingRequestProvider<DiscountItem> getListProvider() {
        return (filterParametrs, listingCallback) -> discountService.getDiscountList(filterParametrs, new AsyncCallback<ListResult<DiscountItem>>() {
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<DiscountItem> discountListListResult) {
                listingCallback.onSuccess(discountListListResult);
            }

        });
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
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_DISCOUNT_ADD)) {
                    addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("discount|add/add"));
                }
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }


            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoDiscounts());
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_DISCOUNT_ADD)) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingDiscounts());
                    message.setHref("discount|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "accountMark discount-list";
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

