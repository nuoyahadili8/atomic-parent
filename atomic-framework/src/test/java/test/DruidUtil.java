package test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yangxin_ryan
 * 阿里巴巴 Druid 开源组件，实现解析Hive，XXXXDB等（具体参考github文档）数据库SQL
 * dbTableFormat Function -> 判断输入的SQL是否是 DataBase.Table
 * sqlParser Function -> 解析输入的SQL，解析出对应的TableName 以及DataBase
 */
public class DruidUtil {

    private static Log LOG = LogFactory.getLog(DruidUtil.class);

    private static List<SQLStatement> getSQLStatementList(String sql) {
        String dbType = JdbcConstants.HIVE;
        return SQLUtils.parseStatements(sql, dbType);
    }


    /**
     * chinese:检测输入的查询SQL中数据库与表组合形式是否正确
     * english:check the database and tables in search sql
     *
     * @param sql
     * @return
     */
    public static String dbTableFormat(String sql) {
        String message = "";
        SQLStatementParser parser = new HiveStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        HiveSchemaStatVisitor visitor = new HiveSchemaStatVisitor();
        statement.accept(visitor);
        Map<TableStat.Name, TableStat> tableOpt = visitor.getTables();
        LOG.info(tableOpt);
        if (tableOpt.isEmpty())
            return "抱歉，查询的SQL语句中数据库名与表名以下格式 ：\n Database.Table 存在\n ，请修改后再进行查询！";
        for (TableStat.Name key : tableOpt.keySet()) {
            if (!key.toString().contains(".")) {
                message = "抱歉，查询的SQL语句中数据库名与表名以下格式 ：\n Database.Table 存在\n ，请修改后再进行查询！";
                return message;
            }
        }
        return message;
    }

    /**
     * chinese:生成语法树来解析sql中的表，库，操作
     * english:use Grammar tree parser table, database, operation
     *
     * @param sql
     * @return
     */
    public static List<ParserResult> sqlParser(String sql) {
        List<ParserResult> parserResultList = new ArrayList<>();
        SQLStatementParser parser = new HiveStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        HiveSchemaStatVisitor visitor = new HiveSchemaStatVisitor();
        statement.accept(visitor);
        Map<TableStat.Name, TableStat> tableOpt = visitor.getTables();
        for (TableStat.Name key : tableOpt.keySet()) {
            ParserResult parserResult = new ParserResult();
            if (key.toString().contains(".")) {
                LOG.info("Table And DB");
                String dbName = key.toString().split("\\.")[0];
                String tableName = key.toString().split("\\.")[1];
                String operation = visitor.getTableStat(key.toString()).toString();
                parserResult.setDbName(dbName);
                parserResult.setTableName(tableName);
                parserResult.setOperation(operation);
            } else {
                LOG.info("Only Table");
                String tableName = key.toString();
                parserResult.setTableName(tableName);
                parserResult.setDbName("");
                parserResult.setOperation("");
                LOG.info(tableName);
            }
            parserResultList.add(parserResult);
        }
        return parserResultList;
    }


    public static void main(String[] args) {
        String sql ="insert overwrite table PTEMP3.TMP_MID_TERMN_NEW_AUD_INFO_MON_T11\n" +
                "select JOIN_USER_ID\n" +
                "      ,JOIN_PHONE_NO\n" +
                "      ,imei_no\n" +
                "      ,op_date\n" +
                "      ,login_no\n" +
                "      ,agent_login_no\n" +
                "      ,REGION_CODE\n" +
                "      ,REGION_DESC\n" +
                "      ,CITY_CODE\n" +
                "      ,CITY_DESC\n" +
                "      ,CHNL_ID\n" +
                "      ,CHNL_NAME\n" +
                "      ,CHNL_TYPE_NAME\n" +
                "      ,CLASS_NAME1\n" +
                "      ,CLASS_NAME2\n" +
                "      ,CLASS_NAME3\n" +
                "      ,CLASS_NAME4\n" +
                "from (\n" +
                "select t1.user_id as JOIN_USER_ID\n" +
                "       ,t1.RECV_PHONE_NO as JOIN_PHONE_NO\n" +
                "       ,substr(t1.RES_NO,1,14) imei_no\n" +
                "       ,t1.op_date\n" +
                "       ,t1.login_no\n" +
                "       ,t1.agent_login_no\n" +
                "       ,t4.REGION_CODE\n" +
                "       ,t4.REGION_DESC\n" +
                "       ,t4.CITY_CODE\n" +
                "       ,t4.CITY_DESC\n" +
                "       ,t4.CHNL_ID\n" +
                "       ,t4.CHNL_NAME\n" +
                "       ,t4.CHNL_TYPE_NAME\n" +
                "       ,t4.CLASS_NAME1\n" +
                "       ,t4.CLASS_NAME2\n" +
                "       ,t4.CLASS_NAME3\n" +
                "       ,t4.CLASS_NAME4\n" +
                "       ,ROW_NUMBER() OVER (PARTITION BY substr(t1.RES_NO,1,14),t1.user_id,t1.RECV_PHONE_NO,t1.op_date,t1.login_no ORDER BY agent_login_no desc) sn\n" +
                "from PDATA3.TB_EVT_RES_SALE_RCD_DAY t1\n" +
                "inner join (select login_no\n" +
                "             from PMID3.TB_MID_RES_LOGIN_INFO_DAY\n" +
                "             where chnl_second_class='自营实体渠道'\n" +
                "             group by 1\n" +
                "             )t2\n" +
                "on t1.login_no=t2.login_no\n" +
                "inner join (select login_no\n" +
                "             from PMID3.TB_MID_RES_LOGIN_INFO_DAY\n" +
                "             where chnl_third_class='引商入柜'\n" +
                "             group by 1\n" +
                "             )t3\n" +
                "on t1.agent_login_no=t3.login_no\n" +
                "left join (select login_no\n" +
                "             ,REGION_CODE\n" +
                "             ,REGION_DESC\n" +
                "             ,CITY_CODE\n" +
                "             ,CITY_DESC\n" +
                "             ,CHNL_ID\n" +
                "             ,CHNL_NAME\n" +
                "             ,CASE when chnl_second_class in('社会电子渠道','自营电子渠道') or login_no like 'll%' then '电子渠道'\n" +
                "                  when chnl_second_class like '%直销%' THEN '直销渠道'\n" +
                "                  when chnl_second_class in('社会实体渠道','集团代理商渠道') then '社会渠道'\n" +
                "              ELSE '自办渠道' END as  CHNL_TYPE_NAME\n" +
                "             ,chnl_first_class CLASS_NAME1\n" +
                "             ,chnl_second_class CLASS_NAME2\n" +
                "             ,chnl_third_class CLASS_NAME3\n" +
                "             ,chnl_fourth_class CLASS_NAME4\n" +
                "          from PMID3.TB_MID_RES_LOGIN_INFO_MON\n" +
                "          WHERE DEAL_DATE_P=${hivevar:TX_MONTH}\n" +
                "          group by 1,2,3,4,5,6,7,8,9,10,11,12)t4\n" +
                "on t1.agent_login_no=t4.LOGIN_NO\n" +
                "where (t1.ISNT_ACT_ALLOW = '0' or t1.ISNT_ACT_ALLOW is null)\n" +
                "and coalesce(t1.RES_USE,'0') <> '2'\n" +
                "and t1.OP_CODE in ('1147','4976','4035')\n" +
                "and t1.RES_TYPE='Z'\n" +
                "and t1.AGENT_LOGIN_NO <>''\n" +
                "and t1.login_no<>t1.agent_login_no\n" +
                "and t1.deal_date_p<=date_add('${hivevar:TX_DATE_FORMAT}',-1)\n" +
                "group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17) t\n" +
                "where sn=1;";
//        List<ParserResult> list=DruidUtil.sqlParser(sql);

//        for (ParserResult r:list){
//            System.out.println(r.getDbName()+"==="+r.getTableName()+"==="+r.getOperation());
//        }



        System.out.println("SQL语句为：" + sql);
        //格式化输出
//        String result = SQLUtils.format(sql, JdbcConstants.MYSQL);
//        System.out.println("格式化后输出：\n" + result);
        System.out.println("*********************");
        List<SQLStatement> sqlStatementList = getSQLStatementList(sql);
        //默认为一条sql语句
        SQLStatement stmt = sqlStatementList.get(0);
        HiveSchemaStatVisitor visitor = new HiveSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println("数据库类型\t\t" + visitor.getDbType());

        //获取字段名称
        System.out.println("查询的字段\t\t" + visitor.getColumns());

        //获取表名称
        System.out.println("表名\t\t\t\t" + visitor.getTables().keySet());

        System.out.println("条件\t\t\t\t" + visitor.getConditions());

        System.out.println("group by\t\t" + visitor.getGroupByColumns());

        System.out.println("order by\t\t" + visitor.getOrderByColumns());

    }
}
