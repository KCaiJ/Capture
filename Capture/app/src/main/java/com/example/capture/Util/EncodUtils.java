package com.example.capture.Util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 字节及单位转换工具类
 */
public class EncodUtils {
    /**
     * 将数值转换为long
     * @param company
     * @param num
     * @return
     */
    public static long Transformation(String company, float num){
        if (company.equals("GB"))
            return (long) (num*1024*1024*1024);
        else if (company.equals("MB"))
            return (long) (num*1024*1024);
        else if (company.equals("KB"))
            return (long) (num*1024);
        else if (company.equals("B"))
            return (long) (num);
        return 0;
    }

    /**
     * 转换流量的单位
     * @param num
     * @return
     */
    public  static Map getCompany(long num){
        Map values=new HashMap();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (num / 1024.0 / 1024.0 > 1024) {
            values.put("Num", df.format(num / 1024.0 / 1024.0 / 1024.0));
            values.put("Company", "GB");
        } else {
            if (num / 1024.0 > 1024) {
                values.put("Num", df.format(num / 1024.0 / 1024.0));
                values.put("Company", "MB");
            } else {
                if (num > 1024) {
                    values.put("Num", df.format(num / 1024.0));
                    values.put("Company", "KB");
                } else {
                    values.put("Num", df.format(num));
                    values.put("Company", "B");
                }
            }
        }
        return values;
    }


    /**
     * 16进制字符串转字符串
     */
    public static String hexStr2Str(String hexStr)  {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * byte数组转换为十六进制的字符串
     * **/
    public static String conver16HexStr(byte [] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 16进制转字节数组
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }




    /**
     * 十六进制转换为Integer
     * @param hexValue
     * @return
     */
    public static int hex2int(String hexValue){
        return Integer.valueOf(hexValue,16);
    }

}
