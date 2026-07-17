package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.MyCallsSettings;
import com.edatasite.workforce.gwt.contact.client.rpc.SipuniSettings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Hayot
 * Date: April 30, 2018
 * Time: 4:54:05 PM
 */
public interface TwilioService extends RemoteService {
    String getCountryNameByPhoneNumber(String phoneNumber);

    ArrayList<Appointment> getRecentCallLogs(int activityType, String relationType, Integer relationID);

    ArrayList<TwilioContactItem> getContactByContactNumber(String lastNumber);

    ArrayList<SmsSendItem> getSmsByNumber(String relationType, Integer relationID, String number);

    SmsSendItem sendSms(String number, String text, RelationItem relationItem);

    ListResult<TwilioSettings> list(ListingFilterParameter listingFilterParameter);

    ListResult<AsteriskSettings> listAsteriskSettings(ListingFilterParameter listingFilterParameter);

    ListResult<SipuniSettings> listSipuniSettings(ListingFilterParameter listingFilterParameter);

    Integer save(TwilioSettings settings);

    Integer save(AsteriskSettings settings);

    Integer save(SipuniSettings settings);

    Boolean delete(Integer settingsId);

    Boolean deleteAsteriskSettings(Integer settingsId);

    Boolean deleteSipuniSettings(Integer settingsId);

    TwilioSettings get(Integer id);

    AsteriskSettings getAsteriskSettings(Integer id);

    SipuniSettings getSipuniSettings(Integer id);

    TwilioSettings getByNumber(String number);

    String encrypt(String number);

    String getTwilioToken(Integer twilioSettingId);

    String makeACall(String fromNumber, String toNumber);

    ArrayList<String> getRecordUrls(String callSid);

    TwilioContactItem[] getContactList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    ListResult<MyCallsSettings> listMyCallsSettings(ListingFilterParameter listingFilterParameter);

    Integer save(MyCallsSettings settings);

    Boolean deleteMyCallsSettings(Integer settingsId);

    MyCallsSettings getMyCallsSettings(Integer id);


    class App {
        public static TwilioServiceAsync get() {
            ServiceDefTarget target = GWT.create(TwilioService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/twilio");
            return (TwilioServiceAsync) target;
        }
    }
}