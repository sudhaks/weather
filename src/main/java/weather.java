import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class weather {
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void main(String[] args)  throws Exception {
        System.out.println("Please enter latitude");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String latitude = reader.readLine();

        System.out.println("Please enter longitude");
        String longitude = reader.readLine();
        //latitude = "39.7456";longitude="-97.0892";
        String urlTxt = "https://api.weather.gov/points/" + latitude + "," + longitude;
        try {
            JSONObject jR = new JSONObject(sendGet(urlTxt));
            JSONObject jProp = jR.getJSONObject("properties");

            JSONObject jRe = new JSONObject(sendGet(jProp.getString("forecast")));
            JSONObject jPrope = jRe.getJSONObject("properties");
            JSONArray jPeriod = jPrope.getJSONArray("periods");
            JSONObject jF = jPeriod.getJSONObject(1);
            System.out.println("At Latitude = "+latitude +
                    ", Logitude = " + longitude + " on "+
                    jF.getString("name") +
                    " at time = " + jF.getString("startTime") +
                    " the Temperature is " + jF.getInt("temperature") +
                    " " + jF.getString("temperatureUnit"));

        } catch (IOException e){
            System.out.println("Error Http Get " + urlTxt);
        } finally {
            close();
        }
    }

    private static void close() throws IOException {
        httpClient.close();
    }

    private static String sendGet(String url) throws Exception {
        //ex: "https://api.weather.gov/points/39.7456,-97.0892"
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            try {
                // Get HttpResponse Status
                response.getStatusLine().getStatusCode();

                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                //System.out.println(headers);

                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    //System.out.println(result);
                    return (result);
                }
            } finally {
                response.close();
            }
        } catch (Exception e){
            throw e;
        }
        return null;
    }
}