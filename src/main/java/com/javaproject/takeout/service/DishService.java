package com.javaproject.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaproject.takeout.dto.DishDto;
import com.javaproject.takeout.entity.Dish;

public interface DishService extends IService<Dish> {
    // add dish and insert dish flavor
    public void saveWithFlavor(DishDto dishDto);

    // query dish and dish flavor by id
    public DishDto getByIdWithFlavor(Long id);

    // update dish info  and flavor
    public void updateWithFlavor(DishDto dishDto);
}
