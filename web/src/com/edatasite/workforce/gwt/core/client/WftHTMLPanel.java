package com.edatasite.workforce.gwt.core.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WftHTMLPanel {

    private String controlPattern = "\\$\\$\\w+(?::(?:[A-Za-z]|_[A-Za-z])\\w*|)\\$\\$";
    private String defaultValuePattern = "\\[\\[(.*?)\\]\\]";
    private Map<String, Widget> widgetMap;
    private Map<String, String> valueMap = new HashMap<>();
    private List<String> remaining = new ArrayList<>();
    private HTMLPanel container;

    public WftHTMLPanel(String layoutHTML, Map<String, Widget> widgetMap) {
        this.widgetMap = widgetMap;
        container = new HTMLPanel(replaceControls(layoutHTML));
        mergeWidgets();
    }

    public WftHTMLPanel(String layoutHTML, Map<String, Widget> widgetMap, boolean forCertificateTemplate) {
        this.widgetMap = widgetMap;
        if (forCertificateTemplate) {
            container = new HTMLPanel(replaceControlsWithValues(layoutHTML));
        } else {
            container = new HTMLPanel(replaceControls(layoutHTML));
        }
        mergeWidgets();
    }

    private String replaceControls(String layoutHTML) {
        RegExp regExp = RegExp.compile(controlPattern);
        for (MatchResult matcher = regExp.exec(layoutHTML); matcher != null; matcher = regExp.exec(layoutHTML)) {
            String group, temp;
            group = temp = matcher.getGroup(0);
            if (temp.startsWith("$$")) {
                temp = temp.substring(2, temp.length() - 2);
                if (temp.contains(":")) {
                    String[] controlTokens = temp.split(":");
                    switch (controlTokens[0]) {
                        case "label":
                            remaining.add("label" + controlTokens[1]);
                            layoutHTML = layoutHTML.replace(group, "<label id=\"label" + controlTokens[1] + "\"></label>");
                            break;
                        case "input":
                            remaining.add("input" + controlTokens[1]);
                            layoutHTML = layoutHTML.replace(group, "<div id=\"input" + controlTokens[1] + "\" ></div>");
                            break;
                        default:
                            remaining.add("span" + controlTokens[1]);
                            layoutHTML = layoutHTML.replace(group, "<span id=\"validation" + controlTokens[1] + "\"></span>");
                            break;
                    }
                }
            }
        }
        return layoutHTML;
    }

    private String replaceControlsWithValues(String layoutHTML) {
        RegExp regExp = RegExp.compile(controlPattern);
        for (MatchResult matcher = regExp.exec(layoutHTML); matcher != null; matcher = regExp.exec(layoutHTML)) {
            String group, temp;
            group = temp = matcher.getGroup(0);
            if (temp.startsWith("$$")) {
                temp = temp.substring(2, temp.length() - 2);
                if (temp.contains(":")) {
                    String[] controlTokens = temp.split(":");
                    switch (controlTokens[0]) {
                        case "label":
                            layoutHTML = layoutHTML.replace(group, "<label id=\"label" + controlTokens[1] + "\"></label>");
                            break;
                        case "input":
                            RegExp regExpValue = RegExp.compile("\\$\\$" + controlTokens[0] + ":" + controlTokens[1] + "\\$\\$" + defaultValuePattern);
                            MatchResult matcherValue = regExpValue.exec(layoutHTML);
                            if (matcherValue != null && matcherValue.getGroupCount() >= 2) {
                                String value = matcherValue.getGroup(1);
                                valueMap.put("input" + controlTokens[1], value);
                                layoutHTML = layoutHTML.replace("[[" + value + "]]", "");
                            }
                            layoutHTML = layoutHTML.replace(group, "<div id=\"input" + controlTokens[1] + "\" ></div>");
                            break;
                        default:
                            layoutHTML = layoutHTML.replace(group, "<span id=\"validation" + controlTokens[1] + "\"></span>");
                            break;
                    }
                }
            }
        }
        return layoutHTML;
    }

    public void mergeWidgets() {
        if (widgetMap == null || widgetMap.isEmpty()) {
            return;
        }
        for (String key : widgetMap.keySet()) {
            if (container.getElementById(key) != null && widgetMap.get(key) != null) {
                container.addAndReplaceElement(widgetMap.get(key), key);
                remaining.remove(key);
            }
        }
        for (String id : remaining) {
            Element toRemove = container.getElementById(id);
            if (toRemove != null) {
                toRemove.removeFromParent();
            }
        }
    }

    public HTMLPanel getContainer() {
        return container;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }
}
