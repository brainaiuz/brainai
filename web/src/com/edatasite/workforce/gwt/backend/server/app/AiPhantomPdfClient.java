package com.edatasite.workforce.gwt.backend.server.app;

import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service("aiPhantomPdfClient")
public class AiPhantomPdfClient {
    private static final Logger log = LoggerFactory.getLogger(AiPhantomPdfClient.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "https://aipdfgen.kpi.com/api/imageToHtml/generate-html";

    public static PDFSettingsTransObject getHtmlFromAi(PDFSettingsTransObject transObject, String pdfType, String imgLink) {

        String responseText;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            PdfGenerationRequest requestBody = new PdfGenerationRequest(String.valueOf(
                    transObject.getCompanyID()), pdfType, imgLink);

            HttpEntity<PdfGenerationRequest> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<PdfGenerationResponse> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, PdfGenerationResponse.class);
            PdfGenerationResponse pdfTemplate = response.getBody();

            transObject.setHeader(pdfTemplate.header().replace("\"", ""));
            transObject.setContent(pdfTemplate.content().replace("\"", ""));
            transObject.setFooter(pdfTemplate.footer().replace("\"", ""));
            return transObject;

        } catch (Exception e) {
            responseText = e.getMessage();

            if (e instanceof HttpClientErrorException clientError) {
                responseText = clientError.getResponseBodyAsString();
            } else if (e instanceof HttpServerErrorException serverError) {
                responseText = serverError.getResponseBodyAsString();
            }
            log.error("Error sending imageToHtml request: {}", responseText, e);
            return null;
        }
    }

    private record PdfGenerationRequest (
            String companyId,
            String pdfType,
            String link
    ) {
    }

    private record PdfGenerationResponse (
            String header,
            String content,
            String footer
    ) {
    }
}