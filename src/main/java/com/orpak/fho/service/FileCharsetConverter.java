package com.orpak.fho.service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class FileCharsetConverter {

    public static void main(String[] args) throws Exception {
        convert("C:\\HongJianWork\\HJOldPc\\fho\\VehicleListReport.csv",
                "GB2312", "UTF-8", new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith("csv");
                    }
                });
    }

    public static void gbk2utf(String fileName){
        try {
            convert(fileName,
                    "GBK", "UTF-8", new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith("csv");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addBom(String fileName){
        try {
            convert(fileName,
                    "UTF-8", "UTF-8", new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith("csv");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 把指定文件或目录转换成指定的编码
     *
     * @param fileName
     *      要转换的文件
     * @param fromCharsetName
     *      源文件的编码
     * @param toCharsetName
     *      要转换的编码
     * @throws Exception
     */
    public static void convert(String fileName, String fromCharsetName,
                               String toCharsetName) throws Exception {
        convert(new File(fileName), fromCharsetName, toCharsetName, null);
    }

    /**
     * 把指定文件或目录转换成指定的编码
     *
     * @param file
     *      要转换的文件或目录
     * @param fromCharsetName
     *      源文件的编码
     * @param toCharsetName
     *      要转换的编码
     * @throws Exception
     */
    public static void convert(File file, String fromCharsetName,
                               String toCharsetName) throws Exception {
        convert(file, fromCharsetName, toCharsetName, null);
    }

    /**
     * 把指定文件或目录转换成指定的编码
     *
     * @param file
     *      要转换的文件或目录
     * @param fromCharsetName
     *      源文件的编码
     * @param toCharsetName
     *      要转换的编码
     * @param filter
     *      文件名过滤器
     * @throws Exception
     */
    public static void convert(String fileName, String fromCharsetName,
                               String toCharsetName, FilenameFilter filter) throws Exception {
        convert(new File(fileName), fromCharsetName, toCharsetName, filter);
    }

    /**
     * 把指定文件或目录转换成指定的编码
     *
     * @param file
     *      要转换的文件或目录
     * @param fromCharsetName
     *      源文件的编码
     * @param toCharsetName
     *      要转换的编码
     * @param filter
     *      文件名过滤器
     * @throws Exception
     */
    public static void convert(File file, String fromCharsetName,
                               String toCharsetName, FilenameFilter filter) throws Exception {
        if (file.isDirectory()) {
            File[] fileList = null;
            if (filter == null) {
                fileList = file.listFiles();
            } else {
                fileList = file.listFiles(filter);
            }
            for (File f : fileList) {
                convert(f, fromCharsetName, toCharsetName, filter);
            }
        } else {
            if (filter == null
                    || filter.accept(file.getParentFile(), file.getName())) {
                String fileContent = getFileContentFromCharset(file,
                        "utf-8");
               // System.out.println("read filecontent "+fileContent);
                saveFile2Charset(file, toCharsetName, fileContent);
            }
        }
    }

    /**
     * 以指定编码方式读取文件，返回文件内容
     *
     * @param file
     *      要转换的文件
     * @param fromCharsetName
     *      源文件的编码
     * @return
     * @throws Exception
     */
    public static String getFileContentFromCharset(File file,
                                                   String fromCharsetName) throws Exception {
        if (!Charset.isSupported(fromCharsetName)) {
            throw new UnsupportedCharsetException(fromCharsetName);
        }
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream,
                fromCharsetName);
        char[] chs = new char[(int) file.length()];
        reader.read(chs);
        String str = new String(chs).trim();
        reader.close();
        return str;
    }

    /**
     * 以指定编码方式写文本文件，存在会覆盖
     *
     * @param file
     *      要写入的文件
     * @param toCharsetName
     *      要转换的编码
     * @param content
     *      文件内容
     * @throws Exception
     */
    public static void saveFile2Charset(File file, String toCharsetName,
                                        String content) throws Exception {
        if (!Charset.isSupported(toCharsetName)) {
            throw new UnsupportedCharsetException(toCharsetName);
        }
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outWrite = new OutputStreamWriter(outputStream,
                toCharsetName);
        byte[] bom ={(byte) 0xEF,(byte) 0xBB,(byte) 0xBF};
       // outWrite.w
        ((FileOutputStream)outputStream).write(bom);
        //System.out.println("write content "+content);
        outWrite.write(content);
        outWrite.close();
    }

}