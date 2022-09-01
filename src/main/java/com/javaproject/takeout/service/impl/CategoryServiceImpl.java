package com.javaproject.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaproject.takeout.common.CustomException;
import com.javaproject.takeout.common.R;
import com.javaproject.takeout.entity.Category;
import com.javaproject.takeout.entity.Dish;
import com.javaproject.takeout.entity.Setmeal;
import com.javaproject.takeout.mapper.CategoryMapper;
import com.javaproject.takeout.service.CategoryService;
import com.javaproject.takeout.service.DishService;
import com.javaproject.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * remove by id, check if category has correlated dish or setmeal
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // add filter condition
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //  check if category has correlated dish or setmeal, if correlated, raise error
        if (count1>0) {
            throw new CustomException("it is correlated with some dishes, can not delete");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0) {
            throw new CustomException("it is correlated with some setmeal, can not delete");
        }

        // delete
        super.removeById(id);

    }

}
