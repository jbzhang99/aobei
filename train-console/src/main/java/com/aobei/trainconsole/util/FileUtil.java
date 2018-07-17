package com.aobei.trainconsole.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * Created by mr_bl on 2018/6/4.
 */
public class FileUtil {

    /**
     * 使用GBK编码可以避免压缩中文文件名乱码
     */
    private static final String CHINESE_CHARSET = "GBK";

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * 第一步： 把 支付宝生成的账单 下载到本地目录
     *
     * @param billurl
     *            支付宝资源url
     * @param filePath
     *            生成的zip 包目录
     * @throws MalformedURLException
     */
    public static void downloadNet(String billurl, String filePath)
            throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(billurl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            File file = new File(StringUtils.substringBefore(filePath,"bill"));
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            FileOutputStream fs = new FileOutputStream(filePath);

            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unZip(String zipFilePath, String destDir)
            throws Exception {

        ZipFile zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
        Enumeration<?> emu = zipFile.getEntries();
        BufferedInputStream bis;

        FileOutputStream fos;
        BufferedOutputStream bos;

        File file, parentFile;
        ZipEntry entry;
        byte[] cache = new byte[CACHE_SIZE];

        while (emu.hasMoreElements()) {
            entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(destDir + entry.getName()).mkdirs();
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(entry));

            file = new File(destDir + entry.getName());
            parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists())) {
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, CACHE_SIZE);
            int nRead = 0;
            while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                fos.write(cache, 0, nRead);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
        zipFile.close();
    }
}
