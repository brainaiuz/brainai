package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedServiceAsync;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.NewDepartment;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Abdulaziz
 * Date: 15.06.2009
 * Time: 14:49:09
 */
public class CreateDepartmentGuideView extends GettingStartedMainView {
    private final GettingStartedServiceAsync gettingstartedService = GettingStartedService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmForm table;
    private WfmForm.Field nameField;
    private WfmForm.Field membersField;
    private WfmForm.Field departmentLeadersField;
    private TextBox name;
    private DataListBox departmentLeaders;
    private final CallbackSynchronizer callbacksynchronizer = new CallbackSynchronizer();
    private KpiCellTree teamMembersCell;
    private KpiDataGrid<SelectItem> departmentsGrid;

    public CreateDepartmentGuideView() {
        super(true);
    }

    private void clearFields() {
        name.setText("");
        teamMembersCell.clear();
        teamMembersCell.getSelectAll().setValue(false);
    }

    private void init() {
        count = 1;
        LoadingPanel.loading(true);

        gettingstartedService.getLastDepartments(callbacksynchronizer.registerCallback(new AbstractAsyncCallback<SelectItem[]>() {

            public void success(SelectItem[] departments) {
                departmentsGrid.supplyProvider(departments);
                departmentsGrid.refresh();
            }

        }));
        LoadingPanel.loading(false);
    }

    private int count = 1;

    private void initVariables() {
        table = new WfmForm("20%,78%".split(","));

        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);


        departmentLeaders = new DataListBox();
        departmentLeaders.addStyleName(DEFAULT_WIDTH);
        departmentLeaders.setAllowFirstItem(true);

        departmentsGrid = new KpiDataGrid<>(KEY_PROVIDER);
        departmentsGrid.addStyleName(DEFAULT_WIDTH);
        departmentsGrid.setHeight("250px");
        Column<SelectItem, String> department = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
                return (count++) + ". " + object.getName();
            }
        };

        departmentsGrid.addColumn(department, wfmStrings.latestAddedDepartments());
        departmentsGrid.setColumnWidth(department, 60, com.google.gwt.dom.client.Style.Unit.PCT);

        teamMembersCell = new KpiCellTree();

        teamMembersCell.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    Integer selectedLeader = departmentLeaders.getSelectedItem() != null ? departmentLeaders
                            .getSelectedItem().getId()
                            : null;
                    SelectItem[] selection = new SelectItem[selectedDataGrid.getList().size()];

                    for (int i = 0; i < selection.length; i++) {
                        selection[i] = new SelectItem(selectedDataGrid.getList().get(i).getId(), selectedDataGrid.getList().get(i).getName());
                    }

                    departmentLeaders.setEnabled(selection.length > 0);
                    departmentLeaders.setItems(selection);
                    departmentLeaders.setSelected(selectedLeader);
                });
                //Employee Name Blow
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };

                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 50, com.google.gwt.dom.client.Style.Unit.PCT);

                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {

                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    java.util.List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {

            }
        });
        //table.getFlex().setWidth("100%");
        nameField = table.addField(wfmStrings.department(), name, true);
        membersField = table.addField(wfmStrings.members(), teamMembersCell, true);
        departmentLeadersField = table.addField(wfmStrings.departmentLeader(), departmentLeaders, true);

        HTML organiezEmployees = new HTML("<span style='text-transform:capitalize;font-size:13pt;color:#1F4F8F;font-weight: bold;'>" + wfmStrings.organizeEmployees() + "</span>");


        FlexTable internalTable = new FlexTable();
        internalTable.setStyleName("stage-background");
        internalTable.setSize("900px", "100%");
        internalTable.setCellSpacing(20);

        HTMLTable.CellFormatter cellFormatter = internalTable.getCellFormatter();
        cellFormatter.setWidth(0, 0, "75%");
        cellFormatter.setWidth(0, 1, "25%");
        cellFormatter.setWidth(1, 0, "80%");
        internalTable.getFlexCellFormatter().setRowSpan(0, 1, 2);

        internalTable.setWidget(0, 0, organiezEmployees);
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);


        internalTable.setWidget(0, 1, departmentsGrid);
        cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

        internalTable.setWidget(1, 0, table);
        cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

        container.add(internalTable);
        //container.layout(true);
    }

    private SelectItem[] getItems(java.util.List list) {
        SelectItem[] items = new SelectItem[list.size()];
        for (int i = 0; i < list.size(); i++) {
            SelectItem sItem = (SelectItem) list.get(i);
            items[i] = sItem;
        }
        return items;
    }

    public SelectItem[] getItemsFromList(java.util.List list) {//<ProjectMember>
        SelectItem[] items = new SelectItem[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ProjectMember pm = (ProjectMember) list.get(i);
            items[i] = new SelectItem(pm.getId(), pm.getName());
        }
        return items;
    }

    private void initMembers() {
        CommonService.App.get().getCompanyEmployeesWithTeams(new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                teamMembersCell.setItems(result);
            }
        });
    }

    public void reinit() {
        clearFields();
        init();
        initMembers();

    }

    private void save(final boolean saveAndAddAnother) {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        NewDepartment department = new NewDepartment();
        department.setName(this.name.getText());
        department.setDescription(this.name.getText());
        department.setMembersId(teamMembersCell.getSelectedIds());
        department.setStartDate(new Date());
        department.setLeaderId(this.departmentLeaders.getSelectedItem().getId());

        gettingstartedService.createDepartment(department, new AbstractAsyncCallback<Void>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void object) {

                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.department()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPARTMENT_ADD, object, CreateDepartmentGuideView.this);
                shellOk(saveAndAddAnother);
            }
        });
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private void shellOk(boolean saveAndAddAnother) {
        if (saveAndAddAnother) {
            reinit();
        } else {
            clearFields();
            listener.onNextButtonClick();
        }
    }

    private boolean validate() {
        int errors = 0;
        table.cleanupErrors();
        if (!Validation.validateTextBoxRequired(this.name, this.nameField)) {
            errors++;
        }
/*
        if (!Validation.validateCheckedListRequired(teamMembers.getInitialList(), membersField, wfmStrings.pleaseSpecifyMembers())) {
            errors++;
        }
*/
        if (teamMembersCell.getSelectedData().size() == 0) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(this.departmentLeaders, this.departmentLeadersField, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.pleaseSpecifyDepartmentLeader(), wfmStrings.department()))) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public boolean isFieldsEmpty() {
        return ((name.getText().equals("") || name.getText().equals(null))
                && (departmentLeaders.getItems() == null || departmentLeaders.getItems().length == 0));
    }

    public void showView() {
        initVariables();
        initMembers();
        init();
    }

    public void refresh() {
        clearFields();
        initMembers();
        init();

    }

    protected void saveAddAnother() {
        save(true);
    }

    protected void skipThisStep() {
        listener.onNextButtonClick();
    }

    protected boolean saveAndNext() {
        if (!isFieldsEmpty()) {
            save(false);
            return false;
        }
//        reinit();
        return true;
    }
}
