package com.edatasite.workforce.gwt.trainingcenter.server;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiry;
import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiryItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CourseManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EnquiryItemManager;
import com.edatasite.workforce.gwt.core.server.db.EnquiryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.EnquiryService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.AddEditEnquiryItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/18/12
 * Time: 1:40 PM
 */
@Transactional
@Service("enquiryService")
public class EnquiryServiceImpl implements EnquiryService {

    @Autowired
    private EnquiryManager enquiryManager;
    @Autowired
    private EnquiryItemManager enquiryItemManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager contactManager;
    @Autowired
    private CourseManager courseManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;

    @Override
    public void saveEnqueryItem(EnquiryItem enquiryItem) {
        EdsEnquiry edsEnquiry = new EdsEnquiry();
        if (enquiryItem.getObjectID() != null) {
            edsEnquiry = enquiryManager.get(enquiryItem.getObjectID());
        } else {
            enquiryManager.create(edsEnquiry);
        }
        if (enquiryItem.getCustomer().getId() != null) {
            edsEnquiry.setCustomer(crmAccountManager.get(enquiryItem.getCustomer().getId()));
        }
        if (enquiryItem.getContactDetails().getObjectId() != null) {
            edsEnquiry.setContact(contactManager.get(enquiryItem.getContactDetails().getObjectId()));
        }
        if (enquiryItem.getEnquiryMode().getId() != null) {
            edsEnquiry.setEnquiryMode(referenceManager.get(enquiryItem.getEnquiryMode().getId()));
        }
        if (enquiryItem.getNumberData() != null) {
            edsEnquiry.setIntNumber(enquiryItem.getNumberData().getIntNumber());
            edsEnquiry.setNumber(enquiryItem.getNumberData().getNumberString());
        }
        edsEnquiry.setRefInfo(enquiryItem.getRefInfo());
        edsEnquiry.setEnquiryDate(enquiryItem.getEnquiryDate());
        edsEnquiry.setLastUpdateTime(new Date());

        enquiryItemManager.deleteEnquiryItems(edsEnquiry.getObjectID());
        // create Enquiry item
        for (EnquiryCourseItem enquiryCourseItem : enquiryItem.getCourseItemList()) {
            EdsEnquiryItem edsEnquiryItem = new EdsEnquiryItem();
            edsEnquiryItem.setEnquiry(edsEnquiry);
            if (enquiryCourseItem.getCourseItem().getId() != null) {
//                edsEnquiryItem.setItem(courseManager.get(enquiryCourseItem.getCourseItem().getId()));
            }
            // Session
            if (enquiryCourseItem.getSession().getId() != null) {
                edsEnquiryItem.setSession(referenceManager.get(enquiryCourseItem.getSession().getId()));
            }
            edsEnquiryItem.setVenue(enquiryCourseItem.getVenue());

            // Number of Stdents
            if (enquiryCourseItem.getNoOfStudents() != null) {
                edsEnquiryItem.setQty(new BigDecimal(enquiryCourseItem.getNoOfStudents()));
            }
            edsEnquiryItem.setDateRequired(enquiryCourseItem.getDateRequired());
            enquiryItemManager.create(edsEnquiryItem);
        }
    }

    @Override
    public EnquiryItem getEnquiryItem(Integer enquiryId) {
        EnquiryItem enquiryItem = new EnquiryItem();
        enquiryItem.setObjectID(enquiryId);
        EdsEnquiry edsEnquiry = enquiryManager.get(enquiryId);
        if (edsEnquiry.getIntNumber() != null) {
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            if (settings != null && settings.getEnquiryNumberingFormat() != null) {
                enquiryItem.setNumberData(settings.parseNumberData(edsEnquiry.getIntNumber() != null ? edsEnquiry.getIntNumber() - 1 : null, settings.getEnquiryNumberingFormat()));
            } else {
                enquiryItem.setNumberData(EdsNumberingSettings.getDefaultData(edsEnquiry.getIntNumber() != null ? edsEnquiry.getIntNumber() - 1 : null, EdsNumberingSettings.DEF_ENQUIRY_PREFIX));
            }
        }
        if (edsEnquiry.getCustomer() != null) {
            enquiryItem.setCustomer(new SelectItem(edsEnquiry.getCustomer().getObjectID(), edsEnquiry.getCustomer().getName()));
            if (edsEnquiry.getCustomer().getCurrency() != null) {
                enquiryItem.setCurrency(new SelectItem(edsEnquiry.getCustomer().getCurrency().getObjectID(), edsEnquiry.getCustomer().getCurrency().getName()));
            }
        }
        if (edsEnquiry.getEnquiryMode() != null) {
            enquiryItem.setEnquiryMode(new SelectItem(edsEnquiry.getEnquiryMode().getObjectID(), edsEnquiry.getEnquiryMode().getName()));
        }
        enquiryItem.setEnquiryDate(edsEnquiry.getEnquiryDate());
        enquiryItem.setRefInfo(edsEnquiry.getRefInfo());
        if (edsEnquiry.getContact() != null) {
            enquiryItem.setContactDetails(edsEnquiry.getContact().getRPC(null));
        }
        ArrayList<EnquiryCourseItem> enquiryCourseItems = new ArrayList<>();
        HashSet<Integer> courseIds = new HashSet<>();
        for (EdsEnquiryItem edsEnquiryItem : edsEnquiry.getItems()) {
            EnquiryCourseItem enquiryCourseItem = new EnquiryCourseItem();
            enquiryCourseItem.setObjectID(edsEnquiryItem.getObjectID());
            if (edsEnquiryItem.getItem() != null) {
                EdsCourse edsCourse = courseManager.get(edsEnquiryItem.getItem().getObjectID());
                enquiryCourseItem.setCourseItem(new SelectItem(edsCourse.getObjectID(), edsCourse.getName()));
                enquiryCourseItem.setDuration(edsCourse.getDuration());
                courseIds.add(enquiryCourseItem.getCourseItem().getId());
            }
            enquiryCourseItem.setDateRequired(edsEnquiryItem.getDateRequired());
            if (edsEnquiryItem.getQty() != null) {
                enquiryCourseItem.setNoOfStudents(edsEnquiryItem.getQty().intValue());
            }
            if (edsEnquiryItem.getSession() != null) {
                enquiryCourseItem.setSession(new SelectItem(edsEnquiryItem.getSession().getObjectID(), edsEnquiryItem.getSession().getName()));
            }
            if (edsEnquiryItem.getVenue() != null) {
                enquiryCourseItem.setVenue(edsEnquiryItem.getVenue());
            }
            enquiryCourseItems.add(enquiryCourseItem);
        }
        List<EdsProductCategory> edsProductCategoryList = courseManager.getCourseProductCategorisList(ServerUtils.getAsCommoDelimited(new ArrayList<Integer>(courseIds), "0"));
        ArrayList<SelectItem> productCategoriesItem = new ArrayList<>();
        for (EdsProductCategory edsProductCategory : edsProductCategoryList) {
            productCategoriesItem.add(new SelectItem(edsProductCategory.getObjectID(), edsProductCategory.getName()));
        }
        enquiryItem.setProductCategories(productCategoriesItem);
        enquiryItem.setCourseItemList(enquiryCourseItems);
        return enquiryItem;
    }

    @Override
    public AddEditEnquiryItem getEnquiryItemForAddEdit(Integer enquiryId) {
        List<EdsReference> enquiryModeDomens = referenceManager.listReferences(EdsEnquiry.ENQUIRY_MODE_PARENT, true);
        List<EdsReference> enquiryItemSessionDomens = referenceManager.listReferences(Constants.SESSION_STATUS, true);
        List<EdsProductCategory> productCategoriesDomens = productCategoryManager.getProductCategories();
        int i = 0;
        SelectItem[] enquiryModeItem = new SelectItem[enquiryModeDomens.size()];
        for (EdsReference edsReference : enquiryModeDomens) {
            enquiryModeItem[i++] = new SelectItem(edsReference.getObjectID(), edsReference.getName());
        }
        i = 0;
        SelectItem[] enquiryItemSession = new SelectItem[enquiryItemSessionDomens.size()];
        for (EdsReference edsReference : enquiryItemSessionDomens) {
            enquiryItemSession[i++] = new SelectItem(edsReference.getObjectID(), edsReference.getName());
        }
        i = 0;
        SelectItem[] productCategpriesItem = new SelectItem[productCategoriesDomens.size()];
        for (EdsProductCategory edsProductCategory : productCategoriesDomens) {
            productCategpriesItem[i++] = new SelectItem(edsProductCategory.getObjectID(), edsProductCategory.getName());
        }
        AddEditEnquiryItem addEditEnquiryItem = new AddEditEnquiryItem();
        if (enquiryId != null) {
            addEditEnquiryItem.setEnquiryItem(getEnquiryItem(enquiryId));
        } else {
            Integer intNumber = enquiryManager.getEnquiryLastIntNumber();
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            if (settings != null && settings.getEnquiryNumberingFormat() != null) {
                addEditEnquiryItem.getEnquiryItem().setNumberData(settings.parseNumberData(intNumber != null ? intNumber : 0, settings.getEnquiryNumberingFormat()));
            } else {
                addEditEnquiryItem.getEnquiryItem().setNumberData(EdsNumberingSettings.getDefaultData(intNumber != null ? intNumber : 0, EdsNumberingSettings.DEF_ENQUIRY_PREFIX));
            }
        }
        addEditEnquiryItem.setEnquiryModes(enquiryModeItem);
        addEditEnquiryItem.setSessionItems(enquiryItemSession);
        addEditEnquiryItem.setProductCategories(productCategpriesItem);
        return addEditEnquiryItem;
    }

    @Override
    public ListResult<EnquiryItem> geEnquiryList(ListingFilterParameter fp) {
        List<EdsEnquiry> edsEnquiryList = enquiryManager.getEnquiryList(fp);
        Integer total = enquiryManager.getEnquiryListTotalCount(fp);
        ArrayList<EnquiryItem> enquiryItems = new ArrayList<>();
        for (EdsEnquiry edsEnquiry : edsEnquiryList) {
            EnquiryItem enquiryItem = new EnquiryItem();
            enquiryItem.setObjectID(edsEnquiry.getObjectID());
            if (edsEnquiry.getCustomer() != null) {
                enquiryItem.setCustomer(new SelectItem(edsEnquiry.getCustomer().getObjectID(), edsEnquiry.getCustomer().getName()));
                if (edsEnquiry.getCustomer().getCurrency() != null) {
                    enquiryItem.setCurrency(new SelectItem(edsEnquiry.getCustomer().getCurrency().getObjectID(), edsEnquiry.getCustomer().getCurrency().getName()));
                }
            }

            if (edsEnquiry.getEnquiryMode() != null) {
                enquiryItem.setEnquiryMode(new SelectItem(edsEnquiry.getEnquiryMode().getObjectID(), edsEnquiry.getEnquiryMode().getName()));
            }
            if (edsEnquiry.getContact() != null) {
                enquiryItem.setContactDetails(edsEnquiry.getContact().getRPC(fp));
            }
            enquiryItem.setNumberData(new NumberData(edsEnquiry.getNumber(), edsEnquiry.getIntNumber() != null ? edsEnquiry.getIntNumber() - 1 : 0));
            enquiryItem.setRefInfo(edsEnquiry.getRefInfo());
            enquiryItem.setEnquiryDate(edsEnquiry.getEnquiryDate());
            enquiryItem.setLastUpdateTime(edsEnquiry.getLastUpdateTime());
            enquiryItems.add(enquiryItem);
        }
        return new ListResult<>(enquiryItems, total);
    }

    @Override
    public SelectItem getCustomerCurrency(Integer selectedItemID) {
        EdsCrmAccount customer = crmAccountManager.get(selectedItemID);
        SelectItem currency = null;
        if (customer.getCurrency() != null) {
            currency = new SelectItem(customer.getCurrency().getObjectID(), customer.getCurrency().getName());
        }
        return currency;
    }

    public ContactListItem getContactDetails(Integer contactID) {
        EdsCrmContact edsCrmContact = contactManager.get(contactID);
        return edsCrmContact.getRPC(null);
    }

    @Override
    public void deleteEnquiry(Integer objectID) {
        EdsEnquiry edsEnquiry = enquiryManager.get(objectID);
        edsEnquiry.setDeleted(true);
    }

    @Override
    public SelectItem[] getCourseByProductCategoryIds(ArrayList<Integer> productCategoryIds) {
        List<EdsCourse> edsCourseList = courseManager.getByProductIdsCourseList(ServerUtils.getAsCommoDelimited(productCategoryIds, "(0)"));
        SelectItem[] items = new SelectItem[edsCourseList.size()];
        int i = 0;
        for (EdsCourse edsCourse : edsCourseList) {
            items[i++] = new SelectItem(edsCourse.getObjectID(), edsCourse.getName(), String.valueOf(edsCourse.getDuration()));
        }
        return items;
    }

    @Override
    public SelectItem[] getClientEnquiries(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setLimit(20);

        List<EdsEnquiry> enquiries = enquiryManager.getEnquiryList(fp);

        return new SelectItem[0];
    }
}
