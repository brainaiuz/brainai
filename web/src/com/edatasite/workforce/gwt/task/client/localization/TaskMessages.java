package com.edatasite.workforce.gwt.task.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 17:28:00
 * To change this template use File | Settings | File Templates.
 */
public interface TaskMessages extends Messages {

    String areYouSureYouWantToDeleteWorkstream(String p0);

    class App {
        private static TaskMessages instance;

        public static TaskMessages get() {
            if (instance == null) {
                instance = GWT.create(TaskMessages.class);
            }
            return instance;
        }
    }

}
