package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * User: ${Dilsh0d}
 * Date: 05-Apr-2010
 * Time: 09:49:17
 */
public enum DateRangeType {
	Daily(0),
	Weekly(1),
	Monthly(2),
	Quarterly(3),
	Yearly(4);

	DateRangeType(Integer id) {
		this.id = id;
	}

	private Integer id;

	public Integer getId() {
		return id;
	}

	public static SelectItem[] getAsSelectItems() {
		SelectItem[] items = new SelectItem[values().length];
		int i = 0;
		for (DateRangeType type : values()) {
			items[i++] = new SelectItem(type.id, type.name());
		}
		return items;
	}
}
