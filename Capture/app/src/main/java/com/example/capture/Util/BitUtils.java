package com.example.capture.Util;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.capture.App.App;
import com.example.capture.Bean.IPPacket;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.NetThread.Packet;
import com.example.capture.SQL.SQL;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * 数据包字节转换工具类
 */

public class BitUtils {
    //将高的8位置为0，低8位保持原样
    public static short getUnsignedByte(byte value) {
            return (short) (value & 0xFF);
        }

    //将高的16位置为0，低16位保持原样
    public static int getUnsignedShort(short value) {
            return value & 0xFFFF;
        }

    //将高的32位置为0，低32位保持原样 L表示long类型
    public static long getUnsignedInt(int value) {
            return value & 0xFFFFFFFFL;
        }

    /**
     * 时间格式化
     * @return
     */
    public  static  String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        return  formatStr;
    }




}
