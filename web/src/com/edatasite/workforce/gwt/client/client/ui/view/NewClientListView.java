package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.client.client.localization.ClientMessages;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
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
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
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
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
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
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.CrmAccountCoreListView;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 1:35:27 PM To
 * change this template use File | Settings | File Templates.
 */
public class NewClientListView extends CrmAccountCoreListView implements Constants, FeatureConstants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ClientMessages clientMessages = ClientMessages.App.get();
    private static final ClientServiceAsync clientService = ClientService.App.get();
    private final ImportFilePopUp imp = new ImportFilePopUp(getImportType(), null);
    private final boolean isAccountingSection;

    public NewClientListView(boolean isAccountingSection) {
        super(CLIENT_LIST);
        setDescription(property.getPlural(wfmStrings.customers()));
        this.isAccountingSection = isAccountingSection;
        if (Utils.hasPermission(ACCOUNTING_CUSTOMER_QUICK_ADD)) {
            setAddNew(() -> new CusSuppQuickAddView(CrmAccountItem.CUSTOMER, null));
        } else if (hasPermissionToCreateClient()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add"));
        }
    }

    public FlowPanel getHelpContainer() {
        if (isAccountingSection) {
            return null;
        } else {
            return HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_CUSTOMER_CENTER_LIST);
        }
    }

    @Override
    protected ListingPanel initializeList() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewClientListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(NewClientListView.this, (sender, args) -> refresh(), WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, WfmUiEventType.ON_SALEINVOICE_ADDED);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_SAVE, NewClientListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOMER_IMPORT_RELOAD_PAGE, NewClientListView.this, (sender, args) -> list.reloadPage());

        return new GuideListingPanel(getListPanelType(), getColumns(), getRequestProvider(), getPanelDesigner(), getSelectionPolicy());
    }


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

    protected GuideListingPanelDesign getPanelDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateClient() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasImportButton() ? imp::open : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return getFacetFilterCallbackProvider();
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return initializeTopMenuNew();
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.isPM() && Utils.hasPermission(PermissionConstants.PM_CUSTOMER_REMOVE_CLIENT)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                } else if (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CUSTOMER_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo(getImporterLink() + imp.getObjectId());
                    }
                });
                if (hasImportButton()) {
                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                }
                exportOption.initExport(null, isShowExport());
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage() {
                    @Override
                    public VerticalPanel getWholeMessage() {
                        return getEmptyDataTable();
                    }
                });
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return (Utils.adminOrDirector() || Utils.hasRole(Constants.SALESMAN) || Utils.hasRole(Constants.SALESPERSON) || isAccountingSection ? Utils.hasPermission(ACCOUNTING_CUSTOMER_EDIT) : Utils.hasPermission(PermissionConstants.PM_CUSTOMER_EDIT));
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CUSTOMIZE_LIST);
            }
        };
    }

    @Override
    protected ListPanelType getListPanelType() {
        return ListPanelType.ClientListPanel;
    }

    public void refresh() {
        list.reloadPage();
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<CrmAccountItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(CrmAccountItem rowValue) {
                return getActions(rowValue);
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Number
        column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(property.getShortForNumber(wfmStrings.number()), CrmAccountItem.ACCOUNT_NUMBER, 120) {
            @Override
            public SimpleLink getCellValue(CrmAccountItem item) {
                return getLink(item.getNumber(), "client|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + item.getName(), item.getNumber(), item.getName());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(100);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.ACCOUNT_NUMBER));
        columns.add(column);
        //Name
        column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.name(), CrmAccountItem.ACCOUNT_NAME, 250) {
            @Override
            public SimpleLink getCellValue(CrmAccountItem item) {
                return getLink(item.getName(), "client|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + item.getName(), item.getNumber(), item.getName());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(200);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.ACCOUNT_NAME));
        columns.add(column);
        //Contact Name
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()), CrmAccountItem.CONTACT_NAME, 200) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getPrimaryContactName();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(150);
        column.setShow(false);
        columns.add(column);
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.expiryDate(), CrmAccountItem.INVOICE_EXPIRE_DATE, 200) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return DateUtils.format(item.getInvoiceExpireDate());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.paidStatus(), CrmAccountItem.INVOICE_PAID_STATUS, 200) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getInvoicePaidStatus();
            }
        };
        columns.add(column);
        //Parent Name
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.parentaccount(), CrmAccountItem.PARENT_ACCOUNT_NAME, 200) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return refactor(item.getParent() != null ? item.getParent().getName() : null);
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(150);
        column.setShow(false);
        columns.add(column);
        //Email
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.email(), CrmAccountItem.EMAIL, 200) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getEmail();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(150);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.EMAIL));
        columns.add(column);
        //Phone
       /* column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.phoneNumber(), CrmAccountItem.PHONE, 95) {
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
        columns.add(column);

        if (isAccountingSection) {
            column = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.balance(), CrmAccountItem.CLIENT_BALANCE, 100) {
                @Override
                public SimpleLink getCellValue(final CrmAccountItem item) {
                    if (item.getClientBalance() != null) {
                        SimpleLink balanceLink;
                        if (item.getClientBalance() >= 0) {
                            balanceLink = new SimpleLink(AccountingUtils.get().formatPrice(item.getClientBalance()));
                        } else {
                            balanceLink = new SimpleLink("(" + AccountingUtils.get().formatPrice((-1) * item.getClientBalance()) + ")");
                        }
                        balanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + item.getObjectId().toString() + "/" + CrmAccountItem.CUSTOMER, item.getNumber(), item.getName()));
                        return balanceLink;
                    }
                    return null;
                }
            };
            column.addStyleAttribute("padding-right", "5px");
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.setMinimumColumnWidth(50);
            column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.CLIENT_BALANCE));
            columns.add(column);

            column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.creditLimit(), CrmAccountItem.CREDIT_LIMIT, 100) {
                @Override
                public String getCellValue(final CrmAccountItem item) {
                    if (item.getCreditLimit() != null) {
                        String balanceLink = "";
                        if (item.getCreditLimit().compareTo(BigDecimal.ZERO) >= 0) {
                            balanceLink = AccountingUtils.get().formatPrice(item.getCreditLimit());
                        } else {
                            balanceLink = "(" + AccountingUtils.get().formatPrice((-1) * item.getCreditLimit().doubleValue()) + ")";
                        }
                        return balanceLink;
                    }
                    return null;
                }
            };
            column.addStyleAttribute("padding-right", "5px");
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.setMinimumColumnWidth(50);
            column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.CREDIT_LIMIT));
            columns.add(column);
        }
        //Country
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.country(), CrmAccountItem.COUNTRY, 95) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getDefaultAddress(true).getCountry();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.COUNTRY));
        columns.add(column);
        //Owner
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.accountEmployee(), CrmAccountItem.OWNER, 140) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                //return item.getOwnerName();
                return item.getOwnerNames();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.OWNER));
        columns.add(column);
        //Fax
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.fax(), CrmAccountItem.FAX, 90) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return new PhoneNumber(item.getFax()).toString();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(45);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.FAX));
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
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.CURRENCY));
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
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.VAT_NUMBER));
        columns.add(column);
        //Payment Method
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.paymentMethod(), CrmAccountItem.PAYMENT_METHOD, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return refactor(item.getPaymentMethod());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.PAYMENT_METHOD));
        columns.add(column);
        //Terms
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.terms(), CrmAccountItem.TERMS, 100) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getTermName() != null ? item.getTermName() : wfmStrings.notAvailable();
            }
        };
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.TERMS));
        columns.add(column);
        //Contact Email
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(Property.get(Constants.Contacts, wfmStrings.email(), wfmStrings.contact()), CrmAccountItem.CONTACT_EMAIL, 100) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getPrimaryContactEmail() != null ? item.getPrimaryContactEmail() : wfmStrings.notAvailable();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.CONTACT_EMAIL));
        columns.add(column);
        //Bank Account
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount()), CrmAccountItem.BANK_ACCOUNT, 100) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getBankName() != null ? item.getBankName() : wfmStrings.notAvailable();
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.BANK_ACCOUNT));
        columns.add(column);
        //Tax
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

        //Created Date
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.createdDate(), CrmAccountItem.CREATION_DATE, 100) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return DateUtils.format(item.getCreatedDate());
            }
        };
        column.addStyleAttribute("padding-left", "5px");
        column.setMinimumColumnWidth(70);
        column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.CREATION_DATE));
        columns.add(column);

        //Blocked
        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.status(), CrmAccountItem.BLOCKED, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return item.isBlocked() ? wfmStrings.blocked() : wfmStrings.active();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        column.setShow(CrmAccountItem.defaultSupplierColumnNames.contains(CrmAccountItem.BLOCKED));
        columns.add(column);

        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            //In target
            column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.inTarget(), CrmAccountItem.IN_TARGET, 100) {
                @Override
                public String getCellValue(final CrmAccountItem item) {
                    return item.isInTarget() ? wfmStrings.yes() : wfmStrings.no();
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
            column.setMinimumColumnWidth(50);
            column.setShow(CrmAccountItem.defaultClientColumnNames.contains(CrmAccountItem.IN_TARGET));
            columns.add(column);
        }

        column = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.salesType(), CrmAccountItem.SALES_TYPE, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return item.getSalesType();
            }
        };
        column.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.SALES_TYPE));
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(50);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[0]);
    }

    protected boolean isShowExport() {
        return Utils.hasPermission(isAccountingSection ? PermissionConstants.ACCOUNTING_CUSTOMER_EXPORT : PermissionConstants.PM_CUSTOMER_EXPORT);
    }

    @Override
    protected boolean hasImportButton() {
        return Utils.hasPermission(isAccountingSection ? PermissionConstants.ACCOUNTING_CUSTOMER_IMPORT : PermissionConstants.PM_CUSTOMER_IMPORT);
    }

    @Override
    protected String getImporterLink() {
        return "importclient|add/add/";
    }

    @Override
    protected ImportTypeEnum getImportType() {
        return ImportTypeEnum.CUSTOMER;
    }

    @Override
    protected String getPDFExporterLink() {
        return "clientListPDFHandler";
    }

    @Override
    protected String getExcelExporterLink() {
        return "downloadClientListExcel";
    }

    @Override
    protected FacetContentConfigure getContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[1], wfmStrings.owner(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_OWNER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[0], wfmStrings.parentaccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[3], wfmStrings.industry(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_INDUSTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_INDUSTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });


        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[6], wfmStrings.billingCountry(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_COUNTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.COUNTRY;
            }


        });
        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[7], wfmStrings.billingState(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_STATE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_STATE_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[10], wfmStrings.taxRate(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_TAX_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_TAX_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[8], wfmStrings.city(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_CITY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_CITY;
            }

            @Override
            public boolean isWithID() {
                return false;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public boolean isConditionItemId() {
                return true;
            }
        });
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[9], wfmStrings.inTarget(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrClientRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrClientRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[11], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_BLOCKED;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_BLOCKED;
            }

            @Override
            public boolean isWithID() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[12], wfmStrings.salesType(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrClientRepresenter.FIELD_SALES_TYPE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrClientRepresenter.FIELD_SALES_TYPE;
            }

            @Override
            public boolean isWithID() {
                return false;
            }
        });

        contentConfigure.addContentConfigureDateListBox(SolrClientRepresenter.FIELD_CREATED_DATE, wfmStrings.createdDate());
        return contentConfigure;
    }

    @Override
    protected FacetCallbackProvider getFacetFilterCallbackProvider() {
        return (data, callback) -> {
            RbacService.App.get().getAccountingFacetFilterData(CrmConstants.CLIENT, data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
        if (!isAccountingSection || Utils.hasPermission(ACCOUNTING_CUSTOMER_SUMMARY)) {
            MenuPopItem clientSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-client-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + item.getName(), item.getNumber(), item.getName()));
            menuBar.addItem(clientSummary);
        }
        if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_CUSTOMER_EDIT) : Utils.hasPermission(PermissionConstants.PM_CUSTOMER_EDIT)) {
            MenuPopItem clientEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("clientedit|addaccount/" + item.getObjectId(), item.getNumber(), item.getName()));
            menuBar.addItem(clientEdit);
        }
        if (!item.isBlocked() && !item.hasSupplierType() && Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
            MenuPopItem sendSalesInvoice = new MenuPopItem(Property.get(Constants.SALE_INVOICE, crmStrings.sendSalesInvoice(), wfmStrings.salesInvoice()), "icon-send-sales-invoice");
            sendSalesInvoice.getElement().setId("sendSalesInvoice");
            sendSalesInvoice.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/contact/" + item.getObjectId()));
            actionItemCount++;
            menuBar.addItem(sendSalesInvoice);
        }
        if (isAccountingSection && Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_DELETE)) {
            MenuPopItem blockCustomer = new MenuPopItem(item.isBlocked() ? wfmStrings.unblock() : wfmStrings.block(), "icon-edit-subscriptions", () -> {
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
            menuBar.addItem(blockCustomer);
        }
        if (Utils.hasPermission(CLIENT_SEND_SMS)) {
            MenuPopItem addSendSms = new MenuPopItem(wfmStrings.sendSms(), "icon-sms");
            addSendSms.ensureDebugId("send_Sms");
            addSendSms.setCommand(() -> {
                addSendSms.closeAll(menuBar);
                new ActivityQuickAddForm(Appointment.SMS, item.getPhone(), null, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
            });
            actionItemCount++;
            menuBar.addItem(addSendSms);
        }
        boolean hasAccountingBeforeBlockDate = (item.getBalanceDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getBalanceDate().getNonConvertedDate()));
        if (!hasAccountingBeforeBlockDate && (isAccountingSection ? Utils.hasPermission(ACCOUNTING_CUSTOMER_DELETE) : Utils.hasPermission(PermissionConstants.PM_CUSTOMER_REMOVE_CLIENT))) {
            MenuPopItem deleteClient = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--NewClientListView", () -> new ClientSupplierRemovePopup(item.getObjectId(), item.getName(), CLIENT_STR, () -> list.reloadPage()));
            actionItemCount++;
            menuBar.addItem(deleteClient);
        }


        final ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    @Override
    protected ListingRequestProvider<CrmAccountItem> getRequestProvider() {
        return (fp, contactCallback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setPM(!isAccountingSection);
            clientService.getNewClients(fp, new AbstractAsyncCallback<NewClientList>() {
                @Override
                public void failure(Throwable throwable) {
                    contactCallback.onFailure(throwable);
                }

                @Override
                public void success(NewClientList list) {

                    totalCount = list.getTotal();
                    contactCallback.onSuccess(list);
                }
            });
        };
    }

    @Override
    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.customer())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        CrmAccountItem item = selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        KpiCheckBox kpiCheckBox = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.removeFromContactsToo(), wfmStrings.contacts()));

        messageBox.add(kpiCheckBox);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = CrmAccountItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    clientService.deleteClientsOrSuppliers(ids, true, kpiCheckBox.getValue(), new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CLIENT_DELETED, result, NewClientListView.this);
                            showItemsDeletedMessageBox(result /*items.size()*/);
                            refresh();
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private static void showItemsDeletedMessageBox(List<Integer> result) {
        String message = result != null && result.size() > 0 ? (clientMessages.successfullyDeletedButSomeNotClients(String.valueOf(result.size()))) : Property.getPluralWithObjectCodeWithReplace(Constants.CLIENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.customers());
        Info.show(message, Info.Type.INFO);
    }

    @Override
    protected MenuBar getActionsForSelections() {
        if (isAccountingSection) {
            if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
                if (actions == null) {
                    actions = new ContextMenu();
                    actions.getMenuBar().setAutoOpen(false);
                    addActionsToActionButton(actions);
                    if (Utils.hasPermission(ACCOUNTING_CUSTOMER_DELETE)) {
                        actions.addMenuItem(wfmStrings.delete(), true, () -> {
                            actions.hide();
                            deleteSelection();
                        });
                    }
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
        return null;
    }

    private void addActionsToActionButton(ContextMenu actionMenu) {
//        if (Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT)) {
//            actionMenu.addMenuItem(accountingStrings.receivePayment(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + RECEIVABLE));
//        }
//        if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_ADD)) {
//            actionMenu.addMenuItem(property.getSingular(accountingStrings.addCustomerPrepayment(), accountingStrings.customerPrepayment()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|add/add/"));
//        }
//        if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_LIST)) {
//            actionMenu.addMenuItem(accountingStrings.prepaymentsList(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("prepaymentList|prepayments/"));
//        }
    }

    private boolean hasPermissionToCreateClient() {
        return isAccountingSection ? Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD) : Utils.hasPermission(PermissionConstants.PM_CUSTOMER_ADD_CLIENT);
    }

    @Override
    protected ActionButton initializeTopMenuNew() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        if (hasPermissionToCreateClient() && Utils.hasPermission(ACCOUNTING_CUSTOMER_QUICK_ADD)) {
            MenuBar menu = new MenuBar(true);

            MenuPopItem addNew = new MenuPopItem(property.getSingular(wfmStrings.customer()));
            addNew.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add"));
            menu.addItem(addNew);

            MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());
            quick.ensureDebugId("new_client");
            quick.setCommand(() -> new CusSuppQuickAddView(CrmAccountItem.CUSTOMER, null));
            menu.addItem(quick);

            if (Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT)) {
                MenuPopItem receivePayment = new MenuPopItem(accountingStrings.receivePayment());
                receivePayment.ensureDebugId("new_receivePayment");
                receivePayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + RECEIVABLE));
                menu.addItem(receivePayment);
            }
            if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_ADD)) {
                MenuPopItem customerPrepayment = new MenuPopItem(accountingStrings.customerPrepayment());
                customerPrepayment.ensureDebugId("new_customerPrepayment");
                customerPrepayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|add/add/"));
                menu.addItem(customerPrepayment);
            }
            if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_LIST)) {
                MenuPopItem prepaymentsList = new MenuPopItem(accountingStrings.prepaymentsList());
                prepaymentsList.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("prepaymentList|prepayments/"));
                prepaymentsList.ensureDebugId("prepaymentsList");
                menu.addItem(prepaymentsList);
            }
            newItem.setMenu(menu);
            return newItem;
        } else if (hasPermissionToCreateClient()) {
            newItem.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add"));
            return newItem;
        } else if (Utils.hasPermission(ACCOUNTING_CUSTOMER_QUICK_ADD)) {
            newItem.addClickHandler(clickEvent -> new CusSuppQuickAddView(CrmAccountItem.CUSTOMER, null));
            return newItem;
        }
        return null;
    }

    @Override
    protected VerticalPanel getEmptyDataTable() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(new Label(property.getPlural(wfmStrings.noClientText(), wfmStrings.customers())));
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label(property.getPlural(wfmStrings.noClientLink(), wfmStrings.customers())));
        SimpleLink simpleLink = new SimpleLink(" " + wfmStrings.here());
        simpleLink.addClickHandler(clickEvent -> new CusSuppQuickAddView(CrmAccountItem.CUSTOMER, null));
        simpleLink.setStyleName("addLinkStyle");
        hp.add(simpleLink);
        vp.add(hp);
        return vp;
    }

    @Override
    public String getIconStyle() {
        return "newClientList new-client-list";
    }

    @Override
    public char getAccessKey() {
        return 'c';
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
        return Constants.CLIENT_LIST;
    }
}
