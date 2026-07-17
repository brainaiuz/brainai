package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsTimeZone;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository("regionManager")
public class RegionManagerImpl extends BaseManager<EdsRegion> implements RegionManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public RegionManagerImpl() {
        super(EdsRegion.class);
    }

    public List<EdsRegion> list() {
        return find("select r from EdsRegion r order by r.name");
    }

    public List<EdsRegion> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r from EdsRegion r where r.country.objectID = ").append(fp.getCountryId());
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and lower(r.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        sql.append(" order by r.name ");
        return find(sql.toString());
    }

    public List<EdsRegion> listByCountry(Integer countryId) {
        return find("select r from EdsRegion r where r.country.objectID =? order by r.name", countryId);
    }

    public SelectItem[] listRegionByCountry(Integer countryId, ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        String language = filterParameter.getLanguage();
        sql.append("select id," + ("ru".equals(language) ? "coalesce(runame,name) as name" : ("uz".equals(language)) ? "coalesce(uzname, name) as name" : "name") + "  from region where countryid= " + countryId);
        if (filterParameter != null && filterParameter.getSqlSearchKey() != null) {
            sql.append(" and lower(name) like '").append(filterParameter.getSqlSearchKey()).append("'");
        }
        sql.append(" order by name");
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class)).toArray(new SelectItem[]{});
    }

    public EdsRegion getRegionByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (EdsRegion) findSingle("select a from EdsRegion a where lower(a.name)=?", name.toLowerCase());
    }

    @Override
    public String getStateTimeZoneAndPhoneCode(EdsCountry country, EdsRegion state) {
        final DateFormat format2 = new SimpleDateFormat("hh:mma");
        EdsTimeZone zone = state.getTimeZone();
        Date countryDate = new Date();
        if (country != null && country.getTelCode() != null && !country.getTelCode().equals("")) {
            return " (" + country.getTelCode() + "," + (zone != null ? " " + format2.format(ServerUtils.getCountryDate(countryDate, zone.getZoneID())) : "") + ")";
        } else {
            return " (" + (zone != null ? format2.format(ServerUtils.getCountryDate(countryDate, zone.getZoneID())) : "") + ")";
        }
    }

    public List<EdsRegion> listBySaudiArabia() {
        String query = "";
        query += "select r from EdsRegion r ";
        query += "where r.country.code=? ";
        query += "and r.country.name=?";
        return find(query, "SA", "Saudi Arabia");
    }

    @Override
    public EdsRegion getRegion(Integer countryId, String state_code) {
        return (EdsRegion) findSingle("select r from EdsRegion r where r.country.objectID =? AND r.code =? order by r.name", countryId, state_code);
    }
}
