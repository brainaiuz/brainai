package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BulkAddCategoriesItem implements IsSerializable {
    Integer objectId;
    Integer total;
    PaymentDeductionSelectItem[] paymentDeductionSelectItems;

    public BulkAddCategoriesItem() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PaymentDeductionSelectItem[] getPaymentDeductionSelectItems() {
        return paymentDeductionSelectItems;
    }

    public void setPaymentDeductionSelectItems(PaymentDeductionSelectItem[] paymentDeductionSelectItems) {
        this.paymentDeductionSelectItems = paymentDeductionSelectItems;
    }
}
