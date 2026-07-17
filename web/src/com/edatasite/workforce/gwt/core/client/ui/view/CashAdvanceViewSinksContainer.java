package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 5:46
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceViewSinksContainer extends SinksContainer {

    public CashAdvanceViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_VIEW)) {
            CashAdvanceSummary summaryView = new CashAdvanceSummary(id, params[1]);
            addView(summaryView);
            addView(new CashAdvancePaymentListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT)) {
            AddEditCashAdvanceView editView = new AddEditCashAdvanceView(id, params[1]);
            addView(editView);
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)){
            addView(new WebHookResponseListView(id, RelationItem.TYPE_CASH_ADVANCE));
        }

    }
}
