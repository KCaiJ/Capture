package com.example.capture.Util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;
import android.util.Log;

import com.example.capture.App.App;
import com.example.capture.Bean.IPPacket;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.Bean.TrafficInfo;
import com.example.capture.Bean.PerSettingInfo;
import com.example.capture.SQL.SQL;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取手机应用包 及权限记录包工具类
 */
public class GetListUtil {
    private static List<PerSettingInfo> packetInfoPerSettings=new ArrayList<>();
    private static  List<TrafficInfo> packetTrafficInfos =new ArrayList<>();
    private static ArrayList<IPPacket> ipPackets=new ArrayList<>();
    private static  List<PerSettingInfo> filterPacketInfo=new ArrayList<>();

    /**
     * 捕获的数据包列表
     * @return
     */
    public static ArrayList<IPPacket> getIpPackets() {
        return ipPackets;
    }

    public static void addIpPackets(IPPacket ipPackets) {
        GetListUtil.ipPackets .add(ipPackets);
    }

    public static void clearIpPackets() {
        GetListUtil.ipPackets .clear();
    }

    /**
     * 筛选后的列表
     * @param filterPacketInfo
     */
    public static void setFilterPacketInfo(List<PerSettingInfo> filterPacketInfo) {
        GetListUtil.filterPacketInfo = filterPacketInfo;
    }
    public static List<PerSettingInfo> getFilterPacketInfo() {
        return filterPacketInfo;
    }

    /**
     * 返回 TrafficInfo的List列表
     * @return
     */
    public static List<TrafficInfo> getPacketTrafficInfos() {
        return packetTrafficInfos;
    }

    /**
     * 返回PacketInfoPerSetting的List
     * @return
     */
    public static List<PerSettingInfo> getPacketInfoPerSettings() {
        return packetInfoPerSettings;
    }

    /**
     * 设置应用流量List
     * @param context
     */
    public static void setPacketTrafficInfos(Context context) {
        packetTrafficInfos.clear();
        List<ResolveInfo> packs= PacketUtils.getPackets(context);
        for (ResolveInfo info:packs){
            if(info.activityInfo.packageName.equals(context.getPackageName())){
                continue;
            }
            int uid=PacketUtils.getUidByPackageName(context,info.activityInfo.packageName);
            if(uid==-1){
                continue;
            }
            long UidRXPre=0;
            long UidTXPre=0;
            String packageName=(String) info.loadLabel(context.getPackageManager());
            SQL dbHelper = App.getSQLDB();
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            Cursor cursor = db.query ("traffic",null,"name='"+packageName+"'",null,null,null,null);
            if (cursor.moveToNext()){
                float Rx= cursor.getFloat(cursor.getColumnIndex("Rx"));
                String RxCompany= cursor.getString(cursor.getColumnIndex("RxCompany"));
                UidRXPre=EncodUtils.Transformation(RxCompany,Rx);
                float Tx= cursor.getFloat(cursor.getColumnIndex("Tx"));
                String TxCompany= cursor.getString(cursor.getColumnIndex("TxCompany"));
                UidTXPre=EncodUtils.Transformation(TxCompany,Tx);
            }
            db.close();
            long rx = TrafficStats.getUidRxBytes(uid)+UidRXPre;
            long tx = TrafficStats.getUidTxBytes(uid)+UidTXPre;

            long totalRxPre=0;
            totalRxPre=EncodUtils.Transformation(App.getCompanyRx(),App.getTotalRx());

            long totalTxPre=0;
            totalTxPre=EncodUtils.Transformation(App.getCompanyTx(),App.getTotalTx());

            long totalrx= TrafficStats.getTotalRxBytes()+totalRxPre;
            long totaltx= TrafficStats.getTotalTxBytes()+totalTxPre;

            float rxf=  (Float.valueOf(rx)/totalrx*100);
            float txf=  (Float.valueOf(tx)/totaltx*100);
            TrafficInfo packetTrafficInfo =new TrafficInfo();
            packetTrafficInfo.setInco(info.loadIcon(context.getPackageManager()));
            packetTrafficInfo.setAppNmae((String) info.loadLabel(context.getPackageManager()));
            packetTrafficInfo.setRx(rx);
            packetTrafficInfo.setTx(tx);
            packetTrafficInfo.setUid(uid);
            packetTrafficInfo.setRxf(rxf);
            packetTrafficInfo.setTxf(txf);
            packetTrafficInfo.setTotalrx(totalrx);
            packetTrafficInfo.setTotaltx(totaltx);
            packetTrafficInfo.setTotalFlow(rx+tx);
            packetTrafficInfo.setTotalFlowf(Float.valueOf((rx+tx))/(totalrx+totaltx)*100);
            packetTrafficInfos.add(packetTrafficInfo);
        }
    }
    /**
     * 设置包权限List
     * @param context
     */
    public static   void setPacketInfoPerSettings(Context context) {
        packetInfoPerSettings.clear();
        List<ResolveInfo> packs= PacketUtils.getPackets(context);
        for (ResolveInfo info:packs){
            if(info.activityInfo.packageName.equals(context.getPackageName())||PacketUtils.getUidByPackageName(context,info.activityInfo.packageName)==-1){
                continue;
            }
            PerSettingInfo packetInfoPerSetting=new PerSettingInfo();
            packetInfoPerSetting.setAppName((String) info.loadLabel(context.getPackageManager()));
            packetInfoPerSetting.setIcon(info.loadIcon(context.getPackageManager()));
            packetInfoPerSetting.setPacketName(info.activityInfo.packageName);

            int per=1;
            SQL sql=App.getPerDb();
            SQLiteDatabase db=sql.getReadableDatabase();
            String appName= (String) info.loadLabel(context.getPackageManager());
            Cursor cursor = db.query ("perSetting",null,"name='"+appName+"'",null,null,null,null);
            if (cursor.moveToNext()){
                per= cursor.getInt(cursor.getColumnIndex("per"));
            }
            ContentValues values = new ContentValues();
            values.put("name", appName);
            values.put("per", per);
            db.replace("perSetting",null,values);
            db.close();
            packetInfoPerSetting.setPermission(per);
            packetInfoPerSettings.add(packetInfoPerSetting);
        }
    }
}
