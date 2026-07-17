package com.finnetlimited.reportservice.core.client.ui.panel;

import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 14-Mar-2010
 * Time: 23:22:39
 */
public class DRSStepPanel extends HTMLPanel {

    private static final String _id = IdType.STEP_PANEL.getName();
    private static final String STYLE = "act";
    private static final String LINE = "-";
    private static int num = 0;
    private String id;
    private String oldSelectId;
    private HashMap<String, String> map = new HashMap<>();

    public DRSStepPanel() {
        super("<div class='plateBox steps' id='" + (_id + num) + "'></div>"
                + "<!--[if lte IE 8]><span class='bgAngle topLeft'></span>" +
                "<span class='bgAngle topRight'></span>" +
                "<span class='bgAngle bottomLeft'></span>" +
                "<span class='bgAngle bottomRight'>&nbsp;</span><![endif]-->"
        );
        id = _id + num;
        num++;
        sinkEvents(Event.ONCLICK);
    }

    private void addFirstStep(String name, String stepId, boolean selected) {
        HTMLPanel step = new HTMLPanel("<a class='first' id=" + stepId + ">" + name + "</a>");
        this.add(step, id);
    }

    private void addLastStep(String name, String stepId, boolean selected) {
        HTMLPanel step = new HTMLPanel(
                "<a class='last' id='" + stepId + "'>" + name + "</a>");
        this.add(step, id);

    }

    private void addStep(String name, String stepId, boolean selected) {
        HTMLPanel step = new HTMLPanel("<a id=" + stepId + ">" + name + "</a>");
        this.add(step, id);
    }

    public void addSteps(String[] stepNames, String[] historyNames, int selected) {
        if (stepNames != null && stepNames.length != 0) {
            if (selected != 0) {
                addFirstStep(stepNames[0], (id + LINE + historyNames[0]), false);
            } else {
                oldSelectId = (id + LINE + historyNames[0]);
                addFirstStep(stepNames[0], oldSelectId, true);
            }
            map.put(historyNames[0], (id + LINE + historyNames[0]));
            for (int i = 1; i < stepNames.length - 1; i++) {
                if (selected != i) {
                    addStep(stepNames[i], (id + LINE + historyNames[i]), false);
                } else {
                    oldSelectId = (id + LINE + historyNames[i]);
                    addStep(stepNames[i], oldSelectId, true);
                }
                map.put(historyNames[i], (id + LINE + historyNames[i]));
            }
            if (stepNames.length != 1) {
                if (selected != stepNames.length - 1) {
                    addLastStep(stepNames[stepNames.length - 1], (id + LINE + historyNames[stepNames.length - 1]), false);
                } else {
                    oldSelectId = (id + LINE + historyNames[stepNames.length - 1]);
                    addLastStep(stepNames[stepNames.length - 1], oldSelectId, true);
                }
                map.put(historyNames[stepNames.length - 1], (id + LINE + historyNames[stepNames.length - 1]));
            }
        }
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        com.google.gwt.dom.client.Element elem = com.google.gwt.dom.client.Element.as(event.getEventTarget());
        String att = elem.getAttribute("id");
        if (att != null) {
            String[] attValue = att.split(LINE);
            if (attValue != null && attValue.length == 2 && id.equals(attValue[0])) {
                com.google.gwt.dom.client.Element oldElem = DOM.getElementById(oldSelectId);
                if (!elem.equals(oldElem)) {
                    if (clickEvent != null) {
                        clickEvent.clickEvent(attValue[1]);
                    }
                    oldElem.getParentElement().removeClassName(STYLE);
                    elem.getParentElement().addClassName(STYLE);
                    oldSelectId = att;
                }
            }
        }
    }

    public void action(String historyName) {
        if (map.containsKey(historyName)) {
            com.google.gwt.user.client.Element oldElem = DOM.getElementById(oldSelectId);
            com.google.gwt.user.client.Element elem = DOM.getElementById(map.get(historyName));
            if (oldElem != null) {
                oldElem.getParentElement().removeClassName(STYLE);
            }
            elem.getParentElement().addClassName(STYLE);
            oldSelectId = map.get(historyName);
        }
    }

    /* add new stage */
    public void insertStep(String name, String historyName, int position) {
        if (!map.containsKey(historyName)) {
            map.put(historyName, (id + LINE + historyName));
            HTML newStep = new HTML("<a  id='" + (id + LINE + historyName) + "'>" + name + "</a>");
            Element elem = DOM.getElementById(id);
            DOM.insertChild(elem, newStep.getElement(), position);
        }
    }

    /* remove stage */
    public void removeStep(String historyName) {
        if (map.containsKey(historyName)) {
            map.remove(historyName);
            Element stepElem = DOM.getElementById(id + LINE + historyName);
            stepElem.getParentElement().removeFromParent();
        }
    }

    public boolean isGroupingAdd() {
        return map.containsKey(HistoryNamesType.AddGroupingReport.name());
    }

    public interface StepPanelClickEvent {
        void clickEvent(String histoyName);
    }

    private StepPanelClickEvent clickEvent;

    public void addClickEvent(StepPanelClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }
}
