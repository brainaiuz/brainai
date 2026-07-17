package com.edatasite.workforce.gwt.accounting.client.ui.view.consignment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Normurod on 1/22/15.
 */
public class ConsignmentListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<Consignment> list;

    public ConsignmentListView() {
        super(CONSIGNMENT);
        setDescription(property.getPlural(wfmStrings.consignments()));
        setAddNew("consignment|add/add");
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ConsignmentListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONSIGNMENT_UPDATE, ConsignmentListView.this, (sender, args) -> list.reloadPage());

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
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("consignment|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoConsignmentItem());
                message.setTextBeforeLink(accountingStrings.youCanStartAddingConsignment());
                message.setHref("consignment|add/add/");
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public Widget getAddAdditionalPanel() {
                return null;
            }
        };
    }

    private ListingRequestProvider<Consignment> getListingRequestProvider() {
        return (filterParametrs, callback) -> ConsignmentService.App.get().getConsignmentList(filterParametrs, new AsyncCallback<ListResult<Consignment>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<Consignment> consignmentListResult) {
                callback.onSuccess(consignmentListResult);
            }
        });
    }


    private int actionItemCount;

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        int index = 0;
        columns[index] = new ColumnDefinitionConfig<Consignment, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final Consignment item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem itemSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                itemSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("consignment|summary/" + item.getObjectID(), item.getNumber()));

                actionItemCount++;
                menuBar.addItem(itemSummary);

                if (!item.isSubsidiaryConsignment()) {
                    MenuPopItem itemEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    itemEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("consignment|edit/" + item.getObjectID(), item.getNumber()));

                    actionItemCount++;
                    menuBar.addItem(itemEdit);
                }

                if (!item.isSubsidiaryConsignment()) {
                    MenuPopItem itemDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    itemDelete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + wfmStrings.consignments() + "?");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                deleteConsignment(item.getObjectID());
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(itemDelete);
                }

                if (actionItemCount > 0) {
                    final ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                }

                return null;
            }
        };
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setColumnSortable(false);

        columns[index] = new ColumnDefinitionConfig<Consignment, Widget>(wfmStrings.number(), NUMBER_COLUMN, 100) {
            @Override
            public Widget getCellValue(final Consignment item) {
                Span numLabel = new Span(item.getNumber());
                numLabel.setStyleName("uploadLinkStyle2");
                numLabel.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("consignment|summary/" + item.getObjectID(), item.getNumber()));
                return numLabel;
            }
        };
        columns[index++].setMinimumColumnWidth(40);

        columns[index] = new ColumnDefinitionConfig<Consignment, String>(wfmStrings.name(), NAME_COLUMN, 200) {
            @Override
            public String getCellValue(Consignment item) {
                return item.getName() != null ? item.getName() : wfmStrings.notAvailable();
            }
        };
        columns[index++].setMinimumColumnWidth(40);

        columns[index] = new ColumnDefinitionConfig<Consignment, String>(wfmStrings.date(), DATE_COLUMN, 100) {
            @Override
            public String getCellValue(Consignment item) {
                return DateUtils.formatInternal(item.getDate().getNonConvertedDate());
            }
        };
        columns[index++].setMinimumColumnWidth(40);

        columns[index] = new ColumnDefinitionConfig<Consignment, String>(wfmStrings.reference(), REFERENCE_COLUMN, 200) {
            @Override
            public String getCellValue(Consignment item) {
                return item.getReference() != null ? item.getReference() : wfmStrings.notAvailable();
            }
        };
        columns[index++].setMinimumColumnWidth(40);

        return columns;
    }

    private void deleteConsignment(Integer objectId) {
        ConsignmentService.App.get().deleteConsignment(objectId, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.consignments()), Info.Type.INFO);
                list.reloadPage();
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
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


    @Override
    public String getPropertyCode() {
        return CONSIGNMENT;
    }
}
