package chai_4d.mbus.map.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpURLUtil
{
    private static final Logger log = LogManager.getLogger(HttpURLUtil.class);

    public static final String callweb = PropertyUtil.URL.getString("callweb");

    private static final String url = PropertyUtil.URL.getString("url");
    private static final String USER_AGENT = "Mozilla/5.0";

    private HttpURLUtil()
    {
    }

    public static String sendGet(String param)
    {
        try
        {
            URL obj = new URL(url + param);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            log.debug("Sending 'GET' request to URL : " + url + param);
            log.debug("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return "";
    }
}
