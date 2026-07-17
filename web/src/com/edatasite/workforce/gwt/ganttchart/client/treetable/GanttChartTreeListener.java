package com.edatasite.workforce.gwt.ganttchart.client.treetable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 16, 2011
 * Time: 10:28:16 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GanttChartTreeListener {
    /**
   * Fired when a tree item is selected.
   *
   * @param item the item being selected.
   */
  void onTreeItemSelected(GanttTreeItem item);

  /**
   * Fired when a tree item is opened or closed.
   *
   * @param item the item whose state is changing.
   */
  void onTreeItemStateChanged(GanttTreeItem item);
}
