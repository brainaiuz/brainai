package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.FingerPrintDeviceStatusHistoryListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Farrukh on 26.10.2017.
 **/
public class FingerPrintDeviceStatusHistoryListSinksContainer extends SinksContainer {

    public FingerPrintDeviceStatusHistoryListSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new FingerPrintDeviceStatusHistoryListView(id, params[1]));
    }
}
