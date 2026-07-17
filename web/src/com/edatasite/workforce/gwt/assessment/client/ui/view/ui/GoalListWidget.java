package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 2/29/12
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalListWidget extends Composite {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<GoalItem> dataGrid;
    private ListDataProvider<GoalItem> dataProvider;
    private FlowPanel centerPanel;

    private boolean isWeighTable;
    private final AddInitiateSimpleAppraisalView addInitiateSimpleAppraisalView;

    public static final ProvidesKey<GoalItem> KEY_PROVIDER = item -> item != null ? item.getObjectId() : null;

    public GoalListWidget(AddInitiateSimpleAppraisalView addInitiateSimpleAppraisalView) {
        this.addInitiateSimpleAppraisalView = addInitiateSimpleAppraisalView;
        draw();
        initWidget(centerPanel);
    }

    // drawInitialize()
    public void drawInitialize(boolean isWeighTable, GoalItem[] goalItems) {
        this.isWeighTable = isWeighTable;
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(hrmsStrings.noGoalsEmptyMessage(), null, null));

        // Говорим таблице всегда занимать 100% высоты своего родителя (centerPanel)
        dataGrid.setHeight("100%");

        // Стандартная логика
        drawSupplyProvider(goalItems);
        dataProvider.refresh();
        dataGrid.clearTable();
        drawColumns();

        // --- Управляем высотой родительской панели ---
        if (goalItems == null || goalItems.length == 0) {
            // Если данных нет, высота панели автоматическая
            centerPanel.setHeight("auto");
        } else {
            // Если данные есть, задаем панели фиксированную высоту
            centerPanel.setHeight("300px");
        }
    }

    public List<GoalItem> getListItems() {
        return dataProvider.getList();
    }

    private void draw() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.removeStyleName("cellBasedWidget-mod--static-body");
        dataGrid.setCustomHeaderStyle(true);
        dataProvider.addDataDisplay(dataGrid);
        centerPanel = new FlowPanel();
        centerPanel.addStyleName("assignedGoalsTableWrapper");
        centerPanel.add(dataGrid);
    }

    private void drawSupplyProvider(GoalItem[] goalItems) {
        List<GoalItem> goalItemList = dataProvider.getList();
        goalItemList.clear();
        if (goalItems != null) {
            Collections.addAll(goalItemList, goalItems);
        }
    }

    private void drawColumns() {
        //goal title
        Column<GoalItem, String> goalTitle = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                return object.getTitle();
            }
        };
        dataGrid.addColumn(goalTitle, hrmsStrings.goalTitle());
        dataGrid.setColumnWidth(goalTitle, 25 - (isWeighTable ? 4 : 0), Style.Unit.PCT);


        //goal description
        Column<GoalItem, String> description = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                return object.getDescription();
            }
        };
        dataGrid.addColumn(description, wfmStrings.description());
        dataGrid.setColumnWidth(description, 30 - (isWeighTable ? 5 : 0), Style.Unit.PCT);

        //goal type
        Column<GoalItem, String> type = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                return object.getGoalCategory();
            }
        };
        type.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        dataGrid.addColumn(type, wfmStrings.type());
        dataGrid.setColumnWidth(type, 25 - (isWeighTable ? 10 : 0), Style.Unit.PCT);

        if (isWeighTable) {
            //weight
            final TextInputCell inputCell = new TextInputCell();
            inputCell.setWidth("40px");
            Column<GoalItem, String> weight = new Column<GoalItem, String>(inputCell) {
                @Override
                public String getValue(GoalItem object) {
                    return object.getWeight() + "";
                }
            };
            weight.setFieldUpdater((index, object, value) -> {
                try {
                    Double goalWeightValue = Double.valueOf(value);
                    if (goalWeightValue < 0) {
                        goalWeightValue = (-1) * goalWeightValue;
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    }
                    if (goalWeightValue > 100) {
                        Info.show(hrmsStrings.enter100(), Info.Type.WARNING);
                        goalWeightValue = 100d;
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    } else if (goalWeightValue == 0) {
                        Info.show(hrmsStrings.removeZeroOrPutWeights(), Info.Type.WARNING);
                    }
                    object.setWeight(goalWeightValue);
                } catch (NumberFormatException ex) {
                    object.setWeight(0f);
                    inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                }

                addInitiateSimpleAppraisalView.recalculateWeights(false);
                dataGrid.redraw();
            });
            weight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(weight, wfmStrings.weight());
            dataGrid.setColumnWidth(weight, 12, Style.Unit.PCT);

            //given score
            final TextInputCell scoreInputCell = new TextInputCell();
            scoreInputCell.setWidth("40px");
            Column<GoalItem, String> givenScore = new Column<GoalItem, String>(scoreInputCell) {
                @Override
                public String getValue(GoalItem object) {
                    return String.valueOf(object.getGivenScore());
                }
            };
            givenScore.setFieldUpdater((index, object, value) -> {
                AppraisalsSettingsItem settingsItem = addInitiateSimpleAppraisalView.getSettingsItem();
                try {
                    Double score = Double.valueOf(value);
                    if (score > settingsItem.getToScale()) {
                        score = settingsItem.getToScale();
                        scoreInputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    } else if (score < 0) {
                        scoreInputCell.clearViewData(KEY_PROVIDER.getKey(object));
                        score = 0d;
                    }
                    object.setGivenScore(score);
                    dataGrid.redraw();
                } catch (NumberFormatException ignore) {
                    scoreInputCell.clearViewData(KEY_PROVIDER.getKey(object));
                    object.setGivenScore(0d);
                    dataGrid.redraw();
                }
            });
            givenScore.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(givenScore, wfmStrings.givenScore());
            dataGrid.setColumnWidth(givenScore, 12, Style.Unit.PCT);


            //weighted result
            Column<GoalItem, String> result = new Column<GoalItem, String>(new TextCell()) {
                @Override
                public String getValue(GoalItem object) {
//                    Double weight = goalsWeightMapListener.get(object.getObjectId());
                    Double weight = object.getWeight();
                    return String.valueOf((object.getGivenScore() * (weight != null ? weight : 0)) / 100);
                }
            };
            result.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(result, wfmStrings.weightedResult());
            dataGrid.setColumnWidth(result, 11, Style.Unit.PCT);
        }
        //remove option
        Column<GoalItem, String> remove = new Column<GoalItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(GoalItem object) {
                return wfmStrings.delete();
            }
        };
        remove.setFieldUpdater((index, object, value) -> {
            List<GoalItem> goalItems = dataProvider.getList();
//                if (isWeighTable && goalsWeightMapListener.contains(object.getObjectId())) {
//                    goalsWeightMapListener.remove(object.getObjectId());
//                }
            goalItems.remove(object);
            addInitiateSimpleAppraisalView.recalculateWeights(false);
        });
        remove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        remove.setSortable(false);
        dataGrid.addColumn(remove, wfmStrings.delete());
        dataGrid.setColumnWidth(remove, 20 - (isWeighTable ? 10 : 0), Style.Unit.PCT);
    }
}
