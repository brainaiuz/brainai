package com.edatasite.workforce.gwt.team.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.ISelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.NewTeam;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.*;
import java.util.stream.Collectors;

public class MultiAddDepartmentView extends CustomForm2 implements Constants, Colapse {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    protected Integer objectId, departmentRefId;
    ;
    protected TeamListItem item;
    int errors = 0;
    private boolean saveAndClose = false;
    private Numbering departmentCode;
    private ReferenceLookUp name;

    private KpiSwitcher status;
    private KpiCellTree requiredSelector;
    private FlexTable table;
    private ReferenceLocale localeItem;
    ArrayList<NewTeam> departmentItems = new ArrayList<>();
    ArrayList<TeamListItem> teamListItems = new ArrayList<>();
    private AddEditLocaleView localeView;
    private TextArea2 description;
    private DatePicker startDate;
    private ArrayList<Integer> selectedEmployees;
    private EmployeeLookUpWithCode departmentLeader, departmentLeader2, departmentLeader3, departmentLeader4, departmentLeader5;
    private EmployeeLookUpWithCode createdBy;
    private TextBox email;
    private DepartmentLookUp parentDepartment;
    private SelectPanel employeesPanel;
    private int limit = 200;
    private int offset = 0;
    private int employeeCount = 0;
    private boolean isEmpty = false;
    private final Set<SelectItem> teamEmployees = new HashSet<>();
    private final ArrayList<Integer> unSelectedEmployees = new ArrayList<>();
    private String test_code_ID_name = "add_department_view_";
    private int limitSelected = 200;
    private int offsetSelected = 0;
    private int countSelected = 0;
    private boolean isEmptySelected = false;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;
    private LocationLookUpWithCode location;
    private Div locationContainer;
    private Div parentDepartmentContainer;
    private DepartmentLookUp departmentLookUp;


    public MultiAddDepartmentView() {
        super("multidepartmentadd");
        setDescription(settingsStrings.addTeam());
    }


    public MultiAddDepartmentView(Integer objectId) {
        super("edit");
        setDescription(property.getSingular(wfmStrings.editDepartment(), wfmStrings.department()));
        this.objectId = objectId;
        if (objectId != null) {
            this.objectId = objectId;
            this.test_code_ID_name = "edit_department_view_";
            setDescription(property.getSingular(wfmStrings.editDepartment(), wfmStrings.department()));
        }
    }

    public static native void selectedEmpoyeeListScrollDownEvent(MultiAddDepartmentView view) /*-{
        var timerID;
        $wnd.jQuery("div.blue-border").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.team.client.ui.view.MultiAddDepartmentView::getSelectedEmployees()();
                }, 200)
            }
        });
    }-*/;

    public static native void empoyeeListScrollDownEvent(MultiAddDepartmentView view) /*-{
        var timerID;
        $wnd.jQuery(".treePanel-class").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.team.client.ui.view.MultiAddDepartmentView::getEmployeeList()();
                }, 200)
            }
        });
    }-*/;

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        if (objectId == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            //save & close
            save.addClickHandler(event -> {
                saveAndClose = true;
                save();
            });

            //save & new
            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> {
                saveAndClose = false;
                save();
            });
            splitButton.addItem(saveAdd);
            addButton(splitButton);

        } else {
            //update
            addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, null, test_code_ID_name.concat("update_button"), event -> {
                saveAndClose = true;
                save();
            });
        }
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Department, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                MultiAddDepartmentView.super.onInitialize();
            }
        });
//        initialize();
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getWikiCode() {
        if (objectId == null) {
            return PermissionConstants.HRMS_DEPARTMENT_ADD;
        }
        return PermissionConstants.HRMS_DEPARTMENT_EDIT;
    }

    @Override
    protected void initPredefinedValues() {

    }

    protected void registerFields() {
        departmentCode = new Numbering(false);
        departmentCode.addStyleName(Constants.DEFAULT_WIDTH);
        departmentCode.addStyleName(test_code_ID_name + "employee_code");
        //name
        name = new ReferenceLookUp(DEPARTMENT_TITLES, () -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("reference|edit/" + departmentRefId);

        });
//        name.setWidth("98%");
        name.addStyleName("file--MultiAddDepartmentView");
        name.ensureDebugId(test_code_ID_name + "name");

        //status
        status = new KpiSwitcher(null, null, true);
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(test_code_ID_name + "active");

        departmentLeader = new EmployeeLookUpWithCode();
        departmentLeader.getFilterParametrs().setDepartmentId(objectId);
        departmentLeader.getFilterParametrs().setWithVacand(true);
        departmentLeader.ensureDebugId(test_code_ID_name + "department_leader");

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.getFilterParametrs().setFromDepartmentBulkUpdate(true);
        departmentLookUp.addStyleName(DEFAULT_WIDTH);
        departmentLookUp.getSuggestBox().addSelectionHandler(event -> {
            CommonService.App.get().getDepartmentsForKpiTree(departmentLookUp.getSelectedItem(), new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    requiredSelector.setItems(result);
                    LoadingPanel.loading(false);
                }
            });

        });

        //involved employees
        requiredSelector = new KpiCellTree();
        requiredSelector.setLimit(5000);
        LoadingPanel.loading(true);
        if (objectId == null) {
            CommonService.App.get().getLocationsForKpiTree(new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    requiredSelector.setItems(result);
                    LoadingPanel.loading(false);
                }
            });
        }
        drawLeftMenu();




        startDate = new DatePicker(true);
        startDate.setDate(new Date());
        startDate.ensureDebugId(test_code_ID_name + "start_date");

        //created by
        createdBy = new EmployeeLookUpWithCode();
        createdBy.selectCurrentUser();
        createdBy.ensureDebugId(test_code_ID_name + "created_by");

        location = new LocationLookUpWithCode();
        location.ensureDebugId(test_code_ID_name + "location");
        locationSelectionHandler(location);


        locationContainer = new Div();
        locationContainer.add(location);

        GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_9, new FormGroup(departmentLeader)));
        GRow secondRow = new GRow(new GColumn(GColumnEnum.COL_5, new FormGroup(wfmStrings.status(), status)));


        table = new FlexTable();
        table.setWidget(0, 0, firstRow);
        table.getColumnFormatter().setWidth(0, "90%");
        table.setWidget(0, 1, secondRow);
        table.getColumnFormatter().setWidth(1, "10%");
        table.ensureDebugId(test_code_ID_name);


        //description
        description = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.addStyleName("file--MultiAddDepartmentView");
        description.ensureDebugId(test_code_ID_name + "description");
        //start date


        //email
        email = new TextBox();
//        email.addStyleName(DEFAULT_WIDTH);
        email.ensureDebugId(test_code_ID_name + "email");
        // Parent Team
        parentDepartment = new DepartmentLookUp(objectId, true);
//        parentDepartment.addStyleName(DEFAULT_WIDTH);
        parentDepartment.ensureDebugId(test_code_ID_name + "parent_team");
        //department leader
        selectedEmployees = new ArrayList<>();
        parentDepartmentContainer = new Div();
        parentDepartmentContainer.add(parentDepartment);


        departmentLeader2 = new EmployeeLookUpWithCode();
//        departmentLeader2.getFilterParametrs().setDepartmentId(objectId);
//        departmentLeader2.addStyleName(DEFAULT_WIDTH);
        departmentLeader2.ensureDebugId(test_code_ID_name + "department_leader2");

        departmentLeader3 = new EmployeeLookUpWithCode();
//        departmentLeader3.getFilterParametrs().setDepartmentId(objectId);
//        departmentLeader3.addStyleName(DEFAULT_WIDTH);
        departmentLeader3.ensureDebugId(test_code_ID_name + "department_leader3");

        departmentLeader4 = new EmployeeLookUpWithCode();
//        departmentLeader4.getFilterParametrs().setDepartmentId(objectId);
//        departmentLeader4.addStyleName(DEFAULT_WIDTH);
        departmentLeader4.ensureDebugId(test_code_ID_name + "department_leader4");

        departmentLeader5 = new EmployeeLookUpWithCode();
//        departmentLeader5.getFilterParametrs().setDepartmentId(objectId);
//        departmentLeader5.addStyleName(DEFAULT_WIDTH);
        departmentLeader5.ensureDebugId(test_code_ID_name + "department_leader5");

        if (objectId == null) {
            departmentLeader.setBeforeSearch(() -> setFilterParams(departmentLeader));
//            departmentLeader2.setBeforeSearch(() -> setFilterParams(departmentLeader2));
//            departmentLeader3.setBeforeSearch(() -> setFilterParams(departmentLeader3));
//            departmentLeader4.setBeforeSearch(() -> setFilterParams(departmentLeader4));
//            departmentLeader5.setBeforeSearch(() -> setFilterParams(departmentLeader5));
        }

        //team employees
        final TableColumn[] assignColumns = new TableColumn[2];
        assignColumns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee(), 145);
        assignColumns[1] = new TableColumn(wfmStrings.action(), wfmStrings.action(), 15);
        employeesPanel = new SelectPanel(assignColumns);
        employeesPanel.setDefaultSettings();
        employeesPanel.setSelectPanelAction(new ISelectPanel() {
            @Override
            public void removeItem(SelectItem item) {
                teamEmployees.clear();
                teamEmployees.add(item);
                selectedEmployees.remove(item.getId());
                clearLeaderLookUpsOracle();
                saveEmployeeTeam(teamEmployees, false);
            }

            @Override
            public void addItem(Set<SelectItem> selectedItems, boolean isSelected) {
                if (!isSelected) {
                    unSelectedEmployees.addAll(selectedItems.stream().map(SelectItem::getId).collect(Collectors.toList()));
                } else {
                    selectedItems.forEach(item -> {
                        unSelectedEmployees.remove(item.getId());
                    });
                }
                selectedEmployees.addAll(selectedItems.stream().map(SelectItem::getId).collect(Collectors.toList()));
                if (isSelected) {
                    saveEmployeeTeam(selectedItems, isSelected);
                }
            }
        });
        employeesPanel.ensureDebugId(test_code_ID_name + "members");

        addFieldsToForm();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, MultiAddDepartmentView.this, (sender, args) -> reloadAssignees());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMENT_EDIT, MultiAddDepartmentView.this, (sender, args) -> reloadAssignees());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_TREE_WIDGET_REFRESH, MultiAddDepartmentView.this, (sender, args) -> getEmployeeList(true));
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }


    private void setFilterParams(LookUp lookUp) {
        if (selectedEmployees.size() > 0) {
            lookUp.getFilterParametrs().setDepartmentId(null);
            lookUp.getFilterParametrs().setIDsOnly(true);
            lookUp.getFilterParametrs().setObjectIDs(selectedEmployees);
        } else {
            lookUp.getFilterParametrs().setDepartmentId(0);
        }
    }

    private void drawLeftMenu() {
        requiredSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };

                selectedDataGrid.addColumn(employee, wfmStrings.locations());
                selectedDataGrid.setColumnWidth(employee, 40, Style.Unit.PCT);
                //remove
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });
    }

    private void clearLeaderLookUpsOracle() {
        departmentLeader.clearOracleItems();
        departmentLeader2.clearOracleItems();
        departmentLeader3.clearOracleItems();
        departmentLeader4.clearOracleItems();
        departmentLeader5.clearOracleItems();
    }

    private void reloadAssignees() {
        employeesPanel.getTreeSelect().clearTree();
        employeesPanel.getTable().clear();
        limit = 200;
        limitSelected = 200;
        offset = 0;
        offsetSelected = 0;
        employeeCount = 0;
        employeeCount = 0;
        countSelected = 0;
        isEmpty = false;
        isEmptySelected = false;
        getEmployeeList();
    }

    private void getSelectedEmployees() {
        countSelected = 0;
        if (!isEmptySelected) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(offsetSelected);
            fp.setLimit(limitSelected);
            fp.setDepartmentId(objectId);
            fp.setCorporate(false);
            fp.setResignedEmployeesIncluded(false);
            if (offsetSelected != 0) {
                LoadingPanel.loading(true);
            }
            ReportService.App.get().getEmployeesMap(fp, LayoutRPC.DEPARTMENT_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        countSelected = employeesPanel.addSelectedItems(items);
                        if (countSelected < 200) {
                            isEmptySelected = true;
                        }
                    } else {
                        isEmptySelected = true;
                    }
                }
            });
            offsetSelected += limitSelected;
        }
    }

    public void getEmployeeList() {
        getEmployeeList(false);
    }

    public void getEmployeeList(boolean search) {
        employeeCount = 0;
        int limitAll = 10000;
        if (!isEmpty && limit < limitAll) {
            ListingFilterParameter fp = new ListingFilterParameter();
            if (search) {
                limit = limitAll;
            }
            fp.setStart(offset);
            fp.setLimit(limit);
            fp.setRelationToID(objectId);
            fp.setCorporate(false);
            fp.setType(0);
            fp.setResignedEmployeesIncluded(false);
            LoadingPanel.loading(true);
            ReportService.App.get().getEmployeesMap(fp, LayoutRPC.DEPARTMENT_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        employeeCount = employeesPanel.addItems(items);
                        employeesPanel.expandTreeView();
                        isEmpty = employeeCount < 200;
                    } else {
                        isEmpty = true;
                    }
                }
            });
            offset += limit;
        }
    }

    private void saveEmployeeTeam(Set<SelectItem> teamEmployees, boolean isChecked) {
        if (objectId != null) {
            LoadingPanel.loading(true);

            HashSet<Integer> ids = teamEmployees.stream().map(SelectItem::getId).collect(Collectors.toCollection(HashSet::new));

            DepartmentService.App.get().saveEmployeeDepartment(ids, objectId, isChecked, true, false, new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void o) {
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    public void addFieldsToForm() {

        if (objectId != null) {
            if (formPropertyMap != null && formPropertyMap.get(DEPARTMENT_LOOKUP) != null) {
                addField(DEPARTMENT_LOOKUP, departmentLookUp, getTitle(formPropertyMap.get(DEPARTMENT_LOOKUP).isChanged() ? formPropertyMap.get(DEPARTMENT_LOOKUP).getTitle() : wfmStrings.department(), formPropertyMap.get(DEPARTMENT_LOOKUP).isRequired()), false,
                        formPropertyMap.get(DEPARTMENT_LOOKUP).isInformation());
                departmentLookUp.setEnabled(!formPropertyMap.get(DEPARTMENT_LOOKUP).isDisabled());
            } else {
                addField(DEPARTMENT_LOOKUP, departmentLookUp, getTitle(wfmStrings.department(), true));
            }
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME) != null) {
            addField(CustomFormConstants.DEPARTMENT_NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME).getTitle() : property.getSingular(wfmStrings.name(), wfmStrings.name()), formPropertyMap.get(CustomFormConstants.DEPARTMENT_NAME).isRequired()));
        } else {
            addField(CustomFormConstants.DEPARTMENT_NAME, name, getTitle(property.getSingular(wfmStrings.name(), wfmStrings.name()), true));
        }


        // DEPARTMENT_PARENT rename "REPORTS_TO"
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT) != null) {
            addField(CustomFormConstants.DEPARTMENT_PARENT, parentDepartmentContainer, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).getTitle() : wfmStrings.reportsTo(), formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).isRequired()));
            parentDepartment.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).isDisabled());
        } else {
            addField(CustomFormConstants.DEPARTMENT_PARENT, parentDepartmentContainer, getTitle(wfmStrings.reportsTo(), true));
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER) != null) {
            addField(CustomFormConstants.DEPARTMENT_LEADER, table, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).getTitle() : property.getSingular(wfmStrings.departmentLeader(), wfmStrings.department()), formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).isRequired()));
            departmentLeader.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER).isDisabled());
        } else {
            addField(CustomFormConstants.DEPARTMENT_LEADER, table, getTitle(property.getSingular(wfmStrings.departmentLeader(), wfmStrings.department()), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION) != null) {
            addField(CustomFormConstants.DEPARTMENT_LOCATION, requiredSelector, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).isRequired()));
            location.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).isDisabled());
        } else {
            addField(CustomFormConstants.DEPARTMENT_LOCATION, requiredSelector, null);
        }

        getCustomFieldUtil().drawCustomFields(this, objectId, false);

        show();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MULTI_DEPARTMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected void getDataToFillFields() {
        DepartmentService.App.get().getTeamForEdit(objectId, "EDIT", new AbstractAsyncCallback<TeamListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final TeamListItem o) {
                LoadingPanel.loading(false);
                item = o;
                fillFieldWithValue();
                localeItem = o.getLocaleItem();
                getEmployeeList();
                empoyeeListScrollDownEvent(MultiAddDepartmentView.this);
                getCustomFieldUtil().fillCustomFieldsWithData(o.getCustomFieldItems());
                if (objectId != null) {
                    getSelectedEmployees();
                    selectedEmpoyeeListScrollDownEvent(MultiAddDepartmentView.this);
                }
            }
        });
    }

    public void fillFieldWithValue() {
        departmentRefId = item.getDepartmentfId();
        if (item.getDepartmentName() != null) {
            name.setSelected(item.getDepartmentName());
        }
        description.setText(item.getDescription());
        startDate.setDate(item.getStartDate());

        if (item.getNumberData() != null) {
            departmentCode.setNumberData(item.getNumberData());
        }

        if (item.getCreator() != null) {
            createdBy.setSelected(item.getCreator());
        }

        if (item.getLocation() != null) {
            location.setSelected(item.getLocation());
        }

        email.setText(item.getEmail());
        if (item.getParentDepartment() != null) {
            parentDepartment.setSelected(item.getParentDepartment());
        }

        if (item.getLeaderId() != null) {
            departmentLeader.setSelected(new SelectItem(item.getLeaderId(), item.getLeader()));
        }

        if (item.getLeaderId2() != null) {
            departmentLeader2.setSelected(new SelectItem(item.getLeaderId2(), item.getLeader2()));
        }

        if (item.getLeaderId3() != null) {
            departmentLeader3.setSelected(new SelectItem(item.getLeaderId3(), item.getLeader3()));
        }

        if (item.getLeaderId4() != null) {
            departmentLeader4.setSelected(new SelectItem(item.getLeaderId4(), item.getLeader4()));
        }

        if (item.getLeaderId5() != null) {
            departmentLeader5.setSelected(new SelectItem(item.getLeaderId5(), item.getLeader5()));
        }
        clearLeaderLookUpsOracle();

        if (objectId == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION).getDefaultValue() != null) {
            description.setText(formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                startDate.setDate(currentDate);
            } else {
                try {
                    startDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).getDefaultValue() != null) {
            createdBy.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).getDefaultValue() != null) {
            location.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).getDefaultValue() != null) {
            email.setText(formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).getDefaultValue() != null) {
            parentDepartment.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).getDefaultValue()));
        }
    }

    protected void save() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        departmentItems.clear();
        teamListItems.clear();
        LoadingPanel.loading(true);
        for (KpiTreeInfo selectedDatum : requiredSelector.getSelectedData()) {
            final NewTeam team = new NewTeam();
            if (objectId == null) {
                team.setName(name.getText());
                team.setDepartmentName(name.getSelectedItem());
                team.setDescription(description.getText());
                team.setStartDate(startDate.getDate());
                team.setEmail(email.getText());
                team.setActive(status.getValue());
                if (departmentLeader.getSelectedItem() != null) {
                    team.setLeader(departmentLeader.getSelectedItemID());
                }
                team.setParent(parentDepartment.getSelectedItem());
                team.setNumberData(departmentCode.getNumberData(true));
                team.setDepartmentCode(departmentCode.getNumberData(true).getNumberString());
                team.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
                team.setLocation(new SelectItem(selectedDatum.getId(), selectedDatum.getName()));
                localeItem = localeView != null ? localeView.getLocaleItem() : localeItem;
                team.setReferenceLocale(localeItem);
                departmentItems.add(team);
            } else {
                item.setObjectID(selectedDatum.getId());
                item = setValuesToRPC(item);
                teamListItems.add(item);
            }
        }
        DepartmentService.App.get().createTeamItems(departmentItems, teamListItems, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
                enableButton(true);
            }

            @Override
            public void onSuccess(Void result) {
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), settingsStrings.dependent1()), Info.Type.INFO);
                LoadingPanel.loading(false);
                onShellOk();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPARTMENT_ADD, item, MultiAddDepartmentView.this);
            }
        });
    }


    public void onShellOk() {
        if (saveAndClose) {
            closeTab();
            enableButton(false);
        } else if (!saveAndClose) {
            closeTab("department|add/add");
        } else {
            reInit();
            enableButton(true);
        }
    }

    public void reInit() {
        objectId = null;
        saveAndClose = false;
        clear();
        initForm();
        registerFields();
    }

    private TeamListItem setValuesToRPC(TeamListItem item) {
        item.setNumberData(departmentCode.getNumberData(true));
        item.setDepartmentCode(departmentCode.getNumberData(true).getNumberString());

        item.setName(name.getText());
        item.setDepartmentName(name.getSelectedItem());
        item.setActive(status.getValue());
        item.setDescription(description.getText());
        item.setStartDate(startDate.getDate());

        if (createdBy.getSelectedItem() != null) {
            item.setCreator(createdBy.getSelectedItem());
        }

        if (location.getSelectedItem() != null) {
            item.setLocation(location.getSelectedItem());
        }
        item.setEmail(email.getText());

        if (departmentLeader.getSelectedItem() != null) {
            item.setLeader(departmentLeader.getSelectedItem().getName());
            item.setLeaderId(departmentLeader.getSelectedItem().getId());
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER)) {
            if (departmentLeader2.getSelectedItem() != null) {
                item.setLeader2(departmentLeader2.getSelectedItem().getName());
                item.setLeaderId2(departmentLeader2.getSelectedItem().getId());
            }

            if (departmentLeader3.getSelectedItem() != null) {
                item.setLeader3(departmentLeader3.getSelectedItem().getName());
                item.setLeaderId3(departmentLeader3.getSelectedItem().getId());
            }

            if (departmentLeader4.getSelectedItem() != null) {
                item.setLeader4(departmentLeader4.getSelectedItem().getName());
                item.setLeaderId4(departmentLeader4.getSelectedItem().getId());
            }

            if (departmentLeader5.getSelectedItem() != null) {
                item.setLeader5(departmentLeader5.getSelectedItem().getName());
                item.setLeaderId5(departmentLeader5.getSelectedItem().getId());
            }
        }
        item.setParentDepartment(parentDepartment.getSelectedItem());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        return item;
    }

    private boolean validate() {
        clearErrorStyle();
        errors = 0;
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(createdBy, !Validation.validateLookUpRequired(name));
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_DESCRIPTION).isRequired()) {
            errors += markAsError(description, !Validation.validateTextAreaRequired(description));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).isRequired()) {
            errors += markAsError(startDate, !Validation.validateDate(startDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_CREATED_BY).isRequired()) {
            errors += markAsError(createdBy, !Validation.validateLookUpRequired(createdBy));
        }

        if (formPropertyMap != null && formPropertyMap.get("LOCATION") != null && formPropertyMap.get("LOCATION").isRequired()) {
            errors += markAsError(location, !Validation.validateLookUpRequired(location));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).isRequired()) {
            errors += markAsError(email, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).getTitle() : wfmStrings.email(), email, formPropertyMap.get(CustomFormConstants.DEPARTMENT_EMAIL).getMinChar()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER2) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER2).isRequired()) {
                errors += markAsError(departmentLeader2, !Validation.validateLookUpRequired(departmentLeader2));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER3) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER3).isRequired()) {
                errors += markAsError(departmentLeader3, !Validation.validateLookUpRequired(departmentLeader3));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER4) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER4).isRequired()) {
                errors += markAsError(departmentLeader4, !Validation.validateLookUpRequired(departmentLeader4));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER5) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LEADER5).isRequired()) {
                errors += markAsError(departmentLeader5, !Validation.validateLookUpRequired(departmentLeader5));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_PARENT).isRequired()) {
            errors += markAsError(parentDepartment, !Validation.validateLookUpRequired(parentDepartment));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT_LOCATION).isRequired()) {
            errors += markAsError(location, !Validation.validateLookUpRequired(location));
        }

        if (employeesPanel.getSelectedItems() != null && employeesPanel.getSelectedItems().length > 0) {
            errors += markAsError(departmentLeader, !Validation.validateLookUpRequired(departmentLeader));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.DEPARTMENT_LIST;
    }

    private void updateLocation(Integer departmentId) {
        DepartmentService.App.get().getLocationByDepartmentId(departmentId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                location.removeFromParent();
                locationContainer.remove(location);
                location = new LocationLookUpWithCode();
                locationSelectionHandler(location);
                location.setSelected(result);
                locationContainer.add(location);
            }
        });
    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode) {
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            parentDepartment.removeFromParent();
            parentDepartmentContainer.remove(parentDepartment);
            parentDepartment = new DepartmentLookUp();
            parentDepartment.getFilterParametrs().setLocationId(location.getSelectedItemID());
            parentDepartmentContainer.add(parentDepartment);
        });

        locationLookUpWithCode.getTextBox().addKeyDownHandler(e -> {
            parentDepartment.removeFromParent();
            parentDepartmentContainer.remove(parentDepartment);
            parentDepartment = new DepartmentLookUp();
            parentDepartment.getSuggestBox().addSelectionHandler(event -> updateLocation(parentDepartment.getSelectedItemID()));
            parentDepartmentContainer.add(parentDepartment);
        });

    }
}
