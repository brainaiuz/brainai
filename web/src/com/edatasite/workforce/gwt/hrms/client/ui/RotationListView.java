package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
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

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ROTATION_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ROTATION_DELETE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ROTATION_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ROTATION_EXPORT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ROTATION_SUMMARY;


public class RotationListView extends BaseListView implements Constants {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<RotationItem> listingPanel;

    public RotationListView() {
        super(ROTATION_LIST);
        setDescription(hrmsStrings.rotations());
        if (hasPermissionToAdd()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/add"));
        }
    }


    private CustomColumnDefinitionConfig[] getColumn() {
        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();
        ColumnDefinitionConfig columns;

        columns = new ColumnDefinitionConfig<RotationItem, Anchor>(wfmStrings.action(), "ROTATION", LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(RotationItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (hasPermissionToSummary()) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/summary/" + item.getId(), item.getRotationCode()));
                    actionItemCount++;
                    menuBar.addItem(summary);
                }
                if (hasPermissionToEdit() && item.getOverallStatus() != null && item.getOverallStatus().getCode() != null && !ROTATION_APPROVED.equals(item.getOverallStatus().getCode())) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/edit/" + item.getId()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (hasPermissionToDelete()) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                    delete.setCommand(() -> deleteRotationItem(item.getId()));
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return hasPermissionToEdit() || hasPermissionToDelete() || hasPermissionToSummary() ? toolItem.getAction() : null;
            }
        };
        columns.setColumnSortable(false);
        columns.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<RotationItem, Widget>(wfmStrings.number(), RotationItem.NUMBER, 150) {
            @Override
            public Widget getCellValue(RotationItem item) {
                Label label = new Label(item.getRotationCode());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.hasPermission(HRMS_ROTATION_SUMMARY)) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/summary/" + item.getId(), item.getRotationCode()));
                }
                return label;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.status(), RotationItem.STATUS, 150) {
            @Override
            public String getCellValue(RotationItem item) {
                String status = " ";
                if (item.getOverallStatus() != null && item.getOverallStatus().getCode() != null) {
                    switch (item.getOverallStatus().getCode()) {
                        case ROTATION_APPROVED:
                            status = wfmStrings.approved();
                            break;
                        case ROTATION_REJECTED:
                            status = wfmStrings.rejected();
                            break;
                        case ROTATION_SUBMITTED:
                            status = wfmStrings.waitingForApproval();
                            break;
                        case ROTATION_DRAFT:
                            status = wfmStrings.draft();
                            break;
                    }
                }
                return status;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.approver(), RotationItem.APPROVER, 110) {
            @Override
            public String getCellValue(RotationItem item) {
                return item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setShow(true);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.date(), RotationItem.DATE, 110) {
            @Override
            public String getCellValue(RotationItem item) {
                return DateUtils.format(item.getDate().getDate());
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.createdBy(), RotationItem.CREATOR, 110) {
            @Override
            public String getCellValue(RotationItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.createdDate(), RotationItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(RotationItem item) {
                return DateUtils.formatInternal(item.getCreatedDate().getDate());
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.modifiedBy(), RotationItem.UPDATER, 110) {
            @Override
            public String getCellValue(RotationItem item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setColumnSortable(true);
        columns.setShow(true);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<RotationItem, String>(wfmStrings.modifiedDate(), RotationItem.UPDATED_DATE, 110) {
            @Override
            public String getCellValue(RotationItem item) {
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
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/add");
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
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.ROTATION_STATUS);
                        fields.add(ListingChooseFilter.ROTATION_CREATOR);
                        fields.add(ListingChooseFilter.ROTATION_APPROVER);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.RotationList;
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
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/add"));
                    return addnew;
                } else {
                    return null;
                }
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                if (hasPermissionToExport())
                    exportOption.initExport(null, true);
            }

        };


    }

    private ListingRequestProvider<RotationItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> HrmsService.App.get().getRotationList(filterParametrs, new AbstractAsyncCallback<ListResult<RotationItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<RotationItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.RotationList, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ROTATION_ADD, RotationListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ROTATION_DELETE, RotationListView.this, (sender, args) -> listingPanel.reloadPage());

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/rotationListExcelHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, filterParameter);
        });
        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/rotationListPdfHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, filterParameter);

        });


        add(listingPanel);
        return null;
    }


    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(HRMS_ROTATION_ADD);
    }

    private boolean hasPermissionToExport() {
        return Utils.hasPermission(HRMS_ROTATION_EXPORT);
    }

    private boolean hasPermissionToEdit() {
        return Utils.hasPermission(HRMS_ROTATION_EDIT);
    }

    private boolean hasPermissionToDelete() {
        return Utils.hasPermission(HRMS_ROTATION_DELETE);
    }

    private boolean hasPermissionToSummary() {
        return Utils.hasPermission(HRMS_ROTATION_SUMMARY);
    }

    private void deleteRotationItem(Integer id) {
        LoadingPanel.loading(true);
        HrmsService.App.get().deleteRotationItem(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), hrmsStrings.rotations()));
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
        return ROTATION_LIST;
    }
}
