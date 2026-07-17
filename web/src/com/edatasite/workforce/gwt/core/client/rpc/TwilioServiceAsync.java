package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.MyCallsSettings;
import com.edatasite.workforce.gwt.contact.client.rpc.SipuniSettings;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Hayot
 * Date: Apr 30, 2018
 * Time: 4:56:48 PM
 */
public interface TwilioServiceAsync {
    void getCountryNameByPhoneNumber(String phoneNumber,AsyncCallback<String> asyncCallback);

    void list(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<TwilioSettings>> asyncCallback);

    void listAsteriskSettings(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<AsteriskSettings>> asyncCallback);


    void listSipuniSettings(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SipuniSettings>> asyncCallback);

    void save(TwilioSettings settings, AsyncCallback<Integer> asyncCallback);

    void save(AsteriskSettings settings, AsyncCallback<Integer> asyncCallback);


    void save(SipuniSettings settings,AsyncCallback<Integer> asyncCallback);

    void delete(Integer settingsId, AsyncCallback<Boolean> asyncCallback);

    void deleteAsteriskSettings(Integer settingsId, AsyncCallback<Boolean> asyncCallback);

    void deleteSipuniSettings(Integer settingsId, AsyncCallback<Boolean> asyncCallback);

    void get(Integer objectId, AsyncCallback<TwilioSettings> asyncCallback);

    void getAsteriskSettings(Integer objectId, AsyncCallback<AsteriskSettings> asyncCallback);


    void getSipuniSettings(Integer objectId, AsyncCallback<SipuniSettings> asyncCallback);

    void getByNumber(String number, AsyncCallback<TwilioSettings> asyncCallback);

    void encrypt(String number, AsyncCallback<String> asyncCallback);

    void makeACall(String fromNumber, String toNumber, AsyncCallback<String> callback);

    void getRecordUrls(String callSid, AsyncCallback<ArrayList<String>> callback);

    void getTwilioToken(Integer twilioSettingId, AsyncCallback<String> callback);

    Request getContactList(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<TwilioContactItem[]> async);

    void getRecentCallLogs(int activityType, String relationType, Integer relationID, AsyncCallback<ArrayList<Appointment>> callback);

    void getContactByContactNumber(String lastNumber, AsyncCallback<ArrayList<TwilioContactItem>> abstractAsyncCallback);

    void getSmsByNumber(String relationType, Integer relationID, String number, AsyncCallback<ArrayList<SmsSendItem>> asyncCallback);

    void sendSms(String number, String text, RelationItem relationItem, AsyncCallback<SmsSendItem> asyncCallback);

    void listMyCallsSettings(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<MyCallsSettings>> asyncCallback);

    void save(MyCallsSettings settings, AsyncCallback<Integer> asyncCallback);

    void deleteMyCallsSettings(Integer settingsId, AsyncCallback<Boolean> asyncCallback);

    void getMyCallsSettings(Integer objectId, AsyncCallback<MyCallsSettings> asyncCallback);

}