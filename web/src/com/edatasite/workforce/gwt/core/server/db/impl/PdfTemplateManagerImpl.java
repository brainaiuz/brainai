package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplate;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:41:34
 * To change this template use File | Settings | File Templates.
 */
@Repository("pdfTemplateManager")
public class PdfTemplateManagerImpl extends BaseManager<EdsPdfTemplate> implements PdfTemplateManager {
    public PdfTemplateManagerImpl() {
        super(EdsPdfTemplate.class);
    }
}
