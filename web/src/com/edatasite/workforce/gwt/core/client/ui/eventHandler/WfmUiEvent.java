package com.edatasite.workforce.gwt.core.client.ui.eventHandler;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Sep 26, 2009
 * Time: 5:22:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WfmUiEvent<T> {
    void onWfmUiEvent(Widget sender, T args);
}