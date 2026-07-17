package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificatesListView extends BaseListView implements TCConstants {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();


    private ListingPanel<CertificateData> listingPanel;

    public CertificatesListView() {
        super(TC_CERTIFICATE);
        setDescription(property.getPlural(wfmStrings.certificates()));
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.CertificateListPanel, getColumns(), getListingRequestProvider(), getListPanelDesign());
        add(listingPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CERTIFICATE_SAVED, CertificatesListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CERTIFICATE_DELETE, CertificatesListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private ListingPanelDesign getListPanelDesign() {
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
                if (Utils.hasPermission(PermissionConstants.TC_CERTIFICATES_ISSUE)) {
                    ActionButton addNewPlacement = getAddNewButton();
                    addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_CERTIFICATE + "|add/add"));
                    return addNewPlacement;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(wfmStrings.certificates().toLowerCase() + "."));
                message.setHref(TC_CERTIFICATE + "|add/add");
                message.setTextBeforeLink(tcStrings.noCertificatesLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<CertificateData> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            TCService.App.get().getCertificateList(filterParameter, new AbstractAsyncCallback<ListResult<CertificateData>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<CertificateData> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        List<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<CertificateData, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CertificateData rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //View certificate
                final MenuPopItem studentSummary = new MenuPopItem(tcStrings.certificateView(), "icon-certificate-view");
                studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_CERTIFICATE + "|summary/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(studentSummary);
                //Edit certificate
                final MenuPopItem courseEdit;
                if (Utils.hasPermission(PermissionConstants.TC_CERTIFICATES_EDIT)) {
                    courseEdit = new MenuPopItem(tcStrings.editCertificate(), "icon-employee-edit-profile");
                    courseEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_CERTIFICATE + "|edit/" + rowValue.getObjectID()));
                    menuItemCount++;
                    menuBar.addItem(courseEdit);
                }

                //Delete certificate

                if (Utils.hasPermission(PermissionConstants.TC_CERTIFICATES_DELETE)) {
                    final MenuPopItem deleteCertificate = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    deleteCertificate.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);

                        //message.setSize(300, 150);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                TCService.App.get().deleteCertificate(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Boolean result) {
                                        if (result) {
                                            LoadingPanel.loading(false);
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.course()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CERTIFICATE_DELETE, result, CertificatesListView.this);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuItemCount++;
                    menuBar.addItem(deleteCertificate);
                }

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(100);
        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateData, SimpleLink>(wfmStrings.number(), CertificateData.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(CertificateData rowValue) {
                return new SimpleLink((rowValue.getCertificateTypeData().getNumberData() != null ? rowValue.getCertificateTypeData().getNumberData().getNumberString() : ""), TC_CERTIFICATE + "|summary/" + rowValue.getObjectID());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateData, SimpleLink>(wfmStrings.student(), CertificateData.STUDENT, 200) {
            @Override
            public SimpleLink getCellValue(CertificateData rowValue) {
                return new SimpleLink((rowValue.getStudent() != null ? rowValue.getStudent() : ""), TC_CERTIFICATE + "|summary/" + rowValue.getObjectID());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateData, String>(wfmStrings.certificateType(), CertificateData.CERTIFICATE_TYPE, 100) {
            @Override
            public String getCellValue(CertificateData rowValue) {
                return rowValue.getCertificateTypeData().getName();
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateData, String>(wfmStrings.issuedDate(), CertificateData.CREATION_DATE, 100) {
            @Override
            public String getCellValue(CertificateData rowValue) {
                return rowValue.getCreationDate() != null ? DateUtils.format(rowValue.getCreationDate()) : "";
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    @Override
    public String getIconStyle() {
        return "bgMark certificate-icon";
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

    @Override
    public String getPropertyCode() {
        return TC_CERTIFICATE;
    }
}
