package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther lyd
 * @createDate 2019/3/14 22:34
 */
@Slf4j
public class RedisShardedPool {
    // 分布式Jedis连接池
    private static ShardedJedisPool pool;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return"));

    private static String  redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String  redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static Integer redisTimeOut = Integer.parseInt(PropertiesUtil.getProperty("redis.timeout"));
    private static String  redisPassword1 = PropertiesUtil.getProperty("redis1.password");
    private static String  redisPassword2 = PropertiesUtil.getProperty("redis2.password");

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

        // 每个redis服务器的配置信息
        // pool = new JedisPool(jedisPoolConfig,redisIp,redisPort,redisTimeOut,redisPassword);
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis1Ip,redis1Port,redisTimeOut);
        jedisShardInfo1.setPassword(redisPassword1);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis2Ip,redis2Port,redisTimeOut);
        jedisShardInfo2.setPassword(redisPassword2);

        // 用集合进行打包
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(Integer.parseInt(PropertiesUtil.getProperty("redis_count")));
        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);

        // 创建分布式的redis客户端
        // Hashing.MURMUR_HASH  采用consistent Hashing一致性算法
        pool = new ShardedJedisPool(jedisPoolConfig, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

        System.out.println("---------------连接池配置结束-----------");

    }

    static {
        log.info("开始初始化redis连接池");
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

        for (int i = 0; i < 10; i++) {
            jedis.set("key"+i, "value"+i);
        }
        returnResource(jedis);
        pool.destroy();
        System.out.println("program is end!");
    }
}
