package com.study.demo.util;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.security.MessageDigest;

public class HashUtils {
    /**
     * 字节数据转换成16进制字符串
     * @param data
     * @return
     * @throws Exception
     */
    public static String bytes2String(byte[] data) throws Exception {
        if (data == null) {
            throw new IllegalAccessException("data must not null");
        }
        if (data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte datum : data) {
            String s = Integer.toHexString(Integer.parseInt((datum & 0xff)+""));
            if(s.length() == 1){
                s = "0"+s;
            }
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * 将16进制字符串转成BYTE数组
     * @param source
     * @return
     * @throws IllegalAccessException
     */
    public static byte[] hexString2Bytes(String source) throws IllegalAccessException {
        if(source == null){
            throw new IllegalAccessException("source must not null");
        }
        if(source.length()==0){
            return new byte[0];
        }
        String copySource = source;
       if(source.length() %2 != 0) {
           copySource = "0"+source;
       }
       byte[] bs = new byte[copySource.length()/2];
       for(int i=0,j=0; i<copySource.length();i=i+2,j++ ){
           String dest = copySource.substring(i,i+2);
           byte b = (byte)Integer.parseInt(dest, 16);
           bs[j]= b;
       }
        return bs;
    }


    public static void main(String[] args) throws Exception {


        System.out.println(md5String(""));

    }


    /**
     * 对字符串进行md5处理
     * @param source
     * @return
     * @throws Exception
     */
    public static String md5String(String source) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] digest = md5.digest(source.getBytes());
        return bytes2String(digest);
    }

    /**
     * 对字符串进行sha1处理
     * @param source
     * @return
     * @throws Exception
     */
    public static String sha1String(String source) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("sha");
        byte[] digest = md5.digest(source.getBytes());
        return bytes2String(digest);
    }

    /**
     * 对字符串进行sha256处理
     * @param source
     * @return
     * @throws Exception
     */
    public static String sha256String(String source) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("sha-256");
        byte[] digest = md5.digest(source.getBytes());
        return bytes2String(digest);
    }

    /**
     * 对字符串进行sha512处理
     * @param source
     * @return
     * @throws Exception
     */
    public static String sha512String(String source) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("sha-512");
        byte[] digest = md5.digest(source.getBytes());
        return bytes2String(digest);
    }

    /**
     * 对字符串进行repimd160处理
     * @param source
     * @return
     * @throws Exception
     */
    public static String repimd160String(String source) throws Exception{
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(source.getBytes(),0,source.getBytes().length);
        byte[] bs = new byte[ripemd160Digest.getDigestSize()];
        int i = ripemd160Digest.doFinal(bs, 0);
        return bytes2String(bs);

    }

}
