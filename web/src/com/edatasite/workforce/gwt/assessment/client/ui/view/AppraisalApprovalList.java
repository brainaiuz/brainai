package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.DepartmentPeriodAppraisalItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class AppraisalApprovalList extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<DepartmentPeriodAppraisalItem> list;

    public AppraisalApprovalList() {
        super(PA_APPROVAL_LIST, hrmsStrings.appraisalApproval());
    }

    @Override
    public String getIconStyle() {
        return "assessment assessment-archive";
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        list = new ListingPanel<>(ListPanelType.AppraisalApprovalListPanel, getColumnsConfig(), getRequestProvider(), getDesign());
        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PERIOD_APPRAISAL_CHANGED, AppraisalApprovalList.this, (sender, args) -> list.reloadPage());
        return null;
    }

    private Anchor getActionMenuItems(final DepartmentPeriodAppraisalItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);

        final MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("appraisal_approval|summary/" + item.getObjectId()));
        actionItemCount++;
        menuBar.addItem(summary);

        final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        if (actionItemCount == 0) {
            return null;
        }
        return toolItem.getAction();
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return 0;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel hp = new HorizontalPanel();
                final DataListBox periodBox = new DataListBox();
                periodBox.setWidth("150px");
                CommonService.App.get().getTeamList(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        periodBox.setItems(result);
                    }
                });

                periodBox.addValueChangeHandler(event -> {
                    list.getFilterParametrs().setDepartmentId(periodBox.getSelectedId());
                    list.reloadPage();
                });
                hp.add(periodBox);
                return hp;
            }
        };
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        int columnSize = 0;
        //action
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final DepartmentPeriodAppraisalItem item) {
                return getActionMenuItems(item);
            }
        };
        columns[columnSize].setColumnSortable(false);
        columns[columnSize].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[columnSize++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //department name
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), DepartmentPeriodAppraisalItem.DEPARTMENT_NAME, 130) {
            @Override
            public String getCellValue(DepartmentPeriodAppraisalItem item) {
                return item.getDepartmentName();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(100);
        //department leader name
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, String>(wfmStrings.leader(), DepartmentPeriodAppraisalItem.DEPARTMENT_LEADER, 130) {
            @Override
            public String getCellValue(DepartmentPeriodAppraisalItem item) {
                return item.getDepartmentLeaderName();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(100);
        //appraisal cycle (period)
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, String>(wfmStrings.appraisalCycle(), DepartmentPeriodAppraisalItem.VALIDITY_PERIOD, 100) {
            @Override
            public String getCellValue(DepartmentPeriodAppraisalItem item) {
                return item.getValidityPeriodItem().getName();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(90);
        //date for sent approval
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, String>(wfmStrings.deleteForSentApprovel(), DepartmentPeriodAppraisalItem.DATE, 100) {
            @Override
            public String getCellValue(DepartmentPeriodAppraisalItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns[columnSize++].setMinimumColumnWidth(80);
        //status name
        columns[columnSize] = new ColumnDefinitionConfig<DepartmentPeriodAppraisalItem, HTML>(wfmStrings.status(), DepartmentPeriodAppraisalItem.STATUS, 100) {
            @Override
            public HTML getCellValue(DepartmentPeriodAppraisalItem item) {
                HTML statusHTML = new HTML(item.getStatusName());
                if (DepartmentPeriodAppraisalItem.PERIOD_REJECTED.equals(item.getStatusCode()) &&
                        item.getRejectionReasonComment() != null && !"".equals(item.getRejectionReasonComment())) {
                    statusHTML.setTitle(item.getRejectionReasonComment());
                }
                return statusHTML;
            }
        };
        columns[columnSize++].setMinimumColumnWidth(80);

        return columns;
    }

    private ListingRequestProvider<DepartmentPeriodAppraisalItem> getRequestProvider() {
        return (filterParametrs, callback) -> {
            String[] statuses = {DepartmentPeriodAppraisalItem.PERIOD_SENT_FOR_APPROVAL, DepartmentPeriodAppraisalItem.PERIOD_APPROVED, DepartmentPeriodAppraisalItem.PERIOD_REJECTED};
            filterParametrs.setStatusCodes(statuses);
            AssessmentService.App.get().getDepartmentPeriodAppraisalItems(filterParametrs, new AsyncCallback<ListResult<DepartmentPeriodAppraisalItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<DepartmentPeriodAppraisalItem> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}