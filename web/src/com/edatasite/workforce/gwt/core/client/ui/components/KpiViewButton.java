package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;

public class KpiViewButton extends FlowPanel {

    private final FlowPanel icon = new FlowPanel();
    private final InlineLabel text = new InlineLabel();
    private final FlowPanel caret = new FlowPanel();

    public KpiViewButton(String label, SvgEnum leftIcon, SvgEnum caretIcon) {
        setStyleName("btn viewButton");

        icon.setStyleName("viewButton__icon");
        if (leftIcon != null) {
            icon.add(new SvgIcon(leftIcon));
        }

        text.setStyleName("viewButton__text");
        text.setText(label);

        caret.setStyleName("viewButton__caret");
        caret.add(new SvgIcon(caretIcon != null ? caretIcon : SvgEnum.chevronDown));

        add(icon);
        add(text);
        add(caret);
    }

    // удобный конструктор: без левой иконки
    public KpiViewButton(String label, SvgEnum caretIcon) {
        this(label, null, caretIcon);
    }

    public void setTextMinWidthByChars(int chars) {
        // min-width: Nch
        getElement().getStyle().setProperty("minWidth", chars + "ch");
    }

    public void setTextMinWidthByLongest(String... labels) {
        int max = 0;
        if (labels != null) {
            for (String s : labels) {
                if (s != null && s.length() > max) max = s.length();
            }
        }
        if (max > 0) setTextMinWidthByChars(max);
    }

    public void setText(String label) {
        text.setText(label);
    }

    public void setActive(boolean active) {
        if (active) addStyleName("active");
        else removeStyleName("active");
    }

    public boolean isActive() {
        return getStyleName().contains("active");
    }

    public void setLeftIcon(SvgEnum leftIcon) {
        icon.clear();
        if (leftIcon != null) {
            icon.add(new SvgIcon(leftIcon));
        }
    }
}
