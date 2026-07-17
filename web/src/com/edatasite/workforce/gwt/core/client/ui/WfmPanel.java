package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.clickablePanel.WfmClickListener;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 16.01.2009
 * Time: 19:38:10
 * To change this template use File | Settings | File Templates.
 */
public class WfmPanel extends Widget {

    private Widget widget;

    private Element b;
    private Element b1;
    private Element b2;
    private Element b3;
    private Element b4;
    private Element b5;
    private Element div;

    private int style;

    private String main;
    private String mainfg;

    private WfmClickListener clickListener;

    public WfmPanel() {
        this(Styles.BLUE);
    }

    public WfmPanel(int style) {
        this.style = style;
        init();
    }

    protected void onAttach() {
        if (widget != null) {
            WidgetHelper.doAttach(widget);
        }
    }

    protected void onDetach() {
        if (widget != null) {
            WidgetHelper.doDetach(widget);
        }
    }

    public void onBrowserEvent(Event event) {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    private void init() {
        initStyles();
        setElement(DOM.createDiv());

        div = DOM.createDiv();
        div.addClassName(mainfg);
        DOM.appendChild(getElement(), drawFirstPart());
        DOM.appendChild(getElement(), div);
        DOM.appendChild(getElement(), drawSecondPart());
    }

    private void initStyles() {
        switch (style) {
            case Styles.RED:
                main = "main-red";
                mainfg = "mainfg-red";
                break;
            case Styles.YELLOW:
                main = "main-yellow";
                mainfg = "mainfg-yellow";
                break;
            case Styles.BLUE:
                main = "main-blue";
                mainfg = "mainfg-blue";
                break;
            case Styles.GREEN:
                main = "main-green";
                mainfg = "mainfg-green";
                break;
            case Styles.WHITE:
                main = "main-white";
                mainfg = "mainfg-white";
                break;
            case Styles.GRAY:
                main = "main-gray";
                mainfg = "mainfg-gray";
                break;
            case Styles.LAVENDER:
                main = "main-lavender";
                mainfg = "mainfg-lavender";
                break;
            case Styles.PINK:
                main = "main-pink";
                mainfg = "mainfg-pink";
                break;
            default:
                main = "main";
                mainfg = "mainfg";
                break;
        }
    }

    /**
     * If the widget has already been attached it removes that widget and put there new widget.
     *
     * @param widget
     */
    public void setWidget(Widget widget) {
        this.widget = widget;
        DOM.insertChild(div, widget.getElement(), 0);
    }

    public void setWidget(Widget widget, HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        setWidget(widget);
        DOM.setElementProperty(div, "align", align.getTextAlignString());
    }

    public void setText(String text) {
        DOM.setInnerText(div, text);
    }

    public void setText(String text, HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        setText(text);
        DOM.setElementProperty(div, "align", align.getTextAlignString());
    }

    public void setHTML(String html) {
        DOM.setInnerHTML(div, html);
    }

    public void setHTML(String html, HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        setHTML(html);
        DOM.setElementProperty(div, "align", align.getTextAlignString());
    }

    private void initialize() {
        b = DOM.createElement("b");
        b.addClassName(main);

        b1 = DOM.createElement("b");
        b1.addClassName("main1");

        b2 = DOM.createElement("b");
        b2.addClassName("main2");

        b3 = DOM.createElement("b");
        b3.addClassName("main3");

        b4 = DOM.createElement("b");
        b4.addClassName("main4");

        b5 = DOM.createElement("b");
        b5.addClassName("main5");

        DOM.appendChild(b1, DOM.createElement("b"));
        DOM.appendChild(b2, DOM.createElement("b"));
    }

    private Element drawFirstPart() {
        initialize();

        DOM.appendChild(b, b1);
        DOM.appendChild(b, b2);
        DOM.appendChild(b, b3);
        DOM.appendChild(b, b4);
        DOM.appendChild(b, b5);

        return b;
    }

    private Element drawSecondPart() {
        initialize();

        DOM.appendChild(b, b5);
        DOM.appendChild(b, b4);
        DOM.appendChild(b, b3);
        DOM.appendChild(b, b2);
        DOM.appendChild(b, b1);

        return b;
    }

    public void addClickListener(WfmClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class Styles {
        public static final int DEFAULT = 0;
        public static final int RED = 1;
        public static final int YELLOW = 2;
        public static final int BLUE = 3;
        public static final int GREEN = 4;
        public static final int WHITE = 5;
        public static final int GRAY = 6;
        public static final int LAVENDER = 7;
        public static final int PINK = 8;
    }
}
