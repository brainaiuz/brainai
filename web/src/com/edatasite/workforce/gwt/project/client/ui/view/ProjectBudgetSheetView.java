package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmToolBar;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTable;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableChildProvider;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableDesigner;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableEmptyDataMessage;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectLabourCosts;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.11.2008
 * Time: 16:30:23
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetSheetView extends View implements Colapse {

    final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer projectId;
    private WfmTreeTable treeTable;

    private WfmButton2 btnUpdate;
    private WfmButton2 btnClearAndReCalculate;

    private Label lblTotalActualHours;
    private Label lblTotalPlannedHours;
    private Label lblTotalActualWageCost;
    private Label lblTotalPlannedWageCost;
    private Label lblTotalForcastedWageCost;
    private Label lblTotalActualClientCost;
    private Label lblTotalPlannedClientCost;
    private Label lblTotalForcastedClientCost;

    private Boolean isClearAndReCalculate = false;
    private int actionItemCount;

    public ProjectBudgetSheetView(Integer projectId) {
        super("projectBudget", projectStrings.budgetSheet());
        this.projectId = projectId;

    }

    private void calculateTotal(ProjectLabourCosts[] items) {
        int totalActualHours = 0;
        int totalPlannedHours = 0;
        float totalActualWageCost = 0;
        float totalPlannedWageCost = 0;
        float totalForcastedWageCost = 0;
        float totalActualClientCost = 0;
        float totalPlannedClientCost = 0;
        float totalForcastedClientCost = 0;

        for (ProjectLabourCosts item : items) {
            totalActualHours += item.getTimspent();
            totalPlannedHours += item.getEstimatedTime();
            totalActualWageCost += item.getActualWageAmount();
            totalPlannedWageCost += item.getPlannedWageAmount();
            totalForcastedWageCost += item.getWageAmmount();
            totalActualClientCost += item.getActualClientChargeAmount();
            totalPlannedClientCost += item.getPlannedClientChargeAmount();
            totalForcastedClientCost += item.getClientWageAmmount();
        }
        lblTotalActualHours.setText(projectStrings.totalActualHours() + ": " + Utils.formatMinutes(totalActualHours));
        lblTotalPlannedHours.setText(projectStrings.totalEstimatedHours() + ": " + Utils.formatMinutes(totalPlannedHours));
        lblTotalActualWageCost.setText(projectStrings.totalActualWageCost() + ": " + Utils.formatDouble(totalActualWageCost));
        lblTotalPlannedWageCost.setText(projectStrings.totalPlannedWageCost() + ": " + Utils.formatDouble(totalPlannedWageCost));
        lblTotalForcastedWageCost.setText(projectStrings.totalForecastWageCost() + ": " + Utils.formatDouble(totalForcastedWageCost));
        lblTotalActualClientCost.setText(projectStrings.totalActualClientCost() + ": " + Utils.formatDouble(totalActualClientCost));
        lblTotalPlannedClientCost.setText(projectStrings.totalPlannedClientCost() + ": " + Utils.formatDouble(totalPlannedClientCost));
        lblTotalForcastedClientCost.setText(projectStrings.totalForecastClientCost() + ": " + Utils.formatDouble(totalForcastedClientCost));


    }

    protected Widget onInitialize() {
        MaterialPanel mainPanel = new MaterialPanel("scroll-box--x");
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("wfm-tool-bar");
        btnUpdate = new WfmButton2(wfmStrings.update());
        btnUpdate.addClickHandler(be -> {
            isClearAndReCalculate = false;
            treeTable.refresh();
        });

        btnClearAndReCalculate = new WfmButton2(wfmStrings.clearAndReCalculate());
        btnClearAndReCalculate.addClickHandler(be -> {
            isClearAndReCalculate = true;
            treeTable.refresh();
        });

        MaterialPanel pnlUpdate = new MaterialPanel();
        pnlUpdate.setStyleName("panel-box panel-box--right");

        Div updateDiv = new Div("panel-box__item");
        Div clearDiv = new Div("panel-box__item");
        updateDiv.add(btnUpdate);
        clearDiv.add(btnClearAndReCalculate);

        pnlUpdate.add(updateDiv);
        pnlUpdate.add(clearDiv);

        treeTable = new WfmTreeTable(createColumns(), getProvider(), getChildProvider(), getDesigner());
        FlexTable hPanel = new FlexTable();
        hPanel.setStyleName("table");
        lblTotalActualHours = new Label();
        lblTotalPlannedHours = new Label();
        lblTotalActualWageCost = new Label();
        lblTotalPlannedWageCost = new Label();
        lblTotalForcastedWageCost = new Label();
        lblTotalActualClientCost = new Label();
        lblTotalPlannedClientCost = new Label();
        lblTotalForcastedClientCost = new Label();

        treeTable.setWidth("100%");
        treeTable.setContentPanelMaxHeight("400px");
        treeTable.getContantPanel().setStyleName("wfm-treeTable__content-panel");
        vp.add(treeTable);

        hPanel.setWidget(0, 0, lblTotalActualHours);
        hPanel.setWidget(0, 1, lblTotalPlannedHours);
        hPanel.setWidget(0, 2, lblTotalActualWageCost);
        hPanel.setWidget(0, 3, lblTotalPlannedWageCost);
        hPanel.setWidget(0, 4, lblTotalForcastedWageCost);
        hPanel.setWidget(0, 5, lblTotalActualClientCost);
        hPanel.setWidget(0, 6, lblTotalPlannedClientCost);
        hPanel.setWidget(0, 7, lblTotalForcastedClientCost);
        hPanel.getRowFormatter().setStyleName(0, "thead");
        vp.add(hPanel);
        mainPanel.add(vp);

        MaterialPanel sectionBoxPanel = new MaterialPanel("section-box box-bg--1");
        sectionBoxPanel.add(pnlUpdate);
        sectionBoxPanel.add(mainPanel);
        add(sectionBoxPanel);
        return null;
    }

    private WfmTreeTableDesigner getDesigner() {
        return new WfmTreeTableDesigner() {
            @Override
            public void treeTableTopPanel(WfmToolBar topPanel) {
            }

            @Override
            public void treeTableBottomPanel(WfmToolBar bottomPanel) {
            }

            @Override
            public void initDataEmptyTable(WfmTreeTableEmptyDataMessage widget) {

            }
        };
    }

    private WfmTreeTableChildProvider getChildProvider() {
        return new WfmTreeTableChildProvider() {
            @Override
            public boolean isHaveChilds(Object object) {
                WfmTreeItem item = (WfmTreeItem) object;
                return item.hasChildren();
            }

            @Override
            public Object[] getChilds(Object object) {
                return null;
            }
        };
    }

    private WfmTreeTableCallbackProvider getProvider() {
        return (treeItem, item, callback) -> {
            final WfmTreeItem objectItem = (WfmTreeItem) treeItem;
            if (objectItem == null) {
                LoadingPanel.loading(true);
                projectService.calculateProjectBudgets(projectId, isClearAndReCalculate, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        LoadingPanel.loading(true);
                        projectService.getProjectLabourCostsItems(projectId, new AsyncCallback<ProjectLabourCosts[]>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(ProjectLabourCosts[] result) {
                                ArrayList<Integer> treeItemIds = new ArrayList<>();
                                if (result != null && result.length > 0) {
                                    for (ProjectLabourCosts labourCost : result) {
                                        treeItemIds.add(labourCost.getId());
                                    }
                                }
                                LoadingPanel.loading(false);
                                callback.onSuccess(result, item, null, treeItemIds);
                                calculateTotal(result);
                            }
                        });
                    }
                });
            } else {
                projectService.getProjectLabourCostsSubItems(objectItem.getId(), new AsyncCallback<ProjectLabourCosts[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(ProjectLabourCosts[] result) {
                        ArrayList<Integer> treeItemIds = new ArrayList<>();
                        if (result != null && result.length > 0) {
                            for (ProjectLabourCosts labourCost : result) {
                                treeItemIds.add(labourCost.getId());
                            }
                        }
                        callback.onSuccess(result, item, objectItem.getId(), treeItemIds);
                    }
                });
            }
        };
    }


    private WfmTreeTableColumn[] createColumns() {
        WfmTreeTableColumn[] columns = new WfmTreeTableColumn[11];
        columns[0] = new WfmTreeTableColumn(wfmStrings.taskName(), 150, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return item.getName();
        });
        columns[1] = new WfmTreeTableColumn(wfmStrings.action(), 50, object -> {
            actionItemCount = 0;
            MenuBar menuBar = new MenuBar(true);
            MenuPopItem itemView = new MenuPopItem(wfmStrings.summaryView(), "");
            itemView.setCommand(() -> {
                ProjectLabourCosts item = (ProjectLabourCosts) object;
                String viewLink;
                if (item.getNodeType() == ProjectLabourCosts.WORKSTREAM) {
                    viewLink = "workstream|budget/" + item.getId().toString();
                } else {
                    viewLink = "task|budget/" + item.getId().toString();
                }
                SinksContainerFactory.entryPoint.onHistoryChanged(viewLink);
            });
            actionItemCount++;
            menuBar.addItem(itemView);
            ToolItem toolItem = new ToolItem(actionItemCount);
            toolItem.setWidget(menuBar);
            return toolItem.getAction();
        });
        columns[2] = new WfmTreeTableColumn(wfmStrings.assignedTo(), 150, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.getAssigneesCommaSep(item.getAssignees());
        });

        columns[3] = new WfmTreeTableColumn(projectStrings.actHrs(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatMinutes(item.getTimspent());
        });

        columns[4] = new WfmTreeTableColumn(projectStrings.plnHrs(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatMinutes(item.getEstimatedTime());
        });

        columns[5] = new WfmTreeTableColumn(projectStrings.actWageCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatDouble(item.getActualWageAmount());
        });

        columns[6] = new WfmTreeTableColumn(projectStrings.plnWageCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatDouble(item.getPlannedWageAmount());
        });

        columns[7] = new WfmTreeTableColumn(projectStrings.frctWageCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return item.getWageAmmountString();
        });

        columns[8] = new WfmTreeTableColumn(projectStrings.actClientCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatDouble(item.getActualClientChargeAmount());
        });

        columns[9] = new WfmTreeTableColumn(projectStrings.plnClientCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return Utils.formatDouble(item.getPlannedClientChargeAmount());
        });

        columns[10] = new WfmTreeTableColumn(projectStrings.frctClientCost(), 100, object -> {
            ProjectLabourCosts item = (ProjectLabourCosts) object;
            return item.getClientChargeAmmountString();
        });

        return columns;
    }

    public String getIconStyle() {
        return null;
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
