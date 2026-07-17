package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08-Jul-2009
 * Time: 19:09:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("campaignManager")
public class CampaignManagerImpl extends BaseManager<EdsCampaign> implements CampaignManager {

    public CampaignManagerImpl() {
        super(EdsCampaign.class);
    }

    DateFormat formatFull = new SimpleDateFormat("MMM d, yyyy");
    DateFormat formatMonth = new SimpleDateFormat("MMMM, yyyy");
    DateFormat formatYM = new SimpleDateFormat("yyyy-MM");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    private String getSqlWhereCampaignList(StringBuilder sql, ListingFilterParameter fp) {
        Date selectedDate = null;
        String sDate = "";
        sql.append(" where ");
        sql.append(ServerUtils.checkForDeleted("c.deleted"));
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and lower(c.name) like '" + fp.getSqlSearchKey() + "' ");
        }
        if (fp != null && fp.getStatusValues() != null && !"".equals(fp.getStatusValues())) {
            sql.append(" and c.status = " + fp.getStatusValues());
        }
        //search by date
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && (fp.getSearchType() == 0 || fp.getSearchType() == 1)) {
            if (!fp.getGroupByName().contains(" Without ")) {
                try {
                    selectedDate = formatFull.parse(fp.getGroupByName());
                    sDate = format2.format(selectedDate);
                } catch (ParseException exFull) {
                    try {
                        selectedDate = formatMonth.parse(fp.getGroupByName());
                        sDate = formatYM.format(selectedDate); //for year-month ex October, 2010
                    } catch (ParseException exMonth) {
                        try {
                            sDate = fp.getGroupByName(); //For year ex.2008
                        } catch (Exception exYear) {
                            exYear.printStackTrace();
                        }
                    }
                }
            } else {
                sDate = "";
            }
            if (fp.getSearchType() == 0) {
                sql.append(sDate.equals("") ? " and c.startDate is null " : " and to_char(c.startDate, 'yyyy-mm-dd') like '" + sDate + "%' ");
            } else if (fp.getSearchType() == 1) {
                sql.append(sDate.equals("") ? " and c.endDate is null " : " and to_char(c.endDate, 'yyyy-mm-dd') like '" + sDate + "%' ");
            }
        }

        //search by status
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 2) {
            if ("Without Status".equals(fp.getGroupByName())) {
                sql.append(" and c.status is null ");
            } else {
                sql.append(" and status.name='" + fp.getGroupByName() + "' ");
            }
        }

        //search by type
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 3) {
            if ("Without Type".equals(fp.getGroupByName())) {
                sql.append(" and c.type is null ");
            } else {
                sql.append(" and type.name='" + fp.getGroupByName() + "' ");
            }
        }

        //search by Assignee
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 4) {
            if ("Without Assignee".equals(fp.getGroupByName())) {
                sql.append(" and c.assignee is null ");
            } else {
                String firstName = fp.getGroupByName().contains(" ") ? fp.getGroupByName().substring(0, fp.getGroupByName().indexOf(" ")) + "" : "";
                String lastName = fp.getGroupByName().contains(" ") ? fp.getGroupByName().substring(fp.getGroupByName().indexOf(" ") + 1) : fp.getGroupByName();
                sql.append(" and assignee.firstName = '" + firstName + "' and assignee.lastName = '" + lastName + "'");
            }
        }

        return sql.toString();
    }

    public List<EdsCampaign> getCampaignList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (fp != null && fp.isLookUp()) { //Look Up uchun faqat statusi COMPLATE bumaganlani olib chiqadi
            sql.append("SELECT c.id,c.name FROM ").append(getCompanyId()).append(".campaign c ");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r ON c.status = r.id ");
            sql.append("WHERE c.deleted is not true ");
            sql.append("AND (c.status is null OR r.code not in ('" + EdsCampaign.COMPLATE + "','" + EdsCampaign.CS_INACTIVE + "')) ");
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and lower(c.name) like '%").append(fp.getSqlSearchKey()).append("%' ");
            }
            sql.append("ORDER BY c.name ");
            List<EdsCampaign> list = new ArrayList<>();
            List<Object[]> campaigns = findNativeLimited(sql.toString() + " offset " + fp.getStart(), fp.getLimit());
            if (campaigns != null && campaigns.size() > 0) {
                for (Object[] campaign : campaigns) {
                    EdsCampaign temp = new EdsCampaign();
                    temp.setObjectID((Integer) campaign[0]);
                    temp.setName((String) campaign[1]);
                    list.add(temp);
                }
            }
            return list;
        } else {
            sql.append("select c from EdsCampaign as c left join c.assignee as assignee left join c.status as status left join c.type as type ");
                getSqlWhereCampaignList(sql, fp);
            if (StringUtils.isNotBlank(fp.getSortField())) {
                    String code = fp.getSortField();
                    if (CampaignItem.OWNER.equals(code)) {
                        sql.append(" ORDER BY assignee.firstName ");
                    } else if (CampaignItem.END_DATE.equals(code)) {
                        sql.append(" ORDER BY c.endDate ");
                    } else if (CampaignItem.START_DATE.equals(code)) {
                        sql.append(" ORDER BY c.startDate ");
                    } else if (CampaignItem.STATUS.equals(code)) {
                        sql.append(" ORDER BY status.sorder ");
                    } else if (CampaignItem.TYPE.equals(code)) {
                        sql.append(" ORDER BY type.sorder ");
                    } else if (CampaignItem.NAME.equals(code)) {
                        sql.append(" ORDER BY c.name ");
                    } else {
                        sql.append(" ORDER BY c.objectID ");
                    }
                    sql.append(!fp.isAscending() ? " desc " : " ");
                } else {
                    sql.append(" ORDER BY c.objectID ");
                }
            return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        }
    }

    public EdsCampaign getCampaignByName(String name) {
        return (EdsCampaign) findSingle("select a from EdsCampaign a where a.name=?", name);
    }

    @Override
    public Integer getCampaignListCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(c) from EdsCampaign as c ");
        getSqlWhereCampaignList(sql, fp);
        return Math.toIntExact((Long) findSingle(sql.toString()));
    }

    public List<String> getCampaignNames() {
        return (List<String>) findNative("select rtrim(ltrim(lower(a.name))) from " + getCompanyId() + ".campaign a where a.name is not null and a.deleted is not true");
    }

    @Override
    public List<Object[]> getList() {
        return findNative("select lower(c.name), c.id from " + getCompanyId() + ".campaign c where deleted is not true");
    }

    @Override
    public void setCampaignsDeletedTrue(String ids) {
        update("update EdsCampaign set deleted =true where id in (" + ids + ")");
    }
}
