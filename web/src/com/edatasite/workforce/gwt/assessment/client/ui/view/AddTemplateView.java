package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.BoolItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TblSmartColFactory;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectCallback;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectShell;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AddTemplateView extends CustomForm implements Colapse {

    public static String ADD_TEMPLATE_VIEW = "ADD_TEMPLATE_VIEW";

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private HTML counterCompetencyWeights;
    private TextBox name;
    private final Set<Integer> ratedSkillIds = new HashSet<>();
    private Integer templateID;
    private boolean isCopyTemplate;
    private MultiSelectDepartmentLookUp departmentLookUp;
    private EmployeeLookUpWithCode ownerLookUp;

    private KpiDataGrid<WfmTreeItem> dataGrid;
    private ListDataProvider<WfmTreeItem> dataProvider;

    private String test_code_ID_name = "add_appraisal_template_view_";

    public static final ProvidesKey<WfmTreeItem> KEY_PROVIDER = item -> item == null ? null : item.getId();


    public AddTemplateView(Integer templateID) {
        this.templateID = templateID;
        this.test_code_ID_name = "edit_appraisal_template_view_";
    }

    public AddTemplateView(Integer templateID, boolean isCopyTemplate) {
        this.templateID = templateID;
        this.isCopyTemplate = isCopyTemplate;
        this.test_code_ID_name = "edit_appraisal_template_view_";
    }

    @Override
    public String getIconStyle() {
        return "assessment app-template-list";
    }

    @Override
    protected void addButtons() {
        if (templateID == null || isCopyTemplate) {
            //save and close
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            save.addClickHandler(event -> save(true));

            //save and new
            MaterialLink saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
            saveAndNewButton.addClickHandler(event -> save(false));

            splitButton.addItem(saveAndNewButton);
            addButton(splitButton);
        } else {
            //update
            addButton(wfmStrings.update(), null, (test_code_ID_name + "update_button"), event -> {
                //update logic
                save(true);
            });
        }
        addButton(hrmsStrings.addCompetencies(), WfmButton2.BTN_PRIMARY, event -> {
            TreeSelectCallback treeSelectCallback = (parent, command) -> AssessmentService.App.get().getSkills(parent.getItem().getId(), new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
                @Override
                public void failure(Throwable caught) {
                    command.execute();
                }

                @Override
                public void success(LinkedList<WfmTreeItem> result) {
                    parent.addItems(result);
                    command.execute();
                }
            });
            final TreeSelectShell shell = new TreeSelectShell(wfmStrings.skills(), treeSelectCallback);
//            shell.setSize(400, 370);
            shell.getTreeSelect().setSearchText(wfmStrings.skills());
            shell.getTreeSelect().hideAvailablityCheckBox();
            shell.getTreeSelect().getSearchPanel().setVisible(false);
            shell.getTreeSelect().getTickAll().setVisible(false);

            shell.addStyleName("skillsPopup");
            shell.getTreeSelect().getPanel().addStyleName("skillsPopupTreeSelectPanel");

            shell.setOnItemSelected(items -> {
                for (WfmTreeItem item : items) {
                    if (item.getId() != null && item.getId() > 0) {
                        addContact(item);
                    }
                }
            });
            shell.open();
            LoadingPanel.loading(true, shell);
            AssessmentService.App.get().getGroups(new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(LinkedList<WfmTreeItem> result) {
                    shell.addRootItems(result);
                    LoadingPanel.loading(false);
                }
            });
        });
    }

    @Override
    protected void getDataToFillFields() {
        reInitOn();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.APPRAISAL_TEMPLATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void addContact(WfmTreeItem contact) {
        List<WfmTreeItem> contacts = dataProvider.getList();
        contacts.add(contact);
    }

    private void addDataDisplay(HasData<WfmTreeItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void clearFields() {
        templateID = null;
        name.setText(null);
        dataProvider.getList().clear();
    }

    private void initialize() {
        //template name
        name = new TextBox();
        name.ensureDebugId(test_code_ID_name + "template_name");
        //competencies table
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);

        departmentLookUp = new MultiSelectDepartmentLookUp();
        departmentLookUp.setWidth("100%");
        departmentLookUp.ensureDebugId(test_code_ID_name + "department_look_up");

        ownerLookUp = new EmployeeLookUpWithCode();
        ownerLookUp.setWidth("100%");
        ownerLookUp.ensureDebugId(test_code_ID_name + "owner_look_up");

        initEditTableColumns();
        dataProvider.refresh();
        //add competencies link

        counterCompetencyWeights = new HTML();
        counterCompetencyWeights.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        counterCompetencyWeights.ensureDebugId(test_code_ID_name + "count_of_competency_weight");

        addTitleField(CustomFormConstants.DETAILS, wfmStrings.manageTemplates());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DEPARTMENT, departmentLookUp, getTitle(wfmStrings.department(), false));
        addField(CustomFormConstants.OWNER, ownerLookUp, getTitle(wfmStrings.owner(), false));
        addField(CustomFormConstants.COMPETENCIES, dataGrid, getTitle(wfmStrings.skills(), true));
        addField(CustomFormConstants.TOTAL_WEIGHT, counterCompetencyWeights, null);

        show();
    }

private void initEditTableColumns() {
    // competency name - create from factory (TblSmartColFactory -> TblSmartTxtCell)
    Column<WfmTreeItem, String> competencyName = TblSmartColFactory.create(WfmTreeItem::getName);
    dataGrid.addColumn(competencyName, hrmsStrings.competencyName());

    // description - the same with factory
    Column<WfmTreeItem, String> description = TblSmartColFactory.create(WfmTreeItem::getDescription);
    dataGrid.addColumn(description, wfmStrings.description());

    // weight
    TextInputCell textInputCell = new TextInputCell();
    final Column<WfmTreeItem, String> weight = new Column<WfmTreeItem, String>(textInputCell) {
        @Override
        public String getValue(WfmTreeItem object) {
            return object.getDoubleValue() != null ? numberFormat.format(object.getDoubleValue()) : "";
        }
    };
    textInputCell.setWidth("40px");
    weight.setFieldUpdater((index, object, value) -> {
        try {
            if (!value.equals("0") && !value.equals("")) {
                TextBox textBox = new TextBox();
                textBox.setText(value);
                Validation.numberValidation(textBox);
                object.setDoubleValue(Double.valueOf(textBox.getText()));
            } else {
                object.setDoubleValue(Double.valueOf(value));
            }
        } catch (NumberFormatException ex) {
            object.setDoubleValue((double) 0f);
        }
        recalculateCompetencyWeights();
    });
    dataGrid.addColumn(weight, wfmStrings.weight());
    dataGrid.setColumnWidth(weight, "56px");
    //remove action
    Column<WfmTreeItem, String> remove = new Column<WfmTreeItem, String>(new SimpleLinkCell()) {
        @Override
        public String getValue(final WfmTreeItem object) {
            return wfmStrings.delete();
        }
    };
    remove.setFieldUpdater((index, object, value) -> {
        List<WfmTreeItem> skills = dataProvider.getList();
        skills.remove(object);
        recalculateCompetencyWeights();
    });
    dataGrid.addColumn(remove, wfmStrings.delete());
    dataGrid.setColumnWidth(remove, "56px");
}

    public void loadDataForEdit() {
        AssessmentService.App.get().getTemplate(templateID, new AbstractAsyncCallback<TemplateItem>() {
            @Override
            public void success(TemplateItem templateItem) {
                LoadingPanel.loading(false);
                if (templateItem == null) {
                    dataProvider.getList().clear();
                    addDataDisplay(dataGrid);
                } else {
                    LinkedList<WfmTreeItem> treeItems = templateItem.getItems();
                    supplyProvider(treeItems);
                    addDataDisplay(dataGrid);
                    name.setText(templateItem.getName());
                    if (templateItem.getDepartment() != null) {
                        departmentLookUp.setSelectedItems(templateItem.getDepartment());
                    }
                    if (templateItem.getOwner() != null) {
                        ownerLookUp.setSelected(templateItem.getOwner());
                    }
                }
                recalculateCompetencyWeights();
            }
        });
    }

    private void onShellOk(boolean close) {
        clearFields();
        if (close) {
            closeTab();
        }
    }

    private void recalculateCompetencyWeights() {
        counterCompetencyWeights.setHTML("");
        int counter = 0;
        List<WfmTreeItem> listItems = dataProvider.getList();
        for (WfmTreeItem wfmTreeItem : listItems) {
            if (wfmTreeItem.getDoubleValue() != null) {
                counter += wfmTreeItem.getDoubleValue();
            }
        }
        counterCompetencyWeights.setHTML(hrmsStrings.overallCompetencyWeight() + ": <b " + (counter > 100 ? "style='color:red;'>" : "style='color:darkgreen;'>") + numberFormat.format(counter) + "</b>");
    }

    private void reInitOn() {
        if (templateID != null) {
            loadDataForEdit();
        } else {
            LoadingPanel.loading(false);
            List<WfmTreeItem> tables = dataProvider.getList();
            tables.clear();
            addDataDisplay(dataGrid);
        }
    }

    public void save(final boolean close) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }

        List<WfmTreeItem> items = dataProvider.getList();
        BoolItem[] ids = new BoolItem[items.size()];
        for (int i = 0; i < items.size(); i++) {
            BoolItem boolItem = new BoolItem(items.get(i).getId(), ratedSkillIds.contains(items.get(i).getId()));
            boolItem.setWeight(items.get(i).getDoubleValue());
            ids[i] = boolItem;
            ids[i].setChecked(true);
        }
        final String successMessage;
        final String failMessage;
        if (templateID == null || isCopyTemplate) {
            successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.template());
            failMessage = hrmsStrings.failedToAddTemplate();
        } else {
            successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.template());
            failMessage = hrmsStrings.failedToUpdateTemplate();
        }

        LoadingPanel.loading(true);
        AssessmentService.App.get().saveTemplate(isCopyTemplate ? null : templateID, name.getText(), ids, departmentLookUp.getSelectedItems(),ownerLookUp.getSelectedItem(),new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(successMessage, Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TEMPLATE_ADD_DELETE, result, AddTemplateView.this);
                onShellOk(close);
            }
        });
    }

    private void supplyProvider(LinkedList<WfmTreeItem> reportResults) {
        List<WfmTreeItem> tables = dataProvider.getList();
        tables.clear();
        tables.addAll(reportResults);
//        Collections.addAll(tables, reportResults.toArray(new WfmTreeItem[reportResults.size()]));
    }

    public boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(name, !Validation.validateTextBoxRequired(name));
        errors += markAsError(dataGrid, validSkillError());

        if (errors > 0) {
            Info.show(hrmsStrings.sureSelectAllRequiredData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public boolean validSkillError() {
        if (dataProvider.getList().size() == 0) {
            dataGrid.setTitle(hrmsStrings.erorSkill());
        } else {
            dataGrid.setTitle("");
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
}
