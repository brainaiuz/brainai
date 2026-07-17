package com.edatasite.workforce.gwt.core.client.form.panel;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * User: Abror Abdukadirov
 * Date: 09.04.2018 19:12
 */
public class DynamicJavaScriptItem extends JavaScriptObject {

    protected DynamicJavaScriptItem() {
    }

    public static native DynamicJavaScriptItem create()/*-{
        return {};
    }-*/;

    public final native void setX(int x)/*-{
        this.x = x;
    }-*/;

    public final native int getX()/*-{
        return this.x;
    }-*/;

    public final native void setY(int y)/*-{
        this.y = y;
    }-*/;

    public final native int getY()/*-{
        return this.y;
    }-*/;

    public final native void setWidth(int width)/*-{
        this.width = width;
    }-*/;

    public final native int getWidth()/*-{
        return this.width;
    }-*/;

    public final native void setMinWidth(int minWidth)/*-{
        this.minWidth = minWidth;
    }-*/;

    public final native int getMinWidth()/*-{
        return this.minWidth;
    }-*/;

    public final native void setHeight(int height)/*-{
        this.height = height;
    }-*/;

    public final native int getHeight()/*-{
        return this.height;
    }-*/;

    public final native void setMinHeight(int minHeight)/*-{
        this.minHeight = minHeight;
    }-*/;

    public final native int getMinHeight()/*-{
        return this.minHeight;
    }-*/;

    public final native Element getElement()/*-{
        return this.el[0];
    }-*/;

    public final native void setFieldId(String fieldId)/*-{
        this.fieldId = fieldId;
    }-*/;

    public final native String getFieldId()/*-{
        return this.fieldId;
    }-*/;
}
