package com.heima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.R;
import com.heima.dto.SetmealDto;
import com.heima.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /*
     * 新增套餐,同时需要保存套餐和菜品的关联关系
     * */
    public void saveWithDish(SetmealDto setmealDto);

    /*
     * 删除套餐以及删除套餐和菜品所关联的数据
     * */
    public void removeWithDish(List<Long> ids);

    //根据id查询对应的套餐信息和套餐对应的菜品
    public SetmealDto getByIdWithDish(Long id);

    //修改套餐以及套餐中的菜品
    public void updateWithDish(SetmealDto setmealDto);


}
