package com.edatasite.workforce.gwt.reportingsystem.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
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
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.AddEditReportingFolder;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.NewReportPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Virus on 9/10/14.
 */
public class CategoryReportListView extends BaseListView implements Colapse, Constants {
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();

    private ListingPanel<SelectListRpc> list;
    private final ReportingCategoryRPC category;

    public CategoryReportListView(ReportingCategoryRPC category) {
        super("categoryReportList" + category.getId(), category.getName());
        this.category = category;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CategoryReportListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_REPORTING_FAVOURITY, CategoryReportListView.this, (sender, args) -> {
            if (category.getId() == 0) {
                list.reloadPage();
            }
        });
        add(list);

        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> result = new ArrayList<>();

        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<SelectListRpc, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SelectListRpc item) {
                MenuBar menuBar = new MenuBar(true);

                menuBar.addItem(new MenuPopItem(reportingStrings.runReport(), "runReport", () -> {
                    if (item.isFakeReport()) {
                        Utils.openURL(GWT.getHostPageBaseURL() + item.getTargetLink());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + item.getId() + "/savedreport/" + Utils.encrypt(item.getCategory()), item.getCategory(), item.getCategory());
                    }
                }));

                MenuPopItem favourite = new MenuPopItem(item.isFavourited() ? reportingStrings.removeFromFavourites() : reportingStrings.makeFavourite(), item.isFavourited() ? "removeFromFavourites" : "makeFavourite");
                favourite.setCommand(() -> ReportingService.App.get().createFavouriteReportTemplate(item.getId(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_REPORTING_FAVOURITY, null, CategoryReportListView.this);
                        favourite.setText(result != null && result ? reportingStrings.removeFromFavourites() : reportingStrings.makeFavourite());
                        favourite.setIconStyle(result != null && result ? "makeFavourite" : "removeFromFavourites");
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                    }
                }));
                menuBar.addItem(favourite);

                menuBar.addItem(new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile", () -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(wfmMessages.areYouSureYouWantToDeleteThe(item.getName()));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            ReportingService.App.get().deleteReport(item.getId(), new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), item.getName()), Info.Type.INFO);
                                    list.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                }));


                final ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        result.add(column);


        column = new ColumnDefinitionConfig<SelectListRpc, SimpleLink>(wfmStrings.reportName(), SelectListRpc.NAME, 250) {
            @Override
            public SimpleLink getCellValue(SelectListRpc item) {
                SimpleLink link = new SimpleLink(item.getName());
                link.addClickHandler(event -> {
                    if (item.isFakeReport()) {
                        Utils.openURL(GWT.getHostPageBaseURL() + item.getTargetLink());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + item.getId() + "/savedreport/" + Utils.encrypt(item.getCategory()), item.getCategory(), item.getCategory());
                    }
                });
                return link;
            }
        };
//        column.setMaximumColumnWidth(150);
        result.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, String>(wfmStrings.description(), SelectListRpc.DESCRIPTION, 250) {

            @Override
            public String getCellValue(SelectListRpc rowValue) {
                return rowValue.getDescription() != null ? rowValue.getDescription() : wfmStrings.notAvailable();
            }
        };
        result.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, String>(wfmStrings.folderName(), SelectListRpc.FOLDER, 200) {
            @Override
            public String getCellValue(SelectListRpc rowValue) {
                return rowValue.getFolder() != null ? rowValue.getFolder() : wfmStrings.notAvailable();
            }
        };
        column.setMaximumColumnWidth(180);
        result.add(column);
        return result.toArray(new CustomColumnDefinitionConfig[0]);
    }

    private ListingRequestProvider<SelectListRpc> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> {
            filterParametrs.setCategoryID(category.getId());
            ReportingService.App.get().getReportList(filterParametrs, new AsyncCallback<ListResult<SelectListRpc>>() {
                public void onFailure(Throwable caught) {
                    listingCallback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<SelectListRpc> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (category != null && category.getId() != 0) {
                    ActionButton actionButton = new ActionButton();
                    actionButton.setText(reportingStrings.newReport());
                    actionButton.addClickHandler(event -> {
                        NewReportPopup.getInstance(category).show();
                    });
                    return actionButton;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (category != null && category.getId() != null && category.getId() != 0) {
                    ActionButton addFolderButton = new ActionButton();
                    addFolderButton.ensureDebugId("category_report_list_add_folder");
                    addFolderButton.setText(wfmStrings.addFolder());
                    addFolderButton.addClickHandler((event) -> {
                        AddEditReportingFolder.categoryId = category.getId();
                        AddEditReportingFolder addFolderPopup = new AddEditReportingFolder();
                        addFolderPopup.addCloseHandler(closeEvent -> NewReportPopup.getInstance(category)); // RELOADING...
                        addFolderPopup.center();
                    });
                    return addFolderButton;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(reportingStrings.currentlyYouDontHaveData());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "ficon--equalizer";
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
