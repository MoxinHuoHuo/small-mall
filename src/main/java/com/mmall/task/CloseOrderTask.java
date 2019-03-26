package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @auther lyd
 * @createDate 2019/3/24 22:11
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    IOrderService iOrderService;

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务-------start");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time"),2);
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务-------end");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV() {
        log.info("关闭订单定时任务-------start");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long result = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if (result != null || result.intValue() == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("===没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务-------end");
    }

    private void closeOrder(String lockName){
        // expire命令用于给该锁设定一个过期时间，用于防止线程crash，导致锁一直有效，从而导致死锁。
        //有效期50秒,防死锁
        RedisShardedPoolUtil.expire(lockName,50);
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());

        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        // iOrderService.closeOrder(hour);

        //释放锁
        RedisShardedPoolUtil.del(lockName);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("=============================");
    }

}
