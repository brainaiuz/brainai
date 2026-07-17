package com.finnetlimited.reportservice.core.server.db.schema;

import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomHtml;

public interface CustomHtmlManager  extends Manager<EdsCustomHtml> {

    EdsCustomHtml getCustomHtmlByReportId(Integer id);
}
