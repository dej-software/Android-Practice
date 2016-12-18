package com.jikexueyuan.locationshareserver;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by dej on 2016/11/6.
 */
public class Server {
    public static void main(String[] args) {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("LineCodec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        acceptor.setHandler(new LocationShareHandler());

        try {
            acceptor.bind(new InetSocketAddress(20000));
            System.out.println("Server started at port 20000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
