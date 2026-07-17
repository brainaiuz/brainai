package com.edatasite.workforce.gwt.project.client.ui.view.projectbudget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/25/12
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class HeaderTable extends FlexTable {
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static DateTimeFormat dateFormatForShow = DateTimeFormat.getFormat("MMM yyyy");

    public HeaderTable(ArrayList<DateNonConvertable[]> monthIntervalsList) {
        super();
        initialize(monthIntervalsList);
    }

    private void initialize(ArrayList<DateNonConvertable[]> monthIntervalsList) {
        setCellSpacing(0);
        setCellPadding(0);

        this.setWidget(0, 0, new HTML("&nbsp;"));
        this.setWidget(1, 0, new HTML("&nbsp;"));
        this.setWidget(1, 1, new HTML("&nbsp;"));
        this.getFlexCellFormatter().setColSpan(0, 0, 2);

        int i = 1;
        for (DateNonConvertable[] monthInterval : monthIntervalsList) {
            addHeaderColumn(this, dateFormatForShow.format(monthInterval[0].getNonConvertedDate()), wfmStrings.budget(), wfmStrings.actual(), i, false);
            i++;
        }
        addHeaderColumn(this, wfmStrings.total(), wfmStrings.budget(), wfmStrings.actual(), i, true);
        i++;
        addHeaderColumn(this, projectStrings.variancePercent(), wfmStrings.difference(), projectStrings.differenceWithPercent(), i, true);
        i++;

        this.setWidget(0, i, new HTML("&nbsp;"));
        this.setWidget(1, i * 2, new HTML("&nbsp;"));
        this.getFlexCellFormatter().setColSpan(0, i, 2);
        this.getFlexCellFormatter().setColSpan(1, i * 2, 2);

        this.getFlexCellFormatter().getElement(0, 0).getStyle().setWidth(300, Style.Unit.PX);
        this.getFlexCellFormatter().getElement(1, 0).getStyle().setWidth(150, Style.Unit.PX);
        this.getFlexCellFormatter().getElement(1, 1).getStyle().setWidth(150, Style.Unit.PX);

        this.getFlexCellFormatter().getElement(0, i).getStyle().setWidth(45, Style.Unit.PX);
        this.getFlexCellFormatter().getElement(1, i * 2).getStyle().setWidth(45, Style.Unit.PX);

        getElement().setAttribute("class", "advanced-Grid  grid-columns reachFullWidthFields file--HeaderTable");
        getElement().setAttribute("style", "table-layout: fixed;border-collapse:collapse;height:40px;");
    }

    private void addHeaderColumn(FlexTable flexTable, String title, String subtitle1, String subtitle2, int i, boolean isTotal) {
        HTML titleHTML = new HTML(title);
        titleHTML.setStyleName("text-center");
        HTML subtitle1HTML = new HTML(subtitle1);
        subtitle1HTML.setStyleName("text-center");
        HTML subtitle2HTML = new HTML(subtitle2);
        subtitle2HTML.setStyleName("text-center");
        flexTable.setWidget(0, i, titleHTML);
        flexTable.setWidget(1, i * 2, subtitle1HTML);
        flexTable.setWidget(1, i * 2 + 1, subtitle2HTML);
        flexTable.getFlexCellFormatter().setColSpan(0, i, 2);
        flexTable.getFlexCellFormatter().setWidth(0, i, "200px");
        flexTable.getFlexCellFormatter().setWidth(1, i * 2, "100px");
        flexTable.getFlexCellFormatter().setWidth(1, i * 2 + 1, "100px");

        flexTable.getFlexCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(1, i * 2, HasHorizontalAlignment.ALIGN_CENTER);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(1, i * 2 + 1, HasHorizontalAlignment.ALIGN_CENTER);

        if (isTotal) {
            titleHTML.getElement().setAttribute("style", "font-weight:bold;");
            flexTable.getFlexCellFormatter().getElement(0, i).setAttribute("style",
                    "padding:7px 0;background-color:#d0edcc;border:1px solid white;text-transform:uppercase;color:#2b6d24;");
            flexTable.getFlexCellFormatter().getElement(1, i * 2).setAttribute("style",
                    "background-color:#3e851f;color:#fff;padding:7px 0;border-right:1px solid white;border-top:3px solid white;border-bottom:3px solid white;");
            flexTable.getFlexCellFormatter().getElement(1, i * 2 + 1).setAttribute("style",
                    "background-color:#3e851f;color:#fff;padding:7px 0;border-right:1px solid white;border-top:3px solid white;border-bottom:3px solid white;");
        } else {
            flexTable.getFlexCellFormatter().getElement(0, i).setAttribute("style", "padding:7px 0;background-color:#f0f0f0;border:1px solid white;");
            flexTable.getFlexCellFormatter().getElement(1, i * 2).setAttribute("style",
                    "background-color:#244f6d;color:#fff;padding:7px 0;border-right:1px solid white;border-top:3px solid white;border-bottom:3px solid white;");
            flexTable.getFlexCellFormatter().getElement(1, i * 2 + 1).setAttribute("style",
                    "background-color:#244f6d;color:#fff;padding:7px 0;border-right:1px solid white;border-top:3px solid white;border-bottom:3px solid white;");
        }
    }
}
