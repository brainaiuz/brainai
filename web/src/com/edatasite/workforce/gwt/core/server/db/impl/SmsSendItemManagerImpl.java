package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.SmsSendItemManager;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/20/11
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("smsSendItemManager")
public class SmsSendItemManagerImpl extends BaseManager<EdsSmsSendItem> implements SmsSendItemManager, Constants {
    public SmsSendItemManagerImpl() {
        super(EdsSmsSendItem.class);
    }

    @Override
    public List<EdsSmsSendItem> getSmsList(Integer id) {
        return (List<EdsSmsSendItem>) find("select distinct s from EdsSmsSendItem s where s.userID=? and s.entityID=? and coalesce(s.isDelete,false)=false order by s.sentDate desc", getUser().getObjectID(), id);
    }

    @Override
    public List<EdsSmsSendItem> getSMSBy(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT sms.*").append(" FROM ").append(getCompanyId()).append(".smsSendItem sms WHERE sms.isDelete is not true ");
        if (fp.getRelationID() != null && fp.getRelationType() != null) {
            List<Integer> trackerIDs = findNative(EdsRelation.getTrackerIDsByRelationQuery(fp.getRelationID(), fp.getRelationType(), RelationItem.TYPE_SMS));
            sql.append(" AND sms.id in (").append(ServerUtils.getAsCommoDelimited(trackerIDs, "0", ",")).append(")");
        }
        sql.append(" order by ");

        if (OrderFieldEnum.DATE.getField().equalsIgnoreCase(fp.getSortField())) {
            sql.append(" sms.sentDate ");
        } else if (OrderFieldEnum.ID.getField().equalsIgnoreCase(fp.getSortField())) {
            sql.append(" sms.id ");
        } else if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(fp.getSortField())) {
            sql.append(" sms.messagetext ");
        } else {
            sql.append(" sms.sentDate ");
        }

        if (fp.isAscending()) {
            sql.append(" asc ");
        } else {
            sql.append(" desc ");
        }

        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit()).append(" offset ").append(fp.getStart());
        }

        return findNative(sql.toString(), EdsSmsSendItem.class);
    }
}
