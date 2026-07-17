package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveRequestListView extends BaseListView implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<LeaveRequestLisItem> listingTable;
    protected HashSet<LeaveRequestLisItem> selectedItems = new HashSet<>();
    private ActionButton more;
    private final String leaveRequest = "leaveRequest";

    public LeaveRequestListView() {
        super("leaveRequestListView");
        setDescription(property.getPlural(wfmStrings.leaveRequests()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            setAddNew("availability|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-task";
    }

    @Override
    public FlowPanel getHelpContainer() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.LeaveRequestApprove, getColumns(), getListData(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveLREditCellValue((LeaveRequestLisItem) rowValue, columnCodeName));
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/leaveRequestListPDFHandler";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListPDF(pdfURL, fp);
        });

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadLeaveRequestListExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListExcel(excelURL, fp);
        });

        listingTable.addSelectionRowHandler(selectedRows -> {
            selectedItems = selectedRows;
            for (LeaveRequestLisItem item : selectedItems) {
                if (!item.getApproverId().equals(Utils.getUserID())) {
                    more.setVisible(false);
                    break;
                }
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RQUEST_APPROVED, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RQUEST_REJECTED, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_ADD, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MULTIPLE_APPROVAL_REQUEST_UPDATE, LeaveRequestListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();
        ColumnDefinitionConfig columns;
        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final LeaveRequestLisItem item) {
                String statusCode = item.getStatusCode() != null ? item.getStatusCode() : "";
                boolean isBeforeLockDate = (Utils.isAttendanceLocked() && item.getStartDate() != null && DateUtils.getTransactionLockDate().after(item.getStartDate().getNonConvertedDate()));
                final Integer objectId = item.getObjectId();
                MenuBar actions = new MenuBar(true);

                actions.setAutoOpen(true);
                int itemCount = 1;
                if (!statusCode.equals(DRAFT)) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView());
                    if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SUMMARY)) {
                        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(getlink(item), item.getLeaveRequestCode(), item.getLeaveRequestCode()));
                    } else {
                        summary.setCommand(() -> Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING));
                    }
                    actions.addItem(summary);
                }

                if (!isBeforeLockDate && statusCode.equals(DRAFT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.ensureDebugId(leaveRequest + "edit");
                    edit.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add/" + item.getEmployeeId() + "/" + objectId, hrmsStrings.editLeaveRequest());
                    });
                    itemCount++;
                    actions.addItem(edit);
                }
                if (!isBeforeLockDate && Constants.LR_TYPE_ANNUAL_LEAVE.equals(item.getReasonCode()) && statusCode.equals(LR_STATUS_APPROVED) && Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_RECALCULATE)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.recalculateLeaveRequest(), "icon-edit");
                    edit.ensureDebugId(leaveRequest + "edit");
                    edit.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add/recalculate/" + item.getEmployeeId() + "/" + objectId, hrmsStrings.editLeaveRequest());
                    });
                    itemCount++;
                    actions.addItem(edit);
                }

                if (!isBeforeLockDate && item.isPending() && (item.isUserIsCurrentApprover() || Utils.isAdmin())) {
                    MenuPopItem approve = new MenuPopItem(wfmStrings.approve());
                    approve.setCommand(() -> approveOrReject(Constants.LR_STATUS_SS_APPROVED, Collections.singletonList(item)));
                    actions.addItem(approve);
                    itemCount++;

                    MenuPopItem reject = new MenuPopItem(wfmStrings.reject());
                    reject.setCommand(() -> approveOrReject(Constants.LR_STATUS_SS_DENIED, Collections.singletonList(item)));
                    actions.addItem(reject);
                    itemCount++;
                }

                PropertyItem propertyItem = Utils.getProperTy("leave_request_list");
                if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                    MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                    MenuBar convertMenu = new MenuBar(true);
                    convertMenu.setAutoOpen(true);
                    int convertItems = 0;
                    for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                        if (convertItem != null) {
                            convertItems = getConvertItems(item, actions, convertMenu, convertItems, convertItem);
                        }
                    }

                    if (convertItems > 0) {
                        convertMenuPopItem.setSubMenu(convertMenu);
                        actions.addItem(convertMenuPopItem);
                        itemCount++;
                    }
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_PDF_BUTTON)) {
                    MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
                    pdfVersion.ensureDebugId("exportToPDF");
                    pdfVersion.setCommand(() -> new PDFTemplateSelector(AccountingConstants.LEAVE_REQUEST, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            LeaveRequestObject requestObject = new LeaveRequestObject(item.getObjectId(), item.getEmployeeId(), id);
                            String pdfUrl = CommandConstants.PDF_URL + "/employeeLeaveRequestViewPDFHandler";
                            HashMap<String, String> requestParams = requestObject.getRequestParams();
                            listingTable.callListPDF(pdfUrl, requestParams);
                        }
                    }));
                    itemCount++;
                    actions.addItem(pdfVersion);
                }

                if (!isBeforeLockDate && Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                    delete.setCommand(() -> deleteAction(item.getObjectId(), item.isPending(), item.getEmployeeId()));
                    actions.addItem(delete);
                    delete.ensureDebugId("delete-leave-request");
                    itemCount++;
                }

                ToolItem toolItem = new ToolItem(itemCount);
                toolItem.setWidget(actions);
                return toolItem.getAction();
            }
        };
        columns.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, Widget>(wfmStrings.number(), LeaveRequestLisItem.CODE, 150) {
            @Override
            public Widget getCellValue(LeaveRequestLisItem item) {
                Label label = new Label(item.getLeaveRequestCode() != null ? item.getLeaveRequestCode() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SUMMARY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getlink(item),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode(),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, Widget>(wfmStrings.employee(), LeaveRequestLisItem.EMPLOYEE_NAME, 150) {
            @Override
            public Widget getCellValue(LeaveRequestLisItem item) {
                Label label = new Label(item.getEmployeeName() != null ? item.getEmployeeName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SUMMARY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getlink(item),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode(),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, Widget>(wfmStrings.reason(), LeaveRequestLisItem.REASON, 80) {
            @Override
            public Widget getCellValue(LeaveRequestLisItem item) {
                Label label = new Label(item.getReason() != null ? item.getReason() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(event -> {
                    if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SUMMARY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getlink(item),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode(),
                                item.getStatusCode() != null && item.getStatusCode().equals(DRAFT) ? hrmsStrings.editLeaveRequest() : item.getLeaveRequestCode());
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                return label;
            }
        };
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.description(), LeaveRequestLisItem.DESCRIPTION, 150) {
            @Override
            public String getCellValue(LeaveRequestLisItem item) {
                return item.getDescription();
            }
        };
        columns.setMinimumColumnWidth(120);
        columns.setShow(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.from(), LeaveRequestLisItem.FROM_DATE, 100) {
            @Override
            public String getCellValue(LeaveRequestLisItem item) {
                if (item.getStartDate() != null) {
                    return item.isAllDay() ? DateUtils.format(item.getStartDate()) : DateUtils.formatInternal(item.getStartDate().getNonConvertedDate());
                }
                return wfmStrings.notAvailable();
            }
        };
        columns.setMinimumColumnWidth(80);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.to(), LeaveRequestLisItem.TO_DATE, 100) {
            @Override
            public String getCellValue(LeaveRequestLisItem item) {
                if (item.getEndDate() != null) {
                    return item.isAllDay() ? DateUtils.format(item.getEndDate()) : DateUtils.formatInternal(item.getEndDate().getNonConvertedDate());
                }
                return wfmStrings.notAvailable();
            }
        };
        columns.setMinimumColumnWidth(80);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.status(), LeaveRequestLisItem.STATUS, 80) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getStatus();
            }
        };
        columns.setMaximumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.leaveDays(), LeaveRequestLisItem.LEAVE_DAYS, 50) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getLeaveDays();
            }
        };
        columns.setMaximumColumnWidth(70);
        columns.setColumnSortable(false);
        columns.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.type(), LeaveRequestLisItem.TYPE, 120) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getType();
            }
        };
        columns.setMaximumColumnWidth(100);
        columns.setColumnSortable(true);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.approver(), LeaveRequestLisItem.APPROVER, 150) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getApproverName();
            }
        };
        columns.setMaximumColumnWidth(100);
        columnDefinitionConfigs.add(columns);


        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.createdDate(), LeaveRequestLisItem.CREATED_DATE, 100) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getCreatedDate() != null ? DateUtils.formatInternal(rowValue.getCreatedDate()) : "";
            }
        };
        columns.setMaximumColumnWidth(80);
        columns.setShow(false);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.registeredBy(), LeaveRequestLisItem.REGISTERED_BY, 150) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getCreator();
            }
        };
        columns.setMaximumColumnWidth(100);
        columns.setShow(false);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(wfmStrings.position(), LeaveRequestLisItem.POSITION, 150) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getPosition();
            }
        };
        columns.setMaximumColumnWidth(100);
        columns.setShow(false);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<LeaveRequestLisItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), LeaveRequestLisItem.DEPARTMENT, 150) {
            @Override
            public String getCellValue(LeaveRequestLisItem rowValue) {
                return rowValue.getDepartment();
            }
        };
        columns.setMaximumColumnWidth(100);
        columns.setShow(false);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        return columnDefinitionConfigs.toArray(new ColumnDefinitionConfig[0]);
    }

    private String getlink(LeaveRequestLisItem item) {
        if (item.getStatus() == null || Constants.DRAFT.equals(item.getStatusCode())) {
            return "availability|add/add/" + item.getEmployeeId() + "/" + item.getObjectId();
        }
        return "leaverequest/" + item.getObjectId();
    }

    private void deleteAction(Integer objectID, boolean pending, Integer employeeId) {
        if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST) || (Utils.getUserID().equals(employeeId) && pending)) {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.warning());
            message.setMessage(hrmsStrings.areYouSureYouWanttoDeleteThisLeaveRequest());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    AvailabilityService.App.get().deleteSickRequestListByParent(objectID, new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, null, LeaveRequestListView.this);
                            Info.show((Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.leaveRequest())), Info.Type.INFO);
                        }
                    });
                }
            });
            message.open();
        } else {
            Info.show(wfmStrings.youDontHavePermission());
        }
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[0], wfmStrings.employee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_EMPLOYEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_EMPLOYEE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[1], wfmStrings.reason(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_REASON_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_REASON_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[2], wfmStrings.current() + " " + wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_STATUS_ID_CODE;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[3], wfmStrings.currentApprover(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_APPROVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_APPROVER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[4], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_DEPARTMENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_DEPARTMENT_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.LeaveFacetFilter.getContentCode()[5], wfmStrings.position(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrLeaveRequestConst.FIELD_POSITION_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrLeaveRequestConst.FIELD_POSITION_ID_NAME;
            }
        });

        contentConfigure.addContentConfigureDateListBox(SolrLeaveRequestConst.FIELD_START_DATE, wfmStrings.startDate());
        contentConfigure.addContentConfigureDateListBox(SolrLeaveRequestConst.FIELD_END_DATE, wfmStrings.endDate());
        contentConfigure.addContentConfigureDateListBox(SolrLeaveRequestConst.FIELD_CREATED_DATE, wfmStrings.createdDate());
        return contentConfigure;
    }

    private GuideListingPanelDesign getDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add") : null;
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
                        return (data, callback) -> RbacService.App.get().getLeaveFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void failure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }

                            @Override
                            public void success(FacetFilterRpc facetFilterRpc) {
                                callback.onSuccess(facetFilterRpc);
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
                    final ActionButton addNewButton = getAddNewButton();
                    addNewButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add"));
                    return addNewButton;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.messCurrentlyLeaveRequest());
                message.setTextBeforeLink(hrmsStrings.messAddLeaveRequestsClicking());
                message.setHref(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add"));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public ActionButton initTopToolBarMore() {
                more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                more.ensureDebugId("leaverequest_list_more_button");

                more.addDomHandler(event -> {
                    MenuBar menu = getMenuItems();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                }, MouseOverEvent.getType());
                return more;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    private MenuBar getMenuItems() {
        MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);

        MenuPopItem approveAll = new MenuPopItem(wfmStrings.approveAll());
        approveAll.getElement().setId("approveAll");
        approveAll.setCommand(() -> updateStatus(Constants.LR_STATUS_SS_APPROVED));
        menuBar.addItem(approveAll);

        MenuPopItem rejectAll = new MenuPopItem(wfmStrings.rejectAll());
        rejectAll.getElement().setId("rejectAll");
        rejectAll.setCommand(() -> updateStatus(Constants.LR_STATUS_SS_DENIED));
        menuBar.addItem(rejectAll);

        return menuBar;
    }

    private void updateStatus(String code) {
        if (selectedItems.size() == 0) {
            Info.show(wfmStrings.pleaseSelect(), Info.Type.INFO);
            return;
        }
        approveOrReject(code, new ArrayList<>(selectedItems));
    }

    private void approveOrReject(String code, List<LeaveRequestLisItem> items) {
        if (code.equals(LR_STATUS_SS_DENIED)) {
            KpiModal reasonBox = new KpiModal();
            reasonBox.setTitle(wfmStrings.pleaseSpecifyRejectionReason());
            reasonBox.setFlexAlignContent(FlexAlignContent.CENTER);
            final TextArea txtReason = new TextArea();
            txtReason.setHeight("120px");
            txtReason.setStyleName("form-control file--SaleQuoteSummaryVIew");
            reasonBox.add(txtReason);
            reasonBox.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> {
                reasonBox.close();
            }));
            reasonBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                String rejectionReason = txtReason.getText();
                if (rejectionReason == null || "".equals(rejectionReason)) {
                    txtReason.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.pleaseSpecifyRejectionReason());
                    return;
                }
                reasonBox.close();
                saveStatus(code, items, rejectionReason);
            }));
            reasonBox.setWidth("400px");
            reasonBox.center();
        } else {
            saveStatus(code, items, null);
        }

    }

    private void saveStatus(String code, List<LeaveRequestLisItem> items, String rejectionReason) {

        ArrayList<Integer> ids = items.stream()
                .map(LeaveRequestLisItem::getObjectId)
                .collect(Collectors.toCollection(ArrayList::new));
        LoadingPanel.loading(true);
        AvailabilityService.App.get().updateMultipleRequests(code, ids, rejectionReason, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                listingTable.reloadPage();
                if (code.equals(Constants.LR_STATUS_SS_APPROVED)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, result, LeaveRequestListView.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, result, LeaveRequestListView.this);
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.approvalProcess()), Info.Type.INFO);
            }
        });
    }

    private ListingRequestProvider<LeaveRequestLisItem> getListData() {
        return (filterParameter, callback) -> AvailabilityService.App.get().getLeaveRequestList(filterParameter, new AbstractAsyncCallback<ListResult<LeaveRequestLisItem>>() {
            @Override
            public void failure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void success(ListResult<LeaveRequestLisItem> leaveRequestListResult) {
                callback.onSuccess(leaveRequestListResult);
            }
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return "leave_request_list";
    }

    private int getConvertItems(LeaveRequestLisItem rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_CERTIFICATE.equals(convertItem.getCode()) && Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD)) {
            MenuPopItem convertToCertificate = new MenuPopItem(Property.get(Constants.CERTIFICATES_LIST, wfmStrings.certificates()), "icon-send-sales-invoice");
            convertToCertificate.setCommand(() -> {
                convertToCertificate.closeAll(menuBar);
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add/CONVERT/" + RelationItem.TYPE_LEAVE_REQUEST + "/" + rowValue.getObjectId());
                }
            });
            convertToCertificate.ensureDebugId("convert_certificate");
            convertMenu.addItem(convertToCertificate);
            convertItems++;
        }
        return convertItems;
    }

    private void saveLREditCellValue(LeaveRequestLisItem rowValue, String columnCodeName) {
        AvailabilityService.App.get().saveLREditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                listingTable.reloadPage();
            }
        });
    }
}
