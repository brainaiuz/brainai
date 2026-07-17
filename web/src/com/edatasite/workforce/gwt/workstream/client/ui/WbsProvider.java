package com.edatasite.workforce.gwt.workstream.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;

public interface WbsProvider {
    void getItems(WbsCallback callback);

    void getChildren(WbsItem parent, WbsCallback callback);
}
