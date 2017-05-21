package com.logger.utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Lu Tingming
 * Time: 2010-11-11 20:53:54
 * Desc: 文件工具
 */
public class FileUtil {
    /**
     * 系统分隔符
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 写入内容到路径指定的文件
     *
     * @param s
     * @param path
     */
    public static void append(String s, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);
            mkdirIfNotExist(file.getParent());
            writer = new BufferedWriter(new FileWriter(file, true));

            writer.write(s);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入内容到路径指定的文件
     *
     * @param s
     * @param path
     */
    public static void write(String s, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);
            mkdirIfNotExist(file.getParent());
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(s);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 杨沛的方法
     * 指定编码写入到文件
     */
    public static void write(List fileContentList, String path, String encode) throws Exception {
        FileOutputStream out = null;
        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            out = new FileOutputStream(new File(path));
            for (Object obj : fileContentList) {
                if (obj instanceof String) {
                    out.write(((String) obj).getBytes(encode));
                }
                if (obj instanceof byte[]) {
                    out.write((byte[]) obj);
                }
            }
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 杨沛的方法
     * 指定编码写入到文件
     */
    public static void write(String s, String path, String encode) throws Exception {
        BufferedWriter out = null;
        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(path)), encode));
            out.write(s);
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入行的列表到路径对应的文件
     *
     * @param list
     * @param path
     */
    public static void writeLines(List<String> list, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);

            writer = new BufferedWriter(new FileWriter(file));

            for (String s : list) {
                writer.write(s);
                writer.write('\n');
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 读入文件到字符串
     *
     * @param path
     * @return
     */
    public static String readFile(String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            //reader.skip(1);
            String strLine;
            while (true) {
                strLine = reader.readLine();
                //System.out.println(strLine);
                if (strLine == null) break;
                sb.append(strLine).append("\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return sb.toString();
    }


    /**
     * 读入文件到字符串
     * 一定行数的内容（用户处理大文件）
     *
     * @param path
     * @return
     */
    public static StringBuffer readFile(String path, int maxLines) {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            //reader.skip(1);
            String strLine;
            int i = 0;
            while (true) {
                i++;
                if (i > maxLines) {
                    break;
                }

                strLine = reader.readLine();
                //System.out.println(strLine);
                if (strLine == null) break;
                sb.append(strLine).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return sb;
    }

    /**
     * 将输入流转换成字符串，不忽略输入流中的换行符
     */
    public static String ConvertToString(final InputStream is, final boolean closeStream) {
        StringBuilder strb = new StringBuilder();
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = is.read(b)) > 0) {
                strb.append(new String(b, 0, len, "UTF-8"));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (closeStream) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return strb.toString();
    }

    /**
     * 找到第一个符合指定正则表达式的匹配字符串
     *
     * @param path
     * @param regex
     * @return
     */
    public static String findFirst(String path, String regex) {
        File file = new File(path);
        BufferedReader reader = null;
        Pattern pattern = Pattern.compile(regex);
        String result = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String strLine;
            while (true) {
                strLine = reader.readLine();
//                System.out.println(strLine);
                if (strLine == null) break;
                Matcher matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    result = matcher.group(1);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 读入文件内容到行的列表
     *
     * @param path
     * @return
     */
    public static ArrayList<String[]> readStringArray(String path) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                list.add(strLine.split("\t"));

                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    list.add(strLine.split("\t"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 从路径得到文件名（包括扩展名）
     *
     * @param path
     * @return
     */
    public static String fullNameFromPath(String path) {
        int idx = path.lastIndexOf("\\");
        int idx1 = path.lastIndexOf("/");
        if (idx1 > idx) idx = idx1;
        return path.substring(idx + 1);
    }


    /**
     * 从路径得到主文件名
     *
     * @param path
     * @return
     */
    public static String mainNameFromPath(String path) {
        return mainNameFromName(fullNameFromPath(path));
    }


    /**
     * 在主文件名后插入字符串
     *
     * @param path
     * @return
     */
    public static String appendMainNameInPath(String path, String insertion) {
        String dir = getDir(path);
        String fullName = path.substring(dir.length());
        String mainName = mainNameFromName(fullName);
        String newName;
        if (fullName.equals(mainName)) {
            newName = fullName + insertion;
        } else {
            String extName = extNameFromName(fullName);
            newName = mainName + insertion + "." + extName;
        }
        return dir + newName;
    }


    /**
     * 替换扩展名
     *
     * @param path
     * @return
     */
    public static String replaceExtName(String path, String newExtName) {
        String dir = getDir(path);
        String fullName = path.substring(dir.length());
        String mainName = mainNameFromName(fullName);

        return dir + mainName + "." + newExtName;
    }


    /**
     * 从文件名得到主文件名（不包括扩展名）
     *
     * @param fullName
     * @return
     */
    public static String mainNameFromName(String fullName) {
        String name = fullNameFromPath(fullName);
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            return name.substring(0, idx);
        } else {
            return name;
        }
    }


    /**
     * 从文件名得到扩展名（不包括.）
     *
     * @param fullName
     * @return
     */
    public static String extNameFromName(String fullName) {
        int idx = fullName.lastIndexOf('.');
        if (idx >= 0) {
            return fullName.substring(idx + 1);
        } else {
            return null;
        }
    }

    /**
     * 将对象序列化到文件
     *
     * @param obj
     * @param path
     * @return
     */
    public static void writeObject(Object obj, String path) throws IOException {
        //DefaultLogger.logStart("write " + obj.getClass().getName() + " to " + path);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw e;
        }
    }

    /**
     * 将对象序列化到文件
     *
     * @param obj
     * @param dir
     * @param fileName
     * @return
     */
    public static void writeObject(Object obj, String dir, String fileName) throws IOException {
        String path = dir + FILE_SEPARATOR + fileName;
        writeObject(obj, path);
    }

    /**
     * 读入对象
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static Object readObject(String dir, String fileName) {
        String path = dir + FILE_SEPARATOR + fileName;
        return readObject(path);
    }

    /**
     * 读入对象
     *
     * @param path
     * @return
     */
    public static Object readObject(String path) {
        ////DefaultLogger.logStart("Load from \"" + path + "\"");
        Object obj;
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            obj = null;
        }
        ////DefaultLogger.logFinish("Load");
        return obj;
    }

    /**
     * 得到子目录
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<File> getSubDirList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<File> dirList = new ArrayList<File>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isDirectory()) {
                dirList.add(fileArray[i]);
            }
        }
        return dirList;
    }

    /**
     * 得到目录下的文件列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<File> getFileList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<File> fileList = new ArrayList<File>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileList.add(fileArray[i]);
            }
        }
        return fileList;
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）
     *
     * @param dir
     * @param pattern
     */
    public static void getFileListR(File dir, String pattern) {
        ArrayList<File> list_all = getSubDicAndFileListR(dir);
        for (File file : list_all) {
            if (file.getAbsolutePath().contains(pattern)) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）（返回结果包括目录）
     *
     * @param dir
     * @return
     */
    public static ArrayList<File> getSubDicAndFileListR(File dir) {
        ArrayList<File> list = new ArrayList<File>();

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            list.add(files[i]);
            if (files[i].isFile()) {
            } else {
                list.addAll(getSubDicAndFileListR(files[i]));
            }
        }

        return list;
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）（返回结果不包括目录）
     *
     * @param dir
     * @return
     */
    public static ArrayList<File> getSubFileListR(File dir) {
        ArrayList<File> list = new ArrayList<File>();

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                list.add(files[i]);
            } else {
                list.addAll(getSubFileListR(files[i]));
            }
        }

        return list;
    }

    /**
     * 对指定目录下的文件（递归包含子目录）修改文件名
     *
     * @param dir
     * @param regex
     * @param replacement
     */
    public static void renameR(File dir, String regex, String replacement) {
        ArrayList<File> list = getSubDicAndFileListR(dir);

        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            final String oldName = file.getName();
            String newName = oldName.replaceAll(regex, replacement);
            if (oldName.equals(newName)) {
                System.out.println("No need to change.");
            } else {
                String newPath = file.getParentFile().getAbsolutePath() + FILE_SEPARATOR + newName;
                System.out.println(file.getAbsolutePath() + "\t->");
                System.out.println(newPath);
                boolean b = file.renameTo(new File(newPath));
                System.out.println(b ? "OK" : "Failed!");
            }
        }
    }

    /**
     * 得到指定目录下的文件数组
     *
     * @param s_dir
     * @return
     */
    public static File[] getFiles(String s_dir) {
        File dir = new File(s_dir);

        return dir.listFiles();
    }

    /**
     * 得到指定目录下的文件的路径的数组
     *
     * @param s_dir
     * @return
     */
    public static String[] getFilePathes(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();

        String[] fileNames = new String[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileNames[i] = fileArray[i].getAbsolutePath();
            }
        }

        return fileNames;
    }

    /**
     * 得到指定目录下的文件的名称的列表
     *
     * @param s_dir
     * @return
     */
    public static String[] getFileNames(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();

        String[] fileNames = new String[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileNames[i] = fileArray[i].getName();
            }
        }

        return fileNames;
    }

    /**
     * 得到指定目录下的文件的路径的列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<String> getFilePathList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<String> fileList = new ArrayList<String>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileList.add(fileArray[i].getAbsolutePath());
            }
        }
        return fileList;
    }

    /**
     * 得到指定目录下的文件的名称的列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<String> getFileNameList(String s_dir) {
        ArrayList<String> pathList = getFilePathList(s_dir);
        ArrayList<String> nameList = new ArrayList<String>();

        for (String path : pathList) {
            nameList.add(fullNameFromPath(path));
        }

        return nameList;
    }

    /**
     * 移动目录
     *
     * @param srcDir
     * @param destDir
     */
    public static void moveDir(String srcDir, String destDir) {
        File f_srcDir = new File(srcDir);
        File[] list = f_srcDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".obj");
            }
        });

        File f_destDir = new File(destDir);
        if (!f_destDir.exists()) {
            f_destDir.mkdirs();
        }

        for (int i = 0; i < list.length; i++) {
            File f_src = list[i];
            File f_desc = new File(destDir + FILE_SEPARATOR + f_src.getName());
            f_src.renameTo(f_desc);
        }
    }

    /**
     * 移动文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void move(String srcPath, String destPath) {
        File f_src = new File(srcPath);
        File f_desc = new File(destPath);
        f_src.renameTo(f_desc);
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void mkdir(String dir) {
        File f = new File(dir);
        f.mkdir();
    }

    /**
     * 如果目录不存在，那么创建目录
     *
     * @param dir
     * @return
     */
    public static boolean mkdirIfNotExist(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            return f.mkdirs();
        }

        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return (new File(path)).exists();
    }

    /**
     * 得到指定目录/文件的父目录
     *
     * @param path d:\a\b
     * @return d:\a\
     */
    public static String getDir(String path) {
        int idx0 = path.lastIndexOf(FILE_SEPARATOR);
        int idx1 = path.lastIndexOf("/");
        int idx = Math.max(idx0, idx1);
        return path.substring(0, idx + 1);
    }


    /**
     * copy file
     *
     * @param srcPath
     * @param destPath
     * @return
     * @throws Exception
     */
    public static boolean copy(String srcPath, String destPath) throws Exception {
        return copy(new File(srcPath), new File(destPath));
    }


    /**
     * copy file
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws Exception
     */
    public static boolean copy(File srcFile, File destFile) throws Exception {
        if (!srcFile.exists()) {
            throw new Exception("Cannot find srcFile:" + srcFile.getAbsolutePath());
        }

        boolean result = false;

        FileInputStream is = null;                                                   //置源流
        FileOutputStream os = null;                                                     //置目标流
        try {
            mkdirIfNotExist(destFile.getParent());
            is = new FileInputStream(srcFile);               //创建输入流
            os = new FileOutputStream(destFile);                     //创建输出流
            byte[] buffer = new byte[4096];                             //保存文件内容到buffer
            int bytes_read;                                                         //缓冲区大小
            while ((bytes_read = is.read(buffer)) != -1) {    //读直到文件末尾
                //写入buffer
                os.write(buffer, 0, bytes_read);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {                                                                         //关闭流（永远不要忘记）
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 拷贝文件
     *
     * @param srcDirPath
     * @param desDirPath
     * @throws IOException
     */
    public static void copyFiles(String srcDirPath, String desDirPath) throws IOException {
        mkdirIfNotExist(desDirPath);

        Runtime.getRuntime().exec("cmd.exe /c start copy " + srcDirPath + "\\*.* " + desDirPath);
    }

    public static String removeNonValidChar(String filename) {
        return filename.replace("\\", "").replace("/", "")
                .replace(":", "").replace("*", "")
                .replace("?", "").replace("\"", "")
                .replace("<", "").replace(">", "")
                .replace("|", "");
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
            if (!flag) {
                System.gc();
                flag = file.delete();
            }
        }
        return flag;
    }

    /**
     * 将流写入到文件
     *
     * @param inputStream
     * @param filePath
     * @return
     */
    public static boolean writeStreamToFile(InputStream inputStream, String filePath) {
        //写入状态初始化为非
        boolean writeStatus = false;
        //若输入流为空，直接返回false
        if (inputStream == null) {
            return writeStatus;
        }
        //新建文件
        File storeFile = new File(filePath);
        mkdirIfNotExist(storeFile.getParent());
        //新建1KB的缓冲
        byte buffer[] = new byte[1024];
        //一次从流中读入的长度
        int length = 0;
        try {
            //打开文件的输出流
            FileOutputStream output = new FileOutputStream(storeFile);
            //若从输入流中读取的文件长度不为-1，将缓冲里的流写入文件中
            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            //写入状态设为真
            writeStatus = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //返回写入状态
        return writeStatus;
    }


    public static void write(String filePath, byte[] bytes) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            os.write(bytes);
        } catch (Exception e) {
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}


