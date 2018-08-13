package com.example.capture.Util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

/**
 * 解压工具类
 */
public class DecompressUtils {
    /**
     * GZIP解压缩
     *
     * @param bytes
     * @return
     */
    public static String unGZip(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }



    /**
     *
     * @param inputByte 待解压缩的字节数组
     * @return 解压缩后的字节数组
     * @throws IOException
     */
    public static String unDEF(byte[] inputByte) throws IOException, DataFormatException {
        int len = 0;
        Inflater infl = new Inflater();
        infl.setInput(inputByte);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] outByte = new byte[1024];
            while (!infl.finished()) {
                // 解压缩并将解压缩后的内容输出到字节输出流bos中
                len = infl.inflate(outByte);
                if (len == 0) {
                    break;
                }
                bos.write(outByte, 0, len);
            }
            infl.end();
            bos.close();
        return bos.toString();
    }


}
