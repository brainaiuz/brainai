package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: User
 * Date: 8/30/12
 * Time: 3:59 PM
 */
public class AddEditOnboardingCheckView extends View implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final HashMap<DataListBox, Integer> checkBoxArrayList = new HashMap<>();
    private final HashMap<String, Integer> stepIds = new HashMap<>();
    private final HashMap<String, Integer> stepEmployeeIds = new HashMap<>();
    private List<SelectItem> stepStatuses;
    private Integer int_userID;
    private FlowPanel mainForm;

    private final String STATUS_DONE = "STATUS_DONE";                   //on boarding check list status completed (status done)
    private final String STATUS_NOT_YET_DONE = "STATUS_NOT_YET_DONE";   //on boarding check list status not completed (status not yet done)

    public AddEditOnboardingCheckView() {
        super("onboardingCheck", hrmsStrings.myOnboardingChecklist());
    }

    public AddEditOnboardingCheckView(Integer userID) {
        super("onboardingCheck", wfmStrings.onboarding());
        if (userID != null) {
            this.int_userID = userID;
        }
    }

    @Override
    public String getIconStyle() {
        return "onboardingPeriod onboardingPeriod-list";
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void getData() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getOnboardingData(int_userID, new AbstractAsyncCallback<LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>>>() {
            @Override
            public void failure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>> object) {
                fillTable(object);
                LoadingPanel.loading(false);
                add(mainForm);
            }
        });
    }

    private void initialize() {
        mainForm = new FlowPanel();
        mainForm.setStyleName("section-box box-bg--1");

        getData();
    }

    private void fillTable(LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>> item) {
        if (item.keySet() == null || item.keySet().size() == 0) {
//            DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.cerrentlyPeriods());
//            message.setTextBeforeLink(wfmStrings.addPeriodsByClicking());
//            message.setHref(ONBOARDING_PERIOD + "|add/add");
//            mainForm.add(message.getWholeMessage());
        }
        int itemSize = 0;
        for (OnboardingItem period : item.keySet()) {

            ArrayList<OnboardingItem> stepId = item.get(period);
            if (stepId != null && stepId.size() > 0) {
                itemSize++;
                Paragraph periodLabel = new Paragraph();
                if (period.getPeriodName() != null) {
                    Label periodName = new Label(hrmsStrings.periodName() + ": " + period.getPeriodName());
                    periodName.setStyleName("title");
                    periodLabel.add(periodName);
                } else {
                    Label periodName = new Label(hrmsStrings.periodName() + ": " + wfmStrings.notAvailable());
                    periodName.setStyleName("title");
                    periodLabel.add(periodName);
                }

                int i = 0;

                final FlexTable table = new FlexTable();
                table.setStyleName("table margin-bottom-lg");

                table.setWidget(i, 0, new Label(wfmStrings.onboardingStep()));
                table.getWidget(i, 0).setStyleName("otimoPeriodCol_1");
                table.setWidget(i, 1, new Label(wfmStrings.description()));
                table.getWidget(i, 1).setStyleName("otimoPeriodCol_2");
                table.setWidget(i, 2, new Label(wfmStrings.status()));
                table.getWidget(i, 2).addStyleName("otimoPeriodColStatus");
                table.setWidget(i, 3, new Label(wfmStrings.form()));
                table.getWidget(i, 3).setStyleName("otimoPeriodCol_4");
                table.getWidget(i, 3).setWidth("60px");
                table.setWidget(i, 4, new Label(wfmStrings.pdf()));
                table.getWidget(i, 4).setStyleName("otimoPeriodCol_4");
                table.getWidget(i, 4).setWidth("60px");
                table.getRowFormatter().setStyleName(i, "thead");
                i++;

                for (OnboardingItem step : item.get(period)) {
                    stepIds.put(step.getStepName(), step.getStepId());
                    final SimpleLink pdf = new SimpleLink(wfmStrings.pdfVersion());
                    pdf.setLayoutData(step.getAssignedEmployee() != null ? step.getAssignedEmployee().getObjectID() : null);
                    pdf.addClickHandler(clickEvent -> {
                        if (pdf.getLayoutData() != null) {
                            String pdfURL = CommandConstants.PDF_URL + "/employeeStepViewPDFHandler";
                            RequestObject requestObject = new RequestObject((Integer) pdf.getLayoutData());
                            HashMap<String, String> parametrs = requestObject.getRequestParams();
                            Utils.sendPDFOrExcelRequest(mainForm, pdfURL, parametrs, "_blank");
                        }
                    });

                    if (step.isCreateForm() && step.getAssignedEmployee() != null) {
                        stepEmployeeIds.put(step.getStepName(), step.getAssignedEmployee().getObjectID());

                        stepStatuses = new ArrayList<>();
                        if (step.getStatusItems() != null && step.getStatusItems().size() > 0) {
                            for (ReferenceItem referenceItem : step.getStatusItems()) {
                                stepStatuses.add(new SelectItem(referenceItem.getObjectID(), referenceItem.getName(), referenceItem.getCode()));
                            }
                        }

                        if (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT)) {
                            final DataListBox statusListBox = new DataListBox();
                            statusListBox.setWidth("140px");
                            statusListBox.ensureDebugId("add_edit_on_boarding_check_view_status_box");
                            statusListBox.setItems(stepStatuses.toArray(new SelectItem[]{}));
                            statusListBox.setSelected(step.getAssignedEmployee().getStatusID());
                            statusListBox.addValueChangeHandler(event -> {
                                //register status change logic
                                if (statusListBox.isSomethingSelected()) {
                                    OnboardingItem item12 = new OnboardingItem();
                                    EmployeeStepItem empItem = new EmployeeStepItem();
                                    empItem.setStatusID(statusListBox.getSelectedId());
                                    item12.setAssignedEmployee(empItem);
                                    item12.setEmployeeId(int_userID);
                                    item12.setStepId(checkBoxArrayList.get(statusListBox));
                                    item12.setStepStatus(true);//DONE status
                                    LoadingPanel.loading(true);
                                    HrmsService.App.get().saveOnboardingData(item12, new AsyncCallback<Void>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void onSuccess(Void result) {
                                            LoadingPanel.loading(false);
                                        }
                                    });
                                }
                            });

                            checkBoxArrayList.put(statusListBox, step.getStepId());
                            table.setWidget(i, 2, statusListBox);

                        } else {
                            HTML status = new HTML();
                            if (step.getAssignedEmployee() != null && step.getAssignedEmployee().getStatusName() != null) {
                                status.setHTML(step.getAssignedEmployee().getStatusName());
                            }
                            table.setWidget(i, 2, status);
                        }

                        SimpleLink formLink = new SimpleLink(hrmsStrings.entryForm());
                        formLink.addClickHandler(clickEvent -> {
                            int index = table.getCellForEvent(clickEvent).getRowIndex();
                            Label stepName = (Label) ((VerticalPanel) table.getWidget(index, 0)).getWidget(0);
                            Label stepFormID = (Label) ((VerticalPanel) table.getWidget(index, 0)).getWidget(1);
                            SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + stepEmployeeIds.get(stepName.getText()) + "/" + stepIds.get(stepName.getText()) + "/" + stepFormID.getText() + "/" + stepName.getText());
                        });

                        VerticalPanel vp = new VerticalPanel();
                        Label htmlPanel = new Label(step.getStepName());
                        vp.add(htmlPanel);
                        Label htmlPanel1 = new Label(step.getFormID());
                        htmlPanel1.setVisible(false);
                        vp.add(htmlPanel1);

                        table.setWidget(i, 0, vp);
                        table.getWidget(i, 0).setStyleName("otimoPeriodCol_1");

                        if (step.getStepDescription() != null && !"".equals(step.getStepDescription())) {
                            table.setWidget(i, 1, new HTML(step.getStepDescription()));
                        } else {
                            table.setWidget(i, 1, new HTML(wfmStrings.notAvailable()));
                        }
                        table.getWidget(i, 1).setStyleName("otimoPeriodCol_2");

                        table.getWidget(i, 2).addStyleName("otimoPeriodColStatus");

                        if (step.isCreateForm()) {
                            table.setWidget(i, 3, formLink);
                            table.getWidget(i, 3).setStyleName("otimoPeriodCol_4");
                        } else {
                            table.setWidget(i, 3, new HTML(""));
                        }
                        table.setWidget(i, 4, pdf);
                        table.getWidget(i, 4).setStyleName("otimoPeriodCol_4");
                        i++;

                    } else if (!step.isCreateForm()) {

                        stepStatuses = new ArrayList<>();
                        if (step.getStatusItems() != null && step.getStatusItems().size() > 0) {
                            for (ReferenceItem referenceItem : step.getStatusItems()) {
                                stepStatuses.add(new SelectItem(referenceItem.getObjectID(), referenceItem.getName(), referenceItem.getCode()));
                            }
                        }

                        if (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT)) {
                            final DataListBox statusListBox = new DataListBox();
//                            statusListBox.setWidth("140px");
                            statusListBox.ensureDebugId("add_edit_on_boarding_check_view_status_box");
                            statusListBox.addStyleName("file--AddEditOnboardingCheckView");
                            if (stepStatuses.size() > 0) {
                                statusListBox.setItems(stepStatuses.toArray(new SelectItem[]{}));
                            }
                            if (step.getAssignedEmployee() != null && step.getAssignedEmployee().getStatusID() != null) {
                                statusListBox.setSelected(step.getAssignedEmployee().getStatusID());
                            }
                            statusListBox.addValueChangeHandler(event -> {
                                //register status change logic
                                if (statusListBox.isSomethingSelected()) {
                                    OnboardingItem item1 = new OnboardingItem();
                                    EmployeeStepItem empItem = new EmployeeStepItem();
                                    empItem.setStatusID(statusListBox.getSelectedId());
                                    item1.setAssignedEmployee(empItem);
                                    item1.setEmployeeId(int_userID);
                                    item1.setStepId(checkBoxArrayList.get(statusListBox));
                                    item1.setStepStatus(true);//DONE status
                                    LoadingPanel.loading(true);
                                    HrmsService.App.get().saveOnboardingData(item1, new AsyncCallback<Void>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void onSuccess(Void result) {
                                            LoadingPanel.loading(false);
                                        }
                                    });
                                }
                            });
                            if (stepStatuses.size() == 0) {
                                table.setWidget(i, 2, new HTML());
                            } else {
                                checkBoxArrayList.put(statusListBox, step.getStepId());
                                table.setWidget(i, 2, statusListBox);
                            }

                        } else {
                            HTML status = new HTML();
                            if (step.getAssignedEmployee() != null && step.getAssignedEmployee().getStatusName() != null) {
                                status.setHTML(step.getAssignedEmployee().getStatusName());
                            }
                            table.setWidget(i, 2, status);
                        }

                        SimpleLink formLink = new SimpleLink(hrmsStrings.viewStep());
                            formLink.addClickHandler(clickEvent -> {
                                int index = table.getCellForEvent(clickEvent).getRowIndex();
                                HTML stepName = (HTML) ((VerticalPanel) table.getWidget(index, 0)).getWidget(0);
                                HTML stepFormID = (HTML) ((VerticalPanel) table.getWidget(index, 0)).getWidget(1);
                                SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + stepIds.get(stepName.getHTML()) + "/" + stepFormID.getHTML());
                            });

                        VerticalPanel vp = new VerticalPanel();
                        HTML htmlPanel = new HTML(step.getStepName());
                        vp.add(htmlPanel);
                        HTML htmlPanel1 = new HTML(step.getFormID());
                        htmlPanel1.setVisible(false);
                        vp.add(htmlPanel1);

                        table.setWidget(i, 0, vp);
                        table.getWidget(i, 0).setStyleName("otimoPeriodCol_1");
                        if (step.getStepDescription() != null && !"".equals(step.getStepDescription())) {
                            table.setWidget(i, 1, new HTML(step.getStepDescription()));
                        } else {
                            table.setWidget(i, 1, new HTML("N/A"));
                        }
                        table.getWidget(i, 1).setStyleName("otimoPeriodCol_2");
                        table.getWidget(i, 2).addStyleName("otimoPeriodColStatus");
                        if (step.isCreateForm()) {
                            table.setWidget(i, 3, formLink);
                            table.getWidget(i, 3).setStyleName("otimoPeriodCol_4");
                        } else {
                            table.setWidget(i, 3, new HTML(""));
                        }
                        table.setWidget(i, 4, pdf);
                        table.getWidget(i, 4).setStyleName("otimoPeriodCol_4");
                        i++;

                    }
                    mainForm.add(periodLabel);
                    mainForm.add(table);
                }
            }
        }
        if (itemSize == 0) {
            DockPanel dockPanel1 = new DockPanel();
            dockPanel1.setSize("100%", "100%");
            dockPanel1.setHorizontalAlignment(dockPanel1.ALIGN_CENTER);
            dockPanel1.setVerticalAlignment(dockPanel1.ALIGN_MIDDLE);
            VerticalPanel message = new VerticalPanel();
            HTML messageNoOnboarding = new HTML(hrmsStrings.messageNoOnboarding());
            messageNoOnboarding.addStyleName("center");
            message.add(messageNoOnboarding);
            message.getElement().getStyle().setMarginTop(200, Style.Unit.PX);
            dockPanel1.add(message, DockPanel.CENTER);
            mainForm.add(dockPanel1);
        }

    }

    private void reInit() {
        int_userID = null;
        initialize();
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