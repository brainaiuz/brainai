package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CheckListBox;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Virus on 10/15/14.
 */
public class ReportingUtils {

    public static String getValue(Widget widget) {
        if (widget instanceof TextBox) {
            return ((TextBox) widget).getText().toLowerCase();
        } else if (widget instanceof ReportingLookUp) {
            String text = ((ReportingLookUp) widget).getText().toLowerCase();
            if (LookUp.SEARCH_TEXT.toLowerCase().equals(text)) {
                return null;
            }
            return text;
        } else if (widget instanceof CheckListBox) {
            ArrayList<SelectItem> checkedList = ((CheckListBox) widget).getSelectedItems();
            StringBuilder tempBuffer = new StringBuilder();
            for (SelectItem selectItem : checkedList) {
                if (tempBuffer.length() > 0) {
                    tempBuffer.append("<->");
                }
                tempBuffer.append(selectItem.getName());
            }
            return tempBuffer.toString();
        } else if (widget instanceof DataListBox) {
            SelectItem selectItem = ((DataListBox) widget).getSelectedItem();
            return selectItem == null ? null : selectItem.getName();
        }
        return "";
    }
}
