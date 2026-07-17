package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ICommand;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CustomLogicCFModal;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldMultiLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.TYPE_ENTITY_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_CHECKBOX;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DROPDOWN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_PERCENTAGE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_RADIOBUTTON;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTAREA;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX_EMAIL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_URL;
import static com.google.gwt.dom.client.Style.Unit.PCT;
import static java.lang.Integer.parseInt;

public class FieldProperty extends KpiModal {

    //    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final String entityName;
    private Integer objectId;
    private final boolean isCustomField;
    private final String fieldID;
    private TextBox fieldName;
    private TextBox aliasName;
    private KpiDataGrid valueTable;
    private TextBox predValuesBox;
    private TextBox prefixBox;
    private TextBox scale;
    private final ArrayList<SelectItem> values = new ArrayList<>();
    private FormGroup prefixFormGroup;
    private FormGroup predFormGroup;
    private DataListBox fieldType;
    private KpiSwitcher required;
    private TextBox minChar;
    private DataListBox minCharCriteria;
    private FormGroup minCharForm;
    private KpiSwitcher tabOptions;
    private KpiSwitcher seeOwnPermission;
    private FormGroup tabOptionsFormGroup;
    private FormGroup seeOwnPermissionFormGroup;
    private KpiSwitcher showInListing;
    private KpiSwitcher showInFilter;
    private KpiSwitcher disabled;
    private FormGroup disabledFormGroup;
    private KpiSwitcher useInPermission;
    private FormGroup useInPermissionFormGroup;
    private FormGroup req;
    private DataListBox lookUpTypeBox;
    private FormGroup lookUpTypeFormGroup;
    private TextArea customQueryBox;
    private FormGroup customQueryBoxFormGroup;
    private final Boolean isQuizCustomForm;
    private KpiSelect2 roleList;
    private KpiSelect2 editRoleWhenDisabled;
    private CompanyCustomFieldItem fieldItem;
    private WfmButton2 saveButton;
    private ICommand command;
    private Div previewDiv;
    private FormGroup defaultValueFormGroup;
    private SelectItem uiTypeSelectItem;
    private ReferenceLookUp referenceLookUp;
    private FormGroup referenceLookUpFormGroup;
    private FormGroup scaleGroup;
    private TextBox minValue;
    private FormGroup allowEditFormGroup;
    private FormGroup chooseRelationalFormGroup;
    private DataListBox setRelationCustomForm;
    private String[] relationCustomFields;
    private SelectionCell itemCell;
    private Integer relationFieldId;
    private MaterialPanel predValuesPanel;
    private FormGroup minValueGroup;
    private MaterialPanel panel;
    private CustomLogicCFModal customLogicCFModal;
    private VerticalPanel areaPanel;
    public HorizontalPanel textPanel;
    public HorizontalPanel textPanel2;
    public HorizontalPanel textPanelMain;

    private FlowPanel switchesPanel;

    private static final int IDX_REQUIRED = 0;
    private static final int IDX_SHOW_IN_LISTING = 1;
    private static final int IDX_SHOW_IN_FILTER  = 2;
    private static final int IDX_DISABLED        = 3;
    private static final int IDX_NEW_TAB         = 4;
    private static final int IDX_SEE_RELATED     = 5;
    private static final int IDX_USE_PERMISSION  = 6;

    FieldProperty(String entityName, String fieldID, boolean customField, boolean isQuizCustomForm) {
        this.entityName = entityName;
        this.fieldID = fieldID;
        this.isCustomField = customField;
        this.isQuizCustomForm = isQuizCustomForm;
    }

    public void initizalize() {
        this.addStyleName("file--FieldPropery");
        setWidth(750);
        open();
        showCustomFieldProperty();
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> null;

    public static SelectItem[] getDataType(String dataType) {
        if (dataType == null) {
            return null;
        }
        ArrayList<SelectItem> items = new ArrayList<>();
        if (dataType.equals(Constants.DATA_TYPE_TEXT)) {
            items.add(new SelectItem(0, UI_TYPE_TEXTBOX));
            items.add(new SelectItem(1, UI_TYPE_TEXTBOX_EMAIL));
            items.add(new SelectItem(2, UI_TYPE_URL));
            items.add(new SelectItem(3, UI_TYPE_TEXTAREA));
            items.add(new SelectItem(4, Constants.UI_TYPE_DROPDOWN));
            items.add(new SelectItem(5, Constants.UI_TYPE_CHECKBOX));
            items.add(new SelectItem(6, Constants.UI_TYPE_RADIOBUTTON));
            items.add(new SelectItem(7, Constants.UI_TYPE_LOOKUP));
            items.add(new SelectItem(8, Constants.UI_TYPE_CURRENCY));
            items.add(new SelectItem(9, Constants.UI_TYPE_MULTI_LOOKUP));
            items.add(new SelectItem(10, Constants.UI_TYPE_HTML_TEXTAREA));
            items.add(new SelectItem(11, Constants.UI_TYPE_AUTONUMBER));
            items.add(new SelectItem(12, Constants.TYPE_ENTITY_LOOKUP));
            items.add(new SelectItem(13, Constants.TYPE_ENTITY_MULTI_LOOKUP));
            items.add(new SelectItem(23, Constants.UI_TYPE_COMMITBOX));
            items.add(new SelectItem(24, Constants.UI_TYPE_APPROVAL_PROCESS));
        } else if (dataType.equals(Constants.DATA_TYPE_DATE)) {
            items.add(new SelectItem(14, Constants.UI_TYPE_DATEPICKER));
            items.add(new SelectItem(15, Constants.UI_TYPE_DATEPICKER_TIME));
        } else if (dataType.equals(Constants.DATA_TYPE_FILE_UPLOAD)) {
            items.add(new SelectItem(16, Constants.UI_TYPE_FILE_UPLOAD_ITEM));
            items.add(new SelectItem(17, Constants.UI_TYPE_FILE_UPLOAD_WIDGET));
        } else if (dataType.equals(Constants.DATA_TYPE_NUMBER)) {
            items.add(new SelectItem(18, UI_TYPE_TEXTBOX, Constants.DATA_TYPE_NUMBER));
            items.add(new SelectItem(19, Constants.UI_TYPE_DROPDOWN, Constants.DATA_TYPE_NUMBER));
            items.add(new SelectItem(20, Constants.UI_TYPE_RADIOBUTTON, Constants.DATA_TYPE_NUMBER));
            items.add(new SelectItem(21, Constants.UI_TYPE_PERCENTAGE, Constants.DATA_TYPE_NUMBER));
        } else if (dataType.equals(Constants.DATA_TYPE_PROFILE_IMAGE)) {
            items.add(new SelectItem(22, Constants.UI_TYPE_PROFILE_IMAGE_WIDGET));
        }
        return items.toArray(new SelectItem[]{});
    }


    private void toggleInSwitches(Widget w, boolean show, int index) {
        if (switchesPanel == null) return;
        if (show) {
            if (w.getParent() == null) {
                if (index >= 0 && index <= switchesPanel.getWidgetCount()) {
                    switchesPanel.insert(w, index);
                } else {
                    switchesPanel.add(w);
                }
            }
            w.setVisible(true); // на случай, если кто-то ранее скрывал стилем
        } else {
            if (w.getParent() == switchesPanel) {
                switchesPanel.remove(w);
            } else {
                w.setVisible(false);
            }
        }
    }

    private void showCustomFieldProperty() {
        GRow mainRow = new GRow();
        fieldName = new TextBox();
        fieldName.setEnabled(false);
        fieldName.setMaxLength(1000);

        aliasName = new TextBox();
        aliasName.setMaxLength(1000);

        fieldType = new DataListBox();
        fieldType.setEnabled(false);
        roleList = new KpiSelect2(true);
        editRoleWhenDisabled = new KpiSelect2(true);

        required = new KpiSwitcher();

        minChar = new TextBox();
        Validation.addPhoneNumberKeyboardListener(minChar);

        minCharCriteria = new DataListBox();
        minCharCriteria.setSelected(new SelectItem(0, "Equal"));
        minCharCriteria.setEnabled(false);

        tabOptions = new KpiSwitcher();
        seeOwnPermission = new KpiSwitcher();

        scale = new TextBox();
        Validation.addNumericKeyboardListener(scale);
        scale.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        minValue = new TextBox();
        Validation.addNumericKeyboardListener(minValue, 2, false);
        minValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        scale.addValueChangeHandler(handler -> {
            Integer i = Integer.parseInt(handler.getValue());
            Validation.addNumericKeyboardListener(minValue, i, false);
        });
        showInListing = new KpiSwitcher();
        showInFilter = new KpiSwitcher();
        setRelationCustomForm = new DataListBox();
        chooseRelationalFormGroup = new FormGroup(wfmStrings.relationField(), setRelationCustomForm);
        chooseRelationalFormGroup.setVisible(false);
        getRelationItems();
        setRelationCustomForm.setChangeEvent(() -> {
            relationFieldId = setRelationCustomForm.getSelectedId();
            setRelationFields(setRelationCustomForm.getSelectedValue());
        });
        disabled = new KpiSwitcher();
        disabled.addValueChangeHandler(value -> {
            if (value.getValue()) {
                allowEditFormGroup.setVisible(true);
                editRoleWhenDisabled.setItems(fieldItem.getAllRoles());
            } else {
                editRoleWhenDisabled.clear();
                allowEditFormGroup.setVisible(false);
            }
        });
        useInPermission = new KpiSwitcher();

        lookUpTypeBox = new DataListBox();
        lookUpTypeBox.setItems(CustomFieldForm.getLookUpTypes());
        lookUpTypeBox.addValueChangeHandler(event -> {
            if (lookUpTypeBox.getSelectedItem() != null && (CustomFieldLookUpTypeEnum.DEPARTMENT.name().equals(lookUpTypeBox.getSelectedItem().getDescription()) ||
                    CustomFieldLookUpTypeEnum.POSITION.name().equals(lookUpTypeBox.getSelectedItem().getDescription()) ||
                    CustomFieldLookUpTypeEnum.LOCATION.name().equals(lookUpTypeBox.getSelectedItem().getDescription()))) {
                toggleInSwitches(useInPermissionFormGroup, true, IDX_USE_PERMISSION);
            } else {
                toggleInSwitches(useInPermissionFormGroup, false, IDX_USE_PERMISSION);
                useInPermission.setValue(false);
            }
        });

        referenceLookUp = new ReferenceLookUp(null);
        referenceLookUp.getSuggestBox().addSelectionHandler(valueChangeEvent -> changeUiType(uiTypeSelectItem, true));

        customQueryBox = new TextArea();

        lookUpTypeFormGroup = new FormGroup(wfmStrings.value(), lookUpTypeBox);
        lookUpTypeFormGroup.setVisible(false);

        referenceLookUpFormGroup = new FormGroup(wfmStrings.reference(), referenceLookUp);
        referenceLookUpFormGroup.setVisible(false);

        scaleGroup = new FormGroup(wfmStrings.decimalPlaces(), scale);
        scaleGroup.setVisible(false);
        new KpiToolTip(scaleGroup, wfmStrings.numberOfDigitsToTheRight());
        minValueGroup = new FormGroup(wfmStrings.minValue(), minValue);
        minValueGroup.setVisible(false);
        lookUpTypeBox.addValueChangeHandler(event -> {
            boolean isReference = CustomFieldLookUpTypeEnum.REFERENCE.equals(CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription()));
            changeUiType(fieldType.isSomethingSelected() ? fieldType.getSelectedItem() : null, isReference);
        });

        switchesPanel = new FlowPanel();
        req = new FormGroup(wfmStrings.required(), required);
        tabOptionsFormGroup = new FormGroup(wfmStrings.newTab(), tabOptions);
        tabOptionsFormGroup.setVisible(false);
        seeOwnPermissionFormGroup = new FormGroup(wfmStrings.useInSeeRelated(), seeOwnPermission);
        seeOwnPermissionFormGroup.setVisible(false);
        disabledFormGroup = new FormGroup(wfmStrings.disabled(), disabled);
        disabledFormGroup.setVisible(false);
        useInPermissionFormGroup = new FormGroup(wfmStrings.useInPermission(), useInPermission);
//        useInPermissionFormGroup.setVisible(false);
        switchesPanel.addStyleName("customField-switchers");
        switchesPanel.add(req);
        switchesPanel.add(new FormGroup(wfmStrings.showInListing(), showInListing));
        switchesPanel.add(new FormGroup(wfmStrings.showInFilter(), showInFilter));
        // условные группы НЕ добавляем здесь — ими управляем через toggleInSwitches(...)

        defaultValueFormGroup = new FormGroup();
        defaultValueFormGroup.setLabel(wfmStrings.defaultValue());

        customQueryBoxFormGroup = new FormGroup(wfmStrings.queryPanel(), customQueryBox);
        customQueryBoxFormGroup.setVisible(false);


        prefixBox = new TextBox();
        prefixFormGroup = new FormGroup(wfmStrings.prefix(), prefixBox);
        prefixFormGroup.setVisible(false);

        FormGroup fieldNameFG = new FormGroup(fieldName);
        Div label = fieldNameFG.getGroupLabel();
        MaterialLink localeLink = new MaterialLink(wfmStrings.vacancyLocale());
        localeLink.addStyleName("btn-small btn--default mb-1");

        MaterialIcon plusIcon = new MaterialIcon();
        plusIcon.setStyleName("ficon--plus-circle");
        localeLink.add(plusIcon);

        label.addStyleName("label-group");
        Span nameField = new Span(wfmStrings.fieldName());
        Span localization = new Span();
        localization.add(localeLink);
        localeLink.addClickHandler(event -> {
            LocalizationCFModal localizationCFModal = new LocalizationCFModal(objectId, LocalizationTypeEnum.FIELD);
            localizationCFModal.center();
        });
        label.add(nameField);
        label.add(localization);

        GColumn leftColumn = new GColumn(GColumnEnum.COL_6);
        GColumn rightColumn = new GColumn(GColumnEnum.COL_6);

        leftColumn.add(fieldNameFG);
        leftColumn.add(new FormGroup(wfmStrings.fieldType(), fieldType));
        leftColumn.add(lookUpTypeFormGroup);
        leftColumn.add(referenceLookUpFormGroup);
        leftColumn.add(prefixFormGroup);
        leftColumn.add(defaultValueFormGroup);
        leftColumn.add(customQueryBoxFormGroup);

        rightColumn.add(new FormGroup(wfmStrings.aliasName(), aliasName));
        rightColumn.add(new FormGroup(wfmStrings.visibleTo(), roleList));
        GRow gRow = new GRow(new GColumn(GColumnEnum.COL_6, scaleGroup), new GColumn(GColumnEnum.COL_6, minValueGroup));
//        rightColumn.add(scaleGroup);
        rightColumn.add(gRow);
        rightColumn.add(switchesPanel);
        minCharForm = new FormGroup(wfmStrings.charLimit(), new InputGroup(minCharCriteria, minChar));
        minCharForm.setVisible(false);
        rightColumn.add(minCharForm);
        allowEditFormGroup = new FormGroup(wfmStrings.allowEdit(), editRoleWhenDisabled);
        allowEditFormGroup.setVisible(false);
        rightColumn.add(allowEditFormGroup);
        leftColumn.add(chooseRelationalFormGroup);
        mainRow.add(leftColumn);
        mainRow.add(rightColumn);
        add(mainRow);

        predValuesBox = new TextBox();
        predValuesBox.addKeyDownHandler(keyDownEvent -> {
            if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (!Utils.isNullOrEmpty(predValuesBox.getText())) {
                    valueTable.removeStyleName(ERROR_FORM_STYLE);
                    addValueToTable(predValuesBox.getText());
                    predValuesBox.setText("");
                    preview(uiTypeSelectItem);
                }
            }
        });
        MaterialPanel predValuesPanel = new MaterialPanel();
        predFormGroup = new FormGroup(wfmStrings.predefinedValues(), predValuesPanel);
        predFormGroup.setVisible(false);
        add(predFormGroup);

        fieldType.addValueChangeHandler(changeEvent -> {
            predValuesBox.setText("");
            changeUiType(fieldType.isSomethingSelected() ? fieldType.getSelectedItem() : null, false);
        });

        HorizontalPanelDiv pvPanel = new HorizontalPanelDiv();
        Div buttonDiv = new Div();
        WfmButton2 addValue = new WfmButton2(wfmStrings.addValue(), WfmButton2.BTN_SECONDARY, clickEvent -> addButtonClick());
        buttonDiv.setStyleName("input-group-append");
        buttonDiv.add(addValue);

        pvPanel.add(predValuesBox);
        pvPanel.add(buttonDiv);
        pvPanel.setStyleName("input-group");
        predValuesPanel.add(pvPanel);

        valueTable = new KpiDataGrid<>(KEY_PROVIDER);
        valueTable.setStyleName("cellBasedWidget-mod");
        valueTable.setSize("100%", "300px");

        Column<SelectItem, String> objectIdCell = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
//                GWT.log("========================= Select item values =========================");
//                GWT.log("name " + object.getName() + " code " + object.getCode() + " number " + object.getNumber() + " param " + object.getParam());
                return object.getDescription();
            }
        };
        valueTable.addColumn(objectIdCell, "Id");
        valueTable.setColumnWidth(objectIdCell, 15, PCT);

        Column<SelectItem, String> value = new Column<SelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return object.getName();
            }
        };
        valueTable.addColumn(value, wfmStrings.value());
        valueTable.setColumnWidth(value, 30, PCT);

        final TextInputCell inputCell = new TextInputCell();
        inputCell.setWidth("100%");
        Column<SelectItem, String> sorder = new Column<SelectItem, String>(inputCell) {
            @Override
            public String getValue(SelectItem object) {
                return object.getId() + "";
            }
        };

        sorder.setFieldUpdater((i, object, value12) -> {
            try {
                if (!value12.equals("0") && value12.length() > 0) {
                    TextBox textBox = new TextBox();
                    textBox.setText(value12);
                    Validation.numberValidationWithoutDot(textBox);
                    if (textBox.getText().length() == 0) {
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                        valueTable.redraw();
                    }
                    object.setId(Integer.parseInt(textBox.getText()));
                } else {
                    object.setId(parseInt(value12));
                }
            } catch (NumberFormatException ex) {
                object.setId(1);
                valueTable.redraw();
            }
            valueTable.redraw();
        });
        valueTable.addColumn(sorder, wfmStrings.sortOrder());
        valueTable.setColumnWidth(sorder, 30, PCT);

        final Column<SelectItem, String> action = new Column<SelectItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return wfmStrings.delete();
            }
        };
        action.setFieldUpdater((index, obj, value1) -> {
            values.remove(obj);
            valueTable.supplyProvider(values);
            valueTable.refresh();
            preview(uiTypeSelectItem);
        });
        valueTable.addColumn(action, wfmStrings.action());
        valueTable.setColumnWidth(action, 25, PCT);
        predValuesPanel.add(valueTable);


        if (relationCustomFields == null) {
            relationCustomFields = new String[]{};
        }
        addRelationColumn(relationCustomFields);

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), clickEvent -> close());
        addButton(cancel);

        saveButton = new WfmButton2(wfmStrings.save(), clickEvent -> saveCustomField());
        addButton(saveButton);

        WfmButton2 customLogic = new WfmButton2(wfmStrings.customLogic(), clickEvent -> {
            customLogicCFModal.center();
        });
        addButton(customLogic);

        getFieldValues();

    }

    private void addScoreColumnToTable() {
        String uiType = fieldItem.getUiType();
        if (isQuizCustomForm && (UI_TYPE_DROPDOWN.equals(uiType) || UI_TYPE_CHECKBOX.equals(uiType) || UI_TYPE_RADIOBUTTON.equals(uiType))) {

            final TextInputCell inputCell = new TextInputCell();
            inputCell.setWidth("100%");
            Column<SelectItem, String> score = new Column<SelectItem, String>(inputCell) {
                @Override
                public String getValue(SelectItem object) {
                    if (object.getParam() == null) {
                        object.setParam("0");
                    }
                    return object.getParam();
                }
            };
            score.setFieldUpdater((index, obj, value1) -> {
                String oldValue = obj.getParam() != null ? obj.getParam() : "0";
                try {
                    TextBox textBox = new TextBox();
                    textBox.setText(value1);
                    Validation.addNumericKeyboardListener(textBox, 2, false);
                    if (textBox.getText().length() > 0 && !(textBox.getText().startsWith("-"))) {
                        obj.setParam(textBox.getText());
                    } else {
                        Info.warn(wfmStrings.enterCurrentVersion());
                        obj.setParam(oldValue);
                    }
                    valueTable.redraw();
                } catch (Exception ex) {
                    obj.setParam(oldValue);
                    valueTable.redraw();
                }

            });
            valueTable.addColumn(score, wfmStrings.score());
            valueTable.setColumnWidth(score, 30, PCT);
        }
    }

    private void setRelationFields(SelectItem selectItem) {
        int j = 0;

        int len = selectItem.getRelatedItems() != null ? selectItem.getRelatedItems().length : 0;
        String[] strs = new String[len + 1];
        strs[j] = "Please Select";
        for (SelectItem relationCustomField : selectItem.getRelatedItems()) {
            if (j++ < strs.length)
                strs[j] = relationCustomField.getName();
        }
        valueTable.removeColumn(4);
        addRelationColumn(strs);
        valueTable.supplyProvider(values);
        valueTable.refresh();
    }

    private void addRelationColumn(String[] relationCustomFieldItems) {
        itemCell = new SelectionCell(Arrays.asList(relationCustomFieldItems));
        final Column<SelectItem, String> relation = new Column<SelectItem, String>(itemCell) {
            @Override
            public String getValue(SelectItem widget) {
                return widget.getCategory();
            }
        };
        relation.setFieldUpdater((i, selectItem, s) -> {
            values.get(i).setCategory(s);

        });
        valueTable.addColumn(relation, wfmStrings.relationAlias());
        valueTable.setColumnWidth(relation, 30, PCT);
    }

    public BigDecimal parsePriceToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
            return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    private void getRelationItems() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyAllDropDownCustomFiedsByEntityName(entityName, objectId, fieldID, isCustomField, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Method failed");
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                LoadingPanel.loading(false);
                setRelationCustomForm.clear();
                if (selectItems != null) {
                    setRelationCustomForm.setItems(selectItems);
                    setSelectedItemToRelationDropDown();
                }
            }
        });
    }

    private void saveCustomField() {
        enableButtons(false);
        if (!validate()) {
            enableButtons(true);
            return;
        }

        if (fieldItem == null) {
            fieldItem = new CompanyCustomFieldItem();
        }

        fieldItem.setFieldName(fieldName.getText().length() > 1000 ? fieldName.getText().substring(0, 1000) : fieldName.getText());
        fieldItem.setAliasName(aliasName.getText().length() > 1000 ? aliasName.getText().substring(0, 1000) : aliasName.getText());

        fieldItem.setUiType(fieldType.getSelectedItem() != null ? fieldType.getSelectedItem().getName() : "");

        SelectItem uiType = fieldType.isSomethingSelected() ? fieldType.getSelectedItem() : null;
        if (uiType != null && Utils.hasPredefinedValue(uiType.getName())) {
            fieldItem.setPredefinedValues(getPredefinedValues());
            fieldItem.setPredefinedValuesWithSorting(getPredefinedValuesWithSorting());
            if (Constants.UI_TYPE_RADIOBUTTON.equals(uiType.getName()) || Constants.UI_TYPE_CHECKBOX.equals(uiType.getName())
                    || UI_TYPE_DROPDOWN.equals(uiType.getName())) {
                fieldItem.setQuizFormScoreValues(getScoreValues());
            }
            if (UI_TYPE_DROPDOWN.equals(uiType.getName())) {
                if (relationFieldId != null) {
                    if (!checkAliesValuesAreSelected()) {
                        Info.show(wfmStrings.fillAllRequiredFields(), Info.Type.WARNING);
                        enableButtons(true);
                        return;
                    }
                    fieldItem.setRelationFieldValues(getRelationFieldValues());
                    fieldItem.setDisabled(false);
                }
            }
        }
        if (uiType != null && (Constants.UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getName()) || Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getName()))) {
            fieldItem.setLookUpTypeEnum(lookUpTypeBox.getSelectedItem() != null
                    ? CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription())
                    : null);
            fieldItem.setReferenceItem(referenceLookUp.getSelectedItem());
        }
        if (uiType != null && (Constants.TYPE_ENTITY_LOOKUP.equals(fieldType.getSelectedItem().getName()) || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getName()))) {
            fieldItem.setQuery(customQueryBox.getText());
        }
        if (Constants.UI_TYPE_AUTONUMBER.equals(fieldType.getSelectedItem().getName())) {
            fieldItem.setPrefix(prefixBox.getText());
        }
        fieldItem.setShowInListing(showInListing.getValue());
        fieldItem.setShowInFilterGrouping(showInFilter.getValue());
        fieldItem.setFacetable(showInFilter.getValue());
        fieldItem.setRequired(required.getValue());
        fieldItem.setMinChar(minChar.getText());
        fieldItem.setAddTab(tabOptions.getValue());
        fieldItem.setSeeOwnPermission(seeOwnPermission.getValue());
        fieldItem.setDisabled(disabled.getValue());
        fieldItem.setUseInPermission(useInPermission.getValue());
        if (minChar.getText() != null && !minChar.getText().equals("")) {
            fieldItem.setRequired(true);
        }
        ArrayList<Integer> editRoles = new ArrayList<>();
        if (disabled.getValue()) {
            if (editRoleWhenDisabled.getSelectedItems() != null && editRoleWhenDisabled.getSelectedItems().size() > 0) {
                editRoles.addAll(editRoleWhenDisabled.getSelectedItems()
                        .stream()
                        .map(SelectItem::getId)
                        .collect(Collectors.toList())
                );
            }
        }
        fieldItem.setRoleEdit(editRoles);

        if (!Utils.isNullOrEmpty(scale.getText())) {
            fieldItem.setScale(Integer.valueOf(scale.getText()));
        }
        if (!Utils.isNullOrEmpty(minValue.getText())) {
            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
            fieldItem.setNumberMinValue(Utils.universalParse(priceFormat, minValue.getText()));
        } else {
            fieldItem.setNumberMinValue(0.0);
        }
        if (!(Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(fieldType.getSelectedItem().getName())
                || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(fieldType.getSelectedItem().getName())
                || Constants.UI_TYPE_PROFILE_IMAGE_WIDGET.equals(fieldType.getSelectedItem().getName())
                || Constants.TYPE_ENTITY_LOOKUP.equals(fieldType.getSelectedItem().getName())
                || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getName())
                || Constants.UI_TYPE_APPROVAL_PROCESS.equals(fieldType.getSelectedItem().getName()))) {
            fieldItem.setDefaultValue(getDefaultValue());
            if (fieldType.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER)) {
                DataListBox type = (DataListBox) defaultValueFormGroup.getGroupContent().getWidget(0);
                String dateType = type.getSelectedItem(true).getDescription();
                if (!dateType.equals("EQUAL")) {
                    fieldItem.setDefaultValue(dateType);
                }
            }
        }
        ArrayList<Integer> roles = new ArrayList<>();
        if (roleList.getSelectedItems() != null && roleList.getSelectedItems().size() > 0) {
            roles.addAll(roleList.getSelectedItems()
                    .stream()
                    .map(SelectItem::getId)
                    .collect(Collectors.toList())
            );
        }
        fieldItem.setAllowedRoles(roles);

        fieldItem.setRelationFieldId(setRelationCustomForm.getSelectedId());
        fieldItem.setCustomLogicField(customLogicCFModal != null ? customLogicCFModal.getSelectedField() : null);
        fieldItem.setCustomLogicValue(customLogicCFModal != null && customLogicCFModal.getSelectedValue() != null ? customLogicCFModal.getSelectedValue().getName() : null);
        LoadingPanel.loading(true);
        CommonService.App.get().saveCustomFields(fieldItem, false, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                enableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customField()), Info.Type.INFO);
                enableButtons(true);

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FIELD_ADD, null, null);

                if (command != null) {
                    command.execute(fieldItem);
                }

                close();
            }
        });
    }

    private String getScoreValues() {
        JSONObject jsonObject = new JSONObject();
        if (isQuizCustomForm && !values.isEmpty()) {
            for (SelectItem item : values) {
                try {
                    jsonObject.put(item.getName(), new JSONNumber(Double.parseDouble(item.getParam())));
                } catch (Exception ex) {
                    jsonObject.put(item.getName(), new JSONNumber(0));
                }
            }
        } else {
            for (SelectItem item : values) {
                jsonObject.put(item.getName(), new JSONNumber(0));
            }
        }
        return jsonObject.toString();
    }

    private String getDefaultValue() {
        GWT.log("ui type " + uiTypeSelectItem.getName() + " defaultValueFormGroup.getGroupContent() " + (defaultValueFormGroup.getGroupContent() == null));
        Widget widget = defaultValueFormGroup.getGroupContent().getWidgetCount() > 0 ? defaultValueFormGroup.getGroupContent().getWidget(uiTypeSelectItem.getName().equals(UI_TYPE_DATEPICKER) ? 1 : 0) : null;
        if (widget == null) {
            return null;
        }
        String value = "";
        if (widget instanceof TextBox) {
            value = ((TextBox) widget).getText();
        } else if (widget instanceof DataListBox) {
            value = ((DataListBox) widget).getDisplayValue();
        } else if (widget instanceof LookUp && ((CustomFieldLookUp) widget).getSelectedItem() != null) {
            value = ((CustomFieldLookUp) widget).getSelectedItem().getId() + Constants.DELIMITR + ((CustomFieldLookUp) widget).getSelectedItem().getName();
        } else if (widget instanceof TextArea) {
            value = ((TextArea) widget).getText();
        } else if (widget instanceof KpiEditor) {
            value = ((KpiEditor) widget).getData();
        } else if (widget instanceof DatePicker) {
            Date date = ((DatePicker) widget).getDate();
            value = date != null ? DateUtils.format(date) : "";
        } else if (widget instanceof DateTimeWidget) {
            Date date = ((DateTimeWidget) widget).getDateTime();
            value = date != null ? DateUtils.formatInternal(date) : "";
        } else if (widget instanceof FlexTable) {
            FlexTable flexTable = (FlexTable) widget;
            int k = 0;
            for (int i = 0; i < flexTable.getRowCount(); i++) {
                for (int j = 0; j < flexTable.getCellCount(i); j++) {
                    Widget w = flexTable.getWidget(i, j);
                    if (w == null) {
                        continue;
                    }
                    if (w instanceof KpiCheckBox) {
                        KpiCheckBox checkBox = (KpiCheckBox) w;
                        if (checkBox.getValue()) {
                            if (k != 0) {
                                value += ", ";
                            }
                            value += checkBox.getText();
                            k++;
                        }
                    } else if (w instanceof KpiRadioButton) {
                        KpiRadioButton radioButton = (KpiRadioButton) w;
                        if (radioButton.getValue()) {
                            value = radioButton.getText();
                            break;
                        }
                    }
                }
            }
        }
        int index = widget instanceof DatePicker ? 2 : 1;
        if (defaultValueFormGroup.getGroupContent() != null && defaultValueFormGroup.getGroupContent().getWidgetCount() > index && defaultValueFormGroup.getGroupContent().getWidget(index) != null) {
            TextBox height = ((TextBox) defaultValueFormGroup.getGroupContent().getWidget(index));
            fieldItem.setMinHeight(height != null ? height.getText() : null);
        }
        return value;
    }

    private SelectItem[] getPredefinedValuesWithSorting() {
        SelectItem[] strings = new SelectItem[values.size()];
        int i = 0;
        int valSize = values.size() + 1;
        for (SelectItem selectItem : values) {
            if (selectItem.getId() == null) {
                selectItem.setId(valSize);
            }
            strings[i++] = selectItem;
        }
        return strings;
    }

    private boolean checkAliesValuesAreSelected() {
        for (SelectItem item : values) {
            if (item.getCategory() == null || "".equals(item.getCategory()) || "Please Select".equals(item.getCategory())) {
                return false;
            }
        }
        return true;
    }

    private String getRelationFieldValues() {
        StringBuilder strings = new StringBuilder();
        for (SelectItem item : values) {
            strings.append(item.getName()).append("=").append(item.getCategory()).append("-:-");
        }
        return "".contentEquals(strings) ? null : strings.toString();
    }

    public void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
    }

    private void changeUiType(SelectItem uiType, boolean isReference) {
        uiTypeSelectItem = uiType;
        lookUpTypeFormGroup.setVisible(false);
        customQueryBoxFormGroup.setVisible(false);
        referenceLookUpFormGroup.setVisible(isReference);

        if (uiType == null) {
            predFormGroup.setVisible(false);
            showInFilter.setEnabled(true);
            showInListing.setEnabled(true);
            showInFilter.setEnabled(true);
            return;
        }

        preview(uiType);

        predFormGroup.setVisible(Utils.hasPredefinedValue(uiType.getName()));
        if (Constants.UI_TYPE_LOOKUP.equals(uiType.getName())) {
            lookUpTypeFormGroup.setVisible(true);
            if (lookUpTypeBox.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.equals(CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription()))) {
                referenceLookUpFormGroup.setVisible(true);
            }
        }
        if (Constants.TYPE_ENTITY_LOOKUP.equals(uiType.getName()) || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(uiType.getName())) {
            customQueryBoxFormGroup.setVisible(true);
        }
        if (Constants.UI_TYPE_AUTONUMBER.equals(uiType.getName())) {
            defaultValueFormGroup.setVisible(false);
            prefixFormGroup.setVisible(true);
            minCharForm.setVisible(true);
        }
        if (UI_TYPE_URL.equals(uiType.getName()) || UI_TYPE_TEXTBOX_EMAIL.equals(uiType.getName()) || UI_TYPE_DATEPICKER.equals(uiType.getName()) || UI_TYPE_DATEPICKER_TIME.equals(uiType.getName()) || UI_TYPE_TEXTBOX.equals(uiType.getName()) || UI_TYPE_PERCENTAGE.equals(uiType.getName()) || UI_TYPE_DROPDOWN.equals(uiType.getName()) || UI_TYPE_LOOKUP.equals(uiType.getName()) || Constants.UI_TYPE_CURRENCY.equals(uiType.getName())) {
            toggleInSwitches(disabledFormGroup, true, IDX_DISABLED);
        }
        if (UI_TYPE_TEXTBOX.equals(uiType.getName()) || UI_TYPE_TEXTBOX_EMAIL.equals(uiType.getName()) || UI_TYPE_TEXTAREA.equals(uiType.getName()) || UI_TYPE_PERCENTAGE.equals(uiType.getName()) || UI_TYPE_URL.equals(uiType.getName())) {
            minCharForm.setVisible(true);
        }
        if (Constants.UI_TYPE_CURRENCY.equals(uiType.getName())) {
            req.setVisible(false);
            defaultValueFormGroup.setVisible(false);
        }
        if (Constants.UI_TYPE_MULTI_LOOKUP.equals(uiType.getName())) {
            defaultValueFormGroup.setVisible(false);
            lookUpTypeFormGroup.setVisible(true);
            showInListing.setEnabled(false);
        }
        boolean filter = Constants.UI_TYPE_DROPDOWN.equals(uiType.getName())
                || Constants.UI_TYPE_CHECKBOX.equals(uiType.getName())
                || Constants.UI_TYPE_RADIOBUTTON.equals(uiType.getName())
                || Constants.UI_TYPE_DATEPICKER.equals(uiType.getName())
                || Constants.UI_TYPE_DATEPICKER_TIME.equals(uiType.getName())
                || Constants.UI_TYPE_LOOKUP.equals(uiType.getName())
                || TYPE_ENTITY_LOOKUP.equals(uiType.getName());

        showInFilter.setEnabled(filter);
        if (!filter) {
            showInFilter.setValue(false);
        }

        chooseRelationalFormGroup.setVisible(Constants.UI_TYPE_DROPDOWN.equals(uiType.getName()));

        boolean listing = !(Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(uiType.getName())
                || Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(uiType.getName())
                || Constants.UI_TYPE_PROFILE_IMAGE_WIDGET.equals(uiType.getName()));
        showInListing.setEnabled(listing);
        if (!listing) {
            showInListing.setValue(false);
        }

        if (Constants.DATA_TYPE_NUMBER.equals(uiType.getDescription())) {
            setKeyPressHandler(predValuesBox);
            scaleGroup.setVisible(true);
            minValueGroup.setVisible(true);
            panel.setVisible(false);
        }
    }

    private void setKeyPressHandler(TextBox textBox) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (key == (char) 0) {
                return;
            }
            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE)
                    && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                    && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                    && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                    && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    private void addButtonClick() {
        if (predValuesBox.getText() != null && predValuesBox.getText().length() > 0) {
            valueTable.removeStyleName(ERROR_FORM_STYLE);
            addValueToTable(predValuesBox.getText().trim());
            predValuesBox.setText("");
            preview(uiTypeSelectItem);
        }
    }

    private void addValueToTable(String value) {
        SelectItem selectItem;
        String[] val = value.split("-:-"); // changed from "=" as it was not ability to save mathematical formulas
        int valSize = values.size();
        if (val.length > 1) {
            Integer sortVal = null;
            try {
                sortVal = val[1] != null && val[1].length() > 1 ? parseInt(val[1]) : valSize;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            selectItem = new SelectItem(sortVal != null ? sortVal : valSize, val[0], Integer.toString(valSize));
        } else {
            Integer sumValSize = values.size() + 1;
            selectItem = new SelectItem(sumValSize, val[0], Integer.toString(valSize));
        }
        addValueToTable(selectItem);
    }

    private void addValueToTable(SelectItem value) {
        values.add(value);
        valueTable.supplyProvider(values);
        valueTable.refresh();
    }

    private void getFieldValues() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCustomFieldByEntityNameAndColumnCode(entityName, fieldID, new AsyncCallback<CompanyCustomFieldItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CompanyCustomFieldItem result) {
                LoadingPanel.loading(false);
                fieldItem = result;
                objectId = result.getObjectId();
                fieldName.setText(result.getFieldName());
                aliasName.setText(result.getAliasName());
                showInListing.setValue(result.isShowInListing());
                showInFilter.setValue(result.isShowInFilterGrouping());
                disabled.setValue(result.isDisabled());
                if (disabled.getValue()) {
                    allowEditFormGroup.setVisible(true);
                    ArrayList<SelectItem> roles = new ArrayList<>();
                    for (SelectItem role : result.getAllRoles()) {
                        if (result.getRoleEdit() != null && !result.getRoleEdit().isEmpty() && result.getRoleEdit().contains(role.getId())) {
                            role.setSelected(true);
                        }
                        roles.add(role);
                    }
                    editRoleWhenDisabled.setItems(roles);
                }

                required.setValue(result.isRequired());
                minChar.setText(result.getMinChar());
                minCharCriteria.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.equal()), new SelectItem(2, wfmStrings.more()), new SelectItem(3, wfmStrings.less())});
                minCharCriteria.setSelected(new SelectItem(1));
                customQueryBox.setText(result.getQuery());
                prefixBox.setText(result.getPrefix());
                tabOptions.setValue(result.isAddTab());
                seeOwnPermission.setValue(result.isSeeOwnPermission());
                useInPermission.setValue(result.isUseInPermission());
                if (result.getLookUpTypeEnum() != null && (result.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.LOCATION) ||
                        result.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.DEPARTMENT) || result.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.POSITION))) {
                    toggleInSwitches(useInPermissionFormGroup, true, IDX_USE_PERMISSION);
                }
                if (result.getScale() != null) {
                    scale.setText(String.valueOf(result.getScale()));
                } else {
                    scale.setText(String.valueOf(2));
                }
                if (result.getNumberMinValue() != null) {
                    minValue.setText(String.valueOf(result.getNumberMinValue()));
                }
                if (fieldItem.isCustomForm() && Constants.UI_TYPE_LOOKUP.equals(result.getUiType())) {
                    toggleInSwitches(tabOptionsFormGroup, true, IDX_NEW_TAB);
                    if (result.getLookUpTypeEnum() != null && result.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.EMPLOYEE)) {
                        toggleInSwitches(seeOwnPermissionFormGroup, true, IDX_SEE_RELATED);
                    }
                }

                fieldType.setItems(getDataType(result.getDataType()));
                fieldType.setSelectedByValue(result.getUiType());
                if (result.getLookUpTypeEnum() != null) {
                    lookUpTypeBox.setSelectedByDescription(result.getLookUpTypeEnum().name());
                    if (CustomFieldLookUpTypeEnum.REFERENCE.equals(result.getLookUpTypeEnum())) {
                        referenceLookUpFormGroup.setVisible(true);
                        if (result.getReferenceItem() != null) {
                            referenceLookUp.setSelected(result.getReferenceItem());
                        }
                    }
                    if (lookUpTypeBox.getSelectedId() != null) {
                        lookUpTypeBox.setEnabled(false);
                    }
                }
                roleList.setItems(result.getRoleList());

                predFormGroup.setVisible(Utils.hasPredefinedValue(result.getUiType()));
                addScoreColumnToTable();
                HashMap<String, String> relationValuesMap = new HashMap<>();
                if (result.getRelationFieldId() != null) {
                    relationFieldId = result.getRelationFieldId();
                    getRelationItems();
                }
                if (UI_TYPE_DROPDOWN.equals(result.getUiType())) {
                    String[] relationFieldsValues = result.getRelationFieldValues() != null ? result.getRelationFieldValues().split("-:-") : new String[0];
                    for (String str : relationFieldsValues) {
                        String[] arr = str.split("=");
                        if (arr.length > 1) {
                            relationValuesMap.put(arr[0], arr[1]);
                        }
                    }
                }
                String[] val = result.getPredefinedValues() != null ? result.getPredefinedValues() : new String[0];
                SelectItem[] valSorting = result.getPredefinedValuesWithSorting() != null ? result.getPredefinedValuesWithSorting() : new SelectItem[0];
                if (valSorting.length > 0) {
                    for (SelectItem aVal : valSorting) {
                        if (aVal != null && aVal.getName() != null) {
                            addValueToTable(aVal);
                        }
                    }
                } else {
                    for (String aVal : val) {
                        if (aVal != null && aVal.length() > 0) {
                            addValueToTable(aVal);
                        }
                    }
                }

                for (SelectItem item : values) {
                    if (relationValuesMap.containsKey(item.getName())) {
                        item.setCategory(relationValuesMap.get(item.getName()));
                    }
                }
                if (isQuizCustomForm && (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType()) || UI_TYPE_CHECKBOX.equals(fieldItem.getUiType()) || UI_TYPE_RADIOBUTTON.equals(fieldItem.getUiType()))) {
                    Map<String, Double> splitedValues = new HashMap<>();
                    if (fieldItem.getQuizFormScoreValues() != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = (JSONObject) JSONParser.parseStrict(fieldItem.getQuizFormScoreValues());
                            for (String key : jsonObject.keySet()) {
                                splitedValues.put(key, jsonObject.get(key).isNumber().doubleValue());
                            }
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }
                    } else {
                        splitedValues = new HashMap<>();
                    }
                    for (SelectItem item : values) {
                        if (splitedValues.get(item.getName()) != null) {
                            item.setParam(splitedValues.get(item.getName()).toString());
                        } else {
                            item.setParam("0");
                        }
                    }
                }
                valueTable.supplyProvider(values);
                valueTable.refresh();
                changeUiType(fieldType.isSomethingSelected() ? fieldType.getSelectedItem() : null, false);
                customLogicCFModal = new CustomLogicCFModal(fieldItem.getEntityName(), fieldItem.getEntityCategoryName(), fieldItem.getCustomLogicField(), fieldItem.getCustomLogicValue());
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        valueTable.removeStyleName(ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(fieldName)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(aliasName)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(fieldType)) {
            errors++;
        }
        if (minChar.getText() != null && !minChar.getText().equals("") && Integer.parseInt(minChar.getText()) == 0) {
            minChar.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }

        if (fieldType.getSelectedItem() != null) {
            if (Utils.hasPredefinedValue(fieldType.getSelectedItem().getName())) {
                if (getPredefinedValues() == null || getPredefinedValues().length == 0) {
                    Validation.validateTextBoxRequired(predValuesBox);
                    valueTable.addStyleName(ERROR_FORM_STYLE);
                    errors++;
                }
            }
            if (Constants.UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getName()) || Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getName())) {
                if (!Validation.validateDataListBoxRequired(lookUpTypeBox)) {
                    errors++;
                } else if (CustomFieldLookUpTypeEnum.REFERENCE.equals(CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription())) && !Validation.validateLookUpRequired(referenceLookUp)) {
                    errors++;
                }
            }
            if (Constants.TYPE_ENTITY_LOOKUP.equals(fieldType.getSelectedItem().getName()) || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getName())) {
                if (!Validation.validateTextBoxRequired(customQueryBox)) {
                    errors++;
                }
            }
            if (Constants.UI_TYPE_AUTONUMBER.equals(fieldType.getSelectedItem().getName())) {
                if (!Validation.validateTextBoxRequired(prefixBox)) {
                    errors++;
                }
            }
            if (UI_TYPE_URL.equals(fieldType.getSelectedItem().getName())) {
                TextBox defaultValue = (TextBox) defaultValueFormGroup.getGroupContent().getWidget(0);
                if (defaultValue.getText() != null && defaultValue.getText().length() > 0 && !Validation.validateUrl(defaultValue, null)) {
                    errors++;
                }
            }
            if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldType.getSelectedItem().getName())) {
                TextBox defaultValue = (TextBox) defaultValueFormGroup.getGroupContent().getWidget(0);
                if (defaultValue.getText() != null && defaultValue.getText().length() > 0 && !Validation.validateEmailRequired(defaultValue)) {
                    errors++;
                }
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String[] getPredefinedValues() {
        if (!values.isEmpty()) {
            String[] strings = new String[values.size()];
            int i = 0;
            values.sort(Comparator.comparing(SelectItem::getId));
            for (SelectItem selectItem : values) {
                strings[i++] = selectItem.getName();
            }
            return strings;
        } else {
            return null;
        }
    }

    private void preview(SelectItem fieldType) {
        String uiType = fieldType.getName();
        String dataType = fieldType.getDescription();
        Widget widget = null;
        DataListBox widgetListBox = null;

        switch (uiType) {
            case UI_TYPE_TEXTBOX: {
                widget = new TextBox();
                ((TextBox) widget).setText(fieldItem.getDefaultValue() != null ? fieldItem.getDefaultValue() : "");
                if (Constants.DATA_TYPE_NUMBER.equals(dataType)) {
                    Validation.addNumericKeyboardListener((TextBox) widget);
                }
                break;
            }
            case Constants.UI_TYPE_PERCENTAGE: {
                widget = new TextBox();
                ((TextBox) widget).setText(fieldItem.getDefaultValue() != null ? fieldItem.getDefaultValue() : "");
                Validation.addPercentageNumericKeyboardListener((TextBox) widget, 100, (double) 100);
                break;
            }
            case UI_TYPE_TEXTBOX_EMAIL: {
                widget = new TextBox();
                ((TextBox) widget).setText(fieldItem.getDefaultValue() != null ? fieldItem.getDefaultValue() : "");
                Validation.validateEmailRequired((TextBox) widget);
                break;
            }
            case UI_TYPE_URL: {
                widget = new TextBox();
                ((TextBox) widget).setText(fieldItem.getDefaultValue() != null ? fieldItem.getDefaultValue() : "");
                break;
            }
            case Constants.UI_TYPE_DROPDOWN: {
                widget = new DataListBox();
                ((DataListBox) widget).setItems(values.toArray(new SelectItem[]{}));
                if (fieldItem.getDefaultValue() != null) {
                    ((DataListBox) widget).setSelectedByValue(fieldItem.getDefaultValue());
                }
                break;
            }
            case Constants.UI_TYPE_CHECKBOX: {
                widget = new FlexTable();
                widget.addStyleName("customwidget custom-checkbox");
                if (values != null && values.size() > 0) {
                    int i = 0, j = 0;
                    for (SelectItem predefinedValue : values) {
                        KpiCheckBox prevRB = new KpiCheckBox(predefinedValue.getName());
                        prevRB.addStyleName("custom-checkbox_" + i + "_" + j);
                        ((FlexTable) widget).setWidget(j, i, prevRB);

                        if (fieldItem.getDefaultValue().contains(predefinedValue.getName())) {
                            prevRB.setValue(true);
                        }

                        if (i == 2) {
                            j++;
                            i = 0;
                        } else {
                            i++;
                        }
                    }
                }
                break;
            }
            case Constants.UI_TYPE_RADIOBUTTON: {
                widget = new DataListBox();
                widget.addStyleName("customwidget custom-radios");
                ((DataListBox) widget).setItems(values.toArray(new SelectItem[]{}));
                if (fieldItem.getDefaultValue() != null) {
                    ((DataListBox) widget).setSelectedByValue(fieldItem.getDefaultValue());
                }
//                widget = new FlexTable();
//                widget.addStyleName("customwidget custom-radios");
//                if (values != null) {
//                    int i = 0, j = 0;
//                    for (SelectItem predefinedValue : values) {
//                        KpiRadioButton prevRB = new KpiRadioButton("rb", predefinedValue.getName());
//                        prevRB.addStyleName("custom-checkbox_" + i + "_" + j);
//                        ((FlexTable) widget).setWidget(j, i, prevRB);
//
//                        if (fieldItem.defaultValues().contains(predefinedValue.getName())) {
//                            prevRB.setValue(true);
//                        }
//
//                        if (i == 2) {
//                            j++;
//                            i = 0;
//                        } else {
//                            i++;
//                        }
//                    }
//                }
                break;
            }
            case Constants.UI_TYPE_DATEPICKER: {
                widgetListBox = new DataListBox();
                widgetListBox.setWithoutNullLabel(true);
                widgetListBox.setItems(new SelectItem[]{
                        new SelectItem(1, wfmStrings.equal(), "EQUAL"),
                        new SelectItem(2, wfmStrings.today(), "TODAY"),
                        new SelectItem(3, wfmStrings.tomorrow(), "TOMORROW"),
                        new SelectItem(4, wfmStrings.yesterday(), "YESTERDAY")
                });
                widgetListBox.setStyle("display: inline-block");
                widget = new DatePicker();
                widget.setWidth("50%");
                widget.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
                widget.getElement().getStyle().setFloat(Style.Float.RIGHT);
                DatePicker finalWidget = (DatePicker) widget;
                widgetListBox.addValueChangeHandler(event -> {
                    if (!event.getValue().getId().equals(1)) {
                        finalWidget.setEnabled(false);
                        finalWidget.clearSelected();
                    } else {
                        finalWidget.setEnabled(true);
                    }
                });
                if (fieldItem.getDefaultValue() != null) {
                    if (("TODAY".equals(fieldItem.getDefaultValue()) || "TOMORROW".equals(fieldItem.getDefaultValue())
                            || "YESTERDAY".equals(fieldItem.getDefaultValue()))) {

                        if ("TOMORROW".equals(fieldItem.getDefaultValue())) {
                            widgetListBox.setSelected(3);
                        } else if ("YESTERDAY".equals(fieldItem.getDefaultValue())) {
                            widgetListBox.setSelected(4);
                        } else {
                            widgetListBox.setSelected(2);
                        }
                        finalWidget.setEnabled(false);
                    } else {
                        try {
                            finalWidget.setDate(DateUtils.parse(fieldItem.getDefaultValue()));
                        } catch (DateFormatException e) {
                        }
                        widgetListBox.setSelected(1);
                    }
                }
                break;
            }
            case Constants.UI_TYPE_DATEPICKER_TIME: {
                widget = new DateTimeWidget(28);
                if (fieldItem.getDefaultValue() != null) {
                    try {
                        ((DateTimeWidget) widget).setDateTime(DateUtils.parseLongFormat(fieldItem.getDefaultValue()));
                    } catch (DateFormatException e) {
                    }
                }
                break;
            }
            case Constants.UI_TYPE_FILE_UPLOAD_WIDGET: {
                widget = new GeneralFileUpload(Constants.F_CUSTOM_FIELD_ITEM, null, null);
                widget.setStyleName("pg_custom__preview_upload_table custom-fileUpload");
                break;
            }
            case UI_TYPE_TEXTAREA: {
                widget = new TextArea();
                widget.addStyleName("custom-textarea");
                if (fieldItem.getDefaultValue() != null) {
                    ((TextArea) widget).setText(fieldItem.getDefaultValue());
                }
                break;
            }
            case Constants.UI_TYPE_HTML_TEXTAREA: {
                widget = new KpiEditor();
                widget.addStyleName("custom-textarea");
                if (fieldItem.getDefaultValue() != null) {
                    ((KpiEditor) widget).setData(fieldItem.getDefaultValue());
                }
                break;
            }
            case Constants.UI_TYPE_LOOKUP: {
                CompanyCustomFieldItem customFieldItem = new CompanyCustomFieldItem();
                customFieldItem.setLookUpTypeEnum(lookUpTypeBox.getSelectedItem() != null ? CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription()) : null);
                if (lookUpTypeBox.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.equals(CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription()))) {
                    customFieldItem.setReferenceItem(referenceLookUp.getSelectedItem());
                }
                widget = new CustomFieldLookUp(customFieldItem);

                if (lookUpTypeBox.getSelectedItem() != null) {
                    CustomFieldLookUpTypeEnum typeEnum = CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription());
                    if (typeEnum.equals(CustomFieldLookUpTypeEnum.EMPLOYEE) || typeEnum.equals(CustomFieldLookUpTypeEnum.POSITION) ||
                            typeEnum.equals(CustomFieldLookUpTypeEnum.LOCATION) || typeEnum.equals(CustomFieldLookUpTypeEnum.DEPARTMENT)) {
                        ((CustomFieldLookUp) widget).addItem(new SelectItem(0, "Current Value"));
                    }
                }
                widget.addStyleName("custom-lookup");
                if (fieldItem.getDefaultValue() != null && fieldItem.getDefaultValue().length() > 0) {
                    if (!"Type here to search...".equals(fieldItem.getDefaultValue())) {
                        Integer lookUpId = parseInt(fieldItem.getDefaultValue().substring(0, fieldItem.getDefaultValue().indexOf(Constants.DELIMITR)));
                        ((CustomFieldLookUp) widget).setSelected(new SelectItem(lookUpId, fieldItem.getDefaultValue().substring(fieldItem.getDefaultValue().indexOf(Constants.DELIMITR) + 3)));
                    }
                }
                break;
            }
            case Constants.TYPE_ENTITY_LOOKUP: {
                widget = new EntityCustomFieldLookUp(customQueryBox.getText());
                widget.addStyleName("custom-lookup");
                break;
            }
            case Constants.TYPE_ENTITY_MULTI_LOOKUP: {
                widget = new EntityCustomFieldMultiLookUp(customQueryBox.getText());
                widget.addStyleName("custom-lookup");
                break;
            }
            case Constants.UI_TYPE_AUTONUMBER: {
                widget = new HTML();
                break;
            }
            case Constants.UI_TYPE_CURRENCY: {
                widget = new HTML();
                break;
            }
            case Constants.UI_TYPE_MULTI_LOOKUP: {
                widget = new HTML();
                break;
            }
        }
        defaultValueFormGroup.getGroupContent().clear();
        if (widgetListBox != null) {
            defaultValueFormGroup.addToContent(widgetListBox);
        }
        if (widget != null) {
            defaultValueFormGroup.addToContent(widget);

            if (Constants.UI_TYPE_HTML_TEXTAREA.equals(uiType)) {
                TextBox minHeight = new TextBox();
                if (fieldItem.getMinHeight() != null) {
                    minHeight.setText(fieldItem.getMinHeight());
                }
                Validation.addNumericKeyboardListener(minHeight);
                minHeight.setPlaceHolder(wfmStrings.height());
                defaultValueFormGroup.addToContent(minHeight);
            }
        }
    }

    private void setSelectedItemToRelationDropDown() {
        for (SelectItem item : setRelationCustomForm.getItems()) {
            if (item != null && (item.getId().equals(relationFieldId))) {
                setRelationCustomForm.setSelected(item);
                setRelationFields(item);
            }
        }
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }
}
