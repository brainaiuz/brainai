package com.edatasite.workforce.gwt.core.server.commons;

import org.apache.poi.ss.util.CellRangeAddress;

public class Region extends CellRangeAddress {
    public Region(int fromRow, short fromColumn, int toRow, short toColumn) {
        super(fromRow, toRow, fromColumn, toColumn);

    }
}
