package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * User: ${Dilsh0d}
 * Date: 05-Apr-2010
 * Time: 09:30:37
 */
public enum SortType {
	Ascending(0),
	Descending(1);

	SortType(int pos) {
		this.pos = pos;
	}

	private int pos;

	public int getPos() {
		return pos;
	}

	public static SelectItem[] getAsSelectItems() {
		SelectItem[] items = new SelectItem[values().length];
		int i = 0;
		for (SortType type : values()) {
			items[i++] = new SelectItem(type.pos, type.name());
		}
		return items;
	}
}
