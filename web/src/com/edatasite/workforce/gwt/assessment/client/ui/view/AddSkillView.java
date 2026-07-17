package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillGroupItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillList;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MultiLanguageRichEditorWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: Admin
 * Date: 14.07.2009
 * Time: 15:59:16
 */

/**
 * If do you want to change this view you need to be careful for localizations
 * Firstly you need to read a code of the view
 * How to work localization option after that you can change. Becouse in this view i can not to find
 * another option to relate TextBox and LocalizationButton.
 * <p>
 * for this option i create inner class called SkillLocalization. This class to save rowId(as "code"),
 * localized values of name and description. When new row added all items code in List<SkillLocalization> incremented rowId
 * When deleted row all items code in List<SkillLocalization> dicremented
 */
public class AddSkillView extends CustomForm implements Colapse, Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final int COLUMNS_COUNT = 3;
    private static final String COMPETENCY_NAME = "COMPETENCY_NAME";
    private static final String COMPETENCY_DESCRIPTION = "COMPETENCY_DESCRIPTION";
    private static final String COMPETENCY_WEIGHT = "COMPETENCY_WEIGHT";
    private Integer employeeID;
    protected Integer objectId;
    protected boolean isEdit;
    private DynamicTable dynamicTable;
    private HorizontalPanel dynamicPanel;
    private DataListBox group;
    private SuperPuperHandler<LinkedList<WfmTreeItem>> onSave;
    private KpiModal skillGroupShell;
    private SkillItem item;
    private final ArrayList<SkillLocalization> localizationList = new ArrayList<>();
    private final String test_code_ID_name = "add_skill_view_";
    private MultiLanguageRichEditorWidget localeView;

    public AddSkillView(String viewName, String description) {
        super(viewName, description);
    }

    public AddSkillView(Integer employeeID) {
        this("addSkill", hrmsStrings.addSkill());
        this.employeeID = employeeID;
    }

    public AddSkillView(SuperPuperHandler<LinkedList<WfmTreeItem>> onSave) {
        if (onSave != null) {
            this.onSave = onSave;
            onInitialize();
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        //save and close
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);

        save.ensureDebugId(test_code_ID_name + "save_and_close_button");
        save.addClickHandler(event -> save(true));

        //save and new
        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.ensureDebugId(test_code_ID_name + "save_and_new_button");
        saveAdd.addClickHandler(event -> save(false));

        splitButton.addItem(saveAdd);
        addButton(splitButton);


    }

    @Override
    protected void getDataToFillFields() {
        initSkillGroup(null);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COMPETENCY_FORM;
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

    private TextArea createRichText() {
        TextArea area = new TextArea();
        return area;
    }

    private void clearFields() {
        group.setSelectedNullLabel();
        dynamicPanel.clear();
        initDynamicTable();
    }

    private DynamicTableColumn[] getColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT];

        columns[0] = new DynamicTableColumn(hrmsStrings.competencyName(), COMPETENCY_NAME, new ColumnStatements(".", hrmsStrings.enterSkillName()), 250);
        columns[1] = new DynamicTableColumn(wfmStrings.description(), COMPETENCY_DESCRIPTION, new ColumnStatements(hrmsStrings.shortDescriptionOfSkill(), hrmsStrings.enterSkillDescription()), 350);
        columns[2] = new DynamicTableColumn(wfmStrings.weight(), COMPETENCY_WEIGHT, new ColumnStatements("", ""), 80);

        return columns;
    }

    private Widget[] getWidgetArray(int rowId) {
        if (item == null) {
            item = new SkillItem();
        }
        Widget[] widgets = new Widget[COLUMNS_COUNT];
        TextBox name = new TextBox();
        name.setText(item.getName());

        TextArea description = createRichText();
        description.setSize("400px", "70px");
        description.setText(item.getDescription());
        final TextBox weight = new TextBox();
        if (item.getWeight() != null) {
            weight.setText(String.valueOf(item.getWeight()));
        }
        weight.addBlurHandler(event -> {
            if (!weight.getText().equals("0") && !weight.getText().equals("")) {
                Validation.numberValidation(weight);
                if (!weight.getText().equals("0") && !weight.getText().equals("")) {
                    double weightDouble = Double.valueOf(weight.getText());
                    if (weightDouble > 100) {
                        Window.alert(hrmsStrings.enter100());
                    }
                } else {
                    if (weight.getText().equals("0")) {
                        Window.alert(hrmsStrings.removeZeroOrPutWeights());
                    }
                }
            } else {
                if (weight.getText().equals("0")) {
                    Window.alert(hrmsStrings.removeZeroOrPutWeights());
                }
            }
        });

        widgets[0] = createLocaleButton(name, rowId, true);
        widgets[1] = createLocaleButton(description, rowId, false);
        widgets[2] = weight;

        return widgets;
    }

    public void initialize() {
        //competency group
        group = new DataListBox();
        group.setAllowFirstItem(true);
        group.ensureDebugId(test_code_ID_name + "competency_group");

        AdvancedInputGroup competencyGroup = new AdvancedInputGroup(group);
        competencyGroup.setAppender("ficon--plus");
        competencyGroup.appenderClickHandler(() -> showAddGroup());

        //competencies
        dynamicPanel = new HorizontalPanel();
        dynamicPanel.ensureDebugId(test_code_ID_name + "competencies_table");

        //add field items
        //competency -> 1
        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.competencyDetails());
        addField(CustomFormConstants.COMPETENCY_GROUP, competencyGroup, getTitle(hrmsStrings.skillGroup(), true));
        addField(CustomFormConstants.COMPETENCIES, dynamicPanel, getTitle(wfmStrings.skills(), true));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCALIZATION_ADD_FOR_SKILL_NAME, AddSkillView.this, (sender, args) -> {
            if (args != null && localeView.getRelatedId() != null) {
                if (localeView.getForName()) {
                    localizationList.get(localeView.getRelatedId()).setNameValueMap((HashMap<String, String>) args);
                } else {
                    localizationList.get(localeView.getRelatedId()).setDescriptionValueMap((HashMap<String, String>) args);
                }
            }
        });

        show();
    }

    private void initDynamicTable() {
        dynamicTable = new DynamicTable(getColumnArray());
        dynamicTable.removeStyleName("dynamictable--full-width");
        dynamicTable.addStyleName("competenciesTable");
        Widget[] widgets = getWidgetArray(0);
        if (item != null && item.getId() != null) {
            dynamicTable.addRow(item.getId(), widgets);
        } else {
            dynamicTable.addRow(widgets);
        }
        dynamicTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                item = null;
                Widget[] widgets = getWidgetArray(rowId + 1);
                dynamicTable.insertRow(rowId + 1, widgets);
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
                localizationList.remove(rowId);
                for (int i = rowId; i < localizationList.size(); i++) {
                    localizationList.get(i).setCode(localizationList.get(i).getCode() - 1);
                }
            }
        });
        dynamicTable.addDynamicTableListener((message, status) -> {
        });
        dynamicPanel.add(dynamicTable);
    }

    private void initSkillGroup(final Integer groupId) {
        AssessmentService.App.get().getSkill(objectId, new AbstractAsyncCallback<SkillItem>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SkillItem result) {
                item = result;
                group.setItems(result.getSkillGroups());
                group.setAllowFirstItem(true);
                if (result.getId() != null) {
                    group.setSelected(result.getGroupId());
                }
                if (groupId != null) {
                    group.setSelected(groupId);
                }
                dynamicPanel.clear();
                initDynamicTable();
            }
        });
    }

    private void onShellOk(boolean close) {
        clearFields();
        if (close) {
            closeTab();
        }
    }

    private void save(final boolean close) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }

        SkillList skillList = setValues();

        //register save logic
        LoadingPanel.loading(true);
        AssessmentService.App.get().addSkill(skillList, new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
            @Override
            public void failure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(LinkedList<WfmTreeItem> result) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfullyAdded());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_COMPETENCY, result, AddSkillView.this);

                if (onSave != null && result != null) {
                    onSave.onFire(result);
                } else {
                    onShellOk(close);
                }
            }
        });
    }

    private FlexTable createLocaleButton(TextBoxBase textBoxBase, int rowId, boolean isName) {
        WfmButton2 locale = new WfmButton2(wfmStrings.localization());
        locale.setStyleName("font-style: italic;", true);
        locale.ensureDebugId(test_code_ID_name + "locale");
        FlexTable localedWidgetBox = new FlexTable();
        localedWidgetBox.setWidget(0, 0, textBoxBase);
        localedWidgetBox.getColumnFormatter().setWidth(0, "85%");
        localedWidgetBox.setWidget(0, 1, locale);
        localedWidgetBox.getColumnFormatter().setWidth(1, "15%");
        localedWidgetBox.ensureDebugId(test_code_ID_name + "localedWidgetBox");
        localedWidgetBox.addStyleName("localedWidget");


        SkillLocalization localization = null;
        if (isName) {
            localization = new SkillLocalization(rowId);
            localization.setNameLocaleButton(locale);
            if (localizationList.size() == rowId) {
                localizationList.add(localization);
            } else {
                localizationList.add(rowId, localization);
                for (int i = rowId + 1; i < localizationList.size(); i++) {
                    SkillLocalization loc = localizationList.get(i);
                    loc.setCode(loc.getCode() + 1);
                }
            }
        } else {
            localization = localizationList.get(rowId);
            localization.setDescriptionLocaleButton(locale);
        }
        if (item != null) {
            localization.setNameValueMap(castLocalizationItemToMap(item.getSkillNameLocalization()));
            localization.setDescriptionValueMap(castLocalizationItemToMap(item.getSkillDescriptionLoc()));
        }
        return localedWidgetBox;
    }

    private SkillList setValues() {
        SkillList skillList = new SkillList();
        skillList.setEmployeeID(employeeID);
        skillList.setSkilGroupId(group.getSelectedItem().getId());
        SkillItem[] skillItems = new SkillItem[dynamicTable.getRowNumber()];

        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            SkillItem skillItem = new SkillItem();
            TextBox skillName = (TextBox) ((FlexTable) tableItem.getColumnById(COMPETENCY_NAME)).getWidget(0, 0);
            TextArea description = (TextArea) ((FlexTable) tableItem.getColumnById(COMPETENCY_DESCRIPTION)).getWidget(0, 0);
            TextBox weight = (TextBox) tableItem.getColumnById(COMPETENCY_WEIGHT);
            skillItem.setId(tableItem.getObjectId());
            skillItem.setName(skillName.getText());
            skillItem.setDescription(description.getText());
            skillItem.setSkillNameLocalization(castMapToLocalizationItem(localizationList.get(i).getNameValueMap()));
            skillItem.setSkillDescriptionLoc(castMapToLocalizationItem(localizationList.get(i).getDescriptionValueMap()));
            try {
                skillItem.setWeight(Double.valueOf(weight.getText()));
            } catch (NumberFormatException ex) {
                skillItem.setWeight((double) 0f);
            }
            skillItems[i] = skillItem;
        }

        skillList.setSkillItems(skillItems);
        return skillList;
    }

    private CustomFormLocalization castMapToLocalizationItem(HashMap<String, String> map) {
        CustomFormLocalization cfl = new CustomFormLocalization();
        cfl.setEnglishName(map.get("en"));
        cfl.setRussianName(map.get("ru"));
        cfl.setArabicName(map.get("ar"));
        cfl.setUzbekName(map.get("uz"));
        return cfl;
    }

    private HashMap<String, String> castLocalizationItemToMap(CustomFormLocalization loc) {
        HashMap<String, String> map = new HashMap<>();
        if (loc == null) {
            return map;
        }
        map.put("en", loc.getEnglishName());
        map.put("ru", loc.getRussianName());
        map.put("ar", loc.getArabicName());
        map.put("uz", loc.getUzbekName());
        return map;
    }

    private void saveCompetencyGroup(TextBox textBox) {
        String competencyGroupName = textBox.getText();
        if (validateCompetencyGroup(competencyGroupName)) {
            textBox.addStyleName("x-form-invalid");
            return;
        }
        SkillGroupItem item = new SkillGroupItem();
        item.setName(competencyGroupName);
        LoadingPanel.loading(true);
        AssessmentService.App.get().addSkillGroup(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                initSkillGroup(result);
                skillGroupShell.close();
            }
        });
    }

    private void showAddGroup() {
        skillGroupShell = new KpiModal();
        final TextBox newGroup = new TextBox();
        newGroup.addKeyDownHandler(event -> {
            TextBox textbox = (TextBox) event.getSource();
            if (textbox.getText().length() < 1) {
                textbox.addStyleName("x-form-invalid");
            } else {
                if (!"".equals(textbox.getStyleName())) {
                    textbox.removeStyleName("x-form-invalid");
                }
            }
        });
        WfmButton2 ok = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> saveCompetencyGroup(newGroup));
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> skillGroupShell.close());

        skillGroupShell.add(new FormGroup(wfmStrings.name(), newGroup, true));

        skillGroupShell.setTitle(hrmsStrings.skillGroup());

        skillGroupShell.addButton(cancel);
        skillGroupShell.addButton(ok);

        skillGroupShell.setWidth(400);
        skillGroupShell.open();
    }

    private boolean validateCompetencyGroup(String groupName) {
        return groupName == null || "".equals(groupName);
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(group, !Validation.validateDataListBoxRequired(group));

        dynamicTable.resetValidation();
        for (int rowId = 0; rowId < dynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = dynamicTable.getItem(rowId);
            TextBox skillName = (TextBox) ((FlexTable) tableItem.getColumnById(COMPETENCY_NAME)).getWidget(0, 0);
            TextArea description = (TextArea) ((FlexTable) tableItem.getColumnById(COMPETENCY_DESCRIPTION)).getWidget(0, 0);
            if (skillName.getText() == null || "".equals(skillName.getText())) {
                dynamicTable.notValid(rowId, COMPETENCY_NAME);
                errors++;
            }
            if (description.getText() == null || "".equals(description.getText())) {
                dynamicTable.notValid(rowId, COMPETENCY_DESCRIPTION);
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else {
            return true;
        }
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

    private class SkillLocalization {
        private Integer code;
        private HashMap<String, String> nameValueMap = new HashMap<>();
        private HashMap<String, String> descriptionValueMap = new HashMap<>();
        private WfmButton2 nameLocaleButton;
        private WfmButton2 descriptionLocaleButton;

        public SkillLocalization(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public HashMap<String, String> getNameValueMap() {
            return nameValueMap;
        }

        public void setNameValueMap(HashMap<String, String> nameValueMap) {
            this.nameValueMap = nameValueMap;
        }

        public HashMap<String, String> getDescriptionValueMap() {
            return descriptionValueMap;
        }

        public void setDescriptionValueMap(HashMap<String, String> descriptionValueMap) {
            this.descriptionValueMap = descriptionValueMap;
        }

        public WfmButton2 getNameLocaleButton() {
            return nameLocaleButton;
        }

        public void setNameLocaleButton(WfmButton2 nameLocaleButton) {
            this.nameLocaleButton = nameLocaleButton;
            nameLocaleButton.setId(code.toString());
            nameLocaleButton.addClickHandler(event -> {
                if (localeView == null) {
                    localeView = new MultiLanguageRichEditorWidget(wfmStrings.localization());
                }
                localeView.setRelatedId(code);
                localeView.setValueMap(localizationList.get(code).getNameValueMap(), true);
                localeView.show();
            });
        }

        public WfmButton2 getDescriptionLocaleButton() {
            return descriptionLocaleButton;
        }

        public void setDescriptionLocaleButton(WfmButton2 descriptionLocaleButton) {
            this.descriptionLocaleButton = descriptionLocaleButton;
            descriptionLocaleButton.setId(code.toString());
            descriptionLocaleButton.addClickHandler(event -> {
                if (localeView == null) {
                    localeView = new MultiLanguageRichEditorWidget(wfmStrings.localization());
                }
                localeView.setRelatedId(code);
                localeView.setValueMap(localizationList.get(code).getDescriptionValueMap(), false);
                localeView.show();
            });
        }

        private void initHandler() {

        }
    }
}
