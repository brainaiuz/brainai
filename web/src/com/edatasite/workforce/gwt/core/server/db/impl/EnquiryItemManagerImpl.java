package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiryItem;
import com.edatasite.workforce.gwt.core.server.db.EnquiryItemManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
@Repository("enquiryItemManager")
public class EnquiryItemManagerImpl extends BaseManager<EdsEnquiryItem> implements EnquiryItemManager {
    public EnquiryItemManagerImpl() {
        super(EdsEnquiryItem.class);
    }

    @Override
    public void deleteEnquiryItems(Integer objectID) {
        updateNative("DELETE FROM " + getCompanyId() + ".enquiryItem WHERE enquiry_id=" + objectID);
    }
}
