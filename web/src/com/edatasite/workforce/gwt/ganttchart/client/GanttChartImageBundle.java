package com.edatasite.workforce.gwt.ganttchart.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 3/25/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GanttChartImageBundle extends ClientBundle {
    GanttChartImageBundle INSTANCE = GWT.create(GanttChartImageBundle.class);

    @Source("com/edatasite/workforce/gwt/ganttchart/server/images/tree_closed.gif")
	ImageResource treeClosed();

    @Source("com/edatasite/workforce/gwt/ganttchart/server/images/tree_open.gif")
	ImageResource treeOpen();

    @Source("com/edatasite/workforce/gwt/ganttchart/server/images/tree_white.gif")
	ImageResource treeWhite();
}
