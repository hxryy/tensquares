package com.tensquare.test;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class RuntimeTest {

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec("cmd /c start C:\\Users\\zhy\\Desktop\\a.bat");
    }
}
