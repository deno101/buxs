package com.dnz.local.buxs.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileUtils {

    public static boolean createNewFile(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        boolean isCreated = false;
        try {
            isCreated = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    public static boolean createNewFileAndWrite(Context context, String fileName, String data){
        File file = new File(context.getFilesDir(), fileName);
        boolean isCreated = false;
        try {
            isCreated = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!file.exists()){
            return false;
        }

        try {
            FileOutputStream fout = new FileOutputStream(file);
            JSONObject jsonObject = new JSONObject(data);

            // Try Locking File
            FileChannel fileChannel = fout.getChannel();
            FileLock lock;

            while ((lock = fileChannel.tryLock(0, Long.MAX_VALUE, false)) == null){
                Thread.currentThread();
                Thread.sleep(200);
            }

            fout.write(jsonObject.toString().getBytes());
            lock.release();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return isCreated;
    }
}
