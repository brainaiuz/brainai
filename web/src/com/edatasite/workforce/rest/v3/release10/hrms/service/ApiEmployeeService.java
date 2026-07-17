package com.edatasite.workforce.rest.v3.release10.hrms.service;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageTO;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.employee.EmployeeSaveDto;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiEmployeeService {
    private final EmployeeService employeeService;
    private final CommonService commonService;
    private final ContactService contactService;
    private final EmployeeServiceLocal employeeServiceLocal;
    private final AllInOneService allInOneService;

    public ApiEmployeeService(EmployeeService employeeService, CommonService commonService, ContactService contactService, EmployeeServiceLocal employeeServiceLocal,AllInOneService allInOneService) {
        this.employeeService = employeeService;
        this.commonService = commonService;
        this.contactService = contactService;
        this.employeeServiceLocal = employeeServiceLocal;
        this.allInOneService = allInOneService;
    }


    public Integer create(EmployeeSaveDto request) {
        if (StringUtils.isBlank(request.getEmpCode())) {
            NumberData numberData = employeeServiceLocal.generateEmployeeNumber(null);
            request.setNumberData(numberData);
            request.setEmpCode(numberData.getNumberString());
        }
        if (request.getPositionId() == null) {
            return employeeService.createEmployee(wrap(request), false);
        }
        if (employeeService.checkPositionAvailability(request.getPositionId())) {
            return employeeService.createEmployee(wrap(request), false);
        }
        return -1;
    }

    public Integer update(EmployeeSaveDto request) {
        return contactService.updateProfile(toProfileItem(request));
    }

    private ProfileItem toProfileItem(EmployeeSaveDto request) {
        ProfileItem employee = new ProfileItem();
        employee.setFirstName(request.getFirstname());
        employee.setLastName(request.getLastname());
        employee.setMiddleName(request.getMiddlename());
        employee.setOtherName(request.getOtherName());
        employee.setTitle(request.getTitle());
        employee.setDepartmentID(request.getDepartmentId());
        employee.setRoleId(request.getRoleId());
        Optional.ofNullable(request.getBirthDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setBirthDate);
        Optional.ofNullable(request.getGender())
                .map(Gender::getName)
                .ifPresent(employee::setGender);
        employee.setAttachments(request.getAttachments());
        employee.setDriverID(request.getDriverID());
        employee.setNationality(request.getNationality());
        employee.setMartialStatusId(request.getMartialStatusId());
        employee.setSpokenLanguages(request.getSpokenLanguages());
        employee.setCreatedFrom(request.getCreatedFrom());
        employee.setNumberData(request.getNumberData());
        employee.setEmpCode(request.getEmpCode());
        employee.setWageRate(request.getWageRate());
        employee.setClientChargeRate(request.getClientChargeRate());
        employee.setQualificationID(request.getQualificationID());
        employee.setStatusId(request.getStatusId());
        employee.setReportsToId(request.getReportsToId());
        employee.setTermsOfContract(request.getTermsOfContract());
        employee.setTermsOfCMonthORYear(request.getTermsOfCMonthORYear());
        employee.setEmpModeId(request.getEmpModeId());
        employee.setSalaryGradeId(request.getSalaryGradeId());
        employee.setSalaryAmount(request.getSalaryAmount());
        employee.setJobTitleId(request.getJobTitleId());
        employee.setJobTitle(request.getJobTitle());
        Optional.ofNullable(request.getVisaExpirationDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setVisaExpirationDate);
        employee.setPosition(request.getPosition());
        employee.setPositionId(request.getPositionId());
        employee.setLocationId(request.getLocationId());
        employee.setApplyPositionLeaveForEmployee(request.isApplyPositionLeaveForEmployee());
        employee.setCoursesItems(request.getCoursesItems());
        employee.setPassportNumber(request.getPassportNumber());
        Optional.ofNullable(request.getPassportIssueDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setPassportIssueDate);
        Optional.ofNullable(request.getPassportExpiryDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setPassportExpiryDate);
        Optional.ofNullable(request.getMedicalInsuranceExpireDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setMedicalInsuranceExpireDate);
        employee.setVisaNumber(request.getVisaNumber());
        Optional.ofNullable(request.getVisaIssueDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setVisaIssueDate);
        employee.setInsuranceNumber(request.getInsuranceNumber());
        Optional.ofNullable(request.getCustomFields())
                .map(c -> CustomFieldsUtils.convertCustomFields(c, commonService.getCompanyCustomFields(ViewName.Employee), null))
                .ifPresent(employee::setCustomFields);
        employee.setPaymentMethod(request.getPaymentMethod());
        employee.setPayrollSettings(request.getPayrollSettings());
        employee.setInactiveCategories(request.getInactiveCategories());
        employee.setDeletedCategories(request.getDeletedCategories());
        employee.setOpeningBalanceDays(request.getOpeningBalanceDays());
        employee.setProbationDays(request.getProbationDays());
        employee.setPhotoId(request.getPhotoID());
        if (request.getSpokingLanguages() != null && !request.getSpokingLanguages().isEmpty()) {
            ArrayList<SpokenLanguageItem> spokenLanguageItems = new ArrayList<>();
            SpokenLanguageTO spokenLanguages = allInOneService.getLanguagesWithLevels();
            if (spokenLanguages != null) {
                for (LanguagesDto language : request.getSpokingLanguages()) {
                    SpokenLanguageItem newLanguage = new SpokenLanguageItem();
                    for (SelectItem languageItem : spokenLanguages.getLanguages()) {
                        if (language.getLanguage().getId().equals(languageItem.getId()) || language.getLanguage().getName().equals(languageItem.getName())) {
                            newLanguage.setLanguage(languageItem);
                            break;
                        }
                    }
                    for (SelectItem languageLevel : spokenLanguages.getLanguageLevels()) {
                        if (language.getLevel().getId().equals(languageLevel.getId()) || language.getLevel().getName().equals(languageLevel.getName())) {
                            newLanguage.setLevel(languageLevel);
                            break;
                        }
                    }
                    spokenLanguageItems.add(newLanguage);
                }
            }
            employee.setSpokingLanguages(spokenLanguageItems);
        }
        Optional.ofNullable(request.getEmployeeDegree())
                .map(e -> new ReferenceItem(e.getId(), e.getName()))
                .ifPresent(employee::setemployeeDegree);
        Optional.ofNullable(request.getTimeslot())
                .map(e -> new SelectItem(e.getId(), e.getName()))
                .ifPresent(employee::setTimeslot);
        employee.setPlacementId(request.getPlacementId());
        employee.setSalaryMode(request.getSalaryMode());
        employee.setActive(true);
        employee.setEmployeeId(request.getObjectID());
        employee.setPrimaryEmail(request.getEmail());
        employee.setPrimaryPhone(request.getPrimaryphone());
        Optional.ofNullable(request.getWorkphone()).ifPresent(employee::setWorkPhone);
        Optional.ofNullable(request.getHomephone()).ifPresent(employee::setHomePhone);
        employee.setPrimaryPhone(request.getPrimaryphone());
        employee.setWorkEmail(new ArrayList<>(List.of(request.getEmail())));
        return employee;
    }

    private NewEmployee wrap(EmployeeSaveDto request) {
        NewEmployee employee = new NewEmployee();
        employee.setAddSingleEmployee(true);
        employee.setFname(request.getFirstname());
        employee.setLname(request.getLastname());
        employee.setMname(request.getMiddlename());
        employee.setOtherName(request.getOtherName());
        employee.setTitle(request.getTitle());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartmentId());
        employee.setRoleId(request.getRoleId());
        Optional.ofNullable(request.getHireDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setStartDate);
        Optional.ofNullable(request.getFireDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setEndDate);
        employee.setHasAccess(request.getHasAccess());
        Optional.ofNullable(request.getBirthDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setBirthDate);
        Optional.ofNullable(request.getGender())
                .map(Gender::getName)
                .ifPresent(employee::setGender);
        employee.setAttachments(request.getAttachments());
        employee.setDriverID(request.getDriverID());
        Optional.ofNullable(request.getDriverID())
                .filter(StringUtils::isNotBlank)
                .filter(NumberUtils::isNumber)
                .map(Long::valueOf)
                .ifPresent(employee::setDriverNumber);
        employee.setNationality(request.getNationality());
        employee.setMartialStatusId(request.getMartialStatusId());
        employee.setSpokenLanguages(request.getSpokenLanguages());
        employee.setCreatedFrom(request.getCreatedFrom());
        employee.setNumberData(request.getNumberData());
        employee.setEmpCode(request.getEmpCode());
        employee.setWageRate(request.getWageRate());
        employee.setClientChargeRate(request.getClientChargeRate());
        employee.setQualificationID(request.getQualificationID());
        employee.setStatusId(request.getStatusId());
        employee.setReportsToId(request.getReportsToId());
        employee.setTermsOfContract(request.getTermsOfContract());
        employee.setTermsOfCMonthORYear(request.getTermsOfCMonthORYear());
        employee.setEmpModeId(request.getEmpModeId());
        employee.setSalaryGradeId(request.getSalaryGradeId());
        employee.setSalaryAmount(request.getSalaryAmount());
        employee.setJobTitleId(request.getJobTitleId());
        employee.setJobTitle(request.getJobTitle());
        Optional.ofNullable(request.getVisaExpirationDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setVisaExpirationDate);
        employee.setPosition(request.getPosition());
        employee.setPositionId(request.getPositionId());
        employee.setLocationId(request.getLocationId());
        employee.setApplyPositionLeaveForEmployee(request.isApplyPositionLeaveForEmployee());
        employee.setCoursesItems(request.getCoursesItems());
        employee.setPassportNumber(request.getPassportNumber());
        Optional.ofNullable(request.getPassportIssueDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setPassportIssueDate);
        Optional.ofNullable(request.getPassportExpiryDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setPassportExpiryDate);
        Optional.ofNullable(request.getMedicalInsuranceExpireDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setMedicalInsuranceExpireDate);
        employee.setVisaNumber(request.getVisaNumber());
        Optional.ofNullable(request.getVisaIssueDate())
                .map(DateNonConvertable::new)
                .ifPresent(employee::setVisaIssueDate);
        employee.setInsuranceNumber(request.getInsuranceNumber());
        employee.setPassportIssueID(request.getPassportIssueID());
        Optional.ofNullable(request.getCustomFields())
                .map(c -> CustomFieldsUtils.convertCustomFields(c, commonService.getCompanyCustomFields(ViewName.Employee), null))
                .ifPresent(employee::setCustomFields);
        employee.setPaymentMethod(request.getPaymentMethod());
        employee.setEssUser(request.getEssUser());
        employee.setPayrollSettings(request.getPayrollSettings());
        employee.setInactiveCategories(request.getInactiveCategories());
        employee.setDeletedCategories(request.getDeletedCategories());
        employee.setOpeningBalanceDays(request.getOpeningBalanceDays());
        employee.setProbationDays(request.getProbationDays());
        employee.setPhotoID(request.getPhotoID());
        if (request.getSpokingLanguages() != null && !request.getSpokingLanguages().isEmpty()) {
            ArrayList<SpokenLanguageItem> spokenLanguageItems = new ArrayList<>();
            SpokenLanguageTO spokenLanguages = allInOneService.getLanguagesWithLevels();
            if (spokenLanguages != null) {
                for (LanguagesDto language : request.getSpokingLanguages()) {
                    SpokenLanguageItem newLanguage = new SpokenLanguageItem();
                    for (SelectItem languageItem : spokenLanguages.getLanguages()) {
                        if (language.getLanguage().getId().equals(languageItem.getId()) || language.getLanguage().getName().equals(languageItem.getName())) {
                            newLanguage.setLanguage(languageItem);
                            break;
                        }
                    }
                    for (SelectItem languageLevel : spokenLanguages.getLanguageLevels()) {
                        if (language.getLevel().getId().equals(languageLevel.getId()) || language.getLevel().getName().equals(languageLevel.getName())) {
                            newLanguage.setLevel(languageLevel);
                            break;
                        }
                    }
                    spokenLanguageItems.add(newLanguage);
                }
            }
            employee.setSpokingLanguages(spokenLanguageItems);
        }
        Optional.ofNullable(request.getEmployeeDegree())
                .map(e -> new ReferenceItem(e.getId(), e.getName()))
                .ifPresent(employee::setEmployeeDegree);
        Optional.ofNullable(request.getTimeslot())
                .map(e -> new SelectItem(e.getId(), e.getName()))
                .ifPresent(employee::setTimeslot);
        employee.setPlacementId(request.getPlacementId());
        employee.setSalaryMode(request.getSalaryMode());
        employee.setActive(true);
        ContactListItem contactListItem = new ContactListItem();
        Optional.ofNullable(request.getWorkphone()).ifPresent(contactListItem::setWorkPhone);
        Optional.ofNullable(request.getHomephone()).ifPresent(contactListItem::setHomePhone);
        contactListItem.setPrimaryPhone(request.getPrimaryphone());
        contactListItem.setWorkEmail(new ArrayList<>(List.of(request.getEmail())));
        employee.setContactListItem(contactListItem);
        Optional.ofNullable(request.getHomephone()).map(e -> e.get(0)).ifPresent(employee::setHphone);
        Optional.ofNullable(request.getWorkphone()).map(e -> e.get(0)).ifPresent(employee::setWphone);
        Optional.ofNullable(request.getPrimaryphone()).ifPresent(employee::setMphone);
        employee.setRegistrationType(request.getRegistrationType());
        employee.setPassword("kpi123");
        return employee;
    }

    public void delete(Integer id) {
        employeeService.deleteEmployee(id, true, true, null, new ReferenceItem());
    }

    public Object generateNumber(Integer id) {
        NumberData numberData = employeeServiceLocal.generateEmployeeNumber(id);
        return numberData;
    }
}
