package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BugListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.UploadFile;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.upload.UploadForm;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 2:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugListView extends BaseListView implements Constants {
    public static final BackendStrings backendStrings = BackendStrings.App.get();

    private ListingPanel list;
    int pageStart = 0;
    private Integer employeeId;
    private String statusDescription;
    private WfmDropdown section;
    private WfmDropdown reportedBy;
    private WfmDropdown priorityDropdown;
    private UploadForm uploadForm;
    private TextArea2 textArea2;
    private Timer timer;





    public BugListView() {
        super(BUG_LIST, backendStrings.bugList());
    }
    public BugListView(Integer employeeId, String statusDescription) {
        this();
        this.employeeId = employeeId;
        this.statusDescription = statusDescription;
    }

    public String getIconStyle() {
        return "backend bugListView";
    }

    public void refresh() {
        list.reloadPage();
    }

    public void refreshPage(int pageStart) {
        list.reloadPage();
    }


    private void saveBug(KpiModal dialogBox) {
        LoadingPanel.loading(true);
        dialogBox.close();
        BugReportItem bugReportItem = new BugReportItem();
        bugReportItem.setReportText(textArea2.getText());
        bugReportItem.setReportSection(section.getSelectedItem().getName());
        bugReportItem.setReportedBy(reportedBy.getSelectedItem().getId());
        bugReportItem.setPriorityID(priorityDropdown.getSelectedItem().getId());
        bugReportItem.setPriority(priorityDropdown.getSelectedItem().getDescription());
        bugReportItem.setUserAgent(Utils.getUserAgent());

        List att = new ArrayList();
        for (int i = 0; i < uploadForm.getUploadFiles().size(); i++) {
            UploadFile uploadFile = (UploadFile) uploadForm.getUploadFiles().get(i);
            if (uploadFile.getId() != null) {
                FileItem fileItem = new FileItem();
                fileItem.setId(uploadFile.getId());
                att.add(fileItem);
            }
        }
        FileItem[] fileItems = new FileItem[att.size()];
        for (int i = 0; i < att.size(); i++) {
            fileItems[i] = (FileItem) att.get(i);
        }
        bugReportItem.setAttachments(fileItems);
        BugReportServiceAsync sendReport = BugReportService.App.get();
        sendReport.sendBugReportNew(bugReportItem, new AbstractAsyncCallback<Void>() {
            public void success(Void result) {
                LoadingPanel.loading(false);
                list.reloadPage();
                Info.show(wfmStrings.theMessageHasBeenSent(), Info.Type.INFO);
            }

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.theMessageCouldNotBeSend(), Info.Type.WARNING);
            }
        });

    }

    protected Widget onInitialize() {
        list = new ListingPanel(ListPanelType.BugListPanel, getColumns(), getListProvider(), getListDesign());
        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadBugListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListExcel(excelURL, filterParametrs);
        });
        add(list);
        return null;
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> {
                    final KpiModal inputBox = new KpiModal();
                    inputBox.setWidth(520);
                    inputBox.setTitle(backendStrings.bugReport());
                    //inputBox.setMessage("Please provide your comments or the bug issue and send it to our support team. Our support team will contact you shortly.");
                    final BugListItem item = new BugListItem();

                    HorizontalPanel hr;
                    final MaterialDialogContent vp = inputBox.getContent();
                    vp.add(new Label(wfmStrings.attachment()));
                    vp.add(uploadForm = new UploadForm(false));
                    //hr.setSpacing(8);
                    HTML htmlSection = new HTML("<b class=customTitle>" + wfmStrings.section() + ":</b>");
                    HTML htmlReportedBy = new HTML("<b class=customTitle>" + wfmStrings.reportedBy() + ":</b>");
                    HTML priority = new HTML("<b class=customTitle>" + wfmStrings.priority() + ":</b>");

                    priorityDropdown = new WfmDropdown();
                    BackendService.App.get().getBugPriority(new AbstractAsyncCallback<SelectItem[]>() {
                        public void success(SelectItem[] items) {

                            priorityDropdown.addItems(items);
                            for (SelectItem item1 : items) {
                                if ((item1).getName().trim().equals("Medium")) {
                                    priorityDropdown.setSelected(item1.getId());
                                }
                            }
                        }
                    });
                    hr = new HorizontalPanel();
                    hr.setSpacing(8);
                    hr.add(priority);
                    hr.add(priorityDropdown);
                    vp.add(hr);

                    section = new WfmDropdown();

                    SelectItem[] createdFromPM = new SelectItem[9];
                    createdFromPM[0] = new SelectItem(0, "PM Welcome");
                    createdFromPM[1] = new SelectItem(1, "Task List");
                    createdFromPM[2] = new SelectItem(2, "Issue List");
                    createdFromPM[3] = new SelectItem(3, "Timesheet");
                    createdFromPM[4] = new SelectItem(4, "Timesheet Approval");
                    createdFromPM[5] = new SelectItem(5, "Project List");
                    createdFromPM[6] = new SelectItem(6, "Clients/Customers List");
                    createdFromPM[7] = new SelectItem(7, "Employee List");
                    createdFromPM[8] = new SelectItem(8, "Departments List");

                    SelectItem[] createdFromPA = new SelectItem[6];
                    createdFromPA[0] = new SelectItem(0, "PA Welcome page");
                    createdFromPA[1] = new SelectItem(1, "360 Review");
                    createdFromPA[2] = new SelectItem(2, "Simple Appraisals");
                    createdFromPA[3] = new SelectItem(3, "Appraisals Archive");
                    createdFromPA[4] = new SelectItem(4, "Templates");
                    createdFromPA[5] = new SelectItem(5, "Performance Note");

                    SelectItem[] createdFromAvail = new SelectItem[9];
                    createdFromAvail[0] = new SelectItem(0, "Attendance Welcome");
                    createdFromAvail[1] = new SelectItem(1, "Attendance home");
                    createdFromAvail[2] = new SelectItem(2, "My Attendance");
                    createdFromAvail[3] = new SelectItem(3, "My Leave Request");
                    createdFromAvail[4] = new SelectItem(4, "Timeslot");
                    createdFromAvail[5] = new SelectItem(5, "Public Holidays");
                    createdFromAvail[6] = new SelectItem(6, "Attendance Tracking");
                    createdFromAvail[7] = new SelectItem(7, "Approve Leave Requests");
                    createdFromAvail[8] = new SelectItem(8, "Attendance Settings");

                    SelectItem[] createdFromAccounting = new SelectItem[10];
                    createdFromAccounting[0] = new SelectItem(0, "Sale Quotes");
                    createdFromAccounting[1] = new SelectItem(1, "Sale Invoices");
                    createdFromAccounting[2] = new SelectItem(2, "Customers/Clients");
                    createdFromAccounting[3] = new SelectItem(3, "Purchase Orders");
                    createdFromAccounting[4] = new SelectItem(4, "Purchase Invoices");
                    createdFromAccounting[5] = new SelectItem(5, "Suppliers/Bills");
                    createdFromAccounting[6] = new SelectItem(6, "Expense Claims");
                    createdFromAccounting[7] = new SelectItem(7, "Bank Accounts");
                    createdFromAccounting[8] = new SelectItem(8, "Chart of Accounts");
                    createdFromAccounting[9] = new SelectItem(9, "Tax Rates");

                    SelectItem[] createdFromGoogle = new SelectItem[3];
                    createdFromGoogle[0] = new SelectItem(0, "Google Documents");
                    createdFromGoogle[1] = new SelectItem(1, "Google Talk");
                    createdFromGoogle[2] = new SelectItem(2, "Google Calendar");

                    SelectItem[] createdFromDashboard = new SelectItem[5];
                    createdFromDashboard[0] = new SelectItem(0, "Company overview dashboard");
                    createdFromDashboard[1] = new SelectItem(1, "Timesheet Report");
                    createdFromDashboard[2] = new SelectItem(2, "Staff availability report");
                    createdFromDashboard[3] = new SelectItem(3, "Staff In/Out Report");
                    createdFromDashboard[4] = new SelectItem(4, "Performance Appraisal Report");

                    SelectItem[] createdFromSettings = new SelectItem[6];
                    createdFromSettings[0] = new SelectItem(0, "Profile");
                    createdFromSettings[1] = new SelectItem(1, "Company Settings");
                    createdFromSettings[2] = new SelectItem(2, "Invoice Settings");
                    createdFromSettings[3] = new SelectItem(3, "Financial Settings");
                    createdFromSettings[4] = new SelectItem(4, "User Credentials");
                    createdFromSettings[5] = new SelectItem(5, "E-mail Notifications");

                    SelectItem[] createdFromMyAccount = new SelectItem[2];
                    createdFromMyAccount[0] = new SelectItem(0, "Current Subscription");
                    createdFromMyAccount[1] = new SelectItem(1, "Subscription History");

                    SelectItem[] createdFromBackend = new SelectItem[9];
                    createdFromBackend[0] = new SelectItem(0, "Backend View");
                    createdFromBackend[1] = new SelectItem(1, "User Back-end Home");
                    createdFromBackend[2] = new SelectItem(2, "Returning Users");
                    createdFromBackend[3] = new SelectItem(3, "Subscriptiontypes");
                    createdFromBackend[4] = new SelectItem(4, "Set Test Company");
                    createdFromBackend[5] = new SelectItem(5, "Summary by Employee");
                    createdFromBackend[6] = new SelectItem(6, "Summary by Section");
                    createdFromBackend[7] = new SelectItem(7, "Access Log");
                    createdFromBackend[8] = new SelectItem(8, "Bug List");

                    section.addItems("Project Management", createdFromPM);
                    section.addItems("Performance Appraisal", createdFromPA);
                    section.addItems("Availability", createdFromAvail);
                    section.addItems("Accounting", createdFromAccounting);
                    section.addItems("Google", createdFromGoogle);
                    section.addItems("Dashboard", createdFromDashboard);
                    section.addItems("Settings", createdFromSettings);
                    section.addItems("My Account", createdFromMyAccount);
                    section.addItems("Backend", createdFromBackend);

                    /*final BugListItem item = new BugListItem();*/

                    reportedBy = new WfmDropdown();
                    BackendService.App.get().getEmployees(new AbstractAsyncCallback<SelectItem[]>() {
                        public void success(SelectItem[] items) {

                            reportedBy.addItems(items);
                            if (item.getAssignee() != null && !("".equals(item.getAssignee()))) {
                                for (SelectItem item1 : items) {
                                    if (item1.getName().equals(item.getAssignee())) {
                                        reportedBy.setSelected(item1.getId());
                                    }
                                }
                            }
                        }
                    });
                    hr = new HorizontalPanel();
                    hr.setSpacing(8);
                    hr.add(htmlSection);
                    hr.add(section);
                    hr.add(htmlReportedBy);
                    hr.add(reportedBy);
                    vp.add(hr);
                    vp.add(textArea2 = new TextArea2());
                    textArea2.setWidth(500);
                    final HorizontalPanelDiv buttons = new HorizontalPanelDiv();
                    final Button ok = new Button(wfmStrings.ok(), (ClickHandler) event -> {
                        if ((priorityDropdown.getSelectedItem() == null || reportedBy.getSelectedItem() == null || section.getSelectedItem() == null) ||
                                ((priorityDropdown.getSelectedItem() != null && priorityDropdown.getSelectedItem().getId() == null) ||
                                        (reportedBy.getSelectedItem() != null && reportedBy.getSelectedItem().getId() == null) ||
                                        (section.getSelectedItem() != null && section.getSelectedItem().getId() == null))) {
                            Info.show(wfmStrings.messageBodyShouldNotBeBlank(), Info.Type.WARNING);
                        } else {
                            if (!uploadForm.isEmpty()) {
                                LoadingPanel.loading(true);
                            } else {
                                LoadingPanel.loading(true);
                            }
                            timer = new Timer() {
                                public void run() {
                                    if (uploadForm.isFinished()) {
                                        timer.cancel();
                                        saveBug(inputBox);
                                    }
                                }
                            };
                            timer.scheduleRepeating(1000);
                        }
                    });
                    final Button cancel = new Button(wfmStrings.cancel(), (ClickHandler) event -> inputBox.close());
                    buttons.add(10, ok, cancel);
                    vp.add(buttons);
                    inputBox.open();
                });
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoBugs());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[14];
        columns[0] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.id(), BugListItem.BUG_ID, 50) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getBugId();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[0].setMinimumColumnWidth(50);
        columns[0].setMaximumColumnWidth(50);

        columns[1] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.bugUpperCase(), BugListItem.BUG, 80) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getBug();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[1].setMinimumColumnWidth(50);

        columns[2] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.user(), BugListItem.USER, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getUser();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[2].setMinimumColumnWidth(50);

        columns[3] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.email(), BugListItem.EMAIL, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getEmail();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[3].setMinimumColumnWidth(50);

        columns[4] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.company(), BugListItem.COMPANY, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getCompany();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[4].setMinimumColumnWidth(50);

        columns[5] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.status(), BugListItem.STATUS, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getStatus();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[5].setMinimumColumnWidth(50);

        columns[6] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.priority(), BugListItem.PRIORITY, 50) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getPriority();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[6].setMinimumColumnWidth(30);

        columns[7] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.label(), BugListItem.LABEL, 50) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getLabel();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[7].setMinimumColumnWidth(30);

        columns[8] = new ColumnDefinitionConfig<BugListItem, String>(backendStrings.createdOn(), BugListItem.CREATION_TIME, 60) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getCreationTime();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[8].setMinimumColumnWidth(30);

        columns[9] = new ColumnDefinitionConfig<BugListItem, String>(backendStrings.createdFrom(), BugListItem.CREATED_FROM, 100) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getCreatedFrom();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[9].setMinimumColumnWidth(50);

        columns[10] = new ColumnDefinitionConfig<BugListItem, String>(backendStrings.lastUpdate(), BugListItem.UPDATE_TIME, 60) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getUpdateTime();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[10].setMinimumColumnWidth(30);

        columns[11] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.assignee(), BugListItem.ASSIGNEE, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return bugListItem.getAssignee();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[11].setMinimumColumnWidth(50);

        columns[12] = new ColumnDefinitionConfig<BugListItem, String>(wfmStrings.comment(), BugListItem.COMMENT, 90) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[12].setMinimumColumnWidth(50);

        columns[13] = new ColumnDefinitionConfig<BugListItem, String>(backendStrings.browser(), BugListItem.BROWSER, 60) {

            @Override
            public String getCellValue(BugListItem bugListItem) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[13].setMinimumColumnWidth(30);

        return columns;
    }

    private ListingRequestProvider<BugListItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            //Rahim: Faxriddin man merge qilganda xatoga o`xshab ko`rindi shunga if larni oldingi holatidan olib qo`shib qo`ydim qarab qo`yarsiz
            /*if (statusId != null && employeeId != null) {
                filterParametrs.setBugStatusId(statusId);
            }*/
            if (/*statusId != null && */employeeId != null && statusDescription != null && !"".equals(statusDescription)) {
                /*filterParametrs.setBugStatusId(statusId);*/
                filterParametrs.setStatusValues(statusDescription);
            }
            if (employeeId != null) {
                filterParametrs.setBugAssigneeId(employeeId);
            }
            BackendService.App.get().getBugLists(filterParametrs, new AsyncCallback<ListResult<BugListItem>>() {
                public void onFailure(Throwable caught) {
                    listingCallback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<BugListItem> backendListListResult) {
                    listingCallback.onSuccess(backendListListResult);
                }

            });

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
