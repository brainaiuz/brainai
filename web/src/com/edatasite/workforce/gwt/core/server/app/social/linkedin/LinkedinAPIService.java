package com.edatasite.workforce.gwt.core.server.app.social.linkedin;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.server.app.social.linkedin.model.LinkedInProfile;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anvar Akramov on 10/6/17.
 */
@Service
public class LinkedinAPIService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate;
    private final String accessTokenUrl = "https://www.linkedin.com/uas/oauth2/accessToken";

    public LinkedinAPIService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String,Object> getAccessToken(String authorizationCode) {

        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(EdsContextParams.getHostname());
        if(hostSetting!=null) {
            String apiKey = hostSetting.getLinkedinAPIKey();
            String secret = hostSetting.getLinkedinSecret();
//            String apiKey = "81ptjrolj7dlpc";
//            String secret = "xWQEdKtiIXvtCMQh";

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("client_id", apiKey);
            params.put("client_secret", secret);
            params.put("code", authorizationCode);
            params.put("redirect_uri", "http://aws.kpi.com/linkedinlogin"/*EdsContextParams.getFullHost()*/);
            params.put("grant_type", "authorization_code");
            String url = accessTokenUrl+"?grant_type=authorization_code&client_id=" + apiKey + "&client_secret=" + secret + "&code=" + authorizationCode + "&redirect_uri=http://aws.kpi.com/linkedinlogin";

            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            if (result == null) {
                throw new RestClientException("access token endpoint returned empty result");
            } else {
                return result;
            }
        } else {
            throw new RestClientException("client_id and client_secret doesnt exist");
        }
    }

    public LinkedInProfile getLinkedinUserData(String accessToken, String fields) {
//        https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code&code=AQRnjqqaD7Tl3IB10tEdmYU4w58-0Cf-zP5TPCAciRNlmlKwms2cNvNYVxOcLaLWxOwICf1YsZHKGq3M2yXIt5ru5_Xr4h6C0v6RRMPeNt7k-BEoY1x_yxnErb6mdHDjDi7mJltOJuLpoTfbD_s&redirect_uri=http://aws.kpi.com/linkedinLogin&client_id=81ptjrolj7dlpc&client_secret=xWQEdKtiIXvtCMQh
//        String code = "AQQYx3bZ4yLkEjC5DVbD1KKgr8qBDNL43-PtI6JLpPlO7iLA4s-Q6WWpB4mD3iB238KBFEaSyZZbvc64XMKTHJOC7vEHCegyA_WMv3h7iiZYgvRhi9weZX547Daj611TDWcBGlpezrinWe_Hl98";
//        String link = "https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code&code=" + code + "&redirect_uri=https://aws.kpi.com/linkedinLogin&client_id=81ptjrolj7dlpc&client_secret=xWQEdKtiIXvtCMQh";
//        https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=81ptjrolj7dlpc&scope=r_basicprofile r_emailaddress&state=1&redirect_uri=http://9830bb97.ngrok.io/linkedinLogin
        LinkedInProfile linkedInProfile = null;
        if(StringUtils.isNotBlank(fields)) {
            try {
//                String s = restTemplate.getForObject(link, String.class);
                linkedInProfile = restTemplate.getForObject("https://api.linkedin.com/v1/people/~" + fields +"?format=json&oauth2_access_token="  + accessToken, LinkedInProfile.class);
            } catch(Exception e) {
                log.error("LinkedIn Get User Data Error: {}", e.getMessage());
            }
        }
        return linkedInProfile;
    }

    public static void main(String args[]) {
        LinkedinAPIService linkedinAPIService = new LinkedinAPIService();
//        Map<String, Object> authData = linkedinAPIService.getAccessToken("AQRaIvsMTQ1-AgNDJphC9bIobUv5Zk3N37o96BbGB_ANs0i38CWISgmFdDraDMyagihukPIySZJ5lsvEVz-I8p0P54VA0GY0_Aeq0aM3Mn-HhJsyZipDsSBJ9f7XTChjLBSRf3twM2nB_cTICXo");
        Map<String, Object> authData = linkedinAPIService.getAccessToken("https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,headline,industry,site-standard-profile-request,public-profile-url,picture-url,summary)?format=json&oauth2_access_token=AQTtmCjzqme21TjdiCtDj_04MUTxcn6EdRQoBMoRzjIlqg3FQbpNXos7RkM5S5XGowkXs4MMJcGPdK8W-8w0e85VegAJ4BLeyrDefkVaB3HALC-M56rM7pfZwtRhgQsw9Mpr7CMun5Zv113j_cA");
        LinkedInProfile user = linkedinAPIService.getLinkedinUserData((String)authData.get("access_token"),
                ":(id,first-name,last-name,email-address,headline,industry,site-standard-profile-request,public-profile-url,picture-url,summary)");
        System.out.println(user);
    }
}
