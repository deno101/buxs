package com.dnz.local.buxs.net;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLBuilder {
    private static final String IP = "192.168.43.210";
    private static final String PORT = "443";

    public static String buildURL(String path) {
        return String.format("http://%s:%s/%s", IP, PORT, path);
    }

    public static String appendQuery(String url, String query) {
        return url + "?" + query;
    }

    public static String buildURL(String path, String query) {
        return String.format("http://%s:%s/%s?%s", IP, PORT, path, query);
    }

    public static URI getBackendURI(){
        URI url = null;
        try {
            url = new URI(String.format("http://%s:%s/mplace",IP,PORT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return url;
    }
}
