package com.edatasite.workforce.gwt.ganttchart.client.treetable;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 16, 2011
 * Time: 10:26:41 AM
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartTreeListenerCollection extends Vector {

    /**
     * Fires a "tree item selected" event to all listeners.
     * @param item the tree item being selected.
     */
    public void fireItemSelected(GanttTreeItem item) {
        for (Object o : this) {
            GanttChartTreeListener listener = (GanttChartTreeListener) o;
            listener.onTreeItemSelected(item);
        }
    }

    /**
     * Fires a "tree item state changed" event to all listeners.
     * @param item the tree item whose state has changed.
     */
    public void fireItemStateChanged(GanttTreeItem item) {
        for (Object o : this) {
            GanttChartTreeListener listener = (GanttChartTreeListener) o;
            listener.onTreeItemStateChanged(item);
        }
    }
}

