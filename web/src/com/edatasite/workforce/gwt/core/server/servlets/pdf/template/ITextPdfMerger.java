package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 09.04.2017 15:49
 */
public class ITextPdfMerger {

    public ByteArrayOutputStream mergePdfFiles(List<InputStream> inputPdfList) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        List<PdfReader> readers = new ArrayList<>();
        int totalPages = 0;

        for (InputStream pdf : inputPdfList) {
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;

        for (PdfReader pdfReader : readers) {
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }
        baos.flush();
        document.close();
        baos.close();

        return baos;
    }
}
