package com.github.howjz.job.operator;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjh
 * @date 2020/12/15 16:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperatorData implements Serializable {
    private static final long serialVersionUID = 8214541295457206481L;

    private Date time = new Date();
}
