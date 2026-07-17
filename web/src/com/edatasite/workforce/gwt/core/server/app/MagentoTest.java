package com.edatasite.workforce.gwt.core.server.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Administrator on 13.11.2017.
 */
public class MagentoTest {

    public static void main(String[] args) {

        try {
            long start = System.currentTimeMillis();
            URL magentoUrl = new URL("http://essoman.com/tests/api_tester.php");
            HttpURLConnection connection = (HttpURLConnection) magentoUrl.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer str = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                str.append(inputLine);
            in.close();
            String response = "<pre>Array(    [0] => stdClass Object        (            [website_id] => 1            [name] => Main Website        )    [1] => stdClass Object        (            [website_id] => 2            [name] => Oman Security        ))<pre>";
            response = response.replace("<pre>Array", "").replace("<pre>", "");
            response = response.replaceAll("\\[", "").replaceAll("]", "");
            response = "{\"websites\": [" + response;
            response = response.replaceAll(" {2}", " ");
            response = response.replaceAll("=> stdClass Object", "");
            response = response.replaceAll("\\(", "{").replaceAll("\\)", "}");
            response = response.replaceFirst("\\{ {2}0 {5}\\{", "{");
            response = response.replaceAll("} {2}[1-9.] {5}\\{", "}, {");
            response = response.replaceAll("website_id =>", "\"website_id\": ").replaceAll("name =>", ",\"name\": ");
            response = response + "}";
            response = response.replace("}}}", "}]}");
            response = response.trim();
            JSONObject obj = new JSONObject(response);

            HashMap<Integer, String> webSitesMap = new HashMap<>();
            JSONArray arr = obj.getJSONArray("websites");
            for (int i = 0; i < arr.length(); i++)
            {
                String post_id = arr.getJSONObject(i).getString("website_id").trim();
                String post_name = arr.getJSONObject(i).getString("name").trim();
                webSitesMap.put(Integer.valueOf(post_id), post_name);
            }
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end-start) + " ms");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
