package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.BookingItemsAddSinksContainer;
import com.edatasite.workforce.gwt.project.client.BookingItemsViewSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/18/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsHistoryProcessor implements HistoryProcessor {
	private WfmStrings wfmStrings = WfmStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
        return new BookingItemsViewSinksContainer(containerName + strings[0], wfmStrings.item(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
        return new BookingItemsAddSinksContainer("bookingitemsadd", wfmStrings.addItem(), params);
	}
}
