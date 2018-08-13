package com.example.capture.Bean;

import android.graphics.drawable.Drawable;

/*
**流量花费Bean
 */
public class TrafficInfo {
    private Drawable inco;//图标
    private String appNmae;//应用名
    private long rx;    //接收流量
    private long tx;    //发送流量
    private float rxf;  //接收流量百分比
    private float txf;  //发送流量百分比
    private int uid;    //uid
    private long totalrx;   //总接收流量
    private long totaltx;   //总发送流量
    private long totalFlow; //总流量
    private float totalFlowf;   //总流量百分比

    public float getTotalFlowf() {
        return totalFlowf;
    }

    public void setTotalFlowf(float totalFlowx) {
        this.totalFlowf = totalFlowx;
    }

    public void setTxf(float txf) {
        this.txf = txf;
    }

    public void setRxf(float rxf) {
        this.rxf = rxf;
    }

    public float getRxf() {
        return rxf;
    }

    public float getTxf() {
        return txf;
    }

    public long getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(long totalFlow) {
        this.totalFlow = totalFlow;
    }

    public long getTotalrx() {
        return totalrx;
    }

    public long getTotaltx() {
        return totaltx;
    }

    public void setTotalrx(long totalrx) {
        this.totalrx = totalrx;
    }

    public void setTotaltx(long totaltx) {
        this.totaltx = totaltx;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Drawable getInco() {
        return inco;
    }

    public String getAppNmae() {
        return appNmae;
    }

    public long getTx() {
        return tx;
    }

    public long getRx() {
        return rx;
    }

    public void setAppNmae(String appNmae) {
        this.appNmae = appNmae;
    }

    public void setInco(Drawable inco) {
        this.inco = inco;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

}
