package com.aobei.trainapi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.aobei.train.IdGenerator;

public class PathUtil {

	/**
	 * 生成路径
	 * 
	 * @param type
	 *            类型
	 * @param sourceFile
	 *            原文件名
	 * @return
	 */
	public static String buildPath(PathType type, String sourceFile) {
		List<String> dirs = new ArrayList<>();
		dirs.addAll(Arrays.asList(type.name().split("_")));
		int index = sourceFile.lastIndexOf(".");
		Calendar calendar = Calendar.getInstance();
		dirs.add(calendar.get(Calendar.YEAR) + "");
		dirs.add((calendar.get(Calendar.MONTH) + 1) + "");
		dirs.add(calendar.get(Calendar.DAY_OF_MONTH) + "");
		dirs.add(IdGenerator.generateId() + sourceFile.substring(index));
		return String.join("/", dirs);
	}

	public enum PathType {
		/** CMS banner */				image_cms_banner(0),
		/** 用户身份证  */				    image_user_idcard(1), 
		/** 用户logo  */					image_user_logo(1),
		/** 合伙人营业执照  */				image_partner_businesslicense(1),
		/** 课程图片  */					image_course_logo(1),
		/** 学员健康证  */					image_student_health(1),
		/** 教师资格证  */					image_teacher_certification(1),
		/** 商品图片  */					image_product_logo(0),
		/** 分类logo  */					image_category_logo(0),
		/** 学员头像  */					image_student_logo(0),
		/** 学员技能证书 */ 					img_student_cert(0),
		/** 无犯罪记录 */					image_innocence_proof(1),
		/** 订单信息导出excel文件 */          file_order_excel(1);
		 
		
		private final int sec;	//是否要加密
		
		private PathType(int sec){
			this.sec = sec;
		}

		/**
		 * 是否要加密
		 * @return 1 加密
		 */
		public int getSec() {
			return sec;
		}
	}

	
	
}
