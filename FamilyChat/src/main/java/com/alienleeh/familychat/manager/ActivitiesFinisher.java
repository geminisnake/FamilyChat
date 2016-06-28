package com.alienleeh.familychat.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class ActivitiesFinisher {
    private static List<Activity> activities = new ArrayList<Activity>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity : activities){
            activity.finish();
        }
    }
}
