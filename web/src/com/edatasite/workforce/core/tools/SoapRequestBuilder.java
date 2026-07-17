package com.edatasite.workforce.core.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * Date: 06.08.12
 * Time: 10:25
 */
public class SoapRequestBuilder {
    String server = "";
    String webServicePath = "";
    String soapAction = "";
    String methodName = "";
    String xmlNamespace = "";
    private Vector paramNames = new Vector();
    private Vector paramData = new Vector();

    public void AddParameter(String Name, Object Data) {
        getParamNames().addElement(Name);
        getParamData().addElement(Data);
    }

    public String sendRequest() {
        String retval = "";
        Socket socket = null;
        try {
            socket = new Socket(getServer(), 80);
        } catch (Exception ex1) {
            return ("Error: " + ex1.getMessage());
        }

        try {
            OutputStream os = socket.getOutputStream();
            boolean autoflush = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            StringBuilder requestText = new StringBuilder();
            requestText.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            requestText.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
            requestText.append("<soap:Body>\n");
            requestText.append("<" + getMethodName() + " xmlns=\"" + getXmlNamespace() + "\">\n");
            //Parameters passed to the method are added here
            for (int t = 0; t < getParamNames().size(); t++) {
                String name = (String) getParamNames().elementAt(t);
                requestText.append("<" + name + ">" + getParamData().elementAt(t) + "</" + name + ">\n");
            }
            requestText.append("</" + getMethodName() + ">\n");
            requestText.append("</soap:Body>\n");
            requestText.append("</soap:Envelope>\n");

            // send an HTTP request to the web service
            StringBuilder requester = new StringBuilder();
            requester.append("POST " + getWebServicePath() + " HTTP/1.1\n");
            requester.append("Host: " + getServer() + "\n");
            requester.append("Content-Type: text/xml; charset=utf-8 \n");
            requester.append("Content-Length: " + requestText.length() + " \n");
            requester.append("SOAPAction: \"" + getSoapAction() + "\"\n");
            requester.append("Connection: Close\n");
            requester.append("\n");


            System.out.println(requester);
            System.out.println(requestText);

            out.println("POST " + getWebServicePath() + " HTTP/1.1");
            out.println("Host: " + getServer());
            out.println("Content-Type: text/xml; charset=utf-8");
            out.println("Content-Length: " + requestText.length());
            out.println("SOAPAction: \"" + getSoapAction() + "\"");
            out.println("Connection: Close");
            out.println();

            out.println(requestText.toString());

            out.println();

            // Если ушло то:
            // <SMSCinID>302b7f1f-d54c-459f-9e0c-8f3d99eec9d4</SMSCinID><ErrCode>00</ErrCode><ErrDesc>Success</ErrDesc>
            // при ошибке:
            // <SMSCinID /><ErrCode>99</ErrCode><ErrDesc>Error</ErrDesc>


            // Read the response from the server ... times out if the response takes
            // more than 3 seconds
            String inputLine;
            StringBuilder sb = new StringBuilder(1000);

            int wait_seconds = 60;
            boolean timeout = false;
            long m = System.currentTimeMillis();
            while ((inputLine = in.readLine()) != null && !timeout) {
                sb.append(inputLine + "\n");
                if ((System.currentTimeMillis() - m) > (1000 * wait_seconds)) {
                    timeout = true;
                }
            }
            in.close();

            System.out.println(sb.toString());

            // The StringBuffer sb now contains the complete result from the
            // webservice in XML format.  You can parse this XML if you want to
            // get more complicated results than a single value.

            String returnparam = getMethodName() + "Result";
            int start = sb.toString().indexOf("<" + returnparam + ">") + returnparam.length() + 2;
            int end = sb.toString().indexOf("</" + returnparam + ">");

            retval = "";
            //Extract a singe return parameter
            if (start >= 0 && end >= start) {
                retval = sb.toString().substring(start, end);
            } else if (timeout) {
                retval = "Error: response timed out.";
            }

            socket.close();
        } catch (Exception ex) {
            return ("Error: cannot communicate.");
        }

        return retval;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getWebServicePath() {
        return webServicePath;
    }

    public void setWebServicePath(String webServicePath) {
        this.webServicePath = webServicePath;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getXmlNamespace() {
        return xmlNamespace;
    }

    public void setXmlNamespace(String xmlNamespace) {
        this.xmlNamespace = xmlNamespace;
    }

    public Vector getParamNames() {
        return paramNames;
    }

    public void setParamNames(Vector paramNames) {
        this.paramNames = paramNames;
    }

    public Vector getParamData() {
        return paramData;
    }

    public void setParamData(Vector paramData) {
        this.paramData = paramData;
    }
}