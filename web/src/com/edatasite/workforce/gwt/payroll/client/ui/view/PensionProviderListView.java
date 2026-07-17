package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionProviderData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/3/15
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private ListingPanel<PensionProviderData> list;


    public PensionProviderListView() {
        super(PENSION_PROVIDER_LIST, payrollStrings.pensionProviders());
    }


    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PaymentDeductionListPanel, getColumn(), getListProvider(), getListDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_PENSION_PROVIDER, PensionProviderListView.this, (sender, args) -> list.reloadPage());

        add(list);

        return null;
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("pensionprovider|add/add"));
                    return addNew;
                } else {
                    return null;
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoPensionProviders());
                if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {
                    message.setTextBeforeLink(payrollStrings.noPensionProviderBeforeLinkMessage());
                    message.setHref("pensionprovider|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<PensionProviderData> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            PayrollService.App.get().getPensionProviders(filterParametrs, new AbstractAsyncCallback<ListResult<PensionProviderData>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PensionProviderData> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }


    public CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];
        columnConfig[index] = new ColumnDefinitionConfig<PensionProviderData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final PensionProviderData item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {

                    int actionItemCount = 0;
                    MenuBar menuBar = new MenuBar(true);

                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), getIconStyle());
                    edit.ensureDebugId("Pension_providers_edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("pensionprovider|add/add/" + item.getObjectID(), item.getProviderName()));
                    menuBar.addItem(edit);

                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), getIconStyle());
                    delete.ensureDebugId("Pension_providers_delete");
                    delete.setCommand(() -> PayrollService.App.get().deletePensionProvider(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), payrollStrings.pensionProvider()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_PENSION_PROVIDER, null, PensionProviderListView.this);
                        }
                    }));
                    menuBar.addItem(delete);
                    actionItemCount++;


                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                } else {
                    return null;
                }
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, SimpleLink>(wfmStrings.name(), "name", 140) {

            @Override
            public SimpleLink getCellValue(PensionProviderData rowValue) {
                return getLink(Optional.ofNullable(rowValue.getProviderName()).orElse(""), "pensionprovider|summary/" + rowValue.getObjectID());
            }
        };
        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, String>(wfmStrings.address(), "address", 140) {

            @Override
            public String getCellValue(PensionProviderData rowValue) {
                return rowValue.getProviderAddress() != null ? rowValue.getProviderAddress() : "";
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, String>(wfmStrings.country(), "country", 100) {

            @Override
            public String getCellValue(PensionProviderData rowValue) {
                return rowValue.getProviderCounty() != null ? rowValue.getProviderCounty() : "";
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, String>(wfmStrings.email(), "email", 100) {

            @Override
            public String getCellValue(PensionProviderData rowValue) {
                return rowValue.getProviderEmail() != null ? rowValue.getProviderEmail() : "";
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, String>(wfmStrings.phone(), "phone", 100) {

            @Override
            public String getCellValue(PensionProviderData rowValue) {
                return rowValue.getProviderTelNo() != null ? rowValue.getProviderTelNo() : "";
            }
        };
        columnConfig[index].setColumnSortable(false);

        columnConfig[++index] = new ColumnDefinitionConfig<PensionProviderData, String>(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()), "contact", 100) {

            @Override
            public String getCellValue(PensionProviderData rowValue) {
                return rowValue.getProviderCPName() != null ? rowValue.getProviderCPName() : "";
            }
        };
        columnConfig[index].setColumnSortable(false);

        return columnConfig;
    }

    @Override
    public String getIconStyle() {
        return "payroll ukni-bands-list";
    }

    @Override
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
