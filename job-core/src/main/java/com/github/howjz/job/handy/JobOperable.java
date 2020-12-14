package com.github.howjz.job.handy;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.operator.Operator;
import com.github.howjz.job.operator.config.ConfigBean;
import com.github.howjz.job.operator.cross.CrossType;
import com.github.howjz.job.operator.error.Errorable;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.operator.notify.NotifyBean;
import com.github.howjz.job.operator.notify.NotifyType;
import com.github.howjz.job.operator.pool.PoolBean;
import com.github.howjz.job.operator.restart.RestartBean;
import com.github.howjz.job.operator.then.Thenable;

/**
 *  操作相关
 *      增加操作后需要触发时，在该类新增方法调用
 * @author zhangjh
 * @date 2020/12/11 23:36
 */
public interface JobOperable {

    default Job config(ConfigBean configBean) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.CONFIG, superJob, configBean);
        return superJob;
    }

    default void pause() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY, superJob, new NotifyBean(NotifyType.PAUSE, superJob, null));
    }

    default void pause(Job task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY, superJob, new NotifyBean(NotifyType.PAUSE, superJob, task));
    }

    default void stop() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY, superJob, new NotifyBean(NotifyType.STOP, superJob, null));
    }

    default void stop(Job task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY,superJob, new NotifyBean(NotifyType.STOP, superJob, task));
    }

    default void restart() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.RESTART, superJob, new RestartBean(superJob, null));
    }

    default void restart(Job task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.RESTART, superJob, new RestartBean(superJob, task));
    }

    default void remove() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY, superJob, new NotifyBean(NotifyType.REMOVE, superJob, null));
    }

    default void remove(Job task) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.NOTIFY, superJob, new NotifyBean(NotifyType.REMOVE, superJob, task));
    }

    default void waiting() throws Exception {
        JobHelper.manager.handleOperate(Operator.Operate.WAITING, (Job) this, null);
    }

    default Job pool(PoolBean poolBean) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.POOL, superJob, poolBean);
        return superJob;
    }

    default Job execute(Executable executable) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.EXECUTE, superJob, executable);
        return superJob;
    }

    default Job then(Thenable thenable) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.THEN, superJob, thenable);
        return superJob;
    }

    default Job allThen(Thenable thenable) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.ALL_THEN, superJob, thenable);
        return superJob;
    }

    default Job join() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.JOIN, superJob, null);
        return superJob;
    }

    default Job error(Errorable errorable) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.ERROR, superJob, errorable);
        return superJob;
    }

    default Job alwaysRetry() throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.ALWAYS_RETRY, superJob, null);
        return superJob;
    }

    default Job cross(CrossType crossType) throws Exception {
        Job superJob = (Job) this;
        JobHelper.manager.handleOperate(Operator.Operate.CROSS, superJob, crossType);
        return superJob;
    }

}
