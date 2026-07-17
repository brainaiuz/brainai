package com.edatasite.workforce.gwt.core.server.controllers.login.marketplace;

import com.google.step2.AuthRequestHelper;
import com.google.step2.AuthResponseHelper;
import com.google.step2.ConsumerHelper;
import com.google.step2.Step2;
import com.google.step2.discovery.IdpIdentifier;
import com.google.step2.openid.ui.UiMessageRequest;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerAssociationStore;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;

import java.util.Map;

/**
 * User: Anvarbek
 * Date: Jun 16, 2010
 * Time: 3:45:32 PM
 */
public class GeneralGoogleMarketplaceService {

    private ConsumerHelper consumerHelper;

    public GeneralGoogleMarketplaceService(ConsumerAssociationStore consumerAssociationStore) {
        ConsumerFactory factory = new ConsumerFactory(consumerAssociationStore);
        consumerHelper = factory.getConsumerHelper();
    }

    public String handleRequest(String domain, String realm, String returnTo) {

        if (domain != null) {
            // User attempting to login with provided domain, build and OpenID request and redirect
            try {
                AuthRequest authRequest = startAuthentication(domain, realm, returnTo);
                return authRequest.getDestinationUrl(true);
            } catch (OpenIDException e) {
                System.out.println("Error initializing OpenID request");
            }

        }
        return null;
    }

    public UserInfo handleResponse(String url, Map map) {
        UserInfo user = null;
        try {
            user = completeAuthentication(map);
        } catch (OpenIDException e) {
            System.out.println("Error processing OpenID response");

        }
        return user;
    }

    UserInfo completeAuthentication(Map map)
            throws OpenIDException {

        ParameterList openidResp = new ParameterList(map);
        return null;
    }

    UserInfo onSuccess(AuthResponseHelper helper) {
        UserInfo userInfo = new UserInfo(helper.getClaimedId().toString(),
                helper.getAxFetchAttributeValue(Step2.AxSchema.EMAIL),
                helper.getAxFetchAttributeValue(Step2.AxSchema.FIRST_NAME),
                helper.getAxFetchAttributeValue(Step2.AxSchema.LAST_NAME),
                helper.getAxFetchAttributeValue(Step2.AxSchema.COUNTRY));
        userInfo.setUserNameBeforeAt(userInfo.getEmail().split("@")[0]);   //anvar.abidov
        userInfo.setDomain(userInfo.getEmail().split("@")[1]);    //edatasite.com
        return userInfo;
    }


    private AuthRequest startAuthentication(String op, String realm, String returnToUrl)
            throws OpenIDException {
        IdpIdentifier openId = new IdpIdentifier(op);


        AuthRequestHelper helper = consumerHelper.getAuthRequestHelper(openId, returnToUrl);
        addAttributes(helper);

        AuthRequest authReq = helper.generateRequest();
        authReq.setRealm(realm);

        UiMessageRequest uiExtension = new UiMessageRequest();
        uiExtension.setIconRequest(true);
        authReq.addExtension(uiExtension);
        return authReq;
    }

    private void addAttributes(AuthRequestHelper helper) {
        helper.requestAxAttribute(Step2.AxSchema.EMAIL, true)
                .requestAxAttribute(Step2.AxSchema.FIRST_NAME, true)
                .requestAxAttribute(Step2.AxSchema.LAST_NAME, true)
                .requestAxAttribute(Step2.AxSchema.COUNTRY, true);
    }
}