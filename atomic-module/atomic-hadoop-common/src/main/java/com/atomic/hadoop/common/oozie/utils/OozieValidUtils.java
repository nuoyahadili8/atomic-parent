package com.atomic.hadoop.common.oozie.utils;

import org.apache.commons.io.FileUtils;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OozieValidUtils {

    public static void main(String[] args) {
        String fileStr = "D:/workflow.xml";
        try {
            // validateCommand(fileStr);
            // String isValid = isValidFile(fileStr);
            String xmlStr = FileUtils.readFileToString(new File(fileStr),"utf8");
            String isValid = isValidXml(xmlStr);
            System.out.println(isValid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(OozieValidUtils.class.getClassLoader().getResource("xsd/wash-action-0.1.xsd").getPath());
        System.out.println(OozieValidUtils.class.getClassLoader().getResourceAsStream("xsd/wash-action-0.1.xsd"));
    }

	/*public static String isValidJson(String oozieJson) {
		String oozieXml = null;
		try {
			oozieXml = XmlJsonUtils.json2xml(oozieJson);
		} catch (Exception e) {
			e.printStackTrace();
			return "Invalid json...... " + oozieJson;
		}
		return isValidXml(oozieXml);
	}*/

    public static String isValidXml(String oozieXml) {
        Reader reader = new StringReader(oozieXml);
        return isValid(reader);
    }

    public static String isValidFile(String oozieXmlfile) {
        File file = new File(oozieXmlfile);
        if (!file.exists()) {
            System.out.println("File does not exists....." + oozieXmlfile);
            return "File does not exists....." + oozieXmlfile;
        }
        Reader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FileNotFound....." + oozieXmlfile);
            return "FileNotFound....." + oozieXmlfile;
        }
        return isValid(reader);
    }

    public static String isValid(Reader reader) {
        try {
            List<StreamSource> sources = new ArrayList<StreamSource>();
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/compare-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/distcp-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/distcp-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/email-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/email-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/es-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ftp-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ftpNew-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/gbase-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive2-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.3.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.4.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.5.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/impala-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-bundle-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-bundle-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.3.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.4.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-sla-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-sla-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.2.5.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.3.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.4.5.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.4.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.5.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sftp-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.3.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/spark-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.3.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.4.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ssh-action-0.1.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ssh-action-0.2.xsd")));
            sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/wash-action-0.1.xsd")));

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(sources.toArray(new StreamSource[sources.size()]));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(reader));
            System.out.println("Valid workflow-app");
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Invalid app definition, " + ex.toString());
            return "Invalid app definition, " + ex.toString();
        }
    }

    private static void validateCommand(String fileStr) throws Exception {
        File file = new File(fileStr);
        if (file.exists()) {
            try {
                List<StreamSource> sources = new ArrayList<StreamSource>();
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/compare-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/distcp-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/distcp-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/email-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/email-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/es-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ftp-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ftpNew-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/gbase-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive2-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.3.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.4.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/hive-action-0.5.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/impala-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-bundle-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-bundle-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.3.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-coordinator-0.4.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-sla-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-sla-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.2.5.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.3.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.4.5.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.4.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/oozie-workflow-0.5.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sftp-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/shell-action-0.3.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/spark-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.3.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/sqoop-action-0.4.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ssh-action-0.1.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/ssh-action-0.2.xsd")));
                sources.add(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/wash-action-0.1.xsd")));
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(sources.toArray(new StreamSource[sources.size()]));
                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(new FileReader(file)));
                System.out.println("Valid workflow-app");
            } catch (Exception ex) {
                throw new Exception("Invalid app definition, " + ex.toString(), ex);
            }
        } else {
            throw new Exception("File does not exists");
        }
    }

}
