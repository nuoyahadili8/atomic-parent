package com.atomic.hadoop.common.oozie.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.oozie.client.CoordinatorAction;
import org.apache.oozie.client.CoordinatorAction.Status;
import org.apache.oozie.client.CoordinatorJob;

import java.util.Date;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/2/002 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Slf4j
@Data
public class CordActionVo {

    private int actionNumber;
    private String consoleUrl;
    private String createdConf;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String errorCode;
    private String errorMessage;
    private String externalId;
    private String externalStatus;
    private String id;
    private String jobId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedTime;
    private String missingDependencies;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date nominalTime;
    private String pushMissingDependencies;
    private String runConf;
    private Status status;
    private String trackerUri;
    private String appName;

    public CordActionVo(CoordinatorAction action) {
        this.actionNumber = action.getActionNumber();
        this.consoleUrl = action.getConsoleUrl();
        this.createdConf = action.getCreatedConf();
        this.createdTime = action.getCreatedTime();
        this.errorCode = action.getErrorCode();
        this.errorMessage = action.getErrorMessage();

        this.externalId = action.getExternalId();
        this.externalStatus = action.getExternalStatus();
        this.id = action.getId();
        this.jobId = action.getJobId();
        this.lastModifiedTime = action.getLastModifiedTime();
        this.missingDependencies = action.getMissingDependencies();
        this.nominalTime = action.getNominalTime();
        this.pushMissingDependencies = action.getPushMissingDependencies();
        this.runConf = action.getRunConf();

        this.status = action.getStatus();
        this.trackerUri = action.getTrackerUri();

    }

    public CordActionVo(CoordinatorAction action, CoordinatorJob coordinatorJob){
        this.actionNumber = action.getActionNumber();
        this.consoleUrl = action.getConsoleUrl();
        this.createdConf = action.getCreatedConf();
        this.createdTime = action.getCreatedTime();
        this.errorCode = action.getErrorCode();
        this.errorMessage = action.getErrorMessage();
        this.externalId = action.getExternalId();
        this.externalStatus = action.getExternalStatus();
        this.id = action.getId();
        this.jobId = action.getJobId();
        this.lastModifiedTime = action.getLastModifiedTime();
        this.missingDependencies = action.getMissingDependencies();
        this.nominalTime = action.getNominalTime();
        this.pushMissingDependencies = action.getPushMissingDependencies();
        this.runConf = action.getRunConf();
        this.status = action.getStatus();
        this.trackerUri = action.getTrackerUri();
        this.appName = coordinatorJob.getAppName();
    }
}
