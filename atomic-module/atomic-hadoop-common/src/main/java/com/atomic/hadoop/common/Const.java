package com.atomic.hadoop.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/2/002 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class Const {

    /*	*//**
     * job 即时运行xml存储key
     *//*
	public  static final String RUNTIME_DATA_DIR_KEY="runtime.job.dat.kuka";

	*//**
     * job 存储物理路径key
     *//*
	public  static final String XML_DATA_DIR_KEY="job.data.dir.kuka";

	*//**
     * job 模板存储物理路径key
     *//*
	public static final String XML_DATA_TEMPLATE_DIR_KEY="job.template.root.kuka";

	*//**
     * flow 存储物理路径key
     *//*
	public  static final String FLOW_DATA_DIR_KEY="flow.data.dir";

	*//**
     * flow 模板存储物理路径key
     *//*
	public static final String FLOW_DATA_TEMPLATE_DIR_KEY="flow.template.root";

	*//**
     * flow 版本备份存储物理路径 key
     *//*
	public static final String FLOW_DATA_BAK_DIR_KEY="flow.bak.root";


	*//**
     * job 修改备份存储物理路径key
     *//*
	public static final String XML_DATA_BAK_DIR_KEY="job.bak.root.kuka";
*/

    /**
     * job 组件面板图标位置 url
     */
    public static final String PLUGIN_ICO_RESOURSE = "plugin.ico.resoures";


    /**
     * flow 组件面板图标位置 url
     */
    public static final String Flow_ICO_RESOURSE = "flow.ico.resoures";


    public static final String LOCALE_KEY = "locale";

    public static final String ZH_CN = "zh";

    public static final String EN_US = "en";


    public static String PLUGIN_RUN_STATUS_NOSTART = "0";
    public static String PLUGIN_RUN_STATUS_RUNNING = "1";
    public static String PLUGIN_RUN_STATUS_DOWN_TRUE = "2";
    public static String PLUGIN_RUN_STATUS_DOWN_FALSE = "3";

    /**
     * Sleep time waiting when buffer is empty
     */
    public static final int TIMEOUT_GET_MILLIS = 50;

    /**
     * Sleep time waiting when buffer is full
     */
    public static final int TIMEOUT_PUT_MILLIS = 50; // luxury problem!

    /**
     * print update every ... lines
     */
    public static final int ROWS_UPDATE = 50000;

    /**
     * Size of rowset: bigger = faster for large amounts of data
     */
    public static final int ROWS_IN_ROWSET = 10000;

    /**
     * Fetch size in rows when querying a database
     */
    public static final int FETCH_SIZE = 5000;

    /**
     * Sort size: how many rows do we sort in memory at once?
     */
    public static final int SORT_SIZE = 5000;

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * PATH:path separator on this operating system
     */
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    /**
     * CR: operating systems specific Cariage Return
     */
    public static final String CR = System.getProperty("line.separator");

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * DOSCR: MS-DOS specific Cariage Return
     */
    public static final String DOSCR = "\r\n";
    /**
     * UNIXCR: UNIX specific Cariage Return
     */
    public static final String UNIXCR = "\n";
    /**
     * An empty ("") String.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The Java runtime version
     */
    public static final String JAVA_VERSION = System.getProperty("java.vm.version");

    public static final int FILE_FORMAT_DOS = 0;
    public static final int FILE_FORMAT_UNIX = 1;
    public static final int FILE_FORMAT_MIXED = 2;
    public static final int FILE_FORMAT_MAC = 2;

    public static final String ISO88591 = "ISO-8859-1";
    public static final String UTF8 = "UTF-8";

    public static final String[] fileFormat = {"DOS", "UNIX", "MIXED", "MAC"};
    public static final String[] booleanExpression = {"1", "0", "TRUE", "FALSE", "Y", "N", "YES", "NO"};

    /**
     * Etl database core 支持的所有类型的数据库类型，对应的实现驱动和实现类
     */
    public static final String[] DataBase_Type_All = {"AS/400", "CACHE", "DB2",
            "DBASE", "DERBY", "EXTENDB", "FIREBIRD", "GENERIC", "GREENPLUM",
            "INFINIDB", "SQLBASE", "H2", "HYPERSONIC", "INFOBRIGHT",
            "INFORMIX", "INGRES", "INTERBASE", "KINGBASEES", "LucidDB",
            "MONETDB", "MSACCESS", "MSSQLNATIVE", "MSSQL", "MYSQL", "NEOVIEW",
            "NETEZZA", "ORACLE", "ORACLERDB", "PALO", "POSTGRESQL",
            "REMEDY-AR-SYSTEM", "SAPDB", "SAPR3", "SQLITE", "SYBASE",
            "SYBASEIQ", "TERADATA", "UNIVERSE", "VERTICA"};

    /**
     * etl database core 支持的数据库类型中主要的数据库类型，在一般版本中使用该数据库支持类型
     * 在以后扩展的EE 企业版本中支持更多类型的数据库驱动和上下，使用DataBase_Type_All
     */
    public static final String[] DataBase_Type_Short = {"ORACLE", "ORACLERDB", "DB2", "MSSQL", "MYSQL", "SYBASE"};

    /**
     * 数据库的访问类型，分为四种
     * Native 原生的jdbc 的连接方式
     * ODBC 在windows系统上面通过ODBC数据源桥接
     * OCI 通过驱动的本地OCI进行连接
     * JNDI，通过JNDI数据源进行连接
     */
    public static final String[] DataBase_Access_Type = {"Native", "ODBC", "OCI", "JNDI"};

    public static final String LINENUM_MODE_LINE = "Line";
    public static final String LINENUM_MODE_WC = "WC";

    /**
     * 根据字符串返回对应的格式编码：DOS:0, UNIX:1, MIXED:2, MAC:2。若未适配到则返回默认值0
     *
     * @param format
     * @return
     */
    public static Integer getFileFormat(String format) {
        if (fileFormat[0].equalsIgnoreCase(format)) {
            return FILE_FORMAT_DOS;
        } else if (fileFormat[1].equalsIgnoreCase(format)) {
            return FILE_FORMAT_UNIX;
        } else if (fileFormat[2].equalsIgnoreCase(format)) {
            return FILE_FORMAT_MIXED;
        } else if (fileFormat[3].equalsIgnoreCase(format)) {
            return FILE_FORMAT_MAC;
        } else {
            return FILE_FORMAT_DOS;
        }
    }

    /**
     * 判断一个list是否是 null，或者 size 等于 0
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 添加list2中的元素到list1中去，返回list1和list2的所有元素的list
     *
     * @param list1
     * @param list2
     * @return
     */
    public static List addAll(List list1, List list2) {
        List alllist = list1;
        for (Object object : list2) {
            alllist.add(object);
        }
        return alllist;
    }

    /**
     * get sublist
     *
     * @param list
     * @param length
     * @return
     */
    public static List sublist(List list, int length) {
        if (isEmpty(list)) {
            return list;
        }
        if (list.size() <= length) {
            return list;
        }
        return list.subList(0, length);
    }

    /**
     * 添加 array1,arrya2 到一个新的数组中去，返回的是包含array1，和array2 的新数组
     *
     * @param array1
     * @param array2
     * @return
     */
    public static byte[] addAll(byte[] array1, byte[] array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    /**
     * 添加一个byte到新的数组中去，返回一个新的数组
     *
     * @param array1
     * @param element
     * @return
     */
    public static byte[] add(byte[] array1, byte element) {
        return ArrayUtils.add(array1, element);
    }


    public static String[] addAll(String[] array1, String[] array2) {
        return (String[]) ArrayUtils.addAll(array1, array2);
    }


    /**
     * 截取一个数组
     *
     * @param array
     * @param startIndex 数组开始位置
     * @param endIndex   数组结束位置
     * @return
     */
    public static byte[] subarray(byte[] array, int startIndex, int endIndex) {
        return ArrayUtils.subarray(array, startIndex, endIndex);
    }

    /**
     * 检查数字中是否包含这个字节
     *
     * @param array
     * @param valueToFind
     * @return
     */
    public static boolean contains(byte[] array, byte valueToFind) {
        return ArrayUtils.contains(array, valueToFind);
    }

    /**
     * test jdbc implete interface isclose method
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static boolean hasIsCloseImpl(ResultSet rs) throws SQLException {
        try {
            rs.isClosed();
        } catch (AbstractMethodError e) {
            return false;
        }
        return true;
    }

    /**
     * @param hexstr
     * @return
     */
    public static String ParseHexStrToShort(String hexstr) {
        if (hexstr.indexOf('x') > -1) {
            return hexstr.substring(hexstr.indexOf('x') + 1);
        } else {
            if (NumberUtils.isDigits(hexstr)) {
                return StringUtils.leftPad(String.valueOf(NumberUtils.toInt(hexstr)), 2, '0');
            }
        }
        return hexstr;

    }

    /**
     * determine the OS name
     *
     * @return The name of the OS
     */
    public static final String getOS() {
        return System.getProperty("os.name");
    }

    /**
     * Implements Oracle style NVL function
     *
     * @param source The source argument
     * @param def    The default value in case source is null or the length of the string is 0
     * @return source if source is not null, otherwise return def
     */
    public static final String NVL(String source, String def) {
        if (source == null || source.length() == 0) {
            return def;
        }
        return source;
    }
}
