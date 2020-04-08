package com.dnz.local.buxs.concurrent;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveFromCart extends AsyncTask<Object, Void, Void> {

    private static final String TAG = "RemoveFromCart";
    private final String FILENAME = "cart";

    @Override
    protected Void doInBackground(Object... objects) {
        int position = (int) objects[0];
        AppCompatActivity activity = (AppCompatActivity) objects[1];

        JSONObject data = readFile(activity);

        writeEditedData(position, activity, data);
        return null;
    }

    private JSONObject readFile(AppCompatActivity activity){
        JSONObject jsonObject;
        try {
            FileInputStream fin = activity.openFileInput(FILENAME);
            InputStreamReader reader;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            } else {
                reader = new InputStreamReader(fin, "UTF-8");
            }

            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                builder.append(line);
            }

            jsonObject = new JSONObject(builder.toString());
            return jsonObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeEditedData(int position, AppCompatActivity activity, JSONObject data){
        try {
            FileOutputStream fout = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            JSONArray jsonArray = data.getJSONArray("cart");
            jsonArray = removeItemAtIndex(position, jsonArray);

            data.put("cart", jsonArray);

            fout.write(data.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray removeItemAtIndex(int index, JSONArray oldArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0;i < oldArray.length()-1; i++){
            if (i == index){
                continue;
            }

            jsonArray.put(oldArray.get(index));
        }
        return jsonArray;
    }
}
