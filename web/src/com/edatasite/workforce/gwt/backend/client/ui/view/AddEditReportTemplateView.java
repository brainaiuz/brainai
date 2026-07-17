package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.CheckListBox;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 22.10.2011
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */

public class AddEditReportTemplateView extends View {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final CoreServiceAsync coreService = CoreService.App.get();
    private final BackendStrings backendStrings = BackendStrings.App.get();

    private Integer objectID;
    private Integer stepID;
    private Integer[] companyIDs;
    private SelectPanel sharedsTree;
    private final TableColumn[] columns = new TableColumn[2];
    private KpiCheckBox isCustom;
    private KpiCheckBox library;
    private KpiCheckBox isSimplified;
    private WfmForm companyForm;
    private TextBox name;
    private DataListBox category;
    private TextArea body;
    private WfmButton2 saveButton;
    private WfmButton2 cancel;
    private WfmForm form;
    private WfmForm.Field nameField;
    private WfmForm.Field categoryField;
    private WfmForm.Field bodyField;
    private CheckListBox roleCheckListBox;
    private KpiCheckBox checkAll = null;
    private KpiCheckBox byDefault = null;

    public AddEditReportTemplateView() {
        super("addreportxmltemplate", "Add Report Template");
        super.setDescription(backendStrings.addReportTemplate());
    }

    public AddEditReportTemplateView(Integer objectID) {
        super("addreportxmltemplate", "Edit Report Template");
        super.setDescription(backendStrings.editReportTemplate());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        name = new TextBox();
        name.setWidth("300px");
        category = new DataListBox();
        category.setWidth("300px");
        body = new TextArea();
        body.setHeight("16em");
        isCustom = new KpiCheckBox(wfmStrings.custom());
        isCustom.addValueChangeHandler(booleanValueChangeEvent -> {
            companyForm.setVisible(isCustom.getValue());
        });
        library = new KpiCheckBox(wfmStrings.isLibrary());
        isSimplified = new KpiCheckBox(backendStrings.isSimplifiedReportTemplate());
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save());
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, evet -> closeTab());
        form = new WfmForm(new String[]{"10%", "90%"});
        form.addStyleName("AddEditReportTemplateView");
        checkAll = new KpiCheckBox(wfmStrings.all());
        byDefault = new KpiCheckBox(wfmStrings.default2());
        roleCheckListBox = new CheckListBox();

        nameField = form.addField(wfmStrings.name(), name, true);
        categoryField = form.addField(wfmStrings.category(), category, true);
        bodyField = form.addField(wfmStrings.text(), body, true);
        form.addField(wfmStrings.custom(), isCustom);
        form.addField(wfmStrings.isLibrary(), library);
        form.addField(backendStrings.isSimplifiedReportTemplate(), isSimplified);

        columns[0] = new TableColumn("company", wfmStrings.company(), 150);
        columns[1] = new TableColumn("delete", wfmStrings.action(), 50);
        sharedsTree = new SelectPanel(columns);
//        sharedsTree.setTreePanelWidth(190);
//        sharedsTree.setHeight(240);
//        sharedsTree.setSearchBoxWidth("132px");
        sharedsTree.addStyleName("AddEditReportTemplateView__sharedsTree");
        sharedsTree.setSearchText(wfmStrings.searchCompany());
//        sharedsTree.setTableWidth(200);
        sharedsTree.getTable().addStyleName("AddEditReportTemplateView__sharedsTree__actions");
        sharedsTree.hideAvailablityCheckBox();
        LoadingPanel.loading(true);
        coreService.getCompanies(objectID, new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<TeamEmployees> result) {
                loadTree(result);
            }
        });
        companyForm = new WfmForm();
        companyForm.setVisible(false);
        companyForm.addField(wfmStrings.company(), sharedsTree, true);

        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.add(form);
        mainPanel.add(companyForm);
        add(mainPanel);

        coreService.getReportTemplate(objectID, new AbstractAsyncCallback<ReportTemplateItem>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ReportTemplateItem result) {
                if (result != null) {
                    stepID = result.getStepId();
                    name.setText(result.getName());
                    body.setText(result.getBody());
                    library.setValue(result.getLibrary());
                    isCustom.setValue(result.getCustom());
                    isSimplified.setValue(result.getSimplified());
                    if (result.getCategories() != null && result.getCategories().length > 0) {
                        category.setItems(result.getCategories());
                    }
                    category.setSelected(result.getCategoryId());

                    companyForm.setVisible(isCustom.getValue());
                }
            }
        });
        getRoles();
        return null;
    }

    private void getRoles() {
        checkAll.addValueChangeHandler(b -> {
            for (SelectItem item : roleCheckListBox.getItems()) {
                roleCheckListBox.setSelected(item.getDescription(), b.getValue());
            }
        });
        byDefault.addValueChangeHandler(b -> {
            for (SelectItem item : roleCheckListBox.getItems()) {
                if ((Constants.ADMIN_CODE +
                        "|" + Constants.ACCOUNTANT_CODE +
                        "|" + Constants.DR_CODE +
                        "|" + Constants.SALESMAN_CODE).contains(item.getDescription()))
                    roleCheckListBox.setSelected(item.getDescription(), b.getValue());
            }
        });
        CoreService.App.get().getTemplateRoles(null, objectID, new AsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> selectItems) {
                        Widget panel = roleCheckListBox.init(selectItems);
//                        panel.setSize("300px", "100px");
                        panel.setHeight("140px");
                        VerticalPanel verticalPanel = new VerticalPanel();
                        verticalPanel.setStyleName("gwt-TabPanelBottom");
                        verticalPanel.add(checkAll);
                        verticalPanel.add(byDefault);
                        verticalPanel.add(panel);
                        form.addField(wfmStrings.role(), verticalPanel);
                        form.addButton(saveButton);
                        form.addButton(cancel);
                    }
                }
        );
    }

    private void loadTree(ArrayList<TeamEmployees> companyList) {
        TreeSelect.setTickAllVisible(companyList.size() != 0);
        for (TeamEmployees teamEmployee : companyList) {
            sharedsTree.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
        }
        for (int i = 0; i < sharedsTree.getTree().getItemCount(); i++) {
            NTreeSelectItem parent = (NTreeSelectItem) sharedsTree.getTree().getItem(i);
            for (int j = 0; j < parent.getChildCount(); j++) {
                NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                for (WfmTreeItem company : companyList.get(0).getMembers()) {
                    if (child.getItem().getId().equals(company.getId()) && company.isChecked()) {
                        child.setChecked(true);
                        sharedsTree.onTreeItemSelection(child, null);
                        break;
                    }
                }
            }
        }
        sharedsTree.expandTreeView();
        LoadingPanel.loading(false);
    }

    private void save() {
        if (!validate()) {
            return;
        }

        Integer categoryID = category.getSelectedItem().getId();
        LoadingPanel.loading(true);
        if (isCustom.getValue()) {
            companyIDs = sharedsTree.getSelectedItems();
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSelected(isCustom.getValue());
        filterParameter.setName(name.getText());
        filterParameter.setDescription(body.getText());
        filterParameter.setCategoryID(categoryID);
        filterParameter.setCompaines(companyIDs);
        filterParameter.setObjectId(objectID);
        filterParameter.setStepID(stepID);
        filterParameter.setLibrary(null != library.getValue() && library.getValue());
        filterParameter.setDeleted(true);
        filterParameter.setColumnsOfListing(new ArrayList<>());
        filterParameter.setIsSimpilifiedReportTemplate(isSimplified.getValue());

        ArrayList<String> checkedRoles = new ArrayList<>();
        for (SelectItem role : roleCheckListBox.getSelectedItems()) {
            checkedRoles.add(role.getDescription());
        }
        if (checkedRoles.size() > 0) {
            filterParameter.setColumnsOfListing(checkedRoles);
        }
        saveButton.setEnabled(false);
        cancel.setEnabled(false);
        coreService.saveOrUpdateReportTemplate(filterParameter, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                cancel.setEnabled(true);
            }

            @Override
            public void success(Integer result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.reportTemplate()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORT_TEMPLATE_ADD_EDIT, result, AddEditReportTemplateView.this);
                LoadingPanel.loading(false);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(category, categoryField, "")) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(body, bodyField)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
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
