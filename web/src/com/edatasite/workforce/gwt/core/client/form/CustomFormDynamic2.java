package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutInterface;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.PageBreak;
import com.edatasite.workforce.gwt.core.client.ui.PercentageWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.AutoNumberCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ValidationType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTip;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10/20/11
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CustomFormDynamic2 extends CustomForm implements CustomFormConstants, PermissionConstants {

    
    private final HashMap<String, SelectItem[]> predefinedItems = new HashMap<>();
    private ModelForm modelForm;
    private final LinkedHashMap<String, MaterialCollapsibleItem> sections = new LinkedHashMap<>();
    private final MaterialCollapsible collapsible = new MaterialCollapsible();
    protected MaterialPanel paginationDiv = new MaterialPanel();
    private final Map<String, String> defaultValueMap = new HashMap<>();
    protected boolean hasPaginationField = false;
    private PageBreak pageBreak;
    private final Map<String, LinkedList<String>> sectionFieldsName = new HashMap<>();

    private final Map<Integer, Set<String>> sectionMaps = new HashMap<>();

    protected CustomFormDynamic2() {
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

    protected CustomFormDynamic2(String name, String description) {
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
            service.getModelGridForm(getFormID(), new AbstractAsyncCallback<ModelForm>() {
                @Override
                public void failure(Throwable caught) {
                    CustomFormDynamic2.this.closeTab();
                }

                @Override
                public void success(ModelForm result) {
                    modelForm = result;
                    setFormRPCAndInit(result);
                    doSectionsOrderForPagination();
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
            panel.addStyleName("summary file--CustomFormDynamic2");
            panel.add(drawCollapsible());
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

    private void doSectionsOrderForPagination() {
        Integer count = 0;
        Set<String> sectionNames = new HashSet<>();
        for (String sectionName : modelForm.getGridColumnMap().keySet()) {
            sectionFieldsName.put(sectionName, modelForm.getGridColumnMap().get(sectionName).stream().map(CustomizeFormItem::getName).collect(Collectors.toCollection(LinkedList::new)));
            if (!modelForm.getSectionsRpcMap().get(sectionName).isPagination()) {
                sectionNames.add(sectionName);
            } else {
                hasPaginationField = true;
                sectionMaps.put(++count, sectionNames);
                sectionNames = new HashSet<>();
                sectionNames.add(sectionName);
            }
        }
        sectionMaps.put(++count, sectionNames);

        if (hasPaginationField) {
            paginationDiv.setWidth("100%");
            paginationDiv.setPaddingLeft(33);
            paginationDiv.setPaddingBottom(22);
            panel.add(paginationDiv);
            pageBreak = new PageBreak();
            paginationDiv.add(pageBreak);
            pageBreak.setSectionFields(sections);
            pageBreak.setSectionMaps(sectionMaps);
            for (String section : sections.keySet()) {
                if (sections.get(section) != null) {
                    sections.get(section).setVisible(false);
                }
            }
            pageBreak.clickedEvent(1);
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
            modelForm.getGridColumnMap().forEach((section, fields) -> {

                DynamicSectionsRpc rpc = modelForm.getSectionsRpcMap() != null ? modelForm.getSectionsRpcMap().get(section) : null;
                if (rpc != null && rpc.isExpanded()) {
                    expandedSection.add(section);
                }
                MaterialCollapsibleItem item = new MaterialCollapsibleItem();
                item.addStyleName("hideCustomField");
                item.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);

                MaterialCollapsibleBody itemBody = new MaterialCollapsibleBody();

                final MaterialPanel[] gridRow = {new MaterialPanel("grid-row")};
                AtomicInteger width = new AtomicInteger();
                AtomicInteger y = new AtomicInteger();
                fields.stream().sorted(Comparator.comparingInt(CustomizeFormItem::getForder))
                        .filter(CustomizeFormItem::isActive).forEach(field -> {

                    width.getAndAdd(field.getWidth());
                    if (width.get() > 12 || y.get() != field.getY()) {
                        width.set(0);
                        gridRow[0] = new MaterialPanel("grid-row");
                        itemBody.add(gridRow[0]);
                    }
                    y.set(field.getY());
                    if (hasSections.contains(field.getSection())) {
                        MaterialPanel gridBoxCol = new MaterialPanel("col-" + field.getWidth());
                        gridRow[0].add(gridBoxCol);
                        createField(field, gridBoxCol);
                    } else {
                        hasSections.add(field.getSection());
                        if (collapse) {
                            String s = localize(field.getSection());
                            if (rpc != null && rpc.getLabel() != null && !rpc.getLabel().isEmpty()) {
                                s = rpc.getLabel();
                            }
                            MaterialCollapsibleHeader collapsibleHeader = new MaterialCollapsibleHeader(new MaterialLink(humanize(s)));
                            collapsibleHeader.getElement().setAttribute("section", humanize(s));
                            item.add(collapsibleHeader);
                        } else {
                            itemBody.addStyleName("no-border");
                        }
                        itemBody.add(gridRow[0]);

                        MaterialPanel gridBoxCol = new MaterialPanel("col-" + field.getWidth());
                        gridRow[0].add(gridBoxCol);
                        createField(field, gridBoxCol);

                        item.add(itemBody);
                        collapsible.add(item);
                        sections.put(field.getSection(), item);
                    }
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
        if (Utils.hasRole(Constants.ADMIN)) {
//            addCustomizeButton();
            if (Utils.isLocalhost() || Utils.isDevhost()) {
//                addCustomizeButton2();
            }
        }
        super.addButtonPanel();
    }

    private void addCustomizeButton() {
        WfmButton2 customize = new WfmButton2(wfmStrings.customize(), BTN_DEFAULT_OUTLINE);
        customize.ensureDebugId("customize");
        customize.setTooltip(wfmStrings.customizeLayout());
        customize.addClickHandler(clickEvent -> {
            String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
            SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
        });
        footer.addToRightSide(customize);
    }

    private void addCustomizeButton2() {
        WfmButton2 customize2 = new WfmButton2(wfmStrings.customize(), BTN_DEFAULT_OUTLINE);
        customize2.ensureDebugId("customize");
        customize2.setTooltip(wfmStrings.customizeLayout());
        customize2.addClickHandler(event -> {
            String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
            SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm2|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
        });
        footer.addToRightSide(customize2);
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
            } else if (widget instanceof KpiEditor) {
                ((KpiEditor) widget).setData(value);
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

    protected void showRequiredFieldsAfterValidation() {
        Set<String> invalidFieldsColumnCode = this.getCustomFieldUtil().validationObjects.keySet();
        for (String fieldColumnCode : invalidFieldsColumnCode) {
            for (String sectionName : sectionFieldsName.keySet()) {
                if (sectionFieldsName.get(sectionName).contains(fieldColumnCode)) {
                    if (pageBreak != null) {
                        pageBreak.setActivePageId(sectionName);
                    }
                    return;
                }
            }
        }
    }
}
