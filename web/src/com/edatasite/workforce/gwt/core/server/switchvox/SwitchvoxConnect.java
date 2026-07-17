package com.edatasite.workforce.gwt.core.server.switchvox;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 */
public class SwitchvoxConnect {
    private static JAXBContext jaxbContext;
    private SwitchvoxConnect switchvoxConnect;

    public static void initContexts() {
        try {
            jaxbContext = JAXBContext.newInstance(SwitchvoxResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... arg) {
//        System.setProperty("socksProxySet", "false");
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", "9999");
        initContexts();
        getPojo("<response method=\"switchvox.currentCalls.getList\">\n" +
                "\t<result>\n" +
                "\t\t<current_calls total_items=\"2\">\n" +
                "                        <current_call state=\"talking\" start_time=\"2009-07-23 15:20:15\" duration=\"16\" to_caller_id_number=\"202\" from_caller_id_name=\"Jim Jones\" to_caller_id_name=\"Dave Smith\" format=\"ulaw-&gt;ulaw\" from_caller_id_number=\"090393930393\" id=\"SIP/didproxy36-6602\" />\n" +
                "                        <current_call lotnum=\"701\" state=\"parked\" start_time=\"2009-07-23 15:20:15\" duration=\"16\" to_caller_id_number=\"700\" from_caller_id_name=\"Cell Phone  CA\" to_caller_id_name=\"701 thru 799\" format=\"ulaw\" from_caller_id_number=\"6195551212\" id=\"IAX2/didproxy36-6602\" />\n" +
                "\t\t</current_calls>\n" +
                "\t</result>\n" +
                "</response>\n" +
                "\n");


//        SSLContext ctx = SSLContext.getInstance("TLS");
//        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
//        SSLContext.setDefault(ctx);
//        createCall("212", "202");
        getCurrentCalls("212");
//        getExtensionList();
//        getCurrentCalls("11");
    }

    public static void getCurrentCalls(String extension) {
        SwitchvoxResponse resp = sendRequest("<request method=\"switchvox.currentCalls.getList\">\n" +
                "    <parameters>\n" +
                "    </parameters>\n" +
                "</request>\n");
        for (SwitchvoxResult.CurrentCalls.CallItem o : resp.getResult().getCurrentCalls().getCalls()) {
            System.out.println(o.getFrom_caller_id_number() + "->" + o.getTo_caller_id_number() + " dur:" + o.getDuration());
        }
    }

    public static void getExtensionList() {
        sendRequest("<request method=\"switchvox.extensions.search\">" +
                "    <parameters>" +
                "        <min_extension>0</min_extension>" +
                "        <max_extension>1000</max_extension>" +
                "    </parameters>" +
                "</request>");
    }

    public static Integer getAccountIDByExtension(String extension) {
        try {
            SwitchvoxResponse resp = sendRequest("<request method=\"switchvox.extensions.getInfo\">\n" +
                    "\t<parameters>\n" +
                    "\t\t<extensions>\n" +
                    "\t\t\t<extension>" + extension + "</extension>\n" +
                    "\t\t</extensions>\n" +
                    "\t</parameters>\n" +
                    "</request>");
            return resp.getResult().getExtensions().getExtension().getAccount_id();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void createCall(String extension, String number) {
        Integer accountID = getAccountIDByExtension(extension);
        sendRequest("\n" +
                "<request method=\"switchvox.call\">\n" +
                "\t<parameters>\n" +
                "\t\t<dial_first>" + extension + "</dial_first>\n" +
                "\t\t<dial_second>" + number + "</dial_second>\n" +
                "\t\t<dial_as_account_id>" + ((accountID != null) ? accountID : "1000") + "</dial_as_account_id>\n" +
                "\t\t<variables>\n" +
                "\t\t\t<variable>balance=300</variable>\n" +
                "\t\t</variables>\n" +
                "\t\t<ignore_user_call_rules>1</ignore_user_call_rules>\n" +
                "\t</parameters>\n" +
                "</request>\n" +
                "\n");
    }

    public static SwitchvoxResponse sendRequest(String strXML) {
        try {

//            PostMethod post = new PostMethod("https://demo4.switchvox.com/xml");
            String domain = "pbx.cmedlab.com";
//            String domain = "demo4.switchvox.com";
//            String domain = "165.166.189.200";
            HttpPost post = new HttpPost("https://" + domain + "/xml");
            post.setEntity(new ByteArrayEntity(strXML.getBytes()));
            // Specify content type and encoding
            // If content encoding is not explicitly specified
            // ISO-8859-1 is assumed
            post.setHeader("Content-type", "application/text");
//            post.setDoAuthentication(true);
            // Get HTTP client
            final BasicCredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
            provider.setCredentials(authScope, new UsernamePasswordCredentials("kpi1", "TX6Q9g63"));
            // Get HTTP client
            try (CloseableHttpClient httpclient = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .build()) {
                // Execute request
                SwitchvoxResponse resp;

                HttpResponse httpResponse = httpclient.execute(post);
                String body = EntityUtils.toString(httpResponse.getEntity());
                resp = getPojo(body);
                System.out.println(httpResponse.getStatusLine().getStatusCode() + "\n" + body);
                return resp;
            } finally {
                // Release current connection to the connection poolT
                // once you are done
                post.releaseConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SwitchvoxResponse getPojo(String xmlString) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (SwitchvoxResponse) (SwitchvoxResponse) unmarshaller.unmarshal(new ByteArrayInputStream(xmlString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
