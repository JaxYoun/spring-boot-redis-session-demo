package com.youn.springbootredissessiondemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/27 14:48
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
}
