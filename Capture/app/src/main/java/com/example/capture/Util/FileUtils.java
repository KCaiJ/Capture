package com.example.capture.Util;

import android.util.Log;

import com.example.capture.App.App;
import com.example.capture.Bean.PacketInfo;
import com.example.capture.NetThread.Packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

/**
 * 文件查找工具类
 */
public class FileUtils {
    /**
     * 读/proc/net/tcp6的数据
     * 根据指定的port,映射找到对应的uid
     */
    public static int readProcFile(int packetPort) {
        File readFile =  new File("/proc/net/tcp6");
        if(!readFile.exists()){
            Log.d("NetUtils","文件不存在");
            return -1;
        }
        int uid = -1;
        try {
            FileInputStream inputStream = new FileInputStream(readFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while((line=bufferedReader.readLine())!=null){
                //    System.out.println(line);
                uid= dealLine(line,packetPort);
                if(uid!=-1){
                    break;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return uid;
        }
    }

    /**
     * 处理一行的数据
     * @param line
     */
    private static int  dealLine(String line,int reqPort){

        String[] strs = line.split(" ");

        int localAddrIndex = 0 ;
        int i ;
        for(i=0;i<strs.length;i++){
            if(!strs[i].equals("")){
                localAddrIndex = i+1;
                break;
            }
        }
        //去除0000 0000 0000 0000 FFFF 0000这段 获取IP地址
        String[] localAddr = strs[localAddrIndex].substring(24).split(":");
        String ip = EncodUtils.hex2int(localAddr[0].substring(6))+"."
                +EncodUtils.hex2int(localAddr[0].substring(4,6))+"."
                +EncodUtils.hex2int(localAddr[0].substring(2,4))+"."
                +EncodUtils.hex2int(localAddr[0].substring(0,2));

     /*   if(!ip.equals(VPN.VPN_ADDRESS)){
            return -1;
        }*/
        int port = EncodUtils.hex2int(localAddr[1]);
        if(port!=reqPort){
            return -1;
        }
        int uidIndex = localAddrIndex+6;
        //忽略空格
        if(strs[uidIndex].equals("")) {
            for (i = uidIndex; i < strs.length; i++) {
                if (!strs[i].equals("")) {
                    uidIndex = i;
                    break;
                }
            }
        }
        System.out.println("Ip:"+ip+ " Port:"+port+" Uid:"+strs[uidIndex]);
        return Integer.parseInt(strs[uidIndex]);
    }

    /**
     * 过滤相关的包,返回的为UId,如果为-1,则说明禁止访问
     */
    public static long filterPacket(Packet transPacket, BlockingQueue<PacketInfo> appCacheQueue) {
        int  uid=-1;
        if (transPacket.isTCP()){
            uid = readProcFile(transPacket.tcpHeader.sourcePort);
        }else{
            if (transPacket.isUDP()){
                uid = readProcFile(transPacket.udpHeader.sourcePort);
            }
        }
        //根据端口号查询UID
        if (uid!=-1) {
            PacketInfo packetinfo = PacketUtils.UidtoPacketInfo(App.getContext(), uid);
            if (packetinfo!=null){
                int per= PacketUtils.findAppPer(packetinfo.getAppName());
                switch (per) {
                    case -1:
                        return uid;
                    case 0:
                        return -1;
                    case 1:
                        appCacheQueue.offer(packetinfo);
                        return -1;
                    case 2:
                        return uid;
                }
            }
        };
        return uid;
    }

}
