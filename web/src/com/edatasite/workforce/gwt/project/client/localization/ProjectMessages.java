package com.edatasite.workforce.gwt.project.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 17:20:14
 * To change this template use File | Settings | File Templates.
 */

public interface ProjectMessages extends Messages {

    String accountIsDuplicated(String p0);

    String accountsAreDuplicated(String p0);


    class App {
        public static ProjectMessages get() {
            return (ProjectMessages) GWT.create(ProjectMessages.class);
        }
    }

}
