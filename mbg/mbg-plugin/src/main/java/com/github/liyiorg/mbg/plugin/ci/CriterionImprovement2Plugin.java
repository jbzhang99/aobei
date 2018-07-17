package com.github.liyiorg.mbg.plugin.ci;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;

/**
 * 
 * Example Criterion improvement 2<br>
 * 
 * dependency plugin <br>
 * com.github.liyiorg.mbg.plugin.ExampleCPlugin <br>
 * 
 * 1 Replace Criterion to com.github.liyiorg.mbg.support.example.Criterion<br>
 * 2 Delete GeneratedCriteria class and Criteria extends
 * com.github.liyiorg.mbg.support.example.MbgGeneratedCriteriaCommon<br>
 * 
 * @author LiYi
 *
 */
public class CriterionImprovement2Plugin extends PluginAdapter {

	private static final String MbgGeneratedCriteriaCommonClass = "com.github.liyiorg.mbg.support.example.MbgGeneratedCriteriaCommon";

	private static final String WItemClass = "com.github.liyiorg.mbg.support.example.WItem";

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		exec(topLevelClass, introspectedTable, context);
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	protected static void exec(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, Context context) {
		// 添加import
		topLevelClass.addImportedType(MbgGeneratedCriteriaCommonClass);
		topLevelClass.addImportedType(WItemClass);

		topLevelClass.addAnnotation("@SuppressWarnings(\"unused\")");

		// 添加内部类W
		addInnerClass_W(topLevelClass, introspectedTable, context);

		List<InnerClass> innerClassList = topLevelClass.getInnerClasses();
		for (int i = 0; i < innerClassList.size(); i++) {
			InnerClass innerClass = innerClassList.get(i);
			if ("GeneratedCriteria".equals(innerClass.getType().getShortName())
					|| "Criterion".equals(innerClass.getType().getShortName())) {
				innerClassList.remove(innerClass);
				i--;
			} else if ("Criteria".equals(innerClass.getType().getShortName())) {
				innerClass.setSuperClass(new FullyQualifiedJavaType(MbgGeneratedCriteriaCommonClass + "<Criteria>"));
			}
		}
	}

	/**
	 * 添加内部类W
	 * 
	 * @param topLevelClass
	 *            topLevelClass
	 * @param introspectedTable
	 *            introspectedTable
	 */
	private static void addInnerClass_W(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
			Context context) {
		InnerClass innerClass_W = new InnerClass("W");
		innerClass_W.setVisibility(JavaVisibility.PUBLIC);
		innerClass_W.setAbstract(true);
		innerClass_W.setStatic(true);
		List<IntrospectedColumn> list = new ArrayList<IntrospectedColumn>();
		list.addAll(introspectedTable.getPrimaryKeyColumns());
		list.addAll(introspectedTable.getBaseColumns());
		list.addAll(introspectedTable.getBLOBColumns());
		for (IntrospectedColumn column : list) {
			if (column.isBLOBColumn() && !column.isStringColumn()) {
				//continue;
			}

			Field field = new Field();
			field.setFinal(true);
			field.setStatic(true);
			field.setVisibility(JavaVisibility.PUBLIC);
			String javaType = column.getFullyQualifiedJavaType().getFullyQualifiedName();
			if (javaType.startsWith("java.")) {
				javaType = column.getFullyQualifiedJavaType().getShortName();
			}
			field.setType(new FullyQualifiedJavaType("WItem<" + javaType + ">"));
			field.setName(column.getActualColumnName());
			field.setInitializationString("WItem.warp(C." + column.getActualColumnName() + ")");
			innerClass_W.addField(field);
		}

		context.getCommentGenerator().addClassComment(innerClass_W, introspectedTable);
		topLevelClass.addInnerClass(innerClass_W);
	}

	@Override
	public boolean validate(List<String> arg0) {
		return true;
	}

}
