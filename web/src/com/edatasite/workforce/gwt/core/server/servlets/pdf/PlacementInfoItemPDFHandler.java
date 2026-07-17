package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsLocationCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PlacementInfoItemPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, IPostPDFHandler {

    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    VacancyManager vacancyManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    private final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactService contactService;
    @Autowired
    private PlacementItemTableManager placementItemTableManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        LeaveRequestObject requestObject = new LeaveRequestObject();
        String sessionId = request.getParameter("sessionId");
        if (StringUtils.isNotBlank(request.getParameter("templateId"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("templateId")));
        }
        if (StringUtils.isNotBlank(sessionId)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat uzDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsUser edsUser = placementManager.getUser();
        ArrayList<EdsEmployee> directors = (ArrayList<EdsEmployee>) employeeManager.getDirectors();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());
        SimpleDateFormat dateSlashFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat totalNumberFormat = getQtyNumberFormat(edsUser.getCompany(), null);

        RequestObject requestObject = (RequestObject) dataClass;
        Integer placementId = requestObject.getObjectID();
        PlacementItem placementItem = recruitmentService.getPlacementItem(placementId, null, null);

        if (placementItem == null) {
            return pdfData;
        }

        EdsCrmContact contactItem = crmContactManager.get(placementItem.getCandidateID());
        EdsLocation edsLocation = null;

        String candidateLocationName = "";
        String locationLocaleEnglish = "";
        String locationLocaleRussian = "";
        String locationLocaleUzbek = "";
        String locationCity = "";
        if (contactItem != null) {
            edsLocation =  contactItem.getPrefferedLocation();
            if (edsLocation != null) {
                candidateLocationName = !edsLocation.getName().isEmpty() ? edsLocation.getName() : "";
                locationCity = edsLocation.getState() != null ? edsLocation.getState().getUzName() != null ? edsLocation.getState().getUzName() : edsLocation.getState().getName() != null ? edsLocation.getState().getName() : "" : "";

                if (edsLocation.getLocale() != null) {
                    locationLocaleEnglish = edsLocation.getLocale().getEnglish() != null ? edsLocation.getLocale().getEnglish() : "";
                    locationLocaleRussian = edsLocation.getLocale().getRussian() != null ? edsLocation.getLocale().getRussian() : "";
                    locationLocaleUzbek =   edsLocation.getLocale().getUzbek()   != null ?   edsLocation.getLocale().getUzbek() : "";
                }
            }
        }

        String candidate = placementItem.getCandidateName() != null ? escapeHtml(placementItem.getCandidateName()) : "-";
        String department = placementItem.getDepartmentName() != null ? escapeHtml(placementItem.getDepartmentName()) : "-";
        String location = placementItem.getLocationName() != null ? escapeHtml(placementItem.getLocationName()) : "-";
        String placementCode = placementItem.getNumberData() != null ? escapeHtml(placementItem.getNumberData().getFirstNumberString()) : "-";
        String position = placementItem.getPositionName() != null ? escapeHtml(placementItem.getPositionName()) : "-";
        String dateOffered = placementItem.getDateOffed() != null ? shortDateFormat.format(placementItem.getDateOffed()) : "-";
        String dateOfferedSlashFormat = placementItem.getDateOffed() != null ? dateSlashFormat.format(placementItem.getDateOffed()) : "-";
        String dateOfferedRu = placementItem.getDateOffed() != null ? dateFormat.format(placementItem.getDateOffed()) : "";
        String dateOfferedUz = placementItem.getDateOffed() != null ? uzDateFormat.format(placementItem.getDateOffed()) : "";

        EdsDepartment edsDepartment = placementItem.getDepartmentID() != null ? departmentManager.get(placementItem.getDepartmentID()) : null;
        String departmentEn = "";
        String departmentRu = "";
        String departmentUz = "";
        if (edsDepartment != null && edsDepartment.getLocale() != null) {
            departmentEn = edsDepartment.getLocale().getEnglish();
            departmentRu = edsDepartment.getLocale().getRussian();
            departmentUz = edsDepartment.getLocale().getUzbek();
        }

        EdsPosition edsPosition = placementItem.getPositionID() != null ? positionManager.get(placementItem.getPositionID()) : null;
        String positionEn = "", positionRu = "", positionUz = "", positionId = "";
        if (edsPosition != null && edsPosition.getLocale() != null) {
            positionEn = edsPosition.getLocale().getEnglish();
            positionRu = edsPosition.getLocale().getRussian();
            positionUz = edsPosition.getLocale().getUzbek();
            positionId = edsPosition.getObjectID().toString();
        }

        String jobReq = "", description = "", responsibility = "";
        Map<String, String> jobReqLocaleMap = new HashMap<>(), descriptionLocaleMap = new HashMap<>(), responsibilityLocaleMap = new HashMap<>();
        if (edsPosition != null) {
            jobReqLocaleMap = getLocaleMap(edsPosition.getJobRequirementsLocalize());
            descriptionLocaleMap = getLocaleMap(edsPosition.getDescriptionLocalize());
            responsibilityLocaleMap = getLocaleMap(edsPosition.getResponsibilityLocalize());

            jobReq = !ServerUtils.isNullOrEmpty(edsPosition.getJobrequirements()) ? edsPosition.getJobrequirements() : "";
            description = !ServerUtils.isNullOrEmpty(edsPosition.getDetailingDescription()) ? edsPosition.getDetailingDescription() : "";
            responsibility = !ServerUtils.isNullOrEmpty(edsPosition.getResponsibility()) ? edsPosition.getResponsibility() : "";
        }

        ArrayList<SelectItem> placementVacancies = recruitmentService.getPlacementVacancies(placementId, placementItem.getCandidateID());
        Integer vacancyId = null;
        StringBuilder s = new StringBuilder();
        String jobrequirements = new String();
        for (int i = 0; i < placementVacancies.size(); i++) {
            s.append(placementVacancies.get(i).getName());
            s.append("\n");
            vacancyId = placementVacancies.get(i).getId();
            EdsVacancy edsVacancy = vacancyManager.get(vacancyId);
            jobrequirements = edsVacancy.getJobrequirements();
        }
        String matchedVacancies = placementVacancies != null ? escapeHtml(s.toString()) : "-";

        String expected_salary = contactItem.getExpectedSalary() != null ? totalNumberFormat.format(contactItem.getExpectedSalary()) : totalNumberFormat.format(BigDecimal.ZERO);
        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }

        NumberToWord numberToWordConverter, numberToWordConverterUzLotin;
        String expectedSalaryInWord = "", expectedSalaryInWordLotin = "";

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable placementDataTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        numberToWordConverter = new NumberToWord_uz();
        numberToWordConverterUzLotin = new NumberToWord_uz_lotin();
        if (contactItem.getExpectedSalary() != null) {
            expectedSalaryInWord = numberToWordConverter.convert(BigDecimal.valueOf(contactItem.getExpectedSalary()).abs().setScale(scale, RoundingMode.HALF_UP));
            expectedSalaryInWordLotin = numberToWordConverterUzLotin.convert(BigDecimal.valueOf(contactItem.getExpectedSalary()).abs().setScale(scale, RoundingMode.HALF_UP));
        }
        numberToWordConverter = new NumberToWord_ru();
        if (contactItem.getExpectedSalary() != null) {
            expectedSalaryInWord = numberToWordConverter.convert(BigDecimal.valueOf(contactItem.getExpectedSalary()).abs().setScale(scale, RoundingMode.HALF_UP));
        }
        numberToWordConverter = new NumberToWord_en();
        if (contactItem.getExpectedSalary() != null) {
            expectedSalaryInWord = numberToWordConverter.convert(BigDecimal.valueOf(contactItem.getExpectedSalary()).abs().setScale(scale, RoundingMode.HALF_UP));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMMM.yyyy");
        Date currentDate = new Date();
        placementDataTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        placementDataTable.addRowWithCode("CANDIDATE_EXPECTED_SALARY_IN_WORD", commonLocalizer.localize("candidateExpectedSalaryInWord"), expectedSalaryInWord);
        placementDataTable.addRowWithCode("CANDIDATE_EXPECTED_SALARY_IN_WORD_LOTIN", commonLocalizer.localize("candidateExpectedSalaryInWord"), expectedSalaryInWordLotin);
        placementDataTable.addRowWithCode("CANDIDATE", commonLocalizer.localize("candidate"), candidate);
        placementDataTable.addRowWithCode("CANDIDATE_EXPECTED_SALARY", commonLocalizer.localize("candidateExpectedSalary"), expected_salary);
        placementDataTable.addRowWithCode("DEPARTMENT", commonLocalizer.localize("department"), department);
        placementDataTable.addRowWithCode("DEPARTMENT_EN", "departmentEn", departmentEn);
        placementDataTable.addRowWithCode("DEPARTMENT_RU", "departmentRu", departmentRu);
        placementDataTable.addRowWithCode("DEPARTMENT_UZ", "departmentUz", departmentUz);
        placementDataTable.addRowWithCode("LOCATION", commonLocalizer.localize("location"), location);
        placementDataTable.addRowWithCode("PLACEMENT_CODE", commonLocalizer.localize("code"), placementCode);
        placementDataTable.addRowWithCode("POSITION", commonLocalizer.localize("position"), position);
        placementDataTable.addRowWithCode("POSITION_EN", "positionEn", positionEn);
        placementDataTable.addRowWithCode("POSITION_RU", "positionRu", positionRu);
        placementDataTable.addRowWithCode("POSITION_UZ", "positionUz", positionUz);
        placementDataTable.addRowWithCode("MATCHED_VACANCIES", "matchedVacancies", matchedVacancies);
        placementDataTable.addRowWithCode("VACANCY_JOB_REQUIREMENTS", "vacancyJobRequirements", jobrequirements);
        placementDataTable.addRowWithCode("DATE_OFFERED", "dateOffered", dateOffered);
        placementDataTable.addRowWithCode("DATE_OFFERED_SLASH_FORMAT", "dateOfferedSlashFormat", dateOfferedSlashFormat);
        placementDataTable.addRowWithCode("DATE_OFFERED_UZ", "dateOfferedUz", dateOfferedUz);
        placementDataTable.addRowWithCode("DATE_OFFERED_RU", "dateOfferedRu", dateOfferedRu);
        placementDataTable.addRowWithCode("CURRENT_DATE", "", simpleDateFormat.format(currentDate));
        placementDataTable.addRowWithCode("CURRENT_DATE_SLASH_FORMAT", "", dateSlashFormat.format(currentDate));
        placementDataTable.addRowWithCode("CURRENT_TIME", "", timeFormat.format(userManager.getUser().getUserDate()));
        placementDataTable.addRowWithCode("CURRENT_DATE_RU", "", dateFormat.format(currentDate));
        placementDataTable.addRowWithCode("CURRENT_DATE_UZ", "", uzDateFormat.format(currentDate));
        placementDataTable.addRowWithCode("STATUS", "", placementItem.getStatusCode());
        placementDataTable.addRowWithCode("POSITION_ID", "", positionId);
        placementDataTable.addRowWithCode("JOB_REQUIREMENTS", commonLocalizer.localize("jobRequirements"), jobReq);
        placementDataTable.addRowWithCode("JOB_REQUIREMENTS_LOCALE_EN", commonLocalizer.localize("jobRequirements"), getLocaleValue(jobReqLocaleMap, "en"));
        placementDataTable.addRowWithCode("JOB_REQUIREMENTS_LOCALE_RU", commonLocalizer.localize("jobRequirements"), getLocaleValue(jobReqLocaleMap, "ru"));
        placementDataTable.addRowWithCode("JOB_REQUIREMENTS_LOCALE_UZ", commonLocalizer.localize("jobRequirements"), getLocaleValue(jobReqLocaleMap, "uz"));
        placementDataTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description), description);
        placementDataTable.addRowWithCode("DESCRIPTION_LOCALE_EN", commonLocalizer.localize(PdfLocalizationName.description), getLocaleValue(descriptionLocaleMap, "en"));
        placementDataTable.addRowWithCode("DESCRIPTION_LOCALE_RU", commonLocalizer.localize(PdfLocalizationName.description), getLocaleValue(descriptionLocaleMap, "ru"));
        placementDataTable.addRowWithCode("DESCRIPTION_LOCALE_UZ", commonLocalizer.localize(PdfLocalizationName.description), getLocaleValue(descriptionLocaleMap, "uz"));
        placementDataTable.addRowWithCode("RESPONSIBILITY", commonLocalizer.localize("responsibilities"), responsibility);
        placementDataTable.addRowWithCode("RESPONSIBILITY_LOCALE_EN", commonLocalizer.localize("responsibilities"), getLocaleValue(responsibilityLocaleMap, "en"));
        placementDataTable.addRowWithCode("RESPONSIBILITY_LOCALE_RU", commonLocalizer.localize("responsibilities"), getLocaleValue(responsibilityLocaleMap, "ru"));
        placementDataTable.addRowWithCode("RESPONSIBILITY_LOCALE_UZ", commonLocalizer.localize("responsibilities"), getLocaleValue(responsibilityLocaleMap, "uz"));

        placementDataTable.addRowWithCode("CANDIDATE_LOCATION_NAME", "", candidateLocationName);
        placementDataTable.addRowWithCode("CANDIDATE_LOCATION_LOCALE_EN", "", locationLocaleEnglish);
        placementDataTable.addRowWithCode("CANDIDATE_LOCATION_LOCALE_RU", "", locationLocaleRussian);
        placementDataTable.addRowWithCode("CANDIDATE_LOCATION_LOCALE_UZ", "", locationLocaleUzbek);
        placementDataTable.addRowWithCode("CANDIDATE_LOCATION_ADDRESS_CITY", "", locationCity);

        ContactListItem contactListItem = contactService.getContact(placementItem.getCandidateID(), true);
        ProfileItem profile = null;
        EdsEmployeeProfile employeeProfile = null;
        if (employeeManager.getEmployeeByPlacementIds(placementId) != null && hrmsServiceLocal.getProfile(employeeManager.getEmployeeByPlacementIds(placementId).getObjectID()) != null) {
            EdsEmployee employeeByPlacementIds = employeeManager.getEmployeeByPlacementIds(placementId);
            profile = hrmsServiceLocal.getProfile(employeeByPlacementIds.getObjectID());
            employeeProfile = employeeByPlacementIds.getProfile();
            String passportExpiryDate = employeeProfile.getPassportExpiryDate()!=null ? dateFormat.format(employeeProfile.getPassportExpiryDate()) :"";
            String passportIssueDate = employeeProfile.getPassportIssueDate() !=null ? dateFormat.format(employeeProfile.getPassportIssueDate()) : "";
            EdsDepartment department1 = departmentManager.get(employeeByPlacementIds.getTeam().getObjectID());
            placementDataTable.addRowWithCode("DEPARTMENT_BY_EMPLOYEE_ENG", "", department1.getLocale() != null ? department1.getLocale().getEnglish() : "");
            placementDataTable.addRowWithCode("DEPARTMENT_BY_EMPLOYEE_UZ", "", department1.getLocale() != null ? department1.getLocale().getUzbek() : "");
            placementDataTable.addRowWithCode("PASSPORT_NUMBER2", "", employeeProfile.getPassportNumber().trim());
            placementDataTable.addRowWithCode("PASSPORT_EXPIRY_DATE2", "", escapeHtml(passportExpiryDate));
            placementDataTable.addRowWithCode("PASSPORT_ISSUED_DATE2", "", escapeHtml(passportIssueDate));
            if ((employeeByPlacementIds.getContact().getAddresses().size() > 0 && employeeByPlacementIds.getContact().getAddresses().get(0) != null)) {
                placementDataTable.addRowWithCode("EMPLOYEE_ADDRESS", "", employeeByPlacementIds.getContact().getAddresses().get(0).getName());
            }
        }

        String candidateFullName = contactItem.getName() != null ? escapeHtml(contactItem.getName()) : "-";
        String candidateFirstname = contactItem.getFirstName() != null ? escapeHtml(contactItem.getFirstName()) : "-";
        String candidateLastname = contactItem.getLastName() != null ? escapeHtml(contactItem.getLastName()) : "-";
        String candidateMiddlename = "";
        String hireDate = "";
        String hireDateUz = "";
        String hireDateRu = "";
        String probationDate = "";
        int probationMonth = 0;
        if (profile != null) {
            probationMonth = (int) (employeeManager.get(profile.getEmployeeId()) != null ? employeeManager.get(profile.getEmployeeId()).getProbationDays() / 30 : 0);
            probationDate = String.valueOf(employeeManager.get(profile.getEmployeeId()) != null ? String.valueOf(probationMonth) : '-');
        }
        String timeSlotStartHour = "-";
        String timeSlotEndHour = "-";
        if (commonServiceLocal != null && employeeManager.getEmployeeByPlacementIds(placementId) != null) {
            timeSlotStartHour = commonServiceLocal.getEmployeeTimeSlot(employeeManager.getEmployeeByPlacementIds(placementId).getObjectID()).getStartHour();
            timeSlotEndHour = commonServiceLocal.getEmployeeTimeSlot(employeeManager.getEmployeeByPlacementIds(placementId).getObjectID()).getEndHour();
        }
        if (employeeManager.getEmployeeByPlacementIds(placementId) != null) {
            candidateMiddlename = employeeManager.getEmployeeByPlacementIds(placementId).getMiddleName() != null ? employeeManager.getEmployeeByPlacementIds(placementId).getMiddleName() : "-";
        } else {
            candidateMiddlename = contactItem.getMiddleName();
        }
        if (profile != null) {
            hireDate = profile.getHireDate() != null ? shortDateFormat.format(profile.getHireDate().getDate()) : "-";
            hireDateUz = profile.getHireDate() != null ? uzDateFormat.format(profile.getHireDate().getDate()) : "-";
            hireDateRu = profile.getHireDate() != null ? dateFormat.format(profile.getHireDate().getDate()) : "-";
        }

        placementDataTable.addRowWithCode("TIMESLOT_START", "timeslotStart", timeSlotStartHour);
        placementDataTable.addRowWithCode("TIMESLOT_END", "timeslotEnd", timeSlotEndHour);
        placementDataTable.addRowWithCode("PROBATION_DAYS", "probationDate", probationDate);
        placementDataTable.addRowWithCode("CANDIDATE_FULL_NAME", "candidateFullname", candidateFullName);
        placementDataTable.addRowWithCode("CANDIDATE_FIRST_NAME", "candidateFirstname", candidateFirstname);
        placementDataTable.addRowWithCode("CANDIDATE_MIDDLE_NAME", "candidateMiddlename", candidateMiddlename);
        placementDataTable.addRowWithCode("CANDIDATE_LAST_NAME", "candidateLastname", candidateLastname);
        placementDataTable.addRowWithCode("HIRE_DATE", "hireDate", hireDate);
        placementDataTable.addRowWithCode("HIRE_DATE_UZ", "hireDate", hireDateUz);
        placementDataTable.addRowWithCode("HIRE_DATE_RU", "hireDate", hireDateRu);
        String dateOfBirth = contactItem.getDateOfBirth() != null ? shortDateFormat.format(contactItem.getDateOfBirth()) : "-";
        placementDataTable.addRowWithCode("DATE_OF_BIRTH", commonLocalizer.localize("dateOfBirth"), dateOfBirth);
        placementDataTable.addRowWithCode("CANDIDATE_PHONE", "phone", escapeHtml(contactItem.getPrimaryPhone()));

        if (contactListItem.getCustomFields() != null && contactListItem.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem companyCustomFieldItem : contactListItem.getCustomFields()) {
                placementDataTable.addRowWithCode(companyCustomFieldItem.getAliasName(), companyCustomFieldItem.getColumnCode(), companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
            }
        }
        if (contactListItem.getNumberData() != null) {
            String candidateNumber = contactListItem.getNumberData().getNumberString();
            placementDataTable.addRowWithCode("CANDIDATE_NUMBER", "candidateNumber", candidateNumber);
        }


        StringBuilder addressBuilder = new StringBuilder();
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
        String address = contactItem.getAddresses() != null ? escapeHtml(addressBuilder.toString()) : "-";
        placementDataTable.addRowWithCode("ADDRESS", commonLocalizer.localize("address"), address);
        placementDataTable.addRowWithCode("FULL_NAME", commonLocalizer.localize("fullName"), candidateFullName);


        baseInvoice.setCustomNumberAndDatesTable(placementDataTable);
        if (profile != null) {
            customData.put("EMPLOYEE_INFO", employeeCustomField(profile));
        }
        if (edsLocation != null) {
            customData.put("LOCATIONCUSTOM_FIELD", locationCustomFieldData(edsLocation.getCustomFields()));
        }
        VacancyItem item = recruitmentService.getVacancyItem(vacancyId);
        customData.put("CUSTOM_FIELD", customFieldData(placementItem));
        customData.put("VACANCY_CUSTOM_FIELD", customFieldData(item));
        customData.put("ALLOWANCE_CATEGORIES", getAllowanceCategoriesTable(placementItem, totalNumberFormat));

        EdsPlacement placement = placementManager.get(placementId);
        if (placement.getItemTables() != null && placement.getItemTables().size() > 0) {
            customData.put("ITEM_TABLE", getItemTable(placement));
        }
        if (contactItem != null && !contactItem.getVacancies().isEmpty()) {
            customData.put("CANDIDIDATE_VACANCIES", getCandidateVacancies(contactItem));
        }

        pdfData.setCompanyData(getCompanyData(edsUser.getCompany(), true, hasPhantom));
        pdfData.setCustomData(customData);
        baseInvoice.setObjectId(placementItem.getObjectID());
        pdfData.setUserId(edsUser.getObjectID().toString());
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setExtraData(getDirectors(placement));

        return pdfData;
    }

    private CustomisedITextTable getCandidateVacancies(EdsCrmContact contactItem) {
        CustomisedITextTable vacansies = new CustomisedITextTable();
        vacansies.addColumn("VACANCY_NAME", commonLocalizer.localize(PdfLocalizationName.vacancy));
        vacansies.addColumn("JOB_REQUIREMENTS", commonLocalizer.localize("jobRequirements"));
        vacansies.addColumn("DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description));
        vacansies.addColumn("RESPONSIBILITY", commonLocalizer.localize("responsibilities"));
        vacansies.addColumn("JOB_REQUIREMENTS_LOCALE_EN", commonLocalizer.localize("jobRequirements"));
        vacansies.addColumn("JOB_REQUIREMENTS_LOCALE_RU", commonLocalizer.localize("jobRequirements"));
        vacansies.addColumn("JOB_REQUIREMENTS_LOCALE_UZ", commonLocalizer.localize("jobRequirements"));
        vacansies.addColumn("DESCRIPTION_LOCALE_EN", commonLocalizer.localize(PdfLocalizationName.description));
        vacansies.addColumn("DESCRIPTION_LOCALE_RU", commonLocalizer.localize(PdfLocalizationName.description));
        vacansies.addColumn("DESCRIPTION_LOCALE_UZ", commonLocalizer.localize(PdfLocalizationName.description));
        vacansies.addColumn("RESPONSIBILITY_LOCALE_EN", commonLocalizer.localize("responsibilities"));
        vacansies.addColumn("RESPONSIBILITY_LOCALE_RU", commonLocalizer.localize("responsibilities"));
        vacansies.addColumn("RESPONSIBILITY_LOCALE_UZ", commonLocalizer.localize("responsibilities"));

        final List<String> vacancyValues = Lists.newArrayList();
        for (EdsVacancy vacancy : contactItem.getVacancies()) {

            String vacancyName = vacancy.getName() != null ? vacancy.getName() : "";
            String jobRequirements = vacancy.getJobrequirements() != null ? vacancy.getJobrequirements() : "";
            String descriptions = vacancy.getDescription() != null ? vacancy.getDescription() : "";
            String responsibilities = vacancy.getResponsibility() != null ? vacancy.getResponsibility() : "";

            vacancyValues.add(vacancyName);
            vacancyValues.add(jobRequirements);
            vacancyValues.add(descriptions);
            vacancyValues.add(responsibilities);

            Map<String, String> jobReqLocaleMap = getLocaleMap(vacancy.getJobRequirementsLocalize());
            Map<String, String> descriptionLocaleMap = getLocaleMap(vacancy.getDescriptionLocalize());
            Map<String, String> responsibilityLocaleMap = getLocaleMap(vacancy.getResponsibilityLocalize());

            vacancyValues.add(getLocaleValue(jobReqLocaleMap, "en"));
            vacancyValues.add(getLocaleValue(jobReqLocaleMap, "ru"));
            vacancyValues.add(getLocaleValue(jobReqLocaleMap, "uz"));
            vacancyValues.add(getLocaleValue(descriptionLocaleMap, "en"));
            vacancyValues.add(getLocaleValue(descriptionLocaleMap, "ru"));
            vacancyValues.add(getLocaleValue(descriptionLocaleMap, "uz"));
            vacancyValues.add(getLocaleValue(responsibilityLocaleMap, "en"));
            vacancyValues.add(getLocaleValue(responsibilityLocaleMap, "ru"));
            vacancyValues.add(getLocaleValue(responsibilityLocaleMap, "uz"));

            vacansies.addRow(vacancyValues.toArray(new String[]{}));
            vacancyValues.clear();
        }

        return vacansies;
    }

    private String getLocaleValue(Map<String, String> localeMap, String key) {
        return localeMap != null ? localeMap.getOrDefault(key, "") : "";
    }

    private Map<String, String> getLocaleMap(String json) {
        if (ServerUtils.isNullOrEmpty(json)) {
            return new HashMap<>();
        } else {
            return WfmJsonUtils.jsonStringConvertToObject(json, HashMap.class);
        }
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

//    private String getDirector(EdsPlacement placement) {
//        ArrayList<EdsEmployee> directors = (ArrayList<EdsEmployee>) employeeManager.getDirectors2();
//
//        ArrayList<EdsEmployee> currentDirectors = directors.stream()
//                .filter(item -> item.getEndDate() != null && item.getEndDate().compareTo(placement.getCreationTime()) > 0
//                        && item.getStartDate().compareTo(placement.getCreationTime()) < 0)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        if (currentDirectors.isEmpty() && directors.isEmpty()) {
//            return "N/A";
//        } else if (!currentDirectors.isEmpty()) {
//            return currentDirectors.get(0).getFullNameWithMiddleName();
//        } else {
//            return directors.get(0).getFullNameWithMiddleName();
//        }
//    }

    public String getDirectors(EdsPlacement placement) {
        ArrayList<EdsEmployee> directors = (ArrayList<EdsEmployee>) employeeManager.getDirectors2();
        if (directors.isEmpty()) {
            return "No director found";
        }
        ArrayList<EdsEmployee> selectedDirectors = directors.stream().filter(item -> item.getEndDate() == null).collect(Collectors.toCollection(ArrayList::new));
        EdsEmployee selectedDirector = selectedDirectors.get(0);
        for (EdsEmployee employee : directors) {
            if (employee.getStartDate() != null
                    && placement.getOfferDate() != null
                    && employee.getStartDate().compareTo(placement.getOfferDate()) <= 0
                    && employee.getEndDate() != null
                    && employee.getEndDate().compareTo(placement.getOfferDate()) > 0) {

                if (isEarlierResignationDate(employee, selectedDirector)) {
                    selectedDirector = employee;
                }
            }
        }

        if (selectedDirector != null) {
            return selectedDirector.getFormmattedName();
        } else {
            return "No director found";
        }
    }

    private boolean isEarlierResignationDate(EdsEmployee employee1, EdsEmployee employee2) {
        Date resignationDate1 = employee1.getEndDate();
        Date resignationDate2 = employee2.getEndDate();

        if (resignationDate1 == null) {
            return false;
        } else if (resignationDate2 == null) {
            return true;
        }

        return resignationDate1.before(resignationDate2);
    }



    private CustomisedITextTable getItemTable(EdsPlacement placement) {
        CustomisedITextTable productItemTable = new CustomisedITextTable();

        Map<String, List<CustomTableRpc>> map = new HashMap<>();

        productItemTable.addColumn(PDFConstants.ITEM_NUMBER, "mark");
        productItemTable.addColumn("MARK_1", "mark");
        productItemTable.addColumn("MARK_2", "mark");
        productItemTable.addColumn("MARK_3", "mark");
        productItemTable.addColumn("MARK_4", "mark");
        productItemTable.addColumn("MARK_5", "mark");
        productItemTable.addColumn(EMPLOYEE_NAME, "EMPLOYEE");
        productItemTable.addColumn(PDFConstants.POSITION, "POSITION");


        if (placement.getItemTables() != null && placement.getItemTables().size() > 0) {
            for (EdsPlacementItemTable itemTable : placement.getItemTables()) {
                CustomTableRpc rpc = itemTable.getRpc();

                rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                        commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.PlacementItemTable, rpc.getUuid())));

                map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
            }
        }
        int index = 0;
        for (List<CustomTableRpc> value : map.values()) {
            for (CustomTableRpc customTableRpc : value) {
                ArrayList<String> row = new ArrayList<>();
                for (CompanyCustomFieldItem customFields : customTableRpc.getItemCustomFields()) {
                    if (customFields != null) {
                        if (TYPE_ENTITY_LOOKUP.equals(customFields.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(customFields.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(customFields.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && customFields.getQueryItems() != null) {
                                    for (final SelectItem selectItem : customFields.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                    row.add(defaultValue);
                                }
                            }
                        } else {
                            String stringFieldNAme = customFields.getFieldStringValue() != null ? customFields.getFieldStringValue() : "";
                            row.add(stringFieldNAme);
                        }


                    }

                }
                productItemTable.addRowWithCode(String.valueOf(index), row.toArray(new String[]{}));
                index++;
            }


        }


        return productItemTable;
    }
    private CustomisedITextTable getAllowanceCategoriesTable(PlacementItem placementItem, DecimalFormat totalNumberFormat) {
        CustomisedITextTable allowanceCategoriesTable = new CustomisedITextTable();
        allowanceCategoriesTable.addColumn("CATEGORY_CODE", "Code");
        allowanceCategoriesTable.addColumn("CATEGORY_NAME", "Name");
        allowanceCategoriesTable.addColumn("ALLOWANCE_AMOUNT", "Amount");

        ContactListItem contactListItem = placementItem.getCandidateID() != null ? contactService.getContact(placementItem.getCandidateID(), true) : null;
        if (contactListItem == null) {
            return allowanceCategoriesTable;
        }

        List<String> values = Lists.newArrayList();
        for (PaymentDeductionObject payment : contactListItem.getAllowanceCategories()) {
            values.add(payment.getCategoryItem() != null ? payment.getCategoryItem().getCode() : "");
            values.add(payment.getCategoryname());
            values.add(payment.getPaymentAmount() != null ? totalNumberFormat.format(payment.getPaymentAmount()) : "");

            allowanceCategoriesTable.addRow(values.toArray(new String[]{}));
            values.clear();
        }

        return allowanceCategoriesTable;
    }

    private CustomisedITextTable employeeCustomField(ProfileItem item) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getAliasName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, field.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(ServerUtils.convertServerDateToUserDate(field.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(field.getUiType())) {
                        if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                            String html = field.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            StringBuilder textValue = new StringBuilder();
                            if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                                Elements pElements = doc.getElementsByTag("p");
                                for (Element element : pElements) {
                                    textValue.append(element.text()).append("<br/>");
                                }
                            } else {
                                textValue.append(doc.body().text());
                            }
                            cols.put(COLUMN_VALUE, textValue.toString());
                        } else {
                            cols.put(COLUMN_VALUE, "");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    cols.put(PASSPORT_NUMBER, item.getPassportNumber() != null ? item.getPassportNumber() : "-");
                    cols.put(PASSPORT_ISSUE, item.getPassportIssueBy() != null ? item.getPassportIssueBy() : "-");
                    cols.put(PASSPORT_EXPIRY_DATE, String.valueOf(item.getPassportIssueDate() != null ? item.getPassportIssueDate().getDate() : "-"));
                    cols.put(PASSPORT_ISSUE_DATE, String.valueOf(item.getPassportIssueDate() != null ? item.getPassportIssueDate().getDate() : "-"));
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("EMPLOYEE_INFO", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    public CustomisedITextTable customFieldData(PlacementItem placementItem) {

        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);
        DecimalFormat numberFormat = getPriceScaleNumberFormat(company, null);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (placementItem != null && placementItem.getCustomFieldItems() != null && placementItem.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem fieldItem : placementItem.getCustomFieldItems()) {
                switch (fieldItem.getDataType()) {
                    case DATA_TYPE_DATE -> {
                        String dateValue = "";
                        String dateValueRu = "";
                        String dateValuePlus14Days = "";
                        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        if (fieldItem.getFieldDateNonConvertedValue() != null) {
                            dateValue = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            dateValueRu = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? dateFormat1.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            if (fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                                calendar.add(Calendar.DAY_OF_MONTH, 14);
                                dateValuePlus14Days = dateFormat1.format(calendar.getTime());
                            }
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), dateValue, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "RU", fieldItem.getAliasName() + "RU", dateValueRu, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", dateValuePlus14Days, DATA_TYPE_DATE);
                    }
                    case DATA_TYPE_NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(fieldItem.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), numberValue, DATA_TYPE_NUMBER);
                    }
                    case DATA_TYPE_TEXT -> {
                        String textValue = "";
                        if (TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(fieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && fieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : fieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(fieldItem.getDefaultName(), fieldItem.getAliasName(), escapeHtml(defaultValue));
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
                            String html = fieldItem.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            textValue = doc.body().text();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            if (Objects.equals(fieldItem.getAliasName(), "Working Location")) {
                                String textValueEn = "";
                                textValue = fieldItem.getFieldStringValue();
                                customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                                for (EdsCustomFormLocalization predefinedValue : customFormLocalizationManager.getPredefinedValues(fieldItem.getLocalization().getId())) {
                                    if (Objects.equals(predefinedValue.getDefaultName(), "Qarshi") || Objects.equals(predefinedValue.getDefaultName(), "Toshkent")) {
                                        textValueEn = !predefinedValue.getEnglishName().isEmpty() ? predefinedValue.getEnglishName() : "-";
                                    }
                                }
                                customFieldTable.addRowWithCode(fieldItem.getAliasName() + "En", fieldItem.getAliasName() + "En", textValueEn, UI_TYPE_HTML_TEXTAREA);
                            }
                            textValue = fieldItem.getFieldStringValue();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        }
                    }
                    case DATA_TYPE_PROFILE_IMAGE -> {
                        String uploadImageId = "";
                        if (fieldItem.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(fieldItem.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                    }
                    default ->
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(fieldItem.getFieldStringValue()), DATA_TYPE_TEXT);
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
        setFileName("Placement");
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
        return commonLocalizer.localize(PdfLocalizationName.placement);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PLACEMENT;
    }

}
