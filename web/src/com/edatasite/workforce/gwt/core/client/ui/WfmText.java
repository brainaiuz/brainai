package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class WfmText extends Widget {

    private Element div;

    public WfmText() {
        init();
    }

    public WfmText(String title) {
        this();
        addBlueStyleTitle(title);
    }

    private void init() {
        div = DOM.createDiv();
        setElement(div);
    }

    /**
     * Shows blue background. Function uses "east-panel" style.
     */
    public void setBlueBackground(boolean show) {
        if (show) {
            setDivStyle("east-panel");
        } else {
            removeStyleName(div, "east-panel");
        }
    }

    /**
     * Adds a title to the text with a style "east-panelStyle" to the element<br>
     *
     * @param title
     */
    public void addBlueStyleTitle(String title) {
        addTitle(title, "east-panel-title");
    }

    /**
     * Adds a title to the text without any styles
     *
     * @param title
     */
    public void addSimpleTitle(String title) {
        addTitle(title, null);
    }

    /**
     * Adds a text which supports HTML format.
     *
     * @param text
     */
    public void addTextArea(String text) {
        addTextArea(text, "east-panelStyle");
    }

    /**
     * Adds a text with numeric lines.
     *
     * @param text
     */
    public void addNumericTextArea(String text) {
        addNumericTextArea(text, "east-panelStyle");
    }

    /**
     * Adds a text with dash lines.
     *
     * @param text
     */
    public void addDashTextArea(String text) {
        addDashTextArea(text, "east-panelStyle");
    }

    private void setDivStyle(String divStyle) {
        if (divStyle != null) {
            div.addClassName(divStyle);

        }
    }

    private void removeStyleName(Element elem, String style) {
        if (elem.getClassName() != null && elem.getClassName().equals(style)) {
            elem.removeClassName(style);
        }
    }

    private void addTitle(String title, String elementStyle) {
        Element el = DOM.createElement("h2");
        if (elementStyle != null) {
            el.addClassName(elementStyle);
        }
        DOM.setInnerHTML(el, title);
        DOM.insertChild(div, el, 0);
    }

    private void addTextArea(String text, String elementStyle) {
        /*Element p = DOM.getChildCount(div) == 0 ? null : DOM.getChild(div, DOM.getChildCount(div) - 1);
          if(p == null){
              p = DOM.createElement("p");
              DOM.insertChild(div, p, 0);
          }*/
        StringBuilder html = new StringBuilder();
        Element p = DOM.createElement("p");
        p.addClassName(elementStyle);
        DOM.insertChild(div, p, DOM.getChildCount(div));
        for (int i = 0; i < text.length(); i++) {
            if (i > 5 && text.charAt(i) == '>' && text.charAt(i - 1) == 'r' && text.charAt(i - 2) == 'b' && text.charAt(i - 3) == '<') {
                html.append(text.charAt(i) + "<br/>");
            } else {
                html.append(text.charAt(i));
            }
        }
        DOM.setInnerHTML(p, html.toString());
    }

    private void addNumericTextArea(String text, String elementStyle) {
        int lines = 0;
        StringBuilder html = new StringBuilder();
        Element p = DOM.createElement("p");
        p.addClassName(elementStyle);
        DOM.insertChild(div, p, DOM.getChildCount(div));
        for (int i = 0; i < text.length(); i++) {
            if (i > 5 && text.charAt(i) == '>' && text.charAt(i - 1) == 'r' && text.charAt(i - 2) == 'b' && text.charAt(i - 3) == '<') {
                lines++;
                html.append(text.charAt(i) + "<br/>&nbsp;&nbsp;&nbsp;" + lines + ".&nbsp;&nbsp;");
            } else {
                html.append(text.charAt(i));
            }
        }
        DOM.setInnerHTML(p, html.toString());
    }


    private void addDashTextArea(String text, String elementStyle) {
        StringBuilder html = new StringBuilder();
        Element p = DOM.createElement("p");
        p.addClassName(elementStyle);
        DOM.insertChild(div, p, DOM.getChildCount(div));
        for (int i = 0; i < text.length(); i++) {
            if (i > 5 && text.charAt(i) == '>' && text.charAt(i - 1) == 'r' && text.charAt(i - 2) == 'b' && text.charAt(i - 3) == '<') {
                html.append(text.charAt(i) + "<br/>&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;");
            } else {
                html.append(text.charAt(i));
            }
        }
        DOM.setInnerHTML(p, html.toString());
    }
}
