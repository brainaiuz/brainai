package com.edatasite.workforce.gwt.profile.client.ui.view;


import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CustomLogicCFModal;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroupAppend;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabBar;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 06-Nov-2010
 * Time: 14:09:10
 */
public abstract class AbstractAddCustomFieldsView extends FooteredView implements Constants, FittedContent {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final CommonServiceAsync commonService = CommonService.App.get();

    private SelectItem[] uiTypes;

    protected WfmForm table;
    private WfmForm tableHeader;
    private WfmForm.Field entityName;
    private WfmForm.Field entityCategoryName;
    private WfmForm.Field fieldName;
    private WfmForm.Field aliasName;
    private WfmForm.Field dataType;
    private WfmForm.Field uiType;
    //    private WfmForm.Field predValues;
    private VerticalPanel queryVp;
    private WfmForm.Field showsField;

    private TextBox fieldNameBox;
    private TextBox aliasNameBox;
    private TextBox predValuesBox;
    private TextArea customQueryBox;
    private DataListBox entityNameBox;
    private DataListBox categoryNameBox;
    private DataListBox dataTypeBox;
    private DataListBox uiTypeBox;
    private DataListBox localeBox;
    private DataListBox lookUpTypeBox;
    private ReferenceLookUp referenceLookUp;
    private KpiCheckBox showInListing;
    private KpiCheckBox isRadiobutton;
    private KpiCheckBox withClickable;
    private KpiCheckBox showInFilterGrouping;
    private KpiCheckBox isFacetable;
    private KpiSwitcher isRequired;
    private MultiTable showTo;
    private WfmButton2 saveClose;
    private WfmButton2 saveAnother;
    private WfmButton2 addButton;
    private WfmButton2 queryButton;
    private WfmButton2 delete;
    protected ArrayList<Widget> buttons = new ArrayList<>();
    private CustomFieldPreviewTab cfPreviewTab;
    private CustomTabBar coBar;
    private VerticalPanel vPanel;
    private FormGroup predefinedValuesFormGroup;
    private FormGroup queryFormGroup;
    private FormGroup lookUpTypeGroup;
    private FormGroup referenceGroup;
    protected FlexTable generalTable;
    protected VerticalPanel customFieldsContent;
    private KpiDataGrid<SelectItem> valueTable;

    private String[] stringItems;
    private String[] numberItems;
    private String[] dateItems;
    private String[] allItems;
    private boolean saveAndClose = false;
    private String uiTypeValue;
    protected Integer companyID;
    protected Integer relationship = null;
    protected String category = null;
    protected Integer objectID;
    private final ArrayList<SelectItem> values = new ArrayList<>();
    private final Map<String, ArrayList<SelectItem>> valuesMap = new HashMap<>();
    private DataListBox entityType;
    protected Command commandProvider;
    protected VerticalPanelDiv pnlDialogContainer;
    protected boolean isItemTableField;
    protected MaterialPanel buttonPanel;

    CompanyCustomFieldItem companyCustomField;
    String customFieldArea = null;
    private ArrayList<SelectItem> rolesList = new ArrayList<>();
    private CustomLogicCFModal customLogicCFModal;

    public AbstractAddCustomFieldsView(String viewName, String description) {
        super(viewName, description);
    }

    /**
     * @return Content Owner widget
     */
    protected Widget onInitialize() {
        initInternal();
        return null;
    }

    /**
     * @return Custom Fields Form Name
     */
    protected abstract String getFormName();

    /**
     * Relates name this is Custom field add View Name
     *
     * @return Relates Names
     */
    protected abstract SelectItem[] getRelatesToNames();

    /**
     * Initialization in forms widgets
     */

    protected void initInternal() {
        entityType = new DataListBox();
        entityType.setId("entityType");
        entityType.addStyleName(DEFAULT_WIDTH);
        profileService.getEntityTypes(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                entityType.setItems(result);
                if (companyCustomField != null) {
                    entityType.setSelected(companyCustomField.getEntityType());
                }
            }
        });
        generalTable = new FlexTable();
        uiTypes = getUiTypes();

        table = new WfmForm();
        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        cfPreviewTab = new CustomFieldPreviewTab(wfmStrings.preview());
        cfPreviewTab.setScroll(true);
        cfPreviewTab.setStyleName("pg_custom_field_preview");

        coBar = new CustomTabBar(1);
        coBar.addWidget(cfPreviewTab);
        coBar.setPanelSize("100%", "230px");
        coBar.selectTab(0);


        entityNameBox = new DataListBox();
        entityNameBox.setId("entityNameBox");
        entityNameBox.addStyleName(DEFAULT_WIDTH);
        entityNameBox.setNullLabel(wfmStrings.pleaseSelect());
        entityNameBox.setItems(getRelatesToNames());

        if (relationship != null || isItemTableField) {
            entityNameBox.setSelected(0);
        }

        categoryNameBox = new DataListBox();
        categoryNameBox.setId("categoryNameBox");
        categoryNameBox.addStyleName(DEFAULT_WIDTH);
        categoryNameBox.setVisible(false);
        categoryNameBox.setNullLabel(wfmStrings.pleaseSelect());

        fieldNameBox = new TextBox();
        fieldNameBox.setMaxLength(200);
        fieldNameBox.getElement().setId("fieldNameBox");
        fieldNameBox.addStyleName(DEFAULT_WIDTH);
        fieldNameBox.setEnabled(false);

        aliasNameBox = new TextBox();
        aliasNameBox.getElement().setId("aliasNameBox");
        aliasNameBox.addStyleName(DEFAULT_WIDTH);
        aliasNameBox.setEnabled(false);

        vPanel = new VerticalPanel();
        predefinedValuesFormGroup = new FormGroup(wfmStrings.predefinedValues(), vPanel, true);

        uiTypeBox = new DataListBox();
        uiTypeBox.setId("uiTypeBox");
        uiTypeBox.addStyleName(DEFAULT_WIDTH);
        uiTypeBox.setItems(uiTypes);
        uiTypeBox.setEnabled(false);

        dataTypeBox = new DataListBox();
        dataTypeBox.setId("dataTypeBox");
        dataTypeBox.addStyleName(DEFAULT_WIDTH);
        dataTypeBox.setEnabled(false);

        customQueryBox = new TextArea();
        customQueryBox.addStyleName(DEFAULT_WIDTH);

        predValuesBox = new TextBox();
        predValuesBox.setMaxLength(1000);
//        predValuesBox.setSize("100%", "40px");
        valueTable = new KpiDataGrid<>(KEY_PROVIDER);
        valueTable.setStyleName("cellBasedWidget-mod");
        valueTable.setSize("100%", "300px");
        valueTable.setVisible(false);
        Column<SelectItem, String> objectIdCell = new Column<SelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return object.getDescription();
            }
        };
        valueTable.addColumn(objectIdCell, "Id");
        valueTable.setColumnWidth(objectIdCell, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<SelectItem, String> value = new Column<SelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return object.getName();
            }
        };
        valueTable.addColumn(value, wfmStrings.value());
        valueTable.setColumnWidth(value, 25, com.google.gwt.dom.client.Style.Unit.PCT);

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
                if (!value12.equals("0") && !value12.equals("")) {
                    TextBox textBox = new TextBox();
                    textBox.setText(value12);
                    Validation.numberValidationWithoutDot(textBox);
                    if (textBox != null && "".equals(textBox.getText())) {
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                        valueTable.redraw();
                    }
                    object.setId(Integer.parseInt(textBox.getText()));
                } else {
                    object.setId(Integer.parseInt(value12));
                }
            } catch (NumberFormatException ex) {
                object.setId(1);
                valueTable.redraw();
            }
            valueTable.redraw();
            cfPreviewTab.setPredefinedValues(getPredefinedValues());
            cfPreviewTab.initData();
        });
        valueTable.addColumn(sorder, wfmStrings.sortOrder());
        valueTable.setColumnWidth(sorder, 35, com.google.gwt.dom.client.Style.Unit.PCT);

        final Column<SelectItem, String> action = new Column<SelectItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return wfmStrings.delete();
            }
        };
        action.setFieldUpdater((index, object, value1) -> {
            values.remove(object);
            valueTable.supplyProvider(values);
            valueTable.refresh();
            cfPreviewTab.setUiType(uiTypeValue);
            cfPreviewTab.setPredefinedValues(getPredefinedValues());
            cfPreviewTab.initData();
        });
        valueTable.addColumn(action, wfmStrings.action());
        valueTable.setColumnWidth(action, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        final Column<SelectItem, String> roles = new Column<SelectItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return wfmStrings.roles();
            }
        };
        roles.setFieldUpdater((index, object, value1) -> new DropDownPermissionSideNavBox(objectID, object.getName()));
        valueTable.addColumn(roles, wfmStrings.action());
        valueTable.setColumnWidth(roles, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        localeBox = new DataListBox();
        localeBox.addStyleName(DEFAULT_WIDTH);
        localeBox.setWithoutNullLabel(true);
        SelectItem defaultItem = new SelectItem(null, "English", "en");
        defaultItem.setSelected(true);
        localeBox.setItems(new SelectItem[]{
                defaultItem,
                new SelectItem(0, "Spanish", "es"),
                new SelectItem(1, "French", "fr"),
                new SelectItem(2, "Portuguese", "pt")
        });
        localeBox.setSelectedDefault();
        localeBox.setVisible(false);

        lookUpTypeBox = new DataListBox();
        lookUpTypeBox.addStyleName(DEFAULT_WIDTH);
        lookUpTypeBox.setItems(CustomFieldForm.getLookUpTypes());

        referenceLookUp = new ReferenceLookUp(null);

        showInListing = new KpiCheckBox("    " + wfmStrings.showInListing());
        showInListing.setEnabled(false);

        isRadiobutton = new KpiCheckBox("    " + wfmStrings.radioButton());
        isRadiobutton.setEnabled(false);

        withClickable = new KpiCheckBox("    " + settingsStrings.clickable());
        withClickable.setVisible(false);
        showInListing.addValueChangeHandler(valueChangeEvent -> {
            if (CustomFieldSection.Lead.getTitle().equals(entityNameBox.getSelectedItem().getDescription())) {
                withClickable.setVisible(false);
                if (showInListing.getValue()) {
                    withClickable.setVisible(true);
                }
            }
        });
        showInFilterGrouping = new KpiCheckBox("    " + settingsStrings.filterGrouping());
        showInFilterGrouping.setEnabled(false);
        showInFilterGrouping.setVisible(false);

        isFacetable = new KpiCheckBox("    " + wfmStrings.showInFilter());
        isFacetable.setEnabled(false);

        isRequired = new KpiSwitcher();
        isRequired.setId("isRequired");
        isRequired.setOnLabel(wfmStrings.required());

        MultiTableWidgets multiTableWidgets = new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.addStyleName(DEFAULT_WIDTH);
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
        showTo = new MultiTable(multiTableWidgets);
        Command command = () -> setRoleItemsToListBox();
        showTo.setOnLinesAdded(command);

        profileService.getRoles(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                rolesList = result;
                setRoleItemsToListBox();
            }
        });

        addButton = new WfmButton2(wfmStrings.addValue());
        addButton.getElement().setId("addValue");

        queryButton = new WfmButton2("Execute Query");
        queryButton.getElement().setId("executeQuery");

        delete = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        delete.getElement().setId("deleteBtn");

        customFieldsContent = new VerticalPanel();
        customFieldsContent.setSpacing(5);
        customFieldsContent.addStyleName(DEFAULT_WIDTH);

        drawFormHeader();
        drawCustomFieldsForm();
        drawGeneralCustomFields();
        showView();

        if (objectID == null) {
            configureFields();
        }
        loadData();

        add(createFooter());
    }

    private void setRoleItemsToListBox() {
        for (int i = 0; i < showTo.getWidgetsMaps().size(); i++) {
            for (Widget widget : showTo.getWidgetsMaps().get(i).getWidgets()) {
                DataListBox listBox = (DataListBox) widget;
                listBox.clear();
                listBox.setItems(rolesList.toArray(new SelectItem[]{}));
            }
        }
    }

    /**
     * Show In View add Widgets
     */
    protected void showView() {

    }

    protected void configureFields() {
        if (entityNameBox.getSelectedItem() != null && ViewName.ProductCategory.name().equals(entityNameBox.getSelectedItem().getDescription())) {
            aliasName.setVisible(objectID == null);
            aliasName.setRequired(objectID == null);
            showsField.setVisible(objectID == null);
            entityNameBox.setSelectedByValue(CustomFieldSection.ProductCategory.getTitle());
            entityName.setVisible(objectID == null);
            fireEntityNameChange(companyCustomField != null ? companyCustomField.getDataType() : null);

        } else if (isItemTableField) {
            fireEntityNameChange(companyCustomField != null ? companyCustomField.getDataType() : null);

        } else if (entityNameBox.getSelectedItem() != null && ViewName.CompanySettings.name().equals(entityNameBox.getSelectedItem().getDescription())) {
            aliasName.setVisible(true);
            aliasName.setRequired(true);
            showsField.setVisible(false);
            entityName.setVisible(true);
            entityCategoryName.setVisible(false);
        } else if (companyCustomField != null && companyCustomField.getEntityName() != null && ViewName.ExpenceReportView.name().equals(companyCustomField.getEntityName())) {
            aliasName.setVisible(true);
            aliasName.setRequired(true);
            showsField.setVisible(true);
            entityName.setVisible(true);
            entityNameBox.setSelectedByValue(CustomFieldSection.ExpenseReportView.getTitle());
            entityCategoryName.setVisible(false);
        } else {
            aliasName.setVisible(true);
            aliasName.setRequired(true);
            showsField.setVisible(true);
            entityName.setVisible(true);
            entityCategoryName.setVisible(false);
        }
    }

    /**
     * View add custom fields
     */
    protected void drawViewCustomFields(SelectItem nowSelect, SelectItem prevSelect) {

    }

    private void loadData() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            profileService.getCustomFieldData(objectID, companyID, new AbstractAsyncCallback<CompanyCustomFieldItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    throwable.printStackTrace();
                }

                @Override
                public void success(CompanyCustomFieldItem cf) {
                    companyCustomField = cf;
                    if (companyCustomField != null) {
                        if (companyCustomField.getEntityType() != null) {
                            entityType.setSelected(companyCustomField.getEntityType());
                        }
                        relationship = companyCustomField.getRelationship();
                        category = companyCustomField.getEntityCategoryName();


                        entityNameBox.addListItem(new SelectItem(1, CustomFieldSection.getBySectionName(companyCustomField.getEntityName()).getTitle(), companyCustomField.getEntityName()));
                        entityNameBox.setSelectedByDescription(CustomFieldSection.getBySectionName(companyCustomField.getEntityName()).name());
                        entityNameBox.setEnabled(false);

                        fieldNameBox.setValue(companyCustomField.getFieldName());
                        aliasNameBox.setValue(companyCustomField.getAliasName());
                        uiTypeBox.setSelectedByValue(companyCustomField.getUiType());
                        uiTypeValue = companyCustomField.getUiType();
                        uiTypeBox.setEnabled(false);
                        dataTypeBox.setEnabled(false);
                        lookUpTypeBox.setEnabled(false);


                        showInListing.setValue(companyCustomField.isShowInListing());
                        withClickable.setValue(companyCustomField.isClickable());
                        if (companyCustomField.isShowInListing() && CustomFieldSection.Lead.getTitle().equals(entityNameBox.getSelectedItem().getDescription())) {
                            withClickable.setVisible(true);
                        }

//                        isRadiobutton.setValue(companyCustomField.isRadioButton());

                        showInFilterGrouping.setValue(companyCustomField.isShowInFilterGrouping());
                        isFacetable.setValue(companyCustomField.isFacetable());
                        isRequired.setValue(companyCustomField.isRequired());

                        getExistingCustomFieds(companyCustomField.getEntityName(), companyCustomField.getDataType(), companyCustomField.getEntityCategoryName());

                        String[] val = companyCustomField.getPredefinedValues() != null ? companyCustomField.getPredefinedValues() : new String[0];
                        SelectItem[] valSorting = companyCustomField.getPredefinedValuesWithSorting() != null ? companyCustomField.getPredefinedValuesWithSorting() : new SelectItem[0];
                        if (valSorting != null && valSorting.length > 0) {
                            for (SelectItem aVal : valSorting) {
                                addValueToTable(aVal);
                            }
                        } else if (val != null) {
                            for (String aVal : val) {
                                addValueToTable(aVal);
                            }
                        }
                        if (uiTypeValue.equals(UI_TYPE_TEXTBOX)) {
                            cfPreviewTab.setTitle(fieldNameBox.getText());
                        } else {
                            cfPreviewTab.setPredefinedValues(val);
                        }
                        if (uiTypeValue.equals(UI_TYPE_TEXTBOX)
                                || uiTypeValue.equals(UI_TYPE_PERCENTAGE)
                                || uiTypeValue.equals(UI_TYPE_URL)
                                || uiTypeValue.equals(UI_TYPE_RADIOBUTTON)
                                || uiTypeValue.equals(UI_TYPE_TEXTBOX_EMAIL)
                                || uiTypeValue.equals(UI_TYPE_TEXTAREA)
                                || uiTypeValue.equals(UI_TYPE_HTML_TEXTAREA)
                                || uiTypeValue.equals(UI_TYPE_DATEPICKER)
                                || uiTypeValue.equals(UI_TYPE_DATEPICKER_TIME)
                                || uiTypeValue.equals(UI_TYPE_FILE_UPLOAD_WIDGET)
                                || uiTypeValue.equals(UI_TYPE_PROFILE_IMAGE_WIDGET)
                                || uiTypeValue.equals(UI_TYPE_FILE_UPLOAD_ITEM)) {
                            predValuelRemove();
                        } else {
                            predefinedValuesFormGroup.setVisible(true);
                        }
                        if (UI_TYPE_ENTITY_DROPDOWN.equals(uiTypeValue) || TYPE_ENTITY_LOOKUP.equals(uiTypeValue) || TYPE_ENTITY_MULTI_LOOKUP.equals(uiTypeValue)) {
                            customQueryBox.setText(companyCustomField.getQuery());
                            queryFormGroup.setVisible(true);
                            predefinedValuesFormGroup.setVisible(false);
                        } else if (UI_TYPE_LOOKUP.equals(uiTypeValue) || UI_TYPE_MULTI_LOOKUP.equals(uiTypeValue)) {
                            if (companyCustomField.getLookUpTypeEnum() != null) {
                                lookUpTypeBox.setSelectedByDescription(companyCustomField.getLookUpTypeEnum().name());
                                if (CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomField.getLookUpTypeEnum())) {
                                    referenceLookUp.setSelected(companyCustomField.getReferenceItem());
                                    referenceGroup.setVisible(true);
                                }
                            }
                            lookUpTypeGroup.setVisible(true);
                            predefinedValuesFormGroup.setVisible(false);
                        }
                        cfPreviewTab.setUiType(companyCustomField.getUiType());
                        cfPreviewTab.setTitle(companyCustomField.getFieldName());
                        cfPreviewTab.initData();

                        if (companyCustomField.getAllowedRoles() != null && !companyCustomField.getAllowedRoles().isEmpty()) {
                            showTo.removeAllRows();
                            showTo.getWidgetsMaps().clear();
                            for (Integer roleID : companyCustomField.getAllowedRoles()) {
                                showTo.onAddLinkClicked();
                            }
                            for (int i = 0; i < companyCustomField.getAllowedRoles().size(); i++) {
                                ((DataListBox) showTo.getWidgetsMaps().get(i).getWidgets()[0]).setSelected(companyCustomField.getAllowedRoles().get(i));
                            }
                        }

                        configureFields();
                        customLogicCFModal = new CustomLogicCFModal(companyCustomField.getEntityName(), companyCustomField.getEntityCategoryName(), companyCustomField.getCustomLogicField(), companyCustomField.getCustomLogicValue());
                    }
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void setSelectedetCategory() {
        categoryNameBox.setSelectedByDescription(companyCustomField.getEntityCategoryName());
//        for (SelectItem citem : categoryNameBox.getItems()) {
//            if (companyCustomField.getEntityCategoryName().equals(citem.getDescription())) {
//                categoryNameBox.setSelected(citem.getId());
//                break;
//            }
//        }
        categoryNameBox.setEnabled(false);
    }

    /**
     * Darw in Form Header Title
     */
    private void drawFormHeader() {
        tableHeader = new WfmForm(new String[]{"100%"});
        tableHeader.setStyleName("box-bg--1 box-radius--top custom_field_title");
        if (pnlDialogContainer != null) {
            pnlDialogContainer.add(tableHeader);
            pnlDialogContainer.add(new HTML("<div class=line></div>"));
        } else {
            add(new HTML("<div class=container-header>" + tableHeader + "</div>"));
            add(new HTML("<div class=line></div>"));
        }
    }

    /**
     * Draw Custom Fields Form UI
     */
    private void drawCustomFieldsForm() {
        registrationsFieldEntityNameEvent();
        registrationsFieldNameEvent();
        registrationsUITypeFieldEvent();
        registrationsDataTypeFieldEvent();
        registrationsLocaleBox();
        registrationsLookUpBoxEvent();
        registrationsReferenceLookUpEvent();

        predValuesBox.addKeyboardListener(enterAdapter);

        final VerticalPanel pvPanel = new VerticalPanel();
        pvPanel.addStyleName(DEFAULT_WIDTH);
        pvPanel.add(valueTable);
        valueTable.getScrollPanel().getElement().getParentElement().setPropertyString("style", "margin-top: 29px");

        addButton.addClickHandler(baseEvent -> addButtonClick());

        queryButton.addClickHandler(event -> executeQuery());

//        HorizontalPanel hPanel = new HorizontalPanel();
        InputGroupAppend addButtonIGA = new InputGroupAppend(addButton, false);
        InputGroup predValuesIG = new InputGroup(predValuesBox, addButtonIGA);
//        hPanel.add(predValuesBox);
//        hPanel.add(new HTML("&nbsp"));
//        hPanel.add(addButton);
//        vPanel.add(hPanel);
        vPanel.add(predValuesIG);

        HorizontalPanel customQueryPanel = new HorizontalPanel();
        customQueryPanel.add(customQueryBox);
        customQueryPanel.add(new HTML("&nbsp"));
        customQueryPanel.add(queryButton);
        customQueryPanel.setCellWidth(queryButton, "127");
        queryVp = new VerticalPanel();
        queryVp.add(customQueryPanel);
        queryVp.add(entityType);
        queryFormGroup = new FormGroup(wfmStrings.queryPanel(), queryVp);
        HorizontalPanel space = new HorizontalPanel();
        space.setHeight("4px");
        vPanel.add(space);
        vPanel.add(pvPanel);

        final HorizontalPanel showPanel = new HorizontalPanel();
        showPanel.add(showInListing);
        showPanel.add(new HTML("&nbsp &nbsp &nbsp &nbsp &nbsp"));
        showPanel.add(withClickable);
        showPanel.add(new HTML("&nbsp &nbsp &nbsp &nbsp &nbsp"));
        showPanel.add(isRadiobutton);
        showPanel.add(new HTML("&nbsp &nbsp &nbsp &nbsp &nbsp"));

        if (!settingsStrings.addCRMCustomFields().equals(getFormName())) {
            showPanel.add(showInFilterGrouping);
            showPanel.add(new HTML("&nbsp &nbsp &nbsp &nbsp &nbsp"));
        }
        showPanel.add(isFacetable);

        final VerticalPanel showsPanel = new VerticalPanel();
        showsPanel.add(showPanel);

        lookUpTypeGroup = new FormGroup(wfmStrings.value(), lookUpTypeBox);
        referenceGroup = new FormGroup(wfmStrings.referencces(), referenceLookUp);

        table.setStyleName("pg_custom_field");
        entityName = table.addField(settingsStrings.relatesTo(), entityNameBox);
        entityCategoryName = table.addField(wfmStrings.byCategory(), categoryNameBox, true);
        fieldName = table.addField(wfmStrings.fieldName(), fieldNameBox, true);
        aliasName = table.addField(wfmStrings.aliasName(), aliasNameBox, true);
        dataType = table.addField(wfmStrings.dataType(), dataTypeBox, true);
        uiType = table.addField(wfmStrings.fieldType(), uiTypeBox, true);

        table.addField("", isRequired, false);
        if (!isItemTableField) {
            showsField = table.addField(settingsStrings.showIn(), showsPanel, false);
        }
        table.addField(wfmStrings.visibleTo(), showTo, false);

        entityName.setVisible(!isItemTableField);
        predefinedValuesFormGroup.setVisible(false);
        queryFormGroup.setVisible(false);
        lookUpTypeGroup.setVisible(false);
        referenceGroup.setVisible(false);

    }

    /**
     * Put Widgets to table positions
     */
    private void drawGeneralCustomFields() {
        generalTable.addStyleName("box-bg--1 pg_custom_field_marg");
        generalTable.setCellPadding(5);
        generalTable.setCellSpacing(5);
        generalTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        generalTable.setWidget(0, 0, table);
        Div div = new Div();
        div.setPaddingRight(30);
        div.add(coBar);
        coBar.addStyleName("mb-4");
        div.add(predefinedValuesFormGroup);
        div.add(queryFormGroup);
        div.add(lookUpTypeGroup);
        div.add(referenceGroup);
        div.add(valueTable);
        generalTable.setWidget(0, 1, div);

        generalTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        generalTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        generalTable.getFlexCellFormatter().setWidth(0, 1, "50%");

        if (pnlDialogContainer != null) {
            pnlDialogContainer.add(generalTable);
            pnlDialogContainer.add(new HTML("<div class=line></div>"));
            pnlDialogContainer.add(customFieldsContent);
        } else {
            add(generalTable);
            add(new HTML("<div class=line></div>"));
            add(customFieldsContent);
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AbstractAddCustomFieldsView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AbstractAddCustomFieldsView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

//        if (saveClose != null) {
//            return;
//        }
        saveClose = new WfmButton2(wfmStrings.save(), Constants.BTN_PRIMARY);
        saveClose.getElement().setId("saveClose");
        saveClose.addClickHandler(sender -> {
            saveAndClose = true;
            saveData();
        });

        saveAnother = new WfmButton2(wfmStrings.saveAndNew(), Constants.BTN_PRIMARY);
        saveAnother.ensureDebugId("saveAdd");
        saveAnother.addClickHandler(sender -> saveData());

        delete.addClickHandler(sender -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(settingsStrings.areYouSureWantRemoveCustomField());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    profileService.deleteCustomField(objectID, companyID, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        @Override
                        public void success(Void aVoid) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, null, null);
                            if (pnlDialogContainer != null) {
                                closeDialog();
                            } else {
                                closeTab();
                            }
                        }
                    });
                }
            });
            messageBox.open();
        });

        Div saveCloseWrapper = new Div();
        saveCloseWrapper.add(saveClose);

        Div saveAnotherWrapper = new Div();
        saveAnotherWrapper.add(saveAnother);

        Div deleteWrapper = new Div();
        deleteWrapper.add(delete);

        if (pnlDialogContainer != null) {
            if (objectID != null) {
                rightSideWidgets.add(deleteWrapper);
            } else {
                rightSideWidgets.add(saveAnotherWrapper);
            }
            rightSideWidgets.add(saveCloseWrapper);
        } else {
            rightSideWidgets.add(saveCloseWrapper);
            if (objectID != null) {
                rightSideWidgets.add(deleteWrapper);
            } else {
                rightSideWidgets.add(saveAnotherWrapper);
            }
        }
        if (objectID != null) {
            WfmButton2 customLogic = new WfmButton2(wfmStrings.customLogic(), clickEvent -> customLogicCFModal.center());
            rightSideWidgets.add(customLogic);
        }

        return rightSideWidgets;
    }

    private void saveData() {
        String entityName = entityNameBox.getSelectedItem() != null ? entityNameBox.getSelectedItem().getDescription() : "";
        String categoryName = (categoryNameBox != null && categoryNameBox.getSelectedItem() != null) ? categoryNameBox.getSelectedItem().getDescription() : null;
        commonService.checkCFNameExists(entityName, categoryName, fieldNameBox.getText(), aliasNameBox.getText(), objectID, false, null, new AbstractAsyncCallback<CompanyCustomFieldItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCustomFieldItem result) {
                if (!validate()) {
                    enableButtons(true);
                } else {
                    if ((!predefinedValuesFormGroup.isVisible() && !result.isFieldNameExists() && !result.isAliasNameExists()) || (predefinedValuesFormGroup.isVisible() && valueTable.getList().size() > 0)) {
                        saveCustomFields();
                    } else if (result.isFieldNameExists() || result.isAliasNameExists()) {
                        Info.show(wfmStrings.customFieldNameExists(), Info.Type.WARNING);
                    }
                }
            }
        });
    }

    /**
     * Registrations Field Entity DataListBox Event
     */
    private void registrationsFieldEntityNameEvent() {
        entityNameBox.addValueChangeHandler(event -> fireEntityNameChange(null));
        categoryNameBox.addValueChangeHandler(event -> fireEntityNameChange(null));
    }

    private void fireEntityNameChange(String dataType) {
        if (entityNameBox.getSelectedIndex() != 0) {
            uiTypeBox.setSelectedNullLabel();
            String categoryName = (categoryNameBox != null && categoryNameBox.getSelectedItem() != null) ? categoryNameBox.getSelectedItem().getDescription() : null;
            if (dataType == null) {
                getExistingCustomFieds(entityNameBox.getSelectedItem().getDescription(), categoryName);
                if (entityNameBox.isSomethingSelected() && ViewName.CompanySettings.name().equals(entityNameBox.getSelectedItem().getDescription())) {
                    showsField.setVisible(false);
                } else if (!isItemTableField) {
                    showsField.setVisible(true);
                }
            } else {
                getExistingCustomFieds(entityNameBox.getSelectedItem().getDescription(), dataType, categoryName);
            }

        } else {
            fieldNameBox.setEnabled(false);
            uiTypeValue = null;
            fieldNameBox.setText("");
            aliasNameBox.setEnabled(false);
            aliasNameBox.setText("");
            uiTypeBox.setEnabled(false);
            uiTypeBox.setSelectedNullLabel();
            dataTypeBox.setEnabled(false);
            dataTypeBox.setSelectedNullLabel();
            showInListing.setEnabled(false);
            showInFilterGrouping.setEnabled(false);
            isFacetable.setEnabled(false);
            isRequired.setEnabled(false);
            aliasName.setVisible(true);
            lookUpTypeBox.setEnabled(false);
            referenceLookUp.setEnabled(false);
            predValuelRemove();
        }
        drawViewCustomFields(entityNameBox.getSelectedItem(), entityNameBox.getPreviousSelectedItem());
    }

    /**
     * Pred Value not null remove in table
     */
    private void predValuelRemove() {
        predefinedValuesFormGroup.setVisible(false);
//        uiTypeValue = null;
    }

    /**
     * Registrations Field name Text Box Event
     */
    private void registrationsFieldNameEvent() {
        fieldNameBox.addKeyUpHandler(changeEvent -> {
            //cfPreviewTab.setUiType(uiTypeValue);
            cfPreviewTab.setTitle(fieldNameBox.getText());
            cfPreviewTab.setPredefinedValues(getPredefinedValues());
            cfPreviewTab.initData();
        });
    }

    /**
     * Registrations UIType DataListBox Event
     */
    private void registrationsUITypeFieldEvent() {
        uiTypeBox.addValueChangeHandler(changeEvent -> {
            onUiTypeBoxChange();
        });
    }

    private void onUiTypeBoxChange() {
        if (uiTypeBox.getSelectedIndex() != 0) {
            isFacetable.setEnabled(false);
            isFacetable.setValue(companyCustomField != null && companyCustomField.isFacetable());
            predValuesBox.setText("");
            if (uiTypeBox.getSelectedItem() != null) {
                uiTypeValue = uiTypeBox.getSelectedItem() != null ? uiTypeBox.getSelectedItem().getName() : "";
            }

            String dataTypeValue = dataTypeBox.getSelectedItem() != null ? dataTypeBox.getSelectedItem().getName() : "";
            cfPreviewTab.setUiType(uiTypeValue);
            cfPreviewTab.setDataType(dataTypeValue);
            String[] val = getPredefinedValues();
            if (uiTypeBox.getSelectedItem() != null && (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX_EMAIL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PERCENTAGE)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_URL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_RADIOBUTTON)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTAREA)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_HTML_TEXTAREA))) {
                cfPreviewTab.setTitle(fieldNameBox.getText());
            } else {
                cfPreviewTab.setPredefinedValues(val);
            }
            queryFormGroup.setVisible(false);
            lookUpTypeGroup.setVisible(false);
            referenceGroup.setVisible(false);
            valueTable.setVisible(false);
            if (uiTypeBox.getSelectedItem() != null
                    && (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PERCENTAGE)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_URL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_RADIOBUTTON)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX_EMAIL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER_TIME)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTAREA)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_HTML_TEXTAREA)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_WIDGET)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PROFILE_IMAGE_WIDGET)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_ITEM))
            ) {
                predValuelRemove();
            } else if (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_ENTITY_DROPDOWN) || uiTypeBox.getSelectedItem().getName().equals(TYPE_ENTITY_LOOKUP) || uiTypeBox.getSelectedItem().getName().equals(TYPE_ENTITY_MULTI_LOOKUP)) {
                queryFormGroup.setVisible(true);
            } else if (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_LOOKUP) || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_MULTI_LOOKUP)) {
                String lookUpType = lookUpTypeBox.getSelectedItem() != null ? lookUpTypeBox.getSelectedItem().getDescription() : null;
                cfPreviewTab.setLookUpType(lookUpType);
                lookUpTypeGroup.setVisible(true);
                if (CustomFieldLookUpTypeEnum.REFERENCE.name().equals(lookUpType)) {
                    referenceGroup.setVisible(true);
                    cfPreviewTab.setReferenceId(referenceLookUp.getSelectedItemID());
                }
            } else {
                localeBox.setVisible(uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DROPDOWN) && entityNameBox.getSelectedItem().getDescription().equals(ViewName.ProductServiceView.name()));
                predefinedValuesFormGroup.setVisible(true);
            }
            if (uiTypeBox.getSelectedItem() != null) {
                valueTable.setVisible(UI_TYPE_DROPDOWN.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_RADIOBUTTON.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_CHECKBOX.equals(uiTypeBox.getSelectedItem().getName()));
            }
            if (uiTypeBox.getSelectedItem() != null) {
                isFacetable.setEnabled(UI_TYPE_DROPDOWN.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_RADIOBUTTON.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_CHECKBOX.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_DATEPICKER.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_DATEPICKER_TIME.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_LOOKUP.equals(uiTypeBox.getSelectedItem().getName())
                        || UI_TYPE_MULTI_LOOKUP.equals(uiTypeBox.getSelectedItem().getName()));
            } else {
                isFacetable.setEnabled(false);
            }
            cfPreviewTab.initData();
        } else {
            predValuelRemove();
            isFacetable.setEnabled(false);
            isFacetable.setValue(false);
        }
    }

    /**
     * Registrations Data Type DataListBox Event
     */
    private void registrationsDataTypeFieldEvent() {
        dataTypeBox.addValueChangeHandler(changeEvent -> {
            onDataTypeBoxChanged();
        });
    }

    private void onDataTypeBoxChanged() {
        if (dataTypeBox.getSelectedItem() != null) {
            isFacetable.setEnabled(false);
            isFacetable.setValue(companyCustomField != null && companyCustomField.isFacetable());
            uiTypeBox.setEnabled(true);
            for (SelectItem uiType1 : uiTypes) {
                uiTypeBox.removeByItemId(uiType1);
            }
            uiTypeValue = null;
            switch (dataTypeBox.getSelectedItem().getName()) {
                case DATA_TYPE_TEXT:
                    predValuesBox.setText("");
                    uiTypeBox.setItems(uiTypes);

                    if (!isItemTableField) {
                        uiTypeBox.removeListItem(uiTypes[4]);
                        uiTypeBox.removeListItem(uiTypes[6]);
                        uiTypeBox.removeListItem(uiTypes[7]);
                        uiTypeBox.removeListItem(uiTypes[8]);
                        uiTypeBox.removeListItem(uiTypes[11]);
                        showInListing.setEnabled(true);
                    } else {
                        uiTypeBox.removeListItem(uiTypes[2]);
                    }
                    predValuelRemove();
                    uiTypeBox.setSelectedNullLabel();
                    break;
                case DATA_TYPE_NUMBER:
                    predValuesBox.setText("");
                    setKeyPressHandler(predValuesBox);
                    uiTypeBox.setItems(uiTypes);
                    uiTypeBox.removeListItem(uiTypes[2]);
                    uiTypeBox.removeListItem(uiTypes[10]);

                    if (!isItemTableField) {
                        uiTypeBox.removeListItem(uiTypes[4]);
                        uiTypeBox.removeListItem(uiTypes[5]);
                        uiTypeBox.removeListItem(uiTypes[6]);
                        uiTypeBox.removeListItem(uiTypes[7]);
                        uiTypeBox.removeListItem(uiTypes[8]);
                        uiTypeBox.removeListItem(uiTypes[9]);
                        uiTypeBox.removeListItem(uiTypes[12]);
                        uiTypeBox.removeListItem(uiTypes[13]);
                        uiTypeBox.removeListItem(uiTypes[14]);
                        uiTypeBox.removeListItem(uiTypes[15]);
                        if (Utils.isSuperUser()) {
                            uiTypeBox.removeListItem(uiTypes[16]);
                            uiTypeBox.removeListItem(uiTypes[17]);
                            uiTypeBox.removeListItem(uiTypes[18]);
                        }
                        showInListing.setEnabled(true);
                    }
                    predValuelRemove();
                    uiTypeBox.setSelectedNullLabel();
                    break;
                case DATA_TYPE_DATE:
                    if (!isItemTableField) {
                        uiTypeBox.addListItem(uiTypes[4]);
                        uiTypeBox.addListItem(uiTypes[8]);
                        //uiTypeBox.setSelected(uiTypes[4]);
                        uiTypeBox.removeListItem(uiTypes[5]);
                        uiTypeBox.removeListItem(uiTypes[6]);
                        uiTypeBox.removeListItem(uiTypes[7]);
                        uiTypeBox.removeListItem(uiTypes[9]);
                        uiTypeBox.removeListItem(uiTypes[11]);
                        uiTypeBox.removeListItem(uiTypes[12]);
                        uiTypeBox.removeListItem(uiTypes[13]);
                        uiTypeBox.removeListItem(uiTypes[14]);
                        uiTypeBox.removeListItem(uiTypes[15]);
                        if (Utils.isSuperUser()) {
                            uiTypeBox.removeListItem(uiTypes[16]);
                            uiTypeBox.removeListItem(uiTypes[17]);
                            uiTypeBox.removeListItem(uiTypes[18]);
                        }
                        uiTypeBox.removeListItem(uiTypes[10]);

                        showInListing.setEnabled(true);
                        isFacetable.setEnabled(true);
                    } else {
                        uiTypeBox.addListItem(uiTypes[2]);
                        //uiTypeBox.setSelected(uiTypes[2]);
                        uiTypeBox.removeListItem(uiTypes[0]);
                        uiTypeBox.removeListItem(uiTypes[1]);
                        uiTypeBox.removeListItem(uiTypes[10]);
                        uiTypeBox.removeListItem(uiTypes[11]);
                        uiTypeBox.removeListItem(uiTypes[12]);
                        uiTypeBox.removeListItem(uiTypes[13]);
                        uiTypeBox.removeListItem(uiTypes[14]);
                        uiTypeBox.removeListItem(uiTypes[15]);
                    }
                    predValuelRemove();
                    break;
                case DATA_TYPE_FILE_UPLOAD:
                    //If form type relates to accounting,FileUploadWidget should not be used. Use FileUploadItem widget instead. Because forms that related to accounting supports FileUploadItem widget
                    if (entityNameBox.getSelectedItem() != null &&
                            (CustomFieldSection.ProductServiceView.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.SaleInvoice.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.PurchaseInvoice.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.SaleQuote.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.PurchaseOrder.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.ExpenseReportView.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.BatchInvoicePaymentView.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.BatchPayBillView.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.BankTransferList.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.RequestForQuote.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                                    || CustomFieldSection.RequestForPurchase.getName().toString().equals(entityNameBox.getSelectedItem().getDescription())
                            )
                    ) {
                        uiTypeBox.addListItem(uiTypes[7]);
                        uiTypeBox.setSelected(uiTypes[7]);
                    } else {
                        uiTypeBox.addListItem(uiTypes[6]);
                        uiTypeBox.addListItem(uiTypes[7]);
                        uiTypeBox.setSelected(uiTypes[6]);
                        uiTypeBox.setSelected(uiTypes[7]);
                    }
                    uiTypeBox.removeListItem(uiTypes[4]);
                    uiTypeBox.removeListItem(uiTypes[5]);
                    uiTypeBox.removeListItem(uiTypes[8]);
                    uiTypeBox.removeListItem(uiTypes[9]);
                    uiTypeBox.removeListItem(uiTypes[11]);
                    uiTypeBox.removeListItem(uiTypes[13]);
                    if (Utils.isSuperUser()) {
                        uiTypeBox.removeListItem(uiTypes[15]);
                        uiTypeBox.removeListItem(uiTypes[16]);
                        uiTypeBox.removeListItem(uiTypes[17]);
                    }
                    uiTypeBox.removeListItem(uiTypes[10]);

                    showInListing.setEnabled(false);
                    predValuelRemove();
                    uiTypeBox.setSelectedNullLabel();
                    break;
                case DATA_TYPE_PROFILE_IMAGE:

                    uiTypeBox.addListItem(uiTypes[14]);
                    uiTypeBox.setSelected(uiTypes[14]);

                    uiTypeBox.removeListItem(uiTypes[4]);
                    uiTypeBox.removeListItem(uiTypes[5]);
                    uiTypeBox.removeListItem(uiTypes[8]);
                    uiTypeBox.removeListItem(uiTypes[9]);
                    uiTypeBox.removeListItem(uiTypes[11]);
                    uiTypeBox.removeListItem(uiTypes[13]);
                    if (Utils.isSuperUser()) {
                        uiTypeBox.removeListItem(uiTypes[15]);
                        uiTypeBox.removeListItem(uiTypes[16]);
                        uiTypeBox.removeListItem(uiTypes[17]);
                    }
                    uiTypeBox.removeListItem(uiTypes[10]);

                    showInListing.setEnabled(false);
                    predValuelRemove();
                    uiTypeBox.setSelectedNullLabel();
                    break;
            }

            if (objectID != null) {
                uiTypeBox.setSelectedByValue(companyCustomField.getUiType());
                uiTypeBox.fireEvent(new OurChangeEvent());
                valueTable.supplyProvider(values);
            } else {
                values.clear();
                valueTable.supplyProvider(values);
                valueTable.refresh();
            }
        } else {
            predValuelRemove();
            values.clear();
            valueTable.supplyProvider(values);
            valueTable.refresh();
            uiTypeValue = null;
            uiTypeBox.setEnabled(false);
            uiTypeBox.setSelectedNullLabel();
            isFacetable.setEnabled(false);
            isFacetable.setValue(false);
        }
        onUiTypeBoxChange();
        cfPreviewTab.setUiType(uiTypeValue);
        cfPreviewTab.initData();
    }

    private void registrationsLocaleBox() {
        localeBox.addValueChangeHandler(event -> addLocaleValueToTable(getValueList()));
    }

    private void registrationsLookUpBoxEvent() {
        lookUpTypeBox.addValueChangeHandler(event -> {
            cfPreviewTab.setTitle(fieldNameBox.getText());
            cfPreviewTab.setLookUpType(lookUpTypeBox.getSelectedItem() != null ? lookUpTypeBox.getSelectedItem().getDescription() : null);
            cfPreviewTab.initData();
            referenceGroup.setVisible(CustomFieldLookUpTypeEnum.REFERENCE.name().equals(event.getValue().getDescription()));
        });
    }

    private void registrationsReferenceLookUpEvent() {
        referenceLookUp.getSuggestBox().addSelectionHandler(event -> {
            cfPreviewTab.setTitle(fieldNameBox.getText());
            cfPreviewTab.setLookUpType(lookUpTypeBox.getSelectedItem() != null ? lookUpTypeBox.getSelectedItem().getDescription() : null);
            cfPreviewTab.setReferenceId(referenceLookUp.getSelectedItemID());
            cfPreviewTab.initData();
        });
    }

    /**
     * Get In Server Existing Custom Fiels
     */
    private void getExistingCustomFieds(String entityName, String category) {
        getExistingCustomFieds(entityName, null, category);
    }

    private void getExistingCustomFieds(String entityName, final String dataType, String categoryName) {
        LoadingPanel.loading(true);
        profileService.getExistingCustomFields(companyID, entityName, categoryName, relationship, objectID, new AbstractAsyncCallback<HashMap<Integer, String[]>>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void success(HashMap<Integer, String[]> integerMap) {
                LoadingPanel.loading(false);

                fieldNameBox.setEnabled(true);
                aliasNameBox.setEnabled(true);

                if (dataType == null) {
                    uiTypeValue = null;
                    dataTypeBox.setEnabled(true);
                }
                showInListing.setEnabled(true);
                showInFilterGrouping.setEnabled(true);

                if (dataTypeBox.getItems() != null && dataTypeBox.getItems().length != 0) {
                    if (dataTypeBox.getItems() != null && dataTypeBox.getItems().length > 0) {
                        SelectItem textItem = dataTypeBox.getItems()[0];
                        dataTypeBox.removeListItem(textItem);
                    }
                    if (dataTypeBox.getItems() != null && dataTypeBox.getItems().length > 0) {
                        SelectItem numItem = dataTypeBox.getItems()[0];
                        dataTypeBox.removeListItem(numItem);
                    }
                    if (dataTypeBox.getItems() != null && dataTypeBox.getItems().length > 0) {
                        SelectItem dateItem = dataTypeBox.getItems()[0];
                        dataTypeBox.removeListItem(dateItem);
                    }
                    if (dataTypeBox.getItems() != null && dataTypeBox.getItems().length > 0) {
                        SelectItem uploadItem = dataTypeBox.getItems()[0];
                        dataTypeBox.removeListItem(uploadItem);
                    }

                }

                if (integerMap.get(0).length < STRING_FIELD_LIMIT) {
                    stringItems = integerMap.get(0);
                    dataTypeBox.addListItem(new SelectItem(0, DATA_TYPE_TEXT));
                } else {
                    stringItems = null;
                }

                if (integerMap.get(1).length < DOULE_FIELD_LIMIT) {
                    numberItems = integerMap.get(1);
                    dataTypeBox.addListItem(new SelectItem(1, DATA_TYPE_NUMBER));
                } else {
                    numberItems = null;
                }

                if (integerMap.get(2).length < FIELD_LIMIT) {
                    dateItems = integerMap.get(2);
                    dataTypeBox.addListItem(new SelectItem(2, DATA_TYPE_DATE));
                } else {
                    dateItems = null;
                }

                if (!isItemTableField) {
                    if (integerMap.get(1).length < FIELD_LIMIT) {
                        dataTypeBox.addListItem(new SelectItem(3, DATA_TYPE_FILE_UPLOAD));
                    } else {
                        dateItems = null;
                    }
                }
                if (integerMap.get(1).length < FIELD_LIMIT) {
                    dataTypeBox.addListItem(new SelectItem(4, DATA_TYPE_PROFILE_IMAGE));
                } else {
                    dateItems = null;
                }

                if (dataType != null) {
                    dataTypeBox.setSelectedByValue(dataType);
                    //dataTypeBox.fireEvent(new OurChangeEvent());
                    onDataTypeBoxChanged();
                }

                if ("OnboardingStep".equals(entityNameBox.getSelectedItem().getDescription())) {
                    ListingFilterParameter fp = new ListingFilterParameter();
                    fp.setShowInListing(true);
                    LoadingPanel.loading(true);
                    commonService.getOnboardingStepdList(fp, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(ArrayList<SelectItem> result) {
                            if (result != null && result.size() > 0) {
                                ArrayList<SelectItem> categoryItems = new ArrayList<>();
                                int[] index = {0};
                                for (SelectItem item : result) {
                                    categoryItems.add(new SelectItem(index[0]++, item.getName(), item.getDescription()));
                                }
                                categoryNameBox.setItems(categoryItems.toArray(new SelectItem[]{}));
                                entityCategoryName.setVisible(true);
                                categoryNameBox.setVisible(true);
                                if ((companyCustomField != null && companyCustomField.getObjectId() != null) && companyCustomField.getEntityCategoryName() != null) {
                                    setSelectedetCategory();
                                }
                            }
                            LoadingPanel.loading(false);
                        }
                    });
                } else {
                    entityCategoryName.setVisible(false);
                    categoryNameBox.setVisible(false);
                }
            }
        });
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> null;

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

    private SelectItem[] getPredefinedValuesWithSorting() {
        if (values != null) {
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
        } else {
            return null;
        }
    }

    private void saveCustomFields() {
        enableButtons(false);
        if (companyCustomField == null) {
            companyCustomField = new CompanyCustomFieldItem();
        }

        companyCustomField.setEntityName(entityNameBox.getSelectedItem().getDescription());
        if (categoryNameBox.getSelectedItem() != null) {
            companyCustomField.setEntityCategoryName(categoryNameBox.getSelectedItem().getDescription());
            companyCustomField.setEntityCategoryAlias(categoryNameBox.getSelectedItem().getName());
        }
        companyCustomField.setFieldName(fieldNameBox.getText());
        companyCustomField.setAliasName(aliasNameBox.getText());
        companyCustomField.setDataType(dataTypeBox.getSelectedItem().getName());
        companyCustomField.setUiType(uiTypeBox.getSelectedItem() != null ? uiTypeBox.getSelectedItem().getName() : "");

        companyCustomField.setRelationship(relationship);

        if (!(uiTypeBox.getSelectedItem() != null && (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX_EMAIL)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER_TIME)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTAREA)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_HTML_TEXTAREA)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_WIDGET)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PROFILE_IMAGE_WIDGET)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_ITEM)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PERCENTAGE)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_URL)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_RADIOBUTTON)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_MULTI_LOOKUP)
                || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_LOOKUP)))) {
            companyCustomField.setPredefinedValues(getPredefinedValues());
            companyCustomField.setPredefinedValuesWithSorting(getPredefinedValuesWithSorting());
        }
        companyCustomField.setShowInListing(showInListing.getValue());
        companyCustomField.setClickable(withClickable.getValue());
        companyCustomField.setShowInFilterGrouping(showInFilterGrouping.getValue());
        companyCustomField.setFacetable(isFacetable.getValue());
        companyCustomField.setRequired(isRequired.getValue());
        companyCustomField.setQuery(customQueryBox.getText());
        if (uiTypeBox.getSelectedItem() != null) {
            if (TYPE_ENTITY_LOOKUP.equals(uiTypeBox.getSelectedItem().getName()) || TYPE_ENTITY_MULTI_LOOKUP.equals(uiTypeBox.getSelectedItem().getName())) {
                companyCustomField.setEntityType(entityType.getSelectedItem());
            } else {
                companyCustomField.setEntityType(null);
            }
            if (UI_TYPE_LOOKUP.equals(uiTypeBox.getSelectedItem().getName()) || UI_TYPE_MULTI_LOOKUP.equals(uiTypeBox.getSelectedItem().getName())) {
                companyCustomField.setLookUpTypeEnum(lookUpTypeBox.getSelectedItem() != null
                        ? CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription())
                        : null);
                if (CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomField.getLookUpTypeEnum())) {
                    companyCustomField.setReferenceItem(referenceLookUp.getSelectedItem());
                }
            }
        } else {
            companyCustomField.setEntityType(null);
        }
        ArrayList<Integer> roles = null;
        if (!showTo.getWidgetsMaps().isEmpty()) {
            roles = new ArrayList<>();
            for (WidgetsMap widgetsMap : showTo.getWidgetsMaps()) {
                if (widgetsMap.getWidgets() != null) {
                    widgetsMap.getWidgets();
                    for (Widget widget : widgetsMap.getWidgets()) {
                        if (((DataListBox) widget).isSomethingSelected()) {
                            roles.add(((DataListBox) widget).getSelectedId());
                        }
                    }
                }
            }
        }
        roles = roles.isEmpty() ? null : roles;
        companyCustomField.setAllowedRoles(roles);
        companyCustomField.setCustomLogicField(customLogicCFModal != null ? customLogicCFModal.getSelectedField() : null);
        companyCustomField.setCustomLogicValue(customLogicCFModal != null && customLogicCFModal.getSelectedValue() != null ? customLogicCFModal.getSelectedValue().getName() : null);

        if (objectID == null) {
            initCustomFieldColumnCode();
            companyCustomField.setActive(true);
        }


        LoadingPanel.loading(true);
        profileService.saveCustomFields(companyID, companyCustomField, isItemTableField, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                enableButtons(true);
                categoryNameBox.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customField()), Info.Type.INFO);
                enableButtons(true);
                categoryNameBox.setEnabled(true);
                onShellOk();

                if (commandProvider != null) {
                    commandProvider.execute();
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FIELD_ADD, null, null);
                enableButtons(true);
            }
        });
    }

    private void initCustomFieldColumnCode() {
        int k = 0;
        int LIMIT_COUNT = FIELD_LIMIT;
        String value = "";

        if (dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_TEXT)) {
            value = "string_value";
            allItems = stringItems;
            LIMIT_COUNT = STRING_FIELD_LIMIT;
        }
        if (dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_NUMBER) || dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_FILE_UPLOAD) || dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_PROFILE_IMAGE)) {
            value = "double_value";
            allItems = numberItems;
            LIMIT_COUNT = DOULE_FIELD_LIMIT;
        }
        if (dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_DATE)) {
            value = "date_value";
            allItems = dateItems;
        }
        if (allItems != null) {
            for (int i = 1; i <= LIMIT_COUNT; i++) {
                String fieldname = value + i;
                k = 0;
                for (String allItem : allItems) {
                    if (fieldname.equals(allItem)) {
                        k++;
                        break;
                    }
                }
                if (k == 0) {
                    companyCustomField.setColumnCode(fieldname);
                    break;
                }
            }
        } else {
            companyCustomField.setColumnCode(value + 1);
        }
    }

    /**
     * Custom Query executer for dropdown
     */
    private void executeQuery() {
        final String quary = customQueryBox.getText();
        if (quary != null && !"".equals(quary)) {
            profileService.getCustomFieldDataByQuery(companyID, quary, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    Window.alert(throwable.getMessage());
                    super.failure(throwable);
                }

                @Override
                public void success(SelectItem[] result) {
                    values.clear();
                    addValueToTable(result);
                    cfPreviewTab.setUiType(uiTypeValue);
                    cfPreviewTab.setQuery(quary);
                    if (TYPE_ENTITY_LOOKUP.equals(uiTypeValue) || TYPE_ENTITY_MULTI_LOOKUP.equals(uiTypeValue)) {
                        cfPreviewTab.initData(quary);
                    } else {
                        cfPreviewTab.setPredefinedEntityValues(result);
                        cfPreviewTab.initData();
                    }
                }
            });
        }
    }

    private void addButtonClick() {
        if (predValuesBox.getText() != null && !"".equals(predValuesBox.getText())) {
            valueTable.removeStyleName(ERROR_FORM_STYLE);
            addValueToTable(predValuesBox.getText());
            predValuesBox.setText("");
            cfPreviewTab.setUiType(uiTypeValue);
            cfPreviewTab.setPredefinedValues(getPredefinedValues());
            cfPreviewTab.initData();
        }
    }

    private void addValueToTable(String value) {
        SelectItem selectItem;
        String[] val = value.split("=");
        Integer valSize = values.size();
        if (val.length > 1) {
            Integer sortVal = null;
            try {
                sortVal = val[1] != null && !"".equals(val[1]) ? Integer.parseInt(val[1]) : valSize;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            selectItem = new SelectItem(sortVal != null ? sortVal : valSize, val[0], valSize.toString());
        } else {
            Integer sumValSize = values.isEmpty() ? 1 : values.size() + 1;
            selectItem = new SelectItem(sumValSize, val[0], valSize.toString());
        }
        addValueToTable(selectItem);
    }

    private void addValueToTable(SelectItem value) {
        values.add(value);
        valueTable.supplyProvider(values);
        valueTable.refresh();
    }

    private void addValueToTable(SelectItem[] selectItems) {
        values.addAll(Arrays.asList(selectItems));
        valueTable.supplyProvider(values);
        valueTable.refresh();
    }

    private void addLocaleValueToTable(ArrayList<SelectItem> valueList) {
        valueTable.supplyProvider(valueList);
        valueTable.refresh();
        cfPreviewTab.setUiType(uiTypeValue);
        cfPreviewTab.setPredefinedValues(getPredefinedValues());
        cfPreviewTab.initData();
    }

    private void enableButtons(boolean enable) {
        if (saveAnother != null) {
            saveAnother.setEnabled(enable);
        }
        if (saveClose != null) {
            saveClose.setEnabled(enable);
        }
    }

    private void onShellOk() {
        if (saveAndClose) {
            if (pnlDialogContainer != null) {
                closeDialog();
            } else {
                closeTab();
            }
        } else {
            reinit();
        }
    }

    public void reinit() {
        if (pnlDialogContainer != null) {
            pnlDialogContainer.clear();
        } else {
            clear();
        }
        initInternal();
    }

    protected void closeDialog() {

    }

    private boolean validate() {
        int errors = 0;
        table.cleanupErrors();
        valueTable.removeStyleName(ERROR_FORM_STYLE);

        if (!Validation.validateListBoxRequired(entityNameBox/*, entityName, settingsStrings.enterEntityName()*/)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(fieldNameBox, fieldName)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(aliasNameBox, aliasName) && aliasName.getRequired()) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(dataTypeBox)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(uiTypeBox)) {
            errors++;
        }
        if (uiTypeBox.getSelectedItem() != null) {
            if (!(uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTBOX_EMAIL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_DATEPICKER_TIME)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PERCENTAGE)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_URL)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_RADIOBUTTON)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_TEXTAREA)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_HTML_TEXTAREA)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_WIDGET)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_PROFILE_IMAGE_WIDGET)
                    || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_FILE_UPLOAD_ITEM))) {

                if (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_ENTITY_DROPDOWN)
                        || uiTypeBox.getSelectedItem().getName().equals(TYPE_ENTITY_LOOKUP)
                        || uiTypeBox.getSelectedItem().getName().equals(TYPE_ENTITY_MULTI_LOOKUP)) {
                    if (!Validation.validateTextAreaRequired(customQueryBox)) {
                        errors++;
                    }
                } else if (uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_LOOKUP) || uiTypeBox.getSelectedItem().getName().equals(UI_TYPE_MULTI_LOOKUP)) {
                    if (!Validation.validateDataListBoxRequired(lookUpTypeBox)) {
                        errors++;
                    } else if (CustomFieldLookUpTypeEnum.REFERENCE.equals(CustomFieldLookUpTypeEnum.get(lookUpTypeBox.getSelectedItem().getDescription())) && !Validation.validateLookUpRequired(referenceLookUp)) {
                        errors++;
                    }
                } else if (valueTable.getList().size() < 1) {
                    Validation.validateTextBoxRequired(predValuesBox);
                    predValuesBox.addStyleName(ERROR_FORM_STYLE);
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

    KeyboardListenerAdapter enterAdapter = new KeyboardListenerAdapter() {
        public void onKeyPress(Widget sender, char key, int modifiers) {
            if (key == (char) KEY_ENTER) {
                addButtonClick();
            }
        }
    };

    public String getIconStyle() {
        return "icon-settings-user-credentials";
    }

    private HandlerRegistration setKeyPressHandler(TextBox textBox) {
        return textBox.addKeyPressHandler(event -> {
            if (dataTypeBox.getSelectedItem().getName().equals(DATA_TYPE_NUMBER)) {
                char key = event.getCharCode();
                if (key == (char) 0) {
                    return;
                }
                if ((!Character.isDigit(key)) && key != '-' && (key != (char) KeyCodes.KEY_TAB)
                        && (key != (char) KeyCodes.KEY_BACKSPACE)
                        && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                        && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                        && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                        && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }
        });
    }

    protected SelectItem[] getUiTypes() {

        SelectItem[] uiTypes = new SelectItem[Utils.isSuperUser() ? 19 : 16];
        uiTypes[0] = new SelectItem(0, UI_TYPE_TEXTBOX);
        uiTypes[1] = new SelectItem(1, UI_TYPE_DROPDOWN);
        uiTypes[2] = new SelectItem(2, UI_TYPE_CHECKBOX);
        uiTypes[3] = new SelectItem(3, UI_TYPE_RADIOBUTTON);
        uiTypes[4] = new SelectItem(4, UI_TYPE_DATEPICKER);
        uiTypes[5] = new SelectItem(5, UI_TYPE_TEXTAREA);
        uiTypes[6] = new SelectItem(6, UI_TYPE_FILE_UPLOAD_WIDGET);
        uiTypes[7] = new SelectItem(7, UI_TYPE_FILE_UPLOAD_ITEM);
        uiTypes[8] = new SelectItem(8, UI_TYPE_DATEPICKER_TIME);
        uiTypes[9] = new SelectItem(9, UI_TYPE_TEXTBOX_EMAIL);
        uiTypes[10] = new SelectItem(10, UI_TYPE_LOOKUP);
        uiTypes[11] = new SelectItem(11, UI_TYPE_PERCENTAGE);
        uiTypes[12] = new SelectItem(12, UI_TYPE_URL);
        uiTypes[13] = new SelectItem(13, UI_TYPE_MULTI_LOOKUP);
        uiTypes[14] = new SelectItem(14, UI_TYPE_HTML_TEXTAREA);
        uiTypes[15] = new SelectItem(15, UI_TYPE_PROFILE_IMAGE_WIDGET);

        if (Utils.isSuperUser()) {
            uiTypes[16] = new SelectItem(16, UI_TYPE_ENTITY_DROPDOWN);
            uiTypes[17] = new SelectItem(17, TYPE_ENTITY_LOOKUP);
            uiTypes[18] = new SelectItem(18, TYPE_ENTITY_MULTI_LOOKUP);
        }

        return uiTypes;
    }

    class OurChangeEvent extends ChangeEvent {
    }

    private ArrayList<SelectItem> getValueList() {
        valuesMap.computeIfAbsent(localeBox.getSelectedItem().getDescription(), k -> new ArrayList<>());
        return valuesMap.get(localeBox.getSelectedItem().getDescription());
    }

    private ArrayList<SelectItem> getValueListByLocale(String locale) {
        valuesMap.computeIfAbsent(locale, k -> new ArrayList<>());
        return valuesMap.get(locale);
    }

}
