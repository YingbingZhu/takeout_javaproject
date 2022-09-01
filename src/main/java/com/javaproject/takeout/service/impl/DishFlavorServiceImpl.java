package com.javaproject.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaproject.takeout.entity.DishFlavor;
import com.javaproject.takeout.mapper.DishFlavorMapper;
import com.javaproject.takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
