package com.github.liyiorg.mbg.plugin.upsert;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Oracle insert and insertSelective improvement<br>
 * <br>
 * merge into employee using dual on ( id=? )	<br>
 *  when matched then update set c1=? , c2=?	<br>
 * when not matched then insert (id,c1,c2) 		<br>
 *   values ( ?,?,? )
 * 
 * @author LiYi
 *
 */
public class OracleUpsertPlugin extends AbstractUpsertPlugin {

	@Override
	protected void buildUpsertMethodBody(Method method) {
		method.addBodyLine("this._sqlImprovement_ = 5;");
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if(readonly){
			return false;
		}
		if(noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
			if (columns.size() > 0) {
				// 获取备注
				List<Element> comments = new ArrayList<Element>();
				for (Element e : element.getElements()) {
					comments.add(e);
					if ("-->".equals(e.getFormattedContent(0))) {
						break;
					}
				}
				XmlElement xmlElementWhen1 = new XmlElement("when");
				xmlElementWhen1.addAttribute(new Attribute("test", "_sqlImprovement_ != 5"));
				for (int i = comments.size(); i < element.getElements().size(); i++) {
					Element e = element.getElements().get(i);
					xmlElementWhen1.addElement(e);
				}
				String ids = null;
				List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
				if(primaryKeyColumns != null && primaryKeyColumns.size() > 0){
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("(");
					for(int i = 0; i < primaryKeyColumns.size(); i++){
						IntrospectedColumn column = primaryKeyColumns.get(i);
						stringBuilder.append(String.format("%s = #{%s,jdbcType=%s}",columnName(column),column.getJavaProperty(),column.getJdbcTypeName()));
						if (i == primaryKeyColumns.size() - 1) {
							break;
						} else {
							stringBuilder.append(" and ");
						}
					}
					stringBuilder.append(")");
					ids = stringBuilder.toString();
				}
				XmlElement xmlElementWhen2 = new XmlElement("when");
				xmlElementWhen2.addAttribute(new Attribute("test", "_sqlImprovement_ == 5"));
				xmlElementWhen2.addElement(new TextElement("merge into " + tableName(introspectedTable) + " using dual"));
				xmlElementWhen2.addElement(new TextElement("on " + ids));
				xmlElementWhen2.addElement(new TextElement("when matched then update set"));
				
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
							xmlElementWhen2.addElement(new TextElement(stringBuilder.toString()));
							stringBuilder = new StringBuilder();
						}
					}
				}
				if(stringBuilder.length() > 0){
					xmlElementWhen2.addElement(new TextElement(stringBuilder.toString()));
				}
				
				xmlElementWhen2.addElement(new TextElement("when not matched then insert"));
				for (int i = comments.size(); i < element.getElements().size(); i++) {
					Element e = element.getElements().get(i);
					if(i == comments.size()){
						xmlElementWhen2.addElement(new TextElement(e.getFormattedContent(0).replaceFirst("(?i)insert\\s+((?i)into\\s+)?", "")));
					}else{
						xmlElementWhen2.addElement(e);
					}
				}
				XmlElement xmlElementChoose = new XmlElement("choose");
				xmlElementChoose.addElement(xmlElementWhen1);
				xmlElementChoose.addElement(xmlElementWhen2);
				
				try {
					java.lang.reflect.Field field = element.getClass().getDeclaredField("elements");
					field.setAccessible(true);
					// 清空内部element
					field.set(element, new ArrayList<Element>());
					// 设置备注
					for (Element e : comments) {
						element.addElement(e);
					}
					// 设置 chooseXMLElement
					element.addElement(xmlElementChoose);
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
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if(readonly){
			return false;
		}
		if(noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
			if (columns.size() > 0) {
				// 获取备注
				List<Element> comments = new ArrayList<Element>();
				for (Element e : element.getElements()) {
					comments.add(e);
					if ("-->".equals(e.getFormattedContent(0))) {
						break;
					}
				}
				XmlElement xmlElementWhen1 = new XmlElement("when");
				xmlElementWhen1.addAttribute(new Attribute("test", "_sqlImprovement_ != 5"));
				for (int i = comments.size(); i < element.getElements().size(); i++) {
					Element e = element.getElements().get(i);
					xmlElementWhen1.addElement(e);
				}
				String ids = null;
				List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
				if(primaryKeyColumns != null && primaryKeyColumns.size() > 0){
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("(");
					for(int i = 0; i < primaryKeyColumns.size(); i++){
						IntrospectedColumn column = primaryKeyColumns.get(i);
						stringBuilder.append(String.format("%s = #{%s,jdbcType=%s}",columnName(column),column.getJavaProperty(),column.getJdbcTypeName()));
						if (i == primaryKeyColumns.size() - 1) {
							break;
						} else {
							stringBuilder.append(" and ");
						}
					}
					stringBuilder.append(")");
					ids = stringBuilder.toString();
				}
				XmlElement xmlElementWhen2 = new XmlElement("when");
				xmlElementWhen2.addAttribute(new Attribute("test", "_sqlImprovement_ == 5"));
				xmlElementWhen2.addElement(new TextElement("merge into " + tableName(introspectedTable) + " using dual"));
				xmlElementWhen2.addElement(new TextElement("on " + ids));
				xmlElementWhen2.addElement(new TextElement("when matched then update"));
				
				XmlElement xmlElementSet = new XmlElement("set");
				xmlElementWhen2.addElement(xmlElementSet);
				for (int i = 0; i < columns.size(); i++) {
					IntrospectedColumn column = columns.get(i);
					XmlElement ifElement = new XmlElement("if");
					ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
					ifElement.addElement(new TextElement(String.format("%s = #{%s,jdbcType=%s},",
							columnName(column), column.getJavaProperty(), column.getJdbcTypeName())));
					xmlElementSet.addElement(ifElement);
				}
				
				xmlElementWhen2.addElement(new TextElement("when not matched then insert"));
				for (int i = comments.size(); i < element.getElements().size(); i++) {
					Element e = element.getElements().get(i);
					if(i == comments.size()){
						xmlElementWhen2.addElement(new TextElement(e.getFormattedContent(0).replaceFirst("(?i)insert\\s+((?i)into\\s+)?", "")));
					}else{
						xmlElementWhen2.addElement(e);
					}
				}
				XmlElement xmlElementChoose = new XmlElement("choose");
				xmlElementChoose.addElement(xmlElementWhen1);
				xmlElementChoose.addElement(xmlElementWhen2);
				
				try {
					java.lang.reflect.Field field = element.getClass().getDeclaredField("elements");
					field.setAccessible(true);
					// 清空内部element
					field.set(element, new ArrayList<Element>());
					// 设置备注
					for (Element e : comments) {
						element.addElement(e);
					}
					// 设置 chooseXMLElement
					element.addElement(xmlElementChoose);
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
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}

}
