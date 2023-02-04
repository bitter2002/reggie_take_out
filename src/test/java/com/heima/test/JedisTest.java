package com.heima.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

/*
 * 使用Jedis获取Redis
 * */
public class JedisTest {
    @Test
    public void testRedis() {
        //1.获取连接
        Jedis jedis = new Jedis("localhost", 6379);
        //2.执行具体操作
        jedis.set("username", "lyz");

        //jedis.del("username");

        jedis.hset("myhash", "addr", "wanan");

        String hValue = jedis.hget("myhash", "addr");
        System.out.println(hValue);
        //3.关闭连接
        jedis.close();
    }
}
