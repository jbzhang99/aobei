package com.aobei.trainconsole.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aobei.train.model.OssImg;
import com.aobei.trainconsole.configuration.CustomProperties;
import com.aobei.trainconsole.util.PathUtil.PathType;

import javax.imageio.ImageIO;

/**
 * 文件上传下载的处理工具类
 * 
 * @author Mr.bu
 *
 */
@Component
public class MyFileHandleUtil {

//	@Autowired
//	private CustomProperties customProperties;
	
	@Autowired
    OSSClient ossClient;

	/**
	 * 简单文件上传 获取文件名称（加时间戳或UUID）以及完成文件上传（到阿里云） 返回参数Map包含4个文件相关的信息 分别是文件名称，文件格式，文件访问地址、存储的bucket
	 * 获取的key分别是：file_name、file_format、file_url、bucketName
	 * 
	 * @param file  文件
	 * @param type 上传的文件的类型
	 * @return
	 */
	public Map<String, String> file_upload(MultipartFile file, PathType type) {
		// 获取连接阿里云的参数
//		String privateBucket = customProperties.getAliyun().getOss().getPrivateBucket();
//		String publicBucket = customProperties.getAliyun().getOss().getPublicBucket();
//		String avatarBucket = customProperties.getAliyun().getOss().getAvatarBucket();
		/**
		 * 创建ossClient
		 */
		Map<String, String> map = new HashMap<>();
		String file_name = null;
		String file_format = null;
		String file_url = null;
		String bucketName = null;
		int width=0;
		int height=0;

		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			String path = PathUtil.buildPath(type, originalFilename,new Date());
			bucketName = type.getBucket();
			int index = originalFilename.lastIndexOf(".");
			file_format = originalFilename.substring(index + 1);
			// 生成新的上传文件名称
			file_name = path.substring(path.lastIndexOf("/")+1);

			try {
				// 判断bucketName是否存在，不存在则创建
				if (!ossClient.doesBucketExist(bucketName)) {
					ossClient.createBucket(bucketName);
				}

				// 上传
				InputStream fileInputStream = file.getInputStream();
				ossClient.putObject(bucketName, path, fileInputStream);

                BufferedImage img = ImageIO.read(file.getInputStream());
                width=img.getWidth();
                height=img.getHeight();
                // 生成访问地址的前缀url
				URL url = new URL("http://" + bucketName + ".oss-cn-beijing.aliyuncs.com/" + path);
				file_url = url.toString();
			} catch (OSSException oe) {
				oe.printStackTrace();
			} catch (ClientException ce) {
				ce.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		map.put("file_name", file_name);
		map.put("file_format", file_format);
		map.put("file_url", file_url);
		map.put("bucketName", bucketName);
		map.put("width",width+"");
		map.put("height",height+"");
		return map;
	}
	
	public Map<String, String> file_upload(InputStream fileInputStream, PathType type) {
		// 获取连接阿里云的参数
//		String privateBucket = customProperties.getAliyun().getOss().getPrivateBucket();
//		String publicBucket = customProperties.getAliyun().getOss().getPublicBucket();
		/**
		 * 创建ossClient
		 */
		Map<String, String> map = new HashMap<>();
		String file_name = null;
		String file_format = null;
		String file_url = null;
		String bucketName = null;
		
		if (fileInputStream != null) {
			String originalFilename = "订单表.xlsx";
			String path = PathUtil.buildPath(type, originalFilename,new Date());
//			if (type.getSec() == 1) {
//				bucketName = privateBucket;
//			}else {
//				bucketName = publicBucket;
//			}
			bucketName = type.getBucket();
			int index = originalFilename.indexOf(".");
			file_format = originalFilename.substring(index + 1);
			// 生成新的上传文件名称
			file_name = path.substring(path.lastIndexOf("/")+1);

			try {
				// 判断bucketName是否存在，不存在则创建
				if (!ossClient.doesBucketExist(bucketName)) {
					ossClient.createBucket(bucketName);
				}

				// 上传
				ossClient.putObject(bucketName, path, fileInputStream);

				// 生成访问地址,过期时间30天
				Date expiration = new Date(new Date().getTime() + 1000 * 3600 * 24 * 30l);
				URL url = ossClient.generatePresignedUrl(bucketName, path, expiration);
				file_url = url.toString();
			} catch (OSSException oe) {
				oe.printStackTrace();
			} catch (ClientException ce) {
				ce.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		map.put("file_name", file_name);
		map.put("file_format", file_format);
		map.put("file_url", file_url);
		map.put("bucketName", bucketName);
		return map;
	}

	/**
	 * 获取带签名和有效时间的url
	 * 
	 * @param type 文件类型
	 *            私有库的库名
	 * @param ossImg
	 *            文件名称
	 * @param expiration_time
	 *            过期时间，单位为s eg：3600 = 1 hour
	 * @return 带签名和有效时长的url
	 */
	public String get_signature_url(PathType type, OssImg ossImg, Long expiration_time) {
		// 获取连接阿里云的参数
		String bucket = null;
		String npath = null;
		// ossImg 内置优先
		if(ossImg != null && StringUtils.hasText(ossImg.getBucket())){
			if("public".equals(ossImg.getAccess_permissions())){
				return ossImg.getUrl();
			}
			bucket = ossImg.getBucket();
			npath = ossImg.getUrl().replaceFirst("http[s]?://.*\\.aliyuncs\\.com/", "");
		}else{
			bucket = type.getBucket();
			String path = PathUtil.buildPath(type, ossImg.getName(),ossImg.getCreate_time());
			String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png|.XLSX|.xlsx|.xls|.XLS)$";
			Pattern pattern = Pattern.compile(reg);
			String[] split = path.split("/");
			for (int i = 0 ; i < split.length ; i++){
				if (pattern.matcher(split[i]).find()){
					split[i] = ossImg.getName();
				}
			}
			npath = String.join("/",split);
		}
		Date expiration = new Date(new Date().getTime() + 1000 * expiration_time);
		URL url = ossClient.generatePresignedUrl(bucket, npath, expiration);
		return url.toString();
	}

	/**
	 * 
	 * @param prefix
	 *            指定bucket的前缀
	 * @param maxKeys
	 *            指定查询多少条bucket数据
	 * @return 返回oss上已创建的bucket列表
	 */
	public List<Bucket> get_bucket_list(String prefix, Integer maxKeys) {
		ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
		if (prefix != null) {
			listBucketsRequest.setPrefix(prefix);
		}
		if (maxKeys != null) {
			listBucketsRequest.setMaxKeys(maxKeys);
		}
		List<Bucket> buckets = ossClient.listBuckets();
		return buckets;
	}
	
	/**
	 * 下载
	 * @param bucketName
	 * @param filename
	 * @return 返回输入流
	 */
	public InputStream file_download(String bucketName,String filename) {
		OSSObject ossObject = ossClient.getObject(bucketName, filename);
	    InputStream inputStream = ossObject.getObjectContent();
	    return inputStream;
	}
	
	/**
	 * 查询指定bucket下的Object集合
	 * @param bucketName
	 * @return OSSObjectSummary objectSummary 
	 * 	objectSummary.getKey() 即可获取Object的文件名
	 */
	public List<OSSObjectSummary> getObjectList(String bucketName){
		ObjectListing objectListing = ossClient.listObjects(bucketName);
		List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
		return objectSummaries;
	}
	
	/**
	 * 删除指定库下的指定文件
	 * @param bucketName
	 * @param filename
	 */
	public void daleteOssObject(String bucketName,String filename) {
		ossClient.deleteObject(bucketName, filename);
	}
	
	
}
