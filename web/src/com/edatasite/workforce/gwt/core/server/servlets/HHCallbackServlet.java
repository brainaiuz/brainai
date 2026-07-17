package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.RecruitmentIntegrationManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.profile.client.rpc.HHCallbackDto;
import com.edatasite.workforce.gwt.profile.client.rpc.HHCandidateContactDto;
import com.edatasite.workforce.gwt.profile.client.rpc.HHCandidateDto;
import com.edatasite.workforce.gwt.profile.client.rpc.HHLoginResponse;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HHCallbackServlet implements HttpRequestHandler {
    @Autowired
    private RecruitmentIntegrationManager recruitmentIntegrationManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String company = req.getParameter("company");
        if (StringUtils.isNotBlank(company)) {
            SecurityContext.getInstance().setCompanyId(company);
        }
        String user = req.getParameter("user");
        if (StringUtils.isNotBlank(user)) {
            SecurityContext.getInstance().setStaticUserID(Integer.valueOf(user));
        }
        String schemaType = req.getParameter("schema");
        if (StringUtils.isNotBlank(schemaType)) {
            SecurityContext.getInstance().setDatabase(schemaType);
        }
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        HHCallbackDto response = gson.fromJson(stringBuilder.toString(), HHCallbackDto.class);
        if (response.getAction_type().equals("NEW_RESPONSE_OR_INVITATION_VACANCY") || response.getAction_type().equals("NEW_NEGOTIATION_VACANCY")) {
            if (StringUtils.isNotBlank(response.getPayload().getResume_id())) {
                EdsRecruitmentIntegration credentials = recruitmentIntegrationManager.getCompanyCredentials();
                if (credentials.getHhTokenExpireAt() != null && credentials.getHhTokenExpireAt().before(new Date())) {
                    ResponseEntity<HHLoginResponse> responseEntity;
                    try {
                        responseEntity = getRestTemplate().postForEntity("https://hh.ru/oauth/token?grant_type=refresh_token&refresh_token=" + credentials.getHhRefreshToken(),
                                getRequestEntity(credentials.getHhAccessToken()), HHLoginResponse.class);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.SECOND, responseEntity.getBody().getExpires_in());
                    recruitmentIntegrationManager.updateHHCredentials(credentials.getHhCode(), responseEntity.getBody().getAccess_token(), responseEntity.getBody().getRefresh_token(), calendar.getTime());
                    credentials = recruitmentIntegrationManager.getCompanyCredentials();
                }
                EdsVacancy vacancy = vacancyManager.getByIntegrationId(response.getPayload().getVacancy_id());
                if (credentials.getHhAccessToken() != null) {
                    HttpHeaders httpHeaders = createHeaders(credentials.getHhAccessToken());
                    ResponseEntity<HHCandidateDto> candidateResponse = null;
                    try {
                        candidateResponse = getRestTemplate().exchange("https://api.hh.ru/resumes/" + response.getPayload().getResume_id(),
                                HttpMethod.GET, new HttpEntity<>(null, httpHeaders), HHCandidateDto.class);
                    } catch (Exception e) {
                        System.out.println("----------Candidate callback error: " + e.getMessage() + "--------------");
                    }
                    if (candidateResponse != null && candidateResponse.hasBody()) {
                        HHCandidateDto candidate = candidateResponse.getBody();
                        ContactListItem item = new ContactListItem();
                        System.out.println(candidateResponse.getBody().toString());
                        item.setNumberData(recruitmentService.generateCandidateNumber(null));
                        item.setContactType(ContactListItem.CANDIDATE);
                        item.setFirstName(candidate.getFirst_name());
                        item.setLastName(candidate.getLast_name());
                        item.setMiddleName(candidate.getMiddle_name());
                        item.setSkills(candidate.getSkills());

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            item.setBirthDate(new DateNonConvertable(format.parse(candidate.getBirth_date())));
                        } catch (Exception ignored) {
                        }

                        if (candidate.getContact() != null && !candidate.getContact().isEmpty()) {
                            for (HHCandidateContactDto contact : candidate.getContact()) {
                                String type = (String) contact.getType().getProperties().get("id");
                                if (type.equals("cell")) {
                                    Map<String, String> map = (Map<String, String>) contact.getValue();
                                    String phone = map.get("country") + map.get("city") + map.get("number");
                                    item.setPrimaryPhone(phone);
                                } else if (type.equals("email")) {
                                    item.setPrimaryEmail((String) contact.getValue());
                                }
                            }
                        }

                        if (candidate.getGender() != null) {
                            item.setGender(candidate.getGender().getId().equals("male") ? "Male" : "Female");
                        }
                        if (candidate.getSalary() != null) {
                            item.setExpectedSalary(candidate.getSalary().getValue().doubleValue());
                        }
                        if (vacancy != null) {
                            item.setVacancies(new ArrayList<>(Collections.singletonList(new SelectItem(vacancy.getObjectID(), true))));
                        }
                        if (candidate.getTotal_experience() != null) {
                            item.setWorkExperience((Integer) candidate.getTotal_experience().getProperties().get("months"));
                            item.setWorkExperienceMonthOrYear(1); //total experience is in months, so 1 is set
                        }
                        Integer candidateId = recruitmentService.saveCandidate(item);

                        if (candidate.getDownload() != null && candidate.getDownload().getPdf() != null && !StringUtils.isNotBlank(candidate.getDownload().getPdf().getUrl())) {
                            URL url = new URL(candidate.getDownload().getPdf().getUrl());
                            try (InputStream inputStream = url.openStream()) {
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }

                                // Create a MultipartFile from the downloaded file
                                MultipartFile multipartFile = new MockMultipartFile(
                                        "file",
                                        url.getFile(),
                                        "application/pdf",
                                        outputStream.toByteArray()
                                );

                                FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_CANDIDATE, Constants.F_DEFAULT);
                                documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), folderResource.getFileType(), candidateId, null);
                            }
                        }
                    }
                }

            }
        }
    }

    private HttpHeaders createHeaders(String secretKey) {
        return new HttpHeaders() {{
            String authHeader = "Bearer " + secretKey;
            set("Authorization", authHeader);
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }

    private RestTemplate getRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    private <T> HttpEntity<T> getRequestEntity(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>(null, httpHeaders);
    }
}
