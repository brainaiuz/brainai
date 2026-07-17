package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Hurshid on 1/18/2019
 */
public class LeaveReasonRpc implements IsSerializable {
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
