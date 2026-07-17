package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DisableProvider;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.GlobalCallback;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * User: iskan
 * Date: Jan 10, 2008
 * Time: 10:56:45 PM
 */

public class WfmForm extends Composite implements CommandConstants, Constants, Clearable {

    public static final Integer ALIGN_LEFT = 1;
    public static final Integer ALIGN_RIGHT = 2;
    public static final Integer ALIGN_CENTER = 3;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private List fields = new ArrayList();
    protected HorizontalPanel buttons = new HorizontalPanel();
    private HorizontalPanel outButtons = new HorizontalPanel();
    private int labelPadding;
    private int widgetPadding;
    private VerticalPanel panel;
    private boolean points = true;
    //    private FlexTable flex;
    private FlowPanel container;
    //private HTMLTable.CellFormatter cellFormatter;
    private String[] columns;
    private boolean isHorizontal = true;
    protected int allColumns;


    public WfmForm() {
        this(null);
        isHorizontal = false;
    }

    public WfmForm(String[] columns) {
        this(columns, "100%");
    }

    public WfmForm(String[] columns, String panelWidth) {
        initWfmForm(columns, panelWidth);
    }

    private void initWfmForm(String[] columns, String panelWidth) {
        this.columns = columns;
        panel = new VerticalPanel();
        panel.setWidth(panelWidth);

        container = new FlowPanel();
        container.setStyleName("wfmform__container");
        outButtons.setWidth("100%");
        panel.add(container);
        panel.add(outButtons);
        initWidget(panel);
    }


    public GlobalCallback passCallback(GlobalCallback callback) {
        setButtonsEnabled(false);
        callback.setCommand(new DisableProvider() {
            public void enable() {
                setButtonsEnabled(true);
            }

            public void disable() {
                setButtonsEnabled(false);
            }
        });
        return callback;
    }

    private void setButtonsEnabled(boolean status) {
        for (int i = 0; i < buttons.getWidgetCount(); i++) {
            Widget button = buttons.getWidget(i);
            if (button instanceof WfmButton2) {
                WfmButton2 bu2 = (WfmButton2) button;
                bu2.setEnabled(status);
            }
        }
    }

    public Field addField(String label, Widget widget) {
        return addField(label, widget, false);
    }

    public Field addField(String label, Widget widget, boolean required, String toolTip, int colspan) {
        return addField(fields.size(), label, widget, false, null, colspan);
    }

    // required does nothing about validation(it doesn't know how to do it),
    // it only draws red asterics after label

    public Field addField(String label, Widget widget, boolean required) {
        return addField(fields.size(), label, widget, required, null, 0);
    }

    public Field addField(String label, Widget widget, String helpMessage, boolean required) {
        return addField(fields.size(), label, widget, required, null, 0, helpMessage, 0);
    }

    public Field addField(String label, Widget widget, String helpMessage, int helpTextScope, boolean required) {
        return addField(fields.size(), label, widget, required, null, 0, helpMessage, helpTextScope);
    }

    public Field addField(String label, Widget widget, boolean required,
                          String toolTip) {
        return addField(fields.size(), label, widget, required, toolTip, 0);
    }

    public Field addField(int index, String label, Widget widget,
                          boolean required, String toolTip) {
        return addField(index, label, widget, required, toolTip, 0);
    }

    public Field addField(int index, String label, Widget widget) {
        return addField(index, label, widget, false, null, 0);
    }

    public Field addField(int index, String label, Widget widget,
                          boolean required, String toolTip, int colspan) {
        return addField(index, label, widget, required, toolTip, colspan, null, 0);
    }

    public Field addField(int index, String label, Widget widget,
                          boolean required, String toolTip, int colspan, String helpText, int helpTextScope) {
        Field field = new Field(label, widget, required, null, helpText, helpTextScope);

        if (widget instanceof HorizontalPanel) {
            throw new IllegalArgumentException("While adding a widget to the WfmForm avoid use of HorizontalPanels, please try to use the widget that implemented HasText or Clearable interfaces. To get more details please refer to Anvarbek or Abdulaziz");
        }
        if (colspan > 0) {
            field.setColspan(colspan);
        }
        field.index = index;
        fields.add(index, field);
        internalAddField(field);
        renderIndexes();
        return field;
    }

    public Field addField(String label, Widget[] widgets, boolean required) {
        return addField(fields.size(), label, widgets, required, 0, null, 0);
    }

    public Field addField(String label, Widget[] widgets, boolean required, int widgetSpacing) {
        return addField(fields.size(), label, widgets, required, widgetSpacing, null, 0);
    }

    public Field addField(String label, Widget[] widgets, boolean required, int widgetSpacing, String helpText, int helpTextScope) {
        return addField(fields.size(), label, widgets, required, widgetSpacing, helpText, helpTextScope);
    }

    public Field addField(String label, Widget[] widgets, boolean required, int widgetSpacing, String helpText) {
        return addField(fields.size(), label, widgets, required, widgetSpacing, helpText, 0);
    }

    private Field addField(int index, String label, Widget[] widgets,
                           boolean required, int widgetSpacing, String helpText, int helpTextScope) {
        Field field = new Field(label, widgets, widgetSpacing, required, helpText, helpTextScope);
        field.index = index;
        fields.add(index, field);
        internalAddField(field);
        return field;
    }

    public Field addImageField(Image image) {
        Field field = new Field("", image, false);
        field.index = fields.size();
        fields.add(field.index, field);
        container.add(field.getWidget());
        return field;
    }

    public Field addTitleField(String text) {
        return addTitleField(text, false);
    }

    public Field addTitleField(String text, boolean mandatory) {
        return addTitleField(text, null, mandatory);
    }

    public Field addTitleField(String text, Widget widget, boolean mandatory) {
        Field field = new Field(text, widget, mandatory);
        field.index = fields.size();
        String label = "<div class=customTitle>" + text + "</div>";
        if (mandatory) {
            label = "<div class=customTitle>" + text + "<span class=txt-elem--required>*</span></div>";
        }
        HorizontalPanel titlePanel = new HorizontalPanel();
        HTML titleLabel = new HTML(label);
        titlePanel.add(titleLabel);
        fields.add(field.index, field);
        if (widget != null) {
            titlePanel.add(widget);
        }
        container.add(titleLabel);
        return field;
    }

    public Field addHorizontalLine() {
        HTML line = new HTML("<div class=line></div>");
        Field field = new Field("", line, false);
        field.index = fields.size();
        fields.add(field.index, field);
        container.add(line);
        return field;
    }

    public Field addHorizontalLineForAppraisals() {
        HTML line = new HTML("<hr>");
        Field field = new Field("", line, false);
        field.index = fields.size();
        fields.add(field.index, field);
        container.add(line);
        return field;
    }

    private void localAddField(Field field) { //this method changed by Normurod
        if (!fields.contains(field)) {

            if (field.index < fields.size()) {
                Field existField = ((Field) fields.get(field.index));
                if (existField != null) {
                    for (int i = existField.index; i < fields.size(); i++) {
                        ((Field) fields.get(i)).index++;
                    }
                }
            } else {
                field.index = fields.size();
            }

//            flex.insertRow(field.index);
            fields.add(field.index, field);

            internalAddField(field);
        }
    }

    String labelSize = null;
    Integer labelAlignment = null;

    public void setLabelSize(String size) {
        labelSize = size;
    }

    public void setLabelAlignment(Integer labelAlignment) {
        this.labelAlignment = labelAlignment;
    }

    protected void internalAddField(Field field) {
        MaterialPanel formGroup = new MaterialPanel("form-group");
        Label fieldLabel;

        if (field.getLabelWidget() != null) {
            fieldLabel = field.getLabelWidget();
        } else if (field.label != null) {
            fieldLabel = new Label(field.label);
        } else {
            fieldLabel = new Label();
        }
        fieldLabel.addStyleName("form-group__label");

        if (field.label != null && field.label.length() > 50) {
            fieldLabel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
        }
        if (field.required) {
            fieldLabel.addStyleName("form-label--required");
        }

        //fieldLabel.setStyleName("paddingStyle");
        field.labelHtml = fieldLabel;
        field.hp = new HorizontalPanel();
        field.hp.setStyleName("form-group__content");

        field.formGroup = formGroup;
        container.add(formGroup);

        if (field.label != null) {
            formGroup.add(field.labelHtml);
        }

        if (field.widget != null) { // if only one widget is specified
            field.hp.add(field.widget);
            field.addMouseOutHandler(new WfmToolTipListener(wfmStrings.errorYourText(), 5000, "x-tip"));
            field.addMouseOverHandler(new WfmToolTipListener(wfmStrings.errorYourText(), 5000, "x-tip"));
            formGroup.add(field.hp);
        } else {
            for (int i = 1; i <= field.widgets.length; i++) {
                field.hp.add(field.widgets[i - 1]);
            }
            formGroup.add(field.hp);
        }
    }

    public void removeField(Field f) {
        container.remove(f.formGroup);
        fields.remove(f);
    }

    protected boolean firstAdd = true;

    public void addButton(Widget button) {
        addButton(button, 1);
    }

    public void addButton(Widget button, int column) {

        if (firstAdd) {
            container.add(buttons);
            firstAdd = false;
        }
        buttons.add(button);

        if (Utils.isDemoAccount()) {
            if (button instanceof WfmButton2) {
                WfmButton2 bu2 = (WfmButton2) button;
                bu2.setEnabled(false);
            }
        }
    }

    public void addButton(int index, Widget widget) {
        container.add(widget);
    }

    public void addOutButton(Widget widget) {
        outButtons.add(widget);
        if (Utils.isDemoAccount()) {
            if (widget instanceof Button) {
                WfmButton2 button = (WfmButton2) widget;
                button.setEnabled(false);
                button.getElement().setInnerText(wfmStrings.disabledForDemoAccount());
            }
        }
    }

    public void cleanupErrors() {
        for (Object field : fields) {
            Field f = (Field) field;
        }
    }

    public void setPoints(boolean points) {
        this.points = points;
    }

    public boolean getPoints() {
        return points;
    }

    public int getLabelPadding() {
        return labelPadding;
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
    }

    public int getWidgetPadding() {
        return widgetPadding;
    }

    public void setWidgetPadding(int widgetPadding) {
        this.widgetPadding = widgetPadding;
    }

    public void clearSelected() {
        for (Object field1 : fields) {
            Field field = (Field) field1;
            field.clearWidgets();
        }
    }

    public Field addWidget(Widget widget) {
        Field field = new Field("", widget, false);
        field.index = fields.size();
        fields.add(field.index, field);
        container.add(widget);
        return field;
    }

    public class Field extends FocusPanel {

        private String label;
        private Label labelWidget;
        protected Widget widget;
        protected Widget[] widgets;
        private Label labelHtml;
        private String contextualHelp;
        private boolean required;
        private int colspan;
        private int helpTextScope;
        private int widgetSpacing;
        private boolean visible = true;
        private HorizontalPanel hp;
        private MaterialPanel formGroup;

        private int index;

        public Field(String label, Widget widget, boolean required) {
            this(label, widget, required, null);
        }

        public Field(String label, Widget[] widgets, int widgetSpacing, boolean required) {
            this(label, null, required, null);
            this.widgets = widgets;
            this.widgetSpacing = widgetSpacing;
        }

        public Field(String label, Widget[] widgets, int widgetSpacing, boolean required, String helpText, int helpTextScope) {
            this(label, null, required, null);
            this.widgets = widgets;
            this.widgetSpacing = widgetSpacing;
            this.helpTextScope = helpTextScope;

            contextualHelp = helpText;
        }

        public Field(String label, Widget widget, boolean required, String toolTip) {
            this(label, widget, required, toolTip, null, 0);
        }

        public Field(String label, Widget widget, boolean required, String toolTip, String helpMessage, int helpTextScope) {
            this(label, widget, required, toolTip, helpMessage, helpTextScope, null);
        }

        public Field(String label, Widget widget, boolean required, String toolTip, String helpMessage, int helpTextScope, Label... labelWidget) {
            this.label = label;
            this.widget = widget;
            this.required = required;
            this.helpTextScope = helpTextScope;
            this.labelWidget = labelWidget != null ? labelWidget[0] : null;

            contextualHelp = helpMessage;
        }

        public void setErrorMessage(String errorMessage, String style, ArrayList widgetIndexes) {
            if (widget != null) {
                widget.removeStyleName("x-form-invalid");
            } else {
                for (int widgetIndex = 0; widgetIndex < 5; widgetIndex++) {
                    if (widgets != null && widgets.length > 0 && widgets[widgetIndex] != null && widgets.length > widgetIndex) {
                        widgets[widgetIndex].removeStyleName("x-form-invalid");
                    }
                }
            }
            if (errorMessage != null) {
                if (widget != null) {
                    widget.addStyleName("x-form-invalid");
                } else {
                    for (Object widgetIndexe : widgetIndexes) {
                        if (widgets != null && widgets.length > 0 && widgets[((Integer) widgetIndexe)] != null && widgets.length > ((Integer) widgetIndexe)) {
                            widgets[((Integer) widgetIndexe)].addStyleName("x-form-invalid");
                        }
                    }
                }
            }
        }

        public void setErrorMessage(String errorMessage, String style, int widgetIndex) {
            // if (errorMessage != null) {
            if (widget != null) {
                widget.removeStyleName("x-form-invalid");
            } else if (widgets != null && widgets.length > 0 && widgets[widgetIndex] != null) {
                widgets[widgetIndex].removeStyleName("x-form-invalid");
            }

            if (errorMessage != null) {
                if (widget != null) {
                    widget.addStyleName("x-form-invalid");
                } else if (widgets != null && widgets.length > 0 && widgets[widgetIndex] != null) {
                    widgets[widgetIndex].addStyleName("x-form-invalid");
                }
            }
        }

        public void setErrorMessage(String errorMessage, String style) {
            setErrorMessage(errorMessage, style, 0);
        }


        public void setName(String name) {
            DOM.setElementProperty(widget.getElement(), "name", COMMAND_NAME + "." + name);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setLabelText(String text) {
            labelHtml = new Label(text);
            labelHtml.setClass("form-group__label" + (required ? " form-label--required" : ""));
//            flex.setWidget(getIndex(), 0, labelHtml);
            container.add(labelHtml);
        }


        public String getContextualHelp() {
            return contextualHelp;
        }

        public void setContextualHelp(String contextualHelp) {
            this.contextualHelp = contextualHelp;
        }

        public void setVisible(boolean visible) {
            if (this.visible = visible) {
                localAddField(this);
            } else {
                if (fields.contains(this)) {
                    removeField(this);
                }
            }
        }

        public boolean isVisible() {
            return visible;
        }

        public Widget[] getWidgets() {
            return widgets;
        }

        public Widget getControl() {
            return widget;
        }

        public int getColspan() {
            return colspan;
        }

        public void setColspan(int colspan) {
            this.colspan = colspan;
        }

        private void clearWidget(Widget widget) {
            if (widget instanceof Clearable) {
                Clearable cl = (Clearable) widget;
                cl.clearSelected();
            } else if ((widget instanceof HasText) && !(widget instanceof HasHTML)) {
                HasText ht = (HasText) widget;
                ht.setText("");
            }
        }

        public void clearWidgets() {
            if (widgets != null) {
                for (Widget widget1 : widgets) {
                    clearWidget(widget1);
                }
            } else if (widget != null) {
                clearWidget(widget);
            }
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public Boolean getRequired() {
            return this.required;
        }

        public String getLabel() {
            return label;
        }

        public Label getLabelWidget() {
            return labelWidget;
        }

        public void setLabelWidget(Label labelWidget) {
            this.labelWidget = labelWidget;
        }
    }

    public void setCellSpacing(int spacing) {
        //flex.setCellSpacing(spacing);
    }

    public void setCellPadding(int padding) {
        //flex.setCellPadding(padding);
    }

    public FlowPanel getContainer() {
        return container;
    }

    public List getFields() {
        return fields;
    }

    protected void renderIndexes() {
        for (Object object : getFields()) {
            if (object instanceof Field) {
                Field field = (Field) object;
                field.setIndex(getFields().indexOf(object));
            }
        }
    }
}
