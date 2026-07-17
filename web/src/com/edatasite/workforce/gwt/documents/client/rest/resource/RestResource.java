package com.edatasite.workforce.gwt.documents.client.rest.resource;


import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class RestResource implements Serializable {

    protected RestResource() {
    }


    Integer objectId;

    String uri;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    /**
     * Retrieve the uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Modify the uri.
     *
     * @param aUri the path to set
     */
    public void setUri(String aUri) {
        uri = aUri;
        if (uri != null) {
            // Remove any parameter part
            int qm = uri.indexOf('?');
            if (qm >= 0) {
                uri = uri.substring(0, qm);
            }
        }
    }

    public static native String getDate(Long ms)/*-{
		return (new Date(ms)).toUTCString();
	}-*/;

    public abstract String getLastModifiedSince();
}
