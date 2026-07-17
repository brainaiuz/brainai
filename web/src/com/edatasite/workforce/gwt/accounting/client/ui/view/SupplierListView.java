package com.edatasite.workforce.gwt.accounting.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.client.client.localization.ClientMessages;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.client.client.ui.view.ClientSupplierRemovePopup;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSupplierRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.CrmAccountCoreListView;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.accounting.client.AccountingUtils.accountingStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 6/2/11
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierListView extends CrmAccountCoreListView implements FeatureConstants {
    private static final ClientMessages clientMessages = ClientMessages.App.get();
    private static final ClientServiceAsync clientService = ClientService.App.get();
    private static final AccountingStrings invoiceString = AccountingStrings.App.get();

    private Integer parentId;

    public SupplierListView() {
        super(SUPPLIER_LIST);
        setDescription(property.getPlural(wfmStrings.supplierCenter()));
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD)) {
            setAddNew(() -> new CusSuppQuickAddView(CrmAccountItem.SUPPLIER, null));
        }
    }


    public SupplierListView(Integer parentId){
        this();
        setDescription(wfmStrings.branches());
        this.parentId = parentId;
    }

    public void refresh() {
        list.reloadPage();
    }

    @Override
    protected ListingPanel initializeList() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, SupplierListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, SupplierListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(SupplierListView.this, (sender, args) -> list.reloadPage(), WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, WfmUiEventType.ON_PURCHASEINVOICE_ADDED);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_IMPORT_RELOAD_PAGE, SupplierListView.this, (sender, args) -> list.reloadPage());
        KpiCheckBox activeCheckBox = new KpiCheckBox("&nbsp; " + accountingStrings.showAll(), true);
        activeCheckBox.addValueChangeHandler(event -> {
            list.getFilterParametrs().setActive(!event.getValue());
            list.reloadPage();
        });
        list = new GuideListingPanel(getListPanelType(), getColumns(), getRequestProvider(), getPanelDesigner(), getSelectionPolicy());
        HorizontalPanelDiv divPanel = new HorizontalPanelDiv();
        divPanel.add(activeCheckBox);
        divPanel.setStyleName("showAllCheckBox file--SupplierListView");
        list.getAdvancedFilterPanel().add(divPanel);
        list.getAdvancedFilterPanel().setVisible(true);

        return list;
    }

    @Override
    protected Command getAddNewAction() {
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD)) {
            return () -> SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
        } else {
            return null;
        }
    }

    @Override
    protected ListPanelType getListPanelType() {
        return ListPanelType.SupplierListPanel;
    }

    @Override
    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<CrmAccountItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(CrmAccountItem rowValue) {
                return getActions(rowValue);
            }
        };
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Number
        column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(property.getShortForNumber(wfmStrings.number()), CrmAccountItem.ACCOUNT_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(CrmAccountItem item) {
                return getLink(item.getNumber(), "suppliersummary|summary/" + item.getObjectId(), item.getNumber(), item.getName());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(100);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.ACCOUNT_NUMBER));
        columns.add(column);
        //Name
        column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.name(), CrmAccountItem.ACCOUNT_NAME, 210) {
            @Override
            public SimpleLink getCellValue(CrmAccountItem item) {
                return getLink(item.getName(), "suppliersummary|summary/" + item.getObjectId(), item.getNumber(), item.getName());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(210);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.ACCOUNT_NAME));
        columns.add(column);
        //Contact
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()), CrmAccountItem.CONTACT_NAME, 210) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getPrimaryContactName();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(210);
        column.setShow(false);
        columns.add(column);
        //Country
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.country(), CrmAccountItem.COUNTRY, 95) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getDefaultAddress(true).getCountry();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.COUNTRY));
        columns.add(column);
        //Email
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.email(), CrmAccountItem.EMAIL, 210) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getEmail();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(210);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.EMAIL));
        columns.add(column);
        //Balance
        column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.balance(), CrmAccountItem.SUPPLIER_BALANCE, 150) {
            @Override
            public SimpleLink getCellValue(final CrmAccountItem item) {
                if (item.getSupplierBalance() != null) {
                    return getLink(item.getSupplierBalance() >= 0 ? AccountingUtils.get().formatPrice(item.getSupplierBalance()) : "(" + AccountingUtils.get().formatPrice((-1) * item.getSupplierBalance()) + ")", "supplierBalance|supplierBalance/" + item.getObjectId().toString() + "/" + CrmAccountItem.SUPPLIER, item.getNumber(), item.getName());
                }
                return null;
            }
        };
        column.addStyleAttribute("padding-rigth", "5px");
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.SUPPLIER_BALANCE));
        columns.add(column);
        //Vat Number
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.vatNumber(), CrmAccountItem.VAT_NUMBER, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                String vatNumber = !Utils.isNullOrEmpty(item.getTrn()) ? item.getTrn() : item.getVatNumber();
                return vatNumber;
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.VAT_NUMBER));
        columns.add(column);
        //Phone
        /*column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.phoneNumber(), CrmAccountItem.PHONE, 95) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return (new PhoneNumber(item.getPhone())).toString();
            }
        };*/
        column = new ColumnDefinitionConfig<CrmAccountItem, Div>(wfmStrings.phone(), CrmAccountItem.PHONE, 90) {
            @Override
            public Div getCellValue(final CrmAccountItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPhone(), RelationItem.TYPE_CRM_ACCOUNT, rowValue.getObjectId(), rowValue.getName(), false, true);
                return phonePopup.getPhoneWidget();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.PHONE));
        columns.add(column);
        //Address
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.billingStreet1(), CrmAccountItem.BILLING_ADDRESS, 140) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getDefaultAddress(true).getAddress();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.BILLING_ADDRESS));
        columns.add(column);
        //Owner
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.accountEmployee(), CrmAccountItem.OWNER, 140) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getOwnerNames();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.OWNER));
        columns.add(column);
        //Currency
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.currency(), CrmAccountItem.CURRENCY, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return refactor(item.getCurrency());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.CURRENCY));
        columns.add(column);
        //Payment Method
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.paymentMethod(), CrmAccountItem.PAYMENT_METHOD, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return refactor(item.getPaymentMethod());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.PAYMENT_METHOD));
        columns.add(column);
        //Blocked
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.status(), CrmAccountItem.STATUS, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return item.isBlocked() ? wfmStrings.blocked() : wfmStrings.active();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.BLOCKED));
        columns.add(column);

        //Created Date
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.createdDate(), CrmAccountItem.CREATION_DATE, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return DateUtils.format(item.getCreatedDate());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.CREATION_DATE));
        columns.add(column);

        //Tax Rate
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.taxRate(), CrmAccountItem.TAX, 100) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getTaxName() != null ? item.getTaxName() : wfmStrings.notAvailable();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.TAX));
        columns.add(column);


        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            //In Target
            column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.inTarget(), CrmAccountItem.IN_TARGET, 100) {
                @Override
                public String getCellValue(final CrmAccountItem item) {
                    return item.isInTarget() ? wfmStrings.yes() : wfmStrings.no();
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
            column.setMinimumColumnWidth(50);
            column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.IN_TARGET));
            columns.add(column);
        }

        return columns.toArray(new CustomColumnDefinitionConfig[0]);
    }

    @Override
    protected boolean hasImportButton() {
        return true;
    }

    @Override
    protected String getImporterLink() {
        return "importsupplier|add/add/";
    }

    @Override
    protected void saveAccountsCellValue(CrmAccountItem rowValue, String columnCodeName) {
        CRMService.App.get().saveClientCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Void result) {
                super.onSuccess(result);
                refresh();
            }
        });
    }

    @Override
    protected ImportTypeEnum getImportType() {
        return ImportTypeEnum.SUPPLIER;
    }

    @Override
    protected String getPDFExporterLink() {
        return "suppliersListPDFHandler";
    }

    @Override
    protected String getExcelExporterLink() {
        return "downloadAccountingSupplierListExcel";
    }

    @Override
    protected FacetContentConfigure getContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[0], wfmStrings.branchOf(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[1], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_OWNER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[3], wfmStrings.industry(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_INDUSTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_INDUSTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[6], wfmStrings.billingCountry(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_COUNTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.COUNTRY;
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[7], wfmStrings.billingState(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_STATE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_STATE_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[9], wfmStrings.taxRate(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSupplierRepresenter.FIELD_TAX_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSupplierRepresenter.FIELD_TAX_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            contentConfigure.addContentConfigure(FacetContentType.SupplierFacetFilter.getContentCode()[8], wfmStrings.inTarget(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrSupplierRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrSupplierRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        contentConfigure.addContentConfigureDateListBox(SolrClientRepresenter.FIELD_CREATED_DATE, wfmStrings.createdDate());
        return contentConfigure;
    }

    @Override
    protected FacetCallbackProvider getFacetFilterCallbackProvider() {
        return (data, callback) -> {
            data.setName(Utils.isLogistics() ? LayoutRPC.LOGISTICS_SECTION : LayoutRPC.ACCOUNTING_SECTION);
            RbacService.App.get().getAccountingFacetFilterData(CrmConstants.SUPPLIER, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(FacetFilterRpc result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private Anchor getActions(final CrmAccountItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_SUMMARY : ACCOUNTING_SUPPLIER_SUMMARY)) {
            MenuPopItem supplierSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-client-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + item.getObjectId(), item.getNumber(), item.getName()));
            actionItemCount++;
            menuBar.addItem(supplierSummary);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_EDIT : ACCOUNTING_SUPPLIER_EDIT)) {
            MenuPopItem supplierEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("supplier|addSupplier/" + item.getObjectId(), item.getNumber(), item.getName()));
            actionItemCount++;
            menuBar.addItem(supplierEdit);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_ADD : ACCOUNTING_PURCHASE_ORDER_ADD)) {
            MenuPopItem addPurchaseOrder = new MenuPopItem(Property.get(Constants.PURCHASE_ORDER, wfmStrings.addMess(), wfmStrings.purchaseorder()), "icon-sales-quote-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|add/add/fromSupplierList/" + item.getObjectId()));
            actionItemCount++;
            menuBar.addItem(addPurchaseOrder);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_ADD : ACCOUNTING_PURCHASE_INVOICE_ADD)) {
            MenuPopItem addPurchaseInvoice = new MenuPopItem(Property.get(Constants.PURCHASE_INVOICE, invoiceString.addPurchaseInvoice(), wfmStrings.purchaseinvoice()), "icon-puchase-invoise-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|add/add/fromSupplierList/" + item.getObjectId()));
            actionItemCount++;
            menuBar.addItem(addPurchaseInvoice);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            final MenuPopItem sendToTarget = new MenuPopItem(accountingStrings.sendToTarget(), "icon-sales-quote-small", () -> {
                LoadingPanel.loading(true);
                TargetErpService.App.get().sendClientToTarget(item.getObjectId(), false, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(String s) {
                        LoadingPanel.loading(false);
                        if (s != null && !s.startsWith("OK")) {
                            Info.show(s, Info.Type.WARNING);
                        } else {
                            Info.show(s.replaceFirst("OK:", ""), Info.Type.INFO);
                        }
                    }
                });
            });
            sendToTarget.setEnabled(!item.isInTarget());
            actionItemCount++;
            menuBar.addItem(sendToTarget);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_DELETE : ACCOUNTING_SUPPLIER_DELETE)) {
            MenuPopItem blockSupplier = new MenuPopItem(item.isBlocked() ? wfmStrings.unblock() : wfmStrings.block(), "icon-edit-subscriptions", () -> {
                LoadingPanel.loading(true);
                clientService.blockAccount(item.getObjectId(), item.isBlocked(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        refresh();
                    }
                });
            });
            actionItemCount++;
            menuBar.addItem(blockSupplier);
        }
        if (Utils.hasPermission(CLIENT_SEND_SMS)) {
            MenuPopItem addSendSms = new MenuPopItem(wfmStrings.sendSms(),"icon-sms");
            addSendSms.ensureDebugId("send_Sms");
            addSendSms.setCommand(() -> {
                addSendSms.closeAll(menuBar);
                new ActivityQuickAddForm(Appointment.SMS, item.getPhone(), null, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
            });
            actionItemCount++;
            menuBar.addItem(addSendSms);
        }
        boolean hasAccountingBeforeBlockDate = (item.getSupplierBalanceDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getSupplierBalanceDate().getNonConvertedDate()));
        if (!hasAccountingBeforeBlockDate && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_DELETE : ACCOUNTING_SUPPLIER_DELETE)) {
            MenuPopItem deleteSupplier = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile", () -> new ClientSupplierRemovePopup(item.getObjectId(), item.getName(), SUPPLIER_STR, () -> list.reloadPage()));
            actionItemCount++;
            menuBar.addItem(deleteSupplier);
        }

        final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    @Override
    protected ListingRequestProvider<CrmAccountItem> getRequestProvider() {
        return (filterParametrs, callback) -> {
            loadSupplierList(filterParametrs, callback, null);
        };
    }


    private void loadSupplierList(ListingFilterParameter fp, ListingCallback<CrmAccountItem> contactCallback, Span container){
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setActive(false);
        fp.setParentID(parentId);
        fp.setModule(Utils.isLogistics() ? LayoutRPC.LOGISTICS_SECTION : LayoutRPC.ACCOUNTING_SECTION);
        clientService.getSuppliers(fp, new AbstractAsyncCallback<SupplierList>() {
            @Override
            public void failure(Throwable throwable) {
                contactCallback.onFailure(throwable);
            }

            @Override
            public void success(SupplierList list) {
                totalCount = list.getTotal();
                if (contactCallback != null) {
                    contactCallback.onSuccess(list);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
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
    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    protected boolean isShowExport() {
        return Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_EXPORT : PermissionConstants.ACCOUNTING_SUPPLIER_EXPORT);
    }

    @Override
    protected boolean hasPermissionToEdit() {
        return Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_EDIT : ACCOUNTING_SUPPLIER_EDIT);
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        CrmAccountItem item = selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = CrmAccountItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    clientService.deleteClientsOrSuppliers(ids, false, false, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_DELETED, result, SupplierListView.this);
                            showItemsDeletedMessageBox(result);
                            list.reloadPage();
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    public static void showItemsDeletedMessageBox(List<Integer> result) {
        String message = result != null && result.size() > 0 ? (Property.getPluralWithObjectCodeWithReplace(Constants.SUPPLIER_LIST, clientMessages.successfullyDeletedButSomeNotSuppliers("" + result.size()), wfmStrings.suppliers())) : Property.getPluralWithObjectCodeWithReplace(Constants.SUPPLIER_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.suppliers());
        Info.show(message, Info.Type.INFO);
    }

    @Override
    protected MenuBar getActionsForSelections() {
        if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                addActionsToActionButton(actions);

            }
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                addActionsToActionButton(emptyActions);
            }
            return emptyActions.getMenuBar();
        }
    }

    private void addActionsToActionButton(ContextMenu actionMenu) {
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PAY_BILL : ACCOUNTING_PAY_BILL)) {
            actionMenu.addMenuItem(Property.get(Constants.PAYBILLS_LIST, invoiceString.payBill()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + PAYABLE));
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_CREDIT_ADD : ACCOUNTING_SUPPLIER_CREDIT_ADD)) {
            actionMenu.addMenuItem(Property.get(Constants.SUPPLIER_LIST, invoiceString.addSupplierCredit(), wfmStrings.supplier()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCredit|add/add/"));
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_CREDIT_LIST : ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
            actionMenu.addMenuItem(invoiceString.prepaymentsList(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCreditList|supplierCredits/"));
        }
    }

    @Override
    protected ActionButton initializeTopMenuNew() {

        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD)) {
            MenuPopItem addNew = new MenuPopItem(property.getSingular(wfmStrings.supplier()));//supplierInformation() specially done cause russian version is different
            addNew.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add"));
            menu.addItem(addNew);
        }

        if (Utils.hasPermission(ACCOUNTING_SUPPLIER_QUICK_ADD)) {
            MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());//quickAddForSupplier() specially done cause russian version is different
            quick.ensureDebugId("new_supplier");
            quick.setCommand(() -> new CusSuppQuickAddView(CrmAccountItem.SUPPLIER, null));
            menu.addItem(quick);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD) && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PAY_BILL : ACCOUNTING_PAY_BILL)) {
            MenuPopItem payBill = new MenuPopItem(Property.get(Constants.PAYBILLS_LIST, invoiceString.payBill()));
            payBill.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + PAYABLE));
            menu.addItem(payBill);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD) && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_CREDIT_ADD : ACCOUNTING_SUPPLIER_CREDIT_ADD)) {
            MenuPopItem supplierPrepayment = new MenuPopItem(property.getSingular(invoiceString.supplierPrepayment(), wfmStrings.supplier()));
            supplierPrepayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCredit|add/add/"));
            menu.addItem(supplierPrepayment);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD) && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_CREDIT_LIST : ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
            MenuPopItem prepaymentList = new MenuPopItem(invoiceString.prepaymentsList());
            prepaymentList.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCreditList|supplierCredits/"));
            menu.addItem(prepaymentList);
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_ADD : ACCOUNTING_SUPPLIER_ADD) || Utils.hasPermission(ACCOUNTING_SUPPLIER_QUICK_ADD)) {
            newItem.setMenu(menu);
            return newItem;
        }
        return null;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setPropertyCode(getPropertyCode());
        fp.setLimit(1);
        loadSupplierList(fp, null, container);
    }

    @Override
    protected VerticalPanel getEmptyDataTable() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(new Label(property.getPlural(wfmStrings.noSupplierText(), wfmStrings.suppliers())));
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label(property.getPlural(wfmStrings.noSupplierLink(), wfmStrings.suppliers())));
        SimpleLink simpleLink = new SimpleLink(" " + wfmStrings.here());
        simpleLink.addClickHandler(clickEvent -> new CusSuppQuickAddView(CrmAccountItem.SUPPLIER, null));
        simpleLink.setStyleName("addLinkStyle");
        hp.add(simpleLink);
        vp.add(hp);
        return vp;
    }

    public String getIconStyle() {
        return "accountMark supplier-list";
    }

    @Override
    public char getAccessKey() {
        return 's';
    }

    public interface InternalImages extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/accounting/client/bundles/icons/suppliers.gif")
        ImageResource addSuppliers();

        @Source("com/edatasite/workforce/gwt/messagecenter/public/images/addInvoice.png")
        ImageResource addNew();
    }

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
        return Constants.SUPPLIER_LIST;
    }
}
