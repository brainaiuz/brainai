package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.RTLTextReplacedElementFactory;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Khasan on 07.10.14.
 */
public abstract class AbstractITextCustomPdfHandler extends AbstractITextPostPdfHandler {


    protected ByteArrayOutputStream getPdfArrayOutputStream(Object dataClass) {
        EdsCompany edsCompany;
        if (getUserId(dataClass) != null) {
            edsCompany = userManager.get(getUserId(dataClass)).getCompany();
        } else {
            edsCompany = userManager.getUser().getCompany();
        }
        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(edsCompany.getObjectID(),
                getPdfCodeName(dataClass) != null ? getPdfCodeName(dataClass).name() : null, getCustomisedPDFTemplateId(dataClass));

        return generateCustomisePdfTemplate(dataClass, edsCompany, edsCompanyPdfTemplate);
    }

    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return null;
    }


    protected ByteArrayOutputStream generateCustomisePdfTemplate(Object dataClass, EdsCompany edsCompany, EdsCompanyPdfTemplate edsCompanyPdfTemplate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String itextGenericPdfData = buildNewPdfDocumentCustomise(dataClass, edsCompany);
        if (edsCompanyPdfTemplate != null) {
            itextGenericPdfData = generateCustomiPDF(edsCompanyPdfTemplate, itextGenericPdfData);
        }
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(itextGenericPdfData.getBytes(StandardCharsets.UTF_8)));
            ITextRenderer renderer = new ITextRenderer();
            RTLTextReplacedElementFactory rtlTextReplacedElementFactory = new RTLTextReplacedElementFactory(renderer.getOutputDevice(), "rtldir-arabic;rtldirheader-arabic");
            renderer.getSharedContext().setReplacedElementFactory(rtlTextReplacedElementFactory);
            if (edsCompanyPdfTemplate != null && edsCompanyPdfTemplate.getFontFamily() != null && !"".equals(edsCompanyPdfTemplate.getFontFamily())) {
                renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/" + edsCompanyPdfTemplate.getFontFamily()), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                if (edsCompanyPdfTemplate.getFontFamily() != null) {
                    if ("calibri.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibrib.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/calibri_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("arial.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ariali.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("arial_narrow.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial_narrowb.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("tahoma.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/thomabd.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/tahoma.ttf"), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                    }
                    if ("avant_garde.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/avant_garde_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("arialuni.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialuni.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("verdana.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanab.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanai.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/verdanaz.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("trebuchet_ms.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/trebucit.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("times.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/timesbi.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("dejavusans.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/dejavusans-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("source_sans_pro.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_black_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_extra_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/source_sans_pro_light_semi_bold_italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("arial-rounded-mt-bold.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial-rounded-mt-bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("helvetica.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/helvetica.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("GARA.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARA.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARABD.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GARAIT.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("GOTHIC.TTF".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHIC.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICB.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICBI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GOTHICI.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("Montserrat-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/Montserrat-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("ManilaSansReg.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansReg.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ManilaSansBld.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("HelveticaNeueLTArabic-Light.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Light.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/HelveticaNeueLTArabic-Roman.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("GothamLight.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamLight.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamMedium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/GothamBook.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("OpenSans-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Italic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-BoldItalic.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/OpenSans-Semibold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                    if ("ALSAgrofont-Regular.ttf".equals(edsCompanyPdfTemplate.getFontFamily())) {
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Regular.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Bold.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-Medium.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ALSAgrofont-BoldExpanded.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    }
                }
            } else {
                renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arial.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/arialbd.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                renderer.getFontResolver().addFont(getAbsaloutPath(getFontBase() + "/ariali.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
            renderer.setDocument(doc, null);
            renderer.layout();
            renderer.createPDF(baos);
        } catch (DocumentException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    protected String generateCustomiPDF(EdsCompanyPdfTemplate edsCompanyPdfTemplate, String content) {
        return null;
    }

    protected String buildNewPdfDocumentCustomise(Object dataClass, EdsCompany company) {
        return null;
    }

}
