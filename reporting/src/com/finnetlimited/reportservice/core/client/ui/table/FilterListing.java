package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 4/20/12
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterListing extends KpiModal {

    private UserFilterTableAddRow userFilter;
    private DRSButton applySubmit;
    private DRSButton resetChanges;
    private ListBox limit;
    private Image loader;

    public FilterListing(final ReportRpc report) {
        applySubmit = new DRSButton("<span>Apply</span>", "optBtn2");
        applySubmit.getElement().setAttribute("style", "margin-right:10px;");
        resetChanges = new DRSButton("<span>Reset</span>", "optBtn2");
        final FlowPanel applyButtonContent = new FlowPanel();

        applyButtonContent.add(applySubmit);
        applyButtonContent.add(resetChanges);

        limit = new ListBox();
        if (ReportType.SUMMARY.name().equals((report.getTableType()))) {
            limit.setEnabled(false);
        }
        limit.getElement().setAttribute("style", "float:right;margin-right:3px;width:50px;");
        limit.addItem("10", "10");
        limit.addItem("20", "20");
        limit.addItem("30", "30");
        limit.addItem("40", "40");
        limit.addItem("50", "50");
        limit.addItem("100", "100");
        limit.addItem("200", "200");
        limit.addItem("500", "500");
        limit.setSelectedIndex(2);
        limit.setEnabled(!report.getTableType().equals(ReportType.SUMMARY.name()));

        limit.addChangeHandler(event -> {
            String limitVal = limit.getValue(limit.getSelectedIndex());
            report.setLimit(Integer.parseInt(limitVal));
        });
        if (report.getLimit() > 0) {
            setLimitSelected(report.getLimit());
        }

        loader = new Image("/images/ajax-loader.gif");
        loader.getElement().setAttribute("style", "width:16px;");
        loader.setVisible(false);

        final FlowPanel pagingPanel = new FlowPanel();
        HTML showMe = new HTML("Rows per page: &nbsp;&nbsp; ");
        showMe.getElement().setAttribute("style", "float:right");

        pagingPanel.add(limit);
        pagingPanel.add(showMe);

        userFilter = new UserFilterTableAddRow(report);
        userFilter.getElement().setAttribute("style", "clear:both");
        add(pagingPanel);

        add(userFilter);

        final FlowPanel footerPanel = new FlowPanel();
        footerPanel.getElement().setAttribute("style", "text-align:center;");
        footerPanel.add(loader);
        footerPanel.add(applyButtonContent);
        add(footerPanel);
        //report.setSelectedColumns(columnRpcs);
    }

    public DRSButton getApplySubmit() {
        return this.applySubmit;
    }

    public DRSButton getResetChanges() {
        return this.resetChanges;
    }

    public UserFilterTableAddRow getUserFilter() {
        return userFilter;
    }

    public ListBox getLimit() {
        return limit;
    }

    public Image getLoader() {
        return loader;
    }

    public ReportRpc clearFilter(ReportRpc report) {
        report.setSett(new ArrayList<>());
        report.setFieldd(new LinkedList<>());
        report.setOperators(new ArrayList<>());
        report.setValues(new ArrayList<>());
        report.setBoolType(new ArrayList<>());
        report.setPromtList(new ArrayList<>());

        userFilter.clear();
        userFilter.addRow();

        return report;
    }

    private void setLimitSelected(int reportLimit) {
        for (Integer i = 0; i < limit.getItemCount(); i++) {
            Integer limitval = Integer.parseInt(limit.getValue(i));
            if (limitval == reportLimit) {
                limit.setSelectedIndex(i);
                break;
            }
        }
    }


}

