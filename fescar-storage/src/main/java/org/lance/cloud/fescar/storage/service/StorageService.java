package org.lance.cloud.fescar.storage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Storage;
import org.lance.cloud.fescar.storage.mapper.StorageMapper;
import org.springframework.stereotype.Service;

@Service
public class StorageService extends ServiceImpl<StorageMapper, Storage> {

    public HttpResult decrease(Storage storage){
        int count = baseMapper.decrease(storage.getCommodityCode(), storage.getCount());
        if (count > 0)
            return HttpResultBuilder.ok(null, "扣除库存成功");
        return HttpResultBuilder.fail("扣除库存失败");
    }
}
