package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.BookingItemsReservationAddSinksContainer;
import com.edatasite.workforce.gwt.project.client.BookingItemsReservationViewSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsReservationHistoryProcessor implements HistoryProcessor {
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return new BookingItemsReservationViewSinksContainer(containerName + strings[0], "Reservation Summary", strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
        return new BookingItemsReservationAddSinksContainer("bookingitemsreservationadd", wfmStrings.addReservation(), params);
	}
}
