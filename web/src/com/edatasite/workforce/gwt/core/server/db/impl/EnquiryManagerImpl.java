package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiry;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.EnquiryManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
@Repository("enquiryManager")
public class EnquiryManagerImpl extends BaseManager<EdsEnquiry> implements EnquiryManager {
    public EnquiryManagerImpl() {
        super(EdsEnquiry.class);
    }

    @Override
    public List<EdsEnquiry> getEnquiryList(ListingFilterParameter fp) {
        StringBuilder hql = new StringBuilder("SELECT en FROM EdsEnquiry en ");
        hql.append("left join en.customer customer ");
        hql.append("left join en.enquiryMode enquiryMode ");
        hql.append("left join en.contact contact ");
        hql.append("WHERE (en.deleted IS NULL OR en.deleted=FALSE) ");

        if (fp.getClientId() != null) {
            hql.append("customer.objectID = ").append(fp.getClientId());
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            hql.append(" AND (lower(enquiryMode.name) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(customer.name) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(en.refInfo) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(contact.primaryEmail ) like '" + fp.getSqlSearchKey() + "')");
        }
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            String ASC = "ASC";
            if (!fp.isAscending()) {
                ASC = "DESC";
            }
            if (EnquiryItem.ENQUIRY_MODE.equals(fp.getSortField())) {
                hql.append(" ORDER BY enquiryMode.name ");
                hql.append(ASC);
            } else if (EnquiryItem.ENQUIRY_NUMBER.equals(fp.getSortField())) {
                hql.append(" ORDER BY en.number ");
                hql.append(ASC);
            } else if (EnquiryItem.ENQUIRY_CUSTOMER.equals(fp.getSortField())) {
                hql.append(" ORDER BY customer.name ");
                hql.append(ASC);
            } else if (EnquiryItem.ENQUIRY_DATE.equals(fp.getSortField())) {
                hql.append(" ORDER BY en.enquiryDate ");
                hql.append(ASC);
            } else if (EnquiryItem.REF_INFO.equals(fp.getSortField())) {
                hql.append(" ORDER BY en.refInfo ");
                hql.append(ASC);
            } else if (EnquiryItem.CONTACT_EMAIL.equals(fp.getSortField())) {
                hql.append(" ORDER BY contact.primaryEmail ");
                hql.append(ASC);
            }

        } else {
            hql.append(" ORDER BY en.lastUpdateTime DESC");
        }
        return findInterval(hql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getEnquiryListTotalCount(ListingFilterParameter fp) {
        StringBuilder hql = new StringBuilder("SELECT count(en.objectID) FROM EdsEnquiry en ");
        hql.append("left join en.customer customer ");
        hql.append("left join en.enquiryMode enquiryMode ");
        hql.append("left join en.contact contact ");
        hql.append("WHERE (en.deleted IS NULL OR en.deleted=FALSE)");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            hql.append(" AND (lower(enquiryMode.name) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(customer.name) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(en.refInfo) like '" + fp.getSqlSearchKey() + "'");
            hql.append(" OR lower(contact.primaryEmail ) like '" + fp.getSqlSearchKey() + "')");
        }
        return ((Long) findSingle(hql.toString())).intValue();
    }

    @Override
    public Integer getEnquiryLastIntNumber() {
        return (Integer) findSingle("SELECT e.intNumber FROM EdsEnquiry e WHERE e.intNumber IS NOT NULL ORDER BY e.intNumber DESC");
    }
}
