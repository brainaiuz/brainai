package com.edatasite.workforce.gwt.core.server.office365.utils;

import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Fetcher implements Office365Constants {
    private static final Logger log = LoggerFactory.getLogger(Office365Fetcher.class);
    private static final TypeReference<Office365BaseResource> TYPE = new TypeReference<Office365BaseResource>() {
    };

    public static class Request<T extends Office365BaseResource> {
        private final String url;
        private final String storageType;

        private HttpRequestBase method;

        private Office365ODataQuery query;
        private final Office365AccessTokenDTO token;
        private Office365BaseResource resource;
        private String realmString;
        private String jsonResource;

        private Class<T> aClass;
        private TypeReference<?> typeReference;

        private byte[] bytes;

        public Request(String storageType, String url, Office365AccessTokenDTO token) {
            this.storageType = storageType;
            this.url = url;
            this.token = token;
        }

        public Request<T> setQuery(Office365ODataQuery query) {
            this.query = query;
            return this;
        }

        public Request<T> setResource(Office365BaseResource resource) {
            this.resource = resource;
            return this;
        }

        public Request<T> setResource(String jsonResource) {
            this.jsonResource = jsonResource;
            return this;
        }

        public Request<T> setBytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Request<T> setClass(Class<T> aClass) {
            this.aClass = aClass;
            return this;
        }

        public Request<T> setTypeReference(TypeReference<?> typeReference) {
            this.typeReference = typeReference;
            return this;
        }

        public Request<T> makeGetMethod() {
            this.method = new HttpGet(this.url);

            return this;
        }

        public Request<T> makePutMethod() {
            this.method = new HttpPut(this.url);

            return this;
        }

        public Request<T> makePostMethod() {
            this.method = new HttpPost(this.url);

            return this;
        }

        public Request<T> makePatchMethod() {
            this.method = new HttpPatch(this.url);

            return this;
        }

        public Request<T> makeDeleteMethod() {
            this.method = new HttpDelete(this.url);

            return this;
        }

        public Request<T> setPostParameter(String key, String value) {
            if (this.method == null || !(this.method instanceof HttpPost)) {
                this.method = new HttpPost(this.url);
            }

            try {
                URI uri = new URIBuilder(method.getURI())
                        .addParameter(key, value)
                        .build();
                method.setURI(uri);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            return this;
        }

        public Response<T> sendGet() {
            this.makeGetMethod();
            return this.send();
        }

        public Response<T> sendPut() {
            this.makePutMethod();
            return this.send();
        }

        public Response<T> sendPost() {
            this.makePostMethod();
            return this.send();
        }

        public Response<T> sendJSonPost() {
            this.makePostMethod();
            return this.sendJSon();
        }

        public Response<T> sendPatch() {
            this.makePatchMethod();
            return this.send();
        }

        public Response<T> sendDelete() {
            this.makeDeleteMethod();
            return this.send();
        }

        public Response<T> send() {
            T response = null;
            int statusCode = 0;
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                if (this.method == null) {
                    this.makeGetMethod();
                }

                log.trace(String.format("Send %s", this.url));

                if (this.query != null) {
                    try {
                        URI uri = new URIBuilder(method.getURI())
                                .addParameters(this.query.toNameValuePair())
                                .build();
                        method.setURI(uri);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (this.token != null) {
                    this.method.addHeader("Authorization", "Bearer " + this.token.getAccessToken());
                } else if (OFFICE_SHARE_POINT.equals(storageType) && url.contains("/_vti_bin/client.svc")) {
                    this.method.addHeader("Authorization", "Bearer ");
                }
                if (OFFICE_SHARE_POINT.equals(storageType)) {
                    method.addHeader("Accept", "application/json; odata=verbose");
                }
                if (this.method instanceof HttpEntityEnclosingRequestBase) {
                    if (this.bytes != null) {
                        if (OFFICE_ONE_DRIVE.equals(storageType)) {
                            this.method.addHeader("Content-Type", "text/plain");
                        } else {
                            this.method.addHeader("Content-Type", "application/x-www-form-urlencoded");
                            this.method.addHeader("Content-Length", String.valueOf(this.bytes.length));
                        }
                        ((HttpEntityEnclosingRequestBase) this.method).setEntity(new ByteArrayEntity(this.bytes));
                    } else if (this.resource != null) {
                        String json = this.resource.toJSON();
                        if (json != null) {
                            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                            ((HttpEntityEnclosingRequestBase) this.method).setEntity(entity);
                        }
                    }
                }

                HttpResponse httpResponse = client.execute(this.method);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (this.method.getFirstHeader("WWW-Authenticate") != null) {
                    String bearerResponseHeader = this.method.getFirstHeader("WWW-Authenticate").getValue();
                    int beginIndex = bearerResponseHeader.indexOf("Bearer realm=\"") + 14;
                    realmString = bearerResponseHeader.substring(beginIndex, beginIndex + 36);
                }
                boolean ok = statusCode == 200 || statusCode == 201 || statusCode == 202 || statusCode == 203;

                if (ok && StringUtils.isNotEmpty(json)) {
                    if (this.typeReference != null) {
                        response = Office365Utils.fromJSON(json, this.typeReference);
                    } else if (this.aClass != null) {
                        response = Office365Utils.fromJSON(json, this.aClass);
                    } else {
                        response = Office365Utils.fromJSON(json, TYPE);
                    }
                } else if (statusCode == 204) {
                    System.out.println(" *************   statusCode ***********  " + statusCode);
                } else {

                    if (statusCode == 401) {
                        if (realmString == null || realmString.isEmpty()) {
                            if (this.token == null) {
                                log.trace("Access Token is empty");
                            } else {
                                log.trace("Invalid access token: " + this.token.toJSON());
                            }
                        }
                    } else {
                        String message = "HTTP Request Error: " + statusCode + " - " + json;

                        log.trace(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.method.releaseConnection();
            }

            return new Response<>(statusCode, response, realmString);
        }

        public Response<T> sendJSon() {
            T response = null;
            int statusCode = 0;
            try (CloseableHttpClient client = HttpClients.createDefault()) {

                if (this.method == null) {
                    this.makeGetMethod();
                }

                log.trace(String.format("Send %s", this.url));

                if (this.query != null) {
                    try {
                        URI uri = new URIBuilder(method.getURI())
                                .addParameters(this.query.toNameValuePair())
                                .build();
                        method.setURI(uri);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (this.token != null) {
                    this.method.addHeader("Authorization", "Bearer " + this.token.getAccessToken());
                } else if (OFFICE_SHARE_POINT.equals(storageType) && url.contains("/_vti_bin/client.svc")) {
                    this.method.addHeader("Authorization", "Bearer ");
                }
                if (OFFICE_SHARE_POINT.equals(storageType)) {
                    method.addHeader("Accept", "application/json; odata=verbose");
                }
                if (this.method instanceof HttpEntityEnclosingRequestBase) {
                    if (this.bytes != null) {
                        if (OFFICE_ONE_DRIVE.equals(storageType)) {
                            this.method.addHeader("Content-Type", "text/plain");
                        } else {
                            this.method.addHeader("Content-Type", "application/x-www-form-urlencoded");
                            this.method.addHeader("Content-Length", String.valueOf(this.bytes.length));
                        }
                        ((HttpEntityEnclosingRequestBase) this.method).setEntity(new ByteArrayEntity(this.bytes));
                    } else if (this.jsonResource != null) {
                        String json = this.jsonResource;
                        if (json != null) {
                            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                            ((HttpEntityEnclosingRequestBase) this.method).setEntity(entity);
                        }
                    }
                }

                HttpResponse httpResponse = client.execute(this.method);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (this.method.getFirstHeader("WWW-Authenticate") != null) {
                    String bearerResponseHeader = this.method.getFirstHeader("WWW-Authenticate").getValue();
                    int beginIndex = bearerResponseHeader.indexOf("Bearer realm=\"") + 14;
                    realmString = bearerResponseHeader.substring(beginIndex, beginIndex + 36);
                }
                boolean ok = statusCode == 200 || statusCode == 201 || statusCode == 202 || statusCode == 203;

                if (ok && StringUtils.isNotEmpty(json)) {
                    if (this.typeReference != null) {
                        response = Office365Utils.fromJSON(json, this.typeReference);
                    } else if (this.aClass != null) {
                        response = Office365Utils.fromJSON(json, this.aClass);
                    } else {
                        response = Office365Utils.fromJSON(json, TYPE);
                    }
                } else if (statusCode == 204) {
                    System.out.println(" *************   statusCode ***********  " + statusCode);
                } else {

                    if (statusCode == 401) {
                        if (realmString == null || realmString.isEmpty()) {
                            if (this.token == null) {
                                log.trace("Access Token is empty");
                            } else {
                                log.trace("Invalid access token: " + this.token.toJSON());
                            }
                        }
                    } else {
                        String message = "HTTP Request Error: " + statusCode + " - " + json;

                        log.trace(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.method.releaseConnection();
            }

            return new Response<>(statusCode, response, realmString);
        }

        public byte[] loadFile(int bytesSize) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            InputStream inputStream = null;
            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("range", String.valueOf(bytesSize));

                if (this.token != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + this.token.getAccessToken());
                }

                inputStream = connection.getInputStream();

                byte[] byteChunk = new byte[bytesSize];
                int n;

                while ((n = inputStream.read(byteChunk)) > 0) {
                    baos.write(byteChunk, 0, n);
                }

                return byteChunk;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    public static class Response<T extends Office365BaseResource> {
        private final int statusCode;
        private String realmString;
        private T resource;

        public Response(int statusCode, T resource, String realm) {
            this.statusCode = statusCode;
            this.resource = resource;
            this.realmString = realm;
        }

        public Response(int statusCode, String json) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public T getResource() {
            return resource;
        }

        public String getRealm() {
            return realmString;
        }
    }

}
