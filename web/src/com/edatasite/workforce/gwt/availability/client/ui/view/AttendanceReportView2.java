package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.landing.WestPanelHelp;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Omonullo on 3/15/2017.
 */
public class AttendanceReportView2 extends View implements FittedContent, Colapse {

    private final static HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private String brigadaIds;
    private String shiftPeriod;

    public AttendanceReportView2() {
        super("attendanceReport2");
        setDescription(property.getPlural(hrmsStrings.attendanceReport()));
    }

    public AttendanceReportView2(String brigadaIds, String period) {
        super("attendanceReport2");
        setDescription(property.getPlural(hrmsStrings.attendanceReport()));
        this.shiftPeriod = period;
        this.brigadaIds = brigadaIds;
    }


    @Override
    protected Widget onInitialize() {
        if (brigadaIds != null) {
            add(new AttendanceReport(brigadaIds, shiftPeriod));
        } else {
            add(new AttendanceReport());
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return "availability team-availability";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-reporting-filters-panel");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-reporting-filters-panel");
    }

    @Override
    public FlowPanel getHelpContainer() {
        final WestPanelHelp westPanel = new WestPanelHelp("<b>" + wfmStrings.leaveType() + "</b>");
        String sb = ("<div>\n" +
                "  <div class=\"lr-holiday-cell\" style=\"width: 25px;\">" + wfmStrings.holidayLetter() + "</div>\n" +
                "  <div class=\"lr-holiday-explanation\"> - " + wfmStrings.holiday() + "</div>\n" +
                "</div>\n" +
                "<div>\n" +
                "  <div class=\"lr-not-approved-cell\" style=\"width:25px;\">" + hrmsStrings.leaveRequestLetter() + "</div>\n" +
                "  <div class=\"not-approved-cell-explanation\"> - " + hrmsStrings.notApprovedLRCellDefinition() + "</div>\n" +
                "</div>\n") +
                "<div>\n" +
                "  <div class=\"absent-leave-cell\" style=\"width:25px;\">" + wfmStrings.absentLetter() + "</div>\n" +
                "  <div class=\"absent-leave-cell-explanation\"> - " + hrmsStrings.absentLeave() + "</div>\n" +
                "</div>\n";
        final HTML html = new HTML(sb);
        westPanel.addHtmlLine(html);
        final FlowPanel wc = new FlowPanel();
        final VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(3);
        vp.add(westPanel);
        vp.setWidth("100%");
        wc.add(vp);
        return wc;
    }

    public String getPropertyCode() {
        return "attendanceReport";
    }

}
