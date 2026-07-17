package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsLocationCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormLocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class VacancyViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        LeaveRequestObject requestObject = new LeaveRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsUser user = vacancyManager.getUser();
        EdsCrmContact contactItem = crmContactManager.get(user.getObjectID());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat totalNumberFormat = new DecimalFormat(",##0");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        RequestObject requestObject = (RequestObject) dataClass;
        Integer vacancyId = requestObject.getObjectID();
        VacancyItem item = recruitmentService.getVacancyItem(vacancyId);
        if (item == null) {
            return pdfData;
        }

        EdsPosition edsPosition = item.getPositionItem() != null && item.getPositionItem().getObjectID() != null ? positionManager.get(item.getPositionItem().getObjectID()) : null;
        String positionEn = "";
        String positionRu = "";
        String positionUz = "";
        if (edsPosition != null && edsPosition.getLocale() != null) {
            positionEn = edsPosition.getLocale().getEnglish() != null ? edsPosition.getLocale().getEnglish() : "";
            positionRu = edsPosition.getLocale().getRussian() != null ? edsPosition.getLocale().getRussian() : "";
            positionUz = edsPosition.getLocale().getUzbek() != null ? edsPosition.getLocale().getUzbek() : "";
        }

        EdsDepartment edsDepartment = item.getDepartment() != null && item.getDepartment().getId() != null ? departmentManager.get(item.getDepartment().getId()) : null;
        String departmentEn = "";
        String departmentRu = "";
        String departmentUz = "";
        if (edsDepartment != null && edsDepartment.getLocale() != null) {
            departmentEn = edsDepartment.getLocale().getEnglish();
            departmentRu = edsDepartment.getLocale().getRussian();
            departmentUz = edsDepartment.getLocale().getUzbek();
        }

        EdsReference edsStatus = item.getStatus() != null && item.getStatus().getName() != null && item.getStatus().getName() != null ? referenceManager.get(item.getStatus().getObjectID()) : null;
        String statusEn = "";
        String statusRu = "";
        String statusUz = "";
        if (edsStatus != null && edsStatus.getLocale() != null) {
            statusEn = edsStatus.getLocale().getEnglish();
            statusRu = edsStatus.getLocale().getRussian();
            statusUz = edsStatus.getLocale().getUzbek();
        } else {
            statusUz = referenceWfmMessageSource.localize(item.getStatus().getCode(), item.getStatus().getName(), new Locale("UZ"));
            statusRu = referenceWfmMessageSource.localize(item.getStatus().getCode(), item.getStatus().getName(), new Locale("RU"));
            statusEn = referenceWfmMessageSource.localize(item.getStatus().getCode(), item.getStatus().getName(), new Locale("EN"));
        }


        EdsReference edsRequireddegree = item.getRequiredDegree() != null && item.getRequiredDegree().getName() != null ? referenceManager.get(item.getRequiredDegree().getObjectID()) : null;
        String requireddegreeEn = "";
        String requireddegreeRu = "";
        String requireddegreeUz = "";
        if (edsRequireddegree != null && edsRequireddegree.getLocale() != null) {
            requireddegreeEn = edsRequireddegree.getLocale().getEnglish();
            requireddegreeRu = edsRequireddegree.getLocale().getRussian();
            requireddegreeUz = edsRequireddegree.getLocale().getUzbek();
        }


        EdsReference edsJobType = item.getJobType() != null && item.getJobType().getName() != null ? referenceManager.get(item.getJobType().getId()) : null;
        String jobTypeEn = "";
        String jobTypeRu = "";
        String jobTypeUz = "";
        if (edsJobType != null && edsJobType.getLocale() != null) {
            jobTypeEn = edsJobType.getLocale().getEnglish();
            jobTypeRu = edsJobType.getLocale().getRussian();
            jobTypeUz = edsJobType.getLocale().getUzbek();
        } else if (edsJobType != null) {
            jobTypeEn = referenceWfmMessageSource.localize(edsJobType.getCode(), item.getJobType().getName(), new Locale("EN"));
            jobTypeRu = referenceWfmMessageSource.localize(edsJobType.getCode(), item.getJobType().getName(), new Locale("Ru"));
            jobTypeUz = referenceWfmMessageSource.localize(edsJobType.getCode(), item.getJobType().getName(), new Locale("Uz"));
        }

        String descriptionEn = "";
        String descriptionRu = "";
        String descriptionUz = "";
        if (item.getDescriptionLocalize() != null && item.getDescriptionLocalize().size() != 0 && !item.getDescriptionLocalize().isEmpty()) {
            descriptionEn = item.getDescriptionLocalize().get("en");
            descriptionRu = item.getDescriptionLocalize().get("ru");
            descriptionUz = item.getDescriptionLocalize().get("uz");
        }

        String responsibilitiesEn = "";
        String responsibilitiesRu = "";
        String responsibilitiesUz = "";
        if (item.getResponsibilitiesLocalize() != null && item.getResponsibilitiesLocalize().size() != 0 && !item.getResponsibilitiesLocalize().isEmpty()) {
            responsibilitiesEn = item.getResponsibilitiesLocalize().get("en");
            responsibilitiesRu = item.getResponsibilitiesLocalize().get("ru");
            responsibilitiesUz = item.getResponsibilitiesLocalize().get("uz");
        }

        String jobRequirementsEn = "";
        String jobRequirementsRu = "";
        String jobRequirementsUz = "";
        if (item.getJobRequirementLocalize() != null && item.getJobRequirementLocalize().size() != 0 && !item.getJobRequirementLocalize().isEmpty()) {
            jobRequirementsEn = item.getJobRequirementLocalize().get("en");
            jobRequirementsRu = item.getJobRequirementLocalize().get("ru");
            jobRequirementsUz = item.getJobRequirementLocalize().get("uz");
        }
        String isApprovedBy = item.getApprovalStatusCode() != "" ? item.getApprovalStatusCode() : "";
        String companyPhone = "";
        String companyEmail = "";
        String companyName = "";
        String companyDistrict = "";
        String companyStatedefault = "";
        String companyStateUzName = "";
        String companyStateRuName = "";
        String companyZipCode = "";
        String companyAddress = "";
        String companyFullAddressValue = "";
        StringBuilder addressBuilder = new StringBuilder();
        EdsLocation edsCompany = item.getLocation() != null && item.getLocation().getId() != null ? locationManager.get(item.getLocation().getId()) : null;
        if (edsCompany != null) {
            companyPhone = edsCompany.getPhone() != null ? edsCompany.getPhone() : "";
            companyEmail = edsCompany.getEmail() != null ? edsCompany.getEmail() : "";
            companyName = edsCompany.getName() != null ? edsCompany.getName() : "";
            companyDistrict = edsCompany.getCityDistrict() != null ? edsCompany.getCityDistrict().getName() : "";
            companyStatedefault = edsCompany.getState() != null && edsCompany.getState().getName() != null ? edsCompany.getState().getName() : "";
            companyStateUzName = edsCompany.getState() != null && edsCompany.getState().getName() != null ? edsCompany.getState().getUzName() : "";
            companyStateRuName = edsCompany.getState() != null && edsCompany.getState().getName() != null ? edsCompany.getState().getRuName() : "";
        }
        if (contactItem.getAddresses() != null) {
            for (EdsAddress address : contactItem.getAddresses()) {
                if (address.getZipCode() != null && address.getZipCode().length() > 0) {
                    addressBuilder.append(address.getZipCode());
                    addressBuilder.append(", ");
                }
                if (address.getCity() != null && address.getCity().length() > 0) {
                    addressBuilder.append(address.getCity());
                    addressBuilder.append(", ");
                }
                if (address.getAddress() != null && address.getAddress().length() > 0) {
                    addressBuilder.append(address.getAddress());
                    addressBuilder.append(", ");

                }
                if (address.getAddressb() != null && address.getAddressb().length() > 0) {
                    addressBuilder.append("\n");
                    addressBuilder.append(address.getAddressb());
                }
            }
        }
        companyFullAddressValue = contactItem.getAddresses() != null ? escapeHtml(addressBuilder.toString()) : "-";


        String jobTitle = escapeHtml(item.getJobTitle());
        String status = item.getStatus() != null && item.getStatus().getName() != null ? item.getStatus().getName() : "";
        String gender = escapeHtml(item.getGender());
        String description = item.getDescription().replaceAll("<br>", "<br/>").replaceAll("&nbsp;", " ");
        String vacancyType = escapeHtml(item.getVacancyTypeName());
        String startDate = item.getStartDate() != null ? dateFormat.format(ServerUtils.convertServerDateToUserDate(item.getStartDate(), user.getUserTimezone())) : "";
        String endDate = item.getEndDate() != null ? dateFormat.format(ServerUtils.convertServerDateToUserDate(item.getEndDate(), user.getUserTimezone())) : "";
        String jobRequirements = item.getJobRequirements().replaceAll("<br>", "<br/>").replaceAll("&nbsp;", " ");
        String vacancyPlaceCount = item.getVacantPlaces() != null ? item.getVacantPlaces().toString() : "";
        String proposedSalary = !ServerUtils.isNullOrEmpty(item.getProposedSalary()) ? totalNumberFormat.format(Double.valueOf(item.getProposedSalary())) : totalNumberFormat.format(BigDecimal.ZERO);
        String vacancyNumber = item.getNumberData() != null ? escapeHtml(item.getNumberData().getNumberString()) : "";
        String contractTo = item.getContractTo() != null ? shortDateFormat.format(item.getContractTo()) : "";
        String contractFrom = item.getContractFrom() != null ? shortDateFormat.format(item.getContractFrom()) : "";
        String position = item.getPositionItem() != null ? escapeHtml(item.getPositionItem().getName()) : "";
        String deparment = item.getDepartment() != null ? escapeHtml(item.getDepartment().getName()) : "";
        String approver = item.getLastApprover() != null && item.getLastApprover().getExactEmployee() != null ? escapeHtml(item.getLastApprover().getExactEmployee().getName()) : "";
        String projectName = escapeHtml(item.getProjectName());
//        String embassy = escapeHtml(item.getEmbassyName());
        String manager = item.getManager() != null ? escapeHtml(item.getManager().getName()) : "";
//        String country = escapeHtml(item.getCountryName());
//        String religion = escapeHtml(item.getReligionName());
        String location = item.getLocation() != null ? escapeHtml(locationManager.get(item.getLocation().getId()).getCode()) : "";
        String requireddegree = item.getRequiredDegree() != null ? escapeHtml(item.getRequiredDegree().getName()) : "";
        String jobFamily = item.getJobfamily() != null ? escapeHtml(item.getJobfamily().getName()) : "";
        String jobType = item.getJobType() != null ? escapeHtml(item.getJobType().getName()) : "";
        String responsibilities = item.getResponsibility().replaceAll("<br>", "<br/>").replaceAll("&nbsp;", " ");
        String contractPeriod = escapeHtml(item.getContractPeriod());
        String locale = ServerUtils.getUserLocale() != null ? ServerUtils.getUserLocale().getLanguage() : "";

        String currency = item.getCurrency() != null ? item.getCurrency().getName() : "";

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable vacancyDataTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        vacancyDataTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        vacancyDataTable.addRowWithCode("JOB_TITLE", crmLocalizer.localize("name"), jobTitle);
        vacancyDataTable.addRowWithCode("JOB_TITLE_EN", crmLocalizer.localize("name", "name", new Locale("EN")), jobTitle);
        vacancyDataTable.addRowWithCode("JOB_TITLE_RU", crmLocalizer.localize("name", "name", new Locale("RU")), jobTitle);
        vacancyDataTable.addRowWithCode("JOB_TITLE_UZ", crmLocalizer.localize("name", "name", new Locale("UZ")), jobTitle);
        vacancyDataTable.addRowWithCode("STATUS", commonLocalizer.localize("status"), status);
        vacancyDataTable.addRowWithCode("STATUS_EN", commonLocalizer.localize("status", "status", new Locale("EN")), statusEn);
        vacancyDataTable.addRowWithCode("STATUS_RU", commonLocalizer.localize("status", "status", new Locale("RU")), statusRu);
        vacancyDataTable.addRowWithCode("STATUS_UZ", commonLocalizer.localize("status", "status", new Locale("UZ")), statusUz);
        vacancyDataTable.addRowWithCode("GENDER", commonLocalizer.localize("gender"), gender);
        vacancyDataTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize("description"), description);
        vacancyDataTable.addRowWithCode("DESCRIPTION_EN", commonLocalizer.localize("Single", "description", new Locale("EN")), descriptionEn);
        vacancyDataTable.addRowWithCode("DESCRIPTION_RU", commonLocalizer.localize("description", "description", new Locale("RU")), descriptionRu);
        vacancyDataTable.addRowWithCode("DESCRIPTION_UZ", commonLocalizer.localize("description", "description", new Locale("UZ")), descriptionUz);
        vacancyDataTable.addRowWithCode("VACANCY_TYPE", hrmsLocalizer.localize("vacancyType"), vacancyType);
        vacancyDataTable.addRowWithCode("START_DATE", commonLocalizer.localize("startDate"), startDate);
        vacancyDataTable.addRowWithCode("END_DATE", commonLocalizer.localize("endDate"), endDate);
        vacancyDataTable.addRowWithCode("JOB_REQUIREMENTS", commonLocalizer.localize("jobRequirements"), jobRequirements);
        vacancyDataTable.addRowWithCode("JOB_REQUIREMENTS_EN", commonLocalizer.localize("jobRequirements", "jobRequirements", new Locale("EN")), jobRequirementsEn);
        vacancyDataTable.addRowWithCode("JOB_REQUIREMENTS_RU", commonLocalizer.localize("jobRequirements", "jobRequirements", new Locale("RU")), jobRequirementsRu);
        vacancyDataTable.addRowWithCode("JOB_REQUIREMENTS_UZ", commonLocalizer.localize("jobRequirements", "jobRequirements", new Locale("UZ")), jobRequirementsUz);
        vacancyDataTable.addRowWithCode("VACANCY_PLACE_COUNT", hrmsLocalizer.localize("vacantPlaceCount"), vacancyPlaceCount);
        vacancyDataTable.addRowWithCode("VACANCY_PLACE_COUNT_EN", hrmsLocalizer.localize("vacantPlaceCount", "vacantPlaceCount", new Locale("EN")), vacancyPlaceCount);
        vacancyDataTable.addRowWithCode("VACANCY_PLACE_COUNT_RU", hrmsLocalizer.localize("vacantPlaceCount", "vacantPlaceCount", new Locale("RU")), vacancyPlaceCount);
        vacancyDataTable.addRowWithCode("VACANCY_PLACE_COUNT_UZ", hrmsLocalizer.localize("vacantPlaceCount", "vacantPlaceCount", new Locale("UZ")), vacancyPlaceCount);
        vacancyDataTable.addRowWithCode("PROPOSED_SALARY", commonLocalizer.localize("proposedSalary"), proposedSalary);
        vacancyDataTable.addRowWithCode("PROPOSED_SALARY_EN", commonLocalizer.localize("proposedSalary", "proposedSalary", new Locale("EN")), proposedSalary);
        vacancyDataTable.addRowWithCode("PROPOSED_SALARY_RU", commonLocalizer.localize("proposedSalary", "proposedSalary", new Locale("RU")), proposedSalary);
        vacancyDataTable.addRowWithCode("PROPOSED_SALARY_UZ", commonLocalizer.localize("proposedSalary", "proposedSalary", new Locale("UZ")), proposedSalary);
        vacancyDataTable.addRowWithCode("VACANCY_NUMBER", hrmsLocalizer.localize("number"), vacancyNumber);
        vacancyDataTable.addRowWithCode("CONTRACT_TO", "vacancyContractTo", contractTo);
        vacancyDataTable.addRowWithCode("CONTRACT_FROM", "vacancyContractFrom", contractFrom);
        vacancyDataTable.addRowWithCode("POSITION", commonLocalizer.localize("position"), position);
        vacancyDataTable.addRowWithCode("POSITION_EN", commonLocalizer.localize("position", "position", new Locale("EN")), positionEn);
        vacancyDataTable.addRowWithCode("POSITION_RU", commonLocalizer.localize("position", "position", new Locale("RU")), positionRu);
        vacancyDataTable.addRowWithCode("POSITION_UZ", commonLocalizer.localize("position", "position", new Locale("UZ")), positionUz);
        vacancyDataTable.addRowWithCode("DEPARTMENT", commonLocalizer.localize("department"), deparment);
        vacancyDataTable.addRowWithCode("DEPARTMENT_EN", commonLocalizer.localize("department", "department", new Locale("EN")), departmentEn);
        vacancyDataTable.addRowWithCode("DEPARTMENT_RU", commonLocalizer.localize("department", "department", new Locale("RU")), departmentRu);
        vacancyDataTable.addRowWithCode("DEPARTMENT_UZ", commonLocalizer.localize("department", "department", new Locale("UZ")), departmentUz);
        vacancyDataTable.addRowWithCode("APPROVER", commonLocalizer.localize("approver"), approver);
        vacancyDataTable.addRowWithCode("APPROVER_EN", commonLocalizer.localize("approver", "approver", new Locale("EN")), approver);
        vacancyDataTable.addRowWithCode("APPROVER_RU", commonLocalizer.localize("approver", "approver", new Locale("RU")), approver);
        vacancyDataTable.addRowWithCode("APPROVER_UZ", commonLocalizer.localize("approver", "approver", new Locale("UZ")), approver);
        vacancyDataTable.addRowWithCode("PROJECT_NAME", commonLocalizer.localize("project"), projectName);
//        vacancyDataTable.addRowWithCode("EMBASSY", commonLocalizer.localize("embassyName"), embassy);
        vacancyDataTable.addRowWithCode("MANAGER", commonLocalizer.localize("manager"), manager);
        vacancyDataTable.addRowWithCode("MANAGER_EN", commonLocalizer.localize("manager", "manager", new Locale("EN")), manager);
        vacancyDataTable.addRowWithCode("MANAGER_RU", commonLocalizer.localize("manager", "manager", new Locale("RU")), manager);
        vacancyDataTable.addRowWithCode("MANAGER_UZ", commonLocalizer.localize("manager", "manager", new Locale("UZ")), manager);
//        vacancyDataTable.addRowWithCode("COUNTRY", commonLocalizer.localize("country"), country);
//        vacancyDataTable.addRowWithCode("RELIGION", commonLocalizer.localize("religion"), religion);
        vacancyDataTable.addRowWithCode("LOCATION", commonLocalizer.localize("location"), location);
        vacancyDataTable.addRowWithCode("REQUIRED_DEGREE", hrmsLocalizer.localize("requiredDegree"), requireddegree);
        vacancyDataTable.addRowWithCode("REQUIRED_DEGREE_EN", hrmsLocalizer.localize("requiredDegree", "requiredDegree", new Locale("EN")), requireddegreeEn);
        vacancyDataTable.addRowWithCode("REQUIRED_DEGREE_RU", hrmsLocalizer.localize("requiredDegree", "requiredDegree", new Locale("RU")), requireddegreeRu);
        vacancyDataTable.addRowWithCode("REQUIRED_DEGREE_UZ", hrmsLocalizer.localize("requiredDegree", "requiredDegree", new Locale("UZ")), requireddegreeUz);
        vacancyDataTable.addRowWithCode("JOB_FAMILY", commonLocalizer.localize("jobFamily"), jobFamily);
        vacancyDataTable.addRowWithCode("JOB_TYPE", commonLocalizer.localize("jobType"), jobType);
        vacancyDataTable.addRowWithCode("JOB_TYPE_EN", commonLocalizer.localize("jobType", "jobType", new Locale("EN")), jobTypeEn);
        vacancyDataTable.addRowWithCode("JOB_TYPE_RU", commonLocalizer.localize("jobType", "jobType", new Locale("RU")), jobTypeRu);
        vacancyDataTable.addRowWithCode("JOB_TYPE_UZ", commonLocalizer.localize("jobType", "jobType", new Locale("UZ")), jobTypeUz);
        vacancyDataTable.addRowWithCode("RESPONSIBILITIES", hrmsLocalizer.localize("responsibilities"), responsibilities);
        vacancyDataTable.addRowWithCode("RESPONSIBILITIES_EN", hrmsLocalizer.localize("responsibilities", "responsibilities", new Locale("EN")), responsibilitiesEn);
        vacancyDataTable.addRowWithCode("RESPONSIBILITIES_RU", hrmsLocalizer.localize("responsibilities", "responsibilities", new Locale("RU")), responsibilitiesRu);
        vacancyDataTable.addRowWithCode("RESPONSIBILITIES_UZ", hrmsLocalizer.localize("responsibilities", "responsibilities", new Locale("UZ")), responsibilitiesUz);
        vacancyDataTable.addRowWithCode("CONTRACT_PERIOD_LABEL", commonLocalizer.localize("period"), contractPeriod);
        vacancyDataTable.addRowWithCode("CONTRACT_PERIOD_LABEL_EN", commonLocalizer.localize("period", "period", new Locale("EN")), contractPeriod);
        vacancyDataTable.addRowWithCode("CONTRACT_PERIOD_LABEL_RU", commonLocalizer.localize("period", "period", new Locale("RU")), contractPeriod);
        vacancyDataTable.addRowWithCode("CONTRACT_PERIOD_LABEL_UZ", commonLocalizer.localize("period", "period", new Locale("UZ")), contractPeriod);
        vacancyDataTable.addRowWithCode("LOCALE", "locale", locale);
        vacancyDataTable.addRowWithCode("COMPANY_PHONE", commonLocalizer.localize("phone"), companyPhone);
        vacancyDataTable.addRowWithCode("COMPANY_NAME", commonLocalizer.localize("name"), companyName);
        vacancyDataTable.addRowWithCode("COMPANY_EMAIL", commonLocalizer.localize("email"), companyEmail);
        vacancyDataTable.addRowWithCode("COMPANY_DISTRICT", commonLocalizer.localize("cityOrDistrict"), companyDistrict);
        vacancyDataTable.addRowWithCode("COMPANY_STATEDEFAULT", commonLocalizer.localize("state"), companyStatedefault);
        vacancyDataTable.addRowWithCode("COMPANY_STATE_UZ", commonLocalizer.localize("state", "state", new Locale("UZ")), companyStateUzName);
        vacancyDataTable.addRowWithCode("COMPANY_STATE_RU", commonLocalizer.localize("state", "state", new Locale("RU")), companyStateRuName);
        vacancyDataTable.addRowWithCode("COMPANY_ZIPCODE", commonLocalizer.localize("postCode"), companyZipCode);
        vacancyDataTable.addRowWithCode("COMPANY_ADDRESS", commonLocalizer.localize("address"), companyAddress);
        vacancyDataTable.addRowWithCode("FULL_ADDRESS", commonLocalizer.localize("addresses"), companyFullAddressValue);
        vacancyDataTable.addRowWithCode("APPROVAL_STATUS", commonLocalizer.localize("approverStatus"), isApprovedBy);
        vacancyDataTable.addRowWithCode("CURRENCY", commonLocalizer.localize("currency"), currency);

        customData.put("CUSTOM_FIELD", customFieldData(item));
        customData.put("LOCATIONCUSTOM_FIELD", locationCustomFieldData(edsCompany.getCustomFields()));

        EdsVacancy vacancy = vacancyManager.get(vacancyId);
        if (vacancy != null && vacancy.getItemTables() != null && vacancy.getItemTables().size() > 0) {
            customData.put("ITEM_TABLE", getItemTable(item));
        }


        if (item.getCustomFieldItems().size() > 6 && item.getCustomFieldItems().get(6) != null && item.getCustomFieldItems().get(6).getLocalization() != null) {
            List<EdsCustomFormLocalization> predefinedValues = customFormLocalizationManager.getPredefinedValues(item.getCustomFieldItems().get(6).getLocalization().getId());
            for (EdsCustomFormLocalization predefinedValue : predefinedValues) {
                assert vacancy != null;
                if (predefinedValue.getDefaultName().equals(vacancy.getVacancyCustomFields().getStringValue7())) {
                    vacancyDataTable.addRowWithCode("AGE_UZ", predefinedValue.getUzbekName());
                    vacancyDataTable.addRowWithCode("AGE_RU", predefinedValue.getRussianName());
                    vacancyDataTable.addRowWithCode("AGE_EN", predefinedValue.getEnglishName());
                }
            }
        }
        if (vacancy != null && vacancy.getGender() != null) {
            vacancyDataTable.addRowWithCode("GENDER_UZ", commonLocalizer.localize(vacancy.getGender().toLowerCase(), vacancy.getGender(), new Locale("UZ")));
            vacancyDataTable.addRowWithCode("GENDER_RU", commonLocalizer.localize(vacancy.getGender().toLowerCase(), vacancy.getGender(), new Locale("RU")));
            vacancyDataTable.addRowWithCode("GENDER_EN", commonLocalizer.localize(vacancy.getGender().toLowerCase(), vacancy.getGender(), new Locale("EN")));
        }
        if (item.getSpokenLanguages() != null && !item.getSpokenLanguages().isEmpty()) {
            vacancyDataTable.addRowWithCode("SPOKEN_LANGUAGE_UZ", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLanguage().getId()).getCode(), item.getSpokenLanguages().get(0).getLanguage().getName(), new Locale("UZ")));
            vacancyDataTable.addRowWithCode("SPOKEN_LANGUAGE_RU", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLanguage().getId()).getCode(), item.getSpokenLanguages().get(0).getLanguage().getName(), new Locale("RU")));
            vacancyDataTable.addRowWithCode("SPOKEN_LANGUAGE_EN", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLanguage().getId()).getCode(), item.getSpokenLanguages().get(0).getLanguage().getName(), new Locale("EN")));
        }
        if (item.getSpokenLanguages() != null && !item.getSpokenLanguages().isEmpty()) {
            vacancyDataTable.addRowWithCode("SPOKEN_LEVEL_UZ", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLevel().getId()).getCode(), item.getSpokenLanguages().get(0).getLevel().getName(), new Locale("UZ")));
            vacancyDataTable.addRowWithCode("SPOKEN_LEVEL_RU", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLevel().getId()).getCode(), item.getSpokenLanguages().get(0).getLevel().getName(), new Locale("RU")));
            vacancyDataTable.addRowWithCode("SPOKEN_LEVEL_EN", referenceWfmMessageSource.localize(referenceManager.get(item.getSpokenLanguages().get(0).getLevel().getId()).getCode(), item.getSpokenLanguages().get(0).getLevel().getName(), new Locale("EN")));

        }


        pdfData.setCustomData(customData);
        baseInvoice.setCustomNumberAndDatesTable(vacancyDataTable);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private CustomisedITextTable getItemTable(VacancyItem vacancy) {
        Map<String, ArrayList<String>> table = new HashMap<>();
        CustomisedITextTable vacancyItemTable = new CustomisedITextTable();
        vacancyItemTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Map<String, ArrayList<CustomTableRpc>> items = vacancy.getCustomTableItems();

        for (Map.Entry<String, ArrayList<CustomTableRpc>> item : items.entrySet()) {
            for (CustomTableRpc rpc : item.getValue()) {
                ArrayList<String> tableRows;
                for (CompanyCustomFieldItem fieldItem : rpc.getItemCustomFields()) {
                    if (table.containsKey(fieldItem.getFieldName())) {
                        tableRows = table.get(fieldItem.getFieldName());
                        tableRows.add(fieldItem.getFieldStringValue());
                        table.put(fieldItem.getFieldName(), tableRows);
                    } else {
                        tableRows = new ArrayList<>();
                        tableRows.add(fieldItem.getFieldStringValue());
                        table.put(fieldItem.getFieldName(), tableRows);
                    }
                }
            }
        }
        Map<String, ArrayList<String>> newMapSortedByKey = table.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, ArrayList<String>> column : newMapSortedByKey.entrySet()) {
            vacancyItemTable.addRowWithCode(column.getKey(), column.getValue().toArray(new String[]{}));
        }
        return vacancyItemTable;
    }

    public CustomisedITextTable customFieldData(VacancyItem item) {
        DecimalFormat totalNumberFormat = new DecimalFormat(" ##0");
        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        customFieldTable.addRowWithCode("additionalInformation_uz", commonLocalizer.localize(PdfLocalizationName.additionalInformation, "additionalInformation", new Locale("UZ")));
        customFieldTable.addRowWithCode("additionalInformation_ru", commonLocalizer.localize(PdfLocalizationName.additionalInformation, "additionalInformation", new Locale("RU")));
        customFieldTable.addRowWithCode("additionalInformation_en", commonLocalizer.localize(PdfLocalizationName.additionalInformation, "additionalInformation", new Locale("EN")));

        if (item != null && item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem customField : item.getCustomFieldItems()) {
                switch (customField.getDataType()) {
                    case DATA_TYPE_DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = escapeHtml(dateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue, DATA_TYPE_DATE);
                    }
                    case DATA_TYPE_NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = escapeHtml(totalNumberFormat.format(Double.valueOf(customField.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue, DATA_TYPE_NUMBER);
                    }
                    case DATA_TYPE_TEXT -> {
                        if (UI_TYPE_HTML_TEXTAREA.equals(customField.getUiType())) {
                            String html = customField.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(doc.body().text()), UI_TYPE_HTML_TEXTAREA);
                        } else if (UI_TYPE_LOOKUP.equals(customField.getUiType())) {
                            customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(customField.getFieldStringValue()), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(customField.getFieldStringValue()), UI_TYPE_HTML_TEXTAREA);
                        }
                    }
                    case DATA_TYPE_PROFILE_IMAGE -> {
                        String uploadImageId = "";
                        if (customField.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(customField.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                    }
                    default ->
                            customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()), DATA_TYPE_TEXT);
                }
            }
        }
        return customFieldTable;
    }

    public CustomisedITextTable locationCustomFieldData(EdsLocationCustomFields locationCustomFields) {
        ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Location);
        ArrayList<CompanyCustomFieldItem> customFieldItems1 = CustomFieldsUtils.setRPCCustomFieldItems(locationCustomFields, customFieldItems);


        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        if (locationCustomFields != null) {
            for (CompanyCustomFieldItem customField : customFieldItems1) {
                if (DATA_TYPE_TEXT.equals(customField.getDataType())) {
                    if (UI_TYPE_HTML_TEXTAREA.equals(customField.getUiType())) {
                        String html = customField.getFieldStringValue();
                        org.jsoup.nodes.Document doc = Jsoup.parse(html);
                        customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(doc.body().text()), UI_TYPE_HTML_TEXTAREA);
                    } else if (UI_TYPE_LOOKUP.equals(customField.getUiType())) {
                        customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(customField.getFieldStringValue()), UI_TYPE_HTML_TEXTAREA);
                    } else {
                        customFieldTable.addRowWithCode(customField.getAliasName(), customField.getAliasName(), escapeHtml(customField.getFieldStringValue()), UI_TYPE_HTML_TEXTAREA);
                    }
                } else {
                    customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()), DATA_TYPE_TEXT);
                }
            }
        }
        return customFieldTable;
    }


    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        setFileName("Vacancy");
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof LeaveRequestObject) {
            return ((LeaveRequestObject) object).getPdfTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer vacancyId = requestObject.getObjectID();
        VacancyItem item = recruitmentService.getVacancyItem(vacancyId);
        String vacancyNumber = item.getNumberData() != null ? escapeHtml(item.getNumberData().getNumberString()) : "";
        String jobTitle = escapeHtml(item.getJobTitle());

        return vacancyNumber + " - " + jobTitle;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.VACANCY;
    }
}
