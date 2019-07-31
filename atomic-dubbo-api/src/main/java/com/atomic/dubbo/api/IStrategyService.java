package com.atomic.dubbo.api;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/24/024 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface IStrategyService {

    /**
     * 暂停任务
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int pauseOozieStrategy(Long oozieStrategyId) throws Exception;

    /**
     * 恢复任务
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int resumeOozieStrategy(Long oozieStrategyId) throws Exception;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int deleteOozieStrategy(Long oozieStrategyId) throws Exception;

    /**
     * 批量删除策略通道
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    void deleteOozieStrategyByIds(String ids) throws Exception;

    /**
     * 策略通道状态修改
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int changeStatus(Long oozieStrategyId) throws Exception;

    /**
     * 立即运行策略通道
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    void run(Long oozieStrategyId) throws Exception;

    /**
     * 新增策略通道表达式
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int insertOozieStrategyCron(Long oozieStrategyId) throws Exception;

    /**
     * 更新策略通道的时间表达式
     *
     * @param oozieStrategyId 策略通道
     * @return 结果
     */
    int updateOozieStrategyCron(Long oozieStrategyId) throws Exception;

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    boolean checkCronExpressionIsValid(String cronExpression) throws Exception;
}
