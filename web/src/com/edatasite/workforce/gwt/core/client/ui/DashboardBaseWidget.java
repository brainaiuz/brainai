package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.chart.client.charts.AbstractChart;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.Objects;

/**
 * Created by Dilshod Madrahimov on 9/16/15 8:10 PM
 */
public abstract class DashboardBaseWidget extends AsyncWidget implements Constants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    interface DashboardBaseWidgetBinder extends UiBinder<Widget, DashboardBaseWidget> {
    }

    private static UiBinder uiBinder = GWT.create(DashboardBaseWidgetBinder.class);

    @UiField
    public HTMLPanel mainPanel;
    @UiField
    public HTMLPanel headerPanel;
    @UiField
    public HTMLPanel headerRow;
    @UiField
    public HTMLPanel title;
    @UiField
    public HTMLPanel filterPanel;
    @UiField
    public HTMLPanel actionPanel;
    @UiField
    public HTMLPanel contentPanel;

    public WfmButton2 resetButton;
    public Div resetPanel;
    public Div emptyMessagePanel = new Div("widget-empty-message");
    public String noDataClass = "no-data--content";

    protected DashboardComponentItem gridItemConfig;
    protected AbstractChart chart;
    protected Integer start = 0;
    protected Integer limit = 10;
    protected ExtendedCommand command;
    protected boolean fromSettings;

    protected boolean enableToShowSample = false;
    protected boolean busy;

    public DashboardBaseWidget() {
        super();
    }

    @Override
    public Widget onInitialize() {
        HTMLPanel rootWidget = (HTMLPanel) uiBinder.createAndBindUi(this);

        add(rootWidget);
        rootWidget.getParent().getElement().setAttribute("id", getCode());
        rootWidget.getParent().getElement().setClassName("gwt-wrapper");
        gridItemConfig.setFromResetButton(false);
        resetButton = new WfmButton2(null, "btn btn--icon", "ficon--repeat");
        resetButton.removeHasiconLeftStyle();
        resetButton.addClickHandler(clickEvent -> {
            gridItemConfig.setFromResetButton(true);
            loadComponentData();
        });
        resetPanel = new Div("widget-heading__action");
        resetPanel.add(resetButton);

        initInternal();

        actionPanel.add(resetPanel);

        if (fromSettings) {
            contentPanel.addStyleName(noDataClass);
        }
        return rootWidget;
    }

    public void provide() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess() {
                loadComponentData();
            }
        });
    }

    public void provideWithSampleData() {
        enableToShowSample = true;
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess() {
                loadComponentData();
            }
        });
    }

    protected void clearPanel() {
        contentPanel.clear();
        contentPanel.getElement().removeAllChildren();

        if (getEmptyText() != null) {
            emptyMessagePanel.getElement().setInnerText(getEmptyText());
            contentPanel.add(emptyMessagePanel);
        }
    }

    public void showEmptyMessage() {
        if (getEmptyText() != null) {
            emptyMessagePanel.getElement().setInnerText(getEmptyText());
            contentPanel.add(emptyMessagePanel);
        }
    }

    public void hideEmptyMessage() {
        if (emptyMessagePanel.getParent() != null) {
            emptyMessagePanel.removeFromParent();
        }
    }

    protected void initInternal() {

    }

    public void resizeChart() {

        if (chart != null) {
            chart.reflow();
        }
    }

    public void setTitle(String title) {
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText(title);

        this.title.clear();
        this.title.add(heading);
    }

    public void setTitle(Widget title) {
        this.title.clear();
        this.title.add(title);
    }

    protected void noData() {
        contentPanel.clear();

        Div noDataContent = new Div("chart-no-data");
        noDataContent.getElement().setInnerHTML(wfmStrings.noDataAvailable());
        contentPanel.add(noDataContent);
        contentPanel.addStyleName(noDataClass);
        getSampleData(true);
    }

    public void loadComponentData() {
        if (!fromSettings) {
            contentPanel.removeStyleName(noDataClass);
        }

        if (enableToShowSample) {
            getSampleData(false);
        } else {
            setStart(0);
            getData();
        }
    }
    protected abstract void getData();

    protected abstract void getSampleData(boolean nodata);

    public abstract String getCode();

    protected String getEmptyText() {
        return wfmStrings.thereAreNoItemsToShow();
    }

    public DashboardComponentItem getGridItemConfig() {
        return gridItemConfig;
    }

    public void setGridItemConfig(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    public Div getEmptyMessagePanel() {
        return emptyMessagePanel;
    }

    public WfmButton2 getResetButton() {
        return resetButton;
    }

    public HTMLPanel getContentPanel() {
        return contentPanel;
    }

    public void setFromSettings(boolean fromSettings) {
        this.fromSettings = fromSettings;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setEnableToShowSample(boolean enableToShowSample) {
        this.enableToShowSample = enableToShowSample;
    }

    protected class DashboardFooter extends MaterialPanel {

        private Span loadingbar = new Span();

        public DashboardFooter() {
            this.setStyleName("widget-footer");
            loadingbar.addStyleName("blue widget-loading--svg widget-loading");
            this.setVisible(false);
            addComponents();
        }

        private void addComponents() {
            WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
            moreButton.getElement().setInnerText(wfmStrings.loadMore());
            moreButton.addClickHandler(clickEvent -> {
                moreButton.setEnabled(false);
                loadingbar.setVisible(true);
                start = start + limit;
                getData();
            });

            setCommand(new ExtendedCommand() {
                @Override
                public void execute(Integer count) {
                    setVisible(Objects.equals(count, limit));
                    loadingbar.setVisible(false);
                    moreButton.setEnabled(true);
                }
            });

            loadingbar.setStyleName("blue widget-loading--svg widget-loading");
            loadingbar.setVisible(false);
            this.add(loadingbar);
            this.add(moreButton);
        }

        public void setCommand(ExtendedCommand command) {
            DashboardBaseWidget.this.command = command;
        }
    }
}
