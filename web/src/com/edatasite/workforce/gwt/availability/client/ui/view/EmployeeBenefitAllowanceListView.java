package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Djuraev on 8/3/15.
 */
public class EmployeeBenefitAllowanceListView extends BaseListView implements Constants {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private DataListBox yearListBox;
    private DataListBox departmentListBox;
    private HTML yearHtml;
    private HTML departmentHtml;
    private ListingPanel list;
    private final Integer lastSavedEmployeeID = 0;
    private final Integer lastSavedDays = 0;
    private Integer selectedYear = Integer.valueOf(DateUtils.getYear(new Date()));

    public EmployeeBenefitAllowanceListView() {
        super(EMPLOYEE_BENEFIT_ALLOWANCE_LIST_VIEW, hrmsStrings.benefitAllowance());
    }

    @Override
    public String getIconStyle() {
        return "cert certificate-icon";
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_BENEFIT_ALLOWANCE, EmployeeBenefitAllowanceListView.this, (sender, args) -> list.reloadPage());

        list = new ListingPanel<>(ListPanelType.AnnualAllowanceListPanel, getDrawColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getDrawColumns() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        //Employee name
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<AnnualLeaveItem, SimpleLink>(wfmStrings.employee(), AnnualLeaveItem.EMPLOYEE_NAME, 150) {
            @Override
            public SimpleLink getCellValue(AnnualLeaveItem rowValue) {
                return getLink(Optional.ofNullable(rowValue.getEmployeeName()).orElse(""),
                        "employeeBenefit|employeeBenefitView/" + rowValue.getEmployeeId() + "/" + selectedYear, rowValue.getEmployeeName());
            }
        };
        columnConfigs.add(column);

        //Department name
        column = new ColumnDefinitionConfig<AnnualLeaveItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department(), wfmStrings.department()), AnnualLeaveItem.DEPARTMENT_NAME, 150) {
            @Override
            public String getCellValue(AnnualLeaveItem rowValue) {
                return rowValue.getDepartmentName();
            }
        };
        column.setColumnSortable(false);
        columnConfigs.add(column);
        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getDesigner() {
        //Year filter start
        yearHtml = new HTML("<b>" + wfmStrings.year() + ":</b>");
        Integer currentYear = Integer.valueOf(DateUtils.getYear(new Date()));
        yearListBox = new DataListBox();
        yearListBox.setWidth("85px");
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

        yearListBox.addValueChangeHandler(event -> {
            selectedYear =  Integer.valueOf(yearListBox.getSelectedItem().getName());
            list.reloadPage();
        });

        departmentHtml = new HTML("<b>" + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()) + ":</b>");
        departmentListBox = new DataListBox();
        departmentListBox.setWidth("180px");
        departmentListBox.setAllowFirstItem(true);
        //
        CommonService.App.get().getTeamList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] items) {
                departmentListBox.setItems(items);
            }
        });
        departmentListBox.addValueChangeHandler(sender -> list.reloadPage());

        return new ListingPanelDesign() {
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
                };
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("75%");

                topPanel.add(yearHtml);
                topPanel.setCellVerticalAlignment(yearHtml, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(yearListBox);
                topPanel.setCellVerticalAlignment(yearListBox, HasVerticalAlignment.ALIGN_MIDDLE);

                topPanel.add(departmentHtml);
                topPanel.setCellVerticalAlignment(departmentHtml, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(departmentListBox);
                departmentListBox.getElement().getStyle().setMarginLeft(-7, Style.Unit.PX);
                topPanel.setCellVerticalAlignment(departmentListBox, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(new HTML("&nbsp;&nbsp;"));

                return topPanel;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noEmployeeMessage());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<AnnualLeaveItem> getProvider() {
        return (filterParametrs, listingCallback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setYear(yearListBox.getSelectedItem().getId());
            filterParametrs.setDepartmentId(departmentListBox.getSelectedItem() != null ? departmentListBox.getSelectedItem().getId() : null);
            filterParametrs.setEmailIncluded(false);
            filterParametrs.setResignedEmployeesIncluded(false);
            filterParametrs.setActive(true);
            filterParametrs.setPlannedDue(true);
            AvailabilityService.App.get().getEmployeeBenefitList(filterParametrs, new AbstractAsyncCallback<ListResult<AnnualLeaveItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<AnnualLeaveItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private void save(BenefitItem benefitItem, String value, Integer empID) {//employeeID is used as rowID
        LoadingPanel.loading(true);
        Double allowance = 0.0;
        if (!value.equals("")) {
            allowance = Double.valueOf(value);
        }
        AnnualLeaveItem item = new AnnualLeaveItem();
        item.setAnnualallowancedays(allowance);
        item.setObjectID(benefitItem.getObjectId());
        item.setEmployeeId(empID);
        item.setAllowanceYear(yearListBox.getSelectedItem().getId());
        AvailabilityService.App.get().updateEmpBenefitAllowance(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
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
}
