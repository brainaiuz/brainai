package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Hurshid on 3/11/2019
 */
public class AnnualBalanceReport extends BaseListView {
    private final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<LeaveBalanceReport> listingTable;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private final int currentYear = Integer.parseInt(dateFormat.format(new Date()));
    private DataListBox yearListBox;
    private HTML yearHtml;
    private boolean employmentInfPermission;

    public AnnualBalanceReport() {
        super("leaveBalanceReport");
        setDescription(property.getPlural(hrmsStrings.annualLeaveBalance()));
    }

    @Override
    public String getIconStyle() {
        return "availability av-welcome";
    }

    @Override
    protected Widget onInitialize() {
        employmentInfPermission = Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYMENT_INFORMATION);

        listingTable = new ListingPanel<>(ListPanelType.LeaveBalanceReport, getColumns(), getListData(), getDesign());
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/downloadAnnualBalanceReport";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListPDF(pdfURL, fp);
        });

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadAnnualBalanceReportExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListExcel(excelURL, fp);
        });

        listingTable.setOnReset(() -> yearListBox.setSelected(currentYear));

        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        //Employee name
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.number(), LeaveBalanceReport.EMPLOYEE_NUMBER, 150) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getEmployeeNumber();
            }
        };
        columnConfig.setMinimumColumnWidth(120);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //Employee name
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, SimpleLink>(wfmStrings.employee(), LeaveBalanceReport.EMPLOYEE_NAME, 200) {
            @Override
            public SimpleLink getCellValue(LeaveBalanceReport item) {
                return getLink(item.getEmployeeName() != null ? item.getEmployeeName() : wfmStrings.notAvailable(), "employeeProfile|hrmsleaveRequests/" + item.getEmployeeID() + "/" + yearListBox.getSelectedItem().getId());
            }
        };
        columnConfig.setMinimumColumnWidth(150);
        columnConfig.setColumnSortable(true);
        columns.add(columnConfig);

        //Leave allowance (days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.leaveAllowanceDays(), LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getLeaveAllowanceDays());
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);


        //Taken(days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.takenDays(), LeaveBalanceReport.TAKEN_DAYS, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getTakenDays());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //Current Balance
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.balance(), LeaveBalanceReport.CURRENT_BALANCE, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getCurrentBalance());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //department
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), LeaveBalanceReport.DEPARTMENT, 150) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getDepartment();
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Account status
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.status(), LeaveBalanceReport.STATUS, 120) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getStatus();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        //Hire Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.hireDate(), LeaveBalanceReport.HIRE_DATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getHireDate() != null ? DateUtils.format1(item.getHireDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Resign Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.resignationDate(), LeaveBalanceReport.RESIGN_DATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getResignDate() != null ? DateUtils.format1(item.getResignDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Opening balance days
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.openingBalanceDays(), LeaveBalanceReport.OPENING_BALANCE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return String.valueOf(item.getOpeningBalance() != null ? item.getOpeningBalance() : 0.0);
            }

            @Override
            public void setCellValue(LeaveBalanceReport rowValue, String cellValue) {
                rowValue.setOpeningBalance(Double.valueOf(cellValue));
                saveCellValue(rowValue);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getDesign() {
//Year filter start
        yearHtml = new HTML(wfmStrings.year());
        yearListBox = new DataListBox();
        yearListBox.setWidth("100px");
        yearListBox.setWithoutNullLabel(true);

        SelectItem[] items = new SelectItem[3];
        int j = 0;
        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            SelectItem year = new SelectItem();
            year.setId(i);
            year.setName(String.valueOf(i));
            items[j] = year;
            j++;
        }
        yearListBox.setItems(items);
        yearListBox.setSelected(currentYear);

        yearListBox.addValueChangeHandler(event -> listingTable.reloadPage());
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getEmployeeAnnualBalanceFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
                listingTable.getPdfVersion().setVisible(false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.thereAreNoResultOnSelectedDate());

                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("75%");

                topPanel.add(yearHtml);
                topPanel.setCellVerticalAlignment(yearHtml, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(yearListBox);
                topPanel.setCellVerticalAlignment(yearListBox, HasVerticalAlignment.ALIGN_MIDDLE);
                return topPanel;
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        if (employmentInfPermission) {
            contentConfigure.addContentConfigure("department", Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure("position", wfmStrings.position(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_POSITION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_POSITION_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure("location", Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_LOCATION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_LOCATION_ID_NAME;
                }
            });
        }

        contentConfigure.addContentConfigure("status", wfmStrings.accountStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
        contentConfigure.setDatePeriodPanelEnabled(false);
        return contentConfigure;
    }

    private ListingRequestProvider<LeaveBalanceReport> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setYear(yearListBox.getSelectedItem().getId());
            availabilityService.getEmployeeLeaveBalanceReport(filterParameter, new AbstractAsyncCallback<ListResult<LeaveBalanceReport>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<LeaveBalanceReport> reportList) {
                    callback.onSuccess(reportList);
                }
            });
        };
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
        return "annualLeaveBalance";
    }
}
