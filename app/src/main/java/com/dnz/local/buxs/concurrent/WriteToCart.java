package com.dnz.local.buxs.concurrent;

import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class WriteToCart extends AsyncTask<Object, Void, Void> {
    private AppCompatActivity activity;
    private final String filename = "cart";


    public WriteToCart(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        boolean isAddedToCart = false;
        if (objects[0] instanceof ArrayList){
            ArrayList<Integer> integers = (ArrayList<Integer>) objects[0];

            writeToFile(integers);
        }else{
            throw new ClassCastException("Unable to cast from Object to ArrayList");
        }
        return null;
    }


    private void writeToFile(ArrayList<Integer> cartList) {
        try {
            JSONArray array = new JSONArray(cartList);
            JSONObject jsonObject = new JSONObject();

            jsonObject = jsonObject.put("cart", array);
            String data = jsonObject.toString();

            FileOutputStream fout = activity.openFileOutput(filename, Context.MODE_PRIVATE);

            FileChannel fileChannel = fout.getChannel();
            FileLock lock;

            // Pause execution until lock is acquired
            while((lock = fileChannel.tryLock(0, Long.MAX_VALUE, false)) == null){
                Thread.currentThread();
                Thread.sleep(200);
            }

            fout.write(data.getBytes());
            lock.release();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
