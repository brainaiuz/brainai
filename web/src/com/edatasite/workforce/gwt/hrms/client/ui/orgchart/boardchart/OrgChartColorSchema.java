package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQuery;

public class OrgChartColorSchema extends Div {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    private static final String[] COLORS = new String[]{
            "#E3E3E3",
            "#D9F5FF",
            "#FFE3D0",
            "#DDFDD7",
            "#ECE6FF",
            "#FFF6CF",
            "#E5EAFF",
            "#FFE0F5",
            "#DDFDD7",
            "#D9F5FF"
    };

    private String color;
    private ColorButton selected;
    private Command changeHandler;
    private ColorSelectHandler selectHandler;
    private KpiSwitcher kpiSwitcher;

    public OrgChartColorSchema(boolean enableColorUsage) {
        // 1. Leave the main container empty of "color-widget" class
        // to avoid CSS grid conflicts with the switcher
        super();
        addStyleName("color-widget-wrapper");

        // 2. Create the internal list for buttons
        // This maintains the original DOM structure for buttons
        final Div colorPickList = new Div("color-widget colorPickList");
        if (!enableColorUsage) {
            // Если это НЕ панель со свитчером (т.е. попап), добавляем рамку инпута
            colorPickList.addStyleName("form-control");
        }

        for (String color : COLORS) {
            colorPickList.add(new ColorButton(color));
        }

        // 3. Set default color safely
        if (COLORS.length > 0) {
            this.color = COLORS[0];
        }

        // 4. Add the buttons list to our main container
        add(colorPickList);

        // 5. Initialize the switcher if needed
        if (enableColorUsage) {
            Span onLabelWrapp = new Span();
            kpiSwitcher = new KpiSwitcher(hrmsStrings.allowColorChanges(), null, false);
            kpiSwitcher.getOnLabel().setClass("switch__label--right");
            kpiSwitcher.setMarginTop(20);
            kpiSwitcher.getOnLabel().clear();
            kpiSwitcher.getOnLabel().add(onLabelWrapp);

            // Add switcher below the buttons list
            Div footer = new Div("color-widget-footer");
            footer.add(kpiSwitcher);
            add(footer);
        }

        // 6. Attach events to the INTERNAL list, not the main container
        JQuery.$(colorPickList.getElement()).on("colorButtonSelected", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object arg) {
                if (arg instanceof ColorButton) {
                    ColorButton sourceButton = (ColorButton) arg;

                    // Logic for selection
                    if (sourceButton.equals(selected)) {
                        sourceButton.setActive(!sourceButton.isActive());
                    } else {
                        if (selected != null) {
                            selected.setActive(false);
                        }
                        sourceButton.setActive(true);
                    }

                    selected = sourceButton;
                    OrgChartColorSchema.this.color = sourceButton.getColor();

                    if (changeHandler != null) {
                        changeHandler.execute();
                    }

                    if (selectHandler != null) {
                        boolean isChecked = (kpiSwitcher != null) && kpiSwitcher.getValue();
                        selectHandler.onSelect(OrgChartColorSchema.this.color, isChecked);
                    }
                }
                return null;
            }
        });
    }

    public OrgChartColorSchema(String color) {
        this(false);
        setColor(color);
    }

    public void setColor(String color) {
        if (selected != null) {
            selected.setActive(false);
        }
        if (color != null) {
            color = "#" + color.replace("#", "");
        } else {
            selected = null;
            return;
        }
        boolean flag = false;
        this.color = color;
        for (String col : COLORS) {
            if (col.equalsIgnoreCase(color)) {
                for (Widget button : getChildrenList()) {
                    ColorButton btn = (ColorButton) button;
                    if (btn.getColor().equalsIgnoreCase(col)) {
                        btn.setActive(true);
                        selected = btn;
                        flag = true;
                    }
                }
            }
        }
        if (!flag) {
            remove(getChildrenList().size() - 1);
            COLORS[COLORS.length - 1] = color;
            ColorButton button = new ColorButton(color);
            add(button);
            button.setActive(true);
            selected = button;
        }
    }

    public String getColor() {
        if (selected != null) {
            return selected.getColor();
        }
        return color;
    }

    private class ColorButton extends Div {
        private final String color;
        private final Div c2;

        public ColorButton(String color) {
            super("color-widget__button");
            this.color = color;
            Div c1 = new Div("color-widget__color-1");
            c1.getElement().getStyle().setBackgroundColor(color);
            c2 = new Div("color-widget__color-2");
            c2.getElement().getStyle().setBackgroundColor(color);

            // Hide the second circle to match new design without breaking logic
            c2.setVisible(false);

            add(c1);
            add(c2);
            addClickHandler(event -> {
                JQuery.$(ColorButton.this.getElement()).trigger("colorButtonSelected", ColorButton.this);
            });
        }

        private void setActive(boolean active) {
            if (active) {
                addStyleName("color-widget__button--selected");
            } else {
                removeStyleName("color-widget__button--selected");
            }
        }

        public boolean isActive() {
            return ColorButton.this.getStyleName().contains("color-widget__button--selected");
        }

        public String getColor() {
            return color;
        }
    }

    public void setChangeHandler(Command changeHandler) {
        this.changeHandler = changeHandler;
    }

    public void setSelectHandler(ColorSelectHandler selectHandler) {
        this.selectHandler = selectHandler;
    }

    public interface ColorSelectHandler {
        void onSelect(String color, boolean changeSubDepColor);
    }
}
