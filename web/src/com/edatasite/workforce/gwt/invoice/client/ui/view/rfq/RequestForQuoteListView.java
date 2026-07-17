package com.edatasite.workforce.gwt.invoice.client.ui.view.rfq;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForQuoteListView extends BaseListView implements Constants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private ListingPanel listPanel;
    private Integer opportunityID;
    private ListingFilterParameter fp;
    private boolean isAccountingSection;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;

    public RequestForQuoteListView() {
        super(REQUEST_FOR_QUOTE);
        setDescription(property.getPlural(wfmStrings.requestForQuote()));
        this.opportunityID = opportunityID;
        if (hasPermissionToCreateRFQ()) {
            setAddNew("requestforquote|add/add");
        }
    }


    public RequestForQuoteListView(ListingFilterParameter filterParameters, boolean isAccountingSection, boolean isBlocked) {
        super(REQUEST_FOR_QUOTE);
        setDescription(property.getPlural(wfmStrings.requestForQuote()));
        if (!isBlocked && hasPermissionToCreateRFQ()) {
            setAddNew("requestforquote|add/add");
        }
        this.isAccountingSection = isAccountingSection;
        this.fp = filterParameters;
        if (fp != null) {
            this.opportunityID = fp.getOpportunityID();
        }
    }

    public static String getIdsOnly(Set<RFQData> selectedItems) {
        StringBuilder ids = null;
        for (RFQData item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getObjectID());
            } else {
                ids.append("," + item.getObjectID());
            }
        }
        return ids.toString();
    }

    private void saveRfqCellValue(RFQData rowValue, String columnCodeName) {
        QuoteService.App.get().saveRfqCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<RFQData> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (RFQData item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.RequestForQuoteListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        if (listPanel.getChooseFilter() != null) {
            listPanel.getChooseFilter().setAccountingListType(getName());
        }
        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveRfqCellValue((RFQData) rowValue, columnCodeName));

        listPanel.setPDFListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs.getStartDate() != null) {
                filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
            }
            if (filterParametrs.getEndDate() != null) {
                filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
            }
            setFilterValues(filterParametrs);
            listPanel.callListPDF(CommandConstants.PDF_URL + "/requestForQuoteListPDFHandler", filterParametrs);
        });

        listPanel.setExcelListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs.getStartDate() != null) {
                filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
            }
            if (filterParametrs.getEndDate() != null) {
                filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
            }
            setFilterValues(filterParametrs);
            listPanel.callListExcel(CommandConstants.COMMON_URL + "/downloadRequestQuoteListExcelHandler", filterParametrs);
        });

        add(listPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_QUOTE_ADDED, RequestForQuoteListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_QUOTE_ADDED, RequestForQuoteListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    listPanel.reloadPage();
                }
            };
            t.schedule(3000);
        });
        listPanel.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (delete != null) {
                    delete.setVisible(true);
                }
            } else {
                if (delete != null) {
                    delete.setVisible(false);
                }
            }
        });

        return null;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (opportunityID != null ? Utils.hasPermission(CONVERT_OPPORTUNITY_TO_RFQ) : hasPermissionToCreateRFQ()) {
                    return RequestForQuoteListView.this::getAddNewButtonLink;
                }
                return null;
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
                        return (data, callback) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            RbacService.App.get().getRFQFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (opportunityID != null ? Utils.hasPermission(CONVERT_OPPORTUNITY_TO_RFQ) : hasPermissionToCreateRFQ()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> getAddNewButtonLink());
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_PDF) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY)) {
                    return getPrintButton(clickEvent -> printSelection());
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_DELETE) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.noRequestForQuoteMessage(), wfmStrings.requestForQuote()));
                if (hasPermissionToCreateRFQ()) {
                    message.setTextBeforeLink(property.getSingular(accountingStrings.noRequestForQuoteBeforeLinkMessage(), wfmStrings.requestForQuote()));
                    message.setHref("requestforquote|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_EDIT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.requestForQuote()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(REQUEST_FOR_QUOTE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/requestForQuotePDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.requestForQuote()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    QuoteService.App.get().deleteSelectedRFQs(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            listPanel.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.requestForQuote()), Info.Type.INFO);

                            for (Integer id : ids) {
                                removeDeletedTab(REQUEST_FOR_QUOTE + id);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.RFQFacetFilter.getContentCode()[0], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.RFQFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.RFQFacetFilter.getContentCode()[2], wfmStrings.customer(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.RFQFacetFilter.getContentCode()[3], wfmStrings.country(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID_NAME;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE, accountingStrings.requestDate());
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, wfmStrings.dueDate());

        return contentConfigure;
    }

    private void getAddNewButtonLink() {
        if (opportunityID != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add/opportunity/" + opportunityID);
        } else if (fp == null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add");
        } else if (fp.getClientId() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add/fromCrmAccount/" + fp.getClientId());
        } else if (fp.getSupplierId() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add/supplier/" + fp.getSupplierId());
        } else {
            String addRFQ = GWT.getHostPageBaseURL() + "Accounting.html#requestforquote|add/add/fromCrmAccount/" + fp.getClientId();
            Window.open(addRFQ, "_blank", "");
        }
    }

    private ListingRequestProvider getListProvider() {
        return (ListingRequestProvider<RFQData>) (listingFilterParameters, callback) -> {
            loadRFQList(listingFilterParameters, callback, null);
        };
    }

    private boolean hasPermissionToCreateRFQ() {
        return Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_ADD) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_ADD);
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        List<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<RFQData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 100) {
            @Override
            public Anchor getCellValue(final RFQData item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_SUMMARY) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY)) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    summary.setCommand(() -> {
                        if (opportunityID != null) {
                            String addRFQ = "requestforquote|summary/" + item.getObjectID();
                            SinksContainerFactory.entryPoint.onHistoryChanged(addRFQ, item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|summary/"
                                    + item.getObjectID(), item.getNumber());
                        }
                    });
                    actionItemCount++;
                    if (!RFQ_DRAFT.equals(item.getStatusCode())) {
                        menuBar.addItem(summary);
                    }
                }
                if ((Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_EDIT) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_EDIT)) && item.isEditable()) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.setCommand(() -> {
                        if (opportunityID != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|edit/" + item.getObjectID());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|edit/" + item.getObjectID(),
                                    item.getNumber());
                        }

                    });
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                PropertyItem propertyItem = Utils.getProperTy(Constants.REQUEST_FOR_QUOTE);
                if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                    MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                    MenuBar convertMenu = new MenuBar(true);
                    convertMenu.setAutoOpen(true);
                    int convertItems = 0;
                    for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                        if (convertItem != null) {
                            convertItems = getConvertItems(item, menuBar, convertMenu, convertItems, convertItem);
                        }
                    }

                    if (convertItems > 0) {
                        convertMenuPopItem.setSubMenu(convertMenu);
                        actionItemCount++;
                        menuBar.addItem(convertMenuPopItem);
                    }
                }

                if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_DELETE) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                QuoteService.App.get().deleteRFQ(item.getObjectID(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        Info.show(property.getSingular(accountingMessages.rfqDeletedSuccessfully(), wfmStrings.requestForQuote()), Info.Type.INFO);
                                        listPanel.reloadPage();
                                        removeDeletedTab(REQUEST_FOR_QUOTE + item.getObjectID());
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf());
                final HTMLPanel panel = new HTMLPanel("");

                pdf.setCommand(() -> new PDFTemplateSelector(AccountingConstants.RFQ, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        generatePDF(panel, id, item.getObjectID());
                    }
                }));
                add(panel);
                actionItemCount++;
                if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_PDF) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY)) {
                    menuBar.addItem(pdf);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(100);
        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, Widget>(wfmStrings.number(), RFQData.REQUEST_NUMBER, 120) {
            @Override
            public Widget getCellValue(final RFQData item) {
                Label number = new Label(item.getNumber());
                number.setStyleName("uploadLinkStyle2");
                number.addClickHandler(clickEvent -> {
                    boolean statusIsDraft = RFQ_DRAFT.equals(item.getStatusCode());

                    if (opportunityID != null) {
                        String addRFQ = "requestforquote|" + (statusIsDraft ? "edit/" : "summary/") + item.getObjectID();
                        SinksContainerFactory.entryPoint.onHistoryChanged(addRFQ, item.getNumber());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|" + (statusIsDraft ? "edit/" : "summary/") + item.getObjectID(), item.getNumber(), item.getNumber());
                    }
                });
                return number;
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), RFQData.OPPORTUNITY_NAME, 180) {
            @Override
            public String getCellValue(RFQData item) {
                return item.getCustomer() != null ? item.getCustomer().getName() : "";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(accountingStrings.requestFrom(), RFQData.REQUEST_FROM, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return COMPANY_SUPPLIERS.equals(item.getRequestFrom()) ? accountingStrings.companySuppliers() : accountingStrings.directorySuppliers();
            }
        };
        column.setColumnSortable(false);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(accountingStrings.requestDate(), RFQData.DATE, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return DateUtils.getDateAndTimeFormatShort2(item.getDate().getNonConvertedDate());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(wfmStrings.dueDate(), RFQData.VALID_UNTIL, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return DateUtils.format(item.getValidUntil());
            }
        };
        columns.add(column);
        column.setColumnSortable(false);

        column = new ColumnDefinitionConfig<RFQData, String>(wfmStrings.opportunity() + "#", RFQData.OPPORTUNITY_NUMBER, 150) {
            @Override
            public String getCellValue(RFQData item) {
                return item.getOppportunityNumber() != null ? item.getOppportunityNumber() : "";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(wfmStrings.status(), RFQData.STATUS, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return getStatusName(item.getOverallStatus());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<RFQData, String>(wfmStrings.approver(), RFQData.APPROVER, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return item.getApprover() != null ? item.getApprover().getName() : "";
            }
        };
        column.setShow(false);
        columns.add(column);

        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            column = new ColumnDefinitionConfig<RFQData, String>(Property.get(Constants.PROJECT, wfmStrings.project()), RFQData.PROJECT, 120) {
                @Override
                public String getCellValue(RFQData item) {
                    return item.getProject() != null ? item.getProject().getName() : "";
                }
            };
            column.setShow(false);
            columns.add(column);
        }

        column = new ColumnDefinitionConfig<RFQData, String>(wfmStrings.country(), RFQData.CUSTOMER_COUNTRY, 100) {
            @Override
            public String getCellValue(RFQData item) {
                return item.getClientAddress() != null ? item.getClientAddress() : "";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private void generatePDF(HTMLPanel panel, Integer templateId, Integer objectId) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateId != null) {
            parameters.put("templateID", String.valueOf(templateId));
        }
        String pdfURL = CommandConstants.PDF_URL + "/requestForQuotePDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        if (fp != null) {
            filterParametrs.setRelationID(fp.getRelationID());
            filterParametrs.setRelationType(fp.getRelationType());

            if (fp.getClientId() != null) {
                filterParametrs.setClientId(fp.getClientId());
            }
        }
        filterParametrs.setOpportunityID(opportunityID);
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
        }
    }

    private String getStatusName(ReferenceItem status) {
        if (status == null || status.getCode() == null) {
            return wfmStrings.notAvailable();
        }
        switch (status.getCode()) {
            case Constants.RFQ_CONVERTED:
                return wfmStrings.converted();
            case Constants.RFQ_PARTIAL_CONVERTED:
                return wfmStrings.partiallyConverted();
            case Constants.RFQ_DRAFT:
                return wfmStrings.draft();
            case Constants.RFQ_SUBMITTED:
                return wfmStrings.submitted();
            case Constants.RFQ_APPROVED:
                return wfmStrings.approved();
            case Constants.RFQ_DECLINED:
                return wfmStrings.rejected();
            default:
                return status.getCode();
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadRFQList(fp, null, container);
    }

    private void loadRFQList(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        if (fp != null) {
            filterParametrs.setClientId(fp.getClientId());
            filterParametrs.setSupplierId(fp.getSupplierId());
        }
        setFilterValues(filterParametrs);
        QuoteService.App.get().getRFQList(filterParametrs, new AsyncCallback<ListResult<RFQData>>() {
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void onSuccess(ListResult<RFQData> list) {

                if (callback != null) {
                    callback.onSuccess(list);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (list.getTotal() != null && list.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(list.getTotal()));
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

    @Override
    public String getPropertyCode() {
        return Constants.REQUEST_FOR_QUOTE;
    }

    private int getConvertItems(RFQData rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_OPPORTUNITY.equals(convertItem.getCode())) {
            if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES)) {
                final MenuPopItem convertToOpp = new MenuPopItem(Property.get(Constants.Opportunities, wfmStrings.opportunity()), "icon-send-sales-invoice");
                convertToOpp.setCommand(() -> {
                    convertToOpp.closeAll(menuBar);
                    if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    } else {
                        Utils.openURL("Crm.html#opportunity|add/add/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    }
                });
                convertToOpp.ensureDebugId("convert_opportunity");
                convertMenu.addItem(convertToOpp);
                convertItems++;
            }
        } else if (RelationItem.TYPE_SALEQUOTE.equals(convertItem.getCode())) {
            if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)) {
                final MenuPopItem convertToSQ = new MenuPopItem(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), "icon-send-sales-invoice");
                convertToSQ.setCommand(() -> {
                    convertToSQ.closeAll(menuBar);
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("salequote|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    } else {
                        Utils.openURL("Accounting.html#salequote|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    }

                });
                convertToSQ.ensureDebugId("convert_SQ");
                convertMenu.addItem(convertToSQ);
                convertItems++;
            }
        } else if (RelationItem.TYPE_SALEORDER.equals(convertItem.getCode())) {
            if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)) {
                final MenuPopItem convertToSO = new MenuPopItem(Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), "icon-send-sales-invoice");
                convertToSO.setCommand(() -> {
                    convertToSO.closeAll(menuBar);
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    } else {
                        Utils.openURL("Accounting.html#saleorder|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    }

                });
                convertToSO.ensureDebugId("convert_SO");
                convertMenu.addItem(convertToSO);
                convertItems++;
            }
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(convertItem.getCode())) {
            if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_PURCHASE_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)) {
                final MenuPopItem convertToPO = new MenuPopItem(Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), "icon-send-sales-invoice");
                convertToPO.setCommand(() -> {
                    convertToPO.closeAll(menuBar);
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    } else {
                        Utils.openURL("Accounting.html#purchaseorder|edit/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
                    }
                });
                convertToPO.ensureDebugId("convert_PO");
                convertMenu.addItem(convertToPO);
                convertItems++;
            }
        } else if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_REQUEST_FOR_QUOTE + "/" + rowValue.getObjectID());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }
        return convertItems;
    }
}
