package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsEmbassy;
import com.edatasite.workforce.core.domain.EdsTimeZone;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository("countryManager")
public class CountryManagerImpl extends BaseManager<EdsCountry> implements CountryManager {
    @Autowired
    private TimeZoneManager zoneManager;

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public CountryManagerImpl() {
        super(EdsCountry.class);
    }

    public List<EdsCountry> list() {
        return find("select c from EdsCountry c where isActive = true  order by c.name");
    }

    public List<EdsCountry> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c from EdsCountry c where 1=1 ");
        String name = "";
        if (fp.isActive()) {
            sql.append(" and isActive = true");
        }
        if (fp != null && fp.getSqlSearchKey() != null) {
            if (fp.getLanguage() != null) {
                switch (fp.getLanguage()) {
                    case "ru" -> name = "ru";
                    case "uz" -> name = "uz";
                }
            }
            sql.append(" and lower(" + name + "name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        sql.append(" order by c.name ");
        return fp.getStart() == null || fp.getLimit() == null ? find(sql.toString()) : findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public EdsCountry getCountryByCode(String s) {
        return (EdsCountry) findSingle("select c from EdsCountry c where isActive = true and c.code=?", s);
    }

    public List<EdsCountry> getCountryByCodeIn(List<String> codes) {

        if (CollectionUtils.isEmpty(codes)) {
            return List.of();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("CODES", codes);
        return (List<EdsCountry>) findByNamedParams("select c from EdsCountry c where isActive = true and c.code IN :CODES", map);
    }


    public EdsCountry getCountryByCallCode(String callCode) {
        if (ServerUtils.isNullOrEmpty(callCode)) {
            return null;
        }
        if (!callCode.startsWith("+")) {
            callCode = "+" + callCode;
        }
        return (EdsCountry) this.findSingle("select c from EdsCountry c where  c.telCode=?", callCode);
    }

    //Don't use this method, instead get country by code!
    @Deprecated
    public EdsCountry getCountryByName(String countryName) {
        if (StringUtils.isBlank(countryName)) {
            return null;
        }
        return (EdsCountry) findSingle("from EdsCountry where isActive = true and lower(name) =?", countryName.toLowerCase());
    }

    @Override
    public String getCountryTimeZoneAndPhoneCode(EdsCountry country) {
        if (country == null) {
            return null;
        }
        final DateFormat format2 = new SimpleDateFormat("hh:mma");
        List<EdsCountryZone> zones = zoneManager.getCountryZones(country);
        EdsTimeZone zone = zones != null && zones.size() != 0 && zones.get(0).getZone() != null ? zones.get(0).getZone() : null;
        Date countryDate = new Date();
        if (country.getTelCode() != null && !country.getTelCode().equals("")) {
            return " (" + country.getTelCode() + "," + (zone != null ? " " + format2.format(ServerUtils.getCountryDate(countryDate, zone.getZoneID())) : "") + ")";
        } else {
            return " (" + (zone != null ? format2.format(ServerUtils.getCountryDate(countryDate, zone.getZoneID())) : "") + ")";
        }
    }

    @Override
    public SelectItem[] getCountryList(ListingFilterParameter listingFilterParameter) {
        StringBuilder sql = new StringBuilder();
        String lang = listingFilterParameter.getLanguage();
        sql.append("select id," + ("ru".equals(lang) ? "coalesce(runame,name) as name" : "uz".equals(lang) ? "coalesce(uzname,name) as name" : "name") + "  from " + getPublic() + ".country where isActive = true ");
        if (listingFilterParameter != null && listingFilterParameter.getSqlSearchKey() != null) {
            sql.append(" and lower(name) like '").append(listingFilterParameter.getSqlSearchKey()).append("'");
        }
        sql.append(" order by name");
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class)).toArray(new SelectItem[]{});
    }

    @Override
    public void deleteEmbasies(EdsCountry country) {
        if (country != null && country.getObjectID() != null) {
            updateNative("update " + getCompanyId() + ".embassy set deleted  = true where countryid = " + country.getObjectID());
        }
    }

    @Override
    public EdsEmbassy getEmbassyById(Integer id) {
        return (EdsEmbassy) findSingle("from EdsEmbassy where  objectID=?", id);
    }

    @Override
    public void createOrUpdateEmbassy(EdsEmbassy embassy) {
        if (embassy.getObjectID() != null) {
            updateNativeByParams("update " + getCompanyId() + ".embassy set name=?,code=?,description=?,sorder=?,deleted=?,countryId=?  where id = " + embassy.getObjectID(),
                    embassy.getName(), embassy.getCode(), embassy.getDescription(), embassy.getSorder(), embassy.isDeleted(), embassy.getCountry().getObjectID());
        } else {
            updateNativeByParams("insert into " + getCompanyId() + ".embassy (name,code,description,sorder,countryId) values (?,?,?,?,?) ",
                    embassy.getName(), embassy.getCode(), embassy.getDescription(), embassy.getSorder(), embassy.getCountry().getObjectID());
        }
    }

    @Override
    public List<EdsEmbassy> listEmbasies() {
        return find("select r from EdsEmbassy r where r.deleted=false order by r.name");
    }

    public void setZoneManager(TimeZoneManager zoneManager) {
        this.zoneManager = zoneManager;
    }
}
