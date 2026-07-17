package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.CustomFieldsListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryCustomFieldListView extends CustomFieldsListView {

    private final Integer relationID;

    public ProductCategoryCustomFieldListView(Integer relationID) {
        super("customfield", settingsStrings.categoryCustomField());
        this.relationID = relationID;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<CompanyCustomFieldItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CompanyCustomFieldItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon");
                edit.setCommand(() -> {
                    if (listing.getFilterParametrs().getCompanyID() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + ViewName.ProductCategory.name() + "/" + listing.getFilterParametrs().getCompanyID());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + ViewName.ProductCategory.name());
                    }
                });
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                delete.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(settingsStrings.areYouSureWantRemoveCustomField());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            profileService.deleteCustomField(item.getObjectId(), listing.getFilterParametrs().getCompanyID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void success(Void aVoid) {
                                    LoadingPanel.loading(false);
                                    Timer timer = new Timer() {
                                        @Override
                                        public void run() {
                                            refresh();
                                        }
                                    };
                                    timer.schedule(1000);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(delete);


                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.objectID(), "objectid", 50) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return String.valueOf(item.getObjectId());
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.fieldName(), "fieldname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getFieldName();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.uiType(), "uitype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getUiType();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.dataType(), "datatype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getDataType();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityName("'" + ViewName.ProductCategory.name() + "'");
            filterParameter.setRelationID(relationID);
            profileService.getCustomFields(filterParameter, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<CompanyCustomFieldItem> customfields) {
                    callback.onSuccess(customfields);
                }
            });
        };
    }

    protected ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newLocation = getAddNewButton();

                newLocation.addClickHandler(baseEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/" + relationID + "/" + ViewName.ProductCategory.name()));

                return newLocation;
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
            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };

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
