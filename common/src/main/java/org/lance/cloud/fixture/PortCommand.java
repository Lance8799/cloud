package org.lance.cloud.fixture;

import org.lance.cloud.utils.NetUtils;
import org.springframework.util.StringUtils;

/**
 * 生成随机端口，设置系统配置
 *
 * 生产环境中不要使用这种静态链式
 */
@Deprecated
public class PortCommand {

    private PortCommand(){}

    private static int from = 10000;
    private static int to = 60000;

    /**
     * 设置端口范围
     * @param from
     * @param to
     * @return
     */
    public static PortCommand range(int from, int to){
        PortCommand.from = from;
        PortCommand.to = to;
        return null;
    }

    /**
     * 生成并保存端口到系统配置
     * @param args
     * @return
     */
    public static void run(String[] args){
        String port = "9999";

        if (args.length == 0){
            int availablePort = NetUtils.getAvailablePort(PortCommand.from, PortCommand.to);
            port = String.valueOf(availablePort);
        }else {
            for (String arg : args){
                if (StringUtils.hasText(arg) && StringUtils.startsWithIgnoreCase(arg, "--server.port")){
                    port = arg.split("=")[1];
                }
            }
        }
        System.setProperty("range.port", port);
    }

}
