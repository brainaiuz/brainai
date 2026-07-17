package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.TblSmartColFactory;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 2/29/12
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompetencyListWidget extends Composite {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<WfmTreeItem> dataGrid;
    private ListDataProvider<WfmTreeItem> dataProvider;
    private FlowPanel centerPanel;
    private boolean isWeighTable;
    private final AddInitiateSimpleAppraisalView addInitiateSimpleAppraisalView;


    public static final ProvidesKey<WfmTreeItem> KEY_PROVIDER = item -> item != null ? item.getId() : null;

    public CompetencyListWidget(AddInitiateSimpleAppraisalView addInitiateSimpleAppraisalView) {
        this.addInitiateSimpleAppraisalView = addInitiateSimpleAppraisalView;
        draw();
        initWidget(centerPanel);
    }

    // drawInitialize()
    public void drawInitialize(boolean isWeighTable, List<WfmTreeItem> competencyItems) {
        this.isWeighTable = isWeighTable;
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(hrmsStrings.noCompetenciesEmptyMessage(), null, null));

        // Говорим таблице занять 100% высоты родителя
        dataGrid.setHeight("100%");

        // Стандартная логика заполнения и отрисовки
        List<WfmTreeItem> currentList = dataProvider.getList();
        currentList.clear();
        if (competencyItems != null) {
            currentList.addAll(competencyItems);
        }

        dataGrid.clearTable();
        drawColumns();
        dataProvider.refresh();

        // --- ЛОГИКА ВЫСОТЫ ---
        if (currentList.isEmpty()) {
            // Если данных нет, высота родительской панели автоматическая
            centerPanel.setHeight("auto");
        } else {
            // Если данные есть, задаем родительской панели фиксированную высоту
            centerPanel.setHeight("300px");
        }
    }

    public void addStyle(String styleName) {
    }

    public void clearErrors() {

    }

    public List<WfmTreeItem> getListItems() {
        return dataProvider.getList();
    }


    private void draw() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        centerPanel = new FlowPanel();
        dataGrid.removeStyleName("cellBasedWidget-mod--static-body");
        dataGrid.addStyleName("initSimpleAppraisalTableWrapper");
        dataGrid.setVisibleRange(0, 1000);
        dataGrid.setCustomHeaderStyle(true);
        dataProvider.addDataDisplay(dataGrid);
        centerPanel.add(dataGrid);
    }

    private void drawSupplyProvider(List<WfmTreeItem> competencyItems) {
        List<WfmTreeItem> competencyItemList = dataProvider.getList();
        competencyItemList.clear();
        competencyItemList.addAll(competencyItems);
//        Collections.addAll(competencyItemList, competencyItems);
    }

    private void drawColumns() {
        //Competency name
        Column<WfmTreeItem, String> competencyName = TblSmartColFactory.create(WfmTreeItem::getName);
        dataGrid.addColumn(competencyName, hrmsStrings.competencyName());


        //description
        Column<WfmTreeItem, String> description = TblSmartColFactory.create(WfmTreeItem::getDescription);
        dataGrid.addColumn(description, wfmStrings.description());

        if (isWeighTable) {
            //weight
            final TextInputCell inputCell = new TextInputCell();
            inputCell.setWidth("40px");
            Column<WfmTreeItem, String> weight = new Column<WfmTreeItem, String>(inputCell) {
                @Override
                public String getValue(WfmTreeItem object) {
                    return (object.getDoubleValue() != null ? object.getDoubleValue() + "" : "0");
                }
            };
            weight.setFieldUpdater((index, object, value) -> {
                try {
                    Double competencyDoubleValue = Double.valueOf(value);
                    if (competencyDoubleValue < 0) {
                        competencyDoubleValue = (-1) * competencyDoubleValue;
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    }
                    if (competencyDoubleValue > 100) {
                        Info.show(hrmsStrings.enter100(), Info.Type.WARNING);
                        competencyDoubleValue = 100d;
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    } else if (competencyDoubleValue == 0) {
                        Info.show(hrmsStrings.removeZeroOrPutWeights(), Info.Type.WARNING);
                    }
                    object.setDoubleValue(competencyDoubleValue);
                } catch (NumberFormatException ex) {
                    object.setDoubleValue(0d);
                    inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                }

                addInitiateSimpleAppraisalView.recalculateWeights(true);
                dataProvider.refresh();
            });
            weight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(weight, wfmStrings.weight());
            dataGrid.setColumnWidth(weight, "56px");

            //given score
            final TextInputCell scoreInputCell = new TextInputCell();
            scoreInputCell.setWidth("40px");
            Column<WfmTreeItem, String> givenScore = new Column<WfmTreeItem, String>(scoreInputCell) {
                @Override
                public String getValue(WfmTreeItem object) {
                    return String.valueOf(object.getGivenScore());
                }
            };
            givenScore.setFieldUpdater((index, object, value) -> {
                AppraisalsSettingsItem settingsItem = addInitiateSimpleAppraisalView.getSettingsItem();
                Double score;
                try {
                    score = Double.valueOf(value);
                    if (score > settingsItem.getToScale()) {
                        score = settingsItem.getToScale();
                    } else if (score < 0) {
                        score = 0d;
                    }
                    object.setGivenScore(score);
                } catch (NumberFormatException ignore) {
                    object.setGivenScore(0d);
                    dataGrid.redraw();
                }
                scoreInputCell.clearViewData(KEY_PROVIDER.getKey(object));
                dataGrid.redraw();
            });
            givenScore.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(givenScore, wfmStrings.givenScore());
            dataGrid.setColumnWidth(givenScore, "56px");

            //weighted result
            Column<WfmTreeItem, String> result = new Column<WfmTreeItem, String>(new TextCell()) {
                @Override
                public String getValue(WfmTreeItem object) {
                    Double weight = object.getDoubleValue();
                    return String.valueOf((object.getGivenScore() * (weight != null ? weight : 0)) / 100);
                }
            };
            result.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(result, wfmStrings.weightedResult());
            dataGrid.setColumnWidth(result, "56px");
        }

        //remove option
        Column<WfmTreeItem, String> remove = new Column<WfmTreeItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(WfmTreeItem object) {
                return wfmStrings.delete();
            }
        };
        remove.setFieldUpdater((index, object, value) -> {
            List<WfmTreeItem> competencyItems = dataProvider.getList();
            competencyItems.remove(object);
            addInitiateSimpleAppraisalView.recalculateWeights(true);
        });
        remove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        remove.setSortable(false);
        dataGrid.addColumn(remove, wfmStrings.delete());
        dataGrid.setColumnWidth(remove, "56px");
    }

    private String numberValidate(String value) {
        String s = value.replaceAll("[^0-9.]", "");
        int oneMorePoint = s.indexOf(".", s.indexOf(".") + 1);
        if (oneMorePoint != -1) {
            s = s.substring(0, oneMorePoint);
        }
        return s;
    }
}
