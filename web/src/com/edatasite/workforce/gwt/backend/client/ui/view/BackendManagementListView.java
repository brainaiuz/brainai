package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 3:19 PM
 */
public class BackendManagementListView extends BaseListView {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private final Integer companyID;
    private final String companyName;
    private ListingPanel<BackendManagementListItem> listingPanel;

    public BackendManagementListView(Integer companyID, String companyName) {
        super("backendManagementView", backendStrings.backendManagement());
        this.companyID = companyID;
        this.companyName = companyName;
    }

    @Override
    public String getIconStyle() {
        return "icon-backendManagementView";
    }

    @Override
    protected Widget onInitialize() {
        String shortCompanyName = companyName;
        if (shortCompanyName.length() > 25) {
            shortCompanyName = shortCompanyName.substring(0, 25) + "...";
        }
        if (container != null) {
            container.setDescription(shortCompanyName);
        }

        listingPanel = new ListingPanel<>(ListPanelType.BackendManagementListPanel, drawColumns(), getProvider(), getDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BACKEND_OPTIONS_ADD, BackendManagementListView.this, (sender, args) -> listingPanel.reloadPage());
        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        //Action Menu
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<BackendManagementListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(BackendManagementListItem rowValue) {
                return getActionMenuItems(rowValue);
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);
        //Company ID
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.companyID(), BackendManagementListItem.COMPANY_ID, 50) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getCompanyID() + "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Company Name
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.companyName(), BackendManagementListItem.COMPANY_NAME, 100) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getCompanyName();
            }
        };
        columns.add(column);

        //User Name
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.username(), BackendManagementListItem.USER_NAME, 90) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getUserName();
            }
        };
        columns.add(column);
        //Creator Name

        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.createdBy(), BackendManagementListItem.CREATOR_NAME, 90) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getCreatorName();
            }
        };
        columns.add(column);

        //Creation Time
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.createdDate(), BackendManagementListItem.CREATION_TIME, 70) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getCreateTime() != null ? DateUtils.formatInternal(rowValue.getCreateTime()) : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Last Updater Name
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.modifiedBy(), BackendManagementListItem.UPDATER_NAME, 90) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getUpdaterName();
            }
        };
        columns.add(column);

        //Last Update Time
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(wfmStrings.modifiedDate(), BackendManagementListItem.UPDATE_TIME, 70) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getUpdateTime() != null ? DateUtils.formatInternal(rowValue.getUpdateTime()) : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Host Name
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.hostName(), BackendManagementListItem.HOST_NAME, 70) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.getHostName();
            }
        };
        columns.add(column);

        //Sales Backend shown to User
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.salesBackend(), BackendManagementListItem.SALES_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnableSalesBackend() ? "TRUE" : "FALSE";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Support Backend shown to User
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.supportBackend(), BackendManagementListItem.SUPPORT_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnableSupportBackend() ? "TRUE" : "FALSE";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //System Backend shown to User
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.adminBackend(), BackendManagementListItem.ADMIN_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnableAdminBackend() ? "TRUE" : "FALSE";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.partnerAdminBackend(), BackendManagementListItem.PARTNER_ADMIN_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnablePartnerAdminBackend() ? "TRUE" : "FALSE";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

       /* //PDF Backend shown to User
        columns[12] = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.pdfBackend(), BackendManagementListItem.PDF_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnablePDFBackend() ? "TRUE" : "FALSE";
            }
        };
        columns[12].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);*/

        //Reporting Backend shown to User
        column = new ColumnDefinitionConfig<BackendManagementListItem, String>(backendStrings.developerBackend(), BackendManagementListItem.DEVELOPER_BACKEND, 60) {
            @Override
            public String getCellValue(BackendManagementListItem rowValue) {
                return rowValue.isEnableDeveloperBackend() ? "TRUE" : "FALSE";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private Anchor getActionMenuItems(final BackendManagementListItem rowValue) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);

        MenuPopItem editItem = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
        editItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("backendManagement|add/add" + "/" + companyID + "/" + companyName + "/" + rowValue.getObjectID()));
        actionItemCount++;
        menuBar.addItem(editItem);

        MenuPopItem deleteItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
        deleteItem.setCommand(() -> {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.setMessage(wfmStrings.areYouSureYouWanttoDeleteThisEntry());
            wfmMessageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    //register delete listener
                    BackendService.App.get().deleteBackendManagement(rowValue.getObjectID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                            listingPanel.reloadPage();
                        }
                    });
                }
            });
            wfmMessageBox.open();
        });
        actionItemCount++;
        menuBar.addItem(deleteItem);
        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("backendManagement|add/add" + "/" + companyID + "/" + companyName));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private ListingRequestProvider<BackendManagementListItem> getProvider() {
        return (fp, listingCallback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setCompanyID(companyID);
            BackendService.App.get().getBackendManagementList(fp, new AbstractAsyncCallback<ListResult<BackendManagementListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<BackendManagementListItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
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
}