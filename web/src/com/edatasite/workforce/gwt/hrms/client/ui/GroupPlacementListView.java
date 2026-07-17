package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_SUBMITTED;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_GROUP_PLACEMENT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_GROUP_PLACEMENT_DELETE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_GROUP_PLACEMENT_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_GROUP_PLACEMENT_SUMMARY;

public class GroupPlacementListView extends BaseListView implements Colapse {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<GroupPlacementItem> listingPanel;

    public GroupPlacementListView() {
        super(GROUP_PLACEMENT_LIST);
        setDescription(Property.get(Constants.GROUP_PLACEMENT, wfmStrings.group() + " " + wfmStrings.placement()));
        if (hasPermissionToAdd()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/add"));
        }
    }


    private CustomColumnDefinitionConfig[] getColumn() {
        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();
        ColumnDefinitionConfig columns;

        columns = new ColumnDefinitionConfig<GroupPlacementItem, Anchor>(wfmStrings.action(), "PLACEMENT", Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(GroupPlacementItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (hasPermissionToSummary()) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    if (item.getOverallStatus() != null && item.getOverallStatus().getCode().equals(GROUP_PLACEMENT_DRAFT)) {
                        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/edit/" + item.getId(), item.getPlacementCode()));
                    } else {
                        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/summary/" + item.getId(), item.getPlacementCode()));
                    }
                    actionItemCount++;
                    menuBar.addItem(summary);
                }
                if (hasPermissionToEdit()) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/edit/" + item.getId(), item.getPlacementCode()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (hasPermissionToDelete()) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                    delete.setCommand(() -> deletePlacement(item.getId()));
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return hasPermissionToEdit() || hasPermissionToDelete() || hasPermissionToSummary() ? toolItem.getAction() : null;
            }
        };
        columns.setColumnSortable(false);
        columns.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<GroupPlacementItem, Widget>(wfmStrings.number(), GroupPlacementItem.NUMBER, 150) {
            @Override
            public Widget getCellValue(GroupPlacementItem item) {
                Label label = new Label(item.getPlacementCode());
                label.setStyleName("uploadLinkStyle2");
                if (hasPermissionToSummary()) {
                    if (item.getOverallStatus() != null && item.getOverallStatus().getCode().equals(GROUP_PLACEMENT_DRAFT)) {
                        label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/edit/" + item.getId(), item.getPlacementCode()));
                    } else {
                        label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/summary/" + item.getId(), item.getPlacementCode()));
                    }
                }
                return label;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.status(), GroupPlacementItem.STATUS, 150) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                String status = " ";
                if (item.getOverallStatus() != null && item.getOverallStatus().getCode() != null) {
                    switch (item.getOverallStatus().getCode()) {
                        case GROUP_PLACEMENT_APPROVED:
                            status = wfmStrings.approved();
                            break;
                        case GROUP_PLACEMENT_REJECTED:
                            status = wfmStrings.rejected();
                            break;
                        case GROUP_PLACEMENT_SUBMITTED:
                            status = wfmStrings.waitingForApproval();
                            break;
                        case GROUP_PLACEMENT_DRAFT:
                            status = wfmStrings.draft();
                            break;
                    }
                }
                return status;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.approver(), GroupPlacementItem.APPROVER, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setShow(true);
        columns.setColumnSortable(true);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.date(), GroupPlacementItem.DATE, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.createdBy(), GroupPlacementItem.CREATOR, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.createdDate(), GroupPlacementItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return DateUtils.formatInternal(item.getCreatedDate().getDate());
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.modifiedBy(), GroupPlacementItem.UPDATER, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<GroupPlacementItem, String>(wfmStrings.modifiedDate(), GroupPlacementItem.UPDATED_DATE, 110) {
            @Override
            public String getCellValue(GroupPlacementItem item) {
                return DateUtils.formatInternal(item.getUpdatedDate().getDate());
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);

        return columnDefinitionConfigs.toArray(new ColumnDefinitionConfig[]{});
    }


    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (hasPermissionToAdd()) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/add");
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

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


                    @Override
                    public ViewName getView() {
                        return ViewName.GroupPlacementList;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }


            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/add"));
                    return addnew;
                } else {
                    return null;
                }
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

        };


    }

    private ListingRequestProvider<GroupPlacementItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> HrmsService.App.get().getGroupPlacementList(filterParametrs, new AbstractAsyncCallback<ListResult<GroupPlacementItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<GroupPlacementItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.GroupPlacementList, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_PLACEMENT_ADD, GroupPlacementListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_PLACEMENT_DELETE, GroupPlacementListView.this, (sender, args) -> listingPanel.reloadPage());

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/groupPlacementListExcelHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, filterParameter);
        });
        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/groupPlacementListPdfHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, filterParameter);

        });


        add(listingPanel);
        return null;
    }


    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(HRMS_GROUP_PLACEMENT_ADD);
    }

    private boolean hasPermissionToEdit() {
        return Utils.hasPermission(HRMS_GROUP_PLACEMENT_EDIT);
    }

    private boolean hasPermissionToDelete() {
        return Utils.hasPermission(HRMS_GROUP_PLACEMENT_DELETE);
    }

    private boolean hasPermissionToSummary() {
        return Utils.hasPermission(HRMS_GROUP_PLACEMENT_SUMMARY);
    }

    private void deletePlacement(Integer id) {
        LoadingPanel.loading(true);
        HrmsService.App.get().deleteGroupPlacementItem(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.placement()));
                listingPanel.reloadPage();
            }
        });

    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
        return GROUP_PLACEMENT_LIST;
    }
}
