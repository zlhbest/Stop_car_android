package com.henshin.stop_car.Tools;

import java.security.MessageDigest;

/**
 * Created by henshin on 2018/2/12.
 */

public class BaseTools
{
    /**
     * md5加密
     * @param pwd
     * @return
     * @throws Exception
     */
    public static String getMd5Code(Object pwd)throws Exception
    {
        //获取md5第一次加密的密文
        String md5pwd1=BaseTools.MD5Encode(pwd.toString());
        //System.out.println("一次密文:"+md5pwd1);
        //编写混淆明文
        String pwd2=md5pwd1+"朱龙㈢㈩Ⅶ昊以させαωηιゅほ無つくぉ法為ㄍㄐㄛㄜνμβㄝㄆㄓㄚ有法朱かねむめ龍昊"+md5pwd1;
        //二次混淆加密
        String md5pwd2=BaseTools.MD5Encode(pwd2);
        //System.out.println("二次密文:"+md5pwd2);
        return md5pwd2;
    }


    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b)
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /**
     * 转换字节为16进制字符串
     * @param b byte
     * @return String
     */
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
        {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    /**
     * 得到MD5的秘文密码
     * @param origin String
     * @throws Exception
     * @return String
     */
    public static String MD5Encode(String origin) throws Exception
    {
        String resultString = null;
        try
        {
            resultString=new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString=byteArrayToHexString(md.digest(resultString.getBytes()));
            return resultString;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
    //******************END  MD5******************
}
