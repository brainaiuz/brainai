package com.edatasite.workforce.gwt.core.client.ui.dialogBox;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: jamshid.asatillayev
 * Date: Dec 22, 2010
 * Time: 3:36:22 AM
 */
public class WfmWindow {
    private final static WfmStrings wfmStrings = WfmStrings.App.get();

    public static void alert(String s) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, s);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.open();
    }

    public static void confirm(String s) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, s);
        messageBox.setTitle(wfmStrings.confirmation());
        messageBox.open();
    }

    public static void error(String message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, message);
        messageBox.setTitle(wfmStrings.error());
        messageBox.open();
    }

    public static void info(String message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, message);
        messageBox.setTitle(wfmStrings.information());
        messageBox.open();
    }
}
