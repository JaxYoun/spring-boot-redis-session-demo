package com.youn.springbootredissessiondemo;

import com.youn.springbootredissessiondemo.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisSessionDemoApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void test() throws Exception {
		stringRedisTemplate.opsForValue().set("aaa", "中文");
		Assert.assertEquals("中文", stringRedisTemplate.opsForValue().get("aaa"));
	}

	@Test
	public void systemCacheTest() {
		HashOperations<String, Object, Object> hashOperations = this.redisTemplate.opsForHash();
		hashOperations.put("name", "1", new User(2L, "yang"));
		System.out.println(hashOperations.get("name", "1"));
		hashOperations.delete("name", "1");
		hashOperations.getOperations().expire("name", 20L, TimeUnit.SECONDS);
		Assert.assertEquals(hashOperations.get("name", "1"), new User(2L, "yang"));
	}

	@Test
	public void contextLoads() {
		System.out.println(0);
	}

}
