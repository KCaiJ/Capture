package com.example.capture.Util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.capture.App.App;
import com.example.capture.Bean.BagBean;
import com.example.capture.Bean.IPPacket;
import com.example.capture.NetThread.Packet;
import com.example.capture.Service.NetKnightService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录发送的数据包内容工具类
 */
public class RecordUtils {
    private static String hear = null;
    private static String body = null;
    private static String perAppName = "";
    private static List<BagBean> bagBeanList = new ArrayList<>();
    private static int Size = 0;
    private static Handler myHandler = App.getmHandler();
    private static IPPacket preipPacket;

    /**
     * 数据包存储
     *
     * @param buffer
     * @param staus
     */
    public static void Record(ByteBuffer buffer, int staus) {
        try {
            if (buffer != null) {
                int sourceport = -1;
                int desPort = -1;
                String Type = null;
                InetAddress desAddress = null;
                if (buffer.limit() <= 0)
                    return;
                buffer.position(0);
                Packet packet = new Packet(buffer);
                if (packet.getPayloadSize() > 0) {
                    //   Log.i( "Record: ", packet.getPayloadSize()+"");
                    if (packet.isTCP()) {
                        if (staus == BagBean.CAPTURE_SENT) {
                            sourceport = packet.tcpHeader.sourcePort;
                            desPort = packet.tcpHeader.destinationPort;
                            desAddress = packet.ip4Header.destinationAddress;
                        } else {
                            sourceport = packet.tcpHeader.destinationPort;
                            desPort = packet.tcpHeader.sourcePort;
                            desAddress = packet.ip4Header.sourceAddress;
                        }
                        Type = "TCP";
                    } else {
                        if (packet.isUDP()) {
                            if (staus == BagBean.CAPTURE_SENT) {
                                sourceport = packet.udpHeader.sourcePort;
                                desPort = packet.udpHeader.destinationPort;
                                desAddress = packet.ip4Header.destinationAddress;
                            } else {
                                sourceport = packet.udpHeader.destinationPort;
                                desPort = packet.udpHeader.sourcePort;
                                desAddress = packet.ip4Header.sourceAddress;
                            }
                            Type = "UDP";
                        }
                    }
                    IPPacket ipPacket = PacketUtils.getIPPacket(sourceport);
                    if (ipPacket != null) {
                        //     Log.w( "Record: ", ipPacket.getPacketInfo().getAppName());
                        if ((!ipPacket.getPacketInfo().getAppName().equals(perAppName) && !perAppName.equals("")) || (NetKnightService.isOne && Size > 100)) {
                            if (bagBeanList.size()> 0) {
                                Log.e( "Record: ","ss" );
                                GetListUtil.addIpPackets(preipPacket);
                                Message message = new Message();
                                message.what = 1;
                                myHandler.sendMessage(message);
                            }
                            bagBeanList = new ArrayList<>();
                            Size = 0;
                        }
                        Size += packet.getPayloadSize();
                        BagBean bagBean = getBean(buffer);
                        if (bagBean != null) {
                            bagBean.setMode(staus);
                            bagBeanList.add(bagBean);
                        }
                        perAppName = ipPacket.getPacketInfo().getAppName();
                        preipPacket = ipPacket;
                        preipPacket.setDestinationPort(desPort);
                        preipPacket.setPacketType(Type);
                        preipPacket.setToIPAdd(desAddress);
                        preipPacket.setDate(BitUtils.getDate());
                        preipPacket.setListBagBean(bagBeanList);
                        preipPacket.setSize(Size);
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /***
     * 数据包解析
     *
     * @param buffer
     * @return
     */
    private static BagBean getBean(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit() - buffer.position()];
        buffer.get(bytes);
        String conver16HexStr = EncodUtils.conver16HexStr(bytes); //16进制字符串
        String reslut = EncodUtils.hexStr2Str(conver16HexStr);  //可视字符串
        Log.e("Record: ", conver16HexStr + "\n" + reslut.replace("\r", ""));
        if (hear != null && body != null) {
            conver16HexStr = body + conver16HexStr;
            try {
                reslut = hear + DecompressUtils.unGZip(EncodUtils.hexStringToByte(conver16HexStr));
                Log.e("Record", "gzip压缩" + reslut);
                hear = null;
                body = null;
            } catch (IOException e) {
                body = conver16HexStr;
                return null;
            }
        } else {
            int stapos = conver16HexStr.indexOf("1F8B08"); //Gzip压缩文件以1F8B08开头 defalur以789C开头
            if (stapos != -1) {
                try {
                    String reslut2 = DecompressUtils.unGZip(EncodUtils.hexStringToByte(conver16HexStr.substring(stapos, conver16HexStr.length())));
                    Log.w("Record", "gzip压缩" + reslut2);
                    reslut = EncodUtils.hexStr2Str(conver16HexStr.substring(0, stapos)) + reslut2;
                } catch (IOException e) {
                    hear = EncodUtils.hexStr2Str(conver16HexStr.substring(0, stapos));
                    body = conver16HexStr.substring(stapos, conver16HexStr.length());
                    return null;
                }
            }
        }
        BagBean bagBean = new BagBean();
        bagBean.setConver16HexStr(conver16HexStr);
        bagBean.setReslutStr(reslut);
        bagBean.setDate(BitUtils.getDate());
        hear = null;
        body = null;
        return bagBean;
    }
}
