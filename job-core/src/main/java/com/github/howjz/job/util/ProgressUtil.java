package com.github.howjz.job.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhangjh
 * @date 2020/12/15 14:13
 */
public class ProgressUtil {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(StringUtils.center("测试", 50 , "="));
        int total = 1000;
        for(int i = 0; i <= total; i++) {
            String text = progress(i, total,
                    true, "[", "]",
                    true, 30, "[", "=", "-", "]",
                    true);
            System.out.print(text);
            Thread.sleep(10);
        }
    }

    public static void clear(String text) {
        for(int i = 0;i < text.length(); i++) {
            System.out.print("\b");
        }
    }

    /**
     * 进度详情
     *  60 / 360
     * @param complete          完成数
     * @param total             总数
     * @param leftText          详情左边文字
     * @param rightText         详情右边文字
     * @return                  进度详情文字
     */
    public static String progressDetail(long complete, long total, String leftText, String rightText) {
        int showCompleteLength = String.format("%s / %s", total, total).length() + 2;
        return leftText + StringUtils.center(String.format("%s / %s", complete, total), showCompleteLength) + rightText;
    }

    /**
     * 进度条
     *  ====-------
     * @param complete          完成数
     * @param total             总数
     * @param length            长度
     * @param leftText          进度条左边文字
     * @param completeText      进度条完成文字
     * @param runningText       进度条运行文字
     * @param rightText         进度条右边文字
     * @return                  进度条文字
     */
    public static String progressBar(long complete, long total, int length, String leftText, String completeText, String runningText, String rightText) {
        int progress = (int) (complete * 1.0 * 100 / total);
        int completeLength = progress * length / 100;
        return leftText + StringUtils.leftPad("", completeLength, completeText) + StringUtils.leftPad("", length - completeLength, runningText) + rightText;
    }

    public static String progressPercent(long complete, long total) {
        int progress = (int) (complete * 1.0 * 100 / total);
        return StringUtils.leftPad(String.format("%s%%", progress), 4, "");
    }

    /**
     * 获取进度条
     * @param complete              完成数
     * @param total                 总数
     * @param showDetail            是否显示详情
     * @param detailLeftText        详情左边文字
     * @param detailRightText       详情右边文字
     * @param showPercent           是否显示百分比
     * @param showBar               是否显示进度条
     * @param barLength             进度条长度
     * @param barLeftText           进度条左边文字
     * @param barCompleteText       进度条完成文字
     * @param barRunningText        进度条执行文字
     * @param barRightText          进度条右边文字
     * @return                      进度文字
     */
    public static String progress(long complete,
                                  long total,

                                  boolean showDetail,
                                  String detailLeftText,
                                  String detailRightText,

                                  boolean showBar,
                                  int barLength,
                                  String barLeftText,
                                  String barCompleteText,
                                  String barRunningText,
                                  String barRightText,

                                  boolean showPercent

    ) {
        return (showDetail ? progressDetail(complete, total, detailLeftText, detailRightText) : "") +
                (showBar ? progressBar(complete, total, barLength, barLeftText, barCompleteText, barRunningText, barRightText ) : "" )+
                (showPercent ? progressPercent(complete, total) : "");


    }

}
