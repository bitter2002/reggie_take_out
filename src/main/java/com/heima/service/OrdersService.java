package com.heima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.R;
import com.heima.entity.Orders;

public interface OrdersService extends IService<Orders> {
    /*
     * 用户下单
     * */
    public void submit(Orders orders);
}
