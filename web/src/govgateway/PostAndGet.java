package govgateway;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 03.03.2010
 * Time: 21:37:07
 * To change this template use File | Settings | File Templates.
 */
public class PostAndGet {
    public static String REQUEST = "https://secure.gateway.gov.uk/submission";
    public static String POLL = "https://secure.gateway.gov.uk/poll";
    public static String REQUEST_TEST = "https://secure.dev.gateway.gov.uk/submission";
    public static String POLL_TEST = "https://secure.dev.gateway.gov.uk/poll";

    public PostAndGet() {
    }

    public static GovGatewayResponse doPostAndGetResponse(String strXML, String submitURL) {
        GovGatewayResponse response = new GovGatewayResponse();

        try {
            // Prepare HTTP post
            HttpPost post = new HttpPost(submitURL);

            // Request content will be retrieved directly
            // from the input stream
            // Per default, the request content needs to be buffered
            // in order to determine its length.
            // Request body buffering can be avoided when
            // content length is explicitly specified
//            post.setRequestEntity(new InputStreamRequestEntity(
//                    new FileInputStream(input), input.length()));
            post.setEntity(new ByteArrayEntity(strXML.getBytes()));

            // Specify content type and encoding
            // If content encoding is not explicitly specified
            // ISO-8859-1 is assumed
            post.setHeader("Content-type", "text/xml; charset=utf-8");

            // Get HTTP client
            ;

            // Execute request
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

                HttpResponse httpResponse = httpclient.execute(post);
                int result = httpResponse.getStatusLine().getStatusCode();
                response.setCode(result);
                // Display status code
                //System.out.println("Response status code: " + result);

                // Display response
                //System.out.println("Response body: ");
                String responseContent = EntityUtils.toString(post.getEntity());
                //System.out.println(responseContent);
                response.setContent(responseContent);
                if (result == 200 && responseContent.contains("<Qualifier>")) {
                    response.setQualifier(getValue("<Qualifier>", "</Qualifier>", responseContent));
                }
                if (result == 200 && responseContent.contains("<CorrelationID>")) {
                    response.setCorrelationID(getValue("<CorrelationID>", "</CorrelationID>", responseContent));
                }
                if (result == 200 && responseContent.contains("<Class>")) {
                    response.setResponseClass(getValue("<Class>", "</Class>", responseContent));
                }

                return response;
            } finally {
                // Release current connection to the connection pool
                // once you are done
                post.releaseConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValue(String startTag, String endTag, String content) {
        if (content != null && content.contains(startTag)) {
            return content.substring(content.indexOf(startTag) + startTag.length(), content.indexOf(endTag));
        } else {
            return "";
        }
    }

    public static String getAttValue(String startTag, String endTag, String content) {
        if (content != null && content.contains(startTag)) {
            return content.substring(content.indexOf(startTag) + startTag.length(), content.indexOf(endTag, content.indexOf(startTag) + startTag.length()));
        } else {
            return "";
        }
    }
}
