package com.atomic.hadoop.common.hdfs;

import com.atomic.common.utils.DateFormatUtil;
import com.atomic.hadoop.common.Const;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.exec.DefaultExecutor;
import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class FileUtil {

    public static final String SUFFIX_TEMP = ".tmp";
    public static final String SUFFIX_TXT = ".txt";
    public static final String SUFFIX_LOG = ".log";
    public static final String SUFFIX_BAD = ".bad";
    public static final String SUFFIX_MD5 = ".md5";
    public static final String SUFFIX_SPLITVOL_VAR = ".${count}";
    public static final String SUFFIX_SEQUENCE_VAR = "${sequence:3}";
    public static final String SUFFIX_SEQUENCE_VAR_START = "${sequence:";
    private static final String mather = "\\[.+\\]";
    private static final Pattern Wilmather = Pattern.compile(mather);
    private static String FILE_SEPARATOR = "/";

    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     *	Gets the name minus the path from a full filename.
     *	This method will handle a file in either Unix or Windows format. The text after the last forward or backslash is returned.
     *
     *	 a/b/c.txt --> c.txt
     *	 a.txt     --> a.txt
     *	 a/b/c     --> c
     *	 a/b/c/    --> ""
     *
     * @param filename
     * @return
     */
    public static String getName(String filename){
        return FilenameUtils.getName(filename);
    }

    public static String dirCompletion(String dir) {
        if (null == dir) {
            return null;
        } else {
            if (dir.endsWith(File.separator)) {
                return dir;
            } else {
                return dir + File.separator;
            }
        }
    }

    /**
     * 移动一个文件到目标文件，目标文件不能是目录
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     *             如果目标文件已存在
     */
    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (destFile != null && !destFile.getParentFile().exists()) {
            try {
                destFile.getParentFile().mkdirs();
            } catch (Exception e) {
                log.error("mkdir exception:",e);
                log.info("mkdir error, and do retry.");
                destFile.getParentFile().mkdirs();
            }
        }
        FileUtils.moveFile(srcFile, destFile);
    }

    /**
     * 移动一个文件到目标文件，目标文件不能是目录。如果目录文件已存在，则先删除
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void moveAndDeleteFile(File srcFile, File destFile) throws IOException {
        if (destFile.exists()) {
            if (!destFile.delete()) {
                throw new IOException("The file cann't be deleted.");
            }
        }
        FileUtils.moveFile(srcFile, destFile);
    }


    /**
     * 重命名srcfile 到目标 file
     * @param srcfile
     * @param destFile
     * @throws IOException
     */
    public static boolean renameFileAndDeleteFile(File srcfile,File destFile) throws IOException{
        if (destFile.exists()) {
            if (!destFile.delete()) {
                throw new IOException("The file cann't be deleted.");
            }
        }
        return srcfile.renameTo(destFile);
    }

    /**
     * 移动一个文件到目标文件，目标文件不能是目录
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void moveFileAndmkParent(File srcFile, File destFile) throws IOException {
        try {
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();
        } catch (Exception e) {
            log.error("mk parent dir exception:",e);
            log.info("mk parent dir, and do retry.");
            destFile.getParentFile().mkdirs();
        }
        FileUtil.moveAndDeleteFile(srcFile,destFile);
        //FileUtils.moveFile(srcFile, destFile);
    }

    /**
     * 移动一个文件夹到目录文件夹
     *
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
    public static void moveToDirectory(File srcDir, File destDir) throws IOException {
        FileUtils.moveToDirectory(srcDir, destDir, false);
    }
    /**
     * 移动一个文件到目录文件夹，若该文件存在，则先删除
     *
     * @param srcDir
     * @param destDir
     * @param destFile
     * @throws IOException
     */
    public static void moveToDirectoryDeleteFile(File srcDir, File destDir,File destFile) throws IOException {
        if(destFile.isFile()&&destFile.exists()){
            if(destFile.delete()){
                FileUtils.moveToDirectory(srcDir, destDir, false);
            }
        }else{
            FileUtils.moveToDirectory(srcDir, destDir, false);
        }
    }

    /**
     * 复制一个文件到目标文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * 复制一个文件到目标文件,把异常catch掉，如果发生异常返回 false，否则返回 ture
     *
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean copyFileResult(File srcFile, File destFile) {
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复制一个文件到目标文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFileAndmkParent(File srcFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        FileUtils.copyFile(srcFile, destFile);
    }


    /**
     * 复制一个文件到指定目录中去
     *
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
    public static void copyFileToDirectory(File srcDir, File destDir) throws IOException {
        FileUtils.copyFileToDirectory(srcDir, destDir);
    }

    /**
     * 复制文件夹到指定目录中去
     *
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        // FileUtils.copyDirectory(srcDir, destDir);
        FileUtils.copyDirectoryToDirectory(srcDir, destDir);
    }

    /**
     * 在指定的目录下创建临时文件，非系统临时目录
     *
     * @param dir
     * @param name
     * @return
     * @throws IOException
     */
    public static synchronized File createTempFile(String dir, String name) throws IOException {
        File tmpfile = null;
        if (checkDirAndCreate(dir)) {
            tmpfile = concatDirAndName(dir, name + "_" + DateFormatUtil.formatShort17(new Date()) + SUFFIX_TEMP);
            tmpfile.createNewFile();
        }
        return tmpfile;
    }

    /**
     * 打开一个文件输入流
     * @param file
     * @return
     * @throws IOException
     */
    public static FileInputStream getFileInputStrean(File file) throws IOException{
        return FileUtils.openInputStream(file);
    }
    /**
     * 打开一个文件的输出流
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static FileOutputStream getFileOutputStream(File file) throws IOException {
        return FileUtils.openOutputStream(file);
    }

    /**
     * 读取一个文件的内容，到string里面，适应于读取内容比较少的配置文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileContent(File file) throws IOException {
        return FileUtils.readFileToString(file, Const.UTF8);
    }

    /**
     * 读取一个文件的内容，到string里面，适应于读取内容比较少的配置文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileContent(File file, String encode) throws IOException {
        return FileUtils.readFileToString(file, encode);
    }
    /**
     * 读取一个文件内容，按行读取
     * @param file
     * @param encode
     * @return
     * @throws IOException
     */
    public static List<String> getFileConntent(File file, String encode) throws IOException{
        return FileUtils.readLines(file, encode);
    }
    /**
     * 读取InputStream，到string里面
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static String getInputContent(InputStream input) throws IOException {
        return IOUtils.toString(input, Const.UTF8);
    }

    /**
     * 读取InputStream，到string里面
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static String getInputContent(InputStream input, String encoding) throws IOException {
        return IOUtils.toString(input, encoding);
    }

    /**
     * 写比较少的内容到一个文件中去，例如写md5 校检码
     *
     * @param file
     * @param str
     * @throws IOException
     */
    public static void WriteContentToFile(File file, String str) throws IOException {
        if (!file.exists())
            file.createNewFile();
        FileUtils.writeStringToFile(file, str);
    }

    /**
     * 写比较少的内容到一个文件中去，带文件编码
     *
     * @param file
     * @param encodeing
     * @throws IOException
     */
    public static void WriteContentToFile(File file, String str,String encodeing) throws IOException {
        if (!file.exists())
            file.createNewFile();
        FileUtils.writeStringToFile(file, str,encodeing);
    }

    public static void WriteContentToFileUTF8(File file, String str) throws IOException {
        if (!file.exists())
            file.createNewFile();
        FileUtils.writeStringToFile(file, str, Const.UTF8);
    }

    /**
     * 检查一个目录路径是否存在，是否是目录
     *
     * @param dirstr
     * @return
     */
    public static boolean isDirExsit(String dirstr) {
        File file = new File(dirstr);
        if (!file.exists())
            return false;
        if (!file.isDirectory())
            return false;
        return true;
    }

    /**
     * 检查一个文件是否存在，是否是目录
     *
     * @param dirstr
     * @return
     */
    public static boolean isFileExsit(String dirstr) {
        File file = new File(dirstr);
        if (!file.exists())
            return false;
        if (!file.isFile())
            return false;
        return true;
    }

    /**
     * 检查这个目录是否存在，如果不存在创建该目录并返回是否已经创建
     *
     * @param dirstr
     * @return
     */
    public static boolean checkDirAndCreate(String dirstr) {
        File dirfile = new File(dirstr);
        if (!dirfile.exists())
            dirfile.mkdirs();
        return isDirExsit(dirstr);
    }

    /**
     * 给文件名后面添加17为 到毫秒的字符串，产生唯一名称
     *
     * @param file
     * @return
     */
    public static File CreateUniqeNameFile(File file) {
        String Extension = getExtension(file.getName());
        File newfile = new File(FilenameUtils.removeExtension(file.getPath()) + "_" + DateFormatUtil.formatShort17(new Date()) + "." + Extension);
        return newfile;
    }

    /**
     * 给文件后加上上17位到毫秒的字串，产生唯一名称
     *
     * @param filename
     * @return
     */
    public static String CreateUniqeName(String filename) {
        String Extension = getExtension(filename);
        String newname = getFileBaseName(filename) + "_" + DateFormatUtil.formatShort17(new Date()) + "." + Extension;
        return newname;
    }

    /**
     * 在基本文件名中追加一个字串 eg：test.dat , 001 ----->test001.dat
     *
     * @param filename
     * @param str
     * @return
     */
    public static String InsertStrToBaseName(String filename, String str) {
        String Extension = getExtension(filename);
        String newname = getFileBaseName(filename) + "_" + str + "." + Extension;
        return newname;
    }


    /**
     * 连接字符不不适用"_"进行连接
     * @param filename
     * @param str
     * @return
     */
    public static String InsertStrToBaseNames(String filename, String str) {
        String Extension = getExtension(filename);
        String newname = getFileBaseName(filename) + "" + str + "." + Extension;
        return newname;
    }

    /**
     * 替换文件的扩展名为传入的扩展名 eg: file : /etl/xx.dat 替换 .md5扩展名后/etl/xx.md5
     *
     * @param file
     * @param newextension
     * @return
     */
    public static File ReplaceExtension(File file, String newextension) {
        File newfile = new File(FilenameUtils.removeExtension(file.getPath()) + newextension);
        return newfile;
    }

    /**
     *
     * 创建目录，如果不能创建返回false，创建成功后检查是否可用
     *
     * @param dirstr
     * @return
     */
    public static boolean CreateDir(String dirstr) {
        File dirfile = new File(dirstr);
        dirfile.mkdirs();
        return isDirExsit(dirstr);
    }

    /**
     * 检查一个文件是否存在，是否是文件，是否可读和写
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExistAndCanRW(String filepath) {
        File file = new File(filepath);
        if (!file.exists())
            return false;
        if (!file.isFile())
            return false;
        if (!file.canRead())
            return false;
//		if (!file.canWrite())
//			return false;
        return true;
    }

    /**
     * 检查一个文件是否存在，是否是文件，是否可读
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExistAndRead(String filepath) {
        File file = new File(filepath);
        if (!file.exists())
            return false;
        if (!file.isFile())
            return false;
        if (!file.canRead())
            return false;
        return true;
    }

    /**
     * 递归的删除这个目录下的所有子目录
     *
     * @param directory
     * @throws IOException
     */
    public static void deleteDirectory(File directory) throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    /**
     *
     * 删除文件，如果是目录包括删除这个目录下的子目录
     *
     * @param file
     * @return
     */
    public static boolean deleteQuietly(File file) {
        return FileUtils.deleteQuietly(file);
    }

    /**
     * 去掉文件名的后缀，前缀返回基本的文件名 eg： xxx.xml return : xxx /rt/ttt/test.zip return: test
     *
     * @param filename
     * @return
     */
    public static String getFileBaseName(String filename) {
        return FilenameUtils.getBaseName(filename);
    }

    /**
     * 检查文件名的后缀 eg : 检查test.gz,后缀是否是 gz，返回 true
     *
     * @param filename
     * @param extension
     * @param iscase
     *            是否区分大小写，true 区分，false 不区分
     * @return
     */
    public static boolean isExtension(String filename, String extension, boolean iscase) {
        if (iscase){
            return FilenameUtils.isExtension(filename, extension);
        }
        else{
            return FilenameUtils.isExtension(filename, extension.toLowerCase()) || FilenameUtils.isExtension(filename, extension.toUpperCase());
        }
    }

    /**
     * 获得文件的扩展名 eg： foo.txt --> "txt" a/b/c.jpg --> "jpg" a/b.txt/c --> "" a/b/c --> ""
     *
     * @param filename
     * @return
     */
    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * 连接路径和文件名称，并生产file 对象返回 /foo/ + bar --> /foo/bar /foo + bar --> /foo/bar /foo + /bar --> /bar /foo + C:/bar --> C:/bar /foo + C:bar --> C:bar (*) /foo/a/ + ../bar --> foo/bar /foo/ + ../../bar
     * --> null /foo/ + /bar --> /bar /foo/.. + /bar --> /bar /foo + bar/c.txt --> /foo/bar/c.txt /foo/c.txt + bar --> /foo/c.txt/bar (!)
     *
     * @param dir
     * @param filename
     * @return
     */
    public static File concatDirAndName(String dir, String filename) {
        return new File(FilenameUtils.concat(dir, filename));
    }

    /**
     * 连接文件名或者文件路径
     *
     * @param dir
     * @param subdir
     * @return
     */
    public static String concatPath(String dir, String subdir) {
        return FilenameUtils.concat(dir, subdir);
    }

    /**
     * 替换文件路径中的basepath到newpath，并返替换后的文件对象
     *
     * @param basepath
     * @param newpath
     * @param file
     * @return
     */
    public static File ReplaceBasePath(String basepath, String newpath, File file) {
        if (StringUtil.isEmpty(newpath) || file == null){
            return file;
        }
        if (StringUtil.isEmpty(basepath)){
            basepath = file.getParent();
        }
        String filepath = normalizePath(file.getPath());
        basepath = normalizePath(basepath);
        newpath = normalizePath(newpath);
        String RelativePath = StringUtils.removeStart(filepath, basepath);
        if (RelativePath.startsWith(FILE_SEPARATOR)){
            RelativePath = RelativePath.substring(1);
        }
        return concatDirAndName(newpath, RelativePath);
    }


    /**
     * 从指定的目录里面根据wildcardStr获得所有的适配文件，可以指定是否适配所有的子目录
     *
     * @param dir
     * @param wildcardStr
     * @param includesubdir
     * @return
     */
    public static List<File> FileListForWildcardFileFilter(File dir, String wildcardStr, boolean includesubdir) {
        List<File> filelist = new ArrayList<File>();
        if (!dir.isDirectory()){
            return filelist;
        }
        IOFileFilter fileFilter = null;
        IOFileFilter dirFilter = null;
        try {
            fileFilter = new WildcardFileFilter(getParseWildcard(wildcardStr));
            if (includesubdir){
                dirFilter = new WildcardFileFilter("*");
            }
            else {
                dirFilter = new WildcardFileFilter(".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<File> iterator = FileUtils.iterateFiles(dir, fileFilter, dirFilter);
        for (Iterator<File> iterator2 = iterator; iterator2.hasNext();) {
            File tmpfiles = iterator2.next();
            filelist.add(tmpfiles);
        }
        return filelist;
    }

    /**
     * 检查文件是否匹配 wildcardMatcher，匹配返回 true ，不匹配返回 false
     *
     * @param filename
     * @param wildcardMatcher
     *            需要对 [ABC]的类型进行解析,windcahrd 是空的返回 false，不能适配
     * @return
     */
    public static boolean wildcardMatchForFileName(String filename, String wildcardMatcher) {
        if (StringUtil.isEmpty(wildcardMatcher)){
            return false;
        }
        String[] wildcards = getParseWildcard(wildcardMatcher);
        for (String string : wildcards) {
            if (FilenameUtils.wildcardMatch(filename, string)){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据正则表达式检查文件名是否符合，符合返回true，不符合返回 false
     *
     * @param filename
     * @param regstr
     *            regstr 是空的 返回 false ，不能适配
     * @return
     */
    public static boolean RegexMatchForFileName(String filename, String regstr) {
        Pattern pattern = null;
        if (!StringUtil.isEmpty(regstr)){
            pattern = Pattern.compile(regstr);
        }
        else{
            pattern = null;
        }
        if (pattern != null){
            return pattern.matcher(filename).matches();
        }
        return false;
    }

    /**
     * 从指定的目录里面根据正则表达式获得所有的适配文件，可以指定是否适配所有的子目录
     *
     * @param dir
     * @param RegStr
     * @param includesubdir
     * @return
     */
    public static List<File> FileListForRegexFileFilter(File dir, String RegStr, boolean includesubdir) {
        List<File> filelist = new ArrayList<File>();
        if (!dir.isDirectory())
            return filelist;
        IOFileFilter fileFilter = null;
        IOFileFilter dirFilter = null;
        try {
            fileFilter = new RegexFileFilter(RegStr);
            if (includesubdir){
                dirFilter = new WildcardFileFilter("*");
            }
            else{
                dirFilter = new WildcardFileFilter(".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<File> iterator = FileUtils.iterateFiles(dir, fileFilter, dirFilter);
        for (Iterator<File> iterator2 = iterator; iterator2.hasNext();) {
            File tmpfiles = iterator2.next();
            filelist.add(tmpfiles);
        }
        return filelist;
    }

    /**
     * 从指定的目录里面根据适配表达式获得文件组，不包括子目录 The wildcard matcher uses the characters '?' and '*' to represent a single or multiple wildcard characters. This is the same as often found on Dos/Unix command
     * lines. The extension check is case-sensitive by
     *
     * @param dir
     * @param wildcardStr
     * @return
     */
    public static File[] FileArrayForWildcardFileFilter(File dir, String wildcardStr) {
        FileFilter fileFilter = new WildcardFileFilter(getParseWildcard(wildcardStr));
        File[] files = dir.listFiles(fileFilter);
        return files;
    }

    /**
     * 从指定的目录里面根据正则表达式获得文件组，不适配子目录
     *
     * @param dir
     * @param wildcardStr
     * @return
     */
    public static File[] FileArrayForRegexFileFilter(File dir, String wildcardStr) {
        FileFilter fileFilter = new RegexFileFilter(wildcardStr);
        File[] files = dir.listFiles(fileFilter);
        return files;
    }

    /**
     * 获得该目录下所有的子目录名称
     *
     * @param dir
     * @param wildcardStr
     * @return
     */
    public static String[] FileArrayForDirectoryFileFilter(File dir, String wildcardStr) {
        String[] files = dir.list(DirectoryFileFilter.INSTANCE);
        return files;
    }

    /**
     * 解析 wildcardStr 穿，生成匹配模式的文件过滤去器
     *
     * @param wildcardStr
     * @return
     */
    public static WildcardFileFilter getWildcardFileFilter(String wildcardStr) {
        WildcardFileFilter fileFilter = new WildcardFileFilter(getParseWildcard(wildcardStr));
        return fileFilter;
    }

    /**
     * 生成一个文件的 md5 码
     *
     * @param file
     * @return
     */
    public static String getFileMd5(File file) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            InputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }
            fis.close();
            // System.out.println("end create md5---->"+file.getName());
            return bufferToHex(messagedigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param bytes
     * @return
     */
    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    /**
     * @param bytes
     * @param m
     * @param n
     * @return
     */
    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    /**
     * @param bt
     * @param stringbuffer
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 生成正则表达式的文件过滤器
     *
     * @param RegStr
     * @return
     */
    public static RegexFileFilter getRegexFileFilter(String RegStr) {
        RegexFileFilter fileFilter = new RegexFileFilter(RegStr);
        return fileFilter;
    }

    /**
     * input param WL[IDO]????????[PRO]551.* out String [] array,eg.WLI????????551.* WLD????????551.* WLO????????551.* parseString
     *
     * @param str
     * @return
     */
    private static String[] getParseWildcard(String str) {
        List<String> morelist = new ArrayList<String>();
        parseString(str, morelist);
        String[] strs = new String[morelist.size()];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = morelist.get(i);
        }
        return strs;
    }

    /**
     *
     * @param str
     * @param morelist
     */
    private static void parseString(String str, List<String> morelist) {
        String[] srcs = null;
        Matcher matcher = Wilmather.matcher(str);
        if (!matcher.find()) {
            morelist.add(str);
        } else {
            StringBuffer buffer = new StringBuffer(str);
            int start = buffer.indexOf("[");
            int end = buffer.indexOf("]");
            String mat = buffer.substring(start, end + 1);
            char[] morestr = buffer.substring(start + 1, end).toCharArray();
            srcs = new String[morestr.length];
            for (int i = 0; i < srcs.length; i++) {
                parseString(str.replace(mat, String.valueOf(morestr[i])), morelist);
            }
        }
    }

    public static String normalizePath(String path) {

        String normalizedPath = path.replaceAll("\\\\", FILE_SEPARATOR);
        while (normalizedPath.endsWith("\\") || normalizedPath.endsWith(FILE_SEPARATOR)) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath;
    }

    /**
     * 替换 ftp 路径中的 \ 到 /
     *
     * @param path
     * @return
     */
    public static String ftpPath(String path) {
        String ftppath = StringUtils.replaceChars(path, '\\', '/');// path.replaceAll(FILE_SEPARATOR,"\\\\");
        return ftppath;
    }

    /**
     * chmod tmpscript file access 
     * @param tempFile
     * @throws Exception
     */
    public static void makeFileExecutable(File tempFile) throws Exception
    {
        if (isFileExistAndCanRW(tempFile.getPath())) {
            if (Const.getOS().startsWith("Windows")) {
                return;
            }
            try {
                String tempFilename =tempFile.getPath();
                // Now we have to make this file executable...
                // On Unix-like systems this is done using the command "/bin/chmod +x filename"
                DefaultExecutor executor=new DefaultExecutor();
                CommandLine command=new CommandLine("chmod");
                command.addArgument("+x");
                command.addArgument(tempFilename);
                executor.execute(command);
            }
            catch(Exception e) {
                throw new Exception("Unable to make file:" + tempFile.getPath() + " to execute script", e);
            }
        }
    }

    /**
     * 通过读取文件内容获取文件行数
     * @param file
     * @return
     * @throws IOException
     */
    public static long getLineNumByRead(File file) throws IOException {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            reader.skip(file.length());
            return reader.getLineNumber();
        } finally {
            if (reader != null){
                reader.close();
            }
        }
    }


    /**
     * get project path
     * @param path
     * @return
     */
    public static String getProjectName(String path){
        String unixpath=FilenameUtils.separatorsToUnix(path);
        int startindex=unixpath.indexOf(FILE_SEPARATOR);
        if(startindex==0){
            unixpath=unixpath.substring(1);
            startindex=unixpath.indexOf(FILE_SEPARATOR);
        }
        if(startindex>-1){
            return unixpath.substring(0,startindex);
        }
        return unixpath;
    }

    /**
     * 获取文件名
     * @param filepath
     * @return
     */
    public static String getFileName(String filepath){
        return FilenameUtils.getName(filepath);
    }

    /**
     * 判断文件是否存在
     * @param file
     * @return
     */
    public static boolean ifExists(File file){
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public static String readFully(Reader rdr)
            throws IOException
    {
        return readFully(rdr, 8192);
    }

    public static String readFully(Reader rdr, int bufferSize)
            throws IOException
    {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        }

        char[] buffer = new char[bufferSize];
        int bufferLength = 0;
        StringBuffer textBuffer = null;
        while (bufferLength != -1) {
            bufferLength = rdr.read(buffer);
            if (bufferLength > 0) {
                textBuffer = textBuffer == null ? new StringBuffer() : textBuffer;
                textBuffer.append(new String(buffer, 0, bufferLength));
            }
        }
        return textBuffer == null ? null : textBuffer.toString();
    }
    // test main
    public static void main(String[] args) throws IOException {
        //File file = new File("D:\\oozie-app\\hongyong\\workflow\\flowtesthy4\\workflow1.json");
        //String sf = FileUtil.getFileContent(file);
        //System.out.println(sf);

        //File file2 = new File("D:\\oozie-app\\hongyong\\workflow\\flowtesthy4\\workflow2.json");
        //FileUtil.WriteContentToFileUTF8(file2, sf);
        // String newpath1="d:\\testhome\\after\\";
        // File file1=new File("d:\\testhome\\before\\test.dat");
        // System.out.println(ReplaceBasePath(basepath1, newpath1, file1).getPath());
        // //
        // String basepath2="d:\\testhome\\before";
        // String newpath2="d:\\testhome\\after";
        // File file2=new File("d:\\testhome\\before\\test.dat");
        // System.out.println(ReplaceBasePath(basepath2, newpath2, file2).getPath());
        //
        // String basepath3="d:/testhome/before/";
        // String newpath3="d:/testhome/after/";
        // File file3=new File("d:/testhome/before/test.dat");
        // System.out.println(ReplaceBasePath(basepath3, newpath3, file3).getPath());
        //
        // String basepath4="/testhome/before";
        // String newpath4="d:/testhome/after";
        // File file4=new File("/testhome/before/test.dat");
        // System.out.println(ReplaceBasePath(basepath4, newpath4, file4).getPath());
        //
        // String basepath5="/testhome/before/";
        // String newpath5="d:\\testhome\\after\\";
        // File file5=new File("/testhome/before/test.dat");
        // System.out.println(ReplaceBasePath(basepath5, newpath5, file5).getPath());


        File file = new File("D:/tmp/test");
        for(int i=0;i<1000;i++){
            File subfile = new File(file,"t"+i);
            FileUtil.WriteContentToFileUTF8(subfile, "t"+i);
        }
    }
}
