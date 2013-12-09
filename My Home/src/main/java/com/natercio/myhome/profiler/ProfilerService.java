package com.natercio.myhome.profiler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.natercio.myhome.db.ORMMyHomeDBHelper;
import com.natercio.myhome.utils.MyHomeLog;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 8/5/13
 * Time: 11:04 PM
 */
public class ProfilerService extends Service implements Serializable {

    //private static final String LOG_FLAG = "Profiler";

    private Looper looper;
    private ServiceHandler serviceHandler;
    private IBinder binder;

    private final Object lock = new Object();

    ORMMyHomeDBHelper dbHelper = null;

    @Override
    public void onCreate() {
        MyHomeLog.LogInfo(getClass().getName(), "Service created");

        HandlerThread thread = new HandlerThread(getClass().getName(), Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        binder = new ServiceBinder();

        looper = thread.getLooper();
        serviceHandler = new ServiceHandler(looper);

        dbHelper = OpenHelperManager.getHelper(this.getApplication(), ORMMyHomeDBHelper.class);// new MyHomeDBHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }

        MyHomeLog.LogInfo(getClass().getName(), "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyHomeLog.LogInfo(getClass().getName(), "Client binded");

        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    public void doTask(ProfilerTask task) {
        Message msg = serviceHandler.obtainMessage();

        msg.what = ServiceHandler.MSG_PROFILER_TASK;
        msg.obj = task;

        serviceHandler.sendMessage(msg);
    }

    public void doTaskAndWait(ProfilerTask task) {
        Message msg = serviceHandler.obtainMessage();

        msg.what = ServiceHandler.MSG_PROFILER_TASK;
        msg.obj = task;

        serviceHandler.sendMessageAtFrontOfQueue(msg);

        while (!task.isFinished())
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////



    ///////////////////////////
    // PRIVATE INNER CLASSES //
    ///////////////////////////

    private final class ServiceHandler extends Handler {

        public static final int MSG_PROFILER_TASK = 0;

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PROFILER_TASK:
                    if (msg.obj instanceof ProfilerTask) {
                        ((ProfilerTask) msg.obj).execute();

                        synchronized(lock) {
                            lock.notify();
                        }
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }

            MyHomeLog.LogInfo(getClass().getName(), "Message handled");
        }
    }

    //////////////////////////
    // PUBLIC INNER CLASSES //
    //////////////////////////

    public final class ServiceBinder extends Binder {
        public ProfilerService getService() {
            return ProfilerService.this;
        }
    }
}
