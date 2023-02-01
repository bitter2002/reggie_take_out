package com.heima.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.CustomException;
import com.heima.common.R;
import com.heima.dto.SetmealDto;
import com.heima.entity.Dish;
import com.heima.entity.Setmeal;
import com.heima.entity.SetmealDish;
import com.heima.mapper.DishMapper;
import com.heima.mapper.SetmealMapper;
import com.heima.service.SetmealDishService;
import com.heima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /*
     * 新增套餐,同时需要保存套餐和菜品的关联关系
     * */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息,操作Setmeal,执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息,操作Setmeal dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /*
     * 删除套餐以及删除套餐和菜品所关联的数据
     * */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态,确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        //如果不能删除,则抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中,不能删除");
        }
        //如果可以删除,先删除套餐表的数据
        this.removeByIds(ids);
        //再删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //先查询Setmeal表中信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        //查询当前套餐对应的菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;


    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //修改setmeal表中信息
        this.updateById(setmealDto);
        //先删除套餐所对应的彩品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //添加提交过来的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }



}
