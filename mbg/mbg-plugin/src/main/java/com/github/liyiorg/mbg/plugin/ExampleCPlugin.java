package com.github.liyiorg.mbg.plugin;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * ExampleCPlugin Add enum C<br>
 *  
 * @author LiYi
 *
 */
public class ExampleCPlugin extends PluginAdapter {
	
	private static final String CItem_NAME = "C_I_T_E_M";
	
	private static final String CInterfaceClass = "com.github.liyiorg.mbg.support.example.CInterface";

	private static final String CItemClass = "com.github.liyiorg.mbg.support.example.CItem";
	
	private final static String REMARKS_PROPERTY_NAME = "remarks";
	
	private static String DEFAULT_REMARKS;

	private String remarks;
	

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		DEFAULT_REMARKS = properties.getProperty(REMARKS_PROPERTY_NAME, "1");
		String remarks_pro = introspectedTable.getTableConfiguration().getProperty(REMARKS_PROPERTY_NAME);
		if (StringUtility.stringHasValue(remarks_pro)) {
			remarks = remarks_pro;
		} else {
			remarks = DEFAULT_REMARKS;
		}
		
	}
	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//添加import
		topLevelClass.addImportedType(CInterfaceClass);
		topLevelClass.addImportedType(CItemClass);
		topLevelClass.addImportedType("java.sql.Types");
		
		//添加内部枚举C
		addInnerEnum_C(topLevelClass, introspectedTable);
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	/**
	 * 添加列枚举
	 * @param topLevelClass topLevelClass
	 * @param introspectedTable introspectedTable
	 */
	private void addInnerEnum_C(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		InnerEnum innerEnum_C = new InnerEnum(new FullyQualifiedJavaType("C"));
		innerEnum_C.setVisibility(JavaVisibility.PUBLIC);
		innerEnum_C.setStatic(true);
		innerEnum_C.addSuperInterface(new FullyQualifiedJavaType(CInterfaceClass));
		//添加构造项
		Map<String, String> constantMap = new LinkedHashMap<String, String>();
		buildEnumConstant(constantMap, introspectedTable.getPrimaryKeyColumns(), innerEnum_C, 1);
		buildEnumConstant(constantMap, introspectedTable.getBaseColumns(), innerEnum_C, 2);
		buildEnumConstant(constantMap, introspectedTable.getBLOBColumns(), innerEnum_C, 3);
		
		StringBuilder stringBuilder = new StringBuilder();
		// 添加表注释
		if(!"0".equals(remarks)){
			stringBuilder.append("//--------------------------------------------------")
			.append(System.lineSeparator())
			.append("\t\t//[").append(introspectedTable.getTableType()).append("]");
			if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
				stringBuilder.append("\t").append(introspectedTable.getRemarks());
			}
			stringBuilder.append(System.lineSeparator())
			.append("\t\t//--------------------------------------------------");
		}
		
		switch (remarks) {
		case "0":
			BuildRemarks.type_0(innerEnum_C, stringBuilder, constantMap);
			break;
		case "2":
			BuildRemarks.type_2(innerEnum_C, stringBuilder, constantMap);
			break;
		case "3":
			BuildRemarks.type_3(innerEnum_C, stringBuilder, constantMap);
			break;
		default:
			BuildRemarks.type_1(innerEnum_C, stringBuilder, constantMap);
		}
		Field field_CItem = new Field(CItem_NAME, new FullyQualifiedJavaType(CItemClass));
		field_CItem.setVisibility(JavaVisibility.PRIVATE);
		field_CItem.setFinal(true);
		innerEnum_C.addField(field_CItem);
		
		String tableAlias = introspectedTable.getTableConfiguration().getAlias();
		String tableAliasStr = StringUtility.stringHasValue(tableAlias) ? "\"" + tableAlias + "\"" : "null";
		String dstr_b = context.getBeginningDelimiter().replaceAll("\"", "\\\\\"");
		String dstr_e = context.getEndingDelimiter().replaceAll("\"", "\\\\\"");
		

		Method method_C = new Method("C");
		method_C.setVisibility(JavaVisibility.PRIVATE);
		method_C.setConstructor(true);
		method_C.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "type"));
		method_C.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "jdbcType"));
		method_C.addParameter(new Parameter(new FullyQualifiedJavaType("boolean"), "delimited"));
		method_C.addBodyLine(CItem_NAME + " = new CItem(type, jdbcType, delimited, name(), "
				+ tableAliasStr + " , \"" + dstr_b + "\", \"" + dstr_e + "\");");
		innerEnum_C.addMethod(method_C);

		Method method_getValue = new Method("getType");
		method_getValue.setVisibility(JavaVisibility.PUBLIC);
		method_getValue.setReturnType(new FullyQualifiedJavaType("int"));
		method_getValue.addBodyLine("return " + CItem_NAME + ".getType();");
		innerEnum_C.addMethod(method_getValue);
		
		Method method_jdbcType = new Method("getJdbcType");
		method_jdbcType.setVisibility(JavaVisibility.PUBLIC);
		method_jdbcType.setReturnType(new FullyQualifiedJavaType("int"));
		method_jdbcType.addBodyLine("return " + CItem_NAME + ".getJdbcType();");
		innerEnum_C.addMethod(method_jdbcType);
		
		Method method_isDelimited = new Method("isDelimited");
		method_isDelimited.setVisibility(JavaVisibility.PUBLIC);
		method_isDelimited.setReturnType(new FullyQualifiedJavaType("boolean"));
		method_isDelimited.addBodyLine("return " + CItem_NAME  + ".isDelimited();");
		innerEnum_C.addMethod(method_isDelimited);
		
		Method method_delimitedName = new Method("delimitedName");
		method_delimitedName.setVisibility(JavaVisibility.PUBLIC);
		method_delimitedName.setReturnType(new FullyQualifiedJavaType("String"));
		method_delimitedName.addBodyLine("return " + CItem_NAME  + ".delimitedName();");
		innerEnum_C.addMethod(method_delimitedName);
		
		Method method_delimitedAliasName = new Method("delimitedAliasName");
		method_delimitedAliasName.setVisibility(JavaVisibility.PUBLIC);
		method_delimitedAliasName.setReturnType(new FullyQualifiedJavaType("String"));
		method_delimitedAliasName.addBodyLine("return " + CItem_NAME  + ".delimitedAliasName();");
		innerEnum_C.addMethod(method_delimitedAliasName);
		
		context.getCommentGenerator().addEnumComment(innerEnum_C, introspectedTable);
		topLevelClass.addInnerEnum(innerEnum_C);
	}
	
	
	/**
	 * 生成构造项
	 * @param map map
	 * @param introspectedColumns introspectedColumns
	 * @param innerEnum_C innerEnum_C
	 * @param type [1,2,3]
	 */
	private void buildEnumConstant(Map<String,String> map,List<IntrospectedColumn> introspectedColumns, InnerEnum innerEnum_C, int type) {
		for (IntrospectedColumn column : introspectedColumns) {
			String columnRemark = null;
			if (StringUtility.stringHasValue(column.getRemarks())) {
				columnRemark = column.getRemarks();
			}
			
			if(StringUtility.stringHasValue(column.getDefaultValue())){
				if(columnRemark == null){
					columnRemark = "[DV=>" + column.getDefaultValue() + "]";
				}else{
					columnRemark += " [DV=>" + column.getDefaultValue() + "]";
				}
			}
			String key = String.format("%s(%d,Types.%s,%b)", column.getActualColumnName(), type,
					column.getJdbcTypeName(), column.isColumnNameDelimited());
			map.put(key, columnRemark);
		}
	}

	public boolean validate(List<String> warnings) {
		return true;
	}
	
	private static class BuildRemarks{
		
		private static final int MAX_TABS_LENGTH = 10;
		private static final int APPEND_T = 2;
		
		
		/**
		 * 计算格式化输出注释的最大key tab length
		 * @param keys keys
		 * @param a 补位长度
		 * @return maxTabs
		 */
		private static int maxTabs(Object[] keys, int a) {
			int maxKeyLength = 0;
			for (int i = 0; i < keys.length; i++) {
				Object key = keys[i];
				if (key != null) {
					//中文字符替换
					String keystr = key.toString().replaceAll("[^\\x00-\\xff]", "##");
					if (keystr.length() > maxKeyLength) {
						maxKeyLength = keystr.length();
					}
				}
			}
			return (maxKeyLength + a) / 4 + ((maxKeyLength + a) % 4 == 0 ? 0 : 1);
		}

		/**
		 * 计算格式化输出注释的预留空白
		 * @param key key
		 * @param maxTabs maxTabs
		 * @param appendT \t 补长
		 * @param a 字符串补位长度
		 * @return \t ...
		 */
		private static String buildTabs(String key, int maxTabs,int appendT,int a) {
			key = key.replaceAll("[^\\x00-\\xff]", "##");
			int keyLength =  key.length();
			int r = (keyLength + a) / 4; 
			int l = maxTabs - r + appendT;
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < l; i++) {
				stringBuilder.append("\t");
			}
			return stringBuilder.toString();
		}
		
		/**
		 * 无备注输出
		 * @param innerEnum_C innerEnum_C
		 * @param stringBuilder stringBuilder
		 * @param constantMap constantMap
		 */
		static void type_0(InnerEnum innerEnum_C, StringBuilder stringBuilder,
				Map<String, String> constantMap) {
			Iterator<String> iterator = constantMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				stringBuilder.append(System.lineSeparator()).append("\t\t");
				stringBuilder.append(key);
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				} 
			}
			innerEnum_C.addEnumConstant(stringBuilder.toString());
		}
		
		/**
		 * 备注输出 <br>
		 * 同行输出 <br>
		 * 格式 <br>
		 * 备注 \t 构造
		 * @param innerEnum_C innerEnum_C
		 * @param stringBuilder stringBuilder
		 * @param constantMap constantMap
		 */
		static void type_1(InnerEnum innerEnum_C, StringBuilder stringBuilder,
				Map<String, String> constantMap) {
			try {
				int maxTabs = maxTabs(constantMap.values().toArray(),7);
				int maxBuildTabs = maxTabs > MAX_TABS_LENGTH ? MAX_TABS_LENGTH : maxTabs;
				Iterator<String> iterator = constantMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String columnRemark = constantMap.get(key);
					boolean hasRemark = StringUtility.stringHasValue(columnRemark);
					String rm = "\t\t\t";
					if(hasRemark){
						stringBuilder.append(System.lineSeparator());
						rm = "/** " + columnRemark + " */";
					}
					stringBuilder.append("\t\t");
					String brm = "";
					if(hasRemark && maxTabs(new Object[]{columnRemark},7) <= MAX_TABS_LENGTH + APPEND_T){
						brm = columnRemark;
					}else{
						rm += System.lineSeparator() + "\t\t\t";
					}
					stringBuilder.append(rm).append(buildTabs(brm, maxBuildTabs, APPEND_T, 7)).append(key);
					if (iterator.hasNext()) {
						stringBuilder.append(",");
					}
				}
				innerEnum_C.addEnumConstant(stringBuilder.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 备注输出 <br>
		 * 平行输出 <br>
		 * 格式 <br>
		 * 备注 <br>
		 * 构造
		 * @param innerEnum_C innerEnum_C
		 * @param stringBuilder stringBuilder
		 * @param constantMap constantMap
		 */
		static void type_2(InnerEnum innerEnum_C, StringBuilder stringBuilder,
				Map<String, String> constantMap) {
			Iterator<String> iterator = constantMap.keySet().iterator();
			boolean first = true;
			while (iterator.hasNext()) {
				String key = iterator.next();
				String constant_str = null;
				if(StringUtility.stringHasValue(constantMap.get(key))){
					constant_str = "/** " + constantMap.get(key) + " */" + System.lineSeparator() + "\t\t" + key;
				}else{
					constant_str = key;
				}
				if(first){
					constant_str = stringBuilder.toString() + System.lineSeparator() + "\t\t" + constant_str;
					first = false;
				}
				innerEnum_C.addEnumConstant(constant_str);
			}
		}

		/**
		 * 备注输出 <br>
		 * 同行输出 <br>
		 * 格式 <br>
		 * 构造 \t 备注
		 * @param innerEnum_C innerEnum_C
		 * @param stringBuilder stringBuilder
		 * @param constantMap constantMap
		 */
		static void type_3(InnerEnum innerEnum_C, StringBuilder stringBuilder,
				Map<String, String> constantMap) {
			int maxTabs = maxTabs(constantMap.keySet().toArray(),1);
			Iterator<String> iterator = constantMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String columnRemark = constantMap.get(key);
				stringBuilder.append(System.lineSeparator()).append("\t\t");
				stringBuilder.append(key);
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				} else if (columnRemark != null) {
					stringBuilder.append(";");
				}

				if (columnRemark != null) {
					stringBuilder.append(buildTabs(key, maxTabs, APPEND_T, 1)).append("//").append(columnRemark);
				}
			}
			innerEnum_C.addEnumConstant(stringBuilder.toString());
		}
	}
	
}
