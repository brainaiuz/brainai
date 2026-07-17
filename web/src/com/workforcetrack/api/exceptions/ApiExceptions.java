package com.workforcetrack.api.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 15.05.12
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class ApiExceptions {

    public static final BaseApiException RUNTIME_EXCEPTION_BASE = new BaseApiException(0, "Runtime exception!");
    //Base Error 1-99
    public static final BaseApiException SESSION_EMPTY = new BaseApiException(1, "Session is empty!");
    public static final BaseApiException SESSION_WRONG = new BaseApiException(2, "Session wrong!");
    public static final BaseApiException SIGNATURE_WRONG = new BaseApiException(3, "Signature mismached or wrong!");
    public static final BaseApiException PARAMS_INCORRECT = new BaseApiException(4, "Method parameters are incorrect");
    public static final BaseApiException USER_NOT_FOUND = new BaseApiException(5, "User not found!");
    public static final BaseApiException INCORRECT_AUTH_INFO = new BaseApiException(6, "Incorrect username or password");
    public static final BaseApiException USER_NOT_ACTIVATED = new BaseApiException(7, "User not activated!");
    public static final BaseApiException INCORRECT_COMPANY_ID = new BaseApiException(8, "Incorrect companyID!");
    public static final BaseApiException SESSION_EXPIRED = new BaseApiException(9, "Session expired!");
    public static final BaseApiException CLIENT_NAME_EMPTY = new BaseApiException(10, "Client name is empty!");


}
