package com.edatasite.workforce.gwt.core.client.ui.wfmTabPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 02.05.2009
 * Time: 16:39:14
 * To change this template use File | Settings | File Templates.
 */
public interface WfmTabListener {

    boolean onBeforeTabSelected(int tabIndex);

    void onTabSelected(int tabIndex);
}
