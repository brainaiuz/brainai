package com.edatasite.workforce.gwt.core.client.ui.viewFooter;

import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Span;

public class FooterInformer extends FigureWidget {
    private Div iconContainer;
    private Span badge;
    private FigCaption caption;

    public FooterInformer(SvgEnum iconEnum, String captionText) {
        createAndAppendIcon(iconEnum);
        createAndAppendCaption(captionText);
    }

    public FooterInformer(SvgEnum iconEnum, String captionText, Widget widget) {
        createAndAppendIcon(iconEnum);
        createAndAppendCaption(captionText);
        appendWidgetToPopup(widget);
    }

    public void createAndAppendIcon(SvgEnum iconEnum) {
        iconContainer = new Div("informer-item__icon");
        SvgIcon icon = new SvgIcon(iconEnum);
        iconContainer.add(icon);
        add(iconContainer);
        initAndAppendBadge();
    }

    public void initAndAppendBadge() {
        badge = new Span();
        badge.addStyleName("badge");
        badge.setVisible(false);
        iconContainer.add(badge);
    }

    public void setBadgeCount(Integer amount) {
        if (amount == null || amount == 0) {
            badge.setText("");
            badge.setVisible(false);
            return;
        }
        badge.setText(String.valueOf(amount));
        badge.setVisible(true);
    }

    public void createAndAppendCaption(String captionText) {
        caption = new FigCaption(captionText);
        add(caption);
    }

    private void appendWidgetToPopup(Widget widget) {
        if (widget == null) {
            return;
        }
        MaterialDropDown dropDown = new MaterialDropDown(this);
        dropDown.add(widget);
        add(dropDown);
    }

    public Div getIconContainer() {
        return iconContainer;
    }

    public Span getBadge() {
        return badge;
    }

    public void setCaptionText(String captionText) {
        this.caption.setText(captionText);
    }
}
