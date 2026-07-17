package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.utils.DashboardUtils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.client.ui.html.*;

import java.util.List;

public class GettingStartedWidget extends DashboardBaseWidget implements SelectionHandler<GettingStartedWidget.GettingStartedStep> {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    private MaterialPanel progressNum;
    private MaterialPanel progressBar;
    protected MaterialPanel progressPanel;
    protected MaterialPanel stepsPanel;

    protected MaterialPanel bodyContent;
    protected MaterialPanel bodyInfo;
    protected MaterialPanel bodyActions;

    private WfmButton2 forwardButton, skipButton;

    private List<GettingStartedItem> items;
    private int currentStepIndex = 0;

    public GettingStartedWidget(List<GettingStartedItem> items) {
        this.items = items;
    }

    @Override
    public void setTitle(String title) {
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText(title);
        heading.setStyleName("gts__title");

        this.title.clear();
        this.title.add(heading);
        this.title.setStyleName("gts__title");
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.accountConfiguration());

        headerRow.removeFromParent();

        //header
        progressNum = new MaterialPanel("gts-progress__num");
        progressBar = new MaterialPanel("determinate");
        MaterialPanel progressBarDiv = new MaterialPanel("progress");
        progressBarDiv.add(progressBar);

        progressPanel = new MaterialPanel("gts-progress");
        progressPanel.add(progressNum);
        progressPanel.add(progressBarDiv);

        stepsPanel = new MaterialPanel("gts-steps controls-lg");

        Span tooltipWrapper = new Span();
        tooltipWrapper.setStyleName("dropdown-kit--arrow--below gts-trace__tooltip");
        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);
        String activation = "infoDropDown";
        iconLink.setActivates(activation);

        MaterialDropDown dropDown = new MaterialDropDown(activation);
        dropDown.addStyleName("dropdown-content dropdown-content-tooltip");
        dropDown.getElement().setInnerHTML("<div style='width: 200px;'>" + wfmStrings.gettingStartedHelpText() + "</div>");
        dropDown.setHover(true);

        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(dropDown);

        headerPanel.add(title);
        headerPanel.add(progressPanel);
        headerPanel.add(stepsPanel);
        headerPanel.add(tooltipWrapper);

        mainPanel.addStyleName("widget--gts");
        headerPanel.addStyleName("gts-trace");

        //body
        bodyContent = new MaterialPanel("widget-content gts__content");
        bodyInfo = new MaterialPanel("gts__info");
        bodyActions = new MaterialPanel("gts__actions");

        bodyContent.add(bodyInfo);
        bodyContent.add(bodyActions);

        skipButton = new WfmButton2(wfmStrings.skip(), WfmButton2.BTN_GREY, event -> nextStep());

        Div div = new Div("gwt-wrapper");
        div.add(bodyContent);

        contentPanel.add(div);

        getDataToFillFields();
    }

    private void getDataToFillFields() {
        for (GettingStartedItem item : items) {
            switch (item.getType()) {
                case DASHBOARD_GETTING_STARTED.COMPANY_SETUP:
                    item.setTitle(wfmStrings.companySetup());
                    item.setDescription(wfmStrings.companySetupDescription2());
                    break;
                case DASHBOARD_GETTING_STARTED.INVITE_USER:
                    item.setTitle(wfmStrings.inviteUsers());
                    item.setDescription(wfmStrings.inviteUserDescription());
                    break;
                case DASHBOARD_GETTING_STARTED.USER_PROFILE:
                    item.setTitle(wfmStrings.updateProfile());
                    item.setDescription(wfmStrings.updateProfileDescription());
                    break;
                case DASHBOARD_GETTING_STARTED.DATA_MIGRATION:
                    item.setTitle(wfmStrings.dataMigration());
                    item.setDescription(wfmStrings.dataMigrationDescription());
                    break;
                case DASHBOARD_GETTING_STARTED.CONFIGURE_EMAIL:
                    item.setTitle(wfmStrings.configureEmail());
                    item.setDescription(wfmMessages.configureEmailDescription(Utils.getProductName()));
                    break;
            }

            GettingStartedStep step = new GettingStartedStep(item);
            step.addSelectionHandler(this);

            stepsPanel.add(step);
        }

        resetStep();
        refreshProgressBar();
    }

    @Override
    protected void getData() {
    }

    public void nextStep() {
        if (currentStepIndex >= 0 && currentStepIndex < stepsPanel.getWidgetCount() - 1) {
            GettingStartedStep currentStep = (GettingStartedStep) stepsPanel.getWidget(currentStepIndex);
            currentStep.setActive(false);

            // next stage
            for (int i = currentStepIndex + 1; i < stepsPanel.getWidgetCount(); i++) {
                GettingStartedStep nextStep =(GettingStartedStep) stepsPanel.getWidget(i);
                if (nextStep.isEnable() && nextStep.isVisible()) {
                    nextStep.setActive(true);
                    setCurrentStepIndex(i);
                    return;
                }
            }
        }
        resetStep();
    }

    public void goToStep(GettingStartedStep step) {
        if (currentStepIndex < stepsPanel.getWidgetCount()) {
            GettingStartedStep currentStep = (GettingStartedStep) stepsPanel.getWidget(currentStepIndex);
            currentStep.setActive(false);
        }

        step.setActive(true);
        for (int i = 0; i < stepsPanel.getWidgetCount(); i++) {
            GettingStartedStep currentStep = (GettingStartedStep) stepsPanel.getWidget(i);
            if (step.equals(currentStep)) {
                setCurrentStepIndex(i);
                break;
            }
        }
    }

    public void resetStep() {
        for (int i = 0; i < stepsPanel.getWidgetCount(); i++) {
            GettingStartedStep step = (GettingStartedStep) stepsPanel.getWidget(i);
            step.setActive(false);
        }

        currentStepIndex = -1;
        for (int i = 0; i < stepsPanel.getWidgetCount(); i++) {
            GettingStartedStep currentStep = (GettingStartedStep) stepsPanel.getWidget(i);
            if (currentStep.isEnable()) {
                currentStep.setActive(true);
                setCurrentStepIndex(i);
                break;
            }
        }
        if (currentStepIndex < 0) {
            new MaterialAnimation().transition(Transition.FADEOUTUP).duration(500).animate(this);
            Timer timer = new Timer() {
                @Override
                public void run() {
                    getParent().removeFromParent();
                }
            };
            timer.schedule(300);
//            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_WIDGET_REMOVE, this, GettingStartedWidget.this);
        }
    }

    public void setCurrentStepIndex(int index) {
        if (this.currentStepIndex != index) {
            this.currentStepIndex = index;
            setStepBody(index);
        }
    }

    public void onSelection(SelectionEvent<GettingStartedStep> event) {
        if (event.getSelectedItem().isEnable()) {
            goToStep(event.getSelectedItem());
        }
    }

    private void setStepBody(int index) {
        bodyInfo.clear();
        bodyActions.clear();

        GettingStartedStep step = (GettingStartedStep) stepsPanel.getWidget(index);
        GettingStartedItem item = step.getItem();


        Paragraph p = new Paragraph(item.getDescription());
        p.getElement().setAttribute("style", "color:hotpink;");
        bodyInfo.add(p);

        forwardButton = new WfmButton2("Take me there", WfmButton2.BTN_PRIMARY, event -> {
            KpiSideNavBox quickAdd = DashboardUtils.generateGettingStartedComponent(item);
            if (quickAdd != null) {
                quickAdd.setCommand(() -> {
                    quickAdd.remove();
                    markAsDone(item);
                });
            }
        });
        bodyActions.add(forwardButton);
        bodyActions.add(skipButton);

        new MaterialAnimation().transition(Transition.FADEIN).duration(400).animate(contentPanel);
    }

    private void markAsDone(GettingStartedItem item) {
        for (int i = 0; i < stepsPanel.getWidgetCount(); i++) {
            GettingStartedStep step = (GettingStartedStep) stepsPanel.getWidget(i);
            if (item.equals(step.getItem())) {
                LoadingPanel.loading(true, this);
                item.setState(GettingStartedItem.State.PASSED);
                ModuleDashboardService.App.get().updateDashboardSetupConfiguration(item, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.someErrorsOccurredWhileConfiguringSettings());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        step.setPassed();
                        nextStep();
                        refreshProgressBar();
                    }
                });
            }
        }
    }

    private void refreshProgressBar() {
        int total = stepsPanel.getWidgetCount();
        int count = 0;
        for (int i = 0; i < stepsPanel.getWidgetCount(); i++) {
            GettingStartedStep step = (GettingStartedStep) stepsPanel.getWidget(i);
            if (step.getItem().isPassed() || step.getItem().isDisabled()) {
                count++;
            }
        }

        double percent = (count*100.0)/total;

        progressNum.getElement().setInnerHTML(percent + "%");
        progressBar.getElement().getStyle().setWidth(percent, Style.Unit.PCT);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.GETTING_STARTED;
    }

    class GettingStartedStep extends MaterialWidget implements HasSelectionHandlers<GettingStartedStep> {
        Span icon;
        StepTitle title;

        GettingStartedItem item;

        public GettingStartedStep(GettingStartedItem item) {
            super(Document.get().createElement("figure"));
            this.item = item;

            icon = new Span();
            icon.addStyleName("figicon");
            Element i = Document.get().createElement("i");
            i.setClassName("ficon--check-thin");
            icon.getElement().appendChild(i);

            title = new StepTitle(item.getTitle());

            if (item.isEnabled()) {
//                title.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                title.addClickHandler(event -> SelectionEvent.fire(GettingStartedStep.this, GettingStartedStep.this));
            } else if (item.isPassed()) {
                setPassed();
            } else {
                setClass("locked");
                setEnabled(false);
//                getElement().getStyle().clearCursor();
                i.setClassName("ficon--lock");
            }

            add(icon);
            add(title);
        }

        public void setActive(boolean active) {
            if (active) {
                setClass(CssName.ACTIVE);
            } else if (item.isEnabled()){
                setClass(CssName.INACTIVE);
            }
        }

        public void setPassed() {
            setClass("passed");
            setEnabled(false);
            item.setState(GettingStartedItem.State.PASSED);
//            getElement().getStyle().clearCursor();
        }

        public boolean isEnable() {
            return item.isEnabled();
        }

        public GettingStartedItem getItem() {
            return item;
        }

        @Override
        public HandlerRegistration addSelectionHandler(SelectionHandler<GettingStartedStep> handler) {
            return this.addHandler((SelectionHandler<GettingStartedStep>) event -> {
                if (isEnable()) {
                    handler.onSelection(event);
                }
            }, SelectionEvent.getType());
        }

        class StepTitle extends MaterialWidget {

            public StepTitle(String title) {
                super(Document.get().createElement("figcaption"));
                getElement().setInnerHTML(title);
            }
        }
    }

    @Override
    protected void getSampleData(boolean nodata) {

    }
}
