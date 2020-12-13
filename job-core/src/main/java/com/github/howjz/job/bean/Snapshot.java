package com.github.howjz.job.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.howjz.job.Job;
import com.github.howjz.job.constant.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


/**
 * @author zhangjh
 * @date 2020/10/23 18:34
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Snapshot implements Serializable {
    private static final long serialVersionUID = -5859189470652608226L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long skip;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long exception;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long stop;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long pause;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long wait;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long ready;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long running;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long complete;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long end;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public static Snapshot fromJobTaskMap(Job job, Map<String, Job> jobTaskMap) {
        // 1、循环所有子任务，根据 thenId 存放执行详情
        Snapshot snapshot = new Snapshot(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        for(String taskId: jobTaskMap.keySet()) {
            Job task = jobTaskMap.get(taskId);
            JobStatus status = task.getStatus();
            switch (status) {
                case SKIP:
                    snapshot.skip += 1;
                    break;
                case EXCEPTION:
                    snapshot.exception += 1;
                    break;
                case STOP:
                    snapshot.stop += 1;
                    break;
                case PAUSE:
                    snapshot.pause += 1;
                    break;
                case WAIT:
                    snapshot.wait += 1;
                    break;
                case READY:
                    snapshot.ready += 1;
                    break;
                case RUNNING:
                    snapshot.running += 1;
                    break;
                case COMPLETE:
                    snapshot.complete += 1;
                    break;
                default:
                    break;
            }
            if (status.isEnd()) {
                snapshot.end += 1;
            }
            snapshot.total += 1;
        }
        return snapshot;
    }

}
