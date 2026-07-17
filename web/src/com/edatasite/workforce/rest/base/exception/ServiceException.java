package com.edatasite.workforce.rest.base.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 * Date: 12/20/13
 * Time: 5:20 PM
 */
@XmlRootElement(name = "ServiceException")
public class ServiceException extends Exception implements Serializable {

    private String name;
    private ServiceExceptionDetails faultDetails[];

    public ServiceException(ServiceExceptionDetails... faultDetails) {
        this.faultDetails = faultDetails;
    }

    public ServiceException(String message, ServiceExceptionDetails faultDetails[]) {
        super(message);
        this.faultDetails = faultDetails;
    }

    public ServiceExceptionDetails[] getFaultDetails() {
        return faultDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}