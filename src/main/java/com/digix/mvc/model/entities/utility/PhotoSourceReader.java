package com.digix.mvc.model.entities.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class PhotoSourceReader {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public static String getSource(String objectID, String accessToken) {
        JSONObject json = null;
        try {
            json = readJsonFromUrl("https://graph.facebook.com/" + objectID + "?fields=source&access_token=" + accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json != null ? json.get("source").toString() : null;
    }
}
