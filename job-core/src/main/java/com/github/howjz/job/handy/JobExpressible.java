package com.github.howjz.job.handy;

import com.github.howjz.job.Job;
import com.github.howjz.job.JobHelper;
import com.github.howjz.job.operator.execute.Executable;
import com.github.howjz.job.util.JsonUtil;

import java.util.Collection;
import java.util.Map;

/**
 *  快捷方法
 *      有什么能提高效率的方法写在这里
 * @author zhangjh
 * @date 2020/12/11 23:43
 */
public interface JobExpressible {

    default Job numTask(int num, java.util.function.Function<Integer, Executable> getFunction) throws Exception {
        Job superJob = (Job) this;
        for(int i = 0; i < num; i++) {
            Job task = JobHelper.manager.addTask(superJob, getFunction.apply(i));
            task.setParam(String.valueOf(i));
        }
        return superJob;
    }

    default <T> Job mappingTask(Collection<T> ts, java.util.function.Function<T, Executable> getFunction) throws Exception {
        Job superJob = (Job) this;
        if (ts != null) {
            for(T t: ts) {
                Job task = JobHelper.manager.addTask(superJob, getFunction.apply(t));
                if (t instanceof Integer) {
                    task.setParam(String.valueOf(t));
                } else if (t instanceof String) {
                    task.setParam((String) t);
                } else {
                    task.setParam(JsonUtil.toJson(t));
                }
            }
        }
        return superJob;
    }

    default <TK, TV> Job mappingTask(Map<TK, TV> map, java.util.function.Function<Map.Entry<TK, TV>, Executable> getFunction) throws Exception {
        Job superJob = (Job) this;
        if (map != null) {
            for(Map.Entry<TK, TV> entry: map.entrySet()) {
                Job task = JobHelper.manager.addTask(superJob, getFunction.apply(entry));
                task.setParam(JsonUtil.toJson(entry));
                superJob.addTask(task);
            }
        }
        return superJob;
    }

}
