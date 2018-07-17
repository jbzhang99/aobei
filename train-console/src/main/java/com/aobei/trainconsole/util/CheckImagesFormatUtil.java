package com.aobei.trainconsole.util;

/**
 * Created by adminL on 2018/7/4.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CheckImagesFormatUtil {

    /**
     * 图片的像素判断
     * @param file 文件
     * @param imageWidth 图片宽度
     * @param imageHeight 图片高度
     * @return true：上传图片宽度和高度都小于等于规定最大值
     * @throws IOException
     */
    public static boolean checkImageElement(File file, int imageWidth, int imageHeight) throws IOException {
        Boolean result = false;
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (bufferedImage != null && height == imageHeight && width == imageWidth) {
            result = true;
        }
        return result;
    }

    /**
     * 校验图片比例
     *
     * @param width 宽
     * @param height 高
     * @return true：符合要求
     * @throws IOException
     */
    public static boolean checkImageScale(int width, int height) throws IOException {
        Boolean result = false;
        /*
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();*/
        int scale = 2;//比例值
        if (width != 0 && height != 0) {
            int scale2 = width / height;
            if (scale == scale2) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 校验图片的大小
     * @param file 文件
     * @param imageSize 图片最大值(KB)
     * @return true：上传图片小于图片的最大值
     */
    /*public static boolean checkImageSize(File file, Long imageSize) {
        if (!file.exists()) {
            return false;
        }
        Long size = file.length() / 1024; // 图片大小
        Long maxImageSize = SettingUtils.get().getMaxImageSize(); // 图片最大不能超过5M
        if (maxImageSize == null) {
            maxImageSize = 5 * 1024L;
        } else {
            maxImageSize = maxImageSize * 1024;
        }
        if (size > maxImageSize) {
            return false;
        }
        if (imageSize == null) {
            return true;
        }
        if (size.intValue() <= imageSize) {
            return true;
        }
        return false;
    }*/

}
