package com.finnetlimited.reportservice.core.client.ui.loading;

import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 30-Mar-2010
 * Time: 17:16:28
 */
public class DRSLoadingPanel {

    public static void show() {
        LoadingPanel.loading(true);
    }

    public static void show(String text) {
        LoadingPanel.loading(true);
    }

    public static void hide() {
        LoadingPanel.loading(false);
    }

    public static void show(Widget widget) {
        LoadingPanel.loading(true);
    }

}
