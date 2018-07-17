package com.github.liyiorg.mbg.plugin.upsert;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * MySQL insert and insertSelective improvement<br>
 * MySQL since version 5.5 <br>
 * 
 * 1 REPLACE 语法格式 <br>
 * 2 ON DUPLICATE KEY UPDATE
 * 
 * @author LiYi
 *
 */
public class MySQLUpsertPlugin extends AbstractUpsertPlugin {

	@Override
	protected void buildUpsertMethodBody(Method method) {
		method.addBodyLine("this._sqlImprovement_ = 2;");
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if(readonly){
			return false;
		}
		if(noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			//添加insert or replace 表达式
			for(Element e : element.getElements()){
				if(e instanceof TextElement){
					TextElement temp = (TextElement)e;
					if(temp.getContent().matches("\\s*(?i)insert\\s+.*")){
						try {
							java.lang.reflect.Field field = TextElement.class.getDeclaredField("content");
							field.setAccessible(true);
							field.set(temp, "${_sqlImprovement_ == 1 ? 'replace' : 'insert'} " + temp.getContent().replaceFirst("\\s*(?i)insert\\s+(.*)", "$1"));
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						} catch (SecurityException e1) {
							e1.printStackTrace();
						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
			List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
			if (columns.size() > 0) {
				XmlElement xmlElement = new XmlElement("if");
				xmlElement.addAttribute(new Attribute("test", "_sqlImprovement_ == 2"));
				xmlElement.addElement(new TextElement("on duplicate key update"));
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < columns.size(); i++) {
					IntrospectedColumn column = columns.get(i);
					stringBuilder.append(String.format("%s = #{%s,jdbcType=%s}", columnName(column),
							column.getJavaProperty(), column.getJdbcTypeName()));
					if (i == columns.size() - 1) {
						break;
					} else {
						stringBuilder.append(",");
						if (i > 0 && (i+1) % 3 == 0) {
							xmlElement.addElement(new TextElement(stringBuilder.toString()));
							stringBuilder = new StringBuilder();
						}
					}
				}
				if(stringBuilder.length() > 0){
					xmlElement.addElement(new TextElement(stringBuilder.toString()));
				}
				element.addElement(xmlElement);
			}
		}
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if(readonly){
			return false;
		}
		if(noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			//添加insert or replace 表达式
			for(Element e : element.getElements()){
				if(e instanceof TextElement){
					TextElement temp = (TextElement)e;
					if(temp.getContent().matches("\\s*(?i)insert\\s+.*")){
						try {
							java.lang.reflect.Field field = TextElement.class.getDeclaredField("content");
							field.setAccessible(true);
							field.set(temp, "${_sqlImprovement_ == 1 ? 'replace' : 'insert'} " + temp.getContent().replaceFirst("\\s*(?i)insert\\s+(.*)", "$1"));
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						} catch (SecurityException e1) {
							e1.printStackTrace();
						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
			List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
			if (columns.size() > 0) {
				XmlElement xmlElement = new XmlElement("if");
				xmlElement.addAttribute(new Attribute("test", "_sqlImprovement_ == 2"));
				xmlElement.addElement(new TextElement("on duplicate key update"));
				XmlElement trim = new XmlElement("trim");
				trim.addAttribute(new Attribute("suffixOverrides", ","));
				xmlElement.addElement(trim);
	
				for (int i = 0; i < columns.size(); i++) {
					IntrospectedColumn column = columns.get(i);
					XmlElement ifElement = new XmlElement("if");
					ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
					ifElement.addElement(new TextElement(String.format("%s = #{%s,jdbcType=%s},",
							columnName(column), column.getJavaProperty(), column.getJdbcTypeName())));
					trim.addElement(ifElement);
				}
				element.addElement(xmlElement);
			}
		}
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}

}
