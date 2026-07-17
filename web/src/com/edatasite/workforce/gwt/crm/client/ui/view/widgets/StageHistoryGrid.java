package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 11.04.12
 * Time: 12:27
 */

public class StageHistoryGrid extends AbstractDataGrid<OpportunityListItem> {
    private Integer opportunityID;
    private ColumnConfigs[] columnConfigs;
    private static NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    public StageHistoryGrid(Integer opportunityID, ColumnConfigs[] columnConfigs) {
        super();
        this.opportunityID = opportunityID;
        this.columnConfigs = columnConfigs;
        initialize();
    }

    @Override
    protected void addColums() {
        if (columnConfigs != null && columnConfigs.length > 0) {
            for (ColumnConfigs columnConfig : columnConfigs) {

                if ("STAGE".equals(columnConfig.getCode())) {
                    //stage
                    Column stage = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            if (item.getProbability() != null && item.getProbability() == 0 && item.getRejectionReason() != null) {
                                return refactor(item.getStageName() + " (" + item.getRejectionReason().getName() + ")");
                            } else {
                                return refactor(item.getStageName());
                            }
                        }
                    };
                    setColumnWidth(stage, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(stage, wfmStrings.stage());
                } else if ("NOTE".equals(columnConfig.getCode())) {
                    //note
                    Column note = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return refactor(item.getNote());

                        }
                    };
                    setColumnWidth(note, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(note, wfmStrings.note());
                } else if ("AMOUNT".equals(columnConfig.getCode())) {
                    //AMOUNT
                    Column amount = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return refactor(item.getAmount() != null ? numberFormat.format(item.getAmount().doubleValue()) : "");
                        }
                    };
                    setColumnWidth(amount, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(amount, wfmStrings.amount());

                } else if ("PROBABILITY".equals(columnConfig.getCode())) {
                    //Probability
                    Column probability = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return refactor(item.getProbability() == null ? "" : "" + item.getProbability() + "%");
                        }
                    };
                    setColumnWidth(probability, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(probability, wfmStrings.probability());
                } else if ("EXPECTED_REVENUE".equals(columnConfig.getCode())) {
                    //expectedRevenue
                    Column expectedRevenue = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return refactor(item.getExpectedRevenue() != null ? numberFormat.format(item.getExpectedRevenue().doubleValue()) : "");
                        }
                    };
                    setColumnWidth(expectedRevenue, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(expectedRevenue, wfmStrings.expectedRevenue());

                } else if ("MODIFIED_BY".equals(columnConfig.getCode())) {
                    //last modified
                    Column lastModified = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return refactor(item.getAuditInfoResource() != null ? item.getAuditInfoResource().getModifiedBy().getFullName() : "");
                        }
                    };
                    setColumnWidth(lastModified, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(lastModified, wfmStrings.modifiedBy());

                } else if ("MODIFIED_DATE".equals(columnConfig.getCode())) {
                    //last modified date
                    Column lastModifiedDate = new Column<OpportunityListItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityListItem item) {
                            return DateUtils.formatInternal(item.getAuditInfoResource().getModificationDate());
                        }
                    };
                    setColumnWidth(lastModifiedDate, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                    addColumn(lastModifiedDate, wfmStrings.modifiedDate());
                }
            }
        }

    }

    @Override
    public void refresher() {
        CRMService.App.get().getSubOpportunities(opportunityID, new AbstractAsyncCallback<ArrayList<OpportunityListItem>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<OpportunityListItem> result) {
                if (result.size() > 0) {
                    supplyProvider(result.toArray(new OpportunityListItem[]{}));
                    reDrawItems();
                }
            }
        });

    }

    public String refactor(String s) {
        if (s != null) {
            return s;
        }
        return "";
    }
}
