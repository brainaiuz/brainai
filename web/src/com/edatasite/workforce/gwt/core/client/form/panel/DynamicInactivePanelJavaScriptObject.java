package com.edatasite.workforce.gwt.core.client.form.panel;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * User: Abror Abdukadirov
 * Date: 09.08.2019 19:22
 */
public class DynamicInactivePanelJavaScriptObject extends JavaScriptObject {

    protected DynamicInactivePanelJavaScriptObject() {

    }

    private static native DynamicInactivePanelJavaScriptObject create()/*-{
      return {};
    }-*/;

    public static DynamicInactivePanelJavaScriptObject createOptions() {
        DynamicInactivePanelJavaScriptObject option = create();
        option.setAnimate(true);
        option.setWidth(12);
        option.setHeight(20);
        option.setCellHeight(40);
//        option.setVerticalMargin(35);
        option.setResizable(true, "e");
        option.setDisableResize(true);
        option.setDraggable(".grid-stack-item-content");
//        option.setFloat(false);
//        option.setAcceptWidgets(".grid-stack-item");
        option.setAlwaysShowResizeHandle(true);
        return option;
    }

    public final native void setAnimate(boolean animate)/*-{
      this.animate = animate;
    }-*/;

    public final native void setFloat(boolean isFloat)/*-{
      this['float'] = isFloat;
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

    public final native void setDraggable(String className)/*-{
      this.draggable = {
        handle: className
      };
    }-*/;

    public final native void setAcceptWidgets(String className)/*-{
      this.acceptWidgets = className;
    }-*/;

    public final native void setAlwaysShowResizeHandle(boolean show)/*-{
      this.alwaysShowResizeHandle = show;
    }-*/;

    public final native void setDisableResize(boolean disable)/*-{
      this.disableResize = disable;
    }-*/;
}
