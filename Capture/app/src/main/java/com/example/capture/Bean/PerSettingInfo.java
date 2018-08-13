package com.example.capture.Bean;


import android.graphics.drawable.Drawable;

/**
 * 权限设置Bean
 */
public class PerSettingInfo {
    private Drawable icon; //图标
    private String AppName; //应用名
    private String packetName;//包名
    private int permission;//权限等级

    public Drawable getIcon() {
        return icon;
    }

    public int getPermission() {
        return permission;
    }

    public String getAppName() {
        return AppName;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
