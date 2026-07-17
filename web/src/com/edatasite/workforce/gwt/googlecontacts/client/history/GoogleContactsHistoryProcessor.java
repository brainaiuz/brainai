package com.edatasite.workforce.gwt.googlecontacts.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.googlecontacts.client.GoogleContactsSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:01:46
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactsHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return new GoogleContactsSinksContainer(containerName + strings[0], "Google Talk"/*, "icon-googlecontacts"*/);
    }

    public SinksContainer processAdd(String[] params) {
        return new GoogleContactsSinksContainer("googlecontactadd","Google Talk" /*, "icon-googlecontacts"*/);
    }
}