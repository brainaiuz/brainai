package com.edatasite.workforce.gwt.core.client.ui.components.svgIcon;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SvgIcon extends Composite {

    interface SvgUiBinder extends UiBinder<HTMLPanel, SvgIcon> {
    }

    private static SvgUiBinder ourUiBinder = GWT.create(SvgUiBinder.class);
    @UiField
    Element useElement;
//    @UiField
//    UseTag use;

    private HTMLPanel main;

    @Deprecated
    @Override
    public void addStyleName(String style) {
        super.addStyleName(style);
    }

    public void addClassName(String style) {
        String currentClass = main.getElement().getAttribute("class");
        if (currentClass != null) {
            currentClass = currentClass.replaceAll(style + " ", " ");
            main.getElement().setAttribute("class", currentClass + " " + style);
        } else {
            main.getElement().setAttribute("class", style);
        }
    }

    public void removeClassName(String style) {
        String currentClass = main.getElement().getAttribute("class");
        if (currentClass != null && currentClass.indexOf(style) != -1) {
            currentClass = currentClass.replaceAll(style, "");
            main.getElement().setAttribute("class", currentClass.trim());
        }
    }

    public SvgIcon(SvgEnum icon, boolean defaultStyle) {
        main = ourUiBinder.createAndBindUi(this);
        initWidget(main);
        useElement.setAttribute("href", "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#" + icon.name());

        if (defaultStyle) {
            addClassName("icon--" + icon.name());
        }
    }

    // temp construct
    public SvgIcon(String iconClass) {
        main = ourUiBinder.createAndBindUi(this);
        initWidget(main);
        useElement.setAttribute("href", "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#" + iconClass);

        addClassName("icon--" + iconClass);
    }

    public SvgIcon(SvgEnum icon) {
        this(icon, true);

    }

}