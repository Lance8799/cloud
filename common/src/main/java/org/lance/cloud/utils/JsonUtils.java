package org.lance.cloud.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.lance.cloud.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 序列化设置
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // 根据字母排序，等效但不及@JsonPropertyOrder(alphabetic = true, value = "{c, b}")
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("to json exception.", e);
            throw new ApplicationException("把对象转换为JSON时出错了", e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz){
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("to object exception.", e);
            throw new ApplicationException("把JSON转换为对象时出错了", e);
        }
    }

    public static JsonNode toNode(String json){
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            log.error("to object exception.", e);
            throw new ApplicationException("把JSON转换为对象时出错了", e);
        }
    }
}
