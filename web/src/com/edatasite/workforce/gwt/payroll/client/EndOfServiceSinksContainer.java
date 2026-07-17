package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.EndOfServiceCalculationView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EndOfServiceGratuitySummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 13.05.14
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceSinksContainer extends SinksContainer {

    public EndOfServiceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            addView(new EndOfServiceGratuitySummaryView(Integer.valueOf(params[1]), Utils.isSaudiCompany()));
        } else {
            addView(new EndOfServiceCalculationView(Utils.isSaudiCompany()));
        }
    }
}
