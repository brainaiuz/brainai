package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.contact.client.ui.AddressWidgetAddView;
import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.contact.client.ui.GeneralEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.*;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.RunWebHookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookListItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.AutoNumberCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ValidationType;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTip;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.ui.*;

import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10/20/11
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CustomForm2 extends CustomForm implements CustomFormConstants, PermissionConstants {


    private final HashMap<String, SelectItem[]> predefinedItems = new HashMap<>();
    private ModelForm modelForm;
    private final LinkedHashMap<String, MaterialCollapsibleItem> sections = new LinkedHashMap<>();
    private final MaterialCollapsible collapsible = new MaterialCollapsible();
    Map<String, String> defaultValueMap = new HashMap<>();
    public SplitButton customizeButton;
    public WfmButton2 updateWebhooksButton;
    protected String relationType;
    protected Integer valueId;
    private HashMap<String, HTML> hiddenTitles = new HashMap<>();


    protected CustomForm2() {
        footer = new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return null;
            }
        });
    }

    protected CustomForm2(String name) {
        super(name);
        footer = new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return null;
            }
        });
    }

    protected CustomForm2(String name, String description) {
        super(name, description);
        footer = new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return null;
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        registerFields();
        if (layoutInterface == null) {
            service.getModelForm(getFormID(), new AbstractAsyncCallback<ModelForm>() {
                @Override
                public void failure(Throwable caught) {
                    CustomForm2.this.closeTab();
                }

                @Override
                public void success(ModelForm result) {
                    modelForm = result;
                    setFormRPCAndInit(result);
                }
            });
        } else {
            setFormRPCAndInit(layoutInterface);
        }
        return this;
    }

    protected abstract void registerFields();

    @Override
    protected void addPanel(LayoutInterface layoutInterface) {
        if (layoutInterface instanceof ModelForm) {
            panel = new HTMLPanel("");
            panel.addStyleName("summary");
            MaterialCollapsible collapsiblePanel = drawCollapsible();
            if (collapsiblePanel != null) {
                panel.add(collapsiblePanel);
            }
            panel.add(footer);
        } else if (layoutInterface instanceof LayoutRPC) {
            super.addPanel(layoutInterface);
        }
    }

    protected void openSection(String sectionName) {
        if (sectionName != null && sections.containsKey(sectionName)) {
            MaterialCollapsibleItem item = sections.get(sectionName);
            if (item != null) {
                item.removeStyleName(CssName.ACTIVE);
                if (item.getHeader() != null) {
                    item.getHeader().removeStyleName(CssName.ACTIVE);
                }
                item.addStyleName(CssName.ACTIVE);

                if (item.getHeader() != null) {
                    item.getHeader().addStyleName(CssName.ACTIVE);
                }
            }
        }
    }

    private MaterialCollapsible drawCollapsible() {

        boolean collapse = modelForm.isCollapse();

        collapsible.setStyleName("collapsible--panels collapsible--arrows-left", true);
        collapsible.setAccordion(false);
        List<String> hasSections = new ArrayList<>();
        if (modelForm.getSectionsRpcMap() == null || modelForm.getColumnMap() == null) {
            return null;
        }

        List<String> expandedSection = new ArrayList<>();
        if (modelForm != null) {
            modelForm.getColumnMap().forEach((section, columns) -> {

                DynamicSectionsRpc rpc = modelForm.getSectionsRpcMap() != null ? modelForm.getSectionsRpcMap().get(section) : null;
                if (rpc != null && rpc.isExpanded()) {
                    expandedSection.add(section);
                }

                MaterialPanel gridRow = new MaterialPanel("grid-row summary-row");

                columns.forEach((columnType, fields) -> {


                    MaterialPanel gridBoxCol = new MaterialPanel(getFormBodyStyle(columns.size()));

                    gridRow.add(gridBoxCol);

                    fields.stream()
                            .sorted(Comparator.comparingInt(CustomizeFormItem::getForder))
                            .filter(CustomizeFormItem::isActive)
                            .forEach(field -> {

                                if (hasSections.contains(field.getSection())) {
                                    createField(field, gridBoxCol);
                                } else {
                                    hasSections.add(field.getSection());

                                    MaterialCollapsibleItem item = new MaterialCollapsibleItem();
                                    item.addStyleName("hideCustomField");
                                    item.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);

                                    MaterialCollapsibleBody itemBody = new MaterialCollapsibleBody();
                                    if (collapse) {
                                        String s = localize(field.getSection());
                                        if (rpc != null && rpc.getLabel() != null && rpc.getLabel().length() > 0) {
                                            s = rpc.getLabel();
                                        }
                                        item.add(new MaterialCollapsibleHeader(new MaterialLink(humanize(s))));
                                    } else {
                                        itemBody.addStyleName("no-border");
                                    }

                                    itemBody.add(gridRow);

                                    createField(field, gridBoxCol);

                                    item.add(itemBody);
                                    collapsible.add(item);
                                    sections.put(field.getSection(), item);
                                }
                            });
                });
            });
        }

        if (expandedSection.isEmpty()) {
            collapsible.open(0);
        } else {
            expandedSection.forEach(this::openSection);
        }

        return collapsible;
    }

    private String humanize(String localizedString) {
        if (localizedString != null && localizedString.contains("_") && !localizedString.contains(" ")) {
            localizedString = localizedString.replace("_", " ");
        }
        return localizedString;
    }

    protected void hideSection(String sectionName) {
        if (sectionName != null && sections.containsKey(sectionName)) {
            sections.get(sectionName).setStyle("display:none");
        }
    }

    protected void showSection(String sectionName) {
        if (sectionName != null && sections.containsKey(sectionName)) {
            sections.get(sectionName).setStyle("");
        }
    }

    public void setVisible(String fieldId, boolean visible) {
        if (fields.get(fieldId) != null) {
            fields.get(fieldId).setVisible(visible);
        }
    }

    protected void addFieldToPanel(Field field) {
        if (!field.isUseless()) {
            Element element = DOM.getElementById("label" + field.getId());
            if (element != null && field.getTitle() != null) {
                HTML label = field.getTitleAsWidget();
                String labelhtml = "<div class='form-group__label'>" + label.getHTML() + "</div>";
                HTML fieldLabel = new HTML(labelhtml);
                fieldLabel.setVisible(!field.isHidden());
                if (field.isHidden()) {
                    hiddenTitles.put(field.getId(), fieldLabel);
                }
                panel.addAndReplaceElement(fieldLabel, element);
                List<Widget> widgets = this.widgets.containsKey("label" + field.getId()) ? this.widgets.get("label" + field.getId()) : new ArrayList<Widget>();
                widgets.add(label);
                this.widgets.put("label" + field.getId(), widgets);
            }

            element = DOM.getElementById("input" + field.getId());
            if (element != null && field.getWidget() != null) {
                showIfCustomField(element.getParentElement());
                addAndReplaceElement(field.getId(), field.getWidget(), element);
                List<Widget> widgets = this.widgets.containsKey("input" + field.getId()) ? this.widgets.get("input" + field.getId()) : new ArrayList<Widget>();
                widgets.add(field.getWidget());
                this.widgets.put("input" + field.getId(), widgets);
                if (field.getWidget() != null) {
                    List<Widget> validationWidgets = this.widgetsForValidation.containsKey(field.getId()) ? this.widgetsForValidation.get(field.getId()) : new ArrayList<Widget>();
                    validationWidgets.add(field.getWidget());
                    this.widgetsForValidation.put(field.getId(), widgets);
                }
            } else {
                ignoreValidation.add(field.getId());
            }
        }
    }

    HashMap<String, HTML> fields = new HashMap<>();

    private void createField(CustomizeFormItem field, MaterialPanel itemBody) {
        if (field.getDefaultValue() != null && field.isCustomField()) {
            if (field.getUiType().equals(UI_TYPE_DATEPICKER) && field.getDefaultValue() != null && !"".equals(field.getDefaultValue()) && ("TODAY".equals(field.getDefaultValue()) || "TOMORROW".equals(field.getDefaultValue())
                    || "YESTERDAY".equals(field.getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(field.getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(field.getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                defaultValueMap.put(field.getName(), DateUtils.format(currentDate));
            } else {
                defaultValueMap.put(field.getName(), field.getDefaultValue());
            }
        }
        HTML div = new HTML();
        div.setStyleName("form-group hideCustomField");

        StringBuilder str = new StringBuilder();
        if (!field.isLabelLess(getFormType())) {
            str.append("$$label:").append(field.getName()).append("$$");
        }
        str.append("<div class='form-group__content'>");
        str.append("$$input:").append(field.getName()).append("$$");
        str.append("</div>");

        div.setHTML(replaceControls(str.toString()));

        itemBody.add(div);

        fields.put(field.getName(), div);
    }

    private String getFormBodyStyle(int size) {
        String style = "";
        if (size == 1) {
            style = ColumnType.COL_1.getStyle();
        } else if (size == 2) {
            style = ColumnType.COL_2.getStyle();
        } else if (size == 3) {
            style = ColumnType.COL_3.getStyle();
        } else if (size == 4) {
            style = ColumnType.COL_4.getStyle();
        }
        return style;
    }

    protected abstract void initPredefinedValues();

    @Override
    protected void addButtonPanel() {
        updateWebhooksButton = new WfmButton2(wfmStrings.update());
        updateWebhooksButton.ensureDebugId("update_webhooks_button");
        updateWebhooksButton.setVisible(false);
        if (Utils.hasPermission(PermissionConstants.ADD_WEBHOOK_FROM_CUSTOMIZE)) {
            footer.addToRightSide(updateWebhooksButton);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setForm(getFormID());
        AllInOneService.App.get().getWorkflowWebHooks(fp, new AsyncCallback<ListResult<WorkflowWebHookListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ListResult<WorkflowWebHookListItem> result) {
                if (result != null && result.getList().size() > 0) {
                    updateWebhooksButton.setVisible(true);
                    updateWebhooksButton.addClickHandler(click -> runWebhooks());
                }
            }
        });

        customizeButton = new SplitButton(120, BTN_DEFAULT_OUTLINE);
        customizeButton.ensureDebugId("customize_button");

        if (Utils.hasRole(ADMIN)) {
            addCustomizeButton();
        }

        if (!customizeButton.getItemsMap().isEmpty() || customizeButton.getDefaultItem() != null) {
            footer.addToRightSide(customizeButton);
        }
        super.addButtonPanel();
    }

    private void addCustomizeButton() {
        List<SplitButtonItem> customizeButtonItems = new ArrayList<>();
        customizeButtonItems.add(new SplitButtonItem("CUSTOMIZE", wfmStrings.customize(), () -> {
            String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
            SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
        }, true));
        customizeButton.addItemList(customizeButtonItems);
//        WfmButton2 customize = new WfmButton2(wfmStrings.customize(), BTN_DEFAULT_OUTLINE);
//        customize.ensureDebugId("customize");
//        customize.setTooltip(layoutStrings.customizeLayout());
//        customize.addClickHandler(clickEvent -> {
//            String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
//            SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
//        });
//        footer.addToRightSide(customize);
    }

    public Widget getDefaultValue(ModelField field, boolean dataModifiedIncrementer, final View theView) {
        Widget widget = null;
        if (field.getWidget() != null) {
            if (Constants.UI_TYPE_DROPDOWN.equals(field.getWidget())) {
                widget = new DataListBox();
                if (dataModifiedIncrementer) {
                    ((DataListBox) widget).addValueChangeHandler(changeEvent -> {
                        if (theView != null) {
                            theView.dataModified();
                        } else {
                            dataModified();
                        }
                    });
                }
                setPredefinedValues(field.getField_ID(), widget);
            }
            if (Constants.UI_TYPE_TEXTBOX.equals(field.getWidget())) {
                widget = new TextBox();
                if (dataModifiedIncrementer) {
                    ((TextBox) widget).addValueChangeHandler(stringValueChangeEvent -> {
                        if (theView != null) {
                            theView.dataModified();
                        } else {
                            dataModified();
                        }
                    });
                }
            }
            if (Constants.UI_TYPE_TEXTAREA.equals(field.getWidget())) {
                widget = new TextArea();
                if (dataModifiedIncrementer) {
                    ((TextArea) widget).addValueChangeHandler(stringValueChangeEvent -> {
                        if (theView != null) {
                            theView.dataModified();
                        } else {
                            dataModified();
                        }
                    });
                }
            }
            if (Constants.UI_TYPE_DATEPICKER.equals(field.getWidget())) {
                widget = new DatePicker(true);
                if (dataModifiedIncrementer) {
                    ((DatePicker) widget).addChangeHandler(changeEvent -> {
                        if (theView != null) {
                            theView.dataModified();
                        } else {
                            dataModified();
                        }
                    });
                }
            }
        }
        if (widget == null) {
            widget = new HTML(wfmStrings.notAvailable());
        }
        return widget;
    }

    private void setDefaultValueToWidget(Widget widget, String value) {
        if (value != null && value.length() > 0 && widget != null) {
            if (widget instanceof KpiSelect2) {
                KpiSelect2 select2 = ((KpiSelect2) widget);
                for (SelectItem item : select2.getItems()) {
                    if (item.getName().equals(value)) {
                        ((KpiSelect2) widget).setSelected(item.getId());
                    }
                }
            } else if (widget instanceof UL) {
                UL ul = (UL) widget;
                for (int i = 0; i < ul.getWidgetCount(); i++) {
                    UL.LI li = (UL.LI) ul.getWidget(i);
                    Widget w = li.getWidget(0);
                    if (w instanceof KpiCheckBox) {
                        KpiCheckBox checkBox = (KpiCheckBox) w;
                        if (value.contains(checkBox.getName())) {
                            checkBox.setValue(true);
                        }

                    } else if (w instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) w;
                        if (value.contains(radioButton.getText())) {
                            radioButton.setValue(true);
                            break;
                        }
                    }
                }
            }
            if (widget instanceof TextArea2) {
                ((TextArea2) widget).setText(value);
            } else if (widget instanceof TextBoxBase) {
                ((TextBoxBase) widget).setValue(value);
            } else if (widget instanceof DatePicker) {
                try {
                    ((DatePicker) widget).setDate(DateUtils.parse(value));
                } catch (Exception ignored) {
                }
            } else if (widget instanceof DateTimeWidget) {
                try {
                    ((DateTimeWidget) widget).setDateTime(DateUtils.parseLongFormat(value));
                } catch (Exception ignored) {
                }
            } else if (widget instanceof CustomFieldLookUp) {
                try {
                    Integer lookUpId = Integer.parseInt(value.substring(0, value.indexOf(Constants.DELIMITR)));
                    ((CustomFieldLookUp) widget).setSelected(new SelectItem(lookUpId, value.substring(value.indexOf(Constants.DELIMITR) + 3)));
                } catch (Exception ignored) {
                }
            } else if (widget instanceof PercentageWidget) {
                try {
                    ((PercentageWidget) widget).setText(value);
                } catch (Exception ignored) {
                }
            } else if (widget instanceof AutoNumberCustomField) {
                try {
                    ((AutoNumberCustomField) widget).setText(value);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    protected void addAndReplaceElement(String fieldID, Widget widget, Element element) {
        if (layoutInterface instanceof LayoutRPC) {
            super.addAndReplaceElement(fieldID, widget, element);
        } else {
            ModelField modelField = modelForm.getFieldByFieldID(fieldID);
            if (modelField != null && modelField.getHelpMessage() != null && !"".equals(modelField.getHelpMessage())) {
                WfmToolTip toolTip = new WfmToolTip();
                toolTip.setHelpText(modelField.getHelpMessage());
                panel.addAndReplaceElement(Utils.getInHorizontalPanel(0, -1, modelField.isFullWidth(), widget, toolTip), element);
            } else {
                panel.addAndReplaceElement(widget, element);
            }
        }
    }

    protected void setDefaultValues() {
        if (!(layoutInterface instanceof LayoutRPC)) {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    for (Map.Entry<String, List<Widget>> widgets : widgets.entrySet()) {
                        if (widgets.getKey().startsWith("input")) {
                            String fieldID = widgets.getKey().replace("input", "");
                            String defaultValue = defaultValueMap.get(fieldID);
                            if (defaultValue != null && defaultValue.length() > 0) {
                                for (Widget widget : widgets.getValue()) {
                                    setDefaultValueToWidget(widget, defaultValue);
                                }
                            }
                        }
                    }
                }
            };
            timer.schedule(800);
        }
    }

    public String localize(String codeWord) {
        String result = Localize.getInstance().localizeByFieldID(getFormID(), codeWord);
        return result != null ? result.toUpperCase() : codeWord;
    }

    public String getFieldLabel(String fieldID) {
        return Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
    }

    public void addField(String id, Widget widget) {
        String title = getFieldLabel(id);
        addField(id, widget, title == null ? "" : title);
    }

    public void setPredefinedValues(String fieldID, Widget widget) {
        if (fieldID != null && widget instanceof DataListBox) {
            ((DataListBox) widget).setItems(predefinedItems.get(fieldID));
        }
    }

    public int customValidate() {
        int error = validateNonStandartFields();
        if (!(layoutInterface instanceof LayoutRPC)) {
            if (layoutInterface != null && layoutInterface.getRequiredCodes().size() > 0) {
                for (String fieldID : layoutInterface.getRequiredCodes()) {
                    if (!ignoreValidation.contains(fieldID)) {
                        ModelField field = modelForm.getFieldByFieldID(fieldID);
                        if (widgetsForValidation.get(fieldID) != null && field.getWidget() != null && fieldMap.containsKey(fieldID) && !field.isIsCustomField()) {
                            if (fieldMap.get(fieldID) != null && fieldMap.get(fieldID).getWidget() != null) {
                                error += validate(fieldID, ValidationType.IsEmpty.getId(), field.getWidget(), fieldMap.get(fieldID).getWidget());
                            }
                        }
                    }
                }
            }
        }
        return error;
    }

    public void addPredefinedValues(String fieldID, SelectItem[] items) {
        predefinedItems.put(fieldID, items);
    }

    private void runWebhooks() {
        LoadingPanel.loading(true);
        HashMap<String, Object> keyValues = getKeyValues();
        AllInOneService.App.get().runFormWebhooks(new RunWebHookRequestItem(getFormID(), relationType, valueId, keyValues, null, null), new AsyncCallback<HashMap<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(HashMap<String, Object> result) {
                if (result != null && !result.isEmpty()) {
                    for (String key : result.keySet()) {
                        if (result.get(key) != null && fieldMap.get(key) != null) {
                            Widget widget = fieldMap.get(key).getWidget();
                            if (widget instanceof TextBox) {
                                ((TextBox) widget).setText((String) result.get(key));
                            } else if (widget instanceof DatePicker) {
                                try {
                                    ((DatePicker) widget).setDate(DateUtils.parse((String) result.get(key), DateTimeFormat.getFormat("yyyy-MM-dd")));
                                } catch (DateFormatException ignored) {
                                }
                            } else if (widget instanceof InputGroup) {
                                widget = ((InputGroup) widget).getWidget(1);
                                if (widget instanceof TextBox) {
                                    ((TextBox) widget).setText((String) result.get(key));
                                }
                            } else if (widget instanceof FlexTable) {
                                FlexTable flexTable = ((FlexTable) widget);
                                for (int i = 0; i < flexTable.getRowCount(); i++) {
                                    for (int i1 = 0; i1 < flexTable.getCellCount(i); i1++) {
                                        Widget column = (flexTable).getWidget(i, i1);
                                        if (CustomFormConstants.GENDER.equals(key)) {
                                            Widget column2 = (flexTable).getWidget(i, i1 + 1);
                                            ((RadioButton) column).setValue(Constants.MALE.equals(result.get(key)));
                                            ((RadioButton) column2).setValue((Constants.FEMALE).equals(result.get(key)));
                                            break;
                                        }

                                    }
                                }
                            } else if (widget instanceof DataListBox) {
                                DataListBox dataListBox = (DataListBox) widget;
                                for (int i = 0; i < dataListBox.getItems().length; i++) {
                                    if (CustomFormConstants.MARTIAL_STATUS.equals(key)) {
                                        if (Constants.SINGLE.equalsIgnoreCase(result.get(key).toString())) {
                                            if (443 == Integer.parseInt(dataListBox.getItems()[i].getKey())) {
                                                dataListBox.setSelected(dataListBox.getItems()[i]);
                                            }
                                        } else {
                                            if (444 == Integer.parseInt(dataListBox.getItems()[i].getKey())) {
                                                dataListBox.setSelected(dataListBox.getItems()[i]);
                                            }
                                        }
                                    }
                                }
                            } else if (widget instanceof MultiTableNewUI) {
                                if (ADDRESS.equals(key)) { //bu casega kirish uchun db ga "ADDRESS" namelik key qo`shish kerak va value null kelmasligi kerak
                                    MultiTableNewUI addressInf = (MultiTableNewUI) widget;
                                    for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {
                                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                                        addressWidget.name.setValue((String) result.get("Address Name"));
                                        addressWidget.street.setValue((String) result.get("Address Line"));
//                                        addressWidget.streetB.setValue("line 2");
                                        addressWidget.city.setValue((String) result.get("City"));
                                        addressWidget.country.setSelected(228);
                                    }
                                }
                            } else if (widget instanceof EditableTable) {
                                EditableTable editableTable = (EditableTable) widget;
                                if (CustomFormConstants.EXPERIENCE.equals(key)) {
                                    for (int i = 0; i < editableTable.getColumns().length; i++) {
                                        if (ItemTableConstants.HIRE_DATE.equals(editableTable.getColumns()[i].getName())) {
                                            GeneralEmployeeEditForm.CustomDatePicker hireDate = (GeneralEmployeeEditForm.CustomDatePicker) editableTable.getColumnById(0, ItemTableConstants.HIRE_DATE);
                                            hireDate.setDate(new Date(result.get("I_T_HIRE_DATE").toString()));
                                            editableTable.getGrid().getModel().update(0, 1, hireDate);
                                        } else if (ItemTableConstants.POSITION.equals(editableTable.getColumns()[i].getName())) {
                                            CustomCellTextBox position = (CustomCellTextBox) editableTable.getColumnById(0, ItemTableConstants.POSITION);
                                            position.setText((String) result.get("I_T_POSITION"));
                                            editableTable.getGrid().getModel().update(0, 4, position);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                LoadingPanel.loading(false);
            }
        });

    }

    protected void runItemTableWebHooks(ItemTableEnum itemTableType, String uuid, EditableTable table) {
        LoadingPanel.loading(true);
        HashMap<String, Object> keyValues = getKeyValues();
        AllInOneService.App.get().runItemTableWebhooks(new RunWebHookRequestItem(getFormID(), relationType, valueId, keyValues, itemTableType, uuid), new AsyncCallback<ArrayList<HashMap<String, Object>>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(ArrayList<HashMap<String, Object>> result) {
                LoadingPanel.loading(false);
                table.removeAllRows();
                if (result != null && !result.isEmpty()) {
                    for (HashMap<String, Object> item : result) {
                        table.getListener().addRow();
                        for (String key : item.keySet()) {
                            if (item.get(key) != null) {
                                Widget widget = table.getColumnById(table.getGrid().getCurrentRow(), key);
                                if (widget instanceof TextBox) {
                                    Object value = item.get(key);
                                    ((TextBox) widget).setText(value instanceof Long || value instanceof Double ? String.valueOf(value) : (String) value);
                                    table.refreshCustomCellDisplayValue(table.getGrid().getCurrentRow(), key);
                                } else if (widget instanceof DatePicker) {
                                    try {
                                        ((DatePicker) widget).setDate(DateUtils.parse((String) item.get(key), DateTimeFormat.getFormat("yyyy-MM-dd")));
                                        table.refreshCustomCellDisplayValue(table.getGrid().getCurrentRow(), key);
                                    } catch (DateFormatException ignored) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void showTitle(String columnCode, boolean visible) {
        if (hiddenTitles.get(columnCode) != null) {
            hiddenTitles.get(columnCode).setVisible(visible);
        }
    }

    private HashMap<String, Object> getKeyValues() {
        HashMap<String, Object> keyValues = new HashMap<>();
        for (String key : fieldMap.keySet()) {
            if (fieldMap.get(key).getWidget() instanceof TextBox && ((TextBox) fieldMap.get(key).getWidget()).getText() != null) {
                keyValues.put("${" + key.toLowerCase() + "}", ((TextBox) fieldMap.get(key).getWidget()).getText());
            }
            if (fieldMap.get(key).getWidget() instanceof Numbering && ((Numbering) fieldMap.get(key).getWidget()).getNumberData(false) != null) {
                keyValues.put("${" + key.toLowerCase() + "}", ((Numbering) fieldMap.get(key).getWidget()).getNumberData(false).getNumberString());
            }
            if (fieldMap.get(key).getWidget() instanceof DatePicker && ((DatePicker) fieldMap.get(key).getWidget()).getDate() != null) {
                keyValues.put("${" + key.toLowerCase() + "}", DateUtils.getDateFormatShort(((DatePicker) fieldMap.get(key).getWidget()).getDate()));
            }
            if (fieldMap.get(key).getWidget() instanceof DOBWidget && ((DOBWidget) fieldMap.get(key).getWidget()).getDOBDate() != null) {
                keyValues.put("${" + key.toLowerCase() + "}", DateUtils.getDateFormatShort(((DOBWidget) fieldMap.get(key).getWidget()).getDOBDate()));
            }
        }
        return keyValues;
    }

    protected void getItemTableWebhooks(EditableTable table, ItemTableEnum type, String uuid) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setItemTable(true);
        fp.setForm(type.getTitle());
        AllInOneService.App.get().getWorkflowWebHooks(fp, new AsyncCallback<ListResult<WorkflowWebHookListItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ListResult<WorkflowWebHookListItem> result) {
                if (result != null && result.getList().size() > 0) {
                    table.getRunWebhooks().setVisible(true);
                    table.getRunWebhooks().addClickHandler(click -> runItemTableWebHooks(type, uuid, table));
                }
            }
        });
    }

}
