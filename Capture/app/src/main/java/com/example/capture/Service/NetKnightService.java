package com.example.capture.Service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.example.capture.App.App;
import com.example.capture.Bean.BagBean;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.Bean.PerSettingInfo;
import com.example.capture.NetThread.NetUDP;
import com.example.capture.R;
import com.example.capture.Util.BitUtils;
import com.example.capture.Util.GetListUtil;
import com.example.capture.NetThread.ByteBufferPool;
import com.example.capture.NetThread.NetInput;
import com.example.capture.NetThread.NetNotifyThread;
import com.example.capture.NetThread.NetOutput;
import com.example.capture.NetThread.Packet;
import com.example.capture.NetThread.TCB;
import com.example.capture.NetThread.TCBCachePool;
import com.example.capture.Util.RecordUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Vpnservice 核心类
 */
public class NetKnightService extends VpnService implements Runnable {
    //VPN转发的IP地址咯
    public static String  VPN_ADDRESS = "10.1.10.1";
    //从虚拟网卡拿到的文件描述符
    private ParcelFileDescriptor mInterface;
    //来自应用的请求的数据包
    private LinkedBlockingQueue<Packet> mInputQueue;
    //即将发送至应用的数据包
    private LinkedBlockingQueue<ByteBuffer> mOutputQueue;
    //缓存的appInfo队列,请求被拦截的队列
    private LinkedBlockingQueue<PacketInfo> mCacheAppInfo;
    //UDP数据队列
    private LinkedBlockingQueue<Packet>  mUDPQueue;
    //网络访问通知线程
    private NetNotifyThread mNetNotify;
    //网络输入输出
    private NetInput mNetInput;
    private NetOutput mNetOutput;
    private NetUDP mNetUdp;
    private Selector mChannelSelector;
    public static volatile boolean isRunning = false;
    public  static  Boolean isOne=false;
    @Override
    public void onCreate() {
        super.onCreate();
        App.setNetKnightService(this);
    }

    //建立VPN，拦截单一的程序应用包
    private void setupVpnOne(String appName){
        //获取应用信息,并设置相应的包
        List<PerSettingInfo> appList = GetListUtil.getPacketInfoPerSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Builder builder = new Builder();
            //只拦截需要拦截的应用
            for (int i = 0; i < appList.size(); i++) {
                PerSettingInfo packetInfoPerSetting = appList.get(i);
                if (appName.equals(packetInfoPerSetting.getAppName())){
                    Log.d("NetKnightService","拦截应用:"+packetInfoPerSetting.getAppName());
                    try {
                        builder = builder.addAllowedApplication(packetInfoPerSetting.getPacketName());
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            mInterface = builder.setSession("NetKnightService").setBlocking(false).addAddress(VPN_ADDRESS,32).addRoute("0.0.0.0",0).establish();
        }else{
            Toast.makeText(NetKnightService.this, R.string.SDK_no_VPN, Toast.LENGTH_SHORT).show();
        }
    }




    //建立vpn,拦截全部的程序应用包
    private void setupVpn(){
        //获取应用信息,并设置相应的包
        List<PerSettingInfo> appList = GetListUtil.getPacketInfoPerSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Builder builder = new Builder();
            //只拦截需要拦截的应用
            for (int i = 0; i < appList.size(); i++) {
                    PerSettingInfo packetInfoPerSetting = appList.get(i);
                    Log.d("NetKnightService","拦截应用:"+packetInfoPerSetting.getAppName());
                    try {
                        builder = builder.addAllowedApplication(packetInfoPerSetting.getPacketName());
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
            }
            mInterface = builder.setSession("NetKnightService").setBlocking(false).addAddress(VPN_ADDRESS,32).addRoute("0.0.0.0",0).establish();
        }else{
            Toast.makeText(NetKnightService.this, R.string.SDK_no_VPN, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        isOne = bundle.getBoolean("isOne");
        if (isOne){
            String appName=bundle.getString("OneAppName");
            setupVpnOne(appName);
        }else{
            setupVpn();
        }
        try {
            mChannelSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCacheAppInfo = new LinkedBlockingQueue<>();
        mNetNotify = new NetNotifyThread(this,mCacheAppInfo);
        mInputQueue = new LinkedBlockingQueue<>();
        mOutputQueue = new LinkedBlockingQueue<>();
        mUDPQueue=new LinkedBlockingQueue<>();
        mNetInput = new NetInput(mOutputQueue, mChannelSelector);
        mNetOutput = new NetOutput(mInputQueue, mOutputQueue, this, mChannelSelector,mCacheAppInfo);
        mNetUdp=new NetUDP(mUDPQueue,mOutputQueue,mCacheAppInfo);
        mNetNotify.start();
        mNetOutput.start();
        mNetInput.start();
        mNetUdp.start();
        new Thread(this).start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void run() {
        isRunning = true;
        FileChannel vpnInput = new FileInputStream(mInterface.getFileDescriptor()).getChannel();
        FileChannel vpnOutput = new FileOutputStream(mInterface.getFileDescriptor()).getChannel();
        ByteBuffer bufferNet2;
        ByteBuffer bufferNet = null;
        boolean isDataSend = true;
        try {
            while (true) {
                //标识位退出循环
                if(!isRunning){
                    onDestroy();
                    break;
                }
                //若数据包发送出去，则获取的Buffer，存储下个获取的数据包
                if(isDataSend ) {
                    bufferNet = ByteBufferPool.acquire();
                }else {
                    //未有数据包发送,则清空
                    bufferNet.clear();
                }
                int inputSize = vpnInput.read(bufferNet);
                if (inputSize > 0) {
                    Log.e("VPNservice","-----readData:-------size:" + inputSize);
                    //flip切换状态,由写状态转换成可读状态
                    bufferNet.flip();
                    //从应用中发送的包
                    Packet packetnet = new Packet(bufferNet);
                    int pos=bufferNet.position();
                    int lim=bufferNet.limit();
                    RecordUtils.Record(bufferNet, BagBean.CAPTURE_SENT);
                    bufferNet.position(pos);
                    bufferNet.limit(lim);
                    if (packetnet.isTCP()) {
                        mInputQueue.offer(packetnet);
                        isDataSend = true;
                    }else{
                        if (packetnet.isUDP()){
                            Log.w("NetUDP", packetnet.toString() );
                            mUDPQueue.offer(packetnet);
                            isDataSend = false;
                        }else{
                            Log.w("VPNservice","暂时不支持其他类型数据,目前仅支持TCP");
                            isDataSend = false;
                        }
                    }
                }else{
                    isDataSend = false;
                }
                //将数据返回到应用中
                bufferNet2 = mOutputQueue.poll();
                if (bufferNet2 != null) {
                    //将limit=position position = 0 开始读操作
                    bufferNet2.flip();
                    while (bufferNet2.hasRemaining()) {
                        vpnOutput.write(bufferNet2);
                    }
                    RecordUtils.Record(bufferNet2, BagBean.CAPTURE_RECEIVED);
                    ByteBufferPool.release(bufferNet2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                vpnInput.close();
                vpnOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭相关资源
     */
    public  void close() {
        isRunning = false;
        mNetInput.quit();
        mNetOutput.quit();
        mNetNotify.quit();
        mNetUdp.quit();
        try {
            mChannelSelector.close();
            mInterface.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TCBCachePool.closeAll();
        mInputQueue = null;
        mOutputQueue = null;
        mCacheAppInfo = null;
        ByteBufferPool.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }
}
