package com.aobei.trainconsole.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.aobei.train.IdGenerator;

public class PathUtil {

	/**
	 * 生成路径
	 * 
	 * @param type
	 *            类型
	 * @param sourceFile
	 *            原文件名
	 * @param create_time
	 *            创建时间
	 * @return
	 */
	public static String buildPath(PathType type, String sourceFile, Date create_time) {
		List<String> dirs = new ArrayList<>();
		dirs.addAll(Arrays.asList(type.name().split("_")));
		int index = sourceFile.lastIndexOf(".");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(create_time);
		dirs.add(calendar.get(Calendar.YEAR) + "");
		dirs.add((calendar.get(Calendar.MONTH) + 1) + "");
		dirs.add(calendar.get(Calendar.DAY_OF_MONTH) + "");
		dirs.add(LocalDateTime.now().toString() + sourceFile.substring(index));
		return String.join("/", dirs);
	}

	public enum PathType {
		/** CMS banner */				image_cms_banner("aobei-test-public",0),
		/** 用户身份证  */				    image_user_idcard("aobei-test",1),
		/** 用户logo  */					image_user_logo("aobei-test",1),
		/** 合伙人营业执照  */				image_partner_businesslicense("aobei-test",1),
		/** 课程图片  */					image_course_logo("aobei-test",1),
		/** 学员健康证  */					image_student_health("aobei-test",1),
		/** 教师资格证  */					image_teacher_certification("aobei-test",1),
		/** 商品图片  */					image_product_logo("aobei-test-public",0),
		/** 商品详情图片  */					image_productLite_logo("aobei-test-public",0),
		/** 商品Tag图片  */					image_productTag_logo("aobei-test-public",0),
		/** 分类logo  */					image_category_logo("aobei-test-public",0),
		/** 学员头像  */					image_student_logo("aobei-test-public",0),
		/** 学员技能证书 */ 					img_student_cert("aobei-test-public",0),
		/** 学员无犯罪证明 */ 					img_student_Innocence("aobei-test",1),
		/** 订单信息导出excel文件 */          file_order_excel("aobei-test",1),

		/** 顾客头像 */                   avatar_customer_logo("aobei-avatar",1),
		/** 合伙人头像 */                   avatar_partner_logo("aobei-avatar",1),
		/** 服务人员头像 */                   avatar_service_logo("aobei-avatar",1),
		/** teacher头像 */                   avatar_teacher_logo("aobei-avatar",1);
		
		 
		
		private final int sec;	//是否要加密

		private final String bucket;	//是否要加密
		
		PathType(String bucket,int sec){
			this.sec = sec;
			this.bucket = bucket;
		}

		/**
		 * 是否要加密
		 * @return 1 加密
		 */
		public int getSec() {
			return sec;
		}

		public String getBucket() {
			return bucket;
		}
	}

}
