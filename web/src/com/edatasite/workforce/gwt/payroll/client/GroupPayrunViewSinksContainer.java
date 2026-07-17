package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun.GroupPayrunEditView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun.GroupPayrunSubView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun.GroupPayrunSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 07.03.14
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class GroupPayrunViewSinksContainer extends SinksContainer {

    public GroupPayrunViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new GroupPayrunSummaryView(id));
        addView(new GroupPayrunEditView(id));
        addView(new GroupPayrunSubView(id));
    }
}
