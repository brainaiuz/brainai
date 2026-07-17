package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NotePopup;
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
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.ui.CrmAccountCoreListView;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.core.domain.EdsRelation.TYPE_CRM_ACCOUNT;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountListView extends CrmAccountCoreListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private static final ClientServiceAsync clientService = ClientService.App.get();
    private HashMap<Integer, SelectItem[]> stateMap = null;
    private OwnerPopup ownerPopup;
    String accountType = "";

    private final boolean addPermission = Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_ADD);
    private final boolean quickAddPermission = Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_QUICK_ADD);

    public CrmAccountListView() {
        super();
        setDescription(property != null ? property.getPlural(wfmStrings.company()) : wfmStrings.company());
        if (quickAddPermission) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM));
        }
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_ACCOUNTS_LIST);
    }

    @Override
    protected ListingPanel initializeList() {
        return new GuideListingPanel(getListPanelType(), getColumns(), getRequestProvider(), getPanelDesigner(), getSelectionPolicy(), true);
    }

    protected void saveAccountsCellValue(CrmAccountItem rowValue, String columnCodeName) {
        crmService.saveAccountsEditCellView(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
        });
    }

    @Override
    protected ListPanelType getListPanelType() {
        return ListPanelType.CrmAccountListPanel;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        //Action
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<CrmAccountItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(CrmAccountItem rowValue) {
                return getActions(rowValue);
            }
        };
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Account Number
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.number(), CrmAccountItem.ACCOUNT_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(final CrmAccountItem item) {
                if (item.hasSupplierType() && item.hasCustomerType()) {
                    accountType = "SupplierCustomer";
                } else if (item.hasSupplierType()) {
                    accountType = "Supplier";
                } else if (item.hasCustomerType()) {
                    accountType = "Customer";
                }
                if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                    return getLink(item.getNumber(), "account|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + accountType, item.getNumber(), item.getName());
                } else {
                    return getLink(item.getNumber(), null);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.ACCOUNT_NUMBER));
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShowPopup(true);
        columnConfigs.add(columnConfig);
        //Account Name
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.name(), CrmAccountItem.ACCOUNT_NAME, 140) {
            @Override
            public SimpleLink getCellValue(final CrmAccountItem item) {
                if (item.hasSupplierType() && item.hasCustomerType()) {
                    accountType = "SupplierCustomer";
                } else if (item.hasSupplierType()) {
                    accountType = "Supplier";
                } else if (item.hasCustomerType()) {
                    accountType = "Customer";
                }
                if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                    return getLink(item.getName(), "account|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + accountType, item.getNumber(), item.getName());
                } else {
                    return getLink(item.getName(), null);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.ACCOUNT_NAME));
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShowPopup(true);
        columnConfigs.add(columnConfig);
        //Phone
//        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, Div>(wfmStrings.phone(), CrmAccountItem.PHONE, 90) {
            @Override
            public Div getCellValue(final CrmAccountItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPhone(), RelationItem.TYPE_CRM_ACCOUNT, rowValue.getObjectId(), rowValue.getName(), false, true, null, TYPE_CRM_ACCOUNT, null, null);
                return phonePopup.getPhoneWidget();
            }
        };
        /*} else {
            columnConfig = new ColumnDefinitionConfig<CrmAccountItem, HTML>(crmStrings.phone(), CrmAccountItem.PHONE, 90) {
                @Override
                public HTML getCellValue(final CrmAccountItem rowValue) {
                    if (rowValue.getPhone() != null && !"N/A".equalsIgnoreCase(rowValue.getPhone())) {
                        return new HTML("<a href=\"tel:" + rowValue.getPhone() + "\">" + rowValue.getPhone() + "</a>");
                    } else {
                        return new HTML(rowValue.getPhone());
                    }
                }
            };
        }*/
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.PHONE));
        columnConfig.setMinimumColumnWidth(45);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Email
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SimpleLink>(wfmStrings.email(), CrmAccountItem.EMAIL, 120) {
            @Override
            public SimpleLink getCellValue(CrmAccountItem item) {
                if (item.hasSupplierType()) {
                    accountType = "Supplier";
                } else if (item.hasCustomerType()) {
                    accountType = "Customer";
                } else {
                    accountType = "CrmAccount";
                }

                return getLink(item.getEmail(), "emailcompose|add/add/" + item.getEmail() + "/" + accountType + "/" + item.getObjectId() + "/" + item.getName());
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.EMAIL));
        columnConfig.setMinimumColumnWidth(45);
        columnConfigs.add(columnConfig);
        //Owner
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.owners(), CrmAccountItem.OWNER, 140) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                //return item.getOwnerName();
                return item.getOwnerNames();
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.OWNER));
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);
        //Account Type
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.type(), CrmAccountItem.ACCOUNT_TYPE, 120) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return SelectItem.getSelectItemsAsCommaDelimeted(item.getAccountTypes(), true);
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.ACCOUNT_TYPE));
        columnConfig.setMaximumColumnWidth(300);
        columnConfig.setMinimumColumnWidth(45);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Parent
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.parentaccount(), CrmAccountItem.PARENT_ACCOUNT_NAME, 100) {

            @Override
            public String getCellValue(CrmAccountItem item) {
                return refactor(item.getParent() != null ? item.getParent().getName() : null);
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.PARENT_ACCOUNT_NAME));
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);
        //Industry
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SelectItem>(wfmStrings.industry(), CrmAccountItem.INDUSTRY, 100) {
            @Override
            public SelectItem getCellValue(final CrmAccountItem item) {
                return new SelectItem(item.getIndustryID(), item.getIndustry());
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, SelectItem cellValue) {
                rowValue.setIndustryID(getSelectedItemID(cellValue));
                rowValue.setIndustry(getSelectedItemName(cellValue));
                saveAccountsCellValue(rowValue, CrmAccountItem.INDUSTRY);
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.INDUSTRY));
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);
        //Fax
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.fax(), CrmAccountItem.FAX, 90) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return Utils.getPhoneCallFormatForListing(item.getFax());
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, String cellValue) {
                rowValue.setFax(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.FAX));
        columnConfig.setMinimumColumnWidth(45);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Website
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.website(), CrmAccountItem.WEBSITE, 90) {
            @Override
            public String getCellValue(CrmAccountItem item) {
                return item.getWebsite();
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, String cellValue) {
                rowValue.setWebsite(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.WEBSITE));
        columnConfig.setMinimumColumnWidth(45);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Billing Address
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.billingStreet1(), CrmAccountItem.BILLING_ADDRESS, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                Address defAddress = item.getDefaultAddress(true);
                return refactor(defAddress != null ? defAddress.getAddress() : null);
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, String cellValue) {
                if (!isEqual(rowValue.getDefaultAddress(true).getAddress(), cellValue)) {
                    rowValue.getDefaultAddress(true).setAddress(cellValue);
                    saveAccountsCellValue(rowValue, CrmAccountItem.BILLING_ADDRESS);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.BILLING_ADDRESS));
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Country
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SelectItem>(wfmStrings.billingCountry(), CrmAccountItem.COUNTRY, 100) {
            @Override
            public SelectItem getCellValue(final CrmAccountItem item) {
                currentItem = item;
                Address defAddress = item.getDefaultAddress(true);
                return new SelectItem(defAddress.getCountryId(), defAddress.getCountry());
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, SelectItem value) {
                if (SelectItem.isDifferent(value, new SelectItem(rowValue.getDefaultAddress(true).getCountryId(), rowValue.getDefaultAddress(true).getCountry()))) {
                    Address address = rowValue.getDefaultAddress(true);
                    address.setCountry(getSelectedItemName(value));
                    address.setCountryId(getSelectedItemID(value));
                    address.setState(null);
                    address.setPrimary(true);
                    if (address.getObjectID() == null) {
                        rowValue.addAddress(true, address);
                    }
                    saveCellValue(rowValue);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.COUNTRY));
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);
        //Billing State
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, SelectItem>(wfmStrings.billingState(), CrmAccountItem.STATE, 100) {
            @Override
            public SelectItem getCellValue(final CrmAccountItem item) {
                currentItem = item;
                Address defAddress = item.getDefaultAddress(true);
                return new SelectItem(defAddress.getStateId(), defAddress.getState());
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, SelectItem value) {
                if (SelectItem.isDifferent(value, new SelectItem(rowValue.getDefaultAddress(true).getStateId(), rowValue.getDefaultAddress(true).getState()))) {
                    rowValue.getDefaultAddress(true).setState(getSelectedItemName(value));
                    rowValue.getDefaultAddress(true).setStateId(getSelectedItemID(value));
                    saveCellValue(rowValue);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.STATE));
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);
        //Billing City
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.city(), CrmAccountItem.CITY, 70) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                Address defAddress = item.getDefaultAddress(true);
                return refactor(defAddress != null ? defAddress.getCity() : null);
            }

            @Override
            public void setCellValue(CrmAccountItem rowValue, String cellValue) {
                if (!isEqual(rowValue.getDefaultAddress(true).getCity(), cellValue)) {
                    rowValue.getDefaultAddress(true).setCity(cellValue);
                    saveAccountsCellValue(rowValue, CrmAccountItem.CITY);
                }
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.CITY));
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);
        //Blocked
        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.blocked(), CrmAccountItem.BLOCKED, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return item.isBlocked() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.BLOCKED));
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<CrmAccountItem, String>(wfmStrings.salesType(), CrmAccountItem.SALES_TYPE, 100) {
            @Override
            public String getCellValue(final CrmAccountItem item) {
                return item.getSalesType();
            }
        };
        columnConfig.setShow(CrmAccountItem.defaultColumnNames.contains(CrmAccountItem.SALES_TYPE));
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(50);
        columnConfigs.add(columnConfig);

        initCellEdit(columnConfigs);
        return columnConfigs.toArray(new CustomColumnDefinitionConfig[0]);
    }

    private boolean isEqual(String firstValue, String secondValue) {
        if (firstValue == null || "".equals(firstValue.trim())) {
            firstValue = "";
        }
        if (secondValue == null || "".equals(secondValue.trim())) {
            secondValue = "";
        }
        return firstValue.equals(secondValue);
    }

    private Integer getSelectedItemID(SelectItem cellValue) {
        return cellValue != null ? cellValue.getId() : null;
    }

    private String getSelectedItemName(SelectItem cellValue) {
        return cellValue != null ? cellValue.getName() : null;
    }

    private void initCellEdit(ArrayList<CustomColumnDefinitionConfig> columns) {
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_EDIT)) {
            LinkedHashMap<String, CustomColumnDefinitionConfig> mapOfColumns = CustomColumnDefinitionConfig.getEditableColumns(columns);
            if (mapOfColumns.size() > 0) {
                for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : mapOfColumns.entrySet()) {
                    InlineCellEditor widget = null;
                    CustomColumnDefinitionConfig column = entry.getValue();
                    if (CrmAccountItem.INDUSTRY.equals(entry.getKey()) ||
                            CrmAccountItem.CURRENCY.equals(entry.getKey()) ||
                            CrmAccountItem.PAYMENT_METHOD.equals(entry.getKey()) ||
                            CrmAccountItem.COUNTRY.equals(entry.getKey()) ||
                            CrmAccountItem.STATE.equals(entry.getKey()) ||
                            CrmAccountItem.COUNTRY2.equals(entry.getKey()) ||
                            CrmAccountItem.STATE2.equals(entry.getKey())) {
                        widget = new DropDownCellEditor<SelectItem>() {
                            @Override
                            protected SelectItem getValue() {
                                return getListBox().getSelectedItem(true);
                            }

                            @Override
                            protected void setValue(SelectItem cellValue) {
                                getListBox().setAllowFirstItem(true);
                                setItemsAndSelect(getListBox(), entry.getKey(), cellValue);
                                if (cellValue == null || cellValue.getId() == null) {
                                    getListBox().setSelectedNullLabel();
                                } else {
                                    getListBox().setSelected(cellValue);
                                }
                            }
                        };
                    }
                    if (CrmAccountItem.FAX.equals(entry.getKey()) ||
                            CrmAccountItem.WEBSITE.equals(entry.getKey()) ||
                            CrmAccountItem.EMAIL.equals(entry.getKey()) ||
                            CrmAccountItem.VAT_NUMBER.equals(entry.getKey()) ||
                            CrmAccountItem.CITY.equals(entry.getKey()) ||
                            CrmAccountItem.BILLING_ADDRESS.equals(entry.getKey()) ||
                            CrmAccountItem.POST_CODE.equals(entry.getKey()) ||
                            CrmAccountItem.CITY2.equals(entry.getKey()) ||
                            CrmAccountItem.MAILING_ADDRESS.equals(entry.getKey()) ||
                            CrmAccountItem.POST_CODE2.equals(entry.getKey())) {
                        widget = new TextBoxCellEditor<String>() {
                            @Override
                            protected String getValue() {
                                String cellValue = getText();
                                return cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equalsIgnoreCase(cellValue)
                                        ? null
                                        : getText();
                            }

                            @Override
                            protected void setValue(String cellValue) {
                                if (!(cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equalsIgnoreCase(cellValue))) {
                                    setText(cellValue);
                                }
                            }
                        };
                    }
                    if (widget != null) {
                        column.setCellEditor(widget);
                        column.setCellChangesSave((rowValue, columnCodeName) -> saveAccountsCellValue((CrmAccountItem) rowValue, columnCodeName));
                    }
                }
                getQuickSaveData();
            }
        }
    }

    private void setItemsAndSelect(DataListBox listBox, String key, SelectItem selectedItem) {
        if (listBox.getItems() == null || listBox.getItems().length < 1) {
            if (CrmAccountItem.INDUSTRY.equals(key)) {
                listBox.setItems(defaultOne.getIndustries());
            } else if (CrmAccountItem.CURRENCY.equals(key)) {
                listBox.setItems(defaultOne.getCurrencies());
            } else if (CrmAccountItem.PAYMENT_METHOD.equals(key)) {
                listBox.setItems(defaultOne.getPaymentMethods());
            } else if (CrmAccountItem.COUNTRY.equals(key) || CrmAccountItem.COUNTRY2.equals(key)) {
                listBox.setItems(defaultOne.getCountrys());
            }
        }
        if (CrmAccountItem.STATE.equals(key) || CrmAccountItem.STATE2.equals(key)) {
            if (getCurrentItem() != null) {
                SelectItem[] states = stateMap.get(getCurrentItem().getDefaultAddress(CrmAccountItem.STATE.equals(key)).getCountryId());
                listBox.setItems(states == null ? new SelectItem[0] : states);
            }
        }
        listBox.setSelected(selectedItem);
    }

    private void getQuickSaveData() {
        crmService.getStatesByCountryName(new AsyncCallback<HashMap<Integer, SelectItem[]>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<Integer, SelectItem[]> result) {
                stateMap = result;
            }
        });
    }

    private Anchor getActions(final CrmAccountItem item) {
        int actionItemCount = 0;
        final MenuBar actions = new MenuBar(true);
        actions.setAutoOpen(true);

        if (item.hasSupplierType() && item.hasCustomerType()) {
            accountType = "SupplierCustomer";
        } else if (item.hasSupplierType()) {
            accountType = "Supplier";
        } else if (item.hasCustomerType()) {
            accountType = "Customer";
        }
        if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
            MenuPopItem accountSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-accounts", () -> SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + accountType, item.getNumber(), item.getName()));
            accountSummary.getElement().setId("summaryView");
            actionItemCount++;
            actions.addItem(accountSummary);
        }
        if (Utils.hasPermission(CRM_ACCOUNTS_EDIT)) {
            final MenuPopItem accountEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("accountedit|editaccount/" + item.getObjectId(), item.getNumber(), item.getName()));
            accountEdit.getElement().setId("editAccount");
            actionItemCount++;
            actions.addItem(accountEdit);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_COPY)) {
            MenuPopItem copyAccount = new MenuPopItem(wfmStrings.copy(), "icon-copy", () -> SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add/" + item.getObjectId() + "/" + COPY));
            copyAccount.getElement().setId("account");
            actionItemCount++;
            actions.addItem(copyAccount);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_ADD_NOTE, CRM_ACCOUNTS_EDIT)) {
            final MenuPopItem noteItem = new MenuPopItem(wfmStrings.addNote(), "icon-add-node", () -> new NotePopup(item.getObjectId(), RelationItem.TYPE_CRM_ACCOUNT));
            noteItem.getElement().setId("addNote");
            actionItemCount++;
            actions.addItem(noteItem);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CONVERT)) {
            MenuPopItem convertTo = new MenuPopItem(wfmStrings.convert(), "icon-add-green");
            convertTo.getElement().setId("convert");
            final MenuBar convertSubMenu = new MenuBar(true);
            convertSubMenu.setAutoOpen(true);
            if (item.getAccountTypes() != null) {
                item.getAccountTypes();
                for (final SelectItem type : item.getAccountTypes()) {
                    if (!type.isSelected()) {
                        MenuPopItem status = new MenuPopItem(type.getName());
                        status.getElement().setId(type.getName());
                        status.setSelection(type.isSelected());
                        status.setCommand(() -> {
                            status.closeAll(actions);
                            ArrayList<Integer> id = new ArrayList<>();
                            id.add(item.getObjectId());
                            convertTo(id, type.getId());
                        });
                        convertSubMenu.addItem(status);
                    }
                }
            }
            convertTo.setSubMenu(convertSubMenu);
            actionItemCount++;
            actions.addItem(convertTo);
        }
        if (Utils.hasPermission(CRM_TASKS_ADD, CRM_ADD_NEW_ACTIVITY_EVENT, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
            MenuPopItem addActivity = new MenuPopItem(wfmStrings.add(), "icon-add-green");
            addActivity.getElement().setId("addActivity");
            MenuBar addActivityMenu = new MenuBar(true);
            addActivityMenu.setAutoOpen(true);
            if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES)) {
                final MenuPopItem addOpportunity = new MenuPopItem(wfmStrings.opportunity(), "icon-opportunity-small");
                addOpportunity.getElement().setId("add_Opportunity");
                addOpportunity.setCommand(() -> {
                    addOpportunity.closeAll(actions);
                    new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
                });
                addActivityMenu.addItem(addOpportunity);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                final MenuPopItem logItem = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
                logItem.getElement().setId("logCall");
                logItem.setCommand(() -> {
                    logItem.closeAll(actions);
                    new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
                });
                addActivityMenu.addItem(logItem);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
                final MenuPopItem scheduleItem = new MenuPopItem(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()), "icon-schedile");
                scheduleItem.getElement().setId("scheduleEventMeeting");
                scheduleItem.setCommand(() -> {
                    scheduleItem.closeAll(actions);
                    new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
                });
                addActivityMenu.addItem(scheduleItem);
            }
            if (Utils.hasPermission(ADD_CONTACT_SMS)) {
                final MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
                addSms.ensureDebugId(wfmStrings.sendSms());
                addSms.setCommand(() -> {
                    addSms.closeAll(actions);
                    ContactListItem contactListItem = new ContactListItem();
                    contactListItem.setObjectId(item.getObjectId());
                    contactListItem.setContactName(item.getPrimaryContactName());
                    contactListItem.setPrimaryPhone(item.getPhone());
                    contactListItem.setClientContact(true);
                    new ActivityQuickAddForm(Appointment.SMS, contactListItem, null, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
                });
                addActivityMenu.addItem(addSms);
            }
            addActivity.setSubMenu(addActivityMenu);
            actionItemCount++;
            actions.addItem(addActivity);
        }
        /*Account Send Sales Invoice BEGIN*/
        if (!item.isBlocked() && !item.hasSupplierType() && Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
            MenuPopItem sendSalesInvoice = new MenuPopItem(Property.get(Constants.SALE_INVOICE, crmStrings.sendSalesInvoice(), wfmStrings.salesInvoice()), "icon-send-sales-invoice");
            sendSalesInvoice.getElement().setId("sendSalesInvoice");
            sendSalesInvoice.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/contact/" + item.getObjectId()));
            actionItemCount++;
            actions.addItem(sendSalesInvoice);
        }
        /*Account Send Sales Invoice END*/
        /*Account Send Sales Quote BEGIN*/
        if (!item.isBlocked() && !item.hasSupplierType() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) {
            MenuPopItem sendSalesQuote = new MenuPopItem(Property.get(Constants.SALE_QUOTE, wfmStrings.sendSalesQuote(), wfmStrings.salesQuote()));
            sendSalesQuote.getElement().setId("sendSalesQuote");
            sendSalesQuote.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/contact/" + item.getObjectId()));
            actionItemCount++;
            actions.addItem(sendSalesQuote);
        }
        /*Account Send Sales Quote END*/
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DETECT_DUBLICATES)) {
            MenuPopItem detectDuplicate = new MenuPopItem(wfmStrings.detectDuplicates(), "icon-accounts", () -> detectDuplicates(item));
            detectDuplicate.getElement().setId("detectDuplicates");
            actionItemCount++;
            actions.addItem(detectDuplicate);
        }
        if (((item.hasSupplierType() || item.hasCustomerType()) && Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DELETE))) {
            MenuPopItem blockSupplier = new MenuPopItem(item.isBlocked()
                    ? wfmStrings.unblock()
                    : wfmStrings.block(), "icon-edit-subscriptions", () -> {
                LoadingPanel.loading(true);
                clientService.blockAccount(item.getObjectId(), item.isBlocked(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        onFailure(throwable);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        list.reloadPage();
                    }
                });
            });
            actionItemCount++;
            actions.addItem(blockSupplier);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CHANGE_OWNER)) {
            MenuPopItem changeOwner = new MenuPopItem(crmStrings.changeOwner(), "icon-change-status", () -> crmService.getCrmAccountOwners(item.getObjectId(), new AsyncCallback<ArrayList<SelectItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> accountOwners) {
                    ownerPopup = new OwnerPopup(accountOwners);
                    ownerPopup.getAccountIDs().clear();
                    ownerPopup.addAccountID(item.getObjectId());
                    ownerPopup.setFilterParameter(null);
                    ownerPopup.setListRefresh(() -> list.reloadPage());
                    ownerPopup.open();
                }
            }));
            changeOwner.ensureDebugId("changeOwner");
            actionItemCount++;
            actions.addItem(changeOwner);
        }


        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DELETE)) {
            MenuPopItem removeAccount = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                KpiCheckBox kpiCheckBox = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.removeFromContactsToo(), wfmStrings.contacts()));
                messageBox.add(kpiCheckBox);
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        ArrayList<Integer> objectIDs = new ArrayList<>();
                        objectIDs.add(item.getObjectId());
                        crmService.deleteCrmAccount(objectIDs, kpiCheckBox.getValue(), new AbstractAsyncCallback<ArrayList<Integer>>() {
                            @Override
                            public void failure(Throwable caught) {

                            }

                            @Override
                            public void success(ArrayList<Integer> result) {
                                if (result.size() == 0) {
                                    Info.show("This account has a balance or it is used in at least one transaction.", Info.Type.WARNING);
                                } else {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.accountCrm()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACCOUNT_DELETED, result, CrmAccountListView.this);
                                }
                            }
                        });
                    }
                });
                messageBox.open();
            });
            removeAccount.getElement().setId("delete");
            actionItemCount++;
            actions.addItem(removeAccount);
        }

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(actions);
        return toolItem.getAction();
    }

    protected boolean isShowExport() {
        return Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_EXPORT);
    }

    protected boolean hasPermissionToEdit() {
        return Utils.hasPermission(CRM_ACCOUNTS_EDIT);
    }

    @Override
    protected boolean hasImportButton() {
        return true;
    }

    @Override
    protected String getImporterLink() {
        return "importaccount|add/add/";
    }

    @Override
    protected ImportTypeEnum getImportType() {
        return ImportTypeEnum.CRM_ACCOUNT;
    }

    @Override
    protected String getPDFExporterLink() {
        return "crmAccountListPDFHandler";
    }

    @Override
    protected String getExcelExporterLink() {
        return "downloadCrmAccountExcel";
    }

    @Override
    protected ListingRequestProvider<CrmAccountItem> getRequestProvider() {
        return (fp, contactCallback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setDetectDuplicates(detectDuplicates);
            fp.setPropertyCode(getPropertyCode());
            fp.setObjectIDs(CrmAccountItem.getIDsOnly(lastSelectedItems));
            fp.setCRM(true);
            crmService.getCrmAccounts(fp, new AbstractAsyncCallback<CrmAccountList>() {
                @Override
                public void failure(Throwable throwable) {
                    contactCallback.onFailure(throwable);
                }

                @Override
                public void success(CrmAccountList result) {
                    totalCount = result.getTotal();
                    defaultOne = result.getDefaultOne();
                    contactCallback.onSuccess(result);
                    if (list.getPagingScrollTable().isShowPopups()) {
                        list.setPopupWidgets(getNotesAsWidgets(result));
                    }
                }
            });
        };
    }

    private ArrayList<ArrayList<Widget>> getNotesAsWidgets(ListResult<CrmAccountItem> result) {
        ArrayList<ArrayList<Widget>> widgets = new ArrayList<>();
        if (result != null && result.getList() != null && result.getList().size() > 0) {
            for (CrmAccountItem crmAccount : result.getList()) {
                ArrayList<Widget> crmAccountsWidgets = new ArrayList<>();
                if (crmAccount.getNote() != null && !"".equals(crmAccount.getNote().trim())) {
                    FlexTable panel = new FlexTable();
                    panel.setWidth("100%");
                    panel.setWidget(0, 0, new HTML("<b class=\"customTitle\">" + wfmStrings.lastNote() + "</b>"));
                    panel.setHTML(1, 0, crmAccount.getNote());
                    crmAccountsWidgets.add(panel);
                }
                widgets.add(crmAccountsWidgets);
            }
        }
        return widgets;
    }

    @Override
    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(wfmStrings.accountCrm()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private static void showContactsDeletedMessageBox(List<Integer> result) {
        String message = result != null && result.size() > 0
                ? (crmMessages.successfullyDeletedButSomeNotAccounts(String.valueOf(result.size())))
                : Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.accounts());
        Info.show(message, Info.Type.INFO);
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
                if (ids.isEmpty()) {
                    return;
                }
                LoadingPanel.loading(true);
                crmService.deleteCrmAccount(ids, kpiCheckBox.getValue(), new AbstractAsyncCallback<ArrayList<Integer>>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(ArrayList<Integer> result) {
                        LoadingPanel.loading(false);
                        if (result.isEmpty()) {
                            Info.show("This account has a balance or it is used in at least one transaction.", Info.Type.WARNING);
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACCOUNT_DELETED, result, CrmAccountListView.this);
                            showContactsDeletedMessageBox(result /*items.size()*/);
                            list.reloadPage();
                        }
                    }
                });
            }
        });
        messageBox.open();
    }

    @Override
    protected void addActionsToActionButton() {
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CONVERT)) {
            final MenuBar convertTo = new MenuBar(true);
            if (defaultOne != null && defaultOne.getAccountTypes() != null) {
                for (final SelectItem type : defaultOne.getAccountTypes()) {
                    convertTo.addItem("&nbsp;&nbsp;&nbsp;" + type.getName(), true, () -> {
                        actions.hide();
                        if (selectedItems == null || selectedItems.size() == 0) {
                            list.showSelectOneMessage();
                        } else {
                            ArrayList<Integer> ids = new ArrayList<>();
                            for (CrmAccountItem item : selectedItems) {
                                ids.add(item.getObjectId());
                            }
                            convertTo(ids, type.getId());
                        }
                    });
                }
                actions.addMenuItemWithMenuBar(wfmStrings.convert(), "", true, convertTo);
            }
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_MERGE)) {
            actions.addMenuItem(wfmStrings.merge(), true, () -> {
                if (selectedItems.size() >= 2) {
                    StringBuilder ids = new StringBuilder();
                    String delimitr = "/";
                    int i = 0;
                    for (CrmAccountItem item : selectedItems) {
                        ids.append(delimitr).append(item.getObjectId().toString());
                        if (i++ == 4) {
                            break;
                        }
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.MERGE + "|add/add/" + CrmConstants.CRM_ACCOUNT + ids);
                }
            });
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DETECT_DUBLICATES)) {
            actions.addMenuItem(wfmStrings.detectDuplicates(), true, this::detectDuplicates);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CHANGE_OWNER)) {
            actions.addMenuItem(crmStrings.changeOwner(), true, () -> {
                //Bulk. User selected all items
                ownerPopup = new OwnerPopup();
                ownerPopup.getAccountIDs().clear();
                ownerPopup.setListRefresh(() -> list.reloadPage());
                if (list.hasCheckedAllTableItems()) {
                    ListingFilterParameter filterParameter = list.getFilterParametrs();
                    filterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParameter.getFacetFilter()));
                    filterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParameter.getListPanelTool()));
                    ownerPopup.setFilterParameter(filterParameter);
                    ownerPopup.getAccountIDs().addAll(CrmAccountItem.getIDsOnly(selectedItems));
                } else {
                    ownerPopup.getAccountIDs().addAll(CrmAccountItem.getIDsOnly(selectedItems));
                }
                ownerPopup.open();
            });
        }

    }

    private void detectDuplicates(CrmAccountItem... items) {
        boolean detectDuplicatesByArguments = items != null && items.length > 0;
        if (!detectDuplicatesByArguments && selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneToDetectDuplicates(wfmStrings.accountCrm()), Info.Type.WARNING);
        } else {
            detectDuplicates = true;
            lastSelectedItems.clear();
            list.getFilterParametrs().setSortField(null);
            list.getFilterParametrs().setSortDir(null);
            lastSelectedItems.addAll(detectDuplicatesByArguments ? Arrays.asList(items) : selectedItems);
            list.reloadPage();
        }
    }

    @Override
    protected ActionButton initializeTopMenuNew() {
        if (addPermission && quickAddPermission) {
            return addButton();
        } else if (addPermission) {
            return fullAddButton();
        } else if (quickAddPermission) {
            return quickAddButton();
        }
        return null;
    }

    public ActionButton addButton() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem newAdd = new MenuPopItem(Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.company()), null, () -> SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add"));
        newAdd.getElement().setId("addAccountCrm");
        menu.addItem(newAdd);

        MenuPopItem newAddQuickAdd = new MenuPopItem(wfmStrings.quickAdd(), null, () -> new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM));
        newAdd.getElement().setId("addAccountCrmQuickAdd");
        menu.addItem(newAddQuickAdd);

        newItem.setMenu(menu);

        return newItem;
    }

    public ActionButton fullAddButton() {
        ActionButton fullAddButton = getAddNewButton(ActionButton.Type.TOOLMENU);
        fullAddButton.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add"));
        return fullAddButton;
    }

    public ActionButton quickAddButton() {
        ActionButton quickAddButton = getAddNewButton(ActionButton.Type.TOOLMENU);
        quickAddButton.addClickHandler(clickEvent -> new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM));
        return quickAddButton;
    }

    @Override
    protected VerticalPanel getEmptyDataTable() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(new Label(crmStrings.messCurrentlyAccounts()));
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label(crmStrings.messAddingAccountsClicking()));
        SimpleLink simpleLink = new SimpleLink(" " + wfmStrings.here());
        simpleLink.addClickHandler(clickEvent -> new CrmQuickAdd(RelationItem.TYPE_CRM_ACCOUNT));
        simpleLink.setStyleName("addLinkStyle");
        hp.add(simpleLink);
        vp.add(hp);
        return vp;
    }

    @Override
    protected Command getAddNewAction() {
        if (addPermission && quickAddPermission) {
            return () -> new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM);
        } else if (quickAddPermission) {
            return () -> new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM);
        } else if (addPermission) {
            return () -> SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add");
        }
        return null;
    }

    protected FacetContentConfigure getContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(5, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.CrmAccountFacetFilter.getContentCode()[0], wfmStrings.parentaccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CrmAccountFacetFilter.getContentCode()[1], wfmStrings.accountType(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCrmAccountRepresenter.FIELD_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCrmAccountRepresenter.FIELD_TYPE_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CrmAccountFacetFilter.getContentCode()[2], wfmStrings.leadOwner(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCrmAccountRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CrmAccountFacetFilter.getContentCode()[4], wfmStrings.industry(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CrmAccountFacetFilter.getContentCode()[7], wfmStrings.country(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCrmAccountRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.COUNTRY;
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
        return contentConfigure;
    }

    protected FacetCallbackProvider getFacetFilterCallbackProvider() {
        return (data, callback) -> {
            RbacService.App.get().getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(FacetFilterRpc result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private void convertTo(ArrayList<Integer> ids, Integer typeID) {
        LoadingPanel.loading(true);
        crmService.convertAccounts(ids, typeID, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Boolean result) {
                LoadingPanel.loading(false);
                if (result != null && result) {
                    Info.show(crmStrings.accountsConverted(), Info.Type.INFO);
                    list.reloadPage();
                }
            }
        });
    }


    public CrmAccountItem getCurrentItem() {
        return currentItem;
    }

    @Override
    public String getIconStyle() {
        return "crm crm-account-list";
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
}
