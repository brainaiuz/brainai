package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PensionSchemaSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 29.10.11
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemeListSinksContainer extends SinksContainer {

    public PensionSchemeListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
//        if (params.length > 0 && params[0] != null&&!params[0].equals("")) {
        addView(new PensionSchemaSummaryView());
//        }else{
//            addView(new PensionSchemeListView());
//        }
    }
}
