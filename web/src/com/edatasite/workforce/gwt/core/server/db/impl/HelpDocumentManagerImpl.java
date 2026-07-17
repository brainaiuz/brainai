package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsHelpDocument;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.HelpDocumentManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 2/28/13
 * Time: 5:30 PM
 */
@Repository("helpDocumentManager")
public class HelpDocumentManagerImpl extends BaseManager<EdsHelpDocument> implements HelpDocumentManager {

    public HelpDocumentManagerImpl() {
        super(EdsHelpDocument.class);
    }
    private boolean isLochinShodiev(){
        return "lochin.shodiev@workforcetrack.com".equals(getUser().getUserName());
    }
    @Override
    public List<EdsHelpDocument> getHelpDocumentList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select doc from EdsHelpDocument doc ");
        sql.append(" where ");
        if(isLochinShodiev()){
           sql.append("1=1");
        } else{
           sql.append("doc.hostName='");
           sql.append(EdsContextParams.getHostname()).append("'");
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(doc.title) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(doc.form) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(doc.section) like '").append(fp.getSqlSearchKey()).append("')");
        }
        sql.append(" order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if ("title".equals(fp.getSortField())) {
                sql.append("doc.title");
            } else if ("form".equals(fp.getSortField())) {
                sql.append("doc.form");
            } else {
                sql.append(" doc.objectID desc");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" doc.objectID  desc");

        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public Integer getHelpDocumentTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(doc.objectID) from EdsHelpDocument doc ");
        sql.append(" where ");
        if(isLochinShodiev()){
            sql.append("1=1");
        }else{
           sql.append("doc.hostName='");
           sql.append(EdsContextParams.getHostname()).append("'");
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(doc.title) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(doc.form) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(doc.section) like '").append(fp.getSqlSearchKey()).append("')");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsHelpDocument> getHelpDocumentBySectionForm(String section, String form) {
        String block = HelpDocumentItem.LEFT_BLOCK;
        if (section != null && form != null) {
            return (List<EdsHelpDocument>) find("select helpdoc from EdsHelpDocument helpdoc where helpdoc.section ='" + section + "' and helpdoc.form ='" + form + "'  and helpdoc.block='" + block + "' and helpdoc.hostName='" + EdsContextParams.getHostname() + "' order by helpdoc.objectID asc ");
        }
        return null;
    }

    @Override
    public EdsHelpDocument getHelpDocumentByFormCode(String form) {
       String block = HelpDocumentItem.RIGHT_TOP;
        return (EdsHelpDocument) findSingle("select doc from EdsHelpDocument doc where doc.block='" + block + "' and doc.form=? and doc.hostName=?", form, EdsContextParams.getHostname());

    }

    @Override
    public void deleteHelpDocument(Integer objectId) {
        update("delete from EdsHelpDocument d where d.objectID=?", objectId);
    }

    @Override
    public Boolean getExistHelpDocument(Integer objectID, String form, String block) {
        if (objectID != null) {
            return find("select doc from EdsHelpDocument doc where doc.form=? and doc.block=? and doc.objectID!=? ", form, block, objectID).size() > 0;
        } else {
            return find("select doc from EdsHelpDocument doc where doc.form=? and doc.block=?",form,block).size() > 0;
        }
    }
}
