package com.example.capture.NetThread;
import java.nio.channels.SelectionKey;

/**
 * 传输控制块,用来监视每条连接的,每条channel对应一条与应用的连接
 */
public class TCB
{
    public String ipAndPort;
    //客户端的顺序码,每次发送多少数据就加多少,普通的无负载的数据包算做是1byte
    public long mySequenceNum, theirSequenceNum;
    //客户端的ack码,为对方发来的seq码加上其发送的数据大小
    public long myAcknowledgementNum, theirAcknowledgementNum;
    //Tcp的状态信息
    public static final int TCB_STATUS_SYN_SENT = 1;
    public static final int TCB_STATUS_SYN_RECEIVED =2;
    public static final int TCB_STATUS_ESTABLISHED = 3;
    public static final int TCB_STATUS_CLOSE_WAIT = 4;
    public static final int TCB_STATUS_LAST_ACK = 5;
    public int tcbStatus = -1;
    //用来封装生成数据包
    public Packet referencePacket;
    public SelectionKey selectionKey;
    public TCB(String ipAndPort, long mySequenceNum, long theirSequenceNum, long myAcknowledgementNum, long theirAcknowledgementNum,Packet referencePacket)
    {
        this.ipAndPort = ipAndPort;
        this.mySequenceNum = mySequenceNum;
        this.theirSequenceNum = theirSequenceNum;
        this.myAcknowledgementNum = myAcknowledgementNum;
        this.theirAcknowledgementNum = theirAcknowledgementNum;
        this.referencePacket = referencePacket;
    }
}
