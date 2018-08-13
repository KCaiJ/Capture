package com.example.capture.NetThread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.capture.Activity.MainActivity;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.R;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 网络应用访问信息的线程
 */
public class NetNotifyThread extends  Thread{
    private final String TAG = "NetNotifyThread";
    private NotificationCompat.Builder mBuilder;
    private int notifyId = 1000;
    private Context mContext;
    //线程退出标识位
    private volatile boolean mQuit = false;
    private NotificationManager mNotificationManager;
    private LinkedBlockingQueue<PacketInfo> mAccessApp ;
    //已经通知过访问的hashmap,就不再进行通知
    //key为appId,value为通知的次数
    private HashMap<Integer,Integer> mHasNotifiedAppMap;

    /**
     * 标识位退出,即使调用interrupt不一定会退出,双重保证
     */
    public void quit(){
        mQuit = true;
        interrupt();
    }
    public NetNotifyThread(Context context, BlockingQueue<PacketInfo> accessApps){
        mAccessApp = (LinkedBlockingQueue<PacketInfo>) accessApps;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mContext = context;
        mHasNotifiedAppMap = new HashMap<>();
    }
    @Override
    public void run() {
        Log.d(TAG,"start");
        PacketInfo app;
        while (true){
            try {
              Thread.sleep(10);
              app =  mAccessApp.take();
            } catch (InterruptedException e) {
                Log.d(TAG, "Stop");
                if (mQuit)
                    return;
                continue;
            }
            if(app==null){
                continue;
            }

           if(mHasNotifiedAppMap.containsKey(app.getUid())){
                continue;
            }
            mHasNotifiedAppMap.put(app.getUid(),1);
            showNotification(app);
        }
    }

    public void showNotification(PacketInfo app){
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle( app.getAppName()+mContext.getString(R.string.NetworkInterception))
                .setContentText(mContext.getString(R.string.ApplicationNetworkingNotifications))
                .setTicker(app.getAppName()+ mContext.getString(R.string.AccessNetwork))// 通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.firewall); //设置图标
        Intent resultIntent = new Intent(mContext,MainActivity.class);   // 点击的意图ACTION是跳转到Intent
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

}
