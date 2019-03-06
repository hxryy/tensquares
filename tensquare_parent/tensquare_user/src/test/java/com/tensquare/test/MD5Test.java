package com.tensquare.test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * MD5加密及问题
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class MD5Test {

    /**
     * BASE64编码：
     *   它的本质就是三变四。
     *   三字节变成四字节。
     *   一个字节占：8位
     *
     *   编码前： s12
     *      每个字符都一个ascii编码。
     *          s:115
     *          1：49
     *          2：50
     *      把找到的数字都转成二进制
     *          115：1110011
     *          49： 110001
     *          50： 110010
     *      不够8位，通过补0补足位数
     *          01110011 00110001 00110010
     *      三变四，每6位一截取
     *          00011100  00110011 00000100 00110010
     *      把他们转成十进制
     *          00011100 :28
     *          00110011 :51
     *          00000100 :4
     *          00110010 :50
     *       拿着转成的十进制数字去base64的码表中对应
     *   编码后： czEy
     *
     * @param args
     */
    public static void main(String[] args)throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        System.out.println(encoder.encode("s12".getBytes()));

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] by = decoder.decodeBuffer("czEy");
        System.out.println(new String(by,0,by.length));
    }















    /**
     * MD5加密
     *   它的加密结果是固定，执行多少次都是一样的。
     *   它生成的加密结果是一个字节数组，在转成字符串时，是由任意字符组成的。
     *   我们需要把它转成有可见字符组成的字符串
     * 可见字符：
     *   指的就是我们可以通过键盘直接输入的字符。
     * 转换的方式:
     *   BASE64编码
     *   BASE64Encoder  编码
     *   BASE64Decoder  解码
     *   由64个基础字符组成，以及没有对象上的一个特殊字符
     *   基础字符：0~9 A~Z a~z + /
     *   特殊字符：=
     * 编码和加密的区别
     *   能编码，就能解码
     *   加密是单向不可逆的
     * @param args
     * @throws Exception

    public static void main(String[] args) throws Exception{
        String password = "子小子";
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] by = md5.digest(password.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        System.out.println(encoder.encode(by));
    }*/
}
