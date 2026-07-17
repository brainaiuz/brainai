package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.ui.ImportVCardFilePopup;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryServiceAsync;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.LookUpCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.crm.client.ui.GoogleContactsQuestionPopup;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:14:38
 */
public class ContactListView extends BaseListView implements Constants, PermissionConstants {

    protected CRMServiceAsync crmService = CRMService.App.get();
    protected KanbanServiceAsync kanbanService = KanbanService.App.get();
    protected ContactServiceAsync contactService = ContactService.App.get();
    private final ContactCategoryServiceAsync contactCategoryService = ContactCategoryService.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected ListingPanel<ContactListItem> list;
    public ContactListItem defaultOne;
    protected int totalCount;
    protected ContextMenu actions;
    protected ContextMenu emptyActions;
    protected HashSet<ContactListItem> selectedItems = new HashSet<>();
    protected HashSet<ContactListItem> lastSelectedItems = new HashSet<>();
    protected CampaignPopup campaignPopup;

    private boolean configureWithGoogle = false;
    private boolean configureWithOffice = false;
    protected boolean hasCheckedAllTableItems = false;
    protected boolean detectDuplicates = false;
    protected boolean isRevertedFirstPage = false;
    protected boolean isClickedDuplicateBtn = false;

    private Integer crmAccountID;
    private String crmAccounName;
    protected Integer campaignID;
    protected String campaignName;
    protected boolean isLead;
    private boolean fromCrmAccountAddEdit;
    HashMap<Integer, SelectItem[]> map = null;
    private Object viewState = null;
    private String selectedCountryName = null;
    private HashMap<String, String[]> countryKey = null;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.CONTACT, null);

    public ContactListView() {
        super(CRM_CONTACT_LIST);
        setDescription(property.getPlural(wfmStrings.contacts()));
        setPlusIcon();
    }

    public ContactListView(Integer campaignID, String campaignName) {
        this();
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        setPlusIcon();
    }

    public ContactListView(String name) {
        super(name);
        setPlusIcon();
    }

    public ContactListView(String name, String description) {
        super(name, description);
        setPlusIcon();
    }

    public ContactListView(Integer crmAccountID) {
        this();
        this.crmAccountID = crmAccountID;
        fromCrmAccountAddEdit = true;
        setPlusIcon();
    }

    private void setPlusIcon() {
        if ((Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS) && Utils.isCRM()) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM,
                    RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName),
                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, crmAccountID, crmAccounName)));
        }
    }

    protected Widget onInitialize() {
        campaignPopup = new CampaignPopup(isLead ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT);
        campaignPopup.setListRefresh(() -> refresh());

        list = new GuideListingPanel(isLead ? ListPanelType.LeadListPanel : ListPanelType.ContactListPanel,
                getColumns(), getRequestProvider(), getPanelDesigner(), SelectionGrid.SelectionPolicy.CHECKBOX);
        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveContactEditCellValue((ContactListItem) rowValue, columnCodeName));
        list.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        list.setOnReset(() -> {
            detectDuplicates = false;
            isRevertedFirstPage = false;
            isClickedDuplicateBtn = false;
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, ContactListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_DELETE, ContactListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ACCOUNT_ADD, ContactListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, ContactListView.this, (sender, args) -> list.reloadPage());
        if (crmAccountID != null) {
            getAccountName();
        }
        add(list);
        return null;
    }

    private void getAccountName() {
        AllInOneService.App.get().getRelationName(crmAccountID, RelationItem.TYPE_CRM_ACCOUNT, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                crmAccounName = result;
            }
        });
    }

    protected void refresh() {
        list.reloadPage();
    }

    protected CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        HashMap<String, CustomColumnDefinitionConfig> editableColumns = new HashMap<>();

        CustomColumnDefinitionConfig column;
        //Action
        column = new ColumnDefinitionConfig<ContactListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ContactListItem rowValue) {
                return getActions(rowValue);
            }
        };
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Contact Name
        column = new ColumnDefinitionConfig<ContactListItem, SimpleLink>(wfmStrings.name(), ContactListItem.CONTACT_NAME, 130) {
            @Override
            public SimpleLink getCellValue(ContactListItem rowValue) {
                if (Utils.hasPermission(CRM_CONTACTS_SUMMARY)) {
                    return getLink(rowValue.getName(), "contact|summary/" + rowValue.getObjectId() + "//" + (rowValue.getCrmAccount() != null ? rowValue.getCrmAccount().getObjectId() : ""), rowValue.getName(), rowValue.getName());
                } else {
                    return getLink(rowValue.getName(), null);
                }
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.CONTACT_NAME));
        column.setMinimumColumnWidth(130);
        columns.add(column);
        //Email
        column = new ColumnDefinitionConfig<ContactListItem, HTML>(wfmStrings.email(), ContactListItem.EMAIL, 120) {
            @Override
            public HTML getCellValue(final ContactListItem rowValue) {
                return getEmailLink(rowValue);
            }

            @Override
            public void setCellValue(ContactListItem rowValue, HTML cellValue) {
                rowValue.setPrimaryEmail(cellValue.getText());
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.EMAIL));
        column.setMinimumColumnWidth(110);
        editableColumns.put(ContactListItem.EMAIL, column);
        columns.add(column);
        //Phone
//        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
        column = new ColumnDefinitionConfig<ContactListItem, Div>(wfmStrings.phone(), ContactListItem.PHONE, 130) {
            @Override
            public Div getCellValue(final ContactListItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPrimaryPhone(), rowValue, false, true);
                return phonePopup.getPhoneWidget();
            }
        };
        /*} else if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ASTERISK)) {
            column = new ColumnDefinitionConfig<ContactListItem, HTML>(crmStrings.phone(), ContactListItem.PHONE, 130) {
                @Override
                public HTML getCellValue(final ContactListItem rowValue) {

                    HTML phoneWidget = new HTML("<a href=\"#\">" + rowValue.getPrimaryPhone() + "</a>");
                    phoneWidget.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            String phone = rowValue.getPrimaryPhone() != null ? rowValue.getPrimaryPhone().trim() : "";
                            phone = phone.replaceAll(" ", "");
                            if (phone != null && phone.length() > 9) {
                                phone = phone.substring(phone.length() - 9);
                            }
                            AsteriskHelper.callAsterisk(phone);
                        }
                    });
                    return phoneWidget;
                }
            };
        } else {
            column = new ColumnDefinitionConfig<ContactListItem, HTML>(crmStrings.phone(), ContactListItem.PHONE, 130) {
                @Override
                public HTML getCellValue(final ContactListItem rowValue) {
                    if (rowValue.getPrimaryPhone() != null && !"N/A".equalsIgnoreCase(rowValue.getPrimaryPhone())) {
                        return new HTML("<a href=\"tel:" + rowValue.getPrimaryPhone() + "\">" + rowValue.getPrimaryPhone() + "</a>");
                    } else {
                        return new HTML(rowValue.getPrimaryPhone());
                    }
                }
            };
        }*/
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.PHONE));
        column.setMinimumColumnWidth(130);
        columns.add(column);
        //Company Name
        column = new ColumnDefinitionConfig<ContactListItem, SimpleLink>(Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.company()), ContactListItem.CRM_ACCOUNT, 130) {
            @Override
            public SimpleLink getCellValue(ContactListItem rowValue) {
                if (rowValue.getCrmAccount() != null && rowValue.getCrmAccount().getName() != null) {
                    if (Utils.isAccounting()) {
                        return getLink(rowValue.getCrmAccount().getName(), "client|summary/" + rowValue.getCrmAccount().getObjectId(), Optional.ofNullable(rowValue.getCrmAccount().getNumber()).orElse(rowValue.getCrmAccount().getName()), rowValue.getCrmAccount().getName());
                    } else if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                        return getLink(rowValue.getCrmAccount().getName(), "account|summary/" + rowValue.getCrmAccount().getObjectId(), Optional.ofNullable(rowValue.getCrmAccount().getNumber()).orElse(rowValue.getCrmAccount().getName()), rowValue.getCrmAccount().getName());
                    } else {
                        return getLink(rowValue.getCrmAccount().getName(), null);
                    }
                } else {
                    return getLink("", "");
                }
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.CRM_ACCOUNT));
        column.setColumnSortable(true);
        column.setMinimumColumnWidth(130);
        columns.add(column);
        //Category
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.category(), ContactListItem.CATEGORIES, 130) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getCategoryNames();
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.CATEGORIES));
        column.setMinimumColumnWidth(70);
        columns.add(column);
        //Country
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.country(), ContactListItem.COUNTRY, 80) {
            @Override
            public SelectItem getCellValue(ContactListItem rowValue) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems.getCountryId() != null ? new SelectItem(addressItems.getCountryId(), addressItems.getCountry()) : null;
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem value) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                if (SelectItem.isDifferent(value, new SelectItem(rowValue.getPrimaryAddress().getCountryId(), rowValue.getPrimaryAddress().getCountry()))) {
                    rowValue.getPrimaryAddress().setCountry(value != null ? value.getName() : null);
                    rowValue.getPrimaryAddress().setCountryId(value != null ? value.getId() : null);
                    rowValue.getPrimaryAddress().setState(null);
                    saveCellValue(rowValue);
                }
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.COUNTRY));
        column.setMinimumColumnWidth(40);
        editableColumns.put(ContactListItem.COUNTRY, column);
        columns.add(column);
        //Owner
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.owner(), ContactListItem.OWNER, 100) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getOwner();
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.OWNER));
        column.setMinimumColumnWidth(100);
        columns.add(column);
        //Mobile
        column = new ColumnDefinitionConfig<ContactListItem, Div>(crmStrings.mobilePhone(), ContactListItem.MOBILE, 130) {
            @Override
            public Div getCellValue(final ContactListItem rowValue) {
                if (rowValue.getMobile() != null && rowValue.getMobile().size() > 0) {
                    PhonePopup phonePopup = new PhonePopup(rowValue.getMobile().get(0), rowValue, true, true);
                    return phonePopup.getPhoneWidget();
                }
                return null;
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.MOBILE));
        column.setMinimumColumnWidth(130);
        columns.add(column);
        //Title
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.jobTitle(), ContactListItem.JOB_TITLE, 80) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getJobTitle();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                rowValue.setJobTitle(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.JOB_TITLE));
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        editableColumns.put(ContactListItem.JOB_TITLE, column);
        columns.add(column);
        //STREET
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.streetAddress1(), ContactListItem.STREET, 100) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems != null && addressItems.getAddress() != null ? addressItems.getAddress() : wfmStrings.notAvailable();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                if (rowValue.getPrimaryAddress() == null) {
                    rowValue.setPrimaryAddress(new Address());
                }
                rowValue.getPrimaryAddress().setAddress(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.STREET));
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        editableColumns.put(ContactListItem.STREET, column);
        columns.add(column);
        //STREET2
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.streetAddress2(), ContactListItem.STREET2, 100) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems != null && addressItems.getAddressb() != null ? addressItems.getAddressb() : wfmStrings.notAvailable();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                if (rowValue.getPrimaryAddress() == null) {
                    rowValue.setPrimaryAddress(new Address());
                }
                rowValue.getPrimaryAddress().setAddressb(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.STREET2));
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(100);
        editableColumns.put(ContactListItem.STREET2, column);
        columns.add(column);
        //CITY
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.city(), ContactListItem.CITY, 100) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems != null && addressItems.getCity() != null ? addressItems.getCity() : wfmStrings.notAvailable();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                if (rowValue.getPrimaryAddress() == null) {
                    rowValue.setPrimaryAddress(new Address());
                }
                rowValue.getPrimaryAddress().setCity(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.CITY));
        column.setMinimumColumnWidth(100);
        editableColumns.put(ContactListItem.CITY, column);
        columns.add(column);
        //State
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.state(), ContactListItem.STATE, 60) {
            @Override
            public SelectItem getCellValue(ContactListItem rowValue) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                setSelectedCountryName(rowValue.getPrimaryAddress().getCountry());
                return new SelectItem(rowValue.getPrimaryAddress().getStateId(), rowValue.getPrimaryAddress().getState());
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem value) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                if (SelectItem.isDifferent(value, new SelectItem(rowValue.getPrimaryAddress().getStateId(), rowValue.getPrimaryAddress().getState()))) {
                    rowValue.getPrimaryAddress().setState(value != null ? value.getName() : null);
                    rowValue.getPrimaryAddress().setStateId(value != null ? value.getId() : null);
                    saveCellValue(rowValue);
                }
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.STATE));
        column.setMinimumColumnWidth(30);
        editableColumns.put(ContactListItem.STATE, column);
        columns.add(column);
        //Post Code
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.postCode(), ContactListItem.POST_CODE, 100) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems != null && addressItems.getZipCode() != null ? addressItems.getZipCode() : wfmStrings.notAvailable();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                if (rowValue.getPrimaryAddress() == null) {
                    rowValue.setPrimaryAddress(new Address());
                }
                rowValue.getPrimaryAddress().setZipCode(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.POST_CODE));
        column.setMinimumColumnWidth(80);
        editableColumns.put(ContactListItem.POST_CODE, column);
        columns.add(column);
        //Contact Department
        column = new ColumnDefinitionConfig<ContactListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.branch()), ContactListItem.DEPARTMENT, 70) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getDepartment();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                rowValue.setDepartment(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setMinimumColumnWidth(70);
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.DEPARTMENT));
        column.setColumnSortable(true);
        editableColumns.put(ContactListItem.DEPARTMENT, column);
        columns.add(column);
        //Contact Date of Birth Day
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.dateOfBirth(), ContactListItem.DATE_OF_BIRTH, 70) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getBirthDate() != null ? DateUtils.format(rowValue.getBirthDate().getNonConvertedDate()) : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.DATE_OF_BIRTH));
        column.setColumnSortable(false);
        columns.add(column);

        //Contact Industry
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.industry(), CrmAccountItem.INDUSTRY, 70) {
            @Override
            public String getCellValue(ContactListItem rowValue) {
                return rowValue.getCrmAccount() != null && rowValue.getCrmAccount().getIndustry() != null ? rowValue.getCrmAccount().getIndustry() : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setShow(ContactListItem.defaultContactColumnNames.contains(CrmAccountItem.INDUSTRY));
        column.setColumnSortable(false);
        columns.add(column);
        //Campaign
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.campaign(), ContactListItem.CAMPAIGN, 70) {
            @Override
            public SelectItem getCellValue(ContactListItem rowValue) {
                return new SelectItem(rowValue.getCampaignId(), rowValue.getCampaignId() != null ? rowValue.getCampaign() : "");
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                rowValue.setCampaignSI(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultContactColumnNames.contains(ContactListItem.CAMPAIGN) && campaignID == null);
        column.setMinimumColumnWidth(70);
        editableColumns.put(ContactListItem.CAMPAIGN, column);
        columns.add(column);

        if (Utils.hasPermission(CRM_EDIT_CONTACT)) {
            initCellEdit(editableColumns);
        }
        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }


    public HTML getEmailLink(final ContactListItem rowValue) {
        SimpleLink sendEmailLink = new SimpleLink("");
        if (!Utils.isNullOrEmpty(rowValue.getPrimaryEmail())) {
            sendEmailLink = new SimpleLink(rowValue.getPrimaryEmail());
            sendEmailLink.addClickHandler(clickEvent -> {
                if (!rowValue.isEmailOptOut()) {
                    String relationType = isLead ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT;
                    //new ComposeView(rowValue.getPrimaryEmail(), RelationItem.newEventRelation(isLead ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, rowValue.getObjectId(), rowValue.getName()));
                    if (rowValue.getCrmAccount() != null && rowValue.getCrmAccount().getObjectId() != null && rowValue.getCrmAccount().getName() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + rowValue.getPrimaryEmail() + "/" + relationType + "/" + rowValue.getObjectId() + "/" + rowValue.getName() + "/" + RelationItem.TYPE_CRM_ACCOUNT + "/" + rowValue.getCrmAccount().getObjectId() + "/" + rowValue.getCrmAccount().getName());
                    } else {
                        goTo("emailcompose|add/add/" + rowValue.getPrimaryEmail() + "/" + relationType + "/" + rowValue.getObjectId() + "/" + rowValue.getName());
                    }
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.open();
                }
            });
        }
        viewState = sendEmailLink;
        return sendEmailLink;
    }

    protected String getRelationType() {
        return RelationItem.TYPE_CONTACT;
    }

    protected void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (ContactListItem.EMAIL.equals(entry.getKey())) {
                widget = getEmailCell();
            } else if (ContactListItem.COUNTRY.equals(entry.getKey())) {
                widget = getCountryCell();
                getQuickSaveData((DropDownCellEditor) widget);
            } else if (ContactListItem.STATE.equals(entry.getKey())) {
                widget = getStateCell();
            } else if (ContactListItem.REPORTS_TO.equals(entry.getKey()) || ContactListItem.CITY.equals(entry.getKey()) || ContactListItem.STREET.equals(entry.getKey()) || ContactListItem.STREET2.equals(entry.getKey()) || ContactListItem.DEPARTMENT.equals(entry.getKey()) || ContactListItem.POST_CODE.equals(entry.getKey()) || ContactListItem.JOB_TITLE.equals(entry.getKey())) {
                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
            } else if (ContactListItem.CRM_ACCOUNT.equals(entry.getKey())) {
                widget = getLookUpWidget(CRMLookUp.CRM_ACCOUNT);
            } else if (ContactListItem.CAMPAIGN.equals(entry.getKey())) {
                widget = getLookUpWidget(CRMLookUp.CAMPAIGN);
            } else if (ContactListItem.CAMPAIGN.equals(entry.getKey())) {
                widget = getLookUpWidget(CRMLookUp.CAMPAIGN);
            } else if (ContactListItem.LEAD_ASSIGNEE.equals(entry.getKey()) || ContactListItem.LEAD_BACKUP_ASSIGNEE.equals(entry.getKey()) || ContactListItem.OWNER.equals(entry.getKey())) {
                EmployeeLookUpWithCode assignee = new EmployeeLookUpWithCode(true, true, false);
                assignee.setPermissionCode(CRM_LEAD_CONTACT_ASSIGNEE);
                assignee.addStyleName(DEFAULT_WIDTH);
                widget = new LookUpCellEditor<SelectItem>(assignee) {
                    @Override
                    protected SelectItem getValue() {
                        return getSelectedItem();
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        assignee.clear();
                        setSelectItem(cellValue);
                    }
                };
            } else if (ContactListItem.LEAD_STATUS.equals(entry.getKey()) || ContactListItem.LEAD_SOURCE.equals(entry.getKey()) || ContactListItem.LEAD_RATING.equals(entry.getKey())) {
                widget = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem();
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setAllowFirstItem(true);
                        if (getListBox().getItems() == null || getListBox().getItems().length < 1 && defaultOne != null) {
                            if (ContactListItem.LEAD_STATUS.equals(entry.getKey())) {
                                getListBox().setItems(defaultOne.getLeadStatuses());
                            } else if (ContactListItem.LEAD_SOURCE.equals(entry.getKey())) {
                                getListBox().setItems(defaultOne.getLeadSources());
                            } else if (ContactListItem.LEAD_RATING.equals(entry.getKey())) {
                                getListBox().setItems(defaultOne.getLeadRatings());
                            } /*else if (ContactListItem.LEAD_ASSIGNEE.equals(entry.getKey()) || ContactListItem.LEAD_BACKUP_ASSIGNEE.equals(entry.getKey()) || ContactListItem.OWNER.equals(entry.getKey())) {
                                getListBox().setItems(defaultOne.getLeadAssignees());
                            }*/
                        }
                        getListBox().setSelectedIndex(0);
                        if (cellValue == null || cellValue.getId() == null) {
                            getListBox().setSelectedNullLabel();
                        } else {
                            getListBox().setSelected(cellValue.getId());
                        }
                    }
                };
            } else if (ContactListItem.EMAIL_ALLOWED.equals(entry.getKey())) {
                widget = new DropDownCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return getListBox().getSelectedItem(true) != null && getListBox().getSelectedItem(true).getId().equals(1) ? wfmStrings.yes() : wfmStrings.no();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        getListBox().setAllowFirstItem(false);
                        getListBox().setWithoutNullLabel(true);
                        if (getListBox().getItems() == null || getListBox().getItems().length < 1) {
                            getListBox().setItems(new SelectItem[]{new SelectItem(0, wfmStrings.no()), new SelectItem(1, wfmStrings.yes())});
                        }
                        getListBox().setSelectedByValue(cellValue);
                    }
                };
            }
            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave((rowValue, columnCodeName) -> saveContactEditCellValue((ContactListItem) rowValue, columnCodeName));
            }
        }
    }

    public LookUpCellEditor<SelectItem> getLookUpWidget(String type) {
        final CRMLookUp lookUp = new CRMLookUp(type);
        return new LookUpCellEditor<SelectItem>(lookUp) {
            @Override
            protected SelectItem getValue() {
                if (type.equals(CRMLookUp.CRM_ACCOUNT)) {
                    if (getSelectedItem() == null) {
                        return getText() != null && !"".equalsIgnoreCase(getText().trim()) ? new SelectItem(null, getText()) : null;
                    }
                }
                return getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                lookUp.clear();
                setSelectItem(cellValue);
            }
        };
    }

    public void getQuickSaveData(final DropDownCellEditor<String> country) {
        crmService.getCountriesKey(new AsyncCallback<HashMap<String, String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<String, String[]> result) {
                countryKey = result;
            }
        });
        CommonService.App.get().getCountries(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                country.setItems(result);
            }
        });
        crmService.getStatesByCountryName(new AsyncCallback<HashMap<Integer, SelectItem[]>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<Integer, SelectItem[]> result) {
                map = result;
            }
        });
    }

    public DropDownCellEditor<SelectItem> getStateCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                String cntry = (selectedCountryName + "(").split("\\(")[0].trim();
                String[] temp = countryKey.get(cntry);
                if (temp == null || temp.length < 2) {
                    getListBox().setItems(new SelectItem[0]);
                    getListBox().setEnabled(false);
                    return;
                }
                String selectedCountryID = countryKey.get(cntry)[0];
                if (selectedCountryID == null) {
                    getListBox().setItems(new SelectItem[0]);
                    getListBox().setEnabled(false);
                    return;
                }
                SelectItem[] items = map.get(Integer.parseInt(selectedCountryID));
                getListBox().setItems(items == null ? new SelectItem[0] : items);
                getListBox().setEnabled(items != null && items.length >= 1);
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue);
                }
            }
        };
    }

    public DropDownCellEditor<SelectItem> getCountryCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    public TextBoxCellEditor<Object> getEmailCell() {
        return new TextBoxCellEditor<Object>(200) {
            @Override
            protected Object getValue() {
                if (getTextBox().getText() != null && !"".equals(getTextBox().getText().trim()) && !Utils.validateEmail(getTextBox().getText(), false)) {
                    getTextBox().setStyleName("x-form-invalid");
                    return ((SimpleLink) viewState).getText();
                }
                ((SimpleLink) viewState).setText(getTextBox().getText());
                return viewState;
            }

            @Override
            protected void setValue(Object cellValue) {
                getTextBox().setText(((HTML) cellValue).getText());
            }
        };
    }

    public void saveContactEditCellValue(ContactListItem rowValue, String columnCodeName) {
        ContactService.App.get().saveContactEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback() {
        });
    }

    protected Anchor getActions(final ContactListItem item) {
        int actionItemCount = 0;
        final MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);
        //Summary
        if (Utils.hasPermission(CRM_CONTACTS_SUMMARY)) {
            MenuPopItem leadSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-contact-small", () -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getObjectId() + "//" + (item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : ""), item.getName(), item.getName());
            });
            leadSummary.ensureDebugId("contact_View");
            actionItemCount++;
            menuBar.addItem(leadSummary);
        }
        //Edit
        if (Utils.hasPermission(CRM_EDIT_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_EDIT))) {
            MenuPopItem contactEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> checkForPermission(item, EDIT));
            contactEdit.ensureDebugId("contact_Edit");
            contactEdit.setCommand(() -> checkForPermission(item, EDIT));
            actionItemCount++;
            menuBar.addItem(contactEdit);
        }
        //Copy
        if (Utils.hasPermission(CRM_COPY_CONTACT)) {
            MenuPopItem copyContact = new MenuPopItem(wfmStrings.copy(), "icon-copy", () -> checkForPermission(item, COPY.hashCode()));
            copyContact.ensureDebugId("contact_copy");
            actionItemCount++;
            menuBar.addItem(copyContact);
        }

        //Note
        if (Utils.hasPermission(CRM_CONTACT_WRITE_NOTE)) {
            MenuPopItem noteItem = new MenuPopItem(wfmStrings.addNote(), "icon-add-node");
            noteItem.ensureDebugId("add_note");
            noteItem.setCommand(() -> isWritePermission(item, wfmStrings.addNote()));
            actionItemCount++;
            menuBar.addItem(noteItem);
        }

        if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES, CRM_QUICK_ADD_NEW_OPPORTUNITIES, CRM_ADD_NEW_ACTIVITY_EVENT, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, CRM_TASKS_ADD, CRM_MESSAGE_CENTER, ADD_CONTACT_SMS)) {
            final MenuBar bar = getActivityMenu(item, menuBar);
            final MenuPopItem addActiv = new MenuPopItem(wfmStrings.add(), "icon-add-green", bar);
            addActiv.ensureDebugId("add_activity");
            actionItemCount++;
            menuBar.addItem(addActiv);
        }

        final MenuBar bar = new MenuBar(true);
        bar.setAutoOpen(true);

        if (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
            MenuPopItem sendSalesInv = new MenuPopItem(wfmStrings.salesInvoice(), "icon-send-sales-invoice", () -> isWritePermission(item, Property.get(Constants.SALE_INVOICE, crmStrings.sendSalesInvoice(), wfmStrings.salesInvoice())));
            sendSalesInv.ensureDebugId("sendSales_Invoice");
            bar.addItem(sendSalesInv);
        }
        if (Utils.hasPermission(CRM_SALES_QUOTE_ADD)) {
            MenuPopItem sendSalesQuote = new MenuPopItem(wfmStrings.salesQuote(), "icon-send-sales-quote", () -> isWritePermission(item, Property.get(Constants.SALE_QUOTE, wfmStrings.sendSalesQuote(), wfmStrings.salesQuote())));
            sendSalesQuote.ensureDebugId("sendSales_Quote");
            bar.addItem(sendSalesQuote);
        }

        if (Utils.hasPermission(CRM_MESSAGE_CENTER)) {
            final MenuPopItem sendEmail = new MenuPopItem(wfmStrings.email(), "icon-create-message ");
            sendEmail.ensureDebugId("send_Email_Supported_Only");
            sendEmail.addStyleName("e-disable");
            if (!Utils.isNullOrEmpty(item.getPrimaryEmail())) {
                sendEmail.addStyleName("e-no-disable");
                sendEmail.setCommand(() -> {
                    sendEmail.closeAll(menuBar);
                    if (!item.isEmailOptOut()) {
                        //new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getName()));
                        SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getName());
                    } else {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.open();
                    }
                });
            }
            bar.addItem(sendEmail);
        }

        if (Utils.hasPermission(ADD_CONTACT_SMS)) {
            final MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
            addSms.ensureDebugId("send_Sms");
            addSms.setCommand(() -> {
                addSms.closeAll(menuBar);
                new ActivityQuickAddForm(Appointment.SMS, item.getPrimaryPhone(), item, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()));
            });
            bar.addItem(addSms);
        }

        if (Utils.hasPermission(CRM_SALES_INVOICE_ADD) || Utils.hasPermission(CRM_SALES_QUOTE_ADD) || Utils.hasPermission(CRM_MESSAGE_CENTER) || Utils.hasPermission(ADD_CONTACT_SMS)) {
            actionItemCount++;
            menuBar.addItem(new MenuPopItem(wfmStrings.send(), "icon-send", bar));
        }

//        if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
//            MenuPopItem editSubscriptions = new MenuPopItem(crmStrings.editSubscriptions(), "icon-edit-subscriptions", () -> showEditSubscriptionsShell(item.getObjectId()));
//            editSubscriptions.ensureDebugId("edit_Subscriptions");
//            actionItemCount++;
//            menuBar.addItem(editSubscriptions);
//        }

        if ((Utils.hasPermission(CRM_EDIT_CONTACT) || Utils.hasPermission(ACCOUNTING_CONTACT_EDIT)) && fromCrmAccountAddEdit) {
            if (item.getCrmAccount().getObjectId() != null && !item.isPrimaryContact()) {
                MenuPopItem makePrimary = new MenuPopItem(wfmStrings.makePrimary(), "icon-edit", () -> checkForPermission(item, MAKE_PRIMARY));
                makePrimary.ensureDebugId("make_Primary");
                actionItemCount++;
                menuBar.addItem(makePrimary);
            }
        }

        if (Utils.hasPermission(CRM_REMOVE_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_DELETE))) {
            MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile", () -> {
                if (item.isEmployeeContact()) {
                    Info.show(crmStrings.inOrderToDeleteTheContactFirstDeleteTheEmployeeCorrespondingToTheContactInTheSystem(), Info.Type.WARNING);
                } else {
                    checkForPermission(item, DELETE);
                }
            });
            removeItem.ensureDebugId("delete");
            actionItemCount++;
            menuBar.addItem(removeItem);
        }

        if (!Utils.getPathName().contains("Crm.html") && Utils.hasPermission(CRM_ADD_NEW_CONTACT) && item.isClientContact()) {//only client contacts supported
            if (!item.isActive()) {
                MenuPopItem activateContactItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile", () -> crmService.activateOrDeActivateClientContact(item.getObjectId(), true, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.show(wfmStrings.error(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(property.getSingular(crmStrings.contactHasBeenActivatedSuccessfully(), wfmStrings.contact()), Info.Type.INFO);
                        list.reloadPage();
                    }
                }));
                activateContactItem.ensureDebugId("activate");
                actionItemCount++;
                menuBar.addItem(activateContactItem);

                MenuPopItem resendActivationLink = new MenuPopItem(wfmStrings.resendActivationLink(), "icon-employee-resend-profile", () -> crmService.resendClientActivationLink(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Boolean aBoolean) {
                        Info.show(wfmStrings.activationLinkHasBeenSent(), Info.Type.INFO);
                    }
                }));
                resendActivationLink.ensureDebugId("resend_Activation_Link");
                actionItemCount++;
                menuBar.addItem(resendActivationLink);
            } else {
                MenuPopItem deActivateContactItem = new MenuPopItem(wfmStrings.deactivate(), "icon-employee-disactivate-profile", () -> crmService.activateOrDeActivateClientContact(item.getObjectId(), false, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.show(wfmStrings.error(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(property.getSingular(wfmStrings.contactHasBeenDeactivated(), wfmStrings.contact()), Info.Type.INFO);
                        list.reloadPage();
                    }
                }));
                deActivateContactItem.ensureDebugId("deactivate");
                actionItemCount++;
                menuBar.addItem(deActivateContactItem);
            }
        }

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private MenuBar getActivityMenu(final ContactListItem item, final MenuBar parentBar) {
        final MenuBar menuBarTwo = new MenuBar(true);
        menuBarTwo.setAutoOpen(true);
        /**/
        if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
            final MenuPopItem logItem = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
            logItem.ensureDebugId("log_call");
            logItem.setCommand(() -> {
                logItem.closeAll(parentBar);
                isWritePermission(item, Property.get(Constants.LOGACALL, wfmStrings.logCall()));
            });
            menuBarTwo.addItem(logItem);
        }

        if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
            final MenuPopItem scheduleItem = new MenuPopItem(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()), "icon-schedile");
            scheduleItem.ensureDebugId("schedule_Event_Meeting");
            scheduleItem.setCommand(() -> {
                scheduleItem.closeAll(parentBar);
                isWritePermission(item, Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()));
            });
            menuBarTwo.addItem(scheduleItem);
        }

        if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES) || Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES)) {
            final MenuPopItem addOpportunity = new MenuPopItem(wfmStrings.opportunity(), "icon-opportunity-small");
            addOpportunity.ensureDebugId("add_Opportunity");
            addOpportunity.setCommand(() -> {
                addOpportunity.closeAll(parentBar);
                if (Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES)) {
                    new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()),
                            RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null, item.getCrmAccount() != null ? item.getCrmAccount().getName() : null));

                } else if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES)) {
                    Integer crmAccountId = null;
                    String crmAccountName = null;
                    if (item.getCrmAccount() != null) {
                        crmAccountId = item.getCrmAccount().getObjectId();
                        crmAccountName = item.getCrmAccount().getName();
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + crmAccountId + "/" + crmAccountName + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getName(), item.getName(), item.getName());
                }
            });
            menuBarTwo.addItem(addOpportunity);
        }

        if (Utils.hasPermission(CRM_TASKS_ADD)) {
            final MenuPopItem taskItem = new MenuPopItem(wfmStrings.task(), "icon-add-task");
            taskItem.ensureDebugId("add_Task");
            taskItem.setCommand(() -> {
                taskItem.closeAll(parentBar);
                isWritePermission(item, Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()));
            });
            menuBarTwo.addItem(taskItem);
        }


        if (Utils.hasPermission(ADD_NEW_CASE)) {
            final MenuPopItem caseItem = new MenuPopItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), "icon-case-view-small");
            caseItem.ensureDebugId("add_case");
            caseItem.setCommand(() -> {
                caseItem.closeAll(parentBar);
                goTo("case|add/add//" + item.getObjectId() + "/" + RelationItem.TYPE_CONTACT + "/" + item.getContactName() + "/" + null + "/" + null + "/" + item.getCrmAccount().getObjectId() + "/" + item.getCrmAccount().getName());
            });
            menuBarTwo.addItem(caseItem);
        }
        if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            final MenuPopItem editSubscriptions = new MenuPopItem(wfmStrings.editSubscription(), "icon-edit-subscriptions", () -> showEditSubscriptionsShell(item.getObjectId()));
            editSubscriptions.ensureDebugId("edit_Subscriptions");
            menuBarTwo.addItem(editSubscriptions);
        }

        return menuBarTwo;
    }

    private void isWritePermission(final ContactListItem contact, final String showMessage) {
        contactService.getContactPermission(contact.getObjectId(), new AbstractAsyncCallback<PermissionHolder>() {
            @Override
            public void failure(Throwable throwable) {
                showPermissionMessage(showMessage);
            }

            @Override
            public void success(PermissionHolder permissionHolder) {
                if (permissionHolder != null && permissionHolder.isWrite()) {
                    afterPermissionGranted(contact, showMessage);
                } else {
                    showPermissionMessage(showMessage);
                }
            }
        });
    }

    private void afterPermissionGranted(final ContactListItem contact, final String showMessage) {
        if (wfmStrings.addNote().equals(showMessage)) {
            new NotePopup(contact.getObjectId(), RelationItem.TYPE_CONTACT);
        } else if (Property.get(Constants.LOGACALL, wfmStrings.logCall()).equals(showMessage)) {
            new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(contact.isLeadContact() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, contact.getObjectId(), contact.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, contact.getCrmAccount().getObjectId(), contact.getCrmAccount().getName()));
        } else if (Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()).equals(showMessage)) {
            new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(contact.isLeadContact() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, contact.getObjectId(), contact.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, contact.getCrmAccount().getObjectId(), contact.getCrmAccount().getName()));
        } else if (Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()).equals(showMessage)) {
            new TaskQuickAddView(RelationItem.newEventRelation(contact.isLeadContact() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, contact.getObjectId(), contact.getName()),
                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectId() : null,
                            contact.getCrmAccount() != null ? contact.getCrmAccount().getName() : null));
        } else if (Property.get(Constants.SALE_INVOICE, crmStrings.sendSalesInvoice(), wfmStrings.salesInvoice()).equals(showMessage) || Property.get(Constants.SALE_QUOTE, wfmStrings.sendSalesQuote(), wfmStrings.salesQuote()).equals(showMessage)) {
            final boolean isInvoice = Property.get(Constants.SALE_INVOICE, crmStrings.sendSalesInvoice(), wfmStrings.salesInvoice()).equals(showMessage);
            if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {
                if (isInvoice ? (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) : (Utils.hasPermission(CRM_SALES_QUOTE_ADD))) {
                    if (!contact.getCrmAccount().isNew()) {
                        String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + (isInvoice ? SALE_INVOICE : SALE_QUOTE) + "|add/add/account/" + contact.getCrmAccount().getObjectId() + "/" + contact.getObjectId();
                        Window.open(addSalesInvoice, "_blank", "");
                    } else {
                        crmService.addAccountToContact(contact, true, new AbstractAsyncCallback<ContactListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(ContactListItem result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, result.getCrmAccount().getObjectId(), ContactListView.this);
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + (isInvoice ? SALE_INVOICE : SALE_QUOTE) + "|add/add/account/" + result.getCrmAccount().getObjectId() + "/" + result.getObjectId();
                                Window.open(addSalesInvoice, "_blank", "");
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            } else {
                showIsAccountingSetUpMessage();
            }
        } else {
            showPermissionMessage(showMessage);
        }
    }

    protected void showIsAccountingSetUpMessage() {
        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.INFO);
    }

    protected void showEditSubscriptionsShell(Integer contactId) {
        KpiModal editSubscriptionsShell = new KpiModal();
        editSubscriptionsShell.setTitle(wfmStrings.mailingLists());
        editSubscriptionsShell.setWidth(400);

        CheckboxMailingListDataGrid mailListTable = new CheckboxMailingListDataGrid(contactId, false, null);
        editSubscriptionsShell.add(mailListTable);
        editSubscriptionsShell.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> editSubscriptionsShell.close()));
        editSubscriptionsShell.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (contactId == null) {
                LoadingPanel.loading(true);
                MassMailService.App.get().createBatchSelectMailingList(mailListTable.getSelectedIdsList(), getIDs(selectedItems), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.subscriptions()), Info.Type.INFO);
                        editSubscriptionsShell.close();
                    }
                });
            } else {//Update one contact/lead mailing list from action
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setContactID(contactId);
                LoadingPanel.loading(true);
                MassMailService.App.get().updateCrmEntityMailLists(fp, mailListTable.getSelectedIdsList(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.subscriptions()), Info.Type.INFO);
                        editSubscriptionsShell.close();
                    }
                });
            }

        }));
        editSubscriptionsShell.open();
    }

    private void showPermissionMessage(String action) {
        final WfmMessageBox dialogBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmMessages.youDoNotHaveEnoughPermission(action, property.getSingular(wfmStrings.contact())));
        dialogBox.open();
    }

    private void checkForPermission(final ContactListItem contact, final int action) {
        contactService.getContactPermission(contact.getObjectId(), new AbstractAsyncCallback<PermissionHolder>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(PermissionHolder permissionHolder) {
                checkForPermission(permissionHolder, contact, action);
            }
        });
    }

    protected ListingRequestProvider getRequestProvider() {
        return new ListingRequestProvider<ContactListItem>() {
            @Override
            public void getRequest(ListingFilterParameter fp, final ListingCallback<ContactListItem> contactCallback) {
                crmService.editLead(null, null, new AbstractAsyncCallback<ContactListItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);    //To change body of overridden methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(ContactListItem result) {
                        defaultOne = result;
                    }
                });
                initContactList(fp, contactCallback, null);
            }
        };
    }

    protected void initContactList(ListingFilterParameter fp, ListingCallback<ContactListItem> contactCallback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fromCrmAccountAddEdit) {
            if (fp.getFacetFilter() != null && fp.getFacetFilter().getFacetContentMap() != null) {
                fp.getFacetFilter().getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[1]).setFacetItems(new SelectItem[]{new SelectItem(crmAccountID)});
            }
            fp.setAccountID(crmAccountID);
        }
        if (campaignID != null) {
            fp.setCampaignID(campaignID);
        }
        if (detectDuplicates && !isRevertedFirstPage && isClickedDuplicateBtn && (list.getPagingScrollTable() != null && list.getPagingScrollTable().getCurrentPage() != 0)) {
            list.gotoPageing(false);
            isRevertedFirstPage = true;
        }
        fp.setDetectDuplicates(detectDuplicates);
        fp.setObjectIDs(ContactListItem.getIDsOnly(lastSelectedItems));
        contactService.getNewContactList(fp, new AbstractAsyncCallback<ListResult<ContactListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                if (contactCallback != null) {
                    contactCallback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<ContactListItem> contactList) {
                totalCount = contactList.getTotal();
                if (contactCallback != null) {
                    isClickedDuplicateBtn = false;
                    contactCallback.onSuccess(contactList);
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

    protected GuideListingPanelDesign getPanelDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {

                if ((Utils.hasPermission(CRM_ADD_NEW_CONTACT) && Utils.isCRM()) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add" + (fromCrmAccountAddEdit && crmAccountID != null ? "/" + crmAccountID : (campaignID != null && campaignName != null ? ("//" + campaignID + "/" + campaignName) : "")));
                } else if (Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS)) {
                    return () ->
                            new CrmQuickAdd(LayoutRPC.CONTACT_FORM,
                                    RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName),
                                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, crmAccountID, crmAccounName));
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return Utils.hasPermission(CRM_CONTACTS_IMPORT) ? () -> imp.open() : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (fromCrmAccountAddEdit) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(crmAccountID)};
                                if (data != null) {
                                    data.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[1]).setFacetItems(item);
                                }
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, RelationItem.TYPE_CRM_ACCOUNT);
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, crmAccountID.toString());
                            }
                            if (campaignID != null) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(campaignID)};
                                if (data != null) {
                                    data.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[6]).setFacetItems(item);
                                }
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, RelationItem.TYPE_CAMPAIGN);
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, campaignID.toString());
                            }
                            RbacService.App.get().getCRMFacetFilterData(CrmConstants.CRM_CONTACT, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getContactContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(CRM_ADD_NEW_CONTACT) || Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS) || Utils.hasPermission(CRM_MULTI_ADD_NEW_CONTACTS)
                        || (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
                    if (Utils.isCRM()) {
                        ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                        addNew.ensureDebugId("crmAddNewContact");
                        MenuBar menuBar = new MenuBar(true);
                        menuBar.setAutoOpen(true);
                        addNew.setMenu(menuBar);

                        if (Utils.hasPermission(CRM_ADD_NEW_CONTACT)) {
                            MenuPopItem addContact = new MenuPopItem(property.getSingular(wfmStrings.contact()));
                            addContact.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add" + (fromCrmAccountAddEdit && crmAccountID != null ? "/" + crmAccountID : (campaignID != null && campaignName != null ? ("//" + campaignID + "/" + campaignName) : ""))));
                            menuBar.addItem(addContact);
                        }
                        if (Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS)) {
                            MenuPopItem quickAdd = new MenuPopItem(wfmStrings.quickAdd());
                            quickAdd.ensureDebugId("quickAdd");
                            quickAdd.setCommand(() -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM,
                                    RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName),
                                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, crmAccountID, crmAccounName)));
                            menuBar.addItem(quickAdd);
                        }
                        if (Utils.hasPermission(CRM_MULTI_ADD_NEW_CONTACTS)) {
                            MenuPopItem addMultipleContact = new MenuPopItem(property.getPlural(crmStrings.addNewMulti(), wfmStrings.contacts()));
                            addMultipleContact.ensureDebugId("Contact_List_AddMultiple_Contact");
                            if (crmAccountID != null) {
                                addMultipleContact.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multicontact|add/add" + "/" + crmAccountID + "/" + RelationItem.TYPE_CRM_ACCOUNT));
                            } else if (campaignID != null) {
                                addMultipleContact.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multicontact|add/add" + "/" + campaignID + "/" + RelationItem.TYPE_CAMPAIGN));
                            } else {
                                addMultipleContact.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multicontact|add/add"));
                            }
                            menuBar.addItem(addMultipleContact);
                        }

                        if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                            MenuPopItem createMailingList = new MenuPopItem(wfmStrings.createMailingList());
                            createMailingList.ensureDebugId("Contact_List_Create_Mailing_List");
                            createMailingList.setCommand(() -> {
                                ListingFilterParameter filterParam = list.getFilterParametrs() == null ? new ListingFilterParameter() : list.getFilterParametrs();
                                filterParam.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParam.getFacetFilter()));
                                filterParam.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParam.getListPanelTool()));
                                new MailListPopup(null, null, false, filterParam);
                            });
                            menuBar.addItem(createMailingList);
                        }

                        return addNew;
                    } else if (Utils.hasPermission(CRM_ADD_NEW_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
                        ActionButton quickAdd = getAddNewButton();
                        quickAdd.addClickHandler(event -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM,
                                RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName),
                                RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, crmAccountID, crmAccounName)));
                        return quickAdd;
                    } else {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("crmContactMore");
                more.addClickHandler(event -> {
                    MenuBar menuBar = getActionsForSelections();
                    menuBar.setAutoOpen(true);
                    more.setMenu(menuBar);
                });
                return more;
            }

            @Override
            public void initImportExportToolBarWidgets(final ExportImportOption exportOption, MaterialDropDown menuContainer) {
//                CommonService.App.get().showContactSynchronize(new AbstractAsyncCallback<Boolean>() {
//                    @Override
//                    public void failure(Throwable throwable) {
//                        setExportOptions(true, menuContainer, exportOption);
//                    }
//
//                    @Override
//                    public void success(Boolean aBoolean) {
//                        setExportOptions(aBoolean, menuContainer, exportOption);
//                    }
//                });
                setExportOptions(false, menuContainer, exportOption);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(crmStrings.messCurrentlyContacts(), wfmStrings.contacts()));
                if (Utils.hasPermission(CRM_ADD_NEW_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_ADD))) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM,
                            RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName),
                            RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, crmAccountID, crmAccounName)));
                    message.setTextBeforeLink(property.getPlural(crmStrings.messYouHere(), wfmStrings.contacts()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(CRM_EDIT_CONTACT);
            }

            @Override
            public Integer getTypeParentId() {
                return fromCrmAccountAddEdit ? crmAccountID : null;
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST_CUSTOMIZE_BUTTON);
            }
        };
    }

    private FacetContentConfigure getContactContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[0], wfmStrings.country(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_COUNTRY_ID_CODE_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.COUNTRY;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[4], wfmStrings.owner(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_OWNER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[1], wfmStrings.company(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[2], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_DEPARTMENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_DEPARTMENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[6], wfmStrings.campaign(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CAMPAIGN_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[3], wfmStrings.jobTitle(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[5], wfmStrings.category(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CATEGORY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CATEGORY_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[7], wfmStrings.state(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_STATE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_STATE_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ContactFacetFilter.getContentCode()[8], wfmStrings.mailList(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_MAIL_LIST_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_MAIL_LIST_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        return contentConfigure;
    }

    private void setExportOptions(Boolean aBoolean, MaterialDropDown menuContainer, ExportImportOption exportOption) {
        FlowPanel toolPanel = new FlowPanel();
        if (aBoolean != null && aBoolean && Utils.hasPermission(CRM_GOOGLE_CONTACTS)) {
            ListItem googleContacts = setSynchronizeGoogleContact();
            menuContainer.add(googleContacts);
            ListItem office365Contacts = setSynchronizeOffice365Contact();
            menuContainer.add(office365Contacts);
        }
        if (Utils.hasPermission(CRM_CONTACTS_IMPORT)) {
            imp.ensureDebugId("imp");
            imp.setSubmitCompleted(() -> {
                if (imp.getObjectId() != null) {
                    goTo("import|add/add/" + imp.getObjectId());
                }
            });

            ListItem li = new ListItem();

            MaterialLink importItem = new MaterialLink(wfmStrings.importString());
            importItem.ensureDebugId("import_button");

            MaterialDropDown items = new MaterialDropDown(importItem);
            items.setHover(true);
            items.setBelowOrigin(true);

            ImportFileActionLink csvLink = new ImportFileActionLink();
            csvLink.setText(wfmStrings.csv());
            csvLink.addClickHandler(ch -> imp.open());
            items.add(csvLink);

            ImportFileActionLink vCard = new ImportFileActionLink();
            vCard.setText("vCard");
            vCard.addClickHandler(ch -> {
                final ImportVCardFilePopup vCardImp = new ImportVCardFilePopup();
                vCardImp.ensureDebugId("vCardImp");
            });
            items.add(vCard);

            li.add(importItem);
            li.add(items);

            menuContainer.add(li);
        }

        if (Utils.hasPermission(CRM_CONTACTS_EXPORT)) {
            ListItem exItem = setExportAs(toolPanel);
            menuContainer.add(exItem);
        }
        exportOption.initExport(toolPanel, false);
    }

    private ListItem setExportAs(FlowPanel toolPanel) {
        ListItem li = new ListItem();

        MaterialLink exportItem = new MaterialLink(wfmStrings.exportAs());

        MaterialDropDown items = new MaterialDropDown(exportItem);
        items.setHover(true);
        items.setBelowOrigin(true);
        items.setClass("dropdown-content--2");
        items.setBelowOrigin(true);

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");
        if (pdfVersion.isVisible()) {
            Div wrapper = new Div("java-wrap");

            MaterialLink pdfVersion = getPdfVersion();
            wrapper.add(pdfVersion);
            items.add(wrapper);

            MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
            mdp.setHover(true);
            mdp.setHoverable(true);

            mdp.add(ContactListView.this::getPortraitLink);
            mdp.add(ContactListView.this::getLandscapeLink);

            wrapper.add(mdp);

        }
        sendPdfRequest(toolPanel);


        MaterialLink excel = new MaterialLink();
        MaterialIcon excelIcon = new MaterialIcon();
        excelIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
        excel.add(excelIcon);
        excel.setText(wfmStrings.excel());
        excel.addClickHandler(event -> {
            if (totalCount > 1000) {
                String limit = "1000";
                if (defaultOne != null && defaultOne.getExcelLimit() != null) {
                    limit = defaultOne.getExcelLimit();
                }
                Window.alert(wfmMessages.currentlyLimitedContactExport(limit));
            }
            String exportToExcelURL = CommandConstants.COMMON_URL + "/downloadCrmContactsExcel";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            HashMap<String, String> parameters = listingFilterParameter.getRequestParams();
            long startedAt = System.currentTimeMillis();
            GWT.log("Export to excel started at: " + new Date());
            Utils.sendPDFOrExcelRequest(toolPanel, exportToExcelURL, parameters, "_blank");
            GWT.log("It took to download excel file: " + (System.currentTimeMillis() - startedAt));
        });
        items.add(excel);

        MaterialLink exportToCSV = new MaterialLink();
        MaterialIcon csvIcon = new MaterialIcon();
        csvIcon.setStylePrimaryName("ficon--file-csv hasicon--left");
        exportToCSV.add(csvIcon);
        exportToCSV.setText(crmStrings.csvExport());
        exportToCSV.addClickHandler(event -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String csvURL = CommandConstants.COMMON_URL + "/downloadCrmContactsCsv";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            HashMap<String, String> params = listingFilterParameter.getRequestParams();
            Utils.sendCSVRequest(toolPanel, csvURL, params, "_blank");
        });
        items.add(exportToCSV);


        MaterialLink exportToVCard = new MaterialLink();
        MaterialIcon icon = new MaterialIcon();
        icon.setStylePrimaryName("ficon--file-vcard hasicon--left");
        exportToVCard.add(icon);
        exportToVCard.setText("vCard");
        exportToVCard.addClickHandler(event -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String csvURL = CommandConstants.COMMON_URL + "/downloadCrmContactsVCard";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            HashMap<String, String> params = listingFilterParameter.getRequestParams();
            Utils.sendCSVRequest(toolPanel, csvURL, params, "_blank");
        });
        items.add(exportToVCard);

        li.add(exportItem);
        li.add(items);
        return li;
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private void sendPdfRequest(FlowPanel toolPanel, boolean landscape) {
        if (totalCount > 1000) {
            String limit = "1000";
            if (defaultOne != null && defaultOne.getPdfLimit() != null) {
                limit = defaultOne.getPdfLimit();
            }
            Window.alert(wfmMessages.currentlyLimitedContactExport(limit));
        }
        String pdfURL = CommandConstants.PDF_URL + "/crmContactListPDFHandler";
        ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
        listingFilterParameter.setLandscape(landscape);
        listingFilterParameter.setPropertyCode(getPropertyCode());
        listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
        listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
        HashMap<String, String> parametrs = listingFilterParameter.getRequestParams();
        long startedAt = System.currentTimeMillis();
        GWT.log("Export to pdf started at: " + new Date());
        Utils.sendPDFOrExcelRequest(toolPanel, pdfURL, parametrs, "_blank");
        GWT.log("Generate pdf finished in: " + (System.currentTimeMillis() - startedAt));
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    private void sendPdfRequest(FlowPanel toolPanel) {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(toolPanel, false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(toolPanel, true);
        });
    }

    private ListItem setSynchronizeGoogleContact() {
        ListItem li = new ListItem();
        MaterialLink exportItem = new MaterialLink(property.getPlural(crmStrings.googleContacts(), wfmStrings.contacts()));

        MaterialDropDown items = new MaterialDropDown(exportItem);
        items.setHover(true);
        items.setBelowOrigin(true);

        MaterialLink syncGoogelSettingsItem = new MaterialLink();
        syncGoogelSettingsItem.setText(crmStrings.settingsGoogleFolders());
        syncGoogelSettingsItem.ensureDebugId("settingsGoogleFolders");

        MaterialLink synchronizeGoogleItem = new MaterialLink();
        synchronizeGoogleItem.setText("&nbsp");
        ContactService.App.get().validateUserGoogle(new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                synchronizeGoogleItem.setText(wfmStrings.configureWithGoogle());
                configureWithGoogle = false;
                syncGoogelSettingsItem.setVisible(false);
            }

            @Override
            public void success(Boolean result) {
                if (!result) {
                    synchronizeGoogleItem.setText(wfmStrings.configureWithGoogle());
                    configureWithGoogle = false;
                    syncGoogelSettingsItem.setVisible(false);
                } else {
                    synchronizeGoogleItem.setText(crmStrings.synGoogleContact());
                    configureWithGoogle = true;
                }
            }
        });

        synchronizeGoogleItem.addClickHandler(event -> {
            if (configureWithGoogle) {
                contactService.hasContactCategorySettings(GOOGLE, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.error(), Info.Type.INFO);
                    }

                    @Override
                    public void onSuccess(Boolean hasContactCategorySetting) {
                        if (hasContactCategorySetting) {
                            LoadingPanel.loading(true);
                            ContactService.App.get().synchronizeContactsWithGoogleInBackground(GOOGLE, new AbstractAsyncCallback<String>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(String result) {
                                    LoadingPanel.loading(false);
                                    if ("Success".equals(result)) {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                                        messageBox.setMessage(crmMessages.googleContactSyncInProgress());
                                        messageBox.setTitle(wfmStrings.information());
                                        messageBox.open();
                                    } else {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                                        messageBox.setTitle(wfmStrings.error());
                                        messageBox.setMessage(crmMessages.syncingGoogleContactMessage(property.getSingular(wfmStrings.contact())));
                                        messageBox.open();
                                    }
                                }
                            });
                        } else {
                            Info.show(crmMessages.matchAtLeastOneCategory(), Info.Type.INFO);
                        }
                    }
                });
            } else {
                ContactService.App.get().validateUserGoogle(new AbstractAsyncCallback<Boolean>() {
                    public void success(Boolean isValid) {
                        if (isValid) {
                            synchronizeGoogleItem.setText(property.getSingular(crmStrings.synchronizeGoogleContact(), wfmStrings.contact()));
                            configureWithGoogle = true;
                            syncGoogelSettingsItem.setVisible(true);
                        } else {
                            new GoogleContactsQuestionPopup(GOOGLE);
                        }
                    }
                });
            }
        });

        syncGoogelSettingsItem.addClickHandler(clickEvent -> new GoogleContactGroupBox(GOOGLE));

        items.add(synchronizeGoogleItem);
        items.add(syncGoogelSettingsItem);
        li.add(exportItem);
        li.add(items);
        return li;
    }

    private ListItem setSynchronizeOffice365Contact() {
        ListItem li = new ListItem();
        MaterialLink exportItem = new MaterialLink(crmStrings.office365Contacts());

        MaterialDropDown items = new MaterialDropDown(exportItem);
        items.setHover(true);
        items.setBelowOrigin(true);

        MaterialLink officeFolderSettingsItem = new MaterialLink();
        officeFolderSettingsItem.setText(crmStrings.settingsOfficeFolders());
        officeFolderSettingsItem.ensureDebugId("settingsOfficeFolders");

        MaterialLink synchronizeOfficeItem = new MaterialLink();
        synchronizeOfficeItem.setText("&nbsp");
        ContactService.App.get().validateUserOffice(new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                synchronizeOfficeItem.setText(crmStrings.confWithOFfice365());
                configureWithOffice = false;
                officeFolderSettingsItem.setVisible(false);
            }

            @Override
            public void success(Boolean result) {
                if (!result) {
                    synchronizeOfficeItem.setText(crmStrings.confWithOFfice365());
                    configureWithOffice = false;
                    officeFolderSettingsItem.setVisible(false);
                } else {
                    synchronizeOfficeItem.setText(crmStrings.synOfficeContact());
                    configureWithOffice = true;
                }
            }
        });

        synchronizeOfficeItem.addClickHandler(clickEvent -> {
            if (configureWithOffice) {
                contactService.hasContactCategorySettings(OFFICE_365, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.show(wfmStrings.error(), Info.Type.INFO);
                    }

                    @Override
                    public void onSuccess(Boolean hasContactCategorySetting) {
                        if (hasContactCategorySetting) {
                            LoadingPanel.loading(true);
                            ContactService.App.get().synchronizeContactsWithGoogleInBackground(OFFICE_365, new AbstractAsyncCallback<String>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(String result) {
                                    LoadingPanel.loading(false);
                                    if ("Success".equals(result)) {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                                        messageBox.setMessage(crmMessages.googleContactSyncInProgress());
                                        messageBox.setTitle(wfmStrings.information());
                                        messageBox.open();
                                    } else {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                                        messageBox.setTitle(wfmStrings.error());
                                        messageBox.setMessage(crmMessages.syncingGoogleContactMessage(property.getSingular(wfmStrings.contact())));
                                        messageBox.open();
                                    }
                                }
                            });
                        } else {
                            Info.show(crmMessages.matchAtLeastOneCategory(), Info.Type.INFO);
                        }
                    }
                });
            } else {
                ContactService.App.get().validateUserOffice(new AbstractAsyncCallback<Boolean>() {
                    public void success(Boolean isValid) {
                        if (isValid) {
                            synchronizeOfficeItem.setText(property.getSingular(crmStrings.synchronizeOfficeContact(), wfmStrings.contact()));
                            configureWithOffice = true;
                            officeFolderSettingsItem.setVisible(true);
                        } else {
                            new GoogleContactsQuestionPopup(OFFICE_365);
                        }
                    }
                });
            }
        });

        officeFolderSettingsItem.addClickHandler(clickEvent -> new GoogleContactGroupBox(OFFICE_365));

        items.add(synchronizeOfficeItem);
        items.add(officeFolderSettingsItem);
        li.add(exportItem);
        li.add(items);
        return li;
    }

    private MenuBar getActionsForSelections() {
        if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(true);
                if (Utils.hasPermission(CRM_CHANGE_CAMPAIGN)) {
                    actions.addMenuItem(crmStrings.changeCampaign(), true, () -> {
                        actions.hide();
                        campaignPopup.getItemIDs().clear();
                        if (selectedItems.size() > 0) {
                            for (ContactListItem item : selectedItems) {
                                campaignPopup.getItemIDs().add(item.getObjectId());
                            }
                            campaignPopup.open();
                        } else {
                            list.showSelectOneMessage();
                        }
                    });
                }
                if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                    actions.addMenuItem(crmStrings.addToMailingList(), null, true, () -> {
                        if (list.getItemCount() > 0) {
                            showEditSubscriptionsShell(null);
                        } else {
                            Info.show(Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.noLeadsToAddToMailList(), wfmStrings.leads()), Info.Type.WARNING);
                        }
                    });
                }
                if (Utils.hasPermission(CRM_CONTACTS_DETECT_DUBLICATES)) {
                    actions.addMenuItem(wfmStrings.detectDuplicates(), true, () -> detectDuplicates());
                }
                if (Utils.hasPermission(CRM_CONTACTS_MERGE)) {
                    actions.addMenuItem(wfmStrings.merge(), true, () -> {
                        if (selectedItems.size() >= 2) {
                            StringBuilder ids = new StringBuilder();
                            String delimitr = "/";
                            int i = 0;
                            for (ContactListItem item : selectedItems) {
                                ids.append(delimitr).append(item.getObjectId().toString());
                                if (i++ == 4) {
                                    break;
                                }
                            }
                            SinksContainerFactory.entryPoint.onHistoryChanged("contactmerge|add/add/" + CrmConstants.CRM_CONTACT + ids);
                        }
                    });
                }
                // copy category
                if (Utils.hasPermission(CRM_CONTACT_CATEGORY_COPY)) {
                    actions.addMenuItem(wfmStrings.copyToCategory(), true, () -> {
                        if (selectedItems != null && selectedItems.size() > 0) {
                            changeCategory(ContactCategoryListItem.COPY);
                        } else {
                            Info.show(wfmMessages.pleaseSelectOneRowCopy(wfmStrings.category()), Info.Type.WARNING);
                        }
                    });
                }
                // move to category
                if (Utils.hasPermission(CRM_CONTACT_CATEGORY_MOVE)) {
                    actions.addMenuItem(wfmStrings.moveToCategory(), true, () -> {
                        if (selectedItems != null && selectedItems.size() > 0) {
                            changeCategory(ContactCategoryListItem.MOVE);
                        } else {
                            Info.show(wfmMessages.pleaseSelectOneRowMove(wfmStrings.category()), Info.Type.WARNING);
                        }
                    });
                }
                if (Utils.hasPermission(CRM_REMOVE_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_DELETE))) {
                    actions.addMenuItem(wfmStrings.delete(), true, () -> {
                        actions.hide();
                        deleteSelection();
                    });
                }
            }
            actions.getMenuBar().setAutoOpen(true);
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                    emptyActions.addMenuItem(wfmStrings.createMailingList(), null, true, () -> {
                        if (totalCount > 0) {
                            ListingFilterParameter filterParam = list.getFilterParametrs() == null ? new ListingFilterParameter() : list.getFilterParametrs();
                            filterParam.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParam.getFacetFilter()));
                            filterParam.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParam.getListPanelTool()));
                            new MailListPopup(null, null, false, filterParam);
                        } else {
                            Info.show(Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.noLeadsToAddToMailList(), wfmStrings.leads()), Info.Type.WARNING);
                        }
                    });
                }
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }
    }

    private void detectDuplicates(ContactListItem... items) {
        boolean detectDuplicatesByArguments = items != null && items.length > 0;
        if (!detectDuplicatesByArguments && selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneToDetectDuplicates(property.getSingular(wfmStrings.contact())), Info.Type.WARNING);
        } else {
            detectDuplicates = true;
            isRevertedFirstPage = false;
            isClickedDuplicateBtn = true;
            lastSelectedItems.clear();
            lastSelectedItems.addAll(detectDuplicatesByArguments ? Arrays.asList(items) : selectedItems);
            list.reloadPage();
        }
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.contact())), Info.Type.WARNING);
        } else {
            deleteContactItem(new ArrayList<>(selectedItems), true);
        }
    }

    protected void deleteContactItem(final ArrayList<ContactListItem> items, final boolean massDeletion) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.sureYouWantToDelete();
        if (massDeletion) {
            message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        }
        messageBox.setMessage(message);
        final KpiCheckBox googleCheckBox = new KpiCheckBox("&nbsp;" + property.getPlural(crmStrings.mesContactfromGoogle(), wfmStrings.contact()), true);
        googleCheckBox.setVisible(ContactListItem.hasTokenAndGoogleIDAndOwner(items, Utils.getUserID()));
        messageBox.add(googleCheckBox);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                boolean deleteFromGoogle = ContactListItem.hasTokenAndGoogleIDAndOwner(items, Utils.getUserID()) && googleCheckBox.getValue();
                deleteContact(items, deleteFromGoogle);
            }
        });
        messageBox.open();
    }

    private void deleteContact(final ArrayList<ContactListItem> items, final boolean deleteFromGoogle) {
        LoadingPanel.loading(true);
        ContactService.App.get().deleteContacts(getIDs(items), null, deleteFromGoogle, new AbstractAsyncCallback<ArrayList<Integer>>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ArrayList<Integer> result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_DELETE, result, ContactListView.this);
                showContactsUpdatedMessageBox(result, items.size() == 1, true, false, getIDs(items).size() == result.size(), 3);
            }
        });
    }

    protected ArrayList<Integer> getIDs(ArrayList<ContactListItem> selectedContacts) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (selectedContacts.size() > 0) {
            for (ContactListItem item : selectedContacts) {
                if (item != null && !ids.contains(item.getObjectId())) {
                    ids.add(item.getObjectId());
                }
            }
        }
        return ids;
    }

    public void showContactsUpdatedMessageBox(ArrayList<Integer> result, boolean onlyOne, boolean deleting, boolean isCandidate, boolean noOneDeleted, Integer action) {
        String message = "";
        if (noOneDeleted || (result != null && result.size() > 0 && onlyOne)) {
            if (action == 1) {
                message = property.getSingular(wfmStrings.copiedSuccessfully(), wfmStrings.contact());
            } else if (action == 2) {
                message = property.getSingular(wfmStrings.movedSuccessfully(), wfmStrings.contact());
            } else {
                message = property.getPlural(crmStrings.cannotDeleteContactsDueToPermissions(), wfmStrings.contacts());
            }
        } else if (!noOneDeleted && result != null && result.size() > 0) {
            message = crmMessages.successfullyDeletedButSomeNot(crmStrings.removed(), String.valueOf(result.size()));
        } else {
            String lead = Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads());
            String contacts = property.getPlural(wfmStrings.contacts());
            String s = isLead ? lead.toLowerCase() : contacts.toLowerCase();
            message = deleting ? (isCandidate ? wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.candidate().toLowerCase()) : crmMessages.messContactsSucDeleted(s)) :
                    wfmStrings.messSuccessfulyyDeleted();
        }
        Info.show(message, Info.Type.INFO);
    }

    private static final int DELETE = 1;
    private static final int MAKE_PRIMARY = 3;

    private void checkForPermission(PermissionHolder permissionHolder, ContactListItem contact, int action) {
        if (action == DELETE) {
            if ((permissionHolder != null && permissionHolder.isDelete()) || Utils.hasRole(ADMIN) || (Utils.getUserID().equals(contact.getOwnerId()))) {
                deleteContactItem(Utils.asArrayList(contact), false);
            } else {
                showPermissionMessage(wfmStrings.delete());
            }
        } else if (action == EDIT) {
            if (permissionHolder.isWrite() || Utils.hasRole(ADMIN) || Utils.hasPermission(CRM_EDIT_CONTACT)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + contact.getObjectId(), contact.getName(), contact.getName());
            } else {
                showPermissionMessage(wfmStrings.edit());
            }
        } else if (action == COPY.hashCode()) {
            if (permissionHolder.isWrite() || Utils.hasRole(ADMIN) || Utils.hasPermission(CRM_COPY_CONTACT)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add/" + contact.getObjectId() + "/" + COPY);
            } else {
                showPermissionMessage(wfmStrings.copy());
            }
        } else if (action == MAKE_PRIMARY) {
            if (permissionHolder.isWrite() || Utils.hasRole(ADMIN) || (Utils.getUserID().equals(contact.getOwnerId()))) {
                makePrimary(contact);
            } else {
                showPermissionMessage(wfmStrings.makePrimary());
            }
        }
    }

    private void makePrimary(final ContactListItem contact) {
        if (contact != null && contact.getCrmAccount().getObjectId() != null && contact.getObjectId() != null) {
            CRMService.App.get().makePrimaryContact(contact.getCrmAccount().getObjectId(), contact.getObjectId(), new AbstractAsyncCallback<ContactListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(ContactListItem result) {
                    if (result != null && result.isPrimaryContact()) {
                        contact.setPrimaryContact(result.isPrimaryContact());
                        Info.show(wfmStrings.madePrimaryForAccount());
                    }
                }
            });
        }
    }

    protected ArrayList<Integer> getIDs(Set<ContactListItem> selectedContacts) {
        return getIDs(new ArrayList<>(selectedContacts));
    }

    public String getIconStyle() {
        return "contact contact-list";
    }

    private void changeCategory(final int action) {
        KpiModal categoryPopup = new KpiModal();
        categoryPopup.setWidth(350);
        categoryPopup.setTitle(wfmStrings.changeCategory());

        final DataListBox categories = new DataListBox();
        categories.addStyleName(DEFAULT_WIDTH);
        LoadingPanel.loading(true);
        contactCategoryService.getContactCategories(new AbstractAsyncCallback<ArrayList<ContactCategoryListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<ContactCategoryListItem> contactCategoryListItems) {
                LoadingPanel.loading(false);
                categories.setItems(ContactCategoryListItem.getAsTreeSelectItem(contactCategoryListItems));
            }
        });
        categoryPopup.add(categories);
        categoryPopup.addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> categoryPopup.close()));
        categoryPopup.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (!Validation.validateDataListBoxRequired(categories)) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }
            LoadingPanel.loading(true);
            contactCategoryService.changeCategory(categories.getSelectedId(), getIDs(selectedItems), action, new AbstractAsyncCallback<ArrayList<Integer>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.errorOccurred(), Info.Type.INFO);
                }

                @Override
                public void success(ArrayList<Integer> result) {
                    LoadingPanel.loading(false);
                    categoryPopup.close();
                    list.reloadPage();
                    showContactsUpdatedMessageBox(result, selectedItems.size() == 1, false, false, getIDs(selectedItems).size() == result.size(), action);
                }
            });
        }));
        categoryPopup.open();
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

    private void setSelectedCountryName(String selectedCountryName) {
        this.selectedCountryName = selectedCountryName;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        if (parentId != null) {
            initContactList(fp, null, container);
            onInitialize();
            clear();
        }
    }

    @Override
    public String getPropertyCode() {
        return Constants.Contacts;
    }
}
