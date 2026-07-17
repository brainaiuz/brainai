package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:13:19
 */
@Repository("locationManager")
public class LocationManagerImpl extends BaseManager<EdsLocation> implements LocationManager {
    public LocationManagerImpl() {
        super(EdsLocation.class);
    }

    public List<EdsLocation> list(ListingFilterParameter fp) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("SELECT l FROM EdsLocation l WHERE l.deleted<>true \n");

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sqlQuery.append(" AND (lower(l.country.name) like'").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.city) like'").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.fax) like'").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.email) like'").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.phone) like'").append(fp.getSqlSearchKey()).append("'\n");
            sqlQuery.append(" OR lower(l.zipCode) like'").append(fp.getSqlSearchKey()).append("')\n");
        }

        sqlQuery.append("ORDER BY l.country.name ASC");

        return find(sqlQuery.toString());
    }

    @Override
    public SelectItem[] getLocationsAsSelectItems(ListingFilterParameter listingFilterParameter) {
        List<EdsLocation> locations = getLocations(listingFilterParameter);
        if (locations != null && locations.size() > 0) {
            List<SelectItem> selectItems = new ArrayList<>();
            for (EdsLocation location : locations) {
                selectItems.add(location.getAsSelectItem());
            }
            return selectItems.toArray(new SelectItem[0]);
        }
        return new SelectItem[0];
    }

    @Override
    public SelectItem[] getLocationsAsSelecItem() {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT l.* FROM ").append(getCompanyId()).append(".location l ");
        sqlQuery.append("LEFT JOIN ").append(getCompanyId()).append(".reference_locale rl ON rl.id = l.localeid ");
        sqlQuery.append("WHERE l.deleted is not true ");
        sqlQuery.append(" ORDER BY l.name ASC");

        List<EdsLocation> locations = findNative(sqlQuery.toString(), EdsLocation.class);
        if (locations.isEmpty()) {
            return new SelectItem[]{};
        }

        List<SelectItem> selectItems = new ArrayList<>();
        for (EdsLocation location : locations) {
            selectItems.add(location.getAsSelectItem());
        }
        return selectItems.toArray(new SelectItem[0]);

    }

    @Override
    public List<Object[]> getList() {
        return findNative("select lower(l.name), l.id from " + getCompanyId() + ".location l where l.deleted is not true");
    }

    @Override
    public List<Object[]> getListByCode() {
        return findNative("select lower(l.code), l.id from " + getCompanyId() + ".location l where l.deleted is not true");
    }

    @Override
    public List<EdsLocation> getLocations(ListingFilterParameter fp) {
        EdsUser user = getUser();
        StringBuilder sqlQuery = new StringBuilder();
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en" -> nameLocale += "COALESCE (rl.english, l.name) ";
            case "ru" -> nameLocale += "COALESCE (rl.russian, l.name) ";
            case "uz" -> nameLocale += "COALESCE (rl.uzbek, l.name) ";
            case "ar" -> nameLocale += "COALESCE (rl.arabic, l.name) ";
            default -> nameLocale += "l.name";
        }
        sqlQuery.append("SELECT l.* FROM ").append(getCompanyId()).append(".location l \n");
        sqlQuery.append("LEFT JOIN ").append(getPublic()).append(".region r ON (l.stateid = r.id) \n");
        sqlQuery.append("LEFT JOIN ").append(getPublic()).append(".country c ON (l.countryid = c.id) \n");
        sqlQuery.append("LEFT JOIN ").append(getCompanyId()).append(".reference_locale rl ON rl.id = l.localeid ");
        sqlQuery.append("WHERE l.deleted is not true \n");
        if (fp.getParentID() != null) {
            sqlQuery.append(" and (l.parentid is null or l.parentid != ").append(fp.getParentID().toString()).append(") ");
        }
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_LOCATION)) {
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_OWN_LOCATION)) {
                sqlQuery.append(" and '").append(user.getObjectID()).append("' = ANY(string_to_array(ownersId, ','))");
            } else {
                return new ArrayList<>();
            }
        }
        if (fp.getSearchKey() != null && !fp.getSearchKey().isEmpty() && !fp.getSearchKey().isBlank()) {
            sqlQuery.append(" AND (lower(c.name) like'").append(fp.getSqlSearchKey()).append("'");
            sqlQuery.append(" OR lower(").append(nameLocale).append(") like'").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.code) like'%").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.city) like'%").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.fax) like'%").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.email) like'%").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(l.phone) like'%").append(fp.getSqlSearchKey()).append("' ");
            sqlQuery.append(" OR lower(r.name) like'").append(fp.getSqlSearchKey()).append("') ");
        }

        if (fp.getSortField() != null) {
            sqlQuery.append(" ORDER BY ");
            if (CompLocationRpc.NAME.equals(fp.getSortField())) {
                sqlQuery.append("l.name ");
            } else if (CompLocationRpc.CODE.equals(fp.getSortField())) {
                sqlQuery.append("l.code ");
            } else if (CompLocationRpc.COUNTRY_NAME.equals(fp.getSortField())) {
                sqlQuery.append("c.name ");
            } else if (CompLocationRpc.STATE_NAME.equals(fp.getSortField())) {
                sqlQuery.append("r.name ");
            } else if (CompLocationRpc.CITY_NAME.equals(fp.getSortField())) {
                sqlQuery.append("l.city ");
            } else if (CompLocationRpc.EMAIL.equals(fp.getSortField())) {
                sqlQuery.append("l.email ");
            } else if (CompLocationRpc.PHONE_NUMBER.equals(fp.getSortField())) {
                sqlQuery.append("l.phone ");
            } else if (CompLocationRpc.FAX.equals(fp.getSortField())) {
                sqlQuery.append("l.fax ");
            } else if (CompLocationRpc.ZIP_CODE.equals(fp.getSortField())) {
                sqlQuery.append("l.zipCode ");
            } else {
                sqlQuery.append("l.name ");
            }

            if (!fp.isAscending()) {
                sqlQuery.append(" DESC ");
            }
        } else {
            sqlQuery.append(" ORDER BY l.name ASC");
        }

        return findNative(sqlQuery.toString(), EdsLocation.class);
    }

    public List<EdsEmployee> getLocationEmployee(Integer locationId) {
        return find("select e from EdsEmployee e " +
                "where e.location.objectID=?  and e.deleted<>true", locationId);
    }

    public List<EdsProject> getProjectLocation(Integer locationID) {
        return find("select p from EdsProject p left join p.projectLocation pl " +
                "where pl.objectID = ? and p.deleted<>true", locationID);
    }

    public List<EdsHoliday> getHolidayLocation(Integer locationID) {
        return find("select h from EdsHoliday h join h.locations ls " +
                "where ls.objectID= ?", locationID);
    }

    public List<EdsUser> getUserLocation(Integer locationID) {
        return find("select u from EdsUser u left join u.location ul " +
                "where ul.objectID=? and u.deleted<>true", locationID);
    }

    public int getLocationUsedSize(Integer locationID) {
        int size = 0;
        int employeeLocationSize = getLocationEmployee(locationID).size();
        int projectLocationSize = getProjectLocation(locationID).size();
        int holidayLocationSize = getHolidayLocation(locationID).size();
        int userLocationSize = getUserLocation(locationID).size();
        size = employeeLocationSize + projectLocationSize + holidayLocationSize + userLocationSize;
        return size;
    }

    public EdsLocation getLocationByName(String locationName) {
        return (EdsLocation) findSingle("select loc from EdsLocation loc where loc.city=? and loc.deleted<>true", locationName);
    }

    @Override
    public EdsLocation getLocationByName(String locationName, Integer locationID) {
        if (locationName != null) {
            locationName = locationName.replace("'", "''");
        }
        return (EdsLocation) findSingle("select loc from EdsLocation loc where loc.deleted<>true and loc.name='" + locationName + "'" + (locationID != null ? " and loc.objectID<>" + locationID : ""));
    }

    @Override
    public EdsLocation getLocationByCode(String code, Integer id) {
        return (EdsLocation) findSingle("select loc from EdsLocation loc where loc.deleted<>true and loc.code='" + code + "'" + (id != null ? " and loc.objectID<>" + id : ""));
    }

    @Override
    public Integer getLastIntNumber() {
        return (Integer) findSingle("select loc.intNumber from EdsLocation loc where (loc.deleted = false or loc.deleted is null) and loc.intNumber is not null order by loc.intNumber desc");
    }

    @Override
    public EdsLocation getRootLocation() {
        return (EdsLocation) findSingle("select l from EdsLocation l where l.parent is null order by l.id asc ");
    }
}
