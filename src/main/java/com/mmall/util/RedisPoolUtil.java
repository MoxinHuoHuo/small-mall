package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @auther lyd
 * @createDate 2019/2/27 21:29
 */
@Slf4j
public class RedisPoolUtil {

    public static Long expire(String key, Integer outTime){
        Jedis jedis = null;
        Long result=null;

        try {

            jedis = RedisPool.getJedis();
            result = jedis.expire(key, outTime);

        } catch (Exception e) {

            log.error("expire key:{} value:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;

        }

        RedisPool.returnResource(jedis);

        return result;
    }

    /**
     * @param key
     * @param outTime 单位是s
     * @param value
     * @return
     */
    public static String setEx(String key, Integer outTime, String value){
        Jedis jedis = null;
        String result=null;

        try {

            jedis = RedisPool.getJedis();
            result = jedis.setex(key, outTime, value);

        } catch (Exception e) {

            log.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;

        }

        RedisPool.returnResource(jedis);

        return result;
    }

    public static String set(String key, String value){
        Jedis jedis = null;
        String result=null;

        try {

            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);

        } catch (Exception e) {

            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;

        }

        RedisPool.returnResource(jedis);

        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result=null;

        try {

            jedis = RedisPool.getJedis();
            result = jedis.get(key);

        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result=null;

        try {

            jedis = RedisPool.getJedis();
            result = jedis.del(key);

        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }


    // ------------test------------------------------------------
    public static void main(String[] args) {
        //Jedis jedis = RedisPool.getJedis();

        String result=RedisPoolUtil.set("stuName2","xiaoLi");

        System.out.println("set返回的值:"+result);

        String value = RedisPoolUtil.get("stuName2");

        System.out.println("------"+value);

        result = String.valueOf(RedisPoolUtil.del("stuName2"));

        System.out.println("------"+result);

        RedisPoolUtil.setEx("stuNameTemp",60,"yingZhen");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("设置成功！重新更新时间！");

        RedisPoolUtil.expire("stuNameTemp",70);

        log.info("---end-----------");

    }


}
