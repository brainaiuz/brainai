package com.edatasite.workforce.gwt.backend.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/18/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WftBackendMessages extends Messages {

	String couldNotMarkCOMPANYNAMEAsTestCompany(String p0);

	String COMPANYNAMEHasBeenMarkedAsTestCompany(String p0);

	String userNamesAreUSERSCOUNTHasUpdatedToATestEmail(String p0, String p1);

    class App {
        private static WftBackendMessages instance;

		public static WftBackendMessages get() {
			if (instance == null) {
				instance = GWT.create(WftBackendMessages.class);
			}
			return instance;
		}
	}

}
