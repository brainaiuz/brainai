package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormItemRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_INVOICE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_VACANCY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_QUICK_ADD_VACANCY;

public class CustomFormItemListView extends BaseListView implements Constants {

    private static final CommonServiceAsync commonService = CommonService.App.get();
    private final String name;
    private final String formID;
    private final Integer fid; //this is custom form id
    private String lookUpType;
    private Integer lookUpTypeId;
    private ListingPanel<FormItems> listingTable;
    private Integer hasQuota;
    private HandlerRegistration handlerRegistration;
    private IntroductionPageRpc pageRpcItem;

    public CustomFormItemListView(final Integer fId, final String name, final String formID) {
        super("custom_form_" + fId, name);
        this.name = name;
        this.formID = formID;
        fid = fId;
        this.setCustomForm(true);
        this.setFromView(formID);
        if (Utils.hasPermission(formID + "_ADD_" + Utils.getCompanyID())) {
            this.setAddNew(Constants.ITEM_LIST + "|add/add/" + fId + "/" + formID + "/" + name);
        }
        getIntroductionPageItem();
        checkQuota(false);
    }

    private void getIntroductionPageItem() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getIntoductionPageByParentFormId(formID, new AsyncCallback<IntroductionPageRpc>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(IntroductionPageRpc pageRpc) {
                LoadingPanel.loading(false);
                pageRpcItem = pageRpc;
            }
        });
        /// this method for to know custom form is Quiz or not
//        LoadingPanel.loading(true);
//        CommonService.App.get().customFormIsQuizForm(formID, new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                isQuizForm = false;
//                LoadingPanel.loading(false);
//            }
//
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//                isQuizForm = true;
//                LoadingPanel.loading(false);
//            }
//        });
    }

    public CustomFormItemListView(final Integer fId, final String name, final String formID, final String lookUpType, final Integer lookUpTypeId) {
        super("custom_form_" + fId, name);
        this.name = name;
        this.formID = formID;
        fid = fId;
        this.lookUpType = lookUpType;
        this.lookUpTypeId = lookUpTypeId;
        this.setCustomForm(true);
        this.setFromView(formID);
        if (Utils.hasPermission(formID + "_ADD_" + Utils.getCompanyID())) {
            this.setAddNew(Constants.ITEM_LIST + "|add/add/" + fId + "/" + formID + "/" + name);
        }
        checkQuota(false);
    }

    protected Widget onInitialize() {
        this.initData();
        return null;
    }

    private void initData() {
        if (this.formID != null) {
            boolean hasPermissionForCustomize = Utils.hasPermission(CustomFormItemListView.this.formID + "_CUSTOMIZE_COLUMN_" + Utils.getCompanyID());
            boolean hasPermissionForFilter = Utils.hasPermission(CustomFormItemListView.this.formID + "_FILTER_" + Utils.getCompanyID());
            this.listingTable = new ListingPanel<>(ListPanelType.CustomFormItemsPanel, this.formID, this.getColumnConfigs(), this.getListingRequestProvider(), this.getListingPanelDesign(), SelectionGrid.SelectionPolicy.ONE_ROW, -1, false, this.fid, this.fid, false, true, hasPermissionForFilter);
        } else {
            this.listingTable = new ListingPanel<>(ListPanelType.CustomFormItemsPanel, this.getColumnConfigs(), this.getListingRequestProvider(), this.getListingPanelDesign(), this.fid, this.fid);
        }
//        if (Utils.hasPermission(this.formID + "_PDF_" + Utils.getCompanyID())) {
        this.listingTable.setPDFListener(clickEvent -> {
            final String pdfURL = CommandConstants.PDF_URL + "/customFormItemListPDFHandler";
            this.listingTable.callListPDF(pdfURL, this.listingTable.getFilterParametrs());
        });
        this.listingTable.setExcelListener(clickEvent -> {
            final String excelUrl = CommandConstants.COMMON_URL + "/customFormItemListExcelHandler";
            this.listingTable.callListExcel(excelUrl, this.listingTable.getFilterParametrs());
        });
//        }
        this.listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> this.saveCustomFormItemCellValue((FormItems) rowValue, columnCodeName));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, this, (sender, args) -> this.listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, this, (sender, args) -> checkQuota(true));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, this, (sender, args) -> reInitialize());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_ITEM_APPROVAL, this, (sender, args) -> {
            final Timer t = new Timer() {
                @Override
                public void run() {
                    CustomFormItemListView.this.listingTable.reloadPage();
                }
            };
            t.schedule(3500);
        });
        this.add(this.listingTable);
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<CustomColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;
        columnConfig = new ColumnDefinitionConfig<FormItems, Anchor>(View.wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(FormItems rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                int menuItemCount = 0;

                //Summary
                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_SUMMARY_" + Utils.getCompanyID()) && !"Draft".equals(rowValue.getStatus())) {
                    MenuPopItem view = new MenuPopItem(View.wfmStrings.summaryView());
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + rowValue.getObjectID() + "/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name));
                    menuItemCount++;
                    menuBar.addItem(view);
                }

                //Edit  item
                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_EDIT_" + Utils.getCompanyID())) {
                    MenuPopItem edit = new MenuPopItem(View.wfmStrings.edit(), "icon-edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + rowValue.getObjectID() + "/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name));
                    menuItemCount++;
                    menuBar.addItem(edit);
                }
                //Copy  item
                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_COPY_" + Utils.getCompanyID())) {
                    MenuPopItem copy = new MenuPopItem(View.wfmStrings.copy(), "icon-edit");
                    if (hasQuota == 0) {
                        copy.setCommand(() -> {
                            if (CustomFormItemListView.this.lookUpType != null && CustomFormItemListView.this.lookUpTypeId != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + rowValue.getObjectID() + "/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name + "/copyItemForm/" + CustomFormItemListView.this.lookUpType + "/" + CustomFormItemListView.this.lookUpTypeId);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + rowValue.getObjectID() + "/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name + "/copyItemForm");
                            }
                        });
                    } else {
                        if (hasQuota == -2)
                            copy.setCommand(() -> Info.warn(wfmStrings.dontHaveQuotaToAdd()));
                        if (hasQuota == -1)
                            copy.setCommand(() -> Info.warn(wfmStrings.youDontHavePermission()));
                    }
                    menuItemCount++;
                    menuBar.addItem(copy);
                }

                final String objectName = CustomFormItemListView.this.formID != null && CustomFormItemListView.this.formID.length() > 5 ? CustomFormItemListView.this.formID.substring(0, CustomFormItemListView.this.formID.length() - 5) : null;
                if (objectName != null) {
                    final PropertyItem propertyItem = Utils.getProperTy(objectName);
                    if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                        final MenuPopItem convertMenuPopItem = new MenuPopItem(View.wfmStrings.convert(), "icon-add-green");

                        final MenuBar convertMenu = new MenuBar(true);
                        convertMenu.setAutoOpen(true);
                        int convertItems = 0;
                        for (final ConvertItem convertItem : propertyItem.getConvertItems()) {
                            if (convertItem != null) {
                                convertItems = CustomFormItemListView.this.getConvertItems(rowValue, menuBar, convertMenu, convertItems, convertItem);
                            }
                        }

                        if (convertItems > 0) {
                            convertMenuPopItem.setSubMenu(convertMenu);
                            menuItemCount++;
                            menuBar.addItem(convertMenuPopItem);
                        }
                    }
                }


                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_PDF_" + Utils.getCompanyID())) {
                    MenuPopItem pdf = new MenuPopItem(View.wfmStrings.pdf());
                    HTMLPanel htmlPanel = new HTMLPanel("");
                    if (CustomFormItemListView.this.formID.equals("ROTATSIJA__FORM")) {
                        pdf.setCommand(() -> AllInOneService.App.get().getCompanyPdfTemplatesWithFormId("CUSTOM_FORM_ITEM_VIEW", CustomFormItemListView.this.formID, new AbstractAsyncCallback<CustomFormItemPdfTemplateList>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                super.onFailure(caught);
                            }

                            @Override
                            public void onSuccess(CustomFormItemPdfTemplateList result) {
                                CustomFormItemListView.this.generatePDF(htmlPanel, CustomFormItemListView.this.fid, rowValue.getObjectID(), result.getDefaultTemplateID());
                            }
                        }));
                        CustomFormItemListView.this.add(htmlPanel);
                        menuItemCount++;
                        menuBar.addItem(pdf);
                    } else {
                        pdf.setCommand(() -> new CustomFormItemPDFTemplateSelector("CUSTOM_FORM_ITEM_VIEW", CustomFormItemListView.this.formID, new ExtendedCommand() {
                            @Override
                            public void execute(final Integer id) {
                                CustomFormItemListView.this.generatePDF(htmlPanel, CustomFormItemListView.this.fid, rowValue.getObjectID(), id);
                            }
                        }));
                        CustomFormItemListView.this.add(htmlPanel);
                        menuItemCount++;
                        menuBar.addItem(pdf);
                    }
                }

                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_DELETE_" + Utils.getCompanyID())) {
                    MenuPopItem delete = new MenuPopItem(View.wfmStrings.delete());
                    delete.setCommand(() -> CustomFormItemListView.this.deleteItem(rowValue.getObjectID()));
                    menuItemCount++;
                    menuBar.addItem(delete);
                }


                final ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.createdBy(), FormItems.CREATER, 120) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                if (rowValue.isAnonymous())
                    return wfmStrings.anonymous();
                else
                    return rowValue.getCreator();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.createdDate(), FormItems.CREATED_DATE, 100) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                return rowValue.getCreatedDate() != null ? DateUtils.formatInternal(rowValue.getCreatedDate()) : "";
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);


        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.modifiedBy(), FormItems.UPDATER, 120) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                if (rowValue.isAnonymous())
                    return wfmStrings.anonymous();
                else
                    return rowValue.getUpdater();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.modifiedDate(), FormItems.UPDATED_DATE, 100) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                return rowValue.getModifiedData() != null ? DateUtils.formatInternal(rowValue.getModifiedData()) : "";
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.status(), FormItems.STATUS, 80) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                return rowValue.getStatus();
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(40);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<FormItems, String>(View.wfmStrings.approver(), FormItems.APPROVER, 80) {
            @Override
            public String getCellValue(final FormItems rowValue) {
                return rowValue.getCurrentApproverName();
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(40);
        columnConfigs.add(columnConfig);
//        if (isQuizForm){
//            String columnName = wfmStrings.quiz() + " " + wfmStrings.total() + " " + wfmStrings.score();
//            columnConfig = new ColumnDefinitionConfig<FormItems, String>(columnName, FormItems.QUIZ, 80) {
//                @Override
//                public String getCellValue(final FormItems rowValue) {
//                    return rowValue.getQuizResult();
//                }
//            };
//            columnConfig.setShow(false);
//            columnConfig.setMinimumColumnWidth(40);
//            columnConfigs.add(columnConfig);
//        }

        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }


    private int getConvertItems(final FormItems rowValue, final MenuBar menuBar, final MenuBar convertMenu, int convertItems, final ConvertItem convertItem) {
        switch (convertItem.getCode()) {
            case RelationItem.TYPE_OPPORTUNITY:
                if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES)) {
                    MenuPopItem convertToOpp = new MenuPopItem(Property.get(Constants.Opportunities, View.wfmStrings.opportunity()), "icon-send-sales-invoice");
                    convertToOpp.setCommand(() -> {
                        convertToOpp.closeAll(menuBar);
                        if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("Accounting.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Crm.html#opportunity|add/add/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }

                    });
                    convertToOpp.ensureDebugId("convert_opportunity");
                    convertMenu.addItem(convertToOpp);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_EMPLOYEE:
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
                    MenuPopItem convertToEmp = new MenuPopItem(Property.get(Constants.EMLOYEE_LIST, wfmStrings.employee()), "icon-send-sales-invoice");
                    convertToEmp.setCommand(() -> {
                        convertToEmp.closeAll(menuBar);
                        if (Utils.getPathName().contains("Hrms.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add/" + FROM_HRMS + "/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Hrms.html#singleemployee|add/add/" + FROM_HRMS + "/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }

                    });
                    convertToEmp.ensureDebugId("convert_employee");
                    convertMenu.addItem(convertToEmp);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_SALEQUOTE:
                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)) {
                    MenuPopItem convertToSQ = new MenuPopItem(Property.get(Constants.SALE_QUOTE, View.wfmStrings.salesQuote()), "icon-send-sales-invoice");
                    convertToSQ.setCommand(() -> {
                        convertToSQ.closeAll(menuBar);
                        if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("salequote|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Accounting.html#salequote|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }

                    });
                    convertToSQ.ensureDebugId("convert_SQ");
                    convertMenu.addItem(convertToSQ);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_SALEORDER:
                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)) {
                    MenuPopItem convertToSO = new MenuPopItem(Property.get(Constants.SALE_ORDER_CODE, View.wfmStrings.saleorder()), "icon-send-sales-invoice");
                    convertToSO.setCommand(() -> {
                        convertToSO.closeAll(menuBar);
                        if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Accounting.html#saleorder|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }

                    });
                    convertToSO.ensureDebugId("convert_SO");
                    convertMenu.addItem(convertToSO);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_PURCHASE_ORDER:
                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_PURCHASE_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)) {
                    MenuPopItem convertToPO = new MenuPopItem(Property.get(Constants.PURCHASE_ORDER, View.wfmStrings.purchaseorder()), "icon-send-sales-invoice");
                    convertToPO.setCommand(() -> {
                        convertToPO.closeAll(menuBar);
                        if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Accounting.html#purchaseorder|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }
                    });
                    convertToPO.ensureDebugId("convert_PO");
                    convertMenu.addItem(convertToPO);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_PURCHASE_INVOICE:
                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_PURCHASE_INVOICE_ADD)) : Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD)) {
                    MenuPopItem convertToPI = new MenuPopItem(Property.get(Constants.PURCHASE_INVOICE, View.wfmStrings.purchaseinvoice()), "icon-send-sales-invoice");
                    convertToPI.setCommand(() -> {
                        convertToPI.closeAll(menuBar);
                        if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Accounting.html#purchaseinvoice|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }
                    });
                    convertToPI.ensureDebugId("convert_PO");
                    convertMenu.addItem(convertToPI);
                    convertItems++;
                }
                break;
            case RelationItem.TYPE_VACANCY:
                if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(HRMS_ADD_VACANCY)) : Utils.hasPermission(HRMS_QUICK_ADD_VACANCY)) {
                    MenuPopItem convertToVacancy = new MenuPopItem(Property.get(Constants.VACANCY, View.wfmStrings.vacancy()), "icon-send-sales-invoice");
                    convertToVacancy.setCommand(() -> {
                        convertToVacancy.closeAll(menuBar);
                        if (Utils.getPathName().contains("Hrms.html") || Utils.getPathName().contains("Crm.html")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        } else {
                            Utils.openURL("Hrms.html#vacancy|edit/CONVERT/" + this.formID + "/" + rowValue.getObjectID());
                        }
                    });
                    convertToVacancy.ensureDebugId("convert_vacancy");
                    convertMenu.addItem(convertToVacancy);
                    convertItems++;
                }
                break;

        }
        return convertItems;
    }

    private void deleteItem(final Integer objectID) {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(View.wfmStrings.confirmation());
        messageBox.setMessage(View.wfmStrings.messAreDelete());
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                CommonService.App.get().deleteCustomFormItem(objectID, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(final Throwable throwable) {
                        LoadingPanel.loading(false);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void success(final Void result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.customField()), Info.Type.INFO);
                        CustomFormItemListView.this.listingTable.reloadPage();
                        checkQuota(true);
                    }
                });
            }
        });
        messageBox.open();
    }

    private ListingRequestProvider<FormItems> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            filterParameter.setParentID(this.fid);
            filterParameter.setForm(this.formID);
            filterParameter.setLookUpBy(this.lookUpType);
            filterParameter.setEntityID(this.lookUpTypeId);
            this.initCustomFormItems(filterParameter, callback, null);
        };
    }

    private void initCustomFormItems(final ListingFilterParameter filterParameter, final ListingCallback<FormItems> callback, final Span container) {
        CommonService.App.get().getCustomFormItems(filterParameter, new AbstractAsyncCallback<ListResult<FormItems>>() {
            @Override
            public void failure(final Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(final ListResult<FormItems> locationList) {
                if (callback != null) {
                    callback.onSuccess(locationList);
                }
                CustomFormItemListView.this.statisticShortcut = CustomFormItemListView.this.statisticShortcut != null ? CustomFormItemListView.this.statisticShortcut : container;
                if (CustomFormItemListView.this.statisticShortcut != null) {
                    if (locationList.getTotal() != null && locationList.getTotal() > 0) {
                        CustomFormItemListView.this.statisticShortcut.setText(CustomFormItemListView.this.countFormat(locationList.getTotal()));
                        CustomFormItemListView.this.statisticShortcut.setClass("tab-label");
                    } else {
                        CustomFormItemListView.this.statisticShortcut.setText("");
                        CustomFormItemListView.this.statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            final ListingFilterParameter fp = CustomFormItemListView.this.listingTable.getFilterParametrs();
                            fp.setParentID(CustomFormItemListView.this.fid);
                            fp.setLookUpBy(CustomFormItemListView.this.lookUpType);
                            fp.setEntityID(CustomFormItemListView.this.lookUpTypeId);
                            RbacService.App.get().getCustomFormItemFacetFilterData(fp, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(final Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(final FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return CustomFormItemListView.this.getFacetContentConfigure();
                    }
                };
            }

            @Override
            public Integer getTypeParentId() {
                return CustomFormItemListView.this.fid;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(CustomFormItemListView.this.formID + "_EDIT_" + Utils.getCompanyID());
            }

            @Override
            public Widget getAddAdditionalPanel() {
                HorizontalPanelDiv panel = new HorizontalPanelDiv();
                panel.setSpacing(5);
                panel.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
                return panel;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_ADD_" + Utils.getCompanyID())) {
                    addNew = CustomFormItemListView.this.getAddNewButton();
                    if (hasQuota == 0) {
                        actionAddClickHandler(addNew);
                    } else {
                        if (hasQuota == -1) {
                            handlerRegistration = addNew.addClickHandler(event -> Info.warn(wfmStrings.youDontHavePermission()));

                        } else if (hasQuota == -2) {
                            handlerRegistration = addNew.addClickHandler(event -> Info.warn(wfmStrings.dontHaveQuotaToAdd()));
                        }
                    }
                }
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(final ExportImportOption exportOption, final MaterialDropDown menuContainer) {
                boolean hasPermissionForExport = Utils.hasPermission(CustomFormItemListView.this.formID + "_EXPORT_" + Utils.getCompanyID());
                exportOption.initExport(null, hasPermissionForExport);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(final ListingEmptyDataInitializer emptyDataTable) {
                final DefaultNoItemsMessage message = new DefaultNoItemsMessage(View.wfmMessages.currentlyDonotHaveAny(CustomFormItemListView.this.name) + "s.");
                message.setTextBeforeLink(View.wfmMessages.addingByClicking(CustomFormItemListView.this.name));
                if (Utils.hasPermission(CustomFormItemListView.this.formID + "_ADD_" + Utils.getCompanyID()) && hasQuota == 0) {
                    message.setHref(Constants.ITEM_LIST + "|add/add/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name);
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        final FacetContentConfigure contentConfigure = new FacetContentConfigure(2, View.wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.CustomFormItemFacetFilter.getContentCode()[0], View.wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCustomFormConst.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCustomFormConst.FIELD_CREATOR_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CustomFormItemFacetFilter.getContentCode()[1], View.wfmStrings.modifiedBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCustomFormConst.FIELD_UPDATER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCustomFormConst.FIELD_UPDATER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CustomFormItemFacetFilter.getContentCode()[2], View.wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCustomFormConst.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCustomFormConst.FIELD_STATUS_ID_NAME;
            }
        });

        contentConfigure.addContentConfigureDateListBox(SolrCustomFormConst.FIELD_CREATED_DATE, View.wfmStrings.createdDate());
        contentConfigure.addContentConfigureDateListBox(SolrCustomFormConst.FIELD_UPDATED_DATE, View.wfmStrings.modifiedDate());
        return contentConfigure;
    }


    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(final Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(CustomFormItemListView.this.onInitialize());
            }
        });
    }

    private void generatePDF(final HTMLPanel panel, final Integer fid, final Integer objectID, final Integer templateId) {
        final CustomFormItemRequestObject requestObject = new CustomFormItemRequestObject(objectID);
        final HashMap<String, String> parameters = requestObject.getRequestParams();
        if (fid != null) {
            parameters.put("fid", String.valueOf(fid));
        }
        if (templateId != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateId));
        }
        final String pdfURL = CommandConstants.PDF_URL + "/customFormItemViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void saveCustomFormItemCellValue(final FormItems rowValue, final String columnCodeName) {
        CommonService.App.get().saveCustomFormItemCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }

    @Override
    public void initStatistics(final Integer parentId, final Span container) {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        fp.setParentID(parentId != null ? parentId : this.fid);
        fp.setForm(this.formID);
        fp.setLookUpBy(this.lookUpType);
        fp.setEntityID(this.lookUpTypeId);
        this.initCustomFormItems(fp, null, container);
    }

    private void checkQuota(boolean isUpdate) {
        AllInOneService.App.get().checkCustomFormQuota(formID, isUpdate, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Integer aBoolean) {
                hasQuota = aBoolean;
                if (isUpdate) {
                    ActionButton addNew = listingTable.getAddNewButton();
                    handlerRegistration.removeHandler();
                    if (hasQuota == 0) {
                        actionAddClickHandler(addNew);
                    } else {
                        handlerRegistration = addNew.addClickHandler(event -> Info.warn(wfmStrings.dontHaveQuotaToAdd()));
                    }
                }
            }
        });
    }

    private void actionAddClickHandler(ActionButton addNew) {
        if (pageRpcItem != null && pageRpcItem.getActive()) {
            if (CustomFormItemListView.this.lookUpType != null && CustomFormItemListView.this.lookUpTypeId != null) {
                handlerRegistration = addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add//" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/intro/" + CustomFormItemListView.this.name + "/" + CustomFormItemListView.this.lookUpType + "/" + CustomFormItemListView.this.lookUpType));
            } else {
                handlerRegistration = addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/intro/" + CustomFormItemListView.this.name));
            }
        } else {
            if (CustomFormItemListView.this.lookUpType != null && CustomFormItemListView.this.lookUpTypeId != null) {
                handlerRegistration = addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add//" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name + "/" + CustomFormItemListView.this.lookUpType + "/" + CustomFormItemListView.this.lookUpType));
            } else {
                handlerRegistration = addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + CustomFormItemListView.this.fid + "/" + CustomFormItemListView.this.formID + "/" + CustomFormItemListView.this.name));
            }
        }
    }
}
