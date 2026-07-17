package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiryItem;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public interface EnquiryItemManager extends Manager<EdsEnquiryItem> {
    void deleteEnquiryItems(Integer objectID);
}
