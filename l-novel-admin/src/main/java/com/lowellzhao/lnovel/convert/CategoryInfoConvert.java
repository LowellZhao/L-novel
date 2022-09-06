package com.lowellzhao.lnovel.convert;

import com.lowellzhao.lnovel.model.entity.CategoryInfo;
import com.lowellzhao.lnovel.model.vo.CategoryInfoVo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author lowellzhao
 * @since 2022/8/3
 */
@Mapper(componentModel = "spring")
public interface CategoryInfoConvert {

    List<CategoryInfoVo> toCategoryInfoVoList(List<CategoryInfo> categoryInfoList);

}
