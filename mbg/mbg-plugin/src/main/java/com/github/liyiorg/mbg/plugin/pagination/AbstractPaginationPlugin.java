package com.github.liyiorg.mbg.plugin.pagination;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 分页插件父类
 * 
 * @author LiYi
 *
 */
public abstract class AbstractPaginationPlugin extends PluginAdapter {
	
	private static final String PaginationAbleClass = "com.github.liyiorg.mbg.support.example.PaginationAble";
	
	public abstract String getDataBaseType();
	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		
		for(Method method : topLevelClass.getMethods()){
			if(method.isConstructor()&& (method.getParameters() == null || method.getParameters().size() == 0)){
				method.addBodyLine("databaseType = \"" + getDataBaseType() + "\";");
				break;
			}
		}
		
		// add PaginationAble interface
		topLevelClass.addImportedType(PaginationAbleClass);
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(PaginationAbleClass));
		return super.modelExampleClassGenerated(topLevelClass,introspectedTable);
	}
	
	
	
}
