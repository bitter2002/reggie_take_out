package com.heima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.R;
import com.heima.dto.DishDto;
import com.heima.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表.dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息和对应的口味信息
    void updateWithFlavor(DishDto dishDto);

     void removeWithFlavor(List<Long> ids);

    public R<String> sellStatus(Integer status, List<Long> ids);
}
