package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.Utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class WestPanelHelp extends Widget {

    /*public interface SearchImages extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/workspace/client/bundles/myworkspacehomeimage/search-button.png")
        ImageResource searchButton();
    }*/


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    //    public static final SearchImages searchImages = (SearchImages) GWT.create(SearchImages.class);
    private String helpCenterSearchUrl;
    private List<Element> liList;
    private Element ul;

    public WestPanelHelp(String header) {
        liList = new ArrayList<>();

        Element div = DOM.createDiv();
        Element h2 = DOM.createElement("h2");
        ul = DOM.createElement("ul");
        DOM.setElementProperty(div, "className", "halp-block-1");
        DOM.insertChild(div, h2, 1);
        DOM.insertChild(div, ul, 2);
        DOM.setInnerHTML(h2, header + "<span></span>");

        setElement(div);
    }

    public WestPanelHelp() {
        liList = new ArrayList<>();
        Element div = DOM.createDiv();
        Element h2 = DOM.createElement("h2");
        h2.getStyle().setPaddingTop(5, Style.Unit.PX);
        ul = DOM.createElement("ul");
        DOM.setElementProperty(div, "className", "halp-block-1");
        DOM.insertChild(div, h2, 1);
        DOM.insertChild(div, ul, 2);
        DOM.setInnerHTML(h2, "<span></span>");

        setElement(div);
    }

    public void addMenuItem(String text) {

        Element li = DOM.createElement("li");
        Element p = DOM.createElement("p");

        DOM.insertChild(ul, li, liList.size());
        DOM.insertChild(li, p, DOM.getChildCount(li));

        DOM.setInnerText(p, text);
        DOM.setElementProperty(li, "className", "last");
        if (liList.size() != 0) {
            liList.get(liList.size() - 1).removeClassName("last");
        }
        liList.add(li);
    }

    public void addSearchBox() {
        Element li = DOM.createElement("li");
        DOM.insertChild(ul, li, liList.size());

        final Element inputBox = DOM.createInputText();
        DOM.setStyleAttribute(inputBox, "width", "111px");
        DOM.setStyleAttribute(inputBox, "height", "20px");
        DOM.setStyleAttribute(inputBox, "color", "darkgray");
        DOM.setElementAttribute(inputBox, "value", wfmStrings.search());

        DOM.sinkEvents(inputBox, Event.ONBLUR | Event.ONFOCUS | Event.ONCHANGE | Event.ONKEYPRESS);
        DOM.setEventListener(inputBox, event -> {
            if (event.getTypeInt() == Event.ONBLUR) {
                if (inputBox.getPropertyString("value") == null || "".equals(inputBox.getPropertyString("value"))) {
                    DOM.setStyleAttribute(inputBox, "color", "darkgray");
                    DOM.setElementAttribute(inputBox, "value", wfmStrings.search());
                }
            } else if (event.getTypeInt() == Event.ONFOCUS) {
                if (wfmStrings.search().equals(inputBox.getPropertyString("value"))) {
                    DOM.setStyleAttribute(inputBox, "color", "black");
                    DOM.setElementAttribute(inputBox, "value", "");
                }
            } else if (event.getTypeInt() == Event.ONCHANGE) {
                helpCenterSearchUrl = "http://www."+ Utils.getHelpHost() + "/search/node/" + inputBox.getPropertyString("value") + "";
            } else if (event.getTypeInt() == Event.ONKEYPRESS) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    if (helpCenterSearchUrl != null && inputBox.getPropertyString("value") != null &&
                            !"".equals(inputBox.getPropertyString("value")) && !wfmStrings.search().equals(inputBox.getPropertyString("value"))) {
                        Window.open(helpCenterSearchUrl, "_blank", Constants.commonParamForUrl);
                    }
                }
            }
        });

        Element searchImage = DOM.createImg();
        DOM.setStyleAttribute(searchImage, "width", "34px");
        DOM.setStyleAttribute(searchImage, "height", "21px");
        DOM.setElementProperty(searchImage, "className", "pointer");
        /*DOM.setImgSrc(searchImage, searchImages.searchButton().getURL());*/
        DOM.sinkEvents(searchImage, Event.ONCLICK);
        DOM.setEventListener(searchImage, event -> {
            if (helpCenterSearchUrl != null && inputBox.getPropertyString("value") != null &&
                    !"".equals(inputBox.getPropertyString("value")) && !wfmStrings.search().equals(inputBox.getPropertyString("value"))) {
                Window.open(helpCenterSearchUrl, "_blank", Constants.commonParamForUrl);
            }
        });

        Element table = DOM.createTable();
        DOM.setElementProperty(table, "align", "center");
        Element tBody = DOM.createTBody();
        Element tr = DOM.createTR();

        Element tdInput = DOM.createTD();
        DOM.appendChild(tdInput, inputBox);
        DOM.appendChild(tr, tdInput);

        Element tdSearch = DOM.createTD();
        DOM.appendChild(tdSearch, searchImage);
        DOM.appendChild(tr, tdSearch);

        DOM.appendChild(tBody, tr);
        DOM.appendChild(table, tBody);

        DOM.insertChild(li, table, DOM.getChildCount(li));
        DOM.setElementProperty(li, "className", "last");
        if (liList.size() != 0) {
            liList.get(liList.size() - 1).removeClassName("last");
        }
        liList.add(li);
    }

    public void addMenuItemLink(String innerText, final String innerURL) {
        Element li = DOM.createElement("li");
        li.getStyle().setBorderStyle(Style.BorderStyle.NONE);
        li.getStyle().setPaddingTop(3, Style.Unit.PX);
        li.getStyle().setPaddingBottom(3, Style.Unit.PX);

        DOM.insertChild(ul, li, liList.size());

        Element link = DOM.createAnchor();
        DOM.setElementAttribute(link, "href", "javascript:;");
        DOM.setStyleAttribute(link, "color", "#2f4f6f");
        DOM.setInnerHTML(link, innerText);
        DOM.sinkEvents(link, Event.ONCLICK);
        DOM.setEventListener(link, event -> Window.open(innerURL, "_blank", Constants.commonParamForUrl));

        DOM.insertChild(li, link, DOM.getChildCount(li));
        DOM.setElementProperty(li, "className", "last");
        if (liList.size() != 0) {
            liList.get(liList.size() - 1).removeClassName("last");
        }
        liList.add(li);
    }

    public void addNewLine(String text) {
        Element li = DOM.getChildCount(ul) == 0 ? null : DOM.getChild(ul, DOM.getChildCount(ul) - 1);
        Element p;
        if (li == null) {
            p = DOM.createElement("p");
            li = DOM.createElement("li");
            DOM.insertChild(ul, li, liList.size());
            DOM.insertChild(li, p, 1);
        } else {
            p = DOM.getChild(li, DOM.getChildCount(li) - 1);
        }
        String html = DOM.getInnerHTML(p);
        html = html + text;
        DOM.setInnerHTML(p, html);
    }

    public void addHtmlLine(HTML html) {
        Element li = DOM.createElement("li");
//		Element p = DOM.createElement("p");

        DOM.insertChild(ul, li, liList.size());
//		DOM.insertChild(li, p, DOM.getChildCount(li));

        DOM.setInnerHTML(li, html.getHTML());
        li.getStyle().setPaddingTop(20d, Style.Unit.PX);
//		MyDOM.setStyleName(li, "last");
//		if(liList.size() != 0){
//		MyDOM.removeStyleName((Element)liList.get(liList.size()-1), "last");
//		}
        liList.add(li);
    }

    public void addMenuItem(Integer number, String header, String text) {

        Element li = DOM.createElement("li");
        Element strong = DOM.createElement("strong");
        Element p = DOM.createElement("p");

        DOM.insertChild(ul, li, liList.size());
        DOM.insertChild(li, strong, 1);
        DOM.insertChild(li, p, 2);

        DOM.setInnerText(strong, number + ". " + header + "-");
        DOM.setInnerText(p, text);
        DOM.setElementProperty(li, "className", "last");
        if (liList.size() != 0) {
            liList.get(liList.size() - 1).removeClassName("last");
//            MyDOM.removeStyleName(, "last");
        }
        liList.add(li);
    }

    public void setElement(Element elem) {
        super.setElement(elem);
    }

}
