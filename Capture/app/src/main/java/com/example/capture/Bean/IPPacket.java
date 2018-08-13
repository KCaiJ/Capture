package com.example.capture.Bean;

import java.net.InetAddress;
import java.util.List;

/**
 * IP数据包Bean 存储数据包所属的应用名，图标 及具体信息
 */
public class IPPacket {
    private PacketInfo packetInfo; //应用信息
    private InetAddress toIPAdd;//目的IP
    private  int destinationPort;   //目的端口
    private String packetType; //协议类型
    private String date; //产生的具体时间
    private int Size; //来往的数据大小
    private List<BagBean> listBagBean; //具体的解析结果

    public PacketInfo getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(PacketInfo packetInfo) {
        this.packetInfo = packetInfo;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public InetAddress getToIPAdd() {
        return toIPAdd;
    }

    public int getSize() {
        return Size;
    }

    public String getDate() {
        return date;
    }

    public String getPacketType() {
        return packetType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    public void setSize(int size) {
        Size = size;
    }

    public void setToIPAdd(InetAddress toIPAdd) {
        this.toIPAdd = toIPAdd;
    }

    public List<BagBean> getListBagBean() {
        return listBagBean;
    }

    public void setListBagBean(List<BagBean> listBagBean) {
        this.listBagBean = listBagBean;
    }
}
