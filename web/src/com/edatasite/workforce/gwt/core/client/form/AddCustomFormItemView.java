package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCFAndFormItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormItemRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.Timer.CountDownTimer;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldEntityLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AddCustomFormItemView extends CustomFormDynamic2 implements FormHasCustomFieldInterface, Constants, Colapse, HasLinksInterface {

    private static final CommonServiceAsync commonService = CommonService.App.get();
    protected Integer objectID;
    protected Integer fID;
    protected boolean isCopy;
    protected String formID;
    protected String name;
    protected String lookUpType;
    protected Integer lookUpTypeId;
    protected String formType;
    protected Integer convertFormId;
    protected FormItems item;
    private FormHasCustomField customFieldUtil;
    private final Map<String, List<CompanyCustomFieldItem>> itemCFs = new LinkedHashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private NoteHistoryWidget noteHistoryWidget;
    protected ChosenApproversWidget approver;
    private SplitButton actions;
    private WfmButton2 saveButton;
    protected boolean isSummary = false;
    protected WfmButton2 editButton;
    protected WfmButton2 submitButton;
    protected WfmButton2 rejectButton;
    protected WfmButton2 approveButton;
    private WfmButton2 draftButton;
    protected boolean hasApproval;
    protected FooterInformer link;
    protected SplitButton printPdfSplitButton;
    protected boolean isPage = false;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private CountDownTimer timer;
    private Integer attempt;
    private boolean firstInit = true;
    private boolean withTimer;
    private long startedAt;
    private long finishedAt;
    private long estimatedTimeInMilliSec;
    private Date timerStartedAt;
    private List<CustomFormAttributeItem> attributeItems;
    private HashMap<String, SelectItem> cfItemSettingsMap;
    private ArrayList<SelectItem> triggersLookUpTypeName;


    public AddCustomFormItemView(final String name, final String description) {
        super(name, description);
    }

    public AddCustomFormItemView(Integer objectID, Integer fID, String formID, String name, boolean isPage) {
        this(Constants.ITEM_LIST + formID, name);
        this.objectID = objectID;
        this.fID = fID;
        this.formID = formID;
        this.isPage = isPage;
    }

    public AddCustomFormItemView(final Integer fID, final String formID, final String formType, final Integer convertFormId) {
        this(Constants.ITEM_LIST + formID, View.wfmStrings.customForms());
        this.fID = fID;
        this.formID = formID;
        this.formType = formType;
        this.convertFormId = convertFormId;
    }

    public AddCustomFormItemView(final Integer objectID, final Integer fID, final String formID, final String name, final boolean isCopy, final String lookUpType, final Integer lookUpTypeId, boolean isPage) {
        this(Constants.ITEM_LIST + formID, wfmStrings.customForms());
        this.objectID = objectID;
        this.fID = fID;
        this.formID = formID;
        this.name = name;
        this.isCopy = isCopy;
        this.lookUpType = lookUpType;
        this.lookUpTypeId = lookUpTypeId;
        this.isPage = isPage;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        commonService.getCustomFormCfAndItem(ViewName.CustomFormItemTable, objectID, fID, formID, isCopy, lookUpType, lookUpTypeId, formType, convertFormId, new AsyncCallback<CompanyCFAndFormItems>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CompanyCFAndFormItems companyCFAndFormItems) {
                LoadingPanel.loading(false);
                item = companyCFAndFormItems.getFormItems();
                configMap = companyCFAndFormItems.getColumnConfigs();
                attributeItems = companyCFAndFormItems.getAttributeItems();
                cfItemSettingsMap = companyCFAndFormItems.getCfItemTableSettings();

                if (companyCFAndFormItems.getTableCustomFieldItem() != null) {
                    companyCFAndFormItems.getTableCustomFieldItem().forEach(item -> itemCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }

                setCfItemSettingsMapItems();

                ArrayList<CompanyCustomFieldItem> customFieldItems = companyCFAndFormItems.getCompanyCustomFieldItems();
                FormItems formTimerItems = companyCFAndFormItems.getFormTimerItems();


                if (formTimerItems != null && formTimerItems.getTimer() != null && objectID == null) {
                    String[] estimatedTime = formTimerItems.getTimer().split(",");
                    estimatedTimeInMilliSec = (Long.parseLong(estimatedTime[0]) * 60 * 60 * 1000) + (Long.parseLong(estimatedTime[1]) * 60 * 1000);
                    withTimer = true;
                    onWindowOpen(formTimerItems, customFieldItems);
                } else {
                    drawPanel(customFieldItems);
                }
            }
        });
        return null;
    }

    private void setCfItemSettingsMapItems() {
        triggersLookUpTypeName = new ArrayList<>();
        cfItemSettingsMap.values().forEach(item -> {
            if (item.getItemTableEntity() != null) {
                triggersLookUpTypeName.add(item);
            }
        });
        getCustomFieldUtil().setTriggersLookUpTypeName(triggersLookUpTypeName);
    }

    public void drawPanel(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        initButtonAndNotes();

        if (customFieldItems != null) {
            getCustomFieldUtil().setCompanyCustomFieldItems(customFieldItems);
        }
        super.onInitialize();
    }

    private void onWindowOpen(FormItems result, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        KpiModal dialogBox = new KpiModal();
        dialogBox.setTitle(wfmStrings.welcomeMessage());
        dialogBox.setWidth(300);
        Label textLabel = new Label();
        textLabel.setText(result.getWelcomeMessage());
        attempt = result.getAttempt() != null ? result.getAttempt() : 0;

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        ok.addClickHandler(clickEvent -> {
            initButtonAndNotes();

            if (customFieldItems != null) {
                getCustomFieldUtil().setCompanyCustomFieldItems(customFieldItems);
                save(null);
            }
            super.onInitialize();
            dialogBox.close();
        });

        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.addClickHandler(x -> {
            dialogBox.close();
            this.closeTab();
        });
        dialogBox.add(textLabel);
        dialogBox.addButton(close);
        dialogBox.addButton(ok);
        dialogBox.open();
    }

    private void initButtonAndNotes() {
        buttonRegister();

        noteHistoryWidget = new NoteHistoryWidget(callback -> commonService.getCustomFormItemHistoryNotes(objectID, formID, callback));
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            commonService.createCustomFormItemNote(this.objectID, historyListItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(final Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(final Integer savedObjectId) {
                    historyListItem.setObjectID(savedObjectId);
                    LoadingPanel.loading(false);
                }
            });
        });
        noteHistoryWidget.setRemoveFromDatabase((historyListItem) -> {
            if (historyListItem != null && historyListItem.getObjectID() != null) {
                LoadingPanel.loading(true);
                commonService.deleteCustomFormItemNote(historyListItem.getObjectID(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(final Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.warn(View.wfmStrings.errorOccuredWhileDeleting());
                    }

                    @Override
                    public void onSuccess(final Boolean integer) {
                        LoadingPanel.loading(false);
                    }
                });
            }
        });
    }

    private void buttonRegister() {
        draftButton = new WfmButton2(View.wfmStrings.draft(), Constants.BTN_DEFAULT);
        submitButton = new WfmButton2(View.wfmStrings.submit(), Constants.BTN_PRIMARY);
        approveButton = new WfmButton2(View.wfmStrings.approve(), Constants.BTN_SUCCESS);
        rejectButton = new WfmButton2(View.wfmStrings.reject(), Constants.BTN_REJECT);
        saveButton = new WfmButton2(View.wfmStrings.save(), Constants.BTN_PRIMARY);
    }

    protected void registerFields() {
        getCustomFieldUtil().drawCustomFields(this, this.objectID);
        drawItemTable();
        isCopy = item.isCopy();
        drawAttributes();
        initHandlers();
        hasApproval = item.getHasApproval();
        showButtons(item);
        pdfTool(item);
        getCustomFieldUtil().setLookUpSelection(() -> {
            setTriggeredLookUpItems();
        });
        if (item != null) {
            if (item.getCustomFieldItems() != null) {
                getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());
            }
            setItemTableValues(item.getTableItems());
        }

        if (objectID == null) {
            setDefaultValues();
        }
        show();
    }

    protected void drawItemTable() {
        if (configMap != null && configMap.size() > 0) {
            for (final Map.Entry<String, ColumnConfigs[]> configMap : configMap.entrySet()) {

                String fieldID = configMap.getKey();
                ColumnConfigs[] configs = configMap.getValue();
                if (configs.length == 0) {
                    continue;
                }

                Map<String, ColumnConfigs> columnsMap = Stream.of(configs)
                        .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

                EditableTable editableTable = new EditableTable(getColumns(columnsMap), true, true);
                editableTableMap.put(fieldID, editableTable);

                editableTable.setLayoutData(fieldID);
                editableTable.setDraggable(true);
                editableTable.setWidth("100%");
                editableTable.setListener(new EditableTableListener() {
                    @Override
                    public void addRow() {
                        editableTable.addRow(getWidgets(new CustomTableRpc(), fieldID));
                    }

                    @Override
                    public void removeRow() {

                    }
                });
                for (int i = 0; i < 3; i++) {
                    editableTable.addRow(getWidgets(new CustomTableRpc(), fieldID));
                }
                addField(fieldID, editableTable, null, true);
            }
        }
    }

    protected void drawAttributes() {
        if (attributeItems != null && !attributeItems.isEmpty()) {
            for (CustomFormAttributeItem item : attributeItems) {
                if (Constants.UI_TYPE_APPROVAL_PROCESS.equals(item.getFieldType()) && approver == null) {
                    approver = new ChosenApproversWidget(formID, isCopy ? null : objectID);
                    selectApproverChange();
                    addField(item.getFieldId(), approver, item.getLabel());
                    for (String cfKey : getCustomFieldUtil().tbValues.keySet()) {
                        Object cfWidget = getCustomFieldUtil().tbValues.get(cfKey);
                        if (cfWidget instanceof CustomFieldLookUp) {
                            if (((CustomFieldLookUp) cfWidget).getFieldItem().getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.USER)) {
                                ((CustomFieldLookUp) cfWidget).getSuggestBox().addSelectionHandler(event -> onEmployeeChange(((CustomFieldLookUp) cfWidget).getFieldItem().getSelectedId()));
                            }
                        }
                    }
                }
            }
        }
    }

    private void onEmployeeChange(Integer employeeId) {
        approver.reloadApproverWidgets(AddCustomFormItemView.this.formID, AddCustomFormItemView.this.objectID, employeeId);
    }

    private ColumnConfig[] getColumns(final Map<String, ColumnConfigs> columnsMap) {
        final ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (final String cc : columnsMap.keySet()) {
            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columns[i++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, View.wfmStrings.item(), 100, columnsMap.get(cc).isRequired());
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columns[i++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, View.wfmStrings.description(), 100, columnsMap.get(cc).isRequired());
                    break;
                default:
                    final ColumnConfig columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        final ColumnConfig columnConfigItem = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigItem.setPixel(false);
                        columnConfigItem.setForceWidthInPercent(true);
                        columns[i++] = columnConfigItem;

                        final ColumnConfig columnConfigDescription = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", View.wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigDescription.setPixel(false);
                        columnConfigDescription.setForceWidthInPercent(true);
                        columns[i++] = columnConfigDescription;
                    } else {
                        columns[i++] = columnConfig;
                    }
                    break;
            }
        }
        return columns;
    }

    private Widget[] getWidgets(final CustomTableRpc item, final String fieldID) {
        int index = 0;

        final Map<String, ColumnConfigs> columnsMap = Stream.of(this.configMap.get(fieldID))
                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

        Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (final String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                SmartProductLookUp product = new SmartProductLookUp(Constants.RECEIVED);
                product.setLayoutData(item.getId());
                product.setValueNotEmptyMeansSelected(true);
                product.setWidth("100%");
                product.addStyleName("lookUp-moveRight");
                product.getSuggestBox().setWidth("100%");
                if (item.getItemID() != null) {
                    if (!(Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_LOOKUP_DESCRIPTION_INCLUDED) || Utils.isNullOrEmpty(item.getItemNumber()))) {
                        product.setSelected(new ProductSelectItem(item.getItemID(), item.getItemNumber() + " -> " + item.getItemName()));
                    } else {
                        product.setSelected(new ProductSelectItem(item.getItemID(), item.getItemName()));
                    }
                } else {
                    if (item.getItemName() != null && !"".equalsIgnoreCase(item.getItemName())) {
                        product.getSuggestBox().setText(item.getItemName());
                        product.getSuggestBox().getElement().setAttribute("style", "color:#536677 !important");
                    }
                }
                product.setTitle(columnCode);
                widgets[index++] = product;
            } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                final TextArea2 txtDescription = new TextArea2(10000);
                txtDescription.setSize("100%", "40px");
                txtDescription.setText(item.getDescription());
                txtDescription.setTitle(columnCode);
                widgets[index++] = txtDescription;
            } else if (this.itemCFs.containsKey(fieldID)) {

                final CompanyCustomFieldItem cfItem = this.getCustomFieldItem(this.itemCFs.get(fieldID), columnCode);

                if (Constants.UI_TYPE_TEXTBOX.equals(cfItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType()) || Constants.UI_TYPE_URL.equals(cfItem.getUiType())) {
                    final CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");
                    if (Constants.DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(t, 5, true);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        t.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    final CustomPercentageField t = new CustomPercentageField(cfItem);
                    t.setWidth("100%");
                    t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        t.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (Constants.UI_TYPE_URL.equals(cfItem.getUiType())) {
                    final CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        t.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (Constants.UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
                    final CustomDropDownField d = new CustomDropDownField(cfItem);
                    d.setWidth("100%");
                    if (cfItem.getPredefinedValues() != null) {
                        final SelectItem[] sItems = new SelectItem[cfItem.getPredefinedValues().length];
                        int x = 0;
                        for (final String s : cfItem.getPredefinedValues()) {
                            sItems[x] = new SelectItem(x, s);
                            x++;
                        }
                        d.setItems(sItems);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        d.setSelectedByValue(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        d.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (Constants.UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
                    final CustomDatePicker d = new CustomDatePicker(cfItem);
                    d.setWidth("100%");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        d.setDate(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfItem.getUiType())) {
                    final CustomDateTime customDateTime = new CustomDateTime(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        customDateTime.setDateTime(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    customDateTime.setTitle(columnCode);
                    widgets[index++] = customDateTime;

                } else if (Constants.UI_TYPE_TEXTAREA.equals(cfItem.getUiType())) {
                    final CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        textAreaField.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        textAreaField.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                    }
                    textAreaField.setTitle(columnCode);
                    widgets[index++] = textAreaField;
                } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(cfItem.getUiType())) {
                    final CustomHTMLTextAreaField htmlTextAreaField = new CustomHTMLTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        htmlTextAreaField.setData(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                        htmlTextAreaField.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());

                    }
                    htmlTextAreaField.setTitle(columnCode);
                    widgets[index++] = htmlTextAreaField;
                } else if (Constants.UI_TYPE_LOOKUP.equals(cfItem.getUiType())) {
                    final CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        final CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                            lookup.setEnabled(!item.getCustomFieldValuesAsMap().get(columnCode).isDisabled());
                        }
                    }
                    lookup.setTitle(columnCode);
                    widgets[index++] = lookup;
                } else if (Constants.TYPE_ENTITY_LOOKUP.equals(cfItem.getUiType())) {
                    final CustomFieldEntityLookUpField lookup = new CustomFieldEntityLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        final CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem != null && customFieldItem.getFieldStringValue() != null) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(customFieldItem.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && customFieldItem.getQueryItems() != null) {
                                for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        lookup.setSelected(new SelectItem(id, selectItem.getName()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    lookup.setTitle(columnCode);
                    widgets[index++] = lookup;
                } else if (Constants.UI_TYPE_CURRENCY.equals(cfItem.getUiType())) {
                    final CustomFieldCurrencyWidget currencyWidget = new CustomFieldCurrencyWidget(cfItem, "CustomForm");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        final CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            currencyWidget.setCurrency(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }

                    currencyWidget.setTitle(columnCode);
                    widgets[index++] = currencyWidget;
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                    final CustomFieldMultiLookUpField multiLookUp = new CustomFieldMultiLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        final CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        final ArrayList<SelectItem> list = new ArrayList<>(customFieldItem.getSelectItems());
                        if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                            multiLookUp.setSelectedItems(list);
                        }
                    }

                    multiLookUp.setTitle(columnCode);
                    widgets[index++] = multiLookUp;
                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cfItem.getUiType())) {

                    final CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    final CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        final CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getItem() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getItem().getId(), customFieldItem.getItem().getName()));
                            textAreaField.setText(customFieldItem.getItem().getDescription());
                        }
                    }
                    lookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                        if (lookup.getSelectedItem() != null && lookup.getSelectedItem().getId() != null) {
                            AllInOneService.App.get().getProductDescription(lookup.getSelectedItem().getId(), new AbstractAsyncCallback<String>() {
                                @Override
                                public void failure(final Throwable throwable) {
                                    super.failure(throwable);
                                }

                                @Override
                                public void success(final String result) {
                                    if (result != null) {
                                        textAreaField.setText(result);
                                        lookup.getSelectedItem().setDescription(result);
                                        final int currentRowId = AddCustomFormItemView.this.editableTableMap.get(fieldID).getGrid().getCurrentRow();
                                        final CustomCell cel = (CustomCell) AddCustomFormItemView.this.editableTableMap.get(fieldID).getColumnCellWidgetById(currentRowId, columnCode + "_DESCRIPTION");
                                        cel.InActive();
                                    }
                                }
                            });
                        }
                    });

                    lookup.setTitle(columnCode);

                    textAreaField.setTitle(View.wfmStrings.description());
                    widgets[index++] = lookup;
                    widgets[index++] = textAreaField;

                }
            }
        }
        return widgets;
    }

    private CompanyCustomFieldItem getCustomFieldItem(final List<CompanyCustomFieldItem> companyCustomFieldItems, final String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
    }

    @Override
    protected void addButtons() {
    }

    public ViewFooter createFooter(final FormItems result) {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddCustomFormItemView.this.getFooterLeftSideWidgets(result);
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddCustomFormItemView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets(final FormItems result) {
        final List<Widget> leftSideWidgets = new ArrayList<>();
        final FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, View.wfmStrings.historyAndNotes(), this.noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");

        if (Utils.hasPermission(this.formID + "_ADD_LINKS_" + Utils.getCompanyID())) {
            this.link = new FooterInformer(SvgEnum.link, View.wfmStrings.links(), null);
            this.link.addClickHandler(event -> {
                if (this.firstClick.get()) {
                    this.getLinkingUtil().getAddLinkSideNavBox();
                    if (this.objectID == null) {
                        final ArrayList<RelationItem> relationItems = new ArrayList<>();
                        this.getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);
                    } else {
                        this.getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(this.item.getRelations(), false);
                    }
                    this.firstClick.set(false);
                } else {
                    this.getLinkingUtil().getAddLinkSideNavBox().show();
                }
            });
            if (this.objectID != null) {
                leftSideWidgets.add(this.link);
                if (this.item.getRelations() != null) {
                    this.link.setBadgeCount(this.item.getRelations().size());
                }
            }
        }

        if (this.objectID != null) {
            leftSideWidgets.add(informer);
        }
        if (result != null && result.getTimer() != null) {
            timer = new CountDownTimer(result.getTimer());
            leftSideWidgets.add(timer);
            startedAt = System.currentTimeMillis();
            timerStartedAt = new Date();
        }
        return leftSideWidgets;
    }

    private String validateTimerDuration(long startedAt, long finishedAt) {
        long totalTime = finishedAt - startedAt;
        int hh, mm, ss;
        if (totalTime > estimatedTimeInMilliSec) {
            hh = (int) ((estimatedTimeInMilliSec / 1000) / 3600);
            mm = (int) (((estimatedTimeInMilliSec / 1000) / 60) % 60);
            ss = (int) ((estimatedTimeInMilliSec / 1000) % 60);
        } else {
            hh = (int) ((totalTime / 1000) / 3600);
            mm = (int) (((totalTime / 1000) / 60) % 60);
            ss = (int) ((totalTime / 1000) % 60);
        }
        return hh + ":" + mm + ":" + ss;
    }

    private List<Widget> getFooterRightSideWidgets() {
        final List<Widget> rightSideWidgets = new ArrayList<>();

        this.draftButton.setVisible(false);
        this.draftButton.addClickHandler(event -> this.save(Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT));
        rightSideWidgets.add(this.draftButton);


        this.submitButton.setVisible(false);
        this.submitButton.addClickHandler(event -> this.approveOrRejectItem(Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
        rightSideWidgets.add(this.submitButton);


        this.approveButton.setVisible(false);
        this.approveButton.addClickHandler(event -> this.approveOrRejectItem(Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED));
        rightSideWidgets.add(this.approveButton);


        this.rejectButton.setVisible(false);
        this.rejectButton.addClickHandler(event -> this.approveOrRejectItem(Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED));
        rightSideWidgets.add(this.rejectButton);

        if (Utils.hasRole(Constants.ADMIN)) {
            final WfmButton2 customize2 = new WfmButton2(CustomFormDynamic2.wfmStrings.customize(), Constants.BTN_DEFAULT_OUTLINE);
            customize2.ensureDebugId("customize");
            customize2.setTooltip(View.wfmStrings.customizeLayout());

            customize2.addClickHandler(event -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                String addPath = "customizeForm2|add/add/" + getFormID() + "/";
                String historyToken = addPath + URL.encodeQueryString(url);

                if (objectID != null) {
                    historyToken += "/" + objectID;
                }

                SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
            });
            rightSideWidgets.add(customize2);
        }

        if (this.objectID != null && Utils.hasPermission(this.formID + "_PDF_" + Utils.getCompanyID())) {
            this.printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            rightSideWidgets.add(this.printPdfSplitButton);
        }

        if (this.objectID != null) {
            this.actions = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);

            final List<SplitButtonItem> actionButtonList = new ArrayList<>();

            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                final String addEventString = Property.get(Constants.EVENT_LIST, View.wfmStrings.addMess(), View.wfmStrings.event());
                final SplitButtonItem addEvent = new SplitButtonItem(addEventString, addEventString, () -> this.addActivity(this.item, Appointment.EVENT));
                addEvent.ensureDebugId("addMess");
                actionButtonList.add(addEvent);
            }

            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                final String callLogString = Property.get(Constants.LOGACALL, View.wfmStrings.logCall());
                final SplitButtonItem callLog = new SplitButtonItem(callLogString, callLogString, () -> this.addActivity(this.item, Appointment.CALL_LOG));
                callLog.ensureDebugId("callALog");
                actionButtonList.add(callLog);
            }
            this.actions.addItemList(actionButtonList);
            rightSideWidgets.add(this.actions);
        }

        if (Utils.hasPermission(this.formID + "_EDIT_" + Utils.getCompanyID())) {
            this.editButton = new WfmButton2(View.wfmStrings.edit(), Constants.BTN_PRIMARY);
            this.editButton.setVisible(false);

            this.editButton.addClickHandler(event -> {
                String historyToken = Constants.ITEM_LIST + "|add/add/" + this.objectID + "/" + this.fID + "/" + this.formID + "/" + this.name;

                if (isPage) {
                    historyToken += "//PAGE";
                }

                SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
                closeTab();
            });
            rightSideWidgets.add(editButton);
        }

        this.saveButton.setVisible(false);
        this.saveButton.addClickHandler(event -> this.save(null));
        rightSideWidgets.add(this.saveButton);
        return rightSideWidgets;
    }

    private void initHandlers() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, this, (sender, args) -> {
            this.selectApproverChange();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMER_ADDED, this, (sender, args) -> {
            finishedAt = System.currentTimeMillis();
            this.onTimerEnd();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REMOVE_TAB, this, ((sender, args) -> {
            if (withTimer) {
                timer.cancel();
            }
        }));

    }

    private void onTimerEnd() {
        commonService.getCustomFormTimerItems(formID, new AbstractAsyncCallback<FormItems>() {
            @Override
            public void onFailure(final Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final FormItems result) {
                modalOpen(result);
            }
        });
    }

    private void modalOpen(FormItems result) {
        KpiModal dialogBox = new KpiModal();
        dialogBox.setTitle(wfmStrings.information());
        dialogBox.setWidth(300);
        Label textLabel = new Label();
        textLabel.setText(result.getEndOfTimeMessage());
        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        ok.addClickHandler(clickEvent -> {
            this.save("TIME_IS_UP");
            dialogBox.close();
        });
        dialogBox.add(textLabel);
        dialogBox.addButton(ok);
        dialogBox.addCloseHandler(handler -> this.save("TIME_IS_UP"));
        dialogBox.open();
    }

    protected void selectApproverChange() {
        if (this.approver != null) {
            if (this.approver.getApproversSize() == 1) {
                this.approveButton.setText(View.wfmStrings.approveAndClose());
            }
            if (this.approver.getFirstApproverLookUp() != null) {
                this.approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    final SelectItem item = this.approver.getFirstApproverLookUp().getSelectedItem();
                    final Integer itemId = item != null ? item.getId() : null;
                    final Integer currentUserId = this.item != null && this.item.getCurrentUserId() != null ? this.item.getCurrentUserId() : Utils.getUserID();
                    if (currentUserId.equals(itemId)) {
                        this.approveButton.setVisible(true);
                        this.submitButton.setVisible(false);
                    } else {
                        this.submitButton.setVisible(true);
                        this.approveButton.setVisible(false);
                    }
                });
                if (this.approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    final SelectItem item = this.approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        this.approveButton.setVisible(true);
                        this.submitButton.setVisible(false);
                    } else {
                        this.approveButton.setVisible(false);
                        this.submitButton.setVisible(true);
                    }
                }
            }
        }
    }

    protected void approveOrRejectItem(final String statusCode) {
        this.save(statusCode);
    }

    private void save(final String statusCode) {
        if (!this.validate(statusCode)) {
            if (!(firstInit && "TIME_IS_UP".equals(statusCode))) {
                Info.show(View.wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }
        }
        item = this.item == null ? new FormItems() : this.item;
        item.setObjectID(objectID);
        item.setFormID(formID);
        item.setTableItems(getObjectData());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        if (hasApproval && approver != null) {
            item.setApprovers(approver.getChosenApprovers());
        }
        item.setStatusCode(statusCode);
        item.setCopy(this.isCopy);
        if (timerStartedAt != null) {
            String format = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").format(timerStartedAt);
            item.setTimerStartedAt(format);
        }
        if (attempt != null) {
            item.setAttempt(this.attempt + 1);
        }
        if (!firstInit) {
            finishedAt = System.currentTimeMillis();
            item.setDurationTime(validateTimerDuration(startedAt, finishedAt));
        }
        LoadingPanel.loading(true);
        commonService.saveCustomFormItem(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(final Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final Integer result) {
                LoadingPanel.loading(false);

                getCustomFieldUtil().setFormItemIdToTheCommitBoxFields(result);
                setObjectId(result);
                if (isPage) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customForms()), Info.Type.INFO);
                }
                if (withTimer) {
                    if (!firstInit) {
                        closeTab();
                    }
                } else {
                    closeTab();
                }
                if (statusCode == null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, result, AddCustomFormItemView.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, result, AddCustomFormItemView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ITEM_APPROVAL, result, AddCustomFormItemView.this);
                }
                if (formType != null && convertFormId != null) {
                    saveConvertedRelations(result);
                }
                firstInit = false;
            }
        });
    }

    private void setObjectId(Integer result) {
        this.objectID = result;
    }

    private HashMap<String, ArrayList<CustomTableRpc>> getObjectData() {
        final HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

        for (final Map.Entry<String, EditableTable> mapTable : this.editableTableMap.entrySet()) {

            final String uuid = mapTable.getKey();

            final List<CompanyCustomFieldItem> itemCustom = this.itemCFs.get(uuid);

            final Map<String, ColumnConfigs> columnsMap = Stream.of(this.configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            final EditableTable productTable = mapTable.getValue();
            final ArrayList<CustomTableRpc> tableItem = new ArrayList<>();

            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                SmartProductLookUp productLookUp = null;
                final CustomTableRpc result = new CustomTableRpc();
                ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();

                for (final String columnCode : columnsMap.keySet()) {
                    if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                        productLookUp = (SmartProductLookUp) productTable.getColumnById(i, ItemTableConstants.PRODUCT);

                        if (productLookUp.getSelectedItemID() != null && productLookUp.getSelectedItem().getName().equals(productLookUp.getSuggestBox().getText())) {
                            result.setItemID(productLookUp.getSelectedItemID());
                            if (productLookUp.getSelectedItem().getName().contains("->")) {
                                result.setItemNumber(productLookUp.getSelectedItem().getName().split("->")[0].trim());
                                result.setItemName(productLookUp.getSelectedItem().getName().split("->")[1]);
                            } else {
                                result.setItemName(productLookUp.getSelectedItem().getName());
                            }
                        } else if (productLookUp.getText() != null) {
                            result.setItemName(productLookUp.getText());
                        }
                    } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                        final TextArea2 description = (TextArea2) productTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                        result.setDescription(description.getText());
                    } else if (this.itemCFs.containsKey(uuid)) {
                        Object customFieldValue = null;
                        Integer customFieldValueId = null;
                        SelectItem itemValue = null;
                        if (Constants.UI_TYPE_TEXTBOX.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType()) || Constants.UI_TYPE_URL.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomTextBoxField t = (CustomTextBoxField) productTable.getColumnById(i, columnCode);
                            if (t.getText() != null && !t.getText().isEmpty()) {
                                customFieldValue = t.getText();
                            }
                        }
                        if (Constants.UI_TYPE_TEXTAREA.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomTextAreaField t = (CustomTextAreaField) productTable.getColumnById(i, columnCode);
                            if (t.getText() != null && !t.getText().isEmpty()) {
                                customFieldValue = t.getText();
                            }
                        }
                        if (Constants.UI_TYPE_PERCENTAGE.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomPercentageField percentageField = (CustomPercentageField) productTable.getColumnById(i, columnCode);
                            if (percentageField != null && !percentageField.getText().isEmpty()) {
                                customFieldValue = percentageField.getText();
                            }

                        } else if (Constants.UI_TYPE_DROPDOWN.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomDropDownField t = (CustomDropDownField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                            }
                        } else if (Constants.UI_TYPE_DATEPICKER.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomDatePicker t = (CustomDatePicker) productTable.getColumnById(i, columnCode);
                            if (t.getDate() != null) {
                                customFieldValue = t.getDate();
                            }
                        } else if (Constants.UI_TYPE_LOOKUP.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                                customFieldValueId = t.getSelectedItem().getId();
                            }
                        } else if (Constants.TYPE_ENTITY_LOOKUP.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomFieldEntityLookUpField t = (CustomFieldEntityLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItemID() != null) {
                                customFieldValue = t.getSelectedItemID().toString();
                            }
                        } else if (Constants.UI_TYPE_CURRENCY.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomFieldCurrencyWidget t = (CustomFieldCurrencyWidget) productTable.getColumnById(i, columnCode);
                            if (t.getCurrencyID() != null) {
                                customFieldValue = t.getCurrencyName();
                                customFieldValueId = t.getCurrencyID();
                            }
                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItems() != null && t.getSelectedItems().size() > 0) {
                                customFieldValue = t.getSelectedItems();
                            }
                        } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(this.getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            final CustomFieldLookUp item = (CustomFieldLookUp) productTable.getColumnById(i, columnCode);
                            final CustomTextAreaField desc = (CustomTextAreaField) productTable.getColumnById(i, columnCode + "_DESCRIPTION");
                            if (item.getSelectedItem() != null) {
                                itemValue = new SelectItem(item.getSelectedItemID(), item.getSelectedItem().getName(), desc.getText());
                            }
                        } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDateTime t = (CustomDateTime) productTable.getColumnById(i, columnCode);
                            if (t.getDateTime() != null) {
                                customFieldValue = t.getDateTime();
                            }
                        }

                        final CompanyCustomFieldItem companyCustomFieldItem = this.getCustomFieldItem(itemCustom, columnCode);
                        final CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
                        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                        resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());

                        if (customFieldValue != null) {
                            if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                                String ss = ((ArrayList<SelectItem>) customFieldValue).stream().map(SelectItem::getName)
                                        .collect(Collectors.joining(" , "));
                                resultItem.setFieldStringValue(ss);
                                resultItem.setSelectItems((ArrayList<SelectItem>) customFieldValue);
                            } else {
                                resultItem.setFieldStringValue((String) customFieldValue);
                            }
                            if (customFieldValueId != null) {
                                resultItem.setSelectedId(customFieldValueId);
                            }
                        }
                        if (itemValue != null) {
                            resultItem.setItem(itemValue);
                        }
                        resultItemList.add(resultItem);
                    }
                }
                result.setUuid(uuid);
                result.setSorder(i + 1);
                result.setItemCustomFields(resultItemList);
                tableItem.add(result);
            }
            map.put(uuid, tableItem);
        }
        return map;
    }

    private boolean areOtherRowsAffected(final EditableTable productTable, final int rowID) {
        boolean result = false;

        final SmartProductLookUp productLookUp = (SmartProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        final TextArea2 descriptionTxtArea = (TextArea2) productTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);

        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= productLookUp != null && (productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null);


        return result;
    }

    private boolean validate(final String statusCode) {
        clearErrorStyle();
        this.getCustomFieldUtil().validationObjects.clear();

        if (Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT.equals(statusCode)) {
            if (this.hasApproval && this.approver != null) {
                return this.approver.isValid();
            }
            return true;
        }

        int errors = customValidate();
        errors += this.getCustomFieldUtil().validateCustomFields();

        if (!this.validateItemTables()) {
            errors++;
        }

        if (this.hasApproval && this.approver != null) {
            if (!this.approver.isValid()) {
                errors++;
            }
        }
        if (errors > 0) {
            showRequiredFieldsAfterValidation();
            return false;
        }
        return true;
    }

    private boolean validateItemTables() {
        final AtomicBoolean errorFound = new AtomicBoolean(false);

        if (this.editableTableMap.size() <= 0) {
            return !errorFound.get();
        }
        this.editableTableMap.forEach((uuid, table) -> {
            int[] inputValue = {0};
            final List<String> requiredFields = new ArrayList<>();
            final List<CompanyCustomFieldItem> itemCustom = this.itemCFs.get(uuid);

            for (final String companyCustomFieldItem : table.getRequiredFields()) {
                if (!companyCustomFieldItem.contains("_DESCRIPTION")) {
                    requiredFields.add(companyCustomFieldItem);
                }
            }

            IntStream.range(0, table.getGrid().getRowCount()).forEach(idx -> {
                int errors;
                table.resetValidation(idx);

                errors = this.validateRequiredItems(uuid, table, itemCustom, idx, requiredFields)[0];
                inputValue[0] += this.validateRequiredItems(uuid, table, itemCustom, idx, requiredFields)[1];
                if (errors == 0) {
                    table.setItemValid(idx, true);
                    table.incValidRow();
                } else if (errors == requiredFields.size()) {
                    table.setItemValid(idx, false);
                } else {
                    this.colorizeErrorField(idx, table, requiredFields, itemCustom);
                    errorFound.set(true);
                }
            });

            if (inputValue[0] == 0 && requiredFields.size() > 0) {
                this.colorizeErrorField(0, table, requiredFields, itemCustom);
                errorFound.set(true);
            }
        });

        return !errorFound.get();
    }

    private int[] validateRequiredItems(final String uuid, final EditableTable table, final List<CompanyCustomFieldItem> itemCustom, final int idx, final List<String> requiredFields) {
        int[] errors = {0};
        int[] inputValueTable = {0};
        requiredFields.forEach(code -> {
            if (ItemTableConstants.PRODUCT.equals(code)) {
                final SmartProductLookUp productLookUp = (SmartProductLookUp) table.getColumnById(idx, ItemTableConstants.PRODUCT);
                if (!Validation.validateLookUpRequired(productLookUp)) {
                    table.setColumnValid(ItemTableConstants.PRODUCT);
                    errors[0]++;
                } else {
                    inputValueTable[0]++;
                }
            } else if (ItemTableConstants.DESCRIPTION.equals(code)) {
                final TextArea2 textArea2 = (TextArea2) table.getColumnById(idx, ItemTableConstants.DESCRIPTION);
                if (!Validation.validateTextAreaRequired(textArea2)) {
                    table.setColumnValid(ItemTableConstants.DESCRIPTION);
                    errors[0]++;
                } else {
                    inputValueTable[0]++;
                }
            } else if (this.itemCFs.containsKey(uuid)) {
                if (Constants.UI_TYPE_TEXTBOX.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomTextBoxField widget = (CustomTextBoxField) table.getColumnById(idx, code);
                    if (!Validation.validateTextBoxRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomTextBoxField widget = (CustomTextBoxField) table.getColumnById(idx, code);
                    if (!Validation.validateEmailRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_URL.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomTextBoxField widget = (CustomTextBoxField) table.getColumnById(idx, code);
                    if (!Validation.validateUrl(widget, null)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_TEXTAREA.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomTextAreaField widget = (CustomTextAreaField) table.getColumnById(idx, code);
                    if (!Validation.validateTextAreaRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomHTMLTextAreaField widget = (CustomHTMLTextAreaField) table.getColumnById(idx, code);
                    if (!Validation.validateHTMLTextAreaRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomPercentageField widget = (CustomPercentageField) table.getColumnById(idx, code);
                    if (!Validation.validateIntegerTextBoxRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_DROPDOWN.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomDropDownField widget = (CustomDropDownField) table.getColumnById(idx, code);
                    if (widget.getSelectedItem() == null) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomDatePicker widget = (CustomDatePicker) table.getColumnById(idx, code);
                    if (!Validation.validateDate(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomDateTime widget = (CustomDateTime) table.getColumnById(idx, code);
                    if (!Validation.validateDateTime(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_LOOKUP.equals(this.getCustomFieldItem(itemCustom, code).getUiType()) || Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomFieldLookUpField widget = (CustomFieldLookUpField) table.getColumnById(idx, code);
                    if (!Validation.validateLookUpRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.TYPE_ENTITY_LOOKUP.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomFieldEntityLookUpField widget = (CustomFieldEntityLookUpField) table.getColumnById(idx, code);
                    if (!Validation.validateLookUpRequired(widget)) {
                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(this.getCustomFieldItem(itemCustom, code).getUiType())) {
                    final CustomFieldMultiLookUpField widget = (CustomFieldMultiLookUpField) table.getColumnById(idx, code);
                    if (widget.getSelectedItems() == null || (widget.getSelectedItems() != null && widget.getSelectedItems().size() == 0)) {
                        widget.addStyleName(Constants.ERROR_FORM_STYLE);
                        Utils.scrollIntoView(widget.getElement());

                        table.setColumnValid(code);
                        errors[0]++;
                    } else {
                        inputValueTable[0]++;
                    }
                }
            }
        });
        final int[] ints = new int[2];
        ints[0] = errors[0];
        ints[1] = inputValueTable[0];
        return ints;
    }

    private void colorizeErrorField(final int rowID, final EditableTable itemsTable, final List<String> requiredCFs, final List<CompanyCustomFieldItem> itemCustom) {
        final SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        final TextArea2 textArea2 = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);

        for (final String fieldItem : requiredCFs) {

            if (ItemTableConstants.PRODUCT.equals(fieldItem) && productLookUp.getSelectedItem() == null) {
                itemsTable.notValid(rowID, ItemTableConstants.PRODUCT);
            }

            if (ItemTableConstants.PRODUCT.equals(fieldItem)) {
                if (!Validation.validateTextAreaRequired(textArea2)) {
                    itemsTable.notValid(rowID, ItemTableConstants.QTY);
                }
            }

            if (Constants.UI_TYPE_TEXTBOX.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateTextBoxRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            }
            if (Constants.UI_TYPE_TEXTAREA.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final TextArea t = (TextArea) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateTextAreaRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateEmailRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_URL.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateUrl(t, null)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateIntegerTextBoxRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_DROPDOWN.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final DataListBox t = (DataListBox) itemsTable.getColumnById(rowID, fieldItem);
                if (t.getSelectedItem() == null) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final DatePicker t = (DatePicker) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateDate(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateDateTime(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.TYPE_ENTITY_LOOKUP.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final CustomFieldEntityLookUpField t = (CustomFieldEntityLookUpField) itemsTable.getColumnById(rowID, fieldItem);
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.notValid(rowID, fieldItem);
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(this.getCustomFieldItem(itemCustom, fieldItem).getUiType())) {
                final CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(rowID, fieldItem);
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    itemsTable.notValid(rowID, fieldItem);
                }
            }
        }
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (this.customFieldUtil == null) {
            this.customFieldUtil = new FormHasCustomField();
        }
        return this.customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
    }

    public void pdfTool(final FormItems result) {
        if (this.printPdfSplitButton == null) {
            return;
        }
        final List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (final SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> this.generatePDF(this.panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", CustomFormDynamic2.wfmStrings.landscape(), () -> this.generatePDF(this.panel, null, true)));
        }
        final Integer finalDefaultTemplateId = defaultTemplateId;

        final SplitButtonItem pdfVersion = new SplitButtonItem(CustomFormConstants.PDF_VERSION, CustomFormDynamic2.wfmStrings.pdfVersion(), () -> this.generatePDF(this.panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        this.printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(final HTMLPanel panel, final Integer templateID, final boolean landscape) {
        final CustomFormItemRequestObject requestObject = new CustomFormItemRequestObject(this.objectID);
        final HashMap<String, String> parameters = requestObject.getRequestParams();
        parameters.put("fid", String.valueOf(this.fID));
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        final String pdfURL = CommandConstants.PDF_URL + "/customFormItemViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void setItemTableValues(final HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
        if (tableItems != null && tableItems.size() > 0) {
            for (final Map.Entry map : tableItems.entrySet()) {
                final String uuid = (String) map.getKey();
                this.editableTableMap.get(uuid).removeAllRows();
                for (final CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                    this.editableTableMap.get(uuid).addRow(this.getWidgets(item, uuid));
                }
            }
        }
    }

    protected void showButtons(final FormItems result) {
        this.addButton(this.createFooter(result));
        if (this.hasApproval) {
            this.saveButton.setVisible(false);

            this.draftButton.setVisible(true);
            if (this.item.getObjectID() != null
                    && this.item.getCurrentApproverId() != this.item.getCurrentUserId()) {
                this.submitButton.setVisible(true);
            }
            if (this.item.getCurrentApproverId() != null
                    && !this.item.getCurrentApproverId().equals(this.item.getCurrentUserId())) {
                this.approveButton.setVisible(false);
            }
        } else {
            this.saveButton.setVisible(true);

            this.draftButton.setVisible(false);
            this.submitButton.setVisible(false);
        }
    }

    @Override
    protected String getFormID() {
        return this.formID;
    }

    @Override
    protected String getFormType() {
        return this.objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(final Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(AddCustomFormItemView.this.onInitialize());
            }
        });
    }

    private ActivityQuickAddForm addActivity(final FormItems result, final int callLog) {
        return new ActivityQuickAddForm(callLog, RelationItem.newEventRelation(result.getFormName(),
                this.objectID, result.getAutoNumber() != null ? result.getAutoNumber() : result.getFormName() + ": " + this.objectID));
    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (this.linkingUtil == null) {
            this.linkingUtil = new HasLinks(this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return AddCustomFormItemView.this.objectID;
                }

                @Override
                public String getRelationType() {
                    return AddCustomFormItemView.this.item.getFormID();
                }

                @Override
                public String getRelationName() {
                    return AddCustomFormItemView.this.item.getAutoNumber() != null ? AddCustomFormItemView.this.item.getAutoNumber()
                            : AddCustomFormItemView.this.item.getFormName() + ": " + AddCustomFormItemView.this.objectID;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return this.linkingUtil;
    }

    private void setTriggeredLookUpItems() {
        if (getCustomFieldUtil().selectedItems != null) {
            LoadingPanel.loading(true);
            commonService.getItemTableValues(formID, getCustomFieldUtil().selectedItems, new AsyncCallback<HashMap<String, ArrayList<CustomTableRpc>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(HashMap<String, ArrayList<CustomTableRpc>> result) {
                    setItemTableValues(result);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void saveConvertedRelations(final Integer _objectId) {
        final ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, this.item.getFormID(), this.item.getAutoNumber() != null ? this.item.getAutoNumber() : this.item.getFormName() + ": " + _objectId, this.convertFormId, this.formType, this.item.getEntityName()));
        AllInOneService.App.get().saveRelations(this.item.getFormID(), _objectId, this.item.getAutoNumber() != null ? this.item.getAutoNumber() : this.item.getFormName() + ": " + _objectId, relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
            @Override
            public void failure(final Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ArrayList<RelationItem> selectItems) {
                LoadingPanel.loading(false);
            }
        });
    }
}
