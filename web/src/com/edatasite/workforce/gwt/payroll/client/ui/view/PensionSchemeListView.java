package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionSchemeData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Georgiy
 * Date: 27.10.11
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemeListView extends BaseListView implements Constants {
    private ListingPanel<PensionSchemeData> list;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    public PensionSchemeListView() {
        super("pensionscheme", payrollStrings.pensionSchemes());

    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PensionSchemeListPanel, drawColumns(), getProvider(), getDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_PENSION_SCHEME, PensionSchemeListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[9];
        int index = 0;
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PensionSchemeData item) {
                return getButtonForRow(item);

            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, String>(payrollStrings.schemeName(), "name", 70) {
            @Override
            public String getCellValue(PensionSchemeData item) {

                return item.getSchemeName();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, String>(payrollStrings.schemeType(), "type", 70) {
            @Override
            public String getCellValue(PensionSchemeData item) {

                return item.getSchemeTypeName();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, Widget>(payrollStrings.employeeContribution(), "employeeContribution", 140) {
            @Override
            public Widget getCellValue(PensionSchemeData item) {
                Label label = new Label(item.getEmployeeContribution());
                label.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                return label;
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, String>(payrollStrings.deductionFrom(), "deductionFrom", 100) {
            @Override
            public String getCellValue(PensionSchemeData item) {

                return (item.getDeductFrom() != 0) ? payrollStrings.deductFromNetPay() : payrollStrings.deductFromGrossPay();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, Widget>(payrollStrings.allowTaxRelief(), "allowTaxRelief", 140) {
            @Override
            public Widget getCellValue(PensionSchemeData item) {
                Label label = new Label((item.getAllowTaxRelief() != 0) ? "No" : "Yes");
                label.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                return label;
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, Widget>(payrollStrings.reduceByBasicRateTax(), "reduceByBRT", 140) {
            @Override
            public Widget getCellValue(PensionSchemeData item) {
                Label label = new Label((item.getReduceByBasicRateTax() != 0) ? "No" : "Yes");
                label.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                return label;

            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, String>(payrollStrings.wagesInsufficientToCover(), "wagesInsufficient", 250) {
            @Override
            public String getCellValue(PensionSchemeData item) {
                return getWagesInsufficientNameById(item);
            }
        };
        columns[index++].setMinimumColumnWidth(200);
        columns[index] = new ColumnDefinitionConfig<PensionSchemeData, Widget>(wfmStrings.employerContribution(), "employerContribution", 140) {
            @Override
            public Widget getCellValue(PensionSchemeData item) {
                Label label = new Label(item.getEmployerContribution());
                label.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                return label;
            }
        };
        columns[index].setMinimumColumnWidth(70);
        columns[index++].setShow(false);
        return columns;
    }

    public ListingRequestProvider<PensionSchemeData> getProvider() {
        return (filterParametrs, callback) -> PayrollService.App.get().getPensionSchemeList(filterParametrs, new AsyncCallback<ListResult<PensionSchemeData>>() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(ListResult<PensionSchemeData> pensionSchemeData) {
                callback.onSuccess(pensionSchemeData);
            }
        });
    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("pensionscheme|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoPayroll());/?????
                //emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private Anchor getButtonForRow(final PensionSchemeData item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        MenuPopItem pensionSummary = new MenuPopItem(payrollStrings.pensionSchemeDetails(), "icon-tasks");
        pensionSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("pensionscheme|view/" + item.getObjectId()));
        actionItemCount++;
        menuBar.addItem(pensionSummary);
        MenuPopItem pensionEdit = new MenuPopItem(wfmStrings.edit() + " " + wfmStrings.pensionScheme(), "icon-employee-edit-profile");
        pensionEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("pensionscheme|add/add/" + item.getObjectId()));
        actionItemCount++;
        menuBar.addItem(pensionEdit);
        MenuPopItem pensionDelete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
        pensionDelete.setCommand(() -> {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.confirmationMessage());
            message.setMessage(wfmMessages.sureYouWantToDelete(" \"" + item.getSchemeName() + "\" ", wfmStrings.pensionScheme() + "?"));
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    deletePensionScheme(item.getObjectId());
                }
            });
            message.open();
        });
        actionItemCount++;
        menuBar.addItem(pensionDelete);
        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private void deletePensionScheme(Integer id) {
        PayrollService.App.get().deletePensionScheme(id, new AsyncCallback<Void>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(Void res) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.pensionScheme()), Info.Type.INFO);
                list.reloadPage();
            }
        });
    }

    private String getWagesInsufficientNameById(PensionSchemeData item) {
        String wages = "";
        switch (item.getWagesInsufficient()) {
            case Constants.INSUFFICIENT_WAGES_DEDUCT_AS_MUCH_AVAILABLE:
                wages = payrollStrings.deductAsMuchAvailable();
                break;
            case Constants.INSUFFICIENT_WAGES_NO_DEDUCTIOIN_MADE:
                wages = payrollStrings.noDeductionMade();
                break;
            case Constants.INSUFFICIENT_WAGES_DEDUCT_FULL_CONTRIBUTION_FROM_EMPLOYER_SUIBSIDY:
                wages = payrollStrings.fullContributionTakenFromEmployerSubsidy();
                break;
        }
        return wages;
    }


    @Override
    public String getIconStyle() {
        return "payroll pension-scheme-list";  //To change body of implemented methods use File | Settings | File Templates.
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
