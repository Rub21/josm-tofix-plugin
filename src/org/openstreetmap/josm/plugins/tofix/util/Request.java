package org.openstreetmap.josm.plugins.tofix.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.tofix.bean.AtributesBean;
import org.openstreetmap.josm.plugins.tofix.bean.ItemFixedBean;
import org.openstreetmap.josm.plugins.tofix.bean.TrackBean;
import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public class Request {

    public static String sendPOST(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        //START - POST
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        // os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        //POST - END
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Util.print(response.toString());
            return response.toString();

        } else {

            return null;
        }
    }

    public static void sendPOST_Json(String url, String object) throws IOException {
        HttpURLConnection con = (HttpURLConnection) ((new URL(url).openConnection()));
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        con.connect();
        byte[] outputBytes = object.getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(outputBytes);
        os.close();
        int responseCode = con.getResponseCode();
        Util.print(responseCode);
    }
    

    public static void sendPOST_fixed(String string_url, ItemFixedBean itemFixedBean) throws IOException {
        URL url = new URL(string_url);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write("user=" + itemFixedBean.getUser() + "&key=" + itemFixedBean.getKey() + "&editor=JOSM");
        writer.flush();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        writer.close();
        reader.close();
    }

    public static String sendGET(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        // con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
            return response.toString();

        } else {
            System.out.println("GET request not worked");
            return null;
        }

    }

}
