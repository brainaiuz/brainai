package com.edatasite.workforce.gwt.payroll.client.ui.view.report.cashAdvanceReport;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CASH_ADVANCE_LIST;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/4/16
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceReportView extends FooteredView implements FittedContent {

    public CashAdvanceReportView() {
        super("cashAdvanceReport");
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
    }

    @Override
    protected Widget onInitialize() {
        CashAdvanceReport uiBinder = new CashAdvanceReport();
        Div formDiv = new Div("add-form");
        formDiv.add(uiBinder.getRootElement());
        formDiv.add(new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                List<Widget> widgets = new ArrayList<>();

                Div paginationDiv = new Div("frame__info-paging");
                paginationDiv.add(uiBinder.getPaginationPanel());
                widgets.add(paginationDiv);

                return widgets;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                List<Widget> widgets = new ArrayList<>();

                Div exportDiv = new Div();
                exportDiv.add(uiBinder.getExportButton());
                widgets.add(exportDiv);

                Div updateDiv = new Div();
                updateDiv.add(uiBinder.getUpdateButton());
                widgets.add(updateDiv);

                return widgets;
            }
        }));
        add(formDiv);
        return null;
    }

    @Override
    public String getIconStyle() {
        return "payroll efile-to-hmrc";
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

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
    public String getPropertyCode() {
        return CASH_ADVANCE_LIST;
    }
}
