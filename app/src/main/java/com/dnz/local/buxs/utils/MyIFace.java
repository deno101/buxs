package com.dnz.local.buxs.utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MyIFace {
    public interface IFGetCartCount{
        void onPostExecuteThread(ArrayList<Integer> data);
    }


    public interface IFAddToCart{
        /*
        * To be used to make ui changes from AsyncTask AddToCart
        */
        void onPostExecuteThread(boolean alreadyInCart, ArrayList<Integer> productsInCart);
    }

    public interface TaskInterval{
        void runOnUiThread(Object... objects);
    }

    public static class MyTimedClass extends TimerTask{
        private AppCompatActivity activity;
        private TaskInterval task;
        private Object objects = null;

        public MyTimedClass(AppCompatActivity activity, TaskInterval task) {
            this.activity = activity;
            this.task = task;
        }

        public MyTimedClass(AppCompatActivity activity, TaskInterval task, Object... objects) {
            this.activity = activity;
            this.task = task;
            this.objects = objects;
        }


        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    task.runOnUiThread(objects);
                }
            });
        }
    }
}
