package com.jerry.serverlist;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 4/12/14.
 */
public class ServerListApplication {
    public static final int EDIT_ACTIVITY_RC = 1;
    private static final String CLASS_NAME = ServerListApplication.class.getSimpleName();
    List<Chespirito> itemList = new ArrayList<Chespirito>();

    /*public ServerListApplication() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
            }
        });
    }*/



    /*@Override
    public void onCreate() {
        super.onCreate();
        itemList = new ArrayList<Chespirito>();
    }*/

    /**
     * Returns the itemList, an array of Item objects.
     * @return itemList
     */
    public List<Chespirito> getItemList() {
        return itemList;
    }

    public void setItemList(List<Chespirito> list) {
        itemList = list;
    }
}
