package com.atomic.framework.shiro.service;

import com.atomic.common.utils.DateFormatUtil;
import com.atomic.common.utils.UuidUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/20/020 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author Administrator
 */
@Service
@Lazy(false)
public class IdGen implements IdGenerator, SessionIdGenerator {

    private static String counterType = "yMdhmsUuid";


    private static SecureRandom random = new SecureRandom();

    public static String getCounter(String simpleName){
        if ("timestamp".equalsIgnoreCase(counterType)) {
            return Long.toString(System.currentTimeMillis());
        } else if ("yMdhmsS".equalsIgnoreCase(counterType)) {
            return DateFormatUtil.formatShort17(new Date());
        } else if ("uuid".equalsIgnoreCase(counterType)) {
            return UuidUtil.uuid();
        } else if ("timestampUuid".equalsIgnoreCase(counterType)) {
            return Long.toString(System.currentTimeMillis()) + UuidUtil.uuid();
        } else if ("yMdhmsSUuid".equalsIgnoreCase(counterType)) {
            return DateFormatUtil.formatShort17(new Date()) + UuidUtil.uuid();
        } else if ("yMdhmsUuid".equalsIgnoreCase(counterType)) {
            return DateFormatUtil.formatDateShort14(new Date()) + UuidUtil.uuid();
        } else {
            return DateFormatUtil.formatDateShort14(new Date()) + UuidUtil.uuid();
        }
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * Activiti ID 生成
     */
    public String getNextId() {
        return IdGen.uuid();
    }

    @Override
    public Serializable generateId(Session session) {
        return IdGen.uuid();
    }

    public static void main(String[] args) {
        System.out.println(IdGen.getCounter(IdGen.class.getSimpleName()));
        System.out.println(IdGen.uuid());
        System.out.println(IdGen.randomLong());
        System.out.println(IdGen.uuid().length());
        System.out.println(new IdGen().getNextId());
//        for (int i=0; i<1000; i++){
//            System.out.println(IdGen.randomLong() + " <-----> ");
//        }
    }

    @Override
    public UUID generateId() {
        return null;
    }
}
