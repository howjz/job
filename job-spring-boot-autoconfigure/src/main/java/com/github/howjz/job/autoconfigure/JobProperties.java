package com.github.howjz.job.autoconfigure;

import com.github.howjz.job.Executor;
import com.github.howjz.job.constant.JobKey;
import com.github.howjz.job.operator.debug.DebugProperties;
import com.github.howjz.job.operator.pool.PoolProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/11 9:46
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Configuration
@ConfigurationProperties(prefix = JobKey.PROJECT_ID)
public class JobProperties implements Serializable {
    private static final long serialVersionUID = 8914568354563934425L;

    // 是否启动
    private boolean enable = false;

    // 默认是local模式
    private Executor.Mode type = Executor.Mode.LOCAL;

    private DebugProperties debug = new DebugProperties();

    private PoolProperties pool = new PoolProperties();
}
