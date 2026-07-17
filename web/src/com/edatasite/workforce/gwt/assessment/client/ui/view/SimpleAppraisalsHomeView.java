//package com.edatasite.workforce.gwt.assessment.client.ui.view;
//
//import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentListData;
//import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
//import com.edatasite.workforce.gwt.assessment.client.ui.view.customTabs.WaitingForSomeoneSimpleReviewTab;
//import com.edatasite.workforce.gwt.core.client.Utils;
//import com.edatasite.workforce.gwt.core.client.View;
//import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
//import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
//import com.edatasite.workforce.gwt.core.client.ui.Constants;
//import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
//import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
//import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
//import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
//import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.core.client.RunAsyncCallback;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.ui.Widget;
//import gwt.material.design.client.ui.html.Div;
//import gwt.material.design.client.ui.html.Icon;
//import gwt.material.design.client.ui.html.Span;
//
//public class SimpleAppraisalsHomeView extends View implements Constants {
//
//    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
//    private WaitingForSomeoneSimpleReviewTab yourPendingReviewTab;
//    private WaitingForSomeoneSimpleReviewTab allAssessmentTab;
//
//    public SimpleAppraisalsHomeView() {
//        super(PA_HOME_VIEW);
//        setDescription(property.getPlural(hrmsStrings.simpleAppraisals()));
//        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
//            setAddNew("initiate|add/");
//        }
//    }
//
//    @Override
//    public String getIconStyle() {
//        return "assessment simple-app-home";
//    }
//
//    @Override
//    protected Widget onInitialize() {
//        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSASSMET_DELETED, SimpleAppraisalsHomeView.this, (sender, args) -> this.refreshTables());
//        draw();
//        return null;
//    }
//
//    private void draw() {
//        HorizontalPanel pdfPanel = new HorizontalPanel();
//        pdfPanel.getElement().getStyle().setVisibility(com.google.gwt.dom.client.Style.Visibility.HIDDEN);
//
//        allAssessmentTab = new WaitingForSomeoneSimpleReviewTab(pdfPanel, hrmsStrings.noReviewsInitiated(), false, false);
//        yourPendingReviewTab = new WaitingForSomeoneSimpleReviewTab(pdfPanel, hrmsStrings.noFillingOutAppraisals(), true, true);
//
//        VerticalPanel generalTable = new VerticalPanel();
//        Div firstPanel = new Div("listing-table-container");
//        Div headerToolBarPanel = new Div("margin-top margin-bottom operPanel");
//        Div headerActionPanel = new Div("operPanel__actions");
//        headerActionPanel.add(new Span(hrmsStrings.waitingForYouReview()));
//        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
//            Span refreshBtn = new Span();
//            refreshBtn.setStyleName("btn btn--icon btn--white");
//            refreshBtn.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("initiate|add/"));
//            Icon plusIcon = new Icon();
//            plusIcon.addStyleName("ficon--plus");
//            refreshBtn.add(plusIcon);
//            headerActionPanel.add(refreshBtn);
//        }
//        headerToolBarPanel.add(headerActionPanel);
//        firstPanel.add(headerToolBarPanel);
//        firstPanel.add(allAssessmentTab);
//        generalTable.add(firstPanel);
//
//        Div secondPanel = new Div("listing-table-container");
//        Div secondHeaderToolBarPanel = new Div("margin-top margin-bottom operPanel");
//        Div secondHeaderActionPanel = new Div("operPanel__actions");
//        secondHeaderActionPanel.add(new Span(hrmsStrings.pendingAppraisals()));
//        Span secondRefreshBtn = new Span();
//        secondRefreshBtn.setStyleName("btn btn--icon btn--white");
//        secondRefreshBtn.addClickHandler(event -> refreshTables());
//        Icon repeatIcon = new Icon();
//        repeatIcon.addStyleName("ficon--repeat");
//        secondRefreshBtn.add(repeatIcon);
//        secondHeaderActionPanel.add(secondRefreshBtn);
//        secondHeaderToolBarPanel.add(secondHeaderActionPanel);
//        secondPanel.add(secondHeaderToolBarPanel);
//        secondPanel.add(yourPendingReviewTab);
//
//        generalTable.add(secondPanel);
//        generalTable.add(pdfPanel);
//
//        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIMPLE_APPRAISAL_INITIATED, SimpleAppraisalsHomeView.this, (sender, args) -> refreshTables());
//        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPRAISAL_DELETE, SimpleAppraisalsHomeView.this, (sender, args) -> refreshTables());
//        refreshTables();
//        add(generalTable);
//    }
//
//    private void distributeDataBetweenTables(AssessmentListData items) {
//        allAssessmentTab.draw(items.getAssessments());
//        yourPendingReviewTab.draw(items.getYourPendingReviewAssessments());
//    }
//
//    private void refreshTables() {
//        LoadingPanel.loading(true);
//        AssessmentService.App.get().getAssessments(new AbstractAsyncCallback<AssessmentListData>() {
//            public void success(AssessmentListData assessmentItems) {
//                LoadingPanel.loading(false);
//                distributeDataBetweenTables(assessmentItems);
//            }
//        });
//    }
//
//    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
//        GWT.runAsync(new RunAsyncCallback() {
//
//            public void onFailure(Throwable caught) {
//                callback.onFailure(caught);
//            }
//
//            public void onSuccess() {
//                callback.onSuccess(onInitialize());
//            }
//        });
//    }
//
//    public String getPropertyCode() {
//        return "simpleAppraisal";
//    }
//}
