package com.github.howjz.job.samples.file;

import com.github.howjz.job.Job;
import com.github.howjz.job.operator.config.ConfigBean;
import com.github.howjz.job.operator.cross.CrossType;
import com.github.howjz.job.operator.genericjob.GenericJob;
import com.github.howjz.job.operator.pool.PoolBean;
import com.github.howjz.job.util.HttpUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  多线程分块下载作业
 *  话说
 *      RandomAccessFile tmpAccessFile = new RandomAccessFile(this.tempFile, "rw")
 *  这个居然支持多线程追加
 * @author zhangjh
 * @date 2020/12/14 13:41
 */
public class DownloadJob extends GenericJob<DownloadFile> {

    private static final long serialVersionUID = 2471762565794699328L;

    private final File tempFile;

    private final File targetFile;

    private final String flagPath;

    private final Map<Integer, Long> chunkComplete;

    /**
     * 文件分块下载作业
     * @param file      文件配置
     * @throws Exception
     */
    public DownloadJob(DownloadFile file) throws Exception {
        super(file);
        this.chunkComplete = new ConcurrentHashMap<>();
        this.tempFile = new File(file.getFileName() + ".tmep");
        this.targetFile = new File(file.getFileName());
        this.flagPath = FilenameUtils.getFullPath(file.getFileName()) + FilenameUtils.getBaseName(file.getFileName());
        if (!this.tempFile.exists()) {
            FileUtils.touch(tempFile);
        }
        this.init();
    }

    @Override
    public void handleCreateJob(Job job) throws Exception {
        job.pool(PoolBean.PRIVATE(10));
        job.cross(CrossType.DISABLE);
    }

    @Override
    public List<Object> generateTaskParams(DownloadFile file) throws Exception {
        // 1、获取文件长度，并查询是否支持分块下载
        Response response = HttpUtil.getContentLengthSync(file.getUrl(), file.getConnectionTimeout(), file.getReadTimeout());
        long contentLength = this.getResponseContentLength(response);
        // 2、计算有多少块分块
        long chunks = (contentLength / file.getChunkSize()) + 1;
        file.setFileSize(contentLength);
        file.setChunks(chunks);
        file.setFileSize(contentLength);
        if (this.targetFile.exists() && this.targetFile.length() == contentLength) {
            // 1.1、如果文件已存在 并且长度相等，直接完成
            this.complete();
            return Collections.emptyList();
        } else {
            // 1.2、移除旧文件
            FileUtils.forceDeleteOnExit(this.targetFile);
        }
        // 3、生成分块编号列表
        System.out.println(String.format("文件 [%s] 划分分块数： [%s]", FilenameUtils.getName(file.getFileName()), chunks));
        return Stream
                .iterate(0, n -> n + 1)
                .limit(chunks)
                .collect(Collectors.toList());
    }

    /**
     * 获取分块编号，开始下载分块
     * @param job       作业
     * @param task      任务
     * @param param     分块编号
     * @return
     * @throws Exception
     */
    @Override
    public String execute(Job job, Job task, Object param) throws Exception {
        // 1、获取分块编号 及 文件对象
        int chunkSize = (int) param;
        DownloadFile file = (DownloadFile) job.getParam();
        // 2、生成下载标记文件
        File flagFile = new File(flagPath + "/flag_" + chunkSize + ".txt");
        if (!flagFile.exists()) {
            FileUtils.touch(flagFile);
        }
        RandomAccessFile cacheAccessFile = new RandomAccessFile(flagFile, "rwd");
        // 3、计算分支开始下载的位置
        long startIndex = chunkSize * file.getChunkSize();
        long originStartIndex = startIndex;
        // 分支结束下载的位置
        long endIndex = (chunkSize + 1) * file.getChunkSize() - 1;
        // 最后一个任务下载剩余长度
        if (chunkSize == file.getChunks() - 1 && endIndex != file.getFileSize() - 1) {
            endIndex = file.getFileSize() - 1;
        }
        // 4、获取下载数据
        Response response = HttpUtil.downloadFileByRangeSync(file.getUrl(), file.getConnectionTimeout(), file.getReadTimeout(), startIndex, endIndex);
        if (response.code() != 206 || response.body() == null) {
            throw new RuntimeException("不支持断点下载");
        }
        if (!flagFile.exists()) {
            // 4.1、读取本地下载标记文件中记录的下载位置
            String startIndexStr = cacheAccessFile.readLine();
            if (startIndexStr != null) {
                startIndex = Long.parseLong(startIndexStr);
                if (startIndex >= endIndex) {
                    return flagFile.getPath();
                }
            }
        }
        // 5、保存文件
        ResponseBody resBody = response.body();
        InputStream is = resBody.byteStream();
        RandomAccessFile tmpAccessFile = new RandomAccessFile(this.tempFile, "rw");
        // 文件写入的开始位置.
        tmpAccessFile.seek(startIndex);
        byte[] buffer = new byte[1024 * 1024];
        int length = -1;
        long progress = startIndex;
        try {
            while ((length = is.read(buffer)) > 0) {
                tmpAccessFile.write(buffer, 0, length);
                progress += length;
                // 将该分支最新完成下载的位置记录并保存到缓存数据文件中
                // 建议转成Base64码，防止数据被修改，导致下载文件出错
                cacheAccessFile.seek(0);
                cacheAccessFile.write((progress + "").getBytes(StandardCharsets.UTF_8));
                // 记录完成进度
                long complete = progress - originStartIndex;
                this.chunkComplete.put(chunkSize, complete);
                file.setCompleteSize(this.chunkComplete.values().stream().mapToLong(i->i).sum());
                System.out.println(String.format("文件分块: [%s:%s]  总进度： [ %s / %s ]", FilenameUtils.getName(file.getFileName()), chunkSize, file.getCompleteSize(), file.getFileSize()));
            }
        } catch (IOException e) {
            return null;
        } finally {
            //关闭资源
            close(cacheAccessFile, is, resBody, tmpAccessFile);
        }
        System.out.println(String.format("文件 [%s] 分块[%s] 下载完成", FilenameUtils.getName(file.getFileName()), chunkSize));
        return flagFile.getPath();
    }

    /**
     * 关闭资源
     *
     * @param resources
     */
    private void close(Closeable... resources) {
        int length = resources.length;
        try {
            for (int i = 0; i < length; i++) {
                Closeable closeable = resources[i];
                if (null != closeable) {
                    resources[i].close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (int i = 0; i < length; i++) {
                resources[i] = null;
            }
        }
    }

    private long getResponseContentLength(Response response) {
        if (200 != response.code() || response.body() == null) {
            throw new RuntimeException("下载链接无效");
        }
        long contentLength = response.body().contentLength();
        if (contentLength < 1) {
            throw new RuntimeException("下载链接无效");
        }
        return response.body().contentLength();
    }

    @Override
    public void handleExceptionTask(Job job, Job task, Exception exception) throws Exception {
        System.out.println("分块 " + task.getParam() + " 发生异常：" + exception);
    }

    @Override
    public void then(Job job) throws Exception {
        // 下载完毕后，重命名目标文件名
        DownloadFile file = (DownloadFile) job.getParam();
        if (this.targetFile.exists() && this.targetFile.length() != file.getFileSize()) {
            FileUtils.forceDelete(this.targetFile);
        }
        if (!this.targetFile.exists()) {
            this.tempFile.renameTo(targetFile);
        }
        if (this.tempFile.exists()) {
            FileUtils.forceDelete(this.tempFile);
        }
        if (new File(flagPath).exists()) {
            FileUtils.forceDelete(new File(flagPath));
        }
        System.out.println(String.format("文件 [%s] 下载完成", FilenameUtils.getName(file.getFileName())));
    }
}
