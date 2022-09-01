package com.javaproject.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaproject.takeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
