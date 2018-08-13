package com.example.capture.NetThread;

import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * 监听来自网络的数据,并将相关数据解析丢回给虚拟网卡去处理
 */
public class NetInput  extends   Thread{
    private final static String TAG = "NetInput";
    //线程退出标识位
    private volatile boolean mQuit = false;
    private BlockingQueue<ByteBuffer> mOutputQueue;
    private Selector mChannelSelector ;
    /**
     * 标识退出,即使调用interrupt不一定会退出,双重保证
     */
    public void quit(){
        mQuit = true;
        interrupt();
    }

    public NetInput(BlockingQueue<ByteBuffer> queue,Selector channelSelector){
        mOutputQueue = queue;
        mChannelSelector = channelSelector;

    }


    /**
     * 需要获取实际网络数据,并把相关的数据写入outputQueue
     */
    @Override
    public void run() {

        Log.d(TAG,"NetInput start");
        while(true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.d(TAG,"Stop");
                if(mQuit)
                    return;
                continue;
            }
            try {
                int readyChannels = mChannelSelector.select();
                if(readyChannels ==0){
                    continue;
                }
                Set selectionKeys = mChannelSelector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while(keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    //将ipAndPort从队列中取出来,若不存在,说明该调度已经结束
                    String ipAndPort = (String) key.attachment();
                    TCB tcb = TCBCachePool.getTCB(ipAndPort);
                    if(tcb == null){
                        //通道关闭
                        Log.d(TAG,"channel is closed:"+ipAndPort);
                        key.channel().close();
                        keyIterator.remove();
                        continue;
                    }
                    if(((SocketChannel)key.channel()).finishConnect()){
                        if(key.isConnectable()){
                            Log.d(TAG,"channel is connectable");
                            buildConnection( tcb, key);
                        }else if(key.isAcceptable()){
                            Log.d(TAG,"channel is acceptable");

                        }else if(key.isReadable()){
                            Log.d(TAG,"channel is readable");
                            transData( tcb , key);
                        }else if(key.isWritable()){
                            Log.d(TAG,"channel is writable");
                        }
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 传输数据,写回实际返回数据到虚拟网卡
     * @param tcb
     * @param key
     */
    private void transData(TCB tcb, SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer responseBuffer = ByteBufferPool.acquire();

        int readBytes =0 ;
        //这样实际数据就能写道里面
        responseBuffer.position(Packet.IP4_HEADER_SIZE + Packet.TCP_HEADER_SIZE);
        try {
           readBytes = channel.read(responseBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            ByteBufferPool.release(responseBuffer);
//            throw new RuntimeException(e);
        }
        synchronized (tcb) {
            Packet responsePacket = tcb.referencePacket;
            Log.d(TAG, "获取回来的数据大小为" + readBytes);
            if (readBytes == -1) {
                key.interestOps(0);//
                tcb.tcbStatus = TCB.TCB_STATUS_LAST_ACK;
                responsePacket.updateTCPBuffer(responseBuffer, (byte) (Packet.TCPHeader.FIN | Packet.TCPHeader.ACK), tcb.mySequenceNum, tcb.myAcknowledgementNum, 0);
                tcb.mySequenceNum++;
                mOutputQueue.offer(responseBuffer);
                Log.d(TAG, "数据读取完毕");
                return;
            }
            responsePacket.updateTCPBuffer(responseBuffer, (byte) (Packet.TCPHeader.ACK | Packet.TCPHeader.PSH), tcb.mySequenceNum, tcb.myAcknowledgementNum, readBytes);
            tcb.mySequenceNum = tcb.mySequenceNum + readBytes;
            responseBuffer.position(Packet.IP4_HEADER_SIZE + Packet.TCP_HEADER_SIZE + readBytes);
        }
        mOutputQueue.offer(responseBuffer);

    }

    /**
     * 非阻塞状态时,实际的channel建立不是立马完成的
     * 虚拟网卡的握手必须等实际channel建立完成
     * @param tcb
     * @param key
     */
    private void buildConnection(TCB tcb,SelectionKey key){
        try {
            if(!((SocketChannel)key.channel()).finishConnect())
            {
                Log.d(TAG,"onConnectState 未建立完成");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读数据
        key.interestOps(SelectionKey.OP_READ);
        Packet responsePacket = tcb.referencePacket;
        ByteBuffer responseBuffer = ByteBufferPool.acquire();
        responsePacket.updateTCPBuffer(responseBuffer, (byte) (Packet.TCPHeader.SYN|Packet.TCPHeader.ACK),tcb.mySequenceNum,tcb.myAcknowledgementNum,0);
        tcb.tcbStatus  = TCB.TCB_STATUS_SYN_RECEIVED;
        tcb.mySequenceNum++;
        mOutputQueue.offer(responseBuffer);
    }
}
