package com.dnz.local.buxs.net;

public class URLBuilder {
    private static final String IP = "165.22.222.126";
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
}
