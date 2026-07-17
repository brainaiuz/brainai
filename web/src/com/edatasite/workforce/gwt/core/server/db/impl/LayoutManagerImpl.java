package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCustomLayout;
import com.edatasite.workforce.core.domain.EdsDefaultLayout;
import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CustomLayoutManager;
import com.edatasite.workforce.gwt.core.server.db.DefaultLayoutManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/25/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("layoutManager")
public class LayoutManagerImpl extends BaseManager<EdsLayout> implements LayoutManager {
    @Autowired
    private DefaultLayoutManager defaultLayoutManager;

    @Autowired
    private CustomLayoutManager customLayoutManager;

    public LayoutManagerImpl() {
        super(EdsDefaultLayout.class);
    }

    @Override
    public String getLayoutHTML(String type) {
        String layout = (String) findSingle("select l.layout from EdsCustomLayout l where l.formID = ?", type);
        if (!StringUtils.isEmpty(layout)) {
            return layout;
        }
        return (String) findSingle("select l.layout from EdsDefaultLayout l where l.formID = ?", type);
    }

    public String getCustomLayout(String type) {
        String layout = (String) findSingle("select l.layout from EdsCustomLayout l where l.formID = ?", type);
        if (!StringUtils.isEmpty(layout)) {
            return layout;
        }
        return null;
    }

    public String getDefaultLayout(String formID) {
        return (String) findSingle("select l.layout from EdsDefaultLayout l where l.formID = ?", formID);
    }

    @Override
    public EdsLayout getLayout(String formID, String formType) {
        formType = formType == null ? LayoutRPC.ADD : formType;
        EdsCustomLayout layout = (EdsCustomLayout) findSingle("select l from EdsCustomLayout l where l.formID = ? and l." + formType + " is true and l.webForm is not true", formID);
        if (layout != null && !"".equals(layout)) {
            return layout;
        }
        return (EdsDefaultLayout) findSingle("select l from EdsDefaultLayout l where l.formID = ? and l." + formType + " is true and l.webForm is not true", formID);
    }

    public EdsLayout getCustomLayout(String formID, String formType) {
        formType = formType == null ? LayoutRPC.ADD : formType;
        EdsCustomLayout layout = (EdsCustomLayout) findSingle("select l from EdsCustomLayout l where l.formID = ? and l." + formType + " is true and l.webForm is not true", formID);
        if (layout != null && !"".equals(layout)) {
            return layout;
        }
        return null;
    }

    public EdsLayout getDefaultLayout(String formID, String formType) {
        return (EdsDefaultLayout) findSingle("select l from EdsDefaultLayout l where l.formID = ? and l." + formType + " is true and l.webForm is not true", formID);
    }

    @Override
    public List<EdsLayout> list(ListingFilterParameter param) {
        StringBuilder sql = new StringBuilder("select * from ");
        if (param.getCompanyID() != null && param.getCompanyID() > 0) {
            sql.append("\"").append(param.getCompanyID()).append("\".customlayout_new_ui ");
        } else {
            sql.append("defaultlayout_new_ui ");
        }
        sql.append(" where 1=1 ");
        if (!StringUtils.isEmpty(param.getSqlSearchKey())) {
            sql.append("AND ").append("lower(title) like '" + param.getSqlSearchKey() + "' ");
        }
        sql.append("order by title");
        if (param.getLimit() > 0) {
            sql.append(" OFFSET " + param.getStart() + " LIMIT " + param.getLimit() + " ");
        }
        return findNative(sql.toString(), param.getCompanyID() != null && param.getCompanyID() > 0 ? EdsCustomLayout.class : EdsDefaultLayout.class);
    }

    @Override
    public Integer listCount(ListingFilterParameter param) {
        StringBuilder sql = new StringBuilder("select count(id) from ");
        if (param.getCompanyID() != null && param.getCompanyID() > 0) {
            sql.append("\"").append(param.getCompanyID()).append("\".customlayout_new_ui ");
        } else {
            sql.append("defaultlayout_new_ui ");
        }
        sql.append(" where 1=1 ");
        if (!StringUtils.isEmpty(param.getSqlSearchKey())) {
            sql.append("AND ").append("lower(title) like '" + param.getSqlSearchKey() + "' ");
        }
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        if (count != null) {
            return count.intValue();
        } else {
            return 0;
        }
    }

    @Override
    public EdsLayout get(Integer companyID, Integer layoutID) {
        if (companyID == null) {
            return defaultLayoutManager.get(layoutID);
        }
        return customLayoutManager.get(layoutID);
    }

    @Override
    public void create(EdsLayout obj) {
        if (obj instanceof EdsDefaultLayout) {
            defaultLayoutManager.create((EdsDefaultLayout) obj);
        } else {
            customLayoutManager.create((EdsCustomLayout) obj);
        }
    }

    @Override
    public void update(EdsLayout obj) {
        if (obj instanceof EdsDefaultLayout) {
            defaultLayoutManager.update((EdsDefaultLayout) obj);
        } else {
            customLayoutManager.update((EdsCustomLayout) obj);
        }
    }

    @Override
    public void delete(EdsLayout obj) {
        if (obj instanceof EdsDefaultLayout) {
            defaultLayoutManager.delete((EdsDefaultLayout) obj);
        } else {
            customLayoutManager.delete((EdsCustomLayout) obj);
        }
    }
}
