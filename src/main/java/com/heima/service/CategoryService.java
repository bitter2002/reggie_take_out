package com.heima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.entity.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
