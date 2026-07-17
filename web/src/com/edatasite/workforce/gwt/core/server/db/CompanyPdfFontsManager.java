package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsPdfFonts;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/24/11
 * Time: 2:31 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CompanyPdfFontsManager extends Manager<EdsPdfFonts>{

    List<EdsPdfFonts> getPdfFonts();

    EdsPdfFonts getPdfFontByID(Integer ID);


}
