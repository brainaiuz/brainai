package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.NAME;

public class PayrollZoneListView extends BaseListView implements Constants {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<SelectItem> listingPanel;

    public PayrollZoneListView() {
        super(PAYROLL_ZONE_LIST, payrollStrings.payrollZones());
    }

    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.PayrollZoneList, drawColumns(), getProvider(), getDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_ZONE_ADD, PayrollZoneListView.this, (sender, args) -> listingPanel.reloadPage());
        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig<?, ?> column = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(SelectItem item) {
                return getButtonForRow(item);

            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.name(), NAME, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getName();

            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.locations(), CustomFormConstants.PROJECT.LOCATION, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getRelatedItems() != null ? Arrays.stream(item.getRelatedItems()).map(SelectItem::getName).collect(Collectors.joining(",")) : "";

            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);
        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private Anchor getButtonForRow(SelectItem item) {
        int actionItemCount = 0;

        MenuBar menuBar = new MenuBar(true);
        MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
        edit.ensureDebugId("Payroll_zone_edit");
        edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollZone|add/" + item.getId(), item.getName()));
        actionItemCount++;
        menuBar.addItem(edit);

        MenuPopItem del = new MenuPopItem(wfmStrings.delete(), "icon-remove");
        del.ensureDebugId("Payroll_zone_delete");
        del.setCommand(() -> {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.confirmationMessage());
            message.setMessage(wfmMessages.sureYouWantToDelete(" \"" + item.getName() + "\" ", ""));
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    ProfileService.App.get().deletePayrollZone(item.getId(), new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable caught) {

                        }

                        public void success(Void result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.category()), Info.Type.INFO);
                            listingPanel.reloadPage();
                        }
                    });

                }
            });
            message.open();
        });
        menuBar.addItem(del);

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private ListingRequestProvider<SelectItem> getProvider() {
        return (filterParametrs, callback) -> PayrollService.App.get().getPayrollZones(filterParametrs, new AbstractAsyncCallback<ListResult<SelectItem>>() {
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void success(ListResult<SelectItem> result) {
                callback.onSuccess(result);
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
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollZone|add"));
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(final ExportImportOption exportOption, MaterialDropDown menuContainer) {

            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoPayrollZones());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "payroll-zone-list";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
