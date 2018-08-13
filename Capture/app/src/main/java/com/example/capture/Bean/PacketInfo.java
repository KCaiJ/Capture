package com.example.capture.Bean;

import android.graphics.drawable.Drawable;
import java.net.InetAddress;

/**
 * 应用包Bean
 */
public class PacketInfo {
    private int uid;  //uid
    private Drawable icon; //图标
    private String AppName;//应用名
    private String packetName;//包名

    public int getUid() {
        return uid;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getAppName() {
        return AppName;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }
}
