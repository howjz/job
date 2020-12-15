package com.github.howjz.job.constant;

/**
 * @author zhangjh
 * @date 2020/12/4 16:16
 */
public class JobKey {

    // 完整的项目ID
    public static final String COMPLETE_PROJECT_ID = "com.github.howjz.job";

    // 简称的项目ID
    public static final String PROJECT_ID = "how.job";

    // 间隔符
    public static final String SEPARATOR = "-";

    // 表示所有
    public static final String ALL = "*";

    // ================================ 主要变量的key ================================
    public static class Context {

        public static final String INSTANCE = "how:job";

        public static final String EXECUTOR = "how:job:executor";

        public static final String EXECUTORS = "how:job:executors";

        public static final String JOB_MAP = "how:job:job:map";

        public static final String JOB_TASK_MAP = "how:job:job:task:map";

        public static final String TASK_QUEUE = "how:job:task:queue";

        public static final String EXECUTOR_POOL = "how:job:executor:job";

        public static final String SPARE_EXECUTOR_POOL = "how:job:spare:executor:job";

        public static final String OPERATORS = "how:job:operators";


        // ================= 操作相关数据 =================
        public static final String OPERATOR_NOTIFY_DATA = "how:job:operator:notify:data";
        public static final String OPERATOR_NOTIFY_DATA_NOTIFY_MAP = "how:job:operator:notify:data:map:notify";

        public static final String OPERATOR_ERROR_DATA = "how:job:operator:error:data";
        public static final String OPERATOR_ERROR_DATA_ALWAYS_RETRY_SET = "how:job:operator:error:data:set:alwaysRetry";
        public static final String OPERATOR_ERROR_DATA_ERRORABLE_MAP = "how:job:operator:error:data:map:errorable";

        public static final String OPERATOR_EXECUTE_DATA = "how:job:operator:execute:data";
        public static final String OPERATOR_EXECUTE_DATA_EXECUTABLE_MAP = "how:job:operator:execute:data:map:executable";
        public static final String OPERATOR_EXECUTE_DATA_EXECUTE_RUNNABLE_MAP = "how:job:operator:execute:data:map:executeRunnable";

        public static final String OPERATOR_LINK_DATA = "how:job:operator:link:data";
        public static final String OPERATOR_LINK_DATA_NOTIFY_MAP = "how:job:operator:link:data:map:notify";
        public static final String OPERATOR_LINK_DATA_WAIT_MAP = "how:job:operator:link:data:map:wait";

        public static final String OPERATOR_THEN_DATA = "how:job:operator:then:data";
        public static final String OPERATOR_THEN_DATA_ALL_THEN_MAP = "how:job:operator:then:data:map:allThen";
        public static final String OPERATOR_THEN_DATA_NOTIFY_MAP = "how:job:operator:then:data:map:notify";
        public static final String OPERATOR_THEN_DATA_WAIT_MAP = "how:job:operator:then:data:map:wait";
        public static final String OPERATOR_THEN_DATA_ThenBeanMap = "how:job:operator:then:data:map:thenBean";


        public static final String OPERATOR_JOIN_DATA = "how:job:operator:join:data";
        public static final String OPERATOR_JOIN_DATA_NOTIFY_MAP = "how:job:operator:join:data:map:notify";
        public static final String OPERATOR_JOIN_DATA_WAIT_MAP = "how:job:operator:join:data:map:wait";


        public static final String OPERATOR_CROSS_DATA = "how:job:operator:cross:data";
        public static final String OPERATOR_CROSS_DATA_CROSS_TYPE_MAP = "how:job:operator:cross:data:map:crossType";

        public static final String OPERATOR_WAITING_DATA = "how:job:operator:waiting:data";
        public static final String OPERATOR_WAITING_DATA_JOB_LOCK = "how:job:operator:waiting:data:lock:job";

        // ======== redis ========
        public static final String REDIS_JOB_TEMPLATE = "how:job:redis:job:template";
        public static final String REDIS_OBJECT_TEMPLATE = "how:job:redis:object:template";

    }

}
