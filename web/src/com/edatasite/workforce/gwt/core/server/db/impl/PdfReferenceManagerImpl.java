package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfReference;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PdfReferenceManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 19:10:20
 * To change this template use File | Settings | File Templates.
 */
@Repository("pdfReferenceManager")
public class PdfReferenceManagerImpl extends BaseManager<EdsPdfReference> implements PdfReferenceManager {
    public PdfReferenceManagerImpl() {
        super(EdsPdfReference.class);
    }

    @Override
    public List<EdsPdfReference> getReferences() {
        return find("select r from EdsPdfReference r where " + ServerUtils.checkForDeleted("r.deleted"));
    }

    @Override
    public EdsPdfReference getById(Integer objectId) {
        String companyId = getCompanyId();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from " + companyId + ".pdfreference r where r.id = " + objectId + " and " + ServerUtils.checkForDeleted("r.deleted"));
        List<EdsPdfReference> edsPdfReferences = findNative(sb.toString(), EdsPdfReference.class);
        if (!edsPdfReferences.isEmpty()) {
            return (EdsPdfReference) edsPdfReferences.get(0);
        } else {
            return null;
        }
    }

    @Override
    public EdsPdfReference getByCode(String code) {
        String sql = "select r from EdsPdfReference r " +
                     "  where (r.deleted = false or r.deleted is null) " +
                     "      and r.code =:code";
        List<EdsPdfReference> list = slaveEntityManager.createQuery(sql, EdsPdfReference.class)
                                                  .setParameter("code", code)
                                                  .setMaxResults(1)
                                                  .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
