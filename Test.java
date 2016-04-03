package com.wangwenjun.socks.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.*;

/**
 * Created by wangwenjun on 2016/4/3.
 */
public class Test
{
    public static void main(String[] args) throws IOException {
        /*URL url = new URL("http://www.baidu.com");
        // 创建代理服务器
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1",
                1999);
         Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
        // 如果我们知道代理server的名字, 可以直接使用
        // 结束
        URLConnection conn = url.openConnection(proxy);
        InputStream in = conn.getInputStream();
        System.out.println(in);
        // InputStream in = url.openStream();
        //String s = new StringReader()
//        System.out.println(s);*/

        SocketAddress addr = new InetSocketAddress("127.0.0.1",1999);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
        Socket socket = new Socket(proxy);
//        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("www.baidu.com", 80));
    }
}
