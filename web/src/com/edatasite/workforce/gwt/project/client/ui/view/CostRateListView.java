package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectEmployeeWageClientHistoryItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.cell.client.DatePickerCellCustom;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: maverick
 * Date: 3/24/11
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class CostRateListView extends BaseListView implements /*RoleProvider,*/Constants {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();

    private final Integer projectID;
    private Integer defaultProjectID;
    private WfmForm form;
    private DataListBox employeeList;
    private WfmForm.Field employeeListField;
    private WfmButton2 saveButton;
    private KpiDataGrid<ProjectEmployeeWageClientHistoryItem> dataGrid;
    private boolean isUnassigned;
    private MaterialPanel gridPanel;
    private final MaterialPanel panel = new MaterialPanel("section-box box-bg--1 box-radius wfmForm-container");

    public CostRateListView(final Integer projectID) {
        super("costrate", wfmStrings.costRate());
        this.projectID = projectID;

    }

    public static final ProvidesKey<ProjectEmployeeWageClientHistoryItem> PROVIDES_KEY = item -> item;

    private void drawComponents() {
        clear();
        LoadingPanel.loading(true);
        loadEmployeeList();
        form = new WfmForm();
        gridPanel = new MaterialPanel();

        dataGrid = new KpiDataGrid<>(PROVIDES_KEY);
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setPixelSize(550, 200);
        dataGrid.getElement().getStyle().setProperty("border", "1px solid #78a7c2");
        gridPanel.add(dataGrid);

        employeeList = new DataListBox();
        employeeList.addStyleName(DEFAULT_WIDTH);
        employeeListField = form.addField(wfmStrings.selectEmployee(), employeeList, true);
        initEditTableColumns();
        employeeList.addValueChangeHandler(sender -> {
            fillHistoryData(employeeList.getSelectedItem().getId());
        });
        form.addField(wfmStrings.changes(), gridPanel);
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(sender -> save());

        if (defaultProjectID != null && defaultProjectID.equals(projectID)) {
            saveButton.setEnabled(false);
        }

        form.addButton(saveButton);
        panel.add(form);
        add(panel);
    }

    private void loadEmployeeList() {

        EmployeeService.App.get().getProjectMembersAll(projectID, new AbstractAsyncCallback<ProjectMember[]>() {

            public void failure(Throwable caught) {

                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ProjectMember[] members) {

                LoadingPanel.loading(false);

                initForm(members);
            }

        });
    }

    private void fillHistoryData(Integer projectEmployeeId) {
        LoadingPanel.loading(true);
        projectService.getProjectEmployeeWageClientHistory(projectEmployeeId, new AbstractAsyncCallback<ProjectEmployeeWageClientHistoryItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProjectEmployeeWageClientHistoryItem[] hist) {
                if (hist != null && hist.length > 0 && employeeList.getSelectedItem().getName().contains("(unassigned)")) {
                    dataGrid.supplyProvider(hist);
                    isUnassigned = true;
                } else if (hist != null && hist.length > 0) {
                    dataGrid.supplyProvider(hist);
                    isUnassigned = false;
                }
                dataGrid.refresh();
                LoadingPanel.loading(false);
            }
        });


    }

    private void initEditTableColumns() {

        final DatePickerCellCustom datePickerCellCustom = new DatePickerCellCustom(DateUtils.getFormat());
        final Column<ProjectEmployeeWageClientHistoryItem, Date> modifiedDate = new Column<ProjectEmployeeWageClientHistoryItem, Date>(datePickerCellCustom) {
            @Override
            public Date getValue(ProjectEmployeeWageClientHistoryItem item) {
                return item.getChangeDate().getNonConvertedDate();
            }
        };
        modifiedDate.setCellStyleNames("datePickerCell-style");
        modifiedDate.setSortable(false);
        modifiedDate.setFieldUpdater((i, item, date) -> {
            if (i > 0) {
                Date oldDate = dataGrid.getList().get(i - 1).getChangeDate().getNonConvertedDate();
                if (oldDate != null && (date.before(oldDate) || DateUtil.resetTime(date).equals(DateUtil.resetTime(oldDate)))) {
                    datePickerCellCustom.clearViewData(PROVIDES_KEY.getKey(item));
                    dataGrid.redraw();
                    Info.show(projectStrings.modifiedDateCannotBeEarlierPreviousDate(), Info.Type.WARNING);
                    return;
                }
            }
            item.setChangeDate(date != null ? new DateNonConvertable(DateUtil.resetTime(date)) : null);
        });
        dataGrid.addColumn(modifiedDate, wfmStrings.modifiedDate());
        dataGrid.setColumnWidth(modifiedDate, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        //wageRate
        Column<ProjectEmployeeWageClientHistoryItem, String> wageRate = null;
        if (Utils.hasGenericAccess(GenericSettingsEnum.IS_DISABLED_WAGE_RATE)) {
            wageRate = new Column<ProjectEmployeeWageClientHistoryItem, String>(new TextCell()) {
                @Override
                public String getValue(final ProjectEmployeeWageClientHistoryItem object) {
                    return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                }
            };
        } else {
            final TextInputCell wageRateInputCell = new TextInputCell();
            wageRate = new Column<ProjectEmployeeWageClientHistoryItem, String>(wageRateInputCell) {
                @Override
                public String getValue(ProjectEmployeeWageClientHistoryItem item) {
                    wageRateInputCell.setEnabled(item.isCurrent());
                    return item.getWageRate() != null ? item.getWageRate().toString() : "00:00";

                }
            };
            wageRate.setFieldUpdater((index, item, value) -> {
                if (isUnassigned) {
                    item.getWageRate();
                } else {
                    item.setWageRate(Double.valueOf(value));
                }
            });
        }
        dataGrid.addColumn(wageRate, wfmStrings.wageRate());
        dataGrid.setColumnWidth(wageRate, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        //chargeRate
        final TextInputCell chargeRateInputCell = new TextInputCell();
        Column<ProjectEmployeeWageClientHistoryItem, String> chargeRate = new Column<ProjectEmployeeWageClientHistoryItem, String>(chargeRateInputCell) {
            @Override
            public String getValue(ProjectEmployeeWageClientHistoryItem item) {
                chargeRateInputCell.setEnabled(item.isCurrent());
                return item.getClientChargeRate() != null ? item.getClientChargeRate().toString() : "00:00";
            }
        };
        chargeRate.setFieldUpdater((index, item, value) -> {
            if (isUnassigned) {
                item.getClientChargeRate();
            } else {
                item.setClientChargeRate(Double.valueOf(value));
            }
        });

        dataGrid.addColumn(chargeRate, wfmStrings.clientChargeRate());
        dataGrid.setColumnWidth(chargeRate, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        //action
        Column<ProjectEmployeeWageClientHistoryItem, String> action = new Column<ProjectEmployeeWageClientHistoryItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(ProjectEmployeeWageClientHistoryItem item) {
                if (isUnassigned) {
                    return "";
                } else {
                    return item.isCurrent() ? "" : wfmStrings.delete();
                }
            }
        };
        action.setFieldUpdater((index, item, value) -> {
            if (!item.isCurrent()) {
                List<ProjectEmployeeWageClientHistoryItem> items = dataGrid.getList();
                items.remove(item);
                deleteHistory(item.getObjectId());
            }
        });

        dataGrid.addColumn(action, wfmStrings.action());
        dataGrid.setColumnWidth(action, 25, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private boolean validate() {
        int errors = 0;

        if (employeeList.getSelectedIndex() == 0) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.pleaseSelectEmployee(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {

        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);

        ProjectService.App.get().updateProjectEmployeeWageClientHistory(dataGrid.getList().toArray(new ProjectEmployeeWageClientHistoryItem[dataGrid.getList().size()]), employeeList.getSelectedItem().getId(), projectID, new AbstractAsyncCallback() {

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_MEMBER_ADD, projectID, CostRateListView.this);
            }
        });
    }

    private void deleteHistory(Integer historyId) {

        projectService.deleteProjectEmployeeWageClientRateHistory(historyId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Void aVoid) {
            }
        });
    }

    private void initForm(ProjectMember[] members) {

        SelectItem[] selectItems = new SelectItem[members.length];
        int loop = 0;
        for (ProjectMember mem : members) {
            selectItems[loop] = new SelectItem(mem.getProjectEmployeeId(), (mem.isDeleted()) ? mem.getName() + " (unassigned)" : mem.getEmployeeNumber() != null ? mem.getEmployeeNumber()  + "-> " + mem.getName() : mem.getName());
            loop++;
        }

        employeeList.setItems(selectItems);
        employeeList.setSelectedIndex(0);

    }

    protected Widget onInitialize() {
        drawComponents();
        return null;
    }

    @Override
    public String getIconStyle() {
        return "bgMark cost-rate-list";
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