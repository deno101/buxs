package com.dnz.local.buxs.net;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.dnz.local.buxs.auth.Authenticator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class MyCookieStore implements CookieStore {

    private static final String TAG = "MyCookieStore";
    private Context context;
    private CookieStore store;
    private String fileName;
    private Authenticator authenticator;

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public MyCookieStore(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;

        store = new CookieManager().getCookieStore();
        getCookiesFromFile();
    }

    private void saveCookieToFile() {
        ArrayList<HttpCookie> httpCookies = new ArrayList<>(store.getCookies());
        Map<Integer, HttpCookie> dictionary = new HashMap<>();
        for (int i = 0; i < httpCookies.size(); i++) {
            dictionary.put(i, httpCookies.get(i));
        }

        String data = new Gson().toJson(dictionary);

        FileOutputStream fout;
        try {
            fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fout.write(data.getBytes());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveCookieToFile: Fatal error writing to file FileNotFound");
            File file = new File(context.getFilesDir(), fileName);
            createNewFile(file);
            saveCookieToFile();
        } catch (IOException e) {
            Log.e(TAG, "saveCookieToFile: Fatal error writing to file IOException");
        }
    }

    private void getCookiesFromFile() {
        String line;
        try {
            FileInputStream fin = context.openFileInput(fileName);

            InputStreamReader reader = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            }else {
                reader = new InputStreamReader(fin, "UTF-8");
            }
            StringBuilder builder = new StringBuilder();

            BufferedReader reader1 = new BufferedReader(reader);
            line = reader1.readLine();
            while (line != null) {
                builder.append(line);
                line = reader1.readLine();
            }

            line = builder.toString();
            Log.e(TAG, "getCookiesFromFile: "+ line);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "getCookiesFromFile: FileNotFoundException");
            File file = new File(context.getFilesDir(), fileName);
            createNewFile(file);
            return;
        } catch (IOException e){
            Log.e(TAG, "getCookiesFromFile: IOException");
            return;
        }

        // convert the string to Json then to Cookies
        try {
            JSONObject jsonObject =  new JSONObject(line);
            Iterator<String> stringIterator = jsonObject.keys();

            while (stringIterator.hasNext()){
                JSONObject cookieInfo = jsonObject.getJSONObject(stringIterator.next());
                String name = cookieInfo.getString("name");
                String value = cookieInfo.getString("value");

                HttpCookie cookie = new HttpCookie(name, value);
                cookie.setDomain(cookieInfo.getString("domain"));

                //TODO : RECHECK FOR ERRORS IN LOWER DEVICES
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cookie.setHttpOnly(cookieInfo.getBoolean("httpOnly"));
                }

                cookie.setMaxAge(cookieInfo.getInt("maxAge"));
                cookie.setPath(cookieInfo.getString("path"));
                cookie.setSecure(cookieInfo.getBoolean("secure"));
                cookie.setDiscard(cookieInfo.getBoolean("toDiscard"));
                cookie.setVersion(cookieInfo.getInt("version"));
                add(null, cookie);
            }
        } catch (JSONException e) {
            Log.e(TAG, "getCookiesFromFile: ", e);
        }

    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        store.add(URI.create(cookie.getDomain()), cookie);
        if (cookie.getName().equals("username")){
            authenticator = new Authenticator(cookie.getValue());
        }
        saveCookieToFile();
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }

    private void createNewFile(File file){
        boolean isCreated;
        try {
            isCreated = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (isCreated){
            try {
                FileOutputStream fout = new FileOutputStream(file);
                String emptyJson = new JSONObject("{}").toString();

                fout.write(emptyJson.getBytes());
                Log.d(TAG, "createNewFile: file created");
            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
