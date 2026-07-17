package com.edatasite.workforce.gwt.backend.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abdulaziz
 * Date: Jul 28, 2010
 * Time: 7:59:02 PM
 */
public class CustomException extends Exception implements IsSerializable {
    public CustomException(){

    }
    private String message;
    public CustomException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

