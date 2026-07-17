package com.edatasite.workforce.gwt.client.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ClientMessages extends Messages {

    String contactWithEmailsExist(String emails);

    String clients(String p0);

    String suppliers(String p0);

    String successfullyDeletedButSomeNotClients(String p0);

    String successfullyDeletedButSomeNotSuppliers(String p0);

    class App {
        public static ClientMessages get() {
            return (ClientMessages) GWT.create(ClientMessages.class);
        }
    }
}
