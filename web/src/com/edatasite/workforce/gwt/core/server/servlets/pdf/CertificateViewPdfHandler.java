package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileConstans;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Khasan on 07.10.14.
 */
public class CertificateViewPdfHandler extends AbstractITextCustomPdfHandler{

    @Autowired
    private HrmsService hrmsService;

    private final String defaultValuePattern = "\\[\\[(.*?)\\]\\]";


    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CERTIFICATE_SUMMARY;
    }

    @Override
    protected String buildNewPdfDocumentCustomise(Object dataClass, EdsCompany company) {
        RequestObject requestObject = (RequestObject) dataClass;
        int id = requestObject.getObjectID();
        CertificateItem item =  hrmsService.getCertificateData(id);
        Map<String, Object> values = new TreeMap<>();
        String newContent = "";

        if (item.getCustomHTMLcontent() != null && !"".equals(item.getCustomHTMLcontent())) {
            newContent = replaceContentAttributes(item.getCustomHTMLcontent(), item, company, values);
        } else {
            newContent = replaceContentAttributes(item.getContent().replace("<br>", "<br/>").replace("<hr>", "<hr/>"), item, company, values);

            if (item.isPdfHeaderFooter()) {
                newContent = addHeaderFooterHtml(newContent);
            } else if (!newContent.trim().startsWith("<div")) {
                newContent = "<div>" + newContent + "</div>";
            }
        }

        try {
            return EdsTemplates.evaluateTemplate(values, newContent);
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String replaceContentAttributes(String content, CertificateItem item, EdsCompany company, Map<String, Object> values) {
        if (content != null && !"".equals(content)) {
            if (content.contains(EmployeeProfileConstans.BOX_1)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_1, "${box1}", escapeHtml(item.getTextBox1()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_2)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_2, "${box2}", escapeHtml(item.getTextBox2()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_3)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_3, "${box3}", escapeHtml(item.getTextBox3()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_4)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_4, "${box4}", escapeHtml(item.getTextBox4()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_5)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_5, "${box5}", escapeHtml(item.getTextBox5()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_6)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_6, "${box6}", escapeHtml(item.getTextBox6()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_7)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_7, "${box7}", escapeHtml(item.getTextBox7()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_8)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_8, "${box8}", escapeHtml(item.getTextBox8()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_9)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_9, "${box9}", escapeHtml(item.getTextBox9()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_10)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_10, "${box10}", escapeHtml(item.getTextBox10()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_11)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_11, "${box11}", escapeHtml(item.getTextBox11()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_12)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_12, "${box12}", escapeHtml(item.getTextBox12()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_13)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_13, "${box13}", escapeHtml(item.getTextBox13()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_14)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_14, "${box14}", escapeHtml(item.getTextBox14()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_15)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_15, "${box15}", escapeHtml(item.getTextBox15()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_16)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_16, "${box16}", escapeHtml(item.getTextBox16()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_17)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_17, "${box17}", escapeHtml(item.getTextBox17()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.BOX_18)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.BOX_18, "${box18}", escapeHtml(item.getTextBox18()), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_1)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_1, "${area1}", escapeHtml(item.getTextArea1()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_2)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_2, "${area2}", escapeHtml(item.getTextArea2()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_3)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_3, "${area3}", escapeHtml(item.getTextArea3()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_4)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_4, "${area4}", escapeHtml(item.getTextArea4()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_5)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_5, "${area5}", escapeHtml(item.getTextArea5()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_6)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_6, "${area6}", escapeHtml(item.getTextArea6()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_7)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_7, "${area7}", escapeHtml(item.getTextArea7()).replace("\\n", "<br/>"), content, values);
            }
            if (content.contains(EmployeeProfileConstans.TEXT_AREA_8)) {
                content = replaceCustomFieldAttributes(EmployeeProfileConstans.TEXT_AREA_8, "${area8}", escapeHtml(item.getTextArea8()).replace("\\n", "<br/>"), content, values);
            }
            content = content.replace("&nbsp;", "")
                    .replace("&", "&amp;");
        }
        String companyName = company.getName();
        String caddress = company.getAddress1() != null ? company.getAddress1() : "";
        String city = company.getCity() != null ? company.getCity() : "";
        String postCode = (company.getPostCode() != null && !"".equals(company.getPostCode())) ? company.getPostCode() : "";
        EdsCountry edsCountry = (company.getCountryZone() != null && company.getCountryZone().getCountry() != null) ? company.getCountryZone().getCountry() : null;
        String country = "";
        if (edsCountry != null) {
            country = countryLocalizer.localize(edsCountry.getCode(), edsCountry.getName());
        }
        String companyLog = "";
        try {
            companyLog = getPdfLogoUrl(company, false);
            if (companyLog != null) {
                companyLog = companyLog.replaceAll("[&]", "&amp;");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityPostCode = (!"".equals(city) && !"".equals(postCode) ? (city + ", " + postCode) : (!"".equals(city) ? city : postCode));
        values.put(EmployeeProfileConstans.HEADER_COMPANY_NAME, companyName);
        values.put(EmployeeProfileConstans.HEADER_ADDRESS, caddress);
        values.put(EmployeeProfileConstans.HEADER_CITY_POSTCODE, cityPostCode);
        values.put(EmployeeProfileConstans.HEADER_COUNTRY, country);
        values.put(EmployeeProfileConstans.HEADER_COMPONY_LOGO, companyLog);

        return content;
    }

    protected String generateCustomiPDF(EdsCompanyPdfTemplate edsCompanyPdfTemplate, String content) {
        Map<String, Object> values = new TreeMap<>();
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        String address = company.getAddress1() != null ? company.getAddress1() : "";
        if (address == "") {
            address = company.getAddress2() != null ? company.getAddress2() : "";
        }
        address = address + (company.getPostCode() != null && company.getPostCode() != "" ? address != "" ? ", " + company.getPostCode() : company.getPostCode() : "");
        String imageUrl = null;
        try {
            imageUrl = getPdfLogoUrl(company, false);
            if (imageUrl != null) {
                imageUrl = imageUrl.replaceAll("[&]", "&amp;");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put("${companyname}", company.getName());
        values.put("${companyaddress}", address);
        values.put("${companylogo}", imageUrl != null ? imageUrl : "");
        values.put("${content}", content);
        try {
            return EdsTemplates.evaluateTemplate(values, edsCompanyPdfTemplate.getTemplate().getContent());
        } catch (EdsTemplateException e) {
            e.printStackTrace();
            return content;
        }
    }

    private String replaceCustomFieldAttributes(String fieldCode, String toReplaceCode, String attributeValue, String content, Map<String, Object> values) {
        String value = null;
        if (content.contains(fieldCode + "[[")) {
            String tempFieldCode = fieldCode.replace("$$", "");
            Pattern pattern = Pattern.compile("\\$\\$" + tempFieldCode + "\\$\\$"  + defaultValuePattern, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);
            if (matcher != null && matcher.find() && matcher.groupCount() > 0) {
                value = matcher.group(1);
                content = content.replace("[[" + value + "]]", "");
            }
        }
        content = content.replace(fieldCode, toReplaceCode);
        values.put(toReplaceCode, attributeValue != null && !"".equals(attributeValue) ? attributeValue : value != null ? value.replace("\\n", "<br/>") : "___________________");

        return content;
    }

    private String addHeaderFooterHtml(String content){
        content = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<style type=\"text/css\" >\n" +
                " @page {\n" +
                "   size: 8.3in 11.7in;\n" +
                "   margin:150px 30px 100px 30px;\n" +
                "   padding:0;\n" +
                "            @top-center {\n" +
                "                content: element(header);\n" +
                "            }\n" +
                "            @bottom-left {\n" +
                "                content: element(footer);\n" +
                "            }\n" +
                "        }\n" +
                "  #header {\n" +
                "   margin:20px 10px;\n" +
                "            display: block;\n" +
                "            position: running(header);\n" +
                "        }\n" +
                "  #footer{\n" +
                "   margin:20px 10px;\n" +
                "            display: block;\n" +
                "            position: running(footer);\n" +
                "  }\n" +
                "  #pagenumber:before {\n" +
                "   content: counter(page);\n" +
                "  }\n" +
                "\n" +
                "  #pagecount:before {\n" +
                "   content: counter(pages);\n" +
                "  }\n" +
                "  .tableBorder{\n" +
                "      border-top:1px solid #000000;\n" +
                "      border-left:1px solid #000000;\n" +
                "  }\n" +
                "  .tableBorder td{\n" +
                "      border-right:1px solid #000000;\n" +
                "      border-bottom:1px solid #000000;\n" +
                "  }\n" +
                "  body{\n" +
                "   font-family:Calibri;\n" +
                "  }\n" +
                "  th{\n" +
                "   font-family:Calibri;\n" +
                "   font-size:12px;\n" +
                "   font-weight:lighter;\n" +
                "  }\n" +
                "  th,td{\n" +
                "   padding:5px;\n" +
                "   overflow: hidden;\n" +
                "    word-wrap: break-word;\n" +
                "\tzoom: 1;\n" +
                "  }\n" +
                "\n" +
                "  b,p,td,span,caption,hr,h1,h2,h3,h4,h5{font-family:Calibri;}\n" +
                "  table {\n" +
                "            -fs-table-paginate: paginate;\n" +
                "\t\t\ttable-layout:fixed;\n" +
                "        }\n" +
                "\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"header\">\n" +
                "  <table width=\"100%\" style=\"border-bottom: 1px solid #000000;\">\n" +
                "   <tr>\n" +
                "    <td width=\"50%\" style=\"font-size:12px\">\n" +
                "\t  <span style=\"color:#7d8ce7;font-weight:bold;font-size:14px\">${ccompanyName}</span> <br/>\n" +
                "\t  ${aaddress} <br/>\n" +
                "\t  ${ccityPostCode} <br/>\n" +
                "\t  ${ccountry} <br/>\n" +
                "\t</td>\n" +
                "    <td width=\"50%\" align=\"right\">\n" +
                "     <img src=\"$ccompanyLogoUrl\"/>\n" +
                "    </td>\n" +
                "   </tr>\n" +
                "  </table>\n" +
                " </div>\n" +
                " <div id=\"footer\">\n" +
                "     <table width=\"100%\" style=\"font-size:12px\">\n" +
                "         <tr>\n" +
                "             <td align=\"left\">\n" +
                "                 <p align=\"left\" style=\"font-size:12px;font-weight:bold\">Powered by: <a href=\"http://www.kpi.com\" style=\"font-weight:bold\">http://www.kpi.com</a></p>\n" +
                "             </td>\n" +
                "             <td width=\"10%\" align=\"right\" style=\"font-weight:bold\">\n" +
                "                 Page <span id=\"pagenumber\"/>\n" +
                "             </td>\n" +
                "         </tr>\n" +
                "     </table>\n" +
                " </div>\n" + content;
        content += "</body>\n" +
                "</html>";
        return content;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        int id = requestObject.getObjectID();
        CertificateItem item =  hrmsService.getCertificateData(id);
        if (item != null) {
            String subject = item.getCertificateNumber().getNumberString() + "_" + item.getEmployee().getName();
            setFileName((subject.length() > 24 ? subject.substring(0, 24) : subject) + "_" + dateFormat(new Date()));
        } else {
            setFileName("certificate_" + dateFormat(new Date()));
        }
    }
}
