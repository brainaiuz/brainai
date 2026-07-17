
package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormValidation;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutInterface;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ValidationType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10/20/11
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CustomForm extends View implements CustomFormConstants, FittedContent {
    private final String controlPattern = "\\$\\$\\w+(?::(?:[A-Za-z]|_[A-Za-z])\\w*|)\\$\\$";

    public static final AllInOneServiceAsync service = AllInOneService.App.get();
    protected ViewFooter footer;
    protected HTMLPanel panel;
    boolean isReadyToReplace;
    private final ArrayList<Field> widgetsInQueue = new ArrayList<>();
    LayoutInterface layoutInterface;
    private boolean show;
    protected HashMap<String, List<Widget>> widgets = new HashMap<>();
    protected HashMap<String, List<Widget>> widgetsForValidation = new HashMap<>();
    public MaterialPanel exportPanel;
    ArrayList<Widget> errorWidgets = new ArrayList<>();
    protected ArrayList<String> ignoreValidation = new ArrayList<>();
    protected HashMap<String, Field> fieldMap = new HashMap<>();
    protected HashMap<String, Widget> buttons = new HashMap<>();

    private String labelEndParameter = ":";

    protected CustomForm() {
    }

    public ArrayList<String> getIgnoreValidation() {
        return ignoreValidation;
    }

    public CustomForm(String name) {
        super(name);
    }

    public CustomForm(String name, String description) {
        super(name, description);
    }

    @Override
    protected Widget onInitialize() {
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
        if (layoutInterface == null) {
            service.getFormData(getFormID(), getFormType(), new AbstractAsyncCallback<LayoutRPC>() {
                @Override
                public void failure(Throwable caught) {
                    CustomForm.this.closeTab();
                }

                @Override
                public void success(LayoutRPC result) {
                    setFormRPCAndInit(result);
                }
            });
        } else {
            setFormRPCAndInit(layoutInterface);
        }
        return this;
    }

    public void setFormRPC(LayoutInterface result) {
        layoutInterface = result;
    }

    public void setFormRPCAndInit(LayoutInterface result) {
        if (result == null) {
            Info.warn(wfmStrings.formIsNotReady());
            closeTab();
            return;
        }
        layoutInterface = result;
        initForm();
    }

    public ArrayList<String> getRequiredCodes() {
        return layoutInterface == null ? new ArrayList<>() : layoutInterface.getRequiredCodes();
    }

    protected String replaceControls(String layoutHTML) {
        RegExp regExp = RegExp.compile(controlPattern);
        for (MatchResult matcher = regExp.exec(layoutHTML); matcher != null; matcher = regExp.exec(layoutHTML)) {
            String group, temp;
            group = temp = matcher.getGroup(0);
            if (temp != null && temp.startsWith("$$")) {
                temp = temp.substring(2, temp.length() - 2);
                if (temp.contains(":")) {
                    String[] controlTokens = temp.split(":");
                    if (controlTokens[0].equals("label")) {
                        layoutHTML = layoutHTML.replace(group, "<label id=\"label" + controlTokens[1] + "\"></label>");
                    } else if (controlTokens[0].equals("input")) {
                        layoutHTML = layoutHTML.replace(group, "<div id=\"input" + controlTokens[1] + "\" ></div>");
                    }
                }
            }
        }
        return layoutHTML;
    }

    protected void initForm() {
        clear();
        exportPanel = new MaterialPanel();
        exportPanel.setVisible(false);
        add(exportPanel);
        addPanel(layoutInterface);
        addButtonPanel();
        add(panel);
        isReadyToReplace = true;
        onReadyToReplaceFields();
    }
    protected void addPanel(LayoutInterface layoutInterface) {
        panel = new HTMLPanel(replaceControls(layoutInterface.getLayout()));
        panel.add(footer);
    }

    protected void addButtonPanel() {
        addButtons();
    }

    private Localize localize;

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }


    protected MaterialDropDown addMoreButton(String text) {
        MaterialMenuBar menubar = new MaterialMenuBar();
        menubar.setClass("dropdown-kit--arrow--below--top");

        MaterialLink ieLink = new MaterialLink(("".equals(text.trim()) ? wfmStrings.more() : text) + " ");
        ieLink.setDataAttribute("alignment", "right");
        ieLink.setHref("javaScript:void(0)");
        ieLink.setClass("btn btn--white btn--primary hasicon--right");

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);

        menubar.add(ieLink);
        menubar.add(menuContainer);

        addButton(menubar);
        return menuContainer;
    }

    protected MaterialDropDown addMoreButtonWhite(String text) {
        MaterialMenuBar menubar = new MaterialMenuBar();
        menubar.setClass("dropdown-kit--arrow--below--top");

        MaterialLink ieLink = new MaterialLink(("".equals(text.trim()) ? wfmStrings.more() : text) + " ");
        ieLink.setDataAttribute("alignment", "right");
        ieLink.setHref("javaScript:void(0)");
        ieLink.setClass("btn btn--white btn--outline hasicon--right");

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);

        menubar.add(ieLink);
        menubar.add(menuContainer);

        addButton(menubar);
        return menuContainer;
    }

    protected MaterialDropDown addMoreSplitButton(String text) {
        return addMoreSplitButton(text, null);
    }

    protected MaterialDropDown addMoreSplitButton(String text, Command cmd) {
        Div menubar = new Div();
        menubar.setClass("btn-group dropdown-split dropdown-split--top");


        MaterialLink name = new MaterialLink(("".equals(text.trim()) ? wfmStrings.more() : text) + " ");
        name.setClass("btn btn--white btn--outline hasicon--right");
        name.setHref("javaScript:void(0)");
        name.addClickHandler(event -> {
            if (cmd != null) {
                cmd.execute();
            }
        });

        MaterialLink ieLink = new MaterialLink();
        ieLink.setDataAttribute("alignment", "right");
        ieLink.setHref("javaScript:void(0)");
        ieLink.setStyleName("dropdown-button btn btn--white btn--outline");


        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);

        Icon moreIcon = new Icon();
        moreIcon.setClass("ficon--more-horiz");
        ieLink.add(moreIcon);
        Div div = new Div("btn-group dropdown-split__toggle");
        div.add(ieLink);
        div.add(menuContainer);
        menubar.add(name);
        menubar.add(div);


        addButton(menubar);
        return menuContainer;
    }

    protected WfmButton2 addEditButton() {
        WfmButton2 edit = new WfmButton2(wfmStrings.edit(), BTN_PRIMARY);
        edit.ensureDebugId("edit");
        addRightButton(edit);
        return edit;
    }

    protected WfmButton2 addRemoveButton() {
        WfmButton2 remove = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        remove.ensureDebugId("delete");
        addRightButton(remove);
        return remove;
    }

    protected WfmButton2 addPdfButton() {
        WfmButton2 pdf = new WfmButton2(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE);
        addRightButton(pdf);
        return pdf;
    }

    protected Widget addButton(MaterialSplitButton widget) {
        if (widget == null) {
            return null;
        }
        widget.addStyleName("dropdown-split--top");
        this.addButton((Widget) widget);
        return widget;
    }


    protected Widget addButton(Widget widget) {
        if (widget == null) {
            return null;
        }
        footer.addToRightSide(widget);
        return widget;
    }

    protected Widget addRightButton(Widget widget) {
        return addButton(widget);
    }

    protected Widget addButton(String id, Widget widget) {
        if (widget != null && id != null) {
            buttons.put(id, widget);
        }
        if (id != null && layoutInterface.isButtonPanelDisabled()) {
            addField(id, widget);
            return widget;
        }
        return addButton(widget);
    }

    protected WfmButton2 addButton(String label, ClickHandler... clickHandler) {
        return addButton(label, null, label.replace(" ", ""), clickHandler);
    }

    protected WfmButton2 addButton(String label, String styleName, ClickHandler... clickHandler) {
        return addButton(label, styleName, null, label.replace(" ", ""), clickHandler);
    }

    protected WfmButton2 addButton(String label, String ID, String ensureDebugID, ClickHandler... clickHandler) {
        return addButton(label, WfmButton2.BTN_PRIMARY, ID, ensureDebugID, clickHandler);
    }

    protected WfmButton2 addButton(String label, String stylyName, String ID, String ensureDebugID, ClickHandler... clickHandler) {
        return addButton(label, stylyName, ID, null, ensureDebugID, clickHandler);
    }

    protected WfmButton2 addButton(String label, String styleName, String ID, String iconStyle, String ensureDebugID, ClickHandler... clickHandler) {
        WfmButton2 button = new WfmButton2(label, styleName, iconStyle);
        button.ensureDebugId(ensureDebugID);
        if (clickHandler != null && clickHandler.length > 0 && clickHandler[0] != null) {
            button.addClickHandler(clickHandler[0]);
        }
        return (WfmButton2) addButton(ID, button);
    }

    private void onReadyToReplaceFields() {
        if (isReadyToReplace && !widgetsInQueue.isEmpty()) {
            for (Field field : widgetsInQueue) {
                addFieldToPanel(field);
            }
            widgetsInQueue.clear();
        }
        if (isReadyToReplace && show) {
            getDataToFillFields();
        }
    }

    protected void addFieldToPanel(Field field) {
        if (!field.isUseless()) {
            Element element = DOM.getElementById("label" + field.getId());
            if (element != null && field.getTitle() != null) {
                HTML label = field.getTitleAsWidget();
                panel.addAndReplaceElement(label, element);
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
                widgets.add(field.getSecondWidget());
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

    protected void addAndReplaceElement(String fieldID, Widget widget, Element element) {
        panel.addAndReplaceElement(widget, element);
    }

    protected void showIfCustomField(com.google.gwt.dom.client.Element element) {
        if (element != null) {
            String classStyle = element.getClassName();
            if (classStyle != null && classStyle.contains("hideCustomField")) {
                classStyle = classStyle.replace("hideCustomField", "");
                element.setClassName(classStyle);
            }
            showIfCustomField(element.getParentElement());
        }
    }

    public void showTitle(String columnCode, boolean visible) {

    }

    protected boolean existsFieldInForm(String fieldID) {
        return !fieldMap.isEmpty() && fieldMap.get(fieldID) != null;
    }

    protected abstract void addButtons();

    protected abstract void getDataToFillFields();

    protected abstract String getFormID();

    protected abstract String getFormType();

    protected abstract String getWikiCode();

    public void addPredefinedValues(String fieldID, SelectItem[] items) {

    }

    public void addField(String id, Widget widget) {
        addField(id, widget, "");
    }

    public void addField(String id, Widget widget, String title) {
        new Field(id, title, widget, null, false, false);
    }

    public void addField(String id, Widget widget, String title, boolean removeSemiColomn) {
        new Field(id, title, widget, null, removeSemiColomn, false);
    }

    public void addField(String id, Widget widget, String title, boolean removeSemiColomn, boolean hasInfo) {
        new Field(id, title, widget, null, removeSemiColomn, hasInfo);
    }

    public void addField(String id, Widget widget, String title, boolean removeSemiColomn, boolean hasInfo, boolean hidden) {
        new Field(id, title, widget, null, removeSemiColomn, hasInfo, hidden);
    }

    public void addField(String id, Widget widget, Widget secondWidget, String title, boolean removeSemiColomn) {
        new Field(id, title, widget, secondWidget, removeSemiColomn, false);
    }

    public Field addCustomField(String id, Widget widget, String title) {
        return new Field(id, title, widget, null, false, false);
    }

    public void addTitleField(String id, String title) {
        addField(id, null, title, true);
    }

    public void show() {
        show = true;
        if (isReadyToReplace) {
            getDataToFillFields();
        }
    }

    public String getTitle(String title, boolean... required) {
        return title + (required != null && required.length > 0 && required[0] ? "<em class='redTitle'>*</em>" : "") + labelEndParameter;
    }

    protected class Field {

        private Widget secondWidget;

        private String id;
        private String title;
        private final HTML titleHTML;
        private boolean isRequired;
        private Widget widget;
        private boolean hidden;

        private Field(String id, String title, Widget widget, Widget secondWidget, boolean removeSemiColomn, boolean hasInfo, boolean... hidden) {
            this.id = id;
            this.removeSemiColumn = removeSemiColomn;
            if (title != null) {
                if (title.endsWith(":")) {
                    title = title.replaceAll(":$", "");
                }
                if (title.endsWith("<em class='redTitle'>*</em>")) {
                    title = title.replace("<em class='redTitle'>*</em>", "");
                    isRequired = true;
                }
            }
            this.title = title;
            titleHTML = title == null ? null : new HTML(title + (isRequired() ? "<em class='redTitle'>*</em>" : "") + (isRemoveSemiColumn() ? "" : labelEndParameter) + (hasInfo ? "<i class=\"ficon--info\"></i>" : ""));
            this.widget = widget;
            this.secondWidget = secondWidget;
            this.hidden = hidden != null ? hidden[0] : false;
            if (!isReadyToReplace) {
                widgetsInQueue.add(this);
            } else {
                addFieldToPanel(this);
            }
            fieldMap.put(id, this);
        }
        private final boolean removeSemiColumn;

        public boolean isRequired() {
            return isRequired || (layoutInterface != null && layoutInterface.getRequiredCodes().contains(id));
        }

        public void setRequired(boolean required) {
            isRequired = required;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public Widget getSecondWidget() {
            return secondWidget;
        }

        public void setSecondWidget(Widget secondWidget) {
            this.secondWidget = secondWidget;
        }

        boolean isUseless() {
            return id == null || "".equals(id) || (widget == null && title == null);
        }

        HTML getTitleAsWidget() {
            return titleHTML;
        }

        boolean isRemoveSemiColumn() {
            return removeSemiColumn;
        }

        public void setVisible(boolean visible) {
            titleHTML.setVisible(visible);
            widget.setVisible(visible);
            if (secondWidget != null) {
                secondWidget.setVisible(visible);
            }
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }
    }

    protected void enableButton(boolean enable) {
        for (Widget button : footer.getRightSideWidgets()) {
            if (button instanceof WfmButton2) {
                ((WfmButton2) button).setEnabled(enable);
            }
            if (button instanceof MaterialSplitButton) {
                if (enable) {
                    button.removeStyleName("disabled disabled-over");
                } else {
                    button.addStyleName("disabled disabled-over");
                }
            }
        }
    }

    public void setInnerHTML(HTML html, String innerHTML) {
        if (html != null) {
            html.setHTML(innerHTML);
        }
    }

    public int customValidate() {
        int error = validateNonStandartFields();
        if (getFormRPC() != null && !getFormRPC().getValidations().isEmpty()) {
            for (CustomFormValidation validation : getFormRPC().getValidations()) {
                if (!ignoreValidation.contains(validation.getFieldCode())) { // && (validation.getJoinedFieldCode() == null || "".equals(validation.getJoinedFieldCode()) || !ignoreValidation.contains(validation.getJoinedFieldCode()))
                    if (widgetsForValidation.get(validation.getFieldCode()) != null && validation.getWidgetType() != null && fieldMap.containsKey(validation.getFieldCode())) {
                        if (fieldMap.get(validation.getFieldCode()) != null && fieldMap.get(validation.getFieldCode()).getWidget() != null) {
                            error += validate(validation.getFieldCode(), validation.getValidationTypeID(), validation.getWidgetType(), fieldMap.get(validation.getFieldCode()).getWidget());
                        }
                    }
                }
            }
        }
        return error;
    }

    private LayoutRPC getFormRPC() {
        return (LayoutRPC) layoutInterface;
    }

    protected int validateNonStandartFields() {
        return 0;
    }

    protected int validate(String fieldCode, Integer validationTypeID, String widgetType, Widget... widgets) {
        int error = 0;
        if (widgets != null && widgets.length > 0) {
            if (Constants.UI_TYPE_RADIOBUTTON.equals(widgetType) || Constants.UI_TYPE_CHECKBOX.equals(widgetType)) {
                error += validateCheckBox(widgets);
            }
            for (Widget widget : widgets) {
                int i = error;
                if (Constants.UI_TYPE_TEXTBOX.equals(widgetType) && widget != null && widget instanceof TextBoxBase) {
                    error += validateTextBox((TextBoxBase) widget, validationTypeID);
                } else if (Constants.UI_TYPE_TEXTAREA.equals(widgetType) && widget != null && widget instanceof TextArea2) {
                    error += validateTextArea2((TextArea2) widget, validationTypeID);
                } else if (Constants.UI_TYPE_DROPDOWN.equals(widgetType) && widget != null && widget instanceof ListBox) {
                    error += validateListBox((ListBox) widget, validationTypeID);
                } else if (Constants.UI_TYPE_LOOKUP.equals(widgetType) && widget != null && widget instanceof LookUp) {
                    error += validateLookUp((LookUp) widget, validationTypeID);
                } else if (Constants.UI_TYPE_DATEPICKER.equals(widgetType) && widget != null && widget instanceof DatePicker) {
                    error += validateDatePicker((DatePicker) widget, validationTypeID);
                } else if (Constants.UI_TYPE_MULTITABLE.equals(widgetType) && widget != null && widget instanceof MultiTable) {
                    error += validateMultiTable((MultiTable) widget, validationTypeID);
                } else if (Constants.UI_TYPE_PHONENUMBER.equals(widgetType) && widget != null && widget instanceof PhoneNumber) {
                    error += validatePhoneNumber((PhoneNumber) widget, validationTypeID);
                } else if (widget instanceof NoteWidget) {
                    error += validateNoteWidget((NoteWidget) widget);
                }
                markAsError(fieldCode, widget, error > i);
            }
        }
        return error;
    }

    private int validateNoteWidget(NoteWidget widget) {
        return widget != null && widget.getNewNotesToSave().isEmpty() && "".equals(widget.getTextBox().getText()) ? 1 : 0;
    }

    private int validatePhoneNumber(PhoneNumber widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget != null && (widget.toString() == null || "".equals(widget.toString())) ? 1 : 0;
        }
        return 0;
    }

    private int validateCheckBox(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                if (widget instanceof KpiCheckBox) {
                    if (((KpiCheckBox) widget).getValue() != null && ((KpiCheckBox) widget).getValue()) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    private int validateDatePicker(DatePicker widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget.getDate() == null ? 1 : 0;
        }
        return 0;
    }

    private int validateLookUp(LookUp widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return !widget.isSelected() ? 1 : 0;
        }
        return 0;
    }

    private int validateMultiTable(MultiTable widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget.isFilled() ? 0 : 1;
        }
        return 0;
    }

    private int validateListBox(ListBox widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget.getSelectedIndex() > 0 ? 0 : 1;
        }
        return 0;
    }

    private int validateTextArea2(TextArea2 widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget.getText() == null || "".equals(widget.getText()) ? 1 : 0;
        }
        return 0;
    }

    private int validateTextBox(TextBoxBase widget, Integer validationTypeID) {
        if (ValidationType.IsEmpty.getId().equals(validationTypeID)) {
            return widget.getValue() == null || "".equals(widget.getValue()) ? 1 : 0;
        } else if (ValidationType.IsEmail.getId().equals(validationTypeID)) {
            return widget.getValue() == null || "".equals(widget.getValue()) ? 0 : Utils.validateEmail(widget.getValue(), false) ? 0 : 1;
        }
        return 0;
    }

    public HTML initHTML() {
        return initHTML(false);
    }

    public HTML initHTML(boolean StyleName) {
        HTML html = new HTML();
        if (StyleName) {
            html.addStyleName("form-control");
        }
        return html;
    }

    public int markAsError(String id, Widget widget, boolean isWrong) {
        if (id == null || !ignoreValidation.contains(id)) {
            return markAsError(widget, isWrong);
        }
        return 0;
    }

    public int markAsError(Widget widget, boolean isWrong) {
        if (widget != null && isWrong) {
            errorWidgets.add(widget);
            widget.addStyleName(Constants.ERROR_FORM_STYLE);

            Utils.openParentSection(widget);

            return 1;
        }
        return 0;
    }

    protected void clearErrorStyle() {
        if (errorWidgets != null && !errorWidgets.isEmpty()) {
            for (Widget widget : errorWidgets) {
                widget.removeStyleName(Constants.ERROR_FORM_STYLE);
            }
        }
        if (getCustomFieldUtil() != null) {
            getCustomFieldUtil().clearErrorMarks();
        }
    }

    public FormHasCustomField getCustomFieldUtil() {
        return null;
    }

    protected void setLabelEndParameter(String labelEndParameter) {
        this.labelEndParameter = labelEndParameter;
    }

    public HTMLPanel getPanel() {
        return panel;
    }

    public Widget getButton(String key) {
        return key == null || buttons.get(key) == null ? null : buttons.get(key);
    }

    protected boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    protected boolean mapHasValueForLang(HashMap<String, String> map, String lang) {
        return map != null && map.get(lang) != null && !map.get(lang).isEmpty();
    }

}
