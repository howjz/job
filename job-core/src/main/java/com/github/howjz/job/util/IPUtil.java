package com.github.howjz.job.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author linzq
 * @author fangwk
 * @date 2019/5/8 9:12
 */
@Slf4j
public class IPUtil {

    /**
     * Get a site local address like 192.168.xxx.xxx
     *
     * @return
     */
    public static String getSiteLocalAddress() {
        try {
            // Windows was special :)
            if (OSUtil.isWindows()) {
                return InetAddress.getLocalHost().getHostAddress();
            }

            // get all net interface
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            // traverse
            while (allNetInterfaces.hasMoreElements()) {

                NetworkInterface netInterface = allNetInterfaces.nextElement();

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {

                    InetAddress ip = addresses.nextElement();

                    if (ip.isSiteLocalAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("getSiteLocalAddress", e);
        }

        return null;
    }
}
