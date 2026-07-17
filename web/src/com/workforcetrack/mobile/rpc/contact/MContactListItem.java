package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.MTreeSelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/10/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MContactListItem implements Serializable {

    public static final int WORK_EMAIL = 1;
    public static final int HOME_EMAIL = 2;
    public static final int OTHER_EMAIL = 3;

    public static final int WORK_PHONE = 1;
    public static final int HOME_PHONE = 2;
    public static final int OTHER_PHONE = 3;
    public static final int HOME_FAX = 4;
    public static final int WORK_FAX = 5;
    public static final int PAGER = 6;
    public static final int MOBILE = 7;

/*    public static final int GTALK = 1;
    public static final int AIM =2;
    public static final int YAHOO = 3;
    public static final int SKYPE =4;
    public static final int QQ = 5;
    public static final int MSN =6;
    public static final int ICQ = 7;
    public static final int JABBER = 8;*/

    public static final int WORK_ADDRESS = 1;
    public static final int HOME_ADDRESS = 2;
    public static final int OTHER_ADDRESS = 3;

    //@XmlElement(nillable = true)
    private Integer objectID;
    private String firstName;
    private String lastName;
    private String contactName;
    private String middleName;
    private String title;
    private Integer titleID;

    private String owner;
    private Integer ownerID;

    private Date birthDate;

    //@XmlElement(nillable = true)
    //CRM ACCOUNT
    private Integer companyID;
    private String companyName;
    private List<MSelectItem> accountTypes;
    private List<MSelectItem> organizationTypes;
    private Integer organizationTypeID;
    private String organizationType;
    private String jobTitle;
    private String department;
    private String categoryNames;
    private Integer industryID;
    private String industry;
    private List<MSelectItem> industries;

    private String reportsTo;
    private String otherName;
    private String position;
    //LEAD Information
    private List<MSelectItem> leadAssignees;
    private Integer leadAssigneeID;
    private String leadAssignee;

    private List<MSelectItem> leadSources;
    private Integer leadSourceID;
    private String leadSource;

    private List<MSelectItem> leadStatuses;
    private Integer leadStatusID;
    private String leadStatus;

    private List<MSelectItem> leadRatings;
    private Integer leadRatingID;
    private String leadRating;

    private List<MSelectItem> countries;
    private List<MSelectItem> states;

    private List<MTreeSelectItem> contactCategories;
    private Integer country;
    private Integer state;
    private Integer assignee;

    private String note;


    // workAddress = 1, homeAddress = 2, otherAddress = 3
    private ArrayList<MContactAddressItems> addresses = new ArrayList<>();
    private Boolean leadContact;

    private String primaryEmail;  //need for getting contact's single email address
    private MContactAddressItems primaryAddress;  //need for getting contact's single address
    private String primaryPhone;  //need for getting contact's single phone

    // emails
    private ArrayList<String> homeEmail = new ArrayList<>();     // email
    private Integer homeEmailId;
    private ArrayList<String> workEmail = new ArrayList<>();
    private ArrayList<String> otherEmail = new ArrayList<>();
    // phones
    private ArrayList<String> workPhone = new ArrayList<>();     // phone
    private Integer workPhoneId;
    private ArrayList<String> homePhone = new ArrayList<>();
    private Integer homePhoneId;
    private ArrayList<String> otherPhone = new ArrayList<>();
    private Integer otherPhoneId;
    private ArrayList<String> homeFax = new ArrayList<>();      // fax
    private Integer homeFaxId;
    private ArrayList<String> workFax = new ArrayList<>();
    private ArrayList<String> pager = new ArrayList<>();
    private ArrayList<String> mobile = new ArrayList<>();
    private Integer mobileId;
    // IM addresses
    private ArrayList<String> gTalk = new ArrayList<>();
    private ArrayList<String> AIM = new ArrayList<>();
    private ArrayList<String> yahoo = new ArrayList<>();
    private ArrayList<String> skype = new ArrayList<>();
    private ArrayList<String> QQ = new ArrayList<>();
    private ArrayList<String> MSN = new ArrayList<>();
    private ArrayList<String> ICQ = new ArrayList<>();
    private ArrayList<String> jabber = new ArrayList<>();
    // web addresses
    private ArrayList<String> homeWebSite = new ArrayList<>();  // website
    private ArrayList<String> workWebSite = new ArrayList<>();
    private ArrayList<String> homePage = new ArrayList<>();
    private ArrayList<String> ftp = new ArrayList<>();
    private ArrayList<String> blog = new ArrayList<>();
    private ArrayList<String> profileWebSite = new ArrayList<>();
    private ArrayList<String> otherWebSite = new ArrayList<>();

    private Integer contactType;

    //private TreeSelectItem[] categories;
    private List<MSelectItem> selectedCategories;

    private MSelectItem webSite;
    private MSelectItem email;

    private Date updatedDate;
    private Date createdDate;
    private Boolean deleted;

    private String deviceID;
    private String deviceContactID;
    private String status;

    private List<MSelectItem> titles;

    public MContactListItem() {
    }

    public MContactListItem(ContactListItem contactListItem) {
        if (contactListItem != null) {
            this.objectID = contactListItem.getObjectId();
            this.lastName = contactListItem.getLastName();
            this.firstName = contactListItem.getFirstName();
            this.middleName = contactListItem.getMiddleName();
            this.position = contactListItem.getJobTitle();
            this.otherName = contactListItem.getOtherName();
            this.reportsTo = contactListItem.getReportsTo();
            this.owner = contactListItem.getOwner();
            this.ownerID = contactListItem.getOwnerId();
            this.birthDate = (contactListItem.getBirthDate() != null && contactListItem.getBirthDate().getDateLong() > 0) ? contactListItem.getBirthDate().getNonConvertedDate() : null;

            if (contactListItem.getCrmAccount() != null) {
                this.companyName = contactListItem.getCrmAccount().getName();
                this.companyID = contactListItem.getCrmAccount().getObjectId();
                //this.accountTypes = WebServiceUtils.getAsMSelectItemList(contactListItem.getCrmAccount().getAccountTypes());
                if (contactListItem.getCrmAccount().getAccountTypes() != null) {
                    this.accountTypes = new ArrayList<>();
                    for (SelectItem selectItem : contactListItem.getCrmAccount().getAccountTypes()) {
                        MSelectItem mSelectItem = new MSelectItem(selectItem);
                        mSelectItem.setDescription(selectItem.isSelected() ? "selected" : "unselected");
                        this.accountTypes.add(mSelectItem);
                    }
                }
            }

            this.contactName = contactListItem.getContactName();
            this.department = contactListItem.getDepartment();
            this.jobTitle = contactListItem.getJobTitle();
            this.primaryAddress = new MContactAddressItems(contactListItem.getPrimaryAddress());
            this.primaryEmail = contactListItem.getPrimaryEmail();
            this.primaryPhone = contactListItem.getPrimaryPhone();
            this.note = contactListItem.getNote();
            this.title = contactListItem.getTitle();

            this.homeEmail = contactListItem.getHomeEmail();
            this.workEmail = contactListItem.getWorkEmail();
            this.otherEmail = contactListItem.getOtherEmail();

            this.homePhone = contactListItem.getHomePhone();
            this.workPhone = contactListItem.getWorkPhone();
            this.otherPhone = contactListItem.getOtherPhone();
            this.workFax = contactListItem.getWorkFax();
            this.homeFax = contactListItem.getHomeFax();
            this.pager = contactListItem.getPager();
            this.mobile = contactListItem.getMobile();

            this.homeWebSite = contactListItem.getHomeWebSite();
            this.workWebSite = contactListItem.getWorkWebSite();
            this.profileWebSite = contactListItem.getProfileWebSite();
            this.blog = contactListItem.getBlog();
            this.otherWebSite = contactListItem.getOtherWebSite();
            this.ftp = contactListItem.getFtp();
            this.otherWebSite = contactListItem.getOtherWebSite();
            this.homePage = contactListItem.getHomePage();

            this.gTalk = contactListItem.getgTalk();
            this.AIM = contactListItem.getAIM();
            this.QQ = contactListItem.getQQ();
            this.yahoo = contactListItem.getYahoo();
            this.skype = contactListItem.getSkype();
            this.ICQ = contactListItem.getICQ();
            this.jabber = contactListItem.getJabber();
            this.MSN = contactListItem.getMSN();
            if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
                this.addresses = new ArrayList<>();
                for (Address contactAddressItems : contactListItem.getAddresses()) {
                    this.addresses.add(new MContactAddressItems(contactAddressItems));
                }
            }

            this.selectedCategories = WebServiceUtils.getAsMSelectItemList(contactListItem.getSelectedCategories());
            this.categoryNames = contactListItem.getCategoryNames();
            this.contactType = contactListItem.getContactType();

            //LEAD INFORMATION
            this.leadAssignee = contactListItem.getLeadAssignee();
            this.leadAssigneeID = contactListItem.getLeadAssigneeID();
            this.leadSource = contactListItem.getLeadSource();
            this.leadSourceID = contactListItem.getLeadSourceID();
            this.leadStatus = contactListItem.getLeadStatus(true).getName();
            this.leadStatusID = contactListItem.getLeadStatus(true).getId();

            this.leadAssignees = WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadAssignees());
            this.leadSources = WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadSources());
            this.leadStatuses = WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadStatuses());
            this.countries = WebServiceUtils.getAsMSelectItemList(contactListItem.getCountries());
            this.states = WebServiceUtils.getAsMSelectItemList(contactListItem.getStates());
            this.leadRatings = WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadRatings());
            this.industries = WebServiceUtils.getAsMSelectItemList(contactListItem.getCrmAccount().getIndustries());
        }
    }

    public MContactListItem(ContactListItem contactListItem, boolean isBriefly) {
        if (contactListItem != null) {
            this.objectID = contactListItem.getObjectId();
            this.lastName = contactListItem.getLastName();
            this.firstName = contactListItem.getFirstName();
            this.middleName = contactListItem.getMiddleName();
            this.otherName = contactListItem.getOtherName();
            this.position = contactListItem.getJobTitle();
            this.reportsTo = contactListItem.getReportsTo();
            this.gTalk = contactListItem.getgTalk();
            this.AIM = contactListItem.getAIM();
            this.QQ = contactListItem.getQQ();
            this.yahoo = contactListItem.getYahoo();
            this.skype = contactListItem.getSkype();
            this.ICQ = contactListItem.getICQ();
            this.jabber = contactListItem.getJabber();
            this.MSN = contactListItem.getMSN();

            this.owner = contactListItem.getOwner();
            this.ownerID = contactListItem.getOwnerId();
            this.birthDate = (contactListItem.getBirthDate() != null && contactListItem.getBirthDate().getDateLong() > 0) ? contactListItem.getBirthDate().getNonConvertedDate() : null;
            if (contactListItem.getCrmAccount() != null) {
                this.companyName = contactListItem.getCrmAccount().getName();
                this.companyID = contactListItem.getCrmAccount().getObjectId();
            }

            this.companyName = (contactListItem.getCrmAccount() != null) ? contactListItem.getCrmAccount().getName() : null;
            this.companyID = (contactListItem.getCrmAccount() != null) ? contactListItem.getCrmAccount().getObjectId() : null;
            this.contactName = contactListItem.getContactName();
            this.department = contactListItem.getDepartment();
            this.jobTitle = contactListItem.getJobTitle();
            this.primaryAddress = new MContactAddressItems(contactListItem.getPrimaryAddress());
            this.primaryEmail = contactListItem.getPrimaryEmail();
            this.primaryPhone = contactListItem.getPrimaryPhone();
            this.note = contactListItem.getNote();
            this.title = contactListItem.getTitle();

            this.categoryNames = contactListItem.getCategoryNames();
            this.contactType = contactListItem.getContactType();

            //LEAD INFORMATION
            this.leadAssignee = contactListItem.getLeadAssignee();
            this.leadAssigneeID = contactListItem.getLeadAssigneeID();
            this.leadSource = contactListItem.getLeadSource();
            this.leadSourceID = contactListItem.getLeadSourceID();
            this.leadStatus = contactListItem.getLeadStatus(true).getName();
            this.leadStatusID = contactListItem.getLeadStatus(true).getId();
            this.leadRating = contactListItem.getLeadRating();
            this.leadRatingID = contactListItem.getLeadRatingID();
        }
    }

    public ContactListItem convertFromOutlook(ContactListItem contactListItem) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }

        contactListItem.setObjectId(this.getObjectID() == null || this.getObjectID() == 0 ? null : this.getObjectID());
        contactListItem.setLastName(this.getLastName());
        contactListItem.setFirstName(this.getFirstName());
        contactListItem.setMiddleName(this.getMiddleName());
        contactListItem.setOwner(this.getOwner());
        contactListItem.setOwnerId(this.getOwnerID() == null || this.getOwnerID() == 0 ? null : this.getOwnerID());
        if (this.getBirthDate() != null) {
            contactListItem.setBirthDate(new DateNonConvertable(this.getBirthDate()));
        }

        if (this.companyID != null || this.companyName != null) {
            if (contactListItem.getCrmAccount() == null || this.companyID == null || this.companyID.equals(0)) {
                contactListItem.setCrmAccount(new CrmAccountItem());
            }
            contactListItem.getCrmAccount().setObjectId(this.companyID == null || this.companyID.equals(0) ? null : this.companyID);
            contactListItem.getCrmAccount().setName(this.companyName);
        }

        contactListItem.setDepartment(this.getDepartment());
        contactListItem.setJobTitle(this.getJobTitle());
        Address contactAddressItems = new Address();
        MContactAddressItems.convert(contactAddressItems, this.getPrimaryAddress(), false);
        contactListItem.setPrimaryAddress(contactAddressItems);
        contactListItem.setPrimaryEmail(this.getPrimaryEmail());
        contactListItem.setPrimaryPhone(this.getPrimaryPhone());
        contactListItem.setTitle(this.getTitle());

        contactListItem.setHomeEmail(this.getHomeEmail());
        contactListItem.setWorkEmail(this.getWorkEmail());
        contactListItem.setOtherEmail(this.getOtherEmail());

        contactListItem.setHomePhone(this.getHomePhone());
        contactListItem.setWorkPhone(this.getWorkPhone());
        contactListItem.setOtherPhone(this.getOtherPhone());
        contactListItem.setWorkFax(this.getWorkFax());
        contactListItem.setHomeFax(this.getHomeFax());
        contactListItem.setPager(this.getPager());
        contactListItem.setMobile(this.getMobile());

        contactListItem.setgTalk(this.getgTalk());
        contactListItem.setWorkWebSite(this.getWorkWebSite());

        if (this.getAddresses() != null && this.getAddresses().size() > 0) {
            ArrayList<Address> contactAddressItemses = new ArrayList<>();
            for (MContactAddressItems mContactAddressItems : this.getAddresses()) {
                Address contactAddressItem = new Address();
                MContactAddressItems.convert(contactAddressItem, mContactAddressItems, false);
                contactAddressItemses.add(contactAddressItem);
            }

            contactListItem.setAddresses(contactAddressItemses);
        } else {
            contactListItem.setAddresses(null);
        }

        if (this.getSelectedCategories() != null) {
            ArrayList<SelectItem> selectedCategories = new ArrayList<>();
            for (MSelectItem mSelectItem : this.getSelectedCategories()) {
                selectedCategories.add(new SelectItem(mSelectItem.getObjectID(), mSelectItem.getName(), mSelectItem.getDescription()));
            }
            contactListItem.setSelectedCategories(selectedCategories);
        }

        return contactListItem;
    }

    public static MContactListItem convertToOutlook(ContactListItem contactListItem) {
        if (contactListItem != null) {
            MContactListItem mContactListItem = new MContactListItem();
            mContactListItem.setObjectID(contactListItem.getObjectId());
            mContactListItem.setLastName(contactListItem.getLastName());
            mContactListItem.setFirstName(contactListItem.getFirstName());
            mContactListItem.setMiddleName(contactListItem.getMiddleName());
            mContactListItem.setOwner(contactListItem.getOwner());
            mContactListItem.setOwnerID(contactListItem.getOwnerId());
            if (contactListItem.getBirthDate() != null && contactListItem.getBirthDate().getDateLong() > 0) {
                mContactListItem.setBirthDate(contactListItem.getBirthDate().getNonConvertedDate());
            }
            if (contactListItem.getCrmAccount() != null) {
                mContactListItem.setCompanyName(contactListItem.getCrmAccount().getName());
                mContactListItem.setCompanyID(contactListItem.getCrmAccount().getObjectId());
            }

            mContactListItem.setUpdatedDate(contactListItem.getUpdatedDate());

            mContactListItem.setDepartment(contactListItem.getDepartment());
            mContactListItem.setJobTitle(contactListItem.getJobTitle());

            mContactListItem.setPrimaryEmail(contactListItem.getPrimaryEmail());
            mContactListItem.setPrimaryPhone(contactListItem.getPrimaryPhone());
            mContactListItem.setTitle(contactListItem.getTitle());

            mContactListItem.setHomeEmail(contactListItem.getHomeEmail());
            mContactListItem.setWorkEmail(contactListItem.getWorkEmail());
            mContactListItem.setOtherEmail(contactListItem.getOtherEmail());

            mContactListItem.setHomePhone(contactListItem.getHomePhone());
            mContactListItem.setWorkPhone(contactListItem.getWorkPhone());
            mContactListItem.setOtherPhone(contactListItem.getOtherPhone());
            mContactListItem.setWorkFax(contactListItem.getWorkFax());
            mContactListItem.setHomeFax(contactListItem.getHomeFax());
            mContactListItem.setPager(contactListItem.getPager());
            mContactListItem.setMobile(contactListItem.getMobile());

            mContactListItem.setgTalk(contactListItem.getgTalk());
            mContactListItem.setWorkWebSite(contactListItem.getWorkWebSite());

            if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
                ArrayList<MContactAddressItems> addresses = new ArrayList<>();
                for (Address contactAddressItems : contactListItem.getAddresses()) {
                    addresses.add(new MContactAddressItems(contactAddressItems));
                }
                mContactListItem.setAddresses(addresses);
            }

            mContactListItem.setSelectedCategories(WebServiceUtils.getAsMSelectItemList(contactListItem.getSelectedCategories()));
            mContactListItem.setContactType(contactListItem.getContactType());
            return mContactListItem;
        }
        return null;
    }

    public static MContactListItem convertLeadToExcel(ContactListItem contactListItem) {
        if (contactListItem != null) {
            MContactListItem mContactListItem = new MContactListItem();
            mContactListItem.setObjectID(contactListItem.getObjectId());
            mContactListItem.setLastName(contactListItem.getLastName());
            mContactListItem.setFirstName(contactListItem.getFirstName());

            mContactListItem.setCompanyID(contactListItem.getCrmAccount().getObjectId());
            mContactListItem.setCompanyName(contactListItem.getCrmAccount().getName());
            mContactListItem.setIndustryID(contactListItem.getCrmAccount().getIndustryID());
            mContactListItem.setIndustry(contactListItem.getCrmAccount().getIndustry());
            mContactListItem.setIndustries(WebServiceUtils.getAsMSelectItemList(contactListItem.getCrmAccount().getIndustries()));

            mContactListItem.setJobTitle(contactListItem.getJobTitle());
            mContactListItem.setPrimaryEmail(contactListItem.getPrimaryEmail());
            mContactListItem.setHomeEmail(contactListItem.getHomeEmail());
            mContactListItem.setWorkWebSite(contactListItem.getWorkWebSite());
            mContactListItem.setSelectedCategories(WebServiceUtils.getAsMSelectItemList(contactListItem.getSelectedCategories()));

            mContactListItem.setLeadAssigneeID(contactListItem.getLeadAssigneeID());
            mContactListItem.setLeadAssignee(contactListItem.getLeadAssignee());
            mContactListItem.setLeadSourceID(contactListItem.getLeadSourceID());
            mContactListItem.setLeadSource(contactListItem.getLeadSource());
            mContactListItem.setLeadStatusID(contactListItem.getLeadStatus(true).getId());
            mContactListItem.setLeadStatus(contactListItem.getLeadStatus(true).getName());
            mContactListItem.setLeadRating(contactListItem.getLeadRating());
            mContactListItem.setLeadRatingID(contactListItem.getLeadRatingID());

            mContactListItem.setLeadAssignees(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadAssignees()));
            mContactListItem.setLeadSources(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadSources()));
            mContactListItem.setLeadStatuses(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadStatuses()));
            mContactListItem.setLeadRatings(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadRatings()));

            return mContactListItem;
        }
        return null;
    }


    public static MContactListItem convertContactToExcel(ContactListItem contactListItem) {
        if (contactListItem != null) {

            MContactListItem mContactListItem = new MContactListItem();
            mContactListItem.setObjectID(contactListItem.getObjectId());

            mContactListItem.setLastName(contactListItem.getLastName());
            mContactListItem.setFirstName(contactListItem.getFirstName());
            mContactListItem.setReportsTo(contactListItem.getReportsTo());
            mContactListItem.setTitle(contactListItem.getTitle());
            mContactListItem.setOwner(contactListItem.getOwner());
            mContactListItem.setOwnerID(contactListItem.getOwnerId());
            if (contactListItem.getBirthDate() != null && contactListItem.getBirthDate().getDateLong() > 0) {
                mContactListItem.setBirthDate(contactListItem.getBirthDate().getNonConvertedDate());
            }
            mContactListItem.setCompanyID(contactListItem.getCrmAccount().getObjectId());
            mContactListItem.setCompanyName(contactListItem.getCrmAccount().getName());

            mContactListItem.setDepartment(contactListItem.getDepartment());

            mContactListItem.setTitle(contactListItem.getTitle());

            if (contactListItem.getWorkEmail().size() > 0) {
                mContactListItem.setEmail(new MSelectItem(EdsCrmContactItemParams.WORK, contactListItem.getWorkEmail().get(0)));
            } else if (contactListItem.getHomeEmail().size() > 0) {
                mContactListItem.setEmail(new MSelectItem(EdsCrmContactItemParams.HOME, contactListItem.getHomeEmail().get(0)));
            } else if (contactListItem.getOtherEmail().size() > 0) {
                mContactListItem.setEmail(new MSelectItem(EdsCrmContactItemParams.OTHER, contactListItem.getOtherEmail().get(0)));
            }
            if (contactListItem.getWorkPhone().size() > 0) {
                mContactListItem.setWorkPhone(new ArrayList<>(Arrays.asList(contactListItem.getWorkPhone().get(0))));
            }
            if (contactListItem.getMobile().size() > 0) {
                mContactListItem.setMobile(new ArrayList<>(Arrays.asList(contactListItem.getMobile().get(0))));
            }

            if (contactListItem.getHomeWebSite().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.HOME, contactListItem.getHomeWebSite().get(0)));
            } else if (contactListItem.getWorkWebSite().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.WORK, contactListItem.getWorkWebSite().get(0)));
            } else if (contactListItem.getBlog().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.BLOG, contactListItem.getBlog().get(0)));
            } else if (contactListItem.getHomePage().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.HOME_PAGE, contactListItem.getHomePage().get(0)));
            } else if (contactListItem.getFtp().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.FTP, contactListItem.getFtp().get(0)));
            } else if (contactListItem.getProfileWebSite().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.PROFILE, contactListItem.getProfileWebSite().get(0)));
            } else if (contactListItem.getOtherWebSite().size() > 0) {
                mContactListItem.setWebSite(new MSelectItem(EdsCrmContactItemParams.OTHER, contactListItem.getOtherWebSite().get(0)));
            }

            if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
                ArrayList<MContactAddressItems> addresses = new ArrayList<>();
                for (Address addressItem : contactListItem.getAddresses()) {
                    if (addresses.size() == 1 && !addresses.get(0).getParentID().equals(addressItem.getRelationType())) {
                        addresses.add(new MContactAddressItems(addressItem));
                        break;
                    } else if (addresses.size() == 0) {
                        addresses.add(new MContactAddressItems(addressItem));
                    }
                }
                mContactListItem.setAddresses(addresses);
            }

            mContactListItem.setSelectedCategories(WebServiceUtils.getAsMSelectItemList(contactListItem.getSelectedCategories()));
            return mContactListItem;
        }
        return null;
    }

    public ContactListItem convertLeadFromExcel(ContactListItem contactListItem) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }

        contactListItem.setObjectId(WebServiceUtils.getNotZeroValue(getObjectID()));
        contactListItem.setLastName(this.getLastName());
        contactListItem.setFirstName(this.getFirstName());

        contactListItem.setJobTitle(this.getJobTitle());
        if (getPrimaryEmail() == null || "".equals(getPrimaryEmail().trim())) {
            if (getHomeEmail() != null && getHomeEmail().size() > 0) {
                contactListItem.setPrimaryEmail(getHomeEmail().get(0));
            }
        } else {
            contactListItem.setPrimaryEmail(getPrimaryEmail());
        }

        contactListItem.setPrimaryEmail(this.getPrimaryEmail());
        contactListItem.setHomeEmail(this.getHomeEmail());
        contactListItem.setWorkWebSite(this.getWorkWebSite());

        contactListItem.setLeadAssigneeID(WebServiceUtils.getNotZeroValue(getLeadAssigneeID()));
        contactListItem.setLeadSourceID(WebServiceUtils.getNotZeroValue(getLeadSourceID()));
        contactListItem.setLeadStatus(contactListItem.getLeadStatus(true));
        contactListItem.getLeadStatus(true).setId(getLeadStatusID());
        contactListItem.setLeadRatingID(WebServiceUtils.getNotZeroValue(getLeadRatingID()));

        Integer companyID = WebServiceUtils.getNotZeroValue(getCompanyID());
        if (companyID == null) {
            contactListItem.setCrmAccount(new CrmAccountItem());
        }
        contactListItem.getCrmAccount().setObjectId(companyID);
        contactListItem.getCrmAccount().setName(getCompanyName());
        contactListItem.getCrmAccount().setIndustryID(WebServiceUtils.getNotZeroValue(getIndustryID()));
        contactListItem.getCrmAccount().setIndustry(getIndustry());

        return contactListItem;

    }

    public ContactListItem convertContactFromExcel(ContactListItem contactListItem) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }

        contactListItem.setObjectId(WebServiceUtils.getNotZeroValue(getObjectID()));
        contactListItem.setLastName(this.getLastName());
        contactListItem.setFirstName(this.getFirstName());
        contactListItem.setReportsTo(getReportsTo());
        contactListItem.setTitle(getTitle());
        contactListItem.setOwner(this.getOwner());
        contactListItem.setOwnerId(WebServiceUtils.getNotZeroValue(getOwnerID()));
        if (this.getBirthDate() != null) {
            contactListItem.setBirthDate(new DateNonConvertable(this.getBirthDate()));
        }

        Integer companyID = WebServiceUtils.getNotZeroValue(getCompanyID());
        if (companyID == null) {
            contactListItem.setCrmAccount(new CrmAccountItem());
        }
        contactListItem.getCrmAccount().setObjectId(companyID);
        contactListItem.getCrmAccount().setName(getCompanyName());
        contactListItem.setDepartment(this.getDepartment());
        contactListItem.setTitle(this.getTitle());

        // SET FIRST EMAIL, PHONE, FAX
        if (contactListItem.getWorkPhone() != null && contactListItem.getWorkPhone().size() == 0) {
            if (getWorkPhone() != null && !"".equals(getWorkPhone().get(0))) {
                contactListItem.getWorkPhone().add(getWorkPhone().get(0));
            }
        } else {
            if (getWorkPhone() != null && !"".equals(getWorkPhone().get(0))) {
                contactListItem.getWorkPhone().set(0, getWorkPhone().get(0));
            } else {
                contactListItem.getWorkPhone().remove(0);
            }
        }

        if (contactListItem.getMobile() != null && contactListItem.getMobile().size() == 0) {
            if (getWorkPhone() != null && !"".equals(getMobile().get(0))) {
                contactListItem.getMobile().add(getMobile().get(0));
            }
        } else {
            if (getMobile() != null && !"".equals(getMobile().get(0))) {
                contactListItem.getMobile().set(0, getMobile().get(0));
            } else {
                contactListItem.getMobile().remove(0);
            }
        }

        if (getEmail() != null && getEmail().getObjectID() != null && getEmail().getName() != null && !"".equals(getEmail().getName())) {
            switch (getEmail().getObjectID()) {
                case EdsCrmContactItemParams.HOME:
                    if (contactListItem.getHomeEmail().size() == 0) {
                        contactListItem.getHomeEmail().add(getEmail().getName());
                    } else {
                        contactListItem.getHomeEmail().set(0, getEmail().getName());
                    }
                    break;
                case EdsCrmContactItemParams.WORK:
                    if (contactListItem.getWorkEmail().size() == 0) {
                        contactListItem.getWorkEmail().add(getEmail().getName());
                    } else {
                        contactListItem.getWorkEmail().set(0, getEmail().getName());
                    }
                    break;

                case EdsCrmContactItemParams.OTHER:
                    if (contactListItem.getOtherEmail().size() == 0) {
                        contactListItem.getOtherEmail().add(getEmail().getName());
                    } else {
                        contactListItem.getOtherEmail().set(0, getEmail().getName());
                    }
                    break;
            }
        }

        if (getWebSite() != null && getWebSite().getObjectID() != null && getWebSite().getName() != null && !"".equals(getWebSite().getName())) {
            switch (getWebSite().getObjectID()) {
                case EdsCrmContactItemParams.HOME:
                    if (contactListItem.getHomeWebSite().size() == 0) {
                        contactListItem.getHomeWebSite().add(getWebSite().getName());
                    } else {
                        contactListItem.getHomeWebSite().set(0, getWebSite().getName());
                    }
                    break;
                case EdsCrmContactItemParams.WORK:
                    if (contactListItem.getWorkWebSite().size() == 0) {
                        contactListItem.getWorkWebSite().add(getWebSite().getName());
                    } else {
                        contactListItem.getWorkWebSite().set(0, getWebSite().getName());
                    }
                    break;

                case EdsCrmContactItemParams.HOME_PAGE:
                    if (contactListItem.getHomePage().size() == 0) {
                        contactListItem.getHomePage().add(getWebSite().getName());
                    } else {
                        contactListItem.getHomePage().set(0, getWebSite().getName());
                    }
                    break;
                case EdsCrmContactItemParams.FTP:
                    if (contactListItem.getFtp().size() == 0) {
                        contactListItem.getFtp().add(getWebSite().getName());
                    } else {
                        contactListItem.getFtp().set(0, getWebSite().getName());
                    }
                    break;
                case EdsCrmContactItemParams.BLOG:
                    if (contactListItem.getBlog().size() == 0) {
                        contactListItem.getBlog().add(getWebSite().getName());
                    } else {
                        contactListItem.getBlog().set(0, getWebSite().getName());
                    }
                    break;
                case EdsCrmContactItemParams.PROFILE:
                    if (contactListItem.getProfileWebSite().size() == 0) {
                        contactListItem.getProfileWebSite().add(getWebSite().getName());
                    } else {
                        contactListItem.getProfileWebSite().set(0, getWebSite().getName());
                    }
                    break;
                case EdsCrmContactItemParams.OTHER:
                    if (contactListItem.getOtherWebSite().size() == 0) {
                        contactListItem.getOtherWebSite().add(getWebSite().getName());
                    } else {
                        contactListItem.getOtherWebSite().set(0, getWebSite().getName());
                    }
                    break;

            }
        }

        if (getAddresses() != null && getAddresses().size() > 0) {
            Integer homeAddressIndex = null, workAddressIndex = null;
            if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
                int i = 0;
                for (Address address : contactListItem.getAddresses()) {
                    if (homeAddressIndex != null && workAddressIndex != null) {
                        break;
                    }
                    if (address.getRelationType() != null && address.getRelationType().equals(EdsCrmContactItemParams.HOME)) {
                        homeAddressIndex = i;
                    } else if (address.getRelationType() != null && address.getRelationType().equals(EdsCrmContactItemParams.WORK)) {
                        workAddressIndex = i;
                    }
                    i++;
                }
                for (MContactAddressItems addressItem : getAddresses()) {
                    if (addressItem.getParentID() != null && addressItem.getParentID().equals(EdsCrmContactItemParams.HOME)) {
                        if (homeAddressIndex != null) {
                            contactListItem.getAddresses().set(homeAddressIndex, addressItem.convertToAddress(null));
                        } else {
                            contactListItem.getAddresses().add(addressItem.convertToAddress(null));
                        }
                    } else if (addressItem.getParentID() != null && addressItem.getParentID().equals(EdsCrmContactItemParams.WORK)) {
                        if (workAddressIndex != null) {
                            contactListItem.getAddresses().set(workAddressIndex, addressItem.convertToAddress(null));
                        } else {
                            contactListItem.getAddresses().add(addressItem.convertToAddress(null));
                        }
                    }
                }
            } else {
                for (MContactAddressItems addressItems : getAddresses()) {
                    contactListItem.getAddresses().add(addressItems.convertToAddress(null));
                }
            }
        } else {
            contactListItem.setAddresses(null);
        }

        if (this.getSelectedCategories() != null) {
            ArrayList<SelectItem> selectedCategories = new ArrayList<>();
            for (MSelectItem mSelectItem : this.getSelectedCategories()) {
                selectedCategories.add(new SelectItem(mSelectItem.getObjectID(), mSelectItem.getName(), mSelectItem.getDescription()));
            }
            contactListItem.setSelectedCategories(selectedCategories);
        }

        return contactListItem;

    }

    public static MContactListItem convertToMobile(ContactListItem contactListItem, boolean isLead, boolean isBriefly) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }
        MContactListItem mContactItem = new MContactListItem();
        mContactItem.setObjectID(contactListItem.getObjectId());
        mContactItem.setOwnerID(contactListItem.getOwnerId());
        mContactItem.setOwner(contactListItem.getOwner());
        mContactItem.setLastName(contactListItem.getLastName());
        mContactItem.setFirstName(contactListItem.getFirstName());
        mContactItem.setContactName(contactListItem.getContactName());
        mContactItem.setJobTitle(contactListItem.getJobTitle());
        mContactItem.setTitles(WebServiceUtils.getAsMSelectItemList(contactListItem.getCrmAccount().getTitle()));
        mContactItem.setTitleID(contactListItem.getTitleId());
        Integer index = WebServiceUtils.getIndexOf(mContactItem.getTitles(), mContactItem.getTitleID());
        if (mContactItem.getTitles() != null && mContactItem.getTitles().size() > 0 && index > 0) {
            mContactItem.setTitle(mContactItem.getTitles().get(index).getName());
        }

        if (contactListItem.getCrmAccount() != null) {
            mContactItem.setCompanyID(contactListItem.getCrmAccount().getObjectId());
            mContactItem.setCompanyName(contactListItem.getCrmAccount().getName());

            if (!isBriefly) {
                if (contactListItem.getCrmAccount().getAccountTypes() != null) {
                    mContactItem.setAccountTypes(new ArrayList<>());
                    for (SelectItem selectItem : contactListItem.getCrmAccount().getAccountTypes()) {
                        MSelectItem mSelectItem = new MSelectItem(selectItem);
                        mSelectItem.setDescription(selectItem.isSelected() ? "selected" : "unselected");
                        mContactItem.getAccountTypes().add(mSelectItem);
                    }
                }
            }
        }
        mContactItem.setHomeEmail(contactListItem.getHomeEmail());
        mContactItem.setWorkEmail(contactListItem.getWorkEmail());
        mContactItem.setOtherEmail(contactListItem.getOtherEmail());

        mContactItem.setHomePhone(contactListItem.getHomePhone());
        mContactItem.setWorkPhone(contactListItem.getWorkPhone());
        mContactItem.setOtherPhone(contactListItem.getOtherPhone());
        mContactItem.setWorkFax(contactListItem.getWorkFax());
        mContactItem.setHomeFax(contactListItem.getHomeFax());
        mContactItem.setPager(contactListItem.getPager());
        mContactItem.setMobile(contactListItem.getMobile());

        mContactItem.setHomeWebSite(contactListItem.getHomeWebSite());
        mContactItem.setWorkWebSite(contactListItem.getWorkWebSite());
        mContactItem.setHomePage(contactListItem.getHomePage());
        mContactItem.setFtp(contactListItem.getFtp());
        mContactItem.setBlog(contactListItem.getBlog());
        mContactItem.setProfileWebSite(contactListItem.getProfileWebSite());
        mContactItem.setOtherWebSite(contactListItem.getOtherWebSite());

        if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
            ArrayList<MContactAddressItems> addresses = new ArrayList<>();
            for (Address addressItem : contactListItem.getAddresses()) {
                addresses.add(new MContactAddressItems(addressItem));
            }
            mContactItem.setAddresses(addresses);
        }

        mContactItem.setCategoryNames(contactListItem.getCategoryNames());
        mContactItem.setContactType(contactListItem.getContactType());
        mContactItem.setSelectedCategories(WebServiceUtils.getAsMSelectItemList(contactListItem.getSelectedCategories()));

        if (isLead) {
            //LEAD Info
            mContactItem.setLeadAssigneeID(contactListItem.getLeadAssigneeID());
            mContactItem.setLeadAssignee(contactListItem.getLeadAssignee());
            mContactItem.setLeadSourceID(contactListItem.getLeadSourceID());
            mContactItem.setLeadSource(contactListItem.getLeadSource());
            mContactItem.setLeadStatusID(contactListItem.getLeadStatus(true).getId());
            mContactItem.setLeadStatus(contactListItem.getLeadStatus(true).getName());

            if (!isBriefly) {
                mContactItem.setLeadAssignees(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadAssignees()));
                mContactItem.setLeadSources(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadSources()));
                mContactItem.setLeadStatuses(WebServiceUtils.getAsMSelectItemList(contactListItem.getLeadStatuses()));
            }

        }


        return mContactItem;
    }

    public ContactListItem convertFromMobile(ContactListItem contactListItem, boolean isLead) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }

        contactListItem.setObjectId(this.getObjectID() == null || this.getObjectID() == 0 ? null : this.getObjectID());
        contactListItem.setLastName(this.getLastName());
        contactListItem.setFirstName(this.getFirstName());
        if (this.getBirthDate() != null) {
            contactListItem.setBirthDate(new DateNonConvertable(this.getBirthDate()));
        }

        if (this.companyID != null || this.companyName != null) {
            if (contactListItem.getCrmAccount() == null || this.companyID == null || this.companyID.equals(0)) {
                contactListItem.setCrmAccount(new CrmAccountItem());
            }
            contactListItem.getCrmAccount().setObjectId(this.companyID);
            contactListItem.getCrmAccount().setName(this.companyName);
            contactListItem.getCrmAccount().setAccountTypes(convertAccountTypes(this.accountTypes));
        }

        contactListItem.setTitle(this.getTitle());
        contactListItem.setTitleId(getTitleID());

        contactListItem.setHomeEmail(this.getHomeEmail());
        contactListItem.setWorkEmail(this.getWorkEmail());
        contactListItem.setOtherEmail(this.getOtherEmail());

        contactListItem.setOtherName(this.getOtherName());
        contactListItem.setReportsTo(this.getReportsTo());
        contactListItem.setJobTitle(this.getJobTitle());

        contactListItem.setHomePhone(this.getHomePhone());
        contactListItem.setWorkPhone(this.getWorkPhone());
        contactListItem.setOtherPhone(this.getOtherPhone());
        contactListItem.setWorkFax(this.getWorkFax());
        contactListItem.setHomeFax(this.getHomeFax());
        contactListItem.setPager(this.getPager());
        contactListItem.setMobile(this.getMobile());

        if (isLead) {
            //LEAD Details
            contactListItem.setLeadAssignee(this.leadAssignee);
            contactListItem.setLeadAssigneeID(this.leadAssigneeID);
            contactListItem.setLeadStatus(contactListItem.getLeadStatus(true));
            contactListItem.getLeadStatus().setName(this.leadStatus);
            contactListItem.getLeadStatus().setId(this.leadStatusID);
            contactListItem.setLeadSource(this.leadSource);
            contactListItem.setLeadSourceID(this.leadSourceID);
        }

        contactListItem.setWorkWebSite(this.getWorkWebSite());
        contactListItem.setHomeWebSite(this.homeWebSite);
        contactListItem.setFtp(this.ftp);
        contactListItem.setBlog(this.blog);
        contactListItem.setOtherWebSite(this.otherWebSite);
        contactListItem.setProfileWebSite(this.profileWebSite);
        contactListItem.setHomePage(this.homePage);

        if (this.getAddresses() != null && this.getAddresses().size() > 0) {
            ArrayList<Address> contactAddressItemses = new ArrayList<>();
            int countAddresses = this.getAddresses().size();
            for (int i = 0; i < countAddresses; i++) {
                Address contactAddressItem = new Address();
                if (this.getAddresses().get(i).getIsNew()) {
                    if (this.getAddresses().get(i).getObjectID() == null) {
                        // new address qoshilganda
                        MContactAddressItems.convert(contactAddressItem, this.getAddresses().get(i), false);
                    } else {
                        // address update bo`lganda
                        int objectId = this.getAddresses().get(i).getObjectID();
                        for (int j = 0; j <contactListItem.getAddresses().size(); j++) {
                            if(contactListItem.getAddresses().get(j).getObjectID().equals(objectId)) {
                                contactAddressItem = contactListItem.getAddresses().get(j);
                                break;
                            };
                        }
                        MContactAddressItems.convert(contactAddressItem, this.getAddresses().get(i), false);
                    }
                } else {
                    // mavjud addresslarni saqlash
                    if(this.getAddresses().get(i).getObjectID() != null) {
                        int objectId = this.getAddresses().get(i).getObjectID();
                        for (int j = 0; j < contactListItem.getAddresses().size(); j++) {
                            if (contactListItem.getAddresses().get(j).getObjectID().equals(objectId)) {
                                contactAddressItem = contactListItem.getAddresses().get(j);
                                break;
                            }
                            ;
                        }
                    }
                    MContactAddressItems.convert(contactAddressItem, this.getAddresses().get(i), false);
                }
                contactAddressItemses.add(contactAddressItem);
            }

            contactListItem.setAddresses(contactAddressItemses);
        } else {
            contactListItem.setAddresses(null);
        }

        if (this.getSelectedCategories() != null) {
            ArrayList<SelectItem> selectedCategories = new ArrayList<>();
            for (MSelectItem mSelectItem : this.getSelectedCategories()) {
                selectedCategories.add(new SelectItem(mSelectItem.getObjectID(), mSelectItem.getName(), mSelectItem.getDescription()));
            }
            contactListItem.setSelectedCategories(selectedCategories);
        }

        contactListItem.setDeviceID(getDeviceID() != null && !"".equals(getDeviceID()) ? getDeviceID() : null);
        contactListItem.setDeviceContactID(getDeviceContactID() != null && !"".equals(getDeviceContactID()) ? getDeviceContactID() : null);

        return contactListItem;
    }

    public SelectItem[] convertAccountTypes(List<MSelectItem> accounts) {
        if (accounts == null) {
            return null;
        }
        List<SelectItem> selectedAccounts = new ArrayList<>();
        for (MSelectItem mSelectItem : accounts) {
            SelectItem selectItem = new SelectItem(mSelectItem.getObjectID(), mSelectItem.getName());
            selectItem.setSelected("selected".equalsIgnoreCase(mSelectItem.getDescription()));
            selectedAccounts.add(selectItem);
        }
        return selectedAccounts.toArray(new SelectItem[]{});
    }

    public static boolean convert(ContactListItem contactListItem, MContactListItem mContactListItem, boolean fromContactListItem) {

        try {
            if (fromContactListItem) {
                mContactListItem.setObjectID(contactListItem.getObjectId());
                mContactListItem.setLastName(contactListItem.getLastName());
                mContactListItem.setFirstName(contactListItem.getFirstName());
                mContactListItem.setMiddleName(contactListItem.getMiddleName());
                mContactListItem.setOwner(contactListItem.getOwner());
                mContactListItem.setOwnerID(contactListItem.getOwnerId());
                mContactListItem.setBirthDate(contactListItem.getBirthDate() != null && contactListItem.getBirthDate().getDateLong() > 0 ? contactListItem.getBirthDate().getNonConvertedDate() : null);
                mContactListItem.setCompanyName(contactListItem.getCrmAccount() != null ? contactListItem.getCrmAccount().getName() : null);
                mContactListItem.setCompanyID(contactListItem.getCrmAccount() != null ? contactListItem.getCrmAccount().getObjectId() : null);
                mContactListItem.setContactName(contactListItem.getContactName());
                mContactListItem.setDepartment(contactListItem.getDepartment());
                mContactListItem.setJobTitle(contactListItem.getJobTitle());
                mContactListItem.setTitle(contactListItem.getTitle());

                mContactListItem.setPrimaryAddress(new MContactAddressItems(contactListItem.getPrimaryAddress()));
                mContactListItem.setPrimaryEmail(contactListItem.getPrimaryEmail());
                mContactListItem.setPrimaryPhone(contactListItem.getPrimaryPhone());
                mContactListItem.setNote(contactListItem.getNote());

                mContactListItem.setHomeEmail(contactListItem.getHomeEmail());
                mContactListItem.setWorkEmail(contactListItem.getWorkEmail());
                mContactListItem.setOtherEmail(contactListItem.getOtherEmail());

                mContactListItem.setHomePhone(contactListItem.getHomePhone());
                mContactListItem.setWorkPhone(contactListItem.getWorkPhone());
                mContactListItem.setOtherPhone(contactListItem.getOtherPhone());
                mContactListItem.setWorkFax(contactListItem.getWorkFax());
                mContactListItem.setHomeFax(contactListItem.getHomeFax());
                mContactListItem.setPager(contactListItem.getPager());
                mContactListItem.setMobile(contactListItem.getMobile());

                mContactListItem.setgTalk(contactListItem.getgTalk());
                mContactListItem.setAIM(contactListItem.getAIM());
                mContactListItem.setQQ(contactListItem.getQQ());
                mContactListItem.setYahoo(contactListItem.getYahoo());
                mContactListItem.setSkype(contactListItem.getSkype());
                mContactListItem.setICQ(contactListItem.getICQ());
                mContactListItem.setJabber(contactListItem.getJabber());
                mContactListItem.setMSN(contactListItem.getMSN());

                if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
                    ArrayList<MContactAddressItems> mContactAddressItemses = new ArrayList<>();
                    for (Address contactAddressItems : contactListItem.getAddresses()) {
                        mContactAddressItemses.add(new MContactAddressItems(contactAddressItems));
                    }
                    mContactListItem.setAddresses(mContactAddressItemses);
                }

                if (contactListItem.getSelectedCategories() != null) {
                    List<MSelectItem> mSelectedCategories = new ArrayList<>();
                    for (SelectItem selectItem : contactListItem.getSelectedCategories()) {
                        mSelectedCategories.add(new MSelectItem(selectItem));
                    }
                    mContactListItem.setSelectedCategories(mSelectedCategories);
                }

            } else {
                contactListItem.setObjectId(mContactListItem.getObjectID() == null || mContactListItem.getObjectID() == 0 ? null : mContactListItem.getObjectID());
                contactListItem.setLastName(mContactListItem.getLastName());
                contactListItem.setFirstName(mContactListItem.getFirstName());
                contactListItem.setMiddleName(mContactListItem.getMiddleName());
                contactListItem.setOwner(mContactListItem.getOwner());
                contactListItem.setOwnerId(mContactListItem.getOwnerID() == null || mContactListItem.getOwnerID() == 0 ? null : mContactListItem.getOwnerID());
                if (mContactListItem.getBirthDate() != null) {
                    contactListItem.setBirthDate(new DateNonConvertable(mContactListItem.getBirthDate()));
                }

                if (contactListItem.getCrmAccount() != null) {
                    contactListItem.getCrmAccount().setName(mContactListItem.getCompanyName());
                    contactListItem.getCrmAccount().setObjectId(mContactListItem.getCompanyID() == null
                            || mContactListItem.getCompanyID() == 0 ? null : mContactListItem.getCompanyID());

                }
                contactListItem.setDepartment(mContactListItem.getDepartment());
                contactListItem.setJobTitle(mContactListItem.getJobTitle());
                Address contactAddressItems = new Address();
                MContactAddressItems.convert(contactAddressItems, mContactListItem.getPrimaryAddress(), false);
                contactListItem.setPrimaryAddress(contactAddressItems);
                contactListItem.setPrimaryEmail(mContactListItem.getPrimaryEmail());
                contactListItem.setPrimaryPhone(mContactListItem.getPrimaryPhone());
                contactListItem.setTitle(mContactListItem.getTitle());

                contactListItem.setUpdatedDate(mContactListItem.getUpdatedDate());

                contactListItem.setHomeEmail(mContactListItem.getHomeEmail());
                contactListItem.setWorkEmail(mContactListItem.getWorkEmail());
                contactListItem.setOtherEmail(mContactListItem.getOtherEmail());

                contactListItem.setHomePhone(mContactListItem.getHomePhone());
                contactListItem.setWorkPhone(mContactListItem.getWorkPhone());
                contactListItem.setOtherPhone(mContactListItem.getOtherPhone());
                contactListItem.setWorkFax(mContactListItem.getWorkFax());
                contactListItem.setHomeFax(mContactListItem.getHomeFax());
                contactListItem.setPager(mContactListItem.getPager());
                contactListItem.setMobile(mContactListItem.getMobile());

                contactListItem.setgTalk(mContactListItem.getgTalk());
                contactListItem.setAIM(mContactListItem.getAIM());
                contactListItem.setQQ(mContactListItem.getQQ());
                contactListItem.setYahoo(mContactListItem.getYahoo());
                contactListItem.setSkype(mContactListItem.getSkype());
                contactListItem.setICQ(mContactListItem.getICQ());
                contactListItem.setJabber(mContactListItem.getJabber());
                contactListItem.setMSN(mContactListItem.getMSN());

                contactListItem.setWorkWebSite(mContactListItem.getWorkWebSite());

                if (mContactListItem.getAddresses() != null && mContactListItem.getAddresses().size() > 0) {
                    ArrayList<Address> contactAddressItemses = new ArrayList<>();
                    for (MContactAddressItems mContactAddressItems : mContactListItem.getAddresses()) {
                        Address contactAddressItem = new Address();
                        MContactAddressItems.convert(contactAddressItem, mContactAddressItems, false);
                        contactAddressItemses.add(contactAddressItem);
                    }

                    contactListItem.setAddresses(contactAddressItemses);
                } else {
                    contactListItem.setAddresses(null);
                }

                if (mContactListItem.getSelectedCategories() != null) {
                    ArrayList<SelectItem> selectedCategories = new ArrayList<>();
                    for (MSelectItem mSelectItem : mContactListItem.getSelectedCategories()) {
                        selectedCategories.add(new SelectItem(mSelectItem.getObjectID(), mSelectItem.getName(), mSelectItem.getDescription()));
                    }
                    contactListItem.setSelectedCategories(selectedCategories);
                }

            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<MSelectItem> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<MSelectItem> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isLeadContact() {
        return leadContact;
    }

    public void setLeadContact(Boolean leadContact) {
        this.leadContact = leadContact;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<String> getHomeEmail() {
        return homeEmail;
    }

    public void setHomeEmail(ArrayList<String> homeEmail) {
        this.homeEmail = homeEmail;
    }

    public ArrayList<String> getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(ArrayList<String> workEmail) {
        this.workEmail = workEmail;
    }

    public ArrayList<String> getOtherEmail() {
        return otherEmail;
    }

    public void setOtherEmail(ArrayList<String> otherEmail) {
        this.otherEmail = otherEmail;
    }

    public ArrayList<String> getWorkPhone() {
        return workPhone;
    }

    public List<MTreeSelectItem> getContactCategories() {
        return contactCategories;
    }

    public void setContactCategories(List<MTreeSelectItem> contactCategories) {
        this.contactCategories = contactCategories;
    }

    public void setWorkPhone(ArrayList<String> workPhone) {
        this.workPhone = workPhone;
    }

    public ArrayList<String> getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(ArrayList<String> homePhone) {
        this.homePhone = homePhone;
    }

    public ArrayList<String> getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(ArrayList<String> otherPhone) {
        this.otherPhone = otherPhone;
    }

    public ArrayList<String> getHomeFax() {
        return homeFax;
    }

    public void setHomeFax(ArrayList<String> homeFax) {
        this.homeFax = homeFax;
    }

    public ArrayList<String> getWorkFax() {
        return workFax;
    }

    public void setWorkFax(ArrayList<String> workFax) {
        this.workFax = workFax;
    }

    public ArrayList<String> getPager() {
        return pager;
    }

    public void setPager(ArrayList<String> pager) {
        this.pager = pager;
    }

    public ArrayList<String> getMobile() {
        return mobile;
    }

    public void setMobile(ArrayList<String> mobile) {
        this.mobile = mobile;
    }

    public ArrayList<MContactAddressItems> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<MContactAddressItems> addresses) {
        this.addresses = addresses;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public MContactAddressItems getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(MContactAddressItems primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getHomeEmailId() {
        return homeEmailId;
    }

    public void setHomeEmailId(Integer homeEmailId) {
        this.homeEmailId = homeEmailId;
    }

    public Integer getWorkPhoneId() {
        return workPhoneId;
    }

    public void setWorkPhoneId(Integer workPhoneId) {
        this.workPhoneId = workPhoneId;
    }

    public Integer getHomePhoneId() {
        return homePhoneId;
    }

    public void setHomePhoneId(Integer homePhoneId) {
        this.homePhoneId = homePhoneId;
    }

    public Integer getOtherPhoneId() {
        return otherPhoneId;
    }

    public void setOtherPhoneId(Integer otherPhoneId) {
        this.otherPhoneId = otherPhoneId;
    }

    public Integer getHomeFaxId() {
        return homeFaxId;
    }

    public void setHomeFaxId(Integer homeFaxId) {
        this.homeFaxId = homeFaxId;
    }

    public Integer getMobileId() {
        return mobileId;
    }

    public void setMobileId(Integer mobileId) {
        this.mobileId = mobileId;
    }

    public ArrayList<String> getgTalk() {
        return gTalk;
    }

    public void setgTalk(ArrayList<String> gTalk) {
        this.gTalk = gTalk;
    }

    public ArrayList<String> getAIM() {
        return AIM;
    }

    public void setAIM(ArrayList<String> AIM) {
        this.AIM = AIM;
    }

    public ArrayList<String> getYahoo() {
        return yahoo;
    }

    public void setYahoo(ArrayList<String> yahoo) {
        this.yahoo = yahoo;
    }

    public ArrayList<String> getSkype() {
        return skype;
    }

    public void setSkype(ArrayList<String> skype) {
        this.skype = skype;
    }

    public ArrayList<String> getQQ() {
        return QQ;
    }

    public void setQQ(ArrayList<String> QQ) {
        this.QQ = QQ;
    }

    public ArrayList<String> getMSN() {
        return MSN;
    }

    public void setMSN(ArrayList<String> MSN) {
        this.MSN = MSN;
    }

    public ArrayList<String> getICQ() {
        return ICQ;
    }

    public void setICQ(ArrayList<String> ICQ) {
        this.ICQ = ICQ;
    }

    public ArrayList<String> getJabber() {
        return jabber;
    }

    public void setJabber(ArrayList<String> jabber) {
        this.jabber = jabber;
    }

    public ArrayList<String> getHomeWebSite() {
        return homeWebSite;
    }

    public void setHomeWebSite(ArrayList<String> homeWebSite) {
        this.homeWebSite = homeWebSite;
    }

    public ArrayList<String> getWorkWebSite() {
        return workWebSite;
    }

    public void setWorkWebSite(ArrayList<String> workWebSite) {
        this.workWebSite = workWebSite;
    }

    public ArrayList<String> getHomePage() {
        return homePage;
    }

    public void setHomePage(ArrayList<String> homePage) {
        this.homePage = homePage;
    }

    public ArrayList<String> getFtp() {
        return ftp;
    }

    public void setFtp(ArrayList<String> ftp) {
        this.ftp = ftp;
    }

    public ArrayList<String> getBlog() {
        return blog;
    }

    public void setBlog(ArrayList<String> blog) {
        this.blog = blog;
    }

    public ArrayList<String> getProfileWebSite() {
        return profileWebSite;
    }

    public void setProfileWebSite(ArrayList<String> profileWebSite) {
        this.profileWebSite = profileWebSite;
    }

    public ArrayList<String> getOtherWebSite() {
        return otherWebSite;
    }

    public void setOtherWebSite(ArrayList<String> otherWebSite) {
        this.otherWebSite = otherWebSite;
    }

    public List<MSelectItem> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<MSelectItem> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public List<MSelectItem> getOrganizationTypes() {
        return organizationTypes;
    }

    public void setOrganizationTypes(List<MSelectItem> organizationTypes) {
        this.organizationTypes = organizationTypes;
    }

    public Integer getOrganizationTypeID() {
        return organizationTypeID;
    }

    public void setOrganizationTypeID(Integer organizationTypeID) {
        this.organizationTypeID = organizationTypeID;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public List<MSelectItem> getLeadAssignees() {
        return leadAssignees;
    }

    public void setLeadAssignees(List<MSelectItem> leadAssignees) {
        this.leadAssignees = leadAssignees;
    }

    public Integer getLeadAssigneeID() {
        return leadAssigneeID;
    }


    public void setLeadAssigneeID(Integer leadAssigneeID) {
        this.leadAssigneeID = leadAssigneeID;
    }

    public String getLeadAssignee() {
        return leadAssignee;
    }

    public void setLeadAssignee(String leadAssignee) {
        this.leadAssignee = leadAssignee;
    }

    public List<MSelectItem> getLeadSources() {
        return leadSources;
    }

    public void setLeadSources(List<MSelectItem> leadSources) {
        this.leadSources = leadSources;
    }

    public Integer getLeadSourceID() {
        return leadSourceID;
    }

    public void setLeadSourceID(Integer leadSourceID) {
        this.leadSourceID = leadSourceID;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public List<MSelectItem> getLeadStatuses() {
        return leadStatuses;
    }

    public void setLeadStatuses(List<MSelectItem> leadStatuses) {
        this.leadStatuses = leadStatuses;
    }

    public Integer getLeadStatusID() {
        return leadStatusID;
    }

    public void setLeadStatusID(Integer leadStatusID) {
        this.leadStatusID = leadStatusID;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Integer getLeadRatingID() {
        return leadRatingID;
    }

    public void setLeadRatingID(Integer leadRatingID) {
        this.leadRatingID = leadRatingID;
    }

    public String getLeadRating() {
        return leadRating;
    }

    public void setLeadRating(String leadRating) {
        this.leadRating = leadRating;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<MSelectItem> getCountries() {
        return countries;
    }

    public void setCountries(List<MSelectItem> countries) {
        this.countries = countries;
    }

    public List<MSelectItem> getStates() {
        return states;
    }

    public void setStates(List<MSelectItem> states) {
        this.states = states;
    }

    public List<MSelectItem> getLeadRatings() {
        return leadRatings;
    }

    public void setLeadRatings(List<MSelectItem> leadRatings) {
        this.leadRatings = leadRatings;
    }

    public List<MSelectItem> getIndustries() {
        return industries;
    }

    public void setIndustries(List<MSelectItem> industries) {
        this.industries = industries;
    }

    public Integer getIndustryID() {
        return industryID;
    }

    public void setIndustryID(Integer industryID) {
        this.industryID = industryID;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public MSelectItem getWebSite() {
        return webSite;
    }

    public void setWebSite(MSelectItem webSite) {
        this.webSite = webSite;
    }

    public MSelectItem getEmail() {
        return email;
    }

    public void setEmail(MSelectItem email) {
        this.email = email;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceContactID() {
        return deviceContactID;
    }

    public void setDeviceContactID(String deviceContactID) {
        this.deviceContactID = deviceContactID;
    }

    public List<MSelectItem> getTitles() {
        return titles;
    }

    public void setTitles(List<MSelectItem> titles) {
        this.titles = titles;
    }

    public Integer getTitleID() {
        return titleID;
    }

    public void setTitleID(Integer titleID) {
        this.titleID = titleID;
    }
}



