package com.example.capture.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;
import android.util.Log;

import com.example.capture.App.App;
import com.example.capture.Bean.TrafficInfo;
import com.example.capture.SQL.SQL;
import com.example.capture.Util.EncodUtils;
import com.example.capture.Util.GetListUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 系统关机广播接收器
 */
public class ShutdownReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            Log.e( "onReceive: ","shutdown" );
            //将数据写入数据库
            SQL dbHelper =App.getSQLDB();
            SQLiteDatabase db =dbHelper.getWritableDatabase();
           if (!insertBySql(db, GetListUtil.getPacketTrafficInfos())){
               insertBySql(db, GetListUtil.getPacketTrafficInfos());
           }
        }
    }

    /**
     * 具体的保存数据方法
     * @param db
     * @param list
     * @return
     */
    public   boolean insertBySql(SQLiteDatabase db, List<TrafficInfo> list) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            for (TrafficInfo info : list) {
                ContentValues values = new ContentValues();
                values.put("name", info.getAppNmae());
                long rx = info.getRx();
                long tx = info.getTx();

                Map vules= EncodUtils.getCompany((long) rx);
                values.put("Rx", String.valueOf(vules.get("Num")));
                values.put("RxCompany", (String) vules.get("Company"));

                vules= EncodUtils.getCompany((long) tx);
                values.put("Tx", String.valueOf(vules.get("Num")));
                values.put("TxCompany", (String) vules.get("Company"));

                db.replace("traffic",null,values);
            }
            long totalrx= TrafficStats.getTotalRxBytes();
            long totaltx= TrafficStats.getTotalTxBytes();

            Map total= EncodUtils.getCompany((long) totalrx);
            App.setTotalRx(Float.valueOf((String) total.get("Num")));
            App.setCompanyRx((String) total.get("Company"));

            total= EncodUtils.getCompany((long) totaltx);
            App.setTotalTx(Float.valueOf((String) total.get("Num")));
            App.setCompanyTx((String) total.get("Company"));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
