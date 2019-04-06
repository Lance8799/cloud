package org.lance.cloud.fescar.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.lance.cloud.domain.entity.Storage;

public interface StorageMapper extends BaseMapper<Storage> {

    @Update("UPDATE t_storage SET count = count-${count} WHERE commodity_code = #{commodityCode}")
    int decrease(@Param("commodityCode") String commodityCode, @Param("count") Integer count);
}
