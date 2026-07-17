package com.edatasite.workforce.rest.v2.release10.core.to.base;

import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Created by Anvar Akramov on 11/21/2019.
 */
public class HttpProxyRequestTO extends ResponseData {

    private String url;
    private HttpMethod method;
    private String requestJson;
    private List<NameValueDto> headers;
    private List<NameValueDto> params;

    public HttpProxyRequestTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public List<NameValueDto> getHeaders() {
        return headers;
    }

    public void setHeaders(List<NameValueDto> headers) {
        this.headers = headers;
    }

    public List<NameValueDto> getParams() {
        return params;
    }

    public void setParams(List<NameValueDto> params) {
        this.params = params;
    }
}
