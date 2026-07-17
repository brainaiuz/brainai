package com.edatasite.workforce.gwt.core.client.ui.preview;

import com.google.gwt.user.client.ui.*;


public class PreviewSectionContainer extends FlowPanel {
    int row;
    FlexTable table = new FlexTable();
    private HTMLTable.CellFormatter cellformatter;

    public PreviewSectionContainer() {
        row = 0;
        table.setCellPadding(0);
        table.setCellSpacing(0);
        setCellformatter(table.getCellFormatter());
//        table.setWidth("100%");
//        getCellformatter().setWidth(0, 0, "20%");
//        getCellformatter().setWidth(0, 1, "80%");
        table.setBorderWidth(0);
        table.addStyleName("previewSection-table");
        add(table);

    }

    public void addSection(PreviewSectionLabel label, PreviewSectionField field) {
        table.setHTML(row, 0, label.getHTML());
        cellformatter.setStylePrimaryName(row, 0, "previewSection-table__th");
        cellformatter.setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
        cellformatter.setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
        cellformatter.setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
        cellformatter.setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
        cellformatter.setStylePrimaryName(row, 1, "task-quickviewPanelCell previewSection-table__td");
        table.setWidget(row, 1, field);
        row++;
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        cellformatter.setStylePrimaryName(row, 0, "quickviewPanelDelimeterCell");
        row++;
    }

    public void addSection(PreviewSectionLabel label, PreviewSectionField field, PreviewSectionField field2) {
        cellformatter.setStylePrimaryName(row, 0, "padding10");
        cellformatter.setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
        cellformatter.setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
        cellformatter.setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
        cellformatter.setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
        cellformatter.setStylePrimaryName(row, 1, "task-quickviewPanelCell");
        cellformatter.setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
        cellformatter.setVerticalAlignment(row, 2, HasVerticalAlignment.ALIGN_TOP);
        cellformatter.setStylePrimaryName(row, 2, "task-quickviewPanelCell");
        table.setHTML(row, 0, label.getHTML());
        table.setWidget(row, 1, field);
        table.setWidget(row, 2, field2);
        row++;
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        cellformatter.setStylePrimaryName(row, 0, "quickviewPanelDelimeterCell");
        row++;
    }


    public void setDescriptionFieldWidth(String width) {
        getCellformatter().setWidth(0, 0, width);
    }

    public void setSummaryFieldWidth(String width) {
        getCellformatter().setWidth(0, 1, width);
    }

    public void setCellformatter(HTMLTable.CellFormatter cellformatter) {
        this.cellformatter = cellformatter;
    }

    public HTMLTable.CellFormatter getCellformatter() {
        return cellformatter;
    }
}
