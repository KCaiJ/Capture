package com.example.capture.NetThread;

import android.util.Log;

import com.example.capture.Bean.PacketInfo;
import com.example.capture.Util.BitUtils;
import com.example.capture.Util.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;

/**
 * UDP数据包转发线程类,采用DatagramSocket  暂未实现，UDP数据包的服务器无响应，改为自己租的服务器则有响应  不知是否是服务器问题所致还是数据包解析错误？
 */
public class NetUDP extends   Thread {
    private final static String TAG = "NetUDP";
    private static final int DATA_LEN = 4096;
    //线程退出标识位
    private volatile boolean mQuit = false;
    private BlockingQueue<Packet> mUdpQueue;
    private BlockingQueue<ByteBuffer> mOutputQueue;
    private BlockingQueue<PacketInfo> mAppCacheQueue;
    /**
     * 标识退出,即使调用interrupt不一定会退出,双重保证
     */
    public void quit(){
        mQuit = true;
        interrupt();
    }

    public  NetUDP(BlockingQueue<Packet> queue,BlockingQueue<ByteBuffer> Outqueue,BlockingQueue<PacketInfo> appCacheQueue){
        mUdpQueue = queue;
        mOutputQueue=Outqueue;
        mAppCacheQueue=appCacheQueue;
    }


    @Override
    public void run() {
        Log.e(TAG, "NetUDP stats");
        Packet currentPacket;
        while (true){
            try {
                //阻塞等到有数据就处理
                currentPacket = mUdpQueue.poll();
                Thread.sleep(15);
                if (currentPacket == null) {
                    continue;
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Stop");
                if (mQuit)
                    return;
                continue;
            }

            DatagramSocket socket = null;
            try {
                //得到数据包的信息
                ByteBuffer payloadBuffer = currentPacket.backingBuffer;
                currentPacket.backingBuffer = null;

               long passAppId = FileUtils.filterPacket(currentPacket,mAppCacheQueue);
                if (passAppId == -1) {
                    continue;
                }
                ByteBuffer responseBuffer = ByteBufferPool.acquire();
                InetAddress desAddress = currentPacket.ip4Header.destinationAddress;
                int sourcePort = currentPacket.udpHeader.sourcePort;
                int desPort = currentPacket.udpHeader.destinationPort;

                int length=payloadBuffer.limit()-payloadBuffer.position();
                byte[]  bytes=new byte[length];
                payloadBuffer.get(bytes);

                socket = new DatagramSocket();
                socket.setSoTimeout(8000);
                DatagramPacket sendPacket   = new DatagramPacket(new byte[0] , 0 ,desAddress , desPort);//要发送的数据报文
                sendPacket.setData(bytes);
                socket.send(sendPacket);          // 发送信息

                DatagramPacket receivePacket = new DatagramPacket(new byte[length+20], length+20);//接收数据的报文
                socket.receive(receivePacket); // 接收信息  ,阻塞到服务器返回数据，直到超时
                Log.e(TAG, "run: receivePacket" );
                currentPacket.swapSourceAndDestination();
                currentPacket.updateUDPBuffer(responseBuffer,receivePacket.getData().length);
                responseBuffer.position(Packet.IP4_HEADER_SIZE + Packet.UDP_HEADER_SIZE );
                responseBuffer.put(receivePacket.getData());
                responseBuffer.position(Packet.IP4_HEADER_SIZE + Packet.UDP_HEADER_SIZE +receivePacket.getData().length);
                Log.d(TAG,"responseBuffer:Before Limit:"+responseBuffer.limit()+" position:"+responseBuffer.position());
                mOutputQueue.offer(responseBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (socket!=null)
                     socket.close();
            }
        }
    }
}
