package com.aobei.trainconsole.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * Created by adminL on 2018/6/20.
 */
public class GenerateQRCode {
    private static final int QRCOLOR = 0xFF000000;   //默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF;   //背景颜色
    private static final String logo_path = "https://aobei-public.oss-cn-beijing.aliyuncs.com/img/puerjia_logo.png";
   /* public static void main(String[] args) throws WriterException
    {
        try
        {
            String logoPath = "C:\\logo.png";
            File logoFile = new File(logoPath);
            String outPath = "C:";
            createQRCode(logoFile, "2asdf5", outPath, 400, 400, "这是二维码下的文字");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    /**
     * 封装生成二维码
     * @param content
     * @param size
     * @return
     */
    public static BufferedImage getQRcode(String content,int size){

        URL url = null;
        File logoFile = null;
        String outPath = null;
        try {
            //读取外部图片信息
            url = new URL(logo_path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream is = conn.getInputStream();
            outPath = "D:";
            //文件保存位置
            File saveDir = new File(outPath);
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int ch;
            //读取输入流信息
            while ((ch = is.read(buffer)) != -1) {
                bytestream.write(buffer,0,ch);
            }
            //转化字节数字对象读取
            byte[] data = bytestream.toByteArray();
            bytestream.close();
            logoFile = new File(saveDir+File.separator+new Random(9)+"logo");
            FileOutputStream fos = new FileOutputStream(logoFile);
            fos.write(data);
            if(fos!=null){
                fos.close();
            }
            if(is!=null){
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*String logoPath = logo_path;
        File logoFile = new File(logoPath);
        String outPath = "D:";*/
        return createQRCode(logoFile, content, outPath, size, size, "二维码");
    }
    /**
     * 生成带logo的二维码图片
     * @param logoFile 二维码图片中间包含的logo图片文件，如果不存在，则生成不带logo图片的二维码
     * @param content 内容或跳转路径
     * @param outPath 二维码输出路径，如果为""则表示不输出图片到指定位置，只返回base64图片字符串
     * @param qrImgWidth 二维码图片宽度
     * @param qrImgHeight 二维码图片高度（有文字的话会加高45px）
     * @param productName 二维码图片下的文字
     * @return
     */
    public static BufferedImage createQRCode(File logoFile, String content, String outPath,
                                      int qrImgWidth, int qrImgHeight, String productName)
    {
        try
        {
            GenerateQRCode zp = new GenerateQRCode();
            // 生成二维码bufferedImage图片
            BufferedImage bim = zp.getQRCODEBufferedImage(content, BarcodeFormat.QR_CODE, qrImgWidth, qrImgHeight, zp.getDecodeHintType());

            // 如果有文字，则二维码图片高度增加45px
            if(!"".equals(productName)){
                qrImgHeight += 45;
            }
            // 给二维码图片添加Logo并保存到指定位置，返回base64编码的图片数据字符串
            return zp.createLogoQRCode(outPath, qrImgWidth, qrImgHeight, bim, logoFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给二维码图片添加Logo图片并生成最终二维码图片
     * @param outPath 输出二维码图片的路径，如果为""则表示不输出图片到指定位置，只返回base64图片字符串
     * @param qrImgWidth 生成二维码图片的宽度
     * @param qrImgHeight 生成二维码图片的高度
     * @param bim 读取二维码图片BufferedImage对象
     * @param logoPic logo图片File文件
     * @return 返回图片base64编码后的字符串
     */
    public BufferedImage createLogoQRCode(String outPath, int qrImgWidth, int qrImgHeight, BufferedImage bim, File logoPic)
    {
        try
        {
            /**
             * 读取二维码图片，并构建绘图对象
             */
            BufferedImage image = bim;

            // 如果logo图片存在，则加入到二维码图片中
            if(logoPic != null && logoPic.exists()){
                Graphics2D g = image.createGraphics();

                /**
                 * 读取Logo图片
                 */
                BufferedImage logo = ImageIO.read(logoPic);
                /**
                 * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
                 */
                int widthLogo = logo.getWidth(null)>image.getWidth()*3/10?(image.getWidth()*3/10):logo.getWidth(null),
                        heightLogo = logo.getHeight(null)>image.getHeight()*3/10?(image.getHeight()*3/10):logo.getWidth(null);

                /**
                 * logo放在中心
                 */
                int x = (image.getWidth() - widthLogo) / 2;
                int y = (image.getHeight() - heightLogo) / 2;
                /**
                 * logo放在右下角
                 *  int x = (image.getWidth() - widthLogo);
                 *  int y = (image.getHeight() - heightLogo);
                 */

                //开始绘制图片
                g.drawImage(logo, x, y, widthLogo, heightLogo, null);
//              g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
//              g.setStroke(new BasicStroke(logoConfig.getBorder()));
//              g.setColor(logoConfig.getBorderColor());
//              g.drawRect(x, y, widthLogo, heightLogo);
                g.dispose();

                logo.flush();
            }

            //logo.flush();
            image.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            ImageIO.write(image, "png", baos);

            // 如果输出路径为空，则不保存二维码图片到指定路径下
            /*if(!"".equals(outPath.trim())){
                //二维码生成的路径，但是实际项目中，我们是把这生成的二维码显示到界面上的，因此下面的折行代码可以注释掉
                //可以看到这个方法最终返回的是这个二维码的imageBase64字符串
                //前端用 <img src="data:image/png;base64,${imageBase64QRCode}"/> 其中${imageBase64QRCode}对应二维码的imageBase64字符串
                ImageIO.write(image, "png", new File(outPath + "\\" + new Date().getTime() + "浦尔家.png"));
            }*/
            /*if(logoPic==null){
                FileOutputStream out=new FileOutputStream("newFile.jpg");
                *//*JPEGImageEncoder encoder= JPEGCodec.createJPEGEncoder(out);
                encoder.encode(image);
                ImageIO.write(image,"JPEG", response.getOutputStream());
                out.close();*//*
                return out;
            }

            // 获取base64编码的二维码图片字符串
            String imageBase64QRCode = Base64.encodeBase64String(baos.toByteArray());
            baos.close();*/
            return image;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建初始化二维码
     *
     * @param bm
     * @return
     */
    public BufferedImage fileToBufferedImage(BitMatrix bm)
    {
        BufferedImage image = null;
        try
        {
            int w = bm.getWidth(), h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 生成二维码bufferedImage图片
     *
     * @param content
     *            编码内容
     * @param barcodeFormat
     *            编码类型
     * @param width
     *            图片宽度
     * @param height
     *            图片高度
     * @param hints
     *            设置参数
     * @return
     */
    public BufferedImage getQRCODEBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints)
    {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try
        {
            multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return
     */
    public Map<EncodeHintType, Object> getDecodeHintType()
    {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }
}

/**
 * logo的配置
 * @author nhhyx
 *
 */
class LogoConfig
{
    // logo默认边框颜色
    public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    // logo默认边框宽度
    public static final int DEFAULT_BORDER = 2;
    // logo大小默认为照片的1/5
    public static final int DEFAULT_LOGOPART = 5;

    private final int border = DEFAULT_BORDER;
    private final Color borderColor;
    private final int logoPart;

    public LogoConfig()
    {
        this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
    }

    public LogoConfig(Color borderColor, int logoPart)
    {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public int getBorder()
    {
        return border;
    }

    public int getLogoPart()
    {
        return logoPart;
    }
}