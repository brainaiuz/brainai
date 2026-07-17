package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmailFilterManager;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailFilterManager")
public class EmailFilterManagerImpl extends BaseManager<EdsEmailFilter> implements EmailFilterManager, Constants {

    public EmailFilterManagerImpl() {
        super(EdsEmailFilter.class);
    }

    @Override
    public List<EdsEmailFilter> getSubFilters(String filterType) {
        return find("select sf from EdsEmailFilter sf where " + ServerUtils.checkForDeleted("sf.deleted") + " and (sf.isParent is null or sf.isParent is false) and sf.type = '" + filterType + "'");
    }

    @Override
    public List<EdsEmailFilter> getParentsOnly() {
        return find("select f from EdsEmailFilter f where " + ServerUtils.checkForDeleted("f.deleted") + " and f.isParent is not null and f.isParent is true");
    }

    @Override
    public List<EdsEmailFilter> list(ListingFilterParameter filterParameter) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select f from EdsEmailFilter f where (f.deleted<>true or f.deleted is null) and f.isRule<>true ");
        if (filterParameter.getSearchKey() != null && !filterParameter.getSearchKey().isEmpty()) {
            sqlQuery.append("and lower(f.name) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' ");
        }
        if (filterParameter.getSortField() != null) {
            sqlQuery.append(" order by f.");
            if (filterParameter.getSortField().equals(EmailFilter.NAME)) {
                sqlQuery.append("name ");
            } else if (filterParameter.getSortField().equals(EmailFilter.ISPARENT)) {
                sqlQuery.append("parent ");
            }
            if (filterParameter.isAscending()) {
                sqlQuery.append("desc");
            } else {
                sqlQuery.append("asc");
            }
        }
        return find(sqlQuery.toString());
    }

    @Override
    public String[] getDefaultSelections(String type, String... defaults) {
        List<Object> list = findNative("select ef.defaultactions from " + getCompanyId() + ".emailfilters ef where ef.deleted is not true and ef.isparent is not true and ef.isrule is not true and ef.type = '" + EmailFilter.CREATE_CASE + "'");
        String[] result = new String[defaults.length];
        if (defaults != null && defaults.length > 0) {
            int i = 0;
            for (String key : defaults) {
                String value = "";
                if (list != null && list.size() > 0) {
                    for (Object l : list) {
                        String defaultActions = (String) l;
                        if ("".equals(value) && defaultActions != null && !"".equals(defaultActions) && defaultActions.contains(key)) {
                            int startIndex = defaultActions.indexOf(key);
                            value = defaultActions.substring(startIndex + key.length() + 1, defaultActions.indexOf(";", startIndex));
                        }
                    }
                }
                result[i++] = value;
            }
        }
        return result;
    }
}