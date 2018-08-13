package com.example.capture.Util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.capture.App.App;
import com.example.capture.Bean.IPPacket;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.SQL.SQL;

import java.util.List;

/**
 * 程序应用工具类
 */
public class PacketUtils {

    /**
     * 获取所有的手机应用包
     * @param context
     * @return
     */
    public static   List<ResolveInfo> getPackets(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return  packageManager.queryIntentActivities(mainIntent, 0);
    }


    /***
     * 根据UID查询应用名
     * @param uid ,context
     * @return
     */
    public static PacketInfo UidtoPacketInfo(Context context, int uid){
        List<ResolveInfo> packs=getPackets(context);
        for (ResolveInfo info:packs){
            int i= getUidByPackageName(context,info.activityInfo.packageName);
            if (uid==i){
                PacketInfo packetinfo=new PacketInfo();
                packetinfo.setUid(uid);
                packetinfo.setAppName((String) info.loadLabel(context.getPackageManager()));
                packetinfo.setIcon(info.loadIcon(context.getPackageManager()));
                packetinfo.setPacketName(info.activityInfo.packageName);
                return  packetinfo;
            }
        }
        return  null;
    }

    /**
     * 根据 packgeName获取UID值
     * @param context
     * @param packageName
     * @return 具有网络权限的UID
     */
    public static int getUidByPackageName(Context context, String packageName) {
        int uid = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            boolean has_permission = (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission("android.permission.INTERNET", packageName));
            if (has_permission) {
                uid = packageInfo.applicationInfo.uid;
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return uid;
    }

    /**+
     * 根据端口号查询应用信息
     * @param port
     * @retur
     */
    public static IPPacket getIPPacket(int port){
        int  uid = FileUtils.readProcFile(port);   //根据端口号查询UID
        if (uid!=-1) {
            PacketInfo  packetinfo = PacketUtils.UidtoPacketInfo(App.getContext(), uid);
            if (packetinfo!=null){
                IPPacket ipPacket=new IPPacket();
                ipPacket.setPacketInfo(packetinfo);
                return  ipPacket;
            }
        };
        return  null;
    }


    /**
     * 根据应用名查询对应权限
     * @param name
     * @return
     */
    public static int findAppPer(String name){
        SQL sql=App.getPerDb();
        SQLiteDatabase db=sql.getReadableDatabase();
        if (db!=null) {
            Cursor cursor = db.query("perSetting", null, "name='" + name + "'", null, null, null, null);
            if (cursor.moveToNext()) {
                return cursor.getInt(cursor.getColumnIndex("per"));
            }
        }
        return -1;
    }
}
