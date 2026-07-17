package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.NotificationMsgService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WaitingForApprovalWidget extends DashboardBaseWidget {
    private Div listContent;
    private DataListBox updateTypes;
    private Span loading;
    private WfmButton2 loadMore;
    private int step = 0;
    private int limit = 20;

    public WaitingForApprovalWidget(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
        Div wrapper = new Div("gwt-wrapper");
        listContent = new Div("widget-content widget-list");
        /*
        <span class="blue widget-loading--svg widget-loading" style="visibility: hidden;"></span>
		<button class="btn btn-lg btn-block text-center">Load More</button>
         */
        loading = new Span();
        loading.setVisible(false);
        loading.addStyleName("blue widget-loading--svg widget-loading");

        loadMore = new WfmButton2(wfmStrings.loadMore(), "btn btn-lg btn-block text-center", (event) -> {
            step++;
            loadData();
        });
    }

    @Override
    protected void initInternal() {
        Heading h3 = new Heading(HeadingSize.H3);
        h3.setText(wfmStrings.waitingForApproval());
        title.add(h3);
        updateTypes = new DataListBox();
        filterPanel.add(updateTypes);
        Div footer = new Div("widget-footer");
        footer.add(loading);
        footer.add(loadMore);
        mainPanel.add(footer);
        contentPanel.add(listContent);
        NotificationMsgService.App.get().getCategoriesList(false, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                ArrayList<Integer> types = new ArrayList<>();
                types.add(1); // Leave requests= Leave Requests
                types.add(2); // Timesheet approvals= Timesheet Approvals
                types.add(3); // Expense claims= Expense Claims
                types.add(7); // Benefit requests= Benefit Requests
                types.add(19); // Purchase Order
                types.add(22); // Cash Advance
                types.add(23); // Onboarding Step
                types.add(24); // Sales Quote
                result = Arrays.stream(result).filter(o -> types.contains(o.getId())).collect(Collectors.toList()).toArray(new SelectItem[]{}); // T2567 filter
                updateTypes.setItems(result);
                updateTypes.addValueChangeHandler((event) -> {
                    step = 0;
                    loadData();
                });
            }
        });
    }

    @Override
    protected void getData() {
        step = 0;
        updateTypes.setSelectedNullLabel();
        loadData();
    }

    private void loadData() {
        loading.setVisible(true);
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setCategoryID(updateTypes.getSelectedId());
        filter.setStart(step * limit);
        filter.setLimit(step);
        filter.setAvoidType("APPROVED");
        NotificationMsgService.App.get().getNotificationsList(filter, new AsyncCallback<ListResult<NotificationItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                loading.setVisible(false);
            }

            @Override
            public void onSuccess(ListResult<NotificationItem> result) {
                loading.setVisible(false);
                if (step == 0) {
                    listContent.clear();
                }
                for (NotificationItem item : result.getList()) {
                    listContent.add(new LineItem(item));
                }
            }
        });
    }


    @Override
    protected void getSampleData(boolean nodata) {

    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.WAITING_FOR_APPROVAL;
    }

    private class LineItem extends Div {
        public LineItem(NotificationItem item) {
            super("widget-row");
            getElement().getStyle().setCursor(Style.Cursor.POINTER);
            Div lDiv = new Div("widget-row__task-num");
            DL ll = new DL();
            DT lt = new DT();
            DD ld = new DD();
            lt.setText(item.getValue());
            ld.setText(item.getUniqueVal() != null ? item.getUniqueVal() : "");
            ll.add(lt);
            ll.add(ld);
            lDiv.add(ll);
            add(lDiv);

            Div rDiv = new Div("widget-row__text");
            DL rl = new DL();
            rl.addStyleName("widget-row__text-dl");
            DT rt = new DT();
            rt.setText(item.getActorUserName());
            DD rd = new DD();
            rl.add(rt);
            rd.setText(item.getName());
            rl.add(rd);
            rDiv.add(rl);
            add(rDiv);
            addClickHandler(event -> {
                Window.open(GWT.getHostPageBaseURL() + item.getActionUrl(), "_blank", "");
            });
        }
    }

}
