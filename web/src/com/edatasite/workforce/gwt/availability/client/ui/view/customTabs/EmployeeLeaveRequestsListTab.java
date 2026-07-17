package com.edatasite.workforce.gwt.availability.client.ui.view.customTabs;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Java6
 * Date: 30.11.11
 * Time: 14:37
 */
public class EmployeeLeaveRequestsListTab extends Composite {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<LeaveRequestLisItem> dataGrid;
    private ListDataProvider<LeaveRequestLisItem> dataProvider;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private EmployeeLeaveRequestChart chart; //Need svg
    private Integer selectedYear;
    private boolean currentUser;
    private final Integer employeeID;
    private final Integer requestID;
    private final MaterialPanel panel = new MaterialPanel("pg_leave__footer");

    public static final ProvidesKey<LeaveRequestLisItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    public EmployeeLeaveRequestsListTab(Integer employeeID, Integer requestID, Integer selectedYear) {
        this.employeeID = employeeID;
        this.requestID = requestID;
        if (selectedYear != null) {
            this.selectedYear = selectedYear;
        }
        initData();
    }

    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

        dataProvider.addDataDisplay(dataGrid);

        viewShow();

        Div div = new Div("pg_leave__latest-table margin-bottom");
        div.add(dataGrid);
        panel.add(div);
        initWidget(panel);
    }

    public void setChart(EmployeeLeaveRequestChart chart) {
        this.chart = chart;
    }

    public void viewShow() {

        draw();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_ADD, EmployeeLeaveRequestsListTab.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, EmployeeLeaveRequestsListTab.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, EmployeeLeaveRequestsListTab.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, EmployeeLeaveRequestsListTab.this, (sender, args) -> draw());
    }

    public void draw() {
        if (dataGrid.getColumnCount() < 1) {
            initTableColumns();
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeID);
        fp.setYear(selectedYear);
        fp.setObjectId(requestID);
        fp.setSortField(LeaveRequestLisItem.FROM_DATE);
        fp.setAscending(false);
        AvailabilityService.App.get().getLeaveRequestList(fp, new AbstractAsyncCallback<ListResult<LeaveRequestLisItem>>() {
            public void success(ListResult<LeaveRequestLisItem> items) {
                dataProvider.getList().clear();
                if (items != null && items.getList().size() > 0) {
                    supplyProvider(items.getList().toArray(new LeaveRequestLisItem[]{}));
                    currentUser = Utils.getUserID().equals(employeeID);
                } else {
                    DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.thereAreNoAnyLeaveRequestsYet());
                    message.setTextBeforeLink(hrmsStrings.youCanStartRegisteringYourLeaveRequests() + " ");
                    message.setHref(clickEvent -> {
                        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add/" + employeeID);
                        } else {
                            Info.show("You don't have enough permissions to add", Info.Type.WARNING);
                        }
                    });
                    dataGrid.setEmptyTableWidget(message.getDivPanelMessage());
                }
            }
        });
    }

    private void supplyProvider(LeaveRequestLisItem[] leaveRequests) {
        List<LeaveRequestLisItem> leaveRequestList = dataProvider.getList();
        leaveRequestList.clear();
        Collections.addAll(leaveRequestList, leaveRequests);
    }

    private void initTableColumns() {
        Column<LeaveRequestLisItem, String> link = new Column<LeaveRequestLisItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final LeaveRequestLisItem object) {
                return object.getReason();
            }
        };
        link.setFieldUpdater((index, object, value) -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + object.getObjectId(), object.getEmployeeName(), object.getEmployeeName());
        });
        dataGrid.addColumn(link, wfmStrings.reason());
        dataGrid.setColumnWidth(link, 6, Style.Unit.PCT);
        //Description
        Column<LeaveRequestLisItem, String> description = new Column<LeaveRequestLisItem, String>(new TextCell()) {
            @Override
            public String getValue(final LeaveRequestLisItem object) {
                return object.getDescription();
            }

            @Override
            public void render(Cell.Context context, LeaveRequestLisItem object, SafeHtmlBuilder sb) {
                String desc = object.getDescription() != null ? (object.getDescription().length() > 60 ? object.getDescription().substring(0, 60) + "..." : object.getDescription()) : "";
                sb.appendHtmlConstant("<div title=\"" + object.getDescription() + "\">" + desc + "</div>");

            }
        };
        dataGrid.addColumn(description, wfmStrings.description());
        dataGrid.setColumnWidth(description, 6, Style.Unit.PCT);

        //From
        Column<LeaveRequestLisItem, String> from = new Column<LeaveRequestLisItem, String>(new TextCell()) {

            @Override
            public String getValue(final LeaveRequestLisItem item) {
                if (item.getStartDate() != null && item.getEndDate() != null) {
                    Date from = item.getStartDate().getNonConvertedDate();
                    Date to = item.getEndDate().getNonConvertedDate();
                    return DateUtils.formatFromTo(from, to, item.isAllDay(), DateUtil.isSameDay(from, to));
                }
                return "";
            }
        };
        dataGrid.addColumn(from, wfmStrings.from() + " / " + wfmStrings.to());
        dataGrid.setColumnWidth(from, 10, Style.Unit.PCT);

        //Paid
        Column<LeaveRequestLisItem, String> paid = new Column<LeaveRequestLisItem, String>(new TextCell()) {

            @Override
            public String getValue(final LeaveRequestLisItem object) {
                return object.getLeaveDays();
            }
        };
        dataGrid.addColumn(paid, wfmStrings.days());
        dataGrid.setColumnWidth(paid, 5, Style.Unit.PCT);

        //NonPaid
        Column<LeaveRequestLisItem, String> nonPaid = new Column<LeaveRequestLisItem, String>(new TextCell()) {

            @Override
            public String getValue(LeaveRequestLisItem object) {
                return object.getType();
            }
        };
        dataGrid.addColumn(nonPaid, wfmStrings.type());
        dataGrid.setColumnWidth(nonPaid, 8, Style.Unit.PCT);

        Column<LeaveRequestLisItem, String> manager = new Column<LeaveRequestLisItem, String>(new TextCell()) {

            @Override
            public String getValue(final LeaveRequestLisItem object) {
                return object.getApproverName();
            }
        };
        dataGrid.addColumn(manager, wfmStrings.approver());
        dataGrid.setColumnWidth(manager, 6, Style.Unit.PCT);

        Column<LeaveRequestLisItem, String> status = new Column<LeaveRequestLisItem, String>(new TextCell()) {

            @Override
            public String getValue(final LeaveRequestLisItem object) {
                return object.getStatus();
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 3.5, Style.Unit.PCT);
        //Action
        if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST)) {
            IconCell removeCell = new IconCell("ficon--trash pointer");
            Column<LeaveRequestLisItem, String> removeAction = new Column<LeaveRequestLisItem, String>(removeCell) {
                @Override
                public String getValue(LeaveRequestLisItem object) {
                    removeCell.setClickHandler(clickEvent -> {
                        if (Utils.hasRole(Utils.ADMIN) || Utils.hasRole(Utils.DR) || Utils.hasRole(Utils.HR)) {
                            deleteAction(object.getObjectId());
                        } else if (currentUser && object.isPending()) {
                            deleteAction(object.getObjectId());
                        } else {
                            Info.warn(wfmStrings.youDontHavePermission());
                        }
                    });
                    return null;
                }
            };
            removeAction.setCellStyleNames("removeImage");
            dataGrid.addColumn(removeAction, wfmStrings.action());
            dataGrid.setColumnWidth(removeAction, 3.5, Style.Unit.PCT);
        }

        //PDF
        IconCell iconCell = new IconCell("ficon--file-pdf pointer");
        Column<LeaveRequestLisItem, String> pdf = new Column<LeaveRequestLisItem, String>(iconCell) {
            @Override
            public String getValue(LeaveRequestLisItem object) {
                iconCell.setClickHandler(clickEvent -> new PDFTemplateSelector(AccountingConstants.LEAVE_REQUEST, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        LeaveRequestObject requestObject = new LeaveRequestObject(object.getObjectId(), object.getEmployeeId(), id);
                        String pdfUrl = CommandConstants.PDF_URL + "/employeeLeaveRequestViewPDFHandler";
                        HashMap<String, String> requestParams = requestObject.getRequestParams();
                        String svg = chart.getSVG();
                        if (chart != null && svg != null) {
                            requestParams.put("svg", svg);
                        }
                        Utils.sendPDFOrExcelRequest(panel, pdfUrl, requestParams, "_blank");
                    }
                }));
                return null;
            }
        };
        dataGrid.addColumn(pdf, wfmStrings.pdf());
        dataGrid.setColumnWidth(pdf, 3, Style.Unit.PCT);
        dataGrid.setStylePrimaryName("pdfHeader");
    }

    private void deleteAction(Integer objectID) {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(hrmsStrings.areYouSureYouWanttoDeleteThisLeaveRequest());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                AvailabilityService.App.get().deleteRequest(objectID, new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, null, EmployeeLeaveRequestsListTab.this);
                        Info.show((Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.leaveRequest())), Info.Type.INFO);
                    }
                });
            }
        });
        message.open();
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }
}