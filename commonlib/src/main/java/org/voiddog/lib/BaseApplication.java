package org.voiddog.lib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础App
 * Created by qigengxin on 16/8/24.
 */
public class BaseApplication extends Application {

    // 保存 Activity 的启动队列
    private List<WeakReference<Activity> > mActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
         if(isMainProcess()){
             registerActivityLifecycleCallbacks(new ActivityLifecycleImpl());
         }
    }

    public boolean isMainProcess() {
        try {
            ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            String mainProcessName = getPackageName();
            int myPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * quit the app
     */
    public void exit() {
        int myPid = android.os.Process.myPid();
        Process.killProcess(myPid);
    }

    /**
     * The number of the launched activity.
     *
     * @return
     */
    public int getLaunchedActivityCount() {
        return mActivities.size();
    }


    /**
     * launched Activity list
     *
     * @return
     */
    public List<WeakReference<Activity>> getLaunchedActivityList() {
        return mActivities;
    }

    /**
     * Get the top Activity in app
     *
     * @return the top activity in app
     */
    public Activity getTopicActivity() {
        int size = mActivities.size();
        if (null != mActivities && size > 0) {
            return mActivities.get(size - 1).get();
        }
        return null;
    }

    class ActivityLifecycleImpl implements ActivityLifecycleCallbacks{

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivities.add(new WeakReference<>(activity));
        }

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivityStopped(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

        @Override
        public void onActivityDestroyed(Activity activity) {
            for(WeakReference<Activity> activityWeakReference : mActivities){
                if(activityWeakReference.get() == activity){
                    mActivities.remove(activityWeakReference);
                    break;
                }
            }
        }
    }
}
