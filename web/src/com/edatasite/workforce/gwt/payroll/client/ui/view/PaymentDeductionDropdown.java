package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 13, 2009
 * Time: 12:52:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentDeductionDropdown extends WfmDropdown {

    public PaymentDeductionDropdown(boolean addNew, String addNewText) {
        super(addNew, addNewText);
    }

    public void addItems(PaymentDeductionSelectItem[] items) {
        super.addItems(items);
    }

    @Override
    public PaymentDeductionSelectItem getSelectedData() {
        return (PaymentDeductionSelectItem) super.getSelectedData();
    }

    public String getSelectedCategoryCode() {
        return getSelectedData() != null ? getSelectedData().getCode() : null;
    }
}
