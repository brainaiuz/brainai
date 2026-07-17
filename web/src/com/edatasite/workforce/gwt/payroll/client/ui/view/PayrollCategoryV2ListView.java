package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashSet;
import java.util.Set;

public class PayrollCategoryV2ListView extends BaseListView implements Constants {

    public static SelectItem PAYMENT_CATEGORY;
    public static SelectItem DEDUCTION_CATEGORY;
    public static SelectItem TAX_CATEGORY;
    public static SelectItem EMPLOYER_CONTRIBUTION_CATEGORY;
    public static SelectItem MATERIAL_AID_CATEGORY;
    private Set<SelectItem> types;

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<CategoryObject> listingPanel;

    public PayrollCategoryV2ListView() {
        super(PAYROLL_CATEGORY_LIST, payrollStrings.payrollCategories());
    }

    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.PayrollCategoryListPanel, drawColumns(), getProvider(), getDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_DEDUCTION_CATEGORY_ADD, PayrollCategoryV2ListView.this, (sender, args) -> listingPanel.reloadPage());

        PAYMENT_CATEGORY = new SelectItem(0, wfmStrings.payment(), PayrollConstants.CATEGORY_PAYMENT);
        DEDUCTION_CATEGORY = new SelectItem(1, wfmStrings.deduction(), PayrollConstants.CATEGORY_DEDUCTION);
        TAX_CATEGORY = new SelectItem(2, payrollStrings.taxCategory(), PayrollConstants.CATEGORY_TAX);
        EMPLOYER_CONTRIBUTION_CATEGORY = new SelectItem(3, wfmStrings.employerContribution(), PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        MATERIAL_AID_CATEGORY = new SelectItem(4, payrollStrings.materialAid(), PayrollConstants.CATEGORY_MATERIAL_AID);

        types = new HashSet<>();
        types.add(PAYMENT_CATEGORY);
        types.add(DEDUCTION_CATEGORY);
        types.add(TAX_CATEGORY);
        types.add(EMPLOYER_CONTRIBUTION_CATEGORY);
        types.add(MATERIAL_AID_CATEGORY);

        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        int index = 0;
        columns[index] = new ColumnDefinitionConfig<CategoryObject, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CategoryObject item) {
                return getButtonForRow(item);

            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index] = new ColumnDefinitionConfig<CategoryObject, String>(wfmStrings.categoryName(), "name", 140) {
            @Override
            public String getCellValue(CategoryObject item) {
                return item.getName();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<CategoryObject, String>(wfmStrings.code(), "code", 100) {
            @Override
            public String getCellValue(CategoryObject item) {
                return item.getCode();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<CategoryObject, String>(wfmStrings.type(), "type", 100) {
            @Override
            public String getCellValue(CategoryObject item) {
                return item.getType() != null ? getTypeLocale(item.getType()) : null;
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<CategoryObject, String>(wfmStrings.debitToAccount(), "debit", 140) {
            @Override
            public String getCellValue(CategoryObject item) {
                return item.getDebitToAccount() != null ? item.getDebitToAccount().getName() : "N/A";
            }
        };
        columns[index++].setMinimumColumnWidth(70);

        columns[index] = new ColumnDefinitionConfig<CategoryObject, String>(wfmStrings.creditToAccount(), "credit", 140) {
            @Override
            public String getCellValue(CategoryObject item) {
                return item.getCreditToAccount() != null ? item.getCreditToAccount().getName() : "N/A";
            }
        };
        columns[index].setMinimumColumnWidth(70);
        return columns;
    }

    private Anchor getButtonForRow(final CategoryObject item) {
        int actionItemCount = 0;
        final String editLink;
        final String editText;
        editText = wfmStrings.edit();
        editLink = "payrollcategory|edit/" + item.getId();

        MenuBar menuBar = new MenuBar(true);
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_CATEGORIES_EDIT)) {
            MenuPopItem edit = new MenuPopItem(editText, "icon-employee-edit-profile");
            edit.ensureDebugId("Payroll_setting_categories_edit");
            edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(editLink, item.getName()));
            actionItemCount++;
            menuBar.addItem(edit);
        }

        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_CATEGORIES_DELETE)) {
            MenuPopItem del = new MenuPopItem(wfmStrings.delete(), "icon-remove");
            del.ensureDebugId("Payroll_setting_categories_delete");
            del.setCommand(() -> PayrollService.App.get().checkUsingCategory(item.getId(), new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable caught) {

                }

                public void success(Boolean result) {
                    if (result) {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.confirmationMessage());
                        message.setMessage(wfmMessages.sureYouWantToDelete(" \"" + item.getName() + "\" ", ""));
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deleteCategory(item.getId(), new AbstractAsyncCallback<Void>() {
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

                    } else {
                        final WfmMessageBox message2 = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                        message2.setTitle(wfmStrings.info());
                        message2.setMessage(wfmMessages.paymentDeductionCannotBeDeleted(" \"" + item.getName() + "\" "));
                        message2.open();

                    }
                }
            }));
            menuBar.addItem(del);
        }
        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private ListingRequestProvider<CategoryObject> getProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setShowActive(Utils.isArabicCompany());
            PayrollService.App.get().getCompanyCategories(filterParametrs, new AbstractAsyncCallback<ListResult<CategoryObject>>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(ListResult<CategoryObject> result) {
                    callback.onSuccess(result);
                }
            });
        };

    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("payrollcategory|add/add");
                    });
                    return addNew;
                } else {
                    return null;
                }
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(payrollStrings.currentlyThereAreNoPayroll());
                emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private String getTypeLocale(String type) {
        SelectItem selectItem = types.stream().filter(item -> item.getDescription().equals(type)).findFirst().orElse(null);
        return selectItem != null ? selectItem.getName() : null;
    }

    @Override
    public String getIconStyle() {
        return "payroll payments-list";
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
