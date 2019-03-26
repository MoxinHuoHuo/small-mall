package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
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

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务-------start");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time"),2);
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务-------end");
    }

}
