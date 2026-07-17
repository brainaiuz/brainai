package com.edatasite.workforce.gwt.core.server.db.impl.webforms;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:04:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("webFormManager")
public class WebFormManagerImpl extends BaseManager<EdsWebForm> implements WebFormManager {
    public WebFormManagerImpl() {
        super(EdsWebForm.class);
    }

    @Override
    public List<EdsWebForm> list(ListingFilterParameter filterParametrs) {
        String orderBy = " order by webForm.lastUpdatedTime desc ";
        if (filterParametrs != null) {
            if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
                String field = null;
                if (filterParametrs.getSortField().equals(WebForm.TYPE)) {
                    field = "webForm.type.name";
                } else if (filterParametrs.getSortField().equals(WebForm.TITLE)) {
                    field = "webForm.title";
                } else if (filterParametrs.getSortField().equals(WebForm.URL)) {
                    field = "webForm.iFrameUrl";
                }
                if (field != null) {
                    orderBy = " order by " + field + " " + (!filterParametrs.isAscending() ? " DESC " : " ASC ");
                }
            }
        }
        String query = "select webForm ";
        query += getSqlWhereList(filterParametrs);
        return findInterval(query + orderBy, filterParametrs.getStart(), filterParametrs.getLimit());
    }

    private String getSqlWhereList(ListingFilterParameter filterParametrs) {
        String query = " from EdsWebForm webForm where " + ServerUtils.checkForDeleted("webForm.deleted");
        String search = "";
        if (filterParametrs != null) {
            if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
                search = " and lower(webForm.title) like lower('%" + filterParametrs.getSearchKey() + "%') " +
                        " OR lower(webForm.type.name) like lower('%" + filterParametrs.getSearchKey() + "%') ";
            }
        }
        EdsUser user = getUser();
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_WEB_FORMS_LIST)) {
            query += " and owner.objectID = " + user.getObjectID();
        }
        return query + search;
    }

    @Override
    public Integer getListCount(ListingFilterParameter filterParametr) {
        return ((Long) findSingle("select count(*) " + getSqlWhereList(filterParametr))).intValue();
    }

    @Override
    public EdsWebForm getWebFormByCustomLayout(Integer customLayoutID) {
        return (EdsWebForm) findSingle("select webForm from EdsWebForm webForm where webForm.layoutID = " + customLayoutID);
    }

    @Override
    public List<EdsWebForm> getCompanyWebFormsIncludeDeleteds() {
        return (List<EdsWebForm>) find(" select wf from EdsWebForm wf ");
    }

    @Override
    public void updateUrl(EdsWebForm webForm) {
        encrypt(webForm, true);
    }

    private void encrypt(final EdsWebForm webForm, boolean forceToEncrypt) {
        if (webForm != null && webForm.getCompany() != null && webForm.getObjectID() != null && (forceToEncrypt || webForm.getiFrameUrl() == null || "".equals(webForm.getiFrameUrl()))) {
            String url = webForm.getObjectID().toString() + "#" + webForm.getCompany().getObjectID();
            String encrypted = EncryptionHelper.encrypt(url, WebFormConstants.WEB_FORM);
            webForm.setiFrameUrl(encrypted);
            update(webForm);
        }
    }

    @Override
    public void create(EdsWebForm webForm) {
        super.create(webForm);
        encrypt(webForm, false);
    }
}
