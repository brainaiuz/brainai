package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

/**
 * Created by User on 7/7/2016.
 */
public class RelatedLinkRPC implements Serializable {
    private String innerHTML;
    private String href;
    private String fromtype;

    public String getInnerHTML() {
        return innerHTML;
    }

    public void setInnerHTML(String innerHTML) {
        this.innerHTML = innerHTML;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getFromtype() {
        return fromtype;
    }

    public void setFromtype(String fromtype) {
        this.fromtype = fromtype;
    }
}
