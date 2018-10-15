package com.youn.springbootredissessiondemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Yang
 * @date: 2018/10/15 22:57
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/zset")
public class ZSetController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/zaddElement")
    private void zaddElement() {
        ZSetOperations<String, String> zSetOpt = this.stringRedisTemplate.opsForZSet();
        zSetOpt.add("zaddElement", "one", 1D);
        zSetOpt.add("zaddElement", "two", 2D);
        zSetOpt.add("zaddElement", "three", 3D);

        zSetOpt.getOperations().
    }

    @GetMapping("/zaddTypeSet")
    private void zaddTypeSet() {
        ZSetOperations<String, String> zSetOpt = this.stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        tupleSet.add(new ZSetOperations.TypedTuple<String>() {
            @Override
            public String getValue() {
                return "ten";
            }

            @Override
            public Double getScore() {
                return 10D;
            }

            @Override
            public int compareTo(ZSetOperations.TypedTuple<String> o) {
                return this.getScore().compareTo(o.getScore());
            }
        });

        zSetOpt.add("NO", tupleSet);
    }

}
