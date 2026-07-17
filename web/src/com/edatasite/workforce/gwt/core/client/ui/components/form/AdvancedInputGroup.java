package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.LabelBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

public class AdvancedInputGroup extends Div {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Command command;

    public AdvancedInputGroup() {
        super("input-group");
    }

    public AdvancedInputGroup(Widget content) {
        this(null, content, null, true, true);
    }

    public AdvancedInputGroup(Widget content, Widget append) {
        this(null, content, append, true, true);
    }

    public AdvancedInputGroup(Widget prepend, Widget content, Widget append, boolean prependWithContent, boolean appendWithContent) {
        this();

        if (prepend != null) {
            add(new InputGroupPrepend(prepend, prependWithContent));
        }
        if (content != null) {
            if (content instanceof LabelBase) {
                Div a = new Div("input-group-content");
                a.add(content);
                add(a);
            } else if (content instanceof InputGroup) {
                int count = ((InputGroup) content).getWidgetCount();
                for (int i = 0; i < count; i++) {
                    add(((InputGroup) content).getWidget(0));
                }
            } else {
                add(content);
            }
        }
        if (append != null) {
            add(new InputGroupAppend(append, appendWithContent));
        }
    }

    public void setAppender(String iconClass) {
        MaterialLink link = new MaterialLink();
        Icon icon = new Icon();
        icon.addStyleName(iconClass);

        link.add(icon);
        link.addClickHandler(event -> {
            if (command != null) {
                command.execute();
            }
        });

        if (WfmButton2.ICON_PLUS.equals(iconClass)) {
            new KpiToolTip(link, wfmStrings.add());

            add(new InputGroupAppend(link, true));
        } else {
            add(new InputGroupAppend(link, true));
        }
    }

    public void appenderClickHandler(Command command) {
        this.command = command;
    }
}
