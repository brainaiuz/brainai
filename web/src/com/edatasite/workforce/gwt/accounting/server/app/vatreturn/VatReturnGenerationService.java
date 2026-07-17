package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnTransactionItem;

import java.util.List;

public interface VatReturnGenerationService<T> {

    T generateVatReturn(Integer vatReturnId);

    List<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box);
}
