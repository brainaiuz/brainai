package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
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
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPDFTemplateSelector;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollMessages;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.HashMap;

/**
 * Created by Shohruh on 27 Oct 2016.
 */
public class AdditionalPaymentListView extends BaseListView implements Constants {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final PayrollMessages payrollMessages = GWT.create(PayrollMessages.class);
    private ListingPanel<AdditionalPayment> listingPanel;
    private ImportFilePopUp imp;
    private MaterialLink group, employee, department, location, supervisor;

    public AdditionalPaymentListView() {
        super(ADDITIONAL_PAYMENT_LIST);
        setDescription(property.getPlural(payrollStrings.additionals()));
        if (hasPermissionToAdd()) {
            if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                setAddNew("additionalPayment|add/add/department");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                setAddNew("additionalPayment|add/add/group");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                setAddNew("additionalPayment|add/add/employee");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                setAddNew("additionalPayment|add/add/location");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                setAddNew("additionalPayment|add/add/supervisor");
            }
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ADD);
    }

    public AdditionalPaymentListView(Integer employeeId) {
        super(ADDITIONAL_PAYMENT_LIST);
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        if (hasPermissionToAdd()) {
            if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                setAddNew("additionalPayment|add/add/department");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                setAddNew("additionalPayment|add/add/group");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                setAddNew("additionalPayment|add/add/employee");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                setAddNew("additionalPayment|add/add/location");
            } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                setAddNew("additionalPayment|add/add/supervisor");
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.AdditionalPayment, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, AdditionalPaymentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADDITIONAL_PAYMENT_DELETE, AdditionalPaymentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_PAYMENT_ADD, AdditionalPaymentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_PAYMENT_DELETE, AdditionalPaymentListView.this, (sender, args) -> listingPanel.reloadPage());


        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/additionalPaymentListExcelHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, filterParameter);
        });
        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentItemPdfHandl";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, filterParameter);

        });
        add(listingPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[12];
        columnConfig[index] = new ColumnDefinitionConfig<AdditionalPayment, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final AdditionalPayment item) {
                int actionItemCount = 0;
                boolean isBeforeLockDate = (Utils.isAdditionalPaymentsLocked() && DateUtils.getTransactionLockDate().after(item.getDefaultDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);
                if (!PAYMENT_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode()) ? "additionalPayment|view/" : "additionalDeduction|view/") + item.getObjectID() + "/" + item.getStatusCode(), item.getReference()));
                    actionItemCount++;
                    menuBar.addItem(view);
                } else if (PAYMENT_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode()) ? "additionalPayment|add/add/" : "additionalDeduction|add/add/") + item.getEntityType() + "/" + item.getObjectID(), item.getReference()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }
                boolean editPermission = item.isApprover() ? Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)
                        && !PAYMENT_STATUS_APPROVED.equals(item.getStatusCode())
                        && (Utils.getUserID().equals(item.getCreator().getId())) : Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT) && !PAYMENT_STATUS_APPROVED.equals(item.getStatusCode());

                if (editPermission && !isBeforeLockDate) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode()) ? "additionalPayment|add/add/" : "additionalDeduction|add/add/") + item.getEntityType() + "/" + item.getObjectID(), item.getReference()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_COPY)) {
                    MenuPopItem copy = new MenuPopItem(wfmStrings.copy(), "icon-employee-edit-profile");
                    copy.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode()) ? "additionalPayment|add/add/" : "additionalDeduction|add/add/") + item.getEntityType() + "/" + item.getObjectID() + "/copy", item.getReference()));
                    actionItemCount++;
                    menuBar.addItem(copy);
                }

                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_PDF)) {
                    final HTMLPanel panel = new HTMLPanel("");
                    MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    pdf.setCommand(() -> new PayrollPDFTemplateSelector(ADDITIONAL_PAYMENT_TEMPLATE, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(panel, item);
                        }
                    }));
                    add(panel);
                    actionItemCount++;
                    menuBar.addItem(pdf);
                }
                if (!isBeforeLockDate && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DELETE)) {

                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deleteAdditionalPayment(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Integer result) {
                                        if (result == -1) {
                                            if (item.getCategory() != null && PayrollConstants.CATEGORY_DEDUCTION.equals(item.getCategory().getCode())) {
                                                Info.show(payrollStrings.additionalDeductionsCannotBeDeleted(), Info.Type.WARNING);
                                            } else {
                                                Info.show(property.getPlural(payrollStrings.additionalPaymentsCannotBeDeleted(), payrollStrings.additionals()), Info.Type.WARNING);
                                            }
                                            return;
                                        }
                                        if (item.getCategory() != null && PayrollConstants.CATEGORY_DEDUCTION.equals(item.getCategory().getCode())) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), payrollStrings.additionalDeduction()), Info.Type.INFO);
                                        } else {
                                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.additionalPayment()), Info.Type.INFO);
                                        }
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADDITIONAL_PAYMENT_DELETE, null, AdditionalPaymentListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        /*  Period Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.period(), "period", 100) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getPeriod();
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        /* Period End */

        /* Reference Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, SimpleLink>(wfmStrings.reference(), "reference", 120) {
            @Override
            public SimpleLink getCellValue(AdditionalPayment item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_VIEW)) {
                    String action = null;
                    if (PAYMENT_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)) {
                        action = (item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode())
                                ? "additionalPayment|add/add/"
                                : "additionalDeduction|add/add/") + item.getEntityType() + "/" + item.getObjectID();
                    } else {
                        action = (item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode())
                                ? "additionalPayment|view/"
                                : "additionalDeduction|view/") + item.getObjectID() + "/" + item.getStatusCode();
                    }
                    return getLink(item.getReference(), action, item.getReference());
                } else {
                    return getLink(item.getReference(), null, item.getReference());
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        /*  Reference End  */

        /* Total Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.total(), "total", 100) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return PayrollClientUtils.format(item.getTotal());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        /* Total End */


        /*  Status Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.status(), "status", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getStatus() != null ? item.getStatus() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /* Status End  */

        /*  Category Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.category(), "categoryType", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getCategoryType();
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Category End */

        /* Approver Start  */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.approver(), "approver", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getApprover() != null ? item.getApprover().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /* Approver End  */

        /*  Creator  Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.createdBy(), "creator", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Creator End  */

        /*  Creator Date  Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.createdDate(), "createdDate", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return DateUtils.formatInternal(item.getDate().getDate());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Creator Date End  */

        /*  Updater  Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.modifiedBy(), "updatedBy", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Updater End  */

        /*  Updater Date  Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, String>(wfmStrings.modifiedDate(), "updatedDate", 120) {
            @Override
            public String getCellValue(AdditionalPayment item) {
                return DateUtils.formatInternal(item.getUpdatedTime().getDate());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Updater Date End  */

        /*  Category Lookup  Start */
        columnConfig[++index] = new ColumnDefinitionConfig<AdditionalPayment, SimpleLink>(wfmStrings.type(), "type", 120) {
            @Override
            public SimpleLink getCellValue(AdditionalPayment item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_VIEW)) {
                    String action = null;
                    if (PAYMENT_STATUS_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)) {
                        action = (item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode())
                                ? "additionalPayment|add/add/"
                                : "additionalDeduction|add/add/") + item.getEntityType() + "/" + item.getObjectID();
                    } else {
                        action = (item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode())
                                ? "additionalPayment|view/"
                                : "additionalDeduction|view/") + item.getObjectID() + "/" + item.getStatusCode();
                    }
                    return getLink(item.getCategoryLookUp(), action, item.getCategoryLookUp());
                } else {
                    return getLink(item.getCategoryLookUp(), null, item.getCategoryLookUp());
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        /*  Category Lookup End  */

        return columnConfig;
    }

    private void generatePDF(HTMLPanel panel, AdditionalPayment item) {
        RequestObject requestObject = new RequestObject(item.getObjectID());
        String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    private ListingRequestProvider<AdditionalPayment> getListingRequestProvider() {
        return (filterParametrs, callback) -> PayrollService.App.get().getAdditionalPaymentList(filterParametrs, new AbstractAsyncCallback<ListResult<AdditionalPayment>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<AdditionalPayment> result) {
                callback.onSuccess(result);
            }
        });
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? AdditionalPaymentListView.this::AddnewAdditionalPayment : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasPermissionToAdd() ? AdditionalPaymentListView.this::importFile : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getAdditionalPaymentFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void failure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }

                            @Override
                            public void success(FacetFilterRpc result) {
                                callback.onSuccess(result);
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetFilterContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {

                MaterialMenuBar menuBar = new MaterialMenuBar();
                int menuBarHeight = 0;
                ActionButton add = getAddNewButton(ActionButton.Type.TOOLMENU);
                if (hasPermissionToAdd()) {
                    menuBarHeight = 34;
                    MaterialLink materialLinkPayment = new MaterialLink(wfmStrings.additionalPayment());
                    materialLinkPayment.setPaddingBottom(8);
                    materialLinkPayment.setPaddingTop(8);
                    materialLinkPayment.setPaddingLeft(15);
                    materialLinkPayment.setPaddingRight(15);
                    materialLinkPayment.setHoverable(true);
                    materialLinkPayment.setWidth("100%");
                    materialLinkPayment.getElement().getStyle().setColor("#333");
                    materialLinkPayment.setShadow(1);
                    materialLinkPayment.setBorderRadius("5px");
                    materialLinkPayment.setDisplay(Display.BLOCK);

                    MaterialDropDown mdp = new MaterialDropDown(materialLinkPayment);
                    mdp.getElement().getStyle().setProperty("marginLeft", "100%");
                    mdp.setWidth("100%");
                    mdp.setHover(true);
                    mdp.setHoverable(true);
                    group = new MaterialLink(wfmStrings.group());
                    department = new MaterialLink(wfmStrings.department());
                    location = new MaterialLink(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
                    employee = new MaterialLink(wfmStrings.employee());
                    supervisor = new MaterialLink(wfmStrings.supervisor());
                    group.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/group"));
                    employee.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/employee"));
                    department.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/department"));
                    location.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/location"));
                    supervisor.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/supervisor"));
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                        mdp.add(group);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                        mdp.add(employee);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                        mdp.add(department);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                        mdp.add(location);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                        mdp.add(supervisor);
                    }
                    if (hasPermissionByCategory() != null) {
                        materialLinkPayment.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/" + hasPermissionByCategory()));
                    } else materialLinkPayment.add(mdp);
                    menuBar.add(materialLinkPayment);


                    menuBar.add(new Div());
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_DEDUCTION_ADD)) {
                    menuBarHeight = 68;
                    MaterialLink materialLinkDeduction = new MaterialLink(payrollStrings.additionalDeduction());
                    materialLinkDeduction.ensureDebugId("additionalDeduction");
                    materialLinkDeduction.setPaddingBottom(8);
                    materialLinkDeduction.setPaddingTop(8);
                    materialLinkDeduction.setPaddingLeft(15);
                    materialLinkDeduction.setPaddingRight(15);
                    materialLinkDeduction.setHoverable(true);
                    materialLinkDeduction.getElement().getStyle().setColor("#333");
                    materialLinkDeduction.setShadow(1);
                    materialLinkDeduction.setWidth("100%");
                    materialLinkDeduction.setBorderRadius("5px");
                    materialLinkDeduction.setDisplay(Display.BLOCK);

                    MaterialDropDown mdp = new MaterialDropDown(materialLinkDeduction);
                    mdp.getElement().getStyle().setProperty("marginLeft", "100%");
                    mdp.setMaxWidth("125px");
                    if (hasPermissionToAdd()) mdp.setMarginTop(-35);
                    else mdp.setMarginTop(0);
                    mdp.setBelowOrigin(false);
                    mdp.setHover(true);
                    mdp.setHoverable(true);
                    group = new MaterialLink(wfmStrings.group());
                    department = new MaterialLink(wfmStrings.department());
                    location = new MaterialLink(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
                    employee = new MaterialLink(wfmStrings.employee());
                    supervisor = new MaterialLink(wfmStrings.supervisor());
                    group.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/group"));
                    employee.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/employee"));
                    department.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/department"));
                    location.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/location"));
                    supervisor.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/supervisor"));
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                        mdp.add(group);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                        mdp.add(employee);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                        mdp.add(department);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                        mdp.add(location);
                    }
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                        mdp.add(supervisor);
                    }
                    if (hasPermissionByCategory() != null) {
                        materialLinkDeduction.addClickHandler(clickHandler -> SinksContainerFactory.entryPoint.onHistoryChanged("additionalDeduction|add/add/" + hasPermissionByCategory()));
                    } else materialLinkDeduction.add(mdp);

                    menuBar.add(materialLinkDeduction);
                }
                menuBar.setHeight(String.valueOf(menuBarHeight));
                if (hasPermissionToAdd() || Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_DEDUCTION_ADD)) {
                    add.setMenu(menuBar);
                    return add;
                }
                return null;
            }

            /* Bu metod 4 ala categorydan bitta categoryga permissioni borligini aniqlash uchun yozildi */
            private String hasPermissionByCategory() {
                int i = 0;
                String categoryName = "";
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                    i++;
                    categoryName = "group";
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                    i++;
                    categoryName = "employee";
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                    i++;
                    categoryName = "department";
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                    i++;
                    categoryName = "location";
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                    i++;
                    categoryName = "supervisor";
                }
                if (i == 1) return categoryName;
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                if (hasPermissionToAdd()) {

                    imp = new ImportFilePopUp(ImportTypeEnum.ADDITIONAL_PAYMENT, null);
                    imp.setSubmitCompleted(() -> {
                        if (imp.getObjectId() != null) {
                            goTo("importAdditionalPayment|add/add/" + imp.getObjectId());
                        }
                    });

                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                    exportOption.initExport(null, true);
                } else {
                    exportOption.initExport(null, false);
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(payrollStrings.currentlyThereAreNotAdditionalPayments(), payrollStrings.additionals()));
                if (hasPermissionToAdd()) {
                    if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
                        message.setHref("additionalPayment|add/add/department");
                    } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
                        message.setHref("additionalPayment|add/add/group");
                    } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
                        message.setHref("additionalPayment|add/add/employee");
                    } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
                        message.setHref("additionalPayment|add/add/location");
                    } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
                        message.setHref("additionalPayment|add/add/supervisor");
                    }
                } else message.setHref("");
                message.setHref(hasPermissionToAdd() ? "additionalPayment|add/add/" : "");
                message.setTextBeforeLink(property.getSingular(payrollStrings.youCanStartAddingAdditionalPaymentByClicking(), wfmStrings.additionalPayment()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void importFile() {
        imp.open();
    }

    private void AddnewAdditionalPayment() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
            SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/department");
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
            SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/group");
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
            SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/employee");
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
            SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/location");
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
            SinksContainerFactory.entryPoint.onHistoryChanged("additionalPayment|add/add/supervisor");
        }
    }

    private FacetContentConfigure getFacetFilterContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(5, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[0], wfmStrings.month(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrAdditionalPaymentPresenter.FIELD_MONTH_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrAdditionalPaymentPresenter.FIELD_MONTH_ID_NAME;
            }
        });


        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[5], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrAdditionalPaymentPresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrAdditionalPaymentPresenter.FIELD_STATUS_ID_NAME;
            }
        });


        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[2], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[3], wfmStrings.approver(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[6], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrAdditionalPaymentPresenter.FIELD_CATEGORY_LOOKUP_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrAdditionalPaymentPresenter.FIELD_CATEGORY_LOOKUP_ID_NAME;
            }
        });

        //        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[1], wfmStrings.year(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrAdditionalPaymentPresenter.FIELD_YEAR;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrAdditionalPaymentPresenter.FIELD_YEAR_ID_NAME;
//            }
//        });

//        contentConfigure.addContentConfigure(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4], wfmStrings.amount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT;
//            }
//        });


        contentConfigure.setDatePeriodPanelEnabled(false);
        return contentConfigure;
    }


    @Override
    public String getIconStyle() {
        return "payroll aeo-list";
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

    @Override
    public String getPropertyCode() {
        return ADDITIONAL_PAYMENT_LIST;
    }
}
