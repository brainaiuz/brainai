package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.view.UpdatesDialogBox;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/27/15
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunListView extends BaseListView implements Constants {

    private ListingPanel<SinglePayrunItem> list;
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private final HorizontalPanel horizontalPanel = new HorizontalPanel();
    private Integer employeeID;
    private final HashMap<Integer, String> monthItems = new HashMap<>();
    private Boolean hasPayments = false;

    public SinglePayrunListView() {
        super(SINGLE_PAYRUN_LIST);
        setDescription(property.getPlural(wfmStrings.payslips()));
        if (hasPermissionToAdd()) {
            setAddNew("singlePayrun|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_ADD);
    }

    public SinglePayrunListView(Integer employeeID) {
        this();
        this.employeeID = employeeID;
        if (hasPermissionToAdd()) {
            setAddNew("singlePayrun|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems.put(i, DateTimeFormat.getFormat("MMMM").format(date));
            date = DateUtil.addMonths(date, 1);
        }

        list = new GuideListingPanel(ListPanelType.SinglePayrunListPanel, getColumn(), getListProvider(), getListDesign());

        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveSinglePayrunCellValue((SinglePayrunItem) rowValue, columnCodeName));

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/singlePayrunListExcelHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParameter);
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/singlePayrunListPDFHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParameter);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYSLIP_SAVED, SinglePayrunListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_ADD, SinglePayrunListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_DELETE, SinglePayrunListView.this, (sender, args) -> list.reloadPage());

        add(horizontalPanel);
        add(list);
        return null;
    }

    private void saveSinglePayrunCellValue(SinglePayrunItem rowValue, String columnCodeName) {
        if (rowValue.isApproved()) {
            Info.warn("You cannot edit approved item");
        } else {
            PayrollService.App.get().saveSingleParunCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        }
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? SinglePayrunListView.this::addNewPayrun : null;
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
                        return (data, callback) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            if (employeeID != null) {
                                data.setUserID(employeeID);
                            }
                            RbacService.App.get().getSinglePayrunFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
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

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        addNewPayrun();
                    });
                    return addNew;
                } else {
                    return null;
                }
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.currentlyThereAreNoPayslips(), wfmStrings.payslips()));
                message.setTextBeforeLink(property.getSingular(payrollStrings.noSinglePayrunBeforeLinkMessage(), wfmStrings.payslip()));
                message.setHref("singlePayrun|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT);
            }
        };
    }

    private void addNewPayrun() {
        SinksContainerFactory.entryPoint.onHistoryChanged("singlePayrun|add/add");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.SinglePayrunFacetFilter.getContentCode()[0], wfmStrings.employee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SinglePayrunFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSinglePayrunRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSinglePayrunRepresenter.FIELD_STATUS_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SinglePayrunFacetFilter.getContentCode()[2], wfmStrings.approver(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSinglePayrunRepresenter.FIELD_APPROVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSinglePayrunRepresenter.FIELD_APPROVER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SinglePayrunFacetFilter.getContentCode()[4], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        return contentConfigure;
    }


    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[11];
        columnConfig[index] = new ColumnDefinitionConfig<SinglePayrunItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final SinglePayrunItem item) {
                int actionItemCount = 0;
                boolean isBeforeLockDate = (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate().after(item.getToDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);
                if (!PAYRUN_STATUS_DRAFT.equals(item.getStatusCode()) && (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_VIEW) || Utils.isHRMS())) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("singlePayrun|viewPayslip/" + item.getObjectID(), item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(), item.getEmployee()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }
                if (!isBeforeLockDate && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT) && !item.isApproved()) {

                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("singlePayrun|edit/" + item.getObjectID() + "/" + item.getEmployeeID(), item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(), item.getEmployee()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_PDF) || Utils.isHRMS()) {
                    MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    pdf.setCommand(() -> new PayrollPDFTemplateSelector(SINGLE_PAYRUN, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(item, id);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(pdf);
                }
                if (!isBeforeLockDate && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(Utils.isUKCompany() ? payrollStrings.rollback() : wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        PayrollService.App.get().hasPaymentItems(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                try {
                                    throw caught;
                                } catch (Throwable ex) {
                                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                                }
                            }

                            @Override
                            public void onSuccess(Boolean hasPayments) {
                                hasPayments(hasPayments);
                            }
                        });
                        if (hasPayments) {
                            messageBox.setMessage(wfmStrings.deleteAllRelatedPayments() + " " + wfmStrings.sureYouWantToDelete());
                        }
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deleteSinglePayrun(item.getObjectID(), item.getEmployeeID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.payslip()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, SinglePayrunListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_UPDATE)) {
                    MenuPopItem updates = new MenuPopItem(wfmStrings.updates(), "payslip-16");
                    updates.setCommand(() -> PayrollService.App.get().getSinglePayrunUpdates(item.getObjectID(), new AbstractAsyncCallback<ArrayList<MyUpdateItem>>() {
                        @Override
                        public void onSuccess(ArrayList<MyUpdateItem> result) {
                            final UpdatesDialogBox updatesDialogBox = new UpdatesDialogBox(Property.get(SINGLE_PAYRUN_LIST, payrollStrings.singlePayrunUpdates(), wfmStrings.payslip()), result);
                            updatesDialogBox.open();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(updates);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, SimpleLink>(wfmStrings.employeeCode(), SinglePayrunItem.EMPLOYEE_CODE, 80) {
            @Override
            public SimpleLink getCellValue(SinglePayrunItem item) {
                boolean isBeforeLockDate = (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate().after(item.getToDate().getNonConvertedDate()));
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_VIEW) || Utils.isHRMS()) {
                    SimpleLink link = null;
                    if (!isBeforeLockDate && PAYRUN_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT)) {
                        link = getLink(item.getEmployeeCode(), "singlePayrun|edit/" + item.getObjectID() + "/" + item.getEmployeeID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    } else {
                        link = getLink(item.getEmployeeCode(), "singlePayrun|viewPayslip/" + item.getObjectID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    }
                    return link;
                } else {
                    return item.getEmployee() != null ? getLink(item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : "", null, item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(), item.getEmployee()) : getLink(wfmStrings.notAvailable(), null);
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(60);
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, SimpleLink>(wfmStrings.employee(), SinglePayrunItem.EMPLOYEE, 120) {
            @Override
            public SimpleLink getCellValue(SinglePayrunItem item) {
                boolean isBeforeLockDate = (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate().after(item.getToDate().getNonConvertedDate()));
                if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_VIEW) || Utils.isHRMS()) {
                    SimpleLink link = null;
                    if (!isBeforeLockDate && PAYRUN_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT)) {
                        link = getLink(item.getEmployee(), "singlePayrun|edit/" + item.getObjectID() + "/" + item.getEmployeeID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    } else {
                        link = getLink(item.getEmployee(), "singlePayrun|viewPayslip/" + item.getObjectID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    }
                    return link;
                } else {
                    return item.getEmployee() != null ? getLink(item.getEmployee(), null, item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(), item.getEmployee()) : getLink(wfmStrings.notAvailable(), null);
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, SimpleLink>(wfmStrings.period(), SinglePayrunItem.PERIOD, 100) {
            @Override
            public SimpleLink getCellValue(SinglePayrunItem item) {
                StringBuilder period = new StringBuilder();
                String month = item.getMonthID() != null && monthItems.get(item.getMonthID()) != null ? monthItems.get(item.getMonthID()) : item.getMonth();
                period.append(month != null ? month : "")
                        .append(period.length() > 0 && item.getYear() != null ? "," : "")
                        .append(item.getYear() != null ? item.getYear() : "");
                if (period.length() > 0) {
                    boolean isBeforeLockDate = (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate().after(item.getToDate().getNonConvertedDate()));
                    SimpleLink link = null;
                    if (!isBeforeLockDate && PAYRUN_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT)) {
                        link = getLink(period.toString(), "singlePayrun|edit/" + item.getObjectID() + "/" + item.getEmployeeID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    } else {
                        link = getLink(period.toString(), "singlePayrun|viewPayslip/" + item.getObjectID(),
                                item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() : item.getEmployee(),
                                item.getEmployee());
                    }
                    return link;
                } else {
                    return getLink(wfmStrings.notAvailable(), null);
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.total(), SinglePayrunItem.TOTAL, 100) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return PayrollClientUtils.format(item.getTotal());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.status(), SinglePayrunItem.STATUS, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return item.getStatus() != null ? item.getStatus() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(80);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.approver(), SinglePayrunItem.APPROVER, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return item.getApprover() != null ? item.getApprover().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(80);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.createdBy(), SinglePayrunItem.PREPARER, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(80);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(false);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.currency(), SinglePayrunItem.CURRENCY, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return item.getCurrency() != null && item.getCurrency().getName() != null ? item.getCurrency().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(false);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.processDate(), SinglePayrunItem.PROCESS_DATE, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return DateUtils.shortDateTimeFormat.format(item.getProcessDate() != null ? item.getProcessDate().getNonConvertedDate() : item.getToDate().getNonConvertedDate());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(false);

        columnConfig[++index] = new ColumnDefinitionConfig<SinglePayrunItem, String>(wfmStrings.paymentMethod(), SinglePayrunItem.PAYMENT_METHOD, 120) {
            @Override
            public String getCellValue(SinglePayrunItem item) {
                return item.getPayMethodName() != null ? item.getPayMethodName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(false);

        return columnConfig;
    }

    private void hasPayments(Boolean hasPaymentsItems) {
        hasPayments = hasPaymentsItems;
    }

    private void generatePDF(SinglePayrunItem item, Integer templateId) {
        PayslipTableRequestObject requestObject = new PayslipTableRequestObject(item.getObjectID());
        requestObject.setEmployeeName(item.getEmployee());
        requestObject.setEmployeeCode(item.getEmployeeCode());
        requestObject.setMonth(item.getMonth());
        requestObject.setYear(item.getYear());
        requestObject.setPdfTemplateID(templateId);
        String pdfURL = CommandConstants.PDF_URL + "/singlePayrunPdfHandler";
        Utils.sendPDFOrExcelRequest(horizontalPanel, pdfURL, requestObject.getRequestParams(), "_blank");
    }


    private ListingRequestProvider<SinglePayrunItem> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            if (employeeID != null) {
                filterParametrs.setEmployeeId(employeeID);
            }
            if (filterParametrs.getFacetFilter() != null) {
                filterParametrs.setStartDate(filterParametrs.getFacetFilter().getStartDate());
                filterParametrs.setEndDate(filterParametrs.getFacetFilter().getEndDate());
            }
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);

            PayrollService.App.get().getSinglePayrunList(filterParametrs, new AbstractAsyncCallback<ListResult<SinglePayrunItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<SinglePayrunItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "payroll welcome";
    }

    @Override
    public String getPropertyCode() {
        return SINGLE_PAYRUN_LIST;
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
    public void initStatistics(Integer parentId, Span container) {
        loadDependents(new ListingFilterParameter(), null, container);
    }

    private void loadDependents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setEmployeeId(employeeID);
        PayrollService.App.get().getSinglePayrunList(fp, new AbstractAsyncCallback<ListResult<SinglePayrunItem>>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(ListResult<SinglePayrunItem> result) {
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
