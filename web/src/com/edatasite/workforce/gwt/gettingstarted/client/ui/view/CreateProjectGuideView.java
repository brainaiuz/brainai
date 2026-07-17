package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedServiceAsync;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
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
 * Date: 16.06.2009
 * Time: 12:56:51
 */
public class CreateProjectGuideView extends GettingStartedMainView {
    private final GettingStartedServiceAsync gettingstartedService = GettingStartedService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private WfmForm table;
    private WfmForm.Field nameField;
    private WfmForm.Field projectMembersField;
    private WfmForm.Field descriptionField;
    private WfmForm.Field startDateField;
    private WfmForm.Field endDateField;
    private WfmForm.Field managerField;
    private Numbering number;
    private TextBox name;

    private DataListBox manager;

    private KpiDataGrid<SelectItem> projectsList;
    private final CallbackSynchronizer callbackSynchronizer = new CallbackSynchronizer();

    private KpiCellTree teamMembersCell;

    private NumberData numberData;

    public CreateProjectGuideView() {
        super(true);
    }

    public void init() {
        count = 1;
        LoadingPanel.loading(true);

        gettingstartedService.getLastProjects(callbackSynchronizer.registerCallback(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                projectsList.supplyProvider(result);
                projectsList.refresh();
            }
        }));

        ProjectService.App.get().generateProjectNumber(new Date(), null, null, new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(NumberData result) {
                numberData = result;
                number.setNumberData(numberData);
            }
        });
        LoadingPanel.loading(false);
    }


    public void clearFields() {
        name.setText("");
        teamMembersCell.clear();
        teamMembersCell.getSelectAll().setValue(false);
    }

    public void reinit() {
        clearFields();
        init();
        initMembers();
    }

    private int count = 1;

    public void initComponents() {
        table = new WfmForm("20%,78%".split(","));

        number = new Numbering();

        number.addStyleName(DEFAULT_WIDTH);
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);

        manager = new DataListBox();
        manager.setAllowFirstItem(true);
        manager.addStyleName(DEFAULT_WIDTH);

        projectsList = new KpiDataGrid<>(KEY_PROVIDER);
        projectsList.addStyleName(DEFAULT_WIDTH);
        projectsList.setHeight("250px");

        Column<SelectItem, String> department = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
                return (count++) + ". " + object.getName();
            }
        };

        projectsList.addColumn(department, wfmStrings.latestAddedProjects());
        projectsList.setColumnWidth(department, 60, com.google.gwt.dom.client.Style.Unit.PCT);

        teamMembersCell = new KpiCellTree();
        teamMembersCell.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    Integer selectedLeader = manager.getSelectedItem() != null ? manager
                            .getSelectedItem().getId()
                            : null;
                    SelectItem[] selection = new SelectItem[selectedDataGrid.getList().size()];

                    for (int i = 0; i < selection.length; i++) {
                        selection[i] = new SelectItem(selectedDataGrid.getList().get(i).getId(), selectedDataGrid.getList().get(i).getName());
                    }

                    manager.setEnabled(selection.length > 0);
                    manager.setItems(selection);
                    manager.setSelected(selectedLeader);
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
        table.addField(wfmStrings.projectNumber(), number);
        nameField = table.addField(wfmStrings.projectName(), name, true);
        projectMembersField = table.addField(wfmStrings.membersInvolved(), teamMembersCell, true);
        managerField = table.addField(wfmStrings.manager(), manager, true);

        HTML addproject = new HTML("<span style='text-transform:capitalize;font-size:13pt;color:#1F4F8F;font-weight: bold;'>" + wfmStrings.addMess() + "</span>");

        FlexTable internalTable = new FlexTable();
        internalTable.setStyleName("stage-background");
        internalTable.setSize("900px", "100%");
        internalTable.setCellSpacing(20);
        HTMLTable.CellFormatter cellFormatter = internalTable.getCellFormatter();
        cellFormatter.setWidth(0, 0, "75%");
        cellFormatter.setWidth(0, 1, "25%");
        cellFormatter.setWidth(1, 0, "80%");

        internalTable.setWidget(0, 0, addproject);
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.getFlexCellFormatter().setRowSpan(0, 1, 2);
        internalTable.setWidget(0, 1, projectsList);
        cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

        internalTable.setWidget(1, 0, table);
        cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

        container.add(internalTable);
        //container.layout(true);


    }

    private void initMembers() {
        CommonService.App.get().getCompanyEmployeesWithTeams(new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                teamMembersCell.setItems(result);
            }
        });
    }

    public SelectItem[] getItemsFromList(java.util.List list) {//<ProjectMember>
        SelectItem[] items = new SelectItem[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ProjectMember pm = (ProjectMember) list.get(i);
            items[i] = new SelectItem(pm.getId(), pm.getName());
        }
        return items;
    }

    private void save(final boolean saveAndAddAnother) {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);

        ProjectMember[] pMembers = new ProjectMember[teamMembersCell.getSelectedIds().length];
        for (int i = 0; i < teamMembersCell.getSelectedIds().length; i++) {
            pMembers[i] = new ProjectMember();
            pMembers[i].setId(teamMembersCell.getSelectedIds()[i]);
        }

        ProjectSingleItem project = new ProjectSingleItem();
        project.setName(name.getText());
        project.setProjectMembers(pMembers);
        project.setManagerId(manager.getSelectedItem().getId());
        project.setDescription(name.getText());
        project.setStartDate(DateUtil.resetTime(new Date()));

        if (numberData != null) {
            numberData = number.getNumberData(false);
            project.setNumberData(numberData);
        }
        project.setStatusId(ONGOING);

        gettingstartedService.createProject(project, new AbstractAsyncCallback() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (NumberExistingException ex) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK,
                                                                       ex.getDetailedMessage());
                    messageBox.setTitle(wfmStrings.error());
                    messageBox.open();
                } catch (Throwable ex) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }

            public void success(Object object) {
                LoadingPanel.loading(false);

                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.newProject()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_ADD, object, CreateProjectGuideView.this);
                shellOk(saveAndAddAnother);

            }
        });

    }

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
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            errors++;
        }
/*
        if (!Validation.validateCheckedListRequired(proMembers.getInitialList(), projectMembersField, wfmStrings.pleaseCheckMembers())) {
            errors++;
        }
*/
        if (!Validation.validateListBoxRequired(manager, managerField, wfmStrings.pleaseSpecifyManager())) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public boolean isFieldsEmpty() {
        return ((name.getText().equals("") || name.getText().equals(null))
                && (manager.getItems() == null || manager.getItems().length == 0));
    }

    public void showView() {
        initComponents();
        initMembers();
        init();

    }

    public void refresh() {
        init();
        initMembers();
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
        return true;
    }
}
