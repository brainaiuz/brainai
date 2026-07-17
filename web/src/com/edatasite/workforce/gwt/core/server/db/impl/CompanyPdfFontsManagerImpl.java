package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfFonts;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfFontsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/24/11
 * Time: 2:34 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("companyPdfFontsManager")
public class CompanyPdfFontsManagerImpl extends BaseManager<EdsPdfFonts> implements CompanyPdfFontsManager{
    public CompanyPdfFontsManagerImpl() {
        super(EdsPdfFonts.class);
    }

    @Override
    public List<EdsPdfFonts> getPdfFonts() {
       return (List<EdsPdfFonts>) find("select f from EdsPdfFonts f ");
    }

    @Override
    public EdsPdfFonts getPdfFontByID(Integer ID) {
        if(ID == null){
            return null;
        }
      return (EdsPdfFonts) findSingle("select f from EdsPdfFonts f where f.id =? ",ID);
    }
}
