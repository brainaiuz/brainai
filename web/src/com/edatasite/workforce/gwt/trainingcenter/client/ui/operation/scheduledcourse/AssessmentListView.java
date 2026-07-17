package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.AssessmentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

/**
* Created by IntelliJ IDEA.
* User: Abdullo
* Date: 17.09.12
* Time: 22:07
*/
public class AssessmentListView extends BaseListView implements TCConstants {

    private ListingPanel<AssessmentItem> listingPanel;
    private Integer id;

    public AssessmentListView(Integer id) {
        super("assessment", "Assessment List");
        this.id = id;
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.AssessmentListPanel, getColumns(), getProvider(), getDesign());

        add(listingPanel);
        return null;
    }

    @Override
    public String getIconStyle() {
        return "bgMark assessment-list-icon";
    }


    public CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[3];
        columns[0] = new ColumnDefinitionConfig<AssessmentItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            public Anchor getCellValue(final AssessmentItem rowValue) {
                return new Anchor();
            }

        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<AssessmentItem, SimpleLink>(wfmStrings.name(), AssessmentItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(AssessmentItem rowValue) {
                return new SimpleLink(rowValue.getName(), (TC_ASSESSMENT_VIEW + "|summary/" + rowValue.getObjectId() + "/" + rowValue.getStudentQuestionaireId()));
            }
        };
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<AssessmentItem, String>(wfmStrings.total() + " Points", AssessmentItem.TOTAL_POINTS, 100) {
            @Override
            public String getCellValue(AssessmentItem rowValue) {
                return rowValue.getTotalPoints();
            }
        };
        columns[2].setMinimumColumnWidth(100);

        return columns;
    }

    public ListingRequestProvider<AssessmentItem> getProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setObjectId(id);
            TCService.App.get().getAssessmentList(filterParametrs, new AbstractAsyncCallback<ListResult<AssessmentItem>>(){
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ListResult<AssessmentItem> result) {
                    callback.onSuccess(result);
                }
            });

        };

    }

    public ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton more = new ActionButton("Import XML file");
                more.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("xmlimport|add/add"));
                return more;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }
/*
            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNewPlacement = new ActionButton(tcStrings.newStudnt(), ActionButton.Type.BUTTON);
                addNewPlacement.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDE + "|add/add");
                    }
                });
                return addNewPlacement;
            }
*/

        };

    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
