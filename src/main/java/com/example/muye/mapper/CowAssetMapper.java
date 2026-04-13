package com.example.muye.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.muye.entity.CowAsset;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CowAssetMapper extends BaseMapper<CowAsset> {
    // 基础的增删改查已经有了，不需要写任何代码！
}
