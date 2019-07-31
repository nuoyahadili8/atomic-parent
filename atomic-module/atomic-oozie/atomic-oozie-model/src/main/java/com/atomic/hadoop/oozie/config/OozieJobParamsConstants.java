package com.atomic.hadoop.oozie.config;

import java.util.Properties;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/5/28/028 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class OozieJobParamsConstants {

    public static Properties getDefaultsParams(){
        Properties params = new Properties();
        params.setProperty("TX_MINUTE","${coord:formatTime(coord:dateOffset(coord:nominalTime(), 0, 'MINUTE'), 'mm')}");
        params.setProperty("TX_HOUR","${coord:formatTime(coord:dateOffset(coord:nominalTime(), 0, 'HOUR'), 'HH')}");
        params.setProperty("CYCLE_DATE","${coord:formatTime(coord:dateOffset(coord:nominalTime(), -400, 'DAY'), 'yyyyMMdd')}");
        params.setProperty("CYCLE_DATE_FORMAT","${coord:formatTime(coord:dateOffset(coord:nominalTime(), -400, 'DAY'), 'yyyy-MM-dd')}");
        params.setProperty("TX_DATE","${coord:formatTime(coord:dateOffset(coord:nominalTime(), 0, 'DAY'), 'yyyyMMdd')}");
        params.setProperty("TX_DATE_FORMAT","${coord:formatTime(coord:dateOffset(coord:nominalTime(), 0, 'DAY'), 'yyyy-MM-dd')}");
        params.setProperty("CYCLE_MONTH","${coord:formatTime(coord:dateOffset(coord:nominalTime(), -13, 'MONTH'), 'yyyyMM')}");
        params.setProperty("TH_MONTH","${coord:formatTime(coord:dateOffset(coord:nominalTime(), 0, 'MONTH'), 'yyyyMM')}");
        params.setProperty("TX_MONTH","${coord:formatTime(coord:dateOffset(coord:nominalTime(), -1, 'MONTH'), 'yyyyMM')}");
        return params;
    }
}
