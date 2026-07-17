package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.AssessmentTemplateManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("assessmentTemplateManager")
public class AssessmentTemplateManagerImpl extends BaseManager<EdsAssessmentTemplate> implements AssessmentTemplateManager {

    public AssessmentTemplateManagerImpl() {
        super(EdsAssessmentTemplate.class);
    }

    public List<EdsAssessmentTemplate> getAssessmentTemplates(ListingFilterParameter fp) {

        if (fp.getDepartmentId() != null) {
            StringBuilder sql = new StringBuilder("select distinct at.* from " + getCompanyId() + ".assessmentTemplate at");
            sql.append(" left join " + getCompanyId() + ".template_department td on at.id = td.template_id ");
            sql.append(" where (at.deleted <> true OR at.deleted is null)");
            sql.append(" and td.department_id = ").append(fp.getDepartmentId());
            if (fp.getUserID() != null) {
                sql.append(" or at.ownerId = ").append(fp.getUserID());
            }
            return (List<EdsAssessmentTemplate>) findNative(sql.toString(), EdsAssessmentTemplate.class);
        }
        StringBuilder hql = new StringBuilder("select distinct at from EdsAssessmentTemplate at where (at.deleted <> true OR at.deleted is null)");
        if (fp.getUserID() != null) {
            hql.append(" and at.owner is not null and at.owner.objectID = ").append(fp.getUserID());
        }
        if (fp.getSortField() != null) {
            if (TemplateListItem.NAME.equals(fp.getSortField())) {
                hql.append(" order by at.name ");
            }

            if (fp.isAscending()) {
                hql.append(" ASC ");
            } else {
                hql.append(" DESC ");
            }
        }
        return findInterval(hql.toString(), fp.getStart(), fp.getLimit());
    }

    public EdsAssessmentTemplate getDefaultTemplate() {
        return get(EdsAssessmentTemplate.DEFAULT);
    }

    public Long getTemplatesTotal(ListingFilterParameter fp) {
        StringBuilder hql = new StringBuilder("select count(at.objectID) from EdsAssessmentTemplate at where at.deleted <> true OR at.deleted is null");
        return (Long)findSingle(hql.toString());
    }
}
