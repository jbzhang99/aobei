package com.github.liyiorg.mbg.plugin.upsert;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * PostgreSQL insert and insertSelective improvement<br>
 * PostgreSQL since version 9.5 <br>
 * 
 * 3 ON CONFLICT DO UPDATE
 * 4 ON CONFLICT DO NOTHING
 * 
 * @author LiYi
 *
 */
public class PostgreSQLUpsertPlugin extends AbstractUpsertPlugin {

	@Override
	protected void buildUpsertMethodBody(Method method) {
		method.addBodyLine("this._sqlImprovement_ = 3;");
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if(readonly){
			return false;
		}
		if(noGeneratedKey && introspectedTable.hasPrimaryKeyColumns()){
			List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
			if (columns.size() > 0) {
				String conflict_cs = "";
				List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
				if(primaryKeyColumns != null && primaryKeyColumns.size() > 0){
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("(");
					for(int i = 0; i < primaryKeyColumns.size(); i++){
						IntrospectedColumn c = primaryKeyColumns.get(i);
						stringBuilder.append(columnName(c));
						if (i == primaryKeyColumns.size() - 1) {
							break;
						} else {
							stringBuilder.append(",");
						}
					}
					stringBuilder.append(") ");
					conflict_cs = stringBuilder.toString();
				}
				XmlElement xmlElementWhen1 = new XmlElement("when");
				xmlElementWhen1.addAttribute(new Attribute("test", "_sqlImprovement_ == 3"));
				xmlElementWhen1.addElement(new TextElement("on conflict " + conflict_cs + "do update"));
				xmlElementWhen1.addElement(new TextElement("set"));
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
							xmlElementWhen1.addElement(new TextElement(stringBuilder.toString()));
							stringBuilder = new StringBuilder();
						}
					}
				}
				if(stringBuilder.length() > 0){
					xmlElementWhen1.addElement(new TextElement(stringBuilder.toString()));
				}
				
				XmlElement xmlElementWhen2 = new XmlElement("when");
				xmlElementWhen2.addAttribute(new Attribute("test", "_sqlImprovement_ == 4"));
				xmlElementWhen2.addElement(new TextElement("on conflict " + conflict_cs + "do nothing"));
				
				XmlElement xmlElementChoose = new XmlElement("choose");
				xmlElementChoose.addElement(xmlElementWhen1);
				xmlElementChoose.addElement(xmlElementWhen2);
				
				element.addElement(xmlElementChoose);
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
				String conflict_cs = "";
				List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
				if(primaryKeyColumns != null && primaryKeyColumns.size() > 0){
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("(");
					for(int i = 0; i < primaryKeyColumns.size(); i++){
						IntrospectedColumn c = primaryKeyColumns.get(i);
						stringBuilder.append(columnName(c));
						if (i == primaryKeyColumns.size() - 1) {
							break;
						} else {
							stringBuilder.append(",");
						}
					}
					stringBuilder.append(") ");
					conflict_cs = stringBuilder.toString();
				}
				
				XmlElement xmlElementWhen1 = new XmlElement("when");
				xmlElementWhen1.addAttribute(new Attribute("test", "_sqlImprovement_ == 3"));
				xmlElementWhen1.addElement(new TextElement("on conflict " + conflict_cs + "do update"));
				XmlElement xmlElementSet = new XmlElement("set");
				xmlElementWhen1.addElement(xmlElementSet);
	
				for (int i = 0; i < columns.size(); i++) {
					IntrospectedColumn column = columns.get(i);
					XmlElement ifElement = new XmlElement("if");
					ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
					ifElement.addElement(new TextElement(String.format("%s = #{%s,jdbcType=%s},",
							columnName(column), column.getJavaProperty(), column.getJdbcTypeName())));
					xmlElementSet.addElement(ifElement);
				}
				
				XmlElement xmlElementWhen2 = new XmlElement("when");
				xmlElementWhen2.addAttribute(new Attribute("test", "_sqlImprovement_ == 4"));
				xmlElementWhen2.addElement(new TextElement("on conflict " + conflict_cs + "do nothing"));
				
				XmlElement xmlElementChoose = new XmlElement("choose");
				xmlElementChoose.addElement(xmlElementWhen1);
				xmlElementChoose.addElement(xmlElementWhen2);
				
				element.addElement(xmlElementChoose);
			}
		}
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}
	
}
