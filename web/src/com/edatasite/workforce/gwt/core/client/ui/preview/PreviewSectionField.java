package com.edatasite.workforce.gwt.core.client.ui.preview;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUploadItem;
import com.google.gwt.user.client.ui.*;

public class PreviewSectionField extends Composite {

    private int row;
    private int cellPadding = 10;
    private int cellSpacing = 10;
    private FlexTable table;
    private HTMLTable.CellFormatter cellFormatter;

    public PreviewSectionField() {
        initClassVariables("20%", "80%");
    }

    public PreviewSectionField(String leftWidth, String rightWidth) {
        initClassVariables(leftWidth, rightWidth);
    }

    public PreviewSectionField(String leftWidth, String rightWidth, int cellPadding, int cellSpacing) {
        this.cellPadding = cellPadding;
        this.cellSpacing = cellSpacing;
        initClassVariables(leftWidth, rightWidth);
    }

    private void initClassVariables(String leftWidth, String rightWidth) {
        table = new FlexTable();
        table.setCellPadding(cellPadding);
        table.setCellSpacing(cellSpacing);
        row = 0;
        cellFormatter = table.getCellFormatter();
        table.setWidth("100%");
        cellFormatter.setWidth(0, 0, leftWidth);
        cellFormatter.setWidth(0, 1, rightWidth);
        initWidget(table);
    }

    public void addField(String name, Widget value) {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b class=customTitle>");
            if (name != null) {
                sb.append(name);
                sb.append(":</b>");
            }
            table.setHTML(row, 0, sb.toString());
            table.setWidget(row, 1, value);
            cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
            cellFormatter.setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
            row++;
        }
    }

    public void addFileUploadField(String name, GeneralFileUploadItem value) {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b class=customTitle>");
            if (name != null) {
                sb.append(name);
                sb.append(":</b>");
            }
            table.setHTML(row, 0, sb.toString());
            table.setWidget(row, 1, value);
            cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
            cellFormatter.setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
            row++;
        }
    }

    public void addModifiedField(String name, Widget value) {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b class=customTitle>");
            sb.append(name);
            sb.append("</b>");
            table.setHTML(row, 0, sb.toString());
            table.setWidget(row, 1, value);
            cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            row++;
        }
    }

    public void addField(String name, Widget value, boolean isRequiredField) {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b class=customTitle>");
            sb.append(name);
            if (isRequiredField) {
                sb.append("<font color='red'>*</font>:</b>");
            } else {
                sb.append(":</b>");
            }
            table.setHTML(row, 0, sb.toString());
            table.setWidget(row, 1, value);
            cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            row++;
        }
    }

    public void addField(String name, Widget value, int step) {
        if (value != null) {

            table.setHTML(row, 0, name);
            table.setWidget(row, 1, value);
            cellFormatter.setAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            row = row + step;
        }
    }

    public void addField(String name, String value) {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b class=customTitle>");
            sb.append(name);
            sb.append(":</b>");
            table.setHTML(row, 0, sb.toString());
            table.setHTML(row, 1, value);
            cellFormatter.setAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
            cellFormatter.setHorizontalAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT());
            row++;
        }
    }

    public void addField(String name, String value, boolean isBlack) {
        if (value == null) {
            value = "";
        }

        table.setHTML(row, 0, name);
        table.setHTML(row, 1, value);
        cellFormatter.setAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
        row++;
    }

    public void addFieldB(String name, Widget value) {
        if (value == null) {
            value = new Widget();
        }

        table.setWidget(row, 0, new HTML(name));
        table.setWidget(row, 1, value);
        table.getCellFormatter().setWordWrap(row, 1, true);
        cellFormatter.setAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setAlignment(row, 1, Utils.HORIZONTAL_ALIGNMENT_LEFT(), HasVerticalAlignment.ALIGN_TOP);
        row++;
    }

    public void addSpace() {
        table.setHTML(row, 0, "&nbsp;");
        table.setHTML(row, 1, "&nbsp;");
        row++;
    }

    public void addWidget(Widget widget) {
        table.setWidget(row, 0, widget);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        row++;
    }

    public void addWidget(Widget widget, String width) {
        table.setWidget(row, 0, widget);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        cellFormatter.setWidth(row, 0, width);
        row++;
    }

    public void addFieldName(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b class=customTitle><font size=\"2\">");
        sb.append(name);
        sb.append(":</font></b>");
        table.setHTML(row, 0, sb.toString());
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        cellFormatter.setHorizontalAlignment(row, 0, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        row++;
    }
}