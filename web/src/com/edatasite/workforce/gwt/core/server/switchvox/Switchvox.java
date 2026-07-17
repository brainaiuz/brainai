package com.edatasite.workforce.gwt.core.server.switchvox;


import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 */
public class Switchvox {
    static Map<String, Long> lastCheckMap = new ConcurrentHashMap<>();
    static Map<String, List<SwitchvoxResult.CurrentCalls.CallItem>> callsMap = new ConcurrentHashMap<>();
    static Map<String, Integer> exceptionCountMap = new ConcurrentHashMap<>();
    private static JAXBContext jaxbContext;
    private static ThreadLocal<Switchvox> instance = new ThreadLocal<>();
    private Switchvox switchvoxConnect;

    private Switchvox() {
        initContexts();
    }

    public static Switchvox getInstance() {
        if (instance.get() == null) {
            instance.set(new Switchvox());
        }
        return instance.get();

    }

    public void initContexts() {
        try {
            jaxbContext = JAXBContext.newInstance(SwitchvoxResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createCall(SwitchvoxCredentials cred, String extension, String number) {
        if (extension == null) {
            return;
        }
        sendRequest("\n" +
                "<request method=\"switchvox.call\">\n" +
                "\t<parameters>\n" +
                "\t\t<dial_first>" + extension + "</dial_first>\n" +
                "\t\t<dial_second>" + number + "</dial_second>\n" +
                "\t\t<dial_as_account_id>" + getAccountIDByExtension(cred, extension) + "</dial_as_account_id>\n" +
//                "\t\t<ignore_user_call_rules>1</ignore_user_call_rules>\n" +
                "\t</parameters>\n" +
                "</request>\n" +
                "\n", cred);
    }

    public Integer getAccountIDByExtension(SwitchvoxCredentials cred, String extension) {
        try {
            SwitchvoxResponse resp = sendRequest("<request method=\"switchvox.extensions.getInfo\">\n" +
                    "\t<parameters>\n" +
                    "\t\t<extensions>\n" +
                    "\t\t\t<extension>" + extension + "</extension>\n" +
                    "\t\t</extensions>\n" +
                    "\t</parameters>\n" +
                    "</request>", cred);
            return resp.getResult().getExtensions().getExtension().getAccount_id();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1000;//return 1000 by default
    }

    public SwitchvoxResult.CurrentCalls.CallItem getCurrentCallerPhone(SwitchvoxCredentials cred, String extension) {
        String domain = cred.getDomain();
        if (extension == null) {
            return null;
        }
        synchronized (this) {
            if (lastCheckMap.get(domain) == null || lastCheckMap.get(domain) + 10 * 1000 < System.currentTimeMillis()) {
                lastCheckMap.put(domain, System.currentTimeMillis());
                callsMap.put(domain, getCurrentCalls(cred, extension));
            }
        }

        if (callsMap.get(domain) != null && !callsMap.get(domain).isEmpty()) {
            for (SwitchvoxResult.CurrentCalls.CallItem o : callsMap.get(domain)) {
//                System.out.println(o.getFrom_caller_id_number() + "->" + o.getTo_caller_id_number() + " dur:" + o.getDuration());
                if (extension.equals(o.getTo_caller_id_number())) {
                    return o;
                }
            }
        }
        return null;
    }

    public List<SwitchvoxResult.CurrentCalls.CallItem> getCurrentCalls(SwitchvoxCredentials cred, String extension) {
        SwitchvoxResponse resp = sendRequest("<request method=\"switchvox.currentCalls.getList\">\n" +
                "    <parameters>\n" +
                "    </parameters>\n" +
                "</request>\n", cred);
//        SwitchvoxResponse resp = getPojo("<response method=\"switchvox.currentCalls.getList\">\n" +
//                "\t<result>\n" +
//                "\t\t<current_calls total_items=\"2\">\n" +
//                "                        <current_call state=\"talking\" start_time=\"2009-07-23 15:20:15\" duration=\"16\" to_caller_id_number=\"202\" from_caller_id_name=\"Jim Jones\" to_caller_id_name=\"Dave Smith\" format=\"ulaw-&gt;ulaw\" from_caller_id_number=\"00496950993068\" id=\"SIP/didproxy36-6602\" />\n" +
//                "                        <current_call lotnum=\"701\" state=\"parked\" start_time=\"2009-07-23 15:20:15\" duration=\"16\" to_caller_id_number=\"700\" from_caller_id_name=\"Cell Phone  CA\" to_caller_id_name=\"701 thru 799\" format=\"ulaw\" from_caller_id_number=\"6195551212\" id=\"IAX2/didproxy36-6602\" />\n" +
//                "\t\t</current_calls>\n" +
//                "\t</result>\n" +
//                "</response>\n" +
//                "\n");
        if (resp != null && resp.getResult() != null && resp.getResult().getCurrentCalls() != null && resp.getResult().getCurrentCalls().getCalls() != null) {
            return resp.getResult().getCurrentCalls().getCalls();
        } else {
            return new ArrayList<>();
        }
    }

    public SwitchvoxResponse sendRequest(String strXML, SwitchvoxCredentials cred) {
        try {
//            System.setProperty("socksProxySet", "false");
//            System.setProperty("socksProxyHost", "127.0.0.1");
//            System.setProperty("socksProxyPort", "9999");

//            PostMethod post = new PostMethod("https://demo4.switchvox.com/xml");
            String domain = cred.getDomain();
//            String domain = "demo4.switchvox.com";
            HttpPost post = new HttpPost("https://" + domain + "/xml");
            post.setEntity(new ByteArrayEntity(strXML.getBytes()));
            post.setHeader("Content-type", "application/text");

            final BasicCredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
            provider.setCredentials(authScope, new UsernamePasswordCredentials(cred.getUsername(), cred.getPassword()));
            // Get HTTP client
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(6000)
                    .build();

            try (CloseableHttpClient httpclient = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build()) {
                // Execute request
                SwitchvoxResponse resp;

                HttpResponse httpResponse = httpclient.execute(post);
                resp = getPojo(EntityUtils.toString(httpResponse.getEntity()));
//                System.out.println(status + "\n" + post.getResponseBodyAsString());
                exceptionCountMap.put(cred.getDomain(), 0);
                return resp;
            } finally {
                post.releaseConnection();
            }
        } catch (IOException e) {
            exceptionCountMap.put(cred.getDomain(), exceptionCountMap.get(cred.getDomain()) != null ? (exceptionCountMap.get(cred.getDomain()) + 1) : 1);
            if (exceptionCountMap.get(cred.getDomain()) > 5) {
                e.printStackTrace();
                lastCheckMap.put(cred.getDomain(), System.currentTimeMillis() + 60 * 60 * 1000);//do not check for 1 hour
                callsMap.clear();
                throw new RuntimeException(">>>>>>>>>>> Switchvox cannot connect to " + cred.getDomain());
            }
        }
        return null;
    }

    public SwitchvoxResponse getPojo(String xmlString) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (SwitchvoxResponse) (SwitchvoxResponse) unmarshaller.unmarshal(new ByteArrayInputStream(xmlString.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
