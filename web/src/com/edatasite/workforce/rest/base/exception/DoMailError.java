package com.edatasite.workforce.rest.base.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 * Date: 12/20/13
 * Time: 5:37 PM
 */
@XmlRootElement(name = "KpiError")
public class DoMailError implements Serializable {
    private ServiceExceptionDetails errors[];

    public DoMailError(ServiceExceptionDetails[] faultDetails) {
        this.errors = faultDetails;
    }

    public ServiceExceptionDetails[] getErrors() {
        return errors;
    }

    public void setErrors(ServiceExceptionDetails[] errors) {
        this.errors = errors;
    }
}
