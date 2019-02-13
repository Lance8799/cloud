package org.lance.cloud.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class NetUtils {

    private static Random random = new Random();

    /**
     * 指定范围获取可用的端口
     * @param from
     * @param to
     * @return
     */
    public static int getAvailablePort(int from, int to){
        int port;
        do {
            port = random.nextInt(to - from) + from;
        } while (isPortUsing(port));
        return port;
    }

    /**
     * 判断端口是否占用
     * @param host
     * @param port
     * @return
     */
    public static boolean isPortUsing(String host, int port){
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            Socket socket = new Socket(inetAddress, port);
            socket.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * 判断本机端口是否占用
     * @param port
     * @return
     */
    public static boolean isPortUsing(int port){
        return isPortUsing("127.0.0.1", port);
    }
}
