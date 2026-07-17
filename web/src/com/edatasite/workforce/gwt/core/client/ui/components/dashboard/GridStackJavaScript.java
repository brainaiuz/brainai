package com.edatasite.workforce.gwt.core.client.ui.components.dashboard;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * User: Abror Abdukadirov
 * Date: 17.04.2018 14:34
 */
public class GridStackJavaScript extends JavaScriptObject {

    protected GridStackJavaScript() {
    }

    public static native GridStackJavaScript create()/*-{
        return {};
    }-*/;

    public static GridStackJavaScript createOptions(boolean isStatic) {
        GridStackJavaScript option = create();
        option.setAnimate(true);
        option.setCellHeight(80);
        option.setWidth(12);
        option.setHeight(100);
        option.setVerticalMargin(20);
        option.setResizable(true, "e, se, s, sw, w");
        option.setDraggable(".grid-stack-item-content");
        option.setAlwaysShowResizeHandle(true);
        option.setStaticGrid(isStatic);
        if (isStatic) {
            option.setDisableDrag(isStatic);
            option.setDisableResize(isStatic);
        }
        return option;
    }

    public final native void setAnimate(boolean animate)/*-{
        this.animate = animate;
    }-*/;

    public final native void setWidth(int width)/*-{
        this.width = width;
    }-*/;

    public final native void setHeight(int height)/*-{
        this.height = height;
    }-*/;

    public final native void setCellHeight(int height)/*-{
        this.cellHeight = height;
    }-*/;

    public final native void setVerticalMargin(int margin)/*-{
        this.verticalMargin = margin;
    }-*/;

    public final native void setResizable(boolean auto, String handles)/*-{
        this.resizable = {
            autoHide: auto,
            handles: handles
        };
    }-*/;

    public final native void setDraggable(String handle)/*-{
        this.draggable = {
            handle: handle
        };
    }-*/;

    public final native void setAlwaysShowResizeHandle(boolean show)/*-{
        this.alwaysShowResizeHandle = show;
    }-*/;

    public final native void setStaticGrid(boolean staticGrid)/*-{
        this.staticGrid = staticGrid;
    }-*/;

    public final native boolean isStaticGrid()/*-{
        return this.staticGrid;
    }-*/;

    public final native void setDisableDrag(boolean disable)/*-{
        this.disableDrag = disable;
    }-*/;

    public final native void setDisableResize(boolean disable)/*-{
        this.disableResize = disable;
    }-*/;
}
