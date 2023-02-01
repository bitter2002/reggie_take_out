package com.heima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.CustomException;
import com.heima.common.R;
import com.heima.entity.Category;
import com.heima.entity.Dish;
import com.heima.entity.Setmeal;
import com.heima.mapper.CategoryMapper;
import com.heima.service.CategoryService;
import com.heima.service.DishService;
import com.heima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /*
     * 根据id删除分类,删除之前要进行判断
     * */
    @Override
    public void remove(Long ids) {
        //查询分类是否关联菜品,如果已经关联,判处一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //已经关联菜品,抛出一个业务异常
            throw new CustomException("当前分类下关联菜品,不能删除");
        }
        //查询分类是否关联套餐,如果已经关联,判处一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联套餐,抛出一个业务异常
            throw new CustomException("当前分类下关联套餐,不能删除");
        }
        //正常删除分类
        super.removeById(ids);
    }

}
