package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface AssessmentTemplateManager extends Manager<EdsAssessmentTemplate> {

    List<EdsAssessmentTemplate> getAssessmentTemplates(ListingFilterParameter fp);

    EdsAssessmentTemplate getDefaultTemplate();

    Long getTemplatesTotal(ListingFilterParameter fp);

}
