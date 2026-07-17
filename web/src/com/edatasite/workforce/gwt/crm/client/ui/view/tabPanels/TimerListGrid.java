package com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.wfmTimer.client.rpc.ClockService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;

import java.util.Date;

/**
 * User: Hayot
 * Date: Apr 10, 2010
 * Time: 4:49:32 PM
 */
public class TimerListGrid extends AbstractDataGrid<ClockItem> {

    private Integer caseID;
    public TimerListGrid(Integer caseID) {
        super();
        this.caseID = caseID;
        initialize();
    }


    @Override
    protected void addColums() {

        //owner
        Column<ClockItem, String> owner = new Column<ClockItem, String>(new TextCell()) {
            @Override
            public String getValue(final ClockItem item) {
                return item.getOwnerName();
            }
        };
        addColumn(owner, wfmStrings.employee());
        setColumnWidth(owner, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        //subject
        Column<ClockItem, String> subject = new Column<ClockItem, String>(new TextCell()) {
            @Override
            public String getValue(final ClockItem item) {
                return item.getComment() != null ? item.getComment() : "";
            }
        };
        addColumn(subject, wfmStrings.comment());
        setColumnWidth(subject, 40, com.google.gwt.dom.client.Style.Unit.PCT);

          //startDateField
        Column<ClockItem, SafeHtml> dateField = new Column<ClockItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ClockItem item) {
                return (SafeHtml) () -> "<span>" + (DateUtils.formatInternal(item.getStartDate())) + "</span>";
            }
        };
        addColumn(dateField, wfmStrings.date());
        setColumnWidth(dateField, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //spend time
        Column<ClockItem, SafeHtml> spendField = new Column<ClockItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ClockItem item) {
                return (SafeHtml) () -> {
                    DateTimeFormat timerFormat = DateTimeFormat.getFormat("HH:mm:ss");
                    Date timerDate = new Date();
                    timerDate.setHours(0);
                    timerDate.setMinutes(0);
                    timerDate.setSeconds(0);
                    timerDate.setSeconds(item.getElapsedTime() != null ? item.getElapsedTime() : 0);
                    return "<span>" + timerFormat.format(timerDate)+ "</span>";
                };
            }
        };
        addColumn(spendField, wfmStrings.timeSpentOnly());
        setColumnWidth(spendField, 20, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    @Override
    public void refresher() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationID(caseID);
        ClockService.App.get().getCaseClocks(caseID, Constants.CRM_CASE, new AbstractAsyncCallback<ListResult<ClockItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(final ListResult<ClockItem> result) {
                if (result != null && result.getList() != null && result.getList().size() > 0) {
                    supplyProvider(result.getList().toArray(new ClockItem[]{}));
                    reDrawItems();
                }
            }
        });
    }
}