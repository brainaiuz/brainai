package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/2/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceTermsListView extends BaseListView {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private ListingPanel<InvoiceTermsItem> list;

    public InvoiceTermsListView() {
        super("terms", wfmStrings.terms());
        setAddNew(() -> new TermPopupView());
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.TermsListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_TERM_SAVED, InvoiceTermsListView.this, (sender, args) -> list.reloadPage());

        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(event -> new TermPopupView());
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDontHaveAnyTerms());
                message.setTextBeforeLink(accountingStrings.noTermBeforeLinkMessage());
                message.setHref(clickEvent -> new TermPopupView());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<InvoiceTermsItem> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> InvoiceService.App.get().getInvoiceTermsList(filterParametrs, new AsyncCallback<ListResult<InvoiceTermsItem>>() {
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<InvoiceTermsItem> result) {
                listingCallback.onSuccess(result);
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];

        columns[0] = new ColumnDefinitionConfig<InvoiceTermsItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final InvoiceTermsItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("Terms_edit_button");
                edit.setCommand(() -> new TermPopupView(item.getId()));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                deletePage.getElement().setId("Tems_delete_button");
                deletePage.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            InvoiceService.App.get().deleteInvoiceTerm(item.getId(), new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    Info.show(accountingMessages.termDeletedSuccessfully(), Info.Type.INFO);
                                    list.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuBar.addItem(deletePage);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<InvoiceTermsItem, String>(wfmStrings.name(), "name", 200) {


            @Override
            public String getCellValue(InvoiceTermsItem item) {
                return item.getName();
            }
        };
        columns[1].setMinimumColumnWidth(100);
//        columns[1].setMaximumColumnWidth(300);

        columns[2] = new ColumnDefinitionConfig<InvoiceTermsItem, String>(wfmStrings.days(), "days", 100) {

            @Override
            public String getCellValue(InvoiceTermsItem item) {
                return item.getDays() != null ? item.getDays().toString() : "";
            }
        };
        columns[2].setMinimumColumnWidth(100);
        columns[2].setMaximumColumnWidth(150);
        return columns;
    }

    @Override
    public String getIconStyle() {
        return "accountMark report-list";
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
