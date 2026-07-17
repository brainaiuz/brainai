package govgateway;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Mar 10, 2010
 * Time: 2:10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class GovGatewayResponse {
    private int code;
    private String content;
    private String qualifier;
    private String correlationID;
    private String responseClass;

    public GovGatewayResponse() {
    }

    public GovGatewayResponse(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public String getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(String responseClass) {
        this.responseClass = responseClass;
    }
}
