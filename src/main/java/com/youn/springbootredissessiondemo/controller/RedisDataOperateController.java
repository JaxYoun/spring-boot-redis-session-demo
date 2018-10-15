package com.youn.springbootredissessiondemo.controller;

import com.youn.springbootredissessiondemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 针对redis支持的五种数据结构进行测试
 */
@RestController
@RequestMapping("/redis/operate")
public class RedisDataOperateController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 字符串操作
     */
    @PostMapping("/stringOpt")
    public void stringOpt(String key, String value) {
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        //向Redis存带有过期时间的键值对
        valueOperations.set("young", "杨", 20L, TimeUnit.HOURS);
        System.out.println(valueOperations.get("young"));

        valueOperations.set("kkk0", "old", 20L, TimeUnit.HOURS);
        System.out.println(valueOperations.get("kkk"));

        System.out.println(valueOperations.getAndSet("kkk1", "new"));  //拿出旧值，并替换为新值
        System.out.println(valueOperations.get("kkk1"));
    }

    /**
     * hash串操作
     */
    @PostMapping("/hashOpt")
    public void hashOpt() {
        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        hashOperations.put("hash", "H1", new User(100L, "name0"));
        hashOperations.put("hash", "H2", new User(100L, "name1"));
    }

    /**
     * list操作
     */
    @PostMapping("/listOpt")
    public void listOpt() {
        ListOperations listOperations = this.redisTemplate.opsForList();
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());

//        listOperations.rightPush("list0", new User(100L, "name0"), new User(100L, "name0"));
        listOperations.rightPush("list0", "eee");
        listOperations.rightPushAll("list0", "eee", "lll");
        System.out.println(listOperations.size("list0"));
//        List<String> stringList = listOperations.range("list0", 2L,5L);
        Long count = listOperations.size("list0");
        for (int i = 0; i < count; i++) {
            Object object = listOperations.leftPop("list0");
            System.out.println(object);
        }
    }

    /**
     * 操作复杂对象list
     */
    @PostMapping("/listOptUser")
    public void listOptUser() {
        this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        ListOperations listOperations = this.redisTemplate.opsForList();
        listOperations.rightPushAll("userList",
                new User(100L, "name0"),
                new User(101L, "name0"),
                new User(102L, "name0"),
                new User(102L, "name0"),
                new User(102L, "name0"),
                new User(103L, "name0"));
        long count = listOperations.size("userList");
        for (int i = 0; i < count - 2; i++) {
            User user = (User) listOperations.leftPop("userList");
            System.out.println(user.getId());
            System.out.println(user.getName());
        }
    }

    /**
     * 操作user集合
     */
    @PostMapping("/setOpt")
    public void setOpt() {
        this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        SetOperations opsForSet = this.redisTemplate.opsForSet();
        opsForSet.add("set0", new User(100L, "name0"));
        opsForSet.add("set0", new User(100L, "name1"));

        User[] userArr = {
                new User(100L, "name2"),
                new User(100L, "name3")
        };

        opsForSet.add("set0", userArr);
        long count = opsForSet.size("set0");
        System.out.println(count);
    }

}
