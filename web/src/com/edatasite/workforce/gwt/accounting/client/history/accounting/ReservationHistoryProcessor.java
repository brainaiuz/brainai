package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ReservationAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ReservationViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 5:35:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmString = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ReservationViewSinksContainer(containerName + strings[0], wfmString.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ReservationAddSinksContainer("reservationadd", wfmString.addReservation());
    }
}