package com.github.howjz.job.operator.restart;

import com.github.howjz.job.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/12/14 0:36
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RestartBean implements Serializable {
    private static final long serialVersionUID = -1764811037338446329L;

    private Job job;

    private Job task;
}
