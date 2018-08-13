package com.example.capture.Bean;

/**
 * 数据包解析结果Bean
 */
public class BagBean {
    public static final int CAPTURE_SENT = 1;
    public static final int CAPTURE_RECEIVED =2;
    private String conver16HexStr;  //16进制字符串
    private String reslutStr;   //普通字符串
    private int  mode;  //数据包的来源
    private String date; //时间

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setConver16HexStr(String conver16HexStr) {
        this.conver16HexStr = conver16HexStr;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setReslutStr(String reslutStr) {
        this.reslutStr = reslutStr;
    }

    public int getMode() {
        return mode;
    }

    public String getConver16HexStr() {
        return conver16HexStr;
    }

    public String getReslutStr() {
        return reslutStr;
    }
}
