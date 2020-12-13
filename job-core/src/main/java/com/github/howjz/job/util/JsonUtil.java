package com.github.howjz.job.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * JSON工具类
 * @author zhangjh
 * @date 2019/5/21 18:30
 */
@Slf4j
public class JsonUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper mapper;

    private static final JacksonJsonFilter JACKSON_JSON_FILTER = new JacksonJsonFilter();

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormat);
        // 允许对象忽略json中不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现特殊字符和转义符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 忽视为null的属性，允许为空字符串或空对象
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public synchronized static String toJson(Object obj) {
        try {
            mapper.setFilterProvider(JACKSON_JSON_FILTER);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("toJson Exception: {}", e.getMessage());
            throw new RuntimeException("转换json字符失败!");
        }
    }

    public synchronized static <T> T toObject(byte[] buffer, Class<T> clazz) {
        try {
            return mapper.readValue(buffer, clazz);
        } catch (IOException e) {
            log.error("toObject IOException: {}", e.getMessage());
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
    }

    public synchronized static <T> T toObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("toObject IOException: {}", e.getMessage());
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
    }

    public synchronized static Object toObject(byte[] buffer) {
        try {
            if(buffer.length > 0){
                return mapper.readValue(buffer, Object.class);
            }
        } catch (IOException e) {
            log.error("toObject IOException: {}", e.getMessage());
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
        return null;
    }

    public synchronized static Object toObject(String json) {
        try {
            if(StringUtils.isNotEmpty(json)){
                return mapper.readValue(json, Object.class);
            }
        } catch (IOException e) {
            log.error("toObject IOException: {}", json);
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
        return null;
    }

    public synchronized static <T> T toObject(byte[] buffer, TypeReference<T> clazz) {
        try {
            return mapper.readValue(buffer, clazz);
        } catch (IOException e) {
            log.error("toObject IOException: {}", e.getMessage());
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
    }

    public synchronized static <T> T toObject(String json, TypeReference<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("toObject IOException: {}", json);
            throw new RuntimeException("将json字符转换为对象时失败!");

        }
    }

    public synchronized static boolean isJson(byte[] content) {
        try {
            mapper.readTree(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
