package com.github.howjz.job.handy;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.bean.NullJob;
import com.github.howjz.job.bean.Snapshot;
import com.github.howjz.job.constant.JobStatus;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.operator.genericjob.GenericJob;
import org.apache.commons.lang.ObjectUtils;

/**
 *  间接调用
 *      间接快捷调用 Helper 的方法
 * @author zhangjh
 * @date 2020/12/11 23:53
 */
public interface JobIndirect {

    default Job addTask(GenericJob task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.addTask(superJob, (Job) task);
        return superJob;
    }

    // ============== 间接调用 ==============
    default Job addTask(Job task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.addTask(superJob, task);
        return superJob;
    }

    default Job addTask(Executable executable) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.addTask(superJob, executable);
        return superJob;
    }

    default boolean exist() {
        return this.getClass() != NullJob.class;
    }

    // 基本无作用
    default Job ready() throws Exception {
        return (Job) this;
    }

    default Job start() throws Exception {
        Job superJob = (Job) this;
        JobHelper.startJob(superJob);
        return superJob;
    }

    default boolean isComplete() {
        Job superJob = (Job) this;
        Snapshot snapshot = superJob.getSnapshot();
        return (snapshot != null && ((snapshot.getTotal() != 0 && ObjectUtils.equals(snapshot.getTotal(), snapshot.getComplete())) || (snapshot.getTotal() == 0 && JobStatus.COMPLETE == superJob.getStatus())))
                || (snapshot == null && superJob.getTasks().size() == 0 && JobStatus.COMPLETE == superJob.getStatus())
                || superJob.getTasks().size() == 0;
    }

    default boolean isEnd() {
        Job superJob = (Job) this;
        Snapshot snapshot = superJob.getSnapshot();
        return (snapshot != null && ((snapshot.getTotal() != 0 && ObjectUtils.equals(snapshot.getTotal(), snapshot.getEnd())) || (snapshot.getTotal() == 0 && JobStatus.COMPLETE == superJob.getStatus())))
                || (snapshot == null && superJob.getTasks().size() == 0 && JobStatus.COMPLETE == superJob.getStatus())
                || superJob.getTasks().size() == 0;
    }
}
