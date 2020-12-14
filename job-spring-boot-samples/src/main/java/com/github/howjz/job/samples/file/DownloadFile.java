package com.github.howjz.job.samples.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FilenameUtils;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/14 15:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownloadFile implements Serializable {
    private static final long serialVersionUID = -5328749445991113757L;

    private String url;

    private String fileName;

    // 分块大小
    private long chunkSize = 1024 * 1024 * 10;

    // 总分块数
    private long chunks;

    // 连接超时时间，单位为毫秒。0 是永远不超时
    private long connectionTimeout = 0;

    // 读取超时时间，单位为毫秒.0 是永远不超时
    private long readTimeout = 0;

    // 文件大小
    private long fileSize;

    // 文件MD5
    private String fileMd5;

    // 已完成大小
    private long completeSize;

    public DownloadFile(String url) {
        this.url = url;
        String name = FilenameUtils.getName(url);
        if (name.contains("?")) {
            name = name.substring(0, name.indexOf("?"));
        }
        this.fileName = "E://data-temp/download/" + name;
    }
}
