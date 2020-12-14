package com.github.howjz.job.util;

import okhttp3.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjh
 * @date 2020/12/14 13:52
 */
public class HttpUtil {


    /**
     * 默认连接超时时间,3s。0 是永远不超时
     */
    public static final long DEFAULT_CONNECTION_TIMEOUT = 3000;

    /**
     * 默认读取超时时间,120s。0 是永远不超时
     */
    public static final long DEFAULT_READ_TIMEOUT = 120000;

    private static OkHttpClient client;

    static {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .cache(new Cache(new File(FileUtils.getTempDirectory().getPath(), "okhttpcache"), 10 * 1024 * 1024))
                    .build();
        }
    }

    /**
     * @param url
     * @param connectionTimeout 连接超时时间，单位为毫秒
     * @param readTimeout       读取超时时间，单位为毫秒
     * @throws IOException
     */
    public static Response getContentLengthSync(String url, long connectionTimeout, long readTimeout) throws IOException {
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
//                .method("OPTIONS", null)
                .build();
        return doSync(request, connectionTimeout, readTimeout);
    }

    /**
     * 同步请求
     */
    private static Response doSync(Request request, long connectionTimeout, long readTimeout) throws IOException {
        OkHttpClient newClient = client.newBuilder().connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
        // 创建请求会话
        Call call = newClient.newCall(request);
        // 同步执行会话请求
        return call.execute();
    }


    /**
     * @param url               下载链接
     * @param connectionTimeout 连接超时时间，单位为毫秒
     * @param readTimeout       连接超时时间，单位为毫秒
     * @param startIndex        下载起始位置
     * @param endIndex          结束为止
     * @param startIndex
     * @param endIndex
     * @throws IOException
     */
    public static Response downloadFileByRangeSync(String url, long connectionTimeout, long readTimeout, long startIndex, long endIndex) throws IOException {
        // 创建一个Request
        // 设置分段下载的头信息。 Range:做分段数据请求,断点续传指示下载的区间。格式: Range bytes=0-1024或者bytes:0-1024
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(url)
                .build();
        return doSync(request, connectionTimeout, readTimeout);
    }


}
