package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsSignature;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.SignatureManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11.02.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
@Repository("signatureManager")
public class SignatureManagerImpl extends BaseManager<EdsSignature> implements SignatureManager {

    public SignatureManagerImpl() {
        super(EdsSignature.class);
    }

    @Override
    public List<EdsSignature> getSignatures(ListingFilterParameter fp) {
        StringBuilder s = new StringBuilder();
        if (fp != null && !fp.getShowInListing() && fp.getClientId() != null) {
            s.append("AND (user.id=" + fp.getClientId() + ") ");
        }
        if (fp != null && fp.getSqlSearchKey() != null) {
            String searchKey = fp.getSqlSearchKey();
            s.append("AND ").append("lower(user.userName) like '" + searchKey + "' ");
        }
        if (fp != null) {
            if (fp.getSortField() != null || !"".equals(fp.getSortField())) {
                s.append("ORDER BY userID ");
                s.append(!fp.isAscending() ? "desc " : "");
            }
        }
        return find("SELECT t FROM EdsSignature t where (deleted is null or deleted<>true) " + s);
    }

    @Override
    public EdsSignature getByUser(EdsUser user) {
        user = user == null ? getUser() : user;
        return (EdsSignature) findSingle("select s from EdsSignature s where " + ServerUtils.checkForDeleted("s.deleted") + " and s.user.objectID = " + (user == null ? 0 : user.getObjectID()));
    }
}
