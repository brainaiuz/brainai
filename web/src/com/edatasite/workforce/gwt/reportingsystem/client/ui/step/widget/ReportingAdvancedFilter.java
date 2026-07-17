package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.JsResources;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.AdvancedFilterRow;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.RHTMLPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

/**
 * Created by Virus on 9/6/14.
 */
public class ReportingAdvancedFilter extends AsyncWidget {

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static ReportingAdvancedFilterUiBinder ourUiBinder = GWT.create(ReportingAdvancedFilterUiBinder.class);
    private ReportingStepControlView view;
    @UiField
    RHTMLPanel container;
    @UiField
    KpiTextBox textPattern;
    @UiField
    TableSectionElement tfooter;
    @UiField
    Div criteriaLabel;
    private boolean ready;
    private boolean elementVisible;
    private RunReportPanel filterReport;
    private BudgetRunReportPanel budgetfilterReport;

    ReportingAdvancedFilter() {
        super(null, "customReport_tab_6");
    }

    public static native void run_switch_stage()/*-{
        $doc.switch_stage();
    }-*/;

    private static native String bind(String textPattern) /*-{
        try {
            return '' + parser.parse(textPattern);
        } catch (e) {
            return e.message;
        }
    }-*/;

    private void initHandlers() {
        view.setIncludeFiltersCommand(() -> {
            ReportRpc reportRpc = view.getReport();
            reportRpc.getFieldd().clear();
            reportRpc.getValues().clear();
            reportRpc.clearBoolType();
            reportRpc.getOperators().clear();
            reportRpc.getSett().clear();
            for (ColumnRpc columnRpc : reportRpc.getColumnMap().values()) {
                columnRpc.setListFilter(false);
            }
            int y = 0;
            for (int i = 0; i < container.getWidgetCount(); i++) {
                if (container.getWidget(i) instanceof AdvancedFilterRow) {
                    AdvancedFilterRow filterRow = (AdvancedFilterRow) container.getWidget(i);
                    if (!filterRow.hasFocus()) {
                        filterRow.getFilter(y++);
                    }
                }
            }
            reportRpc.setNowPosition(1);
            if (filterReport != null || budgetfilterReport != null) {
                setDefaultHideElements();
            }
        });
        container.setRefreshCommand((value) -> {
            reIndex().execute();
            clickPattern(value).onBrowserEvent(null);
        });


        textPattern.addBlurHandler(event -> checkPattern(true));
        textPattern.addChangeHandler(event -> checkPattern(false));
    }

    private void checkPattern(boolean notify) {
        if (Utils.isNullOrEmpty(textPattern.getValue().trim())) {
            return;
        }
        String result = bind(textPattern.getValue().trim());
        if ("true".equals(result.trim().toLowerCase())) {
            view.getReport().setFilterPattern(textPattern.getValue());
            return;
        }
        if (notify) {
            new WfmMessageBox(IconEnum.INFO, Action.OK, result).open();
        }
    }

    private void setDefaultHideElements() {
        if (container.getWidgetCount() == 10) {
            ReportingAdvancedFilter.this.removeStyleName("hide_filter");
            ReportingAdvancedFilter.this.addStyleName("show_filter");
        }
    }

    private EventListener clickPattern(boolean force) {
        return event -> {
            view.includeFilters();
            String pattern = null;
            StringBuilder patt = new StringBuilder();
            boolean open = true;
            for (int i = 0; i < container.getWidgetCount(); i++) {
                AdvancedFilterRow row = (AdvancedFilterRow) container.getWidget(i);
                if (open) {
                    patt.append("(");
                    open = false;
                }
                if (i > 0) {
                    if (row.getOperator() != null && elementVisible) {
                        patt.append(row.getOperator().toLowerCase()).append(" ");
                    } else {
                        patt.append(" and "); // and if no operation
                    }
                }
                patt.append(i + 1).append(" ");
            }
            if (!open) {
                patt.append(")");
            }
            if (force) {
                pattern = patt.toString().replace(" )", ")");
            }
            if (pattern != null) {
                textPattern.setValue(pattern);
            }
            view.getReport().setFilterPattern(textPattern.getValue());
            textPattern.setPlaceholder(pattern);

            tfooter.removeClassName("nonactive");
            tfooter.addClassName("active");
            textPattern.removeStyleName("state_origin");
            textPattern.addStyleName("flipped_content");
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REPORT_FILTER_CHANGED, null, ReportingAdvancedFilter.this);
        };
    }

    private Command reIndex() {
        return () -> {
            for (int i = 0; i < container.getWidgetCount(); i++) {
                Widget widget = container.getWidget(i);
                if (widget instanceof AdvancedFilterRow) {
                    ((AdvancedFilterRow) widget).getCurrentRow();
                }
            }
        };
    }

    private AdvancedFilterRow addFilterRow(Integer id) {
        AdvancedFilterRow filterRow = new AdvancedFilterRow(view, id, elementVisible);
        filterRow.setAddRowCommand(() -> {
            ReportingAdvancedFilter.this.addFilterRow(null).setAfter(reIndex());
            calculatePattern();
        });
        if (filterReport != null || budgetfilterReport != null) {
            setDefaultHideAllFilters(id);
        }
        container.add(filterRow);
        filterRow.init();
        return filterRow;
    }

    private void setDefaultHideAllFilters(Integer id) {
        if (container.getWidgetCount() == 10 && id != null) {
            ReportingAdvancedFilter.this.removeStyleName("show_filter");
            ReportingAdvancedFilter.this.addStyleName("hide_filter");
        } else if (container.getWidgetCount() == 10 && id == null) {
            ReportingAdvancedFilter.this.removeStyleName("hide_filter");
            ReportingAdvancedFilter.this.addStyleName("show_filter");
        }
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    void setElementVisible(boolean elementvisible) {
        this.elementVisible = elementvisible;
        if (ready) {
            if (!elementVisible) {
                criteriaLabel.addStyleName("hide");
                tfooter.addClassName("hide");
            } else {
                criteriaLabel.removeStyleName("hide");
                tfooter.removeClassName("hide");
            }
            for (int i = 0; i < container.getWidgetCount(); i++) {
                if (container.getWidget(i) instanceof AdvancedFilterRow) {
                    AdvancedFilterRow row = (AdvancedFilterRow) container.getWidget(i);
                    row.setShowElement(elementvisible);
                }
            }
        }
    }

    protected Widget onInitialize() {
        add(ourUiBinder.createAndBindUi(this));
        criteriaLabel.getElement().setInnerHTML(wfmStrings.criteriaPattern());
        this.ready = true;
        run_switch_stage();
        container.clear();
        ReportRpc reportRpc = view.getReport();

        tfooter.removeClassName("hide");
        if (!elementVisible) {
            criteriaLabel.addStyleName("hide");
            tfooter.addClassName("hide");
        }
        int i;
        for (i = 0; i < reportRpc.getFieldd().size(); i++) {
            ColumnRpc columnRpc = reportRpc.getFieldd().get(i);
            if (!columnRpc.isListFilter()) {
                addFilterRow(i);
            }
        }
        for (; i < 2; i++) {
            addFilterRow(null);
        }
        initHandlers();
        if (!Utils.isNullOrEmpty(reportRpc.getFilterPattern())) {
            textPattern.setValue(reportRpc.getFilterPattern());
        }
        ScriptInjector.fromString(JsResources.instance.pegJs().getText()).inject();
        ScriptInjector.fromString(JsResources.instance.criteriaExpressionJs().getText()).inject();
        ScriptInjector.fromString("var parser = eval(module.exports);").inject();

        return null;
    }

    private void calculatePattern() {
        String pattern;
        StringBuilder patt = new StringBuilder();
        boolean open = true;
        for (int i = 0; i < container.getWidgetCount(); i++) {
            AdvancedFilterRow row = (AdvancedFilterRow) container.getWidget(i);
            if (open) {
                patt.append("(");
                open = false;
            }
            if (i > 0) {
                if (row.getOperator() != null && elementVisible) {
                    patt.append(row.getOperator().toLowerCase()).append(" ");
                } else {
                    patt.append(" and "); // and if no operation
                }
            }
            patt.append(i + 1).append(" ");
        }
        if (!open) {
            patt.append(")");
        }
        pattern = patt.toString().replace(" )", ")");
        textPattern.setValue(pattern);
        view.getReport().setFilterPattern(textPattern.getValue());
        textPattern.setPlaceholder(pattern);
    }

    void setFilterReport(RunReportPanel filterReport) {
        this.filterReport = filterReport;
    }

    void setBudgetFilterReport(BudgetRunReportPanel budgetfilterReport) {
        this.budgetfilterReport = budgetfilterReport;
    }

    interface ReportingAdvancedFilterUiBinder extends UiBinder<HTMLPanel, ReportingAdvancedFilter> {
    }

}