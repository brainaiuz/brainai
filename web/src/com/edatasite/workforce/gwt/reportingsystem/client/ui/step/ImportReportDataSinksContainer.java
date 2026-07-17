package com.edatasite.workforce.gwt.reportingsystem.client.ui.step;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ImportReportDataView;

import java.util.LinkedList;

/**
 * Created by Faxriddin Taslimov on 13/08/19.
 */
public class ImportReportDataSinksContainer extends SinksContainer {
    public ImportReportDataSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ImportReportDataView(Integer.valueOf(params[1]), Integer.valueOf(params[2])));
    }
}
