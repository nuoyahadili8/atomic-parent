package test;

import org.apache.commons.compress.utils.CharsetNames;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/30/030 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class XmlGet {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://192.168.20.203:3306/oozie?user=root&password=cloudera");
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select conf from WF_JOBS where app_name = 'antest' and id='0001930-190718153349907-oozie-oozi-W' ");
        while(rs.next()){
            Blob blob = rs.getBlob(1);
            byte[] b = blob.getBytes(1, (int)blob.length());
            String result = new String(b,CharsetNames.UTF_8);
            System.out.println(result);
        }
        conn.close();
        stmt.close();
    }
}
