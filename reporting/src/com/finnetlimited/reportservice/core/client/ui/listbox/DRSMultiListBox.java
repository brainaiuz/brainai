package com.finnetlimited.reportservice.core.client.ui.listbox;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDirectoryPathRpc;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 13:42:39
 */
public class DRSMultiListBox extends HTMLPanel {

    private static final String _id = IdType.LIST_BOX.getName();
    private static final String LIST = "list";
    private static final String STYLE = "act";
    private static final String LINE = "###";
    private static int num = 0;

    private String id;
    private StringBuilder htmlText;
    private String oldSelectedId;
    private SelectItem selectedTemplate;
    private String directoryName;

    public DRSMultiListBox() {
        super("<ul class='select-list left' id='" + (_id + num) + "'></ul>");
        id = _id + num;
        num++;
        sinkEvents(Event.ONCLICK);
        htmlText = new StringBuilder("");
    }

    public void addList(ArrayList<ReportDirectoryPathRpc> list) {
        boolean isFirst = true;
        for (ReportDirectoryPathRpc aList : list) {
            htmlText.append("<li class='parent'><a id='" + (LIST + LINE + id + LINE + aList.getDirectoryName()) + "'>" + aList.getDirectoryName() + "</a></li>");
            for (SelectItem reportTemplate : aList.getFiles()) {
                if (isFirst) {
                    oldSelectedId = (LIST + LINE + id + LINE + aList.getDirectoryName() + LINE + reportTemplate.getName() + LINE + reportTemplate.getId().toString()) + LINE + reportTemplate.getDescription();
                    directoryName = aList.getDirectoryName();
                    selectedTemplate = reportTemplate;
                    htmlText.append("<li class='act' ><a style='padding-left:15px;' id='" + (LIST + LINE + id + LINE + aList.getDirectoryName() + LINE + reportTemplate.getName() + LINE + reportTemplate.getId().toString() + LINE + reportTemplate.getDescription()) + "'>" + reportTemplate.getName() + "</a></li>");
                    isFirst = !isFirst;
                } else {
                    htmlText.append("<li><a style='padding-left:15px;' id='" + (LIST + LINE + id + LINE + aList.getDirectoryName() + LINE + reportTemplate.getName() + LINE + reportTemplate.getId().toString() + LINE + reportTemplate.getDescription()) + "'>" + reportTemplate.getName() + "</a></li>");
                }
            }
        }

        add(new HTML(htmlText.toString()), id);
    }

    public void setSelectedByName(SelectItem reportTemplate) {
        Element oldElem = DOM.getElementById(oldSelectedId);
        Element elem = DOM.getElementById(LIST + LINE + id + LINE + directoryName + LINE + reportTemplate.getName() + LINE + reportTemplate.getId().toString());
        if (!oldElem.equals(elem)) {
            com.google.gwt.dom.client.Element oldElemParent = oldElem.getParentElement();
            com.google.gwt.dom.client.Element elemParent = elem.getParentElement();
            oldElemParent.removeClassName(STYLE);
            elemParent.addClassName(STYLE);
            oldSelectedId = LIST + LINE + id + LINE + directoryName + LINE + reportTemplate.getName();
            selectedTemplate = new SelectItem(1, elem.getInnerText());
            this.directoryName = directoryName;
            if (changeEvent != null) {
                changeEvent.changeEvent(reportTemplate);
            }
        }
    }

    public SelectItem getSelectedTemplate() {
        return selectedTemplate;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        com.google.gwt.dom.client.Element elem = com.google.gwt.dom.client.Element.as(event.getEventTarget());
        String att = elem.getAttribute("id");
        if (att != null) {
            String[] arr = att.split(LINE);
            if (arr != null && arr.length == 6) {
                if (LIST.equals(arr[0])) {
                    Element oldElem = DOM.getElementById(oldSelectedId);
                    com.google.gwt.dom.client.Element oldElemDom = oldElem.getParentElement();
                    if (!elem.equals(oldElem)) {
                        oldElemDom.removeClassName(STYLE);
                        com.google.gwt.dom.client.Element elemParent = elem.getParentElement();
                        elemParent.addClassName(STYLE);
                        oldSelectedId = att;
                        directoryName = arr[2];
                        selectedTemplate = new SelectItem(Integer.valueOf(arr[4]), elem.getInnerText(), arr[5]);
                        if (changeEvent != null) {
                            changeEvent.changeEvent(selectedTemplate);
                        }
                    }
                }
            }
        }
    }

    public interface ChangeEvent {
        void changeEvent(SelectItem reportTemplate);
    }

    private ChangeEvent changeEvent;

    public void addChangeEvent(ChangeEvent changeEvent) {
        this.changeEvent = changeEvent;
    }
}
