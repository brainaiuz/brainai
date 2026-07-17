package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.IMAddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.reference.WebAddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class ImportContactView extends ImportAbstractView implements Colapse {

    public static final String PARAM_LIST_BOX = "PARAM_LIST_BOX";
    private static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    private static final String ADDRESS_NAME_LIST_BOX = "ADDRESS_NAME_LIST_BOX";
    private static final String ADDRESS_STREET_LIST_BOX = "ADDRESS_STREET_LIST_BOX";
    private static final String ADDRESS_STREET_LIST_BOX2 = "ADDRESS_STREET_LIST_BOX2";
    private static final String ADDRESS_CITY_LIST_BOX = "ADDRESS_CITY_LIST_BOX";
    private static final String ADDRESS_COUNTRY_LIST_BOX = "ADDRESS_COUNTRY_LIST_BOX";
    private static final String ADDRESS_STATE_LIST_BOX = "ADDRESS_STATE_LIST_BOX";
    private static final String ADDRESS_POSTCODE_LIST_BOX = "ADDRESS_POSTCODE_LIST_BOX";
    private static final Integer EMAILS = 1;
    private static final Integer PHONES = 2;
    private static final Integer IM_ADDRESSES = 3;
    private static final Integer WEB_ADDRESSES = 4;
    private static final Integer ADDRESSES = 5;

    private Integer mailingListId;
    //Personal Information
    private KpiRadioButton ownerFromFile;
    private KpiRadioButton ownerFromSystem;
    private DataListBox systemOwner;
    private DataListBox fileOwner;
    private FlowPanel ownerPanel;
    private DataListBox firstName;
    private DataListBox lastName;
    private DataListBox title;
    private DataListBox dateOfBirth;
    private DataListBox companyName;
    private DataListBox jobTitle;
    private DataListBox department;
    //Contact Information
    private MultiTable emails;
    private MultiTable phones;
    private MultiTable imAddresses;
    private MultiTable webAddresses;
    //Address Information
    private MultiTable addresses;
    //CRM details
    private KpiRadioButton categoryFromFile;
    private KpiRadioButton categoryFromSystem;
    private DataListBox fileCategory;
    private DataListBox systemCategory;
    private FlowPanel categoryPanel;
    private KpiRadioButton campaignFromFile;
    private KpiRadioButton campaignFromSystem;
    private DataListBox fileCampaign;
    private CRMLookUp systemCampaign;
    private FlowPanel campaignPanel;
    private KpiSwitcher emailOpt;
    private KpiSwitcher primaryContact;
    //Lead Form
    protected DataListBox leadBackupAssignee;
    protected DataListBox leadSource;
    protected DataListBox leadStatus;
    protected DataListBox leadRating;
    protected KpiRadioButton assigneeFromSystem;
    protected KpiRadioButton assigneeFromFile;
    protected DataListBox fileAssignee;
    protected DataListBox systemAssignee;
    protected FlowPanel assigneePanel;
    private final CrmStrings crmStrings = CrmStrings.App.get();

    public ImportContactView(Integer objectId, String name, String description) {
        super(name, description);
        this.objectId = objectId;
    }

    public ImportContactView(Integer objectId, Integer mailingListId) {
        super("importcontact", wfmMessages.importEntity(wfmStrings.contact()));
        this.objectId = objectId;
        this.mailingListId = mailingListId;
    }

    public void initialize() {
        LoadingPanel.loading(true);
        createAndSetWidth();
        super.initialize();
        ContactService.App.get().getContactDataForImport(ImportTypeEnum.CANDIDATE.equals(getType()) ? PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER : PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem result) {
                systemOwner.setItems(result.getLeadAssignees());
                systemOwner.setSelected(Utils.getUserID());
                if (ImportTypeEnum.LEAD.equals(getType())) {
                    systemAssignee.setItems(result.getLeadAssignees());
                    systemAssignee.setSelected(Utils.getUserID());
                    leadBackupAssignee.setItems(result.getLeadAssignees());
                } else if (ImportTypeEnum.CONTACT.equals(getType())) {
                    systemCategory.setItems(result.getCategories());
                }
                LoadingPanel.loading(false);
            }
        });
    }

    protected void createAndSetWidth() {
        //Personal Information
        ownerFromFile = new KpiRadioButton("ownerInsertionType", wfmStrings.fromFile());
        ownerFromFile.getElement().setId("ownerFromFile");
        ownerFromFile.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                ownerPanel.clear();
                ownerPanel.add(fileOwner);
            }
        });
        ownerFromFile.setValue(true);
        ownerFromSystem = new KpiRadioButton("ownerInsertionType", wfmStrings.fromSystem());
        ownerFromSystem.getElement().setId("ownerFromSystem");
        ownerFromSystem.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                ownerPanel.clear();
                ownerPanel.add(systemOwner);
            }
        });

        systemOwner = new DataListBox();
        systemOwner.getElement().setId("systemOwner");
        systemOwner.addStyleName(DEFAULT_WIDTH);
        fileOwner = new DataListBox();
        fileOwner.getElement().setId("fileOwner");
        fileOwner.addStyleName(DEFAULT_WIDTH);
        ownerPanel = new FlowPanel();
        ownerPanel.add(fileOwner);

        firstName = new DataListBox();
        firstName.getElement().setId("first_name");
        firstName.addStyleName(DEFAULT_WIDTH);

        lastName = new DataListBox();
        lastName.getElement().setId("last_name");
        lastName.addStyleName(DEFAULT_WIDTH);

        dateOfBirth = new DataListBox();
        dateOfBirth.getElement().setId("date_of_birth");
        dateOfBirth.addStyleName(DEFAULT_WIDTH);

        title = new DataListBox();
        title.getElement().setId("title");
        title.addStyleName(DEFAULT_WIDTH);

        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            jobTitle = new DataListBox();
            jobTitle.getElement().setId("job_title");
            jobTitle.addStyleName(DEFAULT_WIDTH);

            department = new DataListBox();
            department.getElement().setId("department");
            department.addStyleName(DEFAULT_WIDTH);

            companyName = new DataListBox();
            companyName.getElement().setId("company_name");
            companyName.addStyleName(DEFAULT_WIDTH);
        }

        //Contact Information
        emails = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(EMAILS);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        emails.getElement().setId("emails");
        phones = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(PHONES);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        phones.getElement().setId("phones");
        imAddresses = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(IM_ADDRESSES);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        imAddresses.getElement().setId("imAddress");
        webAddresses = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(WEB_ADDRESSES);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        webAddresses.getElement().setId("webAddress");
        //Address Information
        addresses = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets();
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        addresses.getElement().setId("addresses");

        //Crm Details
        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            if (ImportTypeEnum.CONTACT.equals(getType())) {
                categoryFromFile = new KpiRadioButton("categoryInsertionType", wfmStrings.fromFile());
                categoryFromFile.getElement().setId("from_file");
                categoryFromFile.addValueChangeHandler(booleanValueChangeEvent -> {
                    if (booleanValueChangeEvent.getValue()) {
                        categoryPanel.clear();
                        categoryPanel.add(fileCategory);
                    }
                });
                categoryFromFile.setValue(true);
                categoryFromSystem = new KpiRadioButton("categoryInsertionType", wfmStrings.fromSystem());
                categoryFromSystem.getElement().setId("not_from_file");
                categoryFromSystem.addValueChangeHandler(booleanValueChangeEvent -> {
                    if (booleanValueChangeEvent.getValue()) {
                        categoryPanel.clear();
                        categoryPanel.add(systemCategory);
                    }
                });

                fileCategory = new DataListBox();
                fileCategory.getElement().setId("from_file");
                fileCategory.addStyleName(DEFAULT_WIDTH);
                systemCategory = new DataListBox();
                systemCategory.getElement().setId("not_from_file_list");
                systemCategory.addStyleName(DEFAULT_WIDTH);
                categoryPanel = new FlowPanel();
                categoryPanel.add(fileCategory);

                primaryContact = new KpiSwitcher();
                primaryContact.getElement().setId("primary_contact");
            }

            campaignFromFile = new KpiRadioButton("campaignInsertionType", wfmStrings.fromFile());
            campaignFromFile.getElement().setId("campaign_from_file");
            campaignFromFile.addValueChangeHandler(booleanValueChangeEvent -> {
                if (booleanValueChangeEvent.getValue()) {
                    campaignPanel.clear();
                    campaignPanel.add(fileCampaign);
                }
            });
            campaignFromFile.setValue(true);
            campaignFromSystem = new KpiRadioButton("campaignInsertionType", wfmStrings.fromSystem());
            campaignFromSystem.getElement().setId("campaign_not_from_file");
            campaignFromSystem.addValueChangeHandler(booleanValueChangeEvent -> {
                if (booleanValueChangeEvent.getValue()) {
                    campaignPanel.clear();
                    campaignPanel.add(systemCampaign);
                }
            });

            fileCampaign = new DataListBox();
            fileCampaign.getElement().setId("fileCampaign");
            fileCampaign.addStyleName(DEFAULT_WIDTH);
            systemCampaign = new CRMLookUp(LookUpConstants.CRM_CAMPAIGN_ID);
            systemCampaign.getElement().setId("systemCampain");
            systemCampaign.addStyleName(DEFAULT_WIDTH);
            campaignPanel = new FlowPanel();
            campaignPanel.add(fileCampaign);

            emailOpt = new KpiSwitcher();
            emailOpt.getElement().setId("email_option");
        }
    }

    @Override
    public void drawForm() {
        addTitleField(PERSONAL_INFORMATION, wfmStrings.personalInformation());
        FlexTable radio = new FlexTable();
        radio.addStyleName(DEFAULT_WIDTH);
        radio.setWidget(0, 0, ownerFromFile);
        radio.setWidget(0, 1, ownerFromSystem);
        addField(OWNER_CHOOSE, radio, getTitle(crmStrings.chooseOwner()));
        addField(OWNER, ownerPanel, getTitle(wfmStrings.owner()));
        addField(CustomFormConstants.FIRST_NAME, firstName, getTitle(wfmStrings.firstName()));
        addField(CustomFormConstants.LAST_NAME, lastName, getTitle(wfmStrings.lastName()));
        addField(BIRTH_DAY, dateOfBirth, getTitle(wfmStrings.dateOfBirth()));
        addField(BIRTH_DAY_FORMAT, new HTML("<span>&nbsp;" + crmStrings.dateFormatMustBeInThisFormat() + DATE_PATTERN + "</span>"), "");
        addField(TITLE, title, getTitle(wfmStrings.title()));

        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            addField(JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
            addField(COMPANY_NAME, companyName, getTitle(wfmStrings.companyName()));
        }

        addTitleField(CONTACT_INFORMATION, Property.get(Constants.Contacts, wfmStrings.contactInformation(), wfmStrings.contact()));
        addField(CustomFormConstants.EMAIL, emails, getTitle(wfmStrings.email()));
        addField(CustomFormConstants.PHONE, phones, getTitle(wfmStrings.phone()));
        addField(IM_ADDRESS, imAddresses, getTitle(wfmStrings.imAddress()));
        addField(WEB_ADDRESS, webAddresses, getTitle(wfmStrings.webAddress()));

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(ADDRESS, addresses, getTitle(wfmStrings.address()));

        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            addTitleField(CRM_DETAILS, wfmStrings.crmDetails());
            if (ImportTypeEnum.CONTACT.equals(getType())) {
                FlexTable radioButtons = new FlexTable();
                radioButtons.setWidget(0, 0, categoryFromFile);
                radioButtons.setWidget(0, 1, categoryFromSystem);
                addField(CHOOSE_CATEGORY_INSERTION_TYPE, radioButtons, crmStrings.chooseCategoryInsertionType());
                addField(CATEGORY, categoryPanel, wfmStrings.category());
                addField(PRIMARY_CONTACT, primaryContact, wfmStrings.primaryContact());
            }
            FlexTable radioButtons = new FlexTable();
            radioButtons.addStyleName(DEFAULT_WIDTH);
            radioButtons.setWidget(0, 0, campaignFromFile);
            radioButtons.setWidget(0, 1, campaignFromSystem);
            addField(CHOOSE_CAMPAIGN_FROM_FILE, radioButtons, crmStrings.chooseCampaignInsertionType());
            addField(CAMPAIGN, campaignPanel, wfmStrings.campaign());
            addField(EMAIL_OPT_OUT, emailOpt, wfmStrings.emailOptOut());
        }
        super.drawForm();
    }

    @Override
    public void setItems(SelectItem[] items) {
        //Personal Information
        fileOwner.setItems(items, wfmStrings.owner());
        firstName.setItems(items, wfmStrings.firstName());
        lastName.setItems(items, wfmStrings.lastName());
        dateOfBirth.setItems(items, wfmStrings.dateOfBirth());
        title.setItems(items, wfmStrings.title());
        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            jobTitle.setItems(items, wfmStrings.jobTitle());
            department.setItems(items, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
            companyName.setItems(items, wfmStrings.companyName());
        }
        //Contact Information
        emails.removeAllRows();
        emails.addWidgets(getMultiWidgets(EMAILS));
        phones.removeAllRows();
        phones.addWidgets(getMultiWidgets(PHONES));
        imAddresses.removeAllRows();
        imAddresses.addWidgets(getMultiWidgets(IM_ADDRESSES));
        webAddresses.removeAllRows();
        webAddresses.addWidgets(getMultiWidgets(WEB_ADDRESSES));
        //Address information
        addresses.removeAllRows();
        addresses.addWidgets(getAddressWidgets());
        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            //Crm Details
            if (ImportTypeEnum.CONTACT.equals(getType())) {
                fileCategory.setItems(items, wfmStrings.category());
            }
            fileCampaign.setItems(items, wfmStrings.campaign());
            fileAssignee.setItems(items, wfmStrings.assignee());
        }
        if (tbValues != null) {
            for (DataListBox dataListBox : tbValues) {
                if (dataListBox != null) {
                    dataListBox.setItems(items);
                }
            }
        }
        LoadingPanel.loading(false);
    }

    protected ContactListItem getRPC() {
        ContactListItem item = new ContactListItem();
        item.setObjectId(objectId);

        item.setOwnerFromFile(ownerFromFile.getValue());
        item.setOwnerId(ownerFromFile.getValue() ? getSelectedItem(fileOwner) : getSelectedItem(systemOwner));
        item.setFirstNameId(getSelectedItem(firstName));
        item.setLastNameId(getSelectedItem(lastName));
        item.setBirthDateId(getSelectedItem(dateOfBirth));
        item.setTitleId(getSelectedItem(title));

        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            item.setJobTitleId(getSelectedItem(jobTitle));
            item.setDepartmentID(getSelectedItem(department));
            item.getCrmAccount().setObjectId(getSelectedItem(companyName));
        }

        setMultiResults(item, EMAILS);
        setMultiResults(item, PHONES);
        setMultiResults(item, IM_ADDRESSES);
        setMultiResults(item, WEB_ADDRESSES);

        setMultiResults(item, ADDRESSES);

        if (!ImportTypeEnum.CANDIDATE.equals(getType())) {
            if (ImportTypeEnum.CONTACT.equals(getType())) {
                item.setCategoryFromFile(categoryFromFile.getValue());
                item.setCategoryID(categoryFromFile.getValue() ? getSelectedItem(fileCategory) : systemCategory.getSelectedId());
                item.setPrimaryContact(primaryContact.getValue());
            }
            item.setCampaignFromFile(campaignFromFile.getValue());
            item.setCampaignId(campaignFromFile.getValue() ? getSelectedItem(fileCampaign) : systemCampaign.getSelectedItemID());
            item.setEmailOptOut(emailOpt.getValue());
        }
        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (
                    int i = 0;
                    i < tbValues.length;
                    i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());
                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }
        return item;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Contact;
    }

    private WidgetsMap getAddressWidgets() {
        WidgetsMap widgetsMap = new WidgetsMap();

        HTML addressNameLabel = new HTML(wfmStrings.addressName());
        addressNameLabel.setWidth("120px");
        DataListBox addressName = new DataListBox();
        addressName.getElement().setId("addressName");
        addressName.addStyleName(DEFAULT_WIDTH);

        HTML streetLabel = new HTML(wfmStrings.addressLine1());
        streetLabel.setWidth("120px");
        DataListBox street = new DataListBox();
        street.getElement().setId("street");
        street.addStyleName(DEFAULT_WIDTH);

        HTML street2Label = new HTML(wfmStrings.addressLine2());
        street2Label.setWidth("120px");
        DataListBox street2 = new DataListBox();
        street2.getElement().setId("streetAddress2");
        street2.addStyleName(DEFAULT_WIDTH);

        HTML cityLabel = new HTML(wfmStrings.city());
        cityLabel.setWidth("120px");
        DataListBox city = new DataListBox();
        city.getElement().setId("city");
        city.addStyleName(DEFAULT_WIDTH);

        HTML countryLabel = new HTML(wfmStrings.country());
        countryLabel.setWidth("120px");
        DataListBox country = new DataListBox();
        country.getElement().setId("country");
        country.addStyleName(DEFAULT_WIDTH);

        HTML stateLabel = new HTML(wfmStrings.state());
        stateLabel.setWidth("120px");
        DataListBox state = new DataListBox();
        state.getElement().setId("state");
        state.addStyleName(DEFAULT_WIDTH);

        HTML postCodeLabel = new HTML(wfmStrings.postCode());
        postCodeLabel.setWidth("120px");
        DataListBox postCode = new DataListBox();
        postCode.getElement().setId("postcode");
        postCode.addStyleName(DEFAULT_WIDTH);

        DataListBox addressLocation = new DataListBox();
        addressLocation.getElement().setId("address_location");
        addressLocation.setWidth("100px");
        addressLocation.setWithoutNullLabel(true);
        addressLocation.setItems(AddContactView.getAddress());
        addressLocation.setSelected(AddressReference.WORK.getId());

        if (items != null) {
            addressName.setItems(items, wfmStrings.addressName());
            street.setItems(items, wfmStrings.addressLine1());
            street2.setItems(items, wfmStrings.addressLine2());
            city.setItems(items, wfmStrings.city());
            country.setItems(items, wfmStrings.country());
            state.setItems(items, wfmStrings.state());
            postCode.setItems(items, wfmStrings.postCode());
        }

        FlexTable addressPanel = new FlexTable();
        addressPanel.getElement().setAttribute("style", "border-collapse:separate;border-spacing:5px 5px;");
        addressPanel.setCellSpacing(10);
        addressPanel.setCellPadding(10);
        addressPanel.setWidget(0, 0, addressNameLabel);
        addressPanel.setWidget(0, 1, addressName);
        addressPanel.setWidget(0, 2, addressLocation);
        addressPanel.setWidget(1, 0, streetLabel);
        addressPanel.setWidget(1, 1, street);
        addressPanel.setWidget(2, 0, street2Label);
        addressPanel.setWidget(2, 1, street2);
        addressPanel.setWidget(3, 0, cityLabel);
        addressPanel.setWidget(3, 1, city);
        addressPanel.setWidget(4, 0, countryLabel);
        addressPanel.setWidget(4, 1, country);
        addressPanel.setWidget(5, 0, stateLabel);
        addressPanel.setWidget(5, 1, state);
        addressPanel.setWidget(6, 0, postCodeLabel);
        addressPanel.setWidget(6, 1, postCode);
        country.setAllowFirstItem(true);
        state.setAllowFirstItem(true);
        widgetsMap.addWidgetToMap(ADDRESS_NAME_LIST_BOX, addressName);
        widgetsMap.addWidgetToMap(ADDRESS_STREET_LIST_BOX, street);
        widgetsMap.addWidgetToMap(ADDRESS_STREET_LIST_BOX2, street2);
        widgetsMap.addWidgetToMap(ADDRESS_CITY_LIST_BOX, city);
        widgetsMap.addWidgetToMap(ADDRESS_COUNTRY_LIST_BOX, country);
        widgetsMap.addWidgetToMap(ADDRESS_STATE_LIST_BOX, state);
        widgetsMap.addWidgetToMap(ADDRESS_POSTCODE_LIST_BOX, postCode);
        widgetsMap.addWidgetToMap(RELATION_LIST_BOX, addressLocation);
        widgetsMap.addWidgets(addressPanel);
        return widgetsMap;
    }

    private static SelectItem[] getIMAddress() {
        SelectItem[] items = new SelectItem[8];
        items[0] = new SelectItem(IMAddressReference.GTALK.getId(), wfmStrings.contactgtalk());
        items[1] = new SelectItem(IMAddressReference.AIM.getId(), wfmStrings.contactaim());
        items[2] = new SelectItem(IMAddressReference.YAHOO.getId(), wfmStrings.contactyahoo());
        items[3] = new SelectItem(IMAddressReference.SKYPE.getId(), wfmStrings.contactskype());
        items[4] = new SelectItem(IMAddressReference.QQ.getId(), wfmStrings.contactqq());
        items[5] = new SelectItem(IMAddressReference.MSN.getId(), wfmStrings.contactmsn());
        items[6] = new SelectItem(IMAddressReference.ICQ.getId(), wfmStrings.contacticq());
        items[7] = new SelectItem(IMAddressReference.JABBER.getId(), wfmStrings.contactjabber());
        return items;
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        importFile.setMailingListId(mailingListId);
        return importFile;
    }

    public ImportFile createColumns(ContactListItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            //Personal Information
            importFile.addColumn(ImportField.ContactField.FIELD_OWNER_FROMFILE, item.isOwnerFromFile() ? 1 : 0);
            importFile.addColumn(ImportField.ContactField.FIELD_OWNER, item.getOwnerId());
            importFile.addColumn(ImportField.ContactField.FIELD_FIRSTNAME, item.getFirstNameId());
            importFile.addColumn(ImportField.ContactField.FIELD_LASTNAME, item.getLastNameId());
            importFile.addColumn(ImportField.ContactField.FIELD_BIRTHDAY, item.getBirthDateId());
            importFile.addColumn(ImportField.ContactField.FIELD_TITLE, item.getTitleId());
            importFile.addColumn(ImportField.ContactField.FIELD_JOB_TITLE, item.getJobTitleId());
            importFile.addColumn(ImportField.ContactField.FIELD_DEPARTMENT, item.getDepartmentID());
            importFile.addColumn(ImportField.ContactField.FIELD_ACCOUNT, item.getCrmAccount().getObjectId());
            //Contact Information
            //add Emails
            if (item.getHomeEmail().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_HOME_EMAILS, item.getHomeEmail().toArray(new String[]{}));
            }
            if (item.getWorkEmail().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_WORK_EMAILS, item.getWorkEmail().toArray(new String[]{}));
            }
            if (item.getOtherEmail().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_OTHER_EMAILS, item.getOtherEmail().toArray(new String[]{}));
            }
            //add Phones
            if (item.getHomePhone().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_HOME_PHONES, item.getHomePhone().toArray(new String[]{}));
            }
            if (item.getWorkPhone().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_WORK_PHONES, item.getWorkPhone().toArray(new String[]{}));
            }
            if (item.getMobile().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_MOBILE_PHONES, item.getMobile().toArray(new String[]{}));
            }
            if (item.getHomeFax().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_HOMEFAX_PHONES, item.getHomeFax().toArray(new String[]{}));
            }
            if (item.getWorkFax().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_WORKFAX_PHONES, item.getWorkFax().toArray(new String[]{}));
            }
            if (item.getPager().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_PAGER_PHONES, item.getPager().toArray(new String[]{}));
            }
            if (item.getOtherPhone().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_OTHER_PHONES, item.getOtherPhone().toArray(new String[]{}));
            }
            if (item.getExtension().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_EXTENSION, item.getExtension().toArray(new String[]{}));
            }
            //add IMs
            if (item.getgTalk().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_GTALKS, item.getgTalk().toArray(new String[]{}));
            }
            if (item.getAIM().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_AIMS, item.getAIM().toArray(new String[]{}));
            }
            if (item.getYahoo().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_YAHOOS, item.getYahoo().toArray(new String[]{}));
            }
            if (item.getSkype().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_SKYPES, item.getSkype().toArray(new String[]{}));
            }
            if (item.getQQ().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_QQS, item.getQQ().toArray(new String[]{}));
            }
            if (item.getMSN().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_MSNS, item.getMSN().toArray(new String[]{}));
            }
            if (item.getICQ().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_ICQS, item.getICQ().toArray(new String[]{}));
            }
            if (item.getJabber().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_IMS, ImportField.ContactField.FIELD_IM_JABBERS, item.getJabber().toArray(new String[]{}));
            }
            //add webAddresses
            if (item.getHomeWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_HOME_WEB_ADDRESSES, item.getHomeWebSite().toArray(new String[]{}));
            }
            if (item.getWorkWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_WORK_WEB_ADDRESSES, item.getWorkWebSite().toArray(new String[]{}));
            }
            if (item.getHomePage().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_HOMEPAGE_WEB_ADDRESSES, item.getHomePage().toArray(new String[]{}));
            }
            if (item.getFtp().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_FTP_WEB_ADDRESSES, item.getFtp().toArray(new String[]{}));
            }
            if (item.getBlog().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_BLOG_WEB_ADDRESSES, item.getBlog().toArray(new String[]{}));
            }
            if (item.getProfileWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_PROFILE_WEB_ADDRESSES, item.getProfileWebSite().toArray(new String[]{}));
            }
            if (item.getOtherWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_OTHER_WEB_ADDRESSES, item.getOtherWebSite().toArray(new String[]{}));
            }
            if (item.getLinkedinWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_LINKEDIN_WEB_ADDRESSES, item.getLinkedinWebSite().toArray(new String[]{}));
            }
            if (item.getFacebookWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_FACEBOOK_WEB_ADDRESSES, item.getFacebookWebSite().toArray(new String[]{}));
            }
            if (item.getTwitterWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_TWITTER_WEB_ADDRESSES, item.getTwitterWebSite().toArray(new String[]{}));
            }
            if (item.getInstagramWebSite().size() > 0) {
                importFile.addExtraColumn(false, ImportField.ContactField.FIELD_WEBS, ImportField.ContactField.FIELD_INSTAGRAM_WEB_ADDRESSES, item.getInstagramWebSite().toArray(new String[]{}));
            }
            //Address Information
            if (item.getAddresses() != null && item.getAddresses().size() > 0) {
                for (Address address : item.getAddresses()) {
                    if (address != null) {
                        String addressID = importFile.getAsString(address.getNameId());
                        String street1ID = importFile.getAsString(address.getAddressId());
                        String street2ID = importFile.getAsString(address.getAddressBId());
                        String cityID = importFile.getAsString(address.getCityId());
                        String countryID = importFile.getAsString(address.getCountryId());
                        String stateID = importFile.getAsString(address.getStateId());
                        String postCodeID = importFile.getAsString(address.getZipCodeId());
                        int addressRelation = 0;
                        if (address.getRelationType() != null) {
                            if (address.getRelationType().equals(1)) {
                                addressRelation = ImportField.ContactField.FIELD_HOME_ADDRESSES;
                            } else if (address.getRelationType().equals(2)) {
                                addressRelation = ImportField.ContactField.FIELD_WORK_ADDRESSES;
                            } else {
                                addressRelation = ImportField.ContactField.FIELD_OTHER_ADDRESSES;
                            }
                        } else {
                            addressRelation = ImportField.ContactField.FIELD_WORK_ADDRESSES;
                        }
                        importFile.addExtraColumn(true, ImportField.ContactField.FIELD_ADDRESSES, addressRelation, street1ID, street2ID, cityID, countryID, stateID, postCodeID, addressID);
                    }
                }
            }
            //Crm Details
            importFile.addColumn(ImportField.ContactField.FIELD_CATEGORY_FROMFILE, item.isCategoryFromFile() ? 1 : 0);
            importFile.addColumn(ImportField.ContactField.FIELD_CATEGORY, item.getCategoryID());
            importFile.addColumn(ImportField.ContactField.FIELD_PRIMARY_CONTACT, item.isPrimaryContact() ? 1 : 0);
            importFile.addColumn(ImportField.ContactField.FIELD_CAMPAIGN_FROMFILE, item.isCampaignFromFile() ? 1 : 0);
            importFile.addColumn(ImportField.ContactField.FIELD_CAMPAIGN, item.getCampaignId());
            importFile.addColumn(ImportField.ContactField.FIELD_EMAIL_OPT, item.isEmailOptOut() ? 1 : 0);
            //Lead Details
            importFile.addColumn(ImportField.ContactField.FIELD_ASSIGNEE_FROMFILE, item.isAssigneeFromFile() ? 1 : 0);
            importFile.addColumn(ImportField.ContactField.FIELD_LEAD_ASSIGNEE, item.getLeadAssigneeID());
            importFile.addColumn(ImportField.ContactField.FIELD_LEAD_BACKUP_ASSIGNEE, item.getLeadBackupAssigneeID());
            importFile.addColumn(ImportField.ContactField.FIELD_LEAD_SOURCE, item.getLeadSourceID());
            importFile.addColumn(ImportField.ContactField.FIELD_LEAD_STATUS, item.getLeadStatusID());
            importFile.addColumn(ImportField.ContactField.FIELD_LEAD_RATING, item.getLeadRatingID());
            //Candidate Details
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_PROJECT, item.getProjectItem() != null ? item.getProjectItem().getId() : null);
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_SOURCE, item.getCandidateSource() != null ? item.getCandidateSource().getId() : null);
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_STATUS, item.getCandidateStatus() != null ? item.getCandidateStatus().getId() : null);
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_CREATED_DATE, item.getCreatedDateID());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_VACANCIES, item.getVacancyID());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_WORK_EXPERIENCE, item.getWorkExperience());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR, item.getWorkExperienceMonthOrYear());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_CURRENT_EMPLOYER, item.getCurrentEmployerID());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_EXPECTED_SALARY, item.getExpectedSalaryID());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_LOCATION, item.getLocationID());
            importFile.addColumn(ImportField.ContactField.FIELD_CANDIDATE_SKILLS, item.getSkillsID());
            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                int s = ImportField.ContactField.FIELD_CUSTOM_FIELD_START_NUMBER;
                for (CompanyCustomFieldItem customField : item.getCustomFields()) {
                    if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                        Integer columnID = Integer.parseInt(customField.getFieldStringValue());
                        importFile.addExtraColumn(false,
                                s++,
                                columnID,
                                customField.getDataType(),
                                customField.getColumnCode(),
                                customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                                customField.getUiType(),
                                customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null);
                    } else {
                        importFile.addExtraColumn(false, s++, null);
                    }
                }
            }
        }
        return importFile;
    }

    private void setMultiResults(final ContactListItem item, Integer type) {
        ArrayList<HashMap<String, Widget>> list = null;
        if (EMAILS.equals(type)) {
            list = emails.getWidgets();
        } else if (PHONES.equals(type)) {
            list = phones.getWidgets();
        } else if (IM_ADDRESSES.equals(type)) {
            list = imAddresses.getWidgets();
        } else if (WEB_ADDRESSES.equals(type)) {
            list = webAddresses.getWidgets();
        } else if (ADDRESSES.equals(type)) {
            list = addresses.getWidgets();
        }
        if (list != null) {
            for (HashMap<String, Widget> row : list) {
                if (row != null) {
                    DataListBox param = (DataListBox) row.get(PARAM_LIST_BOX);
                    DataListBox relation = (DataListBox) row.get(RELATION_LIST_BOX);
                    if (EMAILS.equals(type)) {
                        setEmail(item, param, relation);
                    } else if (PHONES.equals(type)) {
                        setPhone(item, param, relation);
                    } else if (IM_ADDRESSES.equals(type)) {
                        setIMAdress(item, param, relation);
                    } else if (WEB_ADDRESSES.equals(type)) {
                        setWebAdress(item, param, relation);
                    } else if (ADDRESSES.equals(type)) {
                        DataListBox addressName = (DataListBox) row.get(ADDRESS_NAME_LIST_BOX);
                        DataListBox street = (DataListBox) row.get(ADDRESS_STREET_LIST_BOX);
                        DataListBox street2 = (DataListBox) row.get(ADDRESS_STREET_LIST_BOX2);
                        DataListBox city = (DataListBox) row.get(ADDRESS_CITY_LIST_BOX);
                        DataListBox country = (DataListBox) row.get(ADDRESS_COUNTRY_LIST_BOX);
                        DataListBox state = (DataListBox) row.get(ADDRESS_STATE_LIST_BOX);
                        DataListBox postCode = (DataListBox) row.get(ADDRESS_POSTCODE_LIST_BOX);
                        setAdress(item, relation, addressName, street, street2, city, country, state, postCode);
                    }
                }
            }
        }
    }

    private void setEmail(final ContactListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(AddressReference.HOME.getId())) {
                    item.getHomeEmail().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(AddressReference.WORK.getId())) {
                    item.getWorkEmail().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(AddressReference.OTHER.getId())) {
                    item.getOtherEmail().add(getSelectedItemAsString(box));
                }
            } else {
                item.getHomeEmail().add(getSelectedItemAsString(box));
            }
        }
    }

    private void setPhone(final ContactListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(PhoneReference.HOME.getId())) {
                    item.getHomePhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.WORK.getId())) {
                    item.getWorkPhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.MOBILE.getId())) {
                    item.getMobile().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.HOMEFAX.getId())) {
                    item.getHomeFax().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.WORKFAX.getId())) {
                    item.getWorkFax().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.PAGER.getId())) {
                    item.getPager().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.OTHER.getId())) {
                    item.getOtherPhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.EXTENSION.getId())) {
                    item.getExtension().add(getSelectedItemAsString(box));
                }
            } else {
                item.getHomePhone().add(getSelectedItemAsString(box));
            }
        }
    }

    private void setIMAdress(final ContactListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(IMAddressReference.GTALK.getId())) {
                    item.getgTalk().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.AIM.getId())) {
                    item.getAIM().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.YAHOO.getId())) {
                    item.getYahoo().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.SKYPE.getId())) {
                    item.getSkype().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.QQ.getId())) {
                    item.getQQ().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.MSN.getId())) {
                    item.getMSN().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.ICQ.getId())) {
                    item.getICQ().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(IMAddressReference.JABBER.getId())) {
                    item.getJabber().add(getSelectedItemAsString(box));
                }
            } else {
                item.getgTalk().add(getSelectedItemAsString(box));
            }
        }
    }

    private WidgetsMap getMultiWidgets(Integer type) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final DataListBox paramListBox = new DataListBox();
        paramListBox.getElement().setId("param_list_box");
        paramListBox.addStyleName(DEFAULT_WIDTH);
        DataListBox relationListBox = new DataListBox();
        relationListBox.getElement().setId("relation_list_box");
        relationListBox.setWidth("130px");
        relationListBox.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        if (type.equals(EMAILS)) {
            relationListBox.setItems(AddContactView.getAddress());
        } else if (type.equals(PHONES)) {
            relationListBox.setItems(AddContactView.getPhoneNumber());
        } else if (type.equals(IM_ADDRESSES)) {
            relationListBox.setItems(getIMAddress());
        } else if (type.equals(WEB_ADDRESSES)) {
            relationListBox.setItems(AddContactView.getWebAddress());
        }
        if (items != null) {
            if (type.equals(EMAILS)) {
                paramListBox.setItems(items, wfmStrings.email());
            } else if (type.equals(PHONES)) {
                paramListBox.setItems(items, wfmStrings.phone());
            } else if (type.equals(IM_ADDRESSES)) {
                paramListBox.setItems(items, wfmStrings.imAddress());
            } else if (type.equals(WEB_ADDRESSES)) {
                paramListBox.setItems(items, wfmStrings.webAddress());
            }
        }
        widgetsMap.addWidgetToMap(PARAM_LIST_BOX, paramListBox);
        widgetsMap.addWidgetToMap(RELATION_LIST_BOX, relationListBox);
        widgetsMap.addWidgets(paramListBox, relationListBox);
        return widgetsMap;
    }

    private void setWebAdress(final ContactListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(WebAddressReference.HOME.getId())) {
                    item.getHomeWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.WORK.getId())) {
                    item.getWorkWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.HOMEPAGE.getId())) {
                    item.getHomePage().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.FTP.getId())) {
                    item.getFtp().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.BLOG.getId())) {
                    item.getBlog().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.PROFILE.getId())) {
                    item.getProfileWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.OTHER.getId())) {
                    item.getOtherWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.LINKEDIN.getId())) {
                    item.getLinkedinWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.FACEBOOK.getId())) {
                    item.getFacebookWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.TWITTER.getId())) {
                    item.getTwitterWebSite().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(WebAddressReference.INSTAGRAM.getId())) {
                    item.getInstagramWebSite().add(getSelectedItemAsString(box));
                }
            } else {
                item.getHomeWebSite().add(getSelectedItemAsString(box));
            }
        }
    }

    private void setAdress(final ContactListItem item, DataListBox relation, DataListBox addressName, DataListBox street, DataListBox street2, DataListBox city, DataListBox country, DataListBox state, DataListBox postCode) {
        Address contactAddressItem = new Address();
        contactAddressItem.setNameId(getSelectedItem(addressName));
        contactAddressItem.setRelationType(getSelectedItem(relation));
        contactAddressItem.setAddressId(getSelectedItem(street));
        contactAddressItem.setAddressBId(getSelectedItem(street2));
        contactAddressItem.setCityId(getSelectedItem(city));
        contactAddressItem.setCountryId(getSelectedItem(country));
        contactAddressItem.setStateId(getSelectedItem(state));
        contactAddressItem.setZipCodeId(getSelectedItem(postCode));
        if (contactAddressItem.isNotEmpty()) {
            item.getAddresses().add(contactAddressItem);
        }
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.CONTACT;
    }

    public void reinit() {
        clear();
        loadPage();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_CRM_CONTACT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }
}