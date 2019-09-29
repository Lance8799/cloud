package org.lance.cloud.fixture;

import org.lance.cloud.utils.NetUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * SpringBoot端口范围启动器
 *
 * @author Lance
 */
public class RangePortLauncher {

    private static int FROM = 10000;
    private static int TO = 50000;

    /**
     * 使用server.port参数设置端口，或者指定端口范围，生成并保存到系统配置
     *
     * @param from
     *  开始端口
     * @param to
     *  结束端口
     * @param args
     *  启动参数
     */
    public static void start(int from, int to, String[] args){
        String port = Arrays.stream(args)
                .filter(arg -> StringUtils.startsWithIgnoreCase(arg, "--server.port"))
                // 没有设置port参数时，使用指定端口
                .findFirst().orElseGet(() -> "server.port=" + NetUtils.getAvailablePort(from > 0 ? from : FROM, to > 0 ? to : TO))
                .split("=")[1];

        System.setProperty("range.port", port);
    }

    /**
     * 使用server.port参数设置端口，或者默认范围[10000,50000]的端口
     *
     * @param args
     *  启动参数
     */
    public static void start(String[] args) {
        start(-1, -1, args);
    }
}
