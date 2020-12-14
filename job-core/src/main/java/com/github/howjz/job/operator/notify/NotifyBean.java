package com.github.howjz.job.operator.notify;

import com.github.howjz.job.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/14 9:47
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyBean implements Serializable {
    private static final long serialVersionUID = 6962242928677065592L;

    private NotifyType notifyType;

    private Job job;

    private Job task;

}
