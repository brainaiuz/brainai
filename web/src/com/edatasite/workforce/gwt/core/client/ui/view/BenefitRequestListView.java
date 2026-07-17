package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
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
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Created by Djuraev on 8/7/15.
 */
public class BenefitRequestListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());

    private ListingPanel<BenefitRequestItem> list;
    private final Integer userID;


    public BenefitRequestListView(Integer id) {
        super(BENEFIT_REQUESTS);
        setDescription(property.getPlural(hrmsStrings.benefitRequests()));
        this.userID = id;
        if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
            setAddNew("benefitRequest|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "org-benifitRequest";
    }

    protected Widget onInitialize() {
        list = new ListingPanel(ListPanelType.BenefitRequestPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());

        list.setExcelListener(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/benefitRequestListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParametrs);
        });
        list.setPDFListener(event -> {
            String pdfURL = CommandConstants.PDF_URL + "/benefitRequestListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BENEFIT_REQUEST_ADD, BenefitRequestListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BENEFIT_REQUEST_DELETE, BenefitRequestListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BENEFIT_REQUEST_UPDATE, BenefitRequestListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<BenefitRequestItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final BenefitRequestItem rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                MenuPopItem menuItem;
                int menuItemCount = 0;

//                //Edit benefit
                if (BR_WAITING_FOR_APPROVAL.equals(rowValue.getStatus().getCode()) && (Utils.hasPermission(PermissionConstants.EDIT_BENEFIT_REQUEST) || Utils.hasPermission(PermissionConstants.CHANGE_BENEFIT_REQUEST_APPROVER))) {
                    menuItem = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    menuItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("benefitRequest|add/" + rowValue.getObjectID(), rowValue.getRequester(), rowValue.getRequester()));
                    menuItemCount++;
                    menuBar.addItem(menuItem);
                }

                boolean isBenefitOwnerOrManager = Utils.getUserID().equals(rowValue.getApproverID()) || Utils.hasPermission(PermissionConstants.APPROVE_REJECT_ALL_BENEFIT_REQUESTS);

                if (isBenefitOwnerOrManager && BR_WAITING_FOR_APPROVAL.equals(rowValue.getStatus().getCode())) {
                    //Approve
                    menuItem = new MenuPopItem(wfmStrings.approve(), "icon-add-task");
                    menuItem.setCommand(() -> changeStatus(rowValue, BR_APPROVED, null));
                    menuItemCount++;
                    menuBar.addItem(menuItem);

                    //Reject
                    menuItem = new MenuPopItem(wfmStrings.reject(), "icon-reject");
                    menuItem.setCommand(() -> {
                        GrayForm grayForm = new GrayForm();
                        grayForm.noteShell(wfmStrings.rejectionReason(), true, false, null);
                        grayForm.addHistoryPanel(false);
                        Command noteListener = () -> changeStatus(rowValue, BR_REJECTED, grayForm.getHistory().getComment());
                        grayForm.setNoteListener(noteListener);
                    });
                    menuItemCount++;
                    menuBar.addItem(menuItem);
                }

                //Delete benefit
                if (Utils.hasPermission(PermissionConstants.REMOVE_BENEFIT_REQUEST)) {
                    final MenuPopItem deleteBenefit = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteBenefit.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                        //message.setSize(300, 150);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                AvailabilityService.App.get().deleteBenefitRequest(rowValue.getObjectID(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), hrmsStrings.benefitRequest()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BENEFIT_REQUEST_DELETE, result, BenefitRequestListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuItemCount++;
                    menuBar.addItem(deleteBenefit);
                }

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.requester(), BenefitRequestItem.REQUESTER, 100) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return rowValue.getRequester();
            }
        };
        column.setColumnSortable(true);
        columns.add(column);


        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.type(), BenefitRequestItem.BENEFIT_TYPE, 80) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return rowValue.getBenefitName();
            }
        };
        column.setColumnSortable(true);
        columns.add(column);


        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.qty(), BenefitRequestItem.REQUESTED_QUANTITY, 80) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return String.valueOf(Utils.getNumberFormat().format(rowValue.getRequestedQuantity()));
            }
        };
        columns.add(column);


        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.date(), BenefitRequestItem.DATE, 80) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return rowValue.getDate() != null ? format.format(rowValue.getDate().getNonConvertedDate()) : "";
            }
        };
        columns.add(column);


        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.approver(), BenefitRequestItem.APPROVER, 100) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return rowValue.getApprover();
            }
        };
        column.setColumnSortable(true);
        columns.add(column);


        column = new ColumnDefinitionConfig<BenefitRequestItem, String>(wfmStrings.status(), BenefitRequestItem.STATUS, 80) {
            @Override
            public String getCellValue(BenefitRequestItem rowValue) {
                return rowValue.getStatus().getName() != null ? rowValue.getStatus().getName() : "";
            }
        };
        column.setColumnSortable(true);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private void changeStatus(BenefitRequestItem item, String status, String note) {
        LoadingPanel.loading(true);
        AvailabilityService.App.get().changeBenefitRequestStatus(item.getObjectID(), status, note, item.getRequestedQuantity(), new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result > 0) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BENEFIT_REQUEST_UPDATE, null, BenefitRequestListView.this);
                } else if (result == 0) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                } else {
                    Info.show("Not enough quantity allowance in " + item.getBenefitName(), Info.Type.WARNING);
                }
            }
        });
    }

    private ListingRequestProvider<BenefitRequestItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setEmployeeId(userID);
            AvailabilityService.App.get().getBenefitRequestList(filterParametrs, new AbstractAsyncCallback<ListResult<BenefitRequestItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<BenefitRequestItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("benefitRequest|add/add") : null;
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
                        return (data, callBack) -> {
                            AvailabilityService.App.get().getBenefitRequestsFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callBack.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callBack.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

          

            private FacetContentConfigure getFacetContentConfigure() {
                FacetContentConfigure contentConfigure = new FacetContentConfigure(2, "Benefit Request Filter");

                contentConfigure.addContentConfigure(FacetContentType.BenefitRequesFacetFilter.getContentCode()[0], wfmStrings.requester(), new FacetFieldConfigure() {
                    @Override
                    public String getSolrFieldCriteriaName() {
                        return "requester";
                    }

                    @Override
                    public String getSolrFacetFieldName() {
                        return "requester";
                    }
                });
                contentConfigure.addContentConfigure(FacetContentType.BenefitRequesFacetFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
                    @Override
                    public String getSolrFieldCriteriaName() {
                        return "type";
                    }

                    @Override
                    public String getSolrFacetFieldName() {
                        return "type";
                    }
                });

                contentConfigure.addContentConfigure(FacetContentType.BenefitRequesFacetFilter.getContentCode()[2], wfmStrings.status(), new FacetFieldConfigure() {
                    @Override
                    public String getSolrFieldCriteriaName() {
                        return "status";
                    }

                    @Override
                    public String getSolrFacetFieldName() {
                        return "status";
                    }
                });
                contentConfigure.addContentConfigure(FacetContentType.BenefitRequesFacetFilter.getContentCode()[3], wfmStrings.approver(), new FacetFieldConfigure() {
                    @Override
                    public String getSolrFieldCriteriaName() {
                        return "approver";
                    }

                    @Override
                    public String getSolrFacetFieldName() {
                        return "approver";
                    }
                });
                contentConfigure.setDatePeriodPanelEnabled(false);
                return contentConfigure;
            }
            
        

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("benefitRequest|add/add"));
                    return addnew;
                } else {
                    return null;
                }
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.thereAreNoItemsToShow());
                if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
                    message = new DefaultNoItemsMessage(hrmsStrings.thereAreNoBenefit());
                    message.setHref("benefitRequest|add/add");
                    message.setTextBeforeLink(hrmsStrings.noBenefitRequestLink());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
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

    public String getPropertyCode() {
        return BENEFIT_REQUESTS;
    }



    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadDependents(new ListingFilterParameter(), null, container);
    }

    private void loadDependents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setEmployeeId(userID);
        AvailabilityService.App.get().getBenefitRequestList(fp, new AbstractAsyncCallback<ListResult<BenefitRequestItem>>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(ListResult<BenefitRequestItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }


}
