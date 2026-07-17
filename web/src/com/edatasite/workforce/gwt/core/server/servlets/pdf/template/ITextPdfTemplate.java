package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import java.io.IOException;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2010
 * Time: 15:07:56
 */
public interface ITextPdfTemplate {
    Element generatePdf(Document doc) throws DocumentException, IOException;
}
