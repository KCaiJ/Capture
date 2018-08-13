package com.example.capture.App;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.capture.SQL.SQL;
import com.example.capture.Service.NetKnightService;
import com.example.capture.Util.GetListUtil;

/**
 * 应用App类
 */
public class App extends  Application {
    private static SQL dbHelper,dbHelper_per;
    private static SharedPreferences sp;
    private static Context context;
    private static Handler mHandler;
    private static NetKnightService netKnightService;
    @Override
    public void onCreate() {
        super.onCreate();
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        createSQL(getApplicationContext());
        GetListUtil.setPacketInfoPerSettings(getApplicationContext());
        GetListUtil.setPacketTrafficInfos(getApplicationContext());
    }

    public static NetKnightService getNetKnightService() {
        return netKnightService;
    }

    public static void setNetKnightService(NetKnightService netKnightService) {
        App.netKnightService = netKnightService;
    }

    private static void  createSQL(Context context){
        dbHelper = new SQL(context,"traffic",null,1);
        dbHelper_per = new SQL(context,"traffic",null,1);
    }

    public static void setContext(Context context) {
        App.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static  SQL getSQLDB(){
        return dbHelper;
    }

    public static  SQL getPerDb(){
        return dbHelper_per;
    }

    public static void setCompanyTx(String companyTx) {
        sp.edit().putString("CompanyTx",companyTx).commit();
    }

    public static void setCompanyRx(String companyRx) {
        sp.edit().putString("CompanyRx",companyRx).commit();
    }

    public static void setTotalRx(float totalRx) {
        sp.edit().putFloat("TotalRx",totalRx).commit();
    }

    public static void setTotalTx(float totalTx) {
        sp.edit().putFloat("TotalTx",totalTx).commit();
    }

    public static float getTotalTx() {
        return sp.getFloat("TotalTx",0);
    }

    public static float getTotalRx() {
        return sp.getFloat("TotalRx",0);
    }

    public static String getCompanyRx() {
        return sp.getString("CompanyRx","B");
    }

    public static String getCompanyTx() {
        return sp.getString("CompanyTx","B");
    }

    public static void setmHandler(Handler mHandler) {
        App.mHandler = mHandler;
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static void setLanguage(boolean language){
        sp.edit().putBoolean("language",language).commit();
    }
    public static boolean getLanguage(){

        return sp.getBoolean("language",true);
    }
}
