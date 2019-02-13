package org.lance.cloud.domain.transmit;

import org.lance.cloud.utils.JsonUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 多功能复杂接口，抽象类可能更好
 */
public interface DataTransmit {

    /**
     * 缓存时的ID
     * @return
     */
    default String cacheId() {
        return "";
    }

    /**
     * 转换成json字符串
     * @return
     */
    default String json(){
        return JsonUtils.toJson(this);
    }

    /**
     * 转换成目标对象
     * @param target
     * @param <T>
     * @return
     */
    default <T> T convert(T target){
        if (Objects.nonNull(target))
            BeanUtils.copyProperties(this, target);
        return target;
    }

    /**
     * 目标对象转换成当前对象
     * @param source 源对象
     * @param <T> 数据源类型
     * @param <E> 限定转换类型
     * @return
     */
    @SuppressWarnings("unchecked")
    default <T, E extends DataTransmit> E transfer(T source) {
        if (Objects.nonNull(source))
            BeanUtils.copyProperties(source, this);
        return (E) this;
    }
}
