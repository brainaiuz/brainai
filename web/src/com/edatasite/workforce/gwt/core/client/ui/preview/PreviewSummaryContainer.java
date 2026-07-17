/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/27 2:8:35                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui.preview;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 11-Feb-2010
 * Time: 19:06:47
 * To change this template use File | Settings | File Templates.
 */
public class PreviewSummaryContainer extends FlexTable {

    private int row = 0;
    private int cell = 0;
    private int max = 0;
    private CellFormatter cellFormatter;
    final static WfmStrings wfmStrings = WfmStrings.App.get();

    private boolean isFirstClicked = true;
    private String summaryViewType;
    private Command showViewCommand;

    public PreviewSummaryContainer(String[] columns) {
        super();
        this.setWidth("100%");
        assert columns != null : "This table columns width null ";
        for (int i = 0; i < columns.length; i++) {
            getCellFormatter().setWidth(0, i, columns[i]);
        }
        initialization();
    }

    public PreviewSummaryContainer() {
        super();
        this.setWidth("100%");
        initialization();
        setStyleName("task-quickviewPanelCell file--PreviewSummaryContainer");
    }

    public PreviewSummaryContainer(String style) {
        super();
        this.setWidth("100%");
        initialization();
        setStyleName(style);
    }

    public PreviewSummaryContainer(String style, Command showViewCommand) {
        super();
        this.setWidth("100%");
        initialization();
        setStyleName(style);
        this.showViewCommand = showViewCommand;
    }

    private void initialization() {
        cellFormatter = getCellFormatter();
        this.setCellPadding(0);
        this.setCellSpacing(0);
        this.setBorderWidth(0);
    }

    public void addCell(Widget widget) {
        this.setWidget(row, cell, widget);
//        cellFormatter.setStyleName(row, cell, "task-quickviewPanelCell");
        cellFormatter.setHorizontalAlignment(row, cell, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        cellFormatter.setVerticalAlignment(row, cell++, VerticalPanel.ALIGN_TOP);
        if (max < cell) {
            max = cell;
        }
    }

    public void addCellNewRow(Widget widget) {
        row++;
        cell = 0;
        this.setWidget(row, cell, widget);
//        cellFormatter.setStyleName(row, cell, "task-quickviewPanelCell");
        cellFormatter.setHorizontalAlignment(row, cell, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        cellFormatter.setVerticalAlignment(row, cell++, VerticalPanel.ALIGN_TOP);
    }

    private HorizontalPanel getOtherCollapser(boolean collapsed, Image collapseImage, Image expandImage, HTML label) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(1);
        panel.add(collapsed ? collapseImage : expandImage);
        panel.setCellHorizontalAlignment(collapsed ? collapseImage : expandImage, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.setCellWidth(collapsed ? collapseImage : expandImage, "23px");
        panel.add(label);
        panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
        return panel;
    }

    public void addCellNewRow(Widget widget, final boolean collapsed) {
        final Image collapseImage = new Image("mainStyles/images/default/icons/collapseIcon1.png");
        final Image expandImage = new Image("mainStyles/images/default/icons/collapseIcon2.png");
        collapseImage.setWidth("14px");
        collapseImage.setHeight("14px");
        expandImage.setWidth("14px");
        expandImage.setHeight("14px");
        final HTML otherInformation = new HTML("<b>&nbsp;" + wfmStrings.additionalInformation() + "</b>");

        row++;
        cell = 0;
        this.getFlexCellFormatter().setColSpan(row, cell, max);
        this.setWidget(row, cell, getOtherCollapser(collapsed, collapseImage, expandImage, otherInformation));
        addListeners(row, cell, collapsed, collapseImage, expandImage, otherInformation);
        changeImage(row, cell, collapsed);
        this.getCellFormatter().setHorizontalAlignment(row, cell, HasHorizontalAlignment.ALIGN_LEFT);
        this.setWidget(++row, cell, widget);
//        cellFormatter.setStyleName(row, cell, "task-quickviewPanelCell");
        cellFormatter.setHorizontalAlignment(row, cell, Utils.HORIZONTAL_ALIGNMENT_LEFT());
        cellFormatter.setVerticalAlignment(row, cell++, VerticalPanel.ALIGN_TOP);
    }

    private void addListeners(final int row1, final int cell1, final boolean collapsed, final Image collapseImage, final Image expandImage, HTML lable) {
        collapseImage.addClickHandler(event -> changeImage(row1, cell1, !collapsed));
        lable.addClickHandler(event -> changeImage(row1, cell1, !collapsed));
        expandImage.addClickHandler(event -> changeImage(row1, cell1, !collapsed));
    }

    private void changeImage(int row, int cell, boolean collapsed/*, final Image collapseImage, final Image expandImage*/) {
        final Image collapseImage = new Image("/mainStyles/images/default/icons/collapseIcon1.png");
        final Image expandImage = new Image("/mainStyles/images/default/icons/collapseIcon2.png");
        collapseImage.setWidth("14px");
        collapseImage.setHeight("14px");
        expandImage.setWidth("14px");
        expandImage.setHeight("14px");
        final HTML otherInformation = new HTML("<b>" + wfmStrings.additionalInformation() + "</b>");
        this.setWidget(row, cell, getOtherCollapser(collapsed, collapseImage, expandImage, otherInformation));
        this.getFlexCellFormatter().setVisible(row + 1, 0, !collapsed);
        addListeners(row, cell, collapsed, collapseImage, expandImage, otherInformation);
        if (isFirstClicked && !collapsed && showViewCommand != null) {
            isFirstClicked = false;
            showViewCommand.execute();
        }
    }

    public void drawLine() {
        row++;
        cell = 0;
        this.getFlexCellFormatter().setColSpan(row, cell, max);
        this.setHTML(row, 0, "<hr style='border-bottom: 1px solid #d9d9d9;border-top:0;border-right:0;border-left:0;width:98%;'/>");
//        cellFormatter.setStyleName(row, 0, "task-quickviewPanelCell");
        row++;
    }

    public void noneLine() {
        row++;
        cell = 0;
        this.getFlexCellFormatter().setColSpan(row, cell, max);
        row++;
    }

    public int getRow() {
        return row;
    }

    public boolean isFirstClicked() {
        return isFirstClicked;
    }

    public void setFirstClicked(boolean firstClicked) {
        isFirstClicked = firstClicked;
    }

    public String getSummaryViewType() {
        return summaryViewType;
    }

    public void setSummaryViewType(String summaryViewType) {
        this.summaryViewType = summaryViewType;
    }
}
