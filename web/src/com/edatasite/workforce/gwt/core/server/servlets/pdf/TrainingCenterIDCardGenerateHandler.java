package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificate;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateItem;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateType;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CertificateManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/18/12
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingCenterIDCardGenerateHandler extends AbstractITextPostPdfHandler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_VALUE = "COLUMN_VALUE";
    public static final String ACCOUNT = "ACCOUNT";

    @Autowired
    private CertificateManager certificateManager;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    protected CommonService commonService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return new ITextGenericPdfData();
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        pdf.setCompanyData(getCompanyData(company, true, hasPhantom));

        RequestObject requestObject = (RequestObject) dataClass;
        Integer certificateID = requestObject.getObjectID();

        EdsCertificate certificate = certificateManager.get(certificateID);
        EdsCertificateType certificateType = certificate.getCertificateType();
        EdsStudent student = certificate.getStudent();
        List<Object[]> datas = courseScheduleStudentManager.getStudentDetailsForIDCard(certificateType.getObjectID());
        String csNumber = "";
        Date startDate = null;
        Date endDate = null;
        String grade = "";
        if (datas != null && datas.size() > 0) {
            for (Object[] ob : datas) {
                csNumber = (String) ob[0];
                startDate = (Date) ob[1];
                endDate = (Date) ob[2];
                grade = (String) ob[3];
            }
        }

        String studentPhoto = student.getPhoto() != null ? hrmsService.getImageUrl(student.getPhoto().getObjectID()) : null;

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        EdsCrmAccount customer = student.getCustomer();

        CustomisedITextTable idCardCustomData = new CustomisedITextTable();
        idCardCustomData.setName("ID Card");
        idCardCustomData.addColumnOrder("NAME", "VALUE");
        idCardCustomData.addRowWithCode("CERTIFICATE_TYPE", "", escapeHtml(certificateType.getName()));
        idCardCustomData.addRowWithCode("FRONT_IMAGE_URL", "", EdsContextParams.getFullHost() + certificateType.getFrontImageURL());
        idCardCustomData.addRowWithCode("BACK_IMAGE_URL", "", EdsContextParams.getFullHost() + certificateType.getBackImageURL());
        idCardCustomData.addRowWithCode("CERTIFICATE_NUMBER", "", escapeHtml(certificate.getNumber()));
        idCardCustomData.addRowWithCode("STUDENT_NAME", "Name:", escapeHtml(student.getFullName()));
        idCardCustomData.addRowWithCode("STUDENT_NUMBER", "", escapeHtml(student.getNumber() != null ? student.getNumber() : ""));
        idCardCustomData.addRowWithCode("STUDENT_GENDER", "", escapeHtml(student.getGender()));
        idCardCustomData.addRowWithCode("CS_NUMBER", "", escapeHtml(csNumber));
        idCardCustomData.addRowWithCode("GRADE", "", escapeHtml(grade));
        idCardCustomData.addRowWithCode("CS_START_DATE", "", startDate != null ? dateFormat.format(startDate) : "");
        idCardCustomData.addRowWithCode("CS_END_DATE", "", endDate != null ? dateFormat.format(endDate) : "");
        idCardCustomData.addRowWithCode("CURRENT_DATE", "", dateFormat.format(new Date()));
        idCardCustomData.addRowWithCode("REFERENCE_INDICATOR", "Ref Ind:", new DecimalFormat("0000").format(student.getObjectID()));

        idCardCustomData.addRowWithCode("DOB", "DOB:", (student.getContact() != null && student.getContact().getDateOfBirth() != null) ? dateFormat.format(student.getContact().getDateOfBirth()) : "");

        idCardCustomData.addRowWithCode("RESIDENCE_CARD_NO", "Residence Card No:", student.getSafetyPPNumber() != null ? escapeHtml(student.getSafetyPPNumber()) : "");

        idCardCustomData.addRowWithCode("COMPANY_EMPLOYEE_NUMBER", "Employee Number:", student.getCompEmplNumber() != null ? escapeHtml(student.getCompEmplNumber()) : "");


        idCardCustomData.addRowWithCode("COMPANY_NAME", "Company:", escapeHtml(customer.getName()));
        if (studentPhoto != null) {
            idCardCustomData.addRowWithCode("STUDENT_PHOTO", "", escapeHtml(studentPhoto));
        } else {
            idCardCustomData.addRowWithCode("STUDENT_PHOTO", "", escapeHtml("/tc/images/no-photo.gif"));
        }

        HashMap<Integer, String> colorStyle = new HashMap<>();
        for (SelectItem color : TCConstants.COLORS) {
            colorStyle.put(color.getId(), color.getName().toLowerCase());
        }
        idCardCustomData.setCustomFields(getCustomFields(customer));

        CustomisedITextTable idCardItemsData = new CustomisedITextTable();
        idCardItemsData.setName("ID Card Items");
        idCardItemsData.addColumnOrder("VALUE", "COLOR");
        List<EdsCertificateItem> certificateItemList = certificate.getItems();
        for (EdsCertificateItem certificateItem : certificateItemList) {
            if (certificateItem.getSorder() != null && certificateItem.getValues() != null) {
                idCardItemsData.addRowWithCode(certificateItem.getSorder().toString(), escapeHtml(certificateItem.getValues()),
                        (certificateItem.getColor() != null && colorStyle.get(certificateItem.getColor()) != null) ? colorStyle.get(certificateItem.getColor()) : "black");
            }
        }

        customData.put("ID_CARD", idCardCustomData);
        customData.put("ID_CARD_ITEMS", idCardItemsData);

        pdf.setCustomData(customData);
        return pdf;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCrmAccount customer) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (customer != null && customer.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(customer.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmAccount));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(certificateManager.getUser().getCompany());
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, (item.getFieldDateNonConvertedValue() != null && item.getFieldDateNonConvertedValue().getNonConvertedDate() != null) ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put(ACCOUNT, itemCusFields);
            }
        }
        return customFields;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ID_CARD;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsCertificate certificate = certificateManager.get(requestObject.getObjectID());
        setFileName("ID_CARD_" + certificate.getNumber());
    }
}
