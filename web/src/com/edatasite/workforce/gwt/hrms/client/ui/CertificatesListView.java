package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
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
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_;

/**
 * Created by Khasan on 06.09.14.
 */
public class CertificatesListView extends BaseListView implements Constants {

    private HorizontalPanel postFormPanel;
    private ListingPanel<CertificateItem> list;
    private Integer employeeId;
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    public CertificatesListView() {
        super(CERTIFICATES_LIST);
        setDescription(property.getPlural(wfmStrings.hrLetters()));
        if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD)) {
            setAddNew("certificate|add/add");
        }
    }

    public CertificatesListView(Integer empId) {
        this();
        this.employeeId = empId;
    }

    @Override
    public String getIconStyle() {
        return "cert certificate-icon";
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.CertificatePanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ADD_OR_EDIT_CERTIFICATE, CertificatesListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.DELETE_CERTIFICATE, CertificatesListView.this, (sender, args) -> list.reloadPage());
        postFormPanel = new HorizontalPanel();
        add(list);
        add(postFormPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        List<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;
        column = new ColumnDefinitionConfig<CertificateItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CertificateItem rowValue) {
                String basePermission = rowValue.getFormID() != null ? CERTIFICATE_OF_EMPLOYMENT_ + rowValue.getFormID().replace("_FORM", "") : null;
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                String certificateNumber = rowValue.getCertificateNumber() != null ? rowValue.getCertificateNumber().getNumberString() : "";
                String certificateType = rowValue.getCertificateType() != null && rowValue.getCertificateType().getName() != null ? rowValue.getCertificateType().getName() : "";
//                View certificate
                final MenuPopItem studentSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-position-small");
                if (!Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT.equals(rowValue.getStatusCode())) {
                    studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|summary/" + rowValue.getObjectId(), certificateNumber, certificateType));
                } else {
                    studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add/" + rowValue.getObjectId() + "/" + rowValue.getFormID(), certificateNumber, certificateType));
                }
                menuItemCount++;
                menuBar.addItem(studentSummary);
                //Edit certificate
//                if (rowValue.getFormID() != null && Utils.hasPermission(basePermission + "_EDIT_" + Utils.getCompanyID())) {
//                    final MenuPopItem courseEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
//                    courseEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add/" + rowValue.getObjectId() + "/" + rowValue.getFormID(), certificateNumber, certificateType));
//                    menuItemCount++;
//                    menuBar.addItem(courseEdit);
//                }
                //Print PDF
                if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_PDF)) {
                    final MenuPopItem printPDF = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
                    printPDF.setCommand(() -> {
                        RequestObject requestObject = new RequestObject(rowValue.getObjectId());
                        String pdfURL = CommandConstants.PDF_URL + "/generateCertificatePDF";
                        final HashMap<String, String> parametrs = requestObject.getRequestParams();
                        Utils.sendPDFOrExcelRequest(postFormPanel, pdfURL, parametrs, "_blank");
                    });
                    menuItemCount++;
                    menuBar.addItem(printPDF);
                }

                //Delete certificate
                if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_DELETE)) {
                    final MenuPopItem deleteCertificate = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteCertificate.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);

                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                HrmsService.App.get().deleteCertificate(rowValue.getObjectId(), new AbstractAsyncCallback<Boolean>() {
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
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_CERTIFICATE, result, CertificatesListView.this);
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
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, SimpleLink>(wfmStrings.number(), CertificateItem.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(CertificateItem rowValue) {
                String certificateNumber = rowValue.getCertificateNumber() != null ? rowValue.getCertificateNumber().getNumberString() : "";
                String certificateType = rowValue.getCertificateType() != null && rowValue.getCertificateType().getName() != null ? rowValue.getCertificateType().getName() : "";
                if (!Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT.equals(rowValue.getStatusCode())) {
                    return new SimpleLink(certificateNumber, "certificate|summary/" + rowValue.getObjectId(), certificateType, certificateNumber);
                } else {
                    return new SimpleLink(certificateNumber, "certificate|add/add/" + rowValue.getObjectId() + "/" + rowValue.getFormID(), certificateType, certificateNumber);
                }
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.employeeCode(), CertificateItem.EMPLOYEE_CODE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getEmployeeCode() != null ? rowValue.getEmployeeCode() : "";
            }
        };
        column.setShow(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.employee(), CertificateItem.EMPLOYEE, 200) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getEmployee() != null ? rowValue.getEmployee().getName() : "";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.type(), CertificateItem.CERTIFICATE_TYPE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getCertificateType() != null ? rowValue.getCertificateType().getName() : "";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.createdDate(), CertificateItem.CREATED_DATE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getCreationDate() != null ? DateUtils.dateFormatWithHour(rowValue.getCreationDate()) : "";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.createdBy(), CertificateItem.CREATED_BY, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getCreatedBy() != null ? rowValue.getCreatedBy().getName() : "";
            }
        };
        column.setColumnSortable(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.modifiedDate(), CertificateItem.ISSUED_DATE, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getUpdatedDate() != null ? DateUtils.dateFormatWithHour(rowValue.getUpdatedDate()) : "";
            }
        };
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.modifiedBy(), CertificateItem.ISSUED_BY, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getUpdatedBy() != null ? rowValue.getUpdatedBy().getName() : "";
            }
        };
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.approver(), CertificateItem.APPROVER, 100) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getCurrentApproverEmployeeName() != null ? rowValue.getCurrentApproverEmployeeName() : "";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CertificateItem, String>(wfmStrings.status(), CertificateItem.STATUS, 80) {
            @Override
            public String getCellValue(CertificateItem rowValue) {
                return rowValue.getOverallStatus() != null && rowValue.getOverallStatus().getCode() != null ? rowValue.getOverallStatus().getCode() : "";
            }
        };
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<CertificateItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadCertificatesList(filterParametrs, callback, null);
        };
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getCertificateFacetFilterData(data, new AsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                if (callback != null) {
                                    callback.onFailure(throwable);
                                }
                            }

                            @Override
                            public void onSuccess(FacetFilterRpc facetFilterRpc) {
                                if (callback != null) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
                        contentConfigure.addContentConfigure(FacetContentType.CertificateFilter.getContentCode()[0], wfmStrings.employee(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrCertificateRepresenter.FIELD_EMPLOYEE_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrCertificateRepresenter.FIELD_EMPLOYEE_ID_NAME;
                            }
                        });
                        contentConfigure.addContentConfigure(FacetContentType.CertificateFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrCertificateRepresenter.FIELD_TYPE_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrCertificateRepresenter.FIELD_TYPE_ID_NAME;
                            }
                        });
                        contentConfigure.addContentConfigure(FacetContentType.CertificateFilter.getContentCode()[2], wfmStrings.modifiedDate(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrCertificateRepresenter.FIELD_ISSUED_BY_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrCertificateRepresenter.FIELD_ISSUED_BY_ID_NAME;
                            }


                        });
                        contentConfigure.addContentConfigure(FacetContentType.CertificateFilter.getContentCode()[4], wfmStrings.createdBy(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrCertificateRepresenter.FIELD_CREATED_BY_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrCertificateRepresenter.FIELD_CREATED_BY_ID_NAME;
                            }


                        });
                        contentConfigure.addContentConfigure(FacetContentType.CertificateFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
                            @Override
                            public String getSolrFieldCriteriaName() {
                                return SolrCertificateRepresenter.FIELD_STATUS_ID;
                            }

                            @Override
                            public String getSolrFacetFieldName() {
                                return SolrCertificateRepresenter.FIELD_STATUS_ID_NAME;
                            }


                        });
                        contentConfigure.addContentConfigureDateListBox(SolrCertificateRepresenter.FIELD_ISSUED_DATE, wfmStrings.issuedDate());
                        contentConfigure.addContentConfigureDateListBox(SolrCertificateRepresenter.FIELD_CREATED_DATE, wfmStrings.createdDate());

                        return contentConfigure;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|add/add"));
                    return addnew;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.currentlyNoCertificatesYet());
                if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD)) {
                    message.setTextBeforeLink(hrmsStrings.youCanStartAddingCertificateByClicking());
                    message.setHref("certificate|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }


        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadCertificatesList(new ListingFilterParameter(), null, container);
    }

    private void loadCertificatesList(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setEmployeeId(employeeId);
        HrmsService.App.get().getCertificateList(filterParametrs, new AbstractAsyncCallback<ListResult<CertificateItem>>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<CertificateItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
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

    public String getPropertyCode() {
        return CERTIFICATES_LIST;
    }
}
