package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;

public interface DefaultMailServiceLocal {

    Integer testConnection(EmailAccountItem item);
}
