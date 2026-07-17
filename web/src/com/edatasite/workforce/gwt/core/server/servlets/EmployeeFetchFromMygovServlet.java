package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovAddressDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovAddressResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovCarDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovDependentDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovDependentResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovExperienceDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovExperienceResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovMarriageDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovMarriageResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPassportResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPositionDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPositionResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPropertiesResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPropertyDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovSalaryDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovSalaryResponseDto;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class EmployeeFetchFromMygovServlet implements HttpRequestHandler {

    Logger log = LoggerFactory.getLogger(EmployeeFetchFromMygovServlet.class);

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ExecutorService executor;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String location = request.getParameter("location");
        ServerSecurityContext.getInstance().setCompanyId(311555);
        ServerSecurityContext.getInstance().setDatabase("FREE");
        ServerSecurityContext.getInstance().setStaticUserID(1);
        List<EdsEmployee> employees;
        if (location != null) {
            employees = employeeManager.getEmployeesByLocation(Integer.parseInt(location));
        } else {
            employees = employeeManager.getCompanyEmployees();
        }
        if (employees != null) {
            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();
            Integer userId = ServerSecurityContext.getInstance().getStaticUserID();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsEmployee employee : employees) {
                if (employee != null) {
                    if (employee.getProfile().getEmployeeCode() != null && employee.getProfile().getEmployeeCode().length() == 14) {
                        Callable<Void> task = () -> {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            ServerSecurityContext.getInstance().setStaticUserID(userId);
                            ProfileItem profile = contactService.editProfile(employee.getObjectID());
                            List<CustomFieldRequest> cfs = new ArrayList<>();

                            String pinfl = employee.getProfile().getEmployeeCode();
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", "123");
                            map.put("pin", pinfl);
                            MyGovResponseDto responseDto = null;
                            String errors = "";
                            try {
                                responseDto = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/deaths", new HttpEntity<>(map, headers), MyGovResponseDto.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "DEATH";
                            }
                            if (responseDto != null && responseDto.getItems() != null) {
                                cfs.add(new CustomFieldRequest("Date of Death ", responseDto.getItems().get(0).get("deathDate")));
                            }

//                        insertDependents(employee, pinfl);

                            if (employee.getBirthDay() != null) {
                                MyGovPassportResponseDto passResponse = getPassInfo(pinfl, new SimpleDateFormat("dd.MM.yyyy").format(employee.getBirthDay()));
                                if (passResponse != null) {
                                    if (passResponse.getName() != null) {
                                        profile.setFirstName(passResponse.getName().getNameLatin());
                                        profile.setLastName(passResponse.getName().getSurnameLatin());
                                        profile.setMiddleName(passResponse.getName().getPatronymLatin());
                                    }
                                    if (passResponse.getNationality() != null) {
                                        profile.setNationality(passResponse.getNationality().getValue());
                                    }
                                    if (passResponse.getBirth() != null) {
                                        cfs.add(new CustomFieldRequest("Place of birth", passResponse.getBirth().getPlace()));
                                    }
                                    if (passResponse.getCurrentDocument() != null) {
                                        profile.setPassportNumber(passResponse.getCurrentDocument().getSerNum());
                                        profile.setPassportIssueBy(passResponse.getCurrentDocument().getGivePlace());
                                        try {
                                            profile.setPassportIssueDate(new DateNonConvertable(new SimpleDateFormat("yyyy-MM-dd").parse(passResponse.getCurrentDocument().getBeginDate())));
                                            profile.setPassportExpiryDate(new DateNonConvertable(new SimpleDateFormat("yyyy-MM-dd").parse(passResponse.getCurrentDocument().getEndDate())));
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                    profile.setGender(passResponse.getSex() == 1 ? "Male" : "Female");
//                                if (passResponse.getPhoto() != null) {
//                                    try {
//                                        MultipartFile file = new MockMultipartFile("Profile-image.jpg", "Profile-image.jpg", "application/octet-stream", Base64.getDecoder().decode(passResponse.getPhoto()));
//                                        CreateDocumentCommand documentCommand = new CreateDocumentCommand();
//                                        documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
//                                        documentCommand.setCompanyID(employee.getCompany().getObjectID());
//                                        documentCommand.setFolderName("static");
//                                        documentCommand.setNotdownloadable("YES");
//                                        WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
//                                        documentCommand.addFile(multipartFile);
//                                        try {
//                                            String[] result = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
//                                            if (result != null && result.length > 0) {
//                                                commonService.saveImageUrl(Integer.valueOf(result[0]), employee.getObjectID());
//                                            }
//                                        } catch (Throwable throwable) {
//                                            errors += " FAIL " + "PHOTO";
//                                        }
//                                    } catch (Exception e) {
//                                        errors += " FAIL " + "PHOTO";
//                                    }
//                                }
                                } else {
                                    errors += " FAIL " + "PASSPORT";
                                }
                            }

                            MyGovAddressResponseDto address = getAddress(pinfl);
                            if (address != null && address.getPermanentRegistration() != null) {
                                Address address1 = new Address();
                                address1.setCity(address.getPermanentRegistration().getRegion().getValue());
                                address1.setAddress(address.getPermanentRegistration().getAddress());
                                profile.setAddresses(new ArrayList<>(Collections.singletonList(address1)));
                            } else {
                                errors += " FAIL " + "ADDRESS";
                            }

                            if (profile.getPassportNumber() != null) {
                                map = new HashMap<>();
                                map.put("passport_sn", profile.getPassportNumber().substring(0, 2));
                                map.put("passport_num", profile.getPassportNumber().substring(2));
                                map.put("type", 1);
                                map.put("transaction_id", 123);
                                map.put("sender_pinfl", pinfl);
                                map.put("purpose", pinfl);
                                map.put("consent", "YES");
                                String alimentRes = null;
                                try {
                                    alimentRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/aliments-pinfl", new HttpEntity<>(map, headers), String.class);
                                } catch (Exception e) {
                                    errors += " FAIL " + "ALIMENT";
                                }
                                Object data = null;
                                if (alimentRes != null) {
                                    data = new Gson().fromJson(alimentRes, HashMap.class).get("exec_works");
                                }
                                cfs.add(new CustomFieldRequest("ALIMENT", data != null ? "Ha" : "Yo'q"));

                                String travelRes = null;
                                try {
                                    travelRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/travels-banned-pinfl", new HttpEntity<>(map, headers), String.class);
                                } catch (Exception e) {
                                    errors += " FAIL " + "TRAVEL";
                                }
                                data = null;
                                if (travelRes != null) {
                                    data = new Gson().fromJson(travelRes, HashMap.class).get("ban_info");
                                }
                                cfs.add(new CustomFieldRequest("TRAVEL", data != null ? "Ha" : "Yo'q"));

                                map = new HashMap<>();
                                map.put("pinfl", pinfl);
                                map.put("passport_Serial", profile.getPassportNumber().substring(0, 2));
                                map.put("passport_Number", profile.getPassportNumber().substring(2));
                                String disabilityRes = null;
                                try {
                                    disabilityRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/vtek-info", new HttpEntity<>(map, headers), String.class);
                                } catch (Exception e) {
                                    errors += " FAIL " + "DISABILITY";
                                }
                                data = null;
                                if (disabilityRes != null) {
                                    data = new Gson().fromJson(disabilityRes, HashMap.class).get("data");
                                }
                                cfs.add(new CustomFieldRequest("DISABILITY", data != null ? "Ha" : "Yo'q"));
                            }

                            map = new HashMap<>();
                            map.put("pin", pinfl);
                            String drugRes = null;
                            try {
                                drugRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/drug-dispensaries-pin", new HttpEntity<>(map, headers), String.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "DRUGS";
                            }
                            Object data = null;
                            if (drugRes != null) {
                                data = new Gson().fromJson(drugRes, HashMap.class).get("data");
                            }
                            cfs.add(new CustomFieldRequest("DRUG", data != null ? "Ha" : "Yo'q"));

                            String mentalRes = null;
                            try {
                                mentalRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/mentalhealth-dispensaries-pin", new HttpEntity<>(map, headers), String.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "MENTAL";
                            }
                            data = null;
                            if (mentalRes != null) {
                                data = new Gson().fromJson(mentalRes, HashMap.class).get("data");
                            }
                            cfs.add(new CustomFieldRequest("MENTAL", data != null ? "Ha" : "Yo'q"));

                            MyGovExperienceResponseDto experienceRes = null;
                            try {
                                experienceRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/experiences", new HttpEntity<>(map, headers), MyGovExperienceResponseDto.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "EXPERIENCE";
                            }
                            if (experienceRes != null && experienceRes.getResult() != null && experienceRes.getResult().getData() != null && experienceRes.getResult().getData().getExperiences() != null) {
                                List<ExperienceTableItems> experienceTableItems = new ArrayList<>();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                for (MyGovExperienceDto exp : experienceRes.getResult().getData().getExperiences()) {
                                    ExperienceTableItems items = new ExperienceTableItems();
                                    items.setOrganization(exp.getCompany_name());
                                    items.setDepartment(exp.getStructure_name());
                                    items.setPosition(exp.getPosition_name());
                                    try {
                                        items.setHireDate(dateFormat.parse(exp.getStart_date()));
                                    } catch (ParseException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    try {
                                        items.setResignDate(exp.getEnd_date() != null ? dateFormat.parse(exp.getEnd_date()) : null);
                                    } catch (ParseException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    experienceTableItems.add(items);
                                }
                                profile.setExperienceTableItems(experienceTableItems.toArray(new ExperienceTableItems[]{}));
                            }

                            HashMap<String, ArrayList<CustomTableRpc>> customItemTables = new HashMap<>();
                            map = new HashMap<>();
                            map.put("tin", "");
                            map.put("lang", "uz");
                            map.put("pinfl", pinfl);
                            map.put("seriesPassport", "");
                            map.put("numberPassport", "");
                            MyGovSalaryResponseDto salaryRes = null;
                            try {
                                salaryRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/information-salaries", new HttpEntity<>(map, headers), MyGovSalaryResponseDto.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "SALARY";
                            }
                            if (salaryRes != null && salaryRes.getData() != null && salaryRes.getData().getSalaries() != null) {

                                ArrayList<CustomTableRpc> tableItems = new ArrayList<>();

                                int order = 1;
                                for (MyGovSalaryDto salary : salaryRes.getData().getSalaries()) {
                                    CustomTableRpc rpc = new CustomTableRpc();
                                    List<CustomFieldRequest> customFields = new ArrayList<>();
                                    customFields.add(new CustomFieldRequest("Tashkilot nomi", salary.getCompanyName()));
                                    customFields.add(new CustomFieldRequest("Yil", salary.getYear()));
                                    customFields.add(new CustomFieldRequest("Muddati", salary.getPeriod()));
                                    customFields.add(new CustomFieldRequest("Ish xaqi", salary.getSalary()));
                                    customFields.add(new CustomFieldRequest("Soliq", salary.getSalaryTaxSum()));
                                    rpc.setUuid("ITEM_TABLE_Rx0kaEqcV7");
                                    rpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFields, commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, "ITEM_TABLE_Rx0kaEqcV7"), null));
                                    rpc.setSorder(order);
                                    tableItems.add(rpc);
                                    order++;
                                }
                                customItemTables.put("ITEM_TABLE_Rx0kaEqcV7", tableItems);
                            } else {
                                errors += " FAIL " + "SALARY";
                            }

                            map = new HashMap<>();
                            map.put("tin", "");
                            map.put("lang", "uz");
                            map.put("pinfl", pinfl);
                            MyGovPropertiesResponseDto propertyRes = null;
                            try {
                                propertyRes = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/properties", new HttpEntity<>(map, headers), MyGovPropertiesResponseDto.class);
                            } catch (Exception e) {
                                errors += " FAIL " + "PROPERTIES";
                            }
                            if (propertyRes != null) {
                                if (propertyRes.getData() != null) {
                                    ArrayList<CustomTableRpc> tableItems = new ArrayList<>();

                                    int order = 1;
                                    for (MyGovPropertyDto property : propertyRes.getData()) {
                                        CustomTableRpc rpc = new CustomTableRpc();
                                        List<CustomFieldRequest> customFields = new ArrayList<>();
                                        customFields.add(new CustomFieldRequest("Manzil", property.getAddress()));
                                        customFields.add(new CustomFieldRequest("Maydoni", property.getTotal_area()));
                                        customFields.add(new CustomFieldRequest("Kadastr codi", property.getObj_code()));
                                        customFields.add(new CustomFieldRequest("Turi", property.getObj_name()));
                                        rpc.setUuid("ITEM_TABLE_rF1nnyBZar");
                                        rpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFields, commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, "ITEM_TABLE_rF1nnyBZar"), null));
                                        rpc.setSorder(order);
                                        tableItems.add(rpc);
                                        order++;
                                    }
                                    customItemTables.put("ITEM_TABLE_rF1nnyBZar", tableItems);
                                }

                                if (propertyRes.getData_car() != null) {
                                    ArrayList<CustomTableRpc> tableItems = new ArrayList<>();

                                    int order = 1;
                                    for (MyGovCarDto car : propertyRes.getData_car()) {
                                        CustomTableRpc rpc = new CustomTableRpc();
                                        List<CustomFieldRequest> customFields = new ArrayList<>();
                                        customFields.add(new CustomFieldRequest("Model", car.getModel()));
                                        customFields.add(new CustomFieldRequest("Rangi", car.getColor()));
                                        customFields.add(new CustomFieldRequest("Yil", car.getYear()));
                                        customFields.add(new CustomFieldRequest("Avtoulov raqami", car.getGos_number()));
                                        rpc.setUuid("ITEM_TABLE_3ghyuJbyTL");
                                        rpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFields, commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, "ITEM_TABLE_3ghyuJbyTL"), null));
                                        rpc.setSorder(order);
                                        tableItems.add(rpc);
                                        order++;
                                    }
                                    customItemTables.put("ITEM_TABLE_3ghyuJbyTL", tableItems);
                                }
                            } else {
                                errors += " FAIL " + "PROPERTIES";
                            }

                            profile.setCustomTableItems(customItemTables);
                            cfs.add(new CustomFieldRequest("STATUS", errors));
                            profile.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Employee), employee.getCustomFields()));
                            contactService.updateProfile(profile);
                            return null;
                        };
                        tasks.add(task);
                    }
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void insertDependents(EdsEmployee employee, String pinfl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("id", "111");
        request.put("pin", pinfl);
        MyGovDependentResponseDto result = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/birthdates", new HttpEntity<>(request, httpHeaders), MyGovDependentResponseDto.class);
        if (result != null && result.getItems() != null && !result.getItems().isEmpty()) {
            MyGovDependentDto item = result.getItems().get(0);
            DependentItem father = new DependentItem();
            father.setFirstName(item.getF_first_name());
            father.setLastName(item.getF_family());
            father.setMiddleName(item.getF_patronym());
            father.setRelationship("Father");
            father.setEmployeeId(employee.getObjectID());
            List<CustomFieldRequest> cfs = new ArrayList<>();
            MyGovAddressResponseDto addressResponse = getAddress(item.getF_pnfl());
            if (addressResponse != null && addressResponse.getPermanentRegistration() != null) {
                MyGovAddressDto address = addressResponse.getPermanentRegistration();
                father.setCity(address.getRegion() != null ? (String) address.getRegion().getValue() : null);
                cfs.add(new CustomFieldRequest("COUNTRY", address.getCountry() != null ? address.getCountry().getValue() : ""));
                father.setAddress(address.getDistrict() != null ? address.getDistrict().getValue() : "" + " " + address.getAddress());
            }
            MyGovPassportResponseDto passResponse = getPassInfo(item.getF_pnfl(), item.getF_birth_day());
            if (passResponse != null) {
                if (passResponse.getCurrentDocument() != null) {
                    cfs.add(new CustomFieldRequest("PASS_NUMBER", passResponse.getCurrentDocument().getSerNum()));
                    cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passResponse.getCurrentDocument().getGivePlace()));
                    cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passResponse.getCurrentDocument().getBeginDate()));
                    cfs.add(new CustomFieldRequest("PASS_END_DATE", passResponse.getCurrentDocument().getEndDate()));
                }
                cfs.add(new CustomFieldRequest("SEX", passResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
            }
            cfs.add(new CustomFieldRequest("PINFL", item.getF_pnfl()));

            MyGovPositionResponseDto position = getPosition(item.getF_pnfl());
            if (position != null && !position.getPositions().isEmpty()) {
                MyGovPositionDto positionDto = position.getPositions().get(0);
                cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
            }
            father.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
            hrmsService.saveDependent(father);

            DependentItem mother = new DependentItem();
            mother.setFirstName(item.getM_first_name());
            mother.setLastName(item.getM_family());
            mother.setMiddleName(item.getM_patronym());
            mother.setRelationship("Mother");
            mother.setEmployeeId(employee.getObjectID());
            cfs = new ArrayList<>();
            MyGovAddressResponseDto mAddressResponse = getAddress(item.getM_pnfl());
            if (mAddressResponse != null && mAddressResponse.getPermanentRegistration() != null) {
                MyGovAddressDto mAddress = mAddressResponse.getPermanentRegistration();
                mother.setCity(mAddress.getRegion() != null ? (String) mAddress.getRegion().getValue() : null);
                cfs.add(new CustomFieldRequest("COUNTRY", mAddress.getCountry() != null ? mAddress.getCountry().getValue() : null));
                mother.setAddress(mAddress.getDistrict() != null ? mAddress.getDistrict().getValue() : "" + " " + mAddress.getAddress());
            }
            MyGovPassportResponseDto passMResponse = getPassInfo(item.getM_pnfl(), item.getM_birth_day());
            if (passMResponse != null) {
                if (passMResponse.getCurrentDocument() != null) {
                    cfs.add(new CustomFieldRequest("PASS_NUMBER", passMResponse.getCurrentDocument().getSerNum()));
                    cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passMResponse.getCurrentDocument().getGivePlace()));
                    cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passMResponse.getCurrentDocument().getBeginDate()));
                    cfs.add(new CustomFieldRequest("PASS_END_DATE", passMResponse.getCurrentDocument().getEndDate()));
                }
                cfs.add(new CustomFieldRequest("SEX", passMResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
            }
            cfs.add(new CustomFieldRequest("PINFL", item.getM_pnfl()));

            MyGovPositionResponseDto mPosition = getPosition(item.getM_pnfl());
            if (mPosition != null && !mPosition.getPositions().isEmpty()) {
                MyGovPositionDto positionDto = mPosition.getPositions().get(0);
                cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
            }
            mother.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
            hrmsService.saveDependent(mother);

            MyGovMarriageResponseDto marriageResponse = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/marriages", new HttpEntity<>(request, httpHeaders), MyGovMarriageResponseDto.class);
            if (marriageResponse != null && !marriageResponse.getItems().isEmpty()) {
                MyGovMarriageDto marriage = marriageResponse.getItems().get(0);
                if (marriage.getW_pnfl() != null && marriage.getH_pnfl() != null) {
                    boolean isHusband = marriage.getW_pnfl().equals(pinfl);

                    DependentItem dependent = new DependentItem();
                    dependent.setFirstName(isHusband ? marriage.getH_first_name() : marriage.getW_first_name());
                    dependent.setLastName(isHusband ? marriage.getH_family() : marriage.getW_family_after() != null ? marriage.getW_family_after() : marriage.getW_family());
                    dependent.setMiddleName(isHusband ? marriage.getH_patronym() : marriage.getW_patronym());
                    dependent.setRelationship(isHusband ? "Husband" : "Wife");
                    dependent.setEmployeeId(employee.getObjectID());
                    cfs = new ArrayList<>();
                    MyGovAddressResponseDto dAddressResponse = getAddress(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl());
                    if (dAddressResponse != null) {
                        MyGovAddressDto dAddress = dAddressResponse.getPermanentRegistration();
                        dependent.setCity(dAddress.getRegion() != null ? (String) dAddress.getRegion().getValue() : null);
                        cfs.add(new CustomFieldRequest("COUNTRY", dAddress.getCountry() != null ? dAddress.getCountry().getValue() : ""));
                        dependent.setAddress(dAddress.getDistrict() != null ? dAddress.getDistrict().getValue() : "" + " " + dAddress.getAddress());
                    }
                    MyGovPassportResponseDto passDResponse = getPassInfo(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl(), isHusband ? marriage.getH_birth_day() : marriage.getW_birth_day());
                    if (passDResponse != null) {
                        if (passDResponse.getCurrentDocument() != null) {
                            cfs.add(new CustomFieldRequest("PASS_NUMBER", passDResponse.getCurrentDocument().getSerNum()));
                            cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passDResponse.getCurrentDocument().getGivePlace()));
                            cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passDResponse.getCurrentDocument().getBeginDate()));
                            cfs.add(new CustomFieldRequest("PASS_END_DATE", passDResponse.getCurrentDocument().getEndDate()));
                        }
                        cfs.add(new CustomFieldRequest("SEX", passDResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
                    }
                    cfs.add(new CustomFieldRequest("PINFL", isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl()));

                    MyGovPositionResponseDto mDPosition = getPosition(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl());
                    if (mDPosition != null && !mDPosition.getPositions().isEmpty()) {
                        MyGovPositionDto positionDto = mDPosition.getPositions().get(0);
                        cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                        cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                        cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
                    }
                    dependent.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
                    hrmsService.saveDependent(dependent);
                }
            }
        }
    }

    private MyGovPassportResponseDto getPassInfo(String pinfl, String birthDay) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date;
        try {
            date = LocalDate.parse(birthDay, inputFormatter);
        } catch (Exception e) {
            return null;
        }
        String outputDate = date.format(outputFormatter);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("pin", pinfl);
        request.put("lang_id", 3);
        request.put("birth_date", outputDate);
        request.put("document", "uz");
        request.put("is_photo", "Y");
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/people/info", new HttpEntity<>(request, httpHeaders), MyGovPassportResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private MyGovAddressResponseDto getAddress(String pinfl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("pin", pinfl);
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/addresses-pin", new HttpEntity<>(request, httpHeaders), MyGovAddressResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private MyGovPositionResponseDto getPosition(String pinfl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        DynamicDto request = new DynamicDto();
        request.addProperty("pin", pinfl);
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/current-positions", new HttpEntity<>(request, httpHeaders), MyGovPositionResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }
}
