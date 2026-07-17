package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.NiTaxChangesListItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 6:09:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiTaxChangesListView extends BaseListView {

    private ListingPanel<NiTaxChangesListItem> list;

    private String name;
    private Integer type;
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public NiTaxChangesListView(String name) {
        super("summary", name.equals("1") ? payrollStrings.categoryChanges() : payrollStrings.taxCodeChanges());
        this.name = name;
        this.type = Integer.valueOf(name);
    }


    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.NiTaxChangesListPanel, drawColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private ListingRequestProvider<NiTaxChangesListItem> getProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setType(type);
            PayrollService.App.get().getNiTaxChanges(filterParametrs, new AbstractAsyncCallback<ListResult<NiTaxChangesListItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<NiTaxChangesListItem> payeBandObjectListResult) {
                    callback.onSuccess(payeBandObjectListResult);
                }

            });
        };
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        columns[0] = new ColumnDefinitionConfig<NiTaxChangesListItem, String>(wfmStrings.employee(), NiTaxChangesListItem.EMPLOYEENAME, 200) {

            @Override
            public String getCellValue(NiTaxChangesListItem item) {
                return item.getEmployeeName();
            }
        };
        columns[1] = new ColumnDefinitionConfig<NiTaxChangesListItem, String>(wfmStrings.New() + " " + (name.equals("1") ? wfmStrings.niTable() : wfmStrings.code()), NiTaxChangesListItem.NEWCODE, 100) {

            @Override
            public String getCellValue(NiTaxChangesListItem item) {
                return item.getNewCode();
            }
        };
        columns[2] = new ColumnDefinitionConfig<NiTaxChangesListItem, String>(wfmStrings.oldW() + " " + (name.equals("1") ? wfmStrings.niTable() : wfmStrings.code()), NiTaxChangesListItem.OLDCODE, 100) {

            @Override
            public String getCellValue(NiTaxChangesListItem item) {
                return item.getOldCode();
            }
        };
        columns[3] = new ColumnDefinitionConfig<NiTaxChangesListItem, String>(wfmStrings.modifiedDate(), NiTaxChangesListItem.DATE, 100) {

            @Override
            public String getCellValue(NiTaxChangesListItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate()) : "";
            }
        };
        columns[4] = new ColumnDefinitionConfig<NiTaxChangesListItem, String>(payrollStrings.methodOfChange(), NiTaxChangesListItem.METHODOFCHANGE, 150) {

            @Override
            public String getCellValue(NiTaxChangesListItem item) {
                return item.getMethodOfChange() != null ? item.getMethodOfChange() : "";
            }
        };
        return columns;
    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlytThereArenNoNiCategoryChanges());
                emptyDataTable.initEmptyDataTable(message);
                list.getEmptyTable().getElement().getStyle().setMarginTop(20, com.google.gwt.dom.client.Style.Unit.PCT);
            }
        };
    }

    public String getIconStyle() {
        return "payroll tax-changes";
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

}
