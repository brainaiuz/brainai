package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;

/**
 * Created by Khasan on 13.10.14.
 */
public class CertificateTypesListView extends BaseListView implements Constants {

    private ListingPanel<CertificateItem> list;

    public CertificateTypesListView() {
        super(CERTIFICATE_TYPES_LIST, wfmStrings.certificateTemplates());
    }

    @Override
    public String getIconStyle() {
        return "cert certificate-icon";
    }


    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CertificateTypePanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ADD_OR_EDIT_CERTIFICATE_TYPE, CertificateTypesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.DELETE_CERTIFICATE_TYPE, CertificateTypesListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        CustomColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        columns[0] = new ColumnDefinitionConfig<CertificateItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CertificateItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
//                View certificate
                MenuPopItem certificateSummary = new MenuPopItem(wfmStrings.summaryView(), "properties-icon");
                certificateSummary.getElement().setId("certificate_type_summary_id");
                certificateSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customizeCertificate|summary/" + rowValue.getObjectId(), rowValue.getName()));
                menuItemCount++;
                menuBar.addItem(certificateSummary);
                //Edit certificate
                MenuPopItem certificateEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                certificateEdit.getElement().setId("certificate_type_id");
                certificateEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customizeCertificate|add/add/" + rowValue.getObjectId(), rowValue.getName()));
                menuItemCount++;
                menuBar.addItem(certificateEdit);
                certificateEdit.setVisible(Utils.hasPermission(PermissionConstants.CETIFICATE_TEMPLATE_EDIT));

                //Delete certificate
                MenuPopItem deleteCertificate = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deleteCertificate.getElement().setId("certificate_type_delete_id");
                deleteCertificate.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);

                    message.setWidth(400);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            ProfileService.App.get().deleteCertificateType(rowValue.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    if (result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.certificate()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_CERTIFICATE_TYPE, result, CertificateTypesListView.this);
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deleteCertificate);
                deleteCertificate.setVisible(Utils.hasPermission(PermissionConstants.CETIFICATE_TEMPLATE_DELETE));

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<CertificateItem, SimpleLink>(wfmStrings.name(), CertificateItem.NAME, 150) {
            @Override
            public SimpleLink getCellValue(CertificateItem rowValue) {
                return getLink(Optional.ofNullable(rowValue.getName()).orElse(""), "customizeCertificate|summary/" + rowValue.getObjectId());
            }
        };
        columns[2] = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.description(), CertificateItem.DESCRIPTION, 200) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getDescription() != null ? rowValue.getDescription() : "";
            }
        };
        columns[3] = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.type(), CertificateItem.CERTIFICATE_TYPE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getType() != null ? rowValue.getType().getName() : "";
            }
        };
        columns[4] = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.createdBy(), CertificateItem.ISSUED_BY, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getUpdatedBy() != null ? rowValue.getUpdatedBy().getName() : "";
            }
        };
        columns[5] = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.createdDate(), CertificateItem.ISSUED_DATE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getUpdatedDate() != null ? DateUtils.format(rowValue.getUpdatedDate()) : "";
            }
        };

        return columns;
    }

    private ListingRequestProvider<CertificateItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            HrmsService.App.get().getCertificateTypeList(filterParametrs, new AbstractAsyncCallback<ListResult<CertificateItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<CertificateItem> result) {
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
                if (Utils.hasPermission(PermissionConstants.CETIFICATE_TEMPLATE_ADD)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("customizeCertificate|add/add"));
                    return addnew;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyNoCertificateTemplateYet());
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
