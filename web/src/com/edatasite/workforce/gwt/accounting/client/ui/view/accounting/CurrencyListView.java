package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_CURRENCY_RATE_EDIT;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 21.01.16
 * Time: 17:38:57
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyListView extends BaseListView implements Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private ListingPanel<CurrencyListItem> listingTable;
    private final CurrencyServiceAsync service = CurrencyService.App.get();
    private final Date currentDate = new Date();

    private DatePicker datePicker;
    private final String currencyListView = "currency_list_view";

    public CurrencyListView() {
        super("exrates", accountingStrings.currencyRates());
        if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
            setAddNew(new Command() {
                @Override
                public void execute() {
                    new CurrencyViewPopup(datePicker.getDate(), o -> listingTable.reloadPage());
                }
            });
        }
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        listingTable.hideSearchButton();
        add(listingTable);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<CurrencyListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final CurrencyListItem currencyListItem) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_CURRENCY_RATE_EDIT)) {
                    MenuPopItem currencyEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    currencyEdit.getElement().setId("currency_edit_button");
                    currencyEdit.setCommand(() -> new CurrencyViewPopup(currencyListItem.getCurrency().getId(), datePicker.getDate(), o -> listingTable.reloadPage()));
                    actionItemCount++;
                    menuBar.addItem(currencyEdit);
                }
                MenuPopItem currencyDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                currencyDelete.getElement().setId("currency_delete_button");
                currencyDelete.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            service.deleteCurrency(currencyListItem.getCurrency().getId(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.currency()), Info.Type.INFO);
                                    listingTable.reloadPage();
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                menuBar.addItem(currencyDelete);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<CurrencyListItem, String>(wfmStrings.currency(), CurrencyListItem.NAME, 200) {

            @Override
            public String getCellValue(CurrencyListItem currencyListItem) {
                return currencyListItem.getCurrency().getName() + " - " + currencyListItem.getCurrency().getFullName();
            }
        };
        columns[1].setColumnSortable(false);
        columns[1].setMinimumColumnWidth(180);

        columns[2] = new ColumnDefinitionConfig<CurrencyListItem, Widget>(wfmStrings.exchangeRate(), CurrencyListItem.EXRATE, 100) {

            @Override
            public Widget getCellValue(final CurrencyListItem currencyListItem) {
                SimpleLink link = new SimpleLink(AccountingUtils.get().formatExRate(currencyListItem.getExchangeRate()));
                link.addClickHandler(clickEvent -> new CurrencyViewPopup(currencyListItem.getCurrency().getId(), datePicker.getDate(), o -> listingTable.reloadPage()));
                return link;
            }
        };
        columns[2].setColumnSortable(false);
        columns[2].setMinimumColumnWidth(80);

        columns[3] = new ColumnDefinitionConfig<CurrencyListItem, String>(wfmStrings.note(), CurrencyListItem.UPDATED, 200) {

            @Override
            public String getCellValue(CurrencyListItem currencyListItem) {
                if (!currencyListItem.isFromService() && currencyListItem.getUpdateTime() != null) {
                    return wfmStrings.exRateProvided() + " " + DateUtils.getDateAndTimeFormatShort2(currencyListItem.getUpdateTime().getNonConvertedDate());
                }
                return null;
            }
        };
        columns[3].setColumnSortable(false);
        columns[3].setMinimumColumnWidth(180);

        return columns;
    }

    private ListingRequestProvider getListingRequestProvider() {
        return (filterParametrs, callback) -> service.getCurrencyRateList(new DateNonConvertable(datePicker.getDate()), new AsyncCallback<ListResult<CurrencyListItem>>() {

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<CurrencyListItem> data) {
                callback.onSuccess(data);
            }
        });
    }

    private ListingPanelDesign getListingPanelDesign() {
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
                };
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                datePicker = new DatePicker(currentDate);
                datePicker.ensureDebugId(currencyListView + "date");
                datePicker.setDate(currentDate);
                datePicker.getPopup().addChangeHandler(changeEvent -> listingTable.reloadPage());

                topPanel.add(datePicker);
                topPanel.setCellVerticalAlignment(datePicker, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.setCellVerticalAlignment(datePicker, HasVerticalAlignment.ALIGN_MIDDLE);
                return topPanel;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addCurrencyRate = null;
                if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)) {
                    addCurrencyRate = getAddNewButton();
                    addCurrencyRate.addClickHandler(clickEvent -> new CurrencyViewPopup(datePicker.getDate(), o -> listingTable.reloadPage()));
                }

                return addCurrencyRate;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoForeignCurrencies());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    private ListPanelType getPanelType() {
        return ListPanelType.CurrencyListPanel;
    }

    @Override
    public String getIconStyle() {
        return "accountMark trial-balance";
    }

    @Override
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
