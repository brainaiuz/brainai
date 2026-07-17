package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQuery;

public class ColorWidget extends Div {
    private static final String[] colors = new String[]{
            "#2C74DB",
            "#F1AB37",
            "#337BE2",
            "#99CA3A",
            "#855099",
            "#E73532",
            "#59B378",
            "#2E649E",
            "#49943D",
            "#6A4AA9",
            "#47A7E6",
            "#65B763",
            "#ACDAED",
            "#CC0000",
            "#000000",
            "#AEDACD",
            "#D40CA2",
            "#AA80C6",
            "#22DA93",
            "#D6955F",
            "#1F90B3",
            "#5F5F5F",
            "#BDBDBD",
            "#49BD06",
            "#CA6957",
            "#D8AC73",
            "#7BDDAF",
            "#6963B7",
            "#EDACEA",
            "#CCA700",
    };
    private String color; // color parameter required to keep previous user choices. Remove when you feel it is good to go
    private ColorButton selected;
    private Command changeHandler;

    public ColorWidget() {
        super("color-widget");
        for (String color : colors) {
            add(new ColorButton(color));
        }
        JQuery.$(ColorWidget.this.getElement()).on("colorButtonSelected", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object arg) {
                if (arg instanceof ColorButton) {

                    ColorButton sourceButton = (ColorButton) arg;
                    if (arg.equals(selected)) {
                        sourceButton.setActive(!sourceButton.isActive());
                    } else {
                        if (selected != null) {
                            selected.setActive(false);
                        }
                        sourceButton.setActive(true);
                    }
                    selected = sourceButton;
                    color = sourceButton.getColor();
                    if (changeHandler != null) {
                        changeHandler.execute();
                    }
                }
                return null;
            }
        });
        setColor(colors[0]);
    }

    public ColorWidget(String color) {
        this();
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
        for (String col : colors) {
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
            colors[colors.length - 1] = color;
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
}
