package com.edatasite.workforce.gwt.core.server.app.social.facebook;

import com.edatasite.workforce.gwt.core.server.app.social.facebook.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
@Service
public class FacebookAPIService {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate;

    public FacebookAPIService() {
        this.restTemplate = new RestTemplate();
    }

    public User getUserProfile(String accessToken, String fields) {
        User facebookUser = null;
        if(StringUtils.isNotBlank(fields)) {
            try {
                facebookUser = restTemplate.getForObject("https://graph.facebook.com/v2.10/me?access_token=" + accessToken + "&fields={fields}", User.class,fields);
            } catch(Exception e) {
                log.error("Facebook Get User Data Error: {}", e.getMessage());
            }
        }
        return facebookUser;
    }

    public User getUserProfile(String accessToken, String... fieldsToRetrieve ) {
        User facebookUser = null;
        if(fieldsToRetrieve.length>0) {
            StringBuffer fields = new StringBuffer("");
            for(int i=0;i<fieldsToRetrieve.length;i++) {
                fields.append(fieldsToRetrieve[i]);
                if(i<fieldsToRetrieve.length-1) {
                    fields.append(",");
                }
            }
            try {
                facebookUser = restTemplate.getForObject("https://graph.facebook.com/v2.10/me?access_token=" + accessToken + "&fields={fields}", User.class,fields);
            } catch(Exception e) {
                log.error("Facebook Get User Data Error: {}", e.getMessage());
            }
        }
        return facebookUser;
    }

    public static void main(String args[]) {

        final String[] PROFILE_FIELDS = {
                "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email",
                "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type",
                "is_verified", "languages", "last_name", "link", "locale", "location{location}", "meeting_for", "middle_name", "name", "name_format",
                "political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other",
                "sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift",
                "website", "work" //,"mobile_phone","address"
        };
        FacebookAPIService facebookAPIService = new FacebookAPIService();
        User user = facebookAPIService.getUserProfile("EAAZAMtnNVEngBADgcs5SqLZBPHY2omaI7daf1MU9VLbT730xmQms4YtofN4BjhSRmpbAiyZCBOILuk5U7jCZCFvKR844ENueIFbOVWJUtWcxro1ul5ugIDZARCbcf2WcehIoWqIJOCovKVhuqCirjt1i2JGoMgPyYq0AZA9i77wwZDZD",
                new String[]{"id","email","first_name","last_name","location{location}", "picture.type(large)"}//.fields(location) picture.height(200).width(200)
                //PROFILE_FIELDS
                //"id,email,first_name,last_name,location"
                );
        System.out.println(user);
    }
}
