package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.reportingsystem.client.JsResources;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.KpiWidgetFilterRow;
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
public class KpiWidgetAdvancedFilter extends AsyncWidget {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static ReportingAdvancedFilterUiBinder ourUiBinder = GWT.create(ReportingAdvancedFilterUiBinder.class);
    private ReportingStepControlView view;
    private boolean isFirst;
    @UiField
    RHTMLPanel container;
    @UiField
    KpiTextBox textPattern;
    @UiField
    TableSectionElement tfooter;
    @UiField
    Div criteriaLabel;
    private boolean ready;
    private Command includeFilterCommand;
    private boolean elementVisible;
    private boolean isHidden;

    KpiWidgetAdvancedFilter() {
        super(null, "customReport_tab_6");
    }

    KpiWidgetAdvancedFilter(boolean isHidden) {
        super(null, "customReport_tab_6");
        this.isHidden = isHidden;
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
        setIncludeFiltersCommand(() -> {
            KpiWidgetFilterItem kpiWidgetItem;
            if (isFirst) {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
            } else {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
            }
            kpiWidgetItem.getFieldd().clear();
            kpiWidgetItem.getValues().clear();
            kpiWidgetItem.clearBoolType();
            kpiWidgetItem.getOperators().clear();
            kpiWidgetItem.getSett().clear();
            for (ColumnRpc columnRpc : view.getReport().getColumnMap().values()) {
                columnRpc.setListFilter(false);
            }
            int y = 0;
            for (int i = 0; i < container.getWidgetCount(); i++) {
                if (container.getWidget(i) instanceof KpiWidgetFilterRow) {
                    KpiWidgetFilterRow filterRow = (KpiWidgetFilterRow) container.getWidget(i);
                    if (!filterRow.hasFocus()) {
                        filterRow.getFilter(y++);
                    }
                }
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
            KpiWidgetFilterItem kpiWidgetItem;
            if (isFirst) {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
            } else {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
            }
            kpiWidgetItem.setFilterPattern(textPattern.getValue());
            return;
        }
        if (notify) {
            new WfmMessageBox(IconEnum.INFO, Action.OK, result).open();
        }
    }

    private EventListener clickPattern(boolean force) {
        return event -> {
            includeFilters();
            String pattern = null;
            StringBuilder patt = new StringBuilder();
            boolean open = true;
            for (int i = 0; i < container.getWidgetCount(); i++) {
                KpiWidgetFilterRow row = (KpiWidgetFilterRow) container.getWidget(i);
                if (open) {
                    patt.append("(");
                    open = false;
                }
                if (i > 0) {
                    if (row.getOperator() != null && elementVisible) {
                        patt.append(row.getOperator().toLowerCase()).append(" ");
                    } else {
                        patt.append(" and ");
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
            KpiWidgetFilterItem kpiWidgetItem;
            if (isFirst) {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
            } else {
                kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
            }
            kpiWidgetItem.setFilterPattern(textPattern.getValue());
            textPattern.setPlaceholder(pattern);

            tfooter.removeClassName("nonactive");
            tfooter.addClassName("active");
            textPattern.removeStyleName("state_origin");
            textPattern.addStyleName("flipped_content");
        };
    }

    private void setIncludeFiltersCommand(Command filterChanges) {
        this.includeFilterCommand = filterChanges;
    }

    private void includeFilters() {
        if (includeFilterCommand != null) {
            includeFilterCommand.execute();
        }
    }

    private Command reIndex() {
        return () -> {
            for (int i = 0; i < container.getWidgetCount(); i++) {
                Widget widget = container.getWidget(i);
                if (widget instanceof KpiWidgetFilterRow) {
                    ((KpiWidgetFilterRow) widget).getCurrentRow();
                }
            }
        };
    }

    private KpiWidgetFilterRow addFilterRow(Integer id) {
        KpiWidgetFilterRow filterRow = new KpiWidgetFilterRow(view, id, isFirst, elementVisible);
        filterRow.setAddRowCommand(() -> {
            KpiWidgetAdvancedFilter.this.addFilterRow(null).setAfter(reIndex());
            calculatePattern();
        });
        if (isHidden) {
            filterRow.setHideCommand(true);
        }
        container.add(filterRow);
        filterRow.init();
        return filterRow;

    }

    public void setView(ReportingStepControlView view, boolean isFirst) {
        this.view = view;
        this.isFirst = isFirst;
    }

    void setElementVisible(boolean elementvisible) {
        elementVisible = elementvisible;
        if (ready) {
            if (!elementVisible) {
                criteriaLabel.addStyleName("hide");
                tfooter.addClassName("hide");
            } else {
                criteriaLabel.removeStyleName("hide");
                tfooter.removeClassName("hide");
            }
            for (int i = 0; i < container.getWidgetCount(); i++) {
                if (container.getWidget(i) instanceof KpiWidgetFilterRow) {
                    KpiWidgetFilterRow row = (KpiWidgetFilterRow) container.getWidget(i);
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

        KpiWidgetFilterItem kpiWidgetItem;
        if (isFirst) {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
        } else {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
        }

        tfooter.removeClassName("hide");
        if (!elementVisible) {
            criteriaLabel.addStyleName("hide");
            tfooter.addClassName("hide");
        }
        int i;
        for (i = 0; i < kpiWidgetItem.getFieldd().size(); i++) {
            ColumnRpc columnRpc = kpiWidgetItem.getFieldd().get(i);
            if (!columnRpc.isListFilter()) {
                addFilterRow(i);
            }
        }
        for (; i < 1; i++) {
            addFilterRow(null);
        }
        initHandlers();
        if (!Utils.isNullOrEmpty(kpiWidgetItem.getFilterPattern())) {
            textPattern.setValue(kpiWidgetItem.getFilterPattern());
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
            KpiWidgetFilterRow row = (KpiWidgetFilterRow) container.getWidget(i);
            if (open) {
                patt.append("(");
                open = false;
            }
            if (i > 0) {
                if (row.getOperator() != null && elementVisible) {
                    patt.append(row.getOperator().toLowerCase()).append(" ");
                } else {
                    patt.append(" and ");
                }
            }
            patt.append(i + 1).append(" ");
        }
        if (!open) {
            patt.append(")");
        }
        pattern = patt.toString().replace(" )", ")");
        textPattern.setValue(pattern);
        KpiWidgetFilterItem kpiWidgetItem;
        if (isFirst) {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
        } else {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
        }
        kpiWidgetItem.setFilterPattern(textPattern.getValue());
        textPattern.setPlaceholder(pattern);
    }

    interface ReportingAdvancedFilterUiBinder extends UiBinder<HTMLPanel, KpiWidgetAdvancedFilter> {
    }

}