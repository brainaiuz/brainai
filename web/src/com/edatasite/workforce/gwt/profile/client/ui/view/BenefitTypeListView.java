package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Oct 23, 2009
 * Time: 4:33:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class BenefitTypeListView extends BaseListView implements Constants {

    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"

    private ListingPanel<BenefitItem> list;


    public BenefitTypeListView() {
        super(BENEFITS, wfmStrings.benefitTypes());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.BenefitPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ADD_OR_EDIT_BENEFIT, BenefitTypeListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.DELETE_BENEFIT, BenefitTypeListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        CustomColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        columns[0] = new ColumnDefinitionConfig<BenefitItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final BenefitItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
//                View benefit
                MenuPopItem studentSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-position-small");
                studentSummary.getElement().setId("Benefit_type_summary_button");
                studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("benefit|summary/" + rowValue.getObjectId(), rowValue.getName()));
                menuItemCount++;
                menuBar.addItem(studentSummary);
                //Edit benefit

                if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE_ADD)) {
                    MenuPopItem editBenefit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    editBenefit.getElement().setId("Benefit_type_edit_button");
                    editBenefit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("benefit|add/add/" + rowValue.getObjectId(), rowValue.getName()));
                    menuItemCount++;
                    menuBar.addItem(editBenefit);
                }

                //Delete benefit
                MenuPopItem deleteCertificate = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deleteCertificate.getElement().setId("Benefit_type_delete_button");
                deleteCertificate.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);

                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            HrmsService.App.get().deleteBenefit(rowValue.getObjectId(), new AbstractAsyncCallback<Integer>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Integer result) {
                                    LoadingPanel.loading(false);

                                    if (result > 0) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.benefit()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_BENEFIT, result, BenefitTypeListView.this);
                                    } else if (result == -2) {
                                        Info.show(wfmStrings.youCannotDelete() + rowValue.getName() + ". " + wfmStrings.itIsUsedOAtLeastABenefitRequest(), Info.Type.WARNING);
                                    } else {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deleteCertificate);

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<BenefitItem, SimpleLink>(wfmStrings.name(), BenefitItem.NAME, 120) {
            @Override
            public SimpleLink getCellValue(BenefitItem rowValue) {
                return getLink(rowValue.getName(), "benefit|summary/" + rowValue.getObjectId());
            }
        };

        columns[2] = new ColumnDefinitionConfig<BenefitItem, String>(wfmStrings.type(), BenefitItem.TYPE, 80) {
            @Override
            public String getCellValue(BenefitItem rowValue) {
                return rowValue.getType();
            }
        };
        columns[3] = new ColumnDefinitionConfig<BenefitItem, String>(wfmStrings.quantityType(), BenefitItem.QTYTYPE, 80) {
            @Override
            public String getCellValue(BenefitItem rowValue) {
                return rowValue.getQtytype();
            }
        };
        columns[4] = new ColumnDefinitionConfig<BenefitItem, String>(wfmStrings.currency(), BenefitItem.CURRENCY, 80) {
            @Override
            public String getCellValue(BenefitItem rowValue) {
                return rowValue.getCurrency();
            }
        };
        columns[5] = new ColumnDefinitionConfig<BenefitItem, String>(wfmStrings.expiryDate(), BenefitItem.EXPIRE_DATE, 100) {
            @Override
            public String getCellValue(BenefitItem rowValue) {
                return rowValue.getExpireDate() != null ? format.format(rowValue.getExpireDate().getNonConvertedDate()) : "";
            }
        };

        columns[6] = new ColumnDefinitionConfig<BenefitItem, String>(wfmStrings.active(), BenefitItem.STATUS, 80) {
            @Override
            public String getCellValue(BenefitItem rowValue) {
                return rowValue.isActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };


        return columns;
    }

    private ListingRequestProvider<BenefitItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            HrmsService.App.get().getBenefitList(filterParametrs, new AbstractAsyncCallback<ListResult<BenefitItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<BenefitItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
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
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE_ADD)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("benefit|add/add"));
                    return addnew;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.thereAreNoItemsToShow());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
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
