package com.example.capture.NetThread;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TCB缓存池,采用concurrenthashmap实现
 */
public class TCBCachePool  {
    private static ConcurrentHashMap<String,TCB> mPool;
    static{
        mPool = new ConcurrentHashMap<String,TCB>();
    }
    /**
     * 存储TCB
     * @param ipAndPort
     * @param tcb
     */
    public static void putTCB(String ipAndPort,TCB tcb){
        if(TextUtils.isEmpty(ipAndPort)||tcb ==null){
            return;
        }
        mPool.put(ipAndPort,tcb);
    }
    /**
     * 获取TCB
     * @param ipAndPort
     * @return
     */
    public static TCB getTCB(String ipAndPort){
        TCB tcb = mPool.get(ipAndPort);
        return tcb;
    }
    /**
     * 关闭TCB,并移除
     * @param ipAndPort
     */
    public static void closeTCB(String ipAndPort){
        mPool.remove(ipAndPort);
    }
    /**
     * 移除所有,包括相关的channel
     */
    public static void closeAll() {
        Iterator iter = mPool.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            TCB val = (TCB) entry.getValue();
            try {
                val.selectionKey.channel().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("TCBCachePool","TCB Cache全部清理完毕");
        mPool.clear();
    }
}
