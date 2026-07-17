package com.edatasite.workforce.gwt.core.client.ui.components.dashboard;

import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * User: Abror Abdukadirov
 * Date: 05.04.2018 19:34
 */
public class GridStackPanel extends Composite {
    interface GridStackPanelUiBinder extends UiBinder<Widget, GridStackPanel> {
    }

    private static GridStackPanelUiBinder ourUiBinder = GWT.create(GridStackPanelUiBinder.class);

    private HashMap<Element, GridStackItemPanel> gridItemMap = new HashMap<>();
    private HashMap<String, GridStackJavaScriptItem> positionMap = new HashMap<>();
    private HashMap<String, Integer> coordinates = new HashMap<>();

    @UiField
    HTMLPanel container;

    private boolean isStatic;
    private DataListBox inactiveListBox;
    private boolean hasInitialized = false;

    public GridStackPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void initializeConfig(boolean isStatic) {
        this.isStatic = isStatic;
        initializeGridOptions(GridStackJavaScript.createOptions(isStatic));
    }

    public void registerHandles() {
        $(container.getElement()).on("change", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                if (param1 instanceof JavaScriptObject) {
                    GridStackJavaScriptItem[] items = (GridStackJavaScriptItem[]) param1;

                    for (int i = 0; i < items.length; ++i) {
                        GridStackJavaScriptItem item = items[i];

                        String componentCode = null;
                        Widget widget = getWidgetByElement(item.getElement());

                        if (widget != null) {
                            if (widget instanceof GridStackItemPanel) {
                                GridStackItemPanel gridItemPanel = (GridStackItemPanel) widget;
                                widget = gridItemPanel.getItemWidget();
                                componentCode = gridItemPanel.getComponentCode();
                            }
                            if (widget instanceof DashboardBaseWidget) {

                                DashboardBaseWidget dashboardBaseWidget = ((DashboardBaseWidget) widget);

                                dashboardBaseWidget.resizeChart();

                                if (componentCode == null) {
                                    componentCode = dashboardBaseWidget.getCode();
                                }
                            }
                        }
                        item.setComponentCode(componentCode);
                        positionMap.put(componentCode, item);
                    }
                }
                return null;
            }
        });

        $(container.getElement()).on("gsresizestop", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                $(".cp_grid div").removeClass("active");
                return null;
            }
        });

        $(container.getElement()).on("dragstart", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                $(".grid-stack-item").mousemove(new Functions.EventFunc1() {
                    @Override
                    public Object call(Event e, Object param1) {
                        try {
                            setBackgroundGrid();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    }
                });
                return null;
            }
        });

        $(container.getElement()).on("dragstop", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                $(".cp_grid div").removeClass("active");
                $(".grid-stack-item").unbind("mousemove");
                return null;
            }
        });

        rewriteItemResizeHandle();
    }

    public void rewriteItemResizeHandle() {
        $(".grid-stack-item").unbind("resize");
        $(".grid-stack-item").resize(new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                try {
                    setBackgroundGrid();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        });
    }

    public void deleteAnimationHandles() {
        $(".widget-delete").unbind("mouseenter mouseleave");

        $(".widget-delete").hover(new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                if ($(e.getCurrentTarget()) != null) {
                    JQueryElement gridItem = $(e.getCurrentTarget()).parents(".grid-stack-item");
                    if (gridItem == null) {
                        return null;
                    }
                    if (gridItem.find(".widget-body") != null) {
                        gridItem.find(".widget-body")
                                .css("opacity", "0.5")
                                .css("transition", "opacity 0.3s ease-out 0s")
                                .find("*")
                                .css("filter", "grayscale(1)")
                                .css("transition", "filter 0.3s ease-out 0s");
                    }
                }
                return null;
            }
        }, new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object param1) {
                if ($(e.getCurrentTarget()) != null) {
                    JQueryElement gridItem = $(e.getCurrentTarget()).parents(".grid-stack-item");
                    if (gridItem == null) {
                        return null;
                    }
                    if (gridItem.find(".widget-body") != null) {
                        gridItem.find(".widget-body")
                                .css("opacity", "1")
                                .find("*")
                                .css("filter", "grayscale(0)");
                    }
                }
                return null;
            }
        });
    }

    public void unbindDeleteAnimationHandles() {
        $(".widget-delete").unbind("mouseenter mouseleave");
    }

    private void setBackgroundGrid() {
        JQueryElement placeholder = $(".grid-stack-placeholder");

        int gsx = Integer.valueOf((String) placeholder.attr("data-gs-x"));
        int gsy = Integer.valueOf((String) placeholder.attr("data-gs-y"));
        int gsw = Integer.valueOf((String) placeholder.attr("data-gs-width"));
        int gsh = Integer.valueOf((String) placeholder.attr("data-gs-height"));

        boolean isduplicate = false;

        if (coordinates.size() > 0) {
            if (coordinates.get("x") == gsx
                    && coordinates.get("y") == gsy
                    && coordinates.get("w") == gsw
                    && coordinates.get("h") == gsh) {
                isduplicate = true;
            }
        }
        if (!isduplicate) {
            //coordinates
            coordinates.put("x", gsx);
            coordinates.put("y", gsy);

            //dimension
            coordinates.put("w", gsw);
            coordinates.put("h", gsh);

            //corners
            coordinates.put("lt", gsy * 12 + gsx + 1);
            coordinates.put("rt", gsy * 12 + gsx + gsw);
            coordinates.put("lb", (gsy + gsh - 1) * 12 + gsx + 1);
            coordinates.put("rb", (gsy + gsh - 1) * 12 + gsx + gsw);

            $(".cp_grid div").removeClass("active");

            int k = 0;
            int coldiff = coordinates.get("rt") - coordinates.get("lt");

            //highlight the cells under the placeholder on resize and dragNdrop
            for (int i = coordinates.get("lt"); i < (coordinates.get("lt") + coordinates.get("h")); i++) {
                int row = i + k;
                for (int column = 0; column < coldiff; column++) {
                    //columns of the this row starting form the second column
                    $(".cp_grid div").eq(row + column).addClass("active");
                }
                //rows - first columns only.
                $(".cp_grid div").eq(row - 1).addClass("active");

                k += 11; // get to the next row, the same column.
            }
        }
    }

    public void addNewItem(GridStackItemPanel itemPanel, boolean autoPosition) {
        gridItemMap.put(itemPanel.getElement(), itemPanel);

        GridStackJavaScriptItem scriptItem = GridStackJavaScriptItem.create();
        scriptItem.setX(itemPanel.getX());
        scriptItem.setY(itemPanel.getY());
        scriptItem.setWidth(itemPanel.getWidth());
        scriptItem.setMinWidth(itemPanel.getMinWidth());
        scriptItem.setHeight(itemPanel.getHeight());
        scriptItem.setMinHeight(itemPanel.getMinHeight());
        scriptItem.setComponentCode(itemPanel.getComponentCode());
        positionMap.put(itemPanel.getComponentCode(), scriptItem);

        addItemNavite(container.getElement(),
                itemPanel.getElement(),
                itemPanel.getX(),
                itemPanel.getY(),
                itemPanel.getWidth(),
                itemPanel.getHeight(),
                autoPosition);

        if (hasInjected()) {
            container.add(itemPanel);
        } else {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    container.add(itemPanel);
                }
            };
            timer.schedule(4000);
        }
        if (!isStatic) {
            movableNative(container.getElement(), itemPanel.getElement());
            resizableNative(container.getElement(), itemPanel.getElement());
        }
        itemPanel.getHideLink().addClickHandler(event -> {
            removeItem(itemPanel);
        });
        if (isStatic) {
            itemPanel.getHideLink().setDisplay(Display.NONE);
        } else {
            itemPanel.getHideLink().setDisplay(Display.BLOCK);
        }
    }

    public void removeItem(GridStackItemPanel itemPanel) {
        positionMap.remove(itemPanel.getComponentCode());
        gridItemMap.remove(itemPanel.getElement());

        removeItemNative(container.getElement(), itemPanel.getElement());

        commitNative(container.getElement());

        if (inactiveListBox != null) {
            DashboardComponentItem item = new DashboardComponentItem();
            item.setId(itemPanel.getObjectId());
            item.setReportId(itemPanel.getReportId());
            item.setReportWidgetId(itemPanel.getReportWidgetId());
            item.setName(itemPanel.getComponentName());
            item.setComponentCode(itemPanel.getComponentCode());
            item.setWidth(itemPanel.getWidth());
            item.setMinWidth(itemPanel.getMinWidth());
            item.setHeight(itemPanel.getHeight());
            item.setMinHeight(itemPanel.getMinHeight());
            inactiveListBox.addListItem(item);
            inactiveListBox.setDisplay(Display.BLOCK);
        }
    }

    public void removeAllItem() {
        positionMap.clear();
        gridItemMap.clear();

        removeAllItemNative(container.getElement());

        commitNative(container.getElement());
    }

    private Widget getWidgetByElement(Element element) {
        if (element == null) {
            return null;
        }
        GridStackItemPanel gridItemPanel = gridItemMap.get(element);
        if (gridItemPanel != null && gridItemPanel.getItemWidget() != null) {
            return gridItemPanel.getItemWidget();
        }
        if (this.container.getWidgetCount() > 0) {
            for (int i = 0; i < this.container.getWidgetCount(); i++) {
                Widget gridItem = this.container.getWidget(i);
                if (gridItem != null && element.equals(gridItem.getElement())) {
                    return gridItem;
                }
            }
        }
        return null;
    }

    public ArrayList<DashboardComponentItem> getComponents() {
        ArrayList<DashboardComponentItem> result = new ArrayList<>();

        if (positionMap.size() <= 0) {
            return result;
        }
        for (GridStackJavaScriptItem item : positionMap.values()) {
            DashboardComponentItem itemRpc = new DashboardComponentItem();
            itemRpc.setX(item.getX());
            itemRpc.setY(item.getY());
            itemRpc.setWidth(item.getWidth());
            itemRpc.setMinWidth(item.getMinWidth());
            itemRpc.setHeight(item.getHeight());
            itemRpc.setMinHeight(item.getMinHeight());
            itemRpc.setComponentCode(item.getComponentCode());
            result.add(itemRpc);
        }
        return result;
    }

    private void changeWidgetStyles() {
        this.hasInitialized = true;
        for (GridStackItemPanel itemPanel : gridItemMap.values()) {
            if (isStatic) {
                itemPanel.getHideLink().setDisplay(Display.NONE);
                itemPanel.addStyleName("ui-resizable-autohide");
            } else {
                itemPanel.getHideLink().setDisplay(Display.BLOCK);
            }
        }
    }

    public void commit() {
        commitNative(container.getElement());
    }

    public boolean hasInjected() {
        return hasInjectedNative(container.getElement());
    }

    private native boolean hasInjectedNative(Element container) /*-{
        var grid = $wnd.$(container).data('gridstack');
        return grid !== undefined;
    }-*/;

    private native void addItemNavite(Element container, Element item, int x, int y, int width, int height, boolean auto) /*-{
        var grid = $wnd.$(container).data('gridstack');
        if (grid) {
            grid.addWidget($wnd.$(item), x, y, width, height, auto);
        } else {
            setTimeout(function () {
                var grid = $wnd.$(container).data('gridstack');
                grid.addWidget($wnd.$(item), x, y, width, height, auto);
            }, 4000);
        }
    }-*/;

    private native void removeItemNative(Element container, Element item) /*-{
        var grid = $wnd.$(container).data('gridstack');

        grid.removeWidget($wnd.$(item), true);
    }-*/;

    private native void removeAllItemNative(Element container) /*-{
        var grid = $wnd.$(container).data('gridstack');

        grid.removeAll(true);
    }-*/;

    private native void commitNative(Element container) /*-{
        var grid = $wnd.$(container).data('gridstack');
        if (grid) {
            grid.commit();
        } else {
            setTimeout(function () {
                var grid = $wnd.$(container).data('gridstack');
                grid.commit();
            }, 4000);
        }
    }-*/;

    private native void movableNative(Element container, Element element) /*-{
        var grid = $wnd.$(container).data('gridstack');
        if (grid) {
            grid.movable(element, true);
        } else {
            setTimeout(function () {
                var grid = $wnd.$(container).data('gridstack');
                grid.movable(element, true);
            }, 4000);
        }
    }-*/;

    private native void resizableNative(Element container, Element element) /*-{
        var grid = $wnd.$(container).data('gridstack');
        if (grid) {
            grid.resizable(element, true);
        } else {
            setTimeout(function () {
                var grid = $wnd.$(container).data('gridstack');
                grid.resizable(element, true);
            }, 4000);
        }
    }-*/;

    private native void initializeGridOptions(GridStackJavaScript options) /*-{
        var that = this;
        $wnd.$(function () {
            $wnd.$('.grid-stack').gridstack(options);

            $wnd.$('.grid-stack').data('gridstack').setStatic(options.staticGrid);

            that.@com.edatasite.workforce.gwt.core.client.ui.components.dashboard.GridStackPanel::changeWidgetStyles()();
        });
    }-*/;

    public void clear() {
        container.clear();
    }

    public HTMLPanel getContainer() {
        return container;
    }

    public ArrayList<String> getComponentCodes() {
        return new ArrayList<>(positionMap.keySet());
    }

    public Map<String, GridStackJavaScriptItem> getActiveComponentMap() {
        return positionMap;
    }

    public HashMap<Element, GridStackItemPanel> getGridItemMap() {
        return gridItemMap;
    }

    public void setInactiveListBox(DataListBox inactiveListBox) {
        this.inactiveListBox = inactiveListBox;
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }
}
