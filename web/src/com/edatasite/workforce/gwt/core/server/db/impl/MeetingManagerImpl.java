package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: developer
 * Date: 4/20/12
 * Time: 7:14 PM
 */
@Repository("meetingManager")
public class MeetingManagerImpl extends AttachmentSupportManager<EdsMeetingMinutes> implements MeetingManager {

    public MeetingManagerImpl() {
        super(EdsMeetingMinutes.class);
    }

    public List<EdsMeetingMinutes> getMeetingMinutesList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        boolean hasAccess = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_MEETING_MINUTES_WORKSPACE);
        if (filterParameter.isHRMS()) {
            hasAccess = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_MEETING_MINUTES);
        }
        sql.append("SELECT distinct mm.id, mm.*, cb.firstName, pb.firstName, ty.name \n");
        sql.append("FROM ").append(getCompanyId()).append(".meetingminutes mm \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".meetingattendees ma ON (mm.id=ma.meetingminutesid and ma.isattendees is not false ) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myuser pb ON (pb.id = mm.prepairedby) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myuser cb ON (cb.id = mm.userid) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference ty ON (ty.id = mm.type) \n");
        if (hasAccess) {
            sql.append("WHERE 1=1 \n");
        } else {
            sql.append("WHERE (mm.prepairedby=" + filterParameter.getUserID() + " or mm.userid=" + filterParameter.getUserID() + " or ma.attendeesemployeeid=" + filterParameter.getUserID() + ")");
        }

        //searching
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" LOWER(mm.title) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(mm.meetingnumber) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(ty.name) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");

            sql.append("OR LOWER(cb.firstName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(cb.lastName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(cb.firstName || ' ' || cb.lastName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(cb.lastName || ' ' || cb.firstName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");

            sql.append("OR LOWER(pb.firstName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(pb.lastName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(pb.firstName || ' ' || pb.lastName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(pb.lastName || ' ' || pb.firstName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");

            sql.append(") \n");
        }
        //ordering
        sql.append(" ORDER BY ");
        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if (MeetingMinutesItem.ACTION.equals(filterParameter.getSortField()) || MeetingMinutesItem.NAME.equals(filterParameter.getSortField())) {
                sql.append(" mm.title");
            } else if (MeetingMinutesItem.LOCATION.equals(filterParameter.getSortField())) {
                sql.append(" mm.location");
            } else if (MeetingMinutesItem.TYPE.equals(filterParameter.getSortField())) {
                sql.append(" ty.name");
            } else if (MeetingMinutesItem.CALLED_BY.equals(filterParameter.getSortField())) {
                sql.append(" cb.firstName");
            } else if (MeetingMinutesItem.DATE.equals(filterParameter.getSortField())) {
                sql.append(" mm.startDate");
            } else if (MeetingMinutesItem.END_DATE.equals(filterParameter.getSortField())) {
                sql.append(" mm.dueDate");
            } else if (MeetingMinutesItem.PREPARED_BY.equals(filterParameter.getSortField())) {
                sql.append(" pb.firstName");
            } else if (MeetingMinutesItem.MEETING_ID.equals(filterParameter.getSortField())) {
                sql.append(" mm.meetingnumber");
            } else {
                sql.append(" mm.title");
            }
            if (filterParameter.getSortDir() != null) {
                if (Integer.valueOf(1).equals(filterParameter.getSortDir())) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" mm.lastUpdateTime DESC nulls last");
        }

        return findNative(sql.toString(), EdsMeetingMinutes.class);
    }

    @Override
    public List<EdsMeetingMinutes> getMeetingMinutesById(Integer projectId) {
        Map<String, Object> paramMap = new HashMap<>();
        String sql = "";
        sql = "select mm from EdsMeetingMinutes mm where mm.objectID=?";

        return find("select mm from EdsMeetingMinutes mm where mm.objectID=?", projectId);
    }

    @Override
    public List<EdsMeetingMinutes> getMeetingMinutesList() {
        return find("select mm from EdsMeetingMinutes mm order by mm.lastUpdateTime desc");
    }

    @Override
    public Integer getProductLastIntNumber() {
//		return (Integer) findSingle("select p.objectID from EdsMeetingMinutes p where p.deleted=false and p.intNumber is not null order by p.intNumber desc");
        return (Integer) findSingle("select mm.intNumber from EdsMeetingMinutes mm where  mm.intNumber is not null order by mm.intNumber desc ");
    }

    @Override
    public boolean isProductNumberExists(String number, Integer productID) {
        if (productID != null) {
            return find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.productNumber = ? and p.objectID != ?", number.trim(), productID).size() > 0;
        } else {
            return find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.productNumber = ?", number.trim()).size() > 0;
        }
    }
}