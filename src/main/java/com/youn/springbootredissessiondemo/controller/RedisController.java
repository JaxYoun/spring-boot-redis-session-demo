package com.youn.springbootredissessiondemo.controller;

import com.youn.springbootredissessiondemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/26 13:46
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 手动-String缓存操作
     *
     * @return
     */
    @GetMapping("/stringTest")
    public String stringTest() {
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        valueOperations.set("yang", "杨", 20L, TimeUnit.SECONDS);
        System.out.println(valueOperations.get("yang"));

        valueOperations.set("kkk", "kkk", 20L, TimeUnit.SECONDS);
        System.out.println(valueOperations.get("kkk"));
        return valueOperations.getAndSet("kkk", "kk");  //拿出久值，并重新设置新值
    }

    /**
     * 手动-Object缓存操作
     *
     * @return
     */
    @GetMapping("/objectTest")
    public Object objectTest() {
        ValueOperations<String, User> valueOperations = this.redisTemplate.opsForValue();
        valueOperations.set("yangjianxiong", new User(1L, "杨建雄"), 10L, TimeUnit.SECONDS);
        User user0 = valueOperations.get("youn");
        if (user0 != null) {
            return user0;
        } else {
            User myUser = new User(1L, "new杨建雄");
            valueOperations.set("youn", myUser, 10L, TimeUnit.HOURS);
            return myUser;
        }

    }

    /**
     * hash缓存-多用于系统缓存
     */
    @GetMapping("/systemHashCacheTest")
    public void systemHashCacheTest() {
        HashOperations<String, Object, Object> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.put("name", "1", new User(2L, "yang"));
        System.out.println(hashOperations.get("name", "1"));
        hashOperations.delete("name", "1");
        hashOperations.getOperations().expire("name", 20L, TimeUnit.SECONDS);
    }

    /**
     * 自动-二级缓存
     *
     * @return
     */
    @Cacheable(key = "#userKey")
    @GetMapping("/getUser")
    public Object getUser() {
        return null;
    }

}
