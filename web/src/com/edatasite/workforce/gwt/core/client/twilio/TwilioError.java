package com.edatasite.workforce.gwt.core.client.twilio;

import java.util.HashMap;
import java.util.Map;

public class TwilioError {
    Map<Integer, String> errors = new HashMap<>();
    public TwilioError() {
        errors.put(1, "No Error");
        errors.put(31000,	"Generic error.	A call error occurred that is not covered by a more specific error code. No further information available. Check the debugger for more info.");
        errors.put(31003,	"Connection timeout.	The server could not produce a response within a suitable amount of time.");
        errors.put(31004,	"Initialization error in iOS SDK.	The iOS SDK couldn't initialize user agent due to an internal error.");
        errors.put(31005,	"Connection error.	A connection error occurred during the call.");
        errors.put(31006,	"Audio device error.	Unable to start the audio device.");
        errors.put(21218,	"TwiML application not found.	The server was not able to find the TwiML application associated with the App SID.");
        errors.put(31100,	"Malformed request.	The request could not be understood due to malformed syntax.");
        errors.put(31106,	"Invalid data.	The provided data to handleMessage(...)/handleNotification(...) API was not valid.");
        errors.put(31107,	"No CallInvite with this CallSid.	A cancel notification was received for a non existing CallInvite.");
        errors.put(20101,	"Invalid access token.	The Access Token provided to the SDK is invalid. This may happen when the Access Token is structurally incorrect.");
        errors.put(20102,	"Invalid Access Token header.	The header of the Access Token provided to the SDK is invalid.");
        errors.put(20103,	"Invalid Access Token issuer/subject.	The issuer or subject of the Access Token provided is invalid.");
        errors.put(20104,	"Access Token expired or expiration date invalid.	The Access Token provided to the SDK has expired, the expiration time specified in the token was invalid, or the expiration time specified was too far in the future.");
        errors.put(20105,	"Access Token not yet valid.	The Access Token provided to the SDK is not yet valid.");
        errors.put(20106,	"Invalid Access Token grants.	The Access Token does not include voice grants.");
        errors.put(20107,	"Invalid Access Token signature.	The signature for the Access Token provided was invalid. Ensure the secret included in the Access Token is valid.");
        errors.put(20151,	"The authorization with Token failed.	The system could not perform the authorization for the given request.");
        errors.put(20157,	"Expiration Time Exceeds Maximum Time Allowed.	The expiration time provided in the Access Token exceeds the maximum duration allowed.");
        errors.put(20403,	"403 Forbidden.	The expiration time provided in the Access Token exceeds the maximum duration allowed.");
        errors.put(31201,	"Authorization error.	The server understood the request, but is refusing to fulfill it. This happens when a CallInvite is generated from invalid data.");
        errors.put(31301,	"Registration or Unregistration failed.	The registration request to Twilio failed with an exception. This can be caused by an http related error or by an invalid push credential.");
    }


    private static final TwilioError instance = new TwilioError();

    public static TwilioError getInstance() {
        return instance;
    }

    public boolean hasError(int code) {
        return errors.containsKey(code);
    }

    public String getError(int code) {
        return errors.get(code);
    }
}
