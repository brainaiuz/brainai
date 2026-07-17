package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
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
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TrainingContractItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingContractListView extends BaseListView implements TCConstants {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int totalCount = 0;

    public TrainingContractListView() {
        super(TC_TRAINING_CONTRACT);
        setDescription(property.getPlural(tcStrings.customerContracts()));
    }

    @Override
    protected Widget onInitialize() {
        final ListingPanel<TrainingContractItem> listingPanel = new ListingPanel<>(ListPanelType.TrainingContractListPanel, getColumns(), getListingRequestProvider(), getDesign());

        add(listingPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRAINING_CONTRACT_ADD_EDIT, TrainingContractListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRAINING_CONTRACT_DELETE, TrainingContractListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[8];
        //action
        columns[0] = new ColumnDefinitionConfig<TrainingContractItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TrainingContractItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //Edit  item
                final MenuPopItem treningContractEdit = new MenuPopItem(tcStrings.customerContractsEdit(), "icon-employee-edit-profile");
                treningContractEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_TRAINING_CONTRACT + "|add/add/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(treningContractEdit);
                treningContractEdit.setVisible(Utils.hasPermission(PermissionConstants.TC_TRAINING_CONTRACT_EDIT));
                //change prices
                final MenuPopItem changeContractPrices = new MenuPopItem(tcStrings.changePrices(), "icon-edit-subscriptions");
                changeContractPrices.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_CHANGE_CONTRACT_PRICE + "|summary/" + rowValue.getObjectID() + "/true"));
                menuItemCount++;
                menuBar.addItem(changeContractPrices);
                changeContractPrices.setVisible(Utils.hasPermission(PermissionConstants.TC_TRAINING_CONTRACT_EDIT));
                //Delete item
                final MenuPopItem deleteTrainingContract = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deleteTrainingContract.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            TCService.App.get().deleteTreningContracts(rowValue.getObjectID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), tcStrings.customerContracts()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TRAINING_CONTRACT_DELETE, result, TrainingContractListView.this);
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deleteTrainingContract);
                deleteTrainingContract.setVisible(Utils.hasPermission(PermissionConstants.TC_TRAINING_CONTRACT_DELETE));

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        //Name
        columns[1] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.name(), TrainingContractItem.NAME, 100) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return rowValue.getName();
            }
        };
        columns[1].setMinimumColumnWidth(50);
        //Account name
        columns[2] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.accountName(), TrainingContractItem.ACCOUNT, 100) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return rowValue.getAccountItem() != null ? rowValue.getAccountItem().getName() : "";
            }
        };
        columns[2].setMinimumColumnWidth(70);
        // Description
        columns[3] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.description(), TrainingContractItem.DESCRIPTION, 300) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return rowValue.getDescription();
            }
        };
        columns[3].setMinimumColumnWidth(100);
        columns[3].setColumnSortable(false);
        // Start date
        columns[4] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.startDate(), TrainingContractItem.START_DATE, 80) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return DateUtils.format(rowValue.getStartDate());
            }
        };
        columns[4].setMinimumColumnWidth(100);
        // End date
        columns[5] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.endDate(), TrainingContractItem.END_DATE, 80) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return DateUtils.format(rowValue.getEndDate());
            }
        };
        columns[5].setMinimumColumnWidth(70);

        columns[6] = new ColumnDefinitionConfig<TrainingContractItem, String>(tcStrings.courseName(), TrainingContractItem.COURSE, 100) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                return rowValue.getCoursesAsString() != null ? rowValue.getCoursesAsString() : "";
            }
        };
        columns[6].setMinimumColumnWidth(70);
        columns[6].setColumnSortable(false);

        columns[7] = new ColumnDefinitionConfig<TrainingContractItem, String>(wfmStrings.prePaid(), TrainingContractItem.PREPIAD, 60) {
            @Override
            public String getCellValue(TrainingContractItem rowValue) {
                if (rowValue.getPrepaid()) {
                    return wfmStrings.yes();
                } else {
                    return wfmStrings.no();
                }
            }
        };
        columns[7].setMinimumColumnWidth(60);
        columns[7].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[7].setColumnSortable(false);

        return columns;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.TC_TRAINING_CUSTOMER_CONTRACT_ADD)) {
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_TRAINING_CONTRACT + "|add/add"));
                    return addNewPlacement;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.customerContracts().toLowerCase() + "."));
                message.setHref(TC_TRAINING_CONTRACT + "|add/add");
                message.setTextBeforeLink(tcStrings.noCustomerContractsLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<TrainingContractItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            TCService.App.get().getTreningContractsList(filterParameter, new AbstractAsyncCallback<ListResult<TrainingContractItem>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<TrainingContractItem> result) {
                    totalCount = result.getTotal();
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "bgMark course-icon";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
    @Override
    public String getPropertyCode() {
        return TC_TRAINING_CONTRACT;
    }
}
