package com.orion.bridge.utils;

import com.orion.bridge.constant.CnConst;
import com.orion.bridge.constant.Const;
import com.orion.ext.location.region.LocationRegions;
import com.orion.ext.location.region.core.Region;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.net.IPs;
import com.orion.lang.utils.time.Dates;

/**
 * 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/12/3 9:28
 */
public class Utils {

    private Utils() {
    }

    /**
     * 获取随机后缀
     *
     * @return suffix
     */
    public static String getRandomSuffix() {
        return UUIds.random32().substring(0, 5).toUpperCase();
    }

    /**
     * 获取后缀
     *
     * @param symbol symbol
     * @return suffix
     */
    public static String getSymbolSuffix(String symbol) {
        return " - " + symbol + Strings.SPACE + UUIds.random32().substring(0, 5).toUpperCase();
    }

    /**
     * 检查是否为合法 ip 区间
     *
     * @param range range
     * @return 是否合法
     */
    public static boolean validIpRange(String range) {
        // ip区间
        String[] split = range.split(Const.SLASH);
        String first = split[0];
        String last = split[1];
        if (split.length != 2) {
            return false;
        }
        // 检查第一段
        if (!IPs.isIpv4(first)) {
            return false;
        }
        // 尾ip
        if (!last.contains(Const.DOT)) {
            last = first.substring(0, first.lastIndexOf(Const.DOT)) + Const.DOT + last;
        }
        return IPs.isIpv4(last);
    }

    /**
     * 检查 ip 是否在范围内
     *
     * @param ip     ip
     * @param filter filter
     * @return 是否在范围内
     */
    public static boolean checkIpIn(String ip, String filter) {
        filter = filter.trim();
        // 单个ip
        if (!filter.contains(Const.SLASH)) {
            return ip.equals(filter);
        }
        // ip区间
        String[] split = filter.split(Const.SLASH);
        String first = split[0];
        String last = split[1];
        // 尾ip
        if (!last.contains(Const.DOT)) {
            last = first.substring(0, first.lastIndexOf(Const.DOT)) + Const.DOT + last;
        }
        return IPs.ipInRange(first, last, ip);
    }

    /**
     * 获取时差
     *
     * @param ms ms
     * @return 时差
     */
    public static String interval(Long ms) {
        if (ms == null) {
            return null;
        }
        return Dates.interval(ms, false, "d ", "h ", "m ", "s");
    }

    /**
     * 清空高亮标签
     *
     * @param m m
     * @return clean
     */
    public static String cleanStainTag(String m) {
        if (Strings.isEmpty(m)) {
            return m;
        }
        return m.replaceAll("<sb>", Const.EMPTY)
                .replaceAll("<sb 0>", Const.EMPTY)
                .replaceAll("<sb 2>", Const.EMPTY)
                .replaceAll("</sb>", Const.EMPTY)
                .replaceAll("<sr>", Const.EMPTY)
                .replaceAll("<sr 0>", Const.EMPTY)
                .replaceAll("<sr 2>", Const.EMPTY)
                .replaceAll("</sr>", Const.EMPTY)
                .replaceAll("<b>", Const.EMPTY)
                .replaceAll("</b>", Const.EMPTY);
    }

    /**
     * 获取 ip 位置
     *
     * @param ip ip
     * @return ip 位置
     */
    public static String getIpLocation(String ip) {
        if (ip == null) {
            return CnConst.UNKNOWN;
        }
        Region region;
        try {
            region = LocationRegions.getRegion(ip, 3);
        } catch (Exception e) {
            return CnConst.UNKNOWN;
        }
        if (region != null) {
            String net = region.getNet();
            String province = region.getProvince();
            if (net.equals(CnConst.INTRANET_IP)) {
                return net;
            }
            if (province.equals(CnConst.UNKNOWN)) {
                return province;
            }
            StringBuilder location = new StringBuilder()
                    .append(region.getCountry())
                    .append(Const.DASHED)
                    .append(province)
                    .append(Const.DASHED)
                    .append(region.getCity());
            location.append(" (").append(net).append(')');
            return location.toString();
        }
        return CnConst.UNKNOWN;
    }

}
