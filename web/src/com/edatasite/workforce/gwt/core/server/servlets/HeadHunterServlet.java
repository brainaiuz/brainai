package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.db.RecruitmentIntegrationManager;
import com.edatasite.workforce.gwt.profile.client.rpc.HHLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HeadHunterServlet implements HttpRequestHandler {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private RecruitmentIntegrationManager recruitmentIntegrationManager;
    @Autowired
    ZoomService zoomService;

    @Override
    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String type = request.getParameter("integration_type");
        response.setContentType("text/html");
        if (code != null) {
            if ("zoom".equals(type)) {
                String accessToken = zoomService.getAccessToken(code);
                if (accessToken != null) {
                    response.getWriter().print(getHtmlContent(true));
                } else {
                    response.getWriter().print(getHtmlContent(false));
                }
                return;
            }
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

            EdsRecruitmentIntegration credentials = recruitmentIntegrationManager.getCompanyCredentials();
            ResponseEntity<HHLoginResponse> responseEntity;
            try {
                responseEntity = restTemplate.postForEntity("https://hh.ru/oauth/token?client_id=" + credentials.getHhClientId() + "&client_secret=" + credentials.getHhClientSecret() +
                        "&code=" + code + "&grant_type=authorization_code", getRequestEntity(), HHLoginResponse.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                response.getWriter().print(getHtmlContent(false));
                return;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND, responseEntity.getBody().getExpires_in());
            recruitmentIntegrationManager.updateHHCredentials(code, responseEntity.getBody().getAccess_token(), responseEntity.getBody().getRefresh_token(), calendar.getTime());
            response.getWriter().print(getHtmlContent(true));
        }
    }

    private <T> HttpEntity<T> getRequestEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        return new HttpEntity<>(null, httpHeaders);
    }

    private String getHtmlContent(boolean isSuccess) {
        String html;
        if (isSuccess) {
            html =
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "  <title>Registration was Successfully</title>\n" +
                            "  <style>\n" +
                            "    body {\n" +
                            "      text-align: center;\n" +
                            "      padding: 20px;\n" +
                            "    }\n" +
                            "    h1 {\n" +
                            "      color: #4CAF50;\n" +
                            "    }\n" +
                            "    .tick-circle {\n" +
                            "      display: flex;\n" +
                            "      align-items: center;\n" +
                            "      justify-content: center;\n" +
                            "      width: 120px;\n" +
                            "      height: 120px;\n" +
                            "      border-radius: 50%;\n" +
                            "      background-color: #4CAF50;\n" +
                            "      margin: 20px auto;\n" +
                            "    }\n" +
                            "    .tick {\n" +
                            "      color: #ffffff;\n" +
                            "      font-size: 72px;\n" +
                            "    }\n" +
                            "  </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "  <h1>Registration was Successfully</h1>\n" +
                            "  <div class=\"tick-circle\">\n" +
                            "    <div class=\"tick\">&#10004;</div>\n" +
                            "  </div>\n" +
                            "  <p>Thank you for registering!</p>\n" +
                            "  <p>Your application has been successfully created.</p>\n" +
                            "</body>\n" +
                            "</html>\n";
        } else {
            html =
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "  <title>Registration Failed</title>\n" +
                            "  <style>\n" +
                            "    body {\n" +
                            "      text-align: center;\n" +
                            "      padding: 20px;\n" +
                            "    }\n" +
                            "    h1 {\n" +
                            "      color: #F44336;\n" +
                            "    }\n" +
                            "    .sad-circle {\n" +
                            "      display: flex;\n" +
                            "      align-items: center;\n" +
                            "      justify-content: center;\n" +
                            "      width: 120px;\n" +
                            "      height: 120px;\n" +
                            "      border-radius: 50%;\n" +
                            "      background-color: #F44336;\n" +
                            "      margin: 20px auto;\n" +
                            "      position: relative;\n" +
                            "    }\n" +
                            "    .sad {\n" +
                            "      color: #ffffff;\n" +
                            "      font-size: 72px;\n" +
                            "    }\n" +
                            "    .tear {\n" +
                            "      position: absolute;\n" +
                            "      width: 50px;\n" +
                            "      height: 100px;\n" +
                            "      border-radius: 50%;\n" +
                            "      background-color: #F44336;\n" +
                            "      top: 50%;\n" +
                            "      left: 50%;\n" +
                            "      transform: translate(-50%, -50%) rotate(45deg);\n" +
                            "      box-shadow: -10px 10px 0px 0px #D32F2F;\n" +
                            "      z-index: -1;\n" +
                            "    }\n" +
                            "  </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "  <h1>Registration Failed</h1>\n" +
                            "  <div class=\"sad-circle\">\n" +
                            "    <div class=\"sad\">&#128546;</div>\n" +
                            "    <div class=\"tear\"></div>\n" +
                            "  </div>\n" +
                            "  <p>Sorry, your registration was unsuccessful.</p>\n" +
                            "  <p>Please try again later.</p>\n" +
                            "</body>\n" +
                            "</html>";
        }
        return html;
    }
}
