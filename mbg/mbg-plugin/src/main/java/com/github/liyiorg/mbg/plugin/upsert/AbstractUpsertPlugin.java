package com.github.liyiorg.mbg.plugin.upsert;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 
 * @author LiYi
 *
 */
public abstract class AbstractUpsertPlugin extends PluginAdapter {
	
	private final static String ModelUpsertClass = "com.github.liyiorg.mbg.support.model.ModelUpsert";

	protected abstract void buildUpsertMethodBody(Method method);
	
	protected boolean readonly;
	
	protected boolean noGeneratedKey;
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String readonly_pro = introspectedTable.getTableConfiguration().getProperty("readonly");
		readonly = StringUtility.isTrue(readonly_pro);
		noGeneratedKey = introspectedTable.getTableConfiguration().getGeneratedKey() == null;
	}
	
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if(!readonly && noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			topLevelClass.addImportedType(ModelUpsertClass);
			topLevelClass.addSuperInterface(new FullyQualifiedJavaType(ModelUpsertClass));
			
			Method method = new Method("upsert");
			method.setVisibility(JavaVisibility.PUBLIC);
			buildUpsertMethodBody(method);
			topLevelClass.addMethod(method);
			
			// 添加 _sqlImprovement_ 属性
			buildSqlImprovementCode(topLevelClass, introspectedTable);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}
	
	/**
	 * 添加 _sqlImprovement_ 属性
	 * 
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	private void buildSqlImprovementCode(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Field field = new Field();
		field.setName("_sqlImprovement_");
		field.setType(new FullyQualifiedJavaType("Integer"));
		field.setVisibility(JavaVisibility.PRIVATE);
		context.getCommentGenerator().addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);
		
		Method methodSet = new Method("sqlImprovement");
		methodSet.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), "sqlImprovement"));
		methodSet.setVisibility(JavaVisibility.PUBLIC);
		methodSet.addBodyLine("this._sqlImprovement_ = sqlImprovement;");
		context.getCommentGenerator().addGeneralMethodComment(methodSet, introspectedTable);
		topLevelClass.addMethod(methodSet);

		Method methodGet = new Method("get_sqlImprovement_");
		methodGet.setVisibility(JavaVisibility.PUBLIC);
		methodGet.setReturnType(new FullyQualifiedJavaType("Integer"));
		methodGet.addBodyLine("return this._sqlImprovement_;");
		context.getCommentGenerator().addGeneralMethodComment(methodGet, introspectedTable);
		topLevelClass.addMethod(methodGet);
	}
	
	/**
	 * 获取列名拼接字符
	 * @param column column
	 * @return columnName
	 */
	protected String columnName(IntrospectedColumn column){
		if(column.isColumnNameDelimited()){
			return context.getBeginningDelimiter() + column.getActualColumnName() + context.getEndingDelimiter();
		}else{
			return column.getActualColumnName();
		}
	}
	
	/**
	 * 获取表名拼接字符
	 * @param introspectedTable introspectedTable
	 * @return tableName
	 */
	protected String tableName(IntrospectedTable introspectedTable){
		String fullTableName = StringUtility.composeFullyQualifiedTableName(
				introspectedTable.getTableConfiguration().getCatalog(), 
				introspectedTable.getTableConfiguration().getSchema(),
				introspectedTable.getTableConfiguration().getTableName(),
				'.');
		if(introspectedTable.getTableConfiguration().isDelimitIdentifiers()){
			return context.getBeginningDelimiter() + fullTableName + context.getEndingDelimiter();
		}else{
			return fullTableName;
		}
	}

	public boolean validate(List<String> warnings) {
		return true;
	}
	
}
