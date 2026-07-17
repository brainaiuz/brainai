package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.LocalizationManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: Jan 8, 2008
 * Time: 5:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("localizationManager")
public class LocalizationManagerImpl extends BaseManager<EdsLocalization> implements LocalizationManager {

    public LocalizationManagerImpl() {
        super(EdsLocalization.class);
    }


    @Override
    public List<EdsLocalization> list(ListingFilterParameter filter, String code, String untranslatedField) {
        StringBuilder sqlQuery = new StringBuilder();
        String key = "";
        if (filter.getSearchKey() != null && !"".equals(filter.getSearchKey().trim())) {
            key = " (l.code like '%" + filter.getSearchKey() + "%' or l.en like '%" + filter.getSearchKey() + "%' or l.ru like '%" + filter.getSearchKey() + "%' or " +
                    "  l.arabic like '%" + filter.getSearchKey() + "%' or  l.fr like '%" + filter.getSearchKey() + "%' or " +
                    "  l.ger like '%" + filter.getSearchKey() + "%' or  l.ita like '%" + filter.getSearchKey() + "%' or " +
                    "  l.neder like '%" + filter.getSearchKey() + "%' or  l.por like '%" + filter.getSearchKey() + "%' or l.defaulttext like '%" + filter.getSearchKey() + "%' or" +
                    "  l.spa like '%" + filter.getSearchKey() + "%' or  l.thai like '%" + filter.getSearchKey() + "%' or l.turkish like '%" + filter.getSearchKey() + "%') ";
        }
        String untranslateQuery = "";
        if (!"".equals(untranslatedField)) {
            untranslateQuery = " (l." + untranslatedField + "='' or l." + untranslatedField + " is null)";
        } else {
            untranslateQuery = " (l.defaulttext='' or l.en='' or l.ru='' or l.arabic='' or l.fr='' or l.ita='' or l.neder='' or l.por='' or l.spa='' or l.thai='' or l.turkish='' or l.ger='' or l.defaulttext is null or l.en is null or l.ru is null or l.arabic is null or l.fr is null or l.ita is null or l.neder is null or l.por is null or l.spa is null or l.thai is null or l.turkish is null or l.ger is null) ";
        }
        String codeString = "";
        if (!"".equals(code)) {
            codeString = " l.propertycode='" + code + "'";
            if (!"".equals(key)) {
                codeString = codeString + " and " + key;
            }
        } else if (!"".equals(key)) {
            codeString = key;
        }
        String condition = " where l.isActive=true and " + untranslateQuery + (!"".equals(codeString) ? " and " + codeString : "");
        String orderBy = " order by l.id desc ";
        if (filter.getSortField() != null) {
            orderBy = " order by l." + filter.getSortField();
            if (filter.isAscending()) {
                orderBy = orderBy + " asc";
            } else {
                orderBy = orderBy + " desc";
            }
        }
        sqlQuery.append("select * from " + getPublic() + ".localization l " + condition + " " + orderBy);
        return findNative(sqlQuery.toString(), EdsLocalization.class);
    }

    @Override
    public List<EdsLocalization> list() {  // Bu zaprosi uzgartirishdan oldin Fatxulla Nigmatjonovga bildirila
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM " + getPublic() + ".localization l WHERE l.isActive=TRUE ORDER BY l.propertycode, l.id");
        return findNative(sqlQuery.toString(), EdsLocalization.class);
    }

    @Override
    public List<EdsLocalization> list(String property) {
        return findByNamedParams("select t from EdsLocalization t where t.isActive = true and t.propertyCode=:code", preparing(new Entry("code", property)));
    }

    @Override
    public EdsLocalization getLocalization(Integer id) {
        return (EdsLocalization) findSingle("SELECT loc FROM EdsLocalization loc WHERE loc.isActive=true and loc.objectID = ?", id);
    }

    @Override
    public List<String> propertyList() {
        List list = findNative("SELECT DISTINCT(propertycode) FROM " + getPublic() + ".localization WHERE isActive=TRUE ORDER BY propertycode");
        List<String> items = new ArrayList<>();
        for (Object obj : list) {
            String val = (String) obj;
            items.add(val);
        }
        return items;
    }

    @Override
    public EdsLocalization getLocalizationByCode(String propertyCode, String code) {
        return (EdsLocalization) findSingle("SELECT loc FROM EdsLocalization loc WHERE loc.isActive=true and loc.code = ? and loc.propertyCode=?", code.trim(), propertyCode.trim());
    }

    @Override
    public Date getModifiedDate() {
        EdsLocalization localization = (EdsLocalization) findSingle("FROM EdsLocalization loc WHERE loc.isActive=true AND lastUpdate is not null ORDER BY loc.lastUpdate desc");
        return localization.getDefaultLastUpdate();
    }

    @Override
    public List<EdsLocalization> listByPropertyCode(String propertyCode, String code) {
        return find("select loc from EdsLocalization loc where loc.isActive=true and loc.propertyCode = ? and loc.code=?", propertyCode.trim(), code.trim());
    }

    @Override
    public List<SelectItem> listByProperty(String propertyCode, String language) {
        String sql = "SELECT NEW com.edatasite.workforce.gwt.core.client.rpc.SelectItem(loc.objectID,loc.code,loc." + language + ") from EdsLocalization loc where loc.isActive=true and loc.propertyCode = '" + propertyCode + "'";
        return (List<SelectItem>) find(sql);
    }

    @Override
    public EdsLocalization get(String propertyCode, String code) {
        return (EdsLocalization) findSingleByNamedParams("select t from EdsLocalization t " +
                        "where t.propertyCode=:property and t.code=:code and t.isActive = true ",
                preparing(new Entry("property", propertyCode), new Entry("code", code)));
    }
}

