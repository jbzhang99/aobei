package com.github.liyiorg.mbg.plugin.ci;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 
 * choose improvement type<br>
 * 
 * set table>property criterionImprovement=[1,2]
 * 
 * @author LiYi
 *
 */
public class CriterionImprovementPlugin extends PluginAdapter {

	private final static String CRITERIONIMPROVEMENT_PROPERTY_NAME = "ci";
	
	private static String DEFAULT_CRITERION_IMPROVEMENT;

	private String criterionImprovement = null;

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		DEFAULT_CRITERION_IMPROVEMENT = properties.getProperty(CRITERIONIMPROVEMENT_PROPERTY_NAME, "1");
		String criterionImprovement_pro = introspectedTable.getTableConfiguration().getProperty(CRITERIONIMPROVEMENT_PROPERTY_NAME);
		if (StringUtility.stringHasValue(criterionImprovement_pro)) {
			criterionImprovement = criterionImprovement_pro;
		} else {
			criterionImprovement = DEFAULT_CRITERION_IMPROVEMENT;
		}
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		switch (criterionImprovement) {
		case "2":
			CriterionImprovement2Plugin.exec(topLevelClass, introspectedTable, context);
			break;
		default:
			CriterionImprovement1Plugin.exec(topLevelClass, introspectedTable, context);
			break;
		}
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean validate(List<String> arg0) {
		return true;
	}

}
