package com.workforcetrack.mobile.rpc.exception;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/11/11
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncorrectParametersException extends Exception{

    public IncorrectParametersException(){

    }

    public IncorrectParametersException(String msg) {
        super(msg);
    }
}