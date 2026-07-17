package com.edatasite.workforce.gwt.core.server.app.social.zoom.impl;

import com.edatasite.workforce.core.domain.EdsZoomMeeting;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.dto.ZoomLoginResponseDto;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.dto.ZoomMeetingRequestDto;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.dto.ZoomMeetingResponseDto;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.model.ZoomAutoRecording;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.model.ZoomMeetingSettings;
import com.edatasite.workforce.gwt.core.server.db.RecruitmentIntegrationManager;
import com.edatasite.workforce.gwt.core.server.db.ZoomMeetingManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ZoomServiceImpl implements ZoomService {
    @Autowired
    private RecruitmentIntegrationManager recruitmentIntegrationManager;
    @Autowired
    ZoomMeetingManager zoomMeetingManager;
    private final String baseUrl = "https://api.zoom.us/v2";
    private final String accessTokenUrl = "https://zoom.us/oauth/token";
    private final RestTemplate restTemplate = new RestTemplate();

    public Appointment createMeeting(EdsEvent event) {
        String createMeetingUrl = baseUrl + "/users/me/meetings";
        ZoomMeetingRequestDto requestDto = new ZoomMeetingRequestDto();
        requestDto.setTopic(event.getSubject().equals(" ") ? "-undifined" : event.getSubject());
        requestDto.setAgenda("");
        requestDto.setDuration((int) TimeUnit.MILLISECONDS.toMinutes(event.getEndDate().getTime() - event.getStartDate().getTime()));
        requestDto.setStart_time(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00").format(event.getStartDate().before(new Date()) ? new Date() : event.getStartDate()) + "Z");
        requestDto.setSettings(new ZoomMeetingSettings(ZoomAutoRecording.CLOUD.name().toLowerCase()));
        requestDto.setPassword("");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ResponseEntity<ZoomMeetingResponseDto> zoomMeetingDtoResponseEntity = null;
        Appointment item = new Appointment();
        try {
            String json = ow.writeValueAsString(requestDto);
            zoomMeetingDtoResponseEntity = restTemplate.postForEntity(createMeetingUrl, new HttpEntity<>(json, httpHeaders()), ZoomMeetingResponseDto.class);
            item.setZoomObjectId(createMeetingToEntity(zoomMeetingDtoResponseEntity.getBody(), event));
            item.setLinkURL(zoomMeetingDtoResponseEntity != null ? zoomMeetingDtoResponseEntity.getBody().getJoin_url() : "");
        } catch (Exception e) {
            System.err.println("Create zoom meeting ERROR ! message: " + e.getMessage());
        }
        return item;
    }

    public void updateMeeting(EdsEvent event) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        EdsZoomMeeting meetingEvent = null;
        if (event.getObjectID() != null) {
            meetingEvent = zoomMeetingManager.getMeetingByEventId(event.getObjectID());
        } else {
            meetingEvent = zoomMeetingManager.getMeetingByEventUrl(event.getDescription());
        }
        String updateMeetingUrl = baseUrl + "/meetings/" + meetingEvent.getMeetingId();
        ZoomMeetingRequestDto requestDto = new ZoomMeetingRequestDto();
        requestDto.setTopic(event.getSubject());
        requestDto.setAgenda(event.getDescription());
        requestDto.setDuration((int) TimeUnit.MILLISECONDS.toMinutes(event.getEndDate().getTime() - event.getStartDate().getTime()));
        requestDto.setStart_time(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00").format(event.getStartDate().before(new Date()) ? new Date() : event.getStartDate()) + "Z");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(requestDto);
            ResponseEntity<String> exchange = restTemplate.exchange(updateMeetingUrl, HttpMethod.PATCH, new HttpEntity<>(json, httpHeaders()), String.class);
            if (exchange != null && exchange.getStatusCode().value() == 204) {
                updateMeeetingFromEntity(requestDto, event);
            }
        } catch (Exception e) {
            System.err.println("Update zoom meeting ERROR ! message: " + e.getMessage());
        }
    }

    public void deleteMeeting(EdsEvent event) {
        EdsZoomMeeting meetingByEventId;
        if (event.getObjectID() != null) {
            meetingByEventId = zoomMeetingManager.getMeetingByEventId(event.getObjectID());
        } else {
            meetingByEventId = zoomMeetingManager.getMeetingByEventUrl(event.getDescription());
        }

        String deleteMeetingUrl = baseUrl + "/meetings/" + meetingByEventId.getMeetingId();
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(deleteMeetingUrl, HttpMethod.DELETE, new HttpEntity<>(null, httpHeaders()), String.class);
            if (exchange != null && exchange.getStatusCode().value() == 204) {
                deleteMeetnigFromEntity(meetingByEventId);
            }
        } catch (Exception e) {
            System.err.println("Delete zoom meeting ERROR ! message: " + e.getMessage());
        }
    }

    public Integer createMeetingToEntity(ZoomMeetingResponseDto meetingItem, EdsEvent event) {
        EdsZoomMeeting zoomMeeting = new EdsZoomMeeting();
        zoomMeeting.setMeetingId(meetingItem.getId());
        zoomMeeting.setTopic(meetingItem.getTopic());
        zoomMeeting.setAgenda(meetingItem.getAgenda());
        zoomMeeting.setPassword(meetingItem.getPassword());
        zoomMeeting.setJoinUrl(meetingItem.getJoin_url());
        zoomMeeting.setStartUrl(meetingItem.getStart_url());
        zoomMeeting.setTimezone(meetingItem.getTimezone());
        zoomMeeting.setCreatedAt(meetingItem.getCreated_at());
        zoomMeeting.setStartTime(meetingItem.getStart_time());
        zoomMeeting.setDuration(meetingItem.getDuration());
//        zoomMeeting.setEventId(event);
        zoomMeeting.setAuto_recording(meetingItem.getSettings().getAuto_recording());
        zoomMeetingManager.createOrUpdate(zoomMeeting);
        return zoomMeeting.getObjectID();
    }

    public void updateMeeetingFromEntity(ZoomMeetingRequestDto meetingItem, EdsEvent event) throws ParseException {
        EdsZoomMeeting oldMeeting = zoomMeetingManager.getMeetingByEventId(event.getObjectID());
        oldMeeting.setTopic(meetingItem.getTopic());
        oldMeeting.setAgenda(meetingItem.getAgenda());
        oldMeeting.setStartTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00").parse(meetingItem.getStart_time()));
        oldMeeting.setDuration(meetingItem.getDuration());
        zoomMeetingManager.update(oldMeeting);
    }

    public void deleteMeetnigFromEntity(EdsZoomMeeting meeting) {
        zoomMeetingManager.deleteMeetingById(meeting.getMeetingId());
    }

    public String getAccessToken(String code) {
        if (code != null) {
            String url = accessTokenUrl + "?grant_type=authorization_code&code=" + code + "&redirect_uri=" + EdsContextParams.getHost() + "/common/headHunterServlet?integration_type=zoom";
            ResponseEntity<ZoomLoginResponseDto> zoomresponseEntity = null;
            try {
                zoomresponseEntity = restTemplate.postForEntity(url, httpEntityForAccessToken(), ZoomLoginResponseDto.class);
            } catch (Exception e) {
                System.err.println("Get zoom access token ERROR ! message: " + e.getMessage() + "\n url = " + url);
                return null;
            }
            if (zoomresponseEntity != null && zoomresponseEntity.getBody() != null) {
                recruitmentIntegrationManager.updateZoomCredentials(code, zoomresponseEntity.getBody().getAccess_token(), zoomresponseEntity.getBody().getRefresh_token());
                return zoomresponseEntity.getBody().getAccess_token();
            }
        }
        return null;
    }

    public String refreshAccessToken() {
        EdsRecruitmentIntegration zoomCredentials = recruitmentIntegrationManager.getCompanyCredentials();
        String url = accessTokenUrl + "?grant_type=refresh_token&refresh_token=" + zoomCredentials.getZoomRefreshToken();
        ResponseEntity<ZoomLoginResponseDto> zoomresponseEntity = null;
        try {
            zoomresponseEntity = restTemplate.postForEntity(url, httpEntityForAccessToken(), ZoomLoginResponseDto.class);
        } catch (Exception e) {
            System.err.println("Refresh zoom access token ERROR ! message: " + e.getMessage() + "\n url = " + url);
        }
        assert zoomresponseEntity != null;
        zoomCredentials.setZoomAccessToken(zoomresponseEntity.getBody().getAccess_token());
        zoomCredentials.setZoomRefreshToken(zoomresponseEntity.getBody().getRefresh_token());
        recruitmentIntegrationManager.update(zoomCredentials);
        return zoomCredentials.getZoomAccessToken();
    }

    private HttpHeaders httpHeaders() {
        String accessToken = refreshAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        return httpHeaders;
    }

    private <T> HttpEntity<T> httpEntityForAccessToken() {
        EdsRecruitmentIntegration zoomCredentials = recruitmentIntegrationManager.getCompanyCredentials();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        String encoded = Base64.getEncoder().encodeToString((zoomCredentials.getZoomClientId() + ":" + zoomCredentials.getZoomClientSecret()).getBytes());
        httpHeaders.set("Authorization", "Basic " + encoded);
        return new HttpEntity<>(httpHeaders);
    }
}
