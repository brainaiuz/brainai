package com.edatasite.workforce.gwt.core.client.ui.SortableTable;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.gen2.table.override.client.FlexTable;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid Asatillayev
 * Date: Aug 3, 2010
 * Time: 3:04:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableHeader extends FlexTable {
    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public TableHeader(String text, int colspan) {
        super();
        setStyleName("file--TableHeader");
        setText(0, 0, wfmStrings.hoursSpentByAllTotal());
        getFlexCellFormatter().setStyleName(0, 0, "file--TableHeader");
        getFlexCellFormatter().setColSpan(0, 0, 12);
    }
}
