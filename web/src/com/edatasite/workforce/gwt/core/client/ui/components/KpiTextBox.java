package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialTextBox;

/**
 * Created by: Azazello
 * Date: 1/12/2018
 * Time: 1:10 PM
 */
public class KpiTextBox extends MaterialTextBox implements Constants{
    public KpiTextBox() {
        super();
        remove(getLabelWidget());
        remove(getErrorLabel());
    }

    public KpiTextBox(InputType type) {
        super();
        setType(type);
        remove(getLabelWidget());
        remove(getErrorLabel());
    }

    public KpiTextBox(String placeholder) {
        super(placeholder);
    }

    @Override
    public void setText(String text) {
        getValueBoxBase().setText(text);
    }


    public void setSelectionRange(Integer start, Integer end) {
        setSelectionRange(getElement(), start, end);
    }

    private native void setSelectionRange(com.google.gwt.user.client.Element element, Integer start, Integer end) /*-{
        $wnd.jQuery(element).find('input').get(0).setSelectionRange(start, end);
    }-*/;
}
