package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @auther lyd
 * @createDate 2019/2/26 22:15
 */
@Slf4j
public class RedisPool {

    private static JedisPool pool;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return"));

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static Integer redisTimeOut = Integer.parseInt(PropertiesUtil.getProperty("redis.timeout"));
    private static String redisPassword = PropertiesUtil.getProperty("redis.password");

    private static void initPool(){
        System.out.println("---------------连接池配置中-----------");

        // 创建一个redis连接池配置对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);

        // 当连接耗尽时候，是否阻塞。false不进行阻塞，直接抛出异常。true阻塞到超时为止。
        jedisPoolConfig.setBlockWhenExhausted(true);

        pool = new JedisPool(jedisPoolConfig,redisIp,redisPort,redisTimeOut,redisPassword);

        System.out.println("---------------连接池配置结束-----------");

    }

    static {
        log.info("开始初始化redis连接池");
        initPool();
    }

    public static Jedis getJedis() {
         return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }


}
