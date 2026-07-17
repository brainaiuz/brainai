package com.edatasite.workforce.gwt.core.client.ui.SortableTable;

import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestSortableTable {

    /**
     * This is the entry point method.
     */
    public static SortableTable getTable() {
        final SortableTable sortableTable = new SortableTable();

        sortableTable.setWidth(500 + "px");
        sortableTable.setStyleName("sortableTable");
        sortableTable.setBorderWidth(1);
        sortableTable.setCellPadding(4);
        sortableTable.setCellSpacing(1);

        sortableTable.addColumnHeader("Employee", 0, null);
        sortableTable.addColumnHeader("Days", 1, null);
//		sortableTable.addColumnHeader("Hire Date", 2);
        sortableTable.addColumnHeader("Bonus", 2, null);

        // The rowIndex should begin with 1 as rowIndex 0 is for the Header
        // Any row with index == 0 will not be displayed.
        sortableTable.setValue(1, 0, "Parvinder Thapar");
        sortableTable.setValue(1, 1, 28);
//		sortableTable.setValue(1, 2, new SimpleDate(2005, 10, 25));
        sortableTable.setValue(1, 2, Float.valueOf("125.27"));

        sortableTable.setValue(2, 0, "David Brooks");
        sortableTable.setValue(2, 1, 32);
//		sortableTable.setValue(2, 2, new SimpleDate(2000, 4, 1));
        sortableTable.setValue(2, 2, Float.valueOf("105.78"));

        sortableTable.setValue(3, 0, "Raj Rajendran");
        sortableTable.setValue(3, 1, 30);
//		sortableTable.setValue(3, 2, new SimpleDate(2001, 12, 9));
        sortableTable.setValue(3, 2, Float.valueOf("236.82"));

        sortableTable.setValue(4, 0, "Brian Foley");
        sortableTable.setValue(4, 1, 38);
//		sortableTable.setValue(4, 2, new SimpleDate(2003, 2, 24));
        sortableTable.setValue(4, 2, Float.valueOf("489.29"));

        sortableTable.setValue(5, 0, "Visala Dhara");
        sortableTable.setValue(5, 1, 30);
//		sortableTable.setValue(5, 2, new SimpleDate(2001, 4, 23));
        sortableTable.setValue(5, 2, Float.valueOf("892.72"));

        sortableTable.setValue(6, 0, "Wasim Khan");
        sortableTable.setValue(6, 1, 35);
//		sortableTable.setValue(6, 2, new SimpleDate(1999, 7, 10));
        sortableTable.setValue(6, 2, Float.valueOf("1242.89"));

        sortableTable.setValue(7, 0, "Bob Hammel");
        sortableTable.setValue(7, 1, 56);
//		sortableTable.setValue(7, 2, new SimpleDate(1995, 2, 14));
        sortableTable.setValue(7, 2, Float.valueOf("107.21"));

        sortableTable.setValue(8, 0, "Jeanie Sa-ville");
        sortableTable.setValue(8, 1, 58);
//		sortableTable.setValue(8, 2, new SimpleDate(1989, 6, 1));
        sortableTable.setValue(8, 2, Float.valueOf("2372.42"));

        sortableTable.setValue(9, 0, "Scott Loyet");
        sortableTable.setValue(9, 1, 42);
//		sortableTable.setValue(9, 2, new SimpleDate(1992, 2, 29));
        sortableTable.setValue(9, 2, Float.valueOf("896.74"));

        sortableTable.setValue(10, 0, "Dennis Twiss");
        sortableTable.setValue(10, 1, 59);
//		sortableTable.setValue(10, 2, new SimpleDate(1990, 4, 15));
        sortableTable.setValue(10, 2, Float.valueOf("1896.74"));

        sortableTable.setValue(11, 0, "Mike McIntosh");
        sortableTable.setValue(11, 1, 76);
//		sortableTable.setValue(11, 2, new SimpleDate(1982, 5, 25));
        sortableTable.setValue(11, 2, Float.valueOf("689.77"));

        sortableTable.setValue(12, 0, "Andrews Andy");
        sortableTable.setValue(12, 1, 62);
//		sortableTable.setValue(12, 2, new SimpleDate(1994, 1, 15));
        sortableTable.setValue(12, 2, Float.valueOf("829.24"));

        sortableTable.setValue(13, 0, "Bob Regent");
        sortableTable.setValue(13, 1, 29);
//		sortableTable.setValue(13, 2, new SimpleDate(1996, 2, 12));
        sortableTable.setValue(13, 2, Float.valueOf("621.52"));

        sortableTable.setValue(14, 0, "Chris Chalmers");
        sortableTable.setValue(14, 1, 32);
//		sortableTable.setValue(14, 2, new SimpleDate(1997, 4, 1));
        sortableTable.setValue(14, 2, Float.valueOf("804.26"));

        sortableTable.setValue(15, 0, "Christopher Mathrusse");
        sortableTable.setValue(15, 1, 64);
//		sortableTable.setValue(15, 2, new SimpleDate(2005, 9, 10));
        sortableTable.setValue(15, 2, Float.valueOf("761.25"));

        sortableTable.setValue(16, 0, "John Smith");
        sortableTable.setValue(16, 1, 56);
//		sortableTable.setValue(16, 2, new SimpleDate(1992, 2, 16));
        sortableTable.setValue(16, 2, Float.valueOf("789.29"));

        sortableTable.setValue(17, 0, "Jane Smith");
        sortableTable.setValue(17, 1, 45);
//		sortableTable.setValue(17, 2, new SimpleDate(1989, 7, 25));
        sortableTable.setValue(17, 2, Float.valueOf("2254.87"));

        sortableTable.setValue(18, 0, "Jason Chen");
        sortableTable.setValue(18, 1, 37);
//		sortableTable.setValue(18, 2, new SimpleDate(1995, 8, 24));
        sortableTable.setValue(18, 2, Float.valueOf("978.32"));

        sortableTable.setValue(19, 0, "Tina Matt");
        sortableTable.setValue(19, 1, 49);
//		sortableTable.setValue(19, 2, new SimpleDate(1998, 9, 15));
        sortableTable.setValue(19, 2, Float.valueOf("189.64"));

        sortableTable.setValue(20, 0, "Roxanne Rocks");
        sortableTable.setValue(20, 1, 43);
//		sortableTable.setValue(20, 2, new SimpleDate(1992, 11, 12));
        sortableTable.setValue(20, 2, Float.valueOf("1209.73"));

        // In your application code, the following should be part
        // of the looop that adds the data to the Table.
        // Since we have some hard-coded data here, this is done
        // separately down here....
        // Please note that this is totally optional. This has nothing
        // to do with the Sorting capabilities of the widget.
        // This is just the cosmetics injection to the table.

        // Set Style Name for the header
        RowFormatter rowFormatter = sortableTable.getRowFormatter();
        rowFormatter.setStyleName(0, "tableHeader");

        // Set the Styles for the Data Rows and Columns
        CellFormatter cellFormatter = sortableTable.getCellFormatter();
        // Set the styles for the headers
        for (int colIndex = 0; colIndex < 4; colIndex++) {
            cellFormatter.setStyleName(0, colIndex, "headerStyle");
            cellFormatter.setAlignment(0, colIndex, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
        }

        for (int rowIndex = 1; rowIndex < 21; rowIndex++) {
            if (rowIndex % 2 == 0) {
                rowFormatter.setStyleName(rowIndex, "customRowStyle");
            } else {
                rowFormatter.setStyleName(rowIndex, "tableRow");
            }
            for (int colIndex = 0; colIndex < 4; colIndex++) {
                cellFormatter.setStyleName(rowIndex, colIndex, "customFont");
                if (colIndex == 1 || colIndex == 3) {
                    cellFormatter.setAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
                } else if (colIndex == 0) {
                    cellFormatter.setAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
                }
                if (colIndex == 2) {
                    cellFormatter.setAlignment(rowIndex, colIndex, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
                }
            }
        }

        // Add the table to the doc
//		RootPanel.get("slot1").add(sortableTable);
        return sortableTable;
    }
}
