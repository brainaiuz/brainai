package com.edatasite.workforce.gwt.core.client.ui.multiwidget;

import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: hayot
 * Date: Jan 12, 2011
 * Time: 4:43:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class WidgetsMap {
    private Integer objectID;
    private LinkedList<Widget> widgets = new LinkedList<>();
    private LinkedList<Widget> leftWidgets = new LinkedList<>();
    private LinkedList<Widget> rightWidgets = new LinkedList<>();
    private HashMap<String, Widget> mapOfWidgets;

    public WidgetsMap() {
        mapOfWidgets = new HashMap<>();
    }

    public Widget[] getWidgets() {
        return widgets.toArray(new Widget[]{});
    }

    public void addWidgets(Widget... widgets) {
        addWidgetsToPlaces(null, widgets);
    }

    private void addWidgetsToPlaces(LinkedList list, Widget... widgets) {
        if (widgets != null && widgets.length > 0) {
            for (Widget widget : widgets) {
                if (widget != null && !this.widgets.contains(widget)) {
                    this.widgets.add(widget);
                }
                if (list != null && widget != null && !list.contains(widget)) {
                    list.add(widget);
                }
            }
        }
    }

    public void addLeftWidgets(Widget... widgets) {
        addWidgetsToPlaces(leftWidgets, widgets);
    }

    public void addRightWidgets(Widget... widgets) {
        addWidgetsToPlaces(rightWidgets, widgets);
    }

    public boolean isLeftWidget(Widget widget){
        return leftWidgets.contains(widget);
    }

    public boolean isRightWidget(Widget widget){
        return rightWidgets.contains(widget);
    }

    /**
     * @param nameOfTheWidget this value must be unic for the multiTable
     * @param widget
     */
    public void addToRight(String nameOfTheWidget, Widget widget) {
        if(nameOfTheWidget != null){
            mapOfWidgets.put(nameOfTheWidget, widget);
        }
        addRightWidgets(widget);
    }

    public void addToLeft(String nameOfTheWidget, Widget widget) {
        if(nameOfTheWidget != null){
            mapOfWidgets.put(nameOfTheWidget, widget);
        }
        addLeftWidgets(widget);
    }

    public void addToCenter(String nameOfTheWidget, Widget widget) {
        if(nameOfTheWidget != null){
            mapOfWidgets.put(nameOfTheWidget, widget);
        }
        add(nameOfTheWidget, widget);
    }
    public void add(String nameOfTheWidget, Widget widget) {
        if(nameOfTheWidget != null){
            mapOfWidgets.put(nameOfTheWidget, widget);
        }
        addWidgets(widget);
    }

    @Deprecated
    public void addWidgetToMap(String nameOfTheWidget, Widget widget) {
        mapOfWidgets.put(nameOfTheWidget, widget);
    }

    public HashMap<String, Widget> getWidgetsMap() {
        return mapOfWidgets;
    }

    public Widget getWidget(String widgetName) {
        return mapOfWidgets.get(widgetName);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
