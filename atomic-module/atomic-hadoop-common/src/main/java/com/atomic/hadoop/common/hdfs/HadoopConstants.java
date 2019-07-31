package com.atomic.hadoop.common.hdfs;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/29/029 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public interface HadoopConstants {


    String PROJECT_NAME_NOT_UNIQUE = "1";

    String PROJECT_NAME_UNIQUE = "0";

    String HIVE_CREDENTIAL_TYPE = "hive2";

    String HIVE_CREDENTIAL_NAME = "hive2_credential";

    String HCATLOG_CREDENTIAL_TYPE = "hcat";

    String HCATLOG_CREDENTIAL_NAME = "hcat_credential";

    String HBASE_CREDENTIAL = "hbase";

    String IS_CRED = "on";

    String FILE_SEPARATOR = "/";

    String FILE_SYSTEM_ROOT_PATH = "file:///";

    String DIR_KEYTAB = "keytab";

    String HADOOP_DEFAULTFS = "fs.defaultFS";

    String HAS_KERBEROS = "1";

    String HADOOP_SECURITY_AUTHENTICATION="hadoop.security.authentication";

    String HADOOP_KERBEROS="kerberos";

    String HADOOP_USER_NAME = "hadoop.user.name";

    String USER_NAME = "user.name";

    String USERNAME_CLIENT_KEYTAB_FILE = "username.client.keytab.file";

    String USERNAME_CLIENT_KEYTAB_PRINCIPAL = "username.client.kerberos.principal";

    String HADOOP_DISABLE_CACHE = "fs.hdfs.impl.disable.cache";

    String HADOOP_USER_NAME_JAVA = "HADOOP_USER_NAME";

    String JAVA_SECURITY_KRB5_CONF_KEY = "java.security.krb5.conf";

    String OOZIE_CRED_HIVE = "hive2";

    String OOZIE_CRED_HCAT = "hcat";

    String OOZIE_CRE_HBASE = "hbase";

    String JOB_ENABLE = "on";

    String JOB_ENABLE_ONE = "1";

    String HADOOP_CONFIGURATION = "HadoopConfiguration";

    String CYCLE_MONTH ="M";

    String CYCLE_DAY = "D";

    String CYCLE_HOUR = "H";

    String CRON_DAY = "0 0 * * ?";

    String CRON_MONTH = "0 0 1 * ?";

    String CRON_HOUR = "0 * * * ?";

}
