package org.lance.cloud.fescar.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.lance.cloud.domain.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {

    @Insert("INSERT INTO t_order VALUES(null,#{order.orderNo},#{order.userId},#{order.commodityCode},${order.count},${order.amount})")
    int create(@Param("order") Order order);

}
