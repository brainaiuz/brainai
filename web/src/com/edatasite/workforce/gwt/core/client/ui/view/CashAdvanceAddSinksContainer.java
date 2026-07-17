package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 5:42
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceAddSinksContainer extends SinksContainer {

    public CashAdvanceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_ADD)) {
        AddEditCashAdvanceView cashAdvanceView = new AddEditCashAdvanceView();
        addView(cashAdvanceView);}
    }
}
