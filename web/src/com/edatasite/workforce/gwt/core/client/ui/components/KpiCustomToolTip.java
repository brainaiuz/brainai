package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

public class KpiCustomToolTip extends Composite {

    private Span wrapper;
    private MaterialDropDown popUp;
    private String wrapperClassLeft = "dropdown-kit--arrow--left";
    private String wrapperClassRight = "dropdown-kit--arrow--right";
    private boolean isRight;
    private boolean isInverted;

    public KpiCustomToolTip(String message, boolean isRight, boolean isInverted) {
        this.isRight = isRight;
        this.isInverted = isInverted;
        init(message);
    }

    public KpiCustomToolTip(String message, boolean right) {
        this.isRight = right;
        init(message);
    }

    public KpiCustomToolTip(String message) {
        init(message);
    }

    private void init(String message) {
        wrapper = new Span();
        if(isRight) {
            wrapper.setStyleName(wrapperClassRight);
        } else {
            wrapper.setStyleName(wrapperClassLeft);
        }

        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink infoLink = new MaterialLink();
        infoLink.setStyleName("infoDropDown dropdown-button");
        if(isInverted) {
            infoLink.addStyleName("dropdown-button--inverted");
        }
        infoLink.add(iInfo);

        popUp = new MaterialDropDown(infoLink);
        popUp.addStyleName("dropdown-content dropdown-content-tooltip");
        popUp.getElement().setInnerHTML(message);
        popUp.setHover(true);

        wrapper.add(infoLink);
        wrapper.add(popUp);

//        setTooltipClass();
//        Window.addResizeHandler(e -> {
//            setTooltipClass();
//        });
        initWidget(wrapper);
    }

    public void setMessage(String message) {
        popUp.getElement().setInnerHTML(message);
    }

    private void setTooltipClass() {
        int frameWidth = JQuery.$(".frame__content__body.scroll-content").outerWidth();

        if (frameWidth < 960) {
            wrapper.setStyleName("dropdown-kit--arrow--right");
        } else {
            wrapper.setStyleName("dropdown-kit--arrow--left");
        }
    }
}
