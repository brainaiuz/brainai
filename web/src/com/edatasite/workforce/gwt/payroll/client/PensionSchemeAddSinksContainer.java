package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PensionSchemaAddEditView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 4:20:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemeAddSinksContainer extends SinksContainer {
    public PensionSchemeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
//        if (params.length > 1 && params[1] != null) {
//            addView(new AddPensionSchemeView(Integer.parseInt(params[1])));
//        } else {
//            addView(new AddPensionSchemeView());
//        }
        addView(new PensionSchemaAddEditView());
    }
}
