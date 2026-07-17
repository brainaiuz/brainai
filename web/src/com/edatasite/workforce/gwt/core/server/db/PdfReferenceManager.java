package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsPdfReference;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 19:11:11
 * To change this template use File | Settings | File Templates.
 */
public interface PdfReferenceManager extends Manager<EdsPdfReference> {

    List<EdsPdfReference> getReferences();

    EdsPdfReference getById(Integer id);

    EdsPdfReference getByCode(String code);
}
