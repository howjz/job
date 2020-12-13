package com.github.howjz.job.operator.debug;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjh
 * @date 2020/12/13 14:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DebugProperties implements Serializable {
    private static final long serialVersionUID = -3692468256737070983L;

    // =========================== 配置信息 ===========================
    // 是否打印日志
    private boolean log = false;

    // debug的休眠时间，会使得全部任务执行变慢，但是更好debug
    private long sleep = 0;

    // =========================== 存放其他数据，不应随便修改 ===========================
    // 最后访问时间
    private Date lastTime;

    // 当前的任务总数
    private int taskCount;

}
