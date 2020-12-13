package com.github.howjz.job.operator.config;

import lombok.*;

import java.io.Serializable;

/**
 * @author zhangjh
 * @date 2020/11/12 14:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ConfigBean implements Serializable {
    private static final long serialVersionUID = 3827996429676503760L;

    // 用途标记
    private String purpose;

    @Builder.Default
    private Integer retry = 0;

    // 是否动态作业，true：动态作业，同个ID作业未执行完成前可以继续添加子任务   false：非动态，同个ID作业只有当 未开始 或者 完成后才可以继续添加子任务
    // TODO: 2020/11/13  
    @Builder.Default
    private boolean dynamic = false;

}
