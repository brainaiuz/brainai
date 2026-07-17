package com.edatasite.workforce.gwt.accounting.client.rpc.target;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by Shohruh on 27-Jan-17.
 */
public interface TargetErpService extends RemoteService {


    /**
     *
     * @param item
     * for customer currentDate, accountOwner, accountName, accountNumber,
     * accountPhone, addressLine(AdLine1 concat AdLine2), city,
     * country, state, postCode, bankAccount, currency, vatNumber,
     * paymentMethod, tax,
     * @return
     */
    String sendClientToTarget(Integer id, Boolean isClient);

    String sendInvoiceToTarget(Integer id);

    class App {
        public static TargetErpServiceAsync get() {
            ServiceDefTarget target = GWT.create(TargetErpService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/targeterp");
            return (TargetErpServiceAsync) target;
        }
    }
}
