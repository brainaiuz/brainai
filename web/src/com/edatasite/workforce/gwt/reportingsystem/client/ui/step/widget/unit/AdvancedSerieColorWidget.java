package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ReportingColumnColorSettings;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.HashMap;
import java.util.LinkedList;

public class AdvancedSerieColorWidget extends KpiSideNavBox {
    public static final String GRADIENT = "GRADIENT";
    public static final String CONDITIONAL_FORMATTING = "CONDITIONAL_FORMATTING";
    private final Command saveAction;
    private final HashMap<ChartSerie, ReportingColumnColorSettings> chartColorItems = new HashMap<>();
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private final HashMap<ChartSerie, GradientColorWidget> gradientColorItems = new HashMap<>();
    private ReportingColumnColorSettings colorSettings;
    private GradientColorWidget gradientSettings;
    private ChartTypeEnum activeChartType;
    private boolean isWidget = false;
    private boolean isGradient = false;
    private Heading header;
    private DataListBox colorType;
    private Div colorContainer;
    private ChartSerie currentSerie;

    public AdvancedSerieColorWidget(Command saveAction, ChartTypeEnum activeChartType, Boolean isGradient) {
        super(true, 440);
        this.saveAction = saveAction;
        this.activeChartType = activeChartType;
        this.isGradient = isGradient;
        initialize();
    }

    /**
     * need to add gauge chart and reporting widget UI colorType
     * dropdown (Gradient or conditionalFormatting) and need to customize this class for these types\
     */
    public AdvancedSerieColorWidget(Command saveAction, boolean isWidget) {
        super(true, 440);
        this.saveAction = saveAction;
        this.isWidget = isWidget;
        initialize();
    }

    public void setGradient(boolean gradient) {
        isGradient = gradient;
    }

    public boolean isGradient() {
        return isGradient;
    }

    public ChartTypeEnum getActiveChartType() {
        return activeChartType;
    }

    public void setActiveChartType(ChartTypeEnum activeChartType) {
        this.activeChartType = activeChartType;
    }

    private void initialize() {
        header = new Heading(HeadingSize.H1);
        changeTitle();
        addHeader(header);
//        addColorSettings();

        addBody(initColorDataListBox());
        colorContainer = new Div();
        addBody(colorContainer);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(handler -> {
            if (saveAction != null) {
                saveAction.execute();
            }
            hide();
        });
        addFooter(saveButton);
    }

    private FormGroup initColorDataListBox() {
        colorType = new DataListBox();
        colorType.setWithoutNullLabel(true);
        colorType.setItems(new SelectItem[]{
                new SelectItem(1, reportingStrings.conditionalFormating(), CONDITIONAL_FORMATTING),
                new SelectItem(2, wfmStrings.gradient(), GRADIENT)
        });
        colorType.setEnabled(!isWidget);
        colorType.addValueChangeHandler(handler -> {
            isGradient = GRADIENT.equalsIgnoreCase(colorType.getSelectedItem(true).getDescription());
            if (currentSerie != null) {
                if (isGradient && chartColorItems.get(currentSerie) != null) {
                    chartColorItems.remove(currentSerie);
                }
                if (!isGradient && gradientColorItems.get(currentSerie) != null) {
                    gradientColorItems.remove(currentSerie);
                }
            }
            changeTitle();
            initReportWidgetData(false);
        });

        return new FormGroup(wfmStrings.color(), colorType);
    }

    public void initReportWidgetData(boolean b) {
        if (isGradient) {
            if (gradientSettings == null) {
                gradientSettings = new GradientColorWidget(saveAction);
                gradientColorItems.put(currentSerie, gradientSettings);
            }
        } else {
            if (colorSettings == null) {
                colorSettings = new ReportingColumnColorSettings();
                chartColorItems.put(currentSerie, colorSettings);
            }
        }
        addWidgetToContainer();
        if (b) {
            showColorSettings();
        }
    }

    private void addWidgetToContainer() {
        colorContainer.clear();
        if (isGradient) {
            colorContainer.add(gradientSettings);
            colorType.setSelectedByDescription(GRADIENT);
        } else {
            colorSettings.activate(true);
            colorContainer.add(colorSettings);
            colorType.setSelectedByDescription(CONDITIONAL_FORMATTING);
        }
    }

    public void showColorSettings() {
        show();
    }

    public LinkedList<ColumnColor> getData(ChartSerie chartSerie) {
        if (isGradient) {
            if (gradientColorItems.get(chartSerie) != null) {
                return getGradientColorAsList(gradientColorItems.get(chartSerie).getSelectedColor());
            }
        } else {
            if (chartColorItems.get(chartSerie) != null) {
                return chartColorItems.get(chartSerie).getData();
            }
        }
        return new LinkedList<>();
    }

    public LinkedList<ColumnColor> getData() {
        if (isGradient) {
            return getGradientColorAsList(gradientSettings != null ? gradientSettings.getSelectedColor() : null);
        }
        return colorSettings != null ? colorSettings.getData() : new LinkedList<>();
    }

    public void setData(SerieConfItem serieConf) {
        if (isGradient) {
            if (gradientSettings == null) {
                gradientSettings = new GradientColorWidget(saveAction);
            }
            setGradientColorBySerieConf(serieConf);
        } else {
            if (colorSettings == null) {
                colorSettings = new ReportingColumnColorSettings();
            }
            colorSettings.setData(serieConf.getColorList());
        }
    }

    private LinkedList<ColumnColor> getGradientColorAsList(String color) {
        if (Utils.isNullOrEmpty(color)) {
            return null;
        }
        ColumnColor columnColor = new ColumnColor();
        columnColor.setColor(color);
        columnColor.setGradient(true);
        LinkedList<ColumnColor> gradientColorItemAsList = new LinkedList<>();
        gradientColorItemAsList.add(columnColor);
        return gradientColorItemAsList;
    }

    private void setGradientColorBySerieConf(SerieConfItem serieConf) {
        if (serieConf.getColorList().size() > 0) {
            gradientSettings.setSelectedColor(serieConf.getColorList().get(0).getColor());
        }
    }

    public void setData(ChartSerie chartSerie, SerieConfItem serieConf) {
        if (isGradient) {
            gradientSettings = gradientColorItems.get(chartSerie);
            if (gradientSettings == null) {
                gradientSettings = new GradientColorWidget(saveAction);
            }
            setGradientColorBySerieConf(serieConf);
            gradientColorItems.put(chartSerie, gradientSettings);
        } else {
            colorSettings = chartColorItems.get(chartSerie);
            if (colorSettings == null) {
                colorSettings = new ReportingColumnColorSettings();
            }
            colorSettings.setData(serieConf.getColorList());
            chartColorItems.put(chartSerie, colorSettings);
        }
    }

    public void addChartSerie(ChartSerie chartSerie) {
        this.currentSerie = chartSerie;
        if (chartSerie.getSerieConf() != null) {
            if (isGradient) {
                gradientSettings = gradientColorItems.get(chartSerie);
                if (gradientSettings == null) {
                    gradientSettings = new GradientColorWidget(saveAction);
                }
                if (chartSerie.getSerieConf().getColorList() != null && chartSerie.getSerieConf().getColorList().size() == 1) {
                    gradientSettings.setSelectedColor(chartSerie.getSerieConf().getColorList().get(0).getColor());
                }
                gradientColorItems.put(chartSerie, gradientSettings);
            } else {
                colorSettings = chartColorItems.get(chartSerie);
                if (colorSettings == null) {
                    colorSettings = new ReportingColumnColorSettings();
                }
                colorSettings.setData(chartSerie.getSerieConf().getColorList());
                chartColorItems.put(chartSerie, colorSettings);
            }
        }
        addWidgetToContainer();
        showColorSettings();
    }

    public void removeSerieColor(ChartSerie chartSerie) {
        if (isGradient) {
            if (gradientColorItems.get(chartSerie) != null) {
                gradientColorItems.remove(chartSerie);
            }
        } else {
            if (chartColorItems.get(chartSerie) != null) {
                chartColorItems.remove(chartSerie);
            }
        }
    }

    public void changeTitle() {
        if (isGradient) {
            header.setText(wfmStrings.gradient());
        } else {
            header.setText(reportingStrings.conditionalFormating());
        }
    }

    public void disebleGradientWidget(Boolean b) {
        if (!b) {
            this.isGradient = b;
            colorType.setSelectedByDescription(CONDITIONAL_FORMATTING);
        }
        colorType.setEnabled(b);
    }
}
