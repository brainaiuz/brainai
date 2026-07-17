package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("positionManager")
public class PositionManagerImpl extends BaseManager<EdsPosition> implements PositionManager {

    public PositionManagerImpl() {
        super(EdsPosition.class);

    }

    public List<EdsPosition> list() {
        return find("select p from EdsPosition p where p.deleted is null or p.deleted is false order by p.name");
    }

    public EdsPosition getPositionForEdit(Integer posId) {
        return (EdsPosition) findSingle("select p from EdsPosition p where p.objectID =?", posId);
    }

    public List<EdsPosition> listCompanyPositions() {
        return find("select distinct p from EdsPosition p where p.deleted<>true");
    }

    @Override
    public List<EdsPosition> getPositionListByStatusID(Integer status, ListingFilterParameter fp) {
        if (fp == null) fp = new ListingFilterParameter();
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.* FROM ");
        query.append(getCompanyId());
        query.append(".position p ");
        query.append("WHERE p.isdeleted IS NOT TRUE ");
        query.append(" AND (p.status IS NULL OR p.status != ");
        query.append(status);
        query.append(") ");

        if (fp.getDepartmentId() != null){
            query.append(" AND p.department =").append(fp.getDepartmentId()).append(" ");
        }

        String searchInput = fp.getSqlSearchKey();
        if (searchInput != null && !searchInput.trim().isEmpty()) {
            query.append(" AND LOWER(p.name) LIKE '%");
            query.append(searchInput.toLowerCase());
            query.append("%' ");

            query.append("ORDER BY CASE WHEN LOWER(p.name) = '");
            query.append(searchInput.toLowerCase());
            query.append("' THEN 0 ");
            query.append("WHEN LOWER(p.name) LIKE '");
            query.append(searchInput.toLowerCase());
            query.append("%' THEN 1 ");
            query.append("ELSE 2 END, p.name ");
        } else {
            query.append("ORDER BY p.name ");
        }

        Integer limit = fp.getLimit();
        if (limit == null || limit != 0) {
            query.append(" LIMIT ");
            query.append(limit != null ? limit : 20);
        }

        return findNative(query.toString(), EdsPosition.class);
    }

    public List<EdsPosition> getPositionList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.* ");
        sql.append(", (select count(e1) from " + getCompanyId() + ".employee e1 " +
                "left join " + getCompanyId() + ".myuser mu on e1.id=mu.id where e1.positionid=p.id and mu.deleted<>true) noofemployee");

        sql.append(" from " + getCompanyId() + ".position as p ");
        if (!Strings.isNullOrEmpty(fp.getEmployeeIDs())) {
            sql.append("left join " + getCompanyId() + ".employee emp on emp.positionid=p.id ");
        }
        sql.append("left join " + getCompanyId() + ".positioncustomfields cf on cf.id = p.customfieldsid ");
        sql.append("left join " + getCompanyId() + ".reference_locale rl ON rl.id = p.localeid ");
        sql.append(" and p.isDeleted <> true ");
        getSqlWhere(fp, sql);

        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            sql.append(" ORDER BY ");

            if (PositionItem.POSITION_CODE.equals(fp.getSortField())) {
                sql.append(" p.numberData ");
            }
            else if (PositionItem.POSITION_TITLE.equals(fp.getSortField())) {
                sql.append(" p.name ");
            }
            else if (PositionItem.STATUS.equals(fp.getSortField())) {
                sql.append(" p.status ");             }

            else if (PositionItem.CREATED_BY.equals(fp.getSortField())) {
                sql.append(" p.created_by ");
            }
            else if (PositionItem.CREATED_DATE.equals(fp.getSortField())) {
                sql.append(" p.created_date ");
            }
            else if (PositionItem.MODIFIED_BY.equals(fp.getSortField())) {
                sql.append(" p.modified_by ");
            }
            else if (PositionItem.MODIFIED_DATE.equals(fp.getSortField())) {
                sql.append(" p.modified_date ");
            }

            else if (PositionItem.POSITION_COUNT.equals(fp.getSortField())) {
                sql.append(" noofemployee ");
            }

            else {
                sql.append("p.name");
            }
            if (fp.isAscending()) {
                sql.append(" ASC ");
            } else {
                sql.append(" DESC ");
            }
        }
        return findNative(sql.toString(), EdsPosition.class);
    }

    @Override
    public ArrayList<EdsPosition> getPostionsByLocation(Integer locationId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getCompanyId()).append(".position where locationid = ").append(locationId).append(" and isdeleted is not true order by name");
        return (ArrayList<EdsPosition>) findNative(sql.toString(), EdsPosition.class);
    }

    public SelectItem[] getPositionListAsSelectItem(ListingFilterParameter fp) {
        List<EdsPosition> positionList = getPositionList(fp);

        if (positionList == null || positionList.size() == 0) {
            return new SelectItem[0];
        } else {
            SelectItem[] result = new SelectItem[positionList.size()];
            int i = 0;
            for (EdsPosition position : positionList) {
                int id = position.getObjectID();
                String name = position.getName();
                result[i] = new SelectItem(id, name);
                i++;
            }
            return result;

        }
    }

    private void getSqlWhere(ListingFilterParameter fp, StringBuilder sql) {
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en" -> nameLocale += "COALESCE (rl.english, p.name) ";
            case "ru" -> nameLocale += "COALESCE (rl.russian, p.name) ";
            case "uz" -> nameLocale += "COALESCE (rl.uzbek, p.name) ";
            case "ar" -> nameLocale += "COALESCE (rl.arabic, p.name) ";
            default -> nameLocale += "p.name";
        }

        sql.append(" where ");
        sql.append(" (p.isdeleted<>true or p.isdeleted is null) ");
        if (!Strings.isNullOrEmpty(fp.getEmployeeIDs())) {
            sql.append(" and emp.id in (" + fp.getEmployeeIDs() + ") ");
        }
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" and (lower(" + nameLocale + ") like '" + "%" + fp.getSqlSearchKey().toLowerCase() + "%' ");
            sql.append("or lower(p.numberData) like '%" + fp.getSqlSearchKey().toLowerCase() + "%' ");
            for (int i = 1; i < 51; i++) {
                sql.append("or lower(cf.string_value" + i + ") like '%" + fp.getSqlSearchKey().toLowerCase() + "%' ");
            }
            sql.append(")");
        }
        if (fp.getDepartmentId() != null) {
            sql.append(" and p.department = ").append(fp.getDepartmentId());
        }
        if (fp.getLocationId() != null) {
            sql.append(" and p.locationid = ").append(fp.getLocationId());
        }
        if (fp.getStatusID() != null) {
            sql.append(" and p.status = ").append(fp.getStatusID());
        }
        if (fp.getType() != null) {
            sql.append(" and p.type = ").append(fp.getType());
        }
    }

    public void deletePosition(EdsPosition position) {
        update("update EdsPosition pos set pos.deleted=true where pos=? and (pos.deleted<>true or pos.deleted is null)", position);
    }

    @Override
    public Integer getPositionListCount(ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(p.id) from " + getCompanyId() + ".position as p ");
        sql.append("left join " + getCompanyId() + ".positioncustomfields cf on cf.id = p.customfieldsid ");
        sql.append("left join " + getCompanyId() + ".reference_locale rl ON p.localeid = rl.id");
        getSqlWhere(filterParametrs, sql);
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public EdsPosition getByName(String name) {
        return (EdsPosition) findSingle("select p from EdsPosition p where TRIM(LOWER(p.name)) =?", name.toLowerCase().trim());
    }

    @Override
    public EdsPosition getPositionByName(String name, Integer objectID) {
        if (name != null) {
            name = name.replace("'", "''").trim();
        }
        return (EdsPosition) findSingle("select pos from EdsPosition pos where pos.deleted<>true and pos.name='" + name + "'" + (objectID != null ? " and pos.objectID<>" + objectID : ""));
    }

    @Override
    public EdsPosition getPositionByCode(String code, Integer objectID) {
        return (EdsPosition) findSingle("select pos from EdsPosition pos where pos.deleted<>true and pos.numberData=?" + (objectID != null ? " and pos.objectID<>" + objectID : ""), code);
    }

    @Override
    public boolean isPositionNumberExist(String numberString, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select e.intNumber from EdsPosition e where (e.deleted = false or e.deleted is null)  " + " and e.numberData = ? and e.objectID <> ? ", numberString, objectID);
        } else {
            numberList = find("select e.intNumber from EdsPosition e where (e.deleted = false or e.deleted is null)  " + " and e.numberData = ?", numberString);
        }
        return numberList != null && numberList.size() > 0;
    }

    @Override
    public Integer getPositionLastIntNumber() {
        return (Integer) findSingle("select pos.intNumber from EdsPosition pos where (pos.deleted = false or pos.deleted is null) and pos.intNumber is not null order by pos.intNumber desc");
    }

    @Override
    public Integer getPositionsSizeByNameForValidation(String positionName, Integer locationId, Integer objectId) {
        StringBuilder sql = new StringBuilder("select count(*) from ")
                .append(getCompanyId())
                .append(".position where isDeleted is not true and name = ?");
        List<Object> params = new ArrayList<>();
        params.add(positionName);
        if (locationId != null) {
            sql.append(" and locationid = ?");
            params.add(locationId);
        } else {
            sql.append(" and locationid is null");
        }
        if (objectId != null) {
            sql.append(" and id <> ?");
            params.add(objectId);
        }
        BigInteger result = (BigInteger) findNativeSingle(sql.toString(), params.toArray());
        return result.intValue();
    }

    @Override
    public Map<Integer, Integer> getPositionHeadCountMap() {
        Map<Integer, Integer> positionHeadCountMap = new HashMap<>();
        List<Object[]> objects = findNative("select distinct p.id , (select count(e1) from " + getCompanyId() + ".employee e1 left join " + getCompanyId() + ".myuser mu on e1.id=mu.id where e1.positionid=p.id and mu.deleted<>true) count from " + getCompanyId() + ".position as p where p.isDeleted is not true");
        for (Object[] object : objects) {
            Integer id = (Integer) object[0];
            BigInteger count = (BigInteger) object[1];
            positionHeadCountMap.put(id, count.intValue());
        }
        return positionHeadCountMap;
    }

    @Override
    public List<EdsPosition> getPositionsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsPosition p ");
        sql.append(" where  ").append(ServerUtils.checkForDeleted("p.deleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sql.append(" and p.lastUpdateTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sql.append(" and p.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sql.append(" order by p.objectID ");

        return findIntervalByNamedParams(sql.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getPositionIdsByIds(String ids) {
        return find("SELECT c.objectID FROM EdsPosition c WHERE c.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("c.deleted"));
    }

    @Override
    public List<EdsPosition> getPositionWithDistinctName() {
        String nameLocale = "";
        StringBuilder sql = new StringBuilder();
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en":
                nameLocale += "COALESCE (rl.english, p.name) ";
                break;
            case "ru":
                nameLocale += "COALESCE (rl.russian, p.name) ";
                break;
            case "uz":
                nameLocale += "COALESCE (rl.uzbek, p.name) ";
                break;
            case "ar":
                nameLocale += "COALESCE (rl.arabic, p.name) ";
                break;
            default:
                nameLocale += "p.name";
                break;
        }
        sql.append("SELECT DISTINCT ON (" + nameLocale + ") * FROM " + getCompanyId() + ".position p left join " + getCompanyId() + ".reference_locale rl on rl.id = p.localeid WHERE isdeleted IS NOT TRUE ");

        return (List<EdsPosition>) findNative(sql.toString(), EdsPosition.class);
    }

    @Override
    public List<Integer> getPositionIdsWithLimit(Integer start, Integer limit) {
        return findInterval("select c.objectID from EdsPosition c where " + ServerUtils.checkForDeleted("c.deleted"), start, limit);
    }

    @Override
    public List<EdsPosition> getPositionListByName(String name) {
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en":
                nameLocale += "rl.english";
                break;
            case "ru":
                nameLocale += "rl.russian";
                break;
            case "uz":
                nameLocale += "rl.uzbek";
                break;
            case "ar":
                nameLocale += "rl.arabic";
                break;
            default:
                nameLocale += "p.name";
                break;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ")
                .append(getCompanyId())
                .append(".position p ")
                .append("LEFT JOIN ")
                .append(getCompanyId())
                .append(".reference_locale rl ON p.localeid = rl.id ")
                .append("WHERE isdeleted IS NOT TRUE AND ((")
                .append("p.localeid IS NULL AND p.name = '").append(name).append("')")
                .append("OR")
                .append("(p.localeid IS NOT NULL AND ").append(nameLocale).append("= '").append(name).append("'));");

        return findNative(sql.toString(), EdsPosition.class);
    }

    @Override
    public ArrayList<EdsPosition> getPositionListByDepartment(Integer departmentId) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(getCompanyId()).append(".position ");
        query.append(" where isdeleted IS NOT TRUE ");
        query.append(" and department = ").append(departmentId);

        return (ArrayList<EdsPosition>) findNative(query.toString(), EdsPosition.class);
    }

    @Override
    public ArrayList<SelectItem> getReferenceRelatedPositions(Integer referenceId) {
        ArrayList<SelectItem> positionList = new ArrayList<>();
        List<Object[]> items = (List<Object[]>) findNative("select id,numberdata from " + getCompanyId() + ".position where isdeleted is not true and  positionNameId  = " + referenceId);
        for (Object[] item : items) {
           positionList.add(new SelectItem((Integer) item[0],(String) item[1]));
        }
        return positionList;
    }

    public List<Integer> getCompanyDeletedPositionsForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsPosition ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }
}
