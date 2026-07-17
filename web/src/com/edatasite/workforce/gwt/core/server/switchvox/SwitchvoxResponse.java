package com.edatasite.workforce.gwt.core.server.switchvox;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result"
})
@XmlRootElement(name = "response")
public class SwitchvoxResponse {

    @XmlElement(name = "result")
    protected SwitchvoxResult result;

    @XmlAttribute
    protected String method;

    public SwitchvoxResult getResult() {
        return result;
    }

    public void setResult(SwitchvoxResult result) {
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
