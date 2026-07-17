package com.edatasite.workforce.gwt.ganttchart.client.treetable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 16, 2011
 * Time: 10:27:40 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GanttChartTreeRenderer {
    /**
	 * Called to render a tree item row.
	 * @param table
	 * @param item
	 * @param row
	 */
	void renderTreeItem(GanttChartTree table, GanttTreeItem item, int row);
}
